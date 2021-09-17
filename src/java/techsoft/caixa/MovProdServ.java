package techsoft.caixa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import techsoft.tabelas.InserirException;
import techsoft.util.Datas;

public class MovProdServ {
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.MovProdServ");
    
    public static int validaMovimento(String estacao) {
        String msg = "";
        Connection cn = null;
        int centroCusto = 0;
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall("{call SP_VERIFICA_CENTRO_CUSTO (?)}", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            cal.setString(1, estacao);
            ResultSet rs = cal.executeQuery();
            
            if (rs.next()){  
                centroCusto = rs.getInt("CD_TRANSACAO");
            } 
            
            rs.close();
            
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return centroCusto;
    }      
    
    public static void atualiza(int idCentroCusto, String devMovimento, int cdPendencia, String nome)throws InserirException{

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "";
            if (cdPendencia==0){
                sql = "INSERT INTO TB_RECEBIMENTO_PENDENTE (NO_REFERENCIA, CD_TRANSACAO) VALUES (?, ?)";
                PreparedStatement p = cn.prepareStatement(sql);
                p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                p.setString(1, nome.toUpperCase());
                p.setInt(2, idCentroCusto);
                p.executeUpdate();
                ResultSet rs = p.getGeneratedKeys();

                if(rs.next()){
                    cdPendencia = rs.getInt(1);
                    cn.commit();
                }
                
            }else{
                sql = "DELETE TB_DET_RECEBIMENTO_PENDENTE WHERE CD_RECEBIMENTO = ?";
                PreparedStatement p = cn.prepareStatement(sql);
                p = cn.prepareStatement(sql);
                p.setInt(1, cdPendencia);
                p.executeUpdate();

                cn.commit();
            }
            
            if (!"".equals(devMovimento)){
                String[] vetMovimento = devMovimento.split(";");
                String[] vetDet;

                for(int i =0; i < vetMovimento.length ; i++){
                    vetDet = vetMovimento[i].split("#");

                    sql = "INSERT INTO TB_DET_RECEBIMENTO_PENDENTE (CD_RECEBIMENTO, CD_PRODUTO_SERVICO, QT_PRODUTO_SERVICO, DT_LANCAMENTO) VALUES(?,?,?,?) ";
                    PreparedStatement p = cn.prepareStatement(sql);
                    p = cn.prepareStatement(sql);
                    p.setInt(1, cdPendencia);
                    p.setInt(2, Integer.parseInt(vetDet[1]));
                    p.setInt(3, Integer.parseInt(vetDet[2]));
                    p.setDate(4, new java.sql.Date(Datas.parse(vetDet[0]).getTime()));

                    p.executeUpdate();

                    cn.commit();
                }
            }
                

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }
    
    public static void exclui(int cdPendencia)throws InserirException{

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_DELETA_REC_PENDENTE (?)}");
            cal.setInt(1, cdPendencia);
            cal.executeUpdate();

            cn.commit();
                

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }
}
