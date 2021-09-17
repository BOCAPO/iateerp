package techsoft.controle.curso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.tabelas.Modalidade;
import techsoft.util.Datas;

@Controller
public class ControleRelatorio {

    @App("1280")
    public static void alunosModalidade(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT T1.DESCR_CURSO AS Curso,");
              sql.append(" T2.NOME_PESSOA as Aluno,");
              sql.append(" SUBSTRING(CONVERT(VARCHAR, T2.CD_CATEGORIA)  + ' / ' +");
              sql.append(" case ");
              sql.append(" when T2.cd_matricula < 10 then ");
              sql.append(" '000' + str(T2.cd_matricula, 1, 0) ");
              sql.append(" when T2.cd_matricula < 100 and T2.cd_matricula > 9 then ");
              sql.append(" '00' + str(T2.cd_matricula, 2, 0) ");
              sql.append(" when T2.cd_matricula > 99 and T2.cd_matricula < 1000 then ");
              sql.append(" '0' + str(T2.cd_matricula, 3, 0) ");
              sql.append(" Else ");
              sql.append(" str(T2.cd_matricula, 4, 0) ");
              sql.append(" End, 1, 10) as  '  Título', ");
              sql.append(" CONVERT(VARCHAR(10),T3.DT_MATRICULA,103) AS 'Dt. Matrícula', ");
              sql.append(" T3.PERC_DESCONTO_PESSOAL as Desconto, ");
              sql.append(" T3.DT_VALIDADE_PASSAPORTE as 'Dt.Venc.Passaporte' ");
              sql.append(" from TB_CURSO T1, ");
              sql.append(" TB_PESSOA T2, ");
              sql.append(" TB_MATRICULA_CURSO T3, ");
              sql.append(" TB_TURMA T4 ");
              sql.append(" WHERE T3.CD_MATRICULA = T2.CD_MATRICULA ");
              sql.append(" AND  T3.CD_CATEGORIA = T2.CD_CATEGORIA ");
              sql.append(" AND  T3.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE ");
              sql.append(" AND  T3.SEQ_TURMA = T4.SEQ_TURMA ");
              sql.append(" AND  T4.CD_CURSO = T1.CD_CURSO ");
              sql.append(" AND  T4.DT_FIM_TURMA >= GETDATE() ");
            
            boolean b = Boolean.parseBoolean(request.getParameter("somentePassaportesVigentes"));
            if(b){
                sql.append(" AND T3.DT_VALIDADE_PASSAPORTE >= GETDATE() ");
            }
         
            StringBuilder tmp = new StringBuilder();
            for(String s : request.getParameterValues("cursos")){
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() -2, tmp.length());
            sql.append(" AND  T1.CD_CURSO IN (");
            sql.append(tmp.toString());
            sql.append(")");

            tmp.delete(0, tmp.length());
            for(String s : request.getParameterValues("categorias")){
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() -2, tmp.length());
            sql.append(" AND  T2.CD_CATEGORIA IN (");
            sql.append(tmp.toString());
            sql.append(")");
            sql.append(" AND  T3.CD_SIT_MATRICULA = 'NO'");
            sql.append(" ORDER BY 1, ");
            
            String order = request.getParameter("order");
            if("T".endsWith(order)){
                sql.append("4");
            }else if("M".endsWith(order)){
                sql.append("3");
            }else if("N".endsWith(order)){
                sql.append("2");
            }else{
                sql.append("5");
            }

            String titulo2 = "";
            try{
                int i = Integer.parseInt(request.getParameter("idModalidade"));
                if(i > 0){
                    Modalidade m = Modalidade.getInstance(i);
                    titulo2 = m.getDescricao();
                }
            }catch(NumberFormatException e){
                
            }
            
            request.setAttribute("titulo", "Alunos por Modalidade");
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else{
            request.setAttribute("modalidades", ComboBoxLoader.listar("TB_MODALIDADE_curso"));
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT' "));
            String sql = "VW_CURSOS";
            int idModalidade = 0;
            try{
                idModalidade = Integer.parseInt(request.getParameter("idModalidade"));
                if(idModalidade > 0){
                    request.setAttribute("idModalidade", idModalidade);
                    sql += " WHERE CD_MODALIDADE = " + idModalidade;
                }
            }catch(NumberFormatException e){
                
            }
            request.setAttribute("cursos", ComboBoxLoader.listar(sql));
            request.getRequestDispatcher("/pages/1280.jsp").forward(request, response);
        }
    }    
    
