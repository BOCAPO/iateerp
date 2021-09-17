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
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;

public class Mensagem {

    private int id;
    private int doc;
    private String descricao;
    
    private static final Logger log = Logger.getLogger("techsoft.acesso.Mensagem");
    
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
    public int getDoc() {
        return doc;
    }
    public void setDoc(int doc) {
        this.doc = doc;
    }
    
    public static List<Mensagem> listar(int doc, String tipo) {

        
        String sql = "SELECT " +
                            "CD_TP_EVENTO_ACESSO, " +
                            "DESCR_TP_EVENTO_ACESSO " +
                     "FROM " +
                            "TB_Tipo_Evento_Acesso T1 " +
                     "WHERE ";
                
        if (tipo=="N"){
            sql = sql + " NOT ";
        }
        
        sql = sql + " EXISTS (SELECT 1 FROM TB_CONTROLE_ACESSO T0 WHERE T1.CD_TP_EVENTO_ACESSO = T0.CD_TP_EVENTO_ACESSO AND T0.NR_DOCUMENTO_ACESSO = " + doc + ") " +
                    " ORDER BY 2 ";
                
        Connection cn = null;
        ArrayList<Mensagem> l = new ArrayList<Mensagem>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Mensagem i = new Mensagem();
                i.setId(rs.getInt("CD_TP_EVENTO_ACESSO"));
                i.setDescricao(rs.getString("DESCR_TP_EVENTO_ACESSO"));

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
    
    public void incluir(Auditoria audit) {
        String sql = "INSERT INTO TB_CONTROLE_ACESSO (CD_TP_EVENTO_ACESSO, NR_DOCUMENTO_aCESSO) VALUES(?, ?)";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, this.id);
            p.setInt(2, this.doc);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, "" + this.id, "" + this.doc);
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
    
    public void excluir(Auditoria audit) {
        String sql = "DELETE FROM TB_CONTROLE_ACESSO WHERE CD_TP_EVENTO_ACESSO = ? AND NR_DOCUMENTO_ACESSO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, this.id);
            p.setInt(2, this.doc);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, "" + this.id, "" + this.doc);
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
