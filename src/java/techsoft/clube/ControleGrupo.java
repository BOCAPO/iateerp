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
import techsoft.clube.LocalAcademia;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.Grupo;
import techsoft.seguranca.Usuario;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;


@Controller
public class ControleGrupo{
    

    @App("8020")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("detalhar".equals(acao)){
            int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
            Grupo g = Grupo.getInstance(idGrupo);
            g.carregarPermissoes();
            request.setAttribute("grupo", g);
            request.getRequestDispatcher("/pages/8020-detalhe.jsp").forward(request, response);
        }else{
            request.setAttribute("grupos", Grupo.listar());
            request.getRequestDispatcher("/pages/8020-lista.jsp").forward(request, response);
        }

    }
    
    @App("8021")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.getRequestDispatcher("/pages/8021.jsp").forward(request, response);
        }else{
            Grupo g = new Grupo(request.getParameter("descricao"));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8021", request.getRemoteAddr());
            g.inserir(audit);

            response.sendRedirect("c?app=8020");
        }
            
    }
    @App("8022")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showFormRenomear".equals(acao)){
            int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
            Grupo g = Grupo.getInstance(idGrupo);
            request.setAttribute("grupo", g);

            request.getRequestDispatcher("/pages/8022-renomear.jsp").forward(request, response);
        }else if("showFormAdicionarPermissoes".equals(acao)){
            int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
            Grupo g = Grupo.getInstance(idGrupo);
            g.carregarPermissoes();
            request.setAttribute("grupo", g);

            request.getRequestDispatcher("/pages/8022-adicionar-permissao.jsp").forward(request, response);
        }else if("renomear".equals(acao)){
            int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
            Grupo g = Grupo.getInstance(idGrupo);
            g.setDescricao(request.getParameter("descricao"));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8022", request.getRemoteAddr());
            g.alterar(audit);

            response.sendRedirect("c?app=8020");
        }else if("excluirPermissao".equals(acao)){
            int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
            int idPermissao = Integer.parseInt(request.getParameter("idPermissao"));
            Grupo g = Grupo.getInstance(idGrupo);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8022", request.getRemoteAddr());
            g.excluirPermissao(idPermissao, audit);

            response.sendRedirect("c?app=8020&acao=detalhar&idGrupo=" + idGrupo);
        }else if("adicionarPermissoes".equals(acao)){
            int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
            Grupo g = Grupo.getInstance(idGrupo);
            String[] permissoes = request.getParameterValues("permissoes");
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8022", request.getRemoteAddr());
            for(String s : permissoes){
                    int i = Integer.parseInt(s);
                    g.adicionarPermissao(i, audit);
            }
            response.sendRedirect("c?app=8020&acao=detalhar&idGrupo=" + idGrupo);
        }
            
        }
    
    @App("8023")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
        Grupo g = Grupo.getInstance(idGrupo);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8023", request.getRemoteAddr());
        g.excluir(audit);

        response.sendRedirect("c?app=8020");
    }        

    @App("8024")
    public static void usuarios(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        String acao = request.getParameter("acao");

        int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
        Grupo g = Grupo.getInstance(idGrupo);
        request.setAttribute("grupo", g);

        request.getRequestDispatcher("/pages/8024-usuarios.jsp").forward(request, response);
    }        

    
}



