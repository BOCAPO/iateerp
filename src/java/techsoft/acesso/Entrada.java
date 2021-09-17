package techsoft.acesso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.text.DecimalFormat;
import techsoft.tabelas.InserirException;
import java.util.Date;

public class Entrada {

    private int documento;
    private String mensagem;
    private String foto;
    private String cor;
    private String corFonte;
    private String nome;
    private String cargoEspecial;
    private Date dtNasc;
    
    
    private static final Logger log = Logger.getLogger("techsoft.acesso.Entrada");
    
    public int getDocumento() {
        return documento;
    }
    public void setDocumento(int documento) {
        this.documento = documento;
    }
    public String getMensagem() {
        return mensagem;
    }
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public String getCor() {
        return cor;
    }
    public void setCor(String cor) {
        this.cor = cor;
    }
    public String getCorFonte() {
        return corFonte;
    }
    public void setCorFonte(String corFonte) {
        this.corFonte = corFonte;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCargoEspecial() {
        return cargoEspecial;
    }
    public void setCargoEspecial(String Especial) {
        this.cargoEspecial = cargoEspecial;
    }
    public Date getDtNasc() {
        return dtNasc;
    }
    public void setDtNasc(Date dtNasc) {
        this.dtNasc = dtNasc;
    }
    

    public static Entrada getInstance(int doc, int idLocal, String placa, int qtPessoa, String entradaSaida, String usuario) {
        Connection cn = Pool.getInstance().getConnection();
        String sql = "";
        ResultSet rs2 = null;
        CallableStatement p2 = null;
        
        
        Entrada e = new Entrada();
        DecimalFormat f = new DecimalFormat("000000000");
        String docStr = String.valueOf(f.format(Integer.valueOf(doc)));

        //ESTE É O MAIOR IF DO PROGRAMA, VERIFICA QUAL O TIPO DE DOCUMENTO
        //     01 - CARTEIRA
        //     02 - PASSAPORTE
        //     03 - CONVITE
        //     04 - PERMISSAO PROVISORIA
        //     05 - CRACHA DE VISITANTE
        //     06 - AUTORIZACAO DE EMBARQUE
        //     07 - FUNCIONARIO
        

        if ("1".equals(docStr.substring(1, 2))){
            //     01 - CARTEIRA
            sql = "SP_ACESSO_CARTEIRA";
            e.foto = "f?tb=61&cart="+docStr; 
        }else{
           if ("2".equals(docStr.substring(1, 2))){
            //     02 - PASSAPORTE
               sql = "SP_ACESSO_PASSAPORTE";
               e.foto = "f?tb=62&pass="+docStr; 
           }else if ("3".equals(docStr.substring(1, 2)) || "8".equals(docStr.substring(1, 2))){
            //     03 ou 08 - CONVITE
               sql = "SP_ACESSO_CONVITE";
               e.foto = "f?tb=4&cd="+docStr; 
           }else if ("4".equals(docStr.substring(1, 2))){
            //     04 - PERMISSAO PROVISORIA   
               sql = "SP_ACESSO_PERMISSAO_PROVISORIA";
               e.foto = "f?tb=21&nr="+docStr; 
           }else if ("5".equals(docStr.substring(1, 2))){
           //     05 - CRACHA DE VISITANTE   
               sql = "SP_ACESSO_CRACHA_VISITANTE";
               e.foto = "f?tb=71&nr="+docStr; 
           }else if ("6".equals(docStr.substring(1, 2))){
           //     06 - AUTORIZACAO DE EMBARQUE   
               sql = "SP_ACESSO_AUTORIZACAO_EMBARQUE";
               e.foto = ""; 
           }else if ("7".equals(docStr.substring(1, 2))){
           //     07 - FUNCIONARIO
               sql = "SP_ACESSO_FUNCIONARIO";
               e.foto = "f?tb=51&nr="+docStr; 
           }else {
               e.documento = doc;
               e.mensagem = "PROIBIR ACESSO:<br>&nbsp&nbsp Documento Inexistente.";
               e.cor = "red";
               e.corFonte = "black";
               return e;
           }
               
        }
        
        sql = "{call " + sql + "(?, ?, ?, ?, ?, ?)}";
        
        try{
            p2 = cn.prepareCall(sql);

            p2.setInt(1, doc);
            p2.setInt(2, idLocal);
            p2.setString(3, entradaSaida);
            if ("".equals(placa)){
                p2.setNull(4, java.sql.Types.VARCHAR);
            }else{
                p2.setString(4, placa);
            }
            
            p2.setInt(5, qtPessoa);
            p2.setString(6, usuario);
            p2.execute();

            rs2 = p2.getResultSet();

            if(rs2.next()){
                e.documento = doc;
                
                if ("NF".equals(rs2.getString("TP_MSG_ACESSO"))){
                    e.mensagem = "PROIBIR ACESSO:<br>&nbsp&nbsp Documento Inexistente.";
                    e.cor = "red";
                    e.corFonte = "black";
                }else if ("PA".equals(rs2.getString("TP_MSG_ACESSO"))){
                    e.mensagem = rs2.getString("DE_MSG_RETORNO");
                    e.cor = "red";
                    e.corFonte = "black";
                }else if ("PP".equals(rs2.getString("TP_MSG_ACESSO"))){
                    e.mensagem = rs2.getString("DE_MSG_RETORNO");
                    e.cor = "red";
                    e.corFonte = "black";
                }else if ("CI".equals(rs2.getString("TP_MSG_ACESSO"))){
                    e.mensagem = rs2.getString("DE_MSG_RETORNO");
                    e.cor = "black";
                    e.corFonte = "white";
                }else if ("AL".equals(rs2.getString("TP_MSG_ACESSO"))){
                    e.mensagem = rs2.getString("DE_MSG_RETORNO");
                    e.cor = "yellow";
                    e.corFonte = "black";
                }else{
                    e.mensagem = rs2.getString("DE_MSG_RETORNO");
                    e.cor = "green";
                    e.corFonte = "black";
                }
                
                e.mensagem = e.mensagem.replaceAll("@ENTER@", "<br>");
                e.mensagem = e.mensagem.replaceAll(" ", "&nbsp");
                
                if (!"NF".equals(rs2.getString("TP_MSG_ACESSO"))){
                    e.nome = rs2.getString("NOME_PESSOA");

                    if ("1".equals(docStr.substring(1, 2))){
                        //carteira tem data de nascimento e cargo especial
                        e.mensagem =  rs2.getString("DE_TODAS_PLACAS") + "<br>" + e.mensagem;
                        e.dtNasc = rs2.getDate("dt_nascimento");
                        e.cargoEspecial = rs2.getString("DESCR_CARGO_ESPECIAL");
                    }
                }
            }

            cn.commit();
        } catch (Exception ex) {
            log.severe(ex.getMessage() + " - sql: "+sql+" - parametros: "+doc+","+idLocal+","+entradaSaida+","+placa+","+qtPessoa+","+usuario);
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
        
        
        return e;
    }


    
}
