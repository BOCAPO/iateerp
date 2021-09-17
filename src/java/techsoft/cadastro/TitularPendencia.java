package techsoft.cadastro;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class TitularPendencia {

    private int id;//id na tabela TB_PESSOA_CADASTRO_PENDENTE
    private int matricula;
    private int idCategoria;
    private String email;
    private String celular;
    private Contato enderecoResidencial = new Contato();
    private Contato enderecoComercial = new Contato();
    private boolean showWarning;//mostra que alteracao esta sendo feita em um cadastro que ja tem pendencia
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.TitularPendencia");

    public int getId() {
        return id;
    }

    public int getMatricula() {
        return matricula;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Contato getEnderecoResidencial() {
        return enderecoResidencial;
    }

    public void setEnderecoResidencial(Contato enderecoResidencial) {
        this.enderecoResidencial = enderecoResidencial;
    }

    public Contato getEnderecoComercial() {
        return enderecoComercial;
    }

    public void setEnderecoComercial(Contato enderecoComercial) {
        this.enderecoComercial = enderecoComercial;
    }

    public boolean isShowWarning() {
        return showWarning;
    }
    
    public void alterarEnderecos(Auditoria audit){

        Connection cn = null;
        ParametroAuditoria par = new ParametroAuditoria();
        String sql = "";

        try {
            cn = Pool.getInstance().getConnection();
            
            if(id == 0){
                sql = "{call SP_INCLUI_PENDENCIA_CAD_PESSOA("
                        + "?, ?, 0,"//matricula, categoria, seqDependente
                        + "?, ?, ?, ?,"//telR, telC, celular, email
                        + "?, ?, ?, ?, ?,"//endereco,bairro,cidade,uf,cep (res)
                        + "?, ?, ?, ?, ?,"//endereco,bairro,cidade,uf,cep (com)
                        + "'P', ?)}";
                CallableStatement c = cn.prepareCall(sql);//login
                c.setInt(1, par.getSetParametro(matricula));
                c.setInt(2, par.getSetParametro(idCategoria));
                c.setString(3, par.getSetParametro(enderecoResidencial.getTelefone()));
                c.setString(4, par.getSetParametro(enderecoComercial.getTelefone()));
                c.setString(5, par.getSetParametro(celular));
                c.setString(6, par.getSetParametro(email));
                c.setString(7, par.getSetParametro(enderecoResidencial.getEndereco()));
                c.setString(8, par.getSetParametro(enderecoResidencial.getBairro()));
                c.setString(9, par.getSetParametro(enderecoResidencial.getCidade()));
                c.setString(10, par.getSetParametro(enderecoResidencial.getUF()));
                c.setString(11, par.getSetParametro(enderecoResidencial.getCEP()));
                c.setString(12, par.getSetParametro(enderecoComercial.getEndereco()));
                c.setString(13, par.getSetParametro(enderecoComercial.getBairro()));
                c.setString(14, par.getSetParametro(enderecoComercial.getCidade()));
                c.setString(15, par.getSetParametro(enderecoComercial.getUF()));
                c.setString(16, par.getSetParametro(enderecoComercial.getCEP()));                
                c.setString(17, par.getSetParametro(audit.getLogin()));

                c.executeUpdate();
                cn.commit();
            }else{
                sql = "UPDATE TB_PESSOA_CADASTRO_PENDENTE SET "
                        + "NR_TEL_RESID = ?, "
                        + "NR_TEL_COM = ?, "
                        + "NR_TEL_CEL = ?, "
                        + "EMAIL = ?, "
                        + "ENDERECO_R = ?, "
                        + "BAIRRO_R = ?, "
                        + "CIDADE_R = ?, "
                        + "UF_R = ?, "
                        + "CEP_R = ?, "
                        + "ENDERECO_C = ?, "
                        + "BAIRRO_C = ?, "
                        + "CIDADE_C = ?, "
                        + "UF_C = ?, "
                        + "CEP_C = ?, "                        
                        + "dt_ultima_atualizacao = GETDATE(), "
                        + "user_ultima_atualizacao = ? "
                        + "WHERE CD_PENDENCIA = ?";
                        
                PreparedStatement p = cn.prepareStatement(sql);
                p.setString(1, par.getSetParametro(enderecoResidencial.getTelefone()));
                p.setString(2, par.getSetParametro(enderecoComercial.getTelefone()));
                p.setString(3, par.getSetParametro(celular));
                p.setString(4, par.getSetParametro(email));
                p.setString(5, par.getSetParametro(enderecoResidencial.getEndereco()));
                p.setString(6, par.getSetParametro(enderecoResidencial.getBairro()));
                p.setString(7, par.getSetParametro(enderecoResidencial.getCidade()));
                p.setString(8, par.getSetParametro(enderecoResidencial.getUF()));
                p.setString(9, par.getSetParametro(enderecoResidencial.getCEP()));
                p.setString(10, par.getSetParametro(enderecoComercial.getEndereco()));
                p.setString(11, par.getSetParametro(enderecoComercial.getBairro()));
                p.setString(12, par.getSetParametro(enderecoComercial.getCidade()));
                p.setString(13, par.getSetParametro(enderecoComercial.getUF()));
                p.setString(14, par.getSetParametro(enderecoComercial.getCEP()));
                p.setString(15, par.getSetParametro(audit.getLogin()));
                p.setInt(16, id);

                p.executeUpdate();
                cn.commit();                
            }

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

    public static TitularPendencia getInstance(int matricula, int idCategoria){

        Connection cn = null;
        TitularPendencia tp = new TitularPendencia();
        tp.matricula = matricula;
        tp.idCategoria = idCategoria;
        
        String sql = "SELECT * FROM TB_PESSOA_CADASTRO_PENDENTE WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = 0 AND CD_SITUACAO = 'P'";
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                tp.showWarning = true;
                tp.id = rs.getInt("cd_pendencia");
                tp.email = rs.getString("Email");
                tp.celular = rs.getString("NR_TEL_CEL");
                tp.enderecoResidencial.setEndereco(rs.getString("ENDERECO_R"));
                tp.enderecoResidencial.setBairro(rs.getString("BAIRRO_R"));
                tp.enderecoResidencial.setCidade(rs.getString("CIDADE_R"));
                tp.enderecoResidencial.setUF(rs.getString("UF_R"));
                tp.enderecoResidencial.setCEP(rs.getString("CEP_R"));
                tp.enderecoResidencial.setTelefone(rs.getString("NR_TEL_RESID"));
                tp.enderecoComercial.setEndereco(rs.getString("ENDERECO_C"));
                tp.enderecoComercial.setBairro(rs.getString("BAIRRO_C"));
                tp.enderecoComercial.setCidade(rs.getString("CIDADE_C"));
                tp.enderecoComercial.setUF(rs.getString("UF_C"));
                tp.enderecoComercial.setCEP(rs.getString("CEP_C"));
                tp.enderecoComercial.setTelefone(rs.getString("NR_TEL_COM")); 
            }else{
                Titular t = Titular.getInstance(matricula, idCategoria);
                tp.showWarning = false;
                tp.id = 0;
                tp.email = t.getEmail();
                tp.celular = t.getTelefoneCelular();
                tp.enderecoComercial = t.getEnderecoComercial();
                tp.enderecoResidencial = t.getEnderecoResidencial();
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
        
        return tp;
    }
        
}
