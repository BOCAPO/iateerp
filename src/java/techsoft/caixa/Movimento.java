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
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;
import techsoft.tabelas.InserirException;
import techsoft.util.Datas;

public class Movimento {
    
    private String pagante;
    private int seqAutenticacao;
    private int seqAbertura;
    private int cdCaixa;
    private int cdTransacao;
    private String deTransacao;
    private Date dtSitCaixa;
    private float valor;
    
    private String operador;
    private int matricula;
    private int categoria;

    private float vrDevido;
    private float vrAcrescimo;
    private float vrDesconto;
    private float vrDinheiro;
    private float vrCheque;
    private float vrOutros;

    private String cheques;
    private String outrasFormas;
    private String detCarne;
    private Date dtVenc;
    private String observacao;
    private String controleParcPre;
    private String estacao;
    
    private int nrConvite;
    
    private String detMov;
    
    private int idTxIndividual;
    
    private String taxasIndividuais;

    private static final Logger log = Logger.getLogger("techsoft.caixa.Movimento");
    
    public String getPagante() {
        return pagante;
    }
    public void setPagante(String pagante) {
        this.pagante = pagante;
    }
    public int getSeqAutenticacao() {
        return seqAutenticacao;
    }
    public void setSeqAutenticacao(int seqAutenticacao) {
        this.seqAutenticacao = seqAutenticacao;
    }
    public int getSeqAbertura() {
        return seqAbertura;
    }
    public void setSeqAbertura(int seqAbertura) {
        this.seqAbertura = seqAbertura;
    }
    public int getCdCaixa() {
        return cdCaixa;
    }
    public void setCdCaixa(int cdCaixa) {
        this.cdCaixa = cdCaixa;
    }
    public int getCdTransacao() {
        return cdTransacao;
    }
    public void setCdTransacao(int cdTransacao) {
        this.cdTransacao = cdTransacao;
    }
    public String getDeTransacao() {
        return deTransacao;
    }
    public void setDeTransacao(String deTransacao) {
        this.deTransacao = deTransacao;
    }
    public Date getDtSitCaixa() {
        return dtSitCaixa;
    }
    public void setDtSitCaixa(Date dtSitCaixa) {
        this.dtSitCaixa = dtSitCaixa;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public String getOperador() {
        return operador;
    }
    public void setOperador(String operador) {
        this.operador = operador;
    }
    public int getMatricula() {
        return matricula;
    }
    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
    public int getCategoria() {
        return categoria;
    }
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    public float getVrDevido() {
        return vrDevido;
    }
    public void setVrDevido(float vrDevido) {
        this.vrDevido = vrDevido;
    }
    public float getVrAcrescimo() {
        return vrAcrescimo;
    }
    public void setVrAcrescimo(float vrAcrescimo) {
        this.vrAcrescimo = vrAcrescimo;
    }
    public float getVrDesconto() {
        return vrDesconto;
    }
    public void setVrDesconto(float vrDesconto) {
        this.vrDesconto = vrDesconto;
    }
    public float getVrDinheiro() {
        return vrDinheiro;
    }
    public void setVrDinheiro(float vrDinheiro) {
        this.vrDinheiro = vrDinheiro;
    }
    public float getVrCheque() {
        return vrCheque;
    }
    public void setVrCheque(float vrCheque) {
        this.vrCheque = vrCheque;
    }
    public String getCheques() {
        return cheques;
    }
    public void setCheques(String cheques) {
        this.cheques = cheques;
    }
    public float getVrOutros() {
        return vrOutros;
    }
    public void setVrOutros(float vrOutros) {
        this.vrOutros = vrOutros;
    }
    public String getOutrasFormas() {
        return outrasFormas;
    }
    public void setOutrasFormas(String outrasFormas) {
        this.outrasFormas = outrasFormas;
    }
    public Date getDtVenc() {
        return dtVenc;
    }
    public void setDtVenc(Date dtVenc) {
        this.dtVenc = dtVenc;
    }
    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    public String getControleParcPre() {
        return controleParcPre;
    }
    public void setControleParcPre(String controleParcPre) {
        this.controleParcPre = controleParcPre;
    }
    public String getEstacao() {
        return estacao;
    }
    public void setEstacao(String estacao) {
        this.estacao = estacao;
    }
    public String getDetCarne() {
        return detCarne;
    }
    public void setDetCarne(String detCarne) {
        this.detCarne = detCarne;
    }
    
    public String getDetMov() {
        return detMov;
    }
    public void setDetMov(String detMov) {
        this.detMov = detMov;
    }
    public int getIdTxIndividual() {
        return idTxIndividual;
    }
    public void setIdTxIndividual(int idTxIndividual) {
        this.idTxIndividual = idTxIndividual;
    }
    public String getTaxasIndividuais() {
        return taxasIndividuais;
    }
    public void setTaxasIndividuais(String taxasIndividuais) {
        this.taxasIndividuais = taxasIndividuais;
    }
    public int getNrConvite() {
        return nrConvite;
    }
    public void setNrConvite(int nrConvite) {
        this.nrConvite = nrConvite;
    }
        
    
    public static String validaMovimento(String estacao, String funcionario) {
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
            }else{
                rs.first();              
                Calendar cd = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dt1 = sdf.format(cd.getTime());
                String dt2 = sdf.format(rs.getDate("DT_SIT_CAIXA"));
                if (!dt1.equals(dt2)){
                    msg = "Caixa ABERTO nesta máquina com data diferente da data atual. Feche o Caixa!";
                }else{
                    String caixa = rs.getString("USER_ACESSO_SISTEMA");
                    if (caixa==null){
                        caixa = "";
                    }
                    if(!funcionario.trim().toUpperCase().equals(caixa.trim().toUpperCase())){
                        msg = "O Caixa está aberto nesta máquina pelo(a) " + caixa;
                    }
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
    public static List<Movimento> listar(String estacao) {
        
        Connection cn = null;
        ArrayList<Movimento> l = new ArrayList<Movimento>();
        
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall("{call SP_RECUPERA_MOV_CAIXA (?)}");
            cal.setString(1, estacao);
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Movimento i = new Movimento();
                i.setPagante(rs.getString("NOME_PAGANTE"));
                i.setSeqAutenticacao(rs.getInt("Seq_Autenticacao"));
                i.setCdTransacao(rs.getInt("CD_TRANSACAO"));
                i.setDeTransacao(rs.getString("DESCR_TRANsACAO"));
                i.setValor(rs.getFloat("VAL_MOVIMENTO_CAIXA"));
                i.setCdCaixa(rs.getInt("CD_Caixa"));
                i.setSeqAbertura(rs.getInt("Seq_Abertura"));
                i.setDtSitCaixa(rs.getTimestamp("DT_SIT_CAIXA"));
                
                l.add(i);
            }
            cn.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        return l;
    }
    
    public void estornar(Auditoria audit)throws ExcluirException{
 
        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            ParametroAuditoria par = new ParametroAuditoria();
            cal = cn.prepareCall("{call SP_ESTORNA_MOVIMENTO (?, ?, ?, ?)}");
            cal.setInt(1, par.getSetParametro(cdCaixa));
            cal.setInt(2, par.getSetParametro(seqAbertura));
            cal.setInt(3, par.getSetParametro(seqAutenticacao));
            cal.setTimestamp(4, new java.sql.Timestamp(par.getSetParametro(dtSitCaixa).getTime()));
            
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    seqAutenticacao = rs.getInt("SQ_AUTENTICACAO");
                    cn.commit();
                    audit.registrarMudanca("{call SP_ESTORNA_MOVIMENTO (?, ?, ?, ?)}", par.getParametroFinal());
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new ExcluirException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new ExcluirException(err);

            }

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }
    
    public void incluir(Auditoria audit)throws InserirException{
 
        Connection cn = null;
        CallableStatement cal = null;
        
        try {
            String[] linhaCheque = cheques.split(";");
            cn = Pool.getInstance().getConnection();
            String sql="";
            Transacao d = Transacao.getInstance(cdTransacao);
            
            if (d.getTipo().equals("R")){
                // RECEBIMENTO DE CARNE
                
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date dtAtual = new Date();
                BigDecimal vrEnc = BigDecimal.ZERO;
                BigDecimal vrMulta = BigDecimal.ZERO;
                BigDecimal vrJuros = BigDecimal.ZERO;
                
                float vrDesc = 0;
                float vrPagar = 0;
                BigDecimal vrDescBD = BigDecimal.ZERO;
                BigDecimal vrPagarBD = BigDecimal.ZERO;
                int maiorCheque=0;
                int maiorFormaPagamento=0;
                if (!cheques.equals("")){
                    maiorCheque = maiorCheque();
                }
                if (!outrasFormas.equals("")){
                    maiorFormaPagamento = maiorFormaPagamento();
                }

                sql = "SELECT DT_VENC_CARNE, VAL_CARNE, SEQ_CARNE, ISNULL(IC_PERMITE_PAGAMENTO, 'N') IC_PERMITE_PAGAMENTO " +
                      "FROM TB_CARNE  " +
                      "WHERE " +
                      "     CD_MATRICULA = " + matricula + " AND " +
                      "     CD_CATEGORIA = " + categoria + " AND " +
                      "     CD_SIT_CARNE = 'NP' AND " +
                      "     DT_VENC_CARNE " + operador + " '" + fmt.format(dtVenc) + "'" +
                      "ORDER BY DT_VENC_CARNE DESC     ";
                
                cal = cn.prepareCall(sql);    

                ResultSet rs = cal.executeQuery();
                while (rs.next()) {
                    if (!rs.getString("IC_PERMITE_PAGAMENTO").equals("S")){
                        
                        sql = " {CALL SP_CALCULA_ENCARGOS_INPC (?, ?, ?, null, ?)}";
                        cal = cn.prepareCall(sql);    

                        cal.setDate(1, new java.sql.Date(rs.getDate("DT_VENC_CARNE").getTime()));
                        cal.setDate(2, new java.sql.Date(dtAtual.getTime()));
                        cal.setFloat(3, rs.getFloat("VAL_CARNE"));
                        cal.setInt(4, rs.getInt("SEQ_CARNE"));

                        ResultSet rs2 = cal.executeQuery();
                        vrEnc = BigDecimal.ZERO;
                        vrMulta = BigDecimal.ZERO;
                        vrJuros = BigDecimal.ZERO;
                        if (rs2.next()) {
                            vrEnc = vrEnc.add(rs2.getBigDecimal(1).setScale(2, BigDecimal.ROUND_DOWN));
                            vrMulta = vrMulta.add(rs2.getBigDecimal(2).setScale(2, BigDecimal.ROUND_DOWN) );
                            vrJuros = vrEnc.subtract(vrMulta);
                        }
                        
                        vrDescBD = BigDecimal.ZERO;
                        vrPagarBD = BigDecimal.ZERO;
                        
                        vrDesc = (vrDesconto * rs.getFloat("VAL_CARNE")) / vrDevido;
                        vrPagar = rs.getFloat("VAL_CARNE") + vrEnc.floatValue() - vrDesc;
                        vrDescBD = new BigDecimal(String.valueOf(vrDesc));
                        vrPagarBD = new BigDecimal(String.valueOf(vrPagar));
                        
                        cal = cn.prepareCall("{call SP_PAGA_CARNE_CAIXA (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                        ParametroAuditoria par = new ParametroAuditoria();
                        cal.setString(1, par.getSetParametro(estacao));
                        cal.setString(2, par.getSetParametro(pagante));
                        cal.setBigDecimal(3, par.getSetParametro(vrPagarBD.setScale(2, BigDecimal.ROUND_DOWN)));
                        cal.setInt(4, par.getSetParametro(cdTransacao));
                        if ("".equals(cheques)){
                            cal.setNull(5, java.sql.Types.INTEGER);
                            par.getSetNull();
                        }else{
                            cal.setInt(5, par.getSetParametro(linhaCheque.length));
                        }
                        
                        cal.setInt(6, par.getSetParametro(rs.getInt("SEQ_CARNE")));
                        cal.setBigDecimal(7, par.getSetParametro(vrDescBD.setScale(2, BigDecimal.ROUND_DOWN)));
                        cal.setFloat(8, par.getSetParametro(vrEnc.floatValue()));
                        cal.setFloat(9, par.getSetParametro(vrMulta.floatValue()));
                        cal.setFloat(10, par.getSetParametro(vrJuros.floatValue()));
                        
                        cal.setInt(11, par.getSetParametro(matricula));
                        cal.setInt(12, par.getSetParametro(categoria));

                        ResultSet rs3 = cal.executeQuery();
                        if (rs3.next()){
                            seqAutenticacao = rs3.getInt("autenticacao");
                            seqAbertura = rs3.getInt("Abertura");
                            cdCaixa = rs3.getInt("Caixa");
                            dtSitCaixa = rs3.getTimestamp("data_movimento");
                            
                            cn.commit();
                            audit.registrarMudanca("{call SP_PAGA_CARNE_CAIXA (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}", par.getParametroFinal());
                            
                            if (!cheques.equals("")){
                                incluirCheque(vrPagar, maiorCheque);
                            }
                            if (!outrasFormas.equals("")){
                                incluirFormaPagamento(vrPagar, maiorFormaPagamento);
                            }
                        }
                        rs3.close();
                    }
                }    
                
            }else{
                //TRANSACOES DE CREDITO e PRE PAGOS
                cal = cn.prepareCall("{call SP_INCLUI_MOVIMENTO_CAIXA (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                ParametroAuditoria par = new ParametroAuditoria();

                cal.setString(1, par.getSetParametro(estacao));
                cal.setString(2, par.getSetParametro(pagante));
                cal.setFloat(3, par.getSetParametro(valor));
                cal.setInt(4, par.getSetParametro(cdTransacao));
                if ("".equals(cheques)){
                    cal.setNull(5, java.sql.Types.INTEGER);
                    par.getSetNull();
                }else{
                    cal.setInt(5, par.getSetParametro(linhaCheque.length));
                }
                if (idTxIndividual>0){
                    cal.setInt(6, par.getSetParametro(idTxIndividual));
                }else{
                    cal.setNull(6, java.sql.Types.INTEGER);
                    par.getSetNull();
                }
                if (matricula>0){
                    cal.setInt(7, par.getSetParametro(matricula));
                }else{
                    cal.setNull(7, java.sql.Types.INTEGER);
                    par.getSetNull();
                }
                if (categoria>0){
                    cal.setInt(8, par.getSetParametro(categoria));
                }else{
                    cal.setNull(8, java.sql.Types.INTEGER);
                    par.getSetNull();
                }
                
                cal.setString(9, par.getSetParametro(audit.getLogin()));
                
                if (nrConvite>0){
                    cal.setInt(10, par.getSetParametro(nrConvite));
                }else{
                    cal.setNull(10, java.sql.Types.INTEGER);
                    par.getSetNull();
                }
                
                ResultSet rs = cal.executeQuery();
                if (rs.next()){
                    if (rs.getString("MSG").equals("OK")){
                        seqAutenticacao = rs.getInt("autenticacao");
                        seqAbertura = rs.getInt("Abertura");
                        cdCaixa = rs.getInt("Caixa");
                        dtSitCaixa = rs.getTimestamp("data_movimento");
                        cn.commit();
                        audit.registrarMudanca("{call SP_INCLUI_MOVIMENTO_CAIXA (?, ?, ?, ?, ?, ?, ?, ?, ?)}", par.getParametroFinal());
                        rs.close();

                        if (!"".equals(cheques)){
                            incluirCheque(valor, maiorCheque());
                        }
                        
                        if (!outrasFormas.equals("")){
                            incluirFormaPagamento(valor, maiorFormaPagamento());
                        }
                        
                        if (!"".equals(taxasIndividuais) && taxasIndividuais != null){
                            incluirTaxasIndividuais(audit.getLogin());
                        }
                        
                        //usado para Lancamento de Produtos e Servicos
                        if (!"".equals(detMov) && detMov != null){
                            incluirLancamento();
                        }
                    }else{
                        String err = rs.getString("MSG");
                        log.warning(err);
                        throw new InserirException(err);
                    }
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new InserirException(err);
                }
            }
            
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new InserirException(ex.getMessage());
            }

            log.severe(e.getMessage());
            throw new InserirException(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new InserirException(e.getMessage());
            }
        }
    }    

    public void incluirCheque(float valCobranca, int seqCheque){
        
        Connection cn = null;
        CallableStatement cal = null;
        float valCheque=0;
        float valDestinado=0;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql="";
        
            String[] vetCheques = cheques.split(";");
            String[] vetDet;
            valDestinado = (vrCheque * valCobranca) / valor;

            for(int i =0; i < vetCheques.length ; i++){
                vetDet = vetCheques[i].split("#");

                sql = "INSERT INTO TB_CHEQUE_RECEBIDO (CD_CAIXA, CD_TRANSACAO, DT_MOVIMENTO_CAIXA, " + 
                      "SEQ_AUTENTICACAO, SEQ_ABERTURA, NR_CHEQUE, CD_BANCO, DT_DEPOSITO, VAL_CHEQUE, " +
                      "DT_ESTORNO, SERIE_CHEQUE, IC_CONTROLA_PARC_PRE, VR_TOTAL_CHEQUE, SEQ_CHEQUE)" + 
                      "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, null, ?, ?, ?, ?)";
                cal = cn.prepareCall(sql);

                cal.setInt(1, cdCaixa);
                cal.setInt(2, cdTransacao);
                cal.setTimestamp(3, new java.sql.Timestamp(dtSitCaixa.getTime()));
                cal.setInt(4, seqAutenticacao);
                cal.setInt(5, seqAbertura);
                cal.setInt(6, Integer.parseInt(vetDet[1]));
                cal.setInt(7, Integer.parseInt(vetDet[0]));
                cal.setDate(8, new java.sql.Date(Datas.parse(vetDet[3]).getTime()));
                valCheque = (Float.parseFloat(vetDet[4]) * valDestinado) / vrCheque;

                cal.setFloat(9, valCheque);
                cal.setString(10, vetDet[2]);
                if (controleParcPre==null){
                    cal.setString(11, "N");
                }else{
                    if (controleParcPre.equals("true")){
                        cal.setString(11, "S");
                    }else{
                        cal.setString(11, "N");
                    }
                }
                cal.setFloat(12, Float.parseFloat(vetDet[4]));
                cal.setInt(13, seqCheque);

                cal.executeUpdate();
                cn.commit();

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
    
    public void incluirFormaPagamento(float valCobranca, int seqFormaPagamento){
        
        Connection cn = null;
        CallableStatement cal = null;
        float valCheque=0;
        float valDestinado=0;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql="";
        
            String[] vetFormaPagamento = outrasFormas.split(";");
            String[] vetDet;
            valDestinado = (vrOutros * valCobranca) / valor;

            for(int i =0; i < vetFormaPagamento.length ; i++){
                vetDet = vetFormaPagamento[i].split("#");

                sql = "INSERT INTO TB_MOVIMENTO_FORMA_PAGAMENTO (CD_CAIXA, CD_TRANSACAO, DT_MOVIMENTO_CAIXA, " + 
                      "SEQ_AUTENTICACAO, SEQ_ABERTURA, CD_FORMA_PAGAMENTO, VAL_PAGO, VR_TOTAL_PAGO, SEQ_PAGAMENTO, DE_DOCUMENTO)" +
                      "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                cal = cn.prepareCall(sql);

                cal.setInt(1, cdCaixa);
                cal.setInt(2, cdTransacao);
                cal.setTimestamp(3, new java.sql.Timestamp(dtSitCaixa.getTime()));
                cal.setInt(4, seqAutenticacao);
                cal.setInt(5, seqAbertura);
                
                valCheque = (Float.parseFloat(vetDet[1]) * valDestinado) / vrOutros;
                cal.setInt(6, Integer.parseInt(vetDet[0]));
                cal.setFloat(7, valCheque);

                
                cal.setFloat(8, Float.parseFloat(vetDet[1]));
                cal.setInt(9, seqFormaPagamento);
                cal.setString(10, vetDet[2]);

                cal.executeUpdate();
                cn.commit();

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
    
    public void incluirTaxasIndividuais(String usuario){
        
        Connection cn = null;
        CallableStatement cal = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql="";
        
            String[] vetTaxas = taxasIndividuais.split(",");

            for(int i =0; i < vetTaxas.length ; i++){

                cal = cn.prepareCall("{call SP_VINCULA_TX_IND_MOV_CAIXA (?, ?, ?, ?, ?, ?, ?)}");

                cal.setInt(1, cdCaixa);
                cal.setInt(2, seqAbertura);
                cal.setInt(3, seqAutenticacao);
                cal.setTimestamp(4, new java.sql.Timestamp(dtSitCaixa.getTime()));
                cal.setInt(5, cdTransacao);
                cal.setInt(6, Integer.parseInt(vetTaxas[i]));
                cal.setString(7, usuario);
                
                cal.executeUpdate();
                cn.commit();
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

    public void incluirLancamento(){
        
        Connection cn = null;
        CallableStatement cal = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql="";
            float vrDesc = 0;
            float vrAtual = 0;
        
            String[] vetMov = detMov.split(";");
            String[] vetDet;

            for(int i =0; i < vetMov.length ; i++){
                vetDet = vetMov[i].split("#");
                vrAtual = Float.parseFloat(vetDet[3]);
                vrDesc = (vrAtual * vrDesconto)/vrDevido;

                sql = "INSERT INTO TB_LANCAMENTO (CD_CAIXA, SEQ_ABERTURA, DT_MOVIMENTO_CAIXA, SEQ_AUTENTICACAO, " + 
                      "CD_PRODUTO_SERVICO, QT_PRODUTO_SERVICO, VR_LANCAMENTO, VR_DESCONTO, DT_LANCAMENTO) " +
                      "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                cal = cn.prepareCall(sql);

                cal.setInt(1, cdCaixa);
                cal.setInt(2, seqAbertura);
                cal.setTimestamp(3, new java.sql.Timestamp(dtSitCaixa.getTime()));
                cal.setInt(4, seqAutenticacao);
                
                cal.setInt(5, Integer.parseInt(vetDet[1]));
                cal.setInt(6, Integer.parseInt(vetDet[2]));

                cal.setFloat(7, vrAtual);
                cal.setFloat(8, vrDesc);
                
                cal.setDate(9, new java.sql.Date(Datas.parse(vetDet[0]).getTime()));

                cal.executeUpdate();
                cn.commit();
                
                sql = "UPDATE TB_PRODUTO_SERVICO " + 
                      "SET QT_ESTOQUE_ATUAL = QT_ESTOQUE_ATUAL - " + vetDet[2] +
                      "WHERE CD_PRODUTO_SERVICO = " + vetDet[1];
                cal = cn.prepareCall(sql);
                cal.executeUpdate();
                cn.commit();
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
    
    public int maiorCheque(){
        
        Connection cn = null;
        CallableStatement cal = null;
        int nr=0;
        
        try {
            cn = Pool.getInstance().getConnection();
            
            cal = cn.prepareCall("UPDATE TB_PARAMETRO_SISTEMA SET SEQ_CHEQUE = SEQ_CHEQUE + 1");
            cal.executeUpdate();
            cn.commit();

            cal = cn.prepareCall("SELECT SEQ_CHEQUE FROM TB_PARAMETRO_SISTEMA ");
            ResultSet rs = cal.executeQuery();
            rs.next();
            nr = rs.getInt("SEQ_CHEQUE");
            rs.close();
        
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
        
        return nr;
    }
    
    
    public int maiorFormaPagamento(){
        
        Connection cn = null;
        CallableStatement cal = null;
        int nr=0;
        
        try {
            cn = Pool.getInstance().getConnection();
            
            cal = cn.prepareCall("UPDATE TB_PARAMETRO_SISTEMA SET SEQ_FORMA_PAGAMENTO = SEQ_FORMA_PAGAMENTO + 1");
            cal.executeUpdate();
            cn.commit();

            cal = cn.prepareCall("SELECT SEQ_FORMA_PAGAMENTO FROM TB_PARAMETRO_SISTEMA ");
            ResultSet rs = cal.executeQuery();
            rs.next();
            nr = rs.getInt("SEQ_FORMA_PAGAMENTO");
            rs.close();
        
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
        
        return nr;
    }
        
    
    public static Movimento getInstance(int cdCaixa, int seqAbertura, int seqAutenticacao, Date dtMovimento) {
        String sql = "SELECT * " +
                     "FROM TB_MOVIMENTO_CAIXA T1, TB_TRANSACAO T2 "+
                     "WHERE T1.CD_TRANSACAO = T2.CD_TRANSACAO " + 
                     " AND  T1.CD_CAIXA = ? " +
                     " AND  T1.SEQ_ABERTURA = ? " +
                     " AND  T1.SEQ_AUTENTICACAO = ? " +
                     " AND  T1.DT_MOVIMENTO_CAIXA = ? ";
                
        Connection cn = null;
        Movimento i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, cdCaixa);
            p.setInt(2, seqAbertura);
            p.setInt(3, seqAutenticacao);
            p.setTimestamp(4, new java.sql.Timestamp(dtMovimento.getTime()));
            
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new Movimento();
                i.setCdCaixa(rs.getInt("CD_CAIXA"));
                i.setSeqAbertura(rs.getInt("SEQ_ABERTURA"));
                i.setSeqAutenticacao(rs.getInt("SEQ_AUTENTICACAO"));
                i.setDtSitCaixa(rs.getTimestamp("DT_MOVIMENTO_CAIXA"));
                i.setCdTransacao(rs.getInt("CD_TRANSACAO"));
                i.setDeTransacao(rs.getString("DESCR_TRANSACAO"));
                i.setPagante(rs.getString("NOME_PAGANTE"));
                i.setValor(rs.getFloat("VAL_MOVIMENTO_CAIXA"));
                i.setMatricula(rs.getInt("CD_MATRICULA"));
                i.setCategoria(rs.getInt("CD_CATEGORIA"));
                
                i.setCheques("");
                
                sql = "SELECT * " +
                      "FROM TB_CHEQUE_RECEBIDO "+
                      "WHERE  CD_CAIXA = ? " +
                      " AND  SEQ_ABERTURA = ? " +
                      " AND  SEQ_AUTENTICACAO = ? " +
                      " AND  DT_MOVIMENTO_CAIXA = ? ";
                p = cn.prepareStatement(sql);
                p.setInt(1, cdCaixa);
                p.setInt(2, seqAbertura);
                p.setInt(3, seqAutenticacao);
                p.setTimestamp(4, new java.sql.Timestamp(dtMovimento.getTime()));
                
                ResultSet rs2 = p.executeQuery();
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                while(rs2.next()){
                    i.setCheques(i.getCheques() + rs2.getString("CD_BANCO")+'#'+rs2.getString("NR_CHEQUE")+'#'+rs2.getString("SERIE_CHEQUE")+'#'+sdf.format(rs2.getDate("DT_DEPOSITO"))+'#'+rs2.getFloat("VAL_CHEQUE")+';');
                }
                
                if (rs.getInt("SEQ_CARNE_MOVIMENTO")>0){
                    
                    i.setDetCarne("");
                    DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
                    sql = "SELECT * " +
                          "FROM TB_CARNE "+
                          "WHERE SEQ_CARNE = ? ";
                    p = cn.prepareStatement(sql);
                    p.setInt(1, rs.getInt("SEQ_CARNE_MOVIMENTO"));
                    ResultSet rs3 = p.executeQuery();

                    while(rs3.next()){
                        i.setVrDesconto(rs3.getFloat("VAL_DESC_CARNE"));
                        i.setVrAcrescimo(rs3.getFloat("VAL_ENCAR_CARNE"));
                        i.setDetCarne(i.getDetCarne() + "Vencimento: " + sdf.format(rs3.getDate("DT_VENC_CARNE"))+"# ;");
                        
                        CallableStatement cal = cn.prepareCall("{call SP_RECUPERA_HISTORICO (?)}");
                        cal.setInt(1, rs.getInt("SEQ_CARNE_MOVIMENTO"));
                        ResultSet rs4 = cal.executeQuery();
                        
                        while(rs4.next()){
                            i.setDetCarne(i.getDetCarne() + "    " + rs4.getString("DESCR_TAXA") + "#" + df.format(rs4.getFloat("VR_TAXA")) +";");
                        }
                        
                    }
                }
                
                sql = "SELECT * " +
                      "FROM TB_LANCAMENTO "+
                      "WHERE  CD_CAIXA = ? " +
                      " AND  SEQ_ABERTURA = ? " +
                      " AND  SEQ_AUTENTICACAO = ? " +
                      " AND  DT_MOVIMENTO_CAIXA = ? "+
                      " ORDER BY DT_LANCAMENTO ";
                
                p = cn.prepareStatement(sql);
                p.setInt(1, cdCaixa);
                p.setInt(2, seqAbertura);
                p.setInt(3, seqAutenticacao);
                p.setTimestamp(4, new java.sql.Timestamp(dtMovimento.getTime()));
                
                ResultSet rs3 = p.executeQuery();
                i.setDetMov("");
                while(rs3.next()){
                    i.setDetMov(i.getDetMov() + sdf.format(rs3.getDate("DT_LANCAMENTO")) + "#" + rs3.getString("CD_PRODUTO_SERVICO") + "#" + rs3.getString("QT_PRODUTO_SERVICO") + "#" + rs3.getString("VR_LANCAMENTO")+";");
                }
                
                sql = "SELECT NU_SEQ_TAXA_INDIVIDUAL " +
                      "FROM TB_MOV_CAIXA_TX_IND "+
                      "WHERE  CD_CAIXA = ? " +
                      " AND  SEQ_ABERTURA = ? " +
                      " AND  SEQ_AUTENTICACAO = ? " +
                      " AND  DT_MOVIMENTO_CAIXA = ? ";
                
                p = cn.prepareStatement(sql);
                p.setInt(1, cdCaixa);
                p.setInt(2, seqAbertura);
                p.setInt(3, seqAutenticacao);
                p.setTimestamp(4, new java.sql.Timestamp(dtMovimento.getTime()));
                
                ResultSet rs4 = p.executeQuery();
                i.setTaxasIndividuais("");
                while(rs4.next()){
                    i.setTaxasIndividuais(i.getTaxasIndividuais()+ rs4.getString("NU_SEQ_TAXA_INDIVIDUAL")+",");
                }
                
                
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

        return i;
    }     



}
