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
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;

public class CentroCusto {
    private int id;
    private String descricao;
    private int taxa;
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
    public int getTaxa() {
        return taxa;
    }
    public void setTaxa(int taxa) {
        this.taxa = taxa;
    }
    public String getDeTaxa() {
        return deTaxa;
    }
    public void setDeTaxa(String deTaxa) {
        this.deTaxa = deTaxa;
    }
    
    public static List<CentroCusto> listar() {
        String sql = "SELECT T1.CD_TRANSACAO, " +
                            "T1.DESCR_TRANSACAO, " +
                            "T2.DESCR_TX_ADMINISTRATIVA, " +
                            "T1.CD_TX_ADMINISTRATIVA " +
                     " FROM TB_TRANSACAO  T1, TB_TAXA_ADMINISTRATIVA T2 " +
                     " WHERE T1.CD_TX_ADMINISTRATIVA *= T2.CD_TX_ADMINISTRATIVA AND CD_DEBITO_CREDITO = 'P' " +
                     " ORDER BY 2 ";

        Connection cn = null;
        ArrayList<CentroCusto> l = new ArrayList<CentroCusto>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                CentroCusto i = new CentroCusto();
                i.setId(rs.getInt("CD_TRANSACAO"));
                i.setDescricao(rs.getString("DESCR_TRANSACAO"));
                i.setTaxa(rs.getInt("CD_TX_ADMINISTRATIVA"));
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
    
    public static CentroCusto getInstance(int id) {
        String sql = "SELECT CD_TRANSACAO, " +
                            "DESCR_TRANSACAO, " +
                            "CD_TX_ADMINISTRATIVA " +
                     "FROM TB_TRANSACAO " +
                     "WHERE CD_TRANSACAO = ?";
        Connection cn = null;
        CentroCusto i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new CentroCusto();
                i.setId(rs.getInt("CD_TRANSACAO"));
                i.setDescricao(rs.getString("DESCR_TRANSACAO"));
                i.setTaxa(rs.getInt("CD_TX_ADMINISTRATIVA"));
                
                
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
        
    
    public void inserir(Auditoria audit)throws InserirException{
 
        ResultSet rs = null;
        Connection cn = null;
        
        if(id != 0 || descricao == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            String sql = "INSERT INTO TB_TRANSACAO VALUES(?, 'P', 0, ?)";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, par.getSetParametro(descricao));
            if (taxa==0){
                p.setNull(2, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(2, par.getSetParametro(taxa));
            }
            
            p.executeUpdate();
            rs = p.getGeneratedKeys();

            if(rs.next()){
                id = rs.getInt(1);
                cn.commit();
                audit.registrarMudanca(sql, par.getParametroFinal());
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

    public void alterar(Auditoria audit)throws InserirException{
 
        ResultSet rs = null;
        Connection cn = null;
        
        if(descricao == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            String sql = "UPDATE TB_TRANSACAO SET DESCR_TRANSACAO = ?, CD_TX_ADMINISTRATIVA = ? WHERE CD_TRANSACAO = ? ";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            p.setString(1, par.getSetParametro(descricao));
            if (taxa==0){
                p.setNull(2, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(2, par.getSetParametro(taxa));
            }
            p.setInt(3, par.getSetParametro(id));
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());

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
    
    public static void atualizarUsuarios(int id, String usuarios){
        String sql = "DELETE TB_USUARIO_CENTRO_CUSTO WHERE CD_TRANSACAO = ? ";
       
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            p.executeUpdate();
            
            String[] vetUsuarios = usuarios.split(";");

            sql = "INSERT INTO TB_USUARIO_CENTRO_CUSTO VALUES(?, ?)";
            p = cn.prepareStatement(sql);
            for(int i =0; i < vetUsuarios.length ; i++){
                p.setString(1, vetUsuarios[i]);
                p.setInt(2, id);
                p.executeUpdate();
            }

            cn.commit();
            
        } catch (Exception e) {
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
