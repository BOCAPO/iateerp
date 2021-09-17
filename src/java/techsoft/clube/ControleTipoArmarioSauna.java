package techsoft.clube;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;

@Controller
public class ControleTipoArmarioSauna{
    

    @App("2660")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", TipoArmarioSauna.listar());
            request.getRequestDispatcher("/pages/2660-lista.jsp").forward(request, response);

    }
    
    @App("2661")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2661");
                request.getRequestDispatcher("/pages/2660.jsp").forward(request, response);            
            }else{
                TipoArmarioSauna d = new TipoArmarioSauna();
            	d.setDescricao(request.getParameter("descricao"));

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2661", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2660");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("2662")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2662");
                
                int id = Integer.parseInt(request.getParameter("id"));
                TipoArmarioSauna d = TipoArmarioSauna.getInstance(id);
                request.setAttribute("item", d);
                request.getRequestDispatcher("/pages/2660.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                TipoArmarioSauna d = TipoArmarioSauna.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2662", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=2660");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("2663")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        TipoArmarioSauna b = TipoArmarioSauna.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2663", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2660");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
