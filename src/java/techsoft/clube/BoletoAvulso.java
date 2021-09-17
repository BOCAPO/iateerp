package techsoft.clube;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.operacoes.Taxa;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;
import techsoft.tabelas.InserirException;

public class BoletoAvulso {

    private int id;
    private Taxa taxa;
    private int matricula;
    private int categoria;
    private int dependente;
    private String sacado;
    private BigDecimal valor;
    private BigDecimal desconto;
    private BigDecimal encargos;
    private BigDecimal vrPago;
    private Date dtGeracao;
    private Date dtVencimento;
    private Date dtPagamento;
    private Date dtBaixa;
    private Date dtCancelamento;
    private String situacao;

    private static final Logger log = Logger.getLogger("techsoft.clube.AchadosPerdidos");

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Taxa getTaxa() {
        return taxa;
    }

    public void setTaxa(Taxa taxa) {
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

    public String getSacado() {
        return sacado;
    }

    public void setSacado(String sacado) {
        this.sacado = sacado;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getEncargos() {
        return encargos;
    }

    public void setEncargos(BigDecimal encargos) {
        this.encargos = encargos;
    }

    public BigDecimal getVrPago() {
        return vrPago;
    }

    public void setVrPago(BigDecimal vrPago) {
        this.vrPago = vrPago;
    }

    public Date getDtGeracao() {
        return dtGeracao;
    }

    public void setDtGeracao(Date dtGeracao) {
        this.dtGeracao = dtGeracao;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public Date getDtPagamento() {
        return dtPagamento;
    }

    public void setDtPagamento(Date dtPagamento) {
        this.dtPagamento = dtPagamento;
    }

    public Date getDtBaixa() {
        return dtBaixa;
    }

    public void setDtBaixa(Date dtBaixa) {
        this.dtBaixa = dtBaixa;
    }

    public Date getDtCancelamento() {
        return dtCancelamento;
    }

    public void setDtCancelamento(Date dtCancelamento) {
        this.dtCancelamento = dtCancelamento;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public static List<BoletoAvulso> listar(int taxa, String sacado, String situacao, int matricula, int categoria, int dependente) {
        ArrayList<BoletoAvulso> l = new ArrayList<BoletoAvulso>();
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_BOLETO_AVULSO ('C', null, ?, ?, ?, ?, ?, null, null, ?)}");
            cal.setInt(1, taxa);
            cal.setInt(2, matricula);
            cal.setInt(3, categoria);
            cal.setInt(4, dependente);
            cal.setString(5, sacado);
            cal.setString(6, situacao);

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                BoletoAvulso d = new BoletoAvulso();

                d.setId(rs.getInt("SEQ_BOLETO"));
                d.setTaxa(Taxa.getInstance(rs.getInt("CD_TX_ADMINISTRATIVA")));
                if (rs.getString("CD_MATRICULA") != null) {
                    d.setMatricula(rs.getInt("CD_MATRICULA"));
                    d.setCategoria(rs.getInt("CD_CATEGORIA"));
                    d.setDependente(rs.getInt("SEQ_DEPENDENTE"));
                } else {
                    d.setMatricula(0);
                    d.setCategoria(0);
                    d.setDependente(0);
                }

                d.setSacado(rs.getString("NO_SACADO"));

                d.setValor(rs.getBigDecimal("VR_BOLETO"));
                d.setDesconto(rs.getBigDecimal("VR_DESCONTO"));
                d.setEncargos(rs.getBigDecimal("VR_ENCARGOS"));
                d.setVrPago(rs.getBigDecimal("VR_PAGO"));

                d.setDtGeracao(rs.getDate("DT_GERACAO"));
                d.setDtVencimento(rs.getDate("DT_VENCIMENTO"));
                d.setDtPagamento(rs.getDate("DT_PAGAMENTO"));
                d.setDtBaixa(rs.getDate("DT_BAIXA"));
                d.setDtCancelamento(rs.getDate("DT_CANCELAMENTO"));

                d.setSituacao(rs.getString("CD_SITUACAO"));

                l.add(d);
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

    public static BoletoAvulso getInstance(int id) {
        BoletoAvulso d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_BOLETO_AVULSO ('N', ?)}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if (rs.next()) {
                d = new BoletoAvulso();

                d.setId(rs.getInt("SEQ_BOLETO"));
                d.setTaxa(Taxa.getInstance(rs.getInt("CD_TX_ADMINISTRATIVA")));
                if (rs.getString("CD_MATRICULA") != null) {
                    d.setMatricula(rs.getInt("CD_MATRICULA"));
                    d.setCategoria(rs.getInt("CD_CATEGORIA"));
                    d.setDependente(rs.getInt("SEQ_DEPENDENTE"));
                } else {
                    d.setMatricula(0);
                    d.setCategoria(0);
                    d.setDependente(0);
                }

                d.setSacado(rs.getString("NO_SACADO"));

                d.setValor(rs.getBigDecimal("VR_BOLETO"));
                d.setDesconto(rs.getBigDecimal("VR_DESCONTO"));
                d.setEncargos(rs.getBigDecimal("VR_ENCARGOS"));
                d.setVrPago(rs.getBigDecimal("VR_PAGO"));

                d.setDtGeracao(rs.getDate("DT_GERACAO"));
                d.setDtVencimento(rs.getDate("DT_VENCIMENTO"));
                d.setDtPagamento(rs.getDate("DT_PAGAMENTO"));
                d.setDtBaixa(rs.getDate("DT_BAIXA"));
                d.setDtCancelamento(rs.getDate("DT_CANCELAMENTO"));

                d.setSituacao(rs.getString("CD_SITUACAO"));

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

    public void excluir(Auditoria audit) throws ExcluirException {
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_BOLETO_AVULSO ('E', ?)}");
            cal.setInt(1, getId());
            ResultSet rs = cal.executeQuery();
            if (rs.next()) {
                if (rs.getString("MSG").equals("OK")) {
                    cn.commit();
                    audit.registrarMudanca("{call SP_BOLETO_AVULSO ('N', ?)}", String.valueOf(getId()));
                } else {
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new ExcluirException(err);
                }
            } else {
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
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }

    public void inserir(Auditoria audit) throws InserirException {

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            ParametroAuditoria par = new ParametroAuditoria();

            cal = cn.prepareCall("{call SP_BOLETO_AVULSO ('I', NULL, ?, ?, ?, ?, ?, ?, ?, NULL)}");

            /*
             01 - @OPERACAO		CHAR(1)                 ,
             02 - @SEQ_BOLETO		INT		= NULL	,
             03 - @CD_TX_ADMINISTRATIVA	SMALLINT	= NULL	,
             04 - @CD_MATRICULA		INT		= NULL	,
             05 - @CD_CATEGORIA		SMALLINT	= NULL	,
             06 - @SEQ_DEPENDENTE	SMALLINT	= NULL	,
             07 - @NO_SACADO		VARCHAR(50)	= NULL	,
             08 - @VR_BOLETO		MONEY		= NULL	,
             09 - @DT_VENCIMENTO		DATETIME	= NULL	,
             10 - @CD_SITUACAO		CHAR(2)         = NULL	,
             */
            cal.setInt(1, par.getSetParametro(taxa.getId()));

            if (matricula > 0) {
                cal.setInt(2, par.getSetParametro(matricula));
                cal.setInt(3, par.getSetParametro(categoria));
                cal.setInt(4, par.getSetParametro(dependente));
            } else {
                cal.setNull(2, java.sql.Types.INTEGER);
                par.getSetNull();
                cal.setNull(3, java.sql.Types.INTEGER);
                par.getSetNull();
                cal.setNull(4, java.sql.Types.INTEGER);
                par.getSetNull();

            }

            cal.setString(5, par.getSetParametro(sacado));

            cal.setBigDecimal(6, valor);

            cal.setDate(7, new java.sql.Date(par.getSetParametro(dtVencimento).getTime()));

            ResultSet rs = cal.executeQuery();

            if (rs.next()) {
                if (rs.getString("MSG").equals("OK")) {
                    cn.commit();
                    audit.registrarMudanca("{call SP_ACHADOS_PERDIDOS ('I', NULL, ?, ?, ?, ?, ?, ?, ?, NULL)}", par.getParametroFinal());
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
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");

            }

            log.severe(e.getMessage());
            throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }

    public static BoletoImpresso dadosBoleto(int id, Date dtMaxPagto) {
        BoletoImpresso b = null;
        Connection cn = null;
        CallableStatement cal = null;
        ResultSet rs = null;

        b = new BoletoImpresso();

        try {
            cn = Pool.getInstance().getConnection();
            cal = cn.prepareCall("{call SP_BOLETO_AVULSO ('B', ?, NULL, NULL, NULL, NULL, NULL, NULL, ?, NULL)}");

            cal.setInt(1, id);
            cal.setDate(2, new java.sql.Date(dtMaxPagto.getTime()));

            rs = cal.executeQuery();

            if (rs.next()) {

                b.setNuConvenio(rs.getString("NR_CONVENIO_BOLETO"));
                b.setNossoNumero(rs.getString("SEQ_BOLETO"));
                b.setCodigoCedente(rs.getString("NR_AGENCIA_CEDENTE"));
                b.setNoSacado(rs.getString("NO_SACADO"));

                b.setDtDocumento(rs.getDate("DT_ATUAL"));
                b.setDtProcessamento(rs.getDate("DT_ATUAL"));
                b.setDataVencimento(rs.getDate("DT_VENCIMENTO"));
                
                b.setEdSacado(rs.getString("ENDERECO"));
                b.setEdSacadoCompl(rs.getString("COMPLEMENTO_END"));

                BigDecimal bd = new BigDecimal(Float.toString(rs.getFloat("VR_BOLETO")));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

                b.setValor(bd);

                b.setFatVenc(rs.getString("FAT_VENC"));

                b.setDataPagamento(dtMaxPagto);
            }

            cn.close();

            cn = Pool.getInstance().getConnection();
            cal = cn.prepareCall("{call SP_COD_BARRA_CARNE (?, ?, ?, ?)}");
            cal.setInt(1, Integer.parseInt(b.getNossoNumero()));
            cal.setBigDecimal(2, b.getValor());
            cal.setDate(3, new java.sql.Date(dtMaxPagto.getTime()));
            cal.setString(4, "1");

            rs = cal.executeQuery();

            if (rs.next()) {
                b.setCodBarras(rs.getString("COD_BARRA"));
                b.setLinhaDigitavel(rs.getString("LINHA_DIGITAVEL"));
            }

            cn.commit();
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

        return b;
    }

}
