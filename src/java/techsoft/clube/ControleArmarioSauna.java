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
public class ControleArmarioSauna{
    

    @App("2670")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", ArmarioSauna.listar());
            request.getRequestDispatcher("/pages/2670-lista.jsp").forward(request, response);

    }
    
    @App("2671")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2671");
                request.setAttribute("tipoArmarios", TipoArmarioSauna.listar());
                
                request.getRequestDispatcher("/pages/2670.jsp").forward(request, response);            
            }else{
                ArmarioSauna d = new ArmarioSauna();
            	d.setDescricao(request.getParameter("descricao"));
                
            	d.setTipoArmarioSauna(TipoArmarioSauna.getInstance(Integer.parseInt(request.getParameter("idTipoArmario"))));

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2671", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2670");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("2672")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2672");
                
                int id = Integer.parseInt(request.getParameter("id"));
                ArmarioSauna d = ArmarioSauna.getInstance(id);
                request.setAttribute("item", d);
                request.setAttribute("tipoArmarios", TipoArmarioSauna.listar());
                
                request.getRequestDispatcher("/pages/2670.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                ArmarioSauna d = ArmarioSauna.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2672", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=2670");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("2673")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        ArmarioSauna b = ArmarioSauna.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2673", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2670");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
    
    @App("2674")
    public static void bloquear(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        String acao = request.getParameter("acao");
        
        int id = Integer.parseInt(request.getParameter("id"));
        ArmarioSauna b = ArmarioSauna.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2674", request.getRemoteAddr());

        try{
            if("bloquear".equals(acao)){
                b.bloquear(audit);
            }else{
                b.desbloquear(audit);
            }    
            response.sendRedirect("c?app=2670");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
