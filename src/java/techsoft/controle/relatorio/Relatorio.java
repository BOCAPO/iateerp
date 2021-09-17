package techsoft.controle.relatorio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.util.Datas;

@Controller
public class Relatorio {
    
    @App("1400")
    public static void documentosEmitidos(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            String relatorio = request.getParameter("imprimir");
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            boolean sintetico = request.getParameter("tipo").equals("S");
            
            String titulo = null;
            String titulo2 = null;
            boolean quebra1 = false;
            boolean quebra2 = false;
            int total1 = -1;
            int total2 = -1;
            int total3 = -1;
            int total4 = -1;
            
            if(relatorio.equals("carteira")){
                if(sintetico){
                    sql.append("Select T1.DESCR_CATEGORIA as 'CATEGORIA',");
                    sql.append("       T2.USER_EMITIU_CARTEIRA AS 'FUNCIONÁRIO', ");
                    sql.append("       COUNT(*) AS 'QUANTIDADE' ");
                    sql.append("From   TB_HIST_EMISSAO_CARTEIRA T2,");
                    sql.append("       TB_CATEGORIA T1 ");
                    sql.append("Where  T1.CD_CATEGORIA = T2.CD_CATEGORIA");
                    sql.append("       AND T1.CD_CATEGORIA IN (");
                    for(String s : request.getParameterValues("categorias")){
                        sql.append(s);
                        sql.append(", ");
                    }
                    sql.delete(sql.length() -2, sql.length());
                    sql.append(") ");
                    sql.append("       AND T2.DT_EMISSAO BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append("       AND '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                    sql.append("Group  BY T1.DESCR_CATEGORIA, T2.USER_EMITIU_CARTEIRA");

                    titulo = "Quantidade de Carteirinhas emitidas";
                    titulo2 = "Período  " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim");
                    quebra1 = true;
                    quebra2 = false;
                    total1 = 2;
                    total2 = -1;
                    total3 = -1;
                    total4 = -1;
                }else{
                    sql.append("Select T1.DESCR_CATEGORIA as 'CATEGORIA',");
                    sql.append("       T3.NOME_PESSOA AS 'NOME', ");
                    sql.append("       T3.CD_MATRICULA AS 'TITULO', ");
                    sql.append("       T2.USER_EMITIU_CARTEIRA AS 'FUNCIONÁRIO', ");
                    sql.append("       T2.DT_EMISSAO AS 'Dt. Emissão' ");
                    sql.append("From   TB_HIST_EMISSAO_CARTEIRA T2,");
                    sql.append("       TB_CATEGORIA T1, TB_PESSOA T3 ");
                    sql.append("Where  T1.CD_CATEGORIA = T2.CD_CATEGORIA and t2.cd_matricula *= t3.cd_matricula and T2.CD_CATEGORIA *= T3.CD_CATEGORIA and T2.SEQ_DEPENDENTE *= T3.SEQ_DEPENDENTE ");
                    sql.append("       AND T1.CD_CATEGORIA IN (");
                    for(String s : request.getParameterValues("categorias")){
                        sql.append(s);
                        sql.append(", ");
                    }
                    sql.delete(sql.length() -2, sql.length());
                    sql.append(") ");                
                    sql.append("       AND T2.DT_EMISSAO BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append("       AND '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                    sql.append(" order by T1.DESCR_CATEGORIA, T2.DT_EMISSAO");

                    titulo = "Carteirinhas emitidas";
                    titulo2 = "Período  " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim");
                    quebra1 = true;
                    quebra2 = false;
                    total1 = -1;
                    total2 = -1;
                    total3 = -1;
                    total4 = -1;
                }
            }else if(relatorio.equals("convite")){
                if(sintetico){
                    sql.append("Select T1.DESCR_TIPO_CONVITE as 'TIPO CONVITE',");
                    sql.append("       T2.USER_ACESSO_SISTEMA AS 'FUNCIONÁRIO', ");
                    sql.append("       COUNT(*) AS 'QUANTIDADE' ");
                    sql.append("From   TB_CONVITE T2,");
                    sql.append("       TB_TIPO_CONVITE T1 ");
                    sql.append("Where  T1.CD_TIPO_CONVITE = T2.CD_CATEGORIA_CONVITE");
                    sql.append("       AND T1.CD_TIPO_CONVITE IN (");
                    for(String s : request.getParameterValues("tiposConvite")){
                        sql.append("'");
                        sql.append(s);
                        sql.append("', ");
                    }
                    sql.delete(sql.length() -2, sql.length());
                    sql.append(") ");
                    sql.append("       AND T2.DT_RETIRADA BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append("       AND '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                    sql.append("Group  BY T1.DESCR_TIPO_CONVITE, T2.USER_ACESSO_SISTEMA");
      
                    titulo = "Quantidades de Convites emitidos";
                    titulo2 = "Período  " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim");
                    quebra1 = true;
                    quebra2 = false;
                    total1 = 2;
                    total2 = -1;
                    total3 = -1;
                    total4 = -1;
                }else{
                    sql.append("Select T1.DESCR_TIPO_CONVITE as 'TIPO CONVITE',");
                    sql.append("       T2.NOME_SACADOR AS 'SACADOR', ");
                    sql.append("       T2.NOME_CONVIDADO 'CONVIDADO', ");
                    sql.append("       T2.DT_RETIRADA AS 'EMISSAO', ");
                    sql.append("       T2.USER_ACESSO_SISTEMA AS 'FUNCIONÁRIO' ");
                    sql.append("From   TB_CONVITE T2,");
                    sql.append("       TB_TIPO_CONVITE T1 ");
                    sql.append("Where  T1.CD_TIPO_CONVITE = T2.CD_CATEGORIA_CONVITE ");
                    sql.append("       AND T1.CD_TIPO_CONVITE IN (");
                    for(String s : request.getParameterValues("tiposConvite")){
                        sql.append("'");
                        sql.append(s);
                        sql.append("', ");
                    }
                    sql.delete(sql.length() -2, sql.length());
                    sql.append(") ");
                    sql.append("       AND T2.DT_RETIRADA BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append("       AND '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                    sql.append("ORDER BY    T1.DESCR_TIPO_CONVITE,  T2.DT_RETIRADA");

                    titulo = "Quantidades de Convites emitidos";
                    titulo2 = "Período  " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim");
                    quebra1 = true;
                    quebra2 = false;
                    total1 = -1;
                    total2 = -1;
                    total3 = -1;
                    total4 = -1;
                }

            }else if(relatorio.equals("passaporte")){
                if(sintetico){
                    sql.append("SELECT ");
                    sql.append("DESCR_CURSO AS CURSO, ");
                    sql.append("COUNT(*) AS QUANTIDADE ");
                    sql.append("FROM ");
                    sql.append("TB_CURSO       T1, ");
                    sql.append("TB_HIST_EMISSAO_PASSAPORTE T2, ");
                    sql.append("TB_TURMA T3 ");
                    sql.append("WHERE ");
                    sql.append("T1.CD_CURSO = T3.CD_CURSO  AND ");
                    sql.append("T2.SEQ_TURMA = T3.SEQ_TURMA AND ");
                    sql.append("T1.CD_CURSO IN (");
                    for(String s : request.getParameterValues("cursos")){
                        sql.append(s);
                        sql.append(", ");
                    }
                    sql.delete(sql.length() -2, sql.length());
                    sql.append(") ");
                    sql.append(" AND T2.DT_EMISSAO BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append("       AND '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                    sql.append("GROUP BY ");
                    sql.append("DESCR_CURSO ");
                    
                    titulo = "Quantidades de Passaportes emitidos";
                    titulo2 = "Período  " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim");
                    quebra1 = false;
                    quebra2 = false;
                    total1 = -1;
                    total2 = -1;
                    total3 = -1;
                    total4 = -1;
                }else{
                    sql.append("SELECT ");
                    sql.append("T1.DESCR_CURSO AS CURSO, ");
                    sql.append("T4.NOME_PESSOA AS NOME, ");
                    sql.append("T2.DT_EMISSAO AS EMISSAO ");
                    sql.append("FROM ");
                    sql.append("TB_CURSO       T1, ");
                    sql.append("TB_HIST_EMISSAO_PASSAPORTE T2, ");
                    sql.append("TB_TURMA T3, TB_PESSOA T4 ");
                    sql.append("WHERE t2.cd_matricula *= t4.cd_matricula and T2.CD_CATEGORIA *= T4.SEQ_DEPENDENTE and T2.SEQ_DEPENDENTE *= T4.CD_CATEGORIA AND ");
                    sql.append("T1.CD_CURSO = T3.CD_CURSO  AND ");
                    sql.append("T2.SEQ_TURMA = T3.SEQ_TURMA AND ");
                    sql.append("T1.CD_CURSO IN (");
                    for(String s : request.getParameterValues("cursos")){
                        sql.append(s);
                        sql.append(", ");
                    }
                    sql.delete(sql.length() -2, sql.length());
                    sql.append(") ");
                    sql.append(" AND T2.DT_EMISSAO BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append("       AND '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                    sql.append("ORDER BY ");
                    sql.append("DESCR_CURSO, DT_EMISSAO");
     
                    titulo = "Quantidades de Passaportes emitidos";
                    titulo2 = "Período  " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim");
                    quebra1 = true;
                    quebra2 = false;
                    total1 = -1;
                    total2 = -1;
                    total3 = -1;
                    total4 = -1;                    
                }

            }else if(relatorio.equals("crachaFuncionario")){
                if(sintetico){
                    sql.append("Select count(*) as 'QUANTIDADE' ");
                    sql.append("From   TB_FUNCIONARIO ");
                    sql.append("Where  DT_EMISSAO_CRACHA BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append("       AND '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                    sql.append("       AND TP_FUNCIONARIO = 'F'");
      
                    titulo = "Quantidades de Crachas de Funcionarios emitidos";
                    titulo2 = "Período  " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim");
                    quebra1 = false;
                    quebra2 = false;
                    total1 = -1;
                    total2 = -1;
                    total3 = -1;
                    total4 = -1;
                }else{
                    sql.append("Select T1.NOME_FUNCIONARIO AS 'FUNCIONARIO', T2.DESCR_CARGO AS CARGO, T3.DESCR_SETOR AS SETOR,  T1.DT_EMISSAO_CRACHA AS EMISSAO ");
                    sql.append("From   TB_FUNCIONARIO T1, TB_CARGO_FUNCAO T2, TB_SETOR T3 ");
                    sql.append("Where  T1.CD_CARGO *= T2.CD_CARGO AND T1.CD_SETOR *= T3.CD_SETOR ");
                    sql.append("       AND  DT_EMISSAO_CRACHA BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append("       AND '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                    sql.append("       AND TP_FUNCIONARIO = 'F' ORDER BY T1.DT_EMISSAO_CRACHA");
      
                    titulo = "Quantidades de Crachas de Funcionarios emitidos";
                    titulo2 = "Período  " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim");
                    quebra1 = false;
                    quebra2 = false;
                    total1 = -1;
                    total2 = -1;
                    total3 = -1;
                    total4 = -1;
                }
            }else{//crachaConcessionario
                if(sintetico){
                    sql.append("Select count(*) as 'QUANTIDADE' ");
                    sql.append("From   TB_FUNCIONARIO ");
                    sql.append("Where  DT_EMISSAO_CRACHA BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append("       AND '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                    sql.append("       AND TP_FUNCIONARIO = 'C'");
      
                    titulo = "Quantidades de Crachas de Concessionários emitidos";
                    titulo2 = "Período  " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim");
                    quebra1 = false;
                    quebra2 = false;
                    total1 = -1;
                    total2 = -1;
                    total3 = -1;
                    total4 = -1;                    
                }else{
                    sql.append("Select T1.NOME_FUNCIONARIO AS 'FUNCIONARIO', T2.DESCR_CARGO AS CARGO, T3.DESCR_SETOR AS SETOR,  T1.DT_EMISSAO_CRACHA AS EMISSAO ");
                    sql.append("From   TB_FUNCIONARIO T1, TB_CARGO_FUNCAO T2, TB_SETOR T3 ");
                    sql.append("Where  T1.CD_CARGO *= T2.CD_CARGO AND T1.CD_SETOR *= T3.CD_SETOR ");
                    sql.append("       AND  DT_EMISSAO_CRACHA BETWEEN '");
                    sql.append(fmt.format(d1));
                    sql.append(" 00:00:00'");
                    sql.append("       AND '");
                    sql.append(fmt.format(d2));
                    sql.append(" 23:59:59' ");
                    sql.append("       AND TP_FUNCIONARIO = 'C' ORDER BY T1.DT_EMISSAO_CRACHA");

                    titulo = "Quantidades de Crachas de Concessionários emitidos";
                    titulo2 = "Período  " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim");
                    quebra1 = false;
                    quebra2 = false;
                    total1 = -1;
                    total2 = -1;
                    total3 = -1;
                    total4 = -1;                                        
                }
            }
            
            request.setAttribute("titulo", titulo);
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", quebra1);
            request.setAttribute("quebra2", quebra2);
            request.setAttribute("total1", total1);
            request.setAttribute("total2", total2);
            request.setAttribute("total3", total3);
            request.setAttribute("total4", total4);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT' AND TP_CATEGORIA = 'SO'"));
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.setAttribute("tiposConvite", ComboBoxLoader.listar("TB_TIPO_CONVITE"));
            
            request.getRequestDispatcher("/pages/1400.jsp").forward(request, response);            
        }
    }
        
    @App("1420")
    public static void reservaDependencia(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            String relatorio = request.getParameter("imprimir");
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            boolean sintetico = request.getParameter("tipo").equals("S");
            
            if(sintetico){
                sql.append("SELECT ");
                sql.append("T1.DESCR_DEPENDENCIA AS Dependências, ");
                sql.append("COUNT(*) AS 'Qt. Reservas' ");
                sql.append("FROM ");
                sql.append("TB_DEPENDENCIA T1, ");
                sql.append("TB_RESERVA_DEPENDENCIA T2 ");
                sql.append("WHERE ");
                sql.append("T1.SEQ_DEPENDENCIA = T2.SEQ_DEPENDENCIA    AND ");
                sql.append("T2.CD_STATUS_RESERVA = 'CF' AND ");
                sql.append("T1.SEQ_DEPENDENCIA IN (");
                for(String s : request.getParameterValues("dependencias")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");
                sql.append(" AND T2.DT_INIC_UTILIZACAO BETWEEN '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00'");
                sql.append("       AND '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");
                sql.append("GROUP BY ");
                sql.append("T1.DESCR_DEPENDENCIA");
   
                request.setAttribute("titulo", "Reletório de Reserva de Dependências - Sintético");
                request.setAttribute("titulo2", "de " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim"));

                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", false);
                request.setAttribute("quebra2", false);
                request.setAttribute("total1", -1);
                request.setAttribute("total2", 1);
                request.setAttribute("total3", -1);
                request.setAttribute("total4", -1);

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
            }else{
                sql.append("SELECT ");
                sql.append("T1.DESCR_DEPENDENCIA AS Dependências, ");
                sql.append("T2.NOME_INTERESSADO AS Responsável, ");
                sql.append("T2.DT_INIC_UTILIZACAO AS Utilização ");
                sql.append("FROM ");
                sql.append("TB_DEPENDENCIA T1, ");
                sql.append("TB_RESERVA_DEPENDENCIA T2 ");
                sql.append("WHERE ");
                sql.append("T1.SEQ_DEPENDENCIA = T2.SEQ_DEPENDENCIA    AND ");
                sql.append("T2.CD_STATUS_RESERVA = 'CF' AND ");
                sql.append("T1.SEQ_DEPENDENCIA IN (");
                for(String s : request.getParameterValues("dependencias")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");
                sql.append(" AND T2.DT_INIC_UTILIZACAO BETWEEN '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:00'");
                sql.append("       AND '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");
                sql.append("ORDER BY 1, 2");
   
                request.setAttribute("titulo", "Reletório de Reserva de Dependências - Analítico");
                request.setAttribute("titulo2", "de " + request.getParameter("dataInicio") 
                            + " a " + request.getParameter("dataFim"));

                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", true);
                request.setAttribute("quebra2", false);
                request.setAttribute("total1", -1);
                request.setAttribute("total2", -1);
                request.setAttribute("total3", -1);
                request.setAttribute("total4", -1);

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
            }
        }else{
            request.setAttribute("dependencias", ComboBoxLoader.listar("TB_DEPENDENCIA"));            
            request.getRequestDispatcher("/pages/1420.jsp").forward(request, response);
        }
    }

    @App("1980")
    public static void convites(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            int tituloInicio = 0;
            try{
                tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            }catch(NumberFormatException e){
                tituloInicio = 0;
            }
            int tituloFim = 0;
            try{
                tituloFim = Integer.parseInt(request.getParameter("tituloFim"));
            }catch(NumberFormatException e){
                tituloFim = 0;
            }
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date emissaoInicio = Datas.parse(request.getParameter("emissaoInicio"));
            Date emissaoFim = Datas.parse(request.getParameter("emissaoFim"));
            Date validadeInicio = Datas.parse(request.getParameter("validadeInicio"));
            Date validadeFim = Datas.parse(request.getParameter("validadeFim"));
            Date utilizacaoInicio = Datas.parse(request.getParameter("utilizacaoInicio"));
            Date utilizacaoFim = Datas.parse(request.getParameter("utilizacaoFim"));            

            sql.append("SELECT ");
            sql.append("DESCR_TIPO_CONVITE AS 'Tipo Convite', ");
            sql.append("NOME_SACADOR  AS 'Sacador', ");
            sql.append("NOME_CONVIDADO AS 'Convidado', ");
            sql.append("DT_RETIRADA  AS 'Dt. Retirada', ");
            sql.append("DT_MAX_UTILIZACAO AS 'Dt. Validade', ");
            sql.append("DT_UTILIZACAO  AS 'Dt. Utilizacao', ");
            sql.append("Case CD_STATUS_CONVITE ");
            sql.append("WHEN 'UT' THEN 'Utilizado' ");
            sql.append("WHEN 'NU' THEN 'Não Utilizado' ");
            sql.append("WHEN 'CA' THEN 'Cancelado' ");
            sql.append("END AS 'Situação' ");
            sql.append("From ");
            sql.append("TB_CONVITE  T1, ");
            sql.append("TB_TIPO_CONVITE T2 ");
            sql.append("Where ");
            sql.append("T1.CD_CATEGORIA_CONVITE = T2.CD_TIPO_CONVITE AND ");
            sql.append("T1.CD_CATEGORIA_CONVITE IN (");
            for(String s : request.getParameterValues("tiposConvite")){
                sql.append("'");
                sql.append(s);
                sql.append("', ");
            }
            sql.delete(sql.length() -2, sql.length());
            sql.append(") ");                
            sql.append(" AND T1.CD_STATUS_CONVITE IN (");
            for(String s : request.getParameterValues("situacoes")){
                sql.append("'");
                sql.append(s);
                sql.append("', ");
            }
            sql.delete(sql.length() -2, sql.length());
            sql.append(") ");                

            if(tituloInicio > 0){
               sql.append(" AND T1.CD_MATRICULA >= ");
               sql.append(tituloInicio);
            }
            if(tituloFim > 0){
               sql.append(" AND T1.CD_MATRICULA <= ");
               sql.append(tituloFim);
            }
            
            if(emissaoInicio != null){
                sql.append(" AND T1.DT_RETIRADA BETWEEN '");
                sql.append(fmt.format(emissaoInicio));
                sql.append("' AND '");
                sql.append(fmt.format(emissaoFim));
                sql.append("' ");
            }
            
            if(validadeInicio != null){
                sql.append(" AND T1.DT_MAX_UTILIZACAO BETWEEN '");
                sql.append(fmt.format(validadeInicio));
                sql.append("' AND '");
                sql.append(fmt.format(validadeFim));
                sql.append("' ");
            }
            
            if(utilizacaoInicio != null){
                sql.append(" AND T1.DT_UTILIZACAO BETWEEN '");
                sql.append(fmt.format(utilizacaoInicio));
                sql.append("' AND '");
                sql.append(fmt.format(utilizacaoFim));
                sql.append("' ");
            }            

            sql.append(" ORDER BY DESCR_TIPO_CONVITE");
            
            request.setAttribute("titulo", "Relatório de Convites");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", true);
            request.setAttribute("quebra2", true);
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                
        }else{
            request.setAttribute("tiposConvite", ComboBoxLoader.listar("TB_TIPO_CONVITE"));
            request.getRequestDispatcher("/pages/1980.jsp").forward(request, response);            
        }
    }
    
    @App("2130")
    public static void nomeConvidado(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            StringBuilder from = new StringBuilder();
            int qtInicio = 0;
            try{
                qtInicio = Integer.parseInt(request.getParameter("qtInicio"));
            }catch(NumberFormatException e){
                qtInicio = 0;
            }
            int qtFim = 9999;
            try{
                qtFim = Integer.parseInt(request.getParameter("qtFim"));
            }catch(NumberFormatException e){
                qtFim = 9999;
            }
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date emissaoInicio = Datas.parse(request.getParameter("emissaoInicio"));
            Date emissaoFim = Datas.parse(request.getParameter("emissaoFim"));
            Date validadeInicio = Datas.parse(request.getParameter("validadeInicio"));
            Date validadeFim = Datas.parse(request.getParameter("validadeFim"));
            Date utilizacaoInicio = Datas.parse(request.getParameter("utilizacaoInicio"));
            Date utilizacaoFim = Datas.parse(request.getParameter("utilizacaoFim"));            
            boolean sintetico = request.getParameter("tipo").equals("S");
            String cpfConvidado = request.getParameter("cpfConvidado");
            
            if(sintetico){
                sql.append("SELECT ");
                sql.append("CPF_CONVIDADO 'CPF', ");
                sql.append("(SELECT TOP 1 NOME_CONVIDADO FROM TB_CONVITE T0 WHERE TX.CPF_CONVIDADO = T0.CPF_CONVIDADO) 'Convidado', ");
                sql.append("QTD AS 'Quant.' ");
                sql.append("FROM (");
                sql.append("SELECT ");
                sql.append("CPF_CONVIDADO, ");
                sql.append("COUNT(*) QTD ");
            }else{
                sql.append("SELECT ");
                sql.append("CPF_CONVIDADO  AS 'CPF', ");
                sql.append("NOME_CONVIDADO AS 'Convidado', ");
                sql.append("NOME_SACADOR  AS 'Sacador', ");
                sql.append("DESCR_TIPO_CONVITE AS 'Tipo Convite', ");
                sql.append("DT_RETIRADA  AS 'Dt. Retirada', ");
                sql.append("DT_MAX_UTILIZACAO AS 'Dt. Validade', ");
                sql.append("DT_UTILIZACAO  AS 'Dt. Utilizacao', ");
                sql.append("Case CD_STATUS_CONVITE ");
                sql.append("WHEN 'UT' THEN 'Utilizado' ");
                sql.append("WHEN 'NU' THEN 'Não Utilizado' ");
                sql.append("WHEN 'CA' THEN 'Cancelado' ");
                sql.append("END AS 'Situação' ");
            }

            from.append("From ");
            from.append("TB_CONVITE  T1, ");
            from.append("TB_TIPO_CONVITE T2 ");
            from.append("Where ");
            from.append("T1.CD_CATEGORIA_CONVITE = T2.CD_TIPO_CONVITE AND ");
            from.append("LTRIM(RTRIM(ISNULL(T1.CPF_CONVIDADO, ''))) <> '' AND ");
            from.append("T1.CD_CATEGORIA_CONVITE IN (");
            for(String s : request.getParameterValues("tiposConvite")){
                from.append("'");
                from.append(s);
                from.append("', ");
            }
            from.delete(from.length() -2, from.length());
            from.append(") ");                
            from.append(" AND T1.CD_STATUS_CONVITE IN (");
            for(String s : request.getParameterValues("situacoes")){
                from.append("'");
                from.append(s);
                from.append("', ");
            }
            from.delete(from.length() -2, from.length());
            from.append(") ");                            

            if(emissaoInicio != null){
                from.append(" AND T1.DT_RETIRADA BETWEEN '");
                from.append(fmt.format(emissaoInicio));
                from.append("' AND '");
                from.append(fmt.format(emissaoFim));
                from.append("' ");
            }
            
            if(validadeInicio != null){
                from.append(" AND T1.DT_MAX_UTILIZACAO BETWEEN '");
                from.append(fmt.format(validadeInicio));
                from.append("' AND '");
                from.append(fmt.format(validadeFim));
                from.append("' ");
            }
            
            if(utilizacaoInicio != null){
                from.append(" AND T1.DT_UTILIZACAO BETWEEN '");
                from.append(fmt.format(utilizacaoInicio));
                from.append("' AND '");
                from.append(fmt.format(utilizacaoFim));
                from.append("' ");

            }            
            if(cpfConvidado != null && !cpfConvidado.equals("")){
                from.append(" AND T1.CPF_CONVIDADO = '");
                from.append(cpfConvidado);
                from.append("' ");
            }            
                
            sql.append(from);
            
            if(sintetico){    
                sql.append(" GROUP BY CPF_CONVIDADO ");
                sql.append(" HAVING COUNT(*) BETWEEN ");
                sql.append(qtInicio);
                sql.append(" AND ");
                sql.append(qtFim);
                sql.append(") TX  ");
            }else{
                String tmp = from.toString().toUpperCase();
                tmp = tmp.replaceAll("T1", "TX");
                tmp = tmp.replaceAll("T2", "TY");
                tmp = tmp.replaceAll("WHERE", "WHERE T1.CPF_CONVIDADO = TX.CPF_CONVIDADO AND ");
                
                StringBuilder subSql = new StringBuilder();
                subSql.append("SELECT ");
                subSql.append("CPF_CONVIDADO, ");
                subSql.append("COUNT(*)");
                subSql.append(tmp);
                subSql.append(" GROUP BY CPF_CONVIDADO ");
                subSql.append(" HAVING COUNT(*) BETWEEN ");
                subSql.append(qtInicio);
                subSql.append(" AND ");
                subSql.append(qtFim);                
                
                sql.append("AND EXISTS (");
                sql.append(subSql);
                sql.append(")");
            }
            
            sql.append(" ORDER BY 1, 2");
            
            String titulo2 = null;
            boolean quebra1 = false;
            boolean quebra2 = false;
            
            if(sintetico){
                titulo2 = "Sintético";
            }else{
                titulo2 = "Analítico";
            }
            request.setAttribute("titulo", "Relatório de Convites");
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", quebra1);
            request.setAttribute("quebra2", quebra2);
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else{
            request.setAttribute("tiposConvite", ComboBoxLoader.listar("TB_TIPO_CONVITE"));
            request.getRequestDispatcher("/pages/2130.jsp").forward(request, response);            
        }
    }
    
    @App("2220")
    public static void carros(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            boolean socio = Boolean.parseBoolean(request.getParameter("socio"));
            boolean geral = Boolean.parseBoolean(request.getParameter("geral"));
            
            if(socio){
                sql.append("SELECT RIGHT('0000' + CONVERT(VARCHAR, T3.CD_MATRICULA), 4)  + '/' + RIGHT('00' + CONVERT(VARCHAR, T3.CD_CATEGORIA), 2) + ' - ' + ");
                sql.append("T3.NOME_PESSOA as Pessoa, T1.DE_MARCA as Marca, T2.DE_MODELO as Modelo, T4.DE_PLACA as Placa FROM TB_MARCA_CARRO T1, TB_MODELO_CARRO T2, TB_PESSOA T3, TB_CARRO_PESSOA T4 WHERE T1.CD_MARCA = T2.CD_MARCA AND T2.CD_MODELO = T4.CD_MODELO AND T3.CD_MATRICULA = T4.CD_MATRICULA AND T3.CD_CATEGORIA = T4.CD_CATEGORIA AND T3.SEQ_DEPENDENTE = T4.SEQ_DEPENDENTE");
            }

            if(geral){
                if(socio){
                    sql.append(" UNION ALL ");
                }
                sql.append("SELECT T4.NO_PESSOA, T1.DE_MARCA, T2.DE_MODELO, T4.DE_PLACA FROM TB_MARCA_CARRO T1, TB_MODELO_CARRO T2, TB_CARRO T4 WHERE T1.CD_MARCA = T2.CD_MARCA AND T2.CD_MODELO = T4.CD_MODELO ");
            }

            sql.append(" ORDER BY 1 ");

            request.setAttribute("titulo", "Relatório de Carros");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", false);
            request.setAttribute("quebra2", false);
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("/pages/2220.jsp").forward(request, response);
        }
    }
    
    @App("2230")
    public static void permissaoProvisoria(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            Date validade = Datas.parse(request.getParameter("validade"));
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String nome = request.getParameter("nome");
            String atividade = request.getParameter("atividade");
            
            sql.append("select Nome_autorizado as Nome, DESCR_ATIVIDADE as Atividade, convert(varchar, DT_INIC_TRAB, 103) + ' a ' + convert(varchar, DT_FIM_TRAB, 103) as 'Dt. Fim' from tb_autor_Especial WHERE 1 = 1");

            if(nome != null && !nome.trim().equals("")){
                sql.append(" AND NOME_AUTORIZADO LIKE '");
                sql.append(nome.toUpperCase());
                sql.append("%'");
            }
            
            if(atividade != null && !atividade.trim().equals("")){
                sql.append(" AND DESCR_ATIVIDADE LIKE '");
                sql.append(atividade.toUpperCase());
                sql.append("%'");
            }            

            if(validade != null){
                sql.append(" AND DT_INIC_TRAB <= '");
                sql.append(fmt.format(validade));
                sql.append("' AND DT_Fim_TRAB >= '");
                sql.append(fmt.format(validade));
                sql.append("'");
            }

            sql.append(" ORDER BY 1, 2");
            
            request.setAttribute("titulo", "Permissão Provisória");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", false);
            request.setAttribute("quebra2", false);
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("/pages/2230.jsp").forward(request, response);
        }
    }
    
    @App("1870")
    public static void emiate(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("gerar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            StringBuilder where = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            boolean titular = Boolean.parseBoolean(request.getParameter("titular"));
            boolean dependente = Boolean.parseBoolean(request.getParameter("dependente"));
            boolean masculino = Boolean.parseBoolean(request.getParameter("masculino"));
            boolean feminino = Boolean.parseBoolean(request.getParameter("feminino"));
            
            where.append(" AND T1.CD_CATEGORIA IN (");
            for(String s : request.getParameterValues("categorias")){
                where.append(s);
                where.append(", ");
            }
            where.delete(where.length() -2, where.length());
            where.append(") ");
            where.append(" AND T1.DT_NASCIMENTO BETWEEN '");
            where.append(fmt.format(d1));
            where.append("' AND '");
            where.append(fmt.format(d2));
            where.append("' ");
            where.append(" AND T1.SEQ_DEPENDENTE ");
            if(titular){
                if(dependente){
                    where.append(" >= 0");
                }else{
                    where.append(" = 0");
                }
            }else{
                where.append(" > 0");
            }
            
            where.append(" AND T1.CD_SEXO IN ('");
            if(masculino){
                if(feminino){
                    where.append("M', 'F");
                }else{
                    where.append("M");
                }
            }else{
                where.append("F");
            }
            where.append("')");
            
            sql.append("SELECT ");
            sql.append("T3.DESCR_CATEGORIA   , ");
            sql.append("T1.CD_MATRICULA      , ");
            sql.append("T1.NOME_PESSOA    , ");
            sql.append("T1.DT_NASCIMENTO  , ");
            sql.append("T2.ENDERECO_R     , ");
            sql.append("T2.BAIRRO_R    , ");
            sql.append("T2.CIDADE_R    , ");
            sql.append("T2.UF_R        , ");
            sql.append("T2.CEP_R    , ");
            sql.append("T2.TELEFONE_R, ");
            sql.append("T1.DE_EMAIL ");
            sql.append("From ");
            sql.append("TB_PESSOA      T1, ");
            sql.append("VW_COMPLEMENTO    T2, ");
            sql.append("TB_CATEGORIA T3 ");
            sql.append("Where ");
            sql.append("T1.CD_MATRICULA      = T2.CD_MATRICULA    AND ");
            sql.append("T1.CD_CATEGORIA      = T2.CD_CATEGORIA    AND ");
            sql.append("T1.CD_CATEGORIA      = T3.CD_CATEGORIA    AND ");
            sql.append("T2.CD_END_CORRESPONDENCIA= 'R' ");
            sql.append(where);

            sql.append(" Union All ");

            sql.append("SELECT ");
            sql.append("T3.DESCR_CATEGORIA   , ");
            sql.append("T1.CD_MATRICULA      , ");
            sql.append("T1.NOME_PESSOA    , ");
            sql.append("T1.DT_NASCIMENTO  , ");
            sql.append("T2.ENDERECO_C     , ");
            sql.append("T2.BAIRRO_C    , ");
            sql.append("T2.CIDADE_C    , ");
            sql.append("T2.UF_C        , ");
            sql.append("T2.CEP_C    , ");
            sql.append("T2.TELEFONE_C, ");
            sql.append("T1.DE_EMAIL ");
            sql.append("From ");
            sql.append("TB_PESSOA      T1, ");
            sql.append("VW_COMPLEMENTO    T2, ");
            sql.append("TB_CATEGORIA T3 ");
            sql.append("Where ");
            sql.append("T1.CD_MATRICULA      = T2.CD_MATRICULA    AND ");
            sql.append("T1.CD_CATEGORIA      = T2.CD_CATEGORIA    AND ");
            sql.append("T1.CD_CATEGORIA      = T3.CD_CATEGORIA    AND ");
            sql.append("T2.CD_END_CORRESPONDENCIA= 'C' ");
            sql.append(where);
            sql.append(" Order By 1,2");
            
            String path = request.getServletContext().getRealPath("/pages/");
            File mdb = new File(path + File.separator + "emiate.mdb");          
            try{
                File copia = Emiate.gerar(sql.toString(), mdb);
                response.setContentLength((int)copia.length());
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setHeader("Content-Disposition","attachment; filename=\"EMIATE-" + fmt.format(new Date()) + ".MDB\"");

                byte[] buf = new byte[4096];
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(copia));
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                int i = 0;
                while((i = in.read(buf)) > 0){
                    out.write(buf, 0, i);
                }
                out.close();
                in.close();   
                copia.delete();
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT' AND TP_CATEGORIA = 'SO'"));            
            request.getRequestDispatcher("/pages/1870.jsp").forward(request, response);
        }
    }
    
    @App("1930")
    public static void etiqueta(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        StringBuilder order = new StringBuilder();
        String ord1 = request.getParameter("ord1");
        String ord2 = request.getParameter("ord2");
        String ord3 = request.getParameter("ord3");
        String ord4 = request.getParameter("ord4");
        boolean imprimirCategoriaMatricula = Boolean.parseBoolean(request.getParameter("imprimirCategoriaMatricula"));
        boolean imprimirLogotipo = Boolean.parseBoolean(request.getParameter("imprimirLogotipo"));
        boolean imprimirTexto = Boolean.parseBoolean(request.getParameter("imprimirTexto"));
        String texto = request.getParameter("texto");
        
        order.append(" ORDER BY ");
        order.append(ord1);
        if(!ord2.equals(ord1)){
            order.append(", ");
            order.append(ord2);
            if(!ord3.equals(ord2) && !ord3.equals(ord1)){
                order.append(", ");
                order.append(ord3);
                if(!ord4.equals(ord3) && !ord4.equals(ord2) && !ord4.equals(ord1)){
                    order.append(", ");
                    order.append(ord4);
                }
            }
        }
        
        String sql = request.getParameter("sql");
        sql += order.toString();
        
        request.setAttribute("sql", sql);
        request.setAttribute("imprimirCategoriaMatricula", imprimirCategoriaMatricula);
        request.setAttribute("imprimirLogotipo", imprimirLogotipo);
        request.setAttribute("imprimirTexto", imprimirTexto);
        request.setAttribute("texto", texto);
        request.getRequestDispatcher("/pages/etiqueta.jsp").forward(request, response);
    }    
}
