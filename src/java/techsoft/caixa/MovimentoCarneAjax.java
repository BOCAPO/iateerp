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

@WebServlet("/MovimentoCarneAjax/*")
public class MovimentoCarneAjax extends HttpServlet {
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.MovimentoCarneAjax");    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        String matricula = request.getParameter("matricula");
        String categoria = request.getParameter("categoria");
        String tipo = request.getParameter("tipo");
        
        if (tipo.equals("1")) {
            String carteira = request.getParameter("carteira");
            String nome = request.getParameter("nome");
            
            json.put("jogador", buscarSocio(json, matricula, categoria, carteira, nome));
        }else if (tipo.equals("2")) {
            String operador = request.getParameter("operador");
            Date dtVenc = Datas.parse(request.getParameter("dtVenc"));
            
            json.put("relacao", buscarCarnes(json, matricula, categoria, operador, dtVenc));
        }
        

        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
    
    private List<JSONObject> buscarSocio(JSONObject json, String matricula, String categoria, String carteira, String nome) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        String sql = "";
        
        if (!"".equals(categoria)&& !"".equals(matricula)){
            sql = "{CALL SP_RECUPERA_CARNE_CAT_MAT (?, ?)}";
        }else if (!"".equals(carteira)){
            sql = "{CALL SP_RECUPERA_CARNE_NU_CARTEIRA (?)}";
        }else if (!"".equals(nome)){
            sql = "{CALL SP_RECUPERA_CARNE_NOME (?)}";
        }
        
        Connection conn = Pool.getInstance().getConnection();
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
            try {
                if (!"".equals(categoria)&& !"".equals(matricula)){
                    stmt.setObject(1, StringUtils.trimToNull(categoria), java.sql.Types.SMALLINT);
                    stmt.setObject(2, StringUtils.trimToNull(matricula), java.sql.Types.INTEGER);
                }else if (!"".equals(carteira)){
                    stmt.setObject(1, StringUtils.trimToNull(carteira), java.sql.Types.INTEGER);
                }else if (!"".equals(nome)){
                    stmt.setObject(1, StringUtils.trimToNull(nome), java.sql.Types.VARCHAR);
                }
                
                ResultSet rs = stmt.executeQuery();
                try {
                    while (rs.next()) {
                        JSONObject socio = new JSONObject();
                        socio.put("nome", rs.getString("NOME_PESSOA"));
                        socio.put("carteira", rs.getInt("NR_CARTEIRA_PESSOA"));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        socio.put("dtVencimento", sdf.format(rs.getDate("DT_VENC_CARNE")));
                        DecimalFormat val = new DecimalFormat("#,##0.00");
                        socio.put("valor", val.format(rs.getFloat("VAL_CARNE")));
                        socio.put("sequencial", rs.getInt("SEQUENCIAL"));
                        
                        DecimalFormat mat = new DecimalFormat("0000");
                        DecimalFormat cat = new DecimalFormat("00");
                        socio.put("titulo", cat.format(rs.getInt("CD_CATEGORIA")) + "/" + mat.format(rs.getInt("CD_MATRICULA")));
                        socio.put("permite_pagamento", rs.getString("IC_PERMITE_PAGAMENTO"));
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
    
    private List<JSONObject> buscarCarnes(JSONObject json, String matricula, String categoria, String operador, Date dtVenc) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        String sql = "";
        
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        sql = "SELECT DT_VENC_CARNE, VAL_CARNE, SEQ_CARNE, ISNULL(IC_PERMITE_PAGAMENTO, 'N') IC_PERMITE_PAGAMENTO " +
              "FROM TB_CARNE  " +
              "WHERE " +
              "     CD_MATRICULA = " + matricula + " AND " +
              "     CD_CATEGORIA = " + categoria + " AND " +
              "     CD_SIT_CARNE = 'NP' AND " +
              "     DT_VENC_CARNE " + operador + " '" + fmt.format(dtVenc) + "'" +
              "ORDER BY DT_VENC_CARNE DESC     ";
        
        Connection conn = Pool.getInstance().getConnection();
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
                
            BigDecimal vrDevido = BigDecimal.ZERO;
            BigDecimal vrEncargos = BigDecimal.ZERO;
            String temProibido="N";
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            DecimalFormat val = new DecimalFormat("#,##0.00");
            DecimalFormat val2 = new DecimalFormat("0.00");
            Date dtAtual = new Date();

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JSONObject carne = new JSONObject();
                
                if (rs.getString("IC_PERMITE_PAGAMENTO").equals("S")){
                    temProibido="S";    
                }else{
                    carne.put("detalhe", "Vencimento: " + sdf.format(rs.getDate("DT_VENC_CARNE")));
                    carne.put("valor", "");
                    l.add(carne);

                    vrDevido = vrDevido.add(rs.getBigDecimal("VAL_CARNE").setScale(2, BigDecimal.ROUND_DOWN));

                    sql = " {CALL SP_CALCULA_ENCARGOS_INPC (?, ?, ?, null, ?)}";
                    stmt = conn.prepareCall(sql);    

                    stmt.setDate(1, new java.sql.Date(rs.getDate("DT_VENC_CARNE").getTime()));
                    stmt.setDate(2, new java.sql.Date(dtAtual.getTime()));
                    stmt.setFloat(3, rs.getFloat("VAL_CARNE"));
                    stmt.setInt(4, rs.getInt("SEQ_CARNE"));

                    ResultSet rs2 = stmt.executeQuery();
                    if (rs2.next()) {
                        vrEncargos = vrEncargos.add(rs2.getBigDecimal(1).setScale(2, BigDecimal.ROUND_DOWN));
                    }

                    sql = " {CALL SP_RECUPERA_HISTORICO (?)} ";
                    stmt = conn.prepareCall(sql);    

                    stmt.setInt(1, rs.getInt("SEQ_CARNE"));

                    ResultSet rs3 = stmt.executeQuery();
                    while (rs3.next()) {
                        JSONObject historico = new JSONObject();
                        historico.put("detalhe", "&nbsp;&nbsp;&nbsp;&nbsp;" + rs3.getString(1));
                        historico.put("valor", val.format(rs3.getFloat(2)));
                        l.add(historico);
                    }
                }
            }

            json.put("temProibido", temProibido);
            json.put("vrDevido", val.format(vrDevido));
            json.put("vrEncargos", val.format(vrEncargos));
                    
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
