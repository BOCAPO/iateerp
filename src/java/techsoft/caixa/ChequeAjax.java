package techsoft.caixa; 

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

@WebServlet(urlPatterns={"/ChequeAjax/*"})

public class ChequeAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.caixa.ChequeAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                String dtDep = request.getParameter("dtDeposito");
                int banco = Integer.parseInt(request.getParameter("banco"));
                int nrCheque = Integer.parseInt(request.getParameter("nrCheque"));
                int seqCheque = Integer.parseInt(request.getParameter("seqCheque"));
                String contrParc = request.getParameter("contrParc");
                
                String sql = "UPDATE " +
                                "TB_CHEQUE_RECEBIDO " + 
                             "SET " + 
                                "IC_CONTROLA_PARC_PRE = ";
                if ("checked".equals(contrParc)){
                    sql = sql + "'S', ";
                }else{
                    sql = sql + "'N', ";
                }
                
                sql = sql + "DT_DEPOSITO = '" + dtDep.substring(3,6)+dtDep.substring(0,3)+dtDep.substring(6) + "' " +
                        "WHERE " + 
                            "CD_BANCO = " + Integer.toString(banco) + " AND " + 
                            "NR_CHEQUE = " + Integer.toString(nrCheque) + " AND " + 
                            "SEQ_CHEQUE = " + Integer.toString(seqCheque);

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.executeUpdate();
                    cn.commit();
                    
                    Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6051", request.getRemoteAddr());
                    audit.registrarMudanca(sql);
                    
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

