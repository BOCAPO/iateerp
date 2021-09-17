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

@WebServlet("/MovimentoConviteEsportivoAjax/*")
public class MovimentoConviteEsportivoAjax extends HttpServlet {
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.MovimentoConviteEsportivoAjax");    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        String matricula = request.getParameter("matricula");
        String categoria = request.getParameter("categoria");
        String nome = request.getParameter("nome");
        String carteira = request.getParameter("carteira");
        String tipo = request.getParameter("tipo");
        
        json.put("jogador", buscarSocio(json, matricula, categoria, nome, carteira, tipo));
        

        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
    
    private List<JSONObject> buscarSocio(JSONObject json, String matricula, String categoria, String nome, String carteira, String tipo) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        
        String sql;
        sql = "{CALL SP_REC_CONV_ESP_MOV_CAIXA (?, ?, ?, ?, ?)}";
        
        int qtReg = 0;
        Connection conn = Pool.getInstance().getConnection();
        
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
            try {
                stmt.setObject(1, StringUtils.trimToNull(matricula), java.sql.Types.INTEGER);
                stmt.setObject(2, StringUtils.trimToNull(categoria), java.sql.Types.SMALLINT);
                stmt.setObject(3, StringUtils.trimToNull(nome+"%"), java.sql.Types.VARCHAR);
                stmt.setObject(4, StringUtils.trimToNull(carteira), java.sql.Types.INTEGER);
                stmt.setObject(5, StringUtils.trimToNull(tipo), java.sql.Types.VARCHAR);
                ResultSet rs = stmt.executeQuery();
                try {
                    while (rs.next()) {
                        JSONObject socio = new JSONObject();
                        socio.put("socio", rs.getString("NOME_PESSOA"));
                        socio.put("convidado", rs.getString("NOME_CONVIDADO"));
                        socio.put("modalidade", rs.getString("DE_MODALIDADE"));
                        
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        socio.put("validade", sdf.format(rs.getDate("DT_INIC_VALIDADE")) + " a " + sdf.format(rs.getDate("DT_FIM_VALIDADE")));
                        
                        socio.put("convite", rs.getString("NR_CONVITE"));
                        
                        l.add(socio);
                        qtReg++;
                    }
                    json.put("qtReg", qtReg);
                } catch (SQLException ex) {
                    json.put("erro", "Erro ao buscar convite (001)");
                } finally {
                    try { rs.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar convite (002)"); }
                }
            } catch (SQLException ex) {
                json.put("erro", "Erro ao buscar convite (003)");
            } finally {
                try { stmt.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar convite (004)"); }
            }
        } catch (SQLException ex) {
            json.put("erro", "Erro ao buscar convite (005)");
        } finally {
            try { conn.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar convite (006)"); }
        }

        return l;
    }
    


}
