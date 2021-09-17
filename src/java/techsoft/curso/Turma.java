package techsoft.curso;

import com.sun.imageio.plugins.common.BogusColorSpace;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.cadastro.Socio;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class Turma {
    private int id;
    private int idCurso;
    private int idProfessor;
    private Date dataInicio;
    private Date dataFim;
    private int qtVagas;//pode ser informada na atualizacao
    private String deTurma;
    
    /*
     * a quantidade de vagas atual nao eh alterada na atualizacao.
     * Ela é usada para gerar o novo saldo de vagas, depois que o 
     * usuario faz atualizacao da quantidade de vagas.
     */
    private int qtVagasAtual;
    private int sdVagas;
    private List<TurmaHorario>[] horarios;
    
    private static final Logger log = Logger.getLogger("techsoft.curso.Turma");
    
    public Turma(){
        inicializarHorarios();
    }

    public int getId() {
        return id;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
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

    public int getQtVagas() {
        return qtVagas;
    }

    public void setQtVagas(int qtVagas) {
        this.qtVagas = qtVagas;
    }

    public int getSdVagas() {
        return sdVagas;
    }
    
    public List<TurmaHorario>[] getHorarios() {
        return horarios;
    }

    public String getDeTurma() {
        return deTurma;
    }

    public void setDeTurma(String deTurma) {
        this.deTurma = deTurma;
    }
    
    public static Turma getInstance(int id){
        Turma t = new Turma();
        String sql = "SELECT * FROM TB_TURMA T LEFT JOIN TB_HORARIO_TURMA H "
                + "ON H.SEQ_TURMA = T.SEQ_TURMA WHERE T.SEQ_TURMA = ?";
        
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                t.id = rs.getInt("SEQ_TURMA");
                t.idCurso = rs.getInt("CD_CURSO");
                t.idProfessor = rs.getInt("CD_FUNCIONARIO");
                t.qtVagas = rs.getInt("QT_VAGAS_TURMA");
                t.qtVagasAtual = t.qtVagas;
                t.sdVagas = rs.getInt("SD_VAGAS_TURMA");
                t.dataInicio = rs.getDate("DT_INIC_TURMA");
                t.dataFim = rs.getDate("DT_FIM_TURMA");
                t.deTurma = rs.getString("DE_TURMA");
                if(rs.getString("HH_INICIO") != null){
                    TurmaHorario h = new TurmaHorario(rs.getString("HH_INICIO"), rs.getString("HH_FIM"));
                    t.horarios[rs.getInt("CD_DIA")-1].add(h);
                }
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }

        return t.id == 0 ? null: t;
    }

    public void alterar(Auditoria audit){

        Connection cn = null;

        try {
            String sql = "UPDATE TB_TURMA SET CD_FUNCIONARIO = ?, "
               + "CD_CURSO = ?, DT_INIC_TURMA = ?, DT_FIM_TURMA = ?, "
               + "qt_vagas_turma = ?, SD_VAGAS_TURMA = ?, DE_TURMA = ? WHERE SEQ_TURMA = ?";
             
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            
            p.setInt(1, par.getSetParametro(idProfessor));
            p.setInt(2, par.getSetParametro(idCurso));
            p.setDate(3, new java.sql.Date(par.getSetParametro(dataInicio).getTime()));            
            p.setDate(4, new java.sql.Date(par.getSetParametro(dataFim).getTime()));            
            p.setInt(5, par.getSetParametro(qtVagas));
            //saldo = nova quantidade de vagas - alunos matriculados
            p.setInt(6, par.getSetParametro(qtVagas - (qtVagasAtual - sdVagas)));
            p.setString(7, par.getSetParametro(deTurma));
            p.setInt(8, par.getSetParametro(id));
            p.executeUpdate();
            audit.registrarMudanca(sql, par.getParametroFinal());

            sql = "DELETE FROM TB_HORARIO_TURMA WHERE SEQ_TURMA = ?";
            p = cn.prepareStatement(sql);
            p.setInt(1, id);
            p.executeUpdate();
            
            sql = "INSERT INTO TB_HORARIO_TURMA (SEQ_TURMA, CD_DIA, HH_INICIO, HH_FIM) VALUES(?, ?, ?, ?)";
            p = cn.prepareStatement(sql);
            for(int k = 0; k < 7; k++){
                for(TurmaHorario t : horarios[k]){
                    par.limpaParametro();
                    p.setInt(1, par.getSetParametro(id));
                    p.setInt(2, par.getSetParametro(k + 1));
                    p.setString(3, par.getSetParametro(t.getHoraInicio()));
                    p.setString(4, t.getHoraFim());
                    p.addBatch();
                    audit.registrarMudanca(sql, par.getParametroFinal());
                }
            }
            p.executeBatch();
            
            cn.commit();
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

        try {
            String sql = "INSERT INTO TB_TURMA (CD_FUNCIONARIO, CD_CURSO, "
                    + "DT_INIC_TURMA, DT_FIM_TURMA, qt_vagas_turma, "
                    + "SD_VAGAS_TURMA, DE_TURMA) VALUES (?, ?, ?, ?, ?, ?, ?)";
             
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ParametroAuditoria par = new ParametroAuditoria();
            
            p.setInt(1, par.getSetParametro(idProfessor));
            p.setInt(2, par.getSetParametro(idCurso));
            p.setDate(3, new java.sql.Date(par.getSetParametro(dataInicio).getTime()));            
            p.setDate(4, new java.sql.Date(par.getSetParametro(dataFim).getTime()));            
            p.setInt(5, par.getSetParametro(qtVagas));
            p.setInt(6, par.getSetParametro(qtVagas));//saldo de vagas = quantidade de vagas
            p.setString(7, par.getSetParametro(deTurma));
            p.executeUpdate();
            audit.registrarMudanca(sql, par.getParametroFinal());
            
            ResultSet rs = p.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
            
            sql = "INSERT INTO TB_HORARIO_TURMA (SEQ_TURMA, CD_DIA, HH_INICIO, HH_FIM) VALUES(?, ?, ?, ?)";
            p = cn.prepareStatement(sql);
            for(int k = 0; k < 7; k++){
                for(TurmaHorario t : horarios[k]){
                    par.limpaParametro();
                    p.setInt(1, par.getSetParametro(id));
                    p.setInt(2, par.getSetParametro(k + 1));
                    p.setString(3, par.getSetParametro(t.getHoraInicio()));
                    p.setString(4, par.getSetParametro(t.getHoraFim()));
                    p.addBatch();
                    audit.registrarMudanca(sql, par.getParametroFinal());
                }
            }
            p.executeBatch();
            
            cn.commit();
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
    
    public void excluir(Auditoria audit){

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement c = cn.prepareCall("{call SP_EXCLUIR_TURMA(?)}");
            
            c.setInt(1, id);   
            c.executeUpdate();
            cn.commit();
            audit.registrarMudanca("{call SP_EXCLUIR_TURMA(?)}", String.valueOf(id));
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

    public boolean isSocioMatriculado(Socio s){
        boolean ret = false;
        String sql = "Select 1 from tb_matricula_curso where cd_matricula = ? "
                + " and cd_categoria = ? and seq_dependente = ? and seq_turma = ?";
           
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, s.getMatricula());
            p.setInt(2, s.getIdCategoria());
            p.setInt(3, s.getSeqDependente());
            p.setInt(4, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                ret = true;
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }

        return ret;
    }
    
    public void matricular(Socio s, float descontoFamiliar, float descontoPessoal, Auditoria audit){          
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement c = cn.prepareCall("{call sp_inclui_matricula(?, ?, ?, ?, ?, ?, ?, ?, 'NO', ?)}");
            ParametroAuditoria par = new ParametroAuditoria();
            c.setInt(1, par.getSetParametro(id));
            c.setInt(2, par.getSetParametro(s.getMatricula()));
            c.setInt(3, par.getSetParametro(s.getSeqDependente()));
            c.setInt(4, par.getSetParametro(s.getIdCategoria()));
            c.setFloat(5, par.getSetParametro(descontoPessoal));
            c.setFloat(6, par.getSetParametro(descontoFamiliar));
            c.setDate(7, new java.sql.Date(par.getSetParametro(new Date()).getTime()));
            c.setDate(8, new java.sql.Date(par.getSetParametro(new Date()).getTime()));
            c.setString(9, par.getSetParametro(audit.getLogin()));
            c.executeUpdate();

            cn.commit();
            audit.registrarMudanca("{call sp_inclui_matricula(?, ?, ?, ?, ?, ?, ?, ?, 'NO', ?)}", par.getParametroFinal());
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    public void cancelarMatricula(Socio s, Auditoria audit)throws CancelarMatriculaException{          
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement c = cn.prepareCall("{call SP_VRF_PRORATA_CANC_CURSO(?, ?, ?, ?)}");
            c.setInt(1, s.getMatricula());
            c.setInt(2, s.getIdCategoria());            
            c.setInt(3, s.getSeqDependente());
            c.setInt(4, id);
            ResultSet rs = c.executeQuery();

            if(rs.next()){
                String msg = rs.getString("msg");
                if(msg != null && !msg.equalsIgnoreCase("OK")){
                    if(msg.equalsIgnoreCase("VR")){
                        if(rs.getInt("VR_TX_PRORATA") > 0 ){
                            
                            
                            c = cn.prepareCall("{call SP_EMITE_TX_IND_CURSO(?, ?, ?, ?, ?, ?)}");
                            c.setInt(1, s.getMatricula());
                            c.setInt(2, s.getIdCategoria());            
                            c.setInt(3, s.getSeqDependente());
                            c.setString(4, audit.getLogin());            
                            
                            BigDecimal bd = new BigDecimal(Float.toString(rs.getFloat("VR_TX_PRORATA")));
                            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                            c.setFloat(5, bd.floatValue());            
                            
                            c.setInt(6, id);            
                            c.executeUpdate();
                            audit.registrarMudanca("SP_EMITE_TX_IND_CURSO", 
                                    String.valueOf(s.getMatricula()),
                                    String.valueOf(s.getIdCategoria()),
                                    String.valueOf(s.getSeqDependente()),
                                    String.valueOf(audit.getLogin()),
                                    String.valueOf(bd.floatValue()),
                                    String.valueOf(id)
                            );
                            
                        }
                    }
                }
            }

            
            c = cn.prepareCall("{call SP_ATUALIZA_MATRICULA(?, ?, ?, ?, 'NO', 'CA', ?)}");
            c.setInt(1, id);            
            c.setInt(2, s.getMatricula());
            c.setInt(3, s.getSeqDependente());
            c.setInt(4, s.getIdCategoria());            
            c.setString(5, audit.getLogin());            
            c.executeUpdate();
            
            cn.commit();
            
            audit.registrarMudanca("{call SP_ATUALIZA_MATRICULA(?, ?, ?, ?, 'NO', 'CA', ?)}", String.valueOf(id),
                    String.valueOf(s.getMatricula()),
                    String.valueOf(s.getSeqDependente()),
                    String.valueOf(s.getIdCategoria()),
                    String.valueOf(audit.getLogin()));
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    public void reativarMatricula(Socio s, Auditoria audit)throws ReativarMatriculaException{          
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_TURMA WHERE SEQ_TURMA = ?");
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            rs.next();
            if(rs.getInt("SD_VAGAS_TURMA") <= 0){
                throw new ReativarMatriculaException("Não há vaga diponível na turma para que a matrícula seja reativada.");
            }

            CallableStatement c = cn.prepareCall("{call SP_ATUALIZA_MATRICULA(?, ?, ?, ?, 'CA', 'NO', ?)}");
            c.setInt(1, id);            
            c.setInt(2, s.getMatricula());
            c.setInt(3, s.getSeqDependente());
            c.setInt(4, s.getIdCategoria());            
            c.setString(5, audit.getLogin());            
            c.executeUpdate();
            
            cn.commit();
            audit.registrarMudanca("SP_ATUALIZA_MATRICULA", String.valueOf(id),
                    String.valueOf(s.getMatricula()),
                    String.valueOf(s.getSeqDependente()),
                    String.valueOf(s.getIdCategoria()),
                    String.valueOf(audit.getLogin()));
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    public void alterarDesconto(Socio s, float descontoPessoal, Auditoria audit){          
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "UPDATE TB_MATRICULA_CURSO "
                + "SET PERC_DESCONTO_PESSOAL = ? "
                + "WHERE CD_MATRICULA = ? AND SEQ_DEPENDENTE = ? "
                + "AND CD_CATEGORIA= ? AND SEQ_TURMA = ?";

            PreparedStatement p = cn.prepareStatement(sql);
            p.setFloat(1, descontoPessoal);
            p.setInt(2, s.getMatricula());
            p.setInt(3, s.getSeqDependente());
            p.setInt(4, s.getIdCategoria());
            p.setInt(5, id);
            p.executeUpdate();
            
            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(descontoPessoal),
                    String.valueOf(s.getMatricula()),
                    String.valueOf(s.getSeqDependente()),
                    String.valueOf(s.getIdCategoria()),
                    String.valueOf(id));
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    private void inicializarHorarios(){
        horarios = new ArrayList[7];
        for(int k = 0; k < 7; k++){
            horarios[k] = new ArrayList<TurmaHorario>();
        }
    }
    
    
}

