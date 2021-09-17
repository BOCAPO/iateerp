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

@WebServlet(urlPatterns={"/TaxaCursoAjax/*"})
public class TaxaCursoAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.operacoes.TaxaCursoAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            int idCurso = Integer.parseInt(request.getParameter("idCurso"));
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

                String sql = "SELECT 1 FROM TB_VAL_TX_CURSO " +
                             "WHERE CD_CURSO = ? " +
                             " AND  CD_CATEGORIA = ? " +
                             " AND  DT_VALID_FIM_TX_CURSO >= ? " +
                             " AND  DT_VALID_INIC_TX_CURSO <= ?  ";


                Connection cn = Pool.getInstance().getConnection();
                try {
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, idCurso);
                    p.setInt(2, idCategoria);
                    p.setTimestamp(3, new java.sql.Timestamp(dtInicio.getTime()));
                    p.setTimestamp(4, new java.sql.Timestamp(dtInicio.getTime()));
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("E&Já existe outra Taxa cadastrada com Data de Fim maior que esta Data de Início!");
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
                 
                
                sql = "SELECT 1 FROM TB_VAL_TX_CURSO " +
                       "WHERE CD_CURSO = ? "+
                       " AND CD_CATEGORIA = ? ";
                
                if (dtFim == null) {
                    sql= sql + " AND DT_VALID_INIC_TX_CURSO >= ? ";
                }else{
                    sql= sql +
                       " AND  DT_VALID_INIC_TX_CURSO <= ? " +
                       " AND  (DT_VALID_FIM_TX_CURSO >= ? OR DT_VALID_FIM_TX_CURSO IS NULL) " +
                       " AND  DT_VALID_INIC_TX_CURSO > ?  ";
                }
                
                cn = Pool.getInstance().getConnection();
                try {
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, idCurso);
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
                        response.getWriter().write("E&Já existe outra Taxa cadastrada em colisão com esta Data de Fim! Informe Data de Fim anterior a Data de Início da outra Taxa!");
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
          

                sql = "SELECT 1 FROM TB_VAL_TX_CURSO " +
                       "WHERE CD_CURSO = ? "+
                       " AND CD_CATEGORIA = ? "+
                       " AND  DT_VALID_INIC_TX_CURSO <= ? " +
                       " AND  DT_VALID_FIM_TX_CURSO IS NULL ";
                
                cn = Pool.getInstance().getConnection();
                try {
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, idCurso);
                    p.setInt(2, idCategoria);
                    p.setTimestamp(3, new java.sql.Timestamp(dtInicio.getTime()));
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("A&Existe outra taxa anterior a esta com Data de Fim em aberto. Deseja encerrar a outra taxa com a data de Fim imediatamente anterior a Data de Início desta nova Taxa?");
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
                    sql = "SELECT 1 FROM TB_VAL_TX_CURSO " +
                            "WHERE CD_CURSO = ? " +
                            " AND  CD_CATEGORIA = ? " +
                            " AND  DT_VALID_INIC_TX_CURSO > ? ";
                }else{
                    
                    sql = "SELECT 1 FROM TB_VAL_TX_CURSO " +
                           "WHERE CD_CURSO = ? " +
                           " AND  CD_CATEGORIA = ? " +
                           " AND  DT_VALID_INIC_TX_CURSO <= ? " +
                           " AND  (DT_VALID_FIM_TX_CURSO >= ? OR DT_VALID_FIM_TX_CURSO IS NULL) " +
                           " AND DT_VALID_INIC_TX_CURSO <> ? ";
                }

                try {
                    PreparedStatement p = cn.prepareStatement(sql);
                    
                    p.setInt(1, idCurso);
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
                            response.getWriter().write("Já existe outra taxa com Data de Início posterior a Data de Início desa Taxa. Informe Data de Fim anterior a Data de Início da outra Taxa!");
                        }else{
                            response.getWriter().write("Já existe outra taxa com Data de Início anterior a Data de Fim informada. Informe Data de Fim anterior a Data de Início da outra Taxa!");
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

