package techsoft.tabelas; 

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

@WebServlet(urlPatterns={"/TipoEventoAjax/*"})
public class TipoEventoAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.cadastro.TipoEventoAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                
                String deTipoEvento = request.getParameter("deTipoEvento");
                
               if(1==2){
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(deTipoEvento);
                    return;
                }
                
                Connection cn = null;

                String sql = "INSERT INTO TB_TIPO_EVENTO VALUES(?)";
                
                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    p.setString(1, deTipoEvento);

                    p.executeUpdate();
                    ResultSet rs = p.getGeneratedKeys();

                    if(rs.next()){
                        cn.commit();
                        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1451", request.getRemoteAddr());
                        audit.registrarMudanca(sql, deTipoEvento);
                        
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(rs.getInt(1));
                        
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
                 
            }else if (tipo.equals("6")){
                String retorno = "OK";
                
               if(1==2){
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(request.getParameter("matricula") + " - " + request.getParameter("categoria"));
                    return;
                }
                
                
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));

                String sql = "{call SP_RECUPERA_CARNE_PENDENTE (?, ?)}";
                Connection cn = null;

                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);
                    p.setInt(1, matricula);
                    p.setInt(2, categoria);

                    ResultSet rs = p.executeQuery();
                    if(rs.next()){
                        if (rs.getString("CD_ACESSO_PERMITIDO").equals("PA")){
                            retorno = "PA";
                        }
                    }
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
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

