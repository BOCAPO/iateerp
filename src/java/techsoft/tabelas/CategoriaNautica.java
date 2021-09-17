
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

public class CategoriaNautica {

    private String id;
    private String descricao;
    private static final Logger log = Logger.getLogger("techsoft.tabelas.CategoriaNautica");

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    public String getId(){
        return id;
    }
    
    public void setId(String id) {
        this.id = id.trim();
    }
    public static List<CategoriaNautica> listar(){
        ArrayList<CategoriaNautica> l = new ArrayList<CategoriaNautica>();
        String sql = "SELECT * FROM TB_CATEGORIA_NAUTICA ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                CategoriaNautica d = new CategoriaNautica();
                d.id = rs.getString(1);
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

    public static CategoriaNautica getInstance(String id){
        CategoriaNautica d = null;
        String sql = "SELECT * FROM TB_CATEGORIA_NAUTICA WHERE CD_CATEGORIA_NAUTICA = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new CategoriaNautica();
                d.id = rs.getString(1);
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
            String sql = "DELETE FROM TB_CATEGORIA_NAUTICA WHERE CD_CATEGORIA_NAUTICA = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, id);
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

        if(id == null || descricao == null) return;

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_CATEGORIA_NAUTICA WHERE DESCR_CATEGORIA_NAUTICA = ? OR CD_CATEGORIA_NAUTICA = ?");
            p.setString(1, descricao);
            p.setString(2, id);
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                String err = "CategoriaNautica já cadastrado " + descricao;
                log.warning(err);
                throw new InserirException(err);
            }else{
                String sql = "INSERT INTO TB_CATEGORIA_NAUTICA (CD_CATEGORIA_NAUTICA, DESCR_CATEGORIA_NAUTICA) VALUES (?, ?)";
                p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                p.setString(1, id);
                p.setString(2, descricao);
                p.executeUpdate();
                rs = p.getGeneratedKeys();

                if(rs.next()){
                    id = rs.getString(1);
                    cn.commit();
                    audit.registrarMudanca(sql, String.valueOf(id), descricao);
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

        if(id == null || descricao == null) return;

        Connection cn = null;

        try {
            String sql = "UPDATE TB_CATEGORIA_NAUTICA SET DESCR_CATEGORIA_NAUTICA = ? WHERE CD_CATEGORIA_NAUTICA = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricao);
            p.setString(2, id);
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

