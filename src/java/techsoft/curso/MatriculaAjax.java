package techsoft.curso; 

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
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/MatriculaAjax/*"})
public class MatriculaAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.curso.MatriculaAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int dependente = Integer.parseInt(request.getParameter("dependente"));
            int categoria = Integer.parseInt(request.getParameter("categoria"));
            int turma = Integer.parseInt(request.getParameter("turma"));
            
            if (tipo.equals("1")){
                Connection cn = null;
                cn = Pool.getInstance().getConnection();
                String retorno = "OK";
                try {
                    CallableStatement c = cn.prepareCall("{call SP_VRF_PRORATA_CANC_CURSO(?, ?, ?, ?)}");
                    c.setInt(1, matricula);
                    c.setInt(2, categoria);            
                    c.setInt(3, dependente);
                    c.setInt(4, turma);
                    ResultSet rs = c.executeQuery();
                    if(rs.next()){
                        String msg = rs.getString("msg");
                        if(msg != null && !msg.equalsIgnoreCase("OK")){
                            if(msg.equalsIgnoreCase("VR")){
                                if(rs.getInt("VR_TX_PRORATA") > 0){
                                    DecimalFormat fmt = new DecimalFormat("#,##0.00");
                                    String question = "O cancelamento desta matrícula vai gerar uma taxa valor de R$";
                                    question += fmt.format(rs.getFloat("VR_TX_PRORATA"));
                                    question += " a ser cobrada no carne do sócio, referente a ";
                                    question += rs.getInt("QT_DIAS");
                                    question += " dias de utilização do serviço. Confirma o cancelamento e a geração da referida taxa?";
                                    retorno = "A&" + question;
                                }
                            }else{
                                retorno = "E&" + msg;
                            }
                        }
                    }
                    
                    cn.close();
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    return;
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

