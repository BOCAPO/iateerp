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

@WebServlet("/TaxaIndividualCarroAjax/*")
public class TaxaIndividualCarroAjax extends HttpServlet {
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.TaxaIndividualCarroAjax");    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        String matricula = request.getParameter("matricula");
        String categoria = request.getParameter("categoria");
        String tipo = request.getParameter("tipo");
        
        json.put("carro", buscarCarro(json, matricula, categoria, tipo));

        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
    
    private List<JSONObject> buscarCarro(JSONObject json, String matricula, String categoria, String tipo) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        
        String sql = "";
        if (tipo.equals("S")){
            sql = "SELECT T1.DE_MARCA, T2.DE_MODELO, T3.DE_COR, T1.CD_MARCA, T2.CD_MODELO, T3.CD_COR, T4.DE_PLACA, T4.CD_CARRO, " + 
                  "(SELECT COUNT(*) FROM IATE_FOTO..TB_FOTO_CARRO_SOCIO T0 WHERE T0.CD_CARRO = T4.CD_CARRO) QT_FOTO " +
                  "FROM TB_MARCA_CARRO T1, TB_MODELO_CARRO T2, TB_COR T3, TB_CARRO_PESSOA T4 " + 
                  "WHERE T1.CD_MARCA = T2.CD_MARCA AND T2.CD_MODELO = T4.CD_MODELO AND T3.CD_COR = T4.CD_COR " +
                  "AND CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = 0 ";   
        }else{
            sql = "SELECT T1.DE_MARCA, T2.DE_MODELO, T3.DE_COR, T1.CD_MARCA, T2.CD_MODELO, T3.CD_COR, T4.DE_PLACA, T4.CD_CARRO, " + 
                  "(SELECT COUNT(*) FROM IATE_FOTO..TB_FOTO_CARRO_FUNCIONARIO T0 WHERE T0.CD_CARRO = T4.CD_CARRO) QT_FOTO " +
                  "FROM TB_MARCA_CARRO T1, TB_MODELO_CARRO T2, TB_COR T3, TB_CARRO_FUNCIONARIO T4 " + 
                  "WHERE T1.CD_MARCA = T2.CD_MARCA AND T2.CD_MODELO = T4.CD_MODELO AND T3.CD_COR = T4.CD_COR " +
                  "AND CD_FUNCIONARIO = ? ";   
        }
        
        int qtReg = 0;
        Connection conn = Pool.getInstance().getConnection();
        
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
            try {
                stmt.setObject(1, StringUtils.trimToNull(matricula), java.sql.Types.SMALLINT);
                if (tipo.equals("S")){
                    stmt.setObject(2, StringUtils.trimToNull(categoria), java.sql.Types.INTEGER);
                }
                
                ResultSet rs = stmt.executeQuery();
                try {
                    while (rs.next()) {
                        JSONObject socio = new JSONObject();
                        socio.put("qtDocumento", rs.getString("QT_FOTO"));
                        socio.put("marca", rs.getString("DE_MARCA"));
                        socio.put("modelo", rs.getString("DE_MODELO"));
                        socio.put("placa", rs.getString("DE_PLACA"));
                        socio.put("cod", rs.getInt("CD_CARRO"));
                        l.add(socio);
                        qtReg++;
                    }
                    json.put("qtReg", qtReg);
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
