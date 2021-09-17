package techsoft.operacoes;

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
import techsoft.tabelas.InserirException;
import techsoft.util.Datas;

public class TaxaCurso {
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.TaxaCurso");
 
    private Date dtInicial;
    private Date dtFinal;
    private float valor;
    private int curso;
    private String deCurso;
    private int categoria;

    public Date getDtInicial() {
        return dtInicial;
    }
    public void setDtInicial(Date dtInicial) {
        this.dtInicial = dtInicial;
    }
    public Date getDtFinal() {
        return dtFinal;
    }
    public void setDtFinal(Date dtFinal) {
        this.dtFinal = dtFinal;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public int getCurso() {
        return curso;
    }
    public void setCurso(int curso) {
        this.curso = curso;
    }
    public int getCategoria() {
        return categoria;
    }
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    public String getDeCurso() {
        return deCurso;
    }
    public void setDeCurso(String deCurso) {
        this.deCurso = deCurso;
    }
    
    public static List<TaxaCurso> consultar(int idCategoria, String tipo, String dtRef ){
        ArrayList<TaxaCurso> l = new ArrayList<TaxaCurso>();
        String sql = "SELECT T1.DT_VALID_INIC_TX_CURSO, T1.DT_VALID_FIM_TX_CURSO, T1.VAL_TX_CURSO, T1.CD_CURSO, T2.DESCR_CURSO FROM TB_VAL_TX_CURSO T1, TB_CURSO T2 WHERE T1.CD_CURSO = T2.CD_CURSO AND T1.CD_CATEGORIA = " + idCategoria;

        if (!tipo.equals("TD")){
            if (tipo.equals("AT")){
                sql = sql + " AND DT_VALID_INIC_TX_CURSO <= '" + dtRef.substring(3, 5) + '/' + dtRef.substring(0, 2) + '/' + dtRef.substring(6) + "'";
            }else{
            }
                sql = sql + " AND (DT_VALID_FIM_TX_CURSO >= '" + dtRef.substring(3, 5) + '/' + dtRef.substring(0, 2) + '/' + dtRef.substring(6) + "' OR DT_VALID_FIM_TX_CURSO IS NULL)";
        }
        
        sql = sql + " ORDER BY DESCR_CURSO ";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                TaxaCurso d = new TaxaCurso();
                d.dtInicial = rs.getTimestamp("DT_VALID_INIC_TX_CURSO");
                d.dtFinal = rs.getTimestamp("DT_VALID_FIM_TX_CURSO");
                d.valor = rs.getFloat("VAL_TX_CURSO");
                d.curso = rs.getInt("CD_CURSO");
                d.deCurso = rs.getString("DESCR_CURSO");
                
                
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
    

    public void inserir(Auditoria audit)throws InserirException{

        Connection cn = null;
        ParametroAuditoria par = new ParametroAuditoria();
        
        try {
            cn = Pool.getInstance().getConnection();
            
            String sql = "UPDATE TB_VAL_TX_CURSO SET DT_VALID_FIM_TX_CURSO = DATEADD(S, -1, ?) WHERE CD_CATEGORIA = ? AND CD_CURSO = ? AND DT_VALID_INIC_TX_CURSO < ? AND DT_VALID_FIM_TX_CURSO IS NULL" ;

            PreparedStatement p = cn.prepareStatement(sql);

            p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            p.setInt(2, par.getSetParametro(categoria));
            p.setInt(3, par.getSetParametro(curso));
            p.setTimestamp(4, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
        
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

        try {
            cn = Pool.getInstance().getConnection();
            
            String sql = "INSERT INTO TB_VAL_TX_CURSO (CD_CATEGORIA, CD_CURSO, DT_VALID_INIC_TX_CURSO, DT_VALID_FIM_TX_CURSO, VAL_TX_CURSO) VALUES (?,?,?,?,?)";
            
            PreparedStatement p = cn.prepareStatement(sql);

            p.setInt(1, par.getSetParametro(categoria));
            p.setInt(2, par.getSetParametro(curso));
            p.setTimestamp(3, new java.sql.Timestamp(par.getSetParametro(dtInicial).getTime()));
            if(dtFinal==null){
                p.setNull(4, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setTimestamp(4, new java.sql.Timestamp(par.getSetParametro(dtFinal).getTime()));
            }
            p.setFloat(5, par.getSetParametro(valor));
            
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


    public void alterar(Auditoria audit)throws InserirException{

        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            
            String sql = "UPDATE TB_VAL_TX_CURSO SET DT_VALID_FIM_TX_CURSO = ? WHERE CD_CURSO = ? AND DT_VALID_INIC_TX_CURSO = ? AND CD_CATEGORIA = ?" ;
            
            PreparedStatement p = cn.prepareStatement(sql);

            if(dtFinal==null){
                p.setNull(1, java.sql.Types.DATE);
            }else{
                p.setTimestamp(1, new java.sql.Timestamp(dtFinal.getTime()));
            }
            p.setInt(2, curso);
            p.setTimestamp(3, new java.sql.Timestamp(dtInicial.getTime()));
            p.setInt(4, categoria);
        
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
