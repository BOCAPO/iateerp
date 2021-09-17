
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

public class EstornoManualCarne {
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.EstornoManualCarne");

    private String userTesoureiro;
    private String userDiretor;
    private String senhaTesoureiro;
    private String senhaDiretor;
    private int qtEstorno;
    private String msgAutenticado;
    private int seqCarne;
    private int matricula;
    private int categoria;
    private Date dataVenc;
    private BigDecimal valor;
    private Date dataPagto;
    private BigDecimal encargos;

    private BigDecimal desconto;
    private String pessoa;
    private Date dataBaixa;
    private String local;

    private String motivo;

    public BigDecimal getEncargos() {
        return encargos;
    }
    public void setEncargos(BigDecimal encargos) {
        this.encargos = encargos;
    }

    public void setPessoa(String pessoa) {
        this.pessoa = pessoa;
    }
    public String getPessoa() {
        return pessoa;
    }
    public Date getDataBaixa() {
        return dataBaixa;
    }
    public void setDataBaixa(Date dataBaixa) {
        this.dataBaixa = dataBaixa;
    }

    public void setLocal(String local) {
        this.local = local;
    }
    public String getLocal() {
        return local;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    public String getMotivo() {
        return motivo;
    }

    
    public BigDecimal getDesconto() {
        return desconto;
    }
    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
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
    public void setQtEstorno(int qtEstorno) {
        this.qtEstorno = qtEstorno;
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
    public int getQtEstorno() {
        return qtEstorno;
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
    
    public void Autenticar(){
        Connection cn = Pool.getInstance().getConnection();;
        String sql = "";
        ResultSet rs = null;
        CallableStatement p = null;

        
        try {
            
            p = cn.prepareCall("{call SP_AUTENTICA_BAIXA_MANUAL (?, ?, ?, ?, 1755)}");
            
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
            
            sql = "SELECT T1.SEQ_CARNE, T1.CD_MATRICULA, T1.CD_CATEGORIA, T1.DT_VENC_CARNE, T2.NOME_PESSOA, "
                       + "T1.VAL_CARNE, T1.VAL_DESC_CARNE, T1.VAL_ENCAR_CARNE, T1.DT_PGTO_CARNE, T1.DT_BAIXA_CARNE, T1.LOCAL_PGTO_CARNE "
                       + "FROM TB_CARNE T1, TB_PESSOA T2 "
                       + "WHERE T1.CD_MATRICULA = T2.CD_MATRICULA "
                       + "AND   T1.CD_CATEGORIA = T2.CD_CATEGORIA "
                       + "AND   T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE "
                       + "AND  T1.CD_SIT_CARNE = 'PG' "
                       + "AND ((T1.SEQ_CARNE = ?) OR "
                       + "     (T1.CD_MATRICULA = ? "
                       + " AND  T1.CD_CATEGORIA = ? "
                       + " AND  T1.DT_VENC_CARNE = ?)) ";

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
                dataPagto = rs.getDate("DT_PGTO_CARNE");
                dataBaixa = rs.getDate("DT_BAIXA_CARNE");
                pessoa = rs.getString("NOME_PESSOA");
                encargos = rs.getBigDecimal("VAL_ENCAR_CARNE");
                desconto = rs.getBigDecimal("VAL_DESC_CARNE");
                if (rs.getString("LOCAL_PGTO_CARNE").equals("C")){
                    local = "CLUBE";
                }else{
                    local = "BANCO";
                }
                
                
                
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
    public void estornaCarne(Auditoria audit, String usuario){
        Connection cn = Pool.getInstance().getConnection();;
        String sql = "";
        ResultSet rs = null;
        CallableStatement p = null;

        
        try {
            
            p = cn.prepareCall("{call SP_ESTORNA_CARNE (?, ?, ?)}");

            p.setInt(1, seqCarne);
            p.setString(2, usuario);
            p.setString(3, motivo);

            p.execute();
            audit.registrarMudanca(sql, String.valueOf(seqCarne), usuario, String.valueOf(motivo));
            qtEstorno = qtEstorno - 1;
            
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
