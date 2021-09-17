package techsoft.tenis.controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import techsoft.tenis.AgendaTenis;
import techsoft.tenis.EventoQuadraTenis;
import techsoft.tenis.JogadorTenis;
import techsoft.tenis.JogoTenis;
import techsoft.tenis.QuadraTenis;

@WebServlet("/ajax/tenis")
public class AjaxTenis extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject responseJSON = new JSONObject();
        
        DateTime agora = DateTime.now();
        
        responseJSON.put("date", agora.toString("EEE dd/MM/yyyy"));
        responseJSON.put("time", agora.toString("HH':'mm"));

        List<JSONObject> quadrasJSON = new ArrayList<JSONObject>();
        for (QuadraTenis quadra : QuadraTenis.listAll()) {
            JSONObject quadraJSON = new JSONObject();
            quadra.ajustaAgenda(agora);
            EventoQuadraTenis atual = quadra.eventoAt(agora);
            EventoQuadraTenis livre = quadra.eventoLivre(agora);

            quadraJSON.put("id", quadra.getId());
            quadraJSON.put("nome", quadra.getNome());
            quadraJSON.put("estado", atual.getEstadoQuadra().toString());
            quadraJSON.put("observacao", StringUtils.trimToEmpty(atual.getObservacao()));
            
            boolean podeMarcar = quadra.podeMarcarJogo(agora);
            quadraJSON.put("podeMarcar", podeMarcar);
            
            String restricao = "";
            if (livre != null && livre.getRestricaoJogo() != null) {
                if (livre.getRestricaoJogo() == JogoTenis.Tipo.SIMPLES) restricao = "Simples";
                if (livre.getRestricaoJogo() == JogoTenis.Tipo.DUPLAS)  restricao = "Duplas";
                if (!podeMarcar) restricao = "";
            }
            quadraJSON.put("restricao", restricao);
            
            if (atual.getEstadoQuadra() != QuadraTenis.Estado.LIVRE && livre != null && podeMarcar)
                quadraJSON.put("liberada", "Livre às " + livre.inicio().toString("HH':'mm"));

            if (atual.intervalo().toPeriod().toStandardMinutes().getMinutes() > 1440)
                quadraJSON.put("periodo", "&#x25d5; " + atual.inicio().toString("dd/MM") + " &ndash; " + atual.fim().plusMinutes(-1).toString("dd/MM"));
            else
                quadraJSON.put("periodo", "&#x25d5; " + atual.inicio().toString("HH':'mm") + " &ndash; " + atual.fim().plusMinutes(-1).toString("HH':'mm"));
            
            List<JSONObject> participantesJSON = new ArrayList<JSONObject>();
            for (JogadorTenis j : atual.getJogadores()) {
                JSONObject participante = new JSONObject();
                participante.put("nome", j.getNome());
                participantesJSON.add(participante);
            }
            quadraJSON.put("participantes", participantesJSON);
            
            List<JSONObject> agendaJSON = new ArrayList<JSONObject>();
            for (EventoQuadraTenis evento : quadra.getAgenda()) {
                JSONObject eventoJSON = new JSONObject();
                if (atual.intervalo().toPeriod().toStandardMinutes().getMinutes() > 1440)
                    eventoJSON.put("intervalo", evento.inicio().toString("dd/MM") + " &ndash; " + evento.fim().plusMinutes(-1).toString("dd/MM"));
                else
                    eventoJSON.put("intervalo", evento.inicio().toString("HH':'mm") + " &ndash; " + evento.fim().plusMinutes(-1).toString("HH':'mm"));


                String s = "";
                if (evento.getEstadoQuadra() == QuadraTenis.Estado.OCUPADA)
                    s = s + "JOGO";
                else
                    s = s + evento.getEstadoQuadra().toString();
                
                if (evento.getRestricaoJogo() != null)
                    s = s + " " + evento.getRestricaoJogo().toString();
                
                if (!StringUtils.isBlank(evento.getObservacao()))
                    s = s + " (" + evento.getObservacao() + ")";
                
                if (evento.getJogadores() != null)
                    for (int i = 0; i < evento.getJogadores().length; i++)
                        s = s + ((i > 0) ? ", "  : ": ") + evento.getJogadores()[i].getNome();
                eventoJSON.put("nome", s);
                if (evento.getOrigem() instanceof JogoTenis) eventoJSON.put("id", ((JogoTenis) evento.getOrigem()).getId());
                agendaJSON.add(eventoJSON);
            }
            quadraJSON.put("agenda", agendaJSON);
            
            quadrasJSON.add(quadraJSON);
        }
        
        responseJSON.put("quadras", quadrasJSON);
        
        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseJSON.toString());
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
