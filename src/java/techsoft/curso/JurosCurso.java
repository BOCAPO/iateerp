package techsoft.curso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;
import techsoft.util.Datas;

public class JurosCurso {
    
    private static final Logger log = Logger.getLogger("techsoft.curso.JurosCurso");
 
    private Date dtInicial;
    private Date dtFinal;
    private float juros;
    private int curso;

    public Date getDtInicial() {
        return dtInicial;
    }
    public void setDtInicial(Date dtInicial) {
        this.dtInicial = dtInicial;
    }
    public Date getDtFinal() {
        return dtFinal;
    }
    public void setDtFinal(Date dtFinal) {
        this.dtFinal = dtFinal;
    }
    public float getJuros() {
        return juros;
    }
    public void setJuros(float juros) {
        this.juros = juros;
    }
    public int getCurso() {
        return curso;
    }
    public void setCurso(int curso) {
        this.curso = curso;
    }
    
    public static List<JurosCurso> consultar(int idCurso, String tipo, String dtRef ){
        ArrayList<JurosCurso> l = new ArrayList<JurosCurso>();
        String sql = "SELECT DT_VALID_INIC_JUROS, DT_VALID_FIM_JUROS, PERC_JUROS FROM TB_JUROS_TX_CURSO WHERE CD_CURSO = " + idCurso;

        if (!tipo.equals("TD")){
            if (tipo.equals("AT")){
                sql = sql + " AND DT_VALID_INIC_JUROS <= '" + dtRef.substring(3, 5) + '/' + dtRef.substring(0, 2) + '/' + dtRef.substring(6) + "'";
            }else{
            }
                sql = sql + " AND (DT_VALID_FIM_JUROS >= '" + dtRef.substring(3, 5) + '/' + dtRef.substring(0, 2) + '/' + dtRef.substring(6) + "' OR DT_VALID_FIM_JUROS IS NULL)";
        }
        
        sql = sql + " ORDER BY DT_VALID_INIC_JUROS DESC     ";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                JurosCurso d = new JurosCurso();
                d.dtInicial = rs.getTimestamp("DT_VALID_INIC_JUROS");
                d.dtFinal = rs.getTimestamp("DT_VALID_FIM_JUROS");
                d.juros = rs.getFloat("PERC_JUROS");
                
                
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
    

    public void inserir(Auditoria audit)throws InserirException{

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            String sql = "INSERT INTO TB_JUROS_TX_CURSO (CD_CURSO, DT_VALID_INIC_JUROS, DT_VALID_FIM_JUROS, PERC_JUROS) VALUES (?,?,?,?)";
            
            PreparedStatement p = cn.prepareStatement(sql);

            p.setInt(1, par.getSetParametro(curso));
            p.setTimestamp(2, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            if(dtFinal==null){
                p.setNull(3, java.sql.Types.DATE);
            }else{
                p.setTimestamp(3, new java.sql.Timestamp(par.getSetParametro(dtFinal).getTime()));
            }
            p.setFloat(4, par.getSetParametro(juros));
            
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());

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


    public void alterar(Auditoria audit)throws InserirException{

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            String sql = "UPDATE TB_JUROS_TX_CURSO SET DT_VALID_FIM_JUROS = ? WHERE CD_CURSO = ? AND DT_VALID_INIC_JUROS= ?";
            
            PreparedStatement p = cn.prepareStatement(sql);

            if(dtFinal==null){
                p.setNull(1, java.sql.Types.DATE);
            }else{
                p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(dtFinal).getTime()));
            }
            p.setInt(2, par.getSetParametro(curso));
            p.setTimestamp(3, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
        
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());

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
