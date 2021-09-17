package techsoft.tabelas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import java.math.BigDecimal;
import techsoft.acesso.LocalAcesso;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;

public class Categoria {
    private int id;
    private String descricao;
    private String abreviacao;
    private String status;
    private String tipo;
    private String tituloTransferivel;
    private String admiteDependente;
    private float vrMaxCarne;
    private int idCatUsuario;
    private String deCatUsuario;
    private int qtUsuario;
    private int qtConvite;
    private int prazoRenovacaoConvite;
    private String corTitular;
    private String corDependente;

    private static final Logger log = Logger.getLogger("techsoft.acesso.Categoria");

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getAbreviacao() {
        return abreviacao;
    }
    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getDeCatUsuario() {
        return deCatUsuario;
    }
    public void setDeCatUsuario(String deCatUsuario) {
        this.deCatUsuario = deCatUsuario;
    }
    public String getTituloTransferivel() {
        return tituloTransferivel;
    }
    public void setTituloTransferivel(String tituloTransferivel) {
        this.tituloTransferivel = tituloTransferivel;
    }
    public String getAdmiteDependente() {
        return admiteDependente;
    }
    public void setAdmiteDependente(String admiteDependente) {
        this.admiteDependente = admiteDependente;
    }
    public float getVrMaxCarne() {
        return vrMaxCarne;
    }
    public void setVrMaxCarne(float vrMaxCarne) {
        this.vrMaxCarne = vrMaxCarne;
    }
    public int getIdCatUsuario() {
        return idCatUsuario;
    }
    public void setIdCatUsuario(int idCatUsuario) {
        this.idCatUsuario = idCatUsuario;
    }
    public int getQtUsuario() {
        return qtUsuario;
    }
    public void setQtUsuario(int qtUsuario) {
        this.qtUsuario = qtUsuario;
    }
    public int getQtConvite() {
        return qtConvite;
    }
    public void setQtConvite(int qtConvite) {
        this.qtConvite = qtConvite;
    }
    public int getPrazoRenovacaoConvite() {
        return prazoRenovacaoConvite;
    }
    public void setPrazoRenovacaoConvite(int prazoRenovacaoConvite) {
        this.prazoRenovacaoConvite = prazoRenovacaoConvite;
    }
    public String getCorTitular() {
        if(corTitular != null){
            return Categoria.decodeColor(corTitular);
        }else{
            return null;
        }
    }

    public void setCorTitular(String corTitular) {
        if(corTitular != null){
            this.corTitular = Categoria.encodeColor(corTitular);
        }else{
            this.corTitular = null;
        }
    }

    public String getCorDependente() {
        if(corDependente != null){
            return Categoria.decodeColor(corDependente);
        }else{
            return null;
        }
    }

    public void setCorDependente(String corDependente) {
        if(corDependente != null){
            this.corDependente = Categoria.encodeColor(corDependente);
        }else{
            this.corDependente = null;
        }
    }


