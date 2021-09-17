
package techsoft.seguranca;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import java.util.Date;

public class ParametroAuditoria {

    private String parametros = "";
    private static final Logger log = Logger.getLogger(ParametroAuditoria.class.getName());
    
    public ParametroAuditoria() {

    }

    public String getSetParametro(String parm){
        adicionaParametro("'" + parm + "'");
        return parm;
    }
    
    public int getSetParametro(int parm){
        adicionaParametro(String.valueOf(parm));
        return parm;
    }
    
    public long getSetParametro(long parm){
        adicionaParametro(String.valueOf(parm));
        return parm;
    }
    
    public float getSetParametro(float parm){
        adicionaParametro(String.valueOf(parm));
        return parm;
    }
    
    public double getSetParametro(double parm){
        adicionaParametro(String.valueOf(parm));
        return parm;
    }
    
    public boolean getSetParametro(boolean parm){
        adicionaParametro(String.valueOf(parm));
        return parm;
    }

    public Date getSetParametro(Date parm){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        adicionaParametro("'" + sdf.format(parm.getTime()) + "'");
        return parm;
    }
    
    public BigDecimal getSetParametro(BigDecimal parm){
        adicionaParametro(parm.toString());
        return parm;
    }
    
    public void getSetNull(){
        adicionaParametro("NULL");
    }

    public void adicionaParametro(String parm){
        if ("".equals(parametros)){
            parametros = parm;
        }else{
            parametros = parametros + ", " + parm;
        }
    }
    
    public String getParametroFinal(){
        return parametros;
    }
            
    public void limpaParametro(){
        parametros = "";
    }
    
    
}
