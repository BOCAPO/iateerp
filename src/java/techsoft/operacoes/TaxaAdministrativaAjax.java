package techsoft.operacoes; 

import java.io.IOException;
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

@WebServlet(urlPatterns={"/TaxaAdministrativaAjax/*"})
public class TaxaAdministrativaAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.operacoes.TaxaAdministrativaAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio"));
            Date dtFim = null;
            if (!request.getParameter("dtFim").equals("")){
                dtFim = Datas.parseDataHora(request.getParameter("dtFim"));
            }
            
            //*****************************
            //VALIDACAO DA INCLUSAO
            //*****************************
            if (tipo.equals("1")){

                String sql = "SELECT 1 FROM TB_VAL_TX_ADMINISTRATIVA " +
                             "WHERE CD_TX_ADM = ? " +
                             " AND  CD_CATEGORIA = ? " +
                             " AND  DT_VALID_FIM_TX_ADM >= ? " +
                             " AND  DT_VALID_INIC_TX_ADM <= ?  ";


                Connection cn = Pool.getInstance().getConnection();
                try {
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, idTaxa);
                    p.setInt(2, idCategoria);
                    p.setTimestamp(3, new java.sql.Timestamp(dtInicio.getTime()));
                    p.setTimestamp(4, new java.sql.Timestamp(dtInicio.getTime()));
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("E&J� existe outra Taxa cadastrada com Data de Fim maior que esta Data de In�cio!");
                        return;
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
                 
                
                sql = "SELECT 1 FROM TB_VAL_TX_ADMINISTRATIVA " +
                       "WHERE CD_TX_ADM = ? "+
                       " AND CD_CATEGORIA = ? ";
                
                if (dtFim == null) {
                    sql= sql + " AND DT_VALID_INIC_TX_ADM >= ? ";
                }else{
                    sql= sql +
                       " AND  DT_VALID_INIC_TX_ADM <= ? " +
                       " AND  (DT_VALID_FIM_TX_ADM >= ? OR DT_VALID_FIM_TX_ADM IS NULL) " +
                       " AND  DT_VALID_INIC_TX_ADM > ?  ";
                }
                
                cn = Pool.getInstance().getConnection();
                try {
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, idTaxa);
                    p.setInt(2, idCategoria);
                    if (dtFim == null) {
                        p.setTimestamp(3, new java.sql.Timestamp(dtInicio.getTime()));
                    }else{
                        p.setTimestamp(3, new java.sql.Timestamp(dtFim.getTime()));
                        p.setTimestamp(4, new java.sql.Timestamp(dtFim.getTime()));
                        p.setTimestamp(5, new java.sql.Timestamp(dtInicio.getTime()));
                    }
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("E&J� existe outra Taxa cadastrada em colis�o com esta Data de Fim! Informe Data de Fim anterior a Data de In�cio da outra Taxa!");
                        return;
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
          

                sql = "SELECT 1 FROM TB_VAL_TX_ADMINISTRATIVA " +
                       "WHERE CD_TX_ADM = ? "+
                       " AND CD_CATEGORIA = ? "+
                       " AND  DT_VALID_INIC_TX_ADM <= ? " +
                       " AND  DT_VALID_FIM_TX_ADM IS NULL ";
                
                cn = Pool.getInstance().getConnection();
                try {
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, idTaxa);
                    p.setInt(2, idCategoria);
                    p.setTimestamp(3, new java.sql.Timestamp(dtInicio.getTime()));
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("A&Existe outra taxa anterior a esta com Data de Fim em aberto. Deseja encerrar a outra taxa com a data de Fim imediatamente anterior a Data de In�cio desta nova Taxa?");
                        return;
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
            
            //*****************************
            //VALIDACAO DA ALTERACAO
            //*****************************
            if (tipo.equals("2")){
                String sql = "";
                Connection cn = Pool.getInstance().getConnection();
                if (dtFim==null){
                    sql = "SELECT 1 FROM TB_VAL_TX_ADMINISTRATIVA " +
                            "WHERE CD_TX_ADM = ? " +
                            " AND  CD_CATEGORIA = ? " +
                            " AND  DT_VALID_INIC_TX_ADM > ? ";
                }else{
                    
                    sql = "SELECT 1 FROM TB_VAL_TX_ADMINISTRATIVA " +
                           "WHERE CD_TX_ADM = ? " +
                           " AND  CD_CATEGORIA = ? " +
                           " AND  DT_VALID_INIC_TX_ADM <= ? " +
                           " AND  (DT_VALID_FIM_TX_ADM >= ? OR DT_VALID_FIM_TX_ADM IS NULL) " +
                           " AND DT_VALID_INIC_TX_ADM <> ? ";
                }

                try {
                    PreparedStatement p = cn.prepareStatement(sql);
                    
                    p.setInt(1, idTaxa);
                    p.setInt(2, idCategoria);
                    if (dtFim ==null) {
                        p.setTimestamp(3, new java.sql.Timestamp(dtInicio.getTime()));
                    }else{
                        p.setTimestamp(3, new java.sql.Timestamp(dtFim.getTime()));
                        p.setTimestamp(4, new java.sql.Timestamp(dtFim.getTime()));
                        p.setTimestamp(5, new java.sql.Timestamp(dtInicio.getTime()));
                    }
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        if (dtFim !=null) {
                            response.getWriter().write("J� existe outra taxa com Data de In�cio posterior a Data de In�cio desa Taxa. Informe Data de Fim anterior a Data de In�cio da outra Taxa!");
                        }else{
                            response.getWriter().write("J� existe outra taxa com Data de In�cio anterior a Data de Fim informada. Informe Data de Fim anterior a Data de In�cio da outra Taxa!");
                        }
                        return;
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

