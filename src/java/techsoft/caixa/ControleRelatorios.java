package techsoft.caixa;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.carteirinha.JobMiniImpressora;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.db.Pool;
import techsoft.operacoes.Taxa;
import techsoft.tabelas.Funcionario;
import techsoft.util.Datas;

@Controller
public class ControleRelatorios{
    

    @App("6190")
    public static void rel6190(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        if ("imprimir".equals(acao)){
            String sql = "";
            String titulo2 = "";
            boolean quebra1 = false;
            boolean quebra2 = false;
            int total1 = 3;
            boolean contaPre = Boolean.parseBoolean(request.getParameter("contaPre"));
            boolean contaPos = Boolean.parseBoolean(request.getParameter("contaPos"));
            String dtIni = request.getParameter("dataInicio");
            String dtFim = request.getParameter("dataFim");
            
            if (Boolean.parseBoolean(request.getParameter("agruparUsuario")) && "A".equals(request.getParameter("tipoRel"))){
                quebra1 = true;
                quebra2 = true;
                total1++;
            }else{
                if (Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                    quebra1 = true;    
                    total1++;
                }
                if ("A".equals(request.getParameter("tipoRel"))){
                    quebra1 = true;    
                }else{
                    total1--;
                }
            }
            
            if ("G".equals(request.getParameter("periodo"))){
                titulo2 = "Data de Geração de " + dtIni + " até " + dtFim;
            }else{
                titulo2 = "Período de Cobrança de " + dtIni + " até " + dtFim;
            }
            
            if (contaPos){
                if ("A".equals(request.getParameter("tipoRel"))){
                    sql = "SELECT ";
                    if (Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                        sql = sql + "UPPER(T3.USER_INCLUSAO) as 'Incl. por', ";
                    }
                    sql = sql +
                             "CONVERT(VARCHAR(50), CONVERT(VARCHAR, T1.CD_CATEGORIA) + '/' + " +
                             "CONVERT(VARCHAR, T1.CD_MATRICULA) + ' - ' +  " +
                             "T1.NOME_PESSOA) as 'Nome', " +
                             "CONVERT(VARCHAR(25), T2.DESCR_TX_ADMINISTRATIVA) as 'Taxa', " +
                             "T3.VR_TAXA as 'Valor', " +
                             "T3.DT_GERACAO_TAXA as 'Dt. Geração', " +
                             "CONVERT(VARCHAR(7), RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) + '/' + CONVERT(VARCHAR, T3.AA_COBRANCA)) AS 'Cobrança em',  " +
                             "T3.IC_SITUACAO_TAXA as 'Sit.', ";
                    if (!Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                        sql = sql + "T3.USER_INCLUSAO as 'Incl. por', ";
                    }
                    sql = sql +
                             "T3.DT_CANCELAMENTO_TAXA as 'Dt. Canc.', " +
                             "T3.USER_CANCELAMENTO as 'Canc. por', " +
                             "T3.NU_SEQ_TAXA_INDIVIDUAL as 'Id' ";
                }else{
                    sql = "SELECT ";
                    if (Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                        sql = sql + "T3.USER_INCLUSAO as 'Incl. por', ";
                    }
                    sql = sql +
                             "CONVERT(VARCHAR(50), CONVERT(VARCHAR, T1.CD_CATEGORIA) + '/' + " +
                             "CONVERT(VARCHAR, T1.CD_MATRICULA) + ' - ' +  " +
                             "T1.NOME_PESSOA) as 'Nome', " +
                             "T3.VR_TAXA as 'Valor' ";
                }


                sql = sql +
                       "FROM " +
                            "TB_PESSOA T1, " +
                            "TB_TAXA_ADMINISTRATIVA T2, " +
                            "TB_VAL_TAXA_INDIVIDUAL T3 " +
                       "WHERE " +
                            "T1.CD_MATRICULA = T3.CD_MATRICULA AND " +
                            "T1.CD_CATEGORIA = T3.CD_CATEGORIA AND " +
                            "T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND " +
                            "T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA ";

                sql = sql + " AND T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "') ";

                if (!"".equals(request.getParameter("categoria"))){
                    sql = sql + " AND T1.CD_CATEGORIA = " + request.getParameter("categoria");
                }    
                if (!"".equals(request.getParameter("matricula"))){
                    sql = sql + " AND T1.CD_MATRICULA = " + request.getParameter("matricula");
                }    
                if (!"".equals(request.getParameter("nome"))){
                    sql = sql + " AND T1.NOME_PESSOA like '" + request.getParameter("nome") + "%'";
                }    
                if (!"0".equals(request.getParameter("taxa"))){
                    sql = sql + " AND T2.CD_TX_ADMINISTRATIVA = " + request.getParameter("taxa");
                }    
                if (!"".equals(request.getParameter("lstUsuario"))){
                    sql = sql + " AND T3.USER_INCLUSAO = '" + request.getParameter("lstUsuario") + "'";
                }    
                if (Boolean.parseBoolean(request.getParameter("normal"))){
                    if (!Boolean.parseBoolean(request.getParameter("cancelada"))){
                        sql = sql + " AND T3.IC_SITUACAO_TAXA = 'N'";
                    }
                }else{
                    sql = sql + " AND T3.IC_SITUACAO_TAXA = 'C'";
                }

                if ("G".equals(request.getParameter("periodo"))){
                    sql = sql + " AND T3.DT_GERACAO_TAXA >= '" + dtIni.substring(3,6)+dtIni.substring(0,3)+dtIni.substring(6)+ " 00:00:00'";
                    sql = sql + " AND T3.DT_GERACAO_TAXA <= '" + dtFim.substring(3,6)+dtFim.substring(0,3)+dtFim.substring(6) + " 23:59:59'";
                }else{
                    sql = sql + " AND CONVERT(VARCHAR, T3.AA_COBRANCA) + RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) >= '" + dtIni.substring(6) + dtIni.substring(3,5) + "'";
                    sql = sql + " AND CONVERT(VARCHAR, T3.AA_COBRANCA) + RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) <= '" + dtFim.substring(6) + dtFim.substring(3,5) + "'";
                }    
                if (Boolean.parseBoolean(request.getParameter("carnePAgo"))){
                    sql = sql +
                             " AND EXISTS " +
                                   "( " +
                                   "SELECT " +
                                         "1 " +
                                   "From " +
                                         "VW_CARNE_PG T4 " +
                                   "Where " +
                                         "T1.CD_MATRICULA      = T4.CD_MATRICULA          AND " +
                                         "T1.CD_CATEGORIA      = T4.CD_CATEGORIA          AND " +
                                         "T1.SEQ_DEPENDENTE = T4.SEQ_DEPENDENTE           AND " +
                                         "T3.MM_COBRANCA    = T4.MM_VENCIMENTO            AND " +
                                         "T3.AA_COBRANCA = T4.AA_VENCIMENTO " +
                                   ")";                
                }
            }
            if (contaPre && contaPos){
                sql = sql + " UNION ALL ";
            }
            if (contaPre){
                if ("A".equals(request.getParameter("tipoRel"))){
                    sql = sql + 
                          "SELECT ";
                    if (Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                        sql = sql + "UPPER(T3.USER_INCLUSAO) as 'Incl. por', ";
                    }
                    sql = sql + 
                             "CONVERT(VARCHAR(50), CONVERT(VARCHAR, T1.CD_CATEGORIA) + '/' + " +
                             "CONVERT(VARCHAR, T1.CD_MATRICULA) + ' - ' +  " +
                             "T1.NOME_PESSOA) as 'Nome', " +
                             "CONVERT(VARCHAR(25), T2.DESCR_TX_ADMINISTRATIVA) as 'Taxa', " +
                             "T3.VR_MOVIMENTO as 'Valor', " +
                             "T3.DT_MOVIMENTO as 'Dt. Geração', " +
                             "NULL AS 'Cobrança em',  " +
                             "T3.IC_SIT_MOVIMENTO as 'Sit.', ";
                    if (!Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                        sql = sql + "T3.USER_INCLUSAO as 'Incl. por', ";
                    }
                    sql = sql + 
                             "T3.DT_CANCELAMENTO as 'Dt. Canc.', " +
                             "T3.USER_CANCELAMENTO as 'Canc. por', " +
                             "T3.NU_SEQ_PRE_PAGO as 'Id' ";
                }else{
                    sql = sql + 
                          "SELECT ";
                    if (Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                        sql = sql + "T3.USER_INCLUSAO as 'Incl. por', ";
                    }
                    sql = sql + 
                             "CONVERT(VARCHAR(50), CONVERT(VARCHAR, T1.CD_CATEGORIA) + '/' + " +
                             "CONVERT(VARCHAR, T1.CD_MATRICULA) + ' - ' +  " +
                             "T1.NOME_PESSOA) as 'Nome', " +
                             "T3.VR_MOVIMENTO as 'Valor' ";
                }


                sql = sql +
                       "FROM " +
                            "TB_PESSOA T1, " +
                            "TB_TAXA_ADMINISTRATIVA T2, " +
                            "TB_VAL_PRE_PAGO T3 " +
                       "WHERE " +
                            "T1.CD_MATRICULA = T3.CD_MATRICULA AND " +
                            "T1.CD_CATEGORIA = T3.CD_CATEGORIA AND " +
                            "T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND " +
                            "T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA ";

                sql = sql + " AND T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "') ";

                if (!"".equals(request.getParameter("categoria"))){
                    sql = sql + " AND T1.CD_CATEGORIA = " + request.getParameter("categoria");
                }    
                if (!"".equals(request.getParameter("matricula"))){
                    sql = sql + " AND T1.CD_MATRICULA = " + request.getParameter("matricula");
                }    
                if (!"".equals(request.getParameter("nome"))){
                    sql = sql + " AND T1.NOME_PESSOA like '" + request.getParameter("nome") + "%'";
                }    
                if (!"0".equals(request.getParameter("taxa"))){
                    sql = sql + " AND T2.CD_TX_ADMINISTRATIVA = " + request.getParameter("taxa");
                }    
                if (!"".equals(request.getParameter("lstUsuario"))){
                    sql = sql + " AND T3.USER_INCLUSAO = '" + request.getParameter("lstUsuario") + "'";
                }    

                if (Boolean.parseBoolean(request.getParameter("normal"))){
                    if (!Boolean.parseBoolean(request.getParameter("cancelada"))){
                        sql = sql + " AND T3.IC_SIT_MOVIMENTO = 'N'";
                    }
                }else{
                    sql = sql + " AND T3.IC_SIT_MOVIMENTO = 'C'";
                }

                sql = sql + " AND T3.DT_MOVIMENTO >= '" + dtIni.substring(3,6)+dtIni.substring(0,3)+dtIni.substring(6)+ " 00:00:00'";
                sql = sql + " AND T3.DT_MOVIMENTO <= '" + dtFim.substring(3,6)+dtFim.substring(0,3)+dtFim.substring(6) + " 23:59:59'";
            }
            
            if (Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                sql = sql +
                        "ORDER BY " +
                            "'Incl. por' ";
                
                if ("A".equals(request.getParameter("tipoRel"))){
                    sql = sql +
                            ", 'Dt. Geração'";
                }
                
            }else{
                if ("A".equals(request.getParameter("tipoRel"))){
                    sql = sql +
                            "ORDER BY " +
                                "'Dt. Geração'";
                }
            }
            
            request.setAttribute("titulo", "Relatório de Taxas Individuais - Sócio");
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", quebra1);
            request.setAttribute("quebra2", quebra2);
            request.setAttribute("total1", total1);
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else if ("reciboPOS".equals(acao)){
            ArrayList<String> linhas = new ArrayList<String>();
            String linha = "";
            linhas.add("IATE CLUBE DE BRASILIA");
            linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
            linhas.add("CGC: 00 018 978/0001-80");
            linhas.add("-------------------------------------");
            linhas.add(" ");
            linhas.add("   ** COMPROVANTE DE CANCELAMENTO ** ");
            linhas.add("   **  DE AUTORIZAÇÃO DE DÉBITO ** ");
            linhas.add(" ");
            linhas.add("   Confirmamos o cancelamento do ");
            linhas.add("     valos abaixo discriminado ");
            linhas.add(" ");
            linhas.add(" ");
            TaxaIndividual t = TaxaIndividual.getInstance(Integer.parseInt(request.getParameter("id")));
            Socio s = Socio.getInstance(t.getMatricula(), t.getDependente(), t.getCategoria());
            Taxa tx = Taxa.getInstance(t.getTaxa());
            
            linha = " Cobrar de: " + s.getNome();
            if (linha.length()>38){
                linhas.add(linha.substring(0,38));
                linhas.add("            " + linha.substring(38));
            }else{
                linhas.add(linha);
            }
            linha = " Referente a: " + tx.getDescricao();
            if (linha.length()>38){
                linhas.add(linha.substring(0,38));
                linhas.add("              " + linha.substring(38));
            }else{
                linhas.add(linha);
            }
            DecimalFormat df = new DecimalFormat( "#,##0.00" );
            linhas.add(" Valor: " + df.format(t.getValor()));
            DecimalFormat df2 = new DecimalFormat( "00" );
            linhas.add(" Seria cobrado a partir de: " + df2.format(t.getMes()) + '/' + t.getAno());
            
            linhas.add(" ");
            linhas.add(" ");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
            linhas.add(" Data: " + sdf.format(cal.getTime()));
            linhas.add(" ");
            
            JobMiniImpressora job = new JobMiniImpressora();
            job.texto = linhas;
            try {        
                Socket sock = new Socket(request.getRemoteAddr(), 49001);
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                out.writeObject(job);
                out.close();
                response.sendRedirect("c?app=6000");
            }catch(Exception ex) {
                request.setAttribute("app", "6020");
                request.setAttribute("msg", ex.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
        }else if ("reciboPRE".equals(acao)){
            ArrayList<String> linhas = new ArrayList<String>();
            String linha = "";
            linhas.add("IATE CLUBE DE BRASILIA");
            linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
            linhas.add("CGC: 00 018 978/0001-80");
            linhas.add("-------------------------------------");
            linhas.add(" ");
            linhas.add("   ** COMPROVANTE DE CANCELAMENTO ** ");
            linhas.add("   **      DE DÉBITO PRÉ-PAGO     ** ");
            linhas.add(" ");
            linhas.add("   Confirmamos o cancelamento do ");
            linhas.add("     valos abaixo discriminado ");
            linhas.add(" ");
            linhas.add(" ");
            TaxaIndividual t = TaxaIndividual.getInstancePre(Integer.parseInt(request.getParameter("id")));
            Taxa tx = Taxa.getInstance(t.getTaxa());
            
            if (t.getIdFuncionario()>0){
                Funcionario s = Funcionario.getInstance(t.getIdFuncionario());
                linha = " Cobrar de: " + s.getNome();
            }else{
                Socio s = Socio.getInstance(t.getMatricula(), t.getDependente(), t.getCategoria());
                linha = " Cobrar de: " + s.getNome();
            }
            
            if (linha.length()>38){
                linhas.add(linha.substring(0,38));
                linhas.add("            " + linha.substring(38));
            }else{
                linhas.add(linha);
            }
            linha = " Referente a: " + tx.getDescricao();
            if (linha.length()>38){
                linhas.add(linha.substring(0,38));
                linhas.add("              " + linha.substring(38));
            }else{
                linhas.add(linha);
            }
            DecimalFormat df = new DecimalFormat( "#,##0.00" );
            linhas.add(" Valor: " + df.format(t.getValor()));
            linhas.add(" Saldo atual: " + df.format(t.getSaldoPrePago()));
            
            linhas.add(" ");
            linhas.add(" ");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
            linhas.add(" Data: " + sdf.format(cal.getTime()));
            linhas.add(" ");
            
            JobMiniImpressora job = new JobMiniImpressora();
            job.texto = linhas;
            try {        
                Socket sock = new Socket(request.getRemoteAddr(), 49001);
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                out.writeObject(job);
                out.close();
                response.sendRedirect("c?app=6000");
            }catch(Exception ex) {
                request.setAttribute("app", "6020");
                request.setAttribute("msg", ex.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
        }else{
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
            String teste = request.getParameter("lstUsuario");

            String sql = " SELECT * FROM tb_taxa_administrativa where ind_taxa_administrativa = 'I' "+
                         " AND CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "')  order by 2";
            request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));

            sql = " select USER_ACESSO_SISTEMA, NOME_USUARIO_SISTEMA from TB_Usuario_Sistema "+
                  " where ISNUMERIC(USER_ACESSO_SISTEMA) = 0 order by 2";
            request.setAttribute("usuarios", ComboBoxLoader.listarSql(sql));

            if (request.getParameter("dataInicio")==null){
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
                request.setAttribute("dataInicio", sdf.format(cal.getTime()));
                request.setAttribute("dataFim", sdf.format(cal.getTime()));
            }
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            request.setAttribute("dtAtual", sdf.format(cal.getTime()));
            
            request.setAttribute("usuario", request.getUserPrincipal().getName());
            request.getRequestDispatcher("/pages/6190-lista.jsp").forward(request, response);
        }    
    }
    
    @App("6050")
    public static void rel6050(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        if ("imprimir".equals(acao)){
            String sql = "";
            String titulo2 = "";
            
            if (request.getParameter("dataInicio").equals(request.getParameter("dataFim"))){
                if ("REC".equals(request.getParameter("periodo"))) {
                    titulo2 = "RECEBIMENTOS NO DIA " + request.getParameter("dataInicio");
                }else{
                    titulo2 = "DEPÓSITO NO DIA " + request.getParameter("dataInicio");
                }
            }else{
                if ("REC".equals(request.getParameter("periodo"))) {
                    titulo2 = "RECEBIMENTOS NO PERÍODO DE " + request.getParameter("dataInicio") + " A " + request.getParameter("dataFim");
                }else{
                    titulo2 = "DEPÓSITOS NO PERÍODO DE " + request.getParameter("dataInicio") + " A " + request.getParameter("dataFim");
                }
            }

            String dtIni = request.getParameter("dataInicio");
            String dtFim = request.getParameter("dataFim");
            sql = "EXEC SP_REC_CHEQUES_PERIODO '";
            sql = sql + dtIni.substring(3,6)+dtIni.substring(0,3)+dtIni.substring(6)+ " 00:00:00', '";
            sql = sql + dtFim.substring(3,6)+dtFim.substring(0,3)+dtFim.substring(6) + " 23:59:59', '";
            sql = sql + request.getParameter("periodo") + "', 'R'";

            request.setAttribute("titulo", "RELAÇÃO DE CHEQUES RECEBIDOS");
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", true);
            request.setAttribute("quebra2", false);
            request.setAttribute("total1", "8");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
            
            
        }else{
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));

            if (request.getParameter("dataInicio")==null){
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
                request.setAttribute("dataInicio", sdf.format(cal.getTime()));
                request.setAttribute("dataFim", sdf.format(cal.getTime()));
            }

            request.getRequestDispatcher("/pages/6050-lista.jsp").forward(request, response);
        }    
    }

    @App("6060")
    public static void movimentoCaixa(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        if ("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            int idCaixa = Integer.parseInt(request.getParameter("idCaixa"));
            int idTransacao = Integer.parseInt(request.getParameter("idTransacao"));
            boolean formaPag = Boolean.parseBoolean(request.getParameter("formaPag"));
            
            sql.append("EXEC SP_REC_MOV_CAIXA_DIAS_TRANS '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00', '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59', ");
            sql.append(idCaixa);
            sql.append(", ");
            sql.append(idTransacao);
            sql.append(", ");
            if (formaPag){
                sql.append("'S'");
            }else{
                sql.append("'N'");
            }

            //faz a contagem de autenticações e estornos
            Connection cn = Pool.getInstance().getConnection();
            int autenticacoes = 0;
            int estornos = 0;
            try{
                ResultSet rs = cn.createStatement().executeQuery(sql.toString());
                while(rs.next()){
                    if (!rs.getString("TRANSACAO").equals("")){
                        autenticacoes++;
                        if(rs.getString("TRANSACAO").contains("ESTORNO")){
                            estornos++;
                        }
                    }
                }
            }catch(SQLException e){
                
            }finally{
                try {
                    cn.close();
                } catch (SQLException ex) {

                }
            }
            
            StringBuilder titulo2 = new StringBuilder();
            titulo2.append("Caixa: ");
            if(idCaixa == 0){
                titulo2.append("TODOS");
            }else{
                Funcionario f = Funcionario.getInstance(idCaixa);
                titulo2.append(f.getNome());
            }
            titulo2.append(" - Transações: ");
            if(idTransacao == 0){
                titulo2.append("TODAS");
            }else if(idTransacao < 0){
                titulo2.append("ESTORNOS");
            }else{
                Transacao t = Transacao.getInstance(idTransacao);
                titulo2.append(t.getDescricao());
            }
            titulo2.append(" - Autenticações: ");
            titulo2.append(autenticacoes);
            titulo2.append(" - Estornos: ");
            titulo2.append(estornos);
            
            request.setAttribute("titulo", "MOVIMENTO DE CAIXA " + request.getParameter("dataInicio")
                            + " - " + request.getParameter("dataFim"));
            request.setAttribute("titulo2", titulo2.toString());
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            if (formaPag){
                request.setAttribute("total1", "10");
            }else{
                request.setAttribute("total1", "8");
            }
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            request.setAttribute("dtAtual", sdf.format(cal.getTime()));
            
            request.setAttribute("caixas", ComboBoxLoader.listarSql("EXEC SP_RECUPERA_FUNC_CAIXA"));
            request.setAttribute("transacoes", ComboBoxLoader.listar("TB_TRANSACAO"));
            request.getRequestDispatcher("/pages/6060.jsp").forward(request, response);
        }
    }
    
    @App("6080")
    public static void contabil(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        if ("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            
            sql.append("EXEC SP_REL_CONTABIL '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00', '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59'");
            if (request.getParameter("tipo").equals("A")){
                sql.append(", 'A'");
            }else{
                sql.append(", 'S'");
            }

            //faz a contagem de transacoes
            Connection cn = Pool.getInstance().getConnection();
            int transacoes = 0;
            try{
                ResultSet rs = cn.createStatement().executeQuery(sql.toString());
                while(rs.next()){
                    transacoes++;
                }
            }catch(SQLException e){
                
            }finally{
                try {
                    cn.close();
                } catch (SQLException ex) {

                }
            }
            request.setAttribute("titulo", "RELATÓRIO CONTÁBIL");
            request.setAttribute("titulo2", "Movimento no Período de " + request.getParameter("dataInicio")
                            + " à " + request.getParameter("dataFim") + " - Transações: " + transacoes);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "true");
            request.setAttribute("total1", "4");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            request.setAttribute("dtAtual", sdf.format(cal.getTime()));
            
            request.getRequestDispatcher("/pages/6080.jsp").forward(request, response);
        }
    }
    
