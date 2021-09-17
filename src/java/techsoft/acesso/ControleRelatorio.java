package techsoft.acesso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.tabelas.Cargo;
import techsoft.tabelas.Categoria;
import techsoft.tabelas.Curso;
import techsoft.tabelas.Setor;
import techsoft.tabelas.TipoConvite;
import techsoft.util.Datas;

@Controller
public class ControleRelatorio {

    @App("1490")
    public static void autorizacaoEntrada(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){   
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT ");
            sql.append("T1.DESCR_LOCAL_ACESSO AS Local, ");
            sql.append("T2.NO_AUTORIZADO as Pessoa, ");
            sql.append("T2.DT_AUTORIZACAO as 'Data/Hora', ");
            sql.append("T2.USER_ACESSO_SISTEMA as Operador ");
            sql.append("FROM ");
            sql.append("TB_LOCAL_ACESSO T1, ");
            sql.append("TB_AUTORIZACAO_ENTRADA T2 ");
            sql.append("WHERE ");
            sql.append("T1.CD_LOCAL_ACESSO = T2.CD_LOCAL_ACESSO AND ");
                    
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            
            sql.append("T2.DT_AUTORIZACAO >= '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00' AND ");
            sql.append("T2.DT_AUTORIZACAO <= '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59' AND ");
            sql.append("T1.CD_LOCAL_ACESSO IN (");
            
            for(String s : request.getParameterValues("locais")){
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() - 2, sql.length());
            
            sql.append(") ");
            sql.append("ORDER BY 1, 3");

            request.setAttribute("titulo", "Relatório de Autorizações de Entrada");
            request.setAttribute("titulo2", request.getParameter("dataInicio")
                    + " a " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            request.setAttribute("locais", ComboBoxLoader.listar("TB_LOCAL_ACESSO"));
            request.getRequestDispatcher("/pages/1490.jsp").forward(request, response);                        
        }
    }
    
