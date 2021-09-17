
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
import java.util.Date;


public class Feriado {

    private Date data;
    private static final Logger log = Logger.getLogger("techsoft.tabelas.Feriado");

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public static List<Feriado> listar(){
        ArrayList<Feriado> l = new ArrayList<Feriado>();
        String sql = "SELECT * FROM TB_FERIADO ORDER BY 1 DESC";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Feriado d = new Feriado();
                d.data = rs.getDate(1);
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


    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_FERIADO WHERE DT_FERIADO = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setDate(1, new java.sql.Date(data.getTime()));
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(data));
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

    public static Feriado getInstance(Date data){
        Feriado d = null;
        String sql = "SELECT * FROM TB_FERIADO WHERE DT_FERIADO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setDate(1, new java.sql.Date(data.getTime()));
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new Feriado();
                d.data = rs.getDate(1);
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
    
    public void inserir(Auditoria audit)throws InserirException{

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_FERIADO WHERE DT_FERIADO = ?");
            p.setDate(1, new java.sql.Date(data.getTime()));

            ResultSet rs = p.executeQuery();

            if(rs.next()){
                String err = "Feriado já cadastrado ";
                log.warning(err);
                throw new InserirException(err);
            }else{
                String sql = "INSERT INTO TB_FERIADO (DT_FERIADO) VALUES (?)";
                p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                p.setDate(1, new java.sql.Date(data.getTime()));
                p.executeUpdate();
                rs = p.getGeneratedKeys();

                if(rs.next()){
                    cn.commit();
                    audit.registrarMudanca(sql, String.valueOf(data));
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


}
