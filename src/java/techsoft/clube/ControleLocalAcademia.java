package techsoft.clube;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.Titular;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.clube.LocalAcademia;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleLocalAcademia{
    

    @App("3200")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", LocalAcademia.listar());
            request.getRequestDispatcher("/pages/3200-lista.jsp").forward(request, response);

    }
    
    @App("3201")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "3201");
                request.getRequestDispatcher("/pages/3200.jsp").forward(request, response);            
            }else{
                LocalAcademia d = new LocalAcademia();
            	d.setDescricao(request.getParameter("descricao"));

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3201", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=3200");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("3202")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "3202");
                
                int id = Integer.parseInt(request.getParameter("id"));
                LocalAcademia d = LocalAcademia.getInstance(id);
                request.setAttribute("local", d);
                request.getRequestDispatcher("/pages/3200.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                LocalAcademia d = LocalAcademia.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3202", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=3200");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("3203")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        LocalAcademia b = LocalAcademia.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3203", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=3200");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
