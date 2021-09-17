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

public class TaxaAdministrativa {
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.TaxaAdministrativa");
 
    private Date dtInicial;
    private Date dtFinal;
    private float valor;
    private int taxa;
    private String deTaxa;
    private int categoria;

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
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public int getTaxa() {
        return taxa;
    }
    public void setTaxa(int taxa) {
        this.taxa = taxa;
    }
    public int getCategoria() {
        return categoria;
    }
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    public String getDeTaxa() {
        return deTaxa;
    }
    public void setDeTaxa(String deTaxa) {
        this.deTaxa = deTaxa;
    }
    
    public static List<TaxaAdministrativa> consultar(int idCategoria, String tipo, String dtRef ){
        ArrayList<TaxaAdministrativa> l = new ArrayList<TaxaAdministrativa>();
        String sql = "SELECT T1.DT_VALID_INIC_TX_ADM, T1.DT_VALID_FIM_TX_ADM, T1.VAL_TX_ADM, T1.CD_TX_ADM, T2.DESCR_TX_ADMINISTRATIVA FROM TB_VAL_TX_ADMINISTRATIVA T1, TB_TAXA_ADMINISTRATIVA T2 WHERE T1.CD_TX_ADM = T2.CD_TX_ADMINISTRATIVA AND T1.CD_CATEGORIA = " + idCategoria;

        if (!tipo.equals("TD")){
            if (tipo.equals("AT")){
                sql = sql + " AND DT_VALID_INIC_TX_ADM <= '" + dtRef.substring(3, 5) + '/' + dtRef.substring(0, 2) + '/' + dtRef.substring(6) + "'";
            }else{
            }
                sql = sql + " AND (DT_VALID_FIM_TX_ADM >= '" + dtRef.substring(3, 5) + '/' + dtRef.substring(0, 2) + '/' + dtRef.substring(6) + "' OR DT_VALID_FIM_TX_ADM IS NULL)";
        }
        
        sql = sql + " ORDER BY DESCR_TX_ADMINISTRATIVA     ";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                TaxaAdministrativa d = new TaxaAdministrativa();
                d.dtInicial = rs.getTimestamp("DT_VALID_INIC_TX_ADM");
                d.dtFinal = rs.getTimestamp("DT_VALID_FIM_TX_ADM");
                d.valor = rs.getFloat("VAL_TX_ADM");
                d.taxa = rs.getInt("CD_TX_ADM");
                d.deTaxa = rs.getString("DESCR_TX_ADMINISTRATIVA");
                
                
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
            
            String sql = "UPDATE TB_VAL_TX_ADMINISTRATIVA SET DT_VALID_FIM_TX_ADM = DATEADD(S, -1, ?) WHERE CD_CATEGORIA = ? AND CD_TX_ADM = ? AND DT_VALID_INIC_TX_ADM < ? AND DT_VALID_FIM_TX_ADM IS NULL" ;

            PreparedStatement p = cn.prepareStatement(sql);

            p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            p.setInt(2, par.getSetParametro(categoria));
            p.setInt(3, par.getSetParametro(taxa));
            p.setTimestamp(4, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
        
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

        try {
            cn = Pool.getInstance().getConnection();
            par.limpaParametro();
            
            String sql = "INSERT INTO TB_VAL_TX_ADMINISTRATIVA (CD_CATEGORIA, CD_TX_ADM, DT_VALID_INIC_TX_ADM, DT_VALID_FIM_TX_ADM, VAL_TX_ADM) VALUES (?,?,?,?,?)";
            
            PreparedStatement p = cn.prepareStatement(sql);

            p.setInt(1, par.getSetParametro(categoria));
            p.setInt(2, par.getSetParametro(taxa));
            p.setTimestamp(3, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            if(dtFinal==null){
                p.setNull(4, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setTimestamp(4, new java.sql.Timestamp(par.getSetParametro(dtFinal).getTime()));
            }
            p.setFloat(5, par.getSetParametro(valor));
            
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
            
            String sql = "UPDATE TB_VAL_TX_ADMINISTRATIVA SET DT_VALID_FIM_TX_ADM = ? WHERE CD_TX_ADM = ? AND DT_VALID_INIC_TX_ADM = ? AND CD_CATEGORIA = ?" ;
            
            PreparedStatement p = cn.prepareStatement(sql);

            if(dtFinal==null){
                p.setNull(1, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(dtFinal).getTime()));
            }
            p.setInt(2, par.getSetParametro(taxa));
            p.setTimestamp(3, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            p.setInt(4, par.getSetParametro(categoria));
        
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
