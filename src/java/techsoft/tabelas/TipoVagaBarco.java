
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

public class TipoVagaBarco {

    private String id;
    private String descricao;
    private static final Logger log = Logger.getLogger("techsoft.tabelas.TipoVagaBarco");

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id.trim();
    }

    public static List<TipoVagaBarco> listar(){
        ArrayList<TipoVagaBarco> l = new ArrayList<TipoVagaBarco>();
        String sql = "SELECT * FROM TB_TIPO_VAGA_BARCO ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                TipoVagaBarco d = new TipoVagaBarco();
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

    public static TipoVagaBarco getInstance(String id){
        TipoVagaBarco d = null;
        String sql = "SELECT * FROM TB_TIPO_VAGA_BARCO WHERE CO_TIPO_VAGA = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new TipoVagaBarco();
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
            String sql = "DELETE FROM TB_TIPO_VAGA_BARCO WHERE CO_TIPO_VAGA = ?";
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
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_TIPO_VAGA_BARCO WHERE DE_TIPO_VAGA = ? OR CO_TIPO_VAGA = ?");
            p.setString(1, descricao);
            p.setString(2, id);
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                String err = "TipoVagaBarco já cadastrado ";
                log.warning(err);
                throw new InserirException(err);
            }else{
                String sql = "INSERT INTO TB_TIPO_VAGA_BARCO (CO_TIPO_VAGA, DE_TIPO_VAGA) VALUES (?, ?)";
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
            String sql = "UPDATE TB_TIPO_VAGA_BARCO SET DE_TIPO_VAGA = ? WHERE CO_TIPO_VAGA = ?";
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
