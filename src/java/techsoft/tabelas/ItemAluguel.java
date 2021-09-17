package techsoft.tabelas;

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

public class ItemAluguel {
    private int id;
    private String descricao;
    private BigDecimal valorSocio;
    private BigDecimal valorNaoSocio;
    private int quantidadeEstoque;

    private static final Logger log = Logger.getLogger("techsoft.tabelas.ItemAluguel");

    public int getId() {
        return this.id;
    }
    public String getDescricao() {
        return this.descricao;
    }
    public BigDecimal getValorSocio() {
        return this.valorSocio;
    }
    public BigDecimal getValorNaoSocio() {
        return this.valorNaoSocio;
    }
    public int getQuantidadeEstoque() {
        return this.quantidadeEstoque;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setValorNaoSocio(BigDecimal valorNaoSocio) {
        this.valorNaoSocio = valorNaoSocio;
    }
    public void setValorSocio(BigDecimal valorSocio) {
        this.valorSocio = valorSocio;
    }
    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public static List<ItemAluguel> listar() {
        String sql = "SELECT * FROM TB_ITEM_ALUGUEL ORDER BY DE_ITEM_ALUGUEL";
        Connection cn = null;
        ArrayList<ItemAluguel> l = new ArrayList<ItemAluguel>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                ItemAluguel i = new ItemAluguel();
                i.id = rs.getInt("CD_ITEM_ALUGUEL");
                i.descricao = rs.getString("DE_ITEM_ALUGUEL");
                i.valorSocio = rs.getBigDecimal("VR_ITEM_ALUGUEL_SO");
                i.valorNaoSocio = rs.getBigDecimal("VR_ITEM_ALUGUEL_NS");
                i.quantidadeEstoque = rs.getInt("QT_ESTOQUE");
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
    
    public static List<ItemAluguel> listar(int idReserva) {
        String sql = "SELECT * FROM TB_ITEM_ALUGUEL WHERE CD_ITEM_ALUGUEL NOT IN (SELECT CD_ITEM_ALUGUEL FROM TB_ITEM_ALUGUEL_DEP WHERE SEQ_RESERVA = ?) ORDER BY DE_ITEM_ALUGUEL";
        Connection cn = null;
        ArrayList<ItemAluguel> l = new ArrayList<ItemAluguel>();
        
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, idReserva);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                ItemAluguel i = new ItemAluguel();
                i.id = rs.getInt("CD_ITEM_ALUGUEL");
                i.descricao = rs.getString("DE_ITEM_ALUGUEL");
                i.valorSocio = rs.getBigDecimal("VR_ITEM_ALUGUEL_SO");
                i.valorNaoSocio = rs.getBigDecimal("VR_ITEM_ALUGUEL_NS");
                i.quantidadeEstoque = rs.getInt("QT_ESTOQUE");
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

    public static ItemAluguel getInstance(int id) {
        String sql = "SELECT * FROM TB_ITEM_ALUGUEL WHERE CD_ITEM_ALUGUEL = ?";
        Connection cn = null;
        ItemAluguel i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new ItemAluguel();
                i.id = rs.getInt("CD_ITEM_ALUGUEL");
                i.descricao = rs.getString("DE_ITEM_ALUGUEL");
                i.valorSocio = rs.getBigDecimal("VR_ITEM_ALUGUEL_SO");
                i.valorNaoSocio = rs.getBigDecimal("VR_ITEM_ALUGUEL_NS");
                i.quantidadeEstoque = rs.getInt("QT_ESTOQUE");
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
            String sql = "INSERT INTO TB_ITEM_ALUGUEL (DE_ITEM_ALUGUEL, VR_ITEM_ALUGUEL_SO, VR_ITEM_ALUGUEL_NS, QT_ESTOQUE) VALUES (?, ?, ?, ?)";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, descricao);
            p.setBigDecimal(2, valorSocio);
            p.setBigDecimal(3, valorNaoSocio);
            p.setInt(4, quantidadeEstoque);

            p.executeUpdate();
            rs = p.getGeneratedKeys();

            if(rs.next()){
                id = rs.getInt(1);
                cn.commit();
                audit.registrarMudanca(sql, descricao, String.valueOf(valorSocio), String.valueOf(valorNaoSocio), String.valueOf(quantidadeEstoque));
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
            String sql = "UPDATE TB_ITEM_ALUGUEL SET DE_ITEM_ALUGUEL = ?, VR_ITEM_ALUGUEL_SO = ?, VR_ITEM_ALUGUEL_NS = ?, QT_ESTOQUE = ? WHERE CD_ITEM_ALUGUEL = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricao);
            p.setBigDecimal(2, valorSocio);
            p.setBigDecimal(3, valorNaoSocio);
            p.setInt(4, quantidadeEstoque);
            p.setInt(5, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, descricao, String.valueOf(valorSocio), String.valueOf(valorNaoSocio), String.valueOf(quantidadeEstoque), String.valueOf(id));
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
    public void excluir(Auditoria audit) {
        String sql = "DELETE FROM TB_ITEM_ALUGUEL WHERE CD_ITEM_ALUGUEL = ?";
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
    
}
