
package techsoft.curso;

public class CancelarMatriculaException extends Exception{

    private static final long serialVersionUID = 30052013L;
    private boolean existeTaxaCancelamento;

    public CancelarMatriculaException(String msg){
        super(msg);
    }

    public CancelarMatriculaException(String msg, boolean existeTaxaCancelamento){
        super(msg);
        this.existeTaxaCancelamento = existeTaxaCancelamento;
    }
    
    public boolean isExisteTaxaCancelamento() {
        return existeTaxaCancelamento;
    }
    
}