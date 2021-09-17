package techsoft.curso; 

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

@WebServlet(urlPatterns={"/JurosCursoAjax/*"})
public class JurosCursoAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.cadastro.EntradaAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            int idCurso = Integer.parseInt(request.getParameter("idCurso"));
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio"));
            Date dtFim = null;
            if (!request.getParameter("dtFim").equals("")){
                dtFim = Datas.parseDataHora(request.getParameter("dtFim"));
            }
            
            //*****************************
            //VALIDACAO DA INCLUSAO
            //*****************************
            if (tipo.equals("1")){

                String sql = "SELECT 1 FROM TB_JUROS_TX_CURSO " +
                             "WHERE CD_CURSO = ? " +
                             " AND  DT_VALID_INIC_JUROS <= ? " +
                             " AND  (DT_VALID_FIM_JUROS >= ? OR DT_VALID_FIM_JUROS IS NULL) ";


                Connection cn = Pool.getInstance().getConnection();
                try {
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, idCurso);
                    p.setTimestamp(2, new java.sql.Timestamp(dtInicio.getTime()));
                    p.setTimestamp(3, new java.sql.Timestamp(dtInicio.getTime()));
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("Já existe outra Taxa cadastrada em colisão com esta Data de Início!");
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
                 
                sql = "SELECT 1 FROM TB_JUROS_TX_CURSO " +
                       "WHERE CD_CURSO = ? ";
                
                if (dtFim != null) {
                    sql= sql +
                       " AND  DT_VALID_INIC_JUROS <= ? " +
                       " AND  (DT_VALID_FIM_JUROS >= ? OR DT_VALID_FIM_JUROS IS NULL) ";
                }else{
                    sql= sql + " and DT_VALID_FIM_JUROS IS NULL ";
                }
                
                cn = Pool.getInstance().getConnection();
                try {
                    PreparedStatement p = cn.prepareStatement(sql);

                    p.setInt(1, idCurso);
                    if (dtFim != null) {
                        p.setTimestamp(2, new java.sql.Timestamp(dtFim.getTime()));
                        p.setTimestamp(3, new java.sql.Timestamp(dtFim.getTime()));
                    }
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("Já existe outra Taxa cadastrada em colisão com esta Data de Fim!");
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
                    sql = "SELECT 1 FROM TB_JUROS_TX_CURSO " +
                            "WHERE CD_CURSO = ? " +
                            " AND  DT_VALID_INIC_JUROS > ? ";
                }else{
                    
                    sql = "SELECT 1 FROM TB_JUROS_TX_CURSO " +
                           "WHERE CD_CURSO = ? " +
                           " AND  DT_VALID_INIC_JUROS <= ? " +
                           " AND  (DT_VALID_FIM_JUROS >= ? OR DT_VALID_FIM_JUROS IS NULL) " +
                           " AND DT_VALID_INIC_JUROS <> ? ";
                }

                try {
                    PreparedStatement p = cn.prepareStatement(sql);
                    
                    p.setInt(1, idCurso);
                    if (dtFim ==null) {
                        p.setTimestamp(2, new java.sql.Timestamp(dtInicio.getTime()));
                    }else{
                        p.setTimestamp(2, new java.sql.Timestamp(dtFim.getTime()));
                        p.setTimestamp(3, new java.sql.Timestamp(dtFim.getTime()));
                        p.setTimestamp(4, new java.sql.Timestamp(dtInicio.getTime()));
                    }
                    
                    ResultSet rs = p.executeQuery();
                    if (rs.next()) {
                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        if (dtFim ==null) {
                            response.getWriter().write("Já existe outra taxa com Data de Início posterior a Data de Início dessa Taxa. Informe Data de Fim anterior a Data de Início da outra Taxa!");
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

