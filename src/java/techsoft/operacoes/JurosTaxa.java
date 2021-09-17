package techsoft.operacoes;

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

public class JurosTaxa {
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.JurosTaxa");
 
    private Date dtInicial;
    private Date dtFinal;
    private float juros;
    private int taxa;

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
    public int getTaxa() {
        return taxa;
    }
    public void setTaxa(int taxa) {
        this.taxa = taxa;
    }
    
    public static List<JurosTaxa> consultar(int idTaxa, String tipo, String dtRef ){
        ArrayList<JurosTaxa> l = new ArrayList<JurosTaxa>();
        String sql = "SELECT DT_VALID_INIC_JUROS, DT_VALID_FIM_JUROS, PERC_JUROS FROM TB_JUROS_TX_ADM WHERE CD_TX_ADM = " + idTaxa;

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
                JurosTaxa d = new JurosTaxa();
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
        ParametroAuditoria par = new ParametroAuditoria();
        try {
            cn = Pool.getInstance().getConnection();
            
            String sql = "INSERT INTO TB_JUROS_TX_ADM (CD_TX_ADM, DT_VALID_INIC_JUROS, DT_VALID_FIM_JUROS, PERC_JUROS) VALUES (?,?,?,?)";
            
            PreparedStatement p = cn.prepareStatement(sql);

            p.setInt(1, par.getSetParametro(taxa));
            p.setTimestamp(2, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            if(dtFinal==null){
                p.setNull(3, java.sql.Types.DATE);
                par.getSetNull();
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
        ParametroAuditoria par = new ParametroAuditoria();
        try {
            cn = Pool.getInstance().getConnection();
            
            String sql = "UPDATE TB_JUROS_TX_ADM SET DT_VALID_FIM_JUROS = ? WHERE CD_TX_ADM = ? AND DT_VALID_INIC_JUROS= ?";
            
            PreparedStatement p = cn.prepareStatement(sql);

            if(dtFinal==null){
                p.setNull(1, java.sql.Types.DATE);
            }else{
                p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(dtFinal).getTime()));
            }
            p.setInt(2, par.getSetParametro(taxa));
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
