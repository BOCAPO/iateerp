
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

public class Cor {

    private int id;
    private String descricao;
    private static final Logger log = Logger.getLogger("techsoft.tabelas.Cor");

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    public int getId(){
        return id;
    }

    public static List<Cor> listar(){
        ArrayList<Cor> l = new ArrayList<Cor>();
        String sql = "SELECT * FROM TB_COR ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Cor d = new Cor();
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

    public static Cor getInstance(int id){
        Cor d = null;
        String sql = "SELECT * FROM TB_COR WHERE CD_COR = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new Cor();
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
            String sql = "DELETE FROM TB_COR WHERE CD_COR = ?";
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
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_COR WHERE DE_COR = ?");
            p.setString(1, descricao);
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                String err = "Cor já cadastrado " + descricao;
                log.warning(err);
                throw new InserirException(err);
            }else{
                String sql = "INSERT INTO TB_COR (DE_COR) VALUES (?)";
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
            String sql = "UPDATE TB_COR SET DE_COR = ? WHERE CD_COR = ?";
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
