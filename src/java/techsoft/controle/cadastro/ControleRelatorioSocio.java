package techsoft.controle.cadastro;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.CartaCobranca;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.cadastro.Titular;
import techsoft.caixa.Transacao;
import techsoft.carteirinha.JobMiniImpressora;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.db.Pool;
import techsoft.util.Datas;
import techsoft.util.Extenso;

@Controller
public class ControleRelatorioSocio {
 
    @App("2280")
    public static void brindeInternet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            boolean naoEntregue = Boolean.parseBoolean(request.getParameter("naoEntregue"));
            boolean entregue = Boolean.parseBoolean(request.getParameter("entregue"));
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

            Date d1 = Datas.parse(request.getParameter("dataAtualizacaoInicio"));
            Date d2 = Datas.parse(request.getParameter("dataAtualizacaoFim"));
            Date d3 = Datas.parse(request.getParameter("dataEntregaInicio"));
            Date d4 = Datas.parse(request.getParameter("dataEntregaFim"));
            
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("CD_MATRICULA AS Titulo, ");
            sql.append("CD_CATEGORIA as 'Cat.', ");
            sql.append("NOME_PESSOA as Sócio, ");
            sql.append("DT_ULT_ATU_INTERNET as 'Dt. Atualiz. Internet', ");
            sql.append("DT_ENTREGA_BRINDE as 'Dt. Entrega Brinde'");
            sql.append("From ");
            sql.append("TB_PESSOA ");
            sql.append("Where ");
            sql.append("(DT_ULT_ATU_INTERNET IS NOT NULL OR DT_ENTREGA_BRINDE IS NOT NULL) ");

            if(d1 != null){
                sql.append(" AND DT_ULT_ATU_INTERNET >= '");
                sql.append(fmt.format(d1));
                sql.append("'");
            }
            if(d2 != null){
                sql.append(" AND DT_ULT_ATU_INTERNET <= '");
                sql.append(fmt.format(d2));
                sql.append("'");
            }
            if(d3 != null && entregue){
                sql.append(" AND DT_ENTREGA_BRINDE >= '");
                sql.append(fmt.format(d3));
                sql.append("'");
            }
            if(d4 != null && entregue){
                sql.append(" AND DT_ENTREGA_BRINDE <= '");
                sql.append(fmt.format(d4));
                sql.append("'");
            }            
        
            if(entregue){
                if(!naoEntregue){
                    sql.append(" AND DT_ENTREGA_BRINDE IS NOT NULL ");
                }
            }else{
                sql.append(" AND DT_ENTREGA_BRINDE IS NULL ");
            }
            
