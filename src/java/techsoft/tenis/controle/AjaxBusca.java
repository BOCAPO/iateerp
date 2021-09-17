package techsoft.tenis.controle;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import techsoft.db.Pool;

@WebServlet("/ajax/busca")
public class AjaxBusca extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipo = request.getParameter("tipo");
        
        JSONObject json = new JSONObject();
        if (tipo.equals("socio")) {
            String matricula = request.getParameter("matricula");
            String categoria = request.getParameter("categoria");
            String nome = request.getParameter("nome");
            if (StringUtils.isBlank(matricula) && StringUtils.isBlank(categoria) && StringUtils.isBlank(nome))
                json.put("erro", "Informe pelo menos um campo de pesquisa");
            else
                json.put("jogador", buscarSocio(json, matricula, categoria, nome));
        }
        if (tipo.equals("convidado")) {
            String convite = request.getParameter("convite");
            String nome = request.getParameter("nome");
            if (StringUtils.isBlank(convite) && StringUtils.isBlank(nome))
                json.put("erro", "Informe pelo menos um campo de pesquisa");
            else
                json.put("jogador", buscarConvidado(json, convite, nome));
        }
        
        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    private List<JSONObject> buscarSocio(JSONObject json, String matricula, String categoria, String nome) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        String sql = "{CALL SP_CONSULTA_SOCIO_TENIS (?, ?, ?)}";
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
                        socio.put("dependente", rs.getInt("SEQ_DEPENDENTE"));
                        socio.put("titulo", rs.getString("TITULO"));
                        socio.put("descricao", rs.getString("CATEGORIA"));
                        socio.put("nome", rs.getString("NOME"));
                        l.add(socio);
                    }
                } catch (SQLException ex) {
                    json.put("erro", "Erro ao buscar s�cio (001)");
                } finally {
                    try { rs.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar s�cio (002)"); }
                }
            } catch (SQLException ex) {
                json.put("erro", "Erro ao buscar s�cio (003)");
            } finally {
                try { stmt.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar s�cio (004)"); }
            }
        } catch (SQLException ex) {
            json.put("erro", "Erro ao buscar s�cio (005)");
        } finally {
            try { conn.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar s�cio (006)"); }
        }

        return l;
    }
    
    private List<JSONObject> buscarConvidado(JSONObject json, String convite, String nome) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        String sql = "{CALL SP_CONSULTA_CONVITE_TENIS (?, ?)}";
        Connection conn = Pool.getInstance().getConnection();
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
            try {
                stmt.setObject(1, StringUtils.trimToNull(convite), java.sql.Types.INTEGER);
                stmt.setObject(2, StringUtils.trimToNull(nome), java.sql.Types.VARCHAR);
                ResultSet rs = stmt.executeQuery();
                try {
                    while (rs.next()) {
                        JSONObject convidado = new JSONObject();
                        convidado.put("convite", rs.getInt("NR_CONVITE"));
                        convidado.put("nome", rs.getString("NOME_CONVIDADO"));
                        l.add(convidado);
                    }
                } catch (SQLException ex) {
                    json.put("erro", "Erro ao buscar convidado (001)");
                } finally {
                    try { rs.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar convidado (002)"); }
                }
            } catch (SQLException ex) {
                json.put("erro", "Erro ao buscar convidado (003)");
            } finally {
                try { stmt.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar convidado (004)"); }
            }
        } catch (SQLException ex) {
            json.put("erro", "Erro ao buscar convidado (005)");
        } finally {
            try { conn.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar convidado (006)"); }
        }

        return l;
    }

}