    public static List<Categoria> listar() {
        String sql = "SELECT " +
                            "CD_CATEGORIA ," +
                            "DESCR_CATEGORIA," +
                            "CASE WHEN STATUS_CATEGORIA = 'AT' THEN 'Ativa' ELSE 'Inativa' END STATUS_CATEGORIA, " +
                            "QT_RENOV_SD_CONV_GERAL ," +
                            "PZ_RENOVACAO_SD_CONV ," +
                            "DT_ULTIMA_RENOVACAO_SD_CONV ," +
                            "CASE WHEN TP_CATEGORIA = 'SO' THEN 'Sócio' ELSE 'Não Sócio' END TP_CATEGORIA," +
                            "CASE WHEN CD_IND_TRANSFERENCIA = 'S' THEN 'Sim' ELSE 'Não' END CD_IND_TRANSFERENCIA," +
                            "ABREV_CATEGORIA ," +
                            "CASE WHEN ADMITE_DEPENDENTE = 'S' THEN 'Sim' ELSE 'Não' END ADMITE_DEPENDENTE," +
                            "CD_CATEGORIA_USUARIO, " +
                            "(SELECT DESCR_CATEGORIA FROM TB_Categoria T0 WHERE T1.CD_CATEGORIA_USUARIO = T0.CD_CATEGORIA) DE_CATEGORIA_USUARIO," +
                            "QT_USUARIO_CATEGORIA ," +
                            "VR_MAX_CARNE, " +
                            "COR_TITULAR, " +
                            "COR_DEPENDENTE " +
                    "FROM TB_CATEGORIA T1 ORDER BY 2";

        Connection cn = null;
        ArrayList<Categoria> l = new ArrayList<Categoria>();
        
        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Categoria i = new Categoria();
                i.setId(rs.getInt("CD_CATEGORIA"));
                i.setDescricao(rs.getString("DESCR_CATEGORIA"));
                i.setAbreviacao(rs.getString("ABREV_CATEGORIA"));
                i.setStatus(rs.getString("STATUS_CATEGORIA"));
                i.setTipo(rs.getString("TP_CATEGORIA"));
                i.setTituloTransferivel(rs.getString("CD_IND_TRANSFERENCIA"));
                i.setAdmiteDependente(rs.getString("ADMITE_DEPENDENTE"));
                i.setDeCatUsuario(rs.getString("DE_CATEGORIA_USUARIO"));
                i.setVrMaxCarne(rs.getFloat("VR_MAX_CARNE"));
                i.setIdCatUsuario(rs.getInt("CD_CATEGORIA_USUARIO"));
                i.setQtUsuario(rs.getInt("QT_USUARIO_CATEGORIA"));
                i.setQtConvite(rs.getInt("QT_RENOV_SD_CONV_GERAL"));
                i.setPrazoRenovacaoConvite(rs.getInt("PZ_RENOVACAO_SD_CONV"));
                i.corTitular = rs.getString("COR_TITULAR");
                i.corDependente = rs.getString("COR_DEPENDENTE");
                
                l.add(i);
            }
            cn.close();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
        
