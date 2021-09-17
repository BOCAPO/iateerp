package techsoft.tabelas;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;

public class Curso {

    private int id;
    private String descricao;
    private int cdTxAdministrativa;
    private String deTxAdministrativa;
    private int cdModalidade;
    private String deModalidade;
    private String situacao;
    private String tpDesconto;

    private static final Logger log = Logger.getLogger("techsoft.tabelas.Curso");

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    public int getCdTxAdministrativa() {
        return cdTxAdministrativa;
    }

    public void setCdTxAdministrativa(int cdTxAdministrativa) {
        this.cdTxAdministrativa = cdTxAdministrativa;
    }

    public String getDeTxAdministrativa() {
        return deTxAdministrativa;
    }

    public void setDeTxAdministrativa(String deTxAdministrativa) {
        this.deTxAdministrativa = deTxAdministrativa.trim();
    }

    public int getCdModalidade() {
        return cdModalidade;
    }

    public void setCdModalidade(int cdModalidade) {
        this.cdModalidade = cdModalidade;
    }

    public String getDeModalidade() {
        return deModalidade;
    }

    public void setDeModalidade(String deModalidade) {
        this.deModalidade = deModalidade.trim();
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao.trim();
    }

    public int getId() {
        return id;
    }

    public String getTpDesconto() {
        return tpDesconto;
    }

    public void setTpDesconto(String tpDesconto) {
        this.tpDesconto = tpDesconto;
    }

    public static List<Curso> listar() {
        ArrayList<Curso> l = new ArrayList<Curso>();
        String sql = "SELECT T1.CD_CURSO, T1.DESCR_CURSO, T1.CD_MODALIDADE, T1.CD_TX_ADMININSTRATIVA, T1.IC_SITUACAO, T2.DESCR_TX_ADMINISTRATIVA, T3.DESCR_MODALIDADE, ISNULL(T1.IC_TIPO_DESCONTO, '') IC_TIPO_DESCONTO FROM TB_CURSO T1, TB_TAXA_ADMINISTRATIVA T2, TB_MODALIDADE_CURSO T3 WHERE T1.CD_TX_ADMININSTRATIVA = T2.CD_TX_ADMINISTRATIVA AND T1.CD_MODALIDADE = T3.CD_MODALIDADE ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Curso d = new Curso();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.cdModalidade = rs.getInt(3);
                d.cdTxAdministrativa = rs.getInt(4);
                d.situacao = rs.getString(5);
                d.deTxAdministrativa = rs.getString(6);
                d.deModalidade = rs.getString(7);
                d.tpDesconto = rs.getString("IC_TIPO_DESCONTO");

                l.add(d);
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

    public static Curso getInstance(int id) {
        Curso d = null;
        String sql = "SELECT T1.CD_CURSO, T1.DESCR_CURSO, T1.CD_MODALIDADE, T1.CD_TX_ADMININSTRATIVA, T1.IC_SITUACAO, T2.DESCR_TX_ADMINISTRATIVA, T3.DESCR_MODALIDADE, ISNULL(T1.IC_TIPO_DESCONTO, '') IC_TIPO_DESCONTO FROM TB_CURSO T1, TB_TAXA_ADMINISTRATIVA T2, TB_MODALIDADE_CURSO T3 WHERE T1.CD_TX_ADMININSTRATIVA = T2.CD_TX_ADMINISTRATIVA AND T1.CD_MODALIDADE = T3.CD_MODALIDADE AND T1.CD_CURSO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                d = new Curso();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.cdModalidade = rs.getInt(3);
                d.cdTxAdministrativa = rs.getInt(4);
                d.situacao = rs.getString(5);
                d.deTxAdministrativa = rs.getString(6);
                d.deModalidade = rs.getString(7);
                d.tpDesconto = rs.getString("IC_TIPO_DESCONTO");
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

        return d;
    }

    public void excluir(Auditoria audit) {
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_CURSO WHERE CD_CURSO = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(id));
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

        if (id != 0 || descricao == null) {
            return;
        }

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_CURSO WHERE DESCR_CURSO = ?");
            p.setString(1, descricao);
            ResultSet rs = p.executeQuery();

            if (rs.next()) {
                String err = "Curso já cadastrado " + descricao;
                log.warning(err);
                throw new InserirException(err);
            } else {
                String sql = "INSERT INTO TB_CURSO (DESCR_CURSO, CD_MODALIDADE, CD_TX_ADMININSTRATIVA, IC_SITUACAO, IC_TIPO_DESCONTO) VALUES (?, ?, ?, ?, ?)";
                p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                p.setString(1, descricao);
                p.setInt(2, cdModalidade);
                p.setInt(3, cdTxAdministrativa);
                p.setString(4, situacao);
                p.setString(5, tpDesconto);
                p.executeUpdate();
                rs = p.getGeneratedKeys();

                if (rs.next()) {
                    id = rs.getInt(1);
                    cn.commit();
                    audit.registrarMudanca(sql, descricao, String.valueOf(cdModalidade), String.valueOf(cdTxAdministrativa), situacao);
                }

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

    public void alterar(Auditoria audit) {

        if (id == 0 || descricao == null) {
            return;
        }

        Connection cn = null;

        try {
            String sql = "UPDATE TB_CURSO SET DESCR_CURSO = ?, CD_MODALIDADE = ?, CD_TX_ADMININSTRATIVA = ?, IC_SITUACAO = ?, IC_TIPO_DESCONTO = ? WHERE CD_CURSO = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricao);
            p.setInt(2, cdModalidade);
            p.setInt(3, cdTxAdministrativa);
            p.setString(4, situacao);
            p.setString(5, tpDesconto);
            p.setInt(6, id);
            p.executeUpdate();
            cn.commit();

            if(tpDesconto.equals("")){
                
                sql = "UPDATE TB_MATRICULA_CURSO "
                    + "SET PERC_DESCONTO_FAMILIA = 0 "
                    + "WHERE SEQ_TURMA IN (SELECT SEQ_TURMA FROM TB_TURMA WHERE CD_CURSO = ?)";
                
                cn = Pool.getInstance().getConnection();
                p = cn.prepareStatement(sql);
                p.setInt(1, id);
                p.executeUpdate();
                cn.commit();
            }
            
            sql = "{call SP_DESCONTO_FAMILIAR_TIPO_S (null, null, null) }";

            cn = Pool.getInstance().getConnection();
            p = cn.prepareStatement(sql);
            p.executeUpdate();
            cn.commit();
            
            cn.commit();
            audit.registrarMudanca(sql, descricao, String.valueOf(cdModalidade), String.valueOf(cdTxAdministrativa), situacao, String.valueOf(id));
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
