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
import techsoft.util.Datas;

@Controller
public class ControleArmarioAcademia{
    

    @App("2530")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", ArmarioAcademia.listar());
            request.getRequestDispatcher("/pages/2530-lista.jsp").forward(request, response);

    }
    
    @App("2531")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2531");
                request.setAttribute("tipoArmarios", TipoArmarioAcademia.listar());
                
                request.getRequestDispatcher("/pages/2530.jsp").forward(request, response);            
            }else{
                ArmarioAcademia d = new ArmarioAcademia();
            	d.setDescricao(request.getParameter("descricao"));
                
            	d.setTipoArmarioAcademia(TipoArmarioAcademia.getInstance(Integer.parseInt(request.getParameter("idTipoArmario"))));

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2531", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2530");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("2532")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2532");
                
                int id = Integer.parseInt(request.getParameter("id"));
                ArmarioAcademia d = ArmarioAcademia.getInstance(id);
                request.setAttribute("item", d);
                request.setAttribute("tipoArmarios", TipoArmarioAcademia.listar());
                
                request.getRequestDispatcher("/pages/2530.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                ArmarioAcademia d = ArmarioAcademia.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2532", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=2530");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("2533")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        ArmarioAcademia b = ArmarioAcademia.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2533", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2530");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
    
    @App("2534")
    public static void bloquear(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        String acao = request.getParameter("acao");
        
        int id = Integer.parseInt(request.getParameter("id"));
        ArmarioAcademia b = ArmarioAcademia.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2534", request.getRemoteAddr());

        try{
            if("bloquear".equals(acao)){
                b.bloquear(audit);
            }else{
                b.desbloquear(audit);
            }    
            response.sendRedirect("c?app=2530");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
