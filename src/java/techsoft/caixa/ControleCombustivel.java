package techsoft.caixa;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.caixa.Combustivel;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;

@Controller
public class ControleCombustivel{
    

    @App("6330")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", Combustivel.listar());
            request.getRequestDispatcher("/pages/6330-lista.jsp").forward(request, response);

    }
    
    @App("6331")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "6331");
                request.getRequestDispatcher("/pages/6330.jsp").forward(request, response);            
            }else{
                Combustivel d = new Combustivel();
            	d.setDescricao(request.getParameter("descricao"));
                d.setValor(new BigDecimal(request.getParameter("valor").replace(",", ".")));

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6331", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=6330");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("6332")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "6332");
                
                int id = Integer.parseInt(request.getParameter("id"));
                Combustivel d = Combustivel.getInstance(id);
                request.setAttribute("combustivel", d);
                request.getRequestDispatcher("/pages/6330.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                Combustivel d = Combustivel.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                d.setValor(new BigDecimal(request.getParameter("valor").replace(",", ".")));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6332", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=6330");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("6333")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        Combustivel b = Combustivel.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6333", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=6330");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
