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
import techsoft.clube.LocalBoxNautica;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleLocalBoxNautica{
    

    @App("2390")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", LocalBoxNautica.listar());
            request.getRequestDispatcher("/pages/2390-lista.jsp").forward(request, response);

    }
    
    @App("2391")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2391");
                request.getRequestDispatcher("/pages/2390.jsp").forward(request, response);            
            }else{
                LocalBoxNautica d = new LocalBoxNautica();
            	d.setDescricao(request.getParameter("descricao"));

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2391", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2390");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("2392")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2392");
                
                int id = Integer.parseInt(request.getParameter("id"));
                LocalBoxNautica d = LocalBoxNautica.getInstance(id);
                request.setAttribute("local", d);
                request.getRequestDispatcher("/pages/2390.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                LocalBoxNautica d = LocalBoxNautica.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2392", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=2390");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("2393")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        LocalBoxNautica b = LocalBoxNautica.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2393", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2390");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
