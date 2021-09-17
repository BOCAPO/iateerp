package techsoft.operacoes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.acesso.LocalAcesso;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.Titular;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.Usuario;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleTaxa{
    

    @App("1410")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("usuario".equals(acao)){
            int id = Integer.parseInt(request.getParameter("idTaxa"));
            Taxa d = Taxa.getInstance(id);
            request.setAttribute("taxa", d);
            request.setAttribute("usuarios", Usuario.listar());
            request.getRequestDispatcher("/pages/1418.jsp").forward(request, response);
        }else if("gravarUsuario".equals(acao)){
                String usuarios="";
                if(request.getParameterValues("idUsuario") != null){
                    for(String s : request.getParameterValues("idUsuario")){
                        usuarios = usuarios + s + ";";
                    }
                }
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1414", request.getRemoteAddr());
                Taxa.atualizarTaxasIndividuais(Integer.parseInt(request.getParameter("idTaxa")), usuarios, audit);
                response.sendRedirect("c?app=1410");

        }else{
            request.setAttribute("lista", Taxa.listar());
            request.getRequestDispatcher("/pages/1410-lista.jsp").forward(request, response);
        }
                

    }
    
    @App("1411")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "1411");
                request.getRequestDispatcher("/pages/1410.jsp").forward(request, response);            
            }else{
                Taxa d = new Taxa();
            	d.setDescricao(request.getParameter("descricao"));
            	d.setTipo(request.getParameter("tipo"));
                d.setReceita(request.getParameter("receita"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1411", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1410");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("1412")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "1412");
                
                int id = Integer.parseInt(request.getParameter("id"));
                Taxa d = Taxa.getInstance(id);
                request.setAttribute("taxa", d);
                request.getRequestDispatcher("/pages/1410.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                Taxa d = Taxa.getInstance(id);
                d.setId(id); 
            	d.setDescricao(request.getParameter("descricao"));
            	d.setTipo(request.getParameter("tipo"));
                d.setReceita(request.getParameter("receita"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1412", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=1410");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    
    @App("1413")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        Taxa b = Taxa.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1413", request.getRemoteAddr());
        b.excluir(audit);

        response.sendRedirect("c?app=1410");
    }      
}
