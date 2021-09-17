package techsoft.cadastro;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;

/*
 * TODO
 * Apos a criacao das classes de curso e turma essa
 * bean deve ser refatorado e esse codigo deve ir
 * para essas classes.
 * 
 */
public class ConsultaCursoSocio {
    private Socio socio;
    private String curso;
    private String codigoSituacao;
    private String professor;
    private String aulaSegunda;
    private String aulaTerca;
    private String aulaQuarta;
    private String aulaQuinta;
    private String aulaSexta;
    private String aulaSabado;    
    private String aulaDomingo;
    private Date inicio;
    private Date fim;
    private String deTurma;
    private int seqTurma;
    private Date dtMatricula;
    private Date dtOcorrencia;
    private BigDecimal descFamiliar;
    private BigDecimal descPessoal;
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.ConsultaCursoSocio");

    
    public Socio getSocio() {
        return socio;
    }

    public String getCurso() {
        return curso;
    }

    public String getCodigoSituacao() {
        return codigoSituacao;
    }

    public String getProfessor() {
        return professor;
    }

    public String getAulaSegunda() {
        return aulaSegunda;
    }

    public String getAulaTerca() {
        return aulaTerca;
    }

    public String getAulaQuarta() {
        return aulaQuarta;
    }

    public String getAulaQuinta() {
        return aulaQuinta;
    }

    public String getAulaSexta() {
        return aulaSexta;
    }

    public String getAulaSabado() {
        return aulaSabado;
    }

    public String getAulaDomingo() {
        return aulaDomingo;
    }

    public Date getInicio() {
        return inicio;
    }

    public Date getFim() {
        return fim;
    }
    
    public String getDeTurma() {
        return deTurma;
    }
    public int getSeqTurma() {
        return seqTurma;
    }
    public Date getDtMatricula() {
        return dtMatricula;
    }
    public Date getDtOcorrencia() {
        return dtOcorrencia;
    }
    public void setDtOcorrencia(Date dtOcorrencia) {
        this.dtOcorrencia = dtOcorrencia;
    }
    public BigDecimal getDescFamiliar() {
        return descFamiliar;
    }
    public void setDescFamiliar(BigDecimal descFamiliar) {
        this.descFamiliar = descFamiliar;
    }
    public BigDecimal getDescPessoal() {
        return descPessoal;
    }
    public void setDescPessoal(BigDecimal descPessoal) {
        this.descPessoal = descPessoal;
    }
    
    
    public static List<ConsultaCursoSocio> listar(Socio s){
        ArrayList<ConsultaCursoSocio> l = new ArrayList<ConsultaCursoSocio>();
        Connection cn = null;
        Connection cn2 = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_RECUPERA_CURSO_ALUNO (?, ?, ?)}");
            cal.setInt(1, s.getMatricula());
            cal.setInt(2, s.getSeqDependente());
            cal.setInt(3, s.getIdCategoria());
                    
            ResultSet rs = cal.executeQuery();
            
            while (rs.next()) {
                String sql = "SELECT CD_DIA, "
                        + "SUBSTRING(HH_INICIO, 1, 2) + ':' + SUBSTRING(HH_INICIO, 3, 2) "
                        + "+ ' às ' + "
                        + "SUBSTRING(HH_FIM, 1, 2) + ':' + SUBSTRING(HH_FIM, 3, 2) "
                        + "FROM TB_HORARIO_TURMA WHERE SEQ_TURMA = "
                        + rs.getString("SEQ_TURMA")
                        + " ORDER BY 1";
                cn2 = Pool.getInstance().getConnection();
                ResultSet rs2 = cn2.createStatement().executeQuery(sql);
                
                ConsultaCursoSocio c = new ConsultaCursoSocio();
                c.socio = s;
                c.curso = rs.getString("descr_curso");
                c.codigoSituacao = rs.getString("CD_SIT_MATRICULA");
                c.professor = rs.getString("NOME_FUNCIONARIO");
                c.inicio = rs.getDate("DT_INIC_TURMA");
                c.fim = rs.getDate("DT_FIM_TURMA");
                c.deTurma = rs.getString("DE_TURMA");
                c.seqTurma = rs.getInt("SEQ_TURMA");
                c.dtMatricula = rs.getDate("DT_MATRICULA");
                c.dtOcorrencia = rs.getDate("DT_OCORRENCIA_SITUACAO");
                c.descFamiliar = rs.getBigDecimal("PERC_DESCONTO_FAMILIA");
                c.descPessoal = rs.getBigDecimal("PERC_DESCONTO_PESSOAL");
                
                while (rs2.next()) {
                    switch (rs2.getInt(1)){
                        case 1:
                            if (c.aulaSegunda!="" && c.aulaSegunda!=null){
                                c.aulaSegunda = c.aulaSegunda + " - " + rs2.getString(2);
                            }else{
                                c.aulaSegunda = rs2.getString(2);
                            }
                            break;
                        case 2:
                            if (c.aulaTerca!="" && c.aulaTerca!=null){
                                c.aulaTerca = c.aulaTerca + " - "  + rs2.getString(2);
                            }else{
                                c.aulaTerca = rs2.getString(2);
                            }
                            break;
                        case 3:
                            if (c.aulaQuarta!="" && c.aulaQuarta!=null){
                                c.aulaQuarta = c.aulaQuarta + " - " + rs2.getString(2);
                            }else{
                                c.aulaQuarta =rs2.getString(2);
                            }
                            break;
                        case 4:
                            if (c.aulaQuinta!="" && c.aulaQuinta!=null){
                                c.aulaQuinta = c.aulaQuinta + " - " + rs2.getString(2);
                            }else{
                                c.aulaQuinta = rs2.getString(2);
                            }
                            break;
                        case 5:
                            if (c.aulaSexta!="" && c.aulaSexta!=null){
                                c.aulaSexta = c.aulaSexta + " - " + rs2.getString(2);
                            }else{
                                c.aulaSexta = rs2.getString(2);
                            }
                            break;
                        case 6:
                            if (c.aulaSabado!="" && c.aulaSabado!=null){
                                c.aulaSabado = c.aulaSabado + " - " + rs2.getString(2);
                            }else{
                                c.aulaSabado = rs2.getString(2);
                            }
                            break;
                        case 7:
                            if (c.aulaDomingo!="" && c.aulaDomingo!=null){
                                c.aulaDomingo = c.aulaDomingo + " - " + rs2.getString(2);
                            }else{
                                c.aulaDomingo = rs2.getString(2);
                            }
                            break;
                    }
                }
                
                rs2.close();
                cn2.close();
                
                l.add(c);
            }
            rs.close();
            cn.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }

        return l;        
    }
}
