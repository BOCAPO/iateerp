package techsoft.tenis.controle;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/tenis")
public class ControleTenis extends HttpServlet{
    private static final Logger log = Logger.getLogger(ControleTenis.class.getCanonicalName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/pages/tenis/broadcast.jsp").forward(request, response);
    }
}
