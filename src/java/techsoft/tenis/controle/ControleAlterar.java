package techsoft.tenis.controle;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import techsoft.tenis.JogoTenis;

@WebServlet("/alterar")
public class ControleAlterar extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        boolean admin = (!StringUtils.isEmpty(request.getParameter("admin")) && (request.getUserPrincipal() != null));
        if (admin) request.setAttribute("admin", admin);
        else return;

        int id = Integer.parseInt(request.getParameter("id"));
        JogoTenis j = JogoTenis.getInstance(id);
        request.setAttribute("jogo", j);
        
        request.setAttribute("simples", (j.getTipo() == JogoTenis.Tipo.SIMPLES));
        request.setAttribute("intervalo", "" + j.getHorarioInicio().toString("HH':'mm") + " &ndash; " + j.getHorarioFim().toString("HH':'mm"));
        
        for (int i = 0; i < j.getJogadores().size(); i++) {
            request.setAttribute("nome" + (i + 1), j.getJogadores().get(i).getNome());
            request.setAttribute("foto" + (i + 1), j.getJogadores().get(i).fotoURL());
        }
        
        
        request.getRequestDispatcher("/pages/tenis/alterar-jogo.jsp").forward(request, response);
    }

}