    @App("3140")
    public static void matriculasCancelamentos(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            boolean cancelamento = Boolean.parseBoolean(request.getParameter("cancelamento"));
            boolean matricula = Boolean.parseBoolean(request.getParameter("matricula"));
            boolean clube = Boolean.parseBoolean(request.getParameter("clube"));
            boolean internet = Boolean.parseBoolean(request.getParameter("internet"));
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String sit1 = null;
            String sit2 = null;
            String origem1 = null;
            String origem2 = null;
            if(cancelamento){
                if(matricula){
                    sit1 = "'NO'";
                    sit2 = "'CA'";
                }else{
                    sit1 = "'CA'";
                    sit2 = "'CA'";                    
                }
            }else{
                if(matricula){
                    sit1 = "'NO'";
                    sit2 = "'NO'";
                }
            }
            if(clube){
                if(internet){
                    origem1 = "'C'";
                    origem2 = "'I'";
                }else{
                    origem1 = "'C'";
                    origem2 = "'C'";                    
                }
            }else{
                if(internet){
                    origem1 = "'I'";
                    origem2 = "'I'";
                }
            }
            String d1 = fmt.format(Datas.parse(request.getParameter("dataInicio")));
            String d2 = fmt.format(Datas.parse(request.getParameter("dataFim")));
            
            String sql = "EXEC SP_REL_MAT_CANC_CURSO '"
                + d1 + " 00:00:00', '"
                + d2 + " 00:00:00', ";
            if(request.getParameter("tipo").equals("A")){
                sql += "'A', ";
            }else{
                sql += "'S', ";
            }

            StringBuilder tmp = new StringBuilder();
            for(String s : request.getParameterValues("cursos")){
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() -2, tmp.length());
            sql += "'(" + tmp.toString() + ")', ";
            
            String order = request.getParameter("order");
            if("N".endsWith(order)){
                sql += "'3', ";
            }else if("M".endsWith(order)){
                sql += "'2', ";
            }else{
                sql += "'4', ";
            }

            sql += sit1 + ", ";
            sql += sit2 + ", ";
            sql += origem1 + ", ";
            sql += origem2;

            String titulo2 = "Período " + request.getParameter("dataInicio")
                    + " até " + request.getParameter("dataFim");
            request.setAttribute("titulo", "Relatório de Matrículas e Cancelamentos");
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.getRequestDispatcher("/pages/3140.jsp").forward(request, response);            
        }   
    }
    
    
    @App("2150")
    public static void descontoAlunos(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){    
            StringBuilder sql = new StringBuilder();
    
            sql.append("SELECT T1.DESCR_CURSO AS Curso, ");
            sql.append("T2.NOME_PESSOA as Aluno, ");
            sql.append("SUBSTRING(CONVERT(VARCHAR, T2.CD_CATEGORIA)  + ' / ' + ");
            sql.append(" case ");
            sql.append("when T2.cd_matricula < 10 then ");
            sql.append("'000' + str(T2.cd_matricula, 1, 0) ");
            sql.append("when T2.cd_matricula < 100 and T2.cd_matricula > 9 then ");
            sql.append("'00' + str(T2.cd_matricula, 2, 0) ");
            sql.append("when T2.cd_matricula > 99 and T2.cd_matricula < 1000 then ");
            sql.append("'0' + str(T2.cd_matricula, 3, 0) ");
            sql.append("Else ");
            sql.append("str(T2.cd_matricula, 4, 0) ");
            sql.append("End, 1, 10) as  '  Título', ");
            sql.append("CONVERT(VARCHAR(10),T2.DT_NASCIMENTO,103) AS 'Dt. Nascimento', ");
            sql.append("CONVERT(VARCHAR(10),T3.DT_MATRICULA,103) AS 'Dt. Matrícula', ");
            sql.append("T3.PERC_DESCONTO_PESSOAL as Desconto ");
            sql.append("from TB_CURSO T1, ");
            sql.append("TB_PESSOA T2, ");
            sql.append("TB_MATRICULA_CURSO T3, ");
            sql.append("TB_TURMA T4 ");
            sql.append("WHERE T3.CD_MATRICULA = T2.CD_MATRICULA ");
            sql.append(" AND  T3.CD_CATEGORIA = T2.CD_CATEGORIA ");
            sql.append(" AND  T3.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE ");
            sql.append(" AND  T3.SEQ_TURMA = T4.SEQ_TURMA ");
            sql.append(" AND  T4.CD_CURSO = T1.CD_CURSO ");
            sql.append(" AND  T4.DT_FIM_TURMA >= GETDATE() ");
            sql.append(" AND  T3.PERC_DESCONTO_PESSOAL IS NOT NULL ");
            sql.append(" AND  T3.PERC_DESCONTO_PESSOAL > 0 ");

            StringBuilder tmp = new StringBuilder();
            for(String s : request.getParameterValues("cursos")){
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() -2, tmp.length());
            sql.append(" AND  T1.CD_CURSO IN (");
            sql.append(tmp.toString());
            sql.append(")");

            tmp.delete(0, tmp.length());
            for(String s : request.getParameterValues("categorias")){
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() -2, tmp.length());                    
            sql.append(" AND  T2.CD_CATEGORIA IN (");
            sql.append(tmp.toString());
            sql.append(")");        
                    
            sql.append(" AND  T3.CD_SIT_MATRICULA = 'NO' ");
              
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date di = Datas.parse(request.getParameter("dataInicio"));
            Date df = Datas.parse(request.getParameter("dataFim"));
            if(di != null){
                sql.append(" AND T2.DT_NASCIMENTO >= '");
                sql.append(fmt.format(di));
                sql.append("' ");
            }
            if(df != null){
                sql.append(" AND T2.DT_NASCIMENTO <= '");
                sql.append(fmt.format(df));
                sql.append(" 23:59:59' ");
            }            

            try{
                int desconto = Integer.parseInt(request.getParameter("descontoInicio").toString().replaceAll(",", "."));
                sql.append(" AND T3.PERC_DESCONTO_PESSOAL  >= ");
                sql.append(desconto);
                
                desconto = Integer.parseInt(request.getParameter("descontoFim").toString().replaceAll(",", "."));
                sql.append(" AND T3.PERC_DESCONTO_PESSOAL  <= ");
                sql.append(desconto);                
            }catch(NumberFormatException e){

            }
              
            sql.append(" ORDER BY 1 ");
         
            request.setAttribute("titulo", "Desconto dos Alunos das Escolinhas");
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
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT' "));
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.getRequestDispatcher("/pages/2150.jsp").forward(request, response);            
        }
    }
    
    @App("3130")
    public static void pauta(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            request.setAttribute("turmas", request.getParameterValues("turmas"));
            request.getRequestDispatcher("/pages/3130-impressao.jsp").forward(request, response);
        }else{
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.setAttribute("professores", ComboBoxLoader.listar("VW_PROFESSORES"));
            request.setAttribute("idCurso", request.getParameter("idCurso"));
            request.setAttribute("idProfessor", request.getParameter("idProfessor"));
            request.getRequestDispatcher("/pages/3130.jsp").forward(request, response);
        }
    }
    
    @App("2400")
    public static void alunosFaixaEtaria(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            String tipo = request.getParameter("tipo");
            String titulo2 = "";
            
            sql.append("SELECT ");
            sql.append("	T1.DESCR_CURSO Curso,");
            
            if (tipo.equals("A") || tipo.equals("D") ){
                sql.append("	T2.DE_TURMA Turma,");
            }
            
            sql.append("	DATEDIFF(hour, ISNULL(T4.DT_NASCIMENTO, GETDATE()), GETDATE())/8766 Idade,");
            
            if (tipo.equals("S") || tipo.equals("A") ){
                sql.append("	COUNT(*) 'Qt. Alunos'");
            }else{
                sql.append("	T4.NOME_PESSOA Nome");
            }
            sql.append(" FROM");
            sql.append("	TB_CURSO T1,");
            sql.append("	TB_TURMA T2,");
            sql.append("	TB_MATRICULA_CURSO T3,");
            sql.append("	TB_PESSOA T4");
            sql.append(" WHERE ");
            sql.append("	T1.CD_CURSO = T2.CD_CURSO AND");
            sql.append("	T2.SEQ_TURMA = T3.SEQ_TURMA AND");
            sql.append("	T3.CD_MATRICULA = T4.CD_MATRICULA AND");
            sql.append("	T3.CD_CATEGORIA = T4.CD_CATEGORIA AND");
            sql.append("	T3.SEQ_DEPENDENTE = T4.SEQ_DEPENDENTE AND");
            sql.append("	T3.CD_SIT_MATRICULA = 'NO'");
            
            StringBuilder tmp = new StringBuilder();
            for(String s : request.getParameterValues("cursos")){
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() -2, tmp.length());
            sql.append(" AND  T1.CD_CURSO IN (");
            sql.append(tmp.toString());
            sql.append(")");

            if(tipo.equals("S") || tipo.equals("A")){
                titulo2 = "Sintético";
                sql.append(" GROUP BY ");
                sql.append("	T1.DESCR_CURSO,");
                if(tipo.equals("A")){    
                    titulo2 = "Analítico";
                    sql.append("	T2.DE_TURMA,");
                }
                sql.append("	DATEDIFF(hour, ISNULL(T4.DT_NASCIMENTO, GETDATE()), GETDATE())/8766");
            }else{
                titulo2 = "Detalhado";  
            }
            sql.append(" ORDER BY 1, 2, 3");
            if(tipo.equals("D")){
                sql.append(", 4");
            }
            
            request.setAttribute("titulo", "Alunos por Faixa Etária");
            request.setAttribute("titulo2", titulo2);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            if(tipo.equals("S")){
                request.setAttribute("quebra2", "false");
            }else{
                request.setAttribute("quebra2", "true");
            }
            
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else{
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.getRequestDispatcher("/pages/2400.jsp").forward(request, response);
        }
    }    
    
    @App("2490")
    public static void descontoFamiliarAcademia(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            String tipo = request.getParameter("tipo");
            
            String sql = "EXEC SP_REL_DESCONTO_ACADEMIA '" + tipo + "'";
            
            request.setAttribute("titulo", "Desconto Familiar Academia");
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
            request.getRequestDispatcher("/pages/2490.jsp").forward(request, response);
        }
    }    
}
