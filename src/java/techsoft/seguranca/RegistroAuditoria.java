
package techsoft.seguranca;

import java.util.Date;

public class RegistroAuditoria {

    private Date dataRegistro;
    private Permissao permissao;
    private String login;
    private String descricao;
    private String estacao;

    //soh a classe auditoria deve instanciar objetos RegistroAuditoria
    RegistroAuditoria(Date dataRegistro, Permissao permissao, String login) {
        this.dataRegistro = dataRegistro;
        this.permissao = permissao;
        this.login = login;
    }

    RegistroAuditoria(Date dataRegistro, Permissao permissao, String login, String descricao) {
        this(dataRegistro, permissao, login);
        this.descricao = descricao;
    }
    
    RegistroAuditoria(Date dataRegistro, Permissao permissao, String login, String descricao, String estacao) {
        this(dataRegistro, permissao, login, descricao);
        this.estacao = estacao;
    }
    
    public Permissao getPermissao(){
        return permissao;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public String getLogin() {
        return login;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public String getEstacao() {
        return estacao;
    }
    
}
