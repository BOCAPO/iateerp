
package techsoft.operacoes;

import java.math.BigDecimal;
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
import techsoft.tabelas.InserirException;

public class CP {

    private Date data;
    private BigDecimal valor;
    private static final Logger log = Logger.getLogger(CP.class.getCanonicalName());


    public Date getData() {
        return this.data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    
    public BigDecimal getValor() {
        return this.valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public static List<CP> listar(String mesAno) {
        String sql = "SELECT * FROM TB_CMP WHERE MONTH(DT_VALIDADE_CMP) = "+mesAno.substring(0,2) +" AND YEAR(DT_VALIDADE_CMP) = "+mesAno.substring(3)+" ORDER BY DT_VALIDADE_CMP";
        Connection cn = null;
        ArrayList<CP> l = new ArrayList<CP>();

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                CP i = new CP();
                i.data = rs.getDate("DT_VALIDADE_CMP");
                i.valor = rs.getBigDecimal("VAL_IND_CMP");
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

    public static CP getInstance(Date data) {
        String sql = "SELECT * FROM TB_CMP WHERE DT_VALIDADE_CMP = ? ";
        Connection cn = null;
        CP i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setDate(1, new java.sql.Date(data.getTime()));
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new CP();
                i.data = rs.getDate("DT_VALIDADE_CMP");
                i.valor = rs.getBigDecimal("VAL_IND_CMP");
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
        String sql = "DELETE FROM TB_CMP WHERE DT_VALIDADE_CMP = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setDate(1, new java.sql.Date(data.getTime()));
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, "" + this.data);
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


    public void inserir(Auditoria audit) throws InserirException {
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_CMP WHERE DT_VALIDADE_CMP = ?");
            p.setDate(1, new java.sql.Date(data.getTime()));

            ResultSet rs = p.executeQuery();

            if(rs.next()) {
                String err = "Já existe CP cadastrada para esta data!";
                log.warning(err);
                throw new InserirException(err);
            } else {
                String sql = "INSERT INTO TB_CMP (DT_VALIDADE_CMP, VAL_IND_CMP) VALUES (?, ?)";
                p = cn.prepareStatement(sql);
                p.setDate(1, new java.sql.Date(data.getTime()));
                p.setBigDecimal(2, this.valor);
                p.executeUpdate();
                cn.commit();
                audit.registrarMudanca(sql, "" + this.data, "" + this.valor);
            }
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
