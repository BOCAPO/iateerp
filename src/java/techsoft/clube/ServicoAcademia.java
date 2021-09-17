
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

public class ServicoAcademia {

    private int id;
    private String descricao;
    private int qtAluno;
    private Integer qtLimiteSocio;
    private Integer qtDiasLimiteSocio;
    private Integer qtDiasAntClube;
    private int qtMinAtendimento;
    private Integer qtMinIntervalo;
    private BigDecimal vrMulta;
    private Integer cdTxMulta;
    private BigDecimal vrServico;
    private Integer cdTxServico;
    private Integer pzCancelamento;
    private String tpPzCancelamento;
    private String tpPzLimite;
    private String icAgendaInternet;
    private Integer qtDiasAntInternet;
    private Date dtInicioVigencia;
    private Date dtFimVigencia;
    private String detalhamento;
    private String instrucao;
    private String tpPzAntAgendClube;
    private String tpPzAntAgendInternet;
    private int vagaClube;
    private int vagaInternet;
    private Integer prazoMaximoClube;
    private Integer prazoMaximoInternet;
    private Integer hhInicClube;
    private Integer hhInicInternet;

    private static final Logger log = Logger.getLogger("techsoft.clube.ServicoAcademia");

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
    public int getQtAluno() {
        return qtAluno;
    }
    public void setQtAluno(int qtAluno) {
        this.qtAluno = qtAluno;
    }
    public Integer getQtLimiteSocio() {
        return qtLimiteSocio;
    }
    public void setQtLimiteSocio(Integer qtLimiteSocio) {
        this.qtLimiteSocio = qtLimiteSocio;
    }
    public Integer getQtDiasLimiteSocio() {
        return qtDiasLimiteSocio;
    }
    public void setQtDiasLimiteSocio(Integer qtDiasLimiteSocio) {
        this.qtDiasLimiteSocio = qtDiasLimiteSocio;
    }
    public Integer getQtDiasAntClube() {
        return qtDiasAntClube;
    }
    public void setQtDiasAntClube(Integer qtDiasAntClube) {
        this.qtDiasAntClube = qtDiasAntClube;
    }
    public int getQtMinAtendimento() {
        return qtMinAtendimento;
    }
    public void setQtMinAtendimento(int qtMinAtendimento) {
        this.qtMinAtendimento = qtMinAtendimento;
    }
    public Integer getQtMinIntervalo() {
        return qtMinIntervalo;
    }
    public void setQtMinIntervalo(Integer qtMinIntervalo) {
        this.qtMinIntervalo = qtMinIntervalo;
    }
    public BigDecimal getVrMulta() {
        return vrMulta;
    }
    public void setVrMulta(BigDecimal vrMulta) {
        this.vrMulta = vrMulta;
    }
    public Integer getCdTxMulta() {
        return cdTxMulta;
    }
    public void setCdTxMulta(Integer cdTxMulta) {
        this.cdTxMulta = cdTxMulta;
    }
    public BigDecimal getVrServico() {
        return vrServico;
    }
    public void setVrServico(BigDecimal vrServico) {
        this.vrServico = vrServico;
    }
    public Integer getCdTxServico() {
        return cdTxServico;
    }
    public void setCdTxServico(Integer cdTxServico) {
        this.cdTxServico = cdTxServico;
    }
    public Integer getPzCancelamento() {
        return pzCancelamento;
    }
    public void setPzCancelamento(Integer pzCancelamento) {
        this.pzCancelamento = pzCancelamento;
    }
    public String getTpPzCancelamento() {
        return tpPzCancelamento;
    }
    public void setTpPzCancelamento(String tpPzCancelamento) {
        this.tpPzCancelamento = tpPzCancelamento;
    }
    public String getTpPzLimite() {
        return tpPzLimite;
    }
    public void setTpPzLimite(String tpPzLimite) {
        this.tpPzLimite = tpPzLimite;
    }
    public String getIcAgendaInternet() {
        return icAgendaInternet;
    }
    public void setIcAgendaInternet(String icAgendaInternet) {
        this.icAgendaInternet = icAgendaInternet;
    }
    public Integer getQtDiasAntInternet() {
        return qtDiasAntInternet;
    }
   public void setQtDiasAntInternet(Integer qtDiasAntInternet) {
        this.qtDiasAntInternet = qtDiasAntInternet;
    }
    public Date getDtInicioVigencia() {
        return dtInicioVigencia;
    }
    public void setDtInicioVigencia(Date dtInicioVigencia) {
        this.dtInicioVigencia = dtInicioVigencia;
    }
    public Date getDtFimVigencia() {
        return dtFimVigencia;
    }
    public void setDtFimVigencia(Date dtFimVigencia) {
        this.dtFimVigencia = dtFimVigencia;
    }
    public String getDetalhamento() {
        return detalhamento;
    }
    public void setDetalhamento(String detalhamento) {
        this.detalhamento = detalhamento;
    }
    public String getInstrucao() {
        return instrucao;
    }
    public void setInstrucao(String instrucao) {
        this.instrucao = instrucao;
    }
    public String getTpPzAntAgendClube() {
        return tpPzAntAgendClube;
    }
    public void setTpPzAntAgendClube(String tpPzAntAgendClube) {
        this.tpPzAntAgendClube = tpPzAntAgendClube;
    }
    public String getTpPzAntAgendInternet() {
        return tpPzAntAgendInternet;
    }
    public void setTpPzAntAgendInternet(String tpPzAntAgendInternet) {
        this.tpPzAntAgendInternet = tpPzAntAgendInternet;
    }
    public int getVagaClube() {
        return vagaClube;
    }
    public void setVagaClube(int vagaClube) {
        this.vagaClube = vagaClube;
    }
    public int getVagaInternet() {
        return vagaInternet;
    }
    public void setVagaInternet(int vagaInternet) {
        this.vagaInternet = vagaInternet;
    }
    public Integer getPrazoMaximoClube() {
        return prazoMaximoClube;
    }
    public void setPrazoMaximoClube(Integer prazoMaximoClube) {
        this.prazoMaximoClube = prazoMaximoClube;
    }
    public Integer getPrazoMaximoInternet() {
        return prazoMaximoInternet;
    }
    public void setPrazoMaximoInternet(Integer prazoMaximoInternet) {
        this.prazoMaximoInternet = prazoMaximoInternet;
    }
    public Integer getHhInicClube() {
        return hhInicClube;
    }
    public void setHhInicClube(Integer hhInicClube) {
        this.hhInicClube = hhInicClube;
    }
    public Integer getHhInicInternet() {
        return hhInicInternet;
    }
   public void setHhInicInternet(Integer hhInicInternet) {
        this.hhInicInternet = hhInicInternet;
    }
   
