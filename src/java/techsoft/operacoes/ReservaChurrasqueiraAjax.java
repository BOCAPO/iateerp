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
import techsoft.seguranca.ParametroAuditoria;

@WebServlet(urlPatterns={"/ReservaChurrasqueiraAjax/*"})

public class ReservaChurrasqueiraAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.cadastro.ReservaChurrasqueiraAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
 
            }else if (tipo.equals("2")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                int dependencia = Integer.parseInt(request.getParameter("dep"));
                String tpValidacao = request.getParameter("tpValidacao");
                
                String data = request.getParameter("data");
                Date dt = Datas.parse(data);
                
                String retorno = "";
                
                String sql = "{call SP_CHAMA_VALIDA_RES_CHU(?, ?, ?, ?, ?, ?)}" ;

                Connection cn = null;
                cn = Pool.getInstance().getConnection();
                CallableStatement cal = null;

                try {
                    cal = cn.prepareCall(sql);
                    cal.setInt(1, dependencia);
                    cal.setInt(2, categoria);
                    cal.setInt(3, matricula);
                    cal.setInt(4, 0);
                    cal.setDate(5, new java.sql.Date(dt.getTime()));
                    cal.setString(6, tpValidacao);

                    ResultSet rs = cal.executeQuery();
                    if (rs.next()) {
                        retorno = rs.getString(1);
                    }    
                    cn.close();

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
                 
            }else if (tipo.equals("3")){
                
            }else if (tipo.equals("4")){
                 
            }else if (tipo.equals("5")){
                String usuario = request.getParameter("usuario");
                String senha = request.getParameter("senha");

                String sql = "{call SP_VERIFICA_SUPERVISOR (?, ?, ?)}";
                Connection cn = null;

                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);
                    p.setString(1, usuario);
                    p.setString(2, senha);
                    p.setInt(3, 1180);

                    ResultSet rs = p.executeQuery();
                    if(rs.next()){
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(rs.getString(1));
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
                String usuario = request.getParameter("usuario");
                String senha = request.getParameter("senha");

                String sql = "{call SP_VERIFICA_SUPERVISOR (?, ?, 1068)}";
                Connection cn = null;

                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);
                    p.setString(1, usuario);
                    p.setString(2, senha);

                    ResultSet rs = p.executeQuery();
                    if(rs.next()){
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(rs.getString(1));
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
            }else if (tipo.equals("7")){
                String usuario = request.getParameter("usuario");
                String idRes = request.getParameter("idRes");

                //strSql = "EXEC SP_CANCELA_CHU_INTERNET " & txtSeqReserva & ", '" & gUsuarioApp & "', 1067, null, '" & NomeEstacao & "'"
                String sql = "{call SP_CANCELA_CHU_INTERNET (?, ?, 1067, null, ?)}";
                Connection cn = null;

                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, Integer.parseInt(idRes));
                    p.setString(2, usuario);
                    p.setString(3, request.getRemoteAddr());
                    
                    p.execute();
                    cn.commit();
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("OK");

                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
                 
            }else if (tipo.equals("8")){
                
                if (Integer.parseInt(request.getParameter("idDep")) < 0){
                    
                    response.getWriter().write("Churrasqueira inválida!");
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    
                    return;
                    
                }
                
                Connection cn = null;
                ReservaChurrasqueira d = new ReservaChurrasqueira();
                d.setDependencia(Integer.parseInt(request.getParameter("idDep")));
                d.setMatricula(Integer.parseInt(request.getParameter("matricula")));
                d.setCategoriaSocio(Integer.parseInt(request.getParameter("categoria")));
                d.setDependente(Integer.parseInt(request.getParameter("dependente")));
                d.setDtInicio(Datas.parseComFormato(request.getParameter("dtUtil"), "yyyy-MM-dd"));
                d.setDtFim(Datas.parseComFormato(request.getParameter("dtUtil"), "yyyy-MM-dd"));
                d.setInteressado(request.getParameter("interessado"));
                d.setTelefone(request.getParameter("telefone"));
                if (request.getParameter("horario").equals("D")){
                    d.setHhInicio("0900");
                    d.setHhFim("1800");
                }else{    
                    d.setHhInicio("1800");
                    d.setHhFim("2200");
                }

                d.setUsuario(request.getUserPrincipal().getName());
                d.setSupervisao(request.getParameter("supervisao"));
                d.setMotivoBloqueio(request.getParameter("motivoBloqueio"));


                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1066", request.getRemoteAddr());

                try {
                    cn = Pool.getInstance().getConnection();

                    String sql = "{call SP_RES_CHURRASQUEIRA_CLUBE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

                    PreparedStatement p = cn.prepareStatement(sql);
                    ParametroAuditoria par = new ParametroAuditoria();

                    p.setInt(1, par.getSetParametro(d.getDependencia()));
                    p.setInt(2, par.getSetParametro(d.getCategoriaSocio()));
                    p.setInt(3, par.getSetParametro(d.getMatricula()));
                    p.setInt(4, par.getSetParametro(d.getDependente()));
                    p.setDate(5, new java.sql.Date(par.getSetParametro(d.getDtInicio()).getTime()));
                    p.setDate(6, new java.sql.Date(par.getSetParametro(d.getDtFim()).getTime()));
                    p.setString(7, par.getSetParametro(d.getInteressado()));
                    p.setString(8, par.getSetParametro(d.getTelefone()));
                    p.setString(9, par.getSetParametro(d.getHhInicio()));
                    p.setString(10, par.getSetParametro(d.getHhFim()));
                    p.setString(11, par.getSetParametro(d.getUsuario()));
                    p.setString(12, par.getSetParametro(d.getSupervisao()));
                    p.setString(13, par.getSetParametro(d.getMotivoBloqueio()));

                    String err = "";    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        if (rs.getString("MSG").equals("OK")) {
                            cn.commit();
                            audit.registrarMudanca(sql, par.getParametroFinal());
                            err = "OK";
                        } else {
                            err = rs.getString("MSG");
                            log.warning(err);
                        }
                    } else {
                        err = "Erro na operação, entre em contato com o Administrador do Sistema";
                        log.warning(err);
                    }

                    response.getWriter().write(err);
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");

            } catch (SQLException e) {
                try {
                    cn.rollback();
                } catch (SQLException ex) {
                    log.severe(ex.getMessage());
                }

                log.severe(e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }
            }                
                
                 
            }

	}
}

