package techsoft.clube;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;

public class BoletoImpresso implements Serializable{

    private static final Logger log = Logger.getLogger("techsoft.clube.BoletoImpresso");
    
    private Date dataVencimento;
    private BigDecimal valor;
    //private float encargos;
    //private float descontos;
    //private float valorPago;
    //private float debAnt;
    private Date dataPagamento;
    //private String localPagamento;
    //private Date dataBaixa;
    //private boolean permitePagamento;
    //private Socio socio;
    
    private String nuConvenio;
    private String nossoNumero;
    //private String numeroDocumento;
    private String codigoCedente;
    private String edSacado;
    private String edSacadoCompl;
    private String noCedente;
    private String noSacado;
    private String fatVenc;
    private String linhaDigitavel;
    private String codBarras;
    private Date dtDocumento;
    private Date dtProcessamento;

    public Date getDataVencimento() {
        return dataVencimento;
    }
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
    public BigDecimal getValor() {
        return valor;
    }
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public Date getDataPagamento() {
        return dataPagamento;
    }
    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
    public void setDtDocumento(Date dtDocumento) {
        this.dtDocumento = dtDocumento;
    }
    public Date getDtDocumento() {
        return dtDocumento;
    }
    public void setDtProcessamento(Date dtProcessamento) {
        this.dtProcessamento = dtProcessamento;
    }
    public Date getDtProcessamento() {
        return dtProcessamento;
    }
    public void setFatVenc(String fatVenc) {
        this.fatVenc = fatVenc;
    }
    public String getFatVenc() {
        return fatVenc;
    }
    public void setNuConvenio(String nuConvenio) {
        this.nuConvenio = nuConvenio;
    }
    public String getNuConvenio() {
        return nuConvenio;
    }
    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }
    public String getNossoNumero() {
        return nossoNumero;
    }
    public void setCodigoCedente(String codigoCedente) {
        this.codigoCedente = codigoCedente;
    }
    public String getCodigoCedente() {
        return codigoCedente;
    }
    public void setNoCedente(String noCedente) {
        this.noCedente = noCedente;
    }
    public String getNoCedente() {
        return noCedente;
    }
    public void setNoSacado(String noSacado) {
        this.noSacado = noSacado;
    }
    public String getNoSacado() {
        return noSacado;
    }
    public void setLinhaDigitavel(String linhaDigitavel) {
        this.linhaDigitavel = linhaDigitavel;
    }
    public String getLinhaDigitavel() {
        return linhaDigitavel;
    }
    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }
    public String getCodBarras() {
        return codBarras;
    }
    public String getEdSacado() {
        return edSacado;
    }
    public void setEdSacado(String edSacado) {
        this.edSacado = edSacado;
    }
    public String getEdSacadoCompl() {
        return edSacadoCompl;
    }
    public void setEdSacadoCompl(String edSacadoCompl) {
        this.edSacadoCompl = edSacadoCompl;
    }
    

}
