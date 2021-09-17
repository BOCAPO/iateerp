package techsoft.operacoes;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

@WebServlet("/ReservaLugarEventoAjax/*")
public class ReservaLugarEventoAjax extends HttpServlet {
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.ReservaLugarEventoAjax");    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        String matricula = request.getParameter("matricula");
        String categoria = request.getParameter("categoria");
        String nome = request.getParameter("nome");
        if (StringUtils.isBlank(matricula) && StringUtils.isBlank(categoria) && StringUtils.isBlank(nome))
            json.put("erro", "Informe pelo menos um campo de pesquisa");
        else
            json.put("jogador", buscarSocio(json, matricula, categoria, nome));

        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
    
    private List<JSONObject> buscarSocio(JSONObject json, String matricula, String categoria, String nome) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        String sql = "{CALL SP_CONSULTA_SOCIO_EVENTO (?, ?, ?)}";
        Connection conn = Pool.getInstance().getConnection();
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
            try {
                stmt.setObject(1, StringUtils.trimToNull(matricula), java.sql.Types.INTEGER);
                stmt.setObject(2, StringUtils.trimToNull(categoria), java.sql.Types.SMALLINT);
                stmt.setObject(3, StringUtils.trimToNull(nome), java.sql.Types.VARCHAR);
                ResultSet rs = stmt.executeQuery();
                try {  
                    while (rs.next()) {
                        JSONObject socio = new JSONObject();
                        socio.put("matricula", rs.getInt("CD_MATRICULA"));
                        socio.put("categoria", rs.getInt("CD_CATEGORIA"));
                        socio.put("tpCategoria", rs.getString("TP_CATEGORIA"));
                        socio.put("dependente", rs.getInt("SEQ_DEPENDENTE"));
                        socio.put("descricao", rs.getString("CATEGORIA"));
                        socio.put("titulo", rs.getString("TITULO"));
                        socio.put("nome", rs.getString("NOME"));
                        socio.put("nome2", rs.getString("NOME2"));
                        l.add(socio);
                    }
                } catch (SQLException ex) {
                    json.put("erro", "Erro ao buscar sócio (001)");
                } finally {
                    try { rs.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar sócio (002)"); }
                }
            } catch (SQLException ex) {
                json.put("erro", "Erro ao buscar sócio (003)");
            } finally {
                try { stmt.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar sócio (004)"); }
            }
        } catch (SQLException ex) {
            json.put("erro", "Erro ao buscar sócio (005)");
        } finally {
            try { conn.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar sócio (006)"); }
        }

        return l;
    }
    
  

}
