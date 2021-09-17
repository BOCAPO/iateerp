
package techsoft.clube;

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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;
import techsoft.tabelas.Funcionario;
import techsoft.tabelas.InserirException;
import techsoft.tabelas.Setor;

public class AchadosPerdidos {

    private int id;
    private String descricao;
    private Setor setorOrigem;
    private Integer qtObjeto;
    private Integer unidade;
    private String condicao;
    private Integer definicao;
    private Setor setorEncontrado;
    private Integer modelo;
    private Integer sexo;
    private String marca;
    private String cor;
    private Integer tamanho;
    private Date dtEntrada;
    private Date dtCatalogacao;
    private Funcionario funcCatalogador;
    private String nomeEntrega;
    private Integer nrPrateleira;
    private Integer perfilRetirante;
    private String nomeRetirante;
    private String docRetirante;
    private String telRetirante;
    private Funcionario funcDevolucao;
    private Date dtDevolucao;
    private Integer situacao;
    private Date dtAdocao;
    
    private static final Logger log = Logger.getLogger("techsoft.clube.AchadosPerdidos");

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
    public Setor getSetorOrigem() {
        return setorOrigem;
    }
    public void setSetorOrigem(Setor setorOrigem) {
        this.setorOrigem = setorOrigem;
    }
    public Integer getQtObjeto() {
        return qtObjeto;
    }
    public void setQtObjeto(Integer qtObjeto) {
        this.qtObjeto = qtObjeto;
    }
    public Integer getUnidade() {
        return unidade;
    }
    public void setUnidade(Integer unidade) {
        this.unidade = unidade;
    }
    public String getCondicao() {
        return condicao;
    }
    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }
    public Integer getDefinicao() {
        return definicao;
    }
    public void setDefinicao(Integer definicao) {
        this.definicao = definicao;
    }
    public Setor getSetorEncontrado() {
        return setorEncontrado;
    }
    public void setSetorEncontrado(Setor setorEncontrado) {
        this.setorEncontrado = setorEncontrado;
    }
    public Integer getModelo() {
        return modelo;
    }
    public void setModelo(Integer modelo) {
        this.modelo = modelo;
    }
    public Integer getSexo() {
        return sexo;
    }
    public void setSexo(Integer sexo) {
        this.sexo = sexo;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getCor() {
        return cor;
    }
    public void setCor(String cor) {
        this.cor = cor;
    }
    public Integer getTamanho() {
        return tamanho;
    }
    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }
    public Date getDtEntrada() {
        return dtEntrada;
    }
    public void setDtEntrada(Date dtEntrada) {
        this.dtEntrada = dtEntrada;
    }
    public Date getDtCatalogacao() {
        return dtCatalogacao;
    }
    public void setDtCatalogacao(Date dtCatalogacao) {
        this.dtCatalogacao = dtCatalogacao;
    }
    public Funcionario getFuncCatalogador() {
        return funcCatalogador;
    }
    public void setFuncCatalogador(Funcionario funcCatalogador) {
        this.funcCatalogador = funcCatalogador;
    }
    public String getNomeEntrega() {
        return nomeEntrega;
    }
    public void setNomeEntrega(String nomeEntrega) {
        this.nomeEntrega = nomeEntrega;
    }
    public Integer getNrPrateleira() {
        return nrPrateleira;
    }
    public void setNrPrateleira(Integer nrPrateleira) {
        this.nrPrateleira = nrPrateleira;
    }
    public Integer getPerfilRetirante() {
        return perfilRetirante;
    }
    public void setPerfilRetirante(Integer perfilRetirante) {
        this.perfilRetirante = perfilRetirante;
    }
    public String getNomeRetirante() {
        return nomeRetirante;
    }
    public void setNomeRetirante(String nomeRetirante) {
        this.nomeRetirante = nomeRetirante;
    }
    public String getDocRetirante() {
        return docRetirante;
    }
    public void setDocRetirante(String docRetirante) {
        this.docRetirante = docRetirante;
    }
    public String getTelRetirante() {
        return telRetirante;
    }
    public void setTelRetirante(String telRetirante) {
        this.telRetirante = telRetirante;
    }
    public Funcionario getFuncDevolucao() {
        return funcDevolucao;
    }
    public void setFuncDevolucao(Funcionario funcDevolucao) {
        this.funcDevolucao = funcDevolucao;
    }
    public Date getDtDevolucao() {
        return dtDevolucao;
    }
    public void setDtDevolucao(Date dtDevolucao) {
        this.dtDevolucao = dtDevolucao;
    }
    public Integer getSituacao() {
        return situacao;
    }
    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }
    public void setDtAdocao(Date dtAdocao) {
        this.dtAdocao = dtAdocao;
    }
    public Date getDtAdocao() {
        return dtAdocao;
    }
    
    public static List<AchadosPerdidos> listar(Integer setorEncontrado, Integer definicao, int situacao, String descricao, Date dtEntradaInicial, Date dtEntradaFinal){
        ArrayList<AchadosPerdidos> l = new ArrayList<AchadosPerdidos>();
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        
        try {
            cal = cn.prepareCall("{call SP_ACHADOS_PERDIDOS ('C', null, ?, null, null, null, null, ?, ?, null, null, null, null, null, ?, null, null, null, null, null, null, null, null, null, null, ?, ?)}");
            cal.setString(1, descricao);
            if (definicao==null){
                cal.setNull(2, java.sql.Types.INTEGER);
            }else{
                cal.setInt(2, definicao);
            }
            if (setorEncontrado==null){
                cal.setNull(3, java.sql.Types.INTEGER);
            }else{
                cal.setInt(3, setorEncontrado);
            }
            if (dtEntradaInicial==null){
                cal.setNull(4, java.sql.Types.DATE);
            }else{
                cal.setDate(4, new java.sql.Date(dtEntradaInicial.getTime()));
            }
            
            cal.setInt(5, situacao);
            
            if (dtEntradaFinal==null){
                cal.setNull(6, java.sql.Types.DATE);
            }else{
                cal.setDate(6, new java.sql.Date(dtEntradaFinal.getTime()));
            }

            
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                AchadosPerdidos d = new AchadosPerdidos();
                
                d.setId(rs.getInt("NU_SEQ_OBJETO"));
                d.setDescricao(rs.getString("DE_OBJETO"));
                
                if (rs.getString("CD_SETOR_ORIGEM") != null){
                    d.setSetorOrigem(Setor.getInstance(rs.getInt("CD_SETOR_ORIGEM")));
                }
                if (rs.getString("QT_OBJETO") != null){
                    d.setQtObjeto(rs.getInt("QT_OBJETO"));
                }
                if (rs.getString("CD_UNIDADE") != null){
                    d.setUnidade(rs.getInt("CD_UNIDADE"));
                }
                
                d.setCondicao(rs.getString("DE_CONDICAO"));
                
                if (rs.getString("CD_DEFINICAO") != null){
                    d.setDefinicao(rs.getInt("CD_DEFINICAO"));
                }
                if (rs.getString("CD_SETOR_ENCONTRADO") != null){
                    d.setSetorEncontrado(Setor.getInstance(rs.getInt("CD_SETOR_ENCONTRADO")));
                }
                if (rs.getString("CD_MODELO") != null){
                    d.setModelo(rs.getInt("CD_MODELO"));
                }
                if (rs.getString("CD_SEXO") != null){
                    d.setSexo(rs.getInt("CD_SEXO"));
                }
                
                d.setMarca(rs.getString("DE_MARCA"));
                d.setCor(rs.getString("DE_COR"));
                
                if (rs.getString("CD_TAMANHO") != null){
                    d.setTamanho(rs.getInt("CD_TAMANHO"));
                }
                
                d.setDtEntrada(rs.getDate("DT_ENTRADA"));
                d.setDtCatalogacao(rs.getDate("DT_CATALOGACAO"));
                
                if (rs.getString("CD_FUNC_CATALOG") != null){
                    d.setFuncCatalogador(Funcionario.getInstance(rs.getInt("CD_FUNC_CATALOG")));
                }
                if (rs.getString("NO_PESSOA_ENTREGA") != null){
                    d.setNomeEntrega(rs.getString("NO_PESSOA_ENTREGA"));
                }
                if (rs.getString("NR_PRATELEIRA") != null){
                    d.setNrPrateleira(rs.getInt("NR_PRATELEIRA"));
                }
                if (rs.getString("CD_PERFIL_RETIRANTE") != null){
                    d.setPerfilRetirante(rs.getInt("CD_PERFIL_RETIRANTE"));
                }
                d.setNomeRetirante(rs.getString("NO_RETIRANTE"));
                d.setDocRetirante(rs.getString("NR_DOCUMENTO"));
                d.setTelRetirante(rs.getString("NR_TELEFONE"));
                if (rs.getString("CD_FUNC_DEVOLUCAO") != null){
                    d.setFuncDevolucao(Funcionario.getInstance(rs.getInt("CD_FUNC_DEVOLUCAO")));
                }
                d.setDtDevolucao(rs.getDate("DT_DEVOLUCAO"));
                if (rs.getString("CD_SITUACAO") != null){
                    d.setSituacao(rs.getInt("CD_SITUACAO"));
                }
                d.setDtAdocao(rs.getDate("DT_ADOCAO"));
                
                
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

    public static AchadosPerdidos getInstance(int id){
        AchadosPerdidos d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ACHADOS_PERDIDOS ('N', ?)}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new AchadosPerdidos();
                
                d.setId(rs.getInt("NU_SEQ_OBJETO"));
                d.setDescricao(rs.getString("DE_OBJETO"));
                
                if (rs.getString("CD_SETOR_ORIGEM") != null){
                    d.setSetorOrigem(Setor.getInstance(rs.getInt("CD_SETOR_ORIGEM")));
                }
                if (rs.getString("QT_OBJETO") != null){
                    d.setQtObjeto(rs.getInt("QT_OBJETO"));
                }
                if (rs.getString("CD_UNIDADE") != null){
                    d.setUnidade(rs.getInt("CD_UNIDADE"));
                }
                
                d.setCondicao(rs.getString("DE_CONDICAO"));
                
                if (rs.getString("CD_DEFINICAO") != null){
                    d.setDefinicao(rs.getInt("CD_DEFINICAO"));
                }
                if (rs.getString("CD_SETOR_ENCONTRADO") != null){
                    d.setSetorEncontrado(Setor.getInstance(rs.getInt("CD_SETOR_ENCONTRADO")));
                }
                if (rs.getString("CD_MODELO") != null){
                    d.setModelo(rs.getInt("CD_MODELO"));
                }
                if (rs.getString("CD_SEXO") != null){
                    d.setSexo(rs.getInt("CD_SEXO"));
                }
                
                d.setMarca(rs.getString("DE_MARCA"));
                d.setCor(rs.getString("DE_COR"));
                
                if (rs.getString("CD_TAMANHO") != null){
                    d.setTamanho(rs.getInt("CD_TAMANHO"));
                }
                
                d.setDtEntrada(rs.getDate("DT_ENTRADA"));
                d.setDtCatalogacao(rs.getDate("DT_CATALOGACAO"));
                
                if (rs.getString("CD_FUNC_CATALOG") != null){
                    d.setFuncCatalogador(Funcionario.getInstance(rs.getInt("CD_FUNC_CATALOG")));
                }
                if (rs.getString("NO_PESSOA_ENTREGA") != null){
                    d.setNomeEntrega(rs.getString("NO_PESSOA_ENTREGA"));
                }
                if (rs.getString("NR_PRATELEIRA") != null){
                    d.setNrPrateleira(rs.getInt("NR_PRATELEIRA"));
                }
                if (rs.getString("CD_PERFIL_RETIRANTE") != null){
                    d.setPerfilRetirante(rs.getInt("CD_PERFIL_RETIRANTE"));
                }
                d.setNomeRetirante(rs.getString("NO_RETIRANTE"));
                d.setDocRetirante(rs.getString("NR_DOCUMENTO"));
                d.setTelRetirante(rs.getString("NR_TELEFONE"));
                if (rs.getString("CD_FUNC_DEVOLUCAO") != null){
                    d.setFuncDevolucao(Funcionario.getInstance(rs.getInt("CD_FUNC_DEVOLUCAO")));
                }
                d.setDtDevolucao(rs.getDate("DT_DEVOLUCAO"));
                if (rs.getString("CD_SITUACAO") != null){
                    d.setSituacao(rs.getInt("CD_SITUACAO"));
                }
                d.setDtAdocao(rs.getDate("DT_ADOCAO"));
                
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

    public void excluir(Auditoria audit) throws ExcluirException {
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ACHADOS_PERDIDOS ('E', ?)}");
            cal.setInt(1, getId());
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ACHADOS_PERDIDOS ('N', ?)}", String.valueOf(getId()));
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

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            ParametroAuditoria par = new ParametroAuditoria();
                    
            cal = cn.prepareCall("{call SP_ACHADOS_PERDIDOS ('I', ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            /*
            01 - @OPERACAO		CHAR(1)			,
            02 - @NU_SEQ_OBJETO		INT		= NULL	,
            03 - @DE_OBJETO		VARCHAR(50)	= NULL	,
            04 - @CD_SETOR_ORIGEM	SMALLINT	= NULL	,
            05 - @QT_OBJETO		SMALLINT	= NULL	,
            06 - @CD_UNIDADE		SMALLINT        = NULL	,
            07 - @DE_SITUACAO		VARCHAR(30)	= NULL	,
            08 - @CD_DEFINICAO		SMALLINT        = NULL	,
            09 - @CD_SETOR_ENCONTRADO	SMALLINT	= NULL	,
            10 - @CD_MODELO		SMALLINT	= NULL	,
            11 - @CD_SEXO		SMALLINT	= NULL	,
            12 - @DE_MARCA		VARCHAR(50)	= NULL	,
            13 - @DE_COR		VARCHAR(50)	= NULL	,
            14 - @CD_TAMANHO		SMALLINT	= NULL	,
            15 - @DT_ENTRADA		DATETIME	= NULL	,
            16 - @DT_CATALOGACAO	DATETIME	= NULL	,
            17 - @CD_FUNC_CATALOG	INT		= NULL	,
            18 - @CD_FUNC_ENTREGA	INT		= NULL	,
            19 - @NR_PRATELEIRA		SMALLINT	= NULL	,
            20 - @CD_PERFIL_RETIRANTE	SMALLINT	= NULL	,
            21 - @NO_RETIRANTE		VARCHAR(50)	= NULL	,
            22 - @NR_DOCUMENTO		VARCHAR(20)	= NULL	,
            23 - @NR_TELEFONE		VARCHAR(20)	= NULL	,
            24 - @CD_FUNC_DEVOLUCAO	INT		= NULL	,
            25 - @DT_DEVOLUCAO		DATETIME	= NULL  ,
            26 -?@CD_SITUACAO		SMALLINT	= NULL
            */
            
            cal.setNull(1, java.sql.Types.INTEGER);
            par.getSetNull();
                
            cal.setString(2, par.getSetParametro(descricao));
            
            if(setorOrigem == null){
                cal.setNull(3, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(3, par.getSetParametro(setorOrigem.getId()));
            }
            if(qtObjeto == null){
                cal.setNull(4, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(4, par.getSetParametro(qtObjeto));
            }
            if(unidade == null){
                cal.setNull(5, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(5, par.getSetParametro(unidade));
            }
            if(condicao == null){
                cal.setNull(6, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(6, par.getSetParametro(condicao));
            }
            if(definicao == null){
                cal.setNull(7, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(7, par.getSetParametro(definicao));
            }
            if(setorEncontrado == null){
                cal.setNull(8, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(8, par.getSetParametro(setorEncontrado.getId()));
            }
            if(modelo == null){
                cal.setNull(9, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(9, par.getSetParametro(modelo));
            }
            if(sexo == null){
                cal.setNull(10, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(10, par.getSetParametro(sexo));
            }
            if(marca == null){
                cal.setNull(11, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(11, par.getSetParametro(marca));
            }
            if(cor == null){
                cal.setNull(12, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(12, par.getSetParametro(cor));
            }
            if(tamanho == null){
                cal.setNull(13, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(13, par.getSetParametro(tamanho));
            }
            if(dtEntrada == null){
                cal.setNull(14, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(14, new java.sql.Date(par.getSetParametro(dtEntrada).getTime()));
            }
            if(dtCatalogacao == null){
                cal.setNull(15, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(15, new java.sql.Date(par.getSetParametro(dtCatalogacao).getTime()));
            }
            if(funcCatalogador == null){
                cal.setNull(16, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(16, par.getSetParametro(funcCatalogador.getId()));
            }
            if(nomeEntrega == null){
                cal.setNull(17, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(17, par.getSetParametro(nomeEntrega));
            }
            if(nrPrateleira == null){
                cal.setNull(18, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(18, par.getSetParametro(nrPrateleira));
            }
            if(perfilRetirante == null){
                cal.setNull(19, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(19, par.getSetParametro(perfilRetirante));
            }
            if(nomeRetirante == null){
                cal.setNull(20, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(20, par.getSetParametro(nomeRetirante));
            }
            if(docRetirante == null){
                cal.setNull(21, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(21, par.getSetParametro(docRetirante));
            }
            if(telRetirante == null){
                cal.setNull(22, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(22, par.getSetParametro(telRetirante));
            }
            if(funcDevolucao == null){
                cal.setNull(23, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(23, par.getSetParametro(funcDevolucao.getId()));
            }
            if(dtDevolucao == null){
                cal.setNull(24, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(24, new java.sql.Date(par.getSetParametro(dtDevolucao).getTime()));
            }
            if(situacao == null){
                cal.setNull(25, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(25, par.getSetParametro(situacao));
            }
            
            ResultSet rs = cal.executeQuery();

            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ACHADOS_PERDIDOS ('I', ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", par.getParametroFinal());
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

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ACHADOS_PERDIDOS ('A', ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            
            ParametroAuditoria par = new ParametroAuditoria();
                    
            /*
            01 - @OPERACAO		CHAR(1)			,
            02 - @NU_SEQ_OBJETO		INT		= NULL	,
            03 - @DE_OBJETO		VARCHAR(50)	= NULL	,
            04 - @CD_SETOR_ORIGEM	SMALLINT	= NULL	,
            05 - @QT_OBJETO		SMALLINT	= NULL	,
            06 - @CD_UNIDADE		SMALLINT        = NULL	,
            07 - @DE_SITUACAO		VARCHAR(30)	= NULL	,
            08 - @CD_DEFINICAO		SMALLINT        = NULL	,
            09 - @CD_SETOR_ENCONTRADO	SMALLINT	= NULL	,
            10 - @CD_MODELO		SMALLINT	= NULL	,
            11 - @CD_SEXO		SMALLINT	= NULL	,
            12 - @DE_MARCA		VARCHAR(50)	= NULL	,
            13 - @DE_COR		VARCHAR(50)	= NULL	,
            14 - @CD_TAMANHO		SMALLINT	= NULL	,
            15 - @DT_ENTRADA		DATETIME	= NULL	,
            16 - @DT_CATALOGACAO	DATETIME	= NULL	,
            17 - @CD_FUNC_CATALOG	INT		= NULL	,
            18 - @CD_FUNC_ENTREGA	INT		= NULL	,
            19 - @NR_PRATELEIRA		SMALLINT	= NULL	,
            20 - @CD_PERFIL_RETIRANTE	SMALLINT	= NULL	,
            21 - @NO_RETIRANTE		VARCHAR(50)	= NULL	,
            22 - @NR_DOCUMENTO		VARCHAR(20)	= NULL	,
            23 - @NR_TELEFONE		VARCHAR(20)	= NULL	,
            24 - @CD_FUNC_DEVOLUCAO	INT		= NULL	,
            25 - @DT_DEVOLUCAO		DATETIME	= NULL  ,
            26 -?@CD_SITUACAO		SMALLINT	= NULL
            */
            
            cal.setInt(1, par.getSetParametro(id));
                
            cal.setString(2, par.getSetParametro(descricao));
            
            if(setorOrigem == null){
                cal.setNull(3, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(3, par.getSetParametro(setorOrigem.getId()));
            }
            if(qtObjeto == null){
                cal.setNull(4, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(4, par.getSetParametro(qtObjeto));
            }
            if(unidade == null){
                cal.setNull(5, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(5, par.getSetParametro(unidade));
            }
            if(condicao == null){
                cal.setNull(6, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(6, par.getSetParametro(condicao));
            }
            if(definicao == null){
                cal.setNull(7, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(7, par.getSetParametro(definicao));
            }
            if(setorEncontrado == null){
                cal.setNull(8, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(8, par.getSetParametro(setorEncontrado.getId()));
            }
            if(modelo == null){
                cal.setNull(9, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(9, par.getSetParametro(modelo));
            }
            if(sexo == null){
                cal.setNull(10, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(10, par.getSetParametro(sexo));
            }
            if(marca == null){
                cal.setNull(11, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(11, par.getSetParametro(marca));
            }
            if(cor == null){
                cal.setNull(12, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(12, par.getSetParametro(cor));
            }
            if(tamanho == null){
                cal.setNull(13, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(13, par.getSetParametro(tamanho));
            }
            if(dtEntrada == null){
                cal.setNull(14, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(14, new java.sql.Date(par.getSetParametro(dtEntrada).getTime()));
            }
            if(dtCatalogacao == null){
                cal.setNull(15, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(15, new java.sql.Date(par.getSetParametro(dtCatalogacao).getTime()));
            }
            if(funcCatalogador == null){
                cal.setNull(16, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(16, par.getSetParametro(funcCatalogador.getId()));
            }
            if(nomeEntrega == null){
                cal.setNull(17, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(17, par.getSetParametro(nomeEntrega));
            }
            if(nrPrateleira == null){
                cal.setNull(18, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(18, par.getSetParametro(nrPrateleira));
            }
            if(perfilRetirante == null){
                cal.setNull(19, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(19, par.getSetParametro(perfilRetirante));
            }
            if(nomeRetirante == null){
                cal.setNull(20, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(20, par.getSetParametro(nomeRetirante));
            }
            if(docRetirante == null){
                cal.setNull(21, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(21, par.getSetParametro(docRetirante));
            }
            if(telRetirante == null){
                cal.setNull(22, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                cal.setString(22, par.getSetParametro(telRetirante));
            }
            if(funcDevolucao == null){
                cal.setNull(23, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(23, par.getSetParametro(funcDevolucao.getId()));
            }
            if(dtDevolucao == null){
                cal.setNull(24, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(24, new java.sql.Date(par.getSetParametro(dtDevolucao).getTime()));
            }
            if(situacao == null){
                cal.setNull(25, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(25, par.getSetParametro(situacao));
            }
            
            ResultSet rs = cal.executeQuery();

            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ACHADOS_PERDIDOS ('A', ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", par.getParametroFinal());
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
                throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }
    
    

    public void atualizarFoto(File f){

        Connection cn = null;
        String sql =  "select top 1 NU_SEQ_OBJETO, foto_OBJETO, cd_foto_OBJETO " 
                + " from tb_foto_ACHADOS_PERDIDOS where NU_SEQ_OBJETO = ? AND 1=2";

        BufferedInputStream bin = null;
        
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            
            try{
                bin = new BufferedInputStream(new FileInputStream(f));
            }catch(FileNotFoundException e){
                log.severe(e.getMessage());
            }
                            
            if(rs.next()){
                
                rs.updateBinaryStream("FOTO_OBJETO", bin);
                rs.updateRow();
            }else{
                rs.moveToInsertRow();
                rs.updateInt("NU_SEQ_OBJETO", id);
                rs.updateBinaryStream("FOTO_OBJETO", bin);
                rs.insertRow();
            }

            cn.commit();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
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

    public void excluirFoto(int id){

        Connection cn = null;        
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("DELETE from tb_foto_ACHADOS_PERDIDOS where cd_foto_objeto = ?");
            p.setInt(1, id);
            p.executeUpdate();
            
            cn.commit();
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
    
    public List<Integer> listarFotos(){

        ArrayList<Integer> l = new ArrayList<Integer>();
        Connection cn = null;

        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("select * from tb_foto_ACHADOS_PERDIDOS where NU_SEQ_OBJETO = ?");
            p.setInt(1, id);

            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                l.add(rs.getInt("CD_FOTO_OBJETO"));
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



}