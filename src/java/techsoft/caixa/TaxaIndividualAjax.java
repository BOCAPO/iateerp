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

@WebServlet(urlPatterns={"/TaxaIndividualAjax/*"})

public class TaxaIndividualAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.caixa.TaxaIndividualAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            
            if (tipo.equals("1")){
                int taxa = Integer.parseInt(request.getParameter("taxa"));
                String conta = request.getParameter("conta");
                
                String sql;
                if (conta.equals("POS")){
                    sql = "UPDATE " +
                                "TB_VAL_TAXA_INDIVIDUAL " + 
                             "SET " + 
                                "IC_SITUACAO_TAXA = 'C', " +
                                "DT_CANCELAMENTO_TAXA = GETDATE(), " +
                                "USER_CANCELAMENTO = '" + request.getParameter("usuario") + "' " +
                             "WHERE " + 
                                "NU_SEQ_TAXA_INDIVIDUAL = " + taxa; 
                    
                }else{
                    sql = "{CALL SP_ESTORNA_PRE_PAGO ("+taxa+",'"+request.getParameter("usuario")+"')}";
                }

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.executeUpdate();
                    cn.commit();
                    
                    Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6191", request.getRemoteAddr());
                    audit.registrarMudanca(sql, Integer.toString(taxa));
                    
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
            } else if(tipo.equals("2")){
                int taxa = Integer.parseInt(request.getParameter("taxa"));
                int mes = Integer.parseInt(request.getParameter("mes"));
                int ano = Integer.parseInt(request.getParameter("ano"));
                
                String sql = "UPDATE " +
                                "TB_VAL_TAXA_INDIVIDUAL " + 
                             "SET " + 
                                "MM_COBRANCA = ?, " +
                                "AA_COBRANCA = ? " +
                             "WHERE " + 
                                "NU_SEQ_TAXA_INDIVIDUAL = ? "; 

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, mes);
                    p.setInt(2, ano);
                    p.setInt(3, taxa);
                    p.executeUpdate();
                    cn.commit();
                    
                    Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6193", request.getRemoteAddr());
                    audit.registrarMudanca(sql, Integer.toString(mes), Integer.toString(ano), Integer.toString(taxa));
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 2" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("3")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                int dependente = Integer.parseInt(request.getParameter("dependente"));
                
                String sql = "SELECT USER_ACESSO_SISTEMA FROM TB_USUARIO_SISTEMA " +
                             "WHERE CD_MATRICULA = " + matricula +
                             " AND CD_CATEGORIA = " + categoria +
                             " AND SEQ_DEPENDENTE = " + dependente; 

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "PROBLEMA";
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 3" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("4")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                int dependente = Integer.parseInt(request.getParameter("dependente"));
                int taxa = Integer.parseInt(request.getParameter("taxa"));
                
                String sql = "SELECT 1 FROM TB_TAXA_BLOQUEADA_PESSOA " +
                             "WHERE CD_MATRICULA = " + matricula +
                             " AND CD_CATEGORIA = " + categoria +
                             " AND SEQ_DEPENDENTE = " + dependente + 
                             " AND CD_TX_ADMINISTRATIVA = " + taxa;

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    if(rs.next()){                    
                        retorno = "PROBLEMA";
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 4" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("5")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                int dependente = Integer.parseInt(request.getParameter("dependente"));
                int taxa = Integer.parseInt(request.getParameter("taxa"));
                
                Connection cn = null;
                String retorno = "OK";
                ResultSet rs = null;
                
                String sql = "SELECT IC_TX_IND_TODAS_TX FROM TB_PESSOA " +
                             "WHERE CD_MATRICULA = " + matricula +
                             " AND CD_CATEGORIA = " + categoria +
                             " AND SEQ_DEPENDENTE = " + dependente;
                
                try {
                    cn = Pool.getInstance().getConnection();
                    rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "PROBLEMA";
                    }else{
                        if(rs.getString("IC_TX_IND_TODAS_TX") == null || rs.getString("IC_TX_IND_TODAS_TX").charAt(0) == 'N'){
                            sql = "SELECT 1 FROM TB_TAXA_PESSOA_AUTORIZADA " +
                                         "WHERE CD_MATRICULA = " + matricula +
                                         " AND CD_CATEGORIA = " + categoria +
                                         " AND SEQ_DEPENDENTE = " + dependente + 
                                         " AND CD_TX_ADMINISTRATIVA = " + taxa;
                            
                            cn = Pool.getInstance().getConnection();
                            rs = cn.createStatement().executeQuery(sql);
                            
                            if(!rs.next()){                    
                                retorno = "PROBLEMA";
                            }
                        }
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 5" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("6")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                
                String sql = "SELECT 1 FROM TB_PESSOA " +
                             "WHERE CD_MATRICULA = " + matricula +
                             " AND CD_CATEGORIA = " + categoria +
                             " AND CD_SIT_PESSOA NOT IN ('US', 'CA') ";

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "PROBLEMA";
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 6" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("7")){
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                
                String sql = "SELECT 1 FROM TB_CATEGORIA " +
                             "WHERE CD_CATEGORIA = " + categoria +
                             " AND TP_CATEGORIA = 'SO' ";

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "PROBLEMA";
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 7" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("8")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                
                String sql = "{CALL SP_VRF_CARNE_TX_INDIVIDUAL ("+matricula+","+categoria+")}";
                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    rs.next();
                    if(rs.getInt(1)>0){        
                        retorno = "PROBLEMA";
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 8" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("9")){
                int taxa = Integer.parseInt(request.getParameter("taxa"));
                
                String sql = "SELECT ISNULL(IC_SELECIONA_CARRO, 'N') FROM TB_TAXA_ADMINISTRATIVA " +
                             "WHERE CD_TX_ADMINISTRATIVA = " + taxa;
                
                Connection cn = null;
                String retorno ;
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    rs.next();
                    if(rs.getString(1).equals("N")){        
                        retorno = "NAO";
                    }else{
                        retorno = "SIM";
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 9" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("10")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                int dependente = Integer.parseInt(request.getParameter("dependente"));
                String senha = request.getParameter("senha");
                
                String sql = "SELECT SENHA_USUARIO_SISTEMA FROM TB_USUARIO_SISTEMA " +
                             "WHERE CD_MATRICULA = " + matricula +
                             " AND CD_CATEGORIA = " + categoria +
                             " AND SEQ_DEPENDENTE = " + dependente;

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "Usuário não cadastrado!";
                    }else{
                        String senha2 = rs.getString("SENHA_USUARIO_SISTEMA").toUpperCase().trim();
                        senha = senha.toUpperCase().trim();
                        if (!senha.equals(senha2)){
                            retorno = "Senha inválida!";
                        }
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 10" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("11")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                int dependente = Integer.parseInt(request.getParameter("dependente"));
                
                String sql = "{CALL SP_RECUPERA_SALDO_PRE_PAGO ("+matricula+","+categoria+","+dependente+",null"+")}";
                Connection cn = null;
                String retorno = "";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "PROBLEMA";
                    }else{
                        if (rs.getFloat("VR_SALDO_ATUAL")>0){
                            DecimalFormat f1 = new DecimalFormat("###,###,###.00");
                            retorno = f1.format(rs.getFloat("VR_SALDO_ATUAL"));
                        }else{
                            retorno = "PROBLEMA";
                        }
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 11" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("12")){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int categoria = Integer.parseInt(request.getParameter("categoria"));
                int dependente = Integer.parseInt(request.getParameter("dependente"));
                float valor = Float.parseFloat(request.getParameter("valor"));
                
                String sql = "{CALL SP_RECUPERA_SALDO_PRE_PAGO ("+matricula+","+categoria+","+dependente+",null"+")}";
                Connection cn = null;
                String retorno = "";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "PROBLEMA";
                    }else{
                        if (rs.getFloat("VR_SALDO_ATUAL")>=valor){
                            retorno = "OK";
                        }else{
                            retorno = "PROBLEMA";
                        }
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 12" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
                
            } else if(tipo.equals("13")){
                int codigo = Integer.parseInt(request.getParameter("codigo"));
                
                String sql = "{CALL SP_RECUPERA_SALDO_PRE_PAGO (null,null,null,"+codigo+")}";
                Connection cn = null;
                String retorno = "";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "PROBLEMA";
                    }else{
                        if (rs.getFloat("VR_SALDO_ATUAL")>0){
                            DecimalFormat f1 = new DecimalFormat("###,###,###.00");
                            retorno = f1.format(rs.getFloat("VR_SALDO_ATUAL"));
                        }else{
                            retorno = "PROBLEMA";
                        }
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 13" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("14")){
                int codigo = Integer.parseInt(request.getParameter("codigo"));
                float valor = Float.parseFloat(request.getParameter("valor"));
                
                String sql = "{CALL SP_RECUPERA_SALDO_PRE_PAGO (null, null, null, "+codigo+")}";
                Connection cn = null;
                String retorno = "";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "PROBLEMA";
                    }else{
                        if (rs.getFloat("VR_SALDO_ATUAL")>=valor){
                            retorno = "OK";
                        }else{
                            retorno = "PROBLEMA";
                        }
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 14" + e.getMessage());
                }finally{
                    try {
                        cn.close();
                    } catch (SQLException e) {
                        log.severe(e.getMessage());
                    }
                }
            } else if(tipo.equals("15")){
                int cdFuncionario  = Integer.parseInt(request.getParameter("cdFuncionario"));
                String senha = request.getParameter("senha");
                
                String sql = "SELECT SENHA_FUNCIONARIO FROM TB_FUNCIONARIO " +
                             "WHERE CD_FUNCIONARIO = " + cdFuncionario;

                Connection cn = null;
                String retorno = "OK";
                try {
                    cn = Pool.getInstance().getConnection();
                    ResultSet rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "Funcionário não cadastrado!";
                    }else{
                        String senha2 = rs.getString("SENHA_FUNCIONARIO");
                        
                        if (rs.wasNull()){
                            retorno = "Funcionário não possui senha para lançamento de taxas!";
                        }else{
                            senha2 = senha2.toUpperCase().trim();
                            senha = senha.toUpperCase().trim();
                            if (!senha.equals(senha2)){
                                retorno = "Senha inválida!";
                            }
                        }

                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
                    cn.close();
                } catch (SQLException e) {
                    log.severe(" tipo 15" + e.getMessage());
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

