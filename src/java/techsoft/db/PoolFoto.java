package techsoft.db;

import java.sql.Connection;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public final class PoolFoto {
    
    private static final String DATASOURCE = "java:comp/env/jdbc/foto";
    private static final PoolFoto pool = new PoolFoto();
    private static final Logger log = Logger.getLogger(PoolFoto.class.getCanonicalName());

    /*
     * Nao eh permitido criar instancias desse objeto, todas as classes chamam
     * a mesma instancia por meio do metodo getInstance().
     */
    private PoolFoto(){

    }

    public static PoolFoto getInstance(){
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
