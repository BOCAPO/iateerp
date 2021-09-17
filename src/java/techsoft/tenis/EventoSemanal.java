package techsoft.tenis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import techsoft.db.DBUtil;
import techsoft.db.SQLObject;
import techsoft.db.SQLType;
import techsoft.seguranca.Auditoria;
import techsoft.util.DayOfWeek;

public class EventoSemanal {
                                               // CREATE TABLE TB_AGENDA_SEMANAL_TENIS                         -- AGENDA SEMANAL DE QUADRA DE TÊNIS
    private short              id;              // CD_EVENTO            SMALLINT    NOT NULL PRIMARY KEY IDENTITY, -- CÓDIGO DO EVENTO
    private QuadraTenis        quadra;          // CD_QUADRA            SMALLINT    NOT NULL,                      -- CÓDIGO DA QUADRA DE TÊNIS
    private EventoSemanal.Tipo tipo;            // CD_TIPO_EVENTO       CHAR(1)     NOT NULL,                      -- CÓDIGO DO TIPO DE EVENTO
    private DayOfWeek          dia;             // DD_SEMANA            TINYINT     NOT NULL,                      -- DIA DA SEMANA DO EVENTO
    private LocalTime          inicio;          // HR_INICIO            TIME(0)     NOT NULL,                      -- HORÁRIO DE INÍCIO DO EVENTO
    private LocalTime          fim;             // HR_FIM               TIME(0)     NOT NULL,                      -- HORÁRIO DE FIM DO EVENTO
    private Short              minutosMarcacao; // QT_MINUTOS_MARCACAO  SMALLINT,                                  -- QUANTIDADE DE MINUTOS ANTES DO FIM DO EVENTO EM QUE A MARCAÇÃO É PERMITIDA
    private String             observacao;      // TX_OBSERVACAO        VARCHAR(40)                                -- TEXTO DE OBSERVACAO SOBRE O EVENTO

    public enum Tipo {
        BLOQUEADA ('B', "Bloqueada"),
        LIVRE     ('L', "Aberta para jogos simples ou de duplas"),
        SIMPLES   ('S', "Aberta somente para jogos simples"),
        DUPLAS    ('D', "Aberta somente para jogos de duplas");

        private final char code;
        private final String description;

        private Tipo(char code, String description) {
            this.code = code;
            this.description = description;
        }

        public char getCode() { return this.code; }
        public String getDescription() { return this.description; }

        public static Tipo forCode(char c) {
            for (Tipo t : Tipo.values()) if (t.code == c) return t;
            return null;
        }

        public static Tipo forCode(String s) {
            if ((s != null) && (s.length() == 1)) return Tipo.forCode(s.toCharArray()[0]);
            else return null;
        }
    }

    
    public short getId() { return this.id; }
    public EventoSemanal.Tipo getTipo() { return this.tipo; }
    public QuadraTenis getQuadra() { return this.quadra; }
    public DayOfWeek getDia() { return this.dia; }
    public LocalTime getInicio() { return this.inicio; }
    public LocalTime getFim() { return this.fim; }
    public Short getMinutosMarcacao() { return this.minutosMarcacao; }
    public String getObservacao() { return this.observacao; }
    
