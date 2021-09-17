package techsoft.tenis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import techsoft.db.DBUtil;
import techsoft.db.SQLObject;
import techsoft.db.SQLType;
import techsoft.seguranca.Auditoria;

public class ExcecaoAgendaSemanal {
                                               // CREATE TABLE TB_EXCECAO_AGENDA_TENIS                         -- EXCEÇÃO DA AGENDA SEMANAL DE QUADRA DE TÊNIS
    private short              id;              // CD_EVENTO            SMALLINT    NOT NULL PRIMARY KEY IDENTITY, -- CÓDIGO DO EVENTO
    private QuadraTenis        quadra;          // CD_QUADRA            SMALLINT    NOT NULL,                      -- CÓDIGO DA QUADRA DE TÊNIS
    private EventoSemanal.Tipo tipo;            // CD_TIPO_EVENTO       CHAR(1)     NOT NULL,                      -- CÓDIGO DO TIPO DE EVENTO
    private DateTime           inicio;          // TS_INICIO            DATETIME    NOT NULL,                      -- TIMESTAMP DO INÍCIO DO EVENTO
    private DateTime           fim;             // TS_FIM               DATETIME    NOT NULL,                      -- TIMESTAMP DO FIM DO EVENTO
    private Short              minutosMarcacao; // QT_MINUTOS_MARCACAO  SMALLINT,                                  -- QUANTIDADE DE MINUTOS ANTES DO FIM DO EVENTO EM QUE A MARCAÇÃO É PERMITIDA    
    private String             observacao;      // TX_OBSERVACAO        VARCHAR(40)                                -- TEXTO DE OBSERVACAO SOBRE O EVENTO

    public short getId() { return this.id; }
    public EventoSemanal.Tipo getTipo() { return this.tipo; }
    public QuadraTenis getQuadra() { return this.quadra; }
    public DateTime getInicio() { return this.inicio; }
    public DateTime getFim() { return this.fim; }
    public Short getMinutosMarcacao() { return this.minutosMarcacao; }
    public String getObservacao() { return this.observacao; }
    
