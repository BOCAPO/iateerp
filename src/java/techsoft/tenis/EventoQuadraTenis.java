package techsoft.tenis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.Interval;

public final class EventoQuadraTenis implements Comparable<EventoQuadraTenis> {
    private final QuadraTenis quadra;
    private final Interval intervalo;
    private final QuadraTenis.Estado estado;
    private final JogoTenis.Tipo restricao;
    private final String observacao;
    private final int prioridade;
    private final Object origem;
    private final JogadorTenis[] jogadores;

    public EventoQuadraTenis(QuadraTenis quadra, Interval intervalo, QuadraTenis.Estado estado, JogoTenis.Tipo restricao, String observacao, Object origem, int prioridade, JogadorTenis... jogadores) {
        if (quadra == null) throw new IllegalArgumentException("Quadra não pode ser null");
        if (intervalo == null) throw new IllegalArgumentException("Intervalo não pode ser null");
        if (estado == null) throw new IllegalArgumentException("Tipo não pode ser null");

        this.quadra = quadra;
        this.intervalo = intervalo;
        this.estado = estado;
        this.restricao = restricao;
        this.observacao = observacao;
        this.prioridade = prioridade;
        this.origem = origem;
        this.jogadores = jogadores;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof EventoQuadraTenis)) return false;
        EventoQuadraTenis e = (EventoQuadraTenis) o;
        if (!ObjectUtils.equals(this.quadra, e.quadra)) return false;
        if (!ObjectUtils.equals(this.intervalo, e.intervalo)) return false;
        if (!ObjectUtils.equals(this.estado, e.estado)) return false;    
        if (!ObjectUtils.equals(this.restricao, e.restricao)) return false;
        if (!ObjectUtils.equals(this.observacao, e.observacao)) return false;
        if (!ObjectUtils.equals(this.origem, e.origem)) return false;
        if (!Arrays.equals(this.jogadores, e.jogadores)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.quadra != null ? this.quadra.hashCode() : 0);
        hash = 97 * hash + (this.intervalo != null ? this.intervalo.hashCode() : 0);
        hash = 97 * hash + (this.estado != null ? this.estado.hashCode() : 0);
        hash = 97 * hash + (this.restricao != null ? this.restricao.hashCode() : 0);
        hash = 97 * hash + (this.observacao != null ? this.observacao.hashCode() : 0);
        hash = 97 * hash + (this.origem != null ? this.origem.hashCode() : 0);
        hash = 97 * hash + Arrays.deepHashCode(this.jogadores);
        return hash;
    }

    @Override
    public int compareTo(EventoQuadraTenis evento) {
        if (this.intervalo.getStart().isEqual(evento.intervalo.getStart())) 
            return this.intervalo.getEnd().compareTo(evento.intervalo.getEnd());
        else
            return this.intervalo.getStart().compareTo(evento.intervalo.getStart());
    }
    
    public QuadraTenis getQuadra() { return this.quadra; }
    public QuadraTenis.Estado getEstadoQuadra() { return this.estado; }
    public JogoTenis.Tipo getRestricaoJogo() { return this.restricao; }
    public String getObservacao() { return this.observacao; }
    public Object getOrigem() { return this.origem; }
    public JogadorTenis[] getJogadores() { return this.jogadores.clone(); }
    
    public Interval intervalo() { return this.intervalo; }
    public DateTime inicio() { return this.intervalo.getStart(); }
    public DateTime fim() { return this.intervalo.getEnd(); }
    
    public boolean isBefore(EventoQuadraTenis evento) { return this.intervalo.isBefore(evento.intervalo); }
    public boolean isAfter(EventoQuadraTenis evento) { return this.intervalo.isAfter(evento.intervalo); }
    public boolean overlaps(EventoQuadraTenis evento) { return this.intervalo.overlaps(evento.intervalo); }
    public boolean abuts(EventoQuadraTenis evento) { return this.intervalo.abuts(evento.intervalo); }
    public boolean happensAt(Instant instant) { return this.intervalo.contains(instant); }
    
    public EventoQuadraTenis[] combine(EventoQuadraTenis evento) {
        List<EventoQuadraTenis> l = new ArrayList<EventoQuadraTenis>();
        
        if (this.intervalo.overlaps(evento.intervalo)) {
            EventoQuadraTenis a, b;
            if (this.prioridade < evento.prioridade) {
                a = this;
                b = evento;
            } else {
                a = evento;
                b = this;
            }
            
            if (a.origem instanceof JogoTenis) // Jogo herda a observação
                a = new EventoQuadraTenis(a.quadra, a.intervalo, a.estado, a.restricao, b.observacao, a.origem, a.prioridade, a.jogadores);
            
            l.add(a);
            if (a.intervalo.getStart().isAfter(b.intervalo.getStart())) {
                Interval i = new Interval(b.intervalo.getStart(), a.intervalo.getStart());
                l.add(new EventoQuadraTenis(b.quadra, i, b.estado, b.restricao, b.observacao, b.origem, b.prioridade, b.jogadores));
            }
            if (a.intervalo.getEnd().isBefore(b.intervalo.getEnd())) {
                Interval i = new Interval(a.intervalo.getEnd(), b.intervalo.getEnd());
                l.add(new EventoQuadraTenis(b.quadra, i, b.estado, b.restricao, b.observacao, b.origem, b.prioridade, b.jogadores));
            }
        } else {
            l.add(this);
            l.add(evento);
        }        
        
        Collections.sort(l);
        return l.toArray(new EventoQuadraTenis[l.size()]);
    }
    
}
