
package techsoft.operacoes;

public class ConvenioBBException extends Exception{

    private static final long serialVersionUID = 21032013L;
    private boolean solicitarConfirmacao = false;
    
    public ConvenioBBException(String msg){
        super(msg);
    }
    
    public ConvenioBBException(String msg, boolean solicitarConfirmacao){
        super(msg);
        this.solicitarConfirmacao = solicitarConfirmacao;
    }
    
    public boolean isSolicitarConfirmacao(){
        return this.solicitarConfirmacao;
    }
    
}