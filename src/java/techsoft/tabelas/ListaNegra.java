
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

public class ListaNegra {

    private int id;
    private String descricao;
    private static final Logger log = Logger.getLogger("techsoft.tabelas.ListaNegra");

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    public int getId(){
        return id;
    }

    public static List<ListaNegra> listar(){
        ArrayList<ListaNegra> l = new ArrayList<ListaNegra>();
        String sql = "SELECT * FROM TB_LISTA_NEGRA ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                ListaNegra d = new ListaNegra();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
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

    public static ListaNegra getInstance(int id){
        ListaNegra d = null;
        String sql = "SELECT * FROM TB_LISTA_NEGRA WHERE CD_LISTA_NEGRA = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new ListaNegra();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
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
            String sql = "DELETE FROM TB_LISTA_NEGRA WHERE CD_LISTA_NEGRA = ?";
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
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_LISTA_NEGRA WHERE NO_LISTA_NEGRA = ?");
            p.setString(1, descricao);
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                String err = "ListaNegra já cadastrado " + descricao;
                log.warning(err);
                throw new InserirException(err);
            }else{
                String sql = "INSERT INTO TB_LISTA_NEGRA (NO_LISTA_NEGRA) VALUES (?)";
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
            String sql = "UPDATE TB_LISTA_NEGRA SET NO_LISTA_NEGRA = ? WHERE CD_LISTA_NEGRA = ?";
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

}


