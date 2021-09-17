
package techsoft.cadastro;

public class ExcluirDependenteException extends Exception{

    private static final long serialVersionUID = 19052011L;
    private boolean existeTaxaIndividual;

    public ExcluirDependenteException(String msg){
        super(msg);
    }

    public ExcluirDependenteException(String msg, boolean existeTaxaIndividual){
        super(msg);
        this.existeTaxaIndividual = existeTaxaIndividual;
    }
    
    public boolean isExisteTaxaIndividual() {
        return existeTaxaIndividual;
    }
    
}