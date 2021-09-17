package techsoft.caixa;

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
public class ControleFormaPagamentoCaixa{
    

    @App("6340")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", FormaPagamentoCaixa.listar());
            request.getRequestDispatcher("/pages/6340-lista.jsp").forward(request, response);

    }
    
    @App("6341")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "6341");
                request.getRequestDispatcher("/pages/6340.jsp").forward(request, response);            
            }else{
                FormaPagamentoCaixa d = new FormaPagamentoCaixa();
            	d.setDescricao(request.getParameter("descricao"));

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6341", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=6340");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("6342")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "6342");
                
                int id = Integer.parseInt(request.getParameter("id"));
                FormaPagamentoCaixa d = FormaPagamentoCaixa.getInstance(id);
                request.setAttribute("item", d);
                request.getRequestDispatcher("/pages/6340.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                FormaPagamentoCaixa d = FormaPagamentoCaixa.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6342", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=6340");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("6343")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        FormaPagamentoCaixa b = FormaPagamentoCaixa.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6343", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=6340");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
