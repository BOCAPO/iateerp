
package techsoft.cadastro;

public class ExcluirTitularException extends Exception{

    private static final long serialVersionUID = 19052011L;
    private boolean existeTaxaIndividual;

    public ExcluirTitularException(String msg){
        super(msg);
    }

    public ExcluirTitularException(String msg, boolean existeTaxaIndividual){
        super(msg);
        this.existeTaxaIndividual = existeTaxaIndividual;
    }
    
    public boolean isExisteTaxaIndividual() {
        return existeTaxaIndividual;
    }
    
}