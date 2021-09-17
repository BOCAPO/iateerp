
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
import techsoft.seguranca.ParametroAuditoria;

public class CargoEspecial {
    
    private int id;
    private String descricao;
    private int qtMax;
    private String gestao;
    private String idTipoCargo;
    private String descricaoTipoCargo;//soh eh usado na listagem
    private String categoria;
    private String corFundo;
    private String corFonte;
    private static final Logger log = Logger.getLogger("techsoft.tabelas.CargoEspecial");
    
    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQtMax() {
        return qtMax;
    }

    public void setQtMax(int qtMax) {
        this.qtMax = qtMax;
    }

    public String getGestao() {
        return gestao;
    }

    public void setGestao(String gestao) {
        this.gestao = gestao;
    }

    public String getIdTipoCargo() {
        return idTipoCargo;
    }

    public void setIdTipoCargo(String idTipoCargo) {
        this.idTipoCargo = idTipoCargo;
    }

    public String getDescricaoTipoCargo() {
        return descricaoTipoCargo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCorFundo() {
        if(corFundo != null){
            return CargoEspecial.decodeColor(corFundo);
        }else{
            return null;
        }
    }

    public void setCorFundo(String corFundo) {
        if(corFundo != null){
            this.corFundo = CargoEspecial.encodeColor(corFundo);
        }else{
            this.corFundo = null;
        }
    }

    public String getCorFonte() {
        if(corFonte != null){
            return CargoEspecial.decodeColor(corFonte);
        }else{
            return null;
        }
    }

    public void setCorFonte(String corFonte) {
        if(corFonte != null){
            this.corFonte = CargoEspecial.encodeColor(corFonte);
        }else{
            this.corFonte = null;
        }
    }
    
    public static List<CargoEspecial> listar(){
        ArrayList<CargoEspecial> l = new ArrayList<CargoEspecial>();
        String sql = "SELECT "
            + "T1.CD_CARGO_ESPECIAL, "
            + "T1.DESCR_CARGO_ESPECIAL, "
            + "T1.QT_VAGAS, "
            + "T1.DE_GESTAO, "
            + "T2.CO_TIPO_CARGO, "
            + "T2.DE_TIPO_CARGO, "
            + "T1.DE_CATEGORIA "
       + "FROM "
            + "TB_CARGO_ESPECIAL T1, "
            + "TB_TIPO_CARGO T2 "
       + "WHERE "
            + "T1.IC_TIPO_CARGO *= T2.CO_TIPO_CARGO ORDER BY 2";

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                CargoEspecial e = new CargoEspecial();
                e.id = rs.getInt(1);
                e.descricao = rs.getString(2);
                e.qtMax = rs.getInt(3);
                e.gestao = rs.getString(4);
                e.idTipoCargo = rs.getString(5);
                e.descricaoTipoCargo = rs.getString(6);
                e.categoria = rs.getString(7);
                l.add(e);
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

    public static CargoEspecial getInstance(int id){
        CargoEspecial e = null;
        String sql = "SELECT * FROM TB_CARGO_ESPECIAL WHERE CD_CARGO_ESPECIAL = ?";
        
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                e = new CargoEspecial();
                e.id = rs.getInt("CD_CARGO_ESPECIAL");
                e.descricao = rs.getString("DESCR_CARGO_ESPECIAL");
                e.qtMax = rs.getInt("QT_VAGAS");
                e.gestao = rs.getString("DE_GESTAO");
                e.corFundo = rs.getString("COR_CARTEIRA");
                e.idTipoCargo = rs.getString("IC_TIPO_CARGO");
                e.categoria = rs.getString("DE_CATEGORIA");
                e.corFonte = rs.getString("COR_FONTE");
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }

        return e;
    }

    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_CARGO_ESPECIAL WHERE CD_CARGO_ESPECIAL = ?";
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

        if(id != 0) return;

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            String sql = "INSERT INTO TB_CARGO_ESPECIAL (DESCR_CARGO_ESPECIAL,"
                    + "QT_VAGAS, COR_CARTEIRA, DE_GESTAO, IC_TIPO_CARGO, "
                    + "DE_CATEGORIA, COR_FONTE) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, par.getSetParametro(descricao));
            p.setInt(2, par.getSetParametro(qtMax));
            if(corFundo != null){
                p.setString(3, par.getSetParametro(corFundo));
            }else{
                p.setNull(3, java.sql.Types.VARCHAR);
                par.getSetNull();
            }            
            if(gestao != null){
                p.setString(4, par.getSetParametro(descricao));
            }else{
                p.setNull(4, java.sql.Types.VARCHAR);
                par.getSetNull();
            }
            if(idTipoCargo.equals("NE")){
                p.setNull(5, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(5, par.getSetParametro(idTipoCargo));
            }
            
            p.setString(6, par.getSetParametro(categoria));
            
            if(corFonte != null){
                p.setString(7, par.getSetParametro(corFonte));
            }else{
                p.setNull(7, java.sql.Types.VARCHAR);
                par.getSetNull();
            }            
            
            p.executeUpdate();
            ResultSet rs = p.getGeneratedKeys();

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

        if(id == 0) return;

        Connection cn = null;

        try {
            String sql = "UPDATE TB_CARGO_ESPECIAL SET DESCR_CARGO_ESPECIAL = ?,"
                    + "QT_VAGAS = ?, COR_CARTEIRA = ?, DE_GESTAO = ?, IC_TIPO_CARGO = ?, "
                    + "DE_CATEGORIA = ?, COR_FONTE = ? WHERE CD_CARGO_ESPECIAL = ?";
            
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setString(1, par.getSetParametro(descricao));
            p.setInt(2, par.getSetParametro(qtMax));
            if(corFundo != null){
                p.setString(3, par.getSetParametro(corFundo));
            }else{
                p.setNull(3, java.sql.Types.VARCHAR);
                par.getSetNull();
            }            
            if(gestao != null){
                p.setString(4, par.getSetParametro(gestao));
            }else{
                p.setNull(4, java.sql.Types.VARCHAR);
                par.getSetNull();
            }
            if(idTipoCargo.equals("NE")){
                p.setNull(5, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(5, par.getSetParametro(idTipoCargo));
            }
            
            p.setString(6, par.getSetParametro(categoria));
            
            if(corFonte != null){
                p.setString(7, par.getSetParametro(corFonte));
            }else{
                p.setNull(7, java.sql.Types.VARCHAR);
                par.getSetNull();
            }            
            
            p.setInt(8, id);

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
    
    /*
     * Decodifica do padrao visual basic 6 para o padrao web
     */
    private static String decodeColor(String color){
        StringBuilder sb = new StringBuilder();
        int i = Integer.parseInt(color);
        int red = 0x000000FF & i;
        int green = (0x000000FF & (i >>> 8));
        int blue = (0x000000FF & (i >>> 16));
        
        if(red <= 0xF){
            sb.append("0");
        }
        sb.append(Integer.toHexString(red));
            
        if(green <= 0xF){
            sb.append("0");
        }
        sb.append(Integer.toHexString(green));
            
        if(blue <= 0xF){
            sb.append("0");
        }        
        sb.append(Integer.toHexString(blue));
            
        return sb.toString();
    }
    
    /*
     * codifica para o padrao que o visual basic 6 usa
     */
    private static String encodeColor(String color){
        int red = Integer.parseInt(color.substring(0, 2), 16);
        int green = Integer.parseInt(color.substring(2, 4), 16);
        int blue = Integer.parseInt(color.substring(4, 6), 16);

        int i = 0;
        i = i | red;
        i = i | (green << 8);
        i = i | (blue << 16);
        
        return String.valueOf(i);
    }    

}
