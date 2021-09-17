package techsoft.cadastro;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;
import techsoft.cadastro.CartaCobranca;

@Controller
public class ControleCartaCobranca{
    

    @App("1200")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", CartaCobranca.listar("='C'"));
            request.getRequestDispatcher("/pages/1200-lista.jsp").forward(request, response);

    }
    
    @App("1201")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "1201");
                request.getRequestDispatcher("/pages/1200.jsp").forward(request, response);            
            }else{
                
                CartaCobranca d = new CartaCobranca();
                d.setDescricao(request.getParameter("descricao"));
                d.setTexto(request.getParameter("texto"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1201", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1200");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }    
    
    @App("1202")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("carta", CartaCobranca.getInstance(request.getParameter("descricao")) );
            request.setAttribute("app", "1202");
            request.getRequestDispatcher("/pages/1200.jsp").forward(request, response);            
        }else{

            CartaCobranca d = CartaCobranca.getInstance(request.getParameter("descricao"));
            d.setTexto(request.getParameter("texto"));

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1202", request.getRemoteAddr());
            try{
                d.alterar(audit);
                response.sendRedirect("c?app=1200");

            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }
    }      
    
    @App("1203")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        CartaCobranca d = CartaCobranca.getInstance(request.getParameter("descricao"));
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1203", request.getRemoteAddr());
        try{
            d.excluir(audit);
            response.sendRedirect("c?app=1200");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }

    }      
}
