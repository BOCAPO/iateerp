package techsoft.cadastro;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class AutorizacaoInternet {
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.AplicacaoInternet");
    private Socio socio;
    private boolean autorizado;
    private String senha;
    //Usada para permissoes de acesso a internet
    private Map<Integer, String> aplicacoesInternet = new HashMap<Integer, String>();    

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }
    
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public Map<Integer, String> getAplicacoesInternet() {
        return aplicacoesInternet;
    }

    public void setApicacoesInternet(Map<Integer, String> aplicacoesInternet) {
        this.aplicacoesInternet = aplicacoesInternet;
    }
        
    public static AutorizacaoInternet getInstance(Socio socio){
        AutorizacaoInternet a = new AutorizacaoInternet();
        a.socio = socio;
        Connection cn = null;

        try {
            String sql = "SELECT USER_ACESSO_SISTEMA, SENHA_USUARIO_SISTEMA FROM TB_USUARIO_SISTEMA "
                    + "WHERE CD_MATRICULA = ? AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";            
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, socio.getMatricula());
            p.setInt(2, socio.getSeqDependente());
            p.setInt(3, socio.getIdCategoria());
            ResultSet rs = p.executeQuery();
            
            if(rs.next()){
                a.senha = rs.getString(2);
                a.autorizado = true;

                sql = "SELECT CD_APLICACAO FROM TB_APL_INTERNET_PESSSOA "
                        + "WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = ?";            
                PreparedStatement p2 = cn.prepareStatement(sql);
                p2.setInt(1, socio.getMatricula());
                p2.setInt(2, socio.getIdCategoria());
                p2.setInt(3, socio.getSeqDependente());

                ResultSet rs2 = p2.executeQuery();
                while(rs2.next()){
                    a.aplicacoesInternet.put(rs2.getInt("CD_APLICACAO"), rs2.getString("CD_APLICACAO"));
                }                
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

        return a;
    }
            
    public void atualizar(Auditoria audit){

        Connection cn = null;

        try {
            DecimalFormat f1 = new DecimalFormat("00");
            DecimalFormat f2 = new DecimalFormat("0000");
            String usuario = f1.format(socio.getIdCategoria());
            usuario += f2.format(socio.getMatricula());
            usuario += f1.format(socio.getSeqDependente());
            
            PreparedStatement p = null;
            String sql;
            cn = Pool.getInstance().getConnection();            
            
            //if(aplicacoesInternet.isEmpty()){
            if(!autorizado){
                sql = "UPDATE TB_USUARIO_SISTEMA SET CD_MATRICULA = NULL, "
                        + "CD_CATEGORIA = NULL, SEQ_DEPENDENTE = NULL WHERE USER_ACESSO_SISTEMA = ?";

                p = cn.prepareStatement(sql);
                p.setString(1, usuario);
                p.executeUpdate();    
                audit.registrarMudanca(sql, usuario);
            }else{
                String tmp = String.valueOf(socio.getMatricula());
                tmp += "/";
                tmp += String.valueOf(socio.getIdCategoria());
                tmp += " - ";
                tmp += socio.getNome();
                
                sql = "{call SP_USUARIO_SOCIO(?, ?, ?, ?, ?, ?, '01/01/1900')}";
                CallableStatement c = cn.prepareCall(sql);
                ParametroAuditoria par = new ParametroAuditoria();
                c.setString(1, par.getSetParametro(usuario));
                c.setString(2, par.getSetParametro(tmp));
                c.setString(3, par.getSetParametro(senha));
                c.setInt(4, par.getSetParametro(socio.getMatricula()));
                c.setInt(5, par.getSetParametro(socio.getSeqDependente()));
                c.setInt(6, par.getSetParametro(socio.getIdCategoria()));
                c.executeUpdate();
                audit.registrarMudanca(sql, par.getParametroFinal());
            }
        
            sql = "DELETE FROM TB_APL_INTERNET_PESSSOA WHERE CD_MATRICULA = ? "
                    + "AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = ?";
            
            p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setInt(1, par.getSetParametro(socio.getMatricula()));
            p.setInt(2, par.getSetParametro(socio.getIdCategoria()));
            p.setInt(3, par.getSetParametro(socio.getSeqDependente()));
            p.executeUpdate();
            audit.registrarMudanca(sql, par.getParametroFinal());

            sql = "INSERT INTO TB_APL_INTERNET_PESSSOA "
                    + "(CD_MATRICULA, SEQ_DEPENDENTE, CD_CATEGORIA, CD_APLICACAO) VALUES (? , ?, ?, ?)";
            p = cn.prepareStatement(sql);
            for(int i : aplicacoesInternet.keySet()){
                p.setInt(1, socio.getMatricula());
                p.setInt(2, socio.getSeqDependente());
                p.setInt(3, socio.getIdCategoria());
                p.setInt(4, i);
                p.executeUpdate();
                audit.registrarMudanca(sql, String.valueOf(socio.getMatricula()), String.valueOf(socio.getSeqDependente()), String.valueOf(socio.getIdCategoria()), String.valueOf(i));
            }

            cn.commit();
            
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
    
    /*    comentei tudo pra baixo pq nao estava sendo usado no projeto e estava sem auditoria
          talvez seja usado no projeto da internet 
    
    public void atualizarSenhaInternet(Auditoria audit){
        String sql = "DELETE FROM TB_APL_INTERNET_PESSSOA WHERE CD_MATRICULA = ? "
                + "AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = ?";
       
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, socio.getMatricula());
            p.setInt(2, socio.getIdCategoria());
            p.setInt(3, socio.getSeqDependente());
            p.executeUpdate();

            sql = "INSERT INTO TB_APL_INTERNET_PESSSOA "
                    + "(CD_MATRICULA, SEQ_DEPENDENTE, CD_CATEGORIA, CD_APLICACAO) VALUES (? , ?, ?, ?)";
            p = cn.prepareStatement(sql);
            for(int i : aplicacoesInternet.keySet()){
//                p.setInt(1, matricula);
//                p.setInt(2, seqDependente);                
//                p.setInt(3, idCategoria);
                p.setInt(4, i);
                p.executeUpdate();
            }

            cn.commit();
            
           // audit.registrarMudanca(idCategoria+ "/" + matricula + nome);
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
    
    public void carregarSenhaInternet(){
        String sql = "DELETE FROM TB_APL_INTERNET_PESSSOA WHERE CD_MATRICULA = ? "
                + "AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = ?";
       
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
//            p.setInt(1, matricula);
//            p.setInt(2, idCategoria);
//            p.setInt(3, seqDependente);
            p.executeUpdate();

            sql = "INSERT INTO TB_APL_INTERNET_PESSSOA "
                    + "(CD_MATRICULA, SEQ_DEPENDENTE, CD_CATEGORIA, CD_APLICACAO) VALUES (? , ?, ?, ?)";
            p = cn.prepareStatement(sql);
            for(int i : aplicacoesInternet.keySet()){
//                p.setInt(1, matricula);
//                p.setInt(2, seqDependente);                
//                p.setInt(3, idCategoria);
                p.setInt(4, i);
                p.executeUpdate();
            }

            cn.commit();
            
           // audit.registrarMudanca(idCategoria+ "/" + matricula + nome);
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
    */
}
