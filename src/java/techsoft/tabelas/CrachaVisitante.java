
package techsoft.tabelas;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.operacoes.Visitante;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class CrachaVisitante {

    private int id;
    private int setor;
    private String deSetor;
    private static final Logger log = Logger.getLogger("techsoft.tabelas.CrachaVisitante");

    public int getSetor() {
        return setor;
    }

    public void setSetor(int setor) {
        this.setor = setor;
    }

    public int getId(){
        return id;
    }

    public String getDeSetor() {
        return deSetor;
    }

    public void setDeSetor(String deSetor) {
        this.deSetor = deSetor;
    }

    public static List<CrachaVisitante> listar(){
        ArrayList<CrachaVisitante> l = new ArrayList<CrachaVisitante>();
        String sql = "SELECT T1.NR_CRACHA, T1.CD_SETOR, T2.DESCR_SETOR FROM TB_CRACHA_VISITANTE T1, TB_SETOR T2 WHERE T1.CD_SETOR = T2.CD_SETOR ORDER BY 3";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                CrachaVisitante d = new CrachaVisitante();
                d.id = rs.getInt(1);
                d.setor = rs.getInt(2);
                d.deSetor = rs.getString(3);
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

    public static CrachaVisitante getInstance(int id){
        CrachaVisitante d = null;
        String sql = "SELECT T1.NR_CRACHA, T1.CD_SETOR, T2.DESCR_SETOR FROM TB_CRACHA_VISITANTE T1, TB_SETOR T2 WHERE T1.CD_SETOR = T2.CD_SETOR AND T1.NR_CRACHA = ?";
        Connection cn = null;
        

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new CrachaVisitante();
                d.id = rs.getInt(1);
                d.setor = rs.getInt(2);
                d.deSetor = rs.getString(3);
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

    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_CRACHA_VISITANTE WHERE NR_CRACHA = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(id));
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

    public void inserir(Auditoria audit)throws InserirException{


        Connection cn = null;

        try {

            String sql = "INSERT INTO TB_CRACHA_VISITANTE (NR_CRACHA, CD_SETOR) VALUES ((SELECT MAX(NR_CRACHA) + 1 FROM TB_CRACHA_VISITANTE), ?)";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, setor);
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(setor));

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

    public boolean emprestado(){
        Connection cn = null;
        boolean emprestado = false;
        
        try {
            String sql = "SELECT * FROM TB_REGISTRO_VISITANTE WHERE NR_CRACHA = "
                    + this.getId()
                    + " AND DT_SAIDA IS NULL";
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            if(rs.next()){
                emprestado = true;
            }

        }catch(SQLException e){
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        return emprestado;
    }
    
    public Visitante registrarEntreaga(Auditoria audit, Visitante visitante,
            Setor setorVisitado)throws InserirException{
        Connection cn = null;
        Visitante ret = null;
        
        if(emprestado()){
            throw new InserirException("Esse crachá já está com um visitante");
        }
                    
        try {
            String sql = "{call SP_REGISTRO_VISITANTE_NOVA(?, ?, ?, ?, ?, ?, ?)}";
            cn = Pool.getInstance().getConnection();
            CallableStatement c = cn.prepareCall(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            
            c.setInt(1, par.getSetParametro(this.id));
            c.setString(2, par.getSetParametro(audit.getLogin()));
            c.setString(3, par.getSetParametro(visitante.getNome().toUpperCase()));
            if(visitante.getDocumento() == null || visitante.getDocumento().trim().equals("")){
                c.setNull(4, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                c.setString(4, par.getSetParametro(visitante.getDocumento()));
            }
            if(visitante.getId() == 0){
                c.setNull(5, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                c.setInt(5, par.getSetParametro(visitante.getId()));
            }
            if(setorVisitado == null || setorVisitado.getId() == 0){
                c.setNull(6, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                c.setInt(6, par.getSetParametro(setorVisitado.getId()));
            }
            if(visitante.getPlaca() == null || visitante.getPlaca().trim().equals("")){
                c.setNull(7, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                c.setString(7, par.getSetParametro(visitante.getPlaca()));
            }
            
            c.execute();
            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(this.id), audit.getLogin(), visitante.getNome());

            ResultSet rs = c.getResultSet();
            if(rs.next()){
                ret = Visitante.getInstance(rs.getInt(1));
            }else{
                throw new InserirException("O sistema nao conseguiu retornar o objeto visitante que efetuou o registro");
            }            
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
        
        return ret;
    }
    
    public void registrarDevolucao(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "UPDATE TB_REGISTRO_VISITANTE SET DT_SAIDA = GETDATE(), "
                    + "USER_FUNC_DEVOLUCAO = ? WHERE NR_CRACHA = ? "
                    + " AND DT_SAIDA IS NULL";

            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            
            p.setString(1, audit.getLogin());
            p.setInt(2, this.id);
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, audit.getLogin(), String.valueOf(this.id));

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
