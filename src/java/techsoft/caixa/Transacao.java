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
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;
import techsoft.tabelas.InserirException;

public class Transacao {
    private int id;
    private String descricao;
    private float valPadrao;
    private String tipo;
    private int cdTaxa;
    private String deTaxa;
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.Transacao");

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public float getValPadrao() {
        return valPadrao;
    }
    public void setValPadrao(float valPadrao) {
        this.valPadrao = valPadrao;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getCdTaxa() {
        return cdTaxa;
    }
    public void setCdTaxa(int cdTaxa) {
        this.cdTaxa = cdTaxa;
    }
    public String getDeTaxa() {
        return deTaxa;
    }
    public void setDeTaxa(String deTaxa) {
        this.deTaxa = deTaxa;
    }

    public static List<Transacao> listar() {
        String sql = "SELECT T1.CD_TRANSACAO, " +
                            "T1.DESCR_TRANSACAO, " +
                            "T1.VAL_PADRAO, " +
                            "T1.CD_DEBITO_CREDITO, " +
                            "T2.CD_TX_ADMINISTRATIVA, " +
                            "T2.DESCR_TX_ADMINISTRATIVA " +
                     "FROM TB_TRANSACAO T1, " +
                     "     TB_TAXA_ADMINISTRATIVA T2 " +
                     "WHERE T1.CD_TX_ADMINISTRATIVA *= T2.CD_TX_ADMINISTRATIVA " +
                     "AND   T1.CD_DEBITO_CREDITO IN ('C', 'S', 'R', 'F') " +
                     "ORDER BY 2 ";

        Connection cn = null;
        ArrayList<Transacao> l = new ArrayList<Transacao>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Transacao i = new Transacao();
                i.setId(rs.getInt("CD_TRANSACAO"));
                i.setDescricao(rs.getString("DESCR_TRANSACAO"));
                i.setTipo(rs.getString("CD_DEBITO_CREDITO"));
                i.setValPadrao(rs.getFloat("VAL_PADRAO"));
                i.setCdTaxa(rs.getInt("CD_TX_ADMINISTRATIVA"));
                i.setDeTaxa(rs.getString("DESCR_TX_ADMINISTRATIVA"));
                
                l.add(i);
            } 
            cn.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        return l;
    }
    
    
    public void inserir(Auditoria audit)throws InserirException{
 
        Connection cn = null;
        CallableStatement cal = null;

        if(id != 0 || descricao == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "{call SP_INCLUI_TRANSACAO (?,?,?,?)}";
            cal = cn.prepareCall(sql);
            ParametroAuditoria par = new ParametroAuditoria();  
            cal.setString(1, par.getSetParametro(descricao));
            cal.setString(2, par.getSetParametro(tipo));
            cal.setFloat(3, par.getSetParametro(valPadrao));
            if (cdTaxa == 0){
                cal.setNull(4, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(4, par.getSetParametro(cdTaxa));
            }
            
            
            ResultSet rs = cal.executeQuery();
            
            if(rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    id = rs.getInt(2);
                    cn.commit();
                    audit.registrarMudanca(sql, par.getParametroFinal());
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new InserirException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new InserirException(err);
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

    public void alterar(Auditoria audit)throws InserirException, AlterarException{
 
        Connection cn = null;
        CallableStatement cal = null;

        if(descricao == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "{call SP_ALTERAR_TRANSACAO (?,?,?,?,?)}";
            cal = cn.prepareCall(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            cal.setInt(1, par.getSetParametro(id));
            cal.setString(2, par.getSetParametro(descricao));
            cal.setString(3, par.getSetParametro(tipo));
            cal.setFloat(4, par.getSetParametro(valPadrao));
            
            if (cdTaxa == 0){
                cal.setNull(5, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(5, par.getSetParametro(cdTaxa));
            }
            
            ResultSet rs = cal.executeQuery();
            
            if(rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca(sql, par.getParametroFinal());
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new AlterarException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new AlterarException(err);
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
    
    public static Transacao getInstance(int id) {
        String sql = "SELECT CD_TRANSACAO, " +
                            "DESCR_TRANSACAO, " +
                            "VAL_PADRAO, " +
                            "CD_DEBITO_CREDITO, " +
                            "CD_TX_ADMINISTRATIVA " +
                     "FROM TB_TRANSACAO " +
                     " WHERE CD_TRANSACAO = ?";
        Connection cn = null;
        Transacao i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new Transacao();
                i.setId(rs.getInt("CD_TRANSACAO"));
                i.setDescricao(rs.getString("DESCR_TRANSACAO"));
                i.setTipo(rs.getString("CD_DEBITO_CREDITO"));
                i.setValPadrao(rs.getFloat("VAL_PADRAO"));
                i.setCdTaxa(rs.getInt("CD_TX_ADMINISTRATIVA"));
                
                
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
    
}
