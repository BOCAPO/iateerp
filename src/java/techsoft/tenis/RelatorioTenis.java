package techsoft.tenis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import org.joda.time.DateTime;
import techsoft.db.SQLType;

public class RelatorioTenis {
    
    private static final Logger log = Logger.getLogger("techsoft.tenis.RelatorioTeni");

    private String quadra;
    private String jogador;
    private DateTime dtInicio;
    private DateTime dtFim;
    private String tipo;
    private String origem;
    private int qtJogoSimples;
    private int qtJogoDuplas;
    private int qtJogoTotal;
    private int minSimples;
    private int minDuplas;
    private int minTotal;
    private int minBloqueio;
    

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public DateTime getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(DateTime dtInicio) {
        this.dtInicio = dtInicio;
    }

    public DateTime getDtFim() {
        return dtFim;
    }

    public void setDtFim(DateTime dtFim) {
        this.dtFim = dtFim;
    }

    public String getQuadra() {
        return quadra;
    }

    public void setQuadra(String quadra) {
        this.quadra = quadra;
    }

    public String getJogador() {
        return jogador;
    }

    public void setJogador(String jogador) {
        this.jogador = jogador;
    }

    public int getQtJogoSimples() {
        return qtJogoSimples;
    }

    public void setQtJogoSimples(int qtJogoSimples) {
        this.qtJogoSimples = qtJogoSimples;
    }

    public int getQtJogoDuplas() {
        return qtJogoDuplas;
    }

    public void setQtJogoDuplas(int qtJogoDuplas) {
        this.qtJogoDuplas = qtJogoDuplas;
    }

    public int getQtJogoTotal() {
        return qtJogoTotal;
    }

    public void setQtJogoTotal(int qtJogoTotal) {
        this.qtJogoTotal = qtJogoTotal;
    }

    public int getMinSimples() {
        return minSimples;
    }

    public void setMinSimples(int minSimples) {
        this.minSimples = minSimples;
    }

    public int getMinDuplas() {
        return minDuplas;
    }

    public void setMinDuplas(int minDuplas) {
        this.minDuplas = minDuplas;
    }

    public int getMinTotal() {
        return minTotal;
    }

    public void setMinTotal(int minTotal) {
        this.minTotal = minTotal;
    }

    public int getMinBloqueio() {
        return minBloqueio;
    }

