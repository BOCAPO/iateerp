package techsoft.caixa;

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
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;

public class ProdutoServico {
    private int id;
    private String descricao;
    private float valPadrao;
    private String tipo;
    private int transacao;
    private String deTransacao;
    private String ativo;
    private String credito;
    private int estoqueAtual;
    private int estoqueMinimo;
    
    private static final Logger log = Logger.getLogger("techsoft.caixa.ProdutoServico");

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public float getValPadrao() {
        return valPadrao;
    }
    public void setValPadrao(float valPadrao) {
        this.valPadrao = valPadrao;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getTransacao() {
        return transacao;
    }
    public void setTransacao(int transacao) {
        this.transacao = transacao;
    }
    public String getDeTransacao() {
        return deTransacao;
    }
    public void setDeTransacao(String deTransacao) {
        this.deTransacao = deTransacao;
    }
    public String getAtivo() {
        return ativo;
    }
    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }
    public String getCredito() {
        return credito;
    }
    public void setCredito(String credito) {
        this.credito = credito;
    }
    public int getEstoqueAtual() {
        return estoqueAtual;
    }
    public void setEstoqueAtual(int estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }
    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }
    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public static List<ProdutoServico> listar(int transacao) {
        String sql = "SELECT " +
                        "CD_PRODUTO_SERVICO, " +
                        "DESCR_PRODUTO_SERVICO, " +
                        "VR_PADRAO, " +
                        "CASE WHEN IC_PRODUTO_SERVICO = 'P' THEN 'Produto' ELSE 'Serviço' END IC_PRODUTO_SERVICO, " +
                        "CASE WHEN IC_ATIVO = 'S' THEN 'Ativo' ELSE 'Inativo' END IC_ATIVO, " +
                        "CASE WHEN IC_CREDITO_IATE = 'S' THEN 'Sim' ELSE 'Não' END IC_CREDITO_IATE, " +
                        "CD_TRANSACAO, " +
                        "(SELECT DESCR_TRANSACAO FROM TB_Transacao T0 WHERE T0.CD_TRANSACAO = T1.CD_TRANSACAO) DE_TRANSACAO, " +
                        "QT_ESTOQUE_ATUAL, " +
                        "QT_ESTOQUE_MINIMO " +
                     "FROM TB_PRODUTO_SERVICO T1 " +
                     "WHERE CD_TRANSACAO = ? " +
                     "ORDER BY 2 ";
        
        Connection cn = null;
        ArrayList<ProdutoServico> l = new ArrayList<ProdutoServico>();
        
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, transacao);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                ProdutoServico i = new ProdutoServico();
                i.setId(rs.getInt("CD_PRODUTO_SERVICO"));
                i.setDescricao(rs.getString("DESCR_PRODUTO_SERVICO"));
                i.setTipo(rs.getString("IC_PRODUTO_SERVICO"));
                i.setValPadrao(rs.getFloat("VR_PADRAO"));

                i.setTransacao(rs.getInt("CD_TRANSACAO"));
                i.setDeTransacao(rs.getString("DE_TRANSACAO"));
                i.setAtivo(rs.getString("IC_ATIVO"));
                i.setCredito(rs.getString("IC_CREDITO_IATE"));
                i.setEstoqueAtual(rs.getInt("QT_ESTOQUE_ATUAL"));
                i.setEstoqueMinimo(rs.getInt("QT_ESTOQUE_MINIMO"));
                
                l.add(i);
            }
            cn.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        return l;
    }

    public static ProdutoServico getInstance(int id) {
        String sql = "SELECT " +
                        "CD_PRODUTO_SERVICO, " +
                        "DESCR_PRODUTO_SERVICO, " +
                        "VR_PADRAO, " +
                        "IC_PRODUTO_SERVICO, " +
                        "IC_ATIVO, " +
                        "IC_CREDITO_IATE, " +
                        "CD_TRANSACAO, " +
                        "(SELECT DESCR_TRANSACAO FROM TB_Transacao T0 WHERE T0.CD_TRANSACAO = T1.CD_TRANSACAO) DE_TRANSACAO, " +
                        "QT_ESTOQUE_ATUAL, " +
                        "QT_ESTOQUE_MINIMO " +
                     "FROM TB_PRODUTO_SERVICO T1 "+
                     "WHERE CD_PRODUTO_SERVICO = ? ";
        Connection cn = null;
        ProdutoServico i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new ProdutoServico();
                i.setId(rs.getInt("CD_PRODUTO_SERVICO"));
                i.setDescricao(rs.getString("DESCR_PRODUTO_SERVICO"));
                i.setTipo(rs.getString("IC_PRODUTO_SERVICO"));
                i.setValPadrao(rs.getFloat("VR_PADRAO"));
                i.setTransacao(rs.getInt("CD_TRANSACAO"));
                i.setDeTransacao(rs.getString("DE_TRANSACAO"));
                i.setAtivo(rs.getString("IC_ATIVO"));
                i.setCredito(rs.getString("IC_CREDITO_IATE"));
                i.setEstoqueAtual(rs.getInt("QT_ESTOQUE_ATUAL"));
                i.setEstoqueMinimo(rs.getInt("QT_ESTOQUE_MINIMO"));
                
                
            }
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return i;
    }    
    
    public void inserir(Auditoria audit)throws InserirException{
 
        ResultSet rs = null;
        Connection cn = null;
        
        if(id != 0 || descricao == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "INSERT INTO TB_PRODUTO_SERVICO VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setString(1, par.getSetParametro(descricao));
            p.setFloat(2, par.getSetParametro(valPadrao));
            p.setString(3, par.getSetParametro(tipo));
            p.setString(4, par.getSetParametro(ativo));
            p.setString(5, par.getSetParametro(credito));
            p.setInt(6, par.getSetParametro(transacao));
            if (estoqueAtual==0){
                p.setNull(7, java.sql.Types.INTEGER);
                par.getSetNull();
                p.setNull(8, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(7, par.getSetParametro(estoqueAtual));
                p.setInt(8, par.getSetParametro(estoqueMinimo));
            }
            
            p.executeUpdate();
            rs = p.getGeneratedKeys();

            if(rs.next()){
                id = rs.getInt(1);
                cn.commit();
                audit.registrarMudanca(sql, par.getParametroFinal());
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

    public void alterar(Auditoria audit)throws InserirException{
 
        ResultSet rs = null;
        Connection cn = null;
        
        if(descricao == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = " UPDATE TB_PRODUTO_SERVICO SET DESCR_PRODUTO_SERVICO = ?, VR_PADRAO = ?, IC_PRODUTO_SERVICO = ?, " +
                         " IC_ATIVO = ?, IC_CREDITO_IATE = ?, QT_ESTOQUE_ATUAL = ?, QT_ESTOQUE_MINIMO = ? " +
                         " WHERE CD_PRODUTO_SERVICO = ? ";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setString(1, par.getSetParametro(descricao));
            p.setFloat(2, par.getSetParametro(valPadrao));
            p.setString(3, par.getSetParametro(tipo));
            p.setString(4, par.getSetParametro(ativo));
            p.setString(5, par.getSetParametro(credito));
            if (estoqueAtual==0){
                p.setNull(6, java.sql.Types.INTEGER);
                par.getSetNull();
                p.setNull(7, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(6, par.getSetParametro(estoqueAtual));
                p.setInt(7, par.getSetParametro(estoqueMinimo));
            }
            p.setInt(8, id);
            
            p.executeUpdate();
            cn.commit();
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
