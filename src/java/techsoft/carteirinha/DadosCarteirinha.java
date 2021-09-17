package techsoft.carteirinha;

import java.io.Serializable;

public class DadosCarteirinha implements Serializable{
    private static final long serialVersionUID = 20180102L;

    private int matricula;
    private String nome;
    private boolean titular;
    private String descricaoCategoria;//socio.descricaoCategoria
    private String descricaoCargoEspecial;//socio.descricaoCargoEspecial
    private String numeroCarteirinha;
    private String abreviacaoCategoria;
    private String corTitular;
    private String corDependente;
    private String dataVencimento;
    private boolean cargoEspecial;
    private String cargoEspecialCorCarteira;
    private String cargoEspecialCorFonte;
    private String urlFotoSocio;
    private String iateHost;
    private int iatePort;
    private String iateApp;

    public int getMatricula() {
        return matricula;
    }
    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public boolean isTitular() {
        return titular;
    }
    public void setTitular(boolean titular) {
        this.titular = titular;
    }
    public String getDescricaoCategoria() {
        return descricaoCategoria;
    }
    public void setDescricaoCategoria(String descricaoCategoria) {
        this.descricaoCategoria = descricaoCategoria;
    }
    public String getDescricaoCargoEspecial() {
        return descricaoCargoEspecial;
    }
    public void setDescricaoCargoEspecial(String descricaoCargoEspecial) {
        this.descricaoCargoEspecial = descricaoCargoEspecial;
    }
    public String getNumeroCarteirinha() {
        return numeroCarteirinha;
    }
    public void setNumeroCarteirinha(String numeroCarteirinha) {
        this.numeroCarteirinha = numeroCarteirinha;
    }
    public String getAbreviacaoCategoria() {
        return abreviacaoCategoria;
    }
    public void setAbreviacaoCategoria(String abreviacaoCategoria) {
        this.abreviacaoCategoria = abreviacaoCategoria;
    }
    public String getCorTitular() {
        return corTitular;
    }
    public void setCorTitular(String corTitular) {
        this.corTitular = corTitular;
    }
    public String getCorDependente() {
        return corDependente;
    }
    public void setCorDependente(String corDependente) {
        this.corDependente = corDependente;
    }
    public String getDataVencimento() {
        return dataVencimento;
    }
    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
    public boolean isCargoEspecial() {
        return cargoEspecial;
    }
    public void setCargoEspecial(boolean cargoEspecial) {
        this.cargoEspecial = cargoEspecial;
    }
    public String getCargoEspecialCorCarteira() {
        return cargoEspecialCorCarteira;
    }
    public void setCargoEspecialCorCarteira(String cargoEspecialCorCarteira) {
        this.cargoEspecialCorCarteira = cargoEspecialCorCarteira;
    }
    public String getCargoEspecialCorFonte() {
        return cargoEspecialCorFonte;
    }
    public void setCargoEspecialCorFonte(String cargoEspecialCorFonte) {
        this.cargoEspecialCorFonte = cargoEspecialCorFonte;
    }
    public String getUrlFotoSocio() {
        return urlFotoSocio;
    }
    public void setUrlFotoSocio(String urlFotoSocio) {
        this.urlFotoSocio = urlFotoSocio;
    }
    public String getIateHost() {
        return iateHost;
    }
    public void setIateHost(String iateHost) {
        this.iateHost = iateHost;
    }
    public int getIatePort() {
        return iatePort;
    }
    public void setIatePort(int iatePort) {
        this.iatePort = iatePort;
    }
    public String getIateApp() {
        return iateApp;
    }
    public void setIateApp(String iateApp) {
        this.iateApp = iateApp;
    }

}