            request.setAttribute("titulo", "Relatório de Brindes");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            request.getRequestDispatcher("/pages/2280.jsp").forward(request, response);            
        }   
    }    
    
    @App("1630")
    public static void cargosEspeciais(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){    
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("T1.DESCR_CARGO_ESPECIAL AS Cargo   , ");
            sql.append("RIGHT('00' + CONVERT(VARCHAR, T2.CD_CATEGORIA), 2) + '/' + ");
            sql.append("RIGHT('0000' + CONVERT(VARCHAR, T2.CD_MATRICULA), 4) as Titulo , ");
            sql.append("T2.NOME_PESSOA AS Pessoa       , ");
            sql.append("Case T2.CD_CARGO_ATIVO ");
            sql.append("WHEN 'S' THEN 'ATIVO' ");
            sql.append("WHEN 'N' THEN 'INATIVO' ");
            sql.append("END As Atividade ");
            sql.append("FROM ");
            sql.append("TB_CARGO_ESPECIAL T1, ");
            sql.append("TB_PESSOA T2 ");
            sql.append("WHERE ");
            sql.append("T1.CD_CARGO_ESPECIAL = T2.CD_CARGO_ESPECIAL AND ");
            
            StringBuilder tmp = new StringBuilder();
            for(String s : request.getParameterValues("cargos")){
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() -2, tmp.length());
            sql.append("T1.CD_CARGO_ESPECIAL IN (");
            sql.append(tmp.toString());
            sql.append(")");
            
            sql.append(" ORDER BY 1, 2, 3 ");

            request.setAttribute("titulo", "Relatório de Cargos Especiais");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else{
            request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_ESPECIAL"));
            request.getRequestDispatcher("/pages/1630.jsp").forward(request, response);
        }
    }
    
    @App("1520")
    public static void geral(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){    
            boolean relatorioEstadoCivil = request.getParameter("informacao").equalsIgnoreCase("E");
            //SELECT PARA CORRESPONDENCIA PARA RESIDENCIA
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append(" RIGHT('00' + CONVERT(VARCHAR, T1.CD_CATEGORIA), 2) + '/' + ");
            sql.append(" RIGHT('0000' + CONVERT(VARCHAR, T1.CD_MATRICULA), 4) + ' - ' + ");
            sql.append(" T1.NOME_PESSOA AS Pessoa, ");

            //foi escolhido relatorio de estado civil
            if(relatorioEstadoCivil){
                sql.append(" CASE ISNULL(T2.ESTADO_CIVIL, 'NI') ");
                sql.append(" when 'NI' THEN 'Não Informado' ");
                sql.append(" when 'CA' THEN 'Casado(a)' ");
                sql.append(" when 'DI' THEN 'Divorciado(a)' ");
                sql.append(" when 'DE' THEN 'Desquitado(a)' ");
                sql.append(" when 'OU' THEN 'Outros' ");
                sql.append(" when 'SO' THEN 'Solteiro(a)' ");
                sql.append(" when 'VI' THEN 'Viúvo(a)' ");
                sql.append(" END AS 'Estado Civil', ");
            }
            
            sql.append(" CASE T2.CD_END_CORRESPONDENCIA ");
            sql.append(" WHEN 'R' THEN CONVERT(VARCHAR(90), ");
            sql.append("      ISNULL(T2.ENDERECO_R, '') + ' - ' + ");
            sql.append("      ISNULL(T2.BAIRRO_R, '') + ' - ' + ");
            sql.append("      ISNULL(T2.CIDADE_R, '') + ' - ' + ");
            sql.append("      ISNULL(T2.UF_R, '') + ' - ' + ");
            sql.append("      ISNULL(T2.CEP_R, '')) ");
            sql.append(" ELSE CONVERT(VARCHAR(90), ");
            sql.append("      ISNULL(T2.ENDERECO_C, '') + ' - ' + ");
            sql.append("      ISNULL(T2.BAIRRO_C, '') + ' - ' + ");
            sql.append("      ISNULL(T2.CIDADE_C, '') + ' - ' + ");
            sql.append("      ISNULL(T2.UF_C, '') + ' - ' + ");
            sql.append("      ISNULL(T2.CEP_C, '')) ");
            sql.append(" END AS Endereço  ");

            //foi escolhido relatorio de telefone
            if(!relatorioEstadoCivil){
                sql.append(", ISNULL(TELEFONE_R, '') + ' / '  + ISNULL(TELEFONE_C, '') AS Telefones");
            }

            sql.append(" FROM ");
            sql.append(" TB_PESSOA T1, ");
            sql.append(" VW_COMPLEMENTO T2 ");
            sql.append(" WHERE ");
            sql.append(" T1.CD_MATRICULA = T2.CD_MATRICULA AND ");
            sql.append(" T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
            sql.append(" T1.SEQ_DEPENDENTE = 0 AND ");

            int tituloInicio = 0;
            int tituloFim = 0;
            try{
                tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            }catch(NumberFormatException e){
                tituloInicio = 0;
            }
            
            if(tituloInicio > 0){
                sql.append(" T1.CD_MATRICULA >= ");
                sql.append(tituloInicio);
                sql.append(" AND ");
            }
            try{
                tituloFim = Integer.parseInt(request.getParameter("tituloFim"));
            }catch(NumberFormatException e){
                tituloFim = 0;
            }
            if(tituloFim > 0){
                sql.append(" T1.CD_MATRICULA <= ");
                sql.append(tituloFim);
                sql.append(" AND ");
            }            
            
            StringBuilder tmp = new StringBuilder();
            String categorias = null;
            for(String s : request.getParameterValues("categorias")){
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() -2, tmp.length());
            sql.append(" T1.CD_CATEGORIA IN( ");
            categorias = tmp.toString();
            sql.append(categorias);
            sql.append(")");
            
            boolean masculino = Boolean.parseBoolean(request.getParameter("masculino"));
            boolean feminino = Boolean.parseBoolean(request.getParameter("feminino"));
            
            if(masculino){
                if(!feminino){
                    sql.append(" AND T1.CD_SEXO = 'M' ");
                }
            }else{
                if(feminino){
                    sql.append(" AND T1.CD_SEXO = 'F' ");
                }
            }

            boolean semCPFCNPJ = Boolean.parseBoolean(request.getParameter("semCPFCNPJ"));

            if(semCPFCNPJ){
                sql.append(" AND ISNULL(T2.CPF_CGC_PESSOA, 0) = 0 ");
            }
            
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date di = Datas.parse(request.getParameter("nascimentoInicio"));
            Date df = Datas.parse(request.getParameter("nascimentoFim"));            

            if(di != null){
                if(df != null){
                    sql.append(" AND T1.DT_NASCIMENTO BETWEEN '");
                    sql.append(fmt.format(di));
                    sql.append("' AND '");
                    sql.append(fmt.format(df));
                    sql.append("' ");
                }else{
                    sql.append(" AND T1.DT_NASCIMENTO >= '");
                    sql.append(fmt.format(di));
                    sql.append("' ");
                }
            }else{
                if(df != null){
                    sql.append(" AND T1.DT_NASCIMENTO <= '");
                    sql.append(fmt.format(df));
                    sql.append("' ");
                }
            }
            
            tmp.delete(0, tmp.length());
            for(String s : request.getParameterValues("estadosCivis")){
                tmp.append("'");
                tmp.append(s);
                tmp.append("', ");
            }
            
            String sqlin = null;
            if(tmp.length() > 0){
                tmp.delete(tmp.length() -2, tmp.length());
                sqlin = tmp.toString();
            }
            
            if(sqlin != null){
                sql.append(" AND ISNULL(T2.ESTADO_CIVIL, 'US') IN (");
                sql.append(sqlin);
                sql.append(")");
            }

            
            String espolio = request.getParameter("espolio");
            if(espolio.equals("S")){
                sql.append(" AND ISNULL(T2.IC_ESPOLIO, 'N') = 'S'");
            }else if(espolio.equals("N")){
                sql.append(" AND ISNULL(T2.IC_ESPOLIO, 'N') = 'N'");
            }
            

            request.setAttribute("titulo", "Relatório Geral de Sócios");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else if("gerarEtiqueta".equals(acao)){
            StringBuilder sql = new StringBuilder();
            
            sql.append("SELECT ");
            sql.append(" T1.CD_MATRICULA AS MATRICULA, ");
            sql.append(" T1.CD_CATEGORIA AS CATEGORIA, ");
            sql.append(" T1.NOME_PESSOA AS NOME, ");
            
            sql.append(" CASE T2.CD_END_CORRESPONDENCIA ");
            sql.append(" WHEN 'R' THEN T2.ENDERECO_R ");
            sql.append(" ELSE T2.ENDERECO_C ");
            sql.append(" END ENDERECO, ");
            sql.append(" CASE T2.CD_END_CORRESPONDENCIA ");
            sql.append(" WHEN 'R' THEN T2.BAIRRO_R ");
            sql.append(" ELSE T2.BAIRRO_C ");
            sql.append(" END BAIRRO, ");
            sql.append(" CASE T2.CD_END_CORRESPONDENCIA ");
            sql.append(" WHEN 'R' THEN T2.CIDADE_R ");
            sql.append(" ELSE T2.CIDADE_C ");
            sql.append(" END CIDADE, ");
            sql.append(" CASE T2.CD_END_CORRESPONDENCIA ");
            sql.append(" WHEN 'R' THEN T2.UF_R ");
            sql.append(" ELSE T2.UF_C ");
            sql.append(" END UF, ");
            sql.append(" CASE T2.CD_END_CORRESPONDENCIA ");
            sql.append(" WHEN 'R' THEN T2.CEP_R ");
            sql.append(" ELSE T2.CEP_C ");
            sql.append(" END CEP, ");
            
            sql.append(" T1.CD_SEXO AS SEXO");
            sql.append(" FROM ");
            sql.append(" TB_PESSOA T1, ");
            sql.append(" TB_COMPLEMENTO T2 ");
            sql.append(" WHERE ");
            sql.append(" T1.CD_MATRICULA = T2.CD_MATRICULA AND ");
            sql.append(" T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
            sql.append(" T1.SEQ_DEPENDENTE = 0 AND ");

            int tituloInicio = 0;
            int tituloFim = 0;
            try{
                tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            }catch(NumberFormatException e){
                tituloInicio = 0;
            }
            
            if(tituloInicio > 0){
                sql.append(" T1.CD_MATRICULA >= ");
                sql.append(tituloInicio);
                sql.append(" AND ");
            }
            try{
                tituloFim = Integer.parseInt(request.getParameter("tituloFim"));
            }catch(NumberFormatException e){
                tituloFim = 0;
            }
            if(tituloFim > 0){
                sql.append(" T1.CD_MATRICULA <= ");
                sql.append(tituloFim);
                sql.append(" AND ");
            }                        

            StringBuilder tmp = new StringBuilder();
            String categorias = null;
            for(String s : request.getParameterValues("categorias")){
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() -2, tmp.length());
            sql.append(" T1.CD_CATEGORIA IN( ");
            categorias = tmp.toString();
            sql.append(categorias);
            sql.append(")");
            
            boolean masculino = Boolean.parseBoolean(request.getParameter("masculino"));
            boolean feminino = Boolean.parseBoolean(request.getParameter("feminino"));
            
            if(masculino){
                if(!feminino){
                    sql.append(" AND T1.CD_SEXO = 'M' ");
                }
            }else{
                if(feminino){
                    sql.append(" AND T1.CD_SEXO = 'F' ");
                }
            }
            
            boolean semCPFCNPJ = Boolean.parseBoolean(request.getParameter("semCPFCNPJ"));

            if(semCPFCNPJ){
                sql.append(" AND ISNULL(T2.CPF_CGC_PESSOA, 0) = 0 ");
            }
            

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date di = Datas.parse(request.getParameter("nascimentoInicio"));
            Date df = Datas.parse(request.getParameter("nascimentoFim"));            

            if(di != null){
                if(df != null){
                    sql.append(" AND T1.DT_NASCIMENTO BETWEEN '");
                    sql.append(fmt.format(di));
                    sql.append("' AND '");
                    sql.append(fmt.format(df));
                    sql.append("' ");
                }else{
                    sql.append(" AND T1.DT_NASCIMENTO >= '");
                    sql.append(fmt.format(di));
                    sql.append("' ");
                }
            }else{
                if(df != null){
                    sql.append(" AND T1.DT_NASCIMENTO <= '");
                    sql.append(fmt.format(df));
                    sql.append("' ");
                }
            }
            
            tmp.delete(0, tmp.length());
            for(String s : request.getParameterValues("estadosCivis")){
                tmp.append("'");
                tmp.append(s);
                tmp.append("', ");
            }
            
            String sqlin = null;
            if(tmp.length() > 0){
                tmp.delete(tmp.length() -2, tmp.length());
                sqlin = tmp.toString();
            }
            
            if(sqlin != null){
                sql.append(" AND ISNULL(T2.ESTADO_CIVIL, 'US') IN (");
                sql.append(sqlin);
                sql.append(")");
            }

            
            String espolio = request.getParameter("espolio");
            if(espolio.equals("S")){
                sql.append(" AND ISNULL(T2.IC_ESPOLIO, 'N') = 'S'");
            }else if(espolio.equals("N")){
                sql.append(" AND ISNULL(T2.IC_ESPOLIO, 'N') = 'N'");
            }

            request.setAttribute("sql", sql.toString());
            request.getRequestDispatcher("/pages/1930.jsp").forward(request, response);
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.getRequestDispatcher("/pages/1520.jsp").forward(request, response);            
        }
    }
    
    @App("1270")
    public static void generico(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");

        if("visualizar".equals(acao)){    
            StringBuilder sql = new StringBuilder();
            boolean todos = Boolean.parseBoolean(request.getParameter("todos"));
            boolean titular = Boolean.parseBoolean(request.getParameter("titular"));            
            boolean imprimirTelefone = Boolean.parseBoolean(request.getParameter("imprimirTelefone"));
            boolean imprimirEmail = Boolean.parseBoolean(request.getParameter("imprimirEmail"));
            boolean masculino = Boolean.parseBoolean(request.getParameter("masculino"));
            boolean feminino = Boolean.parseBoolean(request.getParameter("feminino"));            
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date di = Datas.parse(request.getParameter("nascimentoInicio"));
            Date df = Datas.parse(request.getParameter("nascimentoFim"));
            int tituloInicio = 0;
            int tituloFim = 0;
            
            try{
                tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            }catch(NumberFormatException e){
                tituloInicio = 0;
            }
            try{
                tituloFim = Integer.parseInt(request.getParameter("tituloFim"));
            }catch(NumberFormatException e){
                tituloFim = 0;
            }            
            
            StringBuilder categorias = new StringBuilder();
            if(request.getParameterValues("categorias") != null){
                for(String s : request.getParameterValues("categorias")){
                    categorias.append(s);
                    categorias.append(", ");
                }
                categorias.delete(categorias.length() -2, categorias.length());
            }
            
            StringBuilder cargosEspeciais = new StringBuilder();
            if(request.getParameterValues("cargosEspeciais") != null){
                for(String s : request.getParameterValues("cargosEspeciais")){
                    cargosEspeciais.append(s);
                    cargosEspeciais.append(", ");
                }
                cargosEspeciais.delete(cargosEspeciais.length() -2, cargosEspeciais.length());
            }
            
            boolean usuarioTitulo = Boolean.parseBoolean(request.getParameter("usuarioTitulo"));
            
                
            if(todos || titular){
                sql.append("SELECT ");
                sql.append(" RIGHT('0000' + convert(varchar, T1.cd_matricula),4) + '/' + RIGHT('00' + convert(varchar, T1.cd_categoria), 2) AS TITULO , ");
                sql.append(" T1.NOME_PESSOA AS NOME, ");
                sql.append(" 'TITULAR' AS TP_DEPENDENTE, ");
                sql.append(" CONVERT(VARCHAR(10), CONVERT(VARCHAR, T1.DT_NASCIMENTO, 103)) AS  NASC ");
                
                if(imprimirEmail){
                    sql.append(", T1.DE_EMAIL AS  EMAIL ");
                }
                if(imprimirTelefone){
                    sql.append(", T4.TELEFONE_R AS  FONE ");
                }
                
                sql.append(" FROM ");
                sql.append(" TB_PESSOA T1, ");
                sql.append(" TB_CATEGORIA T2, ");
                sql.append(" VW_COMPLEMENTO T4 ");
                sql.append(" WHERE ");
                sql.append(" T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
                sql.append(" T1.CD_MATRICULA = T4.CD_MATRICULA AND ");
                sql.append(" T1.CD_CATEGORIA = T4.CD_CATEGORIA AND ");
                sql.append(" T1.SEQ_DEPENDENTE = 0 ");
   
                if(tituloInicio > 0){
                    sql.append(" AND T1.CD_MATRICULA >= ");
                    sql.append(tituloInicio);
                }
                
                if(tituloFim > 0){
                    sql.append(" AND T1.CD_MATRICULA <= ");
                    sql.append(tituloFim);
                }

                if(categorias.length() > 0){
                    sql.append(" AND  T1.CD_CATEGORIA IN (");
                    sql.append(categorias.toString());
                    sql.append(")");
                }

                StringBuilder tmp = new StringBuilder();
                if(request.getParameterValues("profissoes") != null){
                    for(String s : request.getParameterValues("profissoes")){
                        tmp.append(s);
                        tmp.append(", ");
                    }
                    tmp.delete(tmp.length() -2, tmp.length());
                }

                if(tmp.length() > 0){
                    sql.append(" AND  T4.CD_PROFISSAO IN (");
                    sql.append(tmp.toString());
                    sql.append(")");
                }            

                if(cargosEspeciais.length() > 0){
                    sql.append(" AND  T1.CD_CARGO_ESPECIAL IN (");
                    sql.append(cargosEspeciais.toString());
                    sql.append(")");
                }            

                if(masculino){
                    if(feminino){
                        sql.append(" AND T1.CD_SEXO IN( 'M' , 'F' ) ");
                    }else{
                        sql.append(" AND T1.CD_SEXO = 'M' ");
                    }
                }else{
                    if(feminino){
                        sql.append(" AND T1.CD_SEXO = 'F' ");
                    }
                }

                if(di != null){
                    if(df != null){
                        sql.append(" AND T1.DT_NASCIMENTO BETWEEN '");
                        sql.append(fmt.format(di));
                        sql.append("' AND '");
                        sql.append(fmt.format(df));
                        sql.append("' ");
                    }else{
                        sql.append(" AND T1.DT_NASCIMENTO >= '");
                        sql.append(fmt.format(di));
                        sql.append("' ");
                    }
                }else{
                    if(df != null){
                        sql.append(" AND T1.DT_NASCIMENTO <= '");
                        sql.append(fmt.format(df));
                        sql.append("' ");                    
                    }
                }
                
                if (usuarioTitulo){
                    sql.append(" AND T1.CD_SIT_PESSOA = 'US' ");
                }
                
                String espolio = request.getParameter("espolio");
                if(espolio.equals("S")){
                    sql.append(" AND ISNULL(T4.IC_ESPOLIO, 'N') = 'S'");
                }else if(espolio.equals("N")){
                    sql.append(" AND ISNULL(T4.IC_ESPOLIO, 'N') = 'N'");
                }
                
        
            }//if todos || titular

            //dependentes eh usado no if a seguir e na montagem do sql abaixo
            StringBuilder dependentes = new StringBuilder();
            if(request.getParameterValues("dependentes") != null){
                for(String s : request.getParameterValues("dependentes")){
                    dependentes.append(s);
                    dependentes.append(", ");
                }
                dependentes.delete(dependentes.length() -2, dependentes.length());
            }

            if(todos || dependentes.length() > 0){
                if(todos || titular){
                    sql.append(" UNION ");
                }
        
                sql.append("SELECT ");
                sql.append(" RIGHT('0000' + convert(varchar, T1.cd_matricula),4) + '/' + RIGHT('00' + convert(varchar, T1.cd_categoria), 2) AS TITULO , ");
                sql.append(" T1.NOME_PESSOA AS NOME,");
                sql.append(" T3.DESCR_TP_DEPENDENTE AS TP_DEPENDENTE, ");
                sql.append(" CONVERT(VARCHAR(10), CONVERT(VARCHAR, T1.DT_NASCIMENTO, 103)) AS  NASC ");
                
                if(imprimirEmail){
                    sql.append(", T1.DE_EMAIL AS  EMAIL ");
                }
                if(imprimirTelefone){
                    sql.append(", T4.TELEFONE_R AS  FONE ");
                }
                
                sql.append(" FROM ");
                sql.append(" TB_PESSOA T1, ");
                sql.append(" TB_CATEGORIA T2, ");
                sql.append(" TB_TIPO_DEPENDENTE T3, ");
                sql.append(" VW_COMPLEMENTO T4 ");
                sql.append(" WHERE ");
                sql.append(" T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
                sql.append(" T1.CD_TP_DEPENDENTE = T3.CD_TP_DEPENDENTE AND ");
                sql.append(" T1.CD_MATRICULA = T4.CD_MATRICULA AND ");
                sql.append(" T1.CD_CATEGORIA = T4.CD_CATEGORIA AND ");
                sql.append(" T1.SEQ_DEPENDENTE > 0 ");
   
                if(tituloInicio > 0){
                    sql.append(" AND T1.CD_MATRICULA >= ");
                    sql.append(tituloInicio);
                }
                
                if(tituloFim > 0){
                    sql.append(" AND T1.CD_MATRICULA <= ");
                    sql.append(tituloFim);
                }
                
                if(categorias.length() > 0){
                    sql.append(" AND  T1.CD_CATEGORIA IN (");
                    sql.append(categorias.toString());
                    sql.append(")");
                }
                
                if(cargosEspeciais.length() > 0){
                    sql.append(" AND T1.CD_CARGO_ESPECIAL IN( ");
                    sql.append(cargosEspeciais.toString());
                    sql.append(")");
                }
        
                if(!todos && dependentes.length() > 0){
                    sql.append(" AND T1.CD_TP_DEPENDENTE IN( ");
                    sql.append(dependentes.toString());
                    sql.append(")");
                }
   
                if(masculino){
                    if(feminino){
                        sql.append(" AND T1.CD_SEXO IN( 'M' , 'F' ) ");
                    }else{
                        sql.append(" AND T1.CD_SEXO = 'M' ");
                    }
                }else{
                    if(feminino){
                        sql.append(" AND T1.CD_SEXO = 'F' ");
                    }
                }
   
                if(di != null){
                    if(df != null){
                        sql.append(" AND T1.DT_NASCIMENTO BETWEEN '");
                        sql.append(fmt.format(di));
                        sql.append("' AND '");
                        sql.append(fmt.format(df));
                        sql.append("' ");
                    }else{
                        sql.append(" AND T1.DT_NASCIMENTO >= '");
                        sql.append(fmt.format(di));
                        sql.append("' ");
                    }
                }else{
                    if(df != null){
                        sql.append(" AND T1.DT_NASCIMENTO <= '");
                        sql.append(fmt.format(df));
                        sql.append("' ");                    
                    }
                }
        
                boolean normal = Boolean.parseBoolean(request.getParameter("normal"));
                boolean deficiente = Boolean.parseBoolean(request.getParameter("deficiente"));            
                boolean universitario = Boolean.parseBoolean(request.getParameter("universitario"));
                Date du = Datas.parse(request.getParameter("dataUniversitario"));
                
                if(deficiente){
                    if(universitario){
                        if(normal){
                            sql.append(" AND T1.CD_CASO_ESPECIAL IN ('N', 'D', 'U')");
                        }else{
                            sql.append(" AND T1.CD_CASO_ESPECIAL IN ( 'D', 'U' ) ");
                        }
                        if(du != null){
                            sql.append(" AND (T1.DT_CASO_ESPECIAL IS NULL OR T1.DT_CASO_ESPECIAL >= '");
                            sql.append(fmt.format(du));
                            sql.append("') ");
                        }
                    }else{
                        if(normal){
                            sql.append(" AND T1.CD_CASO_ESPECIAL = 'D' ");
                        }else{
                            sql.append(" AND T1.CD_CASO_ESPECIAL IN ('D', 'N') ");
                        }
                    }
                }else{
                    if(universitario){
                        if(normal){
                            sql.append(" AND T1.CD_CASO_ESPECIAL IN ('U', 'N') ");
                        }else{
                            sql.append(" AND T1.CD_CASO_ESPECIAL = 'U' ");
                        }
                        
                        if(du != null){
                            sql.append(" AND (T1.DT_CASO_ESPECIAL IS NULL OR T1.DT_CASO_ESPECIAL >= '");
                            sql.append(fmt.format(du));
                            sql.append("') ");
                        }                        
                    }else{
                        sql.append(" AND T1.CD_CASO_ESPECIAL = 'N' ");
                    }
                }                
                
                if (usuarioTitulo){
                    sql.append(" AND T1.CD_SIT_PESSOA = 'US' ");
                }
                
                String espolio = request.getParameter("espolio");
                if(espolio.equals("S")){
                    sql.append(" AND ISNULL(T4.IC_ESPOLIO, 'N') = 'S'");
                }else if(espolio.equals("N")){
                    sql.append(" AND ISNULL(T4.IC_ESPOLIO, 'N') = 'N'");
                }
                
                
            }//if todos || dependentes.length() > 0

            if(sql.length() > 0){
                String ordenacao1 = request.getParameter("ordenacao1");
                String ordenacao2 = request.getParameter("ordenacao2");

                sql.append(" ORDER BY ");
                sql.append(ordenacao1);
                if(!ordenacao1.equals(ordenacao2)){
                    sql.append(", ");
                    sql.append(ordenacao2);
                }
                
                request.setAttribute("titulo", "Relatório de Sócios");
                request.setAttribute("titulo2", "");
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "false");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "-1");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
            }else{
                request.setAttribute("msg", "não foi escolhido nenhum parâmetro para o relatório");
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                            
            }
        }else if("gerarEtiqueta".equals(acao)){
            StringBuilder sql = new StringBuilder();
            boolean todos = Boolean.parseBoolean(request.getParameter("todos"));
            boolean titular = Boolean.parseBoolean(request.getParameter("titular"));            
            boolean imprimirTelefone = Boolean.parseBoolean(request.getParameter("imprimirTelefone"));
            boolean imprimirEmail = Boolean.parseBoolean(request.getParameter("imprimirEmail"));
            boolean masculino = Boolean.parseBoolean(request.getParameter("masculino"));
            boolean feminino = Boolean.parseBoolean(request.getParameter("feminino"));            
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date di = Datas.parse(request.getParameter("nascimentoInicio"));
            Date df = Datas.parse(request.getParameter("nascimentoFim"));
            int tituloInicio = 0;
            int tituloFim = 0;
            try{
                tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            }catch(NumberFormatException e){
                tituloInicio = 0;
            }
            try{
                tituloFim = Integer.parseInt(request.getParameter("tituloFim"));
            }catch(NumberFormatException e){
                tituloFim = 0;
            }
            
            StringBuilder categorias = new StringBuilder();
            if(request.getParameterValues("categorias") != null){
                for(String s : request.getParameterValues("categorias")){
                    categorias.append(s);
                    categorias.append(", ");
                }
                categorias.delete(categorias.length() -2, categorias.length());
            }
             
            StringBuilder cargosEspeciais = new StringBuilder();
            if(request.getParameterValues("cargosEspeciais") != null){
                for(String s : request.getParameterValues("cargosEspeciais")){
                    cargosEspeciais.append(s);
                    cargosEspeciais.append(", ");
                }
                cargosEspeciais.delete(cargosEspeciais.length() -2, cargosEspeciais.length());
            }            
            
            String cepInicio = request.getParameter("cepInicio");
            String cepFim = request.getParameter("cepFim");
            
            if(todos || titular){                
                StringBuilder where = new StringBuilder();
                where.append(" WHERE ");
                where.append(" T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
                where.append(" T1.CD_MATRICULA = T4.CD_MATRICULA AND ");
                where.append(" T1.CD_CATEGORIA = T4.CD_CATEGORIA AND ");
                where.append(" T1.SEQ_DEPENDENTE = 0 AND ");

                if(tituloInicio > 0){
                    where.append(" T1.CD_MATRICULA >= ");
                    where.append(tituloInicio);
                    where.append(" AND ");
                }
                
                if(tituloFim > 0){
                    where.append(" T1.CD_MATRICULA <= ");
                    where.append(tituloFim);
                    where.append(" AND ");
                }
                
                if(categorias.length() > 0){
                    where.append(" T1.CD_CATEGORIA IN (");
                    where.append(categorias.toString());
                    where.append(")");
                }
                
                StringBuilder tmp = new StringBuilder();
                if(request.getParameterValues("profissoes") != null){
                    for(String s : request.getParameterValues("profissoes")){
                        tmp.append(s);
                        tmp.append(", ");
                    }
                    tmp.delete(tmp.length() -2, tmp.length());
                }

                if(tmp.length() > 0){
                    where.append(" AND  T4.CD_PROFISSAO IN (");
                    where.append(tmp.toString());
                    where.append(")");
                }            
        
                if(cargosEspeciais.length() > 0){
                    where.append(" AND  T1.CD_CARGO_ESPECIAL IN (");
                    where.append(cargosEspeciais.toString());
                    where.append(")");
                }
                
                if(masculino){
                    if(feminino){
                        where.append(" AND T1.CD_SEXO IN( 'M' , 'F' ) ");
                    }else{
                        where.append(" AND T1.CD_SEXO = 'M' ");
                    }
                }else{
                    if(feminino){
                        where.append(" AND T1.CD_SEXO = 'F' ");
                    }
                }
        
                if(di != null){
                    if(df != null){
                        where.append(" AND T1.DT_NASCIMENTO BETWEEN '");
                        where.append(fmt.format(di));
                        where.append("' AND '");
                        where.append(fmt.format(df));
                        where.append("' ");
                    }else{
                        where.append(" AND T1.DT_NASCIMENTO >= '");
                        where.append(fmt.format(di));
                        where.append("' ");
                    }
                }else{
                    if(df != null){
                        where.append(" AND T1.DT_NASCIMENTO <= '");
                        where.append(fmt.format(df));
                        where.append("' ");                    
                    }
                }
        
                String espolio = request.getParameter("espolio");
                if(espolio.equals("S")){
                    where.append(" AND ISNULL(T4.IC_ESPOLIO, 'N') = 'S'");
                }else if(espolio.equals("N")){
                    where.append(" AND ISNULL(T4.IC_ESPOLIO, 'N') = 'N'");
                }
                
                
                /**************************
                '   MONTANDO SQL DO TITULAR
                '**************************/
                sql.append("SELECT ");
                sql.append(" T1.CD_MATRICULA AS MATRICULA, ");
                sql.append(" T1.CD_CATEGORIA AS CATEGORIA, ");
                sql.append(" T1.NOME_PESSOA AS NOME, ");
                sql.append(" T4.ENDERECO_R AS ENDERECO, ");
                sql.append(" T4.BAIRRO_R AS BAIRRO, ");
                sql.append(" T4.CIDADE_R AS CIDADE, ");
                sql.append(" T4.UF_R AS UF,");
                sql.append(" T4.CEP_R AS CEP,");
                sql.append(" T1.CD_SEXO AS SEXO");
                sql.append(" FROM ");
                sql.append(" TB_PESSOA T1, ");
                sql.append(" TB_CATEGORIA T2, ");
                sql.append(" TB_COMPLEMENTO T4 ");
                sql.append(where);
                
                if(cepInicio != null && !cepInicio.trim().equals("")){
                    sql.append(" AND T4.CEP_R >= '");
                    sql.append(cepInicio);
                    sql.append("'");
                }
       
                if(cepFim != null && !cepFim.trim().equals("")){
                    sql.append(" AND T4.CEP_R <= '");
                    sql.append(cepFim);
                    sql.append("'");
                }
       
                sql.append(" AND T4.CD_END_CORRESPONDENCIA = 'R'");
                sql.append(" UNION ");
                sql.append("SELECT ");
                sql.append(" T1.CD_MATRICULA AS MATRICULA, ");
                sql.append(" T1.CD_CATEGORIA AS CATEGORIA, ");
                sql.append(" T1.NOME_PESSOA AS NOME, ");
                sql.append(" T4.ENDERECO_C AS ENDERECO, ");
                sql.append(" T4.BAIRRO_C AS BAIRRO, ");
                sql.append(" T4.CIDADE_C AS CIDADE, ");
                sql.append(" T4.UF_C AS UF,");
                sql.append(" T4.CEP_C AS CEP,");
                sql.append(" T1.CD_SEXO AS SEXO");
                sql.append(" FROM ");
                sql.append(" TB_PESSOA T1, ");
                sql.append(" TB_CATEGORIA T2, ");
                sql.append(" TB_COMPLEMENTO T4 ");
                sql.append(where);
                sql.append(" AND T4.CD_END_CORRESPONDENCIA = 'C'");

                if(cepInicio != null && !cepInicio.trim().equals("")){
                    sql.append(" AND T4.CEP_C >= '");
                    sql.append(cepInicio);
                    sql.append("'");
                }
       
                if(cepFim != null && !cepFim.trim().equals("")){
                    sql.append(" AND T4.CEP_C <= '");
                    sql.append(cepFim);
                    sql.append("'");
                }

            }

            //dependentes eh usado no if a seguir e na montagem do sql abaixo
            StringBuilder dependentes = new StringBuilder();
            if(request.getParameterValues("dependentes") != null){
                for(String s : request.getParameterValues("dependentes")){
                    dependentes.append(s);
                    dependentes.append(", ");
                }
                dependentes.delete(dependentes.length() -2, dependentes.length());
            }

            if(todos || dependentes.length() > 0){
                if(todos || titular){
                    sql.append(" UNION ");
                }
                StringBuilder where = new StringBuilder();

                where.append(" WHERE ");
                where.append(" T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
                where.append(" T1.CD_TP_DEPENDENTE = T3.CD_TP_DEPENDENTE AND ");
                where.append(" T1.CD_MATRICULA = T4.CD_MATRICULA AND ");
                where.append(" T1.CD_CATEGORIA = T4.CD_CATEGORIA AND ");
                where.append(" T1.SEQ_DEPENDENTE > 0 AND");

                if(tituloInicio > 0){
                    where.append(" T1.CD_MATRICULA >= ");
                    where.append(tituloInicio);
                    where.append(" AND ");
                }
                
                if(tituloFim > 0){
                    where.append(" T1.CD_MATRICULA <= ");
                    where.append(tituloFim);
                    where.append(" AND ");
                }
        
                if(categorias.length() > 0){
                    where.append(" T1.CD_CATEGORIA IN (");
                    where.append(categorias.toString());
                    where.append(")");
                }                

                if(cargosEspeciais.length() > 0){
                    where.append(" AND  T1.CD_CARGO_ESPECIAL IN (");
                    where.append(cargosEspeciais.toString());
                    where.append(")");
                }

                if(!todos && dependentes.length() > 0){
                    where.append(" AND T1.CD_TP_DEPENDENTE IN( ");
                    where.append(dependentes.toString());
                    where.append(")");
                }
                
                if(masculino){
                    if(feminino){
                        where.append(" AND T1.CD_SEXO IN( 'M' , 'F' ) ");
                    }else{
                        where.append(" AND T1.CD_SEXO = 'M' ");
                    }
                }else{
                    if(feminino){
                        where.append(" AND T1.CD_SEXO = 'F' ");
                    }
                }
        
                if(di != null){
                    if(df != null){
                        where.append(" AND T1.DT_NASCIMENTO BETWEEN '");
                        where.append(fmt.format(di));
                        where.append("' AND '");
                        where.append(fmt.format(df));
                        where.append("' ");
                    }else{
                        where.append(" AND T1.DT_NASCIMENTO >= '");
                        where.append(fmt.format(di));
                        where.append("' ");
                    }
                }else{
                    if(df != null){
                        where.append(" AND T1.DT_NASCIMENTO <= '");
                        where.append(fmt.format(df));
                        where.append("' ");                    
                    }
                }

                boolean normal = Boolean.parseBoolean(request.getParameter("normal"));
                boolean deficiente = Boolean.parseBoolean(request.getParameter("deficiente"));            
                boolean universitario = Boolean.parseBoolean(request.getParameter("universitario"));
                Date du = Datas.parse(request.getParameter("dataUniversitario"));
                
                if(deficiente){
                    if(universitario){
                        if(normal){
                            where.append(" AND T1.CD_CASO_ESPECIAL IN ('N', 'D', 'U')");
                        }else{
                            where.append(" AND T1.CD_CASO_ESPECIAL IN ( 'D', 'U' ) ");
                        }
                        if(du != null){
                            where.append(" AND (T1.DT_CASO_ESPECIAL IS NULL OR T1.DT_CASO_ESPECIAL >= '");
                            where.append(fmt.format(du));
                            where.append("') ");
                        }
                    }else{
                        if(normal){
                            where.append(" AND T1.CD_CASO_ESPECIAL = 'D' ");
                        }else{
                            where.append(" AND T1.CD_CASO_ESPECIAL IN ('D', 'N') ");
                        }
                    }
                }else{
                    if(universitario){
                        if(normal){
                            where.append(" AND T1.CD_CASO_ESPECIAL IN ('U', 'N') ");
                        }else{
                            where.append(" AND T1.CD_CASO_ESPECIAL = 'U' ");
                        }
                        
                        if(du != null){
                            where.append(" AND (T1.DT_CASO_ESPECIAL IS NULL OR T1.DT_CASO_ESPECIAL >= '");
                            where.append(fmt.format(du));
                            where.append("') ");
                        }                        
                    }else{
                        where.append(" AND T1.CD_CASO_ESPECIAL = 'N' ");
                    }
                }                

                String espolio = request.getParameter("espolio");
                if(espolio.equals("S")){
                    where.append(" AND ISNULL(T4.IC_ESPOLIO, 'N') = 'S'");
                }else if(espolio.equals("N")){
                    where.append(" AND ISNULL(T4.IC_ESPOLIO, 'N') = 'N'");
                }
                

                /*********************************
                '       MONTANDO SQL DO DEPENDENTE
                '*********************************/
        
                sql.append("SELECT ");
                sql.append(" T1.CD_MATRICULA AS MATRICULA, ");
                sql.append(" T1.CD_CATEGORIA AS CATEGORIA, ");
                sql.append(" T1.NOME_PESSOA AS NOME, ");
                sql.append(" T4.ENDERECO_R AS ENDERECO, ");
                sql.append(" T4.BAIRRO_R AS BAIRRO, ");
                sql.append(" T4.CIDADE_R AS CIDADE, ");
                sql.append(" T4.UF_R AS UF,");
                sql.append(" T4.CEP_R AS CEP,");
                sql.append(" T1.CD_SEXO AS SEXO");
                sql.append(" FROM ");
                sql.append(" TB_PESSOA T1, ");
                sql.append(" TB_CATEGORIA T2, ");
                sql.append(" TB_TIPO_DEPENDENTE T3, ");
                sql.append(" TB_COMPLEMENTO T4 ");
                sql.append(where);
        
                if(cepInicio != null && !cepInicio.trim().equals("")){
                    sql.append(" AND T4.CEP_R >= '");
                    sql.append(cepInicio);
                    sql.append("'");
                }
       
                if(cepFim != null && !cepFim.trim().equals("")){
                    sql.append(" AND T4.CEP_R <= '");
                    sql.append(cepFim);
                    sql.append("'");
                }        
        
                sql.append(" AND T4.CD_END_CORRESPONDENCIA = 'R' ");
                sql.append(" UNION ");
                sql.append("SELECT ");
                sql.append(" T1.CD_MATRICULA , ");
                sql.append(" T1.CD_CATEGORIA , ");
                sql.append(" T1.NOME_PESSOA , ");
                sql.append(" T4.ENDERECO_C AS ENDERECO, ");
                sql.append(" T4.BAIRRO_C AS BAIRRO, ");
                sql.append(" T4.CIDADE_C AS CIDADE, ");
                sql.append(" T4.UF_C AS UF,");
                sql.append(" T4.CEP_C AS CEP,");
                sql.append(" T1.CD_SEXO");
                sql.append(" FROM ");
                sql.append(" TB_PESSOA T1, ");
                sql.append(" TB_CATEGORIA T2, ");
                sql.append(" TB_TIPO_DEPENDENTE T3, ");
                sql.append(" TB_COMPLEMENTO T4 ");
                sql.append(where);
                
                if(cepInicio != null && !cepInicio.trim().equals("")){
                    sql.append(" AND T4.CEP_C >= '");
                    sql.append(cepInicio);
                    sql.append("'");
                }
       
                if(cepFim != null && !cepFim.trim().equals("")){
                    sql.append(" AND T4.CEP_C <= '");
                    sql.append(cepFim);
                    sql.append("'");
                }        
                
        
                sql.append(" AND T4.CD_END_CORRESPONDENCIA = 'C' ");
            }
            
            request.setAttribute("sql", sql.toString());
            request.getRequestDispatcher("/pages/1930.jsp").forward(request, response);            
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.setAttribute("profissoes", ComboBoxLoader.listar("TB_PROFISSAO"));
            request.setAttribute("cargosEspeciais", ComboBoxLoader.listar("TB_CARGO_ESPECIAL"));
            request.setAttribute("dependentes", ComboBoxLoader.listar("TB_TIPO_DEPENDENTE"));
            request.getRequestDispatcher("/pages/1270.jsp").forward(request, response);
        }
    }    
    
    @App("1250")
    public static void aniversariantes(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");

        if("visualizar".equals(acao) || "gerarArquivo".equals(acao)){    
            StringBuilder sql = new StringBuilder();

            sql.append("Select Case DatePart(Day, DT_NASCIMENTO) ");
            sql.append("WHEN 1 THEN ");
            sql.append("'01' ");
            sql.append("WHEN 2 THEN ");
            sql.append("'02' ");
            sql.append("WHEN 3 THEN ");
            sql.append("'03' ");
            sql.append("WHEN 4 THEN ");
            sql.append("'04' ");
            sql.append("WHEN 5 THEN ");
            sql.append("'05' ");
            sql.append("WHEN 6 THEN ");
            sql.append("'06' ");
            sql.append("WHEN 7 THEN ");
            sql.append("'07' ");
            sql.append("WHEN 8 THEN ");
            sql.append("'08' ");
            sql.append("WHEN 9 THEN ");
            sql.append("'09' ");
            sql.append("Else ");
            sql.append("CONVERT(CHAR(2),DATEPART(DAY, DT_NASCIMENTO))  ");
            sql.append("End ");
            sql.append("+ '/' + ");
            sql.append("Case DatePart(Month, DT_NASCIMENTO) ");


            sql.append("WHEN 1 THEN  ");
            sql.append("'01' ");
            sql.append("WHEN 2 THEN ");
            sql.append("'02' ");
            sql.append("WHEN 3 THEN ");
            sql.append("'03' ");
            sql.append("WHEN 4 THEN ");
            sql.append("'04' ");
            sql.append("WHEN 5 THEN ");
            sql.append("'05' ");
            sql.append("WHEN 6 THEN ");
            sql.append("'06' ");
            sql.append("WHEN 7 THEN ");
            sql.append("'07' ");
            sql.append("WHEN 8 THEN ");
            sql.append("'08' ");
            sql.append("WHEN 9 THEN ");
            sql.append("'09' ");
            sql.append("Else ");
            sql.append("CONVERT(CHAR(2),DATEPART(MONTH, DT_NASCIMENTO)) ");
            sql.append("End ");
            sql.append("AS Dia, ");
            sql.append("NOME_PESSOA AS Nome, ");
            sql.append("SUBSTRING(CONVERT(VARCHAR, T1.CD_CATEGORIA)  + ' / ' + ");

            sql.append(" case ");
            sql.append("when T1.cd_matricula < 10 then ");
            sql.append("'000' + str(T1.cd_matricula, 1, 0) ");
            sql.append("when T1.cd_matricula < 100 and T1.cd_matricula > 9 then ");
            sql.append("'00' + str(T1.cd_matricula, 2, 0) ");
            sql.append("when T1.cd_matricula > 99 and T1.cd_matricula < 1000 then ");
            sql.append("'0' + str(T1.cd_matricula, 3, 0) ");
            sql.append("Else ");
            sql.append("str(T1.cd_matricula, 4, 0) ");
            sql.append("End, 1, 10) as  '  Título', T2.DESCR_CATEGORIA AS Categoria, ");

            sql.append(" CONVERT(VARCHAR(40), CONVERT(VARCHAR, T3.DESCR_CARGO_ESPECIAL) + ' ' + ");
            sql.append(" Case T1.CD_CARGO_ATIVO ");
            sql.append(" WHEN 'S' THEN 'ATIVO' ");
            sql.append(" WHEN 'N' THEN 'INATIVO' ");
            sql.append(" END) AS 'Cargo Especial' ");


            sql.append("From TB_PESSOA T1, TB_CATEGORIA T2, TB_CARGO_ESPECIAL T3 ");
            sql.append("WHERE DATEPART(MONTH, DT_NASCIMENTO) * 100 + DATEPART(DAY, DT_NASCIMENTO) >= ");
            sql.append(request.getParameter("dataInicio").substring(3, 5));
            sql.append(request.getParameter("dataInicio").substring(0, 2));
            sql.append(" AND  DATEPART(MONTH, DT_NASCIMENTO) * 100 + DATEPART(DAY, DT_NASCIMENTO) <= ");
            sql.append(request.getParameter("dataFim").substring(3, 5));
            sql.append(request.getParameter("dataFim").substring(0, 2));            
            
            sql.append(" AND  T1.CD_CATEGORIA in ");

            StringBuilder categorias = new StringBuilder();
            for(String s : request.getParameterValues("categorias")){
                categorias.append(s);
                categorias.append(", ");
            }
            categorias.delete(categorias.length() -2, categorias.length());            
            sql.append(" (");
            sql.append(categorias.toString());
            sql.append(") ");
            
            sql.append(" AND  CD_sexo in ");
            
            String sexo = request.getParameter("sexo");
            if(sexo.equals("T")){
                sql.append(" ('M', 'F') ");
            }else if(sexo.equals("M")){
                sql.append(" ('M') ");
            }else{
                sql.append(" ('F') ");
            }
            
            sql.append(" AND  SEQ_DEPENDENTE BETWEEN ");

            String tipo = request.getParameter("tipo");
            if(tipo.equals("T")){
                sql.append(" 0 and 9999 ");
            }else if(tipo.equals("I")){
                sql.append(" 0 and 0 ");
            }else{
                sql.append(" 1 and 9999 ");
            }
            
            String espolio = request.getParameter("espolio");
            if(espolio.equals("S")){
                sql.append(" AND EXISTS (SELECT 1 FROM TB_COMPLEMENTO T0 ");
                sql.append(" WHERE T0.CD_MATRICULA = T1.CD_MATRICULA AND T0.CD_CATEGORIA = T1.CD_CATEGORIA AND T0.SEQ_DEPENDENTE = T1.SEQ_DEPENDENTE ");
                sql.append(" AND T0.IC_ESPOLIO = 'S')");
            }else if(espolio.equals("N")){
                sql.append(" AND NOT EXISTS (SELECT 1 FROM TB_COMPLEMENTO T0 ");
                sql.append(" WHERE T0.CD_MATRICULA = T1.CD_MATRICULA AND T0.CD_CATEGORIA = T1.CD_CATEGORIA AND T0.SEQ_DEPENDENTE = T1.SEQ_DEPENDENTE ");
                sql.append(" AND T0.IC_ESPOLIO = 'S')");
            }
            
            sql.append(" AND  DT_NASCIMENTO IS NOT NULL ");
            sql.append(" AND  T1.CD_CATEGORIA = T2.CD_CATEGORIA ");
            sql.append(" AND  T1.CD_CARGO_ESPECIAL *= T3.CD_CARGO_ESPECIAL ");
            sql.append("ORDER BY 1 ");

            if("gerarArquivo".equals(acao)){
                Connection cn = Pool.getInstance().getConnection();

                BufferedWriter w = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setHeader("Content-Disposition","attachment; filename=\"aniversariantes.txt\"");
                
                w.write("Dia   Nome                                       Título   Categoria                 Cargo Especial");
                w.write("\r\n");
                w.write("----- ---------------------------------------- ---------- ------------------------- ----------------------------------------");
                w.write("\r\n");
                
                try{
                    ResultSet rs = cn.createStatement().executeQuery(sql.toString());
                    while(rs.next()){
                        w.write(rs.getString(1));
                        w.write(" ");
                        w.write(String.format("%1$-40s", rs.getString(2)));
                        w.write(" ");
                        w.write(String.format("%1$-10s", rs.getString(3)));
                        w.write(" ");
                        w.write(String.format("%1$-25s", rs.getString(4)));
                        w.write(" ");
                        w.write(String.format("%1$-40s", rs.getString(5)));                        
                        w.write("\r\n");
                    }

                }catch(SQLException e){
                    request.setAttribute("msg", e.getMessage());
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }finally{
                    w.close();
                }

            }else{
                request.setAttribute("titulo", "Aniversariantes do mês");
                request.setAttribute("titulo2", request.getParameter("dataInicio") + " até " + request.getParameter("dataFim"));
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "true");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "-1");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
            }
        }else if("gerarEtiqueta".equals(acao)){
            StringBuilder sql = new StringBuilder();
            StringBuilder where = new StringBuilder();

            where.append(" WHERE ");
            where.append(" T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
            where.append(" T1.CD_MATRICULA = T4.CD_MATRICULA AND ");
            where.append(" T1.CD_CATEGORIA = T4.CD_CATEGORIA AND ");
            where.append(" T1.SEQ_DEPENDENTE BETWEEN ");
            String tipo = request.getParameter("tipo");
            if(tipo.equals("T")){
                where.append(" 0 and 9999 ");
            }else if(tipo.equals("I")){
                where.append(" 0 and 0 ");
            }else{
                where.append(" 1 and 9999 ");
            }
            where.append(" AND DATEPART(MONTH, DT_NASCIMENTO) * 100 + DATEPART(DAY, DT_NASCIMENTO) >= ");
            where.append(request.getParameter("dataInicio").substring(3, 5));
            where.append(request.getParameter("dataInicio").substring(0, 2));
            where.append(" AND  DATEPART(MONTH, DT_NASCIMENTO) * 100 + DATEPART(DAY, DT_NASCIMENTO) <= ");
            where.append(request.getParameter("dataFim").substring(3, 5));
            where.append(request.getParameter("dataFim").substring(0, 2));    
            where.append(" AND  T1.CD_CATEGORIA in (");
            for(String s : request.getParameterValues("categorias")){
                where.append(s);
                where.append(", ");
            }
            where.delete(where.length() -2, where.length());
            where.append(") ");
            where.append(" AND  T1.CD_sexo in ");
            String sexo = request.getParameter("sexo");
            if(sexo.equals("T")){
                where.append(" ('M', 'F') ");
            }else if(sexo.equals("M")){
                where.append(" ('M') ");
            }else{
                where.append(" ('F') ");
            }

            String espolio = request.getParameter("espolio");
            if(espolio.equals("S")){
                where.append(" AND EXISTS (SELECT 1 FROM TB_COMPLEMENTO T0 ");
                where.append(" WHERE T0.CD_MATRICULA = T1.CD_MATRICULA AND T0.CD_CATEGORIA = T1.CD_CATEGORIA AND T0.SEQ_DEPENDENTE = T1.SEQ_DEPENDENTE ");
                where.append(" AND T0.IC_ESPOLIO = 'S')");
            }else if(espolio.equals("N")){
                where.append(" AND NOT EXISTS (SELECT 1 FROM TB_COMPLEMENTO T0 ");
                where.append(" WHERE T0.CD_MATRICULA = T1.CD_MATRICULA AND T0.CD_CATEGORIA = T1.CD_CATEGORIA AND T0.SEQ_DEPENDENTE = T1.SEQ_DEPENDENTE ");
                where.append(" AND T0.IC_ESPOLIO = 'S')");
            }
            
            
            sql.append("SELECT ");
            sql.append(" T1.CD_MATRICULA AS MATRICULA, ");
            sql.append(" T1.CD_CATEGORIA AS CATEGORIA, ");
            sql.append(" T1.NOME_PESSOA AS NOME, ");
            sql.append(" T4.ENDERECO_R AS ENDERECO, ");
            sql.append(" T4.BAIRRO_R AS BAIRRO, ");
            sql.append(" T4.CIDADE_R AS CIDADE, ");
            sql.append(" T4.UF_R AS UF,");
            sql.append(" T4.CEP_R AS CEP,");
            sql.append(" T1.CD_SEXO AS SEXO");
            sql.append(" FROM ");
            sql.append(" TB_PESSOA T1, ");
            sql.append(" TB_CATEGORIA T2, ");
            sql.append(" TB_COMPLEMENTO T4 ");
            sql.append(where);
            sql.append(" AND T4.CD_END_CORRESPONDENCIA = 'R'");
            sql.append(" UNION ");
            sql.append("SELECT ");
            sql.append(" T1.CD_MATRICULA AS MATRICULA, ");
            sql.append(" T1.CD_CATEGORIA AS CATEGORIA, ");
            sql.append(" T1.NOME_PESSOA AS NOME, ");
            sql.append(" T4.ENDERECO_C AS ENDERECO, ");
            sql.append(" T4.BAIRRO_C AS BAIRRO, ");
            sql.append(" T4.CIDADE_C AS CIDADE, ");
            sql.append(" T4.UF_C AS UF,");
            sql.append(" T4.CEP_C AS CEP,");
            sql.append(" T1.CD_SEXO AS SEXO");
            sql.append(" FROM ");
            sql.append(" TB_PESSOA T1, ");
            sql.append(" TB_CATEGORIA T2, ");
            sql.append(" TB_COMPLEMENTO T4 ");
            sql.append(where);
            sql.append(" AND T4.CD_END_CORRESPONDENCIA = 'C'");

            request.setAttribute("sql", sql.toString());
            request.getRequestDispatcher("/pages/1930.jsp").forward(request, response);            
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT'"));
            request.getRequestDispatcher("/pages/1250.jsp").forward(request, response);            
        }
    }    
    
    @App("1240")
    public static void dadosCadastraisCompleto(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            int tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            int tituloFinal = Integer.parseInt(request.getParameter("tituloFim"));
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            
            sql.append("SELECT TB_PESSOA.*, ");
            sql.append("TB_CATEGORIA.DESCR_CATEGORIA, ");
            sql.append("VW_COMPLEMENTO.* ");
            sql.append("FROM TB_PESSOA, TB_CATEGORIA, vw_COMPLEMENTO ");
            sql.append("WHERE TB_PESSOA.CD_MATRICULA >= ");
            sql.append(tituloInicio);
            sql.append("AND   TB_PESSOA.CD_MATRICULA <= ");
            sql.append(tituloFinal);
            sql.append("AND   TB_PESSOA.SEQ_DEPENDENTE = 0 ");
            sql.append("AND   TB_PESSOA.CD_CATEGORIA = TB_CATEGORIA.CD_CATEGORIA ");
            sql.append("AND   TB_PESSOA.CD_MATRICULA = vw_COMPLEMENTO.CD_MATRICULA ");
            sql.append("AND   TB_PESSOA.CD_CATEGORIA = vw_COMPLEMENTO.CD_CATEGORIA ");
            sql.append("AND TB_PESSOA.CD_CATEGORIA in (");
            
            for(String s : request.getParameterValues("categorias")){
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() -2, sql.length());            
            sql.append(") ");    
            
            if(d1 != null){
                sql.append(" AND DT_INCL_PESSOA >= '");
                sql.append(fmt.format(d1));
                sql.append("' ");
            }
            
            if(d2 != null){
                sql.append(" AND DT_INCL_PESSOA <= '");
                sql.append(fmt.format(d2));
                sql.append("' ");
            }            

            if(request.getParameter("ordenacao").equals("N")){
                sql.append(" ORDER BY TB_PESSOA.NOME_PESSOA");
            }else{
                sql.append(" ORDER BY TB_PESSOA.CD_MATRICULA");
            }

            request.setAttribute("dadosResidenciais", request.getParameter("dadosResidenciais"));
            request.setAttribute("dadosComerciais", request.getParameter("dadosComerciais"));
            request.setAttribute("dadosBancarios", request.getParameter("dadosBancarios"));
            request.setAttribute("dependentes", request.getParameter("dependentes"));
            request.setAttribute("embarcacoes", request.getParameter("embarcacoes"));
            request.setAttribute("sql", sql.toString());
            request.getRequestDispatcher("/pages/1240-impressao.jsp").forward(request, response);
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT'"));
            request.getRequestDispatcher("/pages/1240.jsp").forward(request, response);
        }
    }
    
    @App("1640")
    public static void dadosCadastraisSimplificado(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            int tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            int tituloFinal = Integer.parseInt(request.getParameter("tituloFim"));
            boolean titular = Boolean.parseBoolean(request.getParameter("titular"));
            boolean dependente = Boolean.parseBoolean(request.getParameter("dependente"));
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            
            sql.append("SELECT TB_PESSOA.*, TB_PESSOA.SEQ_DEPENDENTE AS ID_DEPENDENTE, ");
            sql.append("TB_CATEGORIA.DESCR_CATEGORIA, ");
            sql.append("VW_COMPLEMENTO.* ");
            sql.append("FROM TB_PESSOA, TB_CATEGORIA, VW_COMPLEMENTO ");
            sql.append("WHERE TB_PESSOA.CD_MATRICULA >= ");
            sql.append(tituloInicio);
            sql.append("AND   TB_PESSOA.CD_MATRICULA <= ");
            sql.append(tituloFinal);
            sql.append("AND   TB_PESSOA.CD_CATEGORIA = TB_CATEGORIA.CD_CATEGORIA ");
            sql.append("AND   TB_PESSOA.CD_MATRICULA = VW_COMPLEMENTO.CD_MATRICULA ");
            sql.append("AND   TB_PESSOA.CD_CATEGORIA = VW_COMPLEMENTO.CD_CATEGORIA ");
            sql.append("AND TB_PESSOA.CD_CATEGORIA in (");
            
            for(String s : request.getParameterValues("categorias")){
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() -2, sql.length());            
            sql.append(") ");    
        
            if(titular){
                if(!dependente){
                    sql.append(" AND TB_PESSOA.SEQ_DEPENDENTE = 0 ");
                }
            }else{
                sql.append(" AND TB_PESSOA.SEQ_DEPENDENTE > 0 ");
            }

            String nome = request.getParameter("nome");
            if(nome != null && !nome.trim().equals("")){
                sql.append(" AND TB_PESSOA.NOME_PESSOA LIKE '");
                sql.append(nome.trim().toUpperCase());
                sql.append("%'");
            }
        
            if(d1 != null){
                sql.append(" AND DT_INCL_PESSOA >= '");
                sql.append(fmt.format(d1));
                sql.append("' ");
            }
            
            if(d2 != null){
                sql.append(" AND DT_INCL_PESSOA <= '");
                sql.append(fmt.format(d2));
                sql.append("' ");
            }            

            if(request.getParameter("ordenacao").equals("N")){
                sql.append(" ORDER BY TB_PESSOA.NOME_PESSOA");
            }else{
                sql.append(" ORDER BY TB_PESSOA.CD_MATRICULA");
            }

            request.setAttribute("titular", request.getParameter("titular"));
            request.setAttribute("dependente", request.getParameter("dependente"));
            request.setAttribute("sql", sql.toString());
            request.getRequestDispatcher("/pages/1640-impressao.jsp").forward(request, response);
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT'"));
            request.getRequestDispatcher("/pages/1640.jsp").forward(request, response);
        }
    }
    
    @App("6120")
    public static void extrato(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            DecimalFormat f1 = new DecimalFormat("00");
            DecimalFormat f2 = new DecimalFormat("0000");            
            Titular t = Titular.getInstance(matricula, idCategoria);
            
            sql.append("EXEC SP_EXTRATO_SOCIO ");
            sql.append(matricula);
            sql.append(", ");
            sql.append(idCategoria);
            if(request.getParameter("tipo").equals("I")){
                sql.append(", 1");
            }else{
                sql.append(", 0");
            }
            request.setAttribute("titulo", t.getSocio().getNome() + " / Título:" + f1.format(idCategoria) + "-" + f2.format(matricula));
            request.setAttribute("titulo2", "Valores em atraso são atualizados pelo INPC, juros de mora 1% a.m. acrescido da multa de 10%");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "4");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");
            request.setAttribute("extratoSocio", "SIM");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else if("matricial".equals(acao)){
            ArrayList<String> linhas = new ArrayList<String>();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyMMddHHmm");
            DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
            DecimalFormat dfBanco = new DecimalFormat( "00000" );
            DecimalFormat dfCheque = new DecimalFormat( "000000" );
            String valTemp = "";
            StringBuilder sql = new StringBuilder();
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            DecimalFormat f1 = new DecimalFormat("00");
            DecimalFormat f2 = new DecimalFormat("0000");            
            Titular t = Titular.getInstance(matricula, idCategoria);
            float vrTotal = 0;

            linhas.add("  ");
            linhas.add("IATE CLUBE DE BRASILIA");
            linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
            linhas.add("CNPJ: 00 018 978/0001-80");
            linhas.add("-------------------------------------");
            linhas.add(" ");
            linhas.add("      *** EXTRATO DO SOCIO *** ");
            linhas.add(" ");
            if (t.getSocio().getNome().length()>28){
                linhas.add(" Socio: " + t.getSocio().getNome().substring(0, 28));
                linhas.add("        " + t.getSocio().getNome().substring(28));
            }else{
                linhas.add(" Socio: " + t.getSocio().getNome());
            }
            linhas.add(" Titulo: " + f1.format(t.getSocio().getIdCategoria()) + "-" + f2.format(t.getSocio().getMatricula()));
            linhas.add(" ");


            sql.append("EXEC SP_EXTRATO_SOCIO ");
            sql.append(matricula);
            sql.append(", ");
            sql.append(idCategoria);
            if(request.getParameter("tipo").equals("I")){
                sql.append(", 1");
            }else{
                sql.append(", 0");
            }
            
            Connection cn = Pool.getInstance().getConnection();
            ResultSet rs = null;
            try{
                rs = cn.createStatement().executeQuery(sql.toString());
                if (!rs.next()){
                    linhas.add(" ****** NAO CONSTAM DEBITOS ******");
                }else{
                    linhas.add(" DATA: " + rs.getString(1));
                    valTemp = "            " + df.format(rs.getFloat(2));
                    linhas.add(" VR. CARNE:    " + valTemp.substring(valTemp.length()-12));
                    valTemp = "            " + df.format(rs.getFloat(3));
                    linhas.add(" VR. ENCARGOS: " + valTemp.substring(valTemp.length()-12));
                    valTemp = "            " + df.format(rs.getFloat(4));
                    linhas.add(" VR. TOTAL:    " + valTemp.substring(valTemp.length()-12));
                    linhas.add(" ");
                    vrTotal = vrTotal + rs.getFloat(4);
                    while(rs.next()){
                        linhas.add(" DATA: " + rs.getString(1));
                        valTemp = "            " + df.format(rs.getFloat(2));
                        linhas.add(" VR. CARNE:    " + valTemp.substring(valTemp.length()-12));
                        valTemp = "            " + df.format(rs.getFloat(3));
                        linhas.add(" VR. ENCARGOS: " + valTemp.substring(valTemp.length()-12));
                        valTemp = "            " + df.format(rs.getFloat(4));
                        linhas.add(" VR. TOTAL:    " + valTemp.substring(valTemp.length()-12));
                        linhas.add(" ");
                        vrTotal = vrTotal + rs.getFloat(4);
                    }
                }
                rs.close();
                
            }catch(SQLException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }finally{
                try {
                    cn.close();
                } catch (SQLException ex) {

                }
            }
            
            linhas.add(" ");
            valTemp = "            " + df.format(vrTotal);
            linhas.add(" TOTAL DEVIDO: " + valTemp.substring(valTemp.length()-12));
            
            linhas.add(" ");
            linhas.add(" VALORES EM ATRASO SAO ATUALIZADOS");
            linhas.add(" PELO INPC, JUROS DE MORA 1% A.M. ");
            linhas.add(" ACRESCIDO DE MULTA DE 10%");
            
            linhas.add(" ");
            sql.setLength(0);
            sql.append(" SELECT NU_LOTE FROM TB_ELEICAO WHERE CD_MATRICULA = " + matricula + " AND CD_CATEGORIA = " + idCategoria); 
            
            try{
                cn = Pool.getInstance().getConnection();
                rs = cn.createStatement().executeQuery(sql.toString());
                if (rs.next()){
                    linhas.add(" Mesa de Votacao: " + rs.getString(1));
                }else{
                    linhas.add(" Impossibilitado de votar");
                }
                
            }catch(SQLException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }finally{
                try {
                    cn.close();
                } catch (SQLException ex) {

                }
            }

            linhas.add(" ");
            linhas.add("               - o -");
            linhas.add("                 *");
            linhas.add("                 *");
            linhas.add("                 *");
            linhas.add("                 *");
            linhas.add("                 *");
            linhas.add("                 *");
            linhas.add("                 *");
            linhas.add("                 *");
            linhas.add("                 *");
            linhas.add("                 *");

            JobMiniImpressora job = new JobMiniImpressora();
            job.texto = linhas;
            try {        
                Socket sock = new Socket(request.getRemoteAddr(), 49001);
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                out.writeObject(job);
                out.close();
                response.sendRedirect("c?app=6120&acao=tela&matricula="+matricula+"&idCategoria="+idCategoria+"&tipo="+request.getParameter("tipo"));
            }catch(Exception ex) {
                request.setAttribute("app", "6000");
                request.setAttribute("msg", ex.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        
            
            
        }else if("tela".equals(acao)){
            request.setAttribute("matricula", request.getParameter("matricula"));
            request.setAttribute("idCategoria", request.getParameter("idCategoria"));
            request.setAttribute("acao", acao); 
            
            request.setAttribute("s", Socio.getInstance(Integer.parseInt(request.getParameter("matricula")), 0, Integer.parseInt(request.getParameter("idCategoria"))));
            
            request.setAttribute("menu", request.getParameter("menu"));
            request.getRequestDispatcher("/pages/6120.jsp").forward(request, response);
        }else{
            request.setAttribute("menu", request.getParameter("menu"));
            request.getRequestDispatcher("/pages/6120.jsp").forward(request, response);
        }
    
    }
    
    @App("1370")
    public static void cobranca(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao) || "carta".equals(acao)){
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date dataRef = Datas.parse(request.getParameter("dataRef"));
            boolean mostrarProprietario = Boolean.parseBoolean(request.getParameter("mostrarProprietario"));
            int qtInicio = 1;
            int qtFim = 0;
            int valorInicio = 0;
            int valorFim = 0;
            int tituloInicio = 0;
            int tituloFim = 0;
            try{
                qtInicio = Integer.parseInt(request.getParameter("qtInicio"));
            }catch(NumberFormatException e){
                qtInicio = 1;
            }
            try{
                qtFim = Integer.parseInt(request.getParameter("qtFim"));
            }catch(NumberFormatException e){
                qtFim = Integer.MAX_VALUE;
            }
            try{
                valorInicio = Integer.parseInt(request.getParameter("valorInicio"));
            }catch(NumberFormatException e){
                valorInicio = 0;
            }
            try{
                valorFim = Integer.parseInt(request.getParameter("valorFim"));
            }catch(NumberFormatException e){
                valorFim = (int)Float.MAX_VALUE;
            }
            try{
                tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            }catch(NumberFormatException e){
                tituloInicio = 0;
            }
            try{
                tituloFim = Integer.parseInt(request.getParameter("tituloFim"));
            }catch(NumberFormatException e){
                tituloFim = 0;
            }

            StringBuilder categorias = new StringBuilder();
            for(String s : request.getParameterValues("categorias")){
                categorias.append(s);
                categorias.append(", ");
            }
            categorias.delete(categorias.length() -2, categorias.length());            
            
            StringBuilder cursos = new StringBuilder();
            if(request.getParameterValues("cursos") != null){
                for(String s : request.getParameterValues("cursos")){
                    cursos.append(s);
                    cursos.append(", ");
                }
                cursos.delete(cursos.length() -2, cursos.length());
            }
            
            sql.append("EXEC SP_COBRANCA_SOCIO '");
            sql.append(categorias);
            sql.append("', '");
            sql.append(fmt.format(dataRef));
            sql.append("', ");
            sql.append(tituloInicio);
            sql.append(", ");
            sql.append(tituloFim);
            if(mostrarProprietario){
                sql.append(", 1, '");
            }else{
                sql.append(", 0, '");
            }
            sql.append(cursos);
            sql.append("', ");
            if(request.getParameter("tipo").equals("I")){
                sql.append("1, ");
            }else{
                sql.append("0, ");
            }
            sql.append(qtInicio);
            sql.append(", ");
            sql.append(qtFim);
            sql.append(", ");
            sql.append(valorInicio);
            sql.append(", ");            
            sql.append(valorFim);
            
            
            request.setAttribute("sql", sql);
            
            if ("visualizar".equals(acao)){
                request.setAttribute("titulo", "Relatório de Cobrança");
                request.setAttribute("titulo2", "");
                request.setAttribute("quebra1", "false");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "4");
                request.setAttribute("total2", "5");
                request.setAttribute("total3", "6");
                request.setAttribute("total4", "-1");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
            }else{
                request.setAttribute("sql", sql);
                
                request.setAttribute("cartaDono", CartaCobranca.getInstance(request.getParameter("cartaDono")) );
                request.setAttribute("cartaUsuario", CartaCobranca.getInstance(request.getParameter("cartaUsuario")) );
                
                request.getRequestDispatcher("/pages/1370-carta.jsp").forward(request, response);
            }
            
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.setAttribute("cartas", ComboBoxLoader.listarSql("SELECT DESCR_CARTA_COBRANCA, DESCR_CARTA_COBRANCA FROM TB_CARTA_COBRANCA_NOVA ORDER BY 1"));
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.getRequestDispatcher("/pages/1370.jsp").forward(request, response);
        }
    }
    
    @App("1990")
    public static void etiquetaManual(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("gerarEtiqueta".equals(acao)){
            StringBuilder sql = new StringBuilder();
            StringBuilder where = new StringBuilder();
            
            where.append(" WHERE ");
            where.append(" T1.CD_CATEGORIA = T2.CD_CATEGORIA AND ");
            where.append(" T1.CD_MATRICULA = T4.CD_MATRICULA AND ");
            where.append(" T1.CD_CATEGORIA = T4.CD_CATEGORIA AND ");
            where.append(" T1.SEQ_DEPENDENTE = 0 AND ");

            String[] convites = request.getParameterValues("convites");
            String[] convite = convites[0].split("/");
            where.append("((T1.CD_MATRICULA = ");
            where.append(convite[1]);
            where.append(" AND T1.CD_CATEGORIA = ");
            where.append(convite[0]);
            where.append(")");
            
            for(int i = 1; i < convites.length; i++){
                convite = convites[i].split("/");
                where.append(" OR (T1.CD_MATRICULA = ");
                where.append(convite[1]);
                where.append(" AND T1.CD_CATEGORIA = ");
                where.append(convite[0]);
                where.append(")");
            }
            where.append(")");

            sql.append("SELECT ");
            sql.append(" T1.CD_MATRICULA AS MATRICULA, ");
            sql.append(" T1.CD_CATEGORIA AS CATEGORIA, ");
            sql.append(" T1.NOME_PESSOA AS NOME, ");
            sql.append(" T4.ENDERECO_R AS ENDERECO, ");
            sql.append(" T4.BAIRRO_R AS BAIRRO, ");
            sql.append(" T4.CIDADE_R AS CIDADE, ");
            sql.append(" T4.UF_R AS UF,");
            sql.append(" T4.CEP_R AS CEP,");
            sql.append(" T1.CD_SEXO AS SEXO");
            sql.append(" FROM ");
            sql.append(" TB_PESSOA T1, ");
            sql.append(" TB_CATEGORIA T2, ");
            sql.append(" TB_COMPLEMENTO T4 ");
            sql.append(where);
            sql.append(" AND T4.CD_END_CORRESPONDENCIA = 'R'");
            sql.append(" UNION ");
            sql.append("SELECT ");
            sql.append(" T1.CD_MATRICULA AS MATRICULA, ");
            sql.append(" T1.CD_CATEGORIA AS CATEGORIA, ");
            sql.append(" T1.NOME_PESSOA AS NOME, ");
            sql.append(" T4.ENDERECO_C AS ENDERECO, ");
            sql.append(" T4.BAIRRO_C AS BAIRRO, ");
            sql.append(" T4.CIDADE_C AS CIDADE, ");
            sql.append(" T4.UF_C AS UF,");
            sql.append(" T4.CEP_C AS CEP,");
            sql.append(" T1.CD_SEXO AS SEXO");
            sql.append(" FROM ");
            sql.append(" TB_PESSOA T1, ");
            sql.append(" TB_CATEGORIA T2, ");
            sql.append(" TB_COMPLEMENTO T4 ");
            sql.append(where);
            sql.append(" AND T4.CD_END_CORRESPONDENCIA = 'C'");

            request.setAttribute("sql", sql.toString());
            request.getRequestDispatcher("/pages/1930.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("/pages/1990.jsp").forward(request, response);
        }
    }

    @App("2600")
    public static void relQuadroSocialCarne(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat fmtBD = new SimpleDateFormat("yyyy-MM-dd");

            Date dtReferenciaPessoa = Datas.parse(request.getParameter("dtReferenciaPessoa"));
            Date dtReferenciaCarne = Datas.parse(request.getParameter("dtReferenciaCarne"));
            Date dtVencimentoCarne = Datas.parse(request.getParameter("dtVencimentoCarne"));
            
            
            sql.append("EXEC SP_REL_QUADRO_SOCIAL_CARNE '");
            sql.append(fmtBD.format(dtReferenciaPessoa));
            sql.append(" 23:59:59', '");
            sql.append(fmtBD.format(dtReferenciaCarne));
            sql.append(" 23:59:59', '");
            sql.append(fmtBD.format(dtVencimentoCarne));
            sql.append("'");
            
            request.setAttribute("sql", sql);
            request.setAttribute("titulo", "Relatório do Quadro Social x Financeiro");
            request.setAttribute("titulo2", "Dt. Ref. Pessoa: " + fmt.format(dtReferenciaPessoa) + " - Dt. Ref. Carne: " + fmt.format(dtReferenciaCarne) + " - Dt. Venc. Carne: " + fmt.format(dtVencimentoCarne) );
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else{

            request.getRequestDispatcher("/pages/2600.jsp").forward(request, response);
            
        }
    }

    
    @App("2630")
    public static void relQuadSocialIdade(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){    
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date dtReferencia = Datas.parse(request.getParameter("dtReferencia"));
            boolean titular = Boolean.parseBoolean(request.getParameter("titular"));
            boolean dependente = Boolean.parseBoolean(request.getParameter("dependente"));
            
            StringBuilder sql = new StringBuilder();
            StringBuilder tmp = new StringBuilder();
            String categorias = null;
            for(String s : request.getParameterValues("categorias")){
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() -2, tmp.length());
            
            sql.append("EXEC SP_REL_QUADRO_SOCIAL_IDADE '");
            sql.append(fmt.format(dtReferencia));
            sql.append("', ");
            
            if (titular){
                sql.append("'S', ");
            }else{
                sql.append("'N', ");
            }
            if (dependente){
                sql.append("'S', '");
            }else{
                sql.append("'N', '");
            }
            
            sql.append(tmp);
            sql.append("'");

            request.setAttribute("titulo", "Relatório do Quadro Social por Idade e Sexo");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.getRequestDispatcher("/pages/2630.jsp").forward(request, response);            
        }
    }    

 @App("2690")
    public static void enderecoRepetido(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            StringBuilder categorias = new StringBuilder();
            
            for(String s : request.getParameterValues("categorias")){
                categorias.append(s);
                categorias.append(", ");
            }
            categorias.delete(categorias.length() -2, categorias.length());            
            
            String tipoEndereco = request.getParameter("tipoEndereco");
            String endereco = "N";
            String bairro = "N";
            String cidade = "N";
            String uf = "N";
            String cep = "N";
            
            if (Boolean.parseBoolean(request.getParameter("endereco"))){
                endereco = "S";
            }
            if (Boolean.parseBoolean(request.getParameter("bairro"))){
                bairro = "S";
            }
            if (Boolean.parseBoolean(request.getParameter("cidade"))){
                cidade = "S";
            }
            if (Boolean.parseBoolean(request.getParameter("uf"))){
                uf = "S";
            }
            if (Boolean.parseBoolean(request.getParameter("cep"))){
                cep = "S";
            }

            sql.append("EXEC SP_REL_END_REPETIDO '");
            sql.append(categorias);
            sql.append("', '");
            sql.append(tipoEndereco);
            sql.append("', '");
            sql.append(endereco);
            sql.append("', '");
            sql.append(bairro);
            sql.append("', '");
            sql.append(cidade);
            sql.append("', '");
            sql.append(uf);
            sql.append("', '");
            sql.append(cep);
            sql.append("'");
            
            request.setAttribute("titulo", "Relatório de Endereços Duplicados");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql.toString());
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT'"));
            request.getRequestDispatcher("/pages/2690.jsp").forward(request, response);
        }
    }    
    
}
