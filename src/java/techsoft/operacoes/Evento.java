package techsoft.operacoes;

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
import java.util.Date;
import techsoft.acesso.LocalAcesso;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;

public class Evento {
    private int id;
    private String descricao;
    private String animacao;
    private String local;
    private Date data;
    private String hora;
    private int qtMesas;
    private int qtCadeiras;
    private int qtIngressos;

    private static final Logger log = Logger.getLogger("techsoft.operacoes.Evento");

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
    public String getAnimacao() {
        return animacao;
    }
    public void setAnimacao(String animacao) {
        this.animacao = animacao;
    }
    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }
    public int getQtMesas() {
        return qtMesas;
    }
    public void setQtMesas(int qtMesas) {
        this.qtMesas = qtMesas;
    }
    public int getQtCadeiras() {
        return qtCadeiras;
    }
    public void setQtCadeiras(int qtCadeiras) {
        this.qtCadeiras = qtCadeiras;
    }
    public int getQtIngressos() {
        return qtIngressos;
    }
    public void setQtIngressos(int qtIngressos) {
        this.qtIngressos = qtIngressos;
    }

    public static List<Evento> listar() {
        String sql = "SELECT " +
                        "SEQ_EVENTO, DE_EVENTO, DE_ANIMACAO, DE_LOCAL, DT_EVENTO, " +
                        "HH_EVENTO, QT_MESAS, QT_CADEIRAS, QT_INGRESSO " +
                    "FROM TB_EVENTO";

        Connection cn = null;
        ArrayList<Evento> l = new ArrayList<Evento>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Evento i = new Evento();
                i.setId(rs.getInt("SEQ_EVENTO"));
                i.setDescricao(rs.getString("DE_EVENTO"));
                i.setAnimacao(rs.getString("DE_ANIMACAO"));
                i.setLocal(rs.getString("DE_LOCAL"));
                i.setData(rs.getDate("DT_EVENTO"));
                i.setHora(rs.getString("HH_EVENTO"));
                i.setQtMesas(rs.getInt("QT_MESAS"));
                i.setQtCadeiras(rs.getInt("QT_CADEIRAS"));
                i.setQtIngressos(rs.getInt("QT_INGRESSO"));
                
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
    
    public static List<Evento> listarAtivosFuturos() {
        String sql = "SELECT " +
                        "SEQ_EVENTO, DE_EVENTO, DE_ANIMACAO, DE_LOCAL, DT_EVENTO, " +
                        "HH_EVENTO, QT_MESAS, QT_CADEIRAS, QT_INGRESSO " +
                    "FROM TB_EVENTO WHERE DT_EVENTO >= CONVERT(VARCHAR, GETDATE(), 102)";

        Connection cn = null;
        ArrayList<Evento> l = new ArrayList<Evento>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Evento i = new Evento();
                i.setId(rs.getInt("SEQ_EVENTO"));
                i.setDescricao(rs.getString("DE_EVENTO"));
                i.setAnimacao(rs.getString("DE_ANIMACAO"));
                i.setLocal(rs.getString("DE_LOCAL"));
                i.setData(rs.getDate("DT_EVENTO"));
                i.setHora(rs.getString("HH_EVENTO"));
                i.setQtMesas(rs.getInt("QT_MESAS"));
                i.setQtCadeiras(rs.getInt("QT_CADEIRAS"));
                i.setQtIngressos(rs.getInt("QT_INGRESSO"));
                
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
    

    public static Evento getInstance(int id) {
        String sql = "SELECT " +
                        "SEQ_EVENTO, DE_EVENTO, DE_ANIMACAO, DE_LOCAL, DT_EVENTO, " +
                        "HH_EVENTO, QT_MESAS, QT_CADEIRAS, QT_INGRESSO " +
                    "FROM TB_EVENTO WHERE SEQ_EVENTO = ?";
        
        Connection cn = null;
        Evento i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new Evento();
                i.setId(rs.getInt("SEQ_EVENTO"));
                i.setDescricao(rs.getString("DE_EVENTO"));
                i.setAnimacao(rs.getString("DE_ANIMACAO"));
                i.setLocal(rs.getString("DE_LOCAL"));
                i.setData(rs.getDate("DT_EVENTO"));
                i.setHora(rs.getString("HH_EVENTO"));
                i.setQtMesas(rs.getInt("QT_MESAS"));
                i.setQtCadeiras(rs.getInt("QT_CADEIRAS"));
                i.setQtIngressos(rs.getInt("QT_INGRESSO"));

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
        ParametroAuditoria par = new ParametroAuditoria();
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "INSERT INTO TB_EVENTO (DE_EVENTO, DE_ANIMACAO, DE_LOCAL, DT_EVENTO, " +
                         "HH_EVENTO, QT_MESAS, QT_CADEIRAS, QT_INGRESSO) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, par.getSetParametro(descricao));
            p.setString(2, par.getSetParametro(animacao));
            p.setString(3, par.getSetParametro(local));
            p.setDate(4, new java.sql.Date(par.getSetParametro(data).getTime()));
            p.setString(5, par.getSetParametro(hora));
            p.setInt(6, par.getSetParametro(qtMesas));
            p.setInt(7, par.getSetParametro(qtCadeiras));
            p.setInt(8, par.getSetParametro(qtIngressos));
            
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

    public void alterar(Auditoria audit)throws InserirException{
 
        ResultSet rs = null;
        Connection cn = null;
        ParametroAuditoria par = new ParametroAuditoria();
        if(getDescricao() == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "UPDATE TB_EVENTO SET DE_EVENTO = ?, DE_ANIMACAO = ?, DE_LOCAL = ?, DT_EVENTO = ?, " +
                         "HH_EVENTO = ?, QT_MESAS = ?, QT_CADEIRAS = ?, QT_INGRESSO = ? WHERE SEQ_EVENTO = ? ";

            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, par.getSetParametro(descricao));
            p.setString(2, par.getSetParametro(animacao));
            p.setString(3, par.getSetParametro(local));
            p.setDate(4, new java.sql.Date(par.getSetParametro(data).getTime()));
            p.setString(5, par.getSetParametro(hora));
            p.setInt(6, par.getSetParametro(qtMesas));
            p.setInt(7, par.getSetParametro(qtCadeiras));
            p.setInt(8, par.getSetParametro(qtIngressos));
            
            p.setInt(9, id);
            
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

    public void excluir(Auditoria audit) {
        String sql = "DELETE FROM TB_EVENTO WHERE SEQ_EVENTO = ?";
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
