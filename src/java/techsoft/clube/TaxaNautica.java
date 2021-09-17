package techsoft.clube;

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

public class TaxaNautica {
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.TaxaNautica");
 
    private Date dtInicial;
    private Date dtFinal;
    private float valor;
    private String tipoVaga;
    private String deTipoVaga;

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
    public String getTipoVaga() {
        return tipoVaga;
    }
    public void setTipoVaga(String tipoVaga) {
        this.tipoVaga = tipoVaga;
    }
    public String getDeTipoVaga() {
        return deTipoVaga;
    }
    public void setDeTipoVaga(String deTipoVaga) {
        this.deTipoVaga = deTipoVaga;
    }
    
    public static List<TaxaNautica> consultar(String tipo, String dtRef ){
        ArrayList<TaxaNautica> l = new ArrayList<TaxaNautica>();
        String sql = "SELECT T1.DT_VALID_INIC_TX_NAUTICA, T1.DT_VALID_FIM_TX_NAUTICA, T1.VAL_TX_NAUTICA, T1.CD_TIPO_VAGA, T2.DE_TIPO_VAGA FROM TB_VAL_TX_NAUTICA T1, TB_TIPO_VAGA_BARCO T2 WHERE T1.CD_TIPO_VAGA = T2.CO_TIPO_VAGA ";

        if (!tipo.equals("TD")){
            if (tipo.equals("AT")){
                sql = sql + " AND DT_VALID_INIC_TX_NAUTICA <= '" + dtRef.substring(3, 5) + '/' + dtRef.substring(0, 2) + '/' + dtRef.substring(6) + "'";
            }else{
            }
                sql = sql + " AND (DT_VALID_FIM_TX_NAUTICA >= '" + dtRef.substring(3, 5) + '/' + dtRef.substring(0, 2) + '/' + dtRef.substring(6) + "' OR DT_VALID_FIM_TX_NAUTICA IS NULL)";
        }
        
        sql = sql + " ORDER BY DE_TIPO_VAGA ";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                TaxaNautica d = new TaxaNautica();
                d.dtInicial = rs.getTimestamp("DT_VALID_INIC_TX_NAUTICA");
                d.dtFinal = rs.getTimestamp("DT_VALID_FIM_TX_NAUTICA");
                d.valor = rs.getFloat("VAL_TX_NAUTICA");
                d.tipoVaga = rs.getString("CD_TIPO_VAGA");
                d.deTipoVaga = rs.getString("DE_TIPO_VAGA");
                
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
            
            String sql = "UPDATE TB_VAL_TX_NAUTICA SET DT_VALID_FIM_TX_NAUTICA = DATEADD(S, -1, ?) WHERE CD_TIPO_VAGA = ? AND DT_VALID_INIC_TX_NAUTICA < ? AND DT_VALID_FIM_TX_NAUTICA IS NULL" ;

            PreparedStatement p = cn.prepareStatement(sql);

            p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            p.setString(2, par.getSetParametro(tipoVaga));
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

        try {
            cn = Pool.getInstance().getConnection();
            par.limpaParametro();
            
            String sql = "INSERT INTO TB_VAL_TX_NAUTICA (CD_TIPO_VAGA, DT_VALID_INIC_TX_NAUTICA, DT_VALID_FIM_TX_NAUTICA, VAL_TX_NAUTICA) VALUES (?,?,?,?)";
            
            PreparedStatement p = cn.prepareStatement(sql);

            p.setString(1, par.getSetParametro(tipoVaga));
            p.setTimestamp(2, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            if(dtFinal==null){
                p.setNull(3, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setTimestamp(3, new java.sql.Timestamp(par.getSetParametro(dtFinal).getTime()));
            }
            p.setFloat(4, par.getSetParametro(valor));            
            
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
            
            String sql = "UPDATE TB_VAL_TX_NAUTICA SET DT_VALID_FIM_TX_NAUTICA = ? WHERE CD_TIPO_VAGA = ? AND DT_VALID_INIC_TX_NAUTICA = ?" ;
            
            PreparedStatement p = cn.prepareStatement(sql);

            if(dtFinal==null){
                p.setNull(1, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(dtFinal).getTime()));
            }
            p.setString(2, par.getSetParametro(tipoVaga));
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
