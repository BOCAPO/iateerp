
package techsoft.seguranca;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import techsoft.db.Pool;


public class Usuario {

    private String login;
    private String nome;
    private String ativo;
    private String observacao;
    private List<Grupo> grupos = new ArrayList<Grupo>();
    private List<Grupo> gruposDisponiveis = new ArrayList<Grupo>();
    private Map<Integer, String> taxasIndividuais = new HashMap<Integer, String>();
    private Map<Integer, String> centrosCustos = new HashMap<Integer, String>();
    
    private static final Logger log = Logger.getLogger("techsoft.seguranca.Usuario");

    public Usuario(String login, String nome){
    	this.login = login.trim();
        this.nome = nome.trim();
    }
    public String getLogin() {
            return login;
    }
    public String getNome() {
            return nome;
    }
    public void setNome(String nome) {
            this.nome = nome.trim();
    }
    public String getAtivo() {
        return ativo;
    }
    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }
    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<Grupo> getGrupos() {
            return grupos;
    }

    public List<Grupo> getGruposDisponiveis() {
            return gruposDisponiveis;
    }
    public Map<Integer, String> getTaxasIndividuais() {
        return taxasIndividuais;
    }
    public void setTaxasIndividuais(Map<Integer, String> taxasIndividuais) {
        this.taxasIndividuais = taxasIndividuais;
    }
    public Map<Integer, String> getCentrosCustos() {
        return centrosCustos;
    }
    public void setCentrosCustos(Map<Integer, String> centrosCustos) {
        this.centrosCustos = centrosCustos;
    }

    public static List<Usuario> listar(){
        ArrayList<Usuario> l = new ArrayList<Usuario>();
        String sql = "SELECT USER_ACESSO_SISTEMA, NOME_USUARIO_SISTEMA, IC_ATIVO, DE_OBSERVACAO FROM TB_USUARIO_SISTEMA WHERE CD_MATRICULA IS NULL AND DT_EXPIRACAO IS NULL ORDER BY 1";
        Connection cn = null;

        try {    
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Usuario us = new Usuario(rs.getString(1), rs.getString(2));
                us.setAtivo(rs.getString(3));
                us.setObservacao(rs.getString(4));
                us.carregarTaxasIndividuais();
                us.carregarCentrosCustos();
                l.add(us);
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
    
    public void carregarTaxasIndividuais(){
        String sql = "SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL "
        + "WHERE USER_ACESSO_SISTEMA = ? ";

        taxasIndividuais.clear();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, login);
            
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
    
    public void carregarCentrosCustos(){
        String sql = "SELECT CD_TRANSACAO FROM TB_USUARIO_CENTRO_CUSTO "
        + "WHERE USER_ACESSO_SISTEMA = ? ";

        centrosCustos.clear();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, login);
            
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                centrosCustos.put(rs.getInt("CD_TRANSACAO"), rs.getString("CD_TRANSACAO"));
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

    public static List<Usuario> listarInternet(){
        ArrayList<Usuario> l = new ArrayList<Usuario>();
        String sql = "SELECT USER_ACESSO_SISTEMA, NOME_USUARIO_SISTEMA FROM "
                + "TB_USUARIO_SISTEMA WHERE CD_MATRICULA IS NOT NULL ORDER BY 2";
        Connection cn = null;

        try {    
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                l.add(new Usuario(rs.getString(1), rs.getString(2)));
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
    
    public static Usuario getInstance(String login){
        Usuario u = null;
        String sql = "SELECT USER_ACESSO_SISTEMA, NOME_USUARIO_SISTEMA, IC_ATIVO, DE_OBSERVACAO "
        		+ "FROM TB_USUARIO_SISTEMA WHERE USER_ACESSO_SISTEMA = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, login);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                u = new Usuario(rs.getString(1), rs.getString(2));
                u.setAtivo(rs.getString(3));
                u.setObservacao(rs.getString(4));
                
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

        return u;
    }

    /**
     * Carrega os grupos que o usuario tem e os grupos disponiveis para
     * serem acrescentadas para esse usuario.
     */
    public void carregarGrupos(){
        grupos.clear();
        gruposDisponiveis.clear();
        
        String sql = "{call SP_RECUPERA_GRP_ASSOC (?)}";

        String sql2 = "SELECT * FROM TB_GRUPO WHERE CD_GRUPO NOT IN "
                + "(SELECT CD_GRUPO FROM tb_usuario_por_grupo WHERE user_acesso_sistema = ?) ORDER BY DESCR_GRUPO";
        
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall(sql);
            cal.setString(1, login);
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                grupos.add(new Grupo(rs.getInt(1), rs.getString(2)));
            }
            
            PreparedStatement p = cn.prepareStatement(sql2);
            p.setString(1, login);
            rs = p.executeQuery();
            while (rs.next()) {
                gruposDisponiveis.add(new Grupo(rs.getInt(1), rs.getString(2)));
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
    
    public void excluir(Auditoria audit){
        grupos.clear();
        gruposDisponiveis.clear();
        
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_USUARIO_SISTEMA WHERE USER_ACESSO_SISTEMA = ?";
            cn = Pool.getInstance().getConnection();
            
            PreparedStatement p = cn.prepareStatement("DELETE FROM TB_USUARIO_POR_GRUPO WHERE USER_ACESSO_SISTEMA = ?");
            p.setString(1, login);
            p.executeUpdate();
            
            cn.commit();
            audit.registrarMudanca(sql, login);
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

    public void ativarDesativa(Auditoria audit){
        grupos.clear();
        gruposDisponiveis.clear();
        
        Connection cn = null;

        try {
            String sql = "UPDATE TB_USUARIO_SISTEMA SET IC_ATIVO = CASE WHEN ISNULL(IC_ATIVO, 'S') = 'S' THEN 'N' ELSE 'S' END WHERE USER_ACESSO_SISTEMA = '" + login + "'";
            cn = Pool.getInstance().getConnection();
            
            PreparedStatement p = cn.prepareStatement("UPDATE TB_USUARIO_SISTEMA SET IC_ATIVO = CASE WHEN ISNULL(IC_ATIVO, 'S') = 'S' THEN 'N' ELSE 'S' END WHERE USER_ACESSO_SISTEMA = ?");
            p.setString(1, login);
            p.executeUpdate();
            
            cn.commit();
            audit.registrarMudanca(sql, login);
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
    
    public void inserir(Auditoria audit){

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();


            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_USUARIO_SISTEMA WHERE USER_ACESSO_SISTEMA = ?");
            p.setString(1, login);
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                log.warning("Usuario ja cadastrado" + login);
                return;
            }else{
                String sql = "{call SP_INCLUIR_USUARIO (?, ?, ?, ?)}";
                CallableStatement cal = cn.prepareCall(sql);
                cal.setString(1, login);
                cal.setString(2, nome);
                cal.setString(3, "IATE01");
                cal.setString(4, observacao);
                cal.executeUpdate();

                cn.commit();
                audit.registrarMudanca(sql, login, nome, "IATE01");
            }
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

        try {
            String sql = "{call SP_ALTERAR_USUARIO (?, ?, ?)}";
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall(sql);
            cal.setString(1, login);
            cal.setString(2, nome);
            cal.setString(3, observacao);
            cal.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, login, nome);
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

    public void excluirGrupo(int idGrupo, Auditoria audit){
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_USUARIO_POR_GRUPO WHERE USER_ACESSO_SISTEMA = ? AND CD_GRUPO = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, login);
            p.setInt(2, idGrupo);
            p.executeUpdate();

            cn.commit();
            
            audit.registrarMudanca(sql, login, String.valueOf(idGrupo));
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

    public void adicionarGrupo(int idGrupo, Auditoria audit){
        Connection cn = null;

        try {
            String sql = "INSERT INTO TB_USUARIO_POR_GRUPO VALUES (?, ?)";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, login);
            p.setInt(2, idGrupo);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, login, String.valueOf(idGrupo));
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

    public void alterarSenha(String senhaAtual, String novaSenha)throws AlterarSenhaException{

        /*
         * A validacao para a nova senha eh feita no formulario
         * via javascript. Essa validacao nao eh preciso para
         * a camada de negocio porque se nao foi usado o formulario
         * significa que alguem esta chamando a camanda de negicio
         * diretamente e sabe o que esta fazendo.
         *
         * Ja a senha atual eh preciso porque pode ser um ataque.
         */
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            if("SESI01".equalsIgnoreCase(novaSenha)){
                log.warning("SESI01 não é uma senha válida");
                throw new AlterarSenhaException("SESI01 não é uma senha válida");
            }
            
            PreparedStatement p = cn.prepareStatement(
                    "SELECT * FROM TB_USUARIO_SISTEMA WHERE "
                    + "USER_ACESSO_SISTEMA = ? AND SENHA_USUARIO_SISTEMA = ?");

            p.setString(1, login);
            p.setString(2, senhaAtual);
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                definirSenha(novaSenha);
                //GravaLog 9020 -> estah comentado no VB
            }else{
                log.warning("A senha atual n�o confere");
                throw new AlterarSenhaException("A senha atual n�o confere");
            }

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
     * Define a senha do usuario sem verificar a senha atual.
     * Requer permissao 8034 para essa operacao
     */
    public void definirSenha(String senha){

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            PreparedStatement p = cn.prepareStatement("UPDATE TB_USUARIO_SISTEMA "
                    + "SET SENHA_USUARIO_SISTEMA = ? WHERE USER_ACESSO_SISTEMA = ?");
            p.setString(1, senha);
            p.setString(2, login);
            p.executeUpdate();

            cn.commit();
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
