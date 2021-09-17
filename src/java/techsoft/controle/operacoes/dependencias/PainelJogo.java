package techsoft.controle.operacoes.dependencias;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.tenis.JogadorTenis;
import techsoft.tenis.JogoTenis;
import techsoft.tenis.QuadraTenis;

@Controller
public class PainelJogo {
    
    @App("2370")
    public static void painel(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    
        request.setAttribute("admin", true);
        
        if (request.getParameter("id") != null) PainelJogo.alterar(request, response);
        else request.getRequestDispatcher("/pages/tenis/broadcast.jsp").forward(request, response);
    }

    @App("2371")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    
        int id = Integer.parseInt(request.getParameter("id"));
        QuadraTenis q = QuadraTenis.getInstance(id);
        request.setAttribute("quadra", q);
        
        request.setAttribute("admin", true);
        request.getRequestDispatcher("/pages/tenis/marcar.jsp").forward(request, response);
    }
    
    @App({"2372", "2373"})
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        JogoTenis j = JogoTenis.getInstance(id);
        request.setAttribute("jogo", j);
        request.setAttribute("quadra", j.getQuadra());
        request.setAttribute("tipo", j.getTipo().toString());
        request.setAttribute("jogadores", j.getJogadores().toArray(new JogadorTenis[j.getJogadores().size()]));
        request.setAttribute("horario", "Horário " + j.getHorarioInicio().toString("HH':'mm") + " &ndash; " + j.getHorarioFim().toString("HH':'mm"));
        
        request.setAttribute("admin", true);
        request.getRequestDispatcher("/pages/tenis/alterar.jsp").forward(request, response);
    }
    
}
