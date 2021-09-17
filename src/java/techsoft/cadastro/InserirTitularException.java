
package techsoft.cadastro;

public class InserirTitularException extends Exception{

    private static final long serialVersionUID = 10052011L;
    private boolean listaNegra;

    public InserirTitularException(String msg){
        super(msg);
    }

    public InserirTitularException(String msg, boolean listaNegra){
        super(msg);
        this.listaNegra = listaNegra;
    }

    public boolean isListaNegra() {
        return listaNegra;
    }
    
}