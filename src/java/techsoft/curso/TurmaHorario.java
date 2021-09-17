package techsoft.curso;

public class TurmaHorario {

    private String horaInicio;
    private String horaFim;

    public TurmaHorario(String horaInicio, String horaFim){
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof TurmaHorario)){
            return false;
        }
        
        if(o == this) return true;
        
        TurmaHorario h = (TurmaHorario)o;
        return (this.horaInicio.equals(h.horaInicio) && this.horaFim.equals(h.horaFim));
    }

    @Override
    public int hashCode() {
        int h1 = (horaInicio == null) ? 0 : horaInicio.hashCode();
        int h2 = (horaFim == null) ? 0 : horaFim.hashCode();
        int hash = h1 + h2;
        
        return (hash == 0) ? super.hashCode() : hash;
    }    
    
}
