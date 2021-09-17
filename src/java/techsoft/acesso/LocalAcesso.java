package techsoft.acesso;

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

public class LocalAcesso {
    private int id;
    private String descricao;
    private String estacao;
    private String requerExame;
    private String convUtil;
    private String soCarro;
    private String motraPlaca;
    private String mostraQuantidade;

    private static final Logger log = Logger.getLogger("techsoft.acesso.LocalAcesso");

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
    public String getEstacao() {
        return estacao;
    }
    public void setEstacao(String estacao) {
        this.estacao = estacao;
    }
    public String getRequerExame() {
        return requerExame;
    }
    public void setRequerExame(String requerExame) {
        this.requerExame = requerExame;
    }
    public String getConvUtil() {
        return convUtil;
    }
    public void setConvUtil(String convUtil) {
        this.convUtil = convUtil;
    }
    public String getSoCarro() {
        return soCarro;
    }
    public void setSoCarro(String soCarro) {
        this.soCarro = soCarro;
    }
    public String getMotraPlaca() {
        return motraPlaca;
    }
    public void setMotraPlaca(String motraPlaca) {
        this.motraPlaca = motraPlaca;
    }
    public String getMostraQuantidade() {
        return mostraQuantidade;
    }
    public void setMostraQuantidade(String mostraQuantidade) {
        this.mostraQuantidade = mostraQuantidade;
    }

    public static List<LocalAcesso> listar() {
        String sql = "SELECT DESCR_LOCAL_ACESSO, " +
                            "CD_LOCAL_ACESSO, " +
                            "CASE WHEN REQUER_EXAME_MEDICO = 'S' THEN 'SIM' ELSE 'NÃO' END REQUER_EXAME_MEDICO, " +
                            "CASE WHEN PERMITE_ACESSO_CONVITE_UTILIZA = 'S' THEN 'SIM' ELSE 'NÃO' END PERMITE_ACESSO_CONVITE_UTILIZA, " +
                            "CASE WHEN CD_TIPO_ACESSO = 'S' THEN 'SIM' ELSE 'NÃO' END CD_TIPO_ACESSO, " +
                            "CASE WHEN IC_MOSTRA_PLACA = 'S' THEN 'SIM' ELSE 'NÃO' END IC_MOSTRA_PLACA, " +
                            "CASE WHEN IC_MOSTRA_QUANTIDADE = 'S' THEN 'SIM' ELSE 'NÃO' END IC_MOSTRA_QUANTIDADE, " +
                            "ED_ESTACAO " +
                     "FROM TB_LOCAL_ACESSO ";

        Connection cn = null;
        ArrayList<LocalAcesso> l = new ArrayList<LocalAcesso>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                LocalAcesso i = new LocalAcesso();
                i.setId(rs.getInt("CD_LOCAL_ACESSO"));
                i.setDescricao(rs.getString("DESCR_LOCAL_ACESSO"));
                i.setEstacao(rs.getString("ED_ESTACAO"));
                i.setRequerExame(rs.getString("REQUER_EXAME_MEDICO"));
                i.setConvUtil(rs.getString("PERMITE_ACESSO_CONVITE_UTILIZA"));
                i.setSoCarro(rs.getString("CD_TIPO_ACESSO"));
                i.setMotraPlaca(rs.getString("IC_MOSTRA_PLACA"));
                i.setMostraQuantidade(rs.getString("IC_MOSTRA_QUANTIDADE"));
                
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
 
        ResultSet rs = null;
        Connection cn = null;
        
        if(id != 0 || descricao == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "{call SP_INCLUI_ALTERA_LOCAL_ACESSO (null,?,?,?,?,?,?,?)}";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ParametroAuditoria par = new ParametroAuditoria();
            
            p.setString(1, par.getSetParametro(descricao));
            p.setString(2, par.getSetParametro(requerExame));
            p.setString(3, par.getSetParametro(convUtil));
            p.setString(4, par.getSetParametro(soCarro));
            p.setString(5, par.getSetParametro(motraPlaca));
            p.setString(6, par.getSetParametro(mostraQuantidade));
            if (estacao == ""){
                p.setNull(7, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(7, par.getSetParametro(estacao));
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
            String sql = "{call SP_INCLUI_ALTERA_LOCAL_ACESSO (?,?,?,?,?,?,?,?)}";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setInt(1, par.getSetParametro(id));
            p.setString(2, par.getSetParametro(descricao));
            String teste = par.getSetParametro(requerExame.substring(0,1));
            p.setString(3, par.getSetParametro(requerExame.substring(0,1)));
            p.setString(4, par.getSetParametro(convUtil.substring(0,1)));
            p.setString(5, par.getSetParametro(soCarro.substring(0,1)));
            p.setString(6, par.getSetParametro(motraPlaca.substring(0,1)));
            p.setString(7, par.getSetParametro(mostraQuantidade.substring(0,1)));
            if (estacao == ""){
                p.setNull(8, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(8, par.getSetParametro(estacao));
            }
            
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
    
    public static LocalAcesso getInstance(int id) {
        String sql = "SELECT * FROM TB_LOCAL_ACESSO WHERE CD_LOCAL_ACESSO = ?";
        Connection cn = null;
        LocalAcesso i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new LocalAcesso();
                i.id = rs.getInt("CD_LOCAL_ACESSO");
                i.setDescricao(rs.getString("DESCR_LOCAL_ACESSO"));
                i.setEstacao(rs.getString("ED_ESTACAO"));
                i.setRequerExame(rs.getString("REQUER_EXAME_MEDICO"));
                i.setConvUtil(rs.getString("PERMITE_ACESSO_CONVITE_UTILIZA"));
                i.setSoCarro(rs.getString("CD_TIPO_ACESSO"));
                i.setMotraPlaca(rs.getString("IC_MOSTRA_PLACA"));
                i.setMostraQuantidade(rs.getString("IC_MOSTRA_QUANTIDADE"));
                
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

    
    public void excluir(Auditoria audit) {
        String sql = "DELETE FROM TB_LOCAL_ACESSO WHERE CD_LOCAL_ACESSO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, this.id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, "" + this.id);
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }
}
