
package techsoft.seguranca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;


public class Grupo {

    private int id;
    private String descricao;
    private List<Permissao> permissoes = new ArrayList<Permissao>();
    private List<Permissao> permissoesDisponiveis = new ArrayList<Permissao>();
    private static final Logger log = Logger.getLogger("techsoft.seguranca.Grupo");

    public Grupo(String descricao){
        this.descricao = descricao.trim();
    }

    Grupo(int id, String descricao){
        this.id = id;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    public int getId(){
        return id;
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }
    
    public List<Permissao> getPermissoesDisponiveis() {
		return permissoesDisponiveis;
	}

    public static List<Grupo> listar(){
        ArrayList<Grupo> l = new ArrayList<Grupo>();
        String sql = "SELECT * FROM TB_GRUPO ORDER BY 2";
        Connection cn = null;

        try {    
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                l.add(new Grupo(rs.getInt(1), rs.getString(2)));
            }
            cn.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return l;
    }

    public static Grupo getInstance(int id){
        Grupo g = null;
        String sql = "SELECT * FROM TB_GRUPO WHERE CD_GRUPO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                g = new Grupo(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return g;
    }

    /**
     * Carrega as permissoes que o grupo tem e as permissos disponiveis para
     * serem acrescentadas para esse grupo.
     */
    public void carregarPermissoes(){
        permissoes.clear();
        permissoesDisponiveis.clear();
        
        String sql = "SELECT AG.CD_APLICACAO, CA.DESCR_APLICACAO "
            + "FROM TB_APLICACAO_POR_GRUPO AG, TB_CADASTRO_APLICACAO CA "
            + "WHERE AG.CD_APLICACAO = CA.CD_APLICACAO AND AG.CD_GRUPO = ?";

        String sql2 = "SELECT TB_CADASTRO_APLICACAO.CD_APLICACAO, TB_CADASTRO_APLICACAO.DESCR_APLICACAO "
        	+ "FROM TB_CADASTRO_APLICACAO WHERE TB_CADASTRO_APLICACAO.CD_APLICACAO "
        	+ "NOT IN (SELECT TB_APLICACAO_POR_GRUPO.CD_APLICACAO FROM TB_APLICACAO_POR_GRUPO "
        	+ "WHERE TB_APLICACAO_POR_GRUPO.CD_GRUPO = ?)";
        
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                permissoes.add(new Permissao(rs.getInt(1), rs.getString(2)));
            }
            
            p = cn.prepareStatement(sql2);
            p.setInt(1, id);
            rs = p.executeQuery();
            while (rs.next()) {
                permissoesDisponiveis.add(new Permissao(rs.getInt(1), rs.getString(2)));
            }

        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }
    
    public void excluir(Auditoria audit){
        permissoes.clear();
        permissoesDisponiveis.clear();
        
        Connection cn = null;

        try {
            String sql = "{call SP_EXCLUIR_GRUPO (?)}";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareCall("{call SP_DELETA_APP_POR_GRUPO (?)}");
            p.setInt(1, id);
            p.executeUpdate();
            p = cn.prepareCall("{call SP_EXCLUIR_USU_P_GRP_PARM_GRP (?)}");
            p.setInt(1, id);
            p.executeUpdate();
            p = cn.prepareCall(sql);
            p.setInt(1, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(id));
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    public void inserir(Auditoria audit){

        if(id != 0 || descricao == null) return;

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_GRUPO WHERE DESCR_GRUPO = ?");
            p.setString(1, descricao);
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                log.warning("Grupo ja cadastrado " + descricao);
                return;
            }else{
                String sql = "INSERT INTO TB_GRUPO (DESCR_GRUPO) VALUES (?)";
                p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                p.setString(1, descricao);
                p.executeUpdate();
                rs = p.getGeneratedKeys();

                if(rs.next()){
                    id = rs.getInt(1);
                    cn.commit();
                    audit.registrarMudanca(sql, descricao);
                }

            }
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    public void alterar(Auditoria audit){

        if(id == 0 || descricao == null) return;

        Connection cn = null;

        try {
            String sql = "UPDATE TB_GRUPO SET DESCR_GRUPO = ? WHERE CD_GRUPO = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricao);
            p.setInt(2, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, descricao, String.valueOf(id));
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    public void excluirPermissao(int idPermissao, Auditoria audit){
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_APLICACAO_POR_GRUPO WHERE CD_APLICACAO = ? AND CD_GRUPO = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, idPermissao);
            p.setInt(2, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(idPermissao), String.valueOf(id));
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    public void adicionarPermissao(int idPermissao, Auditoria audit){
        Connection cn = null;

        try {
            String sql = "INSERT INTO TB_APLICACAO_POR_GRUPO (CD_APLICACAO, CD_GRUPO) VALUES (?, ?)";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);

            p.setInt(1, idPermissao);
            p.setInt(2, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(idPermissao), String.valueOf(id));
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    
}
