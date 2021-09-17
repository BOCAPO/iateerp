package techsoft.tabelas;

import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.INPC;

@Controller
public class ControleDiasMultaInternet {
    
    
    @App("2330")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        request.setAttribute("lista", DiasMultaInternet.listar());
        request.getRequestDispatcher("/pages/2330.jsp").forward(request, response);
    
    }
    
    
    @App("2331")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        DiasMultaInternet i = new DiasMultaInternet();
        i.setAno(Integer.parseInt(request.getParameter("ano")));
        i.setMes(Integer.parseInt(request.getParameter("mes")));
        i.setQtDias(Integer.parseInt(request.getParameter("quant")));

        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), request.getParameter("app"), request.getRemoteAddr());
        try {
            i.inserir(audit);
            response.sendRedirect("c?app=2330");
        } catch(Exception e) {
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }
    
    
    @App("2333")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), request.getParameter("app"), request.getRemoteAddr());
        DiasMultaInternet.excluir(id, audit);

        response.sendRedirect("c?app=2330");
    }
    
}
