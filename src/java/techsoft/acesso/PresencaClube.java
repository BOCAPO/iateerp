package techsoft.acesso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.util.Date;
import techsoft.tabelas.InserirException;

public class PresencaClube {
    private Date data;
    private String tipo;
    private String status;
    private String descricao;
    private String local;

    private static final Logger log = Logger.getLogger("techsoft.acesso.PresencaClube");

    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }

    public static List<PresencaClube> listar(int doc , int local, Date dtInicio, Date dtFim) {
        
        String sql = "SELECT " +
                            "T1.SEQ_ACESSO			, " +
                            "T1.DT_HISTORICO_ACESSO		, " +
                            "CASE WHEN T1.ENTRADA_OU_SAIDA = 'E' THEN 'ENTRADA' ELSE 'SAÍDA' END ENTRADA_OU_SAIDA, " +
                            "CASE WHEN T2.CD_STATUS_ACESSO_PERMITIDO	= 'PA' THEN 'PROIBE ACESSO' WHEN T2.CD_STATUS_ACESSO_PERMITIDO	= 'PE' THEN 'PERMITE ACESSO' WHEN T2.CD_STATUS_ACESSO_PERMITIDO	= 'AL' THEN 'ALERTA' ELSE 'PROIBE ACESSO PISCINA' END CD_STATUS_ACESSO_PERMITIDO, " +
                            "T2.DESCR_HISTORICO_ACESSO	, " +
                            "T3.DESCR_LOCAL_ACESSO " +
                    "FROM  " +
                            "TB_ACESSO		T1, " +
                            "TB_HISTORICO_ACESSO	T2, " +
                            "TB_LOCAL_ACESSO		T3 " +
                    "WHERE " +
                            "T1.SEQ_ACESSO 		= T2.SEQ_ACESSO			AND " +
                            "T1.CD_LOCAL_ACESSO	= T3.CD_LOCAL_ACESSO		AND " +
                            "T1.NR_DOC_HIST_ACESSO	= ?		AND " +
                            "T1.DT_HISTORICO_ACESSO	>= ?			AND " +
                            "T1.DT_HISTORICO_ACESSO	<= DATEADD(D, 1, ?) AND "   +
                            "T1.CD_STATUS_ACESSO_PERMITIDO <> 'PE'";
                
        if (local > 0){
            sql = sql + " AND T3.CD_LOCAL_ACESSO = " + local;
        }
        
        sql = sql + " UNION ALL ";
                
        sql = sql + "SELECT " +
                            "T1.SEQ_ACESSO			, " +
                            "T1.DT_HISTORICO_ACESSO		, " +
                            "CASE WHEN T1.ENTRADA_OU_SAIDA = 'E' THEN 'ENTRADA' ELSE 'SAÍDA' END ENTRADA_OU_SAIDA, " +
                            "'PERMITE ACESSO' CD_STATUS_ACESSO_PERMITIDO, " +
                            "NULL DESCR_HISTORICO_ACESSO, " +
                            "T3.DESCR_LOCAL_ACESSO " +
                    "FROM  " +
                            "TB_ACESSO		T1, " +
                            "TB_LOCAL_ACESSO		T3 " +
                    "WHERE " +
                            "T1.CD_LOCAL_ACESSO	= T3.CD_LOCAL_ACESSO		AND " +
                            "T1.NR_DOC_HIST_ACESSO	= ?		AND " +
                            "T1.DT_HISTORICO_ACESSO	>= ?			AND " +
                            "T1.DT_HISTORICO_ACESSO	<= DATEADD(D, 1, ?) AND " +
                            "T1.CD_STATUS_ACESSO_PERMITIDO = 'PE' ";

        if (local > 0){
            sql = sql + " AND T3.CD_LOCAL_ACESSO = " + local;
        }
        
        sql = sql + " ORDER BY 2 ";

        Connection cn = null;

        ArrayList<PresencaClube> l = new ArrayList<PresencaClube>();
        
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall(sql);

            cal.setInt(1, Integer.valueOf(doc));
            cal.setDate(2, new java.sql.Date(dtInicio.getTime()));
            cal.setDate(3, new java.sql.Date(dtFim.getTime()));
            
            cal.setInt(4, Integer.valueOf(doc));
            cal.setDate(5, new java.sql.Date(dtInicio.getTime()));
            cal.setDate(6, new java.sql.Date(dtFim.getTime()));
            
            ResultSet rs = cal.executeQuery();
            String teste="";
            Date teste2 = null;
            while (rs.next()) {
                PresencaClube i = new PresencaClube();
                
                i.setData(rs.getTimestamp("DT_HISTORICO_ACESSO"));
                i.setTipo(rs.getString("ENTRADA_OU_SAIDA"));
                i.setStatus(rs.getString("CD_STATUS_ACESSO_PERMITIDO"));
                i.setDescricao(rs.getString("DESCR_HISTORICO_ACESSO"));
                i.setLocal(rs.getString("DESCR_LOCAL_ACESSO"));
                
                l.add(i);
            }
            cn.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        return l;
    }
    
}
