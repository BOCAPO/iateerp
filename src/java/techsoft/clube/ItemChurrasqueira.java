
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

public class ItemChurrasqueira {

    private int id;
    private int dependencia;
    private int codigo;
    private String descricao;
    private int quantidade;
    private String observacao;
    private static final Logger log = Logger.getLogger("techsoft.clube.ItemChurrasqueira");

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }
    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getDependencia() {
        return dependencia;
    }
    public void setDependencia(int dependencia) {
        this.dependencia = dependencia;
    }
    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    public String getObservacao() {
        return observacao;
    }
   public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    

    public static List<ItemChurrasqueira> listar(int dependencia){
        ArrayList<ItemChurrasqueira> l = new ArrayList<ItemChurrasqueira>();
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        
        try {
            cal = cn.prepareCall("{call SP_ITEM_CHURRASQUEIRA ('T', NULL, ?, NULL, NULL, NULL, NULL)}");
            cal.setInt(1, dependencia);
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                ItemChurrasqueira d = new ItemChurrasqueira();
                d.id = rs.getInt("SEQ_ITEM_CHURRASQUEIRA");
                d.codigo = rs.getInt("CD_ITEM");
                d.descricao = rs.getString("DE_ITEM");
                d.quantidade = rs.getInt("QT_ITEM");
                d.observacao = rs.getString("DE_OBSERVACAO");
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

    public static ItemChurrasqueira getInstance(int id){
        ItemChurrasqueira d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("{call SP_ITEM_CHURRASQUEIRA ('C', ?, NULL, NULL, NULL, NULL, NULL)}");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new ItemChurrasqueira();
                d.id = rs.getInt("SEQ_ITEM_CHURRASQUEIRA");
                d.codigo = rs.getInt("CD_ITEM");
                d.descricao = rs.getString("DE_ITEM");
                d.quantidade = rs.getInt("QT_ITEM");
                d.observacao = rs.getString("DE_OBSERVACAO");
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
            CallableStatement cal = cn.prepareCall("{call SP_ITEM_CHURRASQUEIRA ('E', ?, NULL, NULL, NULL, NULL, NULL)}");
            cal.setInt(1, getId());
            ResultSet rs = cal.executeQuery();
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ITEM_CHURRASQUEIRA ('E', ?, NULL, NULL, NULL, NULL, NULL)}", String.valueOf(getId()));
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
            cal = cn.prepareCall("{call SP_ITEM_CHURRASQUEIRA ('I', NULL, ?, ?, ?, ?, ?)}");
            cal.setInt(1, getDependencia());
            cal.setInt(2, getCodigo());
            cal.setString(3, getDescricao());
            cal.setInt(4, getQuantidade());
            cal.setString(5, getObservacao());
            
            ResultSet rs = cal.executeQuery();

            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ITEM_CHURRASQUEIRA ('I', null, ?, ?, ?, ?, ?)}", String.valueOf(getDependencia()), String.valueOf(getCodigo()), getDescricao(), String.valueOf(getQuantidade()), getObservacao());
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
            cal = cn.prepareCall("{call SP_ITEM_CHURRASQUEIRA ('A', ?, ?, ?, ?, ?, ?)}");
            cal.setInt(1, getId());
            cal.setInt(2, getDependencia());
            cal.setInt(3, getCodigo());
            cal.setString(4, getDescricao());
            cal.setInt(5, getQuantidade());
            cal.setString(6, getObservacao());
            
            ResultSet rs = cal.executeQuery();
            
            if (rs.next()){
                if (rs.getString("MSG").equals("OK")){
                    cn.commit();
                    audit.registrarMudanca("{call SP_ITEM_CHURRASQUEIRA ('A', ?, ?, ?, ?, ?, ?)}", String.valueOf(getId()), String.valueOf(getDependencia()), String.valueOf(getCodigo()), getDescricao(), String.valueOf(getQuantidade()), getObservacao());
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