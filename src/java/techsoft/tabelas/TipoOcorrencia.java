
package techsoft.tabelas;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;

public class TipoOcorrencia {

    private int id;
    private String descricao;
    private String tipo;
    private static final Logger log = Logger.getLogger("techsoft.tabelas.TipoOcorrencia");

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    public int getId(){
        return id;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo.trim();
    }
    public static List<TipoOcorrencia> listar(){
        ArrayList<TipoOcorrencia> l = new ArrayList<TipoOcorrencia>();
        String sql = "SELECT * FROM TB_TIPO_OCORRENCIA_PESSOA ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                TipoOcorrencia d = new TipoOcorrencia();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.tipo =  rs.getString(3);
                l.add(d);
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

    public static TipoOcorrencia getInstance(int id){
        TipoOcorrencia d = null;
        String sql = "SELECT * FROM TB_TIPO_OCORRENCIA_PESSOA WHERE CD_SEQ_TP_OCORRENCIA = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new TipoOcorrencia();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.tipo = rs.getString(3);
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

        return d;
    }

    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_TIPO_OCORRENCIA_PESSOA WHERE CD_SEQ_TP_OCORRENCIA = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
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

    public void inserir(Auditoria audit)throws InserirException{

        if(id != 0 || descricao == null) return;

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_TIPO_OCORRENCIA_PESSOA WHERE DESCR_OCORRENCIA_PESSOA = ?");
            p.setString(1, descricao);
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                String err = "TipoOcorrencia já cadastrado " + descricao;
                log.warning(err);
                throw new InserirException(err);
            }else{
                String sql = "INSERT INTO TB_TIPO_OCORRENCIA_PESSOA (DESCR_OCORRENCIA_PESSOA, CD_TP_OCORRENCIA) VALUES (?, ?)";
                p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                p.setString(1, descricao);
                p.setString(2, tipo);
                p.executeUpdate();
                rs = p.getGeneratedKeys();

                if(rs.next()){
                    id = rs.getInt(1);
                    cn.commit();
                    audit.registrarMudanca(sql, descricao, tipo);
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
            String sql = "UPDATE TB_TIPO_OCORRENCIA_PESSOA SET DESCR_OCORRENCIA_PESSOA = ?, CD_TP_OCORRENCIA = ? WHERE CD_SEQ_TP_OCORRENCIA = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricao);
            p.setString(2, tipo);
            p.setInt(3, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, descricao, tipo, String.valueOf(id));
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
