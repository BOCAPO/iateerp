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
import techsoft.seguranca.Usuario;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;


@Controller
public class ControleUsuario{
    

    @App("8030")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("detalhar".equals(acao)){
            Usuario u = Usuario.getInstance(request.getParameter("login"));
            u.carregarGrupos();
            request.setAttribute("usuario", u);
            request.getRequestDispatcher("/pages/8030-detalhe.jsp").forward(request, response);
        }else{
            request.setAttribute("usuarios", Usuario.listar());
            request.getRequestDispatcher("/pages/8030-lista.jsp").forward(request, response);
        }

    }
    
    @App("8031")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.getRequestDispatcher("/pages/8031.jsp").forward(request, response);
        }else{
            Usuario u = new Usuario(request.getParameter("login"), request.getParameter("nome"));
            u.setObservacao(request.getParameter("observacao"));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8032", request.getRemoteAddr());
            u.inserir(audit);

            response.sendRedirect("c?app=8030");
        }
    }
    @App("8032")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showFormRenomear".equals(acao)){
            Usuario u = Usuario.getInstance(request.getParameter("login"));
            u.carregarGrupos();
            request.setAttribute("usuario", u);

            request.getRequestDispatcher("/pages/8032-renomear.jsp").forward(request, response);
        }else if("showFormAdicionarGrupos".equals(acao)){
            Usuario u = Usuario.getInstance(request.getParameter("login"));
            u.carregarGrupos();
            request.setAttribute("usuario", u);

            request.getRequestDispatcher("/pages/8032-adicionar-grupo.jsp").forward(request, response);
        }else if("renomear".equals(acao)){
            Usuario u = Usuario.getInstance(request.getParameter("login"));
            u.setNome(request.getParameter("nome"));
            u.setObservacao(request.getParameter("observacao"));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8032", request.getRemoteAddr());
            u.alterar(audit);

            response.sendRedirect("c?app=8030");
        }else if("excluirGrupo".equals(acao)){
            Usuario u = Usuario.getInstance(request.getParameter("login"));
            int idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8032", request.getRemoteAddr());
            u.excluirGrupo(idGrupo, audit);

            response.sendRedirect("c?app=8030&acao=detalhar&login=" + u.getLogin());
        }else if("adicionarGrupos".equals(acao)){
            Usuario u = Usuario.getInstance(request.getParameter("login"));
            String[] grupos = request.getParameterValues("grupos");
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8032", request.getRemoteAddr());
            for(String s : grupos){
                    int i = Integer.parseInt(s);
                    u.adicionarGrupo(i, audit);
            }
            response.sendRedirect("c?app=8030&acao=detalhar&login=" + u.getLogin());
        }
            
        }
    
    @App("8033")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        Usuario u = Usuario.getInstance(request.getParameter("login"));
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8033", request.getRemoteAddr());
        u.excluir(audit);

        response.sendRedirect("c?app=8030");
    }        

    @App("8034")
    public static void senhaMaster(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        String acao = request.getParameter("acao");
        
        if("showFormDefinirSenha".equals(acao)){
            Usuario u = Usuario.getInstance(request.getParameter("login"));
            request.setAttribute("usuario", u);

            request.getRequestDispatcher("/pages/8034.jsp").forward(request, response);
        }else{
            Usuario u = Usuario.getInstance(request.getParameter("login"));
            u.definirSenha(request.getParameter("novaSenha1"));

            response.sendRedirect("c?app=8030");
        }
    }        

    @App("8035")
    public static void ativarDesativar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        Usuario u = Usuario.getInstance(request.getParameter("login"));
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "8035", request.getRemoteAddr());
        u.ativarDesativa(audit);

        response.sendRedirect("c?app=8030");
    }        
    
    @App("8036")
    public static void acessos(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        Usuario u = Usuario.getInstance(request.getParameter("login"));
        request.setAttribute("usuario", u);

        request.getRequestDispatcher("/pages/8036-acessos.jsp").forward(request, response);
        
    }        
}
