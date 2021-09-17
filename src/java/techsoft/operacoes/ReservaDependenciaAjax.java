package techsoft.operacoes; 

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import java.util.logging.Logger;
import java.text.DecimalFormat;
import java.util.Date;
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/ReservaDependenciaAjax/*"})

public class ReservaDependenciaAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.cadastro.ReservaDependenciaAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                int dep = Integer.parseInt(request.getParameter("dep"));
                Date inicio = Datas.parseComFormato(request.getParameter("inicio"), "MM/dd/yyyy HH:mm:ss");
                Date fim = Datas.parseComFormato(request.getParameter("fim"), "MM/dd/yyyy HH:mm:ss");
                
                String sql = "SELECT COUNT(*) QT " +
                             "FROM TB_RESERVA_DEPENDENCIA " + 
                             "WHERE SEQ_DEPENDENCIA = ? " + 
                             "AND (? BETWEEN DT_INIC_UTILIZACAO AND DT_FIM_UTILIZACAO " + 
                             "OR ? BETWEEN DT_INIC_UTILIZACAO AND DT_FIM_UTILIZACAO " + 
                             "OR (? < DT_INIC_UTILIZACAO AND ? > DT_FIM_UTILIZACAO)  " + 
                             "OR (? > DT_INIC_UTILIZACAO AND ? < DT_FIM_UTILIZACAO))  " + 
                             "AND CD_STATUS_RESERVA <> 'CA' ";

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, dep);
                    p.setTimestamp(2, new java.sql.Timestamp(inicio.getTime()));
                    p.setTimestamp(3, new java.sql.Timestamp(fim.getTime()));
                    p.setTimestamp(4, new java.sql.Timestamp(inicio.getTime()));
                    p.setTimestamp(5, new java.sql.Timestamp(fim.getTime()));
                    p.setTimestamp(6, new java.sql.Timestamp(inicio.getTime()));
                    p.setTimestamp(7, new java.sql.Timestamp(fim.getTime()));
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt(1)>0){
                            retorno = "PROBLEMA";
                        }
                    }
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
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
            }else if (tipo.equals("2")){
                Date inicio = Datas.parseComFormato(request.getParameter("inicio"), "MM/dd/yyyy");
                
                String sql = " SELECT COUNT(*) " +
                             " FROM TB_PARAMETRO_SISTEMA" + 
                             " WHERE ? < convert(datetime, convert(varchar, GETDATE(), 102)) ";

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setTimestamp(1, new java.sql.Timestamp(inicio.getTime()));
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt(1)>0){
                            retorno = "PROBLEMA";
                        }
                    }
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
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

