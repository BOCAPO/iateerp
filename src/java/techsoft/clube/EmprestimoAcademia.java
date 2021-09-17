
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
import techsoft.cadastro.Socio;
import java.util.Date;

public class EmprestimoAcademia {

    private int id;
    private Socio pessoa;
    private Date dtEmprestimo;
    
    private static final Logger log = Logger.getLogger("techsoft.clube.EmprestimoAcademia");

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Socio getPessoa() {
        return pessoa;
    }
    public void setPessoa(Socio pessoa) {
        this.pessoa = pessoa;
    }
    public Date getDtEmprestimo() {
        return dtEmprestimo;
    }
    public void setDtEmprestimo(Date dtEmprestimo) {
        this.dtEmprestimo = dtEmprestimo;
    }

    public static EmprestimoAcademia getInstance(int id){
        EmprestimoAcademia d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_EMPRESTIMO_ACADEMIA ('N', null, ?, null, null, null)}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new EmprestimoAcademia();
                d.setId(rs.getInt("NU_SEQ_EMPRESTIMO"));
                d.setPessoa(Socio.getInstance(rs.getInt("CD_MATRICULA"), rs.getInt("SEQ_DEPENDENTE"),  rs.getInt("CD_CATEGORIA")));
                d.setDtEmprestimo(rs.getTimestamp("DT_EMPRESTIMO"));
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

        try {
            cal = cn.prepareCall("{call SP_EMPRESTIMO_ACADEMIA ('I', null, null, ?, ?, ?)}");
            cal.setInt(1, pessoa.getMatricula());
            cal.setInt(2, pessoa.getIdCategoria());
            cal.setInt(3, pessoa.getSeqDependente());
            ResultSet rs = cal.executeQuery();


            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    id = rs.getInt("ID");
                    cn.commit();
                    
                    audit.registrarMudanca("{call SP_EMPRESTIMO_ACADEMIA ('I', null, null, ?, ?, ?)}", String.valueOf(pessoa.getMatricula()), String.valueOf(pessoa.getIdCategoria()), String.valueOf(pessoa.getSeqDependente()));
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