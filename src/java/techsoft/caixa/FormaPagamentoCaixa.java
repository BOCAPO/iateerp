
package techsoft.caixa;

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

public class FormaPagamentoCaixa {

    private int id;
    private String descricao;
    private static final Logger log = Logger.getLogger("techsoft.caixa.FormaPagamentoCaixa");

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    public int getId(){
        return id;
    }

    public static List<FormaPagamentoCaixa> listar(){
        ArrayList<FormaPagamentoCaixa> l = new ArrayList<FormaPagamentoCaixa>();
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        
        try {
            cal = cn.prepareCall("{call SP_FORMA_PAGAMENTO_CAIXA (NULL, NULL, 'C')}");
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                FormaPagamentoCaixa d = new FormaPagamentoCaixa();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
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

    public static FormaPagamentoCaixa getInstance(int id){
        FormaPagamentoCaixa d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_FORMA_PAGAMENTO_CAIXA (?, NULL, 'N')}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new FormaPagamentoCaixa();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
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
            cal = cn.prepareCall("{call SP_FORMA_PAGAMENTO_CAIXA (?, NULL, 'E')}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_FORMA_PAGAMENTO_CAIXA (?, NULL, 'N')}", String.valueOf(id));
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new ExcluirException(err);
                }
            }else{
                String err = "Erro na opera��o, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new ExcluirException(err);
            }

        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new ExcluirException("Erro na opera��o, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new ExcluirException("Erro na opera��o, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new ExcluirException("Erro na opera��o, entre em contato com o Administrador do Sistema");
            }
        }
    }

    public void inserir(Auditoria audit)throws InserirException{

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_FORMA_PAGAMENTO_CAIXA (NULL, ?, 'I')}");
            cal.setString(1, descricao);
            ResultSet rs = cal.executeQuery();


            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_FORMA_PAGAMENTO_CAIXA (NULL, ?, 'I')}", descricao);
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new InserirException(err);
                }
            }else{
                String err = "Erro na opera��o, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new InserirException(err);
            }
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new InserirException("Erro na opera��o, entre em contato com o Administrador do Sistema");
                
            }

            log.severe(e.getMessage());
            throw new InserirException("Erro na opera��o, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new InserirException("Erro na opera��o, entre em contato com o Administrador do Sistema");
            }
        }
    }

    public void alterar(Auditoria audit) throws AlterarException{

        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_FORMA_PAGAMENTO_CAIXA (?, ?, 'A')}");
            cal.setInt(1, id);
            cal.setString(2, descricao);
            ResultSet rs = cal.executeQuery();

            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_FORMA_PAGAMENTO_CAIXA (?, ?, 'A')}", String.valueOf(id) , descricao);
                }else{
                    String err = rs.getString("MSG");
                    log.warning(err);
                    throw new AlterarException(err);
                }
            }else{
                String err = "Erro na opera��o, entre em contato com o Administrador do Sistema";
                log.warning(err);
                throw new AlterarException(err);
            }
            
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
                throw new AlterarException("Erro na opera��o, entre em contato com o Administrador do Sistema");
            }

            log.severe(e.getMessage());
            throw new AlterarException("Erro na opera��o, entre em contato com o Administrador do Sistema");
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
                throw new AlterarException("Erro na opera��o, entre em contato com o Administrador do Sistema");
            }
        }
    }

}