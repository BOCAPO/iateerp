
package techsoft.clube;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.operacoes.Taxa;
import techsoft.operacoes.TaxaAdministrativa;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;
import techsoft.tabelas.InserirException;

public class TipoArmario {

    private int id;
    private String descricao;
    private Taxa taxa;
    private static final Logger log = Logger.getLogger("techsoft.clube.TipoArmario");

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }
    public Taxa getTaxa() {
        return taxa;
    }
   public void setTaxa(Taxa  taxa) {
        this.taxa = taxa;
    }
    public int getId(){
        return id;
    }

    public static List<TipoArmario> listar(){
        ArrayList<TipoArmario> l = new ArrayList<TipoArmario>();
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        
        try {
            cal = cn.prepareCall("{call SP_TIPO_ARMARIO (NULL, NULL, NULL, 'C')}");
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                TipoArmario d = new TipoArmario();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                if (rs.getString("CD_TX_ADMINISTRATIVA") != null){
                    d.setTaxa(Taxa.getInstance(rs.getInt("CD_TX_ADMINISTRATIVA")));
                }
                
               
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

    public static TipoArmario getInstance(int id){
        TipoArmario d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_TIPO_ARMARIO (?, NULL, NULL, 'N')}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new TipoArmario();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                if (rs.getString("CD_TX_ADMINISTRATIVA") != null){
                    d.setTaxa(Taxa.getInstance(rs.getInt("CD_TX_ADMINISTRATIVA")));
                }
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
            cal = cn.prepareCall("{call SP_TIPO_ARMARIO (?, NULL, NULL, 'E')}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_TIPO_ARMARIO (?, NULL, NULL, 'N')}", String.valueOf(id));
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
        ParametroAuditoria par = new ParametroAuditoria();
        try {
            cal = cn.prepareCall("{call SP_TIPO_ARMARIO (NULL, ?, ?, 'I')}");
            cal.setString(1, par.getSetParametro(descricao));
            if (taxa == null){
                cal.setNull(2, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(2, par.getSetParametro(taxa.getId()));
            }
            ResultSet rs = cal.executeQuery();

            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_TIPO_ARMARIO (NULL, ? ,?, 'I')}", par.getParametroFinal());
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
        ParametroAuditoria par = new ParametroAuditoria();
    
        try {
            cal = cn.prepareCall("{call SP_TIPO_ARMARIO (?, ?, ?, 'A')}");
            cal.setInt(1, id);
            cal.setString(2, descricao);
            if (taxa == null){
                cal.setNull(3, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(3, par.getSetParametro(taxa.getId()));
            }
            
            ResultSet rs = cal.executeQuery();

            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_TIPO_ARMARIO (?, ?, ?, 'A')}", par.getParametroFinal());
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