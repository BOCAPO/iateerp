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
public class ControleTipoEventoAcesso{
    

    @App("7020")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", TipoEventoAcesso.listar());
            request.getRequestDispatcher("/pages/7020-lista.jsp").forward(request, response);

    }
    
    @App("7021")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "7021");
                request.getRequestDispatcher("/pages/7020.jsp").forward(request, response);            
            }else{
                TipoEventoAcesso d = new TipoEventoAcesso();
            	d.setDescricao(request.getParameter("descricao"));
            	d.setSituacao(request.getParameter("situacao"));
            	d.setStatus(request.getParameter("status"));
            	d.setDtInicio(Datas.parse(request.getParameter("dtInicio")));
            	d.setDtFim(Datas.parse(request.getParameter("dtFim")));

                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "7021", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=7020");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("7022")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "7022");
                
                int id = Integer.parseInt(request.getParameter("id"));
                TipoEventoAcesso d = TipoEventoAcesso.getInstance(id);
                request.setAttribute("tipoEvento", d);
                request.getRequestDispatcher("/pages/7020.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                TipoEventoAcesso d = TipoEventoAcesso.getInstance(id);
                d.setId(id); 
            	d.setDescricao(request.getParameter("descricao"));
            	d.setSituacao(request.getParameter("situacao"));
            	d.setStatus(request.getParameter("status"));
            	d.setDtInicio(Datas.parse(request.getParameter("dtInicio")));
            	d.setDtFim(Datas.parse(request.getParameter("dtFim")));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "7022", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=7020");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    
    @App("7023")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        TipoEventoAcesso b = TipoEventoAcesso.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "7023", request.getRemoteAddr());
        b.excluir(audit);

        response.sendRedirect("c?app=7010");
    }        
        
}
