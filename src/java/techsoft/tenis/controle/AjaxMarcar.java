package techsoft.tenis.controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import techsoft.cadastro.Convite;
import techsoft.cadastro.Socio;
import techsoft.tenis.EventoQuadraTenis;
import techsoft.tenis.JogadorTenis;
import techsoft.tenis.JogoTenis;
import techsoft.tenis.QuadraTenis;

@WebServlet("/ajax/marcar")
public class AjaxMarcar extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        QuadraTenis quadra;
        
        try {
            quadra = QuadraTenis.getInstance(Integer.parseInt(request.getParameter("id")));
        } catch (Exception e) {
            return;
        }
        
        JSONObject json = new JSONObject();
        
        DateTime now = new DateTime();
        EventoQuadraTenis atual = quadra.eventoAt(now);
        EventoQuadraTenis livre = quadra.eventoLivre(now);
        
        json.put("date", now.toString("EEE dd/MM/yyyy"));
        json.put("time", now.toString("HH':'mm"));
        json.put("id", quadra.getId());
        json.put("nome", quadra.getNome());
        json.put("timeout", quadra.getDuracaoMarcacao());
        json.put("estado", atual.getEstadoQuadra().toString());
        json.put("observacao", (atual.getObservacao() == null) ? "" : atual.getObservacao());
        
        boolean podeMarcar = quadra.podeMarcarJogo(now);
        json.put("podeMarcar", podeMarcar);

        String restricao = "";
        if (livre != null && livre.getRestricaoJogo() != null) {
            if (livre.getRestricaoJogo() == JogoTenis.Tipo.SIMPLES) restricao = "SIMPLES";
            if (livre.getRestricaoJogo() == JogoTenis.Tipo.DUPLAS)  restricao = "DUPLAS";
            if (!podeMarcar) restricao = "";
        }
        json.put("restricao", restricao);
        
        if (atual.getEstadoQuadra() != QuadraTenis.Estado.LIVRE && livre != null && podeMarcar)
            json.put("liberada", livre.inicio().toString("HH':'mm"));

        Interval i = quadra.previsaoJogo(JogoTenis.Tipo.SIMPLES);
        if (i != null) json.put("simples", "Horário previsto " + i.getStart().toString("HH':'mm") + " &ndash; " + i.getEnd().toString("HH':'mm"));
        i = quadra.previsaoJogo(JogoTenis.Tipo.DUPLAS);
        if (i != null) json.put("duplas", "Horário previsto " + i.getStart().toString("HH':'mm") + " &ndash; " + i.getEnd().toString("HH':'mm"));
        
        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JSONObject json = new JSONObject();
        
        JSONObject jogo = (JSONObject) JSONValue.parse(request.getParameter("jogo"));
        QuadraTenis quadra = QuadraTenis.getInstance(((Long) jogo.get("quadra")).intValue());
        JSONObject jogadores = (JSONObject) jogo.get("jogadores");
        List<JogadorTenis> l = new ArrayList<JogadorTenis>();
        if (jogadores.containsKey("player1"))
            l.add(this.fromJSON((JSONObject) jogadores.get("player1")));
        if (jogadores.containsKey("player2"))
            l.add(this.fromJSON((JSONObject) jogadores.get("player2")));
        if (jogadores.containsKey("player3"))
            l.add(this.fromJSON((JSONObject) jogadores.get("player3")));
        if (jogadores.containsKey("player4"))
            l.add(this.fromJSON((JSONObject) jogadores.get("player4")));
        
        try {
            JogoTenis j = quadra.marcarJogo(l);
            if (j == null)
                json.put("erro", "Erro ao marcar jogo");
            else
                json.put("info", "Marcado jogo no horário de " + j.getHorarioInicio().toString("HH':'mm") + " - " + j.getHorarioFim().toString("HH':'mm"));
        } catch (Exception e) {
            json.put("erro", e.getMessage());
        }
        
        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    private JogadorTenis fromJSON(JSONObject jogador) {
        JogadorTenis j = null;
        String tipo = (String) jogador.get("tipo");
        
        if ("socio".equals(tipo)) {
            int matricula = ((Long) jogador.get("matricula")).intValue();
            int dependente = ((Long) jogador.get("dependente")).intValue();
            int categoria = ((Long) jogador.get("categoria")).intValue();
            j = new JogadorTenis(Socio.getInstance(matricula, dependente, categoria));
        }
        
        if ("convidado".equals(tipo)) {
            int convite = ((Long) jogador.get("convite")).intValue();
            j = new JogadorTenis(Convite.getInstance(convite));
        }
        
        return j;
    }
    
}
