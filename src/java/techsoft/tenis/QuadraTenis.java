package techsoft.tenis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.ReadableInstant;
import techsoft.db.DBUtil;
import techsoft.db.SQLObject;
import techsoft.db.SQLType;
import techsoft.seguranca.Auditoria;

public class QuadraTenis {
                                    // CREATE TABLE TB_QUADRA_TENIS                                 -- QUADRA DE TÊNIS
    private short  id;              // CD_QUADRA            SMALLINT    NOT NULL PRIMARY KEY IDENTITY, -- CÓDIGO DA QUADRA DE TÊNIS
    private String nome;            // NM_QUADRA            VARCHAR(15) NOT NULL,                      -- NOME DA QUADRA DE TÊNIS
    private short  duracaoSimples;  // QT_MINUTO_SIMPLES    SMALLINT    NOT NULL,                      -- QUANTIDADE DE MINUTOS PARA JOGOS SIMPLES
    private short  duracaoDuplas;   // QT_MINUTO_DUPLAS     SMALLINT    NOT NULL,                      -- QUANTIDADE DE MINUTOS PARA JOGOS DE DUPLAS
    private short  duracaoMarcacao; // QT_SEGUNDO_MARCACAO  SMALLINT    NOT NULL                       -- QUANTIDADE DE SEGUNDOS DISPONÍVEIS DURANTE A MARCAÇÃO DE UM JOGO
    
    private AgendaTenis agenda;
    
    public enum Estado {
        LIVRE,
        OCUPADA,
        BLOQUEADA;
    }
    
    public int getId() { return this.id; }
    public String getNome() { return this.nome; }
    public int getDuracaoSimples() { return this.duracaoSimples; }
    public int getDuracaoDuplas() { return this.duracaoDuplas; }
    public int getDuracaoMarcacao() { return this.duracaoMarcacao; }
    public EventoSemanal[] getAgendaSemanal() { return EventoSemanal.listFor(this); }
    public ExcecaoAgendaSemanal[] getAgendaExcecoes() { return ExcecaoAgendaSemanal.listFor(this); }
    
    public AgendaTenis getAgenda() {
        if (this.agenda == null) this.atualizaAgenda();
        return this.agenda;
    }

    public QuadraTenis atualizaAgenda() {
        this.agenda = new AgendaTenis(this);
        return this; // Fluent interface
    }
    
    public QuadraTenis ajustaAgenda(DateTime horarioInicial) {
        this.agenda = new AgendaTenis(this, horarioInicial);
        return this; // Fluent interface
    }
    
    public boolean podeMarcarJogo() { return podeMarcarJogo(DateTime.now()); }
    
    public boolean podeMarcarJogo(ReadableInstant instanteMarcacao) {
        if (this.agenda == null) this.atualizaAgenda();

        if (!this.agenda.intervalo().contains(instanteMarcacao)) return false;
        
        EventoQuadraTenis livre = this.eventoLivre(instanteMarcacao);
        if (livre == null) return false;
        
        EventoQuadraTenis anterior = this.agenda.eventoAnterior(livre);
        if (anterior == null || anterior.getEstadoQuadra() != QuadraTenis.Estado.BLOQUEADA) return true;
        
        Short minutos = null;
        if (anterior.getOrigem() instanceof EventoSemanal) minutos = ((EventoSemanal) anterior.getOrigem()).getMinutosMarcacao();
        if (anterior.getOrigem() instanceof ExcecaoAgendaSemanal) minutos = ((ExcecaoAgendaSemanal) anterior.getOrigem()).getMinutosMarcacao();
        if (minutos == null) return true;

        DateTime horarioPermitido = anterior.fim().plusMinutes(-1 * minutos).plusMillis(-1);
        if (instanteMarcacao.isAfter(horarioPermitido)) return true;
        else return false;
    }
    
    public Interval previsaoJogo(JogoTenis.Tipo tipoJogo) {
        return this.previsaoJogo(tipoJogo, DateTime.now());
    }
    
    public Interval previsaoJogo(JogoTenis.Tipo tipoJogo, ReadableInstant instanteMarcacao) {
        return JogoTenis.previsao(this, tipoJogo, instanteMarcacao);
    }
    
    public JogoTenis marcarJogo(Collection<JogadorTenis> jogadores) {
        return this.marcarJogo(jogadores.toArray(new JogadorTenis[jogadores.size()]));
    }
    
    public JogoTenis marcarJogo(JogadorTenis... jogadores) {
        JogoTenis j = JogoTenis.marcar(this, jogadores);
        if (j != null) this.atualizaAgenda();
        return j;
    }
    
    public JogoTenis marcarJogo(Auditoria audit, Collection<JogadorTenis> jogadores) {
        return this.marcarJogo(audit, jogadores.toArray(new JogadorTenis[jogadores.size()]));
    }
    
    public JogoTenis marcarJogo(Auditoria audit, JogadorTenis... jogadores) {
        JogoTenis j = JogoTenis.marcar(audit, this, jogadores);
        if (j != null) this.atualizaAgenda();
        return j;
    }
    
    public EventoQuadraTenis eventoAt(ReadableInstant instante) {
        if (this.agenda == null) this.atualizaAgenda();
        AgendaTenis a = (this.agenda.intervalo().contains(instante)) ? this.agenda : new AgendaTenis(this, instante);
        return a.eventoAt(instante);
    }
    
    public EventoQuadraTenis eventoLivre() {
        return this.eventoLivre(DateTime.now());
    }
    
    public EventoQuadraTenis eventoLivre(ReadableInstant instanteInicial) {
        if (this.agenda == null) this.atualizaAgenda();
        for (EventoQuadraTenis e : this.agenda)
            if (!e.fim().isBefore(instanteInicial) && e.getEstadoQuadra() == QuadraTenis.Estado.LIVRE) return e;
        return null;
    }
    
