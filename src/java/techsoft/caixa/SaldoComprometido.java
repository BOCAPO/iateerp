package techsoft.caixa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import techsoft.db.Pool;

/*
 * No VB essa rotina se chamava
 * Public Function LimiteVrUltrapassadoTxIndCheque(CdMatricula As Long, CdCategoria As Integer, VrNovaTaxa As Currency) As Boolean
 */
public class SaldoComprometido {

    private float carneGerado;
    private float carneProjetado;
    private float carneVencido;
    private float chequePre;
    private float taxaIndFutura;
    private float limiteCategoria;
    private boolean limiteUltrapassado;
    private static final Logger log = Logger.getLogger("techsoft.caixa.SaldoComprometido");
    
    private SaldoComprometido(){}
    
    public static SaldoComprometido verificar(int idCategoria, int matricula, float novaTaxa, String origem, String nome){
        SaldoComprometido s = new SaldoComprometido();
        Connection cn = null;
        String local = "1";
        try {
            float vrTxIndFutura;
            float vrChequePre;
            float vrMaximoTxInd = 0;
        
            cn = Pool.getInstance().getConnection();
            String sql = "SELECT VR_MAXIMO_TX_IND FROM TB_COMPLEMENTO WHERE CD_CATEGORIA = "
            + idCategoria + " AND CD_MATRICULA = " + matricula;
            ResultSet rs = cn.createStatement().executeQuery(sql);
            if (rs.next()){
                vrMaximoTxInd = rs.getFloat(1);
            }
            
            if(vrMaximoTxInd < 1){
                local="2";
                ResultSet rs2 = cn.createStatement().executeQuery(
                        "SELECT VR_MAX_CARNE FROM TB_CATEGORIA WHERE CD_CATEGORIA = " + idCategoria);
                if (rs2.next()){
                    vrMaximoTxInd = rs2.getFloat(1);
                }
                rs2.close();
            }
            rs.close();
            
            if(vrMaximoTxInd < 1){
                s.limiteUltrapassado = true;
                return s;
            }
            
            //'**********************************
            //'Busca as taxas individuais futuras (cuja cobranca é superior a do mes atual)
            //'**********************************
            local="3";
            sql = "EXEC SP_REC_TOTAL_TX_IND_FUTURA " + matricula + ", " + idCategoria;
            rs = cn.createStatement().executeQuery(sql);
            rs.next();
            vrTxIndFutura = rs.getFloat(1);
            rs.close();

            //'**************************
            //'Busca os cheques pendentes (cuja data de depósito é no mes/ano do parametro a data atual)
            //'**************************
            local="4";
            sql = "SELECT "
                     + "ISNULL(SUM(VAL_CHEQUE), 0) "
                   + "FROM "
                     + "TB_CHEQUE_RECEBIDO     T1, "
                     + "TB_MOVIMENTO_CAIXA     T2 "
                   + "WHERE "
                     + "T1.CD_CAIXA       = T2.CD_CAIXA     AND "
                     + "T1.SEQ_ABERTURA   = T2.SEQ_ABERTURA AND "
                     + "T1.SEQ_AUTENTICACAO  = T2.SEQ_AUTENTICACAO   AND "
                     + "T1.DT_MOVIMENTO_CAIXA   = T2.DT_MOVIMENTO_CAIXA AND "
                     + "T2.DT_ESTORNO_MOVIMENTO IS NULL    AND "
                     + "T1.DT_DEPOSITO    >= convert(datetime, convert(varchar, getdate(), 101)) AND "
                     + "T2.CD_MATRICULA      = " + matricula
                     + " AND T2.CD_CATEGORIA   = " + idCategoria;
            rs = cn.createStatement().executeQuery(sql);
            rs.next();
            vrChequePre = rs.getFloat(1);
            rs.close();

            //'******* FIM **********
            //'Verifica se a soma dos débitos é maior que o limite para a categoria
            //'******* FIM **********
            s.chequePre = vrChequePre;
            s.taxaIndFutura = vrTxIndFutura;
            s.limiteCategoria = vrMaximoTxInd;

            if((vrTxIndFutura + vrChequePre + novaTaxa) > vrMaximoTxInd){
                s.limiteUltrapassado = true;
            }else{
                s.limiteUltrapassado = false;
            }            
        } catch (SQLException e) {
            log.severe(e.getMessage()+" - local: "+local+" - mat/cat: "+matricula+"/"+idCategoria+" - Origem: "+origem+" - nome:"+nome);
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        return s;
    }
    
    public float getCarneGerado() {
        return carneGerado;
    }

    public float getCarneProjetado() {
        return carneProjetado;
    }

    public float getCarneVencido() {
        return carneVencido;
    }

    public float getChequePre() {
        return chequePre;
    }

    public float getTaxaIndFutura() {
        return taxaIndFutura;
    }

    public float getLimiteCategoria() {
        return limiteCategoria;
    }

    public boolean isLimiteUltrapassado() {
        return limiteUltrapassado;
    }

    
}
