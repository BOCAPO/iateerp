package techsoft.tenis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import techsoft.cadastro.Convite;
import techsoft.cadastro.Socio;
import techsoft.db.SQLType;
import techsoft.db.DBUtil;
import techsoft.db.SQLObject;
import techsoft.seguranca.Auditoria;

public class JogadorTenis {
                                      //CREATE TABLE TB_JOGADOR_TENIS                                 -- JOGADOR DE TÊNIS
    private JogoTenis     jogo;       //CD_JOGO              INT         NOT NULL,                      -- CÓDIGO DO JOGO
    private short         sequencial; //SEQ_JOGADOR          SMALLINT    NOT NULL,                      -- SEQUENCIAL DO JOGADOR NO JOGO
    private final Socio   socio;      //CD_MATRICULA         INT,                                       -- CÓDIGO DA MATRÍCULA DO JOGADOR
                                      //SEQ_DEPENDENTE       SMALLINT,                                  -- SEQUENCIAL DE DEPENDENTE DO JOGADOR
                                      //CD_CATEGORIA         SMALLINT,                                  -- CÓDIGO DA CATEGORIA DO JOGADOR
    private final Convite convite;    //NR_CONVITE           NUMERIC(10),                               -- NÚMERO DO CONVITE DO JOGADOR


    public JogadorTenis(Socio socio) {
        if (socio == null) throw new NullPointerException();
        this.socio = socio;
        this.convite = null;
    }
    
    public JogadorTenis(Convite convidado) {
        if (convidado == null) throw new NullPointerException();
        this.convite = convidado;
        this.socio = null;
    }
    
    private boolean isSocio() { return (this.socio != null); }
    private boolean isConvidado() { return (this.convite != null); }
 
    public Socio getSocio() { return this.socio; }
    public Convite getConvite() { return this.convite; }
    
    public String getNome() { 
        String nome;
        if (this.isSocio())
            nome = this.socio.getNome();
        else {
            nome = this.convite.getConvidado();
            if (StringUtils.isBlank(nome)) nome = String.valueOf(this.convite.getNumero());
        }
        return nome.substring(0, nome.indexOf(' '));
    }
    
