package techsoft.tenis.controle;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import techsoft.tenis.QuadraTenis;

@WebServlet("/marcar")
public class ControleMarcar extends HttpServlet {
    private static final Logger log = Logger.getLogger(ControleMarcar.class.getCanonicalName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        QuadraTenis q = QuadraTenis.getInstance(id);
        request.setAttribute("quadra", q);
        
        request.getRequestDispatcher("/pages/tenis/marcar.jsp").forward(request, response);
    }

}
