
package techsoft.operacoes;

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
import techsoft.clube.ParametroSistema;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class GeraCarne {
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.GeraCarne");

    private Date dataReferencia;
    private int matriculaInicial;
    private int matriculaFinal;
    private String categorias = "";
    private String tipo = "";
    private String resultadoGeracao;

    public void setMatriculaInicial(int matriculaInicial) {
        this.matriculaInicial = matriculaInicial;
    }
    public void setMatriculaFinal(int matriculaFinal) {
        this.matriculaFinal = matriculaFinal;
    }
    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }
    public void setDataReferencia(Date dataReferencia) {
        this.dataReferencia = dataReferencia;
    }
    public String getCategorias() {
        return categorias;
    }
    public String getResultadoGeracao() {
        return resultadoGeracao;
    }    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public void gerar(String estacao, Auditoria audit){
        int qtReg = 0;
        int qtRegPesq = 0;
        Connection cn = Pool.getInstance().getConnection();
        Connection cn2 = Pool.getInstance().getConnection();
        String sql = "";
        ResultSet rs = null;
        ResultSet rs2 = null;
        PreparedStatement p = null;
        CallableStatement p2 = null;
        SimpleDateFormat formatoMes = new SimpleDateFormat("MM");
        SimpleDateFormat formatoAno = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatoYMD = new SimpleDateFormat("yyyy-MM-dd");
        String mes = formatoMes.format(dataReferencia);
        String ano = formatoAno.format(dataReferencia);
        ParametroAuditoria par = new ParametroAuditoria();

        try {
            
            //***********************
            //Gera√ß√£o de carne normal
            //***********************
            
            sql = "SELECT CD_MATRICULA, CD_CATEGORIA "
                       + "FROM TB_PESSOA "
                       + "WHERE SEQ_DEPENDENTE = 0 "
                       + " AND  CD_SIT_PESSOA <> 'US' "
                       + " AND  CD_CATEGORIA IN (" + categorias.substring(0, categorias.length()-1) + ")";

            if(matriculaInicial >= 1 ){
                sql = sql + " AND  CD_MATRICULA >= " + matriculaInicial; 
            }                
            if(matriculaFinal >= 1 ){
                sql = sql + " AND  CD_MATRICULA <= " + matriculaFinal; 
            }              
            
            p = cn.prepareStatement(sql);
            rs = p.executeQuery();
                    
            while(rs.next()){
                par.limpaParametro();
                p2 = cn.prepareCall("{call SP_GERA_CARNE_TITULO (?, ?, ?, ?, NULL, ?, NULL, ?)}");

                p2.setInt(1, par.getSetParametro(rs.getInt("CD_MATRICULA")));
                p2.setInt(2, par.getSetParametro(rs.getInt("CD_CATEGORIA")));
                p2.setDate(3, new java.sql.Date(par.getSetParametro(dataReferencia).getTime()));
                p2.setString(4, par.getSetParametro(audit.getLogin()));
                p2.setString(5, par.getSetParametro(estacao));
                p2.setString(6, par.getSetParametro(tipo));
                p2.execute();

                rs2 = p2.getResultSet();

                qtRegPesq = qtRegPesq+1;
                if(rs2.next()){
                    qtReg = qtReg + rs2.getInt(1);
                }

                cn.commit();
                //audit.registrarMudanca("{call SP_GERA_CARNE_TITULO (?, ?, ?, ?, NULL, ?)}", par.getParametroFinal());

            }
            
            resultadoGeracao = "GERA«√O DE CARNE NORMAL:";
            resultadoGeracao = resultadoGeracao + "<br> Quant. Carne Pesquisado: " + Integer.toString(qtRegPesq);
            resultadoGeracao = resultadoGeracao + "<br> Quant. Carne Gerado: " + Integer.toString(qtReg);

            
            //*****************************************************
            //Gera√ß√£o dos carnes empresariais com usu√°rio no t√≠tulo
            //*****************************************************
            
            if (categorias.indexOf("14") >= 0) {
                qtRegPesq = 0;
                qtReg = 0;
                
                sql = "SELECT CD_MATRICULA, CD_CATEGORIA "
                           + "FROM TB_PESSOA "
                           + "WHERE SEQ_DEPENDENTE = 0 "
                           + " AND  CD_CATEGORIA = 14";

                if(matriculaInicial >= 1 ){
                    sql = sql + " AND  CD_MATRICULA >= " + matriculaInicial; 
                }                
                if(matriculaFinal >= 1 ){
                    sql = sql + " AND  CD_MATRICULA <= " + matriculaFinal; 
                }              

                p = cn.prepareStatement(sql);
                rs = p.executeQuery();
                
                while(rs.next()){
                    par.limpaParametro();
                    
                    p2 = cn.prepareCall("{call SP_GERA_CARNE_TITULO (?, ?, ?, ?, NULL, ?, NULL, ?)}");

                    p2.setInt(1, par.getSetParametro(rs.getInt("CD_MATRICULA")));
                    p2.setInt(2, par.getSetParametro(rs.getInt("CD_CATEGORIA")));
                    p2.setDate(3, new java.sql.Date(par.getSetParametro(dataReferencia).getTime()));
                    p2.setString(4, par.getSetParametro(audit.getLogin()));
                    p2.setString(5, par.getSetParametro(estacao));
                    p2.setString(6, par.getSetParametro(tipo));
                    p2.execute();

                    rs2 = p2.getResultSet();

                    qtRegPesq = qtRegPesq+1;
                    if(rs2.next()){
                        qtReg = qtReg + rs2.getInt(1);
                    }

                    cn.commit();
                    //audit.registrarMudanca("{call SP_GERA_CARNE_TITULO (?, ?, ?, ?, NULL, ?)}", par.getParametroFinal());

                }

                resultadoGeracao = resultadoGeracao + "<br><br> GERA«√O DOS CARNES EMPRESARIAIS COM USU¡RIO NO TÕTULO: ";
                resultadoGeracao = resultadoGeracao + "<br> Quant. Carne Pesquisado: " + Integer.toString(qtRegPesq);
                resultadoGeracao = resultadoGeracao + "<br> Quant. Carne Gerado: " + Integer.toString(qtReg);

            }            

            
            //*************************************************************************
            //Gera√ß√£o dos carnes com usuarios no titulo e que possuem taxas individuais
            //*************************************************************************
            qtRegPesq = 0;
            qtReg = 0;
            
            sql = "SELECT "
                 +       "CD_MATRICULA, "
                 +       "CD_CATEGORIA " 
                 +   "From " 
                 +       "TB_PESSOA T1 " 
                 +   "Where "
                 +       "T1.CD_SIT_PESSOA = 'US' AND " 
                 +       "T1.SEQ_DEPENDENTE = 0 AND " 
                 +       "Exists " 
                 +          "( "
                 +          "SELECT " 
                 +             "1 " 
                 +          "From " 
                 +             "TB_VAL_TAXA_INDIVIDUAL T2 " 
                 +          "Where " 
                 +             "T1.CD_MATRICULA   = T2.CD_MATRICULA    AND " 
                 +             "T1.CD_CATEGORIA   = T2.CD_CATEGORIA    AND " 
                 +             "T2.IC_SITUACAO_TAXA  = 'N'       AND " 
                 +             "AA_COBRANCA       = " + ano + " AND " 
                 +             "MM_COBRANCA       = " + mes + " " 
                 +          ") ";
            
            if(matriculaInicial >= 1 ){
                sql = sql + " AND  CD_MATRICULA >= " + matriculaInicial; 
            }                
            if(matriculaFinal >= 1 ){
                sql = sql + " AND  CD_MATRICULA <= " + matriculaFinal; 
            }               
 
            p = cn.prepareStatement(sql);
            rs = p.executeQuery();

            while(rs.next()){

                p2 = cn.prepareCall("{call SP_GERA_CARNE_PROP_US_TIT (?, ?, ?, ?, ?, ?)}");

                p2.setInt(1, par.getSetParametro(rs.getInt("CD_MATRICULA")));
                p2.setInt(2, par.getSetParametro(rs.getInt("CD_CATEGORIA")));
                p2.setDate(3, new java.sql.Date(par.getSetParametro(dataReferencia).getTime()));
                p2.setString(4, par.getSetParametro(audit.getLogin()));
                p2.setString(5, par.getSetParametro(estacao));
                p2.setString(6, par.getSetParametro(tipo));                
                p2.execute();

                rs2 = p2.getResultSet();

                qtRegPesq = qtRegPesq+1;
                if(rs2.next()){
                    qtReg = qtReg + rs2.getInt(1);
                }

                cn.commit();
                //audit.registrarMudanca("{call SP_GERA_CARNE_PROP_US_TIT (?, ?, ?, ?, ?)}", par.getParametroFinal());

            }

            resultadoGeracao = resultadoGeracao + "<br><br> GERA«√O DOS CARNES COM USU¡RIOS NO TÕTULO E QUE POSSUEM TAXAS INDIVIDUAIS: ";
            resultadoGeracao = resultadoGeracao + "<br> Quant. Carne Pesquisado: " + Integer.toString(qtRegPesq);
            resultadoGeracao = resultadoGeracao + "<br> Quant. Carne Gerado: " + Integer.toString(qtReg);
            
            

            
            //*****************************************************************************************************
            //Geracao dos carnes que foram pagos antecipadamente e pessuem taxa individual para o mes do vencimento
            //*****************************************************************************************************
            qtRegPesq = 0;
            qtReg = 0;
            
            sql = "SELECT "
                 +       "CD_MATRICULA, "
                 +       "CD_CATEGORIA " 
                 +   "FROM " 
                 +       "TB_PESSOA T1 " 
                 +   "WHERE  "
                 +       "EXISTS " 
                 +          "( "
                 +          "SELECT " 
                 +             "1 " 
                 +          "FROM " 
                 +             "TB_CARNE T0 " 
                 +          "Where " 
                 +             "T1.CD_MATRICULA   = T0.CD_MATRICULA    AND " 
                 +             "T1.CD_CATEGORIA   = T0.CD_CATEGORIA    AND " 
                 +             "T1.SEQ_DEPENDENTE = T0.SEQ_DEPENDENTE  AND " 
                 +             "T0.CD_SIT_CARNE   = 'PG'               AND " 
                 +             "T0.DT_VENC_CARNE  = '" + formatoYMD.format(dataReferencia) + "'" 
                 +          ") ";
            
            if(matriculaInicial >= 1 ){
                sql = sql + " AND  T1.CD_MATRICULA >= " + matriculaInicial; 
            }                
            if(matriculaFinal >= 1 ){
                sql = sql + " AND  T1.CD_MATRICULA <= " + matriculaFinal; 
            }               
 
            p = cn.prepareStatement(sql);
            rs = p.executeQuery();

            while(rs.next()){

                p2 = cn.prepareCall("{call SP_GERA_CARNE_PG_ANT (?, ?, ?, ?, ?, ?)}");

                p2.setInt(1, par.getSetParametro(rs.getInt("CD_MATRICULA")));
                p2.setInt(2, par.getSetParametro(rs.getInt("CD_CATEGORIA")));
                p2.setDate(3, new java.sql.Date(par.getSetParametro(dataReferencia).getTime()));
                p2.setString(4, par.getSetParametro(audit.getLogin()));
                p2.setString(5, par.getSetParametro(estacao));
                p2.setString(6, par.getSetParametro(tipo));                
                p2.execute();

                rs2 = p2.getResultSet();

                qtRegPesq = qtRegPesq+1;
                if(rs2.next()){
                    qtReg = qtReg + rs2.getInt(1);
                }

                cn.commit();
                //audit.registrarMudanca("{call SP_GERA_CARNE_PROP_US_TIT (?, ?, ?, ?, ?)}", par.getParametroFinal());

            }

            resultadoGeracao = resultadoGeracao + "<br><br> GERA«√O DOS CARNES PAGOS ANTECIPADAMENTO COM TAXA INDIVIDUAL: ";
            resultadoGeracao = resultadoGeracao + "<br> Quant. Carne Pesquisado: " + Integer.toString(qtRegPesq);
            resultadoGeracao = resultadoGeracao + "<br> Quant. Carne Gerado: " + Integer.toString(qtReg);
                        
            
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
