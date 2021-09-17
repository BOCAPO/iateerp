package techsoft.cadastro;

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
import java.util.Date;
import techsoft.acesso.LocalAcesso;
import techsoft.tabelas.InserirException;

public class CartaCobranca {
    private String descricao;
    private String texto;

    private static final Logger log = Logger.getLogger("techsoft.cadastro.CartaCobranca");

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public static List<CartaCobranca> listar(String tipo) {
        String sql = "SELECT " +
                        "DESCR_CARTA_COBRANCA, TEXTO_CARTA_COBRANCA " +
                    "FROM TB_CARTA_COBRANCA_NOVA WHERE TP_CARTA " + tipo;

        Connection cn = null;
        ArrayList<CartaCobranca> l = new ArrayList<CartaCobranca>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                CartaCobranca i = new CartaCobranca();
                i.setDescricao(rs.getString("DESCR_CARTA_COBRANCA"));
                i.setTexto(rs.getString("TEXTO_CARTA_COBRANCA"));
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
    

    public static CartaCobranca getInstance(String descricao) {
        String sql = "SELECT " +
                        "DESCR_CARTA_COBRANCA, TEXTO_CARTA_COBRANCA " +
                    "FROM TB_CARTA_COBRANCA_NOVA WHERE DESCR_CARTA_COBRANCA = ?";
        
        Connection cn = null;
        CartaCobranca i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricao);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new CartaCobranca();
                i.setDescricao(rs.getString("DESCR_CARTA_COBRANCA"));
                i.setTexto("!"+rs.getString("TEXTO_CARTA_COBRANCA"));
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
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "INSERT INTO TB_CARTA_COBRANCA_NOVA (DESCR_CARTA_COBRANCA, TEXTO_CARTA_COBRANCA, TP_CARTA) VALUES(?, ?, 'C')";

            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricao);
            p.setString(2, texto);
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, descricao, texto);

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
        
        if(getDescricao() == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "UPDATE TB_CARTA_COBRANCA_NOVA SET TEXTO_CARTA_COBRANCA = ? WHERE DESCR_CARTA_COBRANCA = ?";

            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, texto);
            p.setString(2, descricao);
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, texto, descricao);
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
        String sql = "DELETE FROM TB_CARTA_COBRANCA_NOVA WHERE DESCR_CARTA_COBRANCA = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricao);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, descricao);
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
