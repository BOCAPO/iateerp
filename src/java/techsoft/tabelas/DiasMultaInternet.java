
package techsoft.tabelas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;

public class DiasMultaInternet {
    
    private int id;
    private int ano;
    private int mes;
    private int qtDias;
    private static final Logger log = Logger.getLogger(DiasMultaInternet.class.getCanonicalName());

    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getAno() {
        return this.ano;
    }
    
    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getMes() {
        return this.mes;
    }
    
    public void setMes(int mes) {
        this.mes = mes;
    }
    
    public String getMesTexto() {
        String[] m = {"Janeiro",
                      "Fevereiro",
                      "Março",
                      "Abril",
                      "Maio",
                      "Junho",
                      "Julho",
                      "Agosto",
                      "Setembro",
                      "Outubro",
                      "Novembro",
                      "Dezembro"};
        return m[this.mes - 1];
    }   

    public int getQtDias() {
        return this.qtDias;
    }

    public void setQtDias(int qtDias) {
        this.qtDias = qtDias;
    }
    
    public static List<DiasMultaInternet> listar() {
        String sql = "SELECT * FROM TB_DIAS_MULTA_CARNE ORDER BY ANO_REFERENCIA desc, MES_REFERENCIA desc";
        Connection cn = null;
        ArrayList<DiasMultaInternet> l = new ArrayList<DiasMultaInternet>();

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                DiasMultaInternet i = new DiasMultaInternet();
                i.id = rs.getInt("NU_SEQ_DIAS");
                i.ano = rs.getInt("ANO_REFERENCIA");
                i.mes = rs.getInt("MES_REFERENCIA");
                i.qtDias = rs.getInt("QT_DIAS");
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

    public static DiasMultaInternet getInstance(int id) {
        String sql = "SELECT * FROM TB_DIAS_MULTA_CARNE WHERE NU_SEQ_DIAS = ?";
        Connection cn = null;
        DiasMultaInternet i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new DiasMultaInternet();
                i.id = rs.getInt("NU_SEQ_DIAS");
                i.ano = rs.getInt("ANO_REFERENCIA");
                i.mes = rs.getInt("MES_REFERENCIA");
                i.qtDias = rs.getInt("QT_DIAS");
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
    
    public static void excluir(int id, Auditoria audit) {
        String sql = "DELETE FROM TB_DIAS_MULTA_CARNE WHERE NU_SEQ_DIAS = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, "" + id);
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
        if ((this.mes < 1) || (this.mes > 12)) {
            return;
        }

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_DIAS_MULTA_CARNE WHERE ANO_REFERENCIA = ? AND MES_REFERENCIA = ?");
            p.setInt(1, this.ano);
            p.setInt(2, this.mes);
            ResultSet rs = p.executeQuery();

            if(rs.next()) {
                String err = "Já existe registro cadastrado para este mês/ano (" + this.mes + "/" + this.ano + ")!";
                log.warning(err);
                throw new InserirException(err);
            } else {
                String sql = "INSERT INTO TB_DIAS_MULTA_CARNE (ANO_REFERENCIA, MES_REFERENCIA, QT_DIAS) VALUES (?, ?, ?)";
                p = cn.prepareStatement(sql);
                p.setInt(1, this.ano);
                p.setInt(2, this.mes);
                p.setInt(3, this.qtDias);
                p.executeUpdate();
                cn.commit();
                audit.registrarMudanca(sql, "" + this.ano + "," + this.mes + "," + this.qtDias);
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
