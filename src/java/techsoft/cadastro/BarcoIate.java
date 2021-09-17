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
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;

public class BarcoIate {

    private static final Logger log = Logger.getLogger("techsoft.cadastro.BarcoIate");

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

    public static List<BarcoIate> listar() {

        ArrayList<BarcoIate> l = new ArrayList<BarcoIate>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_BARCO_IATE ('C')}");

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                BarcoIate b = new BarcoIate();

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
                b.tripulantes = rs.getInt("QT_MAX_TRIPULANTE");

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

    public static BarcoIate getInstance(int id) {
        BarcoIate b = null;
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            ResultSet rs = cn.createStatement().executeQuery("SELECT * FROM TB_BARCO_IATE WHERE CD_BARCO = " + id);
            while (rs.next()) {
                b = new BarcoIate();

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
                b.tripulantes = rs.getInt("QT_MAX_TRIPULANTE");

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

            CallableStatement cal = cn.prepareCall("{call SP_BARCO_IATE ('E', ?)}");
            cal.setInt(1, id);
            cal.executeUpdate();
            cn.commit();
            cn.close();
            audit.registrarMudanca("{call SP_BARCO_IATE ('E', ?)}", String.valueOf(id));
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
            /*
             0 - @ACAO			CHAR(1)		,
             1 - @CD_BARCO 		smallint = NULL ,
             2 - @CD_CATEGORIA_NAUTICA 	char (4) = NULL ,
             3 - @NO_BARCO 		varchar (40) = NULL ,
             4 - @DT_REGISTRO_BARCO 	datetime = NULL ,
             5 - @NR_CAPITANIA_BARCO 	char (11) = NULL ,
             6 - @NR_BOX_BARCO 		smallint = NULL ,
             7 - @NR_PES 		smallint = NULL ,
             8 - @CD_TP_VAGA_COBRANCA 	char (2) = NULL ,
             9 - @CD_TP_VAGA_ESTACIONAMENTO 	char (2) = NULL ,
             10 - @DT_CADASTRAMENTO_BARCO datetime = NULL ,
             11 - @NU_COMPRIMENTO_TOTAL 	smallint = NULL ,
             12 - @NU_COMPRIMENTO_BOCA 	smallint = NULL ,
             13 - @NU_COMPRIMENTO_PONTAL 	smallint = NULL ,
             14 - @AA_FABRICACAO 		smallint = NULL ,
             15 - @NU_HABILITACAO 	varchar (20) = NULL ,
             16 - @QT_MAX_TRIPULANTE 	smallint = NULL ,
             17 - @IC_DOCUMENTACAO 	char (1) = NULL ,
             18 - @DT_VENC_SEGURO 	datetime = NULL ,
             19 - @DT_VENC_REGISTRO 	datetime = NULL ,
             20 - @DT_VENC_HABILITACAO 	datetime = NULL 
             */

            CallableStatement cal = cn.prepareCall("{call SP_BARCO_IATE ('A', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            ParametroAuditoria par = new ParametroAuditoria();

            cal.setInt(1, par.getSetParametro(id));
            cal.setString(2, par.getSetParametro(categoriaNautica.getId()));
            cal.setString(3, par.getSetParametro(nome));
            if (dataRegistro == null) {
                cal.setNull(4, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(4, new java.sql.Date(par.getSetParametro(dataRegistro).getTime()));
            }
            cal.setString(5, par.getSetParametro(numCapitania));
            cal.setInt(6, par.getSetParametro(box));
            cal.setInt(7, par.getSetParametro(pes));
            cal.setString(8, par.getSetParametro(tipoVaga.getId()));
            cal.setString(9, par.getSetParametro(tipoVaga.getId()));//sim, eh o mesmo parametro de cima
            if (dataCadastro == null) {
                cal.setNull(10, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(10, new java.sql.Date(par.getSetParametro(dataCadastro).getTime()));
            }
            cal.setInt(11, par.getSetParametro(comprimentoTotal));
            cal.setInt(12, par.getSetParametro(comprimentoBoca));
            cal.setInt(13, par.getSetParametro(comprimentoPontal));
            cal.setInt(14, par.getSetParametro(anoFabricacao));
            cal.setString(15, par.getSetParametro(habilitacao));
            cal.setInt(16, par.getSetParametro(tripulantes));
            if (documentacaoCompleta) {
                cal.setString(17, par.getSetParametro("S"));
            } else {
                cal.setString(17, par.getSetParametro("N"));
            }
            if (dataVencimentoSeguro == null) {
                cal.setNull(18, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(18, new java.sql.Date(par.getSetParametro(dataVencimentoSeguro).getTime()));
            }
            if (dataVencimentoRegistro == null) {
                cal.setNull(19, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(19, new java.sql.Date(par.getSetParametro(dataVencimentoRegistro).getTime()));
            }
            if (dataVencimentoHabilitacao == null) {
                cal.setNull(20, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(20, new java.sql.Date(par.getSetParametro(dataVencimentoHabilitacao).getTime()));
            }

            cal.executeUpdate();
            cn.commit();
            cn.close();

            audit.registrarMudanca("{call SP_BARCO_IATE ('A', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}", par.getParametroFinal());
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
            /*
             0 - @ACAO			CHAR(1)		,
             1 - @CD_BARCO 		smallint = NULL ,
             2 - @CD_CATEGORIA_NAUTICA 	char (4) = NULL ,
             3 - @NO_BARCO 		varchar (40) = NULL ,
             4 - @DT_REGISTRO_BARCO 	datetime = NULL ,
             5 - @NR_CAPITANIA_BARCO 	char (11) = NULL ,
             6 - @NR_BOX_BARCO 		smallint = NULL ,
             7 - @NR_PES 		smallint = NULL ,
             8 - @CD_TP_VAGA_COBRANCA 	char (2) = NULL ,
             9 - @CD_TP_VAGA_ESTACIONAMENTO 	char (2) = NULL ,
             10 - @DT_CADASTRAMENTO_BARCO datetime = NULL ,
             11 - @NU_COMPRIMENTO_TOTAL 	smallint = NULL ,
             12 - @NU_COMPRIMENTO_BOCA 	smallint = NULL ,
             13 - @NU_COMPRIMENTO_PONTAL 	smallint = NULL ,
             14 - @AA_FABRICACAO 		smallint = NULL ,
             15 - @NU_HABILITACAO 	varchar (20) = NULL ,
             16 - @QT_MAX_TRIPULANTE 	smallint = NULL ,
             17 - @IC_DOCUMENTACAO 	char (1) = NULL ,
             18 - @DT_VENC_SEGURO 	datetime = NULL ,
             19 - @DT_VENC_REGISTRO 	datetime = NULL ,
             20 - @DT_VENC_HABILITACAO 	datetime = NULL 
             */
            CallableStatement cal = cn.prepareCall("{call SP_BARCO_IATE ('I', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            ParametroAuditoria par = new ParametroAuditoria();
            cal.setInt(1, par.getSetParametro(0));
            cal.setString(2, par.getSetParametro(categoriaNautica.getId()));
            cal.setString(3, par.getSetParametro(nome));
            if (dataRegistro == null) {
                cal.setNull(4, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(4, new java.sql.Date(par.getSetParametro(dataRegistro).getTime()));
            }
            cal.setString(5, par.getSetParametro(numCapitania));
            cal.setInt(6, par.getSetParametro(box));
            cal.setInt(7, par.getSetParametro(pes));
            cal.setString(8, par.getSetParametro(tipoVaga.getId()));
            cal.setString(9, par.getSetParametro(tipoVaga.getId()));//sim, eh o mesmo parametro de cima
            if (dataCadastro == null) {
                cal.setNull(10, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(10, new java.sql.Date(par.getSetParametro(dataCadastro).getTime()));
            }
            cal.setInt(11, par.getSetParametro(comprimentoTotal));
            cal.setInt(12, par.getSetParametro(comprimentoBoca));
            cal.setInt(13, par.getSetParametro(comprimentoPontal));
            cal.setInt(14, par.getSetParametro(anoFabricacao));
            cal.setString(15, par.getSetParametro(habilitacao));
            cal.setInt(16, par.getSetParametro(tripulantes));
            if (documentacaoCompleta) {
                cal.setString(17, par.getSetParametro("S"));
            } else {
                cal.setString(17, par.getSetParametro("N"));
            }
            if (dataVencimentoSeguro == null) {
                cal.setNull(18, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(18, new java.sql.Date(par.getSetParametro(dataVencimentoSeguro).getTime()));
            }
            if (dataVencimentoRegistro == null) {
                cal.setNull(19, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(19, new java.sql.Date(par.getSetParametro(dataVencimentoRegistro).getTime()));
            }
            if (dataVencimentoHabilitacao == null) {
                cal.setNull(20, java.sql.Types.DATE);
                par.getSetNull();
            } else {
                cal.setDate(20, new java.sql.Date(par.getSetParametro(dataVencimentoHabilitacao).getTime()));
            }

            cal.executeUpdate();
            cn.commit();
            cn.close();

            audit.registrarMudanca("{call SP_BARCO_IATE ('I', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}", par.getParametroFinal());
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

}
