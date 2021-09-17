
package techsoft.cadastro;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class Titular {

    private static final Logger log = Logger.getLogger("techsoft.cadastro.Titular");

    //dados pessoais
    private Socio socio;
    private boolean pessoaFisica = true;
    private String cpfCnpj;
    private String RG;
    private String email;
    private String naturalidade;
    private String nacionalidade;
    private String estadoCivil;
    private int idProfissao;
    private int idCargoEspecial;
    private boolean cargoAtivo;
    private boolean espolio;
    private Date dataAdmissao;
    private String nomePai;
    private String nomeMae;
    private Socio proponente;
    //enderecos
    private Contato enderecoResidencial;
    private Contato enderecoComercial;
    private String telefoneCelular;
    private String telefoneAlternativo;
    private String destinoCorrespondencia;
    private String destinoCarne;
    private String fax;

    //debito em conta
    private ContaCorrente contaCorrente;
    //parametros
    private boolean ignorarPagamentos;
    private boolean bloquearReservaChurrasqueira;
    private boolean bloquearEmissaoConvites;
    private boolean bloquearCadastroEmbarcacoes;
    private boolean bloquearMatriculas;
    private boolean bloquearEmprestimoMaterial;
    private boolean gerarCobrancaProprietario;
    private float consumoMaximo;
    private Date dataEntregaBrinde;
    private float maximoTxIndCheque;
    private String tipoEnvioBoleto;
    private Date dataAtualizacaoInternet;    

    //Usada para impedir geracao de taxa administrativa no carne
    private Map<Integer, String> taxasAdministrativas = new HashMap<Integer, String>();

    //Usada para bloquear a utilizacao de taxa
    private Map<Integer, String> taxasIndividuais = new HashMap<Integer, String>();
    
    private List<Dependente> dependentes = new ArrayList<Dependente>();

    public Titular(){
        socio = new Socio();
        enderecoResidencial = new Contato();
        enderecoComercial = new Contato();
        contaCorrente = new ContaCorrente();
    }

    public List<Dependente> getDependentes() {
        return dependentes;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public boolean isBloquearCadastroEmbarcacoes() {
        return bloquearCadastroEmbarcacoes;
    }

    public void setBloquearCadastroEmbarcacoes(boolean bloquearCadastroEmbarcacoes) {
        this.bloquearCadastroEmbarcacoes = bloquearCadastroEmbarcacoes;
    }

    public boolean isBloquearEmissaoConvites() {
        return bloquearEmissaoConvites;
    }

    public void setBloquearEmissaoConvites(boolean bloquearEmissaoConvites) {
        this.bloquearEmissaoConvites = bloquearEmissaoConvites;
    }

    public boolean isBloquearEmprestimoMaterial() {
        return bloquearEmprestimoMaterial;
    }

    public void setBloquearEmprestimoMaterial(boolean bloquearEmprestimoMaterial) {
        this.bloquearEmprestimoMaterial = bloquearEmprestimoMaterial;
    }

    public boolean isBloquearMatriculas() {
        return bloquearMatriculas;
    }

    public void setBloquearMatriculas(boolean bloquearMatriculas) {
        this.bloquearMatriculas = bloquearMatriculas;
    }

    public boolean isBloquearReservaChurrasqueira() {
        return bloquearReservaChurrasqueira;
    }

    public void setBloquearReservaChurrasqueira(boolean bloquearReservaChurrasqueira) {
        this.bloquearReservaChurrasqueira = bloquearReservaChurrasqueira;
    }

    public boolean isCargoAtivo() {
        return cargoAtivo;
    }

    public void setCargoAtivo(boolean cargoAtivo) {
        this.cargoAtivo = cargoAtivo;
    }
    
    public boolean isEspolio() {
        return espolio;
    }

    public void setEspolio(boolean espolio) {
        this.espolio = espolio;
    }

    public boolean isGerarCobrancaProprietario() {
        return gerarCobrancaProprietario;
    }

    public void setGerarCobrancaProprietario(boolean gerarCobrancaProprietario) {
        this.gerarCobrancaProprietario = gerarCobrancaProprietario;
    }

    public float getConsumoMaximo() {
        return consumoMaximo;
    }

    public void setConsumoMaximo(float consumoMaximo) {
        this.consumoMaximo = consumoMaximo;
    }

    public ContaCorrente getContaCorrente() {
        return contaCorrente;
    }

    public void setContaCorrente(ContaCorrente contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Date getDataEntregaBrinde() {
        return dataEntregaBrinde;
    }

    public void setDataEntregaBrinde(Date dataEntregaBrinde) {
        this.dataEntregaBrinde = dataEntregaBrinde;
    }

    public String getDestinoCarne() {
        return destinoCarne;
    }

    public void setDestinoCarne(String destinoCarne) {
        this.destinoCarne = destinoCarne;
    }

    public String getDestinoCorrespondencia() {
        return destinoCorrespondencia;
    }

    public void setDestinoCorrespondencia(String destinoCorrespondencia) {
        this.destinoCorrespondencia = destinoCorrespondencia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contato getEnderecoComercial() {
        return enderecoComercial;
    }

    public void setEnderecoComercial(Contato enderecoComercial) {
        this.enderecoComercial = enderecoComercial;
    }

    public Contato getEnderecoResidencial() {
        return enderecoResidencial;
    }

    public void setEnderecoResidencial(Contato enderecoResidencial) {
        this.enderecoResidencial = enderecoResidencial;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public int getIdCargoEspecial() {
        return idCargoEspecial;
    }

    public void setIdCargoEspecial(int idCargoEspecial) {
        this.idCargoEspecial = idCargoEspecial;
    }

    public int getIdProfissao() {
        return idProfissao;
    }

    public void setIdProfissao(int idProfissao) {
        this.idProfissao = idProfissao;
    }

    public boolean isIgnorarPagamentos() {
        return ignorarPagamentos;
    }

    public void setIgnorarPagamentos(boolean ignorarPagamentos) {
        this.ignorarPagamentos = ignorarPagamentos;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public boolean isPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(boolean pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    public Socio getProponente() {
        return proponente;
    }

    public void setProponente(Socio proponente) {
        this.proponente = proponente;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }
    
    public String getTelefoneAlternativo() {
        return telefoneAlternativo;
    }

    public void setTelefoneAlternativo(String telefoneAlternativo) {
        this.telefoneAlternativo = telefoneAlternativo;
    }
    
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
    
    public Map<Integer, String> getTaxasAdministrativas() {
        return taxasAdministrativas;
    }

    public void setTaxasAdministrativas(Map<Integer, String> taxasAdministrativas) {
        this.taxasAdministrativas = taxasAdministrativas;
    }

    public Map<Integer, String> getTaxasIndividuais() {
        return taxasIndividuais;
    }

    public void setTaxasIndividuais(Map<Integer, String> taxasIndividuais) {
        this.taxasIndividuais = taxasIndividuais;
    }
    
    public float getMaximoTxIndCheque() {
        return maximoTxIndCheque;
    }

    public void setMaximoTxIndCheque(float maximoTxIndCheque) {
        this.maximoTxIndCheque = maximoTxIndCheque;
    }

    public String getTipoEnvioBoleto() {
        return tipoEnvioBoleto;
    }

    public void setTipoEnvioBoleto(String tipoEnvioBoleto) {
        this.tipoEnvioBoleto = tipoEnvioBoleto;
    }

    public Date getDataAtualizacaoInternet() {
        return dataAtualizacaoInternet;
    }

    public void setDataAtualizacaoInternet(Date dataAtualizacaoInternet) {
        this.dataAtualizacaoInternet = dataAtualizacaoInternet;
    }
    
    public void inserir(Auditoria audit) throws InserirTitularException{
        Connection cn = null;

        try {
            int convites = 0;//Saldo de convites geral, da tabela QT_RENOV_SD_CONV_geral
            String carteirinha = null;//recebe o numero de carteirinha gerado nessa rotina

            /*
             * 1 - Verifica se ja este titular ja foi cadastrado
             */
            cn = Pool.getInstance().getConnection();
            String sql = "SELECT CD_MATRICULA FROM TB_PESSOA WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ?";
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, this.socio.getMatricula());
            p.setInt(2, this.socio.getIdCategoria());
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                log.warning("Titular ja cadastrado [matricula = "
                        + this.socio.getMatricula() + " categoria = "
                        + this.socio.getIdCategoria() + "]");
                throw new InserirTitularException("Já existe Titulo cadastrado com esta matricula e desta categoria.", false);
            }

            /* 3 - Pega o proximo numero de carteirinha para o titular */
            try {
                carteirinha = Titular.proximoNumeroCarteirinha();
            } catch (ProximaCarteirinhaException e) {
                throw new InserirTitularException(e.getMessage());
            }

            /*
             * 4 - Busca o saldo de convite geral para a categoria
             */
            sql = "SELECT QT_RENOV_SD_CONV_geral FROM TB_CATEGORIA WHERE CD_CATEGORIA = ?";
            p = cn.prepareStatement(sql);
            p.setInt(1, this.socio.getIdCategoria());
            rs = p.executeQuery();
            if(rs.next()){
                convites = rs.getInt(1);
            }

            /*
             * 5 - Chama a procedure para inserir o titular
             * sao 69 parametros!!!
             */
            sql = "{call SP_INCLUI_PESSOA ("
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement cal = cn.prepareCall(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            
            cal.setInt(1, par.getSetParametro(this.socio.getMatricula()));//CD_MATRICULA
            cal.setInt(2, par.getSetParametro(0));//SEQ_DEPENDENTE
            cal.setInt(3, par.getSetParametro(this.socio.getIdCategoria()));//CD_CATEGORIA
            cal.setNull(4, java.sql.Types.INTEGER);//CD_TP_DEPENDENTE
            par.getSetNull();
            cal.setString(5, par.getSetParametro(this.socio.getNome()));//NOME_PESSOA
            cal.setDate(6, new java.sql.Date(par.getSetParametro(this.socio.getDataNascimento()).getTime()));//DT_NASCIMENTO
            
            if(this.socio.isMasculino()){
                cal.setString(7, par.getSetParametro("M"));//CD_SEXO
            }else{
                cal.setString(7, par.getSetParametro("F"));//CD_SEXO
            }
            cal.setString(8, par.getSetParametro(this.socio.getSituacao()));//CD_SIT_PESSOA
            cal.setDate(9, new java.sql.Date(par.getSetParametro(new Date()).getTime()));//DT_INIC_SIT_PESSOA
            cal.setNull(10, java.sql.Types.DATE);//DT_FIM_SIT_PESSOA
            par.getSetNull();
            cal.setDate(11, new java.sql.Date(par.getSetParametro(new Date()).getTime()));//DT_CADASTRAMENTO_TITULO
            cal.setInt(12, par.getSetParametro(Integer.valueOf(carteirinha)));//NR_CARTEIRA_PESSOA
            cal.setNull(13, java.sql.Types.DATE);//DT_VENC_CARTEIRA
            par.getSetNull();
            cal.setInt(14, par.getSetParametro(0));//NR_CARTEIRAS_EMITIDAS
            cal.setNull(15, java.sql.Types.DATE);//DT_VALID_EX_MEDICO_PESSOA
            par.getSetNull();
            
            cal.setString(16, par.getSetParametro("N"));//CD_CASO_ESPECIAL
            cal.setNull(17, java.sql.Types.VARCHAR);//CD_DEP_ISENTO
            par.getSetNull();
            if(dataAdmissao == null){
                cal.setNull(18, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(18, new java.sql.Date(par.getSetParametro(dataAdmissao).getTime()));//DT_INCL_PESSOA
            }
            
            cal.setInt(19, par.getSetParametro(getIdProfissao()));//CD_PROFISSAO
            cal.setString(20, par.getSetParametro(enderecoResidencial.getEndereco()));//ENDERECO_R
            cal.setString(21, par.getSetParametro(enderecoResidencial.getBairro()));//BAIRRO_R
            cal.setString(22, par.getSetParametro(enderecoResidencial.getCidade()));//CIDADE_R
            cal.setString(23, par.getSetParametro(enderecoResidencial.getUF()));//UF_R
            cal.setString(24, par.getSetParametro(enderecoResidencial.getCEP()));//CEP_R
            cal.setString(25, par.getSetParametro(enderecoResidencial.getTelefone()));//TELEFONE_R
            cal.setString(26, par.getSetParametro(enderecoComercial.getTelefone()));//TELEFONE_C
            cal.setString(27, par.getSetParametro(enderecoComercial.getCEP()));//CEP_C
            cal.setString(28, par.getSetParametro(estadoCivil));//ESTADO_CIVIL
            cal.setInt(29, par.getSetParametro(convites));//SD_CONVITE
            cal.setNull(30, java.sql.Types.DATE);//DT_VENC_PROX_CARNE_TX_ADM
            par.getSetNull();
            cal.setNull(31, java.sql.Types.DATE);//DT_VENC_ULTIMO_CARNE_TX_ADM
            par.getSetNull();
            if(cpfCnpj == null || cpfCnpj.equals("")){
                cal.setNull(32, java.sql.Types.NUMERIC);//CPF_CGC_PESSOA
                par.getSetNull();
            }else{
                cal.setLong(32, par.getSetParametro(Long.parseLong(cpfCnpj)));//CPF_CGC_PESSOA
            }
            cal.setString(33, par.getSetParametro(enderecoComercial.getEndereco()));//ENDERECO_C
            cal.setString(34, par.getSetParametro(enderecoComercial.getUF()));//UF_C
            cal.setString(35, par.getSetParametro(enderecoComercial.getBairro()));//BAIRRO_C
            cal.setString(36, par.getSetParametro(enderecoComercial.getCidade()));//CIDADE_C
            cal.setString(37, par.getSetParametro(String.valueOf(destinoCorrespondencia)));//CD_END_CORRESPONDENCIA
            cal.setString(38, par.getSetParametro(String.valueOf(destinoCarne)));//CD_END_CARNE
            if(contaCorrente.getAgencia() == null){
                cal.setNull(39, java.sql.Types.NUMERIC);//NR_AGENCIA_CC
                par.getSetNull();
            }else{
                cal.setInt(39, par.getSetParametro(contaCorrente.getAgencia()));//NR_AGENCIA_CC
            }
            if(isPessoaFisica()){
                cal.setString(40, par.getSetParametro("F"));//CD_IND_PES_FISICA_JURIDICA
            }else{
                cal.setString(40, par.getSetParametro("J"));//CD_IND_PES_FISICA_JURIDICA
            }
            if(contaCorrente.getDigitoAgencia().trim().equals("")){
                cal.setNull(41, java.sql.Types.VARCHAR);//DV_AGENCIA_CC
                par.getSetNull();
            }else{
                cal.setString(41, par.getSetParametro(contaCorrente.getDigitoAgencia()));//DV_AGENCIA_CC
            }
            if(contaCorrente.getConta().trim().equals("")){
                cal.setNull(42, java.sql.Types.VARCHAR);//NR_CONTA_CORRENTE
                par.getSetNull();
            }else{
                cal.setString(42, par.getSetParametro(contaCorrente.getConta()));//NR_CONTA_CORRENTE
            }
            if(contaCorrente.getDigitoConta().trim().equals("")){
                cal.setNull(43, java.sql.Types.VARCHAR);//DV_CONTA_CORRENTE
                par.getSetNull();
            }else{
                cal.setString(43, par.getSetParametro(contaCorrente.getDigitoConta()));//DV_CONTA_CORRENTE
            }
            cal.setString(44, par.getSetParametro(RG));//NR_IDENTIDADE_PESSOA
            if(contaCorrente.getTitular().trim().equals("")){
                cal.setNull(45, java.sql.Types.VARCHAR);//DV_CONTA_CORRENTE
                par.getSetNull();
            }else{
                cal.setString(45, par.getSetParametro(contaCorrente.getTitular()));//NO_TITULAR_CC
            }
            
            cal.setInt(46, par.getSetParametro(0));//SD_CONVITE_CHURRASQUEIRA
            cal.setInt(47, par.getSetParametro(0));//SD_CONVITE_SINUCA
            cal.setInt(48, par.getSetParametro(0));//SD_CONVITE_SAUNA
            if(idCargoEspecial == 0){
                cal.setNull(49, java.sql.Types.INTEGER);//CD_CARGO_ESPECIAL
                par.getSetNull();
            }else{
                cal.setInt(49, par.getSetParametro(idCargoEspecial));//CD_CARGO_ESPECIAL
            }
            cal.setString(50, par.getSetParametro(nomePai));//NOME_PAI
            cal.setString(51, par.getSetParametro(nomeMae));//NOME_MAE
            cal.setString(52, par.getSetParametro(nacionalidade));//NACIONALIDADE
            cal.setString(53, par.getSetParametro(naturalidade));//NATURALIDADE
            cal.setNull(54, java.sql.Types.INTEGER);//CD_ARMARIO
            par.getSetNull();
            if(idCargoEspecial == 0){
                cal.setNull(55, java.sql.Types.CHAR);//CD_CARGO_ATIVO
                par.getSetNull();
            }else{
                if(isCargoAtivo()){
                    cal.setString(55, par.getSetParametro("S"));//CD_CARGO_ATIVO
                }else{
                    cal.setString(55, par.getSetParametro("N"));//CD_CARGO_ATIVO
                }
            }
            cal.setInt(56, par.getSetParametro(proponente.getMatricula()));//CD_MATRICULA_PROPONENTE
            cal.setInt(57, par.getSetParametro(proponente.getIdCategoria()));//CD_CATEGORIA_PROPONENTE
            cal.setString(58, par.getSetParametro(proponente.getNome()));//NOME_PROPONENTE
            cal.setString(59, par.getSetParametro(email));//EMAIL
            if(isIgnorarPagamentos()){
                cal.setString(60, par.getSetParametro("S"));//CD_VRF_PAGAMENTO
            }else{
                cal.setNull(60, java.sql.Types.CHAR);//CD_VRF_PAGAMENTO
                par.getSetNull();
            }
            if(isBloquearEmissaoConvites()){
                cal.setString(61, par.getSetParametro("S"));//CD_VRF_CONVITE
            }else{
                cal.setNull(61, java.sql.Types.CHAR);//CD_VRF_CONVITE
                par.getSetNull();
            }
            if(isBloquearReservaChurrasqueira()){
                cal.setString(62, par.getSetParametro("S"));//CD_VRF_CHURRASQUEIRA
            }else{
                cal.setNull(62, java.sql.Types.CHAR);//CD_VRF_CHURRASQUEIRA
                par.getSetNull();
            }
            if(isBloquearCadastroEmbarcacoes()){
                cal.setString(63, par.getSetParametro("S"));//CD_VRF_BARCO
            }else{
                cal.setNull(63, java.sql.Types.CHAR);//CD_VRF_BARCO
                par.getSetNull();
            }
            if(isBloquearMatriculas()){
                cal.setString(64, par.getSetParametro("S"));//CD_VRF_ESCOLINHA
            }else{
                cal.setNull(64, java.sql.Types.CHAR);//CD_VRF_ESCOLINHA
                par.getSetNull();
            }
            if(isBloquearEmprestimoMaterial()){
                cal.setString(65, par.getSetParametro("S"));//CD_VRF_EMPRESTIMO
            }else{
                cal.setNull(65, java.sql.Types.CHAR);//CD_VRF_EMPRESTIMO
                par.getSetNull();
            }
            if(isGerarCobrancaProprietario()){
                cal.setString(66, par.getSetParametro("S"));//CD_VRF_COBRANCA_PROP
            }else{
                cal.setNull(66, java.sql.Types.CHAR);//CD_VRF_COBRANCA_PROP
                par.getSetNull();
            }
            cal.setFloat(67, par.getSetParametro(consumoMaximo));
            cal.setString(68, par.getSetParametro(telefoneCelular));
            if(dataEntregaBrinde == null){
                cal.setNull(69, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(69, new java.sql.Date(par.getSetParametro(dataEntregaBrinde).getTime()));
            }
            cal.setFloat(70, par.getSetParametro(maximoTxIndCheque));
            if(tipoEnvioBoleto == null){
                cal.setNull(71, java.sql.Types.CHAR);
                par.getSetNull();
            }else{
                cal.setString(71, par.getSetParametro(tipoEnvioBoleto));
            }
            cal.setString(72, par.getSetParametro(fax));
            
            cal.setString(73, par.getSetParametro(telefoneAlternativo));
            
            if(isEspolio()){
                cal.setString(74, par.getSetParametro("S"));//IC_ESPOLIO
            }else{
                cal.setString(74, par.getSetParametro("N"));//IC_ESPOLIO
            }
            
            
            cal.executeUpdate();
            cn.commit();

            audit.registrarMudanca(sql, par.getParametroFinal());
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

    /*
     * Gera o numero de carteirinha para o titular/dependente
     */
    static String proximoNumeroCarteirinha()throws ProximaCarteirinhaException{

        String carteirinha = null;
        Connection cn = null;

        try{
            cn = cn = Pool.getInstance().getConnection();
            String sql = "SELECT NR_ULTIMA_CARTEIRA FROM TB_PARAMETRO_SISTEMA";
            ResultSet rs = cn.createStatement().executeQuery(sql);
            if(rs.next()){
                int i = rs.getInt(1);
                if(rs.wasNull()){
                    String err = "Tabela de ParÃ¢metros NR_ULTIMA_CARTEIRA = NULL.";
                    log.severe(err);
                    throw new ProximaCarteirinhaException(err);
                }else{
                    DecimalFormat f = new DecimalFormat("0000000000");
                    sql = "UPDATE TB_PARAMETRO_SISTEMA SET NR_ULTIMA_CARTEIRA = NR_ULTIMA_CARTEIRA + 1";
                    cn.createStatement().executeUpdate(sql);
                    cn.commit();
                    i++;
                    carteirinha = f.format(i);
                    carteirinha += String.valueOf(digitoCarteirinha(carteirinha));
                }
            }else{
                String err = "Tabela de ParÃ¢metros nÃ£o possui parÃ¢metros.";
                log.severe(err);
                throw new ProximaCarteirinhaException(err);
            }
        }catch(SQLException e){
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

        return carteirinha;
    }

    public static int digitoCarteirinha(String carteirinha){
       int soma = 0;
       int digito = 0;
       int i = 0;

       if(carteirinha.length() != 10){
           String err = "Tamanho do nÃºmero da carteirinha invÃ¡lido.";
           log.warning(err);
           throw new RuntimeException(err);
       }else{
           i = Integer.parseInt(carteirinha.substring(9, 10));
           i *= 2;
           soma = i;

           i = Integer.parseInt(carteirinha.substring(8, 9));
           i *= 3;
           soma += i;

           i = Integer.parseInt(carteirinha.substring(7, 8));
           i *= 4;
           soma += i;

           i = Integer.parseInt(carteirinha.substring(6, 7));
           i *= 5;
           soma += i;

           i = Integer.parseInt(carteirinha.substring(5, 6));
           i *= 6;
           soma += i;

           i = Integer.parseInt(carteirinha.substring(4, 5));
           i *= 7;
           soma += i;

           i = Integer.parseInt(carteirinha.substring(3, 4));
           i *= 8;
           soma += i;

           i = Integer.parseInt(carteirinha.substring(2, 3));
           i *= 9;
           soma += i;

           i = Integer.parseInt(carteirinha.substring(1, 2));
           i *= 2;
           soma += i;

           i = Integer.parseInt(carteirinha.substring(0, 1));
           i *= 3;
           soma += i;

           digito = (soma * 10) % 11;
           if(digito == 10) digito = 0;
        }

        return digito;
    }

    public static Titular getInstance(int matricula, int idCategoria){

        Connection cn = null;
        Titular t = null;
        String sql = "SELECT * FROM TB_PESSOA WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ?";
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                t = new Titular();
                t.socio = new Socio();
                t.socio.setMatricula(rs.getInt("CD_MATRICULA"));
                t.socio.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                t.socio.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                t.socio.setNome(rs.getString("NOME_PESSOA"));
                if(rs.getString("cd_sexo").equalsIgnoreCase("M")){
                    t.socio.setMasculino(true);
                }else{
                    t.socio.setMasculino(false);
                }
                t.socio.setDataNascimento(rs.getDate("dt_nascimento"));
                t.idCargoEspecial = rs.getInt("CD_CARGO_ESPECIAL");
                if(rs.getString("CD_CARGO_ATIVO") == null || rs.getString("CD_CARGO_ATIVO").charAt(0) == 'N'){
                    t.cargoAtivo = false;
                }else{
                    t.cargoAtivo = true;
                }
                t.socio.setSituacao(rs.getString("CD_SIT_PESSOA"));
                t.dataAdmissao = rs.getDate("DT_INCL_PESSOA");
                t.email = rs.getString("DE_EMAIL");
                t.dataEntregaBrinde = rs.getDate("DT_ENTREGA_BRINDE");
                t.dataAtualizacaoInternet = rs.getDate("DT_ULT_ATU_INTERNET");
            }else{
                log.warning("nao encontrou dados principais do titular matricula "
                    + matricula + " categoria " + idCategoria);
            }

            sql = "SELECT * FROM VW_COMPLEMENTO WHERE CD_MATRICULA = ? AND "
                + "SEQ_DEPENDENTE = 0 AND CD_CATEGORIA = ?";
            p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);

            rs = p.executeQuery();
            if(rs.next()){
                t.enderecoResidencial = new Contato();
                t.enderecoComercial = new Contato();
                t.contaCorrente = new ContaCorrente();
                t.proponente = new Socio();
                t.enderecoResidencial.setEndereco(rs.getString("ENDERECO_R"));
                t.enderecoResidencial.setBairro(rs.getString("BAIRRO_R"));
                t.enderecoResidencial.setCidade(rs.getString("CIDADE_R"));
                t.enderecoResidencial.setUF(rs.getString("UF_R"));
                t.enderecoResidencial.setCEP(rs.getString("CEP_R"));
                t.enderecoResidencial.setTelefone(rs.getString("TELEFONE_R"));
                t.enderecoComercial.setEndereco(rs.getString("ENDERECO_C"));
                t.enderecoComercial.setBairro(rs.getString("BAIRRO_C"));
                t.enderecoComercial.setCidade(rs.getString("CIDADE_C"));
                t.enderecoComercial.setUF(rs.getString("UF_C"));
                t.enderecoComercial.setCEP(rs.getString("CEP_C"));
                t.enderecoComercial.setTelefone(rs.getString("TELEFONE_C"));
                t.contaCorrente.setAgencia((Integer) rs.getInt("NR_AGENCIA_CC"));
                if (rs.wasNull()){t.contaCorrente.setAgencia(null);}
                
                t.contaCorrente.setDigitoAgencia(rs.getString("DV_AGENCIA_CC"));
                t.contaCorrente.setConta(rs.getString("NR_CONTA_CORRENTE"));
                t.contaCorrente.setDigitoConta(rs.getString("DV_CONTA_CORRENTE"));
                t.contaCorrente.setTitular(rs.getString("NO_TITULAR_CC"));
                t.RG = rs.getString("NR_IDENTIDADE_PESSOA");
                t.nacionalidade = rs.getString("NACIONALIDADE");
                t.naturalidade = rs.getString("NAturaLIDADE");
                t.nomePai = rs.getString("nome_PAI");
                t.nomeMae = rs.getString("nome_MAE");
                
                if(rs.getString("CD_IND_PES_FISICA_JURIDICA") == null ||
                        rs.getString("CD_IND_PES_FISICA_JURIDICA").charAt(0) == 'F'){
                    t.pessoaFisica = true;
                }else{
                    t.pessoaFisica = false;
                }

                t.cpfCnpj = rs.getString("CPF_CGC_PESSOA");
                t.destinoCorrespondencia = rs.getString("CD_END_CORRESPONDENCIA");
                t.destinoCarne = rs.getString("CD_END_CARNE");
                t.idProfissao = rs.getInt("CD_PROFISSAO");
                t.estadoCivil = rs.getString("ESTADO_CIVIL");
                t.proponente.setMatricula(rs.getInt("cd_matricula_proponente"));
                t.proponente.setIdCategoria(rs.getInt("cd_categoria_proponente"));
                t.proponente.setNome(rs.getString("nome_proponente"));

                if(rs.getString("CD_VRF_PAGAMENTO") == null ||
                        rs.getString("CD_VRF_PAGAMENTO").charAt(0) == 'N'){
                    t.ignorarPagamentos = false;
                }else{
                    t.ignorarPagamentos = true;
                }

                if(rs.getString("CD_VRF_CONVITE") == null ||
                        rs.getString("CD_VRF_CONVITE").charAt(0) == 'N'){
                    t.bloquearEmissaoConvites = false;
                }else{
                    t.bloquearEmissaoConvites = true;
                }

                if(rs.getString("CD_VRF_CHURRASQUEIRA") == null ||
                        rs.getString("CD_VRF_CHURRASQUEIRA").charAt(0) == 'N'){
                    t.bloquearReservaChurrasqueira = false;
                }else{
                    t.bloquearReservaChurrasqueira = true;
                }

                if(rs.getString("CD_VRF_BARCO") == null ||
                        rs.getString("CD_VRF_BARCO").charAt(0) == 'N'){
                    t.bloquearCadastroEmbarcacoes = false;
                }else{
                    t.bloquearCadastroEmbarcacoes = true;
                }

                if(rs.getString("CD_VRF_ESCOLINHA") == null ||
                        rs.getString("CD_VRF_ESCOLINHA").charAt(0) == 'N'){
                    t.bloquearMatriculas = false;
                }else{
                    t.bloquearMatriculas = true;
                }

                if(rs.getString("CD_VRF_EMPRESTIMO") == null ||
                        rs.getString("CD_VRF_EMPRESTIMO").charAt(0) == 'N'){
                    t.bloquearEmprestimoMaterial = false;
                }else{
                    t.bloquearEmprestimoMaterial = true;
                }

//                if(rs.getString("CD_VRF_COBRANCA_PROP") == null ||
//                        rs.getString("CD_VRF_COBRANCA_PROP").charAt(0) == 'N'){
//                    t.bl = false;
//                }else{
//                    t.bloquearEmissaoConvites = true;
//                }
                t.consumoMaximo = rs.getFloat("VR_MAXIMO_CONSUMO");
                t.maximoTxIndCheque = rs.getFloat("VR_MAXIMO_TX_IND");
                t.tipoEnvioBoleto = rs.getString("TP_ENVIO_BOLETO");
                if(t.tipoEnvioBoleto == null) t.tipoEnvioBoleto = "";
                
                if(rs.getString("IC_ESPOLIO") == null || rs.getString("IC_ESPOLIO").charAt(0) == 'N'){
                    t.espolio = false;
                }else{
                    t.espolio = true;
                }
                

            }else{
                log.warning("nao encontrou dados complementares do titular matricula "
                        + matricula + " categoria " + idCategoria);
            }

            sql = "SELECT * FROM TB_TELEFONE_PESSOA WHERE CD_MATRICULA = ? "
                    + " AND SEQ_DEPENDENTE = 0 AND CD_CATEGORIA = ?";
            p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);

            rs = p.executeQuery();
            while(rs.next()){
                char c = rs.getString("IC_TIPO").charAt(0);
                String fone = rs.getString("NR_TELEFONE");
                switch(c){
                    case 'R':
                        t.enderecoResidencial.setTelefone(fone);
                    break;
                    case 'C':
                        t.enderecoComercial.setTelefone(fone);
                    break;
                    case 'L':
                        t.telefoneCelular = fone;
                    break;
                    case 'F':
                        t.fax = fone;
                    break;
                    case 'A':
                        t.telefoneAlternativo = fone;
                    break;
                }
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
        return t;
    }

    public void alterarEnderecos(Auditoria audit){

        Connection cn = null;

        try {
           String sql = "{call SP_ATU_END_CLUBE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setInt(1, par.getSetParametro(socio.getMatricula()));
            p.setInt(2, par.getSetParametro(socio.getIdCategoria()));
            p.setString(3, par.getSetParametro(String.valueOf(destinoCorrespondencia)));
            p.setString(4, par.getSetParametro(String.valueOf(destinoCarne)));
            
            p.setString(5, par.getSetParametro(enderecoResidencial.getEndereco()));
            p.setString(6, par.getSetParametro(enderecoResidencial.getBairro()));
            p.setString(7, par.getSetParametro(enderecoResidencial.getCidade()));
            p.setString(8, par.getSetParametro(enderecoResidencial.getUF()));
            p.setString(9, par.getSetParametro(enderecoResidencial.getCEP().replace(".", "").replace("-", "")));
            p.setString(10, par.getSetParametro(enderecoResidencial.getTelefone()));
            
            p.setString(11, par.getSetParametro(enderecoComercial.getEndereco()));
            p.setString(12, par.getSetParametro(enderecoComercial.getBairro()));
            p.setString(13, par.getSetParametro(enderecoComercial.getCidade()));
            p.setString(14, par.getSetParametro(enderecoComercial.getUF()));
            p.setString(15, par.getSetParametro(enderecoComercial.getCEP().replace(".", "").replace("-", "")));
            p.setString(16, par.getSetParametro(enderecoComercial.getTelefone()));

            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
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

    public void excluir(Auditoria audit, boolean excluirTaxaFutura)throws ExcluirTitularException{
        Connection cn = null;

        try {
           String sql = "SELECT 1 FROM TB_MATRICULA_CURSO WHERE "
               + "CD_MATRICULA = ? AND CD_CATEGORIA = ? AND CD_SIT_MATRICULA IN ('NO', 'NT')";

            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, socio.getMatricula());
            p.setInt(2, socio.getIdCategoria());

            ResultSet rs = p.executeQuery();
            if(rs.next()){
                String err = "Existe alguma pessoa da família matriculada em algum curso!";
                log.fine(err);
                throw new ExcluirTitularException(err);
            }

            sql = "SELECT 1 FROM TB_BARCO WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ?";
            p = cn.prepareStatement(sql);
            p.setInt(1, socio.getMatricula());
            p.setInt(2, socio.getIdCategoria());

            rs = p.executeQuery();
            if(rs.next()){
                String err = "Existe Embarcação associada a Família!";
                log.fine(err);
                throw new ExcluirTitularException(err);
            }
            
            sql = "SELECT VR_SALDO_ATUAL FROM TB_VAL_PRE_PAGO WHERE NU_SEQ_PRE_PAGO = (SELECT MAX(NU_SEQ_PRE_PAGO) FROM TB_VAL_PRE_PAGO WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ?)";
            p = cn.prepareStatement(sql);
            p.setInt(1, socio.getMatricula());
            p.setInt(2, socio.getIdCategoria());

            rs = p.executeQuery();
            if(rs.next()){
                if(rs.getFloat("VR_SALDO_ATUAL")!=0){
                    String err = "Existe saldo de IateCardPre!";
                    log.fine(err);
                    throw new ExcluirTitularException(err);
                }
            }

            if(!excluirTaxaFutura){
                sql = "SELECT 1 FROM TB_VAL_TAXA_INDIVIDUAL WHERE "
                        + "AA_COBRANCA >= Year(GETDATE()) AND MM_COBRANCA >= Month(GETDATE()) "
                        + "AND IC_SITUACAO_TAXA = 'N' AND CD_MATRICULA = ? AND CD_CATEGORIA = ?";
                p = cn.prepareStatement(sql);
                p.setInt(1, socio.getMatricula());
                p.setInt(2, socio.getIdCategoria());

                rs = p.executeQuery();
                if(rs.next()){
                    String err = "Existe cadastro de taxa individual futura. Deseja Continuar?";
                    log.fine(err);
                    throw new ExcluirTitularException(err, true);
                }
            }

            sql = "{call SP_EXCLUI_PESSOA (?, ?, 0)}";
            CallableStatement cal = cn.prepareCall(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            cal.setInt(1, par.getSetParametro(socio.getMatricula()));
            cal.setInt(2, par.getSetParametro(socio.getIdCategoria()));
            cal.executeUpdate();
            
            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
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

    public void alterar(Auditoria audit){
        Connection cn = null;
        String vCpfCnpj = "";

        try {
            cn = Pool.getInstance().getConnection();

            /*
             * 68 parametros!!!
             */
            String sql = "{call SP_ALTERA_PESSOA ("
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            CallableStatement cal = cn.prepareCall(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            cal.setInt(1, par.getSetParametro(this.socio.getMatricula()));//CD_MATRICULA
            cal.setInt(2, par.getSetParametro(0));//SEQ_DEPENDENTE
            cal.setInt(3, par.getSetParametro(this.socio.getIdCategoria()));//CD_CATEGORIA
            cal.setNull(4, java.sql.Types.INTEGER);//CD_TP_DEPENDENTE
            par.getSetNull();
            cal.setString(5, par.getSetParametro(this.socio.getNome()));//NOME_PESSOA
            cal.setDate(6, new java.sql.Date(par.getSetParametro(this.socio.getDataNascimento()).getTime()));//DT_NASCIMENTO
                        
            if(this.socio.isMasculino()){
                cal.setString(7, par.getSetParametro("M"));//CD_SEXO
            }else{
                cal.setString(7, par.getSetParametro("F"));//CD_SEXO
            }
            cal.setString(8, par.getSetParametro(this.socio.getSituacao()));//CD_SIT_PESSOA
            cal.setDate(9, new java.sql.Date(par.getSetParametro(new Date()).getTime()));//DT_INIC_SIT_PESSOA
            
            cal.setNull(10, java.sql.Types.DATE);//DT_FIM_SIT_PESSOA
            par.getSetNull();

            cal.setInt(11, par.getSetParametro(this.socio.getNrCarteira()));//NR_CARTEIRA_PESSOA
            cal.setNull(12, java.sql.Types.DATE);//DT_VENC_CARTEIRA
            par.getSetNull();

            cal.setInt(13, par.getSetParametro(0));//NR_CARTEIRAS_EMITIDAS
            cal.setNull(14, java.sql.Types.DATE);//DT_VALID_EX_MEDICO_PESSOA
            par.getSetNull();
            cal.setNull(15, java.sql.Types.VARCHAR);//CD_CASO_ESPECIAL
            par.getSetNull();
            cal.setNull(16, java.sql.Types.VARCHAR);//CD_DEP_ISENTO
            par.getSetNull();
            if(dataAdmissao == null){
                cal.setNull(17, java.sql.Types.DATE);
            par.getSetNull();
            }else{
                cal.setDate(17, new java.sql.Date(par.getSetParametro(dataAdmissao).getTime()));//DT_INCL_PESSOA
            }
            cal.setInt(18, par.getSetParametro(getIdProfissao()));//CD_PROFISSAO
            cal.setString(19, par.getSetParametro(enderecoResidencial.getEndereco()));//ENDERECO_R
            cal.setString(20, par.getSetParametro(enderecoResidencial.getBairro()));//BAIRRO_R
            cal.setString(21, par.getSetParametro(enderecoResidencial.getCidade()));//CIDADE_R
            cal.setString(22, par.getSetParametro(enderecoResidencial.getUF()));//UF_R
            cal.setString(23, par.getSetParametro(enderecoResidencial.getCEP().replace(".", "").replace("-", "")));//CEP_R
            cal.setString(24, par.getSetParametro(enderecoResidencial.getTelefone()));//TELEFONE_R
            cal.setString(25, par.getSetParametro(enderecoComercial.getTelefone()));//TELEFONE_C
            cal.setString(26, par.getSetParametro(enderecoComercial.getCEP().replace(".", "").replace("-", "")));//CEP_C
            cal.setString(27, par.getSetParametro(estadoCivil));//ESTADO_CIVIL
            cal.setInt(28, par.getSetParametro(0));//SD_CONVITE
            cal.setNull(29, java.sql.Types.DATE);//DT_VENC_PROX_CARNE_TX_ADM
            par.getSetNull();
            
            cal.setNull(30, java.sql.Types.DATE);//DT_VENC_ULTIMO_CARNE_TX_ADM
            par.getSetNull();
            
            if(cpfCnpj == null || cpfCnpj.equals("")){
                cal.setNull(31, java.sql.Types.NUMERIC);//CPF_CGC_PESSOA
                par.getSetNull();
            }else{
                cal.setLong(31, par.getSetParametro(Long.parseLong(cpfCnpj.replace(".", "").replace("-", "").replace("/", ""))));//CPF_CGC_PESSOA
            }
            cal.setString(32, par.getSetParametro(enderecoComercial.getEndereco()));//ENDERECO_C
            cal.setString(33, par.getSetParametro(enderecoComercial.getUF()));//UF_C
            cal.setString(34, par.getSetParametro(enderecoComercial.getBairro()));//BAIRRO_C
            cal.setString(35, par.getSetParametro(enderecoComercial.getCidade()));//CIDADE_C
            cal.setString(36, par.getSetParametro(String.valueOf(destinoCorrespondencia)));//CD_END_CORRESPONDENCIA
            cal.setString(37, par.getSetParametro(String.valueOf(destinoCarne)));//CD_END_CARNE
            if(contaCorrente.getAgencia() == null){
                cal.setNull(38, java.sql.Types.NUMERIC);//NR_AGENCIA_CC
                par.getSetNull();
            }else{
                cal.setInt(38, par.getSetParametro(contaCorrente.getAgencia()));//NR_AGENCIA_CC
            }
            if(isPessoaFisica()){
                cal.setString(39, par.getSetParametro("F"));//CD_IND_PES_FISICA_JURIDICA
            }else{
                cal.setString(39, par.getSetParametro("J"));//CD_IND_PES_FISICA_JURIDICA
            }
            if(contaCorrente.getDigitoAgencia().trim().equals("")){
                cal.setNull(40, java.sql.Types.VARCHAR);//DV_AGENCIA_CC
                par.getSetNull();
            }else{
                cal.setString(40, par.getSetParametro(contaCorrente.getDigitoAgencia()));//DV_AGENCIA_CC
            }
            if(contaCorrente.getConta().trim().equals("")){
                cal.setNull(41, java.sql.Types.VARCHAR);//NR_CONTA_CORRENTE
                par.getSetNull();
            }else{
                cal.setString(41, par.getSetParametro(contaCorrente.getConta()));//NR_CONTA_CORRENTE
            }
            if(contaCorrente.getDigitoConta().trim().equals("")){
                cal.setNull(42, java.sql.Types.VARCHAR);//DV_CONTA_CORRENTE
                par.getSetNull();
            }else{
                cal.setString(42, par.getSetParametro(contaCorrente.getDigitoConta()));//DV_CONTA_CORRENTE
            }
            cal.setString(43, par.getSetParametro(RG));//NR_IDENTIDADE_PESSOA
            if(contaCorrente.getTitular().trim().equals("")){
                cal.setNull(44, java.sql.Types.VARCHAR);//DV_CONTA_CORRENTE
                par.getSetNull();
            }else{
                cal.setString(44, par.getSetParametro(contaCorrente.getTitular()));//NO_TITULAR_CC
            }
            cal.setInt(45, par.getSetParametro(0));//SD_CONVITE_CHURRASQUEIRA
            cal.setInt(46, par.getSetParametro(0));//SD_CONVITE_SINUCA
            cal.setInt(47, par.getSetParametro(0));//SD_CONVITE_SAUNA
            if(idCargoEspecial == 0){
                cal.setNull(48, java.sql.Types.INTEGER);//CD_TIPO_PESSOA
                par.getSetNull();
            }else{
                cal.setInt(48, par.getSetParametro(idCargoEspecial));//CD_TIPO_PESSOA
            }
            cal.setString(49, par.getSetParametro(nomePai));//NOME_PAI
            cal.setString(50, par.getSetParametro(nomeMae));//NOME_MAE
            cal.setString(51, par.getSetParametro(nacionalidade));//NACIONALIDADE
            cal.setString(52, par.getSetParametro(naturalidade));//NATURALIDADE
            cal.setNull(53, java.sql.Types.INTEGER);//CD_ARMARIO
            par.getSetNull();
            if(idCargoEspecial == 0){
                cal.setNull(54, java.sql.Types.CHAR);//CD_CARGO_ATIVO
                par.getSetNull();
            }else{
                if(isCargoAtivo()){
                    cal.setString(54, par.getSetParametro("S"));//CD_CARGO_ATIVO
                }else{
                    cal.setString(54, par.getSetParametro("N"));//CD_CARGO_ATIVO
                }
            }
            cal.setInt(55, par.getSetParametro(proponente.getMatricula()));//CD_MATRICULA_PROPONENTE
            cal.setInt(56, par.getSetParametro(proponente.getIdCategoria()));//CD_CATEGORIA_PROPONENTE
            cal.setString(57, par.getSetParametro(proponente.getNome()));//NOME_PROPONENTE
            cal.setString(58, par.getSetParametro(email));//EMAIL
            if(isIgnorarPagamentos()){
                cal.setString(59, par.getSetParametro("S"));//CD_VRF_PAGAMENTO
            }else{
                cal.setNull(59, java.sql.Types.CHAR);//CD_VRF_PAGAMENTO
                par.getSetNull();
            }
            if(isBloquearEmissaoConvites()){
                cal.setString(60, par.getSetParametro("S"));//CD_VRF_CONVITE
            }else{
                cal.setNull(60, java.sql.Types.CHAR);//CD_VRF_CONVITE
                par.getSetNull();
            }
            if(isBloquearReservaChurrasqueira()){
                cal.setString(61, par.getSetParametro("S"));//CD_VRF_CHURRASQUEIRA
            }else{
                cal.setNull(61, java.sql.Types.CHAR);//CD_VRF_CHURRASQUEIRA
                par.getSetNull();
            }
            if(isBloquearCadastroEmbarcacoes()){
                cal.setString(62, par.getSetParametro("S"));//CD_VRF_BARCO
            }else{
                cal.setNull(62, java.sql.Types.CHAR);//CD_VRF_BARCO
                par.getSetNull();
            }
            if(isBloquearMatriculas()){
                cal.setString(63, par.getSetParametro("S"));//CD_VRF_ESCOLINHA
            }else{
                cal.setNull(63, java.sql.Types.CHAR);//CD_VRF_ESCOLINHA
                par.getSetNull();
            }
            if(isBloquearEmprestimoMaterial()){
                cal.setString(64, par.getSetParametro("S"));//CD_VRF_EMPRESTIMO
            }else{
                cal.setNull(64, java.sql.Types.CHAR);//CD_VRF_EMPRESTIMO
                par.getSetNull();
            }
            if(isGerarCobrancaProprietario()){
                cal.setString(65, par.getSetParametro("S"));//CD_VRF_COBRANCA_PROP
            }else{
                cal.setNull(65, java.sql.Types.CHAR);//CD_VRF_COBRANCA_PROP
                par.getSetNull();
            }
            cal.setFloat(66, par.getSetParametro(consumoMaximo));
            cal.setString(67, par.getSetParametro(telefoneCelular));
            if(dataEntregaBrinde == null){
                cal.setNull(68, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(68, new java.sql.Date(par.getSetParametro(dataEntregaBrinde).getTime()));
            }
            cal.setFloat(69, par.getSetParametro(maximoTxIndCheque));
            if(tipoEnvioBoleto == null){
                cal.setNull(70, java.sql.Types.CHAR);
                par.getSetNull();
            }else{
                cal.setString(70, par.getSetParametro(tipoEnvioBoleto));
            }            
            cal.setString(71, par.getSetParametro(fax));

            cal.setString(72, par.getSetParametro(telefoneAlternativo));
            
            if(isEspolio()){
                cal.setString(73, par.getSetParametro("S"));//IC_ESPOLIO
            }else{
                cal.setString(73, par.getSetParametro("N"));//IC_ESPOLIO
            }
            
            cal.executeUpdate();
            cn.commit();

            audit.registrarMudanca(sql, par.getParametroFinal());
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

    public void carregarDependentes(){
        dependentes.clear();

        String sql = "SELECT NOME_PESSOA, CD_MATRICULA, SEQ_DEPENDENTE, "
            + "CD_CATEGORIA, CD_TP_DEPENDENTE, DT_NASCIMENTO, CD_SEXO, "
            + "CD_CASO_ESPECIAL, DT_CASO_ESPECIAL, DT_VALID_RET_CONVITE, "
            + "DT_VALID_RESERVA, DT_VALID_MATERIAL, CD_CARGO_ESPECIAL, "
            + "CD_CARGO_ATIVO, DE_EMAIL FROM TB_PESSOA WHERE CD_MATRICULA = ? "
            + "AND SEQ_DEPENDENTE > 0 AND CD_CATEGORIA = ?";

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, socio.getMatricula());
            ps.setInt(2, socio.getIdCategoria());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Dependente d = new Dependente();
                d.setMatricula(rs.getInt("CD_MATRICULA"));
                d.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                d.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                d.setNome(rs.getString("NOME_PESSOA"));
                d.setIdTipoDependente(rs.getInt("CD_TP_DEPENDENTE"));
                d.setCasoEspecial(rs.getString("CD_CASO_ESPECIAL"));
                d.setDataCasoEspecial(rs.getDate("DT_CASO_ESPECIAL"));
                if(rs.getString("CD_SEXO").equalsIgnoreCase("M")){
                    d.setMasculino(true);
                }else{
                    d.setMasculino(false);
                }
                d.setDataNascimento(rs.getDate("DT_NASCIMENTO"));
                d.setDataValidadeRetiradaMaterial(rs.getDate("DT_VALID_MATERIAL"));
                d.setDataValidadeRetiradaConvite(rs.getDate("DT_VALID_RET_CONVITE"));
                d.setDataValidadeReservaChurrasqueira(rs.getDate("DT_VALID_RESERVA"));

                String sqlFone = "SELECT * FROM TB_TELEFONE_PESSOA WHERE CD_MATRICULA = ? "
                    + "AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";
                PreparedStatement pFone = cn.prepareStatement(sqlFone);
                pFone.setInt(1, d.getMatricula());
                pFone.setInt(2, d.getSeqDependente());
                pFone.setInt(3, d.getIdCategoria());

                ResultSet rsFone = pFone.executeQuery();
                while(rsFone.next()){
                    char c = rsFone.getString("IC_TIPO").charAt(0);
                    String fone = rsFone.getString("NR_TELEFONE");
                    switch(c){
                        case 'R':
                            d.setTelefoneCelular(fone);
                        break;
                        case 'C':
                            d.setTelefoneComercial(fone);
                        break;
                        case 'L':
                            d.setTelefoneResidencial(fone);
                        break;
                    }
                }
                rsFone.close();

                d.setEmail(rs.getString("DE_EMAIL"));
                d.setIdCargoEspecial(rs.getInt("CD_CARGO_ESPECIAL"));
                if(rs.getString("CD_CARGO_ATIVO") == null ||
                        rs.getString("CD_CARGO_ATIVO").charAt(0) == 'N'){
                    d.setCargoAtivo(false);
                }else{
                    d.setCargoAtivo(true);
                }

                dependentes.add(d);
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
    }
    
    public void carregarTaxasAdministrativas(){
        String sql = "SELECT CD_TX_ADMINISTRATIVA FROM TB_NAO_GERA_TAXA_CARNE "
        + "WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = 0";

        taxasAdministrativas.clear();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, socio.getMatricula());
            p.setInt(2, socio.getIdCategoria());
            
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                taxasAdministrativas.put(rs.getInt("CD_TX_ADMINISTRATIVA"), rs.getString("CD_TX_ADMINISTRATIVA"));
            }
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
    
    public void atualizarTaxasAdministrativas(Auditoria audit){
        String sql = "DELETE TB_NAO_GERA_TAXA_CARNE WHERE CD_MATRICULA = ? "
                + "AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = 0";
       
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, socio.getMatricula());
            p.setInt(2, socio.getIdCategoria());
            p.executeUpdate();

            sql = "INSERT INTO TB_NAO_GERA_TAXA_CARNE VALUES(? , ?, 0, ?)";
            p = cn.prepareStatement(sql);
            for(int i : taxasAdministrativas.keySet()){
                p.setInt(1, socio.getMatricula());
                p.setInt(2, socio.getIdCategoria());
                p.setInt(3, i);
                p.executeUpdate();
            }

            cn.commit();
            
            audit.registrarMudanca(socio.getIdCategoria() + "/" + socio.getMatricula() + socio.getNome());
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
    
    public void carregarTaxasIndividuais(){
        String sql = "SELECT CD_TX_ADMINISTRATIVA FROM TB_TAXA_BLOQUEADA_PESSOA "
        + "WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = 0";

        taxasIndividuais.clear();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, socio.getMatricula());
            p.setInt(2, socio.getIdCategoria());
            
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                taxasIndividuais.put(rs.getInt("CD_TX_ADMINISTRATIVA"), rs.getString("CD_TX_ADMINISTRATIVA"));
            }
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
    
    public void atualizarTaxasIndividuais(Auditoria audit){
        String sql = "DELETE FROM TB_TAXA_BLOQUEADA_PESSOA WHERE CD_MATRICULA = ? "
                + "AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = 0";
       
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, socio.getMatricula());
            p.setInt(2, socio.getIdCategoria());
            p.executeUpdate();

            sql = "INSERT INTO TB_TAXA_BLOQUEADA_PESSOA "
                    + "(CD_MATRICULA, SEQ_DEPENDENTE, CD_CATEGORIA, CD_TX_ADMINISTRATIVA) VALUES (? , 0, ?, ?)";
            p = cn.prepareStatement(sql);
            for(int i : taxasIndividuais.keySet()){
                p.setInt(1, socio.getMatricula());
                p.setInt(2, socio.getIdCategoria());
                p.setInt(3, i);
                p.executeUpdate();
            }

            cn.commit();
            
            audit.registrarMudanca(socio.getIdCategoria() + "/" + socio.getMatricula() + socio.getNome());
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
    public static int getSdconvite(int matricula, int idCategoria){

        Connection cn = null;
        int s = 0;
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "SELECT SD_CONVITE FROM VW_COMPLEMENTO WHERE CD_MATRICULA = ? AND "
                + "SEQ_DEPENDENTE = 0 AND CD_CATEGORIA = ?";
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                
                s = rs.getInt("SD_CONVITE");
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
        return s;
    }

    public static int getSdconviteSauna(int matricula, int idCategoria){

        Connection cn = null;
        int s = 0;
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "SELECT SD_CONVITE_SAUNA FROM VW_COMPLEMENTO WHERE CD_MATRICULA = ? AND "
                + "SEQ_DEPENDENTE = 0 AND CD_CATEGORIA = ?";
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                
                s = rs.getInt("SD_CONVITE_SAUNA");
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
        return s;
    }

    public void atualizarDataAdmissao(Auditoria audit){
        String sql = "UPDATE TB_PESSOA SET DT_INCL_PESSOA = ? WHERE "
            + "CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = 0";
       
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setDate(1, new java.sql.Date(dataAdmissao.getTime()));
            p.setInt(2, socio.getMatricula());
            p.setInt(3, socio.getIdCategoria());
            p.executeUpdate();

            cn.commit();            
            audit.registrarMudanca(socio.getIdCategoria() + "/" + socio.getMatricula() + socio.getNome());
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
