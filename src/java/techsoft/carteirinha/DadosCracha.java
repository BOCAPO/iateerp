package techsoft.carteirinha;

import java.io.Serializable;
import java.util.Date;
import techsoft.tabelas.FuncionarioHorario;

public class DadosCracha implements Serializable{
    private static final long serialVersionUID = 20180102L;

    private String primeiroNome;
    private String nome;
    private String numeroCracha;
    private int matricula;
    private String setor;
    private String cargo;
    private String tipoSanguineo;
    private boolean funcionario = false;
    private FuncionarioHorario[] horarios;

    public Date dataVencimento;
    public String iateHost;
    public int iatePort;
    public String iateApp;
    public String urlFotoFuncionario;
    
    public String getPrimeiroNome() {
        return primeiroNome;
    }
    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNumeroCracha() {
        return numeroCracha;
    }
    public void setNumeroCracha(String numeroCracha) {
        this.numeroCracha = numeroCracha;
    }
    public int getMatricula() {
        return matricula;
    }
    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
    public String getSetor() {
        return setor;
    }
    public void setSetor(String setor) {
        this.setor = setor;
    }
    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public String getTipoSanguineo() {
        return tipoSanguineo;
    }
    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }
    public boolean isFuncionario() {
        return funcionario;
    }
    public void setFuncionario(boolean funcionario) {
        this.funcionario = funcionario;
    }
    public FuncionarioHorario[] getHorarios() {
        return horarios;
    }
    public void setHorarios(FuncionarioHorario[] horarios) {
        this.horarios = horarios;
    }
    public Date getDataVencimento() {
        return dataVencimento;
    }
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
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
    public String getUrlFotoFuncionario() {
        return urlFotoFuncionario;
    }
    public void setUrlFotoFuncionario(String urlFotoFuncionario) {
        this.urlFotoFuncionario = urlFotoFuncionario;
    }

}

