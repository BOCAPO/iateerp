package techsoft.clube;

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

@WebServlet("/CarroSocioAjax/*")
public class CarroSocioAjax extends HttpServlet {
    
    private static final Logger log = Logger.getLogger("techsoft.clube.CarroSocioAjax");  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        String placa = request.getParameter("placa");
        
        json.put("carroSocio", buscarCarroSocio(json, placa));

        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
    
    private List<JSONObject> buscarCarroSocio(JSONObject json, String placa) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        
        String sql = " SELECT T1.DE_MARCA, T2.DE_MODELO, T3.DE_COR, T1.CD_MARCA, T2.CD_MODELO, T3.CD_COR, T4.DE_PLACA, T4.CD_CARRO, 'SOCIO' TIPO, "
                + " (SELECT COUNT(*) FROM IATE_FOTO..TB_FOTO_CARRO_SOCIO T0 WHERE T0.CD_CARRO = T4.CD_CARRO) QT_FOTO, "
                + " T5.CD_MATRICULA, T5.CD_CATEGORIA, T5.SEQ_DEPENDENTE, T5.NOME_PESSOA "
                + " FROM TB_MARCA_CARRO T1, TB_MODELO_CARRO T2, TB_COR T3, TB_CARRO_PESSOA T4, TB_PESSOA T5 "
                + " WHERE T1.CD_MARCA = T2.CD_MARCA AND T2.CD_MODELO = T4.CD_MODELO AND T3.CD_COR = T4.CD_COR "
                + " AND   T4.CD_MATRICULA = T5.CD_MATRICULA AND T4.CD_CATEGORIA = T5.CD_CATEGORIA AND T4.SEQ_DEPENDENTE = T5.SEQ_DEPENDENTE "
                + " AND DE_PLACA = ? ";
        sql = sql + " UNION ALL "
                + "SELECT T1.DE_MARCA, T2.DE_MODELO, T3.DE_COR, T1.CD_MARCA, T2.CD_MODELO, T3.CD_COR, T4.DE_PLACA, T4.CD_CARRO, 'FUNCIONARIO' TIPO, "
                + " (SELECT COUNT(*) FROM IATE_FOTO..TB_FOTO_CARRO_FUNCIONARIO T0 WHERE T0.CD_CARRO = T4.CD_CARRO) QT_FOTO, "
                + " T5.CD_FUNCIONARIO, NULL, NULL, T5.NOME_FUNCIONARIO "
                + " FROM TB_MARCA_CARRO T1, TB_MODELO_CARRO T2, TB_COR T3, TB_CARRO_FUNCIONARIO T4, TB_FUNCIONARIO T5 "
                + " WHERE T1.CD_MARCA = T2.CD_MARCA AND T2.CD_MODELO = T4.CD_MODELO AND T3.CD_COR = T4.CD_COR "
                + " AND   T4.CD_FUNCIONARIO = T5.CD_FUNCIONARIO "
                + " AND DE_PLACA = ? ";
        
        int qtReg = 0;
        Connection conn = Pool.getInstance().getConnection();
        DecimalFormat dfMat = new DecimalFormat( "0000" );
        DecimalFormat dfCatSeq = new DecimalFormat( "00" );
        
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
            try {
                stmt.setString(1, placa);
                stmt.setString(2, placa);
                
                ResultSet rs = stmt.executeQuery();
                try {
                    while (rs.next()) {
                        JSONObject socio = new JSONObject();
                        socio.put("matricula", rs.getInt("CD_MATRICULA"));
                        socio.put("categoria", rs.getInt("CD_CATEGORIA"));
                        socio.put("dependente", rs.getInt("SEQ_DEPENDENTE"));
                        socio.put("nome", rs.getString("NOME_PESSOA"));
                        socio.put("tipo", rs.getString("TIPO"));
                        
                        if (rs.getString("TIPO").equals("SOCIO")){
                            socio.put("titulo", dfCatSeq.format(rs.getInt("CD_CATEGORIA")) + "/" + dfMat.format(rs.getInt("CD_MATRICULA")) + "-" + dfCatSeq.format(rs.getInt("SEQ_DEPENDENTE")));
                            socio.put("titulo_chave", dfCatSeq.format(rs.getInt("CD_CATEGORIA")) + dfMat.format(rs.getInt("CD_MATRICULA")) + dfCatSeq.format(rs.getInt("SEQ_DEPENDENTE")));
                        }else{
                            socio.put("titulo", dfMat.format(rs.getInt("CD_MATRICULA")));
                            socio.put("titulo_chave", dfMat.format(rs.getInt("CD_MATRICULA")));
                        }
                        socio.put("marca", rs.getString("DE_MARCA"));
                        socio.put("modelo", rs.getString("DE_MODELO"));
                        socio.put("placa", rs.getString("DE_PLACA"));
                        socio.put("cod", rs.getInt("CD_CARRO"));
                        socio.put("qtDocumento", rs.getString("QT_FOTO"));
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
