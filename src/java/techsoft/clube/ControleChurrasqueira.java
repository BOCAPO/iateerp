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
import techsoft.tabelas.Funcionario;
import techsoft.tabelas.ModeloCarro;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleChurrasqueira{
    

    @App("2590")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        
        if("gerar".equals(acao)){
            for (String p : Collections.list(request.getParameterNames()))
                request.setAttribute(p, request.getParameter(p));

            request.getRequestDispatcher("/pages/2590-impressao.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("/pages/2590.jsp").forward(request, response);
        }
        
    }
    
    @App("2620")
    public static void termoVistoria(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        
        if("gerar".equals(acao)){
            for (String p : Collections.list(request.getParameterNames()))
                request.setAttribute(p, request.getParameter(p));

            request.getRequestDispatcher("/pages/2620-impressao.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("/pages/2620.jsp").forward(request, response);
        }
        
    }
    
}
