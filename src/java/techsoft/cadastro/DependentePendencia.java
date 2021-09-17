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

public class DependentePendencia {

    private int id;//id na tabela TB_PESSOA_CADASTRO_PENDENTE
    private int matricula;
    private int idCategoria;
    private int seqDependente;
    private String email;
    private String celular;
    private String telefoneResidencial;
    private String telefoneComercial;
    private boolean showWarning;//mostra que alteracao esta sendo feita em um cadastro que ja tem pendencia
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.DependentePendencia");

    public int getId() {
        return id;
    }
    
    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getSeqDependente() {
        return seqDependente;
    }

    public void setSeqDependente(int seqDependente) {
        this.seqDependente = seqDependente;
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

    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public boolean isShowWarning() {
        return showWarning;
    }
    
    public static DependentePendencia getInstance(int matricula, int idCategoria, int seqDependente){

        Connection cn = null;
        DependentePendencia dp = new DependentePendencia();
        dp.matricula = matricula;
        dp.idCategoria = idCategoria;
        dp.seqDependente = seqDependente;
        
        String sql = "SELECT * FROM TB_PESSOA_CADASTRO_PENDENTE WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = ? AND CD_SITUACAO = 'P'";
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            p.setInt(3, seqDependente);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                dp.showWarning = true;
                dp.id = rs.getInt("cd_pendencia");
                dp.email = rs.getString("Email");
                dp.celular = rs.getString("NR_TEL_CEL");
                dp.telefoneResidencial= rs.getString("NR_TEL_RESID");
                dp.telefoneComercial = rs.getString("NR_TEL_COM"); 
            }else{
                Dependente d = Dependente.getInstance(matricula, seqDependente, idCategoria);
                dp.showWarning = false;
                dp.id = 0;
                dp.email = d.getEmail();
                dp.celular = d.getTelefoneCelular();
                dp.telefoneComercial = d.getTelefoneComercial();
                dp.telefoneResidencial = d.getTelefoneResidencial();
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
        
        return dp;
    }
    
    public void alterar(Auditoria audit){

        Connection cn = null;
        String sql = "";
        ParametroAuditoria par = new ParametroAuditoria();
        try {
            cn = Pool.getInstance().getConnection();
            
            if(id == 0){
                sql = "{call SP_INCLUI_PENDENCIA_CAD_PESSOA("
                        + "?, ?, ?,"//matricula, categoria, seqDependente
                        + "?, ?, ?, ?,"//telR, telC, celular, email
                        + "NULL, NULL, NULL, NULL, NULL,"//endereco,bairro,cidade,uf,cep (res)
                        + "NULL, NULL, NULL, NULL, NULL,"//endereco,bairro,cidade,uf,cep (com)
                        + "'P', ?)}";
                CallableStatement c = cn.prepareCall(sql);//login
                c.setInt(1, par.getSetParametro(matricula));
                c.setInt(2, par.getSetParametro(idCategoria));
                c.setInt(3, par.getSetParametro(seqDependente));
                c.setString(4, par.getSetParametro(telefoneResidencial));
                c.setString(5, par.getSetParametro(telefoneComercial));
                c.setString(6, par.getSetParametro(celular));
                c.setString(7, par.getSetParametro(email));
                c.setString(8, audit.getLogin());

                c.executeUpdate();
                cn.commit();
            }else{
                sql = "UPDATE TB_PESSOA_CADASTRO_PENDENTE SET "
                        + "NR_TEL_RESID = ?, "
                        + "NR_TEL_COM = ?, "
                        + "NR_TEL_CEL = ?, "
                        + "EMAIL = ?, "                    
                        + "dt_ultima_atualizacao = GETDATE(), "
                        + "user_ultima_atualizacao = ? "
                        + "WHERE CD_PENDENCIA = ?";
                        
                PreparedStatement p = cn.prepareStatement(sql);
                
                p.setString(1, par.getSetParametro(telefoneResidencial));
                p.setString(2, par.getSetParametro(telefoneComercial));
                p.setString(3, par.getSetParametro(celular));
                p.setString(4, par.getSetParametro(email));
                p.setString(5, par.getSetParametro(audit.getLogin()));
                p.setInt(6, par.getSetParametro(id));

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
    
}
