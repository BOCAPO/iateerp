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

@WebServlet(urlPatterns={"/MaterialAjax/*"})
public class MaterialAjax extends HttpServlet {
 
        private static final Logger log = Logger.getLogger("techsoft.operacoes.MaterialAjax");    

        @Override
        public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
            String tipo = request.getParameter("tipo");
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int idMaterial = Integer.parseInt(request.getParameter("idMaterial"));
            
            if (tipo.equals("1")){
                String retorno = "";
                
                Connection cn = null;
                ResultSet rs = null;
                
                String sql = "SELECT IC_EMP_TODOS_MAT FROM TB_PESSOA " +
                             "WHERE CD_MATRICULA = " + matricula +
                             " AND CD_CATEGORIA = " + idCategoria +
                             " AND SEQ_DEPENDENTE = " + seqDependente;
                
                try {
                    cn = Pool.getInstance().getConnection();
                    rs = cn.createStatement().executeQuery(sql);
                    
                    if(!rs.next()){                    
                        retorno = "Dependente não autorizado para empréstimo desse material!";
                    }else{
                        if(rs.getString("IC_EMP_TODOS_MAT") == null || rs.getString("IC_EMP_TODOS_MAT").charAt(0) == 'N'){
                            sql = "SELECT 1 FROM TB_MATERIAL_AUTORIZADO_DEPENDENTE " +
                                         "WHERE " +
                                             "CD_MATRICULA = " + matricula + " AND " +
                                             "CD_CATEGORIA = " + idCategoria + " AND " +
                                             "SEQ_DEPENDENTE = " + seqDependente + " AND " +
                                             "CD_MATERIAL = " + idMaterial;
                            
                            cn = Pool.getInstance().getConnection();
                            rs = cn.createStatement().executeQuery(sql);
                            
                            if(!rs.next()){                    
                                retorno = "Dependente não autorizado para empréstimo desse material!";
                            }
                        }
                    }
                    
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(retorno);
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