    public void setMinBloqueio(int minBloqueio) {
        this.minBloqueio = minBloqueio;
    }
    
    
    public static List<RelatorioTenis> consultar(String quadras, String categorias, String matricula, String socio, String convidado, DateTime dtInicio, DateTime dtFim, String tipo, String origem, String resultado){

        ArrayList<RelatorioTenis> l = new ArrayList<RelatorioTenis>();
        Connection cn = null;
        String sql = "";

        sql = "SELECT ";

        if (resultado.equals("S")){
            sql = sql + "DISTINCT  ";
        }
                
        sql = sql + "T2.NM_QUADRA, " +
                    "T2.CD_QUADRA, " +
                    "T1.TS_INICIO, " +
                    "T1.TS_FIM, " +
                    "CASE  " +
                    "WHEN (SELECT COUNT(*) FROM TB_JOGADOR_TENIS T0 WHERE T0.CD_JOGO = T1.CD_JOGO) > 2 THEN " +
                            "'Duplas' " +
                    "ELSE " +
                            "'Simples' " +
                    "END TIPO ";
        
        if (resultado.equals("A")){
            sql = sql + ", CASE  " +
                                "WHEN T3.NR_CONVITE IS NULL THEN " +
                                        "(SELECT RIGHT('00' + CONVERT(VARCHAR, CD_CATEGORIA), 2) + '/' + RIGHT('0000' + CONVERT(VARCHAR, CD_MATRICULA), 4) + ' - ' + NOME_PESSOA FROM TB_PESSOA T0 WHERE T0.CD_MATRICULA = T3.CD_MATRICULA AND T0.CD_CATEGORIA = T3.CD_CATEGORIA AND T0.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE) " +
                                "ELSE " +
                                        "(SELECT NOME_CONVIDADO FROM TB_CONVITE T0 WHERE T0.NR_CONVITE = T3.NR_CONVITE) " +
                        "END JOGADOR, " +
                        "'CLUBE' ORIGEM ";
        }

        sql = sql + "FROM  " +
                    "TB_JOGO_TENIS T1, " +
                    "TB_QUADRA_TENIS T2, " +
                    "TB_JOGADOR_TENIS T3 " +
              "WHERE " +
                    "T1.CD_QUADRA = T2.CD_QUADRA AND " +
                    "T1.CD_JOGO = T3.CD_JOGO ";

        if (!"".equals(quadras)){
            sql = sql + " AND T2.CD_QUADRA IN (" + quadras + ")";
        }
        if (!"".equals(categorias)){
            sql = sql + " AND T3.CD_CATEGORIA IN (" + categorias + ")";
        }
        if (!"".equals(matricula)){
            sql = sql + " AND T3.CD_MATRICULA = " + matricula;
        }
        if (!"".equals(socio)){
            sql = sql + " AND EXISTS (SELECT 1 FROM TB_PESSOA T0 WHERE T3.CD_MATRICULA = T0.CD_MATRICULA AND T3.CD_CATEGORIA = T0.CD_CATEGORIA AND T3.SEQ_DEPENDENTE = T0.SEQ_DEPENDENTE AND T0.NOME_PESSOA LIKE '" + socio + "%')";
        }
        if (!"".equals(convidado)){
            sql = sql + " AND EXISTS (SELECT 1 FROM TB_CONVITE T0 WHERE T3.NR_CONVITE = T0.NR_CONVITE AND T0.NOME_CONVIDADO LIKE '" + convidado + "%')";
        }
        if (!"T".equals(tipo)){
            if ("D".equals(tipo)){
                sql = sql + " AND (SELECT COUNT(*) FROM TB_JOGADOR_TENIS T0 WHERE T0.CD_JOGO = T1.CD_JOGO) > 2";
            }else{
                sql = sql + " AND (SELECT COUNT(*) FROM TB_JOGADOR_TENIS T0 WHERE T0.CD_JOGO = T1.CD_JOGO) < 3";
            }
        }
        
        
        sql = sql + " AND T1.TS_INICIO > '" + dtInicio.toString("yyyy-MM-dd HH:mm:ss") + "'";
        sql = sql + " AND T1.TS_INICIO < '" + dtFim.toString("yyyy-MM-dd HH:mm:ss") + "'";
        
        
        try {
            if (resultado.equals("A"))
            {

                sql = sql + " ORDER BY 1, 2";

                cn = Pool.getInstance().getConnection();
                CallableStatement cal = cn.prepareCall(sql);
                ResultSet rs = cal.executeQuery();
                while (rs.next()) {

                    RelatorioTenis b = new RelatorioTenis();
                    b.quadra              = rs.getString("NM_QUADRA");
                    b.jogador             = rs.getString("JOGADOR");
                    b.tipo             = rs.getString("TIPO");
                    b.origem             = rs.getString("ORIGEM");
                    b.dtInicio          = new DateTime(rs.getTimestamp("TS_INICIO"));
                    b.dtFim             = new DateTime(rs.getTimestamp("TS_FIM"));

                    l.add(b);
                }
            }else{
                sql = "SELECT " +
                            "NM_QUADRA,  " +
                            "SUM(ISNULL(QT_JOGO_SIMPLES, 0)) QT_JOGO_SIMPLES,  " +
                            "SUM(ISNULL(QT_JOGO_DUPLA, 0)) QT_JOGO_DUPLA,  " +
                            "SUM(ISNULL(QT_JOGO_SIMPLES, 0))	+ SUM(ISNULL(QT_JOGO_DUPLA, 0)) QT_JOGO_TOTAL,  " +
                            "SUM(ISNULL(MIN_SIMPLES, 0)) MIN_SIMPLES,  " +
                            "SUM(ISNULL(MIN_DUPLA, 0)) MIN_DUPLA, " +
                            "SUM(ISNULL(MIN_SIMPLES, 0)) + SUM(ISNULL(MIN_DUPLA, 0)) MIN_TOTAL, " +
                            "DBO.FC_CALCULA_BLOQUEIO_QUADRA_TENIS('" + dtInicio.toString("yyyy-MM-dd HH:mm:ss") + "', '" + dtFim.toString("yyyy-MM-dd HH:mm:ss") + "', CD_QUADRA) MIN_BLOQUEIO " +
                        "FROM  " +
                        "( " +
                        "SELECT DISTINCT " +
                                "NM_QUADRA, " +
                                "CD_QUADRA, " +
                                "SUM(CASE WHEN TIPO = 'Simples' THEN 1 END) QT_JOGO_SIMPLES, " +
                                "SUM(CASE WHEN TIPO = 'Duplas' THEN 1 END) QT_JOGO_DUPLA, " +
                                "SUM(CASE WHEN TIPO = 'Simples' THEN DATEDIFF(mi, TS_INICIO, TS_FIM) END) MIN_SIMPLES, " +
                                "SUM(CASE WHEN TIPO = 'Duplas' THEN DATEDIFF(mi, TS_INICIO, TS_FIM) END) MIN_DUPLA " +
                        "FROM " +
                       "(" + sql +
                       ") T1 " +
                       "GROUP BY " +
                            "NM_QUADRA, " +
                            "TIPO, " +
                            "CD_QUADRA " +
                       ") T2 " +
                       "GROUP BY NM_QUADRA, CD_QUADRA";

                sql = sql + " ORDER BY 1 ";

                cn = Pool.getInstance().getConnection();
                CallableStatement cal = cn.prepareCall(sql);
                ResultSet rs = cal.executeQuery();
                while (rs.next()) {

                    RelatorioTenis b = new RelatorioTenis();
                    b.quadra = rs.getString("NM_QUADRA");
                    b.qtJogoSimples = rs.getInt("QT_JOGO_SIMPLES");
                    b.qtJogoDuplas = rs.getInt("QT_JOGO_DUPLA");
                    b.qtJogoTotal = rs.getInt("QT_JOGO_TOTAL");
                    b.minSimples = rs.getInt("MIN_SIMPLES");
                    b.minDuplas = rs.getInt("MIN_DUPLA");
                    b.minTotal = rs.getInt("MIN_TOTAL");
                    b.minBloqueio = rs.getInt("MIN_BLOQUEIO");
                    
                    l.add(b);
                }
                
            }    
            cn.close();

        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return l;
    }

    
}