    public void setTipo(EventoSemanal.Tipo tipo) { this.tipo = tipo; }
    public void setQuadra(QuadraTenis quadra) { this.quadra = quadra; }
    public void setDia(DayOfWeek dia) { this.dia = dia; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public void setInicio(LocalTime inicio) { this.inicio = inicio.withSecondOfMinute(0).withMillisOfSecond(0); }
    public void setFim(LocalTime fim) { this.fim = fim.withSecondOfMinute(59).withMillisOfSecond(999); }
    public void setMinutosMarcacao(Short minutos) { this.minutosMarcacao = minutos; }

    @Override
    public boolean equals(Object o) {
        if (this.id == 0) return false;
        if (o == this) return true;
        if (!(o instanceof EventoSemanal)) return false;
        return (this.id == ((EventoSemanal) o).id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.id;
        return hash;
    }
    
    public EventoQuadraTenis[] toEvento(Interval intervalo) {
        List<EventoQuadraTenis> l = new ArrayList<EventoQuadraTenis>();
        
        DateTime d = intervalo.getStart().toDateMidnight().toDateTime();
        
        while(d.isBefore(intervalo.getEnd())) {
            if (d.getDayOfWeek() != this.dia.getCode()) d = d.plusDays(1);
            else {
                Interval i = new Interval(this.inicio.toDateTime(d), this.fim.toDateTime(d).plusMinutes(1).withSecondOfMinute(0).withMillisOfSecond(0));
                if (intervalo.overlaps(i)) {
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
                    l.add(new EventoQuadraTenis(this.quadra, i, e, t, this.observacao, this, 2));
                }
                d = d.plusDays(7);
            }
        }
        return l.toArray(new EventoQuadraTenis[l.size()]);
    }
    
    public boolean overlaps(EventoSemanal e) {
        if (e == null) return false;
        if (this.dia != e.getDia()) return false;
        if (!this.quadra.equals(e.getQuadra())) return false;
        if (this.inicio.isAfter(e.fim)) return false;
        if (this.fim.isBefore(e.inicio)) return false;
        return true;
    }

    public static EventoSemanal getInstance(int id) {
        String sql = "SELECT * FROM TB_AGENDA_SEMANAL_TENIS WHERE CD_EVENTO = ?";
        return DBUtil.queryObject(new DBUtil.ObjectFactory<EventoSemanal>() {
            @Override
            public EventoSemanal buildFrom(ResultSet rs) throws SQLException {
                EventoSemanal e   = new EventoSemanal();
                e.id              = rs.getShort("CD_EVENTO");
                e.quadra          = QuadraTenis.getInstance(rs.getShort("CD_QUADRA"));
                e.tipo            = EventoSemanal.Tipo.forCode(rs.getString("CD_TIPO_EVENTO"));
                e.dia             = DayOfWeek.forCode(rs.getByte("DD_SEMANA"));
                e.inicio          = (new LocalTime(rs.getTime("HR_INICIO"))).withSecondOfMinute(0).withMillisOfSecond(0);
                e.fim             = (new LocalTime(rs.getTime("HR_FIM"))).withSecondOfMinute(59).withMillisOfSecond(999);
                e.minutosMarcacao = (Short) rs.getObject("QT_MINUTOS_MARCACAO");
                e.observacao      = (String) rs.getObject("TX_OBSERVACAO");
                return e;
            }
        }, sql, SQLType.SMALLINT((short) id));
    }
    
    private static final DBUtil.ObjectFactory<EventoSemanal> builder = new DBUtil.ObjectFactory<EventoSemanal>() {
        @Override
        public EventoSemanal buildFrom(ResultSet rs) throws SQLException {
            EventoSemanal e   = new EventoSemanal();
            e.id              = rs.getShort("CD_EVENTO");
            e.tipo            = EventoSemanal.Tipo.forCode(rs.getString("CD_TIPO_EVENTO"));
            e.dia             = DayOfWeek.forCode(rs.getByte("DD_SEMANA"));
            e.inicio          = (new LocalTime(rs.getTime("HR_INICIO"))).withSecondOfMinute(0).withMillisOfSecond(0);
            e.fim             = (new LocalTime(rs.getTime("HR_FIM"))).withSecondOfMinute(59).withMillisOfSecond(999);
            e.minutosMarcacao = (Short) rs.getObject("QT_MINUTOS_MARCACAO");
            e.observacao      = (String) rs.getObject("TX_OBSERVACAO");
            return e;
        }
    };
    
    public static EventoSemanal[] listFor(QuadraTenis quadra) {
        String sql = "SELECT * FROM TB_AGENDA_SEMANAL_TENIS WHERE CD_QUADRA = ? ORDER BY DD_SEMANA, HR_INICIO";
        List<EventoSemanal> l = DBUtil.queryList(builder, sql, SQLType.SMALLINT((short) quadra.getId()));
        for (EventoSemanal e : l) e.quadra = quadra;
        return l.toArray(new EventoSemanal[l.size()]);
    }

    public void save(Auditoria audit) {
        if (this.tipo == null) throw new IllegalStateException("Situação indefinida");
        if (this.quadra == null) throw new IllegalStateException("Quadra indefinida");
        if (this.dia == null) throw new IllegalStateException("Dia indefinido");
        if (this.inicio == null) throw new IllegalStateException("Horário de início indefinido");
        if (this.fim == null) throw new IllegalStateException("Horário de fim indefinido");
        if (this.inicio.isAfter(this.fim)) throw new IllegalStateException("Horário de início não pode ser posterior ao horário de fim");
        
        for (EventoSemanal e : EventoSemanal.listFor(this.quadra))
            if (this.overlaps(e) && this.id != e.id) throw new RuntimeException("Evento em conflito com outro já existente");

        if (this.id == 0) this.insert(audit);
    }
    
    private void insert(Auditoria audit) {
        String sql = "INSERT INTO TB_AGENDA_SEMANAL_TENIS (" +
                    "CD_QUADRA, "           + // 1
                    "CD_TIPO_EVENTO, "      + // 2
                    "DD_SEMANA, "           + // 3
                    "HR_INICIO, "           + // 4
                    "HR_FIM, "              + // 5
                    "QT_MINUTOS_MARCACAO, " + // 6
                    "TX_OBSERVACAO"         + // 7
                    ") VALUES (? , ? , ? , ? , ? , ?, ?)";

        SQLObject[] parms = {
            SQLType.SMALLINT((short) this.quadra.getId()),                                           // 1
            SQLType.CHAR("" + this.tipo.getCode()),                                                  // 2
            SQLType.TINYINT((byte) this.dia.getCode()),                                              // 3
            SQLType.TIME(new Time(this.inicio.toDateTimeToday().getMillis())),                       // 4
            SQLType.TIME(new Time(this.fim.withMillisOfSecond(0).toDateTimeToday().getMillis())),    // 5
            SQLType.SMALLINT(this.minutosMarcacao),                                                  // 6
            SQLType.VARCHAR(this.observacao)                                                         // 7
        };
        
        this.id = (short) DBUtil.updateGenerateKey(audit, sql, parms);
    }
    
    public void delete(Auditoria audit) {
        if (this.id == 0) return;
        String sql = "DELETE FROM TB_AGENDA_SEMANAL_TENIS WHERE CD_EVENTO = ?";
        DBUtil.update(audit, sql, SQLType.SMALLINT((short) this.id));
    }
    
}
