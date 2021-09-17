
package techsoft.cadastro;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;

public final class ComboBoxLoader implements Serializable{

    private int id;
    private String cod;
    private String descricao;
    private static final Logger log = Logger.getLogger("techsoft.cadastro.ComboBoxLoader");

    public ComboBoxLoader(int id, String descricao, String cod){
        this.id = id;
        this.cod = cod;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static List<ComboBoxLoader> listar(String entidade){
        ArrayList<ComboBoxLoader> l = new ArrayList<ComboBoxLoader>();
        entidade = entidade.replaceAll(";", "");//evitar insecao de sql
        //entidade = entidade.replaceAll("'", "");//evitar insecao de sql
        String sql = "SELECT * FROM " + entidade + " ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                try{
                    l.add(new ComboBoxLoader(rs.getInt(1), rs.getString(2), rs.getString(1)));
                } catch (SQLException e) {
                    l.add(new ComboBoxLoader(0, rs.getString(2), rs.getString(1)));
                }
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
    public static List<ComboBoxLoader> listarSql(String sql){
        ArrayList<ComboBoxLoader> l = new ArrayList<ComboBoxLoader>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                try{
                    l.add(new ComboBoxLoader(rs.getInt(1), rs.getString(2), rs.getString(1)));
                } catch (SQLException e) {
                    l.add(new ComboBoxLoader(0, rs.getString(2), rs.getString(1)));
                }
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
