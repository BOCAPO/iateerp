package techsoft.operacoes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.operacoes.Visitante;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;

public class ExameMedico {

    private int id;
    private int matricula;
    private int categoria;
    private int dependente;
    private Date dtExame;
    private Date dtValidade;
    private String conclusao;
    private String resultado;
    private String descricao;
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.ExameMedico");

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMatricula() {
        return matricula;
    }
    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
    public int getCategoria() {
        return categoria;
    }
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    public int getDependente() {
        return dependente;
    }
    public void setDependente(int dependente) {
        this.dependente = dependente;
    }
    public Date getDtExame() {
        return dtExame;
    }
    public void setDtExame(Date dtExame) {
        this.dtExame = dtExame;
    }
    public Date getDtValidade() {
        return dtValidade;
    }
    public void setDtValidade(Date dtValidade) {
        this.dtValidade = dtValidade;
    }
    public String getConclusao() {
        return conclusao;
    }
    public void setConclusao(String conclusao) {
        this.conclusao = conclusao;
    }
    public String getResultado() {
        return resultado;
    }
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static List<ExameMedico> listar(int matricula, int categoria, int dependente){
        ArrayList<ExameMedico> l = new ArrayList<ExameMedico>();
        String sql = "SELECT * FROM TB_EXAME_MEDICO WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = ? ";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, categoria);
            p.setInt(3, dependente);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                ExameMedico d = new ExameMedico();
                d.setId(rs.getInt("CD_EXAME_MEDICO"));
                d.setMatricula(rs.getInt("CD_MATRICULA"));
                d.setCategoria(rs.getInt("CD_CATEGORIA"));
                d.setDependente(rs.getInt("SEQ_DEPENDENTE"));
                d.setDtExame(rs.getDate("DT_EXAME"));
                d.setDtValidade(rs.getDate("DT_VALIDADE_EXAME"));
                d.setConclusao(rs.getString("DE_CONCLUSAO"));
                d.setResultado(rs.getString("IC_RESULTADO"));
                d.setDescricao(rs.getString("DE_EXAME"));

                l.add(d);
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
    public static ExameMedico getInstance(int id){
        ExameMedico d = null;
                String sql = "SELECT * FROM TB_EXAME_MEDICO WHERE CD_EXAME_MEDICO = ? ";

        Connection cn = null;
        

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new ExameMedico();
                d.setId(rs.getInt("CD_EXAME_MEDICO"));
                d.setMatricula(rs.getInt("CD_MATRICULA"));
                d.setCategoria(rs.getInt("CD_CATEGORIA"));
                d.setDependente(rs.getInt("SEQ_DEPENDENTE"));
                d.setDtExame(rs.getDate("DT_EXAME"));
                d.setDtValidade(rs.getDate("DT_VALIDADE_EXAME"));
                d.setConclusao(rs.getString("DE_CONCLUSAO"));
                d.setResultado(rs.getString("IC_RESULTADO"));
                d.setDescricao(rs.getString("DE_EXAME"));
                

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

        return d;
    }
    

    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_EXAME_MEDICO WHERE CD_EXAME_MEDICO = ?";
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
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

    public void inserir(Auditoria audit)throws InserirException{


        Connection cn = null;
        ParametroAuditoria par = new ParametroAuditoria();      
        try {

            String sql = "INSERT INTO TB_EXAME_MEDICO (CD_MATRICULA, CD_CATEGORIA, SEQ_DEPENDENTE, DT_EXAME, DE_EXAME, DE_CONCLUSAO, IC_RESULTADO, DT_VALIDADE_EXAME)"+
                         "VALUES (?, ?, ?, GETDATE(), ?, ?, ?, ?)";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, par.getSetParametro(matricula));
            p.setInt(2, par.getSetParametro(categoria));
            p.setInt(3, par.getSetParametro(dependente));
            p.setString(4, par.getSetParametro(descricao));
            p.setString(5, par.getSetParametro(conclusao));
            p.setString(6, par.getSetParametro(resultado));
            p.setDate(7, new java.sql.Date(par.getSetParametro(dtValidade).getTime()));
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

    public void alterar(Auditoria audit)throws InserirException{


        Connection cn = null;
        ParametroAuditoria par = new ParametroAuditoria();      
        try {

            String sql = "UPDATE TB_EXAME_MEDICO SET DE_EXAME = ?, DE_CONCLUSAO = ?, IC_RESULTADO = ?, DT_VALIDADE_EXAME = ? WHERE CD_EXAME_MEDICO = ? ";
            
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, par.getSetParametro(descricao));
            p.setString(2, par.getSetParametro(conclusao));
            p.setString(3, par.getSetParametro(resultado));
            p.setDate(4, new java.sql.Date(par.getSetParametro(dtValidade).getTime()));
            p.setInt(5, id);
            
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
