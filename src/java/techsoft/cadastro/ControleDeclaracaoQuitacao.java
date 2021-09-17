package techsoft.cadastro;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;
import techsoft.cadastro.CartaCobranca;

@Controller
public class ControleDeclaracaoQuitacao{
    

    @App("2250")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if("alterar".equals(acao)){
            request.setAttribute("carta", CartaCobranca.getInstance(request.getParameter("descricao")) );
            request.getRequestDispatcher("/pages/2250.jsp").forward(request, response);            
        }else if ("gravar".equals(acao)){

            CartaCobranca d = CartaCobranca.getInstance(request.getParameter("descricao"));
            d.setTexto(request.getParameter("texto"));

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2252", request.getRemoteAddr());
            try{
                d.alterar(audit);
                response.sendRedirect("c?app=2250");

            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }else{
            request.setAttribute("lista", CartaCobranca.listar(("<>'C'")));
            request.getRequestDispatcher("/pages/2250-lista.jsp").forward(request, response);
        }

    }
}
