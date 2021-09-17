package techsoft.operacoes;

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
import java.util.Date;
import techsoft.acesso.LocalAcesso;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;

public class ReservaLugarEvento {
    
    private int mesa;
    private int cadeira;
    private int evento;
    private Date dtReserva;
    private Date dtConfirmacao;
    private String pessoa;
    private String telefone;
    private String usuario;
    private String situacao;
    private int matricula;
    private int categoria;
    private int dependente;
    private String tipoPessoa;
    private float valor;
    private String meia;
            
    private static final Logger log = Logger.getLogger("techsoft.cadastro.ReservaLugarEvento");

    public int getMesa() {
        return mesa;
    }
    public void setMesa(int mesa) {
        this.mesa = mesa;
    }
    public int getCadeira() {
        return cadeira;
    }
    public void setCadeira(int cadeira) {
        this.cadeira = cadeira;
    }
    public int getEvento() {
        return evento;
    }
    public void setEvento(int evento) {
        this.evento = evento;
    }
    public Date getDtReserva() {
        return dtReserva;
    }
    public void setDtReserva(Date dtReserva) {
        this.dtReserva = dtReserva;
    }
    public Date getDtConfirmacao() {
        return dtConfirmacao;
    }
    public void setDtConfirmacao(Date dtConfirmacao) {
        this.dtConfirmacao = dtConfirmacao;
    }
    public String getPessoa() {
        return pessoa;
    }
    public void setPessoa(String pessoa) {
        this.pessoa = pessoa;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getSituacao() {
        return situacao;
    }
    public void setSituacao(String situacao) {
        this.situacao = situacao;
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
    public String getTipoPessoa() {
        return tipoPessoa;
    }
    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public String getMeia() {
        return meia;
    }
    public void setMeia(String meia) {
        this.meia = meia;
    }
   

    public static ReservaLugarEvento getInstance(int evento, int mesa, int cadeira) {
        
        String sql = "SELECT * FROM TB_RESERVA_LUGAR WHERE SEQ_EVENTO = ? AND NR_MESA = ? AND NR_CADEIRA = ? ";
        
        Connection cn = null;
        ReservaLugarEvento i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, evento);
            p.setInt(2, mesa);
            p.setInt(3, cadeira);
            
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new ReservaLugarEvento();
    
                i.setMesa(rs.getInt("NR_MESA"));
                i.setCadeira(rs.getInt("NR_CADEIRA"));
                i.setEvento(rs.getInt("SEQ_EVENTO"));
                i.setDtReserva(rs.getDate("DT_RESERVA"));
                i.setDtConfirmacao(rs.getDate("DT_CONFIRMACAO"));
                i.setPessoa(rs.getString("NO_PESSOA"));
                i.setTelefone(rs.getString("TEL_PESSOA"));
                i.setUsuario(rs.getString("USER_ACESSO_SISTEMA"));
                i.setSituacao(rs.getString("CD_SIT_RESERVA"));
                i.setMatricula(rs.getInt("CD_MATRICULA"));
                i.setCategoria(rs.getInt("CD_CATEGORIA"));
                i.setDependente(rs.getInt("SEQ_DEPENDENTE"));
                i.setTipoPessoa(rs.getString("IC_TIPO_PESSOA"));
                i.setValor(rs.getFloat("VR_RESERVA"));
                i.setMeia(rs.getString("IC_MEIA"));
                
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
    public static String[][] lugaresReservados(int idEvento){
        
        String sql = "SELECT " +
                        "QT_MESAS, QT_CADEIRAS " +
                     "FROM TB_EVENTO " +
                     "WHERE SEQ_EVENTO = ? ";
        
        Connection cn = null;
        int qtMesas = 0;
        int qtCadeiras = 0;
                
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, idEvento);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                qtMesas = rs.getInt("QT_MESAS");
                qtCadeiras = rs.getInt("QT_CADEIRAS");
            }
        }catch(Exception ex){
            log.severe(ex.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        String[][] lugares = new String[qtMesas][qtCadeiras];
        
        
        sql = "SELECT " +
                        "NR_MESA, NR_CADEIRA, CD_SIT_RESERVA " +
                     "FROM TB_RESERVA_LUGAR " +
                     "WHERE SEQ_EVENTO = ? " + 
                     "ORDER BY NR_MESA, NR_CADEIRA ";
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, idEvento);
            ResultSet rs = p.executeQuery();
            int mesa = 0;
            int cadeira = 0;
            String sit = "";
            while (rs.next()) {
                mesa = rs.getInt("NR_MESA")-1;
                cadeira = rs.getInt("NR_CADEIRA")-1;
                sit = rs.getString("CD_SIT_RESERVA");
                lugares[mesa][cadeira]=sit;
            }
            
            
        }catch(Exception ex){
            log.severe(ex.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        
        return lugares;
    }
    
    
    public static void inserir(ArrayList<ReservaLugarEvento> reservas, Auditoria audit)throws InserirException{
 
        ResultSet rs = null;
        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            for(ReservaLugarEvento reserva:reservas){
                par.limpaParametro();
                String sql = "{call SP_INCLUIR_RESERVA_LUGAR (?,?,?,NULL,?,?,?,?,?,?,?,?,?,?)}";
                PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                p.setInt(1, par.getSetParametro(reserva.evento));
                p.setInt(2, par.getSetParametro(reserva.mesa));
                p.setInt(3, par.getSetParametro(reserva.cadeira));

                p.setString(4, par.getSetParametro(reserva.pessoa));
                if("".equals(reserva.telefone)){
                    p.setNull(5, java.sql.Types.VARCHAR);
                    par.getSetNull();
                }else{
                    p.setString(5, par.getSetParametro(reserva.telefone));
                }

                p.setString(6, par.getSetParametro(reserva.usuario));
                p.setString(7, par.getSetParametro(reserva.situacao));

                if("S".equals(reserva.tipoPessoa)){
                    p.setInt(8, par.getSetParametro(reserva.matricula));
                    p.setInt(9, par.getSetParametro(reserva.categoria));
                    p.setInt(10, par.getSetParametro(reserva.dependente));
                }else{
                    p.setNull(8, java.sql.Types.INTEGER);
                    par.getSetNull();
                    p.setNull(9, java.sql.Types.INTEGER);
                    par.getSetNull();
                    p.setNull(10, java.sql.Types.INTEGER);
                    par.getSetNull();
                }

                p.setString(11, par.getSetParametro(reserva.tipoPessoa));
                p.setFloat(12, par.getSetParametro(reserva.valor));
                p.setString(13, par.getSetParametro(reserva.meia));

                rs = p.executeQuery();

                if(rs.next()){
                    if (rs.getInt(1)==1){
                        cn.rollback();
                        log.warning("Não á mais possível incluir a(s) reserva(s) pois algum lugar já foi reservado!");
                        throw new InserirException("Não á mais possível incluir a(s) reserva(s) pois algum lugar já foi reservado!");                        
                    }
                    audit.registrarMudanca(sql, par.getParametroFinal());
                }
            }
            
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

    public void alterar(Auditoria audit)throws InserirException{
 
        ResultSet rs = null;
        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            
            String sql = "UPDATE " +
                        "TB_RESERVA_LUGAR " +
                   "SET " +
                        "NO_PESSOA = ? " +
                        ", TEL_PESSOA = ? ";
            
            if ("RE".equals(situacao)){
                sql = sql +  ", DT_CONFIRMACAO = NULL ";
            }else{
                sql = sql +  ", DT_CONFIRMACAO = GETDATE() ";   
            }
                        
                        
           sql = sql +  ", CD_SIT_RESERVA = ? " +
                        ", USER_ACESSO_SISTEMA = ? " +
                        ", CD_MATRICULA = ? " +
                        ", CD_CATEGORIA = ? " +
                        ", SEQ_DEPENDENTE = ? " +
                        ", IC_TIPO_PESSOA = ? " +
                        ", VR_RESERVA = ? " +
                        ", IC_MEIA = ? " +
                   " WHERE " +
                        "SEQ_EVENTO = ? AND " +
                        "NR_MESA = ? AND " +
                        "NR_CADEIRA = ? ";

            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            p.setString(1, par.getSetParametro(pessoa));
            if("".equals(telefone)){
                p.setNull(2, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(2, par.getSetParametro(telefone));
            }

            p.setString(3, par.getSetParametro(situacao));
            p.setString(4, par.getSetParametro(usuario));

            if("S".equals(tipoPessoa)){
                p.setInt(5, par.getSetParametro(matricula));
                p.setInt(6, par.getSetParametro(categoria));
                p.setInt(7, par.getSetParametro(dependente));
            }else{
                p.setNull(5, java.sql.Types.INTEGER);
                par.getSetNull();
                p.setNull(6, java.sql.Types.INTEGER);
                par.getSetNull();
                p.setNull(7, java.sql.Types.INTEGER);
                par.getSetNull();
            }

            p.setString(8, par.getSetParametro(tipoPessoa));
            p.setFloat(9, par.getSetParametro(valor));
            p.setString(10, par.getSetParametro(meia));
            
            p.setInt(11, par.getSetParametro(evento));
            p.setInt(12, par.getSetParametro(mesa));
            p.setInt(13, par.getSetParametro(cadeira));

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
    
    public void excluir(Auditoria audit) {
        String sql = "DELETE TB_RESERVA_LUGAR WHERE SEQ_EVENTO = ? AND NR_MESA = ? AND NR_CADEIRA = ? ";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, evento);
            p.setInt(2, mesa);
            p.setInt(3, cadeira);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(evento), String.valueOf(mesa), String.valueOf(cadeira));
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
    
    public void confirmar(Auditoria audit) {
        String sql = "UPDATE TB_RESERVA_LUGAR SET CD_SIT_RESERVA = 'CO', DT_CONFIRMACAO = GETDATE() WHERE SEQ_EVENTO = ? AND NR_MESA = ? AND NR_CADEIRA = ? ";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, evento);
            p.setInt(2, mesa);
            p.setInt(3, cadeira);
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
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }  
}
