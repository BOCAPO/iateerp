package techsoft.seguranca; 

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import java.util.logging.Logger;
import java.text.DecimalFormat;
import java.util.Date;
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/UsuarioAjax/*"})
public class UsuarioAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.seguranca.UsuarioAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                String usuario = request.getParameter("usuario");
                String senha = request.getParameter("senha");
                int aplicacao = Integer.parseInt(request.getParameter("aplicacao"));

                String sql = "{call SP_VERIFICA_SUPERVISOR (?, ?, ?)}";
                Connection cn = null;

                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);
                    p.setString(1, usuario);
                    p.setString(2, senha);
                    p.setInt(3, aplicacao);

                    ResultSet rs = p.executeQuery();
                    if(rs.next()){
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(rs.getString(1));
                    }
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            }
	}
}

