package internet; 

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/CarneItauAjax/*"})
public class CarneItauAjax extends HttpServlet {
 
    private static final Logger log = Logger.getLogger("internet.CarneItauAjax");    

    @Override
    public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {

        String tipo = request.getParameter("tipo");

        if (tipo.equals("1")){
            String sql = "SELECT COUNT(*) QTD FROM TB_ENVIO_CARNE_ITAU WHERE IC_ENVIADO = 'N' ";

            Connection cn = null;
            try {
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);

                ResultSet rs = p.executeQuery();
                rs.next();
                String retorno = rs.getString("QTD");

                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(retorno);
            } catch (SQLException e) {
                log.severe(sql + e.getMessage() );
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

