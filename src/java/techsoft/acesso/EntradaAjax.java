package techsoft.acesso; 

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

@WebServlet(urlPatterns={"/EntradaAjax/*"})
public class EntradaAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.cadastro.EntradaAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                String sql = "SELECT COUNT(*) FROM TB_MATRICULA_CURSO " +
                       " WHERE CD_MATRICULA = " + request.getParameter("matricula") +
                       " AND  CD_CATEGORIA = " + request.getParameter("categoria") +
                       " AND  SEQ_DEPENDENTE = " + request.getParameter("dependente") +
                       " AND NR_PASSAPORTE IS NOT NULL";

                Connection cn = null;

                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    if (rs.next()) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(rs.getString(1));
                    }
                    cn.close();
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

