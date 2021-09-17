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

@WebServlet(urlPatterns={"/IntegracaoBennerAjax/*"}) 
public class IntegracaoBennerAjax extends HttpServlet {

    private static final Logger log = Logger.getLogger("techsoft.clube.IntegracaoBennerAjax");    

    @Override
    public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String tipo = request.getParameter("tipo");
        Connection cn = null;

        if (tipo.equals("1")){

            request.setCharacterEncoding("UTF-8");
            
            String dtReferencia = request.getParameter("dtReferencia");
            
            Boolean geraCarne = Boolean.parseBoolean(request.getParameter("geraCarne"));
            Boolean geraCancCarne = Boolean.parseBoolean(request.getParameter("geraCancCarne"));
            Boolean geraBaixaCarne = Boolean.parseBoolean(request.getParameter("geraBaixaCarne"));
            Boolean geraBolAvulso = Boolean.parseBoolean(request.getParameter("geraBolAvulso"));
            Boolean geraBaixaBolAvulso = Boolean.parseBoolean(request.getParameter("geraBaixaBolAvulso"));
            Boolean geraPessoas = Boolean.parseBoolean(request.getParameter("geraPessoas"));
            Boolean geraCancPessoas = Boolean.parseBoolean(request.getParameter("geraCancPessoas"));
            Boolean geraTaxa = Boolean.parseBoolean(request.getParameter("geraTaxa"));
            
            String usuario = request.getParameter("usuario");

            String sql = "{call SP_INTEGRACAO_BENNER_TODOS (?,?,?,?,?,?,?,?,?)}";

            try {
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                ParametroAuditoria par = new ParametroAuditoria();
                Auditoria audit = new Auditoria(usuario, "2550", request.getRemoteAddr());

                Date dataRef = Datas.parse(dtReferencia);
                p.setDate(1, new java.sql.Date(par.getSetParametro(dataRef).getTime()));
                
                if (geraCarne){
                    p.setString(2, par.getSetParametro("S"));
                }else{
                    p.setNull(2, java.sql.Types.CHAR);
                    par.getSetNull();
                }
                if (geraCancCarne){
                    p.setString(3, par.getSetParametro("S"));
                }else{
                    p.setNull(3, java.sql.Types.CHAR);
                    par.getSetNull();
                }
                if (geraBaixaCarne){
                    p.setString(4, par.getSetParametro("S"));
                }else{
                    p.setNull(4, java.sql.Types.CHAR);
                    par.getSetNull();
                }
                if (geraBolAvulso){
                    p.setString(5, par.getSetParametro("S"));
                }else{
                    p.setNull(5, java.sql.Types.CHAR);
                    par.getSetNull();
                }
                if (geraBaixaBolAvulso){
                    p.setString(6, par.getSetParametro("S"));
                }else{
                    p.setNull(6, java.sql.Types.CHAR);
                    par.getSetNull();
                }
                if (geraPessoas){
                    p.setString(7, par.getSetParametro("S"));
                }else{
                    p.setNull(7, java.sql.Types.CHAR);
                    par.getSetNull();
                }
                if (geraCancPessoas){
                    p.setString(8, par.getSetParametro("S"));
                }else{
                    p.setNull(8, java.sql.Types.CHAR);
                    par.getSetNull();
                }
                if (geraTaxa){
                    p.setString(9, par.getSetParametro("S"));
                }else{
                    p.setNull(9, java.sql.Types.CHAR);
                    par.getSetNull();
                }
                

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

