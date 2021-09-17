package techsoft.clube;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
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
public class ControleLiberacaoBoletoInternet{
    

    @App("2650")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", LiberacaoBoletoInternet.listar());
            request.getRequestDispatcher("/pages/2650-lista.jsp").forward(request, response);

    }
    
    @App("2651")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2651");
                request.getRequestDispatcher("/pages/2650.jsp").forward(request, response);            
            }else{
                LiberacaoBoletoInternet d = new LiberacaoBoletoInternet();
                
                Date dtVenc = Datas.parse(request.getParameter("dtVenc"));
                Date dtPgto = Datas.parse(request.getParameter("dtPgto"));
                
                d.setDtVenc(dtVenc);
            	d.setDtPgto(dtPgto);

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2651", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2650");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("2652")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");
            
            if("showForm".equals(acao)){
                request.setAttribute("app", "2652");
                
                int id = Integer.parseInt(request.getParameter("id"));
                LiberacaoBoletoInternet d = LiberacaoBoletoInternet.getInstance(id);
                
                request.setAttribute("item", d);
                request.getRequestDispatcher("/pages/2650.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                LiberacaoBoletoInternet d = LiberacaoBoletoInternet.getInstance(id);
                
                Date dtPgto = Datas.parse(request.getParameter("dtPgto"));
                
            	d.setDtPgto(dtPgto);
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2652", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=2650");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("2653")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        LiberacaoBoletoInternet b = LiberacaoBoletoInternet.getInstance(id);
        
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2653", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2650");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
