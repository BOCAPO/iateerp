package techsoft.caixa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;

public class AbreFechaCaixa {
    
    private String funcionario;
    private String estacao;
    private float recDinheiro;
    private float recCheque;
    private float recEstorno;
    private float pagDinheiro;
    private float pagCheque;
    private float pagEstorno;
    private float total;
    private Date dtMovimento;
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.AbreFechaCaixa");
    
    public String getFuncionario() {
        return funcionario;
    }
    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }
    public String getEstacao() {
        return estacao;
    }
    public void setEstacao(String estacao) {
        this.estacao = estacao;
    }
    public float getRecDinheiro() {
        return recDinheiro;
    }
    public void setRecDinheiro(float recDinheiro) {
        this.recDinheiro = recDinheiro;
    }
    public float getRecCheque() {
        return recCheque;
    }
    public void setRecCheque(float recCheque) {
        this.recCheque = recCheque;
    }
    public float getRecEstorno() {
        return recEstorno;
    }
    public void setRecEstorno(float recEstorno) {
        this.recEstorno = recEstorno;
    }
    public float getPagDinheiro() {
        return pagDinheiro;
    }
    public void setPagDinheiro(float pagDinheiro) {
        this.pagDinheiro = pagDinheiro;
    }
    public float getPagCheque() {
        return pagCheque;
    }
    public void setPagCheque(float pagCheque) {
        this.pagCheque = pagCheque;
    }
    public float getPagEstorno() {
        return pagEstorno;
    }
    public void setPagEstorno(float pagEstorno) {
        this.pagEstorno = pagEstorno;
    }
    public Date getDtMovimento() {
        return dtMovimento;
    }
    public void setDtMovimento(Date dtMovimento) {
        this.dtMovimento = dtMovimento;
    }
    public float getTotal() {
        return total;
    }
    public void setTotal(float total) {
        this.total = total;
    }

    public static String validaAbertura(String estacao, String funcionario) {
        String msg = "";
        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall("{call SP_RECUPERA_CAIXA_AB_MAQUINA (?)}", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            cal.setString(1, estacao);
            ResultSet rs = cal.executeQuery();
            
            int qtReg = 0;  
            while (rs.next()){  
              qtReg++;  
            }  
            
            if(qtReg>1){
                msg = "Mais de um caixa aberto nesta máquina. Contate o suporte.";
            }else{
                if(qtReg>0){
                    rs.first();              
                    Calendar cd = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String dt1 = sdf.format(cd.getTime());
                    String dt2 = sdf.format(rs.getDate("DT_SIT_CAIXA"));
                    if (!dt1.equals(dt2)){
                        msg = "Caixa ABERTO nesta máquina com data diferente da data atual. Feche o Caixa antes de uma Nova Abertura!";
                    }else{
                        String caixa = rs.getString("USER_ACESSO_SISTEMA");
                        if (caixa==null){
                            caixa = "";
                        }
                        if(funcionario.trim().toUpperCase().equals(caixa.toUpperCase().trim())){
                            msg = "O Caixa já foi aberto!";
                        }else{
                            msg = "O Caixa já está aberto nesta máquina pelo(a) " + caixa;
                        }
                    }
                }   
            }
            if ("".equals(msg)){
                //nao tem caixa aberto na maquina
                //vai se o funcionario nao está com caixa aberto em outra maquina
                cal = cn.prepareCall("{call SP_RECUPERA_CAIXA_FUNCIONARIO (?)}", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                cal.setString(1, funcionario);
                ResultSet rs2 = cal.executeQuery();

                if(rs2.next()){
                    if(!estacao.trim().equals(rs2.getString("ENDERECO_ESTACAO").trim())){
                        msg = "O funcionário já encontra-se com o Caixa aberto na máquina " + rs2.getString("ENDERECO_ESTACAO").trim();
                    }
                }

            }                    
    
            if ("".equals(msg)){
                //nao tem caixa aberto na maquina e o funcionario nao tem caixa aberto e outra maquina
                //vai verificar se o funcionario eh caixa
                cal = cn.prepareCall("{call SP_VERIFICA_FUNCIONARIO_CAIXA (?)}", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                cal.setString(1, funcionario);
                ResultSet rs3 = cal.executeQuery();

                if(!rs3.next()){
                    msg = "O funcionário não tem cargo de Caixa!";
                }

            }                    
            
            if ("".equals(msg)){
                msg = "OK";
            }
            
            
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return msg;
    }        
    
    public static String validaFechamento(String estacao, String funcionario) {
        String msg = "";
        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall("{call SP_RECUPERA_CAIXA_AB_MAQUINA (?)}", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            cal.setString(1, estacao);
            ResultSet rs = cal.executeQuery();
            
            int qtReg = 0;  
            while (rs.next()){  
              qtReg++;  
            }  
            
            if(qtReg==0){
                msg = "Não há caixa aberto nesta máquina.";
            }else if(qtReg>1){
                msg = "Mais de um caixa aberto nesta máquina. Contate o suporte.";
            }
            
            if ("".equals(msg)){
                msg = "OK";
            }
            
            
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return msg;
    }      


    public static void abreCaixa(String estacao, String funcionario) {
        String msg = "";
        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            
            cal = cn.prepareCall("{call SP_ATUALIZA_ABERTURA_CAIXA (?, ?)}");

             /*
            String sql = " UPDATE TB_CAIXA_REGISTRADORA " +
                         " SET SEQ_ULTIMA_ABERTURA = CASE WHEN (CONVERT(VARCHAR, GETDATE(), 103) = CONVERT(VARCHAR, DT_SIT_CAIXA, 103)) THEN SEQ_ULTIMA_ABERTURA + 1 ELSE 1 END, " +
                         "     Seq_Ultima_Autenticacao = 0, " + 
                         "     DT_SIT_CAIXA = CONVERT(DATETIME, CONVERT(VARCHAR, GETDATE(), 20)), " + 
                         "     CD_SIT_CAIXA = 'A', " + 
                         "     ENDERECO_ESTACAO = ? " + 
                         " WHERE CD_FUNCIONARIO in (SELECT CD_FUNCIONARIO FROM TB_FUNCIONARIO WHERE USER_ACESSO_SISTEMA = ? )";
            cal = cn.prepareCall(sql);
            */
            ParametroAuditoria par = new ParametroAuditoria();
            cal.setString(1, par.getSetParametro(estacao));
            cal.setString(2, par.getSetParametro(funcionario));
            cal.executeUpdate();
            cn.commit();
            
            Auditoria audit = new Auditoria(funcionario, "6020", estacao);
            audit.registrarMudanca("{call SP_ATUALIZA_ABERTURA_CAIXA (?, ?)}", par.getParametroFinal());
            
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }        


    public void fechaCaixa(String estacao) {
        int cdCaixa = 0;
        int seqAbertura = 0;
        String sql = "";
        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall("{call SP_RECUPERA_FUNC_CAIXA_FECHAR (?)}");
            cal.setString(1, estacao);
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                funcionario = rs.getString("NOME_FUNCIONARIO");
                dtMovimento = rs.getTimestamp("DT_SIT_CAIXA");
                cdCaixa = rs.getInt("CD_Caixa");
                seqAbertura = rs.getInt("SEQ_ULTIMA_ABERTURA");
            }

            cal = cn.prepareCall("{call SP_FECHA_CAIXA (?, ?, ?)}");
            ParametroAuditoria par = new ParametroAuditoria();
            cal.setInt(1, par.getSetParametro(cdCaixa));
            cal.setInt(2, par.getSetParametro(seqAbertura));
            cal.setTimestamp(3, new java.sql.Timestamp(par.getSetParametro(dtMovimento).getTime()));
            ResultSet rs2 = cal.executeQuery();
            if (rs2.next()){
                recDinheiro = rs2.getFloat("REC_DIN");
                recCheque = rs2.getFloat("REC_CHQ");
                recEstorno = rs2.getFloat("REC_EST");

                pagDinheiro = rs2.getFloat("PAG_DIN");
                pagCheque = rs2.getFloat("PAG_CHQ");
                pagEstorno = rs2.getFloat("PAG_EST");
                
                total = rs2.getFloat("Total");
            }
            
            
            cal = cn.prepareCall("{call SP_ATUALIZA_SIT_CAIXA_FECHADO (?)}");
            cal.setString(1, estacao);
            cal.executeUpdate();
            
            cn.commit();
            
            Auditoria audit = new Auditoria(funcionario, "6020", estacao);
            audit.registrarMudanca("{call SP_FECHA_CAIXA (?, ?, ?)}", par.getParametroFinal());
            
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }        

    
    
}
