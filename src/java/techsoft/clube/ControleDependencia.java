package techsoft.clube;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collections;
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
import techsoft.clube.LocalAcademia;
import techsoft.tabelas.Cor;
import techsoft.tabelas.Dependencia;
import techsoft.tabelas.Funcionario;
import techsoft.tabelas.ModeloCarro;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleDependencia{
    

    @App("1080")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        request.setAttribute("dependencias", Dependencia.listar());
        request.getRequestDispatcher("/pages/1080-lista.jsp").forward(request, response);
        
    }
    @App("1081")
    public static void inserir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("showForm".equals(acao)){
            request.setAttribute("app", "1081");
            request.getRequestDispatcher("/pages/1080.jsp").forward(request, response);
        }else{
            Dependencia d = new Dependencia();
            d.setDescricao(request.getParameter("descricao"));
            d.setTextoComprovante(request.getParameter("textoComprovante"));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1081", request.getRemoteAddr());
            try{
                d.inserir(audit);
                response.sendRedirect("c?app=1080");
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }
        
    }
    
    @App("1082")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "1082");
            int idDependencia = Integer.parseInt(request.getParameter("idDependencia"));
            Dependencia d = Dependencia.getInstance(idDependencia);
            request.setAttribute("dependencia", d);
            request.getRequestDispatcher("/pages/1080.jsp").forward(request, response);
        }else{
            int idDependencia = Integer.parseInt(request.getParameter("idDependencia"));
            Dependencia d = Dependencia.getInstance(idDependencia);
            d.setDescricao(request.getParameter("descricao"));
            d.setTextoComprovante(request.getParameter("textoComprovante"));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1082", request.getRemoteAddr());

            d.alterar(audit);

            response.sendRedirect("c?app=1080&acao=detalhar&idDependencia=" + idDependencia);
        }
    }
    
    @App("1083")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        int idDependencia = Integer.parseInt(request.getParameter("idDependencia"));
        Dependencia d = Dependencia.getInstance(idDependencia);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1083", request.getRemoteAddr());
        d.excluir(audit);

        response.sendRedirect("c?app=1080");
    }
    
}

