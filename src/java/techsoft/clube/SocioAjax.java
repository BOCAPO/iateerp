package techsoft.clube; 

import java.io.IOException;
import java.sql.CallableStatement;
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
import techsoft.tabelas.ExcluirException;
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/SocioAjax/*"})
public class SocioAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.clube.SocioAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                
                String sql = "SELECT NOME_PESSOA FROM TB_PESSOA WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = 0";
                CallableStatement cal;
                Connection cn;
                cn = Pool.getInstance().getConnection();
                String retorno;
                try {
                    cal = cn.prepareCall(sql);
                    cal.setInt(1, matricula);
                    cal.setInt(2, categoria);
                    
                    ResultSet rs = cal.executeQuery();
                    if (rs.next()){
                        if (rs.getString("NOME_PESSOA") == null){
                            retorno = "NÃO ENCONTRADO";
                        }else{
                            retorno = rs.getString("NOME_PESSOA");
                        }
                    }else{
                        retorno = "NÃO ENCONTRADO";
                    }
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    
                } catch (SQLException e) {
                    try {
                        cn.rollback();
                    } catch (SQLException ex) {
                        log.severe(ex.getMessage());
                    }

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

