package techsoft.tenis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.ReadableInstant;
import techsoft.db.SQLObject;
import techsoft.db.SQLType;
import techsoft.db.DBUtil;
import techsoft.seguranca.Auditoria;

public class JogoTenis {
                                  // CREATE TABLE TB_JOGO_TENIS                                   -- JOGO DE TÊNIS
    private int         id;       // CD_JOGO              INT         NOT NULL PRIMARY KEY IDENTITY, -- CÓDIGO DO JOGO
    private QuadraTenis quadra;   // CD_QUADRA            SMALLINT    NOT NULL,                      -- CÓDIGO DA QUADRA DE TÊNIS
    private DateTime    inicio;   // TS_INICIO            DATETIME    NOT NULL,                      -- TIMESTAMP DE INÍCIO DO JOGO
    private DateTime    fim;      // TS_FIM               DATETIME    NOT NULL,                      -- TIMESTAMP DE FIM DO JOGO
    private String      usuario;  // USER_ACESSO_SISTEMA  CHAR(12),                                  -- USUÁRIO RESPONSÁVEL PELA MARCAÇÃO
    private DateTime    marcacao; // TS_MARCACAO          DATETIME    NOT NULL                       -- TIMESTAMP DA MARCAÇÃO DO JOGO
    private List<JogadorTenis> jogadores;
   
    public enum Tipo {
        SIMPLES,
        DUPLAS;
    }

    private JogoTenis() {}
    
    public int getId() { return this.id; }
    public QuadraTenis getQuadra() { return this.quadra; }
    public DateTime getHorarioInicio() { return this.inicio; }
    public DateTime getHorarioFim() { return this.fim; }
    public String getUsuarioMarcacao() { return this.usuario; }
    public DateTime getHorarioMarcacao() { return marcacao; }
    public List<JogadorTenis> getJogadores() { return this.jogadores; }
    
