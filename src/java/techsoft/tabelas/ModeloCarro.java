
package techsoft.tabelas;

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

public class ModeloCarro {

    private int id;
    private String descricao;
    private MarcaCarro marca;

    private static final Logger log = Logger.getLogger("techsoft.tabelas.ModeloCarro");

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    public int getId(){
        return id;
    }

    public MarcaCarro getMarca() {
        return marca;
    }

    public void setMarca(MarcaCarro marca) {
        this.marca = marca;
    }
    
    public static List<ModeloCarro> listar(){
        ArrayList<ModeloCarro> l = new ArrayList<ModeloCarro>();
        String sql = "SELECT CD_MODELO, DE_MODELO, T2.CD_MARCA, T2.DE_MARCA FROM TB_MODELO_CARRO T1, TB_MARCA_CARRO T2 WHERE T1.CD_MARCA = T2.CD_MARCA ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                ModeloCarro d = new ModeloCarro();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.marca = MarcaCarro.getInstance(rs.getInt(3));
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

    public static ModeloCarro getInstance(int id){
        ModeloCarro d = null;
        String sql = "SELECT CD_MODELO, DE_MODELO, CD_MARCA FROM TB_MODELO_CARRO WHERE CD_MODELO = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new ModeloCarro();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.marca = MarcaCarro.getInstance(rs.getInt(3));
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
            String sql = "DELETE FROM TB_MODELO_CARRO WHERE CD_MODELO = ?";
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

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement("SELECT * FROM TB_MODELO_CARRO WHERE DE_MODELO = ?");
            p.setString(1, descricao);
            ResultSet rs = p.executeQuery();

            if(rs.next()){
                String err = "ModeloCarro já cadastrado " + descricao;
                log.warning(err);
                throw new InserirException(err);
            }else{
                String sql = "INSERT INTO TB_MODELO_CARRO (DE_MODELO, CD_MARCA) VALUES (?, ?)";
                p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                p.setString(1, descricao);
                p.setInt(2, marca.getId());
                p.executeUpdate();
                rs = p.getGeneratedKeys();

                if(rs.next()){
                    id = rs.getInt(1);
                    cn.commit();
                    audit.registrarMudanca(sql, descricao, String.valueOf(marca.getId()));
                }

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
            String sql = "UPDATE TB_MODELO_CARRO SET DE_MODELO = ?, CD_MARCA = ? WHERE CD_MODELO = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricao);
            p.setInt(2, marca.getId());
            p.setInt(3, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, descricao, String.valueOf(marca.getId()), String.valueOf(id));
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
