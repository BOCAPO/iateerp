package techsoft.caixa;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
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

@WebServlet("/TaxaIndividualFuncionarioAjax/*")
public class TaxaIndividualFuncionarioAjax extends HttpServlet {
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.TaxaIndividualFuncionarioAjax");    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        String nome = request.getParameter("nome");
        String matricula = request.getParameter("matricula");
        
        json.put("funcionario", buscarFuncionario(json, nome, matricula));

        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
    
    private List<JSONObject> buscarFuncionario(JSONObject json, String nome, String matricula) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        
        String sql = "SELECT " +
                    "	T1.CD_FUNCIONARIO," +
                    "	T1.NOME_FUNCIONARIO," +
                    "	T2.DESCR_CARGO," +
                    "	T3.DESCR_SETOR " +
                    "FROM " +
                    "	TB_FUNCIONARIO T1," +
                    "	TB_CARGO_FUNCAO T2," +
                    "	TB_SETOR T3 " +
                    "WHERE " +
                    "	T1.CD_CARGO = T2.CD_CARGO AND" +
                    "	T1.CD_SETOR = T3.CD_SETOR AND";
                
        try{
            if (!matricula.equals("")){ 
                sql = sql + "	T1.NUM_MATRIC_FUNCIONARIO = "+matricula+" ";
            }else{
                sql = sql + "	T1.NOME_FUNCIONARIO LIKE '"+nome+"%' ";
            }
            
            sql = sql + "ORDER BY " +
                        "	T1.NOME_FUNCIONARIO";

            int qtReg = 0;
            Connection conn = Pool.getInstance().getConnection();

            try {
                CallableStatement stmt = conn.prepareCall(sql);    
                try {
                    ResultSet rs = stmt.executeQuery();
                    try {
                        while (rs.next()) {
                            JSONObject funcionario = new JSONObject();
                            funcionario.put("nome", rs.getString("NOME_FUNCIONARIO"));
                            funcionario.put("cargo", rs.getString("DESCR_CARGO"));
                            funcionario.put("setor", rs.getString("DESCR_SETOR"));
                            funcionario.put("codigo", rs.getInt("CD_FUNCIONARIO"));
                            l.add(funcionario);
                            qtReg++;
                        }
                        json.put("qtReg", qtReg);
                    } catch (Exception ex) {
                        json.put("erro", "Erro ao buscar funcionario (001)" + ex.getMessage());
                    } finally {
                        try { rs.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar funcionario (002)"  + ex.getMessage()); }
                    }
                } catch (Exception ex) {
                    json.put("erro", "Erro ao buscar funcionario (003)"  + ex.getMessage());
                } finally {
                    try { stmt.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar funcionario (004)" + ex.getMessage()); }
                }
            } catch (Exception ex) {
                json.put("erro", "Erro ao buscar funcionario (005)" + ex.getMessage());
            } finally {
                try { conn.close(); } catch (SQLException ex) { json.put("erro", "Erro ao buscar funcionario (006)" + ex.getMessage()); }
            }            
        } catch (Exception ex) {
            String err = "Erro na montagem da consulta." + ex.getMessage();
            log.warning(err);
            json.put("erro", "Erro ao buscar funcionario (006)" + ex.getMessage());
        }
        
        return l;
    }
}
