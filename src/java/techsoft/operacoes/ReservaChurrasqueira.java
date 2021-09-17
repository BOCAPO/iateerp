package techsoft.operacoes;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;

public class ReservaChurrasqueira {

    private int numero;
    private int categoriaSocio;
    private int matricula;
    private int dependente;
    private int idChurrasqueira;
    private int saldoConvite;
    private int dependencia;
    private Date dtInicio;
    private Date dtFim;
    private String interessado;
    private String deChurrasqueira;
    private String status;
    private String telefone;
    private String fax;
    private String email;
    private String hhInicio;
    private String hhFim;
    private String usuario;
    private String supervisao;
    private Date dtConfirmacao;
    private String txComprovante;
    private String usuarioCanc;
    private int qtConvites;
    private String origem;
    private String motivoBloqueio;

    private static final Logger log = Logger.getLogger("techsoft.operacoes.ReservaChurrasqueira");

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getDependencia() {
        return dependencia;
    }

    public void setDependencia(int dependencia) {
        this.dependencia = dependencia;
    }

    public int getCategoriaSocio() {
        return categoriaSocio;
    }

    public void setCategoriaSocio(int categoriaSocio) {
        this.categoriaSocio = categoriaSocio;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getDependente() {
        return dependente;
    }

    public void setDependente(int dependente) {
        this.dependente = dependente;
    }

    public int getIdChurrasqueira() {
        return idChurrasqueira;
    }

    public void setIdChurrasqueira(int idChurrasqueira) {
        this.idChurrasqueira = idChurrasqueira;
    }

    public int getSaldoConvite() {
        return saldoConvite;
    }

    public void setSaldoConvite(int saldoConvite) {
        this.saldoConvite = saldoConvite;
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

    public String getInteressado() {
        return interessado;
    }

    public void setInteressado(String interessado) {
        this.interessado = interessado;
    }

    public String getDeChurrasqueira() {
        return deChurrasqueira;
    }

    public void setDeChurrasqueira(String deChurrasqueira) {
        this.deChurrasqueira = deChurrasqueira;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSupervisao() {
        return supervisao;
    }

    public void setSupervisao(String supervisao) {
        this.supervisao = supervisao;
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

    public String getTxComprovante() {
        return txComprovante;
    }

    public void setTxComprovante(String txComprovante) {
        this.txComprovante = txComprovante;
    }

    public Date getDtConfirmacao() {
        return dtConfirmacao;
    }

    public void setDtConfirmacao(Date dtConfirmacao) {
        this.dtConfirmacao = dtConfirmacao;
    }

    public String getUsuarioCanc() {
        return usuarioCanc;
    }

    public void setUsuarioCanc(String usuarioCanc) {
        this.usuarioCanc = usuarioCanc;
    }

    public int getQtConvites() {
        return qtConvites;
    }

    public void setQtConvites(int qtConvites) {
        this.qtConvites = qtConvites;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotivoBloqueio() {
        return motivoBloqueio;
    }

    public void setMotivoBloqueio(String motivoBloqueio) {
        this.motivoBloqueio = motivoBloqueio;
    }

    public static List<ReservaChurrasqueira> reservaSocioConvite(int matricula, int categoria) {

        ArrayList<ReservaChurrasqueira> l = new ArrayList<ReservaChurrasqueira>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;

            String sql = "SELECT "
                    + "T1.NOME_INTERESSADO, "
                    + "T1.DT_INIC_UTILIZACAO, "
                    + "T1.DT_FIM_UTILIZACAO, "
                    + "T2.DESCR_DEPENDENCIA, "
                    + "T1.SEQ_RESERVA "
                    + "FROM "
                    + "TB_RESERVA_DEPENDENCIA T1, "
                    + "TB_DEPENDENCIA T2 "
                    + "WHERE "
                    + "T1.SEQ_DEPENDENCIA = T2.SEQ_DEPENDENCIA AND "
                    + "T1.SEQ_DEPENDENCIA < 24 AND "
                    + "T1.CD_STATUS_RESERVA <> 'CA' AND "
                    + "T1.DT_INIC_UTILIZACAO >= CONVERT(DATETIME, CONVERT(VARCHAR, YEAR(GETDATE())) + '-' +RIGHT('0' + CONVERT(VARCHAR, MONTH(GETDATE())), 2) + '-' + RIGHT('0' + CONVERT(VARCHAR, DAY(GETDATE())), 2)) AND "
                    + "T1.CD_MATRICULA = ? AND "
                    + "T1.CD_CATEGORIA = ? "
                    + "ORDER BY T1.DT_INIC_UTILIZACAO";

            cal = cn.prepareCall(sql);
            cal.setInt(1, matricula);
            cal.setInt(2, categoria);

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                ReservaChurrasqueira s = new ReservaChurrasqueira();

                s.setNumero(rs.getInt("SEQ_RESERVA"));
                s.setDtInicio(rs.getDate("DT_INIC_UTILIZACAO"));
                s.setDtFim(rs.getDate("DT_FIM_UTILIZACAO"));
                s.setInteressado(rs.getString("NOME_INTERESSADO"));
                s.setDeChurrasqueira(rs.getString("DESCR_DEPENDENCIA"));
                s.setSaldoConvite(saldoConviteReserva(rs.getInt("SEQ_RESERVA")));

                l.add(s);
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

    public static ReservaChurrasqueira getInstance(int id) {
        ReservaChurrasqueira d = null;
        String sql = "SELECT T1.*, T2.*, "
                + "(SELECT DE_EMAIL FROM TB_PESSOA T0 WHERE T1.CD_MATRICULA = T0.CD_MATRICULA AND T1.CD_CATEGORIA = T0.CD_CATEGORIA AND T1.SEQ_DEPENDENTE = T0.SEQ_DEPENDENTE) DE_EMAIL, "
                + "(SELECT NR_TELEFONE FROM TB_TELEFONE_PESSOA T0 WHERE T1.CD_MATRICULA = T0.CD_MATRICULA AND T1.CD_CATEGORIA = T0.CD_CATEGORIA AND T1.SEQ_DEPENDENTE = T0.SEQ_DEPENDENTE AND IC_TIPO = 'F') FAX "
                + "FROM TB_RESERVA_DEPENDENCIA T1, TB_DEPENDENCIA T2 "
                + "WHERE T1.SEQ_DEPENDENCIA = T2.SEQ_DEPENDENCIA AND  "
                + "T1.SEQ_RESERVA = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                d = new ReservaChurrasqueira();

                d.deChurrasqueira = rs.getString("DESCR_DEPENDENCIA");
                d.dtInicio = rs.getDate("DT_INIC_UTILIZACAO");
                d.interessado = rs.getString("NOME_INTERESSADO");
                d.telefone = rs.getString("TEL_CONTATO");
                d.hhInicio = rs.getString("HH_INIC_UTIL");
                d.hhFim = rs.getString("HH_FIM_UTIL");
                d.dtConfirmacao = rs.getTimestamp("DT_CONFIRMACAO");
                d.txComprovante = rs.getString("MSG_LINHA");
                d.email = rs.getString("DE_EMAIL");
                d.fax = rs.getString("FAX");
                d.matricula = rs.getInt("CD_MATRICULA");
                d.categoriaSocio = rs.getInt("CD_CATEGORIA");

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

        return d;
    }

    public static int saldoConviteReserva(int reserva) {

        Connection cn = null;
        int saldo = 0;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;

            String sql = "{call SP_REC_SD_CONV_CHU_INTERNET (?)}";

            cal = cn.prepareCall(sql);
            cal.setInt(1, reserva);

            ResultSet rs = cal.executeQuery();
            if (rs.next()) {
                saldo = rs.getInt(1);
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

        return saldo;
    }

    public static List<ReservaChurrasqueira> listar(String data) {

        ArrayList<ReservaChurrasqueira> l = new ArrayList<ReservaChurrasqueira>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;

            String sql = "SELECT "
                    + "T1.DESCR_DEPENDENCIA, "
                    + "T1.SEQ_DEPENDENCIA, "
                    + "T2.SEQ_RESERVA, "
                    + "T2.CD_STATUS_RESERVA, "
                    + "T2.IC_ORIGEM_RESERVA "
                    + "FROM  "
                    + "TB_DEPENDENCIA T1, "
                    + "( "
                    + "SELECT "
                    + "* "
                    + "FROM "
                    + "TB_RESERVA_DEPENDENCIA T0 "
                    + "WHERE "
                    + "DAY(DT_INIC_UTILIZACAO) = ? AND "
                    + "MONTH(DT_INIC_UTILIZACAO) = ? AND "
                    + "YEAR(DT_INIC_UTILIZACAO) = ? AND "
                    + "CD_STATUS_RESERVA <> 'CA'"
                    + ") T2 "
                    + "WHERE "
                    + "T1.SEQ_DEPENDENCIA < 24 AND "
                    + "T1.SEQ_DEPENDENCIA NOT IN (7, 16, 17, 22, 23) AND "
                    + "T1.SEQ_DEPENDENCIA *= T2.SEQ_DEPENDENCIA " + //LEFT OUTER JOIN
                    "ORDER BY "
                    + "T1.DE_ABREVIACAO ";

            cal = cn.prepareCall(sql);
            String Dia = data.substring(0, 2);
            cal.setString(1, Dia);
            String Mes = data.substring(3, 5);
            cal.setString(2, Mes);
            String Ano = data.substring(6);
            cal.setString(3, Ano);

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                ReservaChurrasqueira s = new ReservaChurrasqueira();

                s.setDeChurrasqueira(rs.getString("DESCR_DEPENDENCIA"));
                s.setIdChurrasqueira(rs.getInt("SEQ_DEPENDENCIA"));
                s.setNumero(rs.getInt("SEQ_RESERVA"));
                s.setStatus(rs.getString("CD_STATUS_RESERVA"));
                s.setOrigem(rs.getString("IC_ORIGEM_RESERVA"));

                l.add(s);
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

    public void inserir(Auditoria audit) throws InserirException {

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            String sql = "{call SP_RES_CHURRASQUEIRA_CLUBE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();

            p.setInt(1, par.getSetParametro(dependencia));
            p.setInt(2, par.getSetParametro(categoriaSocio));
            p.setInt(3, par.getSetParametro(matricula));
            p.setInt(4, par.getSetParametro(dependente));
            p.setDate(5, new java.sql.Date(par.getSetParametro(dtInicio).getTime()));
            p.setDate(6, new java.sql.Date(par.getSetParametro(dtFim).getTime()));
            p.setString(7, par.getSetParametro(interessado));
            p.setString(8, par.getSetParametro(telefone));
            p.setString(9, par.getSetParametro(hhInicio.replace(":", "")));
            p.setString(10, par.getSetParametro(hhFim.replace(":", "")));
            p.setString(11, par.getSetParametro(usuario));
            p.setString(12, par.getSetParametro(supervisao));
            p.setString(13, par.getSetParametro(motivoBloqueio));

            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                if (rs.getString("MSG").equals("OK")) {
                    cn.commit();
                    audit.registrarMudanca(sql, par.getParametroFinal());
                } else {
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new InserirException(err);
                }
            } else {
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new InserirException(err);
            }

            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
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

    public static List<ReservaChurrasqueira> Consultar(Date dtInicio, Date dtFim, String nome, int cdDep, int mostraCanc, String origem) {

        ArrayList<ReservaChurrasqueira> l = new ArrayList<ReservaChurrasqueira>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;

            String sql = "SELECT "
                    + "T1.NOME_INTERESSADO, "
                    + "T1.DT_INIC_UTILIZACAO, "
                    + "T1.DT_FIM_UTILIZACAO, "
                    + "T2.DESCR_DEPENDENCIA ";

            if (origem.equals("CH")) {
                sql = sql + ", T1.DE_USER_CANC, "
                        + "T1.USER_CONFIRMACAO, "
                        + "(SELECT COUNT(*) FROM TB_CONVITE T3 WHERE T1.SEQ_RESERVA = T3.SEQ_RESERVA AND CD_STATUS_CONVITE <> 'CA') AS QT_CONVITE ";

            }

            sql = sql + "FROM "
                    + "TB_RESERVA_DEPENDENCIA T1, "
                    + "TB_DEPENDENCIA T2 "
                    + "WHERE "
                    + "T1.SEQ_DEPENDENCIA = T2.SEQ_DEPENDENCIA AND ";

            if (origem.equals("CH")) {
                sql = sql + "T1.SEQ_DEPENDENCIA < 24 ";
            } else {
                sql = sql + "T1.SEQ_DEPENDENCIA > 23 ";
            }

            if (!"".equals(nome)) {
                sql = sql + " AND T1.NOME_INTERESSADO LIKE '%" + nome + "%'";
            }
            if (cdDep > 0) {
                sql = sql + " AND T1.SEQ_DEPENDENCIA = " + cdDep;
            }
            if (mostraCanc != 1) {
                sql = sql + " AND T1.CD_STATUS_RESERVA <> 'CA'";
            }
            if (dtInicio != null) {
                sql = sql + " AND T1.DT_INIC_UTILIZACAO >= ?";
            }
            if (dtFim != null) {
                sql = sql + " AND T1.DT_INIC_UTILIZACAO < DATEADD(D, 1, ?)";
            }

            sql = sql + " ORDER BY DT_INIC_UTILIZACAO ";

            cal = cn.prepareCall(sql);
            if (dtInicio != null) {
                if (dtFim != null) {
                    cal.setDate(1, new java.sql.Date(dtInicio.getTime()));
                    cal.setDate(2, new java.sql.Date(dtFim.getTime()));
                } else {
                    cal.setDate(1, new java.sql.Date(dtInicio.getTime()));
                }
            } else if (dtFim != null) {
                cal.setDate(1, new java.sql.Date(dtFim.getTime()));
            }

            String teste = "";
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                ReservaChurrasqueira s = new ReservaChurrasqueira();

                s.setDeChurrasqueira(rs.getString("DESCR_DEPENDENCIA"));
                teste = rs.getString("NOME_INTERESSADO");
                s.setInteressado(rs.getString("NOME_INTERESSADO"));

                if (origem.equals("CH")) {
                    s.setUsuario(rs.getString("USER_CONFIRMACAO"));
                    s.setUsuarioCanc(rs.getString("DE_USER_CANC"));
                    s.setQtConvites(rs.getInt("QT_CONVITE"));
                }
                s.setDtInicio(rs.getTimestamp("DT_INIC_UTILIZACAO"));
                s.setDtFim(rs.getTimestamp("DT_FIM_UTILIZACAO"));

                l.add(s);
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

}