    @App("6110")
    public static void RecAtraso(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        if ("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            
            sql.append("EXEC SP_REL_RECEB_ATRASO '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00', '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59'");
            
            if (Boolean.parseBoolean(request.getParameter("txAdm"))){
                sql.append(", 'S'");
            }else{
                sql.append(", 'N'");
            }
            if (Boolean.parseBoolean(request.getParameter("txCurso"))){
                sql.append(", 'S'");
            }else{
                sql.append(", 'N'");
            }
            sql.append(", '"+request.getParameter("tipo")+"'");
                

            if (request.getParameter("tipo").equals("A")){
                request.setAttribute("titulo", "RELATÓRIO ANALÍTICO DE RECEBIMENTOS EM ABERTO");
            }else{
                request.setAttribute("titulo", "RELATÓRIO SINTÉTICO DE RECEBIMENTOS EM ABERTO");
            }
            
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "true");
            if (request.getParameter("tipo").equals("A")){
                request.setAttribute("total1", "5");
            }else{
                request.setAttribute("total1", "3");
            }
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            request.setAttribute("dtAtual", sdf.format(cal.getTime()));
            
            request.getRequestDispatcher("/pages/6110.jsp").forward(request, response);
        }
    }
    
    @App("6170")
    public static void produtosServicos(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        if ("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            int idCusto = Integer.parseInt(request.getParameter("idCusto"));
            int idProduto = Integer.parseInt(request.getParameter("idProduto"));
            
            sql.append("EXEC SP_REL_PROD_SERV '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00', '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59', ");            
            sql.append(idCusto);
            sql.append(", '");
            sql.append(request.getParameter("tipo"));
            sql.append("', ");
            sql.append(idProduto);
            sql.append(", '");
            sql.append(request.getUserPrincipal().getName());            
            sql.append("'");

            //faz a contagem de autenticações e estornos
            Connection cn = Pool.getInstance().getConnection();
            int autenticacoes = 0;
            int estornos = 0;
            try{
                ResultSet rs = cn.createStatement().executeQuery(sql.toString());
                while(rs.next()){
                    autenticacoes++;
                    if(rs.getString("Nome").contains("ESTORNO")){
                        estornos++;
                    }
                }
            }catch(SQLException e){
                
            }finally{
                try {
                    cn.close();
                } catch (SQLException ex) {

                }
            }
            
            StringBuilder titulo2 = new StringBuilder();
            titulo2.append("Centro de Custos: ");
            if(idCusto == 0){
                titulo2.append("TODOS");
            }else{
                CentroCusto c = CentroCusto.getInstance(idCusto);
                titulo2.append(c.getDescricao());
            }
            titulo2.append(" - Produtos e Serviços: ");
            if(idProduto == 0){
                titulo2.append("TODOS");
            }else if(idProduto == -1){
                titulo2.append("SOMENTE OS QUE GERAM CRÉDITO AO IATE");
            }else if(idProduto == -2){
                titulo2.append("SOMENTE OS QUE NÃO GERAM CRÉDITO AO IATE");                
            }else{
                ProdutoServico p = ProdutoServico.getInstance(idProduto);
                titulo2.append(p.getDescricao());
            }
            titulo2.append(" - Autenticações: ");
            titulo2.append(autenticacoes);
            titulo2.append(" - Estornos: ");
            titulo2.append(estornos);
            
            request.setAttribute("titulo", "MOVIMENTO DE CAIXA " + request.getParameter("dataInicio")
                            + " - " + request.getParameter("dataFim"));
            request.setAttribute("titulo2", titulo2.toString());
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "7");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            request.setAttribute("dtAtual", sdf.format(cal.getTime()));
            
            String sql = "SELECT t1.CD_TRANSACAO, t1.DESCR_TRANSACAO "
                + " FROM TB_TRANSACAO T1, TB_USUARIO_CENTRO_CUSTO T2 "
                + " WHERE T1.CD_TRANSACAO = T2.CD_TRANSACAO AND T1.CD_DEBITO_CREDITO = 'P' AND T2.USER_ACESSO_SISTEMA = '"
                + request.getUserPrincipal().getName() + "'";
            request.setAttribute("custos", ComboBoxLoader.listarSql(sql));
            request.setAttribute("produtos", ComboBoxLoader.listar("TB_PRODUTO_SERVICO"));
            request.getRequestDispatcher("/pages/6170.jsp").forward(request, response);
        }
    }
    
    @App("6280")
    public static void saldoComprometido(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int idCategoria = 0;
        int matricula = 0;
        try{
            idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        }catch(NumberFormatException e){
            idCategoria = 0;
        }

        try{
            matricula = Integer.parseInt(request.getParameter("matricula"));
        }catch(NumberFormatException e){
            matricula = 0;
        }
        request.setAttribute("matricula", matricula);        
        request.setAttribute("idCategoria", idCategoria);        
        request.setAttribute("saldoComprometido", SaldoComprometido.verificar(idCategoria, matricula, 0f, "6280", ""));        
        request.getRequestDispatcher("/pages/6280.jsp").forward(request, response);
    }

    @App("6210")
    public static void taxaIndividualSintetico(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            boolean agrupado = Boolean.parseBoolean(request.getParameter("agrupado"));
            boolean contaPre = Boolean.parseBoolean(request.getParameter("contaPre"));
            boolean contaPos = Boolean.parseBoolean(request.getParameter("contaPos"));
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            String titulo2;
            if(request.getParameter("tipoPeriodo").equals("G")){
                titulo2 = "Data de Geração: ";
            }else{
                titulo2 = "Data de Cobrança: ";
            }
                
            if (contaPos){
                sql.append("SELECT 'PÓS-PAGO' TIPO, T2.DESCR_TX_ADMINISTRATIVA AS Taxa, ");
                if(agrupado){
                    sql.append("T3.MM_COBRANCA AS Mes, ");
                    sql.append("T3.AA_COBRANCA AS Ano, ");
                }  
                sql.append("SUM(T3.VR_TAXA) AS Valor ");
                sql.append("FROM ");
                sql.append("TB_PESSOA T1, ");
                sql.append("TB_TAXA_ADMINISTRATIVA T2, ");
                sql.append("TB_VAL_TAXA_INDIVIDUAL T3 ");
                sql.append("WHERE ");
                sql.append("T1.CD_MATRICULA = T3.CD_MATRICULA AND ");
                sql.append("T1.CD_CATEGORIA = T3.CD_CATEGORIA AND ");
                sql.append("T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND ");
                sql.append("T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA ");
                sql.append(" AND T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '");
                sql.append(request.getUserPrincipal().getName());
                sql.append("') ");

                try{
                    int idCategoria = Integer.parseInt(request.getParameter("categoria"));
                    sql.append(" AND T1.CD_CATEGORIA = ");
                    sql.append(idCategoria);
                }catch(NumberFormatException e){

                }
                try{
                    int matricula = Integer.parseInt(request.getParameter("matricula"));
                    sql.append(" AND T1.CD_MATRICULA = ");
                    sql.append(matricula);
                }catch(NumberFormatException e){

                }

                String nome = request.getParameter("nome");
                if(nome != null && !nome.trim().equals("")){
                    sql.append(" AND T1.NOME_PESSOA LIKE '");
                    sql.append(nome);
                    sql.append("%'");
                }

                int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
                if(idTaxa > 0){
                    sql.append(" AND T2.CD_TX_ADMINISTRATIVA = ");
                    sql.append(idTaxa);
                }

                sql.append(" AND T3.IC_SITUACAO_TAXA = '");
                sql.append(request.getParameter("situacao"));
                sql.append("' ");

                if(request.getParameter("tipoPeriodo").equals("G")){
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    sql.append(" AND T3.DT_GERACAO_TAXA >= '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append(" AND T3.DT_GERACAO_TAXA <= '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59'");
                }else{
                    titulo2 = "Data de Cobrança: ";
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
                    sql.append(" AND CONVERT(VARCHAR, T3.AA_COBRANCA) + RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) >= '");
                    sql.append(fmt.format(d1));
                    sql.append("'");
                    sql.append(" AND CONVERT(VARCHAR, T3.AA_COBRANCA) + RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) <= '");
                    sql.append(fmt.format(d2));
                    sql.append("'");
                }

                boolean pago = Boolean.parseBoolean(request.getParameter("pago"));
                if(pago){
                    sql.append(" AND EXISTS ");
                    sql.append("( ");
                    sql.append("SELECT ");
                    sql.append("1 ");
                    sql.append("From ");
                    sql.append("VW_CARNE_PG T4 ");
                    sql.append("Where ");
                    sql.append("T1.CD_MATRICULA      = T4.CD_MATRICULA          AND ");
                    sql.append("T1.CD_CATEGORIA      = T4.CD_CATEGORIA          AND ");
                    sql.append("T3.MM_COBRANCA    = T4.MM_VENCIMENTO            AND ");
                    sql.append("T3.AA_COBRANCA = T4.AA_VENCIMENTO ");
                    sql.append(")");
                }

                sql.append(" GROUP  BY  T2.DESCR_TX_ADMINISTRATIVA ");
                if(agrupado){
                   sql.append(", T3.MM_COBRANCA, T3.AA_COBRANCA "); 
                }
            }
            if (contaPre && contaPos){
                sql.append(" UNION ALL ");
            }
            if (contaPre){
                sql.append("SELECT 'PRÉ-PAGO' TIPO, T2.DESCR_TX_ADMINISTRATIVA AS Taxa, ");
                if(agrupado){
                    sql.append("MONTH(T3.DT_MOVIMENTO) AS Mes, ");
                    sql.append("YEAR(T3.DT_MOVIMENTO) AS Ano, ");
                }  
                sql.append("SUM(T3.VR_MOVIMENTO) AS Valor ");
                sql.append("FROM ");
                sql.append("TB_PESSOA T1, ");
                sql.append("TB_TAXA_ADMINISTRATIVA T2, ");
                sql.append("TB_VAL_PRE_PAGO T3 ");
                sql.append("WHERE ");
                sql.append("T1.CD_MATRICULA = T3.CD_MATRICULA AND ");
                sql.append("T1.CD_CATEGORIA = T3.CD_CATEGORIA AND ");
                sql.append("T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND ");
                sql.append("T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA ");
                sql.append(" AND T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '");
                sql.append(request.getUserPrincipal().getName());
                sql.append("') ");

                try{
                    int idCategoria = Integer.parseInt(request.getParameter("categoria"));
                    sql.append(" AND T1.CD_CATEGORIA = ");
                    sql.append(idCategoria);
                }catch(NumberFormatException e){

                }
                try{
                    int matricula = Integer.parseInt(request.getParameter("matricula"));
                    sql.append(" AND T1.CD_MATRICULA = ");
                    sql.append(matricula);
                }catch(NumberFormatException e){

                }

                String nome = request.getParameter("nome");
                if(nome != null && !nome.trim().equals("")){
                    sql.append(" AND T1.NOME_PESSOA LIKE '");
                    sql.append(nome);
                    sql.append("%'");
                }

                int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
                if(idTaxa > 0){
                    sql.append(" AND T2.CD_TX_ADMINISTRATIVA = ");
                    sql.append(idTaxa);
                }

                sql.append(" AND T3.IC_SIT_MOVIMENTO = '");
                sql.append(request.getParameter("situacao"));
                sql.append("' ");

                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                sql.append(" AND T3.DT_MOVIMENTO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00'");
                sql.append(" AND T3.DT_MOVIMENTO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59'");
                
                sql.append(" GROUP  BY  T2.DESCR_TX_ADMINISTRATIVA ");
                if(agrupado){
                   sql.append(", MONTH(T3.DT_MOVIMENTO), YEAR(T3.DT_MOVIMENTO) "); 
                }
            }
            
            sql.append(" ORDER BY 1, T2.DESCR_TX_ADMINISTRATIVA");

            request.setAttribute("titulo", "Relatório de Taxas Individuais (Sintético) - Sócio");
            request.setAttribute("titulo2", titulo2 + request.getParameter("dataInicio")
                            + " até " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            if(agrupado){
                request.setAttribute("total1", "5");
            }else{
                request.setAttribute("total1", "3");
            }
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            request.setAttribute("dtAtual", sdf.format(cal.getTime()));
            
            String sql = "SELECT CD_TX_ADMINISTRATIVA, DESCR_TX_ADMINISTRATIVA FROM tb_taxa_administrativa where ind_taxa_administrativa = 'I' "
                + "AND CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '"
                + request.getUserPrincipal().getName()
                + "') ORDER BY 2  ";
            request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));
            request.getRequestDispatcher("/pages/6210.jsp").forward(request, response);            
        }
    }
    
    @App("6220")
    public static void taxaIndividualSituacaoCarne(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            Date d3 = Datas.parse(request.getParameter("vencimentoInicio"));
            Date d4 = Datas.parse(request.getParameter("vencimentoFim"));            
            String titulo2 = "Analítico";
            String total1 = "3";
            if(request.getParameter("tipo").equals("A")){  
                sql.append("SELECT ");
                sql.append("RIGHT('00' + CONVERT(VARCHAR, T1.CD_CATEGORIA), 2) + '/' + ");
                sql.append("RIGHT('0000' + CONVERT(VARCHAR, T1.CD_MATRICULA), 4) + '-' + ");
                sql.append("RIGHT('0000' + CONVERT(VARCHAR, T1.SEQ_DEPENDENTE), 2) + ' - ' + ");
                sql.append("T1.NOME_PESSOA AS Pessoa, ");
                sql.append("T2.DESCR_TX_ADMINISTRATIVA as Taxa, ");
                sql.append("T3.VR_TAXA as Valor, ");
                sql.append("T3.DT_GERACAO_TAXA as 'Dt. Geracao', ");
                sql.append("RIGHT('00' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) + '/' + ");
                sql.append("CONVERT(VARCHAR, T3.AA_COBRANCA) as 'Cobrar em' ");
                sql.append("FROM ");
                sql.append("TB_PESSOA T1, ");
                sql.append("TB_TAXA_ADMINISTRATIVA T2, ");
                sql.append("TB_VAL_TAXA_INDIVIDUAL T3 ");
                sql.append("WHERE ");
                sql.append("T1.CD_MATRICULA = T3.CD_MATRICULA AND ");
                sql.append("T1.CD_CATEGORIA = T3.CD_CATEGORIA AND ");
                sql.append("T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND ");
                sql.append("T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA AND ");
                sql.append("T3.IC_SITUACAO_TAXA = 'N' ");
                sql.append(" AND T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '");
                sql.append(request.getUserPrincipal().getName());
                sql.append("') ");

                try{
                    int idCategoria = Integer.parseInt(request.getParameter("categoria"));
                    sql.append(" AND T1.CD_CATEGORIA = ");
                    sql.append(idCategoria);
                }catch(NumberFormatException e){

                }
                try{
                    int matricula = Integer.parseInt(request.getParameter("matricula"));
                    sql.append(" AND T1.CD_MATRICULA = ");
                    sql.append(matricula);
                }catch(NumberFormatException e){

                }

                String nome = request.getParameter("nome");
                if(nome != null && !nome.trim().equals("")){
                    sql.append(" AND T1.NOME_PESSOA LIKE '");
                    sql.append(nome);
                    sql.append("%'");
                }

                int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
                if(idTaxa > 0){
                    sql.append(" AND T2.CD_TX_ADMINISTRATIVA = ");
                    sql.append(idTaxa);
                }

                if(request.getParameter("tipoPeriodo").equals("G")){
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    sql.append(" AND T3.DT_GERACAO_TAXA >= '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append(" AND T3.DT_GERACAO_TAXA <= '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59'");
                }else{
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
                    sql.append(" AND CONVERT(VARCHAR, T3.AA_COBRANCA) + RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) >= '");
                    sql.append(fmt.format(d1));
                    sql.append("'");
                    sql.append(" AND CONVERT(VARCHAR, T3.AA_COBRANCA) + RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) <= '");
                    sql.append(fmt.format(d2));
                    sql.append("'");
                }

                String carne = request.getParameter("carne");
                if(carne.equals("S")){
                    sql.append(" AND NOT EXISTS ");
                    sql.append("( ");
                    sql.append("SELECT ");
                    sql.append("1 ");
                    sql.append("FROM ");
                    sql.append("TB_PARCELA_ADMINISTRATIVA T4 ");
                    sql.append("WHERE ");
                    sql.append("T3.NU_SEQ_TAXA_INDIVIDUAL      = T4.NU_SEQ_TAXA_INDIVIDUAL");
                    sql.append(")");
                }else{
                    sql.append(" AND EXISTS ");
                    sql.append("( ");
                    sql.append("SELECT ");
                    sql.append("1 ");
                    sql.append("FROM ");
                    sql.append("TB_PARCELA_ADMINISTRATIVA T4 ");
                    sql.append("WHERE ");
                    sql.append("T3.NU_SEQ_TAXA_INDIVIDUAL      = T4.NU_SEQ_TAXA_INDIVIDUAL AND ");
                    sql.append("T4.CD_SIT_PARC_ADM = '");
                    sql.append(carne);
                    sql.append("'");

                    if(d3 != null){
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                        sql.append(" AND T4.DT_VCTO_PARC_ADM >= '");
                        sql.append(fmt.format(d3));
                        sql.append(" 00:00:00'");
                    }
                    if(d4 != null){
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                        sql.append(" AND T4.DT_VCTO_PARC_ADM <= '");
                        sql.append(fmt.format(d4));
                        sql.append(" 23:59:59'");
                    }

                    sql.append(")");
                }

                sql.append(" ORDER BY T3.DT_GERACAO_TAXA");
   
            }else{
                titulo2 = "Sintético";
                total1 = "2";
                sql.append("SELECT ");
                sql.append("T2.DESCR_TX_ADMINISTRATIVA as Taxa, ");
                sql.append("SUM(T3.VR_TAXA) AS Valor ");
                sql.append("FROM ");
                sql.append("TB_PESSOA T1, ");
                sql.append("TB_TAXA_ADMINISTRATIVA T2, ");
                sql.append("TB_VAL_TAXA_INDIVIDUAL T3 ");
                sql.append("WHERE ");
                sql.append("T1.CD_MATRICULA = T3.CD_MATRICULA AND ");
                sql.append("T1.CD_CATEGORIA = T3.CD_CATEGORIA AND ");
                sql.append("T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND ");
                sql.append("T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA AND ");
                sql.append("T3.IC_SITUACAO_TAXA = 'N' ");
                sql.append(" AND T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '");
                sql.append(request.getUserPrincipal().getName());
                sql.append("') ");

                try{
                    int idCategoria = Integer.parseInt(request.getParameter("categoria"));
                    sql.append(" AND T1.CD_CATEGORIA = ");
                    sql.append(idCategoria);
                }catch(NumberFormatException e){

                }
                try{
                    int matricula = Integer.parseInt(request.getParameter("matricula"));
                    sql.append(" AND T1.CD_MATRICULA = ");
                    sql.append(matricula);
                }catch(NumberFormatException e){

                }

                String nome = request.getParameter("nome");
                if(nome != null && !nome.trim().equals("")){
                    sql.append(" AND T1.NOME_PESSOA LIKE '");
                    sql.append(nome);
                    sql.append("%'");
                }

                int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
                if(idTaxa > 0){
                    sql.append(" AND T2.CD_TX_ADMINISTRATIVA = ");
                    sql.append(idTaxa);
                }

                if(request.getParameter("tipoPeriodo").equals("G")){
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                    sql.append(" AND T3.DT_GERACAO_TAXA >= '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append(" AND T3.DT_GERACAO_TAXA <= '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59'");
                }else{
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyyMM");
                    sql.append(" AND CONVERT(VARCHAR, T3.AA_COBRANCA) + RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) >= '");
                    sql.append(fmt.format(d1));
                    sql.append("'");
                    sql.append(" AND CONVERT(VARCHAR, T3.AA_COBRANCA) + RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) <= '");
                    sql.append(fmt.format(d2));
                    sql.append("'");
                }

                String carne = request.getParameter("carne");
                if(carne.equals("S")){
                    sql.append(" AND NOT EXISTS ");
                    sql.append("( ");
                    sql.append("SELECT ");
                    sql.append("1 ");
                    sql.append("FROM ");
                    sql.append("TB_PARCELA_ADMINISTRATIVA T4 ");
                    sql.append("WHERE ");
                    sql.append("T3.NU_SEQ_TAXA_INDIVIDUAL      = T4.NU_SEQ_TAXA_INDIVIDUAL");
                    sql.append(")");
                }else{
                    sql.append(" AND EXISTS ");
                    sql.append("( ");
                    sql.append("SELECT ");
                    sql.append("1 ");
                    sql.append("FROM ");
                    sql.append("TB_PARCELA_ADMINISTRATIVA T4 ");
                    sql.append("WHERE ");
                    sql.append("T3.NU_SEQ_TAXA_INDIVIDUAL      = T4.NU_SEQ_TAXA_INDIVIDUAL AND ");
                    sql.append("T4.CD_SIT_PARC_ADM = '");
                    sql.append(carne);
                    sql.append("'");

                    if(d3 != null){
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                        sql.append(" AND T4.DT_VCTO_PARC_ADM >= '");
                        sql.append(fmt.format(d3));
                        sql.append(" 00:00:00'");
                    }
                    if(d4 != null){
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                        sql.append(" AND T4.DT_VCTO_PARC_ADM <= '");
                        sql.append(fmt.format(d4));
                        sql.append(" 23:59:59'");
                    }

                    sql.append(")");
                }

                sql.append(" GROUP BY T2.DESCR_TX_ADMINISTRATIVA");
                sql.append(" ORDER BY T2.DESCR_TX_ADMINISTRATIVA");
            }
            request.setAttribute("titulo", "Relatório de Taxas Individuais por Situação do Carnê");
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", total1);
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            request.setAttribute("dtAtual", sdf.format(cal.getTime()));
            
            String sql = "SELECT CD_TX_ADMINISTRATIVA, DESCR_TX_ADMINISTRATIVA FROM tb_taxa_administrativa where ind_taxa_administrativa = 'I' "
                + "AND CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '"
                + request.getUserPrincipal().getName()
                + "') ORDER BY 2";
            request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));
            request.getRequestDispatcher("/pages/6220.jsp").forward(request, response);
        }
    }
    
    @App("6300")
    public static void rel6300(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        if ("imprimir".equals(acao)){
            String sql = "";
            String titulo2 = "";
            boolean quebra1 = false;
            boolean quebra2 = false;
            int total1 = 3;
            String dtIni = request.getParameter("dataInicio");
            String dtFim = request.getParameter("dataFim");
            
            if (Boolean.parseBoolean(request.getParameter("agruparUsuario")) && "A".equals(request.getParameter("tipoRel"))){
                quebra1 = true;
                quebra2 = true;
                total1++;
            }else{
                if (Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                    quebra1 = true;    
                    total1++;
                }
                if ("A".equals(request.getParameter("tipoRel"))){
                    quebra1 = true;    
                }else{
                    total1--;
                }
            }
            
            titulo2 = "Data de Geração de " + dtIni + " até " + dtFim;

            if ("A".equals(request.getParameter("tipoRel"))){
                sql = sql + 
                      "SELECT ";
                if (Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                    sql = sql + "UPPER(T3.USER_INCLUSAO) as 'Incl. por', ";
                }
                sql = sql +         
                         "T1.NOME_FUNCIONARIO as 'Nome', " +
                         "CONVERT(VARCHAR(25), T2.DESCR_TX_ADMINISTRATIVA) as 'Taxa', " +
                         "T3.VR_MOVIMENTO as 'Valor', " +
                         "T3.DT_MOVIMENTO as 'Dt. Geração', " +
                         "T3.IC_SIT_MOVIMENTO as 'Sit.', ";
                if (!Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                    sql = sql + "UPPER(T3.USER_INCLUSAO) as 'Incl. por', ";
                }
                sql = sql +         
                         "T3.DT_CANCELAMENTO as 'Dt. Canc.', " +
                         "T3.USER_CANCELAMENTO as 'Canc. por', " +
                         "T3.NU_SEQ_PRE_PAGO as 'Id' ";
            }else{
                sql = sql + 
                      "SELECT ";
                if (Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                    sql = sql + "UPPER(T3.USER_INCLUSAO) as 'Incl. por', ";
                }
                sql = sql +         
                         "T1.NOME_FUNCIONARIO as 'Nome', " +
                         "T3.VR_MOVIMENTO as 'Valor' ";
            }


            sql = sql +
                   "FROM " +
                        "TB_FUNCIONARIO T1, " +
                        "TB_TAXA_ADMINISTRATIVA T2, " +
                        "TB_VAL_PRE_PAGO T3 " +
                   "WHERE " +
                        "T1.CD_FUNCIONARIO = T3.CD_FUNCIONARIO AND " +
                        "T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA ";

            sql = sql + " AND T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "') ";

            if (!"".equals(request.getParameter("nome"))){
                sql = sql + " AND T1.NOME_FUNCIONARIO like '" + request.getParameter("nome") + "%'";
            }    
            if (!"0".equals(request.getParameter("taxa"))){
                sql = sql + " AND T2.CD_TX_ADMINISTRATIVA = " + request.getParameter("taxa");
            }    
            if (!"".equals(request.getParameter("lstUsuario"))){
                sql = sql + " AND T3.USER_INCLUSAO = '" + request.getParameter("lstUsuario") + "'";
            }    

            if (Boolean.parseBoolean(request.getParameter("normal"))){
                if (!Boolean.parseBoolean(request.getParameter("cancelada"))){
                    sql = sql + " AND T3.IC_SIT_MOVIMENTO = 'N'";
                }
            }else{
                sql = sql + " AND T3.IC_SIT_MOVIMENTO = 'C'";
            }

            sql = sql + " AND T3.DT_MOVIMENTO >= '" + dtIni.substring(3,6)+dtIni.substring(0,3)+dtIni.substring(6)+ " 00:00:00'";
            sql = sql + " AND T3.DT_MOVIMENTO <= '" + dtFim.substring(3,6)+dtFim.substring(0,3)+dtFim.substring(6) + " 23:59:59'";
            
            if (Boolean.parseBoolean(request.getParameter("agruparUsuario"))){
                sql = sql +
                        "ORDER BY " +
                            "'Incl. por' ";
                
                if ("A".equals(request.getParameter("tipoRel"))){
                    sql = sql +
                            ", 'Dt. Geração'";
                }
                
            }else{
                if ("A".equals(request.getParameter("tipoRel"))){
                    sql = sql +
                            "ORDER BY " +
                                "'Dt. Geração'";
                }
            }
            
            request.setAttribute("titulo", "Relatório de Taxas Individuais - Funcionário");
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", quebra1);
            request.setAttribute("quebra2", quebra2);
            request.setAttribute("total1", total1);
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
            

        }else{
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));

            String sql = " SELECT * FROM tb_taxa_administrativa where ind_taxa_administrativa = 'I' "+
                         " AND CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "')  order by 2";
            request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));
            
            sql = " select USER_ACESSO_SISTEMA, NOME_USUARIO_SISTEMA from TB_Usuario_Sistema "+
                  " where ISNUMERIC(USER_ACESSO_SISTEMA) = 0 order by 2";
            request.setAttribute("usuarios", ComboBoxLoader.listarSql(sql));

            if (request.getParameter("dataInicio")==null){
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
                request.setAttribute("dataInicio", sdf.format(cal.getTime()));
                request.setAttribute("dataFim", sdf.format(cal.getTime()));
            }
            
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            request.setAttribute("dtAtual", sdf.format(cal.getTime()));
            
            request.setAttribute("usuario", request.getUserPrincipal().getName());
            request.getRequestDispatcher("/pages/6300.jsp").forward(request, response);
        }    
    }


    @App("6310")
    public static void taxaIndividualSinteticoFunc(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            boolean agrupado = Boolean.parseBoolean(request.getParameter("agrupado"));
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            String titulo2;
            titulo2 = "Data de Geração: ";
                

            sql.append("SELECT 'PRÉ-PAGO' TIPO, T2.DESCR_TX_ADMINISTRATIVA AS Taxa, ");
            if(agrupado){
                sql.append("MONTH(T3.DT_MOVIMENTO) AS Mes, ");
                sql.append("YEAR(T3.DT_MOVIMENTO) AS Ano, ");
            }  
            sql.append("SUM(T3.VR_MOVIMENTO) AS Valor ");
            sql.append("FROM ");
            sql.append("TB_FUNCIONARIO T1, ");
            sql.append("TB_TAXA_ADMINISTRATIVA T2, ");
            sql.append("TB_VAL_PRE_PAGO T3 ");
            sql.append("WHERE ");
            sql.append("T1.CD_FUNCIONARIO = T3.CD_FUNCIONARIO AND ");
            sql.append("T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA ");
            sql.append(" AND T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '");
            sql.append(request.getUserPrincipal().getName());
            sql.append("') ");

            String nome = request.getParameter("nome");
            
            if(nome != null && !nome.trim().equals("")){
                sql.append(" AND T1.NOME_PESSOA LIKE '");
                sql.append(nome);
                sql.append("%'");
            }

            int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
            if(idTaxa > 0){
                sql.append(" AND T2.CD_TX_ADMINISTRATIVA = ");
                sql.append(idTaxa);
            }

            sql.append(" AND T3.IC_SIT_MOVIMENTO = '");
            sql.append(request.getParameter("situacao"));
            sql.append("' ");

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            sql.append(" AND T3.DT_MOVIMENTO >= '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00'");
            sql.append(" AND T3.DT_MOVIMENTO <= '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59'");

            sql.append(" GROUP  BY  T2.DESCR_TX_ADMINISTRATIVA ");
            if(agrupado){
               sql.append(", MONTH(T3.DT_MOVIMENTO), YEAR(T3.DT_MOVIMENTO) "); 
            }
            
            sql.append(" ORDER BY 1, T2.DESCR_TX_ADMINISTRATIVA");

            request.setAttribute("titulo", "Relatório de Taxas Individuais (Sintético) - Funcionário");
            request.setAttribute("titulo2", titulo2 + request.getParameter("dataInicio")
                            + " até " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            if(agrupado){
                request.setAttribute("total1", "5");
            }else{
                request.setAttribute("total1", "3");
            }
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            request.setAttribute("dtAtual", sdf.format(cal.getTime()));
            
            String sql = "SELECT CD_TX_ADMINISTRATIVA, DESCR_TX_ADMINISTRATIVA FROM tb_taxa_administrativa where ind_taxa_administrativa = 'I' "
                + "AND CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '"
                + request.getUserPrincipal().getName()
                + "') ORDER BY 2  ";
            request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));
            request.getRequestDispatcher("/pages/6310.jsp").forward(request, response);            
        }
    }
    
    @App("6320")
    public static void prePagoSaldoGeral(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("visualizar".equals(acao)){
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy");
            
            boolean funcionario = Boolean.parseBoolean(request.getParameter("funcionario"));
            boolean socio = Boolean.parseBoolean(request.getParameter("socio"));
            Date d1 = Datas.parse(request.getParameter("dtReferencia"));
            
            StringBuilder sql = new StringBuilder();

            sql.append("EXEC SP_REL_SALDO_PREPAGO '");
            sql.append(fmt.format(d1));
            sql.append(" 23:59:59', '");
            if (socio){
                sql.append("S', '");
            }else{
                sql.append("N', '");
            }
            if (funcionario){
                sql.append("S'");
            }else{
                sql.append("N'");
            }
            
            request.setAttribute("titulo", "Relatório de Saldo Pré-Pago");
            request.setAttribute("titulo2", "Data de Referência " + fmt2.format(d1));
            
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "3");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            request.setAttribute("origem", request.getParameter("origem"));
            request.getRequestDispatcher("/pages/6320.jsp").forward(request, response);            
        }
    }
}




