package techsoft.tabelas;

import java.io.Serializable;

public class FuncionarioHorario implements Serializable{
    
    private String entrada;
    private String saida;

    public FuncionarioHorario(String entrada, String saida) {
        this.entrada = entrada;
        this.saida = saida;
    }
    
    public String getEntrada() {
        return entrada; 
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSaida() {
        return saida;
    }

    public void setSaida(String saida) {
        this.saida = saida;
    }
    
    
}
