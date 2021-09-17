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
public class ControleTipoArmarioAcademia{
    

    @App("2520")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", TipoArmarioAcademia.listar());
            request.getRequestDispatcher("/pages/2520-lista.jsp").forward(request, response);

    }
    
    @App("2521")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2521");
                request.getRequestDispatcher("/pages/2520.jsp").forward(request, response);            
            }else{
                TipoArmarioAcademia d = new TipoArmarioAcademia();
            	d.setDescricao(request.getParameter("descricao"));

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2521", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2520");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("2522")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2522");
                
                int id = Integer.parseInt(request.getParameter("id"));
                TipoArmarioAcademia d = TipoArmarioAcademia.getInstance(id);
                request.setAttribute("item", d);
                request.getRequestDispatcher("/pages/2520.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                TipoArmarioAcademia d = TipoArmarioAcademia.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2522", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=2520");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("2523")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        TipoArmarioAcademia b = TipoArmarioAcademia.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2523", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2520");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