    public static List<ServicoAcademia> listar(){
        ArrayList<ServicoAcademia> l = new ArrayList<ServicoAcademia>();
        String sql = "{call SP_SERVICO_ACADEMIA (null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'C', null, null, null, null, null, null, null, null, null, null)}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall(sql);
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                ServicoAcademia d = new ServicoAcademia();
                d.setId(rs.getInt(1));
                d.setDescricao(rs.getString(2));
                l.add(d);
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

    public static ServicoAcademia getInstance(int id){
        ServicoAcademia d = null;
        String sql = "{call SP_SERVICO_ACADEMIA (?, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'N', null, null, null, null, null, null, null, null, null, null)}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall(sql);
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new ServicoAcademia();
                d.setId(rs.getInt("NU_SEQ_SERVICO"));
                d.setDescricao(rs.getString("DE_SERVICO"));
                d.setQtAluno(rs.getInt("QT_ALUNO"));
                d.setQtLimiteSocio((Integer) rs.getInt("QT_LIMITE_SOCIO"));
                if (rs.wasNull()){d.setQtLimiteSocio(null);}
                d.setQtDiasLimiteSocio((Integer) rs.getInt("QT_DIAS_LIMITE_SOCIO"));
                if (rs.wasNull()){d.setQtDiasLimiteSocio(null);}
                d.setQtDiasAntClube((Integer) rs.getInt("QT_DIAS_ANT_CLUBE"));
                if (rs.wasNull()){d.setQtDiasAntClube(null);}
                d.setQtMinAtendimento(rs.getInt("QT_MIN_ATENDIMENTO"));
                d.setQtMinIntervalo((Integer) rs.getInt("QT_MIN_INTERVALO"));
                if (rs.wasNull()){d.setQtMinIntervalo(null);}
                d.setVrMulta(rs.getBigDecimal("VR_MULTA"));
                d.setCdTxMulta((Integer) rs.getInt("CD_TX_MULTA"));
                if (rs.wasNull()){d.setCdTxMulta(null);}
                d.setVrServico(rs.getBigDecimal("VR_SERVICO"));
                d.setCdTxServico((Integer) rs.getInt("CD_TX_SERVICO"));
                if (rs.wasNull()){d.setCdTxServico(null);}
                d.setPzCancelamento((Integer) rs.getInt("PZ_CANCELAMENTO"));
                if (rs.wasNull()){d.setPzCancelamento(null);}
                d.setTpPzCancelamento(rs.getString("TP_PRAZO_CANCELAMENTO"));
                if (rs.wasNull()){d.setTpPzLimite(null);}
                d.setTpPzLimite(rs.getString("TP_PRAZO_LIMITE"));
                d.setIcAgendaInternet(rs.getString("IC_AGENDA_INTERNET"));
                d.setQtDiasAntInternet((Integer) rs.getInt("QT_DIAS_ANT_INTERNET"));
                if (rs.wasNull()){d.setQtDiasAntInternet(null);}
                d.setDtInicioVigencia(rs.getDate("DT_INICIO_VIGENCIA"));
                d.setDtFimVigencia(rs.getDate("DT_FIM_VIGENCIA"));
                d.setInstrucao(rs.getString("DE_INSTRUCAO"));
                d.setDetalhamento(rs.getString("DE_DETALHAMENTO"));
                d.setTpPzAntAgendClube(rs.getString("TP_PZ_ANT_AGEND_CLUBE"));
                d.setTpPzAntAgendInternet(rs.getString("TP_PZ_ANT_AGEND_INTERNET"));
                d.setVagaClube(rs.getInt("QT_VAGA_CLUBE"));
                d.setVagaInternet(rs.getInt("QT_VAGA_INTERNET"));
                
                d.setPrazoMaximoClube((Integer) rs.getInt("PZ_MAX_CLUBE"));
                if (rs.wasNull()){d.setPrazoMaximoClube(null);}
                d.setPrazoMaximoInternet((Integer) rs.getInt("PZ_MAX_INTERNET"));
                if (rs.wasNull()){d.setPrazoMaximoInternet(null);}
                
                d.setHhInicClube((Integer) rs.getInt("HH_INIC_CLUBE"));
                if (rs.wasNull()){d.setHhInicClube(null);}
                d.setHhInicInternet((Integer) rs.getInt("HH_INIC_INTERNET"));
                if (rs.wasNull()){d.setHhInicInternet(null);}

                
            }
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return d;
    }

