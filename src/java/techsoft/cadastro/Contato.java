
package techsoft.cadastro;

public final class Contato {
    
    private String endereco;
    private String bairro;
    private String cidade;
    private String UF;
    private String CEP;
    private String telefone;

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getUF() {
        return UF;
    }

    public void setUF(String UF) {
        this.UF = UF;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

//    @Override
//    public String toString(){
//       return endereco + "; " + bairro + "; " + cidade + "; " + UF + "; " + CEP + "; " + telefone;
//    }
}
