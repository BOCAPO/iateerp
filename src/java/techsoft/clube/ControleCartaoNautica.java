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
public class ControleCartaoNautica{
    

    @App("2110")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String idEvento = request.getParameter("idEvento");

            request.setAttribute("idEvento", idEvento);
            request.setAttribute("lista", CartaoNautica.listar(Integer.parseInt(request.getParameter("idEvento"))));
            request.getRequestDispatcher("/pages/2110-lista.jsp").forward(request, response);
    }
    
    @App("2111")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2111");
                request.setAttribute("idEvento", request.getParameter("idEvento"));
                request.getRequestDispatcher("/pages/2110.jsp").forward(request, response);            
            }else{
                CartaoNautica d = new CartaoNautica();
                d.setIdEvento(Integer.parseInt(request.getParameter("idEvento")));
            	d.setCategoria(request.getParameter("categoria"));
                d.setClube(request.getParameter("clube"));
                d.setUf(request.getParameter("uf"));
                d.setNumeracao(request.getParameter("numeracao"));
                d.setNome(request.getParameter("nome"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2111", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2110&idEvento="+request.getParameter("idEvento"));
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("2112")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2112");
                
                int nrCartao = Integer.parseInt(request.getParameter("nrCartao"));
                CartaoNautica d = CartaoNautica.getInstance(nrCartao);
                request.setAttribute("cartao", d);
                request.setAttribute("idEvento", request.getParameter("idEvento"));
                request.getRequestDispatcher("/pages/2110.jsp").forward(request, response);
            }else{
                int nrCartao = Integer.parseInt(request.getParameter("nrCartao"));
                CartaoNautica d = CartaoNautica.getInstance(nrCartao);
            	d.setCategoria(request.getParameter("categoria"));
                d.setClube(request.getParameter("clube"));
                d.setUf(request.getParameter("uf"));
                d.setNumeracao(request.getParameter("numeracao"));
                d.setNome(request.getParameter("nome"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2112", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=2110&idEvento="+request.getParameter("idEvento"));
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("2113")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int nrCartao = Integer.parseInt(request.getParameter("nrCartao"));
        CartaoNautica b = CartaoNautica.getInstance(nrCartao);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2113", request.getRemoteAddr());
        try{
            b.excluir(audit);
            String idEvento = request.getParameter("idEvento");
            response.sendRedirect("c?app=2110&idEvento="+idEvento);
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }    
    
    @App("2114")
    public static void emitirCartao(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        
        int nrCartao = Integer.parseInt(request.getParameter("nrCartao"));
        CartaoNautica c = CartaoNautica.getInstance(nrCartao);
        
        String iateHost = request.getLocalAddr();
        int iatePort = request.getLocalPort();
        String iateApp = request.getContextPath();                
        CredencialNautica cr = new CredencialNautica(c, iateHost, iatePort, iateApp);
        cr.emitir();

        response.sendRedirect("c?app=2110&acao=showForm&idEvento=" + c.getIdEvento());

    }    
}