    public void excluir(Auditoria audit)throws ExcluirException{
        String sql = "{call SP_SERVICO_ACADEMIA (?, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'E', null, null, null, null, null, null, null, null, null, null)}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall(sql);
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca(sql, String.valueOf(id));
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

    public void inserir(Auditoria audit)throws InserirException{

        String sql = "{call SP_SERVICO_ACADEMIA (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'N', ?, ?, ?, 'I', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            
            cal.setString(1, par.getSetParametro(getDescricao()));
            cal.setInt(2, par.getSetParametro(getQtAluno()));
            if (getQtLimiteSocio() == null){
                cal.setNull(3, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(3, par.getSetParametro(getQtLimiteSocio()));
            }
            if (getQtDiasLimiteSocio() == null){
                cal.setNull(4, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(4, par.getSetParametro(getQtDiasLimiteSocio()));
            }
            if (getQtDiasAntClube() == null){
                cal.setNull(5, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(5, par.getSetParametro(getQtDiasAntClube()));
            }
            cal.setInt(6, par.getSetParametro(getQtMinAtendimento()));
            if (getQtMinIntervalo() == null){
                cal.setNull(7, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(7, par.getSetParametro(getQtMinIntervalo()));
            }
            if (getVrMulta() == null){
                cal.setNull(8, java.sql.Types.FLOAT);
                par.getSetNull();
            }else{
                cal.setBigDecimal(8, par.getSetParametro(getVrMulta()));
            }
            if (getCdTxMulta() == null){
                cal.setNull(9, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(9, par.getSetParametro(getCdTxMulta()));
            }
            if (getVrServico() == null){
                cal.setNull(10, java.sql.Types.FLOAT);
                par.getSetNull();
            }else{
                cal.setBigDecimal(10, par.getSetParametro(getVrServico()));
            }
            if (getCdTxServico() == null){
                cal.setNull(11, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(11, par.getSetParametro(getCdTxServico()));
            }
            if (getPzCancelamento() == null){
                cal.setNull(12, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(12, par.getSetParametro(getPzCancelamento()));
            }
            if (getTpPzCancelamento() == null){
                cal.setNull(13, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(13, par.getSetParametro(getTpPzCancelamento()));
            }
            if (getIcAgendaInternet() == null){
                cal.setNull(14, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(14, par.getSetParametro(getIcAgendaInternet()));
            }
            if (getQtDiasAntInternet() == null){
                cal.setNull(15, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(15, par.getSetParametro(getQtDiasAntInternet()));
            }
            if (getDtInicioVigencia()== null){
                cal.setNull(16, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(16, new java.sql.Date(par.getSetParametro(getDtInicioVigencia()).getTime()));
            }
            if (getDtFimVigencia()== null){
                cal.setNull(17, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(17, new java.sql.Date(par.getSetParametro(getDtFimVigencia()).getTime()));
            }
            if (getTpPzLimite() == null){
                cal.setNull(18, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(18, par.getSetParametro(getTpPzLimite()));
            }
            
            cal.setString(19, par.getSetParametro(detalhamento));
            cal.setString(20, par.getSetParametro(instrucao));

            if (getTpPzAntAgendClube()== null){
                cal.setNull(21, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(21, par.getSetParametro(getTpPzAntAgendClube()));
            }
            if (getTpPzAntAgendClube()== null){
                cal.setNull(22, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(22, par.getSetParametro(getTpPzAntAgendInternet()));
            }
            cal.setInt(23, par.getSetParametro(getVagaClube()));
            cal.setInt(24, par.getSetParametro(getVagaInternet()));
            
            if (getPrazoMaximoClube() == null){
                cal.setNull(25, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(25, par.getSetParametro(getPrazoMaximoClube()));
            }
            if (getPrazoMaximoInternet() == null){
                cal.setNull(26, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(26, par.getSetParametro(getPrazoMaximoInternet()));
            }
            if (getHhInicClube() == null){
                cal.setNull(27, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(27, par.getSetParametro(getHhInicClube()));
            }
            if (getHhInicInternet()== null){
                cal.setNull(28, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(28, par.getSetParametro(getHhInicInternet()));
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

    public void alterar(Auditoria audit) throws AlterarException{

        String sql = "{call SP_SERVICO_ACADEMIA (?, ?, NULL, ?, ?, ?, NULL, NULL, ?, ?, ?, ?, ?, ?, ?, ?, 'N', ?, ?, ?, 'A', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            
            cal = cn.prepareCall(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            

            cal.setInt(1, par.getSetParametro(getId()));
            cal.setString(2, par.getSetParametro(getDescricao()));
            
            if (getQtLimiteSocio() == null){
                cal.setNull(3, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(3, par.getSetParametro(getQtLimiteSocio()));
            }
            if (getQtDiasLimiteSocio() == null){
                cal.setNull(4, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(4, par.getSetParametro(getQtDiasLimiteSocio()));
            }
            if (getQtDiasAntClube() == null){
                cal.setNull(5, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(5, par.getSetParametro(getQtDiasAntClube()));
            }
            if (getVrMulta() == null){
                cal.setNull(6, java.sql.Types.FLOAT);
                par.getSetNull();
            }else{
                cal.setBigDecimal(6, par.getSetParametro(getVrMulta()));
            }
            if (getCdTxMulta() == null){
                cal.setNull(7, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(7, par.getSetParametro(getCdTxMulta()));
            }
            if (getVrServico() == null){
                cal.setNull(8, java.sql.Types.FLOAT);
                par.getSetNull();
            }else{
                cal.setBigDecimal(8, par.getSetParametro(getVrServico()));
            }
            if (getCdTxServico() == null){
                cal.setNull(9, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(9, par.getSetParametro(getCdTxServico()));
            }
            if (getPzCancelamento() == null){
                cal.setNull(10, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(10, par.getSetParametro(getPzCancelamento()));
            }
            if (getTpPzCancelamento() == null){
                cal.setNull(11, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(11, par.getSetParametro(getTpPzCancelamento()));
            }
            if (getIcAgendaInternet() == null){
                cal.setNull(12, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(12, par.getSetParametro(getIcAgendaInternet()));
            }
            if (getQtDiasAntInternet() == null){
                cal.setNull(13, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(13, par.getSetParametro(getQtDiasAntInternet()));
            }
            if (getDtInicioVigencia()== null){
                cal.setNull(14, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(14, new java.sql.Date(par.getSetParametro(getDtInicioVigencia()).getTime()));
            }
            if (getDtFimVigencia()== null){
                cal.setNull(15, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(15, new java.sql.Date(par.getSetParametro(getDtFimVigencia()).getTime()));
            }
            if (getTpPzLimite() == null){
                cal.setNull(16, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(16, par.getSetParametro(getTpPzLimite()));
            }
            cal.setString(17, par.getSetParametro(detalhamento));
            cal.setString(18, par.getSetParametro(instrucao));
            
            if (getTpPzAntAgendClube()== null){
                cal.setNull(19, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(19, par.getSetParametro(getTpPzAntAgendClube()));
            }
            if (getTpPzAntAgendClube()== null){
                cal.setNull(20, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(20, par.getSetParametro(getTpPzAntAgendInternet()));
            }
            cal.setInt(21, par.getSetParametro(getVagaClube()));
            cal.setInt(22, par.getSetParametro(getVagaInternet()));

            if (getPrazoMaximoClube() == null){
                cal.setNull(23, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(23, par.getSetParametro(getPrazoMaximoClube()));
            }
            if (getPrazoMaximoInternet() == null){
                cal.setNull(24, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(24, par.getSetParametro(getPrazoMaximoInternet()));
            }

            if (getHhInicClube() == null){
                cal.setNull(25, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(25, par.getSetParametro(getHhInicClube()));
            }
            if (getHhInicInternet()== null){
                cal.setNull(26, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(26, par.getSetParametro(getHhInicInternet()));
            }
            
            
            ResultSet rs = cal.executeQuery();
            
            if (rs.next()){
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

    public static void importar(int idOrigem, int idDestino, Auditoria audit){
        Connection cn = null;

        try {
            String sql = "{call SP_IMPORTA_AGENDA_ACADEMIA (" + idOrigem + ", " + idDestino + ")}";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql);
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