    public String fotoURL() {
        if (this.isSocio())
            return "f?tb=6&mat=" + this.socio.getMatricula() +"&seq=" + this.socio.getSeqDependente() + "&cat=" + this.socio.getIdCategoria();
        else
            return "f?tb=4&nr=" + this.convite.getNumero();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof JogadorTenis)) return false;
        JogadorTenis j = (JogadorTenis) o;
        if (!ObjectUtils.equals(this.socio, j.socio)) return false;
        if (!ObjectUtils.equals(this.convite, j.convite)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.socio != null ? this.socio.hashCode() : 0);
        hash = 29 * hash + (this.convite != null ? this.convite.hashCode() : 0);
        return hash;
    }
    
    private static final DBUtil.ObjectFactory builder = new DBUtil.ObjectFactory() {
        @Override
        public Object buildFrom(ResultSet rs) throws SQLException {
            JogadorTenis j;
            if (rs.getObject("NR_CONVITE") != null)
                j = new JogadorTenis(Convite.getInstance(rs.getInt("NR_CONVITE")));
            else 
                j = new JogadorTenis(Socio.getInstance(rs.getInt("CD_MATRICULA"), rs.getShort("SEQ_DEPENDENTE"), rs.getShort("CD_CATEGORIA")));
            j.sequencial = rs.getShort("SEQ_JOGADOR");
            return j;
        }
    };

    static List<JogadorTenis> listFor(JogoTenis jogo) {
        String sql = "SELECT * FROM TB_JOGADOR_TENIS WHERE CD_JOGO = ? ORDER BY SEQ_JOGADOR";
        List<JogadorTenis> l = DBUtil.queryList(JogadorTenis.builder, sql, SQLType.INTEGER(jogo.getId()));
        for (JogadorTenis j : l) j.jogo = jogo;
        return l;
    }
    
    public boolean isLivre(DateTime instant) {
        String sql = "SELECT COUNT(A.CD_JOGO) "   +
                    "FROM "                       + 
                    "TB_JOGADOR_TENIS AS A, "     +
                    "TB_JOGO_TENIS AS B "         +
                    "WHERE "                      +
                    "A.CD_JOGO  = B.CD_JOGO AND " +
                    "B.TS_FIM  >= ? AND ";          // 1
        
        List<SQLObject> parms = new ArrayList<SQLObject>();
        parms.add(SQLType.TIMESTAMP(Timestamp.valueOf(instant.toString("yyyy-MM-dd HH:mm:ss"))));
        
        if (this.isSocio()) {
            sql = sql + "A.CD_MATRICULA   = ?  AND " + // 2
                        "A.SEQ_DEPENDENTE = ?  AND " + // 3
                        "A.CD_CATEGORIA   = ?";        // 4
            parms.add(SQLType.INTEGER(this.socio.getMatricula()));
            parms.add(SQLType.SMALLINT(this.socio.getSeqDependente()));
            parms.add(SQLType.SMALLINT(this.socio.getIdCategoria()));
        } else {
            sql = sql + "A.NR_CONVITE = ?"; // 2
            parms.add(SQLType.INTEGER(this.convite.getNumero()));
        }
        
        return (DBUtil.queryCount(sql, parms) == 0);
    }
    
    public boolean isLivreExceto(JogoTenis jogo, DateTime instant) {
        String sql = "SELECT COUNT(A.CD_JOGO) "   +
                    "FROM "                       + 
                    "TB_JOGADOR_TENIS AS A, "     +
                    "TB_JOGO_TENIS AS B "         +
                    "WHERE "                      +
                    "A.CD_JOGO  = B.CD_JOGO AND " +
                    "A.CD_JOGO <> ? AND "         + // 1
                    "B.TS_FIM  >= ? AND ";          // 2
        
        List<SQLObject> parms = new ArrayList<SQLObject>();
        parms.add(SQLType.INTEGER(jogo.getId()));                                                 // 1
        parms.add(SQLType.TIMESTAMP(Timestamp.valueOf(instant.toString("yyyy-MM-dd HH:mm:ss")))); // 2
        
        if (this.isSocio()) {
            sql = sql + "A.CD_MATRICULA   = ?  AND " + // 3
                        "A.SEQ_DEPENDENTE = ?  AND " + // 4
                        "A.CD_CATEGORIA   = ?";        // 5
            parms.add(SQLType.INTEGER(this.socio.getMatricula()));      // 3
            parms.add(SQLType.SMALLINT(this.socio.getSeqDependente())); // 4
            parms.add(SQLType.SMALLINT(this.socio.getIdCategoria()));   // 5
        } else {
            sql = sql + "A.NR_CONVITE = ?"; // 3
            parms.add(SQLType.INTEGER(this.convite.getNumero())); // 3
        }
        
        return (DBUtil.queryCount(sql, parms) == 0);
    }
    
    static void insert(Auditoria audit, JogoTenis jogo) {
        String sql = "INSERT INTO TB_JOGADOR_TENIS (" +
                    "CD_JOGO, "        + // 1
                    "SEQ_JOGADOR, "    + // 2
                    "CD_MATRICULA, "   + // 3
                    "SEQ_DEPENDENTE, " + // 4
                    "CD_CATEGORIA, "   + // 5
                    "NR_CONVITE"       + // 6
                    ") VALUES (?, ?, ?, ?, ?, ?)";
        
        JogadorTenis[] jogadores = jogo.getJogadores().toArray(new JogadorTenis[jogo.getJogadores().size()]);
        if (jogadores != null) {
            for (int i = 0; i < jogadores.length; i++) {
                JogadorTenis j = jogadores[i];
                j.jogo = jogo;
                j.sequencial = (short) (i + 1);
                SQLObject[] parms = {
                    SQLType.INTEGER(j.jogo.getId()), // 1
                    SQLType.SMALLINT(j.sequencial),  // 2
                    SQLType.INTEGER(null),           // 3
                    SQLType.SMALLINT(null),          // 4
                    SQLType.SMALLINT(null),          // 5
                    SQLType.NUMERIC(null)            // 6
                };
                if (j.convite != null) parms[5] = SQLType.NUMERIC(j.convite.getNumero());
                else {
                    parms[2] = SQLType.INTEGER(j.socio.getMatricula());
                    parms[3] = SQLType.SMALLINT((short) j.socio.getSeqDependente());
                    parms[4] = SQLType.SMALLINT((short) j.socio.getIdCategoria());
                }
                DBUtil.update(audit, sql, parms);
            }
        }    
    }
    
    static void delete(Auditoria audit, JogoTenis jogo) {
        String sql = "DELETE FROM TB_JOGADOR_TENIS WHERE CD_JOGO = ?";
        DBUtil.update(audit, sql, SQLType.INTEGER(jogo.getId()));
    }
}
