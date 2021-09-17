
package techsoft.cadastro;

public class TransferenciaTituloException extends Exception{

    private static final long serialVersionUID = 24052013L;
    private boolean existeTaxaIndividual;

    public TransferenciaTituloException(String msg){
        super(msg);
    }

    public TransferenciaTituloException(String msg, boolean existeTaxaIndividual){
        super(msg);
        this.existeTaxaIndividual = existeTaxaIndividual;
    }
    
    public boolean isExisteTaxaIndividual() {
        return existeTaxaIndividual;
    }
    
}