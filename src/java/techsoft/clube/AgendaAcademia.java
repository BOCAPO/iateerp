
package techsoft.clube;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Statement;
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
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;
import techsoft.tabelas.InserirException;

public class AgendaAcademia {
    private int id;
    private int cdMatricula;
    private int cdCategoria;
    private int seqDependente;
    private String nomePessoa;
    private Date dtInicio;
    private Date dtFim;
    private int idServico;
    private int idFuncionario;
    private int idAgenda;
    private int idExcecao;
    private String situacao;
    private Date dtAgendamento;
    private String userAgendamento;
    private Date dtCancelamento;
    private String userCancelamento;
    private String icComparecimento;
    private String userSupervisao;
    
    private static final Logger log = Logger.getLogger("techsoft.clube.AgendaAcademia");
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCdMatricula() {
        return cdMatricula;
    }
    public void setCdMatricula(int cdMatricula) {
        this.cdMatricula = cdMatricula;
    }
    public int getCdCategoria() {
        return cdCategoria;
    }
    public void setCdCategoria(int cdCategoria) {
        this.cdCategoria = cdCategoria;
    }
    public int getSeqDependente() {
        return seqDependente;
    }
    public void setSeqDependente(int seqDependente) {
        this.seqDependente = seqDependente;
    }
    public String getNomePessoa() {
        return nomePessoa;
    }
    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
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
    public int getIdServico() {
        return idServico;
    }
    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }
    public int getIdFuncionario() {
        return idFuncionario;
    }
    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
    public int getIdAgenda() {
        return idAgenda;
    }
    public void setIdAgenda(int idAgenda) {
        this.idAgenda = idAgenda;
    }
    public int getIdExcecao() {
        return idExcecao;
    }
    public void setIdExcecao(int idExcecao) {
        this.idExcecao = idExcecao;
    }
    public String getSituacao() {
        return situacao;
    }
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    public Date getDtAgendamento() {
        return dtAgendamento;
    }
    public void setDtAgendamento(Date dtAgendamento) {
        this.dtAgendamento = dtAgendamento;
    }
    public String getUserAgendamento() {
        return userAgendamento;
    }
    public void setUserAgendamento(String userAgendamento) {
        this.userAgendamento = userAgendamento;
    }
    public Date getDtCancelamento() {
        return dtCancelamento;
    }
    public void setDtCancelamento(Date dtCancelamento) {
        this.dtCancelamento = dtCancelamento;
    }
    public String getUserCancelamento() {
        return userCancelamento;
    }
    public void setUserCancelamento(String userCancelamento) {
        this.userCancelamento = userCancelamento;
    }
    public String getIcComparecimento() {
        return icComparecimento;
    }
    public void setIcComparecimento(String icComparecimento) {
        this.icComparecimento = icComparecimento;
    }
    public String getUserSupervisao() {
        return userSupervisao;
    }
    public void setUserSupervisao(String userSupervisao) {
        this.userSupervisao = userSupervisao;
    }
    
    public void inserir(Auditoria audit)throws InserirException{

        String sql = "{call SP_AGENDA_SERVICO_ACADEMIA (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        ParametroAuditoria par = new ParametroAuditoria();

        try {
            cal = cn.prepareCall(sql);
            cal.setInt(1, par.getSetParametro(cdMatricula));
            cal.setInt(2, par.getSetParametro(cdCategoria));
            cal.setInt(3, par.getSetParametro(seqDependente));
            cal.setTimestamp(4, new java.sql.Timestamp(par.getSetParametro(dtInicio).getTime()));
            cal.setTimestamp(5, new java.sql.Timestamp(par.getSetParametro(dtFim).getTime()));
            cal.setInt(6, par.getSetParametro(idServico));
            cal.setInt(7, par.getSetParametro(idFuncionario));
            if (idAgenda>0){
                cal.setInt(8, par.getSetParametro(idAgenda));
            }else{
                cal.setNull(8, java.sql.Types.INTEGER);
                par.getSetNull();

            }
            if (idExcecao>0){
                cal.setInt(9, par.getSetParametro(idExcecao));
            }else{
                cal.setNull(9, java.sql.Types.INTEGER);
                par.getSetNull();
            }
            cal.setString(10, par.getSetParametro(userAgendamento));
            if (userSupervisao != null){
                if (!userSupervisao.equals("")){
                    cal.setString(11, par.getSetParametro(userSupervisao));
                }else{
                    cal.setNull(11, java.sql.Types.VARCHAR);
                    par.getSetNull();
                }
            }else{
                cal.setNull(11, java.sql.Types.VARCHAR);
                par.getSetNull();
            }
                
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
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
                throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }
    
    public static void excluir(int idAgendamento, String userCancelamento, String userSupervisao, Auditoria audit)throws ExcluirException{

        String sql = "{call SP_CANCELA_AGENDA_SERVICO_ACADEMIA (?, ?, ?)}";
        CallableStatement cal;
        Connection cn;
        cn = Pool.getInstance().getConnection();
        ParametroAuditoria par = new ParametroAuditoria();

        try {
            cal = cn.prepareCall(sql);
            cal.setInt(1, par.getSetParametro(idAgendamento));
            cal.setString(2, par.getSetParametro(userCancelamento));
            if (userSupervisao != null){
                if (!userSupervisao.equals("")){
                    cal.setString(3, par.getSetParametro(userSupervisao));
                }else{
                    cal.setNull(3, java.sql.Types.VARCHAR);
                    par.getSetNull();
                }
            }else{
                cal.setNull(3, java.sql.Types.VARCHAR);
                par.getSetNull();
            }
                
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca(sql, String.valueOf(idAgendamento), userCancelamento, userSupervisao);
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new ExcluirException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new ExcluirException(err);
            }
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }

    public static void marcaFalta(int idAgendamento, String usuario, Auditoria audit)throws ExcluirException{

        String sql = "{call SP_MARCA_FALTA_AGENDA_SERVICO_ACADEMIA (?, ?)}";
        CallableStatement cal;
        Connection cn;
        cn = Pool.getInstance().getConnection();
        ParametroAuditoria par = new ParametroAuditoria();

        try {
            cal = cn.prepareCall(sql);
            cal.setInt(1, par.getSetParametro(idAgendamento));
            cal.setString(2, par.getSetParametro(usuario));
                
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca(sql, String.valueOf(idAgendamento), usuario);
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new ExcluirException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new ExcluirException(err);
            }
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }
    
    public static void desmarcaFalta(int idAgendamento, String usuario, Auditoria audit)throws ExcluirException{

        String sql = "{call SP_DESMARCA_FALTA_AGENDA_SERVICO_ACADEMIA (?, ?)}";
        CallableStatement cal;
        Connection cn;
        cn = Pool.getInstance().getConnection();
        ParametroAuditoria par = new ParametroAuditoria();

        try {
            cal = cn.prepareCall(sql);
            cal.setInt(1, par.getSetParametro(idAgendamento));
            cal.setString(2, par.getSetParametro(usuario));
                
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca(sql, String.valueOf(idAgendamento), usuario);
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new ExcluirException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new ExcluirException(err);
            }
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }    
}
