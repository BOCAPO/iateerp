
package techsoft.clube;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.cadastro.Socio;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;
import techsoft.tabelas.InserirException;

public class ItemEmprestimoAcademia {

    private int id;
    private EmprestimoAcademia emprestimo;
    private ArmarioAcademia armario;
    private int item;
        
    private static final Logger log = Logger.getLogger("techsoft.clube.ItemEmprestimoAcademia");

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public EmprestimoAcademia getEmprestimoAcademia() {
        return emprestimo;
    }
    public void setEmprestimoAcademia(EmprestimoAcademia emprestimo) {
        this.emprestimo = emprestimo;
    }
    public ArmarioAcademia getArmario() {
        return armario;
    }
    public void setArmario(ArmarioAcademia armario) {
        this.armario = armario;
    }
    public int getItem() {
        return item;
    }
    public void setItem(int item) {
        this.item = item;
    }
    
    public static ItemEmprestimoAcademia getInstance(int id){
        ItemEmprestimoAcademia d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ITEM_EMPRESTIMO_ACADEMIA ('N', ?, null, null, null)}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new ItemEmprestimoAcademia();
                d.setId(rs.getInt("NU_SEQ_ITEM_EMPRESTIMO"));
                d.setEmprestimoAcademia(EmprestimoAcademia.getInstance(rs.getInt("NU_SEQ_EMPRESTIMO")));
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

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        ParametroAuditoria par = new ParametroAuditoria();
        

        try {
            cal = cn.prepareCall("{call SP_ITEM_EMPRESTIMO_ACADEMIA ('I', null, ?, ?, ?)}");
            cal.setInt(1, par.getSetParametro(emprestimo.getId()));
            if (armario==null){
                cal.setNull(2, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(2, par.getSetParametro(armario.getId()));
            }
            if (item==0){
                cal.setNull(3, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(3, par.getSetParametro(item));
            }
            
            ResultSet rs = cal.executeQuery();

            if (rs.next()){
                    if (rs.getString("MSG").equals("OK")){
                    id = rs.getInt("ID");
                    cn.commit();
                    audit.registrarMudanca("{call SP_ITEM_EMPRESTIMO_ACADEMIA ('I', null, ?, ?, ?)}", par.getParametroFinal());
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
    

    public void devolver(Auditoria audit, String tipoDevolucao, BigDecimal valorMulta, int cdTxAdm)throws InserirException{

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        ParametroAuditoria par = new ParametroAuditoria();
        
        

        try {
            cal = cn.prepareCall("{call SP_ITEM_EMPRESTIMO_ACADEMIA (?, ?, ?, null, null)}");
            
            cal.setString(1, par.getSetParametro(tipoDevolucao));
            
            if (id==0){
                cal.setNull(2, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(2, par.getSetParametro(id));
            }
            if (emprestimo==null){
                cal.setNull(3, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                cal.setInt(3, par.getSetParametro(emprestimo.getId()));
            }
            
            ResultSet rs = cal.executeQuery();

            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    
                    audit.registrarMudanca("{call SP_ITEM_EMPRESTIMO_ACADEMIA (?, ?, ?, null, null)}", par.getParametroFinal());
                    
                    cn.commit();
                    
                    if(valorMulta.compareTo(BigDecimal.ZERO)>0){
                        par.limpaParametro();
                        CallableStatement cal2 = cn.prepareCall("{call SP_GERA_MULTA_EMPRESTIMO_ACADEMIA (?, ?, ?)}");

                        cal2.setInt(1, par.getSetParametro(emprestimo.getId())); //verificar como fica aqui na multa somente da toalha, talvez nao precisa mexer.. Deixa somente vinculado ao emprestimo mae mesmo...
                        cal2.setBigDecimal(2, par.getSetParametro(valorMulta));
                        cal2.setString(3, par.getSetParametro(audit.getLogin()));
                        ResultSet rs2 = cal2.executeQuery();
                        if (rs2.next()){
                            if (rs2.getString("MSG").equals("OK")){        
                                audit.registrarMudanca("{call SP_GERA_MULTA_EMPRESTIMO_ACADEMIA (?, ?, ?)}", par.getParametroFinal());
                            }else{
                                String err = rs.getString("MSG");
                                log.warning(err);
                                throw new InserirException(err);
                            }
                        }
                    }
                    
                    cn.commit();
                    
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


}