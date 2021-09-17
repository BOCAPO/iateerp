
package techsoft.clube;

import java.sql.CallableStatement;
import techsoft.tabelas.InserirException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import oracle.jrockit.jfr.Recording;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.ExcluirException;

public class ExcecaoAgendaAcademia {

    private int id;
    private String tipo;
    private int idServico;
    private String descricaoServico;
    private int idFuncionario;
    private String nomeFuncionario;
    private Date dtInicio;
    private Date dtFim;
    private Date dtAbertura;
    private String hhInicio;
    private String hhFim;
    private String icSegunda;
    private String icTerca;
    private String icQuarta;
    private String icQuinta;
    private String icSexta;
    private String icSabado;
    private String icDomingo;
    
    private static Logger log = Logger.getLogger("techsoft.clube.ExcecaoAgendaAcademia");

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getIdServico() {
        return idServico;
    }
    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }
    public String getDescricaoServico() {
        return descricaoServico;
    }
    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }
    public int getIdFuncionario() {
        return idFuncionario;
    }
    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
    public String getNomeFuncionario() {
        return nomeFuncionario;
    }
    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
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
    public String getHhInicio() {
        return hhInicio;
    }
    public void setHhInicio(String hhInicio) {
        this.hhInicio = hhInicio;
    }
    public String getHhFim() {
        return hhFim;
    }
   public void setHhFim(String hhFim) {
        this.hhFim = hhFim;
    }
    public String getIcSegunda() {
        return icSegunda;
    }
    public void setIcSegunda(String icSegunda) {
        this.icSegunda = icSegunda;
    }
    public String getIcTerca() {
        return icTerca;
    }
    public void setIcTerca(String icTerca) {
        this.icTerca = icTerca;
    }
    public String getIcQuarta() {
        return icQuarta;
    }
    public void setIcQuarta(String icQuarta) {
        this.icQuarta = icQuarta;
    }
    public String getIcQuinta() {
        return icQuinta;
    }
    public void setIcQuinta(String icQuinta) {
        this.icQuinta = icQuinta;
    }
    public String getIcSexta() {
        return icSexta;
    }
    public void setIcSexta(String icSexta) {
        this.icSexta = icSexta;
    }
    public String getIcSabado() {
        return icSabado;
    }
    public void setIcSabado(String icSabado) {
        this.icSabado = icSabado;
    }
    public String getIcDomingo() {
        return icDomingo;
    }
   public void setIcDomingo(String icDomingo) {
        this.icDomingo = icDomingo;
    }
    public Date getDtAbertura() {
        return dtAbertura;
    }
    public void setDtAbertura(Date dtAbertura) {
        this.dtAbertura = dtAbertura;
    }
   

    public static List<ExcecaoAgendaAcademia> listar(String tipo, int idServico, int idFuncionario, String tpPesquisa, Date dtReferencia){
        ArrayList<ExcecaoAgendaAcademia> l = new ArrayList<ExcecaoAgendaAcademia>();
        
        String sql = "{call SP_EXCECAO_AGENDA_SER_ACAD (NULL, ?, NULL, NULL, NULL, NULL, ?, ?, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'C', ?, ?, NULL)}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        
        try {
            cal = cn.prepareCall(sql);
            cal.setString(1, tipo);
            cal.setInt(2, idServico);
            cal.setInt(3, idFuncionario);
            cal.setString(4, tpPesquisa);
            cal.setDate(5, new java.sql.Date(dtReferencia.getTime()));

            ResultSet rs = cal.executeQuery();
            
            while (rs.next()) {
                ExcecaoAgendaAcademia d = new ExcecaoAgendaAcademia();
                
                d.id = rs.getInt("NU_SEQ_EXCECAO");
                d.tipo = rs.getString("IC_TIPO");
                d.idServico = rs.getInt("NU_SEQ_SERVICO");
                d.descricaoServico = rs.getString("DE_SERVICO");
                d.idFuncionario = rs.getInt("CD_FUNCIONARIO");
                d.nomeFuncionario = rs.getString("NOME_FUNCIONARIO");
                d.dtInicio = rs.getDate("DT_INICIO");
                d.dtFim = rs.getDate("DT_FIM");
                d.hhInicio = rs.getString("HH_INICIO");
                d.hhFim = rs.getString("HH_FIM");
                d.icSegunda = rs.getString("IC_SEGUNDA");
                d.icTerca = rs.getString("IC_TERCA");
                d.icQuarta = rs.getString("IC_QUARTA");
                d.icQuinta = rs.getString("IC_QUINTA");
                d.icSexta = rs.getString("IC_SEXTA");
                d.icSabado = rs.getString("IC_SABADO");
                d.icDomingo = rs.getString("IC_DOMINGO");
                d.dtAbertura = rs.getTimestamp("DT_ABERTURA");
                
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

    public static void excluir(Auditoria audit, int id) throws ExcluirException{
        String sql = "{call SP_EXCECAO_AGENDA_SER_ACAD (?, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'E', NULL, NULL, NULL)}";
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

        String sql = "{call SP_EXCECAO_AGENDA_SER_ACAD (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'I', NULL, NULL, ?)}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
                ParametroAuditoria par = new ParametroAuditoria();
                cal = cn.prepareCall(sql);
                
                cal.setString(1, par.getSetParametro(tipo));
                cal.setDate(2, new java.sql.Date(par.getSetParametro(dtInicio).getTime()));
                cal.setDate(3, new java.sql.Date(par.getSetParametro(dtFim).getTime()));
                cal.setString(4, par.getSetParametro(hhInicio.replace(":", "")));
                cal.setString(5, par.getSetParametro(hhFim.replace(":", "")));
                if (idServico > 0){
                    cal.setInt(6, par.getSetParametro(idServico));
                }else{
                    cal.setNull(6, java.sql.Types.INTEGER);
                    par.getSetNull();
                }
                if (idFuncionario > 0){
                    cal.setInt(7, par.getSetParametro(idFuncionario));
                }else{
                    cal.setNull(7, java.sql.Types.INTEGER);
                    par.getSetNull();
                }
                cal.setString(8, par.getSetParametro(icSegunda));
                cal.setString(9, par.getSetParametro(icTerca));
                cal.setString(10, par.getSetParametro(icQuarta));
                cal.setString(11, par.getSetParametro(icQuinta));
                cal.setString(12, par.getSetParametro(icSexta));
                cal.setString(13, par.getSetParametro(icSabado));
                cal.setString(14, par.getSetParametro(icDomingo));
                if (dtAbertura == null){
                    cal.setNull(15, java.sql.Types.INTEGER);
                    par.getSetNull();
                }else{
                    cal.setTimestamp(15, new java.sql.Timestamp(par.getSetParametro(dtAbertura).getTime()));
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

}