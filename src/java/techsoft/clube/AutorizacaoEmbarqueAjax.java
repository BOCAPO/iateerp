package techsoft.cadastro; 

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
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
import java.util.Enumeration;
import techsoft.operacoes.ConviteTermica;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/AutorizacaoEmbarqueAjax/*"}) 
public class AutorizacaoEmbarqueAjax extends HttpServlet {

    private static final Logger log = Logger.getLogger("techsoft.clube.AutorizacaoEmbarqueAjax");    

    @Override
    public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String tipo = request.getParameter("tipo");

        if (tipo.equals("1")){
            String cpf = request.getParameter("cpf");
            String docEst = request.getParameter("docEst");
            String anoValidacao = request.getParameter("anoValidacao");
            
            int seqAutorizacao = 0;
            try{
                seqAutorizacao = Integer.parseInt(request.getParameter("seqAutorizacao"));
            } catch (Exception e) {}

            String sql = "SELECT COUNT(*) QT " +
                         "FROM TB_AUTORIZACAO_EMBARQUE " + 
                         "WHERE YEAR(DT_VALIDADE) = " + anoValidacao + " " + 
                         "AND CD_SIT_AUT_EMBARQUE <> 'CA' ";
            if (cpf.equals("")){
                sql = sql + "AND NR_DOC_ESTRANGEIRO = '" + docEst + "'";
            }else{
                sql = sql + "AND CPF_CONVIDADO = '" + cpf + "'";
            }
            
            if(seqAutorizacao>0){
                sql = sql + "AND SEQ_AUTORIZACAO <> " + seqAutorizacao;
            }

            Connection cn = null;

            try {
                cn = Pool.getInstance().getConnection();
                ResultSet rs = cn.createStatement().executeQuery(sql);
                if (rs.next()) {
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(rs.getString(1));
                }
                cn.close();
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

