package techsoft.tenis.controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import techsoft.cadastro.Convite;
import techsoft.cadastro.Socio;
import techsoft.tenis.JogadorTenis;
import techsoft.tenis.JogoTenis;

@WebServlet("/ajax/alterar")
public class AjaxAlterar extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        
        JSONObject jogoJSON = (JSONObject) JSONValue.parse(request.getParameter("jogo"));
        JogoTenis jogo = JogoTenis.getInstance(((Long) jogoJSON.get("id")).intValue());
        JSONObject jogadores = (JSONObject) jogoJSON.get("jogadores");
        List<JogadorTenis> l = new ArrayList<JogadorTenis>();
        if (jogadores.containsKey("player1"))
            l.add(this.fromJSON((JSONObject) jogadores.get("player1")));
        if (jogadores.containsKey("player2"))
            l.add(this.fromJSON((JSONObject) jogadores.get("player2")));
        if (jogadores.containsKey("player3"))
            l.add(this.fromJSON((JSONObject) jogadores.get("player3")));
        if (jogadores.containsKey("player4"))
            l.add(this.fromJSON((JSONObject) jogadores.get("player4")));
        
        jogo.alterarJogadores(null, l.toArray(new JogadorTenis[l.size()]));
        json.put("info", "Jogo alterado com sucesso");
        
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
