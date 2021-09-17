package techsoft.clube;

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
import techsoft.tabelas.InserirException;

public class ParametroSistema {
    
    private String deInstrucao1;
    private String deInstrucao2;
    private String deInstrucao3;
    private String deInstrucao4;
    private String deInstrucao5;
    private String deInstrucao6;
    private String deInstrucao7;
    private String deInstrucaoDCO1;
    private String deInstrucaoDCO2;
    private String deInstrucaoDCO3;
    private String deInstrucaoDCO4;
    private String deInstrucaoDCO5;
    private String deInstrucaoDCO6;
    private String deInstrucaoDCO7;
    private String deInstrucaoDCO8;
    
    private static final Logger log = Logger.getLogger("techsoft.clube.ParametroSistema");
    
    public String getDeInstrucao1() {
        return deInstrucao1;
    }
    public void setDeInstrucao1(String deInstrucao1) {
        this.deInstrucao1 = deInstrucao1;
    }
    public String getDeInstrucao2() {
        return deInstrucao2;
    }
    public void setDeInstrucao2(String deInstrucao2) {
        this.deInstrucao2 = deInstrucao2;
    }
    public String getDeInstrucao3() {
        return deInstrucao3;
    }
    public void setDeInstrucao3(String deInstrucao3) {
        this.deInstrucao3 = deInstrucao3;
    }
    public String getDeInstrucao4() {
        return deInstrucao4;
    }
    public void setDeInstrucao4(String deInstrucao4) {
        this.deInstrucao4 = deInstrucao4;
    }
    public String getDeInstrucao5() {
        return deInstrucao5;
    }
    public void setDeInstrucao5(String deInstrucao5) {
        this.deInstrucao5 = deInstrucao5;
    }
    public String getDeInstrucao6() {
        return deInstrucao6;
    }
    public void setDeInstrucao6(String deInstrucao6) {
        this.deInstrucao6 = deInstrucao6;
    }
    public String getDeInstrucao7() {
        return deInstrucao7;
    }
    public void setDeInstrucao7(String deInstrucao7) {
        this.deInstrucao7 = deInstrucao7;
    }
    public String getDeInstrucaoDCO1() {
        return deInstrucaoDCO1;
    }
    public void setDeInstrucaoDCO1(String deInstrucaoDCO1) {
        this.deInstrucaoDCO1 = deInstrucaoDCO1;
    }
    public String getDeInstrucaoDCO2() {
        return deInstrucaoDCO2;
    }
    public void setDeInstrucaoDCO2(String deInstrucaoDCO2) {
        this.deInstrucaoDCO2 = deInstrucaoDCO2;
    }
    public String getDeInstrucaoDCO3() {
        return deInstrucaoDCO3;
    }
    public void setDeInstrucaoDCO3(String deInstrucaoDCO3) {
        this.deInstrucaoDCO3 = deInstrucaoDCO3;
    }
    public String getDeInstrucaoDCO4() {
        return deInstrucaoDCO4;
    }
    public void setDeInstrucaoDCO4(String deInstrucaoDCO4) {
        this.deInstrucaoDCO4 = deInstrucaoDCO4;
    }
    public String getDeInstrucaoDCO5() {
        return deInstrucaoDCO5;
    }
    public void setDeInstrucaoDCO5(String deInstrucaoDCO5) {
        this.deInstrucaoDCO5 = deInstrucaoDCO5;
    }
    public String getDeInstrucaoDCO6() {
        return deInstrucaoDCO6;
    }
    public void setDeInstrucaoDCO6(String deInstrucaoDCO6) {
        this.deInstrucaoDCO6 = deInstrucaoDCO6;
    }
    public String getDeInstrucaoDCO7() {
        return deInstrucaoDCO7;
    }
    public void setDeInstrucaoDCO7(String deInstrucaoDCO7) {
        this.deInstrucaoDCO7 = deInstrucaoDCO7;
    }
    public String getDeInstrucaoDCO8() {
        return deInstrucaoDCO8;
    }
    public void setDeInstrucaoDCO8(String deInstrucaoDCO8) {
        this.deInstrucaoDCO8 = deInstrucaoDCO8;
    }

    
    public static ParametroSistema getInstance() {
        String sql = "SELECT " +
                        "MSG_INSTR_COBRANCA_1, " +
                        "MSG_INSTR_COBRANCA_2, " +
                        "MSG_INSTR_COBRANCA_3, " +
                        "MSG_INSTR_COBRANCA_4, " +
                        "MSG_INSTR_COBRANCA_5, " +
                        "MSG_INSTR_COBRANCA_6, " +
                        "MSG_INSTR_COBRANCA_7, " +
                        "MSG_INSTR_DCO_1, " +
                        "MSG_INSTR_DCO_2, " +
                        "MSG_INSTR_DCO_3, " +
                        "MSG_INSTR_DCO_4, " +
                        "MSG_INSTR_DCO_5, " +
                        "MSG_INSTR_DCO_6, " +
                        "MSG_INSTR_DCO_7, " +
                        "MSG_INSTR_DCO_8 " +
             "FROM TB_PARAMETRO_SISTEMA ";
        Connection cn = null;
        ParametroSistema i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new ParametroSistema();
                i.setDeInstrucao1(rs.getString("MSG_INSTR_COBRANCA_1"));
                i.setDeInstrucao2(rs.getString("MSG_INSTR_COBRANCA_2"));
                i.setDeInstrucao3(rs.getString("MSG_INSTR_COBRANCA_3"));
                i.setDeInstrucao4(rs.getString("MSG_INSTR_COBRANCA_4"));
                i.setDeInstrucao5(rs.getString("MSG_INSTR_COBRANCA_5"));
                i.setDeInstrucao6(rs.getString("MSG_INSTR_COBRANCA_6"));
                i.setDeInstrucao7(rs.getString("MSG_INSTR_COBRANCA_7"));
                
                i.setDeInstrucaoDCO1(rs.getString("MSG_INSTR_DCO_1"));
                i.setDeInstrucaoDCO2(rs.getString("MSG_INSTR_DCO_2"));
                i.setDeInstrucaoDCO3(rs.getString("MSG_INSTR_DCO_3"));
                i.setDeInstrucaoDCO4(rs.getString("MSG_INSTR_DCO_4"));
                i.setDeInstrucaoDCO5(rs.getString("MSG_INSTR_DCO_5"));
                i.setDeInstrucaoDCO6(rs.getString("MSG_INSTR_DCO_6"));
                i.setDeInstrucaoDCO7(rs.getString("MSG_INSTR_DCO_7"));
                i.setDeInstrucaoDCO8(rs.getString("MSG_INSTR_DCO_8"));
            }
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return i;
    }
        
    public void atualizaParametrosFinanceiros()throws InserirException{
        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "UPDATE TB_PARAMETRO_SISTEMA SET MSG_INSTR_COBRANCA_1 = ?, MSG_INSTR_COBRANCA_2 = ?, MSG_INSTR_COBRANCA_3 = ?, MSG_INSTR_COBRANCA_4 = ?, MSG_INSTR_COBRANCA_5 = ?, MSG_INSTR_COBRANCA_6 = ?, MSG_INSTR_COBRANCA_7 = ?, " +
                         "MSG_INSTR_DCO_1 = ?, MSG_INSTR_DCO_2 = ?, MSG_INSTR_DCO_3 = ?, MSG_INSTR_DCO_4 = ?, MSG_INSTR_DCO_5 = ?, MSG_INSTR_DCO_6 = ?, MSG_INSTR_DCO_7 = ?, MSG_INSTR_DCO_8 = ? ";
            PreparedStatement p = cn.prepareStatement(sql);
            
            p.setString(1, deInstrucao1);
            p.setString(2, deInstrucao2);
            p.setString(3, deInstrucao3);
            p.setString(4, deInstrucao4);
            p.setString(5, deInstrucao5);
            p.setString(6, deInstrucao6);
            p.setString(7, deInstrucao7);
            
            p.setString(8, deInstrucaoDCO1);
            p.setString(9, deInstrucaoDCO2);
            p.setString(10, deInstrucaoDCO3);
            p.setString(11, deInstrucaoDCO4);
            p.setString(12, deInstrucaoDCO5);
            p.setString(13, deInstrucaoDCO6);
            p.setString(14, deInstrucaoDCO7);
            p.setString(15, deInstrucaoDCO8);
            
            p.executeUpdate();
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
    
    public static void atualizaRemessa(int remessa)throws InserirException{
        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "UPDATE TB_PARAMETRO_SISTEMA SET SEQ_REMESSA_DCO = ? ";
            PreparedStatement p = cn.prepareStatement(sql);
            
            p.setInt(1, remessa);
            
            p.executeUpdate();
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
