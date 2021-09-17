
package techsoft.cadastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.Cor;
import techsoft.tabelas.ModeloCarro;

public class TitularPendenciaCarro {

    private int id;
    private Cor cor;    
    private String placa;
    private ModeloCarro modelo;
    private TitularPendencia titular;
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.TitularPendenciaCarro");

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public TitularPendencia getTitular() {
        return titular;
    }

    public void setTitular(TitularPendencia titular) {
        this.titular = titular;
    }
    
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa.trim();
    }
    public ModeloCarro getModelo() {
        return modelo;
    }

    public void setModelo(ModeloCarro modelo) {
        this.modelo = modelo;
    }
    
    public int getId(){
        return id;
    }

    public static List<TitularPendenciaCarro> listar(TitularPendencia tp){
        ArrayList<TitularPendenciaCarro> l = new ArrayList<TitularPendenciaCarro>();
        String sql = null;
        Connection cn = null;

        if(tp.getId() > 0){
            sql = "SELECT T1.DE_MARCA, T2.DE_MODELO, T3.DE_COR, T1.CD_MARCA, T2.CD_MODELO, T3.CD_COR, T4.DE_PLACA, T4.CD_CARRO, T4.CD_CARRO_ORIGINAL "
                + " FROM TB_MARCA_CARRO T1, TB_MODELO_CARRO T2, TB_COR T3, TB_CARRO_PENDENTE T4 "
                + " WHERE T1.CD_MARCA = T2.CD_MARCA AND T2.CD_MODELO = T4.CD_MODELO AND T3.CD_COR = T4.CD_COR "
                + " AND SEQ_PENDENCIA = " + tp.getId();
        }else{
            sql = "SELECT T1.DE_MARCA, T2.DE_MODELO, T3.DE_COR, T1.CD_MARCA, T2.CD_MODELO, T3.CD_COR, T4.DE_PLACA, T4.CD_CARRO, '' AS CD_CARRO_ORIGINAL "
                + " FROM TB_MARCA_CARRO T1, TB_MODELO_CARRO T2, TB_COR T3, TB_CARRO_PESSOA T4 "
                + " WHERE T1.CD_MARCA = T2.CD_MARCA AND T2.CD_MODELO = T4.CD_MODELO AND T3.CD_COR = T4.CD_COR "
                + " AND CD_MATRICULA = " + tp.getMatricula()
                + " AND CD_CATEGORIA = " + tp.getIdCategoria()
                + " AND SEQ_DEPENDENTE = 0";            
        }
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                TitularPendenciaCarro d = new TitularPendenciaCarro();
                d.id = rs.getInt("CD_CARRO");
                d.cor = Cor.getInstance(rs.getInt("CD_COR"));
                d.placa = rs.getString("DE_PLACA");
                d.modelo = ModeloCarro.getInstance(rs.getInt("CD_MODELO"));
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

    public static TitularPendenciaCarro getInstance(int id){
        TitularPendenciaCarro d = null;
        String sql = "SELECT * FROM TB_CARRO_PENDENTE WHERE CD_CARRO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new TitularPendenciaCarro();
                d.id = rs.getInt(1);
                d.placa = rs.getString("DE_PLACA");
                d.modelo = ModeloCarro.getInstance(rs.getInt("CD_MODELO"));
                d.cor = Cor.getInstance(rs.getInt("CD_COR"));
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
            String sql = "DELETE FROM TB_CARRO_PENDENTE WHERE CD_CARRO = ?";
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

    private void inserirIdOriginal(){
                  
        Connection cn = null;
        Statement p = null;

        try {
            cn = Pool.getInstance().getConnection();
            
            String sql = "INSERT INTO TB_CARRO_PENDENTE (CD_MODELO, CD_COR, DE_PLACA, SEQ_PENDENCIA, CD_CARRO_ORIGINAL) "
               + "SELECT CD_MODELO, CD_COR, DE_PLACA, " + titular.getId() + ", CD_CARRO "
               + " FROM TB_CARRO_PESSOA "
               + " WHERE CD_MATRICULA = " + titular.getMatricula()
               + " AND   CD_CATEGORIA = " + titular.getIdCategoria()
               + " AND   SEQ_DEPENDENTE = 0";

            p.executeUpdate(sql);
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
    
    public void inserir(Auditoria audit){

        if(titular.getId() == 0){
            inserirIdOriginal();
        }
        
        Connection cn = null;
        PreparedStatement p = null;
        ResultSet rs = null;
        ParametroAuditoria par = new ParametroAuditoria();

        try {
            cn = Pool.getInstance().getConnection();
            
            String sql = "insert into TB_CARRO_PENDENTE (CD_MODELO, CD_COR, DE_PLACA, SEQ_PENDENCIA) "
                    + "values(?, ?, ?, ?)";

            p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            p.setInt(1, par.getSetParametro(modelo.getId()));
            p.setInt(2, par.getSetParametro(cor.getId()));
            p.setString(3, par.getSetParametro(placa));
            p.setInt(4, par.getSetParametro(titular.getId()));
            p.executeUpdate();
            rs = p.getGeneratedKeys();

            if(rs.next()){
                id = rs.getInt(1);
                cn.commit();
                audit.registrarMudanca(sql, par.getParametroFinal());
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
    }

    public void alterar(Auditoria audit){

        if(titular.getId() == 0){
            inserirIdOriginal();
        }

        Connection cn = null;
        ParametroAuditoria par = new ParametroAuditoria();
        
        try {
            String sql = "update TB_CARRO_PENDENTE set cd_MODELO = ?, cd_COR = ?, de_PLACA = ? WHERE CD_CARRO = ?";

            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, par.getSetParametro(modelo.getId()));
            p.setInt(2, par.getSetParametro(cor.getId()));
            p.setString(3, par.getSetParametro(placa));
            p.setInt(4, par.getSetParametro(id));

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

}
