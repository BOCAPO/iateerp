
package techsoft.clube;

import java.sql.CallableStatement;
import techsoft.tabelas.InserirException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;

public class CartaoNautica {

    private int nrCartao;
    private int idEvento;
    private String categoria;
    private String clube;
    private String uf;
    private String numeracao;
    private String nome;
    
    private static final Logger log = Logger.getLogger("techsoft.clube.CartaoNautica");

    public int getNrCartao() {
        return nrCartao;
    }
    public void setNrCartao(int nrCartao) {
        this.nrCartao = nrCartao;
    }
    public int getIdEvento() {
        return idEvento;
    }
    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getClube() {
        return clube;
    }
    public void setClube(String clube) {
        this.clube = clube;
    }
    public String getUf() {
        return uf;
    }
    public void setUf(String uf) {
        this.uf = uf;
    }
    public String getNumeracao() {
        return numeracao;
    }
    public void setNumeracao(String numeracao) {
        this.numeracao = numeracao;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
   
    public static List<CartaoNautica> listar(int id){
        ArrayList<CartaoNautica> l = new ArrayList<CartaoNautica>();
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        
        try {
            cal = cn.prepareCall("select * from tb_cartao_nautica where seq_evento = ?");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                CartaoNautica d = new CartaoNautica();
                d.nrCartao = rs.getInt("NR_CARTAO");
                d.idEvento = rs.getInt("SEQ_EVENTO");
                d.categoria = rs.getString("DE_CATEGORIA");
                d.clube = rs.getString("DE_CLUBE");
                d.uf = rs.getString("SG_UF");
                d.numeracao = rs.getString("DE_NUMERACAO");
                d.nome = rs.getString("NO_USUARIO");

                
                l.add(d);
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

    public static CartaoNautica getInstance(int cartao){
        CartaoNautica d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("select * from tb_cartao_nautica where NR_CARTAO = ?");
            cal.setInt(1, cartao);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new CartaoNautica();
                d.idEvento = rs.getInt("SEQ_EVENTO");
                d.nrCartao = rs.getInt("NR_CARTAO");
                d.categoria = rs.getString("DE_CATEGORIA");
                d.clube = rs.getString("DE_CLUBE");
                d.uf = rs.getString("SG_UF");
                d.numeracao = rs.getString("DE_NUMERACAO");
                d.nome = rs.getString("NO_USUARIO");
                
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

        return d;
    }

    public void excluir(Auditoria audit) throws ExcluirException {
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("DELETE FROM TB_CARTAO_NAUTICA WHERE NR_CARTAO = ?");
            cal.setInt(1, nrCartao);
            cal.executeUpdate();
            cn.commit();
            audit.registrarMudanca("DELETE FROM TB_CARTAO_NAUTICA WHERE NR_CARTAO = ?", String.valueOf(nrCartao));
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }

    public void inserir(Auditoria audit)throws InserirException{

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            
            cal = cn.prepareCall("{call [SP_ATUALIZA_NR_CARTAO]}");
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                nrCartao = rs.getInt("NR_CARTAO");
            }
            
            ParametroAuditoria par = new ParametroAuditoria();
            String sql = "INSERT INTO TB_CARTAO_NAUTICA (NR_CARTAO, SEQ_EVENTO, DE_CATEGORIA, DE_CLUBE, SG_UF, DE_NUMERACAO, NO_USUARIO) VALUES(?, ?, ?, ?, ?, ?, ?)";
            cal = cn.prepareCall(sql);
            cal.setInt(1, par.getSetParametro(nrCartao));
            cal.setInt(2, par.getSetParametro(idEvento));
            cal.setString(3, par.getSetParametro(categoria));
            cal.setString(4, par.getSetParametro(clube));
            cal.setString(5, par.getSetParametro(uf));
            cal.setString(6, par.getSetParametro(numeracao));
            cal.setString(7, par.getSetParametro(nome));
            
            cal.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
                
            }

            log.severe(e.getMessage());
            throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }

    public void alterar(Auditoria audit) throws AlterarException{

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            ParametroAuditoria par = new ParametroAuditoria();
            String sql = "UPDATE TB_CARTAO_NAUTICA SET DE_CATEGORIA = ?, DE_CLUBE = ?, SG_UF = ?, DE_NUMERACAO = ?, NO_USUARIO = ? WHERE NR_CARTAO = ?";
            cal = cn.prepareCall(sql);
            
            cal.setString(1, par.getSetParametro(categoria));
            cal.setString(2, par.getSetParametro(clube));
            cal.setString(3, par.getSetParametro(uf));
            cal.setString(4, par.getSetParametro(numeracao));
            cal.setString(5, par.getSetParametro(nome));
            cal.setInt(6, par.getSetParametro(nrCartao));
            
            cal.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }


}