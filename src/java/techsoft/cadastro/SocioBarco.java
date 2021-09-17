package techsoft.cadastro;

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
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.clube.LocalBoxNautica;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;

public class SocioBarco {

    private static final Logger log = Logger.getLogger("techsoft.cadastro.SocioBarco");

    private int id;
    private String nome;
    private CategoriaNautica categoriaNautica;
    private TipoVagaBarco tipoVaga;
    private float desconto;
    private String numCapitania;
    private int box;
    private int pes;
    private Date dataRegistro;
    private int anoFabricacao;
    private String habilitacao;
    private int tripulantes;
    private Date dataCadastro;
    private String classificacao;
    private String modelo;
    private int patrimonio;
    private boolean documentacaoCompleta;
    private int comprimentoTotal;
    private int comprimentoBoca;
    private int comprimentoPontal;
    private Date dataVencimentoSeguro;
    private Date dataVencimentoRegistro;
    private Date dataVencimentoHabilitacao;
    private String obs;
    private Socio socio;
    private int qtM2;
    private int qtGeladeira;
    private boolean emprestimoChave;
    private LocalBoxNautica localBox;
    private String ehBox;
    private Date dtIniDesconto;
    private Date dtFimDesconto;

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaNautica getCategoriaNautica() {
        return categoriaNautica;
    }

    public void setCategoriaNautica(CategoriaNautica categoriaNautica) {
        this.categoriaNautica = categoriaNautica;
    }

    public TipoVagaBarco getTipoVaga() {
        return tipoVaga;
    }

