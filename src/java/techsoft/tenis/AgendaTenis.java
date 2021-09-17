package techsoft.tenis;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.ReadableInstant;

public class AgendaTenis implements Iterable<EventoQuadraTenis> {
    
    private final Interval intervalo;
    private final NavigableSet<EventoQuadraTenis> eventos;
    
    public AgendaTenis(QuadraTenis quadra) {
        this(quadra, DateTime.now());
    }

    public AgendaTenis(QuadraTenis quadra, ReadableInstant horarioInicio) {
        this.eventos = new ConcurrentSkipListSet<EventoQuadraTenis>(new Comparator<EventoQuadraTenis>() {
            @Override
            public int compare(EventoQuadraTenis e1, EventoQuadraTenis e2) {
                DateTime i1 = e1.intervalo().getStart();
                DateTime i2 = e2.intervalo().getStart();
                if (i1.isBefore(i2)) return -1;
                if (i1.isAfter(i2)) return 1;
                return 0;
            }
        });

        // Monta a agenda
        DateTime inicio = new DateTime(horarioInicio).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime fim = inicio.plusHours(12); // Intervalo padrão definido pelo cliente
        this.intervalo = new Interval(inicio, fim);
        this.add(new EventoQuadraTenis(quadra, this.intervalo, QuadraTenis.Estado.LIVRE, null, null, null, 100));
        
        for (EventoSemanal e : EventoSemanal.listFor(quadra))
            this.addAll(e.toEvento(this.intervalo()));
        
        for (ExcecaoAgendaSemanal e : ExcecaoAgendaSemanal.listFor(quadra, this.intervalo()))
            this.addAll(e.toEvento(this.intervalo()));

        for (JogoTenis j : JogoTenis.listFor(quadra, this.intervalo()))
            this.addAll(j.toEvento(this.intervalo()));
    }

    private void addAll(EventoQuadraTenis[] eventos) {
        for (EventoQuadraTenis e : eventos) this.add(e);
    }

    private void add(EventoQuadraTenis evento) {
        EventoQuadraTenis conflito = null;
        for (EventoQuadraTenis e : this.eventos)
            if (evento.overlaps(e)) {
                conflito = e;
                break;
            }
        if (conflito != null) {
            if (!this.eventos.remove(conflito))
                throw new RuntimeException("Erro ao montar agenda da quadra");
            this.addAll(evento.combine(conflito));
        } else {
            if (this.intervalo.overlaps(evento.intervalo()))
                this.eventos.add(evento);
        }
    }

    public final Interval intervalo() { return new Interval(this.inicio(), this.fim()); }
    public final DateTime inicio() { return this.eventos.first().intervalo().getStart(); }
    public final DateTime fim() { return this.eventos.last().intervalo().getEnd(); }
    
    @Override
    public Iterator<EventoQuadraTenis> iterator() { return this.eventos.iterator(); }

    public EventoQuadraTenis eventoAt(ReadableInstant i) {
        if (this.intervalo().contains(i))
            for (EventoQuadraTenis e : this) if (e.intervalo().contains(i)) return e;
        return null;
    }
    
    public EventoQuadraTenis eventoAnterior(EventoQuadraTenis e) {
        if (!this.eventos.contains(e))
            throw new IllegalArgumentException("Evento não faz parte da agenda");
        return this.eventos.lower(e);
    }
    
    public EventoQuadraTenis eventoPosterior(EventoQuadraTenis e) {
        if (!this.eventos.contains(e))
            throw new IllegalArgumentException("Evento não faz parte da agenda");
        return this.eventos.higher(e);
    }

}