    public QuadraTenis setNome(String nome) {
        this.nome = String.format("%1$-15s", StringUtils.trimToEmpty(nome));
        return this; // Fluent interface
    }
    
    public QuadraTenis setMinutosSimples(int minutosSimples) {
        if ((minutosSimples >= 0) && (minutosSimples <= Short.MAX_VALUE)) this.duracaoSimples = (short) minutosSimples;
        else throw new IllegalArgumentException("Invalid value for minutosSimples");
        return this; // Fluent interface
    }

    public QuadraTenis setMinutosDuplas(int minutosDuplas) {
        if ((minutosDuplas >= 0) && (minutosDuplas <= Short.MAX_VALUE)) this.duracaoDuplas = (short) minutosDuplas;
        else throw new IllegalArgumentException("Invalid value for minutosDuplas");
        return this; // Fluent interface
    }
    
    public QuadraTenis setSegundosMarcacao(int segundosMarcacao) {
        if ((segundosMarcacao >= 0) && (segundosMarcacao <= Short.MAX_VALUE)) this.duracaoMarcacao = (short) segundosMarcacao;
        else throw new IllegalArgumentException("Invalid value for segundosMarcacao");
        return this; // Fluent interface
    }
    
    @Override
    public boolean equals(Object o) {
        if (this.id == 0) return false;
        if (o == this) return true;
        if (!(o instanceof QuadraTenis)) return false;
        return (this.id == ((QuadraTenis) o).id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        return hash;
    }

    private static final DBUtil.ObjectFactory<QuadraTenis> builder = new DBUtil.ObjectFactory<QuadraTenis>() {
        @Override
        public QuadraTenis buildFrom(ResultSet rs) throws SQLException {
            QuadraTenis q = new QuadraTenis();
            q.id              = rs.getShort ("CD_QUADRA");
            q.nome            = rs.getString("NM_QUADRA");
            q.duracaoSimples  = rs.getShort ("QT_MINUTO_SIMPLES");
            q.duracaoDuplas   = rs.getShort ("QT_MINUTO_DUPLAS");
            q.duracaoMarcacao = rs.getShort ("QT_SEGUNDO_MARCACAO");
            return q;
        }
    };
    
    public static QuadraTenis getInstance(int id) {
        String sql = "SELECT * FROM TB_QUADRA_TENIS WHERE CD_QUADRA = ?";
        return DBUtil.queryObject(builder, sql, SQLType.SMALLINT((short) id));
    }
    
    public static List<QuadraTenis> listAll() {
        String sql = "SELECT * FROM TB_QUADRA_TENIS ORDER BY NM_QUADRA";
        return DBUtil.queryList(builder, sql);
    }
    
    public QuadraTenis save(Auditoria audit) {
        if (this.nome == null) throw new IllegalStateException("Nome indefinido");
        
        if (this.id == 0) this.insert(audit);
        else this.update(audit);
        
        return this; // Fluent interface
    }
    
    private void insert(Auditoria audit) {
        String sql = "INSERT INTO TB_QUADRA_TENIS (" +
                    "NM_QUADRA, "         + // 1
                    "QT_MINUTO_SIMPLES, " + // 2
                    "QT_MINUTO_DUPLAS, "  + // 3
                    "QT_SEGUNDO_MARCACAO" + // 4
                    ") VALUES (? , ? , ?, ?)";
        
        SQLObject[] parms = {
            SQLType.VARCHAR(this.nome),             // 1
            SQLType.SMALLINT(this.duracaoSimples),  // 2
            SQLType.SMALLINT(this.duracaoDuplas),   // 3
            SQLType.SMALLINT(this.duracaoMarcacao) // 4
        };
        
        this.id = (short) DBUtil.updateGenerateKey(audit, sql, parms);
    }
    
    private void update(Auditoria audit) {
        String sql = "UPDATE TB_QUADRA_TENIS SET " +
                    "NM_QUADRA = ?, "              + // 1
                    "QT_MINUTO_SIMPLES = ?, "      + // 2
                    "QT_MINUTO_DUPLAS = ?, "       + // 3
                    "QT_SEGUNDO_MARCACAO = ? "     + // 4
                    "WHERE "                       +
                    "CD_QUADRA = ? ";                // 5
        
        SQLObject[] parms = {
            SQLType.VARCHAR(this.nome),              // 1
            SQLType.SMALLINT(this.duracaoSimples),   // 2
            SQLType.SMALLINT(this.duracaoDuplas),    // 3
            SQLType.SMALLINT(this.duracaoMarcacao), // 4
            SQLType.SMALLINT(this.id)                // 5
        };
        
        DBUtil.update(audit, sql, parms);
    }
    
    public QuadraTenis delete(Auditoria audit) {
        if (this.id == 0) return this; // Fluent interface
        
        String[] sqls = {
            "DELETE FROM TB_QUADRA_TENIS WHERE CD_QUADRA = ?",
            "DELETE FROM TB_AGENDA_SEMANAL_TENIS WHERE CD_QUADRA = ?",
            "DELETE FROM TB_EXCECAO_AGENDA_TENIS WHERE CD_QUADRA = ?",
            "DELETE FROM TB_JOGADOR_TENIS WHERE CD_JOGO IN (SELECT CD_JOGO FROM TB_JOGO_TENIS WHERE CD_QUADRA = ?)",
            "DELETE FROM TB_JOGO_TENIS WHERE CD_QUADRA = ?"
        };
        for (String sql : sqls)
            DBUtil.update(audit, sql, SQLType.SMALLINT(this.id));
        
        this.id = 0;
        this.agenda = null;
        
        return this; // Fluent interface
    }

}