    public void setTipo(EventoSemanal.Tipo tipo) { this.tipo = tipo; }
    public void setQuadra(QuadraTenis quadra) { this.quadra = quadra; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public void setInicio(DateTime inicio) { this.inicio = inicio.withSecondOfMinute(0).withMillisOfSecond(0); }
    public void setFim(DateTime fim) { this.fim = fim.withSecondOfMinute(59).withMillisOfSecond(999); }
    public void setMinutosMarcacao(Short minutos) { this.minutosMarcacao = minutos; }

    @Override
    public boolean equals(Object o) {
        if (this.id == 0) return false;
        if (o == this) return true;
        if (!(o instanceof ExcecaoAgendaSemanal)) return false;
        return (this.id == ((ExcecaoAgendaSemanal) o).id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.id;
        return hash;
    }

    public EventoQuadraTenis[] toEvento(Interval intervalo) {
        Interval i = new Interval(this.inicio, this.fim.plusMinutes(1).withSecondOfMinute(0).withMillisOfSecond(0));
        if (!intervalo.overlaps(i)) return new EventoQuadraTenis[0];
        
        EventoQuadraTenis[] a = new EventoQuadraTenis[1];
        QuadraTenis.Estado e = null;
        JogoTenis.Tipo t = null;
        switch(this.tipo) {
            case LIVRE:
                e = QuadraTenis.Estado.LIVRE;
                break;
            case SIMPLES:
                e = QuadraTenis.Estado.LIVRE;
                t = JogoTenis.Tipo.SIMPLES;
                break;
            case DUPLAS:
                e = QuadraTenis.Estado.LIVRE;
                t = JogoTenis.Tipo.DUPLAS;
                break;
            case BLOQUEADA:
                e = QuadraTenis.Estado.BLOQUEADA;
                break;
        }
        a[0] = new EventoQuadraTenis(this.quadra, i, e, t, this.observacao, this, 1);
        return a;
    }
    
    public static ExcecaoAgendaSemanal getInstance(int id) {
        String sql = "SELECT * FROM TB_EXCECAO_AGENDA_TENIS WHERE CD_EVENTO = ?";
        return DBUtil.queryObject(new DBUtil.ObjectFactory<ExcecaoAgendaSemanal>() {
            @Override
            public ExcecaoAgendaSemanal buildFrom(ResultSet rs) throws SQLException {
                ExcecaoAgendaSemanal e = new ExcecaoAgendaSemanal();
                e.id              = rs.getShort("CD_EVENTO");
                e.quadra          = QuadraTenis.getInstance(rs.getShort("CD_QUADRA"));
                e.tipo            = EventoSemanal.Tipo.forCode(rs.getString("CD_TIPO_EVENTO"));
                e.inicio          = new DateTime(rs.getTimestamp("TS_INICIO")).withSecondOfMinute(0).withMillisOfSecond(0);
                e.fim             = new DateTime(rs.getTimestamp("TS_FIM")).withSecondOfMinute(59).withMillisOfSecond(999);
                e.minutosMarcacao = (Short) rs.getObject("QT_MINUTOS_MARCACAO");
                e.observacao      = (String) rs.getObject("TX_OBSERVACAO");
                return e;
            }
        }, sql, SQLType.SMALLINT((short) id));
    }

    private static final DBUtil.ObjectFactory<ExcecaoAgendaSemanal> builder = new DBUtil.ObjectFactory<ExcecaoAgendaSemanal>() {
        @Override
        public ExcecaoAgendaSemanal buildFrom(ResultSet rs) throws SQLException {
            ExcecaoAgendaSemanal e = new ExcecaoAgendaSemanal();
            e.id              = rs.getShort("CD_EVENTO");
            e.tipo            = EventoSemanal.Tipo.forCode(rs.getString("CD_TIPO_EVENTO"));
            e.inicio          = new DateTime(rs.getTimestamp("TS_INICIO")).withSecondOfMinute(0).withMillisOfSecond(0);
            e.fim             = new DateTime(rs.getTimestamp("TS_FIM")).withSecondOfMinute(59).withMillisOfSecond(999);
            e.minutosMarcacao = (Short) rs.getObject("QT_MINUTOS_MARCACAO");
            e.observacao      = (String) rs.getObject("TX_OBSERVACAO");
            return e;
        }
    };
    
    public static ExcecaoAgendaSemanal[] listFor(QuadraTenis quadra) {
        String sql = "SELECT * FROM TB_EXCECAO_AGENDA_TENIS WHERE CD_QUADRA = ? ORDER BY TS_INICIO";
        List<ExcecaoAgendaSemanal> l = DBUtil.queryList(builder, sql, SQLType.SMALLINT((short) quadra.getId()));
        for (ExcecaoAgendaSemanal e : l) e.quadra = quadra;
        return l.toArray(new ExcecaoAgendaSemanal[l.size()]);
    }
    
    public static ExcecaoAgendaSemanal[] listFor(QuadraTenis quadra, Interval intervalo) {
        String sql = "SELECT * FROM TB_EXCECAO_AGENDA_TENIS WHERE CD_QUADRA = ? AND TS_FIM >= ? AND TS_INICIO < ? ORDER BY TS_INICIO";
        
        SQLObject[] parms = {
            SQLType.SMALLINT((short) quadra.getId()),
            SQLType.TIMESTAMP(Timestamp.valueOf(intervalo.getStart().toString("yyyy-MM-dd HH:mm:ss"))),
            SQLType.TIMESTAMP(Timestamp.valueOf(intervalo.getEnd().toString("yyyy-MM-dd HH:mm:ss")))
        };
        
        List<ExcecaoAgendaSemanal> l = DBUtil.queryList(builder, sql, parms);
        for (ExcecaoAgendaSemanal e : l) e.quadra = quadra;
        return l.toArray(new ExcecaoAgendaSemanal[l.size()]);
    }
    
    private boolean overlapsAny() {
        String sql = "SELECT COUNT(CD_EVENTO) "     +
                    "FROM TB_EXCECAO_AGENDA_TENIS " +
                    "WHERE "                        +
                    "CD_EVENTO <> ? AND "           + // 1
                    "CD_QUADRA  = ? AND "           + // 2
                    "TS_FIM    >= ? AND "           + // 3
                    "TS_INICIO <= ?";                 // 4
        
        SQLObject[] parms = {
            SQLType.SMALLINT(this.id),
            SQLType.SMALLINT((short) this.quadra.getId()),
            SQLType.TIMESTAMP(Timestamp.valueOf(this.inicio.toString("yyyy-MM-dd HH:mm:ss"))),
            SQLType.TIMESTAMP(Timestamp.valueOf(this.fim.toString("yyyy-MM-dd HH:mm:ss")))
        };
        
        return (DBUtil.queryCount(sql, parms) > 0);
    }
    
    public void save(Auditoria audit) {
        if (this.quadra == null) throw new IllegalStateException("Quadra indefinida");
        if (this.tipo == null) throw new IllegalStateException("Situação indefinida");
        if (this.inicio == null) throw new IllegalStateException("Horário de início indefinido");
        if (this.fim == null) throw new IllegalStateException("Horário de fim indefinido");
        if (this.inicio.isAfter(this.fim)) throw new IllegalStateException("Horário de início não pode ser posterior ao horário de fim");
        if (this.overlapsAny()) throw new RuntimeException("Exceção em conflito com outra já existente");

        if (this.id == 0) this.insert(audit);
    }
    
    private void insert(Auditoria audit) {
        String sql = "INSERT INTO TB_EXCECAO_AGENDA_TENIS (" +
                    "CD_QUADRA, "           + // 1
                    "CD_TIPO_EVENTO, "      + // 2
                    "TS_INICIO, "           + // 3
                    "TS_FIM, "              + // 4
                    "QT_MINUTOS_MARCACAO, " + // 5
                    "TX_OBSERVACAO"         + // 6
                    ") VALUES (?, ?, ?, ?, ?, ?)";
        
        SQLObject[] parms = {
            SQLType.SMALLINT((short) this.quadra.getId()),                                     // 1
            SQLType.CHAR("" + this.tipo.getCode()),                                            // 2
            SQLType.TIMESTAMP(Timestamp.valueOf(this.inicio.toString("yyyy-MM-dd HH:mm:ss"))), // 3
            SQLType.TIMESTAMP(Timestamp.valueOf(this.fim.toString("yyyy-MM-dd HH:mm:ss"))),    // 4
            SQLType.SMALLINT(this.minutosMarcacao),                                            // 5
            SQLType.VARCHAR(this.observacao)                                                   // 6
        };

        this.id = (short) DBUtil.updateGenerateKey(audit, sql, parms);
    }
    
    public void delete(Auditoria audit) {
        if (this.id == 0) return;
        String sql = "DELETE FROM TB_EXCECAO_AGENDA_TENIS WHERE CD_EVENTO = ?";
        DBUtil.update(audit, sql, SQLType.SMALLINT((short) this.id));
    }
    
}
