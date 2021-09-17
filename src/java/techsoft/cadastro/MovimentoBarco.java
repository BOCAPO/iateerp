package techsoft.cadastro;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class MovimentoBarco {
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.MovimentacaoBarco");

    private SocioBarco barco;        
    private int id;
    private Date dataMovimentacao;
    private String tipo;
    private String local;//C=Cais, L=Clube, A=Agua, R=Rampa
    private int passageiros;
    private String usuarioLote;
    private String localLote;//C=Cais, else = sistema
    private Date dataLote;

    public SocioBarco getBarco() {
        return barco;
    }

    public void setBarco(SocioBarco barco) {
        this.barco = barco;
    }

    public int getId() {
        return id;
    }

    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getPassageiros() {
        return passageiros;
    }

    public void setPassageiros(int passageiros) {
        this.passageiros = passageiros;
    }

    public String getUsuarioLote() {
        return usuarioLote;
    }

    public void setUsuarioLote(String usuarioLote) {
        this.usuarioLote = usuarioLote;
    }

    public String getLocalLote() {
        return localLote;
    }

    public void setLocalLote(String localLote) {
        this.localLote = localLote;
    }

    public Date getDataLote() {
        return dataLote;
    }

    public void setDataLote(Date dataLote) {
        this.dataLote = dataLote;
    }


    
    public static List<MovimentoBarco> listar(SocioBarco barco){

        ArrayList<MovimentoBarco> l = new ArrayList<MovimentoBarco>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call sp_recupera_mov_barco (?)}");
            cal.setInt(1, barco.getId());

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                MovimentoBarco m = new MovimentoBarco();
                
                m.id = rs.getInt("CD_MOVIMENTO");
                m.dataMovimentacao = rs.getTimestamp("dt_movimento");
                m.tipo = rs.getString("ic_entrada_saida");
                m.local = rs.getString("ic_local_movimento");
                m.passageiros = rs.getInt("NR_PASSAGEIROS");
                m.usuarioLote = rs.getString("USER_ACESSO_LOTE");
                m.localLote = rs.getString("IC_LOCAL_LOTE");
                m.dataLote = rs.getTimestamp("DT_LOTE");
                m.barco = barco;
                
                l.add(m);
            }
            cn.close();

        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return l;
    }
    
    public static MovimentoBarco getInstance(int id){
        MovimentoBarco m = null;
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "select T1.CD_MOVIMENTO, T1.CD_BARCO, T1.DT_MOVIMENTO, T1.IC_ENTRADA_SAIDA, "
                    + "	T2.DT_LOTE, T2.USER_ACESSO_LOTE, T2.IC_LOCAL_LOTE, T1.IC_LOCAL_MOVIMENTO, "
                    + "	T1.nr_passageiros from 	tb_movimento_barco t1, tb_lote_mov_barco t2 "
                    + " where t1.nu_lote_movimento = t2.nu_lote_movimento AND IC_SITUACAO = 'N' AND "
                    + " T1.CD_MOVIMENTO = ?";
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                m = new MovimentoBarco();
                
                m.id = rs.getInt("CD_MOVIMENTO");
                m.dataMovimentacao = rs.getTimestamp("dt_movimento");
                m.tipo = rs.getString("ic_entrada_saida");
                m.local = rs.getString("ic_local_movimento");
                m.passageiros = rs.getInt("NR_PASSAGEIROS");
                m.usuarioLote = rs.getString("USER_ACESSO_LOTE");
                m.localLote = rs.getString("IC_LOCAL_LOTE");
                m.dataLote = rs.getTimestamp("DT_LOTE");
                m.barco = SocioBarco.getInstance(rs.getInt("CD_BARCO"));
                
            }
            cn.close();

        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return m;
    }
    
    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            CallableStatement cal = cn.prepareCall("{call sp_exclui_mov_barco (?, ?)}");
            cal.setInt(1, id);
            cal.setString(2, audit.getLogin());
            cal.executeUpdate();
            cn.commit();
            cn.close();
            audit.registrarMudanca("{call sp_exclui_mov_barco (?, ?)}", String.valueOf(id), audit.getLogin());
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }
        
    public void inserir(Auditoria audit){

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call sp_inclui_mov_barco (?, ?, ?, ?, ?, ?)}");
            ParametroAuditoria par = new ParametroAuditoria();
            cal.setInt(1, par.getSetParametro(barco.getId()));
            cal.setTimestamp(2, new java.sql.Timestamp(par.getSetParametro(dataMovimentacao).getTime()));
            cal.setString(3, par.getSetParametro(tipo));
            cal.setString(4, par.getSetParametro(audit.getLogin()));
            cal.setString(5, par.getSetParametro(local));
            cal.setInt(6, par.getSetParametro(passageiros));

            cal.executeUpdate();
            cn.commit();
            cn.close();

            audit.registrarMudanca("call sp_inclui_mov_barco ", par.getParametroFinal());
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
