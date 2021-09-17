
package techsoft.cadastro;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.Cor;
import techsoft.tabelas.InserirException;
import techsoft.tabelas.ModeloCarro;

public class CarroSocio {

    protected int id;
    protected Cor cor;
    protected String placa;
    protected ModeloCarro modelo;
    protected Socio socio;
    private int qtDocumento;
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.CarroSocio");

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
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

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }
    public int getQtDocumento() {
        return qtDocumento;
    }
    public void setQtDocumento(int qtDocumento) {
        this.qtDocumento = qtDocumento;
    }

    public static List<CarroSocio> listar(Socio s){
        ArrayList<CarroSocio> l = new ArrayList<CarroSocio>();
        
        String sql = "SELECT T1.DE_MARCA, T2.DE_MODELO, T3.DE_COR, T1.CD_MARCA, T2.CD_MODELO, T3.CD_COR, T4.DE_PLACA, T4.CD_CARRO, "
                + " (SELECT COUNT(*) FROM IATE_FOTO..TB_FOTO_CARRO_SOCIO T0 WHERE T0.CD_CARRO = T4.CD_CARRO) QT_FOTO "
                + " FROM TB_MARCA_CARRO T1, TB_MODELO_CARRO T2, TB_COR T3, TB_CARRO_PESSOA T4"
                + " WHERE T1.CD_MARCA = T2.CD_MARCA AND T2.CD_MODELO = T4.CD_MODELO AND T3.CD_COR = T4.CD_COR "
                + " AND CD_MATRICULA = ? AND CD_CATEGORIA = ? AND SEQ_DEPENDENTE = ?";
        
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, s.getMatricula());
            ps.setInt(2, s.getIdCategoria());
            ps.setInt(3, s.getSeqDependente());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CarroSocio d = new CarroSocio();
                d.id = rs.getInt("CD_CARRO");
                d.cor = Cor.getInstance(rs.getInt("CD_COR"));
                d.placa = rs.getString("DE_PLACA");
                d.modelo = ModeloCarro.getInstance(rs.getInt("CD_MODELO"));
                d.socio = s;
                d.setQtDocumento(rs.getInt("QT_FOTO"));
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

    public static CarroSocio getInstance(int id){
        CarroSocio d = null;
        String sql = "SELECT * FROM TB_CARRO_PESSOA WHERE CD_CARRO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new CarroSocio();
                d.id = rs.getInt("CD_CARRO");
                d.cor = Cor.getInstance(rs.getInt("CD_COR"));
                d.placa = rs.getString("DE_PLACA");
                d.modelo = ModeloCarro.getInstance(rs.getInt("CD_MODELO"));
                Socio s = Socio.getInstance(rs.getInt("CD_MATRICULA"), rs.getInt("SEQ_DEPENDENTE"), rs.getInt("CD_CATEGORIA"));
                d.socio = s;                
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
            String sql = "DELETE FROM TB_CARRO_PESSOA WHERE CD_CARRO = ?";
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

        if(id != 0 || placa == null) return;

        Connection cn = null;
        PreparedStatement p = null;
        ResultSet rs = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "insert into TB_CARRO_PESSOA values(?, ?, ?, ?, ?, ?)";
            
            p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setInt(1, par.getSetParametro(socio.getMatricula()));
            p.setInt(2, par.getSetParametro(socio.getIdCategoria()));
            p.setInt(3, par.getSetParametro(socio.getSeqDependente()));
            p.setInt(4, par.getSetParametro(modelo.getId()));
            p.setInt(5, par.getSetParametro(cor.getId()));
            p.setString(6, par.getSetParametro(placa));
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

        if(id == 0 || placa == null) return;

        Connection cn = null;

        try {
            String sql = "update TB_CARRO_PESSOA set cd_MODELO = ? "
            + ", cd_COR = ?, de_PLACA = ? WHERE CD_CARRO = ?";

            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
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
    
    public void excluirFoto(){

        Connection cn = null;
        String sql =  "DELETE FROM TB_FOTO_CARRO_SOCIO WHERE CD_CARRO = ?";
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            p.executeUpdate();

            cn.commit();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }
    
    public void atualizarFoto(File f){

        Connection cn = null;
        BufferedInputStream bin = null;
        
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(
                "SELECT * FROM TB_FOTO_CARRO_SOCIO WHERE CD_CARRO = ?",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            
            try{
                bin = new BufferedInputStream(new FileInputStream(f));
            }catch(FileNotFoundException e){
                log.severe(e.getMessage());
            }
                            
            if(rs.next()){
               
                rs.updateBinaryStream("FOTO_CARRO", bin);
                rs.updateRow();
            }else{
                rs.moveToInsertRow();
                rs.updateInt("CD_CARRO", id);
                rs.updateBinaryStream("FOTO_CARRO", bin);
                rs.insertRow();
            }

            cn.commit();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
            try {
                bin.close();
            } catch (IOException e) {
                log.severe(e.getMessage());
            }
        }

    }        


}
