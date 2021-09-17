package techsoft.clube;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.operacoes.TaxaAdministrativa;

@Controller
public class ControleRepasseConcessionario{
    

    @App("2570")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String mesAno = request.getParameter("mesAno");
        String idPessoa = request.getParameter("concessionario");
        
        if (mesAno!="" && mesAno!=null){
            String mes = mesAno.substring(0, 2);
            String ano = mesAno.substring(3);
            
            
            String categoria = idPessoa.substring(0,2);
            String matricula = idPessoa.substring(2,6);
            String dependente = idPessoa.substring(6);

            request.setAttribute("mesAno", mesAno);
            request.setAttribute("mes", mes);
            request.setAttribute("ano", ano);
            
            request.setAttribute("categoriaCS", categoria);
            request.setAttribute("matriculaCS", matricula);
            request.setAttribute("dependenteCS", dependente);

            request.setAttribute("csSel", idPessoa);
            
            request.setAttribute("acao", "consultar");

        }

        request.setAttribute("usuario", request.getUserPrincipal().getName());        
        
        request.getRequestDispatcher("/pages/2570-lista.jsp").forward(request, response);
        
        
    }
    
    @App("2571")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        for (String p : Collections.list(request.getParameterNames()))
                request.setAttribute(p, request.getParameter(p));

        String sql = " SELECT * FROM tb_taxa_administrativa where ind_taxa_administrativa = 'I' order by 2";
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
        
        String mesAno = request.getParameter("mesAno");
        String concessionario = request.getParameter("concessionario");
        String mes = mesAno.substring(0, 2);
        String ano = mesAno.substring(3);
        String categoria = concessionario.substring(0,2);
        String matricula = concessionario.substring(2,6);
        String dependente = concessionario.substring(6);

        request.setAttribute("mes", mes);
        request.setAttribute("ano", ano);
        request.setAttribute("mesAno", mesAno);
        request.setAttribute("concessionario", concessionario);
        
        Socio s = Socio.getInstance(Integer.parseInt(matricula), Integer.parseInt(dependente), Integer.parseInt(categoria));
        
        request.setAttribute("s", s);

        request.getRequestDispatcher("/pages/2570.jsp").forward(request, response);
        
    }    

    @App("2580")
    public static void relRepasse(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
             
            boolean analitico = request.getParameter("tipo").equals("A");
            String repasseInicio = request.getParameter("repasseInicio");
            String repasseFim = request.getParameter("repasseFim");
            
            StringBuilder tmp2 = new StringBuilder();
            for (String s : request.getParameterValues("concessionarios")) {
                tmp2.append(s);
                tmp2.append(",");
            }
            tmp2.delete(tmp2.length() - 2, tmp2.length());
            
            StringBuilder sql = new StringBuilder();
            sql.append("EXEC SP_REL_REPASSE_CONCESSIONARIO '");
            
            sql.append(repasseInicio);
            sql.append("', '");
            sql.append(repasseFim);
            sql.append("', '");
            
            if(analitico){
                sql.append("A', '");
            }else{
                sql.append("S', '");
            }
            
            sql.append(tmp2);
            sql.append("'");
            
            request.setAttribute("titulo", "Relatório de Repasse aos Concessionários");
            request.setAttribute("titulo2", "Período " + repasseInicio + " a " + repasseFim);
            
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            
            if (analitico){
                request.setAttribute("total1", "8");
            }else{
                request.setAttribute("total1", "3");
            }
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
            
            
            
            
            
            
        }else{
            request.getRequestDispatcher("/pages/2580.jsp").forward(request, response);
        }
        
    }    

}
