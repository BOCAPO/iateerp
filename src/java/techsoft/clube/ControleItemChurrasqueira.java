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
public class ControleItemChurrasqueira{
    

    @App("2610")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            int idDependencia = Integer.parseInt(request.getParameter("idDependencia"));
            request.setAttribute("lista", ItemChurrasqueira.listar(idDependencia));
            request.setAttribute("idDependencia", idDependencia);
            request.getRequestDispatcher("/pages/2610-lista.jsp").forward(request, response);

    }
    
    @App("2611")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2611");
                int idDependencia = Integer.parseInt(request.getParameter("idDependencia"));
                request.setAttribute("idDependencia", idDependencia);
                request.getRequestDispatcher("/pages/2610.jsp").forward(request, response);            
            }else{
                ItemChurrasqueira d = new ItemChurrasqueira();
                
                int codigo = Integer.parseInt(request.getParameter("codigo"));
                int quantidade = Integer.parseInt(request.getParameter("quantidade"));
                int idDependencia = Integer.parseInt(request.getParameter("idDependencia"));
                String descricao = request.getParameter("descricao");
                
                d.setCodigo(codigo);
            	d.setDescricao(descricao);
                d.setQuantidade(quantidade);
                d.setObservacao(request.getParameter("observacao"));
                d.setDependencia(idDependencia);

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2611", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2610&idDependencia="+idDependencia);
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("2612")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");
            int idDependencia = Integer.parseInt(request.getParameter("idDependencia"));            
            
            if("showForm".equals(acao)){
                request.setAttribute("app", "2612");
                
                int id = Integer.parseInt(request.getParameter("id"));
                ItemChurrasqueira d = ItemChurrasqueira.getInstance(id);
                
                request.setAttribute("item", d);
                request.setAttribute("idDependencia", idDependencia);
                request.getRequestDispatcher("/pages/2610.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                ItemChurrasqueira d = ItemChurrasqueira.getInstance(id);
                
                int codigo = Integer.parseInt(request.getParameter("codigo"));
                int quantidade = Integer.parseInt(request.getParameter("quantidade"));
                String descricao = request.getParameter("descricao");
                
                d.setCodigo(codigo);
            	d.setDescricao(descricao);
                d.setQuantidade(quantidade);
                d.setObservacao(request.getParameter("observacao"));
                d.setDependencia(idDependencia);
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2612", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=2610&idDependencia="+idDependencia);
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("2613")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        ItemChurrasqueira b = ItemChurrasqueira.getInstance(id);
        
        int idDependencia = Integer.parseInt(request.getParameter("idDependencia"));
        
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2613", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2610&idDependencia="+idDependencia);
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
