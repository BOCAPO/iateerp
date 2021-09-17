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
import techsoft.tabelas.InserirException;
import java.util.Date;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.util.Datas;

public class TipoEventoAcesso {
    private int id;
    private String descricao;
    private String situacao;
    private String status;
    private Date dtInicio;
    private Date dtFim;

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
    public String getSituacao() {
        return situacao;
    }
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getDtInicio() {
        return dtInicio;
    }
    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }
    public Date getDtFim() {
        return dtFim;
    }
    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }

    public static List<TipoEventoAcesso> listar() {
        String sql = "SELECT CD_TP_EVENTO_ACESSO, " + 
                            "DESCR_TP_EVENTO_ACESSO, " + 
                            "CASE WHEN CD_SIT_EVENTO = 'AL' THEN 'ALERTA' WHEN CD_SIT_EVENTO = 'PA' THEN 'PROÍBE ACESSO' ELSE 'PROÍBE ACESSO PISCINA' END CD_SIT_EVENTO, " + 
                            "CASE WHEN CD_STATUS_EVENTO_ACESSO = 'AT' THEN 'ATIVO' ELSE 'CANCELADO' END CD_STATUS_EVENTO_ACESSO, " + 
                            "DT_INICIO_VIGENCIA,  " + 
                            "DT_FIM_VIGENCIA 	 " + 
                     "FROM  " + 
                             "TB_TIPO_EVENTO_ACESSO " + 
                     "ORDER BY " + 
                             "2 ";

        Connection cn = null;
        ArrayList<TipoEventoAcesso> l = new ArrayList<TipoEventoAcesso>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                TipoEventoAcesso i = new TipoEventoAcesso();
                i.setId(rs.getInt("CD_TP_EVENTO_ACESSO"));
                i.setDescricao(rs.getString("DESCR_TP_EVENTO_ACESSO"));
                i.setSituacao(rs.getString("CD_SIT_EVENTO"));
                i.setStatus(rs.getString("CD_STATUS_EVENTO_ACESSO"));
                i.setDtInicio(rs.getDate("DT_INICIO_VIGENCIA"));
                i.setDtFim(rs.getDate("DT_FIM_VIGENCIA"));
                
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
        
        if(getId() != 0 || getDescricao() == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "{call SP_INCLUI_TIPO_EVENTO_ACESSO (?,?,?,?,?)}";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ParametroAuditoria par= new ParametroAuditoria();
            p.setString(1, par.getSetParametro(descricao));
            p.setString(2, par.getSetParametro(situacao));
            p.setString(3, par.getSetParametro(status));
            p.setDate(4, new java.sql.Date(par.getSetParametro(dtInicio).getTime()));
            p.setDate(5, new java.sql.Date(par.getSetParametro(dtFim).getTime()));
            
            p.executeUpdate();
            rs = p.getGeneratedKeys();

            if(rs.next()){
                setId(rs.getInt(1));
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
        
        if(getDescricao() == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "{call SP_ALTERA_TIPO_EVENTO_ACESSO (?,?,?,?,?,?)}";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ParametroAuditoria par= new ParametroAuditoria();

            p.setInt(1, par.getSetParametro(id));
            p.setString(2, par.getSetParametro(descricao));
            p.setString(3, par.getSetParametro(situacao));
            p.setString(4, par.getSetParametro(status));
            p.setDate(5, new java.sql.Date(par.getSetParametro(dtInicio).getTime()));
            p.setDate(6, new java.sql.Date(par.getSetParametro(dtFim).getTime()));

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
    
    public static TipoEventoAcesso getInstance(int id) {
        String sql = "SELECT * FROM TB_TIPO_EVENTO_ACESSO WHERE CD_TP_EVENTO_ACESSO = ?";
        Connection cn = null;
        TipoEventoAcesso i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new TipoEventoAcesso();
                i.id = rs.getInt("CD_TP_EVENTO_ACESSO");
                i.setDescricao(rs.getString("DESCR_TP_EVENTO_ACESSO"));
                i.setSituacao(rs.getString("CD_SIT_EVENTO"));
                i.setStatus(rs.getString("CD_STATUS_EVENTO_ACESSO"));
                i.setDtInicio(rs.getDate("DT_INICIO_VIGENCIA"));
                i.setDtFim(rs.getDate("DT_FIM_VIGENCIA"));
                
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
        String sql = "DELETE FROM TB_TIPO_EVENTO_ACESSO WHERE CD_TP_EVENTO_ACESSO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, this.getId());
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, "" + this.getId());
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
