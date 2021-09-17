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

@WebServlet(urlPatterns={"/AgendaAcademiaAjax/*"})
public class AgendaAcademiaAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.clube.AgendaAcademiaAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                String titulo = request.getParameter("titulo");
                int matricula = Integer.parseInt(titulo.substring(0, 4));
                int categoria = Integer.parseInt(titulo.substring(4, 6));
                int dependente = Integer.parseInt(titulo.substring(6));
                Date dtInicio = Datas.parseComFormato(request.getParameter("dtInicio"), "yyyy-MM-dd HH:mm:ss");
                int idServico = Integer.parseInt(request.getParameter("idServico"));
                int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
                
                String sql = "{call SP_CHAMA_VALIDA_AGENDAMENTO_ACADEMIA (?, ?, ?, ?, ?, 'C', ?)}";
                CallableStatement cal;
                Connection cn;
                cn = Pool.getInstance().getConnection();
                String retorno;
                try {
                    cal = cn.prepareCall(sql);
                    cal.setInt(1, matricula);
                    cal.setInt(2, categoria);
                    cal.setInt(3, dependente);
                    cal.setTimestamp(4, new java.sql.Timestamp(dtInicio.getTime()));
                    cal.setInt(5, idServico);
                    cal.setInt(6, idFuncionario);
                    
                    ResultSet rs = cal.executeQuery();
                    if (rs.next()){
                        if (rs.getString("RETORNO") == null){
                            retorno = "OK";
                        }else{
                            retorno = rs.getString("RETORNO");
                        }
                    }else{
                        log.warning("Erro na operação, entre em contato com o Administrador do Sistema");
                        retorno = "Erro na validação do agendamento!";
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
            }else if (tipo.equals("2")){
                int idServico = Integer.parseInt(request.getParameter("idAgendamento"));
                
                String sql = "{call SP_CHAMA_VALIDA_CANC_AGEND_ACADEMIA (?)}";
                CallableStatement cal;
                Connection cn;
                cn = Pool.getInstance().getConnection();
                String retorno;
                try {
                    cal = cn.prepareCall(sql);
                    cal.setInt(1, idServico);
                    
                    ResultSet rs = cal.executeQuery();
                    if (rs.next()){
                        retorno = rs.getString("RETORNO");
                    }else{
                        log.warning("Erro na operação, entre em contato com o Administrador do Sistema");
                        retorno = "Erro na validação do agendamento!";
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

