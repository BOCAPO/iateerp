package techsoft.cadastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class TitularHistorico {

    private int seqUtilizacao;
    private String nome;
    private Date dataInicio;
    private Date dataFim;
    private Titular titular;
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.TitularHistorico");

    public int getSeqUtilizacao() {
        return seqUtilizacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Titular getTitular() {
        return titular;
    }

    public void setTitular(Titular titular) {
        this.titular = titular;
    }
    
    public static List<TitularHistorico> listar(Titular t){
        ArrayList<TitularHistorico> l = new ArrayList<TitularHistorico>();
        String sql = "SELECT NO_PESSOA, DT_INIC_UTILIZACAO, DT_FIM_UTILIZACAO, SEQ_UTILIZACAO "
            + "FROM TB_HIST_UTIL_TITULO WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = 0";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, t.getSocio().getMatricula());
            ps.setInt(2, t.getSocio().getIdCategoria());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TitularHistorico h = new TitularHistorico();
                h.nome = rs.getString(1);
                h.dataInicio = rs.getDate(2);
                h.dataFim = rs.getDate(3);
                h.seqUtilizacao = rs.getInt(4);
                h.titular = t;
                l.add(h);
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

    public static TitularHistorico getInstance(int seqUtilizacao){
        TitularHistorico h = null;
        String sql = "SELECT * FROM TB_HIST_UTIL_TITULO WHERE SEQ_UTILIZACAO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, seqUtilizacao);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                h = new TitularHistorico();
                h.titular = Titular.getInstance(rs.getInt("CD_MATRICULA"), rs.getInt("CD_CATEGORIA"));
                h.nome = rs.getString("NO_PESSOA");
                h.dataInicio = rs.getDate("DT_INIC_UTILIZACAO");
                h.dataFim = rs.getDate("DT_FIM_UTILIZACAO");
                h.seqUtilizacao = rs.getInt("SEQ_UTILIZACAO");
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

        return h;
    }

    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_HIST_UTIL_TITULO WHERE SEQ_UTILIZACAO = ?";
            
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, seqUtilizacao);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(seqUtilizacao));
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

    public void inserir(Auditoria audit){
        Connection cn = null;
        PreparedStatement p = null;
        ResultSet rs = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "INSERT INTO TB_HIST_UTIL_TITULO VALUES (?, ?, 0, ?, ?, ?)";
            
            p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setInt(1, par.getSetParametro(titular.getSocio().getMatricula()));
            p.setInt(2, par.getSetParametro(titular.getSocio().getIdCategoria()));
            p.setString(3, par.getSetParametro(nome));
            p.setDate(4, new java.sql.Date(par.getSetParametro(dataInicio).getTime()));
            p.setDate(5, new java.sql.Date(par.getSetParametro(dataFim).getTime()));

            p.executeUpdate();
            rs = p.getGeneratedKeys();

            if(rs.next()){
                seqUtilizacao = rs.getInt(1);
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

    public void alterar(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "UPDATE TB_HIST_UTIL_TITULO SET "
                    + "NO_PESSOA = ?, "
                    + "DT_INIC_UTILIZACAO = ?, "
                    + "DT_FIM_UTILIZACAO = ? "
                    + "WHERE SEQ_UTILIZACAO = ?";

            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setString(1, par.getSetParametro(nome));
            p.setDate(2, new java.sql.Date(par.getSetParametro(dataInicio).getTime()));
            p.setDate(3, new java.sql.Date(par.getSetParametro(dataFim).getTime()));
            p.setInt(4, par.getSetParametro(seqUtilizacao));
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, nome, par.getParametroFinal());
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
