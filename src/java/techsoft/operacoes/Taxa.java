package techsoft.operacoes;

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
import techsoft.tabelas.InserirException;

public class Taxa {
    private int id;
    private String descricao;
    private String tipo;
    private String receita;
    private String selecionaCarro;

    private static final Logger log = Logger.getLogger("techsoft.operacoes.Taxa");

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
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getReceita() {
        return receita;
    }
    public void setReceita(String receita) {
        this.receita = receita;
    }
    public String getSelecionaCarro() {
        return selecionaCarro;
    }
    public void setSelecionaCarro(String selecionaCarro) {
        this.selecionaCarro = selecionaCarro;
    }

    public static List<Taxa> listar() {
        String sql = "SELECT * FROM TB_TAXA_ADMINISTRATIVA ORDER BY DESCR_TX_ADMINISTRATIVA ";

        Connection cn = null;
        ArrayList<Taxa> l = new ArrayList<Taxa>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Taxa i = new Taxa();
                i.setId(rs.getInt("CD_TX_ADMINISTRATIVA"));
                i.setDescricao(rs.getString("DESCR_TX_ADMINISTRATIVA"));
                i.setTipo(rs.getString("IND_TAXA_ADMINISTRATIVA"));
                i.setReceita(rs.getString("IC_TIPO_RECEITA"));
                i.setSelecionaCarro(rs.getString("IC_SELECIONA_CARRO"));
                
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
    
    
    public void inserir(Auditoria audit)throws InserirException{
 
        ResultSet rs = null;
        Connection cn = null;
        
        if(id != 0 || descricao == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "INSERT INTO TB_TAXA_ADMINISTRATIVA (DESCR_TX_ADMINISTRATIVA, IND_TAXA_ADMINISTRATIVA, IC_TIPO_RECEITA) VALUES(?, ?, ?)";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, descricao);
            p.setString(2, tipo);
            p.setString(3, receita);

            p.executeUpdate();
            rs = p.getGeneratedKeys();

            if(rs.next()){
                id = rs.getInt(1);
                cn.commit();
                audit.registrarMudanca(sql, descricao, tipo);
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
            String sql = "UPDATE TB_TAXA_ADMINISTRATIVA SET DESCR_TX_ADMINISTRATIVA = ?, IND_TAXA_ADMINISTRATIVA = ?, IC_TIPO_RECEITA = ? WHERE CD_TX_ADMINISTRATIVA = ?";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, descricao);
            p.setString(2, tipo);
            p.setString(3, receita);
            p.setInt(4, id);

            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, descricao, tipo, String.valueOf(id));

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
    
    public static Taxa getInstance(int id) {
        String sql = "SELECT * FROM TB_TAXA_ADMINISTRATIVA WHERE CD_TX_ADMINISTRATIVA = ?";
        Connection cn = null;
        Taxa i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new Taxa();
                i.id = rs.getInt("CD_TX_ADMINISTRATIVA");
                i.setDescricao(rs.getString("DESCR_TX_ADMINISTRATIVA"));
                i.setTipo(rs.getString("IND_TAXA_ADMINISTRATIVA"));
                i.setReceita(rs.getString("IC_TIPO_RECEITA"));
                i.setSelecionaCarro(rs.getString("IC_SELECIONA_CARRO"));
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

    
    public void excluir(Auditoria audit) {
        String sql = "DELETE FROM TB_TAXA_ADMINISTRATIVA WHERE CD_TX_ADMINISTRATIVA = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, this.id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, "" + this.id);
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }
    
    public static void atualizarTaxasIndividuais(int idTaxa, String usuarios, Auditoria audit){
        String sql = "DELETE TB_USUARIO_TAXA_INDIVIDUAL WHERE CD_TX_ADMINISTRATIVA = ? ";
       
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, idTaxa);
            p.executeUpdate();
            
            String[] vetUsuarios = usuarios.split(";");

            sql = "INSERT INTO TB_USUARIO_TAXA_INDIVIDUAL VALUES(?, ?)";
            p = cn.prepareStatement(sql);
            for(int i =0; i < vetUsuarios.length ; i++){
                p.setString(1, vetUsuarios[i]);
                p.setInt(2, idTaxa);
                p.executeUpdate();
                audit.registrarMudanca(sql, vetUsuarios[i], String.valueOf(idTaxa));
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
}
