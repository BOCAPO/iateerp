package techsoft.tenis.controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import techsoft.tenis.AgendaTenis;
import techsoft.tenis.EventoQuadraTenis;
import techsoft.tenis.QuadraTenis;

@WebServlet("/ajax/agenda")
public class AjaxAgenda extends HttpServlet {
    private static final Logger log = Logger.getLogger(AjaxAgenda.class.getCanonicalName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JSONObject json = new JSONObject();
        
        QuadraTenis quadra;
        
        try {
            quadra = QuadraTenis.getInstance(Integer.parseInt(request.getParameter("id")));
        } catch (Exception e) {
            return;
        }

        AgendaTenis a = quadra.getAgenda();
        
        json.put("inicio", a.intervalo().getStart().toString("HH':'mm"));
        json.put("fim", a.intervalo().getEnd().toString("HH':'mm"));
        
        List<JSONObject> eventos = new ArrayList<JSONObject>();
        for (EventoQuadraTenis e: a) {
            JSONObject evento = new JSONObject();
            evento.put("horario", e.intervalo().getStart().toString("HH':'mm") + " - " + e.intervalo().getEnd().toString("HH':'mm"));
            evento.put("estado", e.getEstadoQuadra().toString());
            eventos.add(evento);
        }
        
        json.put("eventos", eventos);
        
        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

}
