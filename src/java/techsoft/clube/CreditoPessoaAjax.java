package techsoft.clube; 

import java.io.IOException;
import java.net.URLDecoder;
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

@WebServlet(urlPatterns={"/CreditoPessoaAjax/*"})

public class CreditoPessoaAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.clube.CreditoPessoaAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                int taxa = Integer.parseInt(request.getParameter("taxa"));
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                int dependente = Integer.parseInt(request.getParameter("dependente"));
                int ano = Integer.parseInt(request.getParameter("ano"));
                int mes = Integer.parseInt(request.getParameter("mes"));
                String valorTx = request.getParameter("valor");
                float valor = Float.parseFloat(valorTx);
                String usuario = request.getParameter("usuario");
                String observacao = URLDecoder.decode(request.getParameter("observacao"));
                
                String sql = "{CALL SP_VAL_CREDITO_PESSOA ('I',null,"+taxa+","+matricula+","+dependente+","+categoria+","+mes+","+ano+",null,?,null,null,'"+usuario+"',null,'"+observacao+"')}";

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);
                    
                    p.setFloat(1, valor);

                    p.executeUpdate();
                    cn.commit();
                    
                    Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1367", request.getRemoteAddr());
                    audit.registrarMudanca(sql, Float.toString(valor));
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(sql + e.getMessage() );
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("2")){
                int id = Integer.parseInt(request.getParameter("id"));
                String usuario = request.getParameter("usuario");
                
                String sql = "{CALL SP_VAL_CREDITO_PESSOA ('C',"+id+",null,null,null,null,null,null,null,null,null,null,null,'"+usuario+"',null)}";

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);
                    
                    p.executeUpdate();
                    cn.commit();
                    
                    Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1367", request.getRemoteAddr());
                    audit.registrarMudanca(sql);
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
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