        return l;
    }
    
    
    public void inserir(Auditoria audit)throws InserirException{
 
        ResultSet rs = null;
        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            String sql = "INSERT INTO TB_CATEGORIA (CD_CATEGORIA, DESCR_CATEGORIA, STATUS_CATEGORIA, " +
                         "QT_RENOV_SD_CONV_GERAL, PZ_RENOVACAO_SD_CONV, TP_CATEGORIA," +
                         "CD_IND_TRANSFERENCIA, ABREV_CATEGORIA, ADMITE_DEPENDENTE, CD_CATEGORIA_USUARIO, " +
                         "QT_USUARIO_CATEGORIA, VR_MAX_CARNE) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    
                    
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, par.getSetParametro(id));
            p.setString(2, par.getSetParametro(descricao));
            p.setString(3, par.getSetParametro(status));
            
            if(qtConvite==0){
                p.setNull(4, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(4, par.getSetParametro(qtConvite));    
            }
            if(prazoRenovacaoConvite==0){
                p.setNull(5, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(5, par.getSetParametro(prazoRenovacaoConvite));    
            }
            p.setString(6, par.getSetParametro(tipo));
            p.setString(7, par.getSetParametro(tituloTransferivel));
            p.setString(8, par.getSetParametro(abreviacao));
            p.setString(9, par.getSetParametro(admiteDependente));
            if(idCatUsuario==0){
                p.setNull(10, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(10, par.getSetParametro(idCatUsuario));    
            }
            if(qtUsuario==0){
                p.setNull(11, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(11, par.getSetParametro(qtUsuario));    
            }
            if(vrMaxCarne==0){
                p.setNull(12, java.sql.Types.FLOAT);
                par.getSetNull();
            }else{
                p.setFloat(12, par.getSetParametro(vrMaxCarne));    
            }
            
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
 
        ResultSet rs = null;
        Connection cn = null;
        
        if(getDescricao() == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            String sql = "UPDATE TB_CATEGORIA SET DESCR_CATEGORIA = ?, STATUS_CATEGORIA = ?, " +
                         "QT_RENOV_SD_CONV_GERAL = ?, PZ_RENOVACAO_SD_CONV = ?, TP_CATEGORIA = ?," +
                         "CD_IND_TRANSFERENCIA = ?, ABREV_CATEGORIA = ?, ADMITE_DEPENDENTE = ?, CD_CATEGORIA_USUARIO = ?, " +
                         "QT_USUARIO_CATEGORIA = ?, VR_MAX_CARNE = ?, COR_TITULAR = ?, COR_DEPENDENTE = ? WHERE CD_CATEGORIA = ?";
                    
                    
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, par.getSetParametro(descricao));
            p.setString(2, par.getSetParametro(status));
            
            if(qtConvite==0){
                p.setNull(3, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(3, par.getSetParametro(qtConvite));    
            }
            if(prazoRenovacaoConvite==0){
                p.setNull(4, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(4, par.getSetParametro(prazoRenovacaoConvite));    
            }
            
            
            p.setString(5, par.getSetParametro(tipo));
            p.setString(6, par.getSetParametro(tituloTransferivel));
            p.setString(7, par.getSetParametro(abreviacao));
            p.setString(8, par.getSetParametro(admiteDependente));
            if(idCatUsuario==0){
                p.setNull(9, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(9, par.getSetParametro(idCatUsuario));    
            }
            if(qtUsuario==0){
                p.setNull(10, java.sql.Types.INTEGER);
            }else{
                p.setInt(10, par.getSetParametro(qtUsuario));    
            }
            if(vrMaxCarne==0){
                p.setNull(11, java.sql.Types.FLOAT);
                par.getSetNull();
            }else{
                p.setFloat(11, par.getSetParametro(vrMaxCarne));    
            }
            p.setString(12, par.getSetParametro(corTitular));
            p.setString(13, par.getSetParametro(corDependente));
            p.setInt(14, par.getSetParametro(id));
            
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
    
    public static Categoria getInstance(int id) {
        String sql = "SELECT " +
                            "CD_CATEGORIA ," +
                            "DESCR_CATEGORIA," +
                            "CASE WHEN STATUS_CATEGORIA = 'AT' THEN 'Ativa' ELSE 'Inativa' END STATUS_CATEGORIA, " +
                            "QT_RENOV_SD_CONV_GERAL ," +
                            "PZ_RENOVACAO_SD_CONV ," +
                            "DT_ULTIMA_RENOVACAO_SD_CONV ," +
                            "CASE WHEN TP_CATEGORIA = 'SO' THEN 'Sócio' ELSE 'Não Sócio' END TP_CATEGORIA," +
                            "CASE WHEN CD_IND_TRANSFERENCIA = 'S' THEN 'Sim' ELSE 'Não' END CD_IND_TRANSFERENCIA," +
                            "ABREV_CATEGORIA ," +
                            "CASE WHEN ADMITE_DEPENDENTE = 'S' THEN 'Sim' ELSE 'Não' END ADMITE_DEPENDENTE," +
                            "CD_CATEGORIA_USUARIO, " +
                            "(SELECT DESCR_CATEGORIA FROM TB_Categoria T0 WHERE T1.CD_CATEGORIA_USUARIO = T0.CD_CATEGORIA) DE_CATEGORIA_USUARIO," +
                            "QT_USUARIO_CATEGORIA ," +
                            "VR_MAX_CARNE, " +
                            "COR_TITULAR, " +
                            "COR_DEPENDENTE " +
                    "FROM TB_CATEGORIA T1 WHERE CD_cATEGORIA = ? ";
        
        Connection cn = null;
        Categoria i = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()) {
                i = new Categoria();
                i.setId(rs.getInt("CD_CATEGORIA"));
                i.setDescricao(rs.getString("DESCR_CATEGORIA"));
                i.setAbreviacao(rs.getString("ABREV_CATEGORIA"));
                i.setStatus(rs.getString("STATUS_CATEGORIA"));
                i.setTipo(rs.getString("TP_CATEGORIA"));
                i.setTituloTransferivel(rs.getString("CD_IND_TRANSFERENCIA"));
                i.setAdmiteDependente(rs.getString("ADMITE_DEPENDENTE"));
                i.setDeCatUsuario(rs.getString("DE_CATEGORIA_USUARIO"));
                i.setVrMaxCarne(rs.getFloat("VR_MAX_CARNE"));
                i.setIdCatUsuario(rs.getInt("CD_CATEGORIA_USUARIO"));
                i.setQtUsuario(rs.getInt("QT_USUARIO_CATEGORIA"));
                i.setQtConvite(rs.getInt("QT_RENOV_SD_CONV_GERAL"));
                i.setPrazoRenovacaoConvite(rs.getInt("PZ_RENOVACAO_SD_CONV"));
                i.corTitular = rs.getString("COR_TITULAR");
                i.corDependente = rs.getString("COR_DEPENDENTE");

            }
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        return i;
    }

    
    public void excluir(Auditoria audit) {
        String sql = "DELETE FROM TB_CATEGORIA WHERE CD_CATEGORIA = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, this.getId());
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, "" + this.getId());
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }

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
