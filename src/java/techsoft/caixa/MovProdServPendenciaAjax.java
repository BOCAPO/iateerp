package techsoft.caixa;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import techsoft.db.Pool;
import techsoft.util.Datas;

@WebServlet("/MovProdServPendenciaAjax/*")
public class MovProdServPendenciaAjax extends HttpServlet {
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.MovProdServPendenciaAjax");    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        int pendencia = Integer.parseInt(request.getParameter("pendencia"));
        
        json.put("relacao", buscarPendencias(json, pendencia));

        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
    
    private List<JSONObject> buscarPendencias(JSONObject json, int pendencia) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        String sql = "";
        
        sql = " SELECT T2.DT_LANCAMENTO, T1.DESCR_PRODUTO_SERVICO, T1.CD_PRODUTO_SERVICO, T2.QT_PRODUTO_SERVICO, T2.QT_PRODUTO_SERVICO * T1.VR_PADRAO AS VR_PADRAO " +
              " FROM TB_PRODUTO_SERVICO T1, TB_DET_RECEBIMENTO_PENDENTE T2 " +
              " WHERE T1.CD_PRODUTO_SERVICO = T2.CD_PRODUTO_SERVICO AND T2.CD_RECEBIMENTO = " + pendencia + 
              " ORDER BY T2.DT_LANCAMENTO "  ;
        
        Connection conn = Pool.getInstance().getConnection();
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
                
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            DecimalFormat val = new DecimalFormat("#,##0.00");
            Date dtAtual = new Date(1);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                
                String dt1 = sdf.format(dtAtual);
                String dt2 = sdf.format(rs.getDate("DT_LANCAMENTO"));
                if (!dt1.equals(dt2)){
                    dtAtual = new Date(rs.getDate("DT_LANCAMENTO").getTime());
                    
                    JSONObject pend = new JSONObject();
                    pend.put("detalhe", sdf.format(rs.getDate("DT_LANCAMENTO")));
                    pend.put("quantidade", "");
                    pend.put("valor", "");
                    pend.put("codigo", "");
                    l.add(pend);
                }
                JSONObject pend = new JSONObject();
                pend.put("detalhe", "&nbsp;&nbsp;&nbsp;" + rs.getString("DESCR_PRODUTO_SERVICO"));
                pend.put("quantidade", rs.getInt("QT_PRODUTO_SERVICO"));
                pend.put("valor", val.format(rs.getFloat("VR_PADRAO")));
                pend.put("codigo", rs.getInt("CD_PRODUTO_SERVICO"));
                l.add(pend);
                
            }

        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return l;
    }
  
}
