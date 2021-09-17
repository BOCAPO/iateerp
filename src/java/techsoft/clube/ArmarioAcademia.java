
package techsoft.clube;

import java.sql.CallableStatement;
import techsoft.tabelas.InserirException;
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
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;

public class ArmarioAcademia {

    private int id;
    private String descricao;
    private String situacao;
    private TipoArmarioAcademia tipoArmarioAcademia;
    
    private static final Logger log = Logger.getLogger("techsoft.clube.ArmarioAcademia");

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }
    public String getSituacao() {
        return situacao;
    }
    public void setSituacao(String situacao) {
        this.situacao = situacao.trim();
    }
    public TipoArmarioAcademia getTipoArmarioAcademia() {
        return tipoArmarioAcademia;
    }
    public void setTipoArmarioAcademia(TipoArmarioAcademia tipoArmarioAcademia) {
        this.tipoArmarioAcademia = tipoArmarioAcademia;
    }

    public int getId(){
        return id;
    }

    public static List<ArmarioAcademia> listar(){
        ArrayList<ArmarioAcademia> l = new ArrayList<ArmarioAcademia>();
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        
        try {
            cal = cn.prepareCall("{call SP_ARMARIO_ACADEMIA (NULL, NULL, NULL, 'C')}");
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                ArmarioAcademia d = new ArmarioAcademia();
                d.id = rs.getInt("CD_ARMARIO");
                d.descricao = rs.getString("DESCR_ARMARIO");
                d.situacao = rs.getString("IC_SITUACAO");
                d.tipoArmarioAcademia = TipoArmarioAcademia.getInstance(rs.getInt("CD_TIPO_ARMARIO"));
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

    public static ArmarioAcademia getInstance(int id){
        ArmarioAcademia d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ARMARIO_ACADEMIA (?, NULL, NULL, 'N')}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new ArmarioAcademia();
                d.id = rs.getInt("CD_ARMARIO");
                d.descricao = rs.getString("DESCR_ARMARIO");
                d.tipoArmarioAcademia = TipoArmarioAcademia.getInstance(rs.getInt("CD_TIPO_ARMARIO"));
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

    public void excluir(Auditoria audit) throws ExcluirException {
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ARMARIO_ACADEMIA (?, NULL,  NULL, 'E')}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ARMARIO_ACADEMIA (?, NULL, 'N')}", String.valueOf(id));
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new ExcluirException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new ExcluirException(err);
            }

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }
    
    public void bloquear(Auditoria audit) throws ExcluirException {
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ARMARIO_ACADEMIA (?, NULL,  NULL, 'B')}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ARMARIO_ACADEMIA (?, NULL, 'N')}", "BLOQUEAR " + String.valueOf(id));
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new ExcluirException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new ExcluirException(err);
            }

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }

 public void desbloquear(Auditoria audit) throws ExcluirException {
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ARMARIO_ACADEMIA (?, NULL,  NULL, 'D')}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ARMARIO_ACADEMIA (?, NULL, 'N')}", "DESBLOQUEAR " + String.valueOf(id));
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new ExcluirException(err);
                }
            }else{
                String err = "Erro na operação, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new ExcluirException(err);
            }

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new ExcluirException("Erro na operação, entre em contato com o Administrador do Sistema");
            }
        }
    }    
    
    public void inserir(Auditoria audit)throws InserirException{

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ARMARIO_ACADEMIA (NULL, ?, ?, 'I')}");
            cal.setInt(1, tipoArmarioAcademia.getId());
            cal.setString(2, descricao);
            ResultSet rs = cal.executeQuery();


            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ARMARIO_ACADEMIA (NULL, ?, ?, 'I')}", descricao);
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

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ARMARIO_ACADEMIA (?, ?, ?, 'A')}");
            cal.setInt(1, id);
            cal.setInt(2, tipoArmarioAcademia.getId());
            cal.setString(3, descricao);
            ResultSet rs = cal.executeQuery();

            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ARMARIO_ACADEMIA (?, ?, ?, 'A')}", String.valueOf(id), String.valueOf(tipoArmarioAcademia.getId()), descricao);
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

}