package techsoft.caixa;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.operacoes.Taxa;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.InserirException;

public class TaxaIndividual {

    private int id;
    private int taxa;
    private int matricula;
    private int categoria;
    private int dependente;
    private int mes;
    private int ano;
    private Date dtGeracao;
    private float valor;
    private float saldoPrePago;
    private String situacao;
    private Date dtCancelamento;
    private String userInclusao;
    private String userCancelamento;
    private String observacao;
    private int carro;
    private int idFuncionario;
    private int idCombustivel;
    private float vrCombustivel;
    private float qtLitros;

    private static final Logger log = Logger.getLogger("techsoft.caixa.TaxaIndividual");

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaxa() {
        return taxa;
    }

    public void setTaxa(int taxa) {
        this.taxa = taxa;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getDependente() {
        return dependente;
    }

    public void setDependente(int dependente) {
        this.dependente = dependente;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Date getDtGeracao() {
        return dtGeracao;
    }

    public void setDtGeracao(Date dtGeracao) {
        this.dtGeracao = dtGeracao;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getSaldoPrePago() {
        return saldoPrePago;
    }

    public void setSaldoPrePago(float saldoPrePago) {
        this.saldoPrePago = saldoPrePago;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDtCancelamento() {
        return dtCancelamento;
    }

    public void setDtCancelamento(Date dtCancelamento) {
        this.dtCancelamento = dtCancelamento;
    }

    public String getUserInclusao() {
        return userInclusao;
    }

    public void setUserInclusao(String userInclusao) {
        this.userInclusao = userInclusao;
    }

    public String getUserCancelamento() {
        return userCancelamento;
    }

    public void setUserCancelamento(String userCancelamento) {
        this.userCancelamento = userCancelamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getCarro() {
        return carro;
    }

    public void setCarro(int carro) {
        this.carro = carro;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public int getIdCombustivel() {
        return idCombustivel;
    }

    public void setIdCombustivel(int idCombustivel) {
        this.idCombustivel = idCombustivel;
    }

    public float getVrCombustivel() {
        return vrCombustivel;
    }

    public void setVrCombustivel(float vrCombustivel) {
        this.vrCombustivel = vrCombustivel;
    }

    public float getQtLitros() {
        return qtLitros;
    }

    public void setQtLitros(float qtLitros) {
        this.qtLitros = qtLitros;
    }

    public static TaxaIndividual getInstance(int id) {
        String sql = "SELECT "
                + "* "
                + "FROM TB_VAL_TAXA_INDIVIDUAL T1 "
                + "WHERE NU_SEQ_TAXA_INDIVIDUAL = ? ";
        Connection cn = null;
        TaxaIndividual i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                i = new TaxaIndividual();
                i.setId(rs.getInt("NU_SEQ_TAXA_INDIVIDUAL"));
                i.setMatricula(rs.getInt("CD_MATRICULA"));
                i.setCategoria(rs.getInt("CD_cATEGORIA"));
                i.setDependente(rs.getInt("SEQ_DEPENDENTE"));
                i.setTaxa(rs.getInt("CD_TX_ADMINISTRATIVA"));
                i.setValor(rs.getFloat("VR_TAXA"));
                i.setMes(rs.getInt("MM_COBRANCA"));
                i.setAno(rs.getInt("AA_COBRANCA"));
                i.setDtGeracao(rs.getDate("DT_GERACAO_TAXA"));

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

    public static TaxaIndividual getInstancePre(int id) {
        String sql = "SELECT "
                + "* "
                + "FROM TB_VAL_PRE_PAGO T1 "
                + "WHERE NU_SEQ_PRE_PAGO = ? ";
        Connection cn = null;
        TaxaIndividual i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                i = new TaxaIndividual();
                i.setId(rs.getInt("NU_SEQ_PRE_PAGO"));
                i.setMatricula(rs.getInt("CD_MATRICULA"));
                i.setCategoria(rs.getInt("CD_cATEGORIA"));
                i.setDependente(rs.getInt("SEQ_DEPENDENTE"));
                i.setTaxa(rs.getInt("CD_TX_ADMINISTRATIVA"));
                i.setValor(rs.getFloat("VR_MOVIMENTO"));
                i.setIdFuncionario(rs.getInt("CD_FUNCIONARIO"));

                sql = "{CALL SP_RECUPERA_SALDO_PRE_PAGO (" + rs.getInt("CD_MATRICULA") + "," + rs.getInt("CD_CATEGORIA") + "," + rs.getInt("SEQ_DEPENDENTE") + "," + rs.getInt("CD_FUNCIONARIO") + ")}";
                cn = Pool.getInstance().getConnection();
                ResultSet rs2 = cn.createStatement().executeQuery(sql);
                if (!rs2.next()) {
                    i.setSaldoPrePago(0);
                } else {
                    i.setSaldoPrePago(rs2.getFloat("VR_SALDO_ATUAL"));
                }

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

    public void incluir(int qtVezes, Auditoria audit) throws InserirException {
        int vMes = 0;
        int vAno = 0;
        Connection cn = null;
        CallableStatement cal = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "";
            /*
             @CD_TX_ADMINISTRATIVA	SMALLINT	,
             @CD_MATRICULA 		INT		,
             @SEQ_DEPENDENTE 	SMALLINT 	,
             @CD_CATEGORIA 		SMALLINT 	,
             @MM_COBRANCA 		SMALLINT 	,
             @AA_COBRANCA 		SMALLINT 	,
             @VR_TAXA		MONEY		,
             @USER_INCLUSAO		VARCHAR(12)	,
             @IC_RETORNO		CHAR(1)	= NULL	,
             @DE_OBSERVACAO		VARCHAR(255) = NULL,
             @SEQ_RESERVA		INT = NULL,
             @NR_CONVITE		NUMERIC(9) = NULL,
             @NU_SEQ_MASSAGEM	INT = NULL,
             @CD_CARRO_PESSOA		INT = NULL
             ?	@NU_SEQ_AGENDAMENTO		INT = NULL,
             @CD_COMBUSTIVEL		SMALLINT = NULL,
             @VR_COMBUSTIVEL		FLOAT = NULL,
             @QT_LITROS			FLOAT = NULL
            
             */

            vMes = mes;
            vAno = ano;
            for (int i = 1; i <= qtVezes; i++) {

                sql = " {CALL SP_INCLUI_TAXA_INDIVIDUAL (?, ?, ?, ?, ?, ?, ?, ?, 'S', ?, NULL, NULL, NULL, ?, NULL, ?, ?, ?)}";
                cal = cn.prepareCall(sql);
                ParametroAuditoria par = new ParametroAuditoria();

                cal.setInt(1, par.getSetParametro(taxa));
                cal.setInt(2, par.getSetParametro(matricula));
                cal.setInt(3, par.getSetParametro(dependente));
                cal.setInt(4, par.getSetParametro(categoria));
                cal.setInt(5, par.getSetParametro(vMes));
                cal.setInt(6, par.getSetParametro(vAno));
                cal.setFloat(7, par.getSetParametro(valor));
                cal.setString(8, par.getSetParametro(userInclusao));

                if ("".equals(observacao) || observacao == null) {
                    cal.setNull(9, java.sql.Types.VARCHAR);
                    par.getSetNull();
                } else {
                    cal.setString(9, par.getSetParametro(observacao));
                }

                if (carro > 0) {
                    cal.setInt(10, par.getSetParametro(carro));
                } else {
                    cal.setNull(10, java.sql.Types.INTEGER);
                    par.getSetNull();
                }

                Taxa tx = Taxa.getInstance(taxa);
                if (tx.getSelecionaCarro() != null && tx.getSelecionaCarro().equals("S")) {
                    cal.setInt(11, par.getSetParametro(idCombustivel));
                    cal.setFloat(12, par.getSetParametro(vrCombustivel));
                    cal.setFloat(13, par.getSetParametro(qtLitros));
                } else {
                    cal.setNull(11, java.sql.Types.INTEGER);
                    par.getSetNull();
                    cal.setNull(12, java.sql.Types.FLOAT);
                    par.getSetNull();
                    cal.setNull(13, java.sql.Types.FLOAT);
                    par.getSetNull();
                }

                ResultSet rs = cal.executeQuery();
                if (rs.next()) {
                    id = rs.getInt("NU_SEQ_TAXA_INDIVIDUAL");
                    cn.commit();
                    audit.registrarMudanca(sql, par.getParametroFinal());

                    vMes++;
                    if (vMes > 12) {
                        vMes = 1;
                        vAno++;
                    }
                } else {
                    String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                    log.warning(err);
                    throw new InserirException(err);
                }

            }

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new InserirException(ex.getMessage());
            }

            log.severe(e.getMessage());
            throw new InserirException(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new InserirException(e.getMessage());
            }
        }
    }

    public void incluirPrePago(Auditoria audit) throws InserirException {
        Connection cn = null;
        CallableStatement cal = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "";
            /*
             @CD_MATRICULA			INT,
             @CD_CATEGORIA			SMALLINT,
             @SEQ_DEPENDENTE			SMALLINT,
             @CD_FUNCIONARIO			INT,
             @CD_TX_ADMINISTRATIVA	SMALLINT,
             @VR_MOVIMENTO			MONEY,
             @USER_INCLUSAO			VARCHAR(12),
             @CD_CARRO				INT
             */

            sql = " {CALL SP_INCLUI_DEBITO_PRE_PAGO (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            cal = cn.prepareCall(sql);
            ParametroAuditoria par = new ParametroAuditoria();

            if (idFuncionario > 0) {
                cal.setNull(1, java.sql.Types.VARCHAR);
                par.getSetNull();
                cal.setNull(2, java.sql.Types.VARCHAR);
                par.getSetNull();
                cal.setNull(3, java.sql.Types.VARCHAR);
                par.getSetNull();
                cal.setInt(4, par.getSetParametro(idFuncionario));
            } else {
                cal.setInt(1, par.getSetParametro(matricula));
                cal.setInt(2, par.getSetParametro(dependente));
                cal.setInt(3, par.getSetParametro(categoria));
                cal.setNull(4, java.sql.Types.VARCHAR);
                par.getSetNull();
            }
            cal.setInt(5, par.getSetParametro(taxa));
            cal.setFloat(6, par.getSetParametro(valor));
            cal.setString(7, par.getSetParametro(userInclusao));

            if (carro > 0) {
                cal.setInt(8, par.getSetParametro(carro));
            } else {
                cal.setNull(8, java.sql.Types.INTEGER);
                par.getSetNull();
            }
            
            Taxa tx = Taxa.getInstance(taxa);
            if (tx.getSelecionaCarro() != null && tx.getSelecionaCarro().equals("S")) {
                cal.setInt(9, par.getSetParametro(idCombustivel));
                cal.setFloat(10, par.getSetParametro(vrCombustivel));
                cal.setFloat(11, par.getSetParametro(qtLitros));
            } else {
                cal.setNull(9, java.sql.Types.INTEGER);
                par.getSetNull();
                cal.setNull(10, java.sql.Types.FLOAT);
                par.getSetNull();
                cal.setNull(11, java.sql.Types.FLOAT);
                par.getSetNull();
            }

            ResultSet rs = cal.executeQuery();
            if (rs.next()) {
                if (rs.getString("RETORNO").equals("OK")) {
                    id = rs.getInt("NU_SEQ_PRE_PAGO");
                    saldoPrePago = rs.getFloat("VR_SALDO_ATUAL");
                    cn.commit();
                    audit.registrarMudanca(sql, par.getParametroFinal());
                } else {
                    String err = rs.getString("RETORNO");
                    log.warning(err);
                    throw new InserirException(err);
                }
            } else {
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new InserirException(err);
            }

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new InserirException(ex.getMessage());
            }

            log.severe(e.getMessage());
            throw new InserirException(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new InserirException(e.getMessage());
            }
        }
    }

}
