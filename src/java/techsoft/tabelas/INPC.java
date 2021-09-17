
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

public class INPC {

    private int ano;
    private int mes;
    private BigDecimal valor;
    private static final Logger log = Logger.getLogger(INPC.class.getCanonicalName());

    
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

    public BigDecimal getValor() {
        return this.valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public static List<INPC> listar() {
        String sql = "SELECT * FROM TB_INPC ORDER BY AA_REFERENCIA DESC, MM_REFERENCIA DESC";
        Connection cn = null;
        ArrayList<INPC> l = new ArrayList<INPC>();

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                INPC i = new INPC();
                i.ano = rs.getInt("AA_REFERENCIA");
                i.mes = rs.getInt("MM_REFERENCIA");
                i.valor = rs.getBigDecimal("VAL_IND_INPC");
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

    public static INPC getInstance(int ano, int mes) {
        String sql = "SELECT * FROM TB_INPC WHERE AA_REFERENCIA = ? AND MM_REFERENCIA = ?";
        Connection cn = null;
        INPC i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, ano);
            p.setInt(2, mes);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new INPC();
                i.ano = rs.getInt("AA_REFERENCIA");
                i.mes = rs.getInt("MM_REFERENCIA");
                i.valor = rs.getBigDecimal("VAL_IND_INPC");
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
        String sql = "DELETE FROM TB_INPC WHERE AA_REFERENCIA = ? AND MM_REFERENCIA = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, this.ano);
            p.setInt(2, this.mes);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, "" + this.ano + "," + this.mes);
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
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_INPC WHERE AA_REFERENCIA = ? AND MM_REFERENCIA = ?");
            p.setInt(1, this.ano);
            p.setInt(2, this.mes);
            ResultSet rs = p.executeQuery();

            if(rs.next()) {
                String err = "﻿Já existe INPC cadastrado para este mês/ano (" + this.mes + "/" + this.ano + ")!";
                log.warning(err);
                throw new InserirException(err);
            } else {
                String sql = "INSERT INTO TB_INPC (AA_REFERENCIA, MM_REFERENCIA, VAL_IND_INPC) VALUES (?, ?, ?)";
                p = cn.prepareStatement(sql);
                p.setInt(1, this.ano);
                p.setInt(2, this.mes);
                p.setBigDecimal(3, this.valor);
                p.executeUpdate();
                cn.commit();
                audit.registrarMudanca(sql, "" + this.ano + "," + this.mes + "," + this.valor);
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
