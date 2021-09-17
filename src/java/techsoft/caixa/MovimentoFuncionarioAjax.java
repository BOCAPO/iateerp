package techsoft.caixa;

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

@WebServlet("/MovimentoFuncionarioAjax/*")
public class MovimentoFuncionarioAjax extends HttpServlet {
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.MovimentoFuncionarioAjax");    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        String nome = request.getParameter("nome");
        String cdFuncionario = request.getParameter("cdFuncionario");
        
        json.put("jogador", buscarFuncionario(json, nome, cdFuncionario));

        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
    
    private List<JSONObject> buscarFuncionario(JSONObject json, String nome, String cdFuncionario) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        String sql = "SELECT " +
                    "	T1.CD_FUNCIONARIO,  " +
                    "	T1.NOME_FUNCIONARIO, " +
                    "	T1.NUM_MATRIC_FUNCIONARIO, " +
                    "	T2.DESCR_SETOR, " +
                    "	T3.DESCR_CARGO " +
                    "FROM " +
                    "	TB_FUNCIONARIO T1, " +
                    "	TB_SETOR T2," +
                    "	TB_CARGO_FUNCAO T3 " +
                    "WHERE " +
                    "	T1.CD_SETOR = T2.CD_SETOR AND " +
                    "	T1.CD_CARGO = T3.CD_CARGO AND ";
        if (cdFuncionario.equals("")){
            sql = sql + "   T1.NOME_FUNCIONARIO LIKE '" + nome + "%' ";
        }else{
            sql = sql + "   T1.CD_FUNCIONARIO = " + cdFuncionario + " ";
        }
                    
        sql = sql +
                    "ORDER BY " +
                    "	NOME_FUNCIONARIO";
        int qtReg = 0;
        Connection conn = Pool.getInstance().getConnection();
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
            try {
                ResultSet rs = stmt.executeQuery();
                try {
                    while (rs.next()) {
                        JSONObject funcionario = new JSONObject();
                        funcionario.put("idFuncionario", rs.getInt("CD_FUNCIONARIO"));
                        funcionario.put("nome", rs.getString("NOME_FUNCIONARIO"));
                        funcionario.put("matricula", rs.getInt("NUM_MATRIC_FUNCIONARIO"));
                        funcionario.put("setor", rs.getString("DESCR_SETOR"));
                        funcionario.put("cargo", rs.getString("DESCR_CARGO"));
                        l.add(funcionario);
                        qtReg++;
                    }
                    json.put("qtReg", qtReg);
                } catch (SQLException ex) {
                    json.put("erro", "Erro ao buscar funcionário (001)" + ex.getMessage());
                } finally {
                    try { rs.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar funcionário (002)"); }
                }
            } catch (SQLException ex) {
                json.put("erro", "Erro ao buscar funcionário (003)" + ex.getMessage() );
            } finally {
                try { stmt.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar funcionário (004)"); }
            }
        } catch (SQLException ex) {
            json.put("erro", "Erro ao buscar funcionário (005)" + ex.getMessage());
        } finally {
            try { conn.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar funcionário (006)"); }
        }

        return l;
    }
    
  

}
