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
import java.text.SimpleDateFormat;
import java.util.Date;
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/MovimentoAjax/*"})

public class MovimentoAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.caixa.MovimentoAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                float valor = Float.parseFloat(request.getParameter("valor"));
                String origem = request.getParameter("origem");
                String nome = request.getParameter("nome");
                
                SaldoComprometido s = SaldoComprometido.verificar(categoria, matricula, valor, origem, nome);
                String retorno = "OK";
                if (s.isLimiteUltrapassado()){
                    retorno = "PROBLEMA";
                }
                
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(retorno);
                
                
            } else if(tipo.equals("2")){
                String sql = ""; 
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                
                Connection cn = null;

                sql = "SELECT " +
                         "ISNULL(SUM(VAL_CHEQUE), 0) " +
                       "FROM " +
                         "TB_CHEQUE_RECEBIDO     T1, " +
                         "TB_MOVIMENTO_CAIXA     T2 " +
                       "WHERE " +
                         "T1.CD_CAIXA       = T2.CD_CAIXA     AND " +
                         "T1.SEQ_ABERTURA   = T2.SEQ_ABERTURA AND " +
                         "T1.SEQ_AUTENTICACAO  = T2.SEQ_AUTENTICACAO   AND " +
                         "T1.DT_MOVIMENTO_CAIXA   = T2.DT_MOVIMENTO_CAIXA AND " +
                         "T1.IC_CONTROLA_PARC_PRE = 'S' AND " +
                         "T2.DT_ESTORNO_MOVIMENTO IS NULL AND " +
                         "T1.DT_DEPOSITO >= CONVERT(DATETIME, CONVERT(VARCHAR, GETDATE(), 103), 103) AND " +
                         "T2.CD_MATRICULA = " + matricula + " AND " +
                         "T2.CD_CATEGORIA = " + categoria;
                
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    rs.next();
                    String retorno = "OK";
                    if (rs.getInt(1)>0) {
                        retorno = "JATEM";
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    
                    rs.close();

                } catch (SQLException e) {
                    log.severe(e.getMessage());
                } finally {
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("3")){
                
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                Date dtVenc = Datas.parse(request.getParameter("dtVenc"));

                String sql = "";
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

                sql = "SELECT COUNT(*) " +
                      "FROM TB_CARNE  " +
                      "WHERE " +
                      "     CD_MATRICULA = " + matricula + " AND " +
                      "     CD_CATEGORIA = " + categoria + " AND " +
                      "     CD_SIT_CARNE = 'NP' AND " +
                      "     DT_VENC_CARNE <= '" + fmt.format(dtVenc) + "'";
                
                Connection cn = null;

                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);

                    ResultSet rs = p.executeQuery();
                    rs.next();
                    String qt = rs.getString(1);
                    rs.close();
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(qt);
                    
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

