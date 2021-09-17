package techsoft.clube; 

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import java.util.logging.Logger;
import java.util.Date;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/RepasseConcessionarioAjax/*"}) 
public class RepasseConcessionarioAjax extends HttpServlet {

    private static final Logger log = Logger.getLogger("techsoft.clube.RepasseConcessionarioAjax");    

    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String tipo = request.getParameter("tipo");
        Connection cn = null;

        if (tipo.equals("1")){

            String usuario = request.getParameter("usuario");
            String matricula = request.getParameter("matricula");
            String categoria = request.getParameter("categoria");
            String dependente = request.getParameter("dependente");
            String taxas = request.getParameter("taxas");
            String mes = request.getParameter("mes");
            String ano = request.getParameter("ano");
            
            String sql = "{call SP_REPASSE_CONCESSIONARIO ('I',?,?,?,?,?,?,?,NULL)}";
            try {
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                ParametroAuditoria par = new ParametroAuditoria();
                Auditoria audit = new Auditoria(usuario, "2571", request.getRemoteAddr());

                String[] vetTaxas = taxas.split(";");
                for(int i = 0;i < vetTaxas.length; i++ ){
                    par.limpaParametro();
                    p.setInt(1, par.getSetParametro(Integer.parseInt(matricula)));
                    p.setInt(2, par.getSetParametro(Integer.parseInt(categoria)));
                    p.setInt(3, par.getSetParametro(Integer.parseInt(dependente)));
                    p.setInt(4, par.getSetParametro(Integer.parseInt(mes)));
                    p.setInt(5, par.getSetParametro(Integer.parseInt(ano)));
                    if (vetTaxas[i].substring(0,3).equals("POS")){
                        p.setInt(6, par.getSetParametro(Integer.parseInt(vetTaxas[i].substring(3))));
                        p.setNull(7, java.sql.Types.CHAR);
                        par.getSetNull();
                        
                    }else{
                        p.setNull(6, java.sql.Types.CHAR);
                        p.setInt(7, par.getSetParametro(Integer.parseInt(vetTaxas[i].substring(3))));
                        par.getSetNull();
                    }
                    
                    p.executeUpdate();
                    cn.commit();

                    audit.registrarMudanca(sql, par.getParametroFinal());

                }

                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");

            } catch (SQLException e) {
                log.severe(e.getMessage());
            }finally{
                try {
                    cn.close();
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }
            }
        }else if (tipo.equals("2")){
            String usuario = request.getParameter("usuario");
            String id = request.getParameter("id");
            
            String sql = "{call SP_REPASSE_CONCESSIONARIO ('E',null,null,null,null,null,null,null,?)}";
            try {
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                ParametroAuditoria par = new ParametroAuditoria();
                Auditoria audit = new Auditoria(usuario, "2571", request.getRemoteAddr());

                p.setInt(1, par.getSetParametro(Integer.parseInt(id)));

                p.executeUpdate();
                cn.commit();

                audit.registrarMudanca(sql, par.getParametroFinal());

                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                
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

