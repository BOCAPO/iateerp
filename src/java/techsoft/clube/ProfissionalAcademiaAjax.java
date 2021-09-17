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
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/ProfissionalAcademiaAjax/*"})
public class ProfissionalAcademiaAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.clube.ProfissionalAcademiaAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                
                int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
                int idProfissional = 0;
                if (!request.getParameter("idProfissional").equals("")){
                    idProfissional = Integer.parseInt(request.getParameter("idProfissional"));
                }
                Date dtInicio = null;
                if (!request.getParameter("dtInicio").equals("")){
                    dtInicio = Datas.parse(request.getParameter("dtInicio"));
                }
                Date dtFim = null;
                if (!request.getParameter("dtFim").equals("")){
                    dtFim = Datas.parse(request.getParameter("dtFim"));
                }

                String horario = request.getParameter("horario");
                String dia = request.getParameter("dia");
                
                String hhInicio = horario.substring(0, 4);
                String hhFim = horario.substring(5, 9);
                
                String sql = "{call SP_CHAMA_VALIDA_AGENDA_PROFISSIONAL (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                CallableStatement cal;
                Connection cn;
                cn = Pool.getInstance().getConnection();
                String retorno;
                try {
                    cal = cn.prepareCall(sql);
                    if (dtInicio == null){
                        cal.setNull(1, java.sql.Types.DATE);
                    }else{
                        cal.setDate(1, new java.sql.Date(dtInicio.getTime()));
                    }
                    if (dtFim == null){
                        cal.setNull(2, java.sql.Types.DATE);
                    }else{
                        cal.setDate(2, new java.sql.Date(dtFim.getTime()));
                    }
                    cal.setString(3, hhInicio);
                    cal.setString(4, hhFim);                    
                    cal.setInt(5, idFuncionario);
                    if (dia.equals("Segunda-feira")){
                        cal.setString(6, "S");                  
                    }else{
                        cal.setNull(6, java.sql.Types.CHAR);
                    }
                    if (dia.equals("Terça-feira")){
                        cal.setString(7, "S");                  
                    }else{
                        cal.setNull(7, java.sql.Types.CHAR);
                    }
                    if (dia.equals("Quarta-feira")){
                        cal.setString(8, "S");                  
                    }else{
                        cal.setNull(8, java.sql.Types.CHAR);
                    }
                    if (dia.equals("Quinta-feira")){
                        cal.setString(9, "S");                  
                    }else{
                        cal.setNull(9, java.sql.Types.CHAR);
                    }
                    if (dia.equals("Sexta-feira")){
                        cal.setString(10, "S");                  
                    }else{
                        cal.setNull(10, java.sql.Types.CHAR);
                    }
                    if (dia.equals("Sábado")){
                        cal.setString(11, "S");                  
                    }else{
                        cal.setNull(11, java.sql.Types.CHAR);
                    }
                    if (dia.equals("Domingo")){
                        cal.setString(12, "S");                  
                    }else{
                        cal.setNull(12, java.sql.Types.CHAR);
                    }
                    cal.setInt(13, idProfissional);
                    
                    ResultSet rs = cal.executeQuery();
                    if (rs.next()){
                        if (rs.getString("MSG") == null){
                            retorno = "OK";
                        }else{
                            retorno = rs.getString("MSG");
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
                int idagenda = Integer.parseInt(request.getParameter("idAgenda"));
                String sql = "{call SP_AGENDA_PROF_ACADEMIA (?, null, null, null, null, null, 'E')}";
                CallableStatement cal;
                Connection cn;
                cn = Pool.getInstance().getConnection();
                String retorno;
                try {
                    cal = cn.prepareCall(sql);
                    
                    cal.setInt(1, idagenda);
                    
                    ResultSet rs = cal.executeQuery();
                    if (rs.next()){
                        if (rs.getString("MSG") == null){
                            retorno = "OK";
                        }else{
                            retorno = rs.getString("MSG");
                        }
                    }else{
                        log.warning("Erro na operação, entre em contato com o Administrador do Sistema");
                        retorno = "Erro na validação do agendamento!";
                    }
                    
                    cn.commit();
                            
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

