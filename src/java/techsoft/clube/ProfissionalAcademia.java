
package techsoft.clube;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Statement;
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
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;
import techsoft.tabelas.InserirException;
import techsoft.util.Datas;

public class ProfissionalAcademia {

    private int id;
    private int idServico;
    private int idFuncionario;
    private Date dtInicioVigencia;
    private Date dtFimVigencia;
    private Date dtAbertura;
    private String horarios;
    
    private static final Logger log = Logger.getLogger("techsoft.clube.ProfissionalAcademia");
   
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getIdServico() {
        return idServico;
    }
    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }
    public int getIdFuncionario() {
        return idFuncionario;
    }
    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
    public Date getDtInicioVigencia() {
        return dtInicioVigencia;
    }
    public void setDtInicioVigencia(Date dtInicioVigencia) {
        this.dtInicioVigencia = dtInicioVigencia;
    }
    public Date getDtFimVigencia() {
        return dtFimVigencia;
    }
    public void setDtFimVigencia(Date dtFimVigencia) {
        this.dtFimVigencia = dtFimVigencia;
    }
    public Date getDtAbertura() {
        return dtAbertura;
    }
    public void setDtAbertura(Date dtAbertura) {
        this.dtAbertura = dtAbertura;
    }
    public String getHorarios() {
        return horarios;
    }
    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

   public static ProfissionalAcademia getInstance(int id){
   
        ProfissionalAcademia d = null;
        String sql = "{call SP_PROFISSIONAL_ACADEMIA (?, NULL, NULL, NULL, NULL, NULL,'N')}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall(sql);
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new ProfissionalAcademia();
                d.setId(rs.getInt("NU_SEQ_PROFISSIONAL"));
                d.setIdServico(rs.getInt("NU_SEQ_SERVICO"));
                d.setIdFuncionario(rs.getInt("CD_FUNCIONARIO"));
                d.setDtInicioVigencia(rs.getDate("DT_INICIO_VIGENCIA"));
                d.setDtFimVigencia(rs.getDate("DT_FIM_VIGENCIA"));
                d.setDtAbertura(rs.getTimestamp("DT_ABERTURA"));
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

    public void inserir(Auditoria audit)throws InserirException{

        String sql = "{call SP_PROFISSIONAL_ACADEMIA (null, ?, ?, ?, ?, ?, 'I')}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            ParametroAuditoria par = new ParametroAuditoria();
            cal = cn.prepareCall(sql);
            cal.setInt(1, par.getSetParametro(getIdServico()));
            cal.setInt(2, par.getSetParametro(getIdFuncionario()));
            if (getDtInicioVigencia()== null){
                cal.setNull(3, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(3, new java.sql.Date(par.getSetParametro(getDtInicioVigencia()).getTime()));
            }
            if (getDtFimVigencia()== null){
                cal.setNull(4, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(4, new java.sql.Date(par.getSetParametro(getDtFimVigencia()).getTime()));
            }
            if (getDtAbertura()== null){
                cal.setNull(5, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setTimestamp(5, new java.sql.Timestamp(par.getSetParametro(getDtAbertura()).getTime()));
            }
            
            ResultSet rs = cal.executeQuery();
            
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca(sql, par.getParametroFinal());
                    
                    setId(Integer.parseInt(rs.getString("ID")));

                    if (!"".equals(horarios)){
                        insereHorarios(horarios, getId(), audit);
                    }
                    
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new InserirException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new InserirException(err);
            }
            
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");                
            }

            log.severe(e.getMessage());
            throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");                
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");                
            }
        }
    }

    public void alterar(Auditoria audit) throws AlterarException{

        String sql = "{call SP_PROFISSIONAL_ACADEMIA (?, null, null, ?, ?, ?, 'A')}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            ParametroAuditoria par = new ParametroAuditoria();
            cal = cn.prepareCall(sql);
            
            cal.setInt(1, par.getSetParametro(getId()));
            
            if (getDtInicioVigencia()== null){
                cal.setNull(2, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(2, new java.sql.Date(par.getSetParametro(getDtInicioVigencia()).getTime()));
            }
            if (getDtFimVigencia()== null){
                cal.setNull(3, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setDate(3, new java.sql.Date(par.getSetParametro(getDtFimVigencia()).getTime()));
            }
            if (getDtAbertura()== null){
                cal.setNull(4, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                cal.setTimestamp(4, new java.sql.Timestamp(par.getSetParametro(getDtAbertura()).getTime()));
            }
            
            ResultSet rs = cal.executeQuery();
            
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca(sql, par.getParametroFinal());

                    if (!"".equals(horarios)){
                        try {
                            insereHorarios(horarios, getId(), audit);
                        } catch (InserirException e) {
                            log.severe(e.getMessage());
                            throw new AlterarException(e.getMessage());
                        }
                    }
                    
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new AlterarException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new AlterarException(err);
            }

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");                
                
            }

            log.severe(e.getMessage());
            throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");                
            
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");                
                
            }
        }
    }
    
    public static void excluir(Auditoria audit, int id) throws AlterarException{

        String sql = "{call SP_PROFISSIONAL_ACADEMIA (?, null, null, null, null, null, 'E')}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            ParametroAuditoria par = new ParametroAuditoria();
            cal = cn.prepareCall(sql);
            
            cal.setInt(1, par.getSetParametro(id));
            
            ResultSet rs = cal.executeQuery();
            
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca(sql, par.getParametroFinal());
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new AlterarException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new AlterarException(err);
            }

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");                
                
            }

            log.severe(e.getMessage());
            throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");                
            
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new AlterarException("Erro na operação, entre em contato com o Administrador do Sistema");                
                
            }
        }
    }
    
    private static int diaSemana(String dia){
        int diaInt=0;
        if (dia.equals("Segunda-feira")){
            diaInt=1;
        }else if (dia.equals("Terça-feira")){
            diaInt=2;
        }else if (dia.equals("Quarta-feira")){
            diaInt=3;
        }else if (dia.equals("Quinta-feira")){
            diaInt=4;
        }else if (dia.equals("Sexta-feira")){
            diaInt=5;
        }else if (dia.equals("Sábado")){
            diaInt=6;
        }else if (dia.equals("Domingo")){
            diaInt=7;
        }
        return diaInt;
        
    }

    private static void insereHorarios(String horas, int profissional, Auditoria audit) throws InserirException {
        
        String sql = "{call SP_AGENDA_PROF_ACADEMIA (null, ?, ?, ?, ?, ?, 'I')}";
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        
        try {
            cn = Pool.getInstance().getConnection();
            if (!"".equals(horas)){
                String[] vetDias = horas.split(";");
                String[] vetDet;
                int diaInt=0;
                for(int i =0; i < vetDias.length ; i++){
                    vetDet = vetDias[i].split("/");
                    
                    if (Integer.parseInt(vetDet[4])==0){
                        //vetDet[0] DIA
                        //vetDet[1] HH_INICIO
                        //vetDet[2] HH_FIM
                        //vetDet[3] LOCAL
                        //vetDet[4] ID_AGENDA
                        ParametroAuditoria par = new ParametroAuditoria();
                        cal = cn.prepareCall(sql);

                        cal.setInt(1, par.getSetParametro(profissional));
                        cal.setInt(2, par.getSetParametro(diaSemana(vetDet[0])));
                        cal.setString(3, par.getSetParametro(vetDet[1]));
                        cal.setString(4, par.getSetParametro(vetDet[2]));
                        cal.setInt(5, par.getSetParametro(Integer.parseInt(vetDet[3])));

                        ResultSet rs = cal.executeQuery();

                        if (rs.next()){
                            if (rs.getString("MSG").equals("OK")){
                                cn.commit();
                                audit.registrarMudanca(sql, par.getParametroFinal());
                            }else{
                                String err = rs.getString("MSG");
                                log.warning(err);
                                throw new InserirException(err);
                            }
                        }else{
                            String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                            log.warning(err);
                            throw new InserirException(err);
                        }
                    }
                }
            }
         } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new InserirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
       
    }
    
    public static void duplicar(int idProfissional, Auditoria audit){
        Connection cn = null;

        try {
            String sql = "{call SP_DUPLICA_AGENDA_PROF_ACADEMIA (" + idProfissional + ")}";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql);
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