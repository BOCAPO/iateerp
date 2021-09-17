
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

public class TipoConvite {

    private String id;
    private String descricao;
    private static final Logger log = Logger.getLogger("techsoft.tabelas.TipoConvite");

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }

    public String getId(){
        return id;
    }

    public static List<TipoConvite> listar(){
        ArrayList<TipoConvite> l = new ArrayList<TipoConvite>();
        String sql = "SELECT * FROM TB_TIPO_CONVITE ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                TipoConvite d = new TipoConvite();
                d.id = rs.getString(1);
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

    public static TipoConvite getInstance(String id){
        TipoConvite d = null;
        String sql = "SELECT * FROM TB_TIPO_CONVITE WHERE CD_TIPO_CONVITE = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new TipoConvite();
                d.id = rs.getString(1);
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

}
