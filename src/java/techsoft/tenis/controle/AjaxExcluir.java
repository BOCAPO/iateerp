package techsoft.tenis.controle;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import techsoft.tenis.JogoTenis;

@WebServlet("/ajax/excluir")
public class AjaxExcluir extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        
        JogoTenis j = JogoTenis.getInstance(Integer.parseInt(request.getParameter("id")));
        if (j != null) {
            j.delete(null);
            j = JogoTenis.getInstance(Integer.parseInt(request.getParameter("id")));
            if (j == null) json.put("info", "Jogo excluído com sucesso");
            else json.put("erro", "Erro ao excluir o jogo");
        } else {
            json.put("erro", "Jogo inexistente");
        }

        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
}