    public JogoTenis.Tipo getTipo() {
        if (this.jogadores != null)
            return (this.jogadores.size() > 2) ? JogoTenis.Tipo.DUPLAS : JogoTenis.Tipo.SIMPLES;
        return null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this.id == 0) return false;
        if (o == this) return true;
        if (!(o instanceof JogoTenis)) return false;
        return (this.id == ((JogoTenis) o).id);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.id;
        return hash;
    }

    public EventoQuadraTenis[] toEvento(Interval intervalo) {
        Interval i = new Interval(this.inicio, this.fim.plusMinutes(1).withSecondOfMinute(0).withMillisOfSecond(0));
        if (!intervalo.overlaps(i)) return new EventoQuadraTenis[0];

        EventoQuadraTenis[] a = new EventoQuadraTenis[1];
        a[0] = new EventoQuadraTenis(this.quadra, i, QuadraTenis.Estado.OCUPADA, null, null, this, 0, this.jogadores.toArray(new JogadorTenis[this.jogadores.size()]));
        return a;
    }
    
    public static Interval previsao(QuadraTenis quadra, JogoTenis.Tipo tipoJogo, ReadableInstant instanteMarcacao) {
        AgendaTenis agenda = quadra.getAgenda();
        EventoQuadraTenis evento = quadra.eventoLivre();
        if (evento == null) return null;
        if (evento.getRestricaoJogo() != null && evento.getRestricaoJogo() != tipoJogo) return null;

        DateTime horarioInicio;
        horarioInicio = evento.inicio().withSecondOfMinute(0).withMillisOfSecond(0);
        if (horarioInicio.isBefore(instanteMarcacao)) horarioInicio = new DateTime(instanteMarcacao).withSecondOfMinute(0).withMillisOfSecond(0);
        
        int tempoDisponivel = 0;
        while (evento != null) {
            if (evento.getEstadoQuadra() != QuadraTenis.Estado.LIVRE) break;
            if (evento.getRestricaoJogo() != null && evento.getRestricaoJogo() != tipoJogo) break;
            
            Interval disponibilidade = evento.intervalo();
            if (disponibilidade.getStart().isBefore(horarioInicio)) disponibilidade = disponibilidade.withStart(horarioInicio);
            tempoDisponivel += disponibilidade.toPeriod().toStandardMinutes().getMinutes();
            
            evento = agenda.eventoPosterior(evento);
        }
        
        int duracaoJogo;
        if (tipoJogo == JogoTenis.Tipo.SIMPLES)
            duracaoJogo = (tempoDisponivel < quadra.getDuracaoSimples()) ? tempoDisponivel : quadra.getDuracaoSimples();
        else
            duracaoJogo = (tempoDisponivel < quadra.getDuracaoDuplas()) ? tempoDisponivel : quadra.getDuracaoDuplas();
        
        DateTime horarioFim;
        horarioFim = horarioInicio.plusMinutes(duracaoJogo - 1).withSecondOfMinute(59).withMillisOfSecond(999);
        return new Interval(horarioInicio, horarioFim);
    }

    public static JogoTenis marcar(QuadraTenis quadra, JogadorTenis... jogadores) {
        return JogoTenis.marcar(null, quadra, jogadores);
    }

    public static JogoTenis marcar(Auditoria audit, QuadraTenis quadra, JogadorTenis... jogadores) {
        if (quadra == null)
            throw new NullPointerException("Quadra não pode ser null");
        
        if (jogadores.length == 0)
            throw new IllegalArgumentException("Jogadores não informados");
        
        if (jogadores.length  < 2 || jogadores.length > 4) 
            throw new IllegalArgumentException("Um jogo deve ter entre 2 e 4 jogadores");
        
        for (int i = 0; i < jogadores.length; i++) {
            if (jogadores[i] == null)
                throw new NullPointerException("Nenhum jogador pode ser null");
            for (int j = i + 1; j < jogadores.length; j++)
                if (jogadores[i].equals(jogadores[j]))
                    throw new IllegalArgumentException("Não pode haver jogadores repetidos");
        }
        
        DateTime now = DateTime.now();
        JogoTenis j = new JogoTenis();
        j.quadra = quadra;
        j.usuario = (audit == null) ? null : audit.getLogin();
        j.marcacao = now; 
        j.jogadores = Arrays.asList(jogadores);
        
        for (int k = 0; k < 5; k++) { // Quantidade de tentativas
            Interval i = JogoTenis.previsao(quadra, j.getTipo(), now);
            if (i != null) {                
                j.inicio = i.getStart();
                j.fim = i.getEnd();
                
                for (JogadorTenis jt : j.jogadores)
                    if (!jt.isLivre(now))
                        throw new RuntimeException("Jogador " + jt.getNome() + " já está participando de outro jogo");
                
                if (!j.overlapsAny()) {
                    j.insert(audit);
                    return j;
                }
            } else return null;
        }
        return null;
    }
    
    public static JogoTenis getInstance(int id) {
        String sql = "SELECT * FROM TB_JOGO_TENIS WHERE CD_JOGO = ?";
        return DBUtil.queryObject(new DBUtil.ObjectFactory<JogoTenis>() {
            @Override
            public JogoTenis buildFrom(ResultSet rs) throws SQLException {
                JogoTenis j = new JogoTenis();
                j.id        = rs.getShort("CD_JOGO");
                j.quadra    = QuadraTenis.getInstance(rs.getShort("CD_QUADRA"));
                j.inicio    = new DateTime(rs.getTimestamp("TS_INICIO")).withSecondOfMinute(0).withMillisOfSecond(0);
                j.fim       = new DateTime(rs.getTimestamp("TS_FIM")).withSecondOfMinute(59).withMillisOfSecond(999);
                j.usuario   = rs.getString("USER_ACESSO_SISTEMA");
                j.marcacao  = new DateTime(rs.getTimestamp("TS_MARCACAO"));
                j.jogadores = JogadorTenis.listFor(j);
                return j;
            }
        }, sql, SQLType.INTEGER(id));
    }

    private static final DBUtil.ObjectFactory<JogoTenis> builder = new DBUtil.ObjectFactory<JogoTenis>() {
        @Override
        public JogoTenis buildFrom(ResultSet rs) throws SQLException {
            JogoTenis j = new JogoTenis();
            j.id        = rs.getInt("CD_JOGO");
            j.inicio    = new DateTime(rs.getTimestamp("TS_INICIO")).withSecondOfMinute(0).withMillisOfSecond(0);
            j.fim       = new DateTime(rs.getTimestamp("TS_FIM")).withSecondOfMinute(59).withMillisOfSecond(999);
            j.usuario   = rs.getString("USER_ACESSO_SISTEMA");
            j.marcacao  = new DateTime(rs.getTimestamp("TS_MARCACAO"));
            j.jogadores = JogadorTenis.listFor(j);
            return j;
        }
    };    

    public static List<JogoTenis> listFor(QuadraTenis quadra, Interval intervalo) {
        String sql = "SELECT * FROM TB_JOGO_TENIS WHERE CD_QUADRA = ? AND TS_FIM >= ? AND TS_INICIO < ? ORDER BY TS_INICIO";
        
        SQLObject[] parms = {
            SQLType.SMALLINT((short) quadra.getId()),
            SQLType.TIMESTAMP(Timestamp.valueOf(intervalo.getStart().toString("yyyy-MM-dd HH:mm:ss"))),
            SQLType.TIMESTAMP(Timestamp.valueOf(intervalo.getEnd().toString("yyyy-MM-dd HH:mm:ss")))
        };

        List<JogoTenis> l = DBUtil.queryList(JogoTenis.builder, sql, parms);
        for (JogoTenis j : l) j.quadra = quadra;
        return l;
    }

    private boolean overlapsAny() {
        String sql = "SELECT COUNT(CD_JOGO) " +
                    "FROM TB_JOGO_TENIS "     +
                    "WHERE "                  +
                    "CD_JOGO   <> ? AND "     + // 1
                    "CD_QUADRA  = ? AND "     + // 2
                    "TS_FIM    >= ? AND "     + // 3
                    "TS_INICIO <= ?";           // 4
        
        SQLObject[] parms = {
            SQLType.INTEGER(this.id),
            SQLType.SMALLINT((short) this.quadra.getId()),
            SQLType.TIMESTAMP(Timestamp.valueOf(this.inicio.toString("yyyy-MM-dd HH:mm:ss"))),
            SQLType.TIMESTAMP(Timestamp.valueOf(this.fim.toString("yyyy-MM-dd HH:mm:ss")))
        };
        
        return (DBUtil.queryCount(sql, parms) > 0);
    }
    
    private void insert(Auditoria audit) {
        String sql = "INSERT INTO TB_JOGO_TENIS (" +
                    "CD_QUADRA, "           + // 1
                    "TS_INICIO, "           + // 2
                    "TS_FIM, "              + // 3
                    "USER_ACESSO_SISTEMA, " + // 4
                    "TS_MARCACAO"           + // 5
                    ") VALUES (?, ?, ?, ?, ?)";

        SQLObject[] parms = {
            SQLType.SMALLINT((short) this.quadra.getId()),                                          // 1
            SQLType.TIMESTAMP(Timestamp.valueOf(this.inicio.toString("yyyy-MM-dd HH:mm:ss"))),      // 2
            SQLType.TIMESTAMP(Timestamp.valueOf(this.fim.toString("yyyy-MM-dd HH:mm:ss"))),         // 3
            SQLType.CHAR(this.usuario),                                                             // 4
            SQLType.TIMESTAMP(Timestamp.valueOf(this.marcacao.toString("yyyy-MM-dd HH:mm:ss.SSS"))) // 5
        };

        this.id = DBUtil.updateGenerateKey(audit, sql, parms);
        JogadorTenis.insert(audit, this);
    }
    
    public void alterarJogadores(Auditoria audit, JogadorTenis... jogadores) {
        if (jogadores.length == 0)
            throw new IllegalArgumentException("Jogadores não informados");

        for (int i = 0; i < jogadores.length; i++) {
            if (jogadores[i] == null)
                throw new NullPointerException("Nenhum jogador pode ser null");
            for (int j = i + 1; j < jogadores.length; j++)
                if (jogadores[i].equals(jogadores[j]))
                    throw new IllegalArgumentException("Não pode haver jogadores repetidos");
        }
        
        if (this.getTipo() == JogoTenis.Tipo.SIMPLES && jogadores.length != 2)
            throw new IllegalArgumentException("Deve ser informado exatamente 2 jogadores");
        
        if (this.getTipo() == JogoTenis.Tipo.DUPLAS && (jogadores.length < 3 || jogadores.length > 4))
            throw new IllegalArgumentException("Deve ser informado de 3 a 4 jogadores");

        for (JogadorTenis j : jogadores)
            if (!j.isLivreExceto(this, DateTime.now()))
                throw new RuntimeException("Jogador " + j.getNome() + " já está participando de outro jogo");

        JogadorTenis.delete(audit, this);
        this.jogadores = Arrays.asList(jogadores);
        JogadorTenis.insert(audit, this);
    }
    
    public void delete(Auditoria audit) {
        String sql = "{CALL SP_EXCLUI_JOGO_TENIS (?)}";
        DBUtil.execute(audit, sql, SQLType.INTEGER(this.id));
    }
}
