
package techsoft.clube;

import java.sql.CallableStatement;
import techsoft.tabelas.InserirException;
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
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;

public class LiberacaoBoletoInternet {

    private int id;
    private Date dtVenc;
    private Date dtPgto;
    
    private static final Logger log = Logger.getLogger("techsoft.clube.ItemChurrasqueira");

    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
   public Date getDtVenc() {
        return dtVenc;
    }
    public void setDtVenc(Date dtVenc) {
        this.dtVenc = dtVenc;
    }
    public Date getDtPgto() {
        return dtPgto;
    }
    public void setDtPgto(Date dtPgto) {
        this.dtPgto = dtPgto;
    }
    

    public static List<LiberacaoBoletoInternet> listar(){
        ArrayList<LiberacaoBoletoInternet> l = new ArrayList<LiberacaoBoletoInternet>();
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        
        try {
            cal = cn.prepareCall("{call SP_LIBERACAO_BOLETO_INTERNET ('T', NULL, NULL, NULL)}");
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                LiberacaoBoletoInternet d = new LiberacaoBoletoInternet();
                d.id = rs.getInt("NU_SEQ_LIBERACAO");
                d.dtVenc = rs.getDate("DT_VENC_CARNE");
                if(rs.getDate("DT_PGTO_CARNE") != null){ 
                    d.dtPgto = rs.getDate("DT_PGTO_CARNE");
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

    public static LiberacaoBoletoInternet getInstance(int id){
        LiberacaoBoletoInternet d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_LIBERACAO_BOLETO_INTERNET ('C', ?, NULL, NULL)}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new LiberacaoBoletoInternet();
                d.id = rs.getInt("NU_SEQ_LIBERACAO");
                d.dtVenc = rs.getDate("DT_VENC_CARNE");
                if(rs.getDate("DT_PGTO_CARNE") != null){ 
                    d.dtPgto = rs.getDate("DT_PGTO_CARNE");
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

        Connection cn = Pool.getInstance().getConnection();

        try {
            CallableStatement cal = cn.prepareCall("{call SP_LIBERACAO_BOLETO_INTERNET ('E', ?, NULL, NULL)}");
            cal.setInt(1, getId());
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_LIBERACAO_BOLETO_INTERNET ('E', ?, NULL, NULL)}", String.valueOf(getId()));
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
            cal = cn.prepareCall("{call SP_LIBERACAO_BOLETO_INTERNET ('I', NULL, ?, ?)}");
            
            cal.setDate(1, new java.sql.Date(dtVenc.getTime()));
            
            if(dtPgto==null){
                cal.setNull(2, java.sql.Types.DATE);
            }else{
                cal.setDate(2, new java.sql.Date(dtPgto.getTime()));
            }
            
            ResultSet rs = cal.executeQuery();

            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_LIBERACAO_BOLETO_INTERNET ('I', NULL, ?, ?)}", String.valueOf(dtVenc), String.valueOf(dtPgto));
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
            cal = cn.prepareCall("{call SP_LIBERACAO_BOLETO_INTERNET ('A', ?, NULL, ?)}");
            
            cal.setInt(1, getId());
            if(dtPgto==null){
                cal.setNull(2, java.sql.Types.DATE);
            }else{
                cal.setDate(2, new java.sql.Date(dtPgto.getTime()));
            }
            
            ResultSet rs = cal.executeQuery();
            
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_LIBERACAO_BOLETO_INTERNET ('A', ?, NULL, ?)}", String.valueOf(getId()), String.valueOf(dtPgto));
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