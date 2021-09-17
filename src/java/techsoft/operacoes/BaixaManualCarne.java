
package techsoft.operacoes;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class BaixaManualCarne {
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.BaixaManualCarne");

    private String userTesoureiro;
    private String userDiretor;
    private String senhaTesoureiro;
    private String senhaDiretor;
    private int qtBaixa;
    private String msgAutenticado;
    private int seqCarne;
    private int matricula;
    private int categoria;
    private Date dataVenc;
    private BigDecimal valor;
    private Date dataPagto;
    private BigDecimal encargos;
    private BigDecimal vrPago;
    private String banco;


    public BigDecimal getEncargos() {
        return encargos;
    }
    public void setEncargos(BigDecimal encargos) {
        this.encargos = encargos;
    }
    
    public BigDecimal getVrPago() {
        return vrPago;
    }
    public void setVrPago(BigDecimal vrPago) {
        this.vrPago = vrPago;
    }
    
    public Date getDataPagto() {
        return dataPagto;
    }
    public void setDataPagto(Date dataPagto) {
        this.dataPagto = dataPagto;
    }

    public void setUserTesoureiro(String userTesoureiro) {
        this.userTesoureiro = userTesoureiro;
    }
    public void setUserDiretor(String userDiretor) {
        this.userDiretor = userDiretor;
    }
    public void setSenhaTesoureiro(String senhaTesoureiro) {
        this.senhaTesoureiro = senhaTesoureiro;
    }
    public void setSenhaDiretor(String senhaDiretor) {
        this.senhaDiretor = senhaDiretor;
    }
    public void setQtBaixa(int qtBaixa) {
        this.qtBaixa = qtBaixa;
    }
    public void setSeqCarne(int seqCarne) {
        this.seqCarne = seqCarne;
    }
    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    public int getSeqCarne() {
        return seqCarne;
    }
    public BigDecimal getValor() {
        return valor;
    }
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public int getQtBaixa() {
        return qtBaixa;
    }
    public int getMatricula() {
        return matricula;
    }
    public int getCategoria() {
        return categoria;
    }
    public void setDataVenc(Date dataVenc) {
        this.dataVenc = dataVenc;
    }
    public Date getDataVenc() {
        return dataVenc;
    }
    public void setMsgAutenticado(String msgAutenticado) {
        this.msgAutenticado = msgAutenticado;
    }
    public String getMsgAutenticado() {
        return msgAutenticado;
    }
    public String getUserTesoureiro() {
        return userTesoureiro;
    }
    public String getUserDiretor() {
        return userDiretor;
    }
    public String getSenhaTesoureiro() {
        return senhaTesoureiro;
    }
    public String getSenhaDiretor() {
        return senhaDiretor;
    }
    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }
    
    
    public void Autenticar(){
        Connection cn = Pool.getInstance().getConnection();;
        String sql = "";
        ResultSet rs = null;
        CallableStatement p = null;

        
        try {
            
            p = cn.prepareCall("{call SP_AUTENTICA_BAIXA_MANUAL (?, ?, ?, ?, 1720)}");
            
            p.setString(1, userTesoureiro);
            p.setString(2, senhaTesoureiro);
            p.setString(3, userDiretor);
            p.setString(4, senhaDiretor);

            rs = p.executeQuery();
            rs.next();
            msgAutenticado = rs.getString("RETORNO");
            

        } catch (Exception e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }
    
    public void validaCarne(){
        Connection cn = Pool.getInstance().getConnection();;
        String sql = "";
        ResultSet rs = null;
        CallableStatement p = null;

        
        try {
            
            sql = "SELECT VAL_CARNE, SEQ_CARNE, CD_MATRICULA, CD_CATEGORIA, DT_VENC_CARNE "
                       + "FROM TB_CARNE "
                       + "WHERE ((SEQ_CARNE = ?) OR ("
                       + "      CD_MATRICULA = ? "
                       + " AND  CD_CATEGORIA = ? "
                       + " AND  DT_VENC_CARNE = ?)) "
                       + " AND  CD_SIT_CARNE = 'NP' ";

            p = cn.prepareCall(sql);
            
            p.setInt(1, seqCarne);
            p.setInt(2, matricula);
            p.setInt(3, categoria);
            p.setDate(4, new java.sql.Date(dataVenc.getTime()));

            rs = p.executeQuery();
            
            if (rs.next()){
                seqCarne = rs.getInt("SEQ_CARNE");
                valor = rs.getBigDecimal("VAL_CARNE");
                matricula = rs.getInt("CD_MATRICULA");
                categoria = rs.getInt("CD_CATEGORIA");
                dataVenc = rs.getDate("DT_VENC_CARNE");
            }else{
                valor = BigDecimal.valueOf(0);
            }
            

        } catch (Exception e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }
    
    public void anulaCarne(Auditoria audit){
        Connection cn = Pool.getInstance().getConnection();;
        String sql = "";
        ResultSet rs = null;
        CallableStatement p = null;
        ParametroAuditoria par = new ParametroAuditoria();
        
        try {
            
            p = cn.prepareCall("{call sp_anula_carne (?, ?, ?)}");

            p.setInt(1, par.getSetParametro(categoria));
            p.setInt(2, par.getSetParametro(matricula));
            p.setDate(3, new java.sql.Date(par.getSetParametro(dataVenc).getTime()));

            p.execute();
            rs = p.getResultSet();
            rs.next();
            if (rs.getInt((1)) == 1){
                audit.registrarMudanca(sql, par.getParametroFinal());
                qtBaixa = qtBaixa - 1;
            }
            
            cn.commit();
            

        } catch (Exception e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }
    public void baixaCarne(Auditoria audit){
        Connection cn = Pool.getInstance().getConnection();;
        String sql = "";
        ResultSet rs = null;
        CallableStatement p = null;

        ParametroAuditoria par = new ParametroAuditoria();
        try {
            
            p = cn.prepareCall("{call sp_baixa_carne_manual (?, ?, ?, ?, ?, NULL, 'B', ?)}");

            p.setInt(1, par.getSetParametro(seqCarne));
            p.setBigDecimal(2, par.getSetParametro(valor));
            p.setBigDecimal(3, par.getSetParametro(encargos));
            p.setBigDecimal(4, par.getSetParametro(vrPago));
            p.setDate(5, new java.sql.Date(par.getSetParametro(dataPagto).getTime()));
            p.setString(6, par.getSetParametro(banco));

            p.execute();
            rs = p.getResultSet();
            rs.next();
            if (rs.getInt((1)) == 1){
                audit.registrarMudanca(sql, par.getParametroFinal());
                qtBaixa = qtBaixa - 1;
            }
            
            cn.commit();
            

        } catch (Exception e) {
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
