
package techsoft.tabelas;

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

public class Carro {

    private int id;
    private String descricao;
    private String placa;
    private ModeloCarro modelo;
    
    private static final Logger log = Logger.getLogger("techsoft.tabelas.Carro");

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
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

    public static List<Carro> listar(){
        ArrayList<Carro> l = new ArrayList<Carro>();
        String sql = "SELECT T1.CD_CARRO, T1.NO_PESSOA, T1.DE_PLACA, T1.CD_MODELO, T2.MODELO FROM TB_CARRO T1, VW_MODELO_MARCA T2 WHERE T1.CD_MODELO = T2.CD_MODELO ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Carro d = new Carro();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.placa = rs.getString(3);
                d.modelo = ModeloCarro.getInstance(rs.getInt(4));
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

    public static Carro getInstance(int id){
        Carro d = null;
        String sql = "SELECT T1.CD_CARRO, T1.NO_PESSOA, T1.DE_PLACA, T1.CD_MODELO, T2.MODELO FROM TB_CARRO T1, VW_MODELO_MARCA T2 WHERE T1.CD_MODELO = T2.CD_MODELO AND CD_CARRO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new Carro();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.placa = rs.getString(3);
                d.modelo = ModeloCarro.getInstance(rs.getInt(4));
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
            String sql = "DELETE FROM TB_CARRO WHERE CD_CARRO = ?";
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

        if(id != 0 || descricao == null) return;

        Connection cn = null;
        PreparedStatement p = null;
        ResultSet rs = null;

        try {
            cn = Pool.getInstance().getConnection();
            String sql = "INSERT INTO TB_CARRO (NO_PESSOA, CD_MODELO, DE_PLACA) VALUES (?, ?, ?)";
            p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, descricao);
            p.setInt(2, modelo.getId());
            p.setString(3, placa);
            p.executeUpdate();
            rs = p.getGeneratedKeys();

            if(rs.next()){
                id = rs.getInt(1);
                cn.commit();
                audit.registrarMudanca(sql, descricao, String.valueOf(modelo.getId()), placa);
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

        if(id == 0 || descricao == null) return;

        Connection cn = null;

        try {
            String sql = "UPDATE TB_CARRO SET NO_PESSOA = ?, DE_PLACA = ?, CD_MODELO = ? WHERE CD_CARRO = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricao);
            p.setString(2, placa);
            p.setInt(3, modelo.getId());
            p.setInt(4, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, descricao, placa, String.valueOf(modelo.getId()), String.valueOf(id));
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
