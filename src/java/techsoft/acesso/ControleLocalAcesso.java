package techsoft.acesso;

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
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleLocalAcesso{
    

    @App("7010")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", LocalAcesso.listar());
            request.getRequestDispatcher("/pages/7010-lista.jsp").forward(request, response);

    }
    
    @App("7011")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "7011");
                request.getRequestDispatcher("/pages/7010.jsp").forward(request, response);            
            }else{
                LocalAcesso d = new LocalAcesso();
            	d.setDescricao(request.getParameter("descricao"));
            	d.setEstacao(request.getParameter("estacao"));
            	d.setRequerExame(request.getParameter("requerExame"));
            	d.setConvUtil(request.getParameter("convUtil"));
            	d.setSoCarro(request.getParameter("soCarro"));
            	d.setMotraPlaca(request.getParameter("motraPlaca"));
            	d.setMostraQuantidade(request.getParameter("mostraQuantidade"));

                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "7011", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=7010");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("7012")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "7012");
                
                int id = Integer.parseInt(request.getParameter("id"));
                LocalAcesso d = LocalAcesso.getInstance(id);
                request.setAttribute("local", d);
                request.getRequestDispatcher("/pages/7010.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                LocalAcesso d = LocalAcesso.getInstance(id);
                d.setId(id); 
            	d.setDescricao(request.getParameter("descricao"));
            	d.setEstacao(request.getParameter("estacao"));
            	d.setRequerExame(request.getParameter("requerExame"));
            	d.setConvUtil(request.getParameter("convUtil"));
            	d.setSoCarro(request.getParameter("soCarro"));
            	d.setMotraPlaca(request.getParameter("motraPlaca"));
            	d.setMostraQuantidade(request.getParameter("mostraQuantidade"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "7012", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=7010");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    
    @App("7013")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        LocalAcesso b = LocalAcesso.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "7013", request.getRemoteAddr());
        b.excluir(audit);

        response.sendRedirect("c?app=7010");
    }        
        
}
