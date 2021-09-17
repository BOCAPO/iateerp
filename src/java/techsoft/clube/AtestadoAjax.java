package techsoft.clube; 

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
import techsoft.db.PoolFoto;
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/AtestadoAjax/*"})

public class AtestadoAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.caixa.TaxaIndividualAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                int dependente = Integer.parseInt(request.getParameter("dependente"));
                
                String sql;
                sql = "UPDATE " +
                            "TB_ATESTADO_MEDICO_PESSOA " + 
                         "SET " + 
                            "FOTO_ATESTADO = NULL " +
                         "WHERE " + 
                            "CD_MATRICULA = " + matricula + " AND "  +
                            "CD_CATEGORIA = " + categoria + " AND " +
                            "SEQ_DEPENDENTE = " + dependente;

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = PoolFoto.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.executeUpdate();
                    cn.commit();
                    
                    Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3061", request.getRemoteAddr());
                    audit.registrarMudanca(sql);
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 1" + e.getMessage() );
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

