package techsoft.clube;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.Titular;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.clube.TipoArmario;
import techsoft.operacoes.Taxa;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleTipoArmario{
    

    @App("2380")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", TipoArmario.listar());
            request.getRequestDispatcher("/pages/2380-lista.jsp").forward(request, response);

    }
    
    @App("2381")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2381");
                String sql = " SELECT * FROM tb_taxa_administrativa WHERE IND_TAXA_ADMINISTRATIVA = 'E' ";
                request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));
                
                request.getRequestDispatcher("/pages/2380.jsp").forward(request, response);            
            }else{
                TipoArmario d = new TipoArmario();
            	d.setDescricao(request.getParameter("descricao"));
                if (request.getParameter("taxa")==null){
                    d.setTaxa(null);
                }else{
                    d.setTaxa(Taxa.getInstance(Integer.parseInt(request.getParameter("taxa"))));
                }

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2381", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2380");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("2382")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "2382");
                String sql = " SELECT * FROM tb_taxa_administrativa WHERE IND_TAXA_ADMINISTRATIVA = 'E' ";
                request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));
                
                int id = Integer.parseInt(request.getParameter("id"));
                TipoArmario d = TipoArmario.getInstance(id);
                request.setAttribute("local", d);
                request.getRequestDispatcher("/pages/2380.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                TipoArmario d = TipoArmario.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                if (request.getParameter("taxa")==null){
                    d.setTaxa(null);
                }else{
                    d.setTaxa(Taxa.getInstance(Integer.parseInt(request.getParameter("taxa"))));
                }
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2382", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=2380");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    @App("2383")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        TipoArmario b = TipoArmario.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2383", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2380");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
}
