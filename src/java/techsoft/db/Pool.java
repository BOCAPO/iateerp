
package techsoft.db;

import java.sql.Connection;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public final class Pool {
	
    private static final String DATASOURCE = "java:comp/env/jdbc/iate";
    private static final Pool pool = new Pool();
    private static final Logger log = Logger.getLogger("techsoft.db.Pool");

    /*
     * Nao eh permitido criar instancias desse objeto, todas as classes chamam
     * a mesma instancia por meio do metodo getInstance().
     */
    private Pool(){

    }

    public static Pool getInstance(){
        return pool;
    }

    public Connection getConnection(){
        Connection cn = null;
        try {
            InitialContext contexto = new InitialContext();
            DataSource dataSource = (DataSource) contexto.lookup(DATASOURCE);
            cn = dataSource.getConnection();
            cn.setAutoCommit(false);
        } catch (Exception e) {
            log.severe(e.getMessage());
        }

        return cn;
    }

}