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

public class TaxaBarco {
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.TaxaBarco");
 
    private Date dtInicial;
    private Date dtFinal;
    private float valor;
    private int pesInicial;
    private int pesFinal;
    private String tipoVaga;

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
    public int getPesInicial() {
        return pesInicial;
    }
    public void setPesInicial(int pesInicial) {
        this.pesInicial = pesInicial;
    }
    public int getPesFinal() {
        return pesFinal;
    }
    public void setPesFinal(int pesFinal) {
        this.pesFinal = pesFinal;
    }
    public String getTipoVaga() {
        return tipoVaga;
    }
    public void setTipoVaga(String tipoVaga) {
        this.tipoVaga = tipoVaga;
    }
    
    public static List<TaxaBarco> consultar(String idTipoVaga, String tipo, String dtRef ){
        ArrayList<TaxaBarco> l = new ArrayList<TaxaBarco>();
        String sql = "SELECT T1.DT_INIC_VALID_TX_BARCO, T1.DT_FIM_VALID_TX_BARCO, T1.VAL_TX_BARCO, NR_PES_INIC_TX_BARCO, NR_PES_FIM_TX_BARCO FROM TB_TAXA_BARCO T1 WHERE T1.TP_VAGA_TX_BARCO = '" + idTipoVaga + "'";

        if (!tipo.equals("TD")){
            if (tipo.equals("AT")){
                sql = sql + " AND DT_INIC_VALID_TX_BARCO <= '" + dtRef.substring(3, 5) + '/' + dtRef.substring(0, 2) + '/' + dtRef.substring(6) + "'";
            }else{
            }
                sql = sql + " AND (DT_FIM_VALID_TX_BARCO >= '" + dtRef.substring(3, 5) + '/' + dtRef.substring(0, 2) + '/' + dtRef.substring(6) + "' OR DT_FIM_VALID_TX_BARCO IS NULL)";
        }
        
        sql = sql + " ORDER BY NR_PES_INIC_TX_BARCO ";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                TaxaBarco d = new TaxaBarco();
                d.dtInicial = rs.getTimestamp("DT_INIC_VALID_TX_BARCO");
                d.dtFinal = rs.getTimestamp("DT_FIM_VALID_TX_BARCO");
                d.valor = rs.getFloat("VAL_TX_BARCO");
                d.pesInicial = rs.getInt("NR_PES_INIC_TX_BARCO");
                d.pesFinal = rs.getInt("NR_PES_FIM_TX_BARCO");
                
                
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
            
            String sql = "UPDATE TB_TAXA_BARCO SET DT_FIM_VALID_TX_BARCO = DATEADD(S, -1, ?) WHERE TP_VAGA_TX_BARCO = ? AND NR_PES_INIC_TX_BARCO = ? AND DT_INIC_VALID_TX_BARCO < ? AND DT_FIM_VALID_TX_BARCO IS NULL" ;

            PreparedStatement p = cn.prepareStatement(sql);

            p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            p.setString(2,par.getSetParametro(tipoVaga));
            p.setInt(3,par.getSetParametro(pesInicial));
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
            par.limpaParametro();
            cn = Pool.getInstance().getConnection();
            
            String sql = "INSERT INTO TB_TAXA_BARCO (TP_VAGA_TX_BARCO, NR_PES_INIC_TX_BARCO, NR_PES_FIM_TX_BARCO, DT_INIC_VALID_TX_BARCO, DT_FIM_VALID_TX_BARCO, VAL_TX_BARCO) VALUES (?,?,?,?,?,?)";
            
            PreparedStatement p = cn.prepareStatement(sql);

            p.setString(1, par.getSetParametro(tipoVaga));
            p.setInt(2, par.getSetParametro(pesInicial));
            p.setInt(3, par.getSetParametro(pesFinal));
            p.setTimestamp(4, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            if(dtFinal==null){
                p.setNull(5, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setTimestamp(5, new java.sql.Timestamp(par.getSetParametro(dtFinal).getTime()));
            }
            p.setFloat(6, valor);

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
            
            String sql = "UPDATE TB_TAXA_BARCO SET DT_FIM_VALID_TX_BARCO = ? WHERE TP_VAGA_TX_BARCO = ? AND DT_INIC_VALID_TX_BARCO = ? AND NR_PES_INIC_TX_BARCO = ?" ;
            
            PreparedStatement p = cn.prepareStatement(sql);

            if(dtFinal==null){
                p.setNull(1, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(dtFinal).getTime()));
            }
            p.setString(2, par.getSetParametro(tipoVaga));
            p.setTimestamp(3, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            p.setInt(4, par.getSetParametro(pesInicial));
        
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
