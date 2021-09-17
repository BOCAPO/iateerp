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
public class ControleEventoNautica{
    

    @App("2030")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", EventoNautica.listar());
            request.getRequestDispatcher("/pages/2030-lista.jsp").forward(request, response);

    }
    
    @App("2031")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2031");
                request.getRequestDispatcher("/pages/2030.jsp").forward(request, response);            
            }else{
                EventoNautica d = new EventoNautica();
            	d.setDescricao(request.getParameter("descricao"));
                d.setDtInicio(Datas.parse(request.getParameter("dtInicio")));
                d.setDtFim(Datas.parse(request.getParameter("dtFim")));

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2031", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2030");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("2032")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2032");
                
                int id = Integer.parseInt(request.getParameter("id"));
                EventoNautica d = EventoNautica.getInstance(id);
                request.setAttribute("evento", d);
                request.getRequestDispatcher("/pages/2030.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                EventoNautica d = EventoNautica.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                d.setDtInicio(Datas.parse(request.getParameter("dtInicio")));
                d.setDtFim(Datas.parse(request.getParameter("dtFim")));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2032", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=2030");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("2033")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        EventoNautica b = EventoNautica.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2033", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2030");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
