
package techsoft.seguranca;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;

public class Permissao {

    private int id;
    private String descricao;
    private static final Logger log = Logger.getLogger("techsoft.seguranca.Permissao");

    //soh a classe grupo deve instanciar Permissoes
    Permissao(int id, String descricao){
        this.id = id;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static List<Permissao> listar(){
        ArrayList<Permissao> l = new ArrayList<Permissao>();
        String sql = "SELECT * FROM TB_CADASTRO_APLICACAO ORDER BY 1";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                l.add(new Permissao(rs.getInt(1), rs.getString(2)));
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

    public static List<Permissao> listarInternet(){
        ArrayList<Permissao> l = new ArrayList<Permissao>();
        String sql = "SELECT * FROM TB_APLICACAO_INTERNET ORDER BY 1";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                l.add(new Permissao(rs.getInt(1), rs.getString(2)));
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
    
}