    @App("1290")
    public static void acessoClube(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            String relatorio = request.getParameter("imprimir");
            StringBuilder sql = new StringBuilder();
            int idLocalAcesso = 0;
            String tipoFiltro = request.getParameter("tipoFiltro");
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy");
            
            try{
                idLocalAcesso = Integer.parseInt(request.getParameter("idLocalAcesso"));
            }catch(NumberFormatException e){
                idLocalAcesso = 0;
            }
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));            
            
            StringBuilder tiposAcesso = new StringBuilder("(");
            for(String s : request.getParameterValues("tiposAcesso")){
                tiposAcesso.append("'");
                tiposAcesso.append(s);
                tiposAcesso.append("', ");
            }
            tiposAcesso.delete(tiposAcesso.length() -2, tiposAcesso.length());
            tiposAcesso.append(")");

            String nome = request.getParameter("nome");
            boolean ordenarPorNome = request.getParameter("ordenacao").equals("N");
                
            if(relatorio.equals("carteira") && tipoFiltro.equals("N")){
                sql.append("SELECT ");
                sql.append("T4.DESCR_LOCAL_ACESSO as Local, ");
                sql.append(" SUBSTRING(CONVERT(VARCHAR, T3.CD_CATEGORIA)  + ' / ' + ");
                sql.append(" case ");
                sql.append(" when T3.cd_matricula < 10 then ");
                sql.append("'000' + str(T3.cd_matricula, 1, 0) ");
                sql.append(" when T3.cd_matricula < 100 and T3.cd_matricula > 9 then ");
                sql.append("'00' + str(T3.cd_matricula, 2, 0) ");
                sql.append(" when T3.cd_matricula > 99 and T3.cd_matricula < 1000 then ");
                sql.append("'0' + str(T3.cd_matricula, 3, 0) ");
                sql.append(" Else ");
                sql.append("str(T3.cd_matricula, 4, 0) ");
                sql.append("End, 1, 10) as  '  Título', ");
                sql.append("T3.NOME_PESSOA AS 'Nome', ");
                sql.append("T1.DT_HISTORICO_ACESSO as 'Data/Hora', ");
                sql.append("T1.DE_PLACA_CARRO as 'Placa', ");
                sql.append("T1.QT_PESSOAS AS 'Qt.Pessoas', ");
                sql.append("T1.USER_ACESSO_SISTEMA AS 'Usuário' ");
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_HIST_EMISSAO_CARTEIRA T2, ");
                sql.append("TB_PESSOA T3, ");
                sql.append("TB_LOCAL_ACESSO T4 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T2.NR_CARTEIRA ");
                sql.append("AND   T2.CD_MATRICULA = T3.CD_MATRICULA ");
                sql.append("AND   T2.CD_CATEGORIA = T3.CD_CATEGORIA ");
                sql.append("AND   T2.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE ");
                sql.append("AND   T1.CD_LOCAL_ACESSO = T4.CD_LOCAL_ACESSO ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");
          
                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }
          
                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                sql.append(" AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }

                sql.append(" AND   T3.CD_CATEGORIA IN (");

                for(String s : request.getParameterValues("categorias")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");

                int titulo = 0;
                try{
                    titulo = Integer.parseInt(request.getParameter("tituloCarteira"));
                }catch(Exception e){
                    titulo = 0;
                }
            
                if(titulo > 0){
                    sql.append("AND T3.CD_MATRICULA = ");
                    sql.append(titulo);
                }

                boolean masculino = Boolean.parseBoolean(request.getParameter("masculinoCarteira"));
                boolean feminino = Boolean.parseBoolean(request.getParameter("femininoCarteira"));
                sql.append(" AND T3.CD_SEXO IN (");
                if(masculino){
                    sql.append("'M', ");
                }
                if(feminino){
                    sql.append("'F', ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");

                boolean titular = Boolean.parseBoolean(request.getParameter("titular"));
                boolean dependente = Boolean.parseBoolean(request.getParameter("dependente"));
   
                if(!titular || !dependente){
                    if(titular){
                        sql.append("AND T3.SEQ_DEPENDENTE = 0 ");
                    }else{
                        sql.append("AND T3.SEQ_DEPENDENTE > 0 ");
                    }
                }
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T3.NOME_PESSOA LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }

                if(ordenarPorNome){
                    sql.append("order by 1, 3");
                }else{
                    sql.append("order by 1, T1.DT_HISTORICO_ACESSO");
                }

                request.setAttribute("titulo", "Relatório de Acesso ao Clube - Carteira");
                request.setAttribute("titulo2", "");
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "true");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "5");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
            }else if(relatorio.equals("passaporte") && tipoFiltro.equals("N")){
                sql.append("SELECT T6.DESCR_LOCAL_ACESSO AS Local, T3.NOME_PESSOA as 'Nome', ");
                sql.append("T1.DT_HISTORICO_ACESSO   as 'Data/Hora', ");
                sql.append("T1.DE_PLACA_CARRO as 'Placa', ");
                sql.append("T1.QT_PESSOAS AS 'Qt.Pessoas', ");
                sql.append("T1.USER_ACESSO_SISTEMA AS 'Usuário' ");
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_PESSOA T3, ");
                sql.append("TB_TURMA T4, ");
                sql.append("TB_MATRICULA_CURSO T5, ");
                sql.append("TB_LOCAL_ACESSO T6 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T5.NR_PASSAPORTE ");
                sql.append("AND   T3.CD_MATRICULA = T5.CD_MATRICULA ");
                sql.append("AND   T3.CD_CATEGORIA = T5.CD_CATEGORIA ");
                sql.append("AND   T3.SEQ_DEPENDENTE = T5.SEQ_DEPENDENTE ");
                sql.append("AND   T5.SEQ_TURMA = T4.SEQ_TURMA ");
                sql.append("AND   T1.CD_LOCAL_ACESSO = T6.CD_LOCAL_ACESSO ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }
 
 
                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }

                sql.append(" AND   T4.CD_CURSO IN (");
                for(String s : request.getParameterValues("cursos")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                

                int titulo = 0;
                try{
                    titulo = Integer.parseInt(request.getParameter("tituloPassaporte"));
                }catch(Exception e){
                    titulo = 0;
                }
            
                if(titulo > 0){
                    sql.append("AND T3.CD_MATRICULA = ");
                    sql.append(titulo);
                }

                boolean masculino = Boolean.parseBoolean(request.getParameter("masculinoPassaporte"));
                boolean feminino = Boolean.parseBoolean(request.getParameter("femininoPassaporte"));
                sql.append(" AND T3.CD_SEXO IN (");
                if(masculino){
                    sql.append("'M', ");
                }
                if(feminino){
                    sql.append("'F', ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T3.NOME_PESSOA LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }

                if(ordenarPorNome){
                    sql.append("order by 1, 3");
                }else{
                    sql.append("order by 1, T1.DT_HISTORICO_ACESSO");
                }

                request.setAttribute("titulo", "Relatório de Acesso ao Clube - Passaporte");
                request.setAttribute("titulo2", "");
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "true");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "4");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
            }else if(relatorio.equals("convite") && tipoFiltro.equals("N")){
                sql.append("SELECT T3.DESCR_LOCAL_ACESSO AS Local, T2.NOME_CONVIDADO AS 'Convidado', ");
                sql.append("T2.NOME_SACADOR AS 'Sacador', ");
                sql.append("T4.DESCR_TIPO_CONVITE AS 'Tipo', ");
                sql.append("T1.USER_ACESSO_SISTEMA AS 'Usuário' ");
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_CONVITE T2, ");
                sql.append("TB_LOCAL_ACESSO T3, ");
                sql.append("TB_TIPO_CONVITE T4 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T2.NR_CONVITE ");
                sql.append("AND   T1.CD_LOCAL_ACESSO = T3.CD_LOCAL_ACESSO ");
                sql.append("AND   T2.CD_CATEGORIA_CONVITE = T4.CD_TIPO_CONVITE ");        
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }        

                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }
                
                sql.append(" AND T2.CD_CATEGORIA_CONVITE IN (");
                for(String s : request.getParameterValues("tiposConvite")){
                    sql.append("'");
                    sql.append(s);
                    sql.append("', ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                
                boolean vendido = Boolean.parseBoolean(request.getParameter("convitePagoVendido"));
                boolean normal = Boolean.parseBoolean(request.getParameter("convitePagoNormal"));           
                sql.append("AND   T2.CD_TIPO_CONVITE IN (");
                if(vendido){
                    sql.append("'V', ");
                }
                if(normal){
                    sql.append("'N', ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T2.NOME_CONVIDADO LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }
                
                String cpfConvidado = request.getParameter("cpfConvidado");
                if(cpfConvidado != null && !cpfConvidado.trim().equals("")){
                    sql.append("AND T2.CPF_CONVIDADO = '");
                    sql.append(cpfConvidado);
                    sql.append("' ");
                }

                if(ordenarPorNome){
                    sql.append("order by 1, 3");
                }else{
                    sql.append("order by 1, T1.DT_HISTORICO_ACESSO");
                }

                request.setAttribute("titulo", "Relatório de Acesso ao Clube - Convite");
                request.setAttribute("titulo2", "");
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "true");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "-1");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
            }else if(relatorio.equals("funcionario") && tipoFiltro.equals("N")){
                sql.append("SELECT T5.DESCR_LOCAL_ACESSO AS Local, SUBSTRING(T2.NOME_FUNCIONARIO, 1, 35) as 'Nome', ");
                sql.append("SUBSTRING(T3.DESCR_CARGO, 1, 22) as 'Cargo', ");
                sql.append("SUBSTRING(T4.DESCR_SETOR, 1, 22) as 'Setor', ");
                sql.append("T1.DT_HISTORICO_ACESSO as 'Data/Hora', ");
                sql.append("T1.DE_PLACA_CARRO as 'Placa', ");
                sql.append("T1.QT_PESSOAS AS 'Qt.Pessoas', ");
                sql.append("T1.USER_ACESSO_SISTEMA AS 'Usuário' ");
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_FUNCIONARIO T2, ");
                sql.append("TB_CARGO_FUNCAO T3, ");
                sql.append("TB_SETOR T4, ");
                sql.append("TB_LOCAL_ACESSO T5 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T2.NR_CRACHA ");
                sql.append("AND   T2.CD_CARGO = T3.CD_CARGO ");
                sql.append("AND   T2.CD_SETOR = T4.CD_SETOR ");
                sql.append("AND   T1.CD_LOCAL_ACESSO = T5.CD_LOCAL_ACESSO ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }        

                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }
                
                sql.append("AND   T2.CD_CARGO IN (");
                for(String s : request.getParameterValues("cargos")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                
                
                sql.append("AND   T2.CD_SETOR IN (");
                for(String s : request.getParameterValues("setoresFuncionario")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                
                boolean funcionario = Boolean.parseBoolean(request.getParameter("funcionario"));
                boolean concessionario = Boolean.parseBoolean(request.getParameter("concessionario"));           
                sql.append("AND T2.TP_FUNCIONARIO IN ");
                if(funcionario && concessionario){
                    sql.append("('F', 'C') ");
                }else{
                    if(concessionario){
                        sql.append("('C') ");
                    }else{
                        sql.append("('F') ");
                    }
                }
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T2.NOME_FUNCIONARIO LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }

                if(ordenarPorNome){
                    sql.append("order by 1, 3");
                }else{
                    sql.append("order by 1, T1.DT_HISTORICO_ACESSO");
                }

                request.setAttribute("titulo", "Relatório de Acesso ao Clube - Cracha Funcionário");
                request.setAttribute("titulo2", "");
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "true");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "6");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
            }else if(relatorio.equals("visitante") && tipoFiltro.equals("N")){
                sql.append("SELECT T5.DESCR_LOCAL_ACESSO AS Local, T2.NO_VISITANTE AS 'Nome', ");
                sql.append("T4.DESCR_SETOR AS 'Setor', ");
                sql.append("T1.DT_HISTORICO_ACESSO AS 'Data/Hora', ");
                sql.append("T1.DE_PLACA_CARRO as 'Placa', ");
                sql.append("T1.QT_PESSOAS AS 'Qt.Pessoas', ");
                sql.append("T1.USER_ACESSO_SISTEMA AS 'Usuário' ");
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_VISITANTE T2, ");
                sql.append("TB_CRACHA_VISITANTE T3, ");
                sql.append("TB_SETOR T4, ");
                sql.append("TB_LOCAL_ACESSO T5, ");
                sql.append("TB_REGISTRO_VISITANTE T6 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T6.NR_CRACHA ");
                sql.append("AND   T6.NR_CRACHA = T3.NR_CRACHA ");
                sql.append("AND   T3.CD_SETOR = T4.CD_SETOR ");
                sql.append("AND   T1.CD_LOCAL_ACESSO = T5.CD_LOCAL_ACESSO ");
                sql.append("AND   T2.CD_VISITANTE = T6.CD_VISITANTE ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= T6.DT_ENTRADA ");
                sql.append("AND   (T1.DT_HISTORICO_ACESSO <= T6.DT_SAIDA OR T6.DT_SAIDA IS NULL) ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }        

                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }
                
                sql.append("AND   T3.CD_SETOR IN (");
                for(String s : request.getParameterValues("setoresVisitante")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T2.NO_VISITANTE LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }

                if(ordenarPorNome){
                    sql.append("order by 1, 3");
                }else{
                    sql.append("order by 1, T1.DT_HISTORICO_ACESSO");
                }

                request.setAttribute("titulo", "Relatório de Acesso ao Clube - Cracha Visitante");
                request.setAttribute("titulo2", "");
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "true");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "5");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);              
            }else if(relatorio.equals("permissaoProvisoria") && tipoFiltro.equals("N")){           
                sql.append("SELECT T3.DESCR_LOCAL_ACESSO AS Local, T2.NOME_AUTORIZADO AS 'Nome', ");
                sql.append("SUBSTRING(T2.DESCR_ATIVIDADE, 1, 35) AS 'Atividade', ");
                sql.append("T1.DT_HISTORICO_ACESSO AS 'Data/Hora', ");
                sql.append("T1.DE_PLACA_CARRO as 'Placa', ");
                sql.append("T1.QT_PESSOAS AS 'Qt.Pessoas', ");
                sql.append("T1.USER_ACESSO_SISTEMA AS 'Usuário' ");
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_AUTOR_ESPECIAL T2, ");
                sql.append("TB_LOCAL_ACESSO T3 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T2.NR_AUTORIZACAO ");
                sql.append("AND T1.CD_LOCAL_ACESSO = T3.CD_LOCAL_ACESSO ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }        

                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }                
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T2.NOME_AUTORIZADO LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }

                if(ordenarPorNome){
                    sql.append("order by 1, 3");
                }else{
                    sql.append("order by 1, T1.DT_HISTORICO_ACESSO");
                }

                request.setAttribute("titulo", "Relatório de Acesso ao Clube - Permissão Provisória");
                request.setAttribute("titulo2", "");
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "true");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "5");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
            }else if(relatorio.equals("autorizacaoEmbarque") && tipoFiltro.equals("N")){
                sql.append("SELECT ");
                sql.append("T3.DESCR_LOCAL_ACESSO AS Local, ");
                sql.append("T2.NOME_SOCIO AS 'Responsavel', ");
                sql.append("T2.NOME_CONVIDADO AS 'Convidado', ");
                sql.append("T1.DT_HISTORICO_ACESSO AS 'Data/Hora', ");
                sql.append("T1.DE_PLACA_CARRO as 'Placa', ");
                sql.append("T1.QT_PESSOAS AS 'Qt.Pessoas', ");
                sql.append("T1.USER_ACESSO_SISTEMA AS 'Usuário' ");
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_AUTORIZACAO_EMBARQUE T2, ");
                sql.append("TB_LOCAL_ACESSO T3 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T2.NR_AUT_EMBARQUE ");
                sql.append("AND T1.CD_LOCAL_ACESSO = T3.CD_LOCAL_ACESSO ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }        

                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }                
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T2.NOME_SOCIO LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }

                if(ordenarPorNome){
                    sql.append("order by 1, 3");
                }else{
                    sql.append("order by 1, T1.DT_HISTORICO_ACESSO");
                }

                request.setAttribute("titulo", "Relatório de Acesso ao Clube - Autorização de Embarque");
                request.setAttribute("titulo2", "");
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "true");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "5");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                
            }else if(relatorio.equals("carteira") && !tipoFiltro.equals("N")){
                sql.append("SELECT ");
                if (tipoFiltro.equals("A")){
                    sql.append("COUNT(DISTINCT T1.SEQ_ACESSO) QUANT ");    
                }else{
                    sql.append("SUM(ISNULL(QT_PESSOAS, 1)) QUANT ");    
                }
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_HIST_EMISSAO_CARTEIRA T2, ");
                sql.append("TB_PESSOA T3 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T2.NR_CARTEIRA ");
                sql.append("AND   T2.CD_MATRICULA = T3.CD_MATRICULA ");
                sql.append("AND   T2.CD_CATEGORIA = T3.CD_CATEGORIA ");
                sql.append("AND   T2.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");
          
                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }
          
                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                sql.append(" AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }

                sql.append(" AND   T3.CD_CATEGORIA IN (");

                for(String s : request.getParameterValues("categorias")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");

                int titulo = 0;
                try{
                    titulo = Integer.parseInt(request.getParameter("tituloCarteira"));
                }catch(Exception e){
                    titulo = 0;
                }
            
                if(titulo > 0){
                    sql.append("AND T3.CD_MATRICULA = ");
                    sql.append(titulo);
                }

                boolean masculino = Boolean.parseBoolean(request.getParameter("masculinoCarteira"));
                boolean feminino = Boolean.parseBoolean(request.getParameter("femininoCarteira"));
                sql.append(" AND T3.CD_SEXO IN (");
                if(masculino){
                    sql.append("'M', ");
                }
                if(feminino){
                    sql.append("'F', ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");

                boolean titular = Boolean.parseBoolean(request.getParameter("titular"));
                boolean dependente = Boolean.parseBoolean(request.getParameter("dependente"));
   
                if(!titular || !dependente){
                    if(titular){
                        sql.append("AND T3.SEQ_DEPENDENTE = 0 ");
                    }else{
                        sql.append("AND T3.SEQ_DEPENDENTE > 0 ");
                    }
                }
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T3.NOME_PESSOA LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }

                request.setAttribute("sql", sql);
                request.setAttribute("tipo", "CARTEIRA");
                
                request.setAttribute("dataIni", fmt2.format(d1));
                request.setAttribute("dataFim", fmt2.format(d2));
                if(idLocalAcesso > 0){
                    request.setAttribute("local", LocalAcesso.getInstance(idLocalAcesso).getDescricao());
                }else{
                    request.setAttribute("local", "TODOS");
                }
                request.setAttribute("nome", nome);
                
                String tiposAcesso2 = "";
                for(String s : request.getParameterValues("tiposAcesso")){
                    if(s.equals("PE")){
                        tiposAcesso2 = tiposAcesso2 + "Acesso Acatado<br>";
                    }
                    if(s.equals("PA")){
                        tiposAcesso2 = tiposAcesso2 + "Proibir Acesso<br>";
                    }
                    if(s.equals("AL")){
                        tiposAcesso2 = tiposAcesso2 + "Alerta<br>";
                    }
                }
                
                request.setAttribute("tipoAcesso", tiposAcesso2);
                
                
                request.setAttribute("titParm1", "Título");
                request.setAttribute("parm1", request.getParameter("tituloCarteira"));
                
                request.setAttribute("titParm2", "Tipo");
                String tipo = "";
                if(titular){
                    tipo="Titular<br>";
                }
                if(dependente){
                    tipo=tipo+"Dependente";
                }
                request.setAttribute("parm2", tipo);
                
                request.setAttribute("titParm3", "Sexo");
                String sexo = "";
                if(masculino){
                    sexo="Masculino<br>";
                }
                if(feminino){
                    sexo=sexo+"Feminino";
                }
                request.setAttribute("parm3", sexo);
                
                String lista1 = "";
                for(String s : request.getParameterValues("categorias")){
                    lista1 = lista1 + Categoria.getInstance(Integer.parseInt(s)).getDescricao() + "<br>";
                }

                request.setAttribute("tituloLista1", "Categorias");
                request.setAttribute("lista1", lista1);

                request.getRequestDispatcher("/pages/1290-impressao.jsp").forward(request, response);            
            }else if(relatorio.equals("passaporte") && !tipoFiltro.equals("N")){
                sql.append("SELECT ");
                if (tipoFiltro.equals("A")){
                    sql.append("COUNT(DISTINCT T1.SEQ_ACESSO) QUANT ");    
                }else{
                    sql.append("SUM(ISNULL(QT_PESSOAS, 1)) QUANT ");    
                }
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_PESSOA T3, ");
                sql.append("TB_TURMA T4, ");
                sql.append("TB_MATRICULA_CURSO T5 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T5.NR_PASSAPORTE ");
                sql.append("AND   T3.CD_MATRICULA = T5.CD_MATRICULA ");
                sql.append("AND   T3.CD_CATEGORIA = T5.CD_CATEGORIA ");
                sql.append("AND   T3.SEQ_DEPENDENTE = T5.SEQ_DEPENDENTE ");
                sql.append("AND   T5.SEQ_TURMA = T4.SEQ_TURMA ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }
 
 
                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }

                sql.append(" AND   T4.CD_CURSO IN (");
                for(String s : request.getParameterValues("cursos")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                

                int titulo = 0;
                try{
                    titulo = Integer.parseInt(request.getParameter("tituloPassaporte"));
                }catch(Exception e){
                    titulo = 0;
                }
            
                if(titulo > 0){
                    sql.append("AND T3.CD_MATRICULA = ");
                    sql.append(titulo);
                }

                boolean masculino = Boolean.parseBoolean(request.getParameter("masculinoPassaporte"));
                boolean feminino = Boolean.parseBoolean(request.getParameter("femininoPassaporte"));
                sql.append(" AND T3.CD_SEXO IN (");
                if(masculino){
                    sql.append("'M', ");
                }
                if(feminino){
                    sql.append("'F', ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T3.NOME_PESSOA LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }

                request.setAttribute("sql", sql);
                request.setAttribute("tipo", "PASSAPORTE");
                
                request.setAttribute("dataIni", fmt2.format(d1));
                request.setAttribute("dataFim", fmt2.format(d2));
                if(idLocalAcesso > 0){
                    request.setAttribute("local", LocalAcesso.getInstance(idLocalAcesso).getDescricao());
                }else{
                    request.setAttribute("local", "TODOS");
                }
                request.setAttribute("nome", nome);
                
                String tiposAcesso2 = "";
                for(String s : request.getParameterValues("tiposAcesso")){
                    if(s.equals("PE")){
                        tiposAcesso2 = tiposAcesso2 + "Acesso Acatado<br>";
                    }
                    if(s.equals("PA")){
                        tiposAcesso2 = tiposAcesso2 + "Proibir Acesso<br>";
                    }
                    if(s.equals("AL")){
                        tiposAcesso2 = tiposAcesso2 + "Alerta<br>";
                    }
                }
                
                request.setAttribute("tipoAcesso", tiposAcesso2);
                
                
                request.setAttribute("titParm1", "Título");
                request.setAttribute("parm1", request.getParameter("tituloPassaporte"));
                
                request.setAttribute("titParm2", "Sexo");
                String sexo = "";
                if(masculino){
                    sexo="Masculino<br>";
                }
                if(feminino){
                    sexo=sexo+"Feminino";
                }
                request.setAttribute("parm2", sexo);
                
                String lista1 = "";
                for(String s : request.getParameterValues("cursos")){
                    lista1 = lista1 + Curso.getInstance(Integer.parseInt(s)).getDescricao() + "<br>";
                }

                request.setAttribute("tituloLista1", "Categorias");
                request.setAttribute("lista1", lista1);

                request.getRequestDispatcher("/pages/1290-impressao.jsp").forward(request, response);            
            }else if(relatorio.equals("convite") && !tipoFiltro.equals("N")){
                sql.append("SELECT ");
                if (tipoFiltro.equals("A")){
                    sql.append("COUNT(DISTINCT T1.SEQ_ACESSO) QUANT ");    
                }else{
                    sql.append("SUM(ISNULL(QT_PESSOAS, 1)) QUANT ");    
                }
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_CONVITE T2, ");
                sql.append("TB_TIPO_CONVITE T4 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T2.NR_CONVITE ");
                sql.append("AND   T2.CD_CATEGORIA_CONVITE = T4.CD_TIPO_CONVITE ");        
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }        

                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }
                
                sql.append(" AND T2.CD_CATEGORIA_CONVITE IN (");
                for(String s : request.getParameterValues("tiposConvite")){
                    sql.append("'");
                    sql.append(s);
                    sql.append("', ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                
                boolean vendido = Boolean.parseBoolean(request.getParameter("convitePagoVendido"));
                boolean normal = Boolean.parseBoolean(request.getParameter("convitePagoNormal"));           
                sql.append("AND   T2.CD_TIPO_CONVITE IN (");
                if(vendido){
                    sql.append("'V', ");
                }
                if(normal){
                    sql.append("'N', ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T2.NOME_CONVIDADO LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }



                request.setAttribute("sql", sql);
                request.setAttribute("tipo", "CONVITE");
                
                request.setAttribute("dataIni", fmt2.format(d1));
                request.setAttribute("dataFim", fmt2.format(d2));
                if(idLocalAcesso > 0){
                    request.setAttribute("local", LocalAcesso.getInstance(idLocalAcesso).getDescricao());
                }else{
                    request.setAttribute("local", "TODOS");
                }
                request.setAttribute("nome", nome);
                
                String tiposAcesso2 = "";
                for(String s : request.getParameterValues("tiposAcesso")){
                    if(s.equals("PE")){
                        tiposAcesso2 = tiposAcesso2 + "Acesso Acatado<br>";
                    }
                    if(s.equals("PA")){
                        tiposAcesso2 = tiposAcesso2 + "Proibir Acesso<br>";
                    }
                    if(s.equals("AL")){
                        tiposAcesso2 = tiposAcesso2 + "Alerta<br>";
                    }
                }
                
                request.setAttribute("tipoAcesso", tiposAcesso2);
                
                request.setAttribute("titParm1", "Tipo");
                String tipo = "";
                if(vendido){
                    tipo="Vendido<br>";
                }
                if(normal){
                    tipo=tipo+"Normal";
                }
                request.setAttribute("parm1", tipo);
                
                String lista1 = "";
                for(String s : request.getParameterValues("tiposConvite")){
                    lista1 = lista1 + TipoConvite.getInstance(s).getDescricao() + "<br>";
                }

                request.setAttribute("tituloLista1", "Tipos de Convite");
                request.setAttribute("lista1", lista1);

                request.getRequestDispatcher("/pages/1290-impressao.jsp").forward(request, response);            
            }else if(relatorio.equals("funcionario") && !tipoFiltro.equals("N")){
                sql.append("SELECT ");
                if (tipoFiltro.equals("A")){
                    sql.append("COUNT(DISTINCT T1.SEQ_ACESSO) QUANT ");    
                }else{
                    sql.append("SUM(ISNULL(QT_PESSOAS, 1)) QUANT ");    
                }
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_FUNCIONARIO T2, ");
                sql.append("TB_CARGO_FUNCAO T3, ");
                sql.append("TB_SETOR T4 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T2.NR_CRACHA ");
                sql.append("AND   T2.CD_CARGO = T3.CD_CARGO ");
                sql.append("AND   T2.CD_SETOR = T4.CD_SETOR ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }        

                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }
                
                sql.append("AND   T2.CD_CARGO IN (");
                for(String s : request.getParameterValues("cargos")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                
                
                sql.append("AND   T2.CD_SETOR IN (");
                for(String s : request.getParameterValues("setoresFuncionario")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                
                boolean funcionario = Boolean.parseBoolean(request.getParameter("funcionario"));
                boolean concessionario = Boolean.parseBoolean(request.getParameter("concessionario"));           
                sql.append("AND T2.TP_FUNCIONARIO IN ");
                if(funcionario && concessionario){
                    sql.append("('F', 'C') ");
                }else{
                    if(concessionario){
                        sql.append("('C') ");
                    }else{
                        sql.append("('F') ");
                    }
                }
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T2.NOME_FUNCIONARIO LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }

                
                request.setAttribute("sql", sql);
                request.setAttribute("tipo", "CRACHÁ DE FUNCIONÁRIO");
                
                request.setAttribute("dataIni", fmt2.format(d1));
                request.setAttribute("dataFim", fmt2.format(d2));
                if(idLocalAcesso > 0){
                    request.setAttribute("local", LocalAcesso.getInstance(idLocalAcesso).getDescricao());
                }else{
                    request.setAttribute("local", "TODOS");
                }
                request.setAttribute("nome", nome);
                
                String tiposAcesso2 = "";
                for(String s : request.getParameterValues("tiposAcesso")){
                    if(s.equals("PE")){
                        tiposAcesso2 = tiposAcesso2 + "Acesso Acatado<br>";
                    }
                    if(s.equals("PA")){
                        tiposAcesso2 = tiposAcesso2 + "Proibir Acesso<br>";
                    }
                    if(s.equals("AL")){
                        tiposAcesso2 = tiposAcesso2 + "Alerta<br>";
                    }
                }
                
                request.setAttribute("tipoAcesso", tiposAcesso2);
                
                
                request.setAttribute("titParm1", "Tipo");
                if(funcionario && concessionario){
                    request.setAttribute("parm1", "Funcionário<br>Concessionário");
                }else{
                    if(concessionario){
                        request.setAttribute("parm1", "Concessionário");
                    }else{
                        request.setAttribute("parm1", "Funcionário");
                    }
                }
                
                
                String lista1 = "";
                for(String s : request.getParameterValues("setoresFuncionario")){
                    lista1 = lista1 + Setor.getInstance(Integer.parseInt(s)).getDescricao() + "<br>";
                }
                String lista2 = "";
                for(String s : request.getParameterValues("cargos")){
                    lista2 = lista2 + Cargo.getInstance(Integer.parseInt(s)).getDescricao() + "<br>";
                }
                
                request.setAttribute("tituloLista1", "Setores");
                request.setAttribute("lista1", lista1);
                request.setAttribute("tituloLista2", "Cargos");
                request.setAttribute("lista2", lista2);

                request.getRequestDispatcher("/pages/1290-impressao.jsp").forward(request, response);            
            }else if(relatorio.equals("visitante") && !tipoFiltro.equals("N")){
                sql.append("SELECT ");
                if (tipoFiltro.equals("A")){
                    sql.append("COUNT(DISTINCT T1.SEQ_ACESSO) QUANT ");    
                }else{
                    sql.append("SUM(ISNULL(QT_PESSOAS, 1)) QUANT ");    
                }
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_CRACHA_VISITANTE T3 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T3.NR_CRACHA ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }        

                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }
                
                sql.append("AND   T3.CD_SETOR IN (");
                for(String s : request.getParameterValues("setoresVisitante")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                
                                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T2.NO_VISITANTE LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }


                request.setAttribute("sql", sql);
                request.setAttribute("tipo", "VISITANTE");
                
                request.setAttribute("dataIni", fmt2.format(d1));
                request.setAttribute("dataFim", fmt2.format(d2));
                if(idLocalAcesso > 0){
                    request.setAttribute("local", LocalAcesso.getInstance(idLocalAcesso).getDescricao());
                }else{
                    request.setAttribute("local", "TODOS");
                }
                request.setAttribute("nome", nome);
                
                String tiposAcesso2 = "";
                for(String s : request.getParameterValues("tiposAcesso")){
                    if(s.equals("PE")){
                        tiposAcesso2 = tiposAcesso2 + "Acesso Acatado<br>";
                    }
                    if(s.equals("PA")){
                        tiposAcesso2 = tiposAcesso2 + "Proibir Acesso<br>";
                    }
                    if(s.equals("AL")){
                        tiposAcesso2 = tiposAcesso2 + "Alerta<br>";
                    }
                }
                
                request.setAttribute("tipoAcesso", tiposAcesso2);
                
                String lista1 = "";
                for(String s : request.getParameterValues("setoresVisitante")){
                    lista1 = lista1 + Setor.getInstance(Integer.parseInt(s)).getDescricao() + "<br>";
                }

                request.setAttribute("tituloLista1", "Setor");
                request.setAttribute("lista1", lista1);

                request.getRequestDispatcher("/pages/1290-impressao.jsp").forward(request, response);            
            }else if(relatorio.equals("permissaoProvisoria") && !tipoFiltro.equals("N")){           
                sql.append("SELECT ");
                if (tipoFiltro.equals("A")){
                    sql.append("COUNT(DISTINCT T1.SEQ_ACESSO) QUANT ");    
                }else{
                    sql.append("SUM(ISNULL(QT_PESSOAS, 1)) QUANT ");    
                }

                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_AUTOR_ESPECIAL T2, ");
                sql.append("TB_LOCAL_ACESSO T3 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T2.NR_AUTORIZACAO ");
                sql.append("AND T1.CD_LOCAL_ACESSO = T3.CD_LOCAL_ACESSO ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }        

                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }                
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T2.NO_AUTORIZADO LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }


                request.setAttribute("sql", sql);
                request.setAttribute("tipo", "PERMISSÃO PROVISÓRIA");
                
                request.setAttribute("dataIni", fmt2.format(d1));
                request.setAttribute("dataFim", fmt2.format(d2));
                if(idLocalAcesso > 0){
                    request.setAttribute("local", LocalAcesso.getInstance(idLocalAcesso).getDescricao());
                }else{
                    request.setAttribute("local", "TODOS");
                }
                request.setAttribute("nome", nome);
                
                String tiposAcesso2 = "";
                for(String s : request.getParameterValues("tiposAcesso")){
                    if(s.equals("PE")){
                        tiposAcesso2 = tiposAcesso2 + "Acesso Acatado<br>";
                    }
                    if(s.equals("PA")){
                        tiposAcesso2 = tiposAcesso2 + "Proibir Acesso<br>";
                    }
                    if(s.equals("AL")){
                        tiposAcesso2 = tiposAcesso2 + "Alerta<br>";
                    }
                }
                
                request.setAttribute("tipoAcesso", tiposAcesso2);
                
                request.getRequestDispatcher("/pages/1290-impressao.jsp").forward(request, response);            
            }else if(relatorio.equals("autorizacaoEmbarque") && !tipoFiltro.equals("N")){
                sql.append("SELECT ");
                if (tipoFiltro.equals("A")){
                    sql.append("COUNT(DISTINCT T1.SEQ_ACESSO) QUANT ");    
                }else{
                    sql.append("SUM(ISNULL(QT_PESSOAS, 1)) QUANT ");    
                }
                sql.append("FROM TB_ACESSO T1, ");
                sql.append("TB_AUTORIZACAO_EMBARQUE T2 ");
                sql.append("WHERE T1.NR_DOC_HIST_ACESSO = T2.NR_AUT_EMBARQUE ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO >= '");
                sql.append(fmt.format(d1));
                sql.append(" 00:00:01' ");
                sql.append("AND   T1.DT_HISTORICO_ACESSO <= '");
                sql.append(fmt.format(d2));
                sql.append(" 23:59:59' ");

                if(idLocalAcesso > 0){
                    sql.append("AND T1.CD_LOCAL_ACESSO = ");
                    sql.append(idLocalAcesso);
                    sql.append(" ");
                }        

                sql.append("AND   T1.CD_STATUS_ACESSO_PERMITIDO IN ");
                sql.append(tiposAcesso);
                
                sql.append("AND   T1.ENTRADA_OU_SAIDA = ");
                if(request.getParameter("sentido").equals("E")){
                    sql.append("'E'");
                }else{
                    sql.append("'S'");
                }                
                
                if(nome != null && !nome.trim().equals("")){
                    sql.append("AND T2.NOME_SOCIO LIKE '");
                    sql.append(nome.toUpperCase());
                    sql.append("%' ");
                }

                request.setAttribute("sql", sql);
                request.setAttribute("tipo", "AUTORIZAÇÃO DE EMBARQUE");
                
                request.setAttribute("dataIni", fmt2.format(d1));
                request.setAttribute("dataFim", fmt2.format(d2));
                if(idLocalAcesso > 0){
                    request.setAttribute("local", LocalAcesso.getInstance(idLocalAcesso).getDescricao());
                }else{
                    request.setAttribute("local", "TODOS");
                }
                request.setAttribute("nome", nome);
                
                String tiposAcesso2 = "";
                for(String s : request.getParameterValues("tiposAcesso")){
                    if(s.equals("PE")){
                        tiposAcesso2 = tiposAcesso2 + "Acesso Acatado<br>";
                    }
                    if(s.equals("PA")){
                        tiposAcesso2 = tiposAcesso2 + "Proibir Acesso<br>";
                    }
                    if(s.equals("AL")){
                        tiposAcesso2 = tiposAcesso2 + "Alerta<br>";
                    }
                }
                
                request.setAttribute("tipoAcesso", tiposAcesso2);
                
                request.getRequestDispatcher("/pages/1290-impressao.jsp").forward(request, response);            
            }
        }else{
            request.setAttribute("locaisAcesso", ComboBoxLoader.listar("TB_LOCAL_ACESSO"));
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT' AND TP_CATEGORIA = 'SO'"));
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.setAttribute("tiposConvite", ComboBoxLoader.listar("TB_TIPO_CONVITE"));
            request.setAttribute("setores", ComboBoxLoader.listar("TB_setor"));
            request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_FUNCAO"));
            
            request.getRequestDispatcher("/pages/1290.jsp").forward(request, response);            
        }
    }
    
    @App("1480")
    public static void estatistica(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){   
            StringBuilder sql = new StringBuilder();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));            
            
            sql.append("SELECT RIGHT('00' + CAST(DATEPART(hour, DT_HISTORICO_ACESSO) AS VARCHAR), 2) + ':00 - ' + RIGHT('00' + CAST(DATEPART(hour, DT_HISTORICO_ACESSO) AS VARCHAR), 2) + ':59' AS [Horario Acesso], count(DATEPART(hour, DT_HISTORICO_ACESSO)) AS [Qt. Acesso] ");
            sql.append("FROM TB_ACESSO ");
            sql.append("WHERE DT_HISTORICO_ACESSO >= '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00' AND DT_HISTORICO_ACESSO <= '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59' AND CD_LOCAL_ACESSO IN (");
            for(String s : request.getParameterValues("locaisAcesso")){
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() -2, sql.length());
            sql.append(")");            
            sql.append(" AND CD_STATUS_ACESSO_PERMITIDO IN (");
            for(String s : request.getParameterValues("tiposAcesso")){
                sql.append("'");
                sql.append(s);
                sql.append("', ");
            }
            sql.delete(sql.length() -2, sql.length());
            sql.append(")");            
            sql.append(" AND SUBSTRING(CONVERT(VARCHAR, NR_DOC_HIST_ACESSO), 1, 1) IN (");
            for(String s : request.getParameterValues("documentos")){
                sql.append("'");
                sql.append(s);
                sql.append("', ");
            }
            sql.delete(sql.length() -2, sql.length());
            sql.append(")");            
            sql.append("group by DATEPART(hour, DT_HISTORICO_ACESSO) order by 1");
            
            request.setAttribute("titulo", "Estatísticas de Acesso ao Clube");
            request.setAttribute("titulo2", request.getParameter("dataInicio")
                    + " a " + request.getParameter("dataFim"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "2");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else{
            request.setAttribute("locaisAcesso", ComboBoxLoader.listar("TB_LOCAL_ACESSO"));            
            request.getRequestDispatcher("/pages/1480.jsp").forward(request, response);            
        }
    }
}
