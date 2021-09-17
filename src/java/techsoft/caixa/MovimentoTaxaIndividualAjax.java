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

@WebServlet("/MovimentoTaxaIndividualAjax/*")
public class MovimentoTaxaIndividualAjax extends HttpServlet {
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.MovimentoTaxaIndividualAjax");    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        JSONObject json = new JSONObject();
        String matricula = request.getParameter("matricula");
        String categoria = request.getParameter("categoria");
        String nome = request.getParameter("nome");
        String carteira = request.getParameter("carteira");
        String taxa = request.getParameter("taxa");
        String tipo = request.getParameter("tipo");
        String seqTaxaIndividual = request.getParameter("seqTaxaIndividual");
        
        if (tipo.equals("1")) {
            json.put("jogador", buscarSocio(json, matricula, categoria, nome, carteira, taxa));
        }else if (tipo.equals("2")) {
            json = buscarCarne(seqTaxaIndividual);
        }
        

        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
    
    private List<JSONObject> buscarSocio(JSONObject json, String matricula, String categoria, String nome, String carteira, String taxa) {
        List<JSONObject> l = new ArrayList<JSONObject>();
        
        String sql;
        sql = "{CALL SP_REC_TX_IND_MOV_CAIXA (?, ?, ?, ?, ?)}";
        
        int qtReg = 0;
        Connection conn = Pool.getInstance().getConnection();
        
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
            try {
                stmt.setObject(1, StringUtils.trimToNull(matricula), java.sql.Types.INTEGER);
                stmt.setObject(2, StringUtils.trimToNull(categoria), java.sql.Types.SMALLINT);
                stmt.setObject(3, StringUtils.trimToNull(nome+"%"), java.sql.Types.VARCHAR);
                stmt.setObject(4, StringUtils.trimToNull(carteira), java.sql.Types.INTEGER);
                stmt.setObject(5, StringUtils.trimToNull(taxa), java.sql.Types.SMALLINT);
                ResultSet rs = stmt.executeQuery();
                try {
                    while (rs.next()) {
                        JSONObject socio = new JSONObject();
                        socio.put("nome", rs.getString("NOME_PESSOA"));
                        socio.put("taxa", rs.getString("DESCR_TX_ADMINISTRATIVA"));
                        
                        DecimalFormat val = new DecimalFormat("#,##0.00");
                        socio.put("valor", val.format(rs.getFloat("VR_TAXA")));
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        socio.put("dtGeracao", sdf.format(rs.getDate("DT_GERACAO_TAXA")));
                        
                        socio.put("cobranca", rs.getString("MM_AA_COBRANCA"));

                        socio.put("tituloChave", rs.getString("TITULO_CHAVE"));
                        socio.put("seqTaxa", rs.getString("NU_SEQ_TAXA_INDIVIDUAL"));
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
    
    private JSONObject buscarCarne(String seqTaxaIndividual) {
        String sql = "";
        JSONObject json = new JSONObject();
        
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        sql = "SELECT DT_VCTO_PARC_ADM, T2.VR_TAXA, T1.SEQ_CARNE " +
              "FROM TB_PARCELA_ADMINISTRATIVA T1, TB_VAL_TAXA_INDIVIDUAL T2 " +
              "WHERE T1.NU_SEQ_TAXA_INDIVIDUAL = T2.NU_SEQ_TAXA_INDIVIDUAL AND " +
              "      T1.CD_SIT_PARC_ADM = 'NP' AND " +
              "      T2.NU_SEQ_TAXA_INDIVIDUAL = " + seqTaxaIndividual;
        
        Connection conn = Pool.getInstance().getConnection();
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
                
            BigDecimal vrDevido = BigDecimal.ZERO;
            BigDecimal vrEncargos = BigDecimal.ZERO;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            DecimalFormat val = new DecimalFormat("#,##0.00");
            DecimalFormat val2 = new DecimalFormat("0.00");
            Date dtAtual = new Date();

            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                
                json.put("temCarne", "S");
                json.put("dtVencimento", sdf.format(rs.getDate("DT_VCTO_PARC_ADM")));
                
                vrDevido = vrDevido.add(rs.getBigDecimal("VR_TAXA").setScale(2, BigDecimal.ROUND_DOWN));
                json.put("vrDevido", val.format(vrDevido));

                sql = " {CALL SP_CALCULA_ENCARGOS_INPC (?, ?, ?, null, null)}";
                stmt = conn.prepareCall(sql);    

                stmt.setDate(1, new java.sql.Date(rs.getDate("DT_VCTO_PARC_ADM").getTime()));
                stmt.setDate(2, new java.sql.Date(dtAtual.getTime()));
                stmt.setFloat(3, rs.getFloat("VR_TAXA"));

                ResultSet rs2 = stmt.executeQuery();
                if (rs2.next()) {
                    vrEncargos = vrEncargos.add(rs2.getBigDecimal(1).setScale(2, BigDecimal.ROUND_DOWN));
                }

                json.put("vrEncargos", val.format(vrEncargos));
                
            }else{
                json.put("temCarne", "N");
                json.put("dtVencimento", sdf.format(dtAtual));
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

        return json;
    }  

}