    public void setTipoVaga(TipoVagaBarco tipoVaga) {
        this.tipoVaga = tipoVaga;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    public String getNumCapitania() {
        return numCapitania;
    }

    public void setNumCapitania(String numCapitania) {
        this.numCapitania = numCapitania;
    }

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public int getPes() {
        return pes;
    }

    public void setPes(int pes) {
        this.pes = pes;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getHabilitacao() {
        return habilitacao;
    }

    public void setHabilitacao(String habilitacao) {
        this.habilitacao = habilitacao;
    }

    public int getTripulantes() {
        return tripulantes;
    }

    public void setTripulantes(int tripulantes) {
        this.tripulantes = tripulantes;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(int patrimonio) {
        this.patrimonio = patrimonio;
    }

    public boolean isDocumentacaoCompleta() {
        return documentacaoCompleta;
    }

    public void setDocumentacaoCompleta(boolean documentacaoCompleta) {
        this.documentacaoCompleta = documentacaoCompleta;
    }

    public int getComprimentoTotal() {
        return comprimentoTotal;
    }

    public void setComprimentoTotal(int comprimentoTotal) {
        this.comprimentoTotal = comprimentoTotal;
    }

    public int getComprimentoBoca() {
        return comprimentoBoca;
    }

    public void setComprimentoBoca(int comprimentoBoca) {
        this.comprimentoBoca = comprimentoBoca;
    }

    public int getComprimentoPontal() {
        return comprimentoPontal;
    }

    public void setComprimentoPontal(int comprimentoPontal) {
        this.comprimentoPontal = comprimentoPontal;
    }

    public Date getDataVencimentoSeguro() {
        return dataVencimentoSeguro;
    }

    public void setDataVencimentoSeguro(Date dataVencimentoSeguro) {
        this.dataVencimentoSeguro = dataVencimentoSeguro;
    }

    public Date getDataVencimentoRegistro() {
        return dataVencimentoRegistro;
    }

    public void setDataVencimentoRegistro(Date dataVencimentoRegistro) {
        this.dataVencimentoRegistro = dataVencimentoRegistro;
    }

    public Date getDataVencimentoHabilitacao() {
        return dataVencimentoHabilitacao;
    }

    public void setDataVencimentoHabilitacao(Date dataVencimentoHabilitacao) {
        this.dataVencimentoHabilitacao = dataVencimentoHabilitacao;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public int getQtM2() {
        return qtM2;
    }

    public void setQtM2(int qtM2) {
        this.qtM2 = qtM2;
    }

    public int getQtGeladeira() {
        return qtGeladeira;
    }

    public void setQtGeladeira(int qtGeladeira) {
        this.qtGeladeira = qtGeladeira;
    }

    public boolean isEmprestimoChave() {
        return emprestimoChave;
    }

    public void setEmprestimoChave(boolean emprestimoChave) {
        this.emprestimoChave = emprestimoChave;
    }

    public String getEhBox() {
        return ehBox;
    }

    public void setEhBox(String ehBox) {
        this.ehBox = ehBox;
    }

    public LocalBoxNautica getLocalBox() {
        return localBox;
    }

    public void setLocalBox(LocalBoxNautica localBox) {
        this.localBox = localBox;
    }

    public Date getDtIniDesconto() {
        return dtIniDesconto;
    }

    public void setDtIniDesconto(Date dtIniDesconto) {
        this.dtIniDesconto = dtIniDesconto;
    }

    public Date getDtFimDesconto() {
        return dtFimDesconto;
    }

    public void setDtFimDesconto(Date dtFimDesconto) {
        this.dtFimDesconto = dtFimDesconto;
    }

    public static List<SocioBarco> listar(Socio socio, String tipo) {

        ArrayList<SocioBarco> l = new ArrayList<SocioBarco>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_BARCO ('C', NULL, ?, ?, ?, NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL, NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL, ?)}");
            cal.setInt(1, socio.getMatricula());
            cal.setInt(2, socio.getSeqDependente());
            cal.setInt(3, socio.getIdCategoria());
            cal.setString(4, tipo);

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                SocioBarco b = new SocioBarco();

                b.id = rs.getInt("CD_BARCO");
                b.nome = rs.getString("NO_BARCO");
                b.categoriaNautica = CategoriaNautica.getInstance(rs.getString("CD_CATEGORIA_NAUTICA"));
                b.tipoVaga = TipoVagaBarco.getInstance(rs.getString("CD_TP_VAGA_ESTACIONAMENTO"));
                b.numCapitania = rs.getString("NR_CAPITANIA_BARCO");
                b.box = rs.getInt("NR_BOX_BARCO");
                b.pes = rs.getInt("NR_PES");
                b.dataRegistro = rs.getDate("DT_REGISTRO_BARCO");
                b.dataCadastro = rs.getDate("DT_CADASTRAMENTO_BARCO");
                b.comprimentoTotal = rs.getInt("NU_COMPRIMENTO_TOTAL");
                b.comprimentoBoca = rs.getInt("NU_COMPRIMENTO_BOCA");
                b.comprimentoPontal = rs.getInt("NU_COMPRIMENTO_PONTAL");
                b.anoFabricacao = rs.getInt("AA_FABRICACAO");
                b.habilitacao = rs.getString("NU_HABILITACAO");
                if (rs.getString("IC_DOCUMENTACAO") == null) {
                    b.documentacaoCompleta = false;
                } else {
                    b.documentacaoCompleta = (rs.getString("IC_DOCUMENTACAO").charAt(0) == 'S');
                }

                b.dataVencimentoSeguro = rs.getDate("DT_VENC_SEGURO");
                b.dataVencimentoRegistro = rs.getDate("DT_VENC_REGISTRO");
                b.dataVencimentoHabilitacao = rs.getDate("DT_VENC_HABILITACAO");
                b.desconto = rs.getFloat("VR_PERC_DESCONTO");
                b.patrimonio = rs.getInt("NU_PATRIMONIO");
                b.tripulantes = rs.getInt("QT_MAX_TRIPULANTE");
                if (rs.getString("IC_BOX") == null) {
                    b.ehBox = "N";
                } else {
                    b.ehBox = rs.getString("IC_BOX");
                }
                if (rs.getString("IC_EMP_CHAVE") == null) {
                    b.emprestimoChave = false;
                } else {
                    b.emprestimoChave = (rs.getString("IC_EMP_CHAVE").charAt(0) == 'S');
                }
                b.qtM2 = rs.getInt("QT_M2");
                b.qtGeladeira = rs.getInt("QT_GELADEIRA");
                b.localBox = LocalBoxNautica.getInstance(rs.getInt("CD_LOCAL"));
                b.dtIniDesconto = rs.getDate("DT_INIC_DESCONTO");
                b.dtFimDesconto = rs.getDate("DT_FIM_DESCONTO");

                l.add(b);
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

    public static List<SocioBarco> listar2(int categoria, int matricula, String nome, String capitania, String nomeBarco, String catNautica, String doc, String tipo) {

        ArrayList<SocioBarco> l = new ArrayList<SocioBarco>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            /*
             ALTER PROCEDURE [dbo].[SP_CONS_BARCO]
             @ACAO			CHAR(1)		,
             @CD_MATRICULA 		int = NULL 	,
             @CD_CATEGORIA 		smallint = NULL ,
             @CD_CATEGORIA_NAUTICA 	char (4) = NULL ,
             @NO_BARCO 		varchar (40) = NULL ,
             @NOME_PESSOA 		varchar (50) = NULL ,
             @IC_DOCUMENTACAO		char (1) = NULL,
             @CD_MATRFINAL 		int = NULL 	,
             @S_CATEGORIA 		varchar (255) = NULL ,
             @S_NAUTICA1 		varchar (255) = NULL ,
             @S_NAUTICA2 		varchar (255) = NULL ,
             @S_NAUTICA3 		varchar (255) = NULL ,
             @S_NAUTICA4 		varchar (255) = NULL ,
             @S_NAUTICA5 		varchar (255) = NULL ,
             @S_NAUTICA6 		varchar (255) = NULL ,
             @NR_CAPITANIA_BARCO	CHAR(11) = NULL
             */
            CallableStatement cal = cn.prepareCall("{call SP_CONS_BARCO ('C', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            if (matricula == 0) {
                cal.setNull(1, java.sql.Types.INTEGER);
            } else {
                cal.setInt(1, matricula);
            }
            if (categoria == 0) {
                cal.setNull(2, java.sql.Types.INTEGER);
            } else {
                cal.setInt(2, categoria);
            }
            if ("TODAS".equals(catNautica)) {
                cal.setNull(3, java.sql.Types.VARCHAR);
            } else {
                cal.setString(3, catNautica);
            }
            if ("".equals(nomeBarco)) {
                cal.setNull(4, java.sql.Types.VARCHAR);
            } else {
                cal.setString(4, nomeBarco);
            }
            if ("".equals(nome)) {
                cal.setNull(5, java.sql.Types.VARCHAR);
            } else {
                cal.setString(5, nome);
            }
            if ("T".equals(doc)) {
                cal.setNull(6, java.sql.Types.VARCHAR);
            } else {
                cal.setString(6, doc);
            }

            cal.setNull(7, java.sql.Types.INTEGER);
            cal.setNull(8, java.sql.Types.VARCHAR);
            cal.setNull(9, java.sql.Types.VARCHAR);
            cal.setNull(10, java.sql.Types.VARCHAR);
            cal.setNull(11, java.sql.Types.VARCHAR);
            cal.setNull(12, java.sql.Types.VARCHAR);
            cal.setNull(13, java.sql.Types.VARCHAR);
            cal.setNull(14, java.sql.Types.VARCHAR);
            if ("".equals(capitania)) {
                cal.setNull(15, java.sql.Types.VARCHAR);
            } else {
                cal.setString(15, capitania);
            }

            if ("T".equals(tipo)) {
                cal.setNull(16, java.sql.Types.VARCHAR);
            } else {
                cal.setString(16, tipo);
            }

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                SocioBarco b = new SocioBarco();

                b.socio = Socio.getInstance(rs.getInt("CD_MATRICULA"), rs.getInt("SEQ_DEPENDENTE"), rs.getInt("CD_CATEGORIA"));
                b.id = rs.getInt("CD_BARCO");
                b.nome = rs.getString("NO_BARCO");
                b.categoriaNautica = CategoriaNautica.getInstance(rs.getString("CD_CATEGORIA_NAUTICA"));
                b.localBox = LocalBoxNautica.getInstance(rs.getInt("CD_LOCAL"));
                if (rs.getString("IC_BOX") == null) {
                    b.ehBox = "N";
                } else {
                    b.ehBox = rs.getString("IC_BOX");
                }

                l.add(b);
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

    public static SocioBarco getInstance(int id) {
        SocioBarco b = null;
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            ResultSet rs = cn.createStatement().executeQuery("SELECT * FROM TB_BARCO WHERE CD_BARCO = " + id);
            while (rs.next()) {
                b = new SocioBarco();

                b.id = rs.getInt("CD_BARCO");
                if (rs.getString("IC_BOX") == null) {
                    b.ehBox = "N";
                } else {
                    b.ehBox = rs.getString("IC_BOX");
                }
                if (b.ehBox.equals("S")) {
                    b.nome = "BOX (" + rs.getString("NR_BOX_BARCO") + ")";
                } else {
                    b.nome = rs.getString("NO_BARCO");
                }

                b.categoriaNautica = CategoriaNautica.getInstance(rs.getString("CD_CATEGORIA_NAUTICA"));
                b.tipoVaga = TipoVagaBarco.getInstance(rs.getString("CD_TP_VAGA_ESTACIONAMENTO"));
                b.numCapitania = rs.getString("NR_CAPITANIA_BARCO");
                b.box = rs.getInt("NR_BOX_BARCO");
                b.pes = rs.getInt("NR_PES");
                b.dataRegistro = rs.getDate("DT_REGISTRO_BARCO");
                b.dataCadastro = rs.getDate("DT_CADASTRAMENTO_BARCO");
                b.modelo = rs.getString("DE_MODELO");
                b.classificacao = rs.getString("DE_CLASSIFICACAO");
                b.comprimentoTotal = rs.getInt("NU_COMPRIMENTO_TOTAL");
                b.comprimentoBoca = rs.getInt("NU_COMPRIMENTO_BOCA");
                b.comprimentoPontal = rs.getInt("NU_COMPRIMENTO_PONTAL");
                b.anoFabricacao = rs.getInt("AA_FABRICACAO");
                b.habilitacao = rs.getString("NU_HABILITACAO");
                if (rs.getString("IC_DOCUMENTACAO") == null) {
                    b.documentacaoCompleta = false;
                } else {
                    b.documentacaoCompleta = (rs.getString("IC_DOCUMENTACAO").charAt(0) == 'S');
                }
                b.dataVencimentoSeguro = rs.getDate("DT_VENC_SEGURO");
                b.dataVencimentoRegistro = rs.getDate("DT_VENC_REGISTRO");
                b.dataVencimentoHabilitacao = rs.getDate("DT_VENC_HABILITACAO");
                b.desconto = rs.getFloat("VR_PERC_DESCONTO");
                b.patrimonio = rs.getInt("NU_PATRIMONIO");
                b.tripulantes = rs.getInt("QT_MAX_TRIPULANTE");
                b.obs = rs.getString("DE_OBSERVACAO");

                if (rs.getString("IC_EMP_CHAVE") == null) {
                    b.emprestimoChave = false;
                } else {
                    b.emprestimoChave = (rs.getString("IC_EMP_CHAVE").charAt(0) == 'S');
                }
                b.qtM2 = rs.getInt("QT_M2");
                b.qtGeladeira = rs.getInt("QT_GELADEIRA");
                b.localBox = LocalBoxNautica.getInstance(rs.getInt("CD_LOCAL"));
                b.dtIniDesconto = rs.getDate("DT_INIC_DESCONTO");
                b.dtFimDesconto = rs.getDate("DT_FIM_DESCONTO");

                b.socio = Socio.getInstance(rs.getInt("CD_MATRICULA"), rs.getInt("SEQ_DEPENDENTE"), rs.getInt("CD_CATEGORIA"));
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

        return b;
    }

    public void excluir(Auditoria audit) {
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            CallableStatement cal = cn.prepareCall("{call SP_BARCO ('E', ?, ?, ?, ?)}");
            cal.setInt(1, id);
            cal.setInt(2, socio.getMatricula());
            cal.setInt(3, socio.getSeqDependente());
            cal.setInt(4, socio.getIdCategoria());
            cal.executeUpdate();
            cn.commit();
            cn.close();
            audit.registrarMudanca("{call SP_BARCO ('E', ?, ?, ?, ?)}", String.valueOf(id), String.valueOf(socio.getMatricula()), String.valueOf(socio.getSeqDependente()), String.valueOf(socio.getIdCategoria()));
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    public void alterar(Auditoria audit) {

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            CallableStatement cal = cn.prepareCall("{call SP_BARCO ('A', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cal.setInt(1, par.getSetParametro(id));
            cal.setInt(2, par.getSetParametro(socio.getMatricula()));
            cal.setInt(3, par.getSetParametro(socio.getSeqDependente()));
            cal.setInt(4, par.getSetParametro(socio.getIdCategoria()));
            if (categoriaNautica == null) {
                cal.setString(5, par.getSetParametro(" "));
            } else {
                cal.setString(5, par.getSetParametro(categoriaNautica.getId()));
            }
            cal.setString(6, nome);
            if (dataRegistro == null) {
                cal.setNull(7, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(7, new java.sql.Date(par.getSetParametro(dataRegistro).getTime()));
            }
            cal.setString(8, par.getSetParametro(numCapitania));
            cal.setInt(9, par.getSetParametro(box));
            cal.setInt(10, par.getSetParametro(pes));
            if (tipoVaga == null) {
                cal.setNull(11, java.sql.Types.INTEGER);
                par.getSetNull();
                cal.setNull(12, java.sql.Types.INTEGER);
                par.getSetNull();
            } else {
                cal.setString(11, par.getSetParametro(tipoVaga.getId()));
                cal.setString(12, par.getSetParametro(tipoVaga.getId()));//sim, eh o mesmo parametro de cima
            }
            if (dataCadastro == null) {
                cal.setNull(13, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(13, new java.sql.Date(par.getSetParametro(dataCadastro).getTime()));
            }
            cal.setInt(14, par.getSetParametro(comprimentoTotal));
            cal.setInt(15, par.getSetParametro(comprimentoBoca));
            cal.setInt(16, par.getSetParametro(comprimentoPontal));
            cal.setInt(17, par.getSetParametro(anoFabricacao));
            cal.setString(18, par.getSetParametro(habilitacao));
            cal.setInt(19, par.getSetParametro(tripulantes));
            if (documentacaoCompleta) {
                cal.setString(20, par.getSetParametro("S"));
            } else {
                cal.setString(20, par.getSetParametro("N"));
            }
            if (dataVencimentoSeguro == null) {
                cal.setNull(21, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(21, new java.sql.Date(par.getSetParametro(dataVencimentoSeguro).getTime()));
            }
            if (dataVencimentoRegistro == null) {
                cal.setNull(22, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(22, new java.sql.Date(par.getSetParametro(dataVencimentoRegistro).getTime()));
            }
            if (dataVencimentoHabilitacao == null) {
                cal.setNull(23, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(23, new java.sql.Date(par.getSetParametro(dataVencimentoHabilitacao).getTime()));
            }
            cal.setString(24, par.getSetParametro(obs));
            cal.setFloat(25, par.getSetParametro(desconto));
            cal.setString(26, par.getSetParametro(classificacao));
            cal.setString(27, par.getSetParametro(modelo));
            cal.setInt(28, par.getSetParametro(patrimonio));

            if ("S".equals(ehBox)) {
                cal.setString(29, par.getSetParametro("S"));
            } else {
                cal.setString(29, par.getSetParametro("N"));
            }
            cal.setInt(30, par.getSetParametro(qtM2));
            cal.setInt(31, par.getSetParametro(qtGeladeira));
            if (emprestimoChave) {
                cal.setString(32, par.getSetParametro("S"));
            } else {
                cal.setString(32, par.getSetParametro("N"));
            }
            if (localBox == null) {
                cal.setNull(33, java.sql.Types.INTEGER);
                par.getSetNull();
            } else {
                cal.setInt(33, par.getSetParametro(localBox.getId()));
            }
            if (dtIniDesconto == null) {
                cal.setNull(34, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(34, new java.sql.Date(par.getSetParametro(dtIniDesconto).getTime()));
            }
            if (dtFimDesconto == null) {
                cal.setNull(35, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(35, new java.sql.Date(par.getSetParametro(dtFimDesconto).getTime()));
            }

            cal.executeUpdate();
            cn.commit();
            cn.close();

            audit.registrarMudanca("{call SP_BARCO ('A', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}", par.getParametroFinal());
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    public void inserir(Auditoria audit) {

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_BARCO ('I', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            ParametroAuditoria par = new ParametroAuditoria();
            cal.setInt(1, par.getSetParametro(0));
            cal.setInt(2, par.getSetParametro(socio.getMatricula()));
            cal.setInt(3, par.getSetParametro(socio.getSeqDependente()));
            cal.setInt(4, par.getSetParametro(socio.getIdCategoria()));
            if (categoriaNautica == null) {
                cal.setString(5, par.getSetParametro(" "));
            } else {
                cal.setString(5, par.getSetParametro(categoriaNautica.getId()));
            }

            cal.setString(6, par.getSetParametro(nome));
            if (dataRegistro == null) {
                cal.setNull(7, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(7, new java.sql.Date(par.getSetParametro(dataRegistro).getTime()));
            }
            cal.setString(8, par.getSetParametro(numCapitania));
            cal.setInt(9, par.getSetParametro(box));
            cal.setInt(10, par.getSetParametro(pes));
            if (tipoVaga == null) {
                cal.setNull(11, java.sql.Types.INTEGER);
                par.getSetNull();
                cal.setNull(12, java.sql.Types.INTEGER);
                par.getSetNull();
            } else {
                cal.setString(11, par.getSetParametro(tipoVaga.getId()));
                cal.setString(12, par.getSetParametro(tipoVaga.getId()));//sim, eh o mesmo parametro de cima
            }
            if (dataCadastro == null) {
                cal.setNull(13, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(13, new java.sql.Date(par.getSetParametro(dataCadastro).getTime()));
            }
            cal.setInt(14, par.getSetParametro(comprimentoTotal));
            cal.setInt(15, par.getSetParametro(comprimentoBoca));
            cal.setInt(16, par.getSetParametro(comprimentoPontal));
            cal.setInt(17, par.getSetParametro(anoFabricacao));
            cal.setString(18, par.getSetParametro(habilitacao));
            cal.setInt(19, par.getSetParametro(tripulantes));
            if (documentacaoCompleta) {
                cal.setString(20, par.getSetParametro("S"));
            } else {
                cal.setString(20, par.getSetParametro("N"));
            }
            if (dataVencimentoSeguro == null) {
                cal.setNull(21, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(21, new java.sql.Date(par.getSetParametro(dataVencimentoSeguro).getTime()));
            }
            if (dataVencimentoRegistro == null) {
                cal.setNull(22, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(22, new java.sql.Date(par.getSetParametro(dataVencimentoRegistro).getTime()));
            }
            if (dataVencimentoHabilitacao == null) {
                cal.setNull(23, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(23, new java.sql.Date(par.getSetParametro(dataVencimentoHabilitacao).getTime()));
            }
            cal.setString(24, par.getSetParametro(obs));
            cal.setFloat(25, par.getSetParametro(desconto));
            cal.setString(26, par.getSetParametro(classificacao));
            cal.setString(27, par.getSetParametro(modelo));
            cal.setInt(28, par.getSetParametro(patrimonio));
            if ("S".equals(ehBox)) {
                cal.setString(29, par.getSetParametro("S"));
            } else {
                cal.setString(29, par.getSetParametro("N"));
            }
            cal.setInt(30, par.getSetParametro(qtM2));
            cal.setInt(31, par.getSetParametro(qtGeladeira));
            if (emprestimoChave) {
                cal.setString(32, par.getSetParametro("S"));
            } else {
                cal.setString(32, par.getSetParametro("N"));
            }
            if (localBox == null) {
                cal.setNull(33, java.sql.Types.INTEGER);
                par.getSetNull();
            } else {
                cal.setInt(33, par.getSetParametro(localBox.getId()));
            }
            if (dtIniDesconto == null) {
                cal.setNull(34, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(34, new java.sql.Date(par.getSetParametro(dtIniDesconto).getTime()));
            }
            if (dtFimDesconto == null) {
                cal.setNull(35, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(35, new java.sql.Date(par.getSetParametro(dtFimDesconto).getTime()));
            }

            cal.executeUpdate();
            cn.commit();
            cn.close();

            audit.registrarMudanca("{call SP_BARCO ('I', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}", par.getParametroFinal());
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    public void atualizarFoto(File f) {

        Connection cn = null;
        String sql = "select top 1 cd_barco, foto_barco, cd_foto_barco"
                + " from tb_foto_barco where cd_barco = ? AND 1=2";

        BufferedInputStream bin = null;

        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();

            try {
                bin = new BufferedInputStream(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                log.severe(e.getMessage());
            }

            if (rs.next()) {

                rs.updateBinaryStream("FOTO_BARCO", bin);
                rs.updateRow();
            } else {
                rs.moveToInsertRow();
                rs.updateInt("CD_BARCO", id);
                rs.updateBinaryStream("FOTO_BARCO", bin);
                rs.insertRow();
            }

            cn.commit();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
            try {
                bin.close();
            } catch (IOException e) {
                log.severe(e.getMessage());
            }
        }

    }

    public void excluirFoto(int id) {

        Connection cn = null;
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("DELETE from tb_foto_barco where cd_foto_barco = ?");
            p.setInt(1, id);
            p.executeUpdate();

            cn.commit();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }

    public List<Integer> listarFotos() {

        ArrayList<Integer> l = new ArrayList<Integer>();
        Connection cn = null;

        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("select * from tb_foto_barco where cd_barco = ?");
            p.setInt(1, id);

            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                l.add(rs.getInt("CD_FOTO_BARCO"));
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
