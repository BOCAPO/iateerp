package techsoft.cadastro;

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
import techsoft.seguranca.Auditoria;

public class SocioCarne implements Serializable{

    private static final Logger log = Logger.getLogger("techsoft.cadastro.SocioCarne");
    
    private int id;
    private Date dataVencimento;
    private float valor;
    private float encargos;
    private float descontos;
    private float valorPago;
    private float valorOriginal;
    private float valorCredito;
    private float debAnt;
    private Date dataPagamento;
    private String localPagamento;
    private String bancoPagamento;
    private Date dataBaixa;
    private boolean permitePagamento;
    private Socio socio;
    
    private String nuConvenio;
    private String nossoNumero;
    private String numeroDocumento;
    private String codigoCedente;
    private String edSacado;
    private String edSacadoCompl;
    private String noCedente;
    private String noSacado;
    private String linhaDigitavel;
    private String codBarras;
    private String codCarteira;
    private String CPFPessoa;
    private Date dtDocumento;
    private Date dtProcessamento;

    
    
    public int getId() {
        return id;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getEncargos() {
        return encargos;
    }

    public void setEncargos(float encargos) {
        this.encargos = encargos;
    }
    
    public float getDebAnt() {
        return debAnt;
    }

    public void setDebAnt(float debAnt) {
        this.debAnt = debAnt;
    }

    public float getDescontos() {
        return descontos;
    }

    public void setDescontos(float descontos) {
        this.descontos = descontos;
    }

    public float getValorPago() {
        return valorPago;
    }

    public void setValorPago(float valorPago) {
        this.valorPago = valorPago;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getLocalPagamento() {
        return localPagamento;
    }

    public void setLocalPagamento(String localPagamento) {
        this.localPagamento = localPagamento;
    }

    public String getBancoPagamento() {
        return bancoPagamento;
    }

    public void setBancoPagamento(String bancoPagamento) {
        this.bancoPagamento = bancoPagamento;
    }
    
    public Date getDataBaixa() {
        return dataBaixa;
    }

    public void setDataBaixa(Date dataBaixa) {
        this.dataBaixa = dataBaixa;
    }

    public boolean isPermitePagamento() {
        return permitePagamento;
    }

    public void setPermitePagamento(boolean permitePagamento) {
        this.permitePagamento = permitePagamento;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
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
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    public void setCodigoCedente(String codigoCedente) {
        this.codigoCedente = codigoCedente;
    }
    public String getCodigoCedente() {
        return codigoCedente;
    }
    public void setEdSacado(String edSacado) {
        this.edSacado = edSacado;
    }
    public String getEdSacado() {
        return edSacado;
    }
    public void setEdSacadoCompl(String edSacadoCompl) {
        this.edSacadoCompl = edSacadoCompl;
    }
    public String getEdSacadoCompl() {
        return edSacadoCompl;
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
    public void setCodCarteira(String codCarteira) {
        this.codCarteira = codCarteira;
    }
    public String getCodCarteira() {
        return codCarteira;
    }
    public void setCPFPessoa(String CPFPessoa) {
        this.CPFPessoa = CPFPessoa;
    }
    public String getCPFPessoa() {
        return CPFPessoa;
    }
    
    public float getValorOriginal() {
        return valorOriginal;
    }
    public void setValorOriginal(float valorOriginal) {
        this.valorOriginal = valorOriginal;
    }
    public float getValorCredito() {
        return valorCredito;
    }
    public void setValorCredito(float valorCredito) {
        this.valorCredito = valorCredito;
    }
    
    
    public static List<SocioCarne> listar(Socio socio){

        ArrayList<SocioCarne> l = new ArrayList<SocioCarne>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "select * FROM TB_CARNE WHERE CD_MATRICULA = " + socio.getMatricula()
                    + " AND CD_CATEGORIA = " + socio.getIdCategoria()
                    + " AND CD_SIT_CARNE <> 'CA' ORDER BY DT_VENC_CARNE DESC";

            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                SocioCarne c = new SocioCarne();
                
                c.id = rs.getInt("SEQ_CARNE");
                c.dataVencimento = rs.getDate("DT_VENC_CARNE");
                c.valor = rs.getFloat("VAL_CARNE");
                c.encargos = rs.getFloat("VAL_ENCAR_CARNE");
                c.descontos = rs.getFloat("VAL_DESC_CARNE");
                c.valorPago = rs.getFloat("VAL_PGTO_CARNE");
                c.valorOriginal = rs.getFloat("VR_ORIGINAL");
                c.valorCredito = rs.getFloat("VR_CREDITO");
                c.dataPagamento = rs.getDate("DT_PGTO_CARNE");
                c.localPagamento = rs.getString("local_pgto_carne");
                c.bancoPagamento = rs.getString("banco_pgto");
                c.dataBaixa = rs.getDate("dt_baixa_carne");
                if(rs.getString("IC_PERMITE_PAGAMENTO") == null){
                    c.permitePagamento = false;
                }else{
                    c.permitePagamento = (rs.getString("IC_PERMITE_PAGAMENTO").equalsIgnoreCase("S") ? true : false);
                }
                c.socio = socio;

                l.add(c);
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
    
    public static void trancar (Auditoria audit, int id){

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "UPDATE TB_CARNE SET IC_PERMITE_PAGAMENTO = 'S' WHERE SEQ_CARNE = " + id;

            cn.createStatement().executeUpdate(sql);
            cn.commit();
            audit.registrarMudanca(sql);
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }
    
    public static void destrancar (Auditoria audit, int id){

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "UPDATE TB_CARNE SET IC_PERMITE_PAGAMENTO = NULL WHERE SEQ_CARNE = " + id;

            cn.createStatement().executeUpdate(sql);
            cn.commit();
            audit.registrarMudanca(sql);

        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }    

    public static SocioCarne getInstance(int id){
        SocioCarne b = null;
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            ResultSet rs = cn.createStatement().executeQuery("SELECT * FROM TB_CARNE WHERE SEQ_CARNE = " + id);
            while (rs.next()) {
                b = new SocioCarne();
                
                b.id = rs.getInt("SEQ_CARNE");
                b.dataVencimento = rs.getDate("DT_VENC_CARNE");
                b.valor = rs.getFloat("VAL_CARNE");
                b.socio = Socio.getInstance(rs.getInt("CD_MATRICULA"), rs.getInt("SEQ_DEPENDENTE"), rs.getInt("CD_CATEGORIA"));
                
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

        return b;
    }
    
    
    public static SocioCarne calculaEncargos(Date dtVenc, Date dtPagto, int matricula, int categoria, float valor, int idCarne){
        SocioCarne b = null;
        Connection cn = null;
        CallableStatement ca = null;
        ResultSet rs = null;
        
        b = new SocioCarne();

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_CALCULA_ENCARGOS_INPC (?, ?, ?, null, ?)}");
            cal.setDate(1, new java.sql.Date(dtVenc.getTime()));
            cal.setDate(2, new java.sql.Date(dtPagto.getTime()));
            cal.setFloat(3, valor);
            cal.setInt(4, idCarne);
            
            rs = cal.executeQuery();
            
            if (rs.next()) {
                if (rs.getFloat(1) > 0){
                    b.encargos = rs.getFloat(1);
                }else{
                    b.encargos = 0;                        
                }
            }
            
            cn.close();
            
            cn = Pool.getInstance().getConnection();
            cal = cn.prepareCall("{call SP_REC_CARNE_PEND_ATU (?, ?, ?, ?)}");
            cal.setDate(1, new java.sql.Date(dtVenc.getTime()));
            cal.setInt(2, matricula);
            cal.setInt(3, categoria);
            cal.setDate(4, new java.sql.Date(dtPagto.getTime()));
            
            rs = cal.executeQuery();
            
            if (rs.next()) {
                b.debAnt = rs.getFloat(1);
            }
            
            b.valorPago = valor + b.debAnt + b.encargos;            
            
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

        return b;
    }
    
    public static SocioCarne dadosBoleto(int idCarne, Date dtMaxPagto, String banco, String semEncargos, String semDebAnt){
        SocioCarne b = null;
        Connection cn = null;
        CallableStatement ca = null;
        ResultSet rs = null;
        
        b = new SocioCarne();

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "SELECT " +
                            "T1.NOME_PESSOA, " +
                            "ISNULL(CASE CD_END_CORRESPONDENCIA " + 
                            "WHEN 'R' THEN ENDERECO_R " +
                            "Else: ENDERECO_C " +
                            "END, '') AS ENDERECO, " +
                            "ISNULL(CASE CD_END_CORRESPONDENCIA " +
                            "WHEN 'R' THEN BAIRRO_R " +
                            "Else: BAIRRO_C " +
                            "END, '') AS COMPLEMENTO, " +
                            "ISNULL(CASE CD_END_CORRESPONDENCIA " +
                            "WHEN 'R' THEN CIDADE_R " +
                            "Else: CIDADE_C " +
                            "END, '') AS CIDADE, " +
                            "ISNULL(CASE CD_END_CORRESPONDENCIA " +
                            "WHEN 'R' THEN UF_R " +
                            "Else: UF_C " +
                            "END, '') AS UF, " +
                            "ISNULL(CASE CD_END_CORRESPONDENCIA " +
                            "WHEN 'R' THEN CEP_R " +
                            "Else: CEP_C " +
                            "END, '') AS CEP, " +
                            "ISNULL(T3.CPF_CGC_PESSOA, 0) AS CPF_PESSOA, " +
                            "GETDATE() DT_ATUAL, " +
                            "ISNULL(T4.NR_CONVENIO_BOLETO, '') AS NR_CONVENIO_BOLETO, " +
                            "ISNULL(T4.NR_AGENCIA_CEDENTE, '') AS NR_AGENCIA_CEDENTE, " +
                            "T1.CD_MATRICULA, " +             
                            "T1.CD_CATEGORIA, " +   
                            "T1.SEQ_DEPENDENTE, " +   
                            "RIGHT('00' + CONVERT(VARCHAR, T1.CD_CATEGORIA), 2) + '/' + RIGHT('0000' + CONVERT(VARCHAR, T1.CD_MATRICULA), 4) TITULO, " +
                            "T2.DT_VENC_CARNE, " +            
                            "T2.SEQ_CARNE, " +            
                            "ROUND(T2.VAL_CARNE, 2, 2) VAL_CARNE, " +
                            "T2.DT_VENC_CARNE, " +       
                            "ISNULL((SELECT SUM(VAL_CARNE_RELACIONADO) + SUM(VAL_ENCAR_CARNE_RELACIONADO) FROM TB_CARNE_RELACIONADO WHERE SEQ_CARNE = T2.SEQ_CARNE), 0) VAL_DEB_ANT, " +
                            "ROUND(ISNULL((SELECT SUM(VAL_CARNE_RELACIONADO) + SUM(VAL_ENCAR_CARNE_RELACIONADO) FROM TB_CARNE_RELACIONADO WHERE SEQ_CARNE = T2.SEQ_CARNE), 0) + T2.VAL_CARNE, 2, 2) VAL_TOTAL, " +
                            "IATE.DBO.FC_DEFINE_CARTEIRA_BOLETO(T2.SEQ_CARNE) BANCO " +
                       "FROM " +
                            "TB_PESSOA T1, " +
                            "TB_CARNE T2, " +
                            "TB_COMPLEMENTO T3, " +
                            "TB_PARAMETRO_SISTEMA T4 " +
                       "WHERE " +
                            "T1.CD_MATRICULA = T2.CD_MATRICULA AND " +
                            "T1.CD_CATEGORIA = T2.CD_CATEGORIA AND " +
                            "T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE AND " +
                            "T1.CD_MATRICULA = T3.CD_MATRICULA AND " +
                            "T1.CD_CATEGORIA = T3.CD_CATEGORIA AND " +
                            "T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND " +
                            "T2.SEQ_CARNE = ? ";

            CallableStatement cal = cn.prepareCall(sql);
            cal.setInt(1, idCarne);
            
            rs = cal.executeQuery();
            
            if (rs.next()) {
                b.id = rs.getInt("SEQ_CARNE");
                b.socio = Socio.getInstance(rs.getInt("CD_MATRICULA"), rs.getInt("SEQ_DEPENDENTE"), rs.getInt("CD_CATEGORIA"));
                b.nuConvenio = rs.getString("NR_CONVENIO_BOLETO");
                b.nossoNumero = rs.getString("SEQ_CARNE");
                b.numeroDocumento = rs.getString("TITULO");
                b.codigoCedente = rs.getString("NR_AGENCIA_CEDENTE");
                b.edSacado = rs.getString("Endereco") + " - " + rs.getString("COMPLEMENTO");
                b.edSacadoCompl = rs.getString("Cidade") + " - " + rs.getString("UF") + " - " + rs.getString("Cep");
                b.noCedente = "IATE CLUBE DE BRASILIA";
                b.noSacado = rs.getString("NOME_PESSOA");
                
                b.dtDocumento = rs.getDate("DT_ATUAL");
                b.dtProcessamento = rs.getDate("DT_ATUAL");
                b.socio = Socio.getInstance(rs.getInt("CD_MATRICULA"), 0, rs.getInt("CD_CATEGORIA"));                
                b.dataVencimento = rs.getDate("DT_VENC_CARNE");
                b.CPFPessoa = rs.getString("CPF_PESSOA");
                b.valor = rs.getFloat("VAL_CARNE");
                
                //BigDecimal bd = new BigDecimal(Float.toString(rs.getFloat("VAL_CARNE")));
                //bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                //b.valor = bd.floatValue();
                b.valor = rs.getBigDecimal("VAL_CARNE").floatValue();

                if (banco.equals("")){
                    banco = "ITAU" + rs.getString("banco");
                }
                
                if(banco.equals("ITAU112")){
                    if (semDebAnt.equals("S")){
                        b.valorPago = rs.getBigDecimal("VAL_CARNE").floatValue();
                        b.debAnt = 0;
                    }else{ 
                        b.valorPago = rs.getBigDecimal("VAL_TOTAL").floatValue();
                        b.debAnt = rs.getBigDecimal("VAL_DEB_ANT").floatValue();
                    }    
                    b.dataPagamento = rs.getDate("DT_VENC_CARNE");
                }else{
                    SocioCarne t = SocioCarne.calculaEncargos(b.dataVencimento, dtMaxPagto, rs.getInt("CD_MATRICULA"), rs.getInt("CD_CATEGORIA"), b.valor, rs.getInt("SEQ_CARNE"));
                    
                    if (semEncargos.equals("S")){
                        if (semDebAnt.equals("S")){
                            b.valorPago = rs.getBigDecimal("VAL_CARNE").floatValue();
                            b.debAnt = 0;
                            b.encargos = 0;
                        }else{
                            b.valorPago =  rs.getBigDecimal("VAL_CARNE").floatValue() + t.debAnt;
                            b.debAnt = t.debAnt;
                            b.encargos = 0;
                        }
                    }else{
                        if (semDebAnt.equals("S")){
                            b.valorPago = rs.getBigDecimal("VAL_CARNE").floatValue() + t.encargos;     
                            b.debAnt = 0;
                            b.encargos = t.encargos;
                        }else{
                            b.valorPago = t.valorPago;     
                            b.debAnt = t.debAnt;
                            b.encargos = t.encargos;
                        }
                    }
                    
                    b.dataPagamento = dtMaxPagto;
                }
            }
            
            cn.close();

            cn = Pool.getInstance().getConnection();
            if(banco.equals("BB")){
                cal = cn.prepareCall("{call SP_COD_BARRA_CARNE (?, ?, ?)}");
                cal.setInt(1, Integer.parseInt(b.nossoNumero));
                cal.setFloat(2, b.valorPago);
                cal.setDate(3, new java.sql.Date(dtMaxPagto.getTime()));
            }else if(banco.equals("ITAU109")){
                cal = cn.prepareCall("{call SP_COD_BARRA_CARNE_ITAU (?, ?, ?)}");
                cal.setInt(1, Integer.parseInt(b.nossoNumero));
                cal.setFloat(2, b.valorPago);
                cal.setDate(3, new java.sql.Date(dtMaxPagto.getTime()));
            }else {
                cal = cn.prepareCall("{call SP_COD_BARRA_CARNE_ITAU_112 (?)}");
                cal.setInt(1, Integer.parseInt(b.nossoNumero));
            }
            
            rs = cal.executeQuery();
            
            if (rs.next()) {
                b.codBarras = rs.getString("COD_BARRA");
                b.linhaDigitavel = rs.getString("LINHA_DIGITAVEL");
                if(banco.substring(0,4).equals("ITAU")){
                    b.codCarteira = rs.getString("CD_CARTEIRA");
                }
                
            }
            
            cn.commit();
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

        return b;
    }

    public static List<String> parcelaBoleto(int idCarne, float debAnt, float enc, String icDeclaracao){

        ArrayList<String> l = new ArrayList<String>();
        Connection cn = null;
        ResultSet rs = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_REC_PARC_CARNE_BOLETO (?, ?, ?, ?)}");
            cal.setInt(1, idCarne);
            cal.setFloat(2, debAnt);
            cal.setFloat(3, enc);
            if (icDeclaracao.equals("true")){
                cal.setString(4, "S");
            }else{
                cal.setString(4, "N");
            }
            
            rs = cal.executeQuery();
            while (rs.next()) {
                l.add(rs.getString("RETORNO"));
            }
            cn.commit();
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

    public static String i25Encode(String strBarCode) {
        String[] asPattern = new String[10];
        String sSTART, sSTOP;

        String sMyI25 = "";
        String result = "";
        String sGIF = "";
        boolean bBar = true;
        String dirImages = "images_boleto/";

        sSTART = "NNNN";
        sSTOP  = "WNN";

        asPattern[0] = "NNWWN";
        asPattern[1] = "WNNNW";
        asPattern[2] = "NWNNW";
        asPattern[3] = "WWNNN";
        asPattern[4] = "NNWNW";
        asPattern[5] = "WNWNN";
        asPattern[6] = "NWWNN";
        asPattern[7] = "NNNWW";
        asPattern[8] = "WNNWN";
        asPattern[9] = "NWNWN";

        String sEncodedSTR = "";
        String sUnit = "";

        String sDigit1, sDigit2;

        for (int iCharRead = 0; iCharRead < strBarCode.length(); iCharRead+=2) {

                sDigit1 = strBarCode.substring(iCharRead, iCharRead + 1);
                sDigit2 = strBarCode.substring(iCharRead + 1, iCharRead + 2);

                sDigit1 = asPattern[Integer.parseInt(sDigit1)];
                sDigit2 = asPattern[Integer.parseInt(sDigit2)];

                sUnit = "";

                for (int i = 0; i < 5; i++) {
                        sUnit = sUnit + sDigit1.substring(i, i + 1) + sDigit2.substring(i, i + 1);
                        //sUnit = sUnit & Mid( sDigit1, i, 1 ) & Mid( sDigit2, i, 1 )
                }

            sEncodedSTR += sUnit;

        }

        sMyI25 = sSTART + sEncodedSTR + sSTOP;

        for (int iPos = 0; iPos < sMyI25.length(); iPos++) {
            sGIF = sMyI25.substring(iPos, iPos + 1).toLowerCase() + (bBar?"b.gif":"s.gif");
            result += "<img src=\"" + dirImages + sGIF + "\">";
            bBar = !bBar;
        }

        return result;
        
    }

    
    
}
