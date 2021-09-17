package techsoft.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.seguranca.Auditoria;

public class DBUtil {
    
    private static final Logger log = Logger.getLogger(DBUtil.class.getName());

    public interface ObjectFactory<T> {
        /**
         * Constrói um objeto a partir de um {@link java.sql.ResultSet}.
         */
        T buildFrom(ResultSet rs) throws SQLException;
    }
    
    private static void log(Exception e) {
        StringWriter stack = new StringWriter();
        e.printStackTrace(new PrintWriter(stack));
        log.severe(stack.toString());
    }
    
    public static <T> List<T> queryList(DBUtil.ObjectFactory<T> factory, String sql, List<SQLObject> parms) {
        return DBUtil.queryList(factory, sql, parms.toArray(new SQLObject[parms.size()]));
    }
    
    public static <T> List<T> queryList(DBUtil.ObjectFactory<T> factory, String sql, SQLObject... parms) {
        List<T> list = new ArrayList<T>();
        
        Connection conn = Pool.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);    
            try {
                for (int i = 0; i < parms.length; i++) parms[i].set(stmt, i);
                ResultSet rs = stmt.executeQuery();
                try {
                    while (rs.next()) list.add(factory.buildFrom(rs));
                } catch (SQLException ex) {
                    DBUtil.log(ex);
                } finally {
                    try { rs.close(); } catch (SQLException ex) { DBUtil.log(ex); }
                }
            } catch (SQLException ex) {
                DBUtil.log(ex);
            } finally {
                try { stmt.close(); } catch (SQLException ex) { DBUtil.log(ex); }
            }
        } catch (SQLException ex) {
            DBUtil.log(ex);
        } finally {
            try { conn.close(); } catch (SQLException ex) { DBUtil.log(ex); }
        }
        
        return list;
    }
    
    
    public static <T> T queryObject(DBUtil.ObjectFactory<T> factory, String sql, List<SQLObject> parms) {
        return DBUtil.queryObject(factory, sql, parms.toArray(new SQLObject[parms.size()]));
    }
    
    public static <T> T queryObject(DBUtil.ObjectFactory<T> factory, String sql, SQLObject... parms) {
        T obj = null;
        
        Connection conn = Pool.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);    
            try {
                for (int i = 0; i < parms.length; i++) parms[i].set(stmt, i);
                ResultSet rs = stmt.executeQuery();
                try {
                    if (rs.next()) obj = factory.buildFrom(rs);
                } catch (SQLException ex) {
                    DBUtil.log(ex);
                } finally {
                    try { rs.close(); } catch (SQLException ex) { DBUtil.log(ex); }
                }
            } catch (SQLException ex) {
                DBUtil.log(ex);
            } finally {
                try { stmt.close(); } catch (SQLException ex) { DBUtil.log(ex); }
            }
        } catch (SQLException ex) {
            DBUtil.log(ex);
        } finally {
            try { conn.close(); } catch (SQLException ex) { DBUtil.log(ex); }
        }

        return obj;
    }

    
    public static int queryCount(String sql, List<SQLObject> parms) {
        return DBUtil.queryCount(sql, parms.toArray(new SQLObject[parms.size()]));
    }
    
    public static int queryCount(String sql, SQLObject... parms) {
        int count = 0;
        
        Connection conn = Pool.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);    
            try {
                for (int i = 0; i < parms.length; i++) parms[i].set(stmt, i);
                ResultSet rs = stmt.executeQuery();
                try {
                    if (rs.next()) count = rs.getInt(1);
                } catch (SQLException ex) {
                    DBUtil.log(ex);
                } finally {
                    try { rs.close(); } catch (SQLException ex) { DBUtil.log(ex); }
                }
            } catch (SQLException ex) {
                DBUtil.log(ex);
            } finally {
                try { stmt.close(); } catch (SQLException ex) { DBUtil.log(ex); }
            }
        } catch (SQLException ex) {
            DBUtil.log(ex);
        } finally {
            try { conn.close(); } catch (SQLException ex) { DBUtil.log(ex); }
        }

        return count;
    }
    
    
    public static void update(Auditoria audit, String sql, List<SQLObject> parms) {
        DBUtil.update(audit, sql, parms.toArray(new SQLObject[parms.size()]));
    }
    
    public static void update(Auditoria audit, String sql, SQLObject... parms) {
        Connection conn = Pool.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            boolean success = true;
            try {
                for (int i = 0; i < parms.length; i++) parms[i].set(stmt, i);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                DBUtil.log(ex);
                success = false;
            } finally {
                try { stmt.close(); } catch (SQLException ex) { DBUtil.log(ex); }
            }
            if (success) {
                conn.commit();
                if (audit != null) {
                    List<String> p = new ArrayList<String>();
                    for (SQLObject o : parms) p.add(o.toString());
                    audit.registrarMudanca(sql, p.toArray(new String[p.size()]));
                }
            }
        } catch (SQLException ex) {
            DBUtil.log(ex);
        } finally {
            try { conn.close(); } catch (SQLException ex) { DBUtil.log(ex); }
        }
    }
    
    
    public static void update(String sql, List<SQLObject> parms) {
        DBUtil.update(sql, parms.toArray(new SQLObject[parms.size()]));
    }
    
    public static void update(String sql, SQLObject... parms) {
        DBUtil.update(null, sql, parms);
    }

    
    public static int updateGenerateKey(Auditoria audit, String sql, List<SQLObject> parms) {
        return DBUtil.updateGenerateKey(audit, sql, parms.toArray(new SQLObject[parms.size()]));
    }
    
    public static int updateGenerateKey(Auditoria audit, String sql, SQLObject... parms) {
        int key = 0;
        Connection conn = Pool.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            boolean success = true;
            try {
                for (int i = 0; i < parms.length; i++) parms[i].set(stmt, i);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                try {
                    if (rs.next()) key = rs.getInt(1);
                    else success = false;
                } catch (SQLException ex) {
                    DBUtil.log(ex);
                    success = false;
                } finally {
                    try { rs.close(); } catch (SQLException ex) { DBUtil.log(ex); }
                }
            } catch (SQLException ex) {
                DBUtil.log(ex);
                success = false;
            } finally {
                try { stmt.close(); } catch (SQLException ex) { DBUtil.log(ex); }
            }
            if (success) {
                conn.commit();
                if (audit != null) {
                    List<String> p = new ArrayList<String>();
                    for (SQLObject o : parms) p.add(o.toString());
                    audit.registrarMudanca(sql, p.toArray(new String[p.size()]));
                }
            }
        } catch (SQLException ex) {
            DBUtil.log(ex);
        } finally {
            try { conn.close(); } catch (SQLException ex) { DBUtil.log(ex); }
        }
        return key;
    }
    
    
    public static int updateGenerateKey(String sql, List<SQLObject> parms) {
        return DBUtil.updateGenerateKey(sql, parms.toArray(new SQLObject[parms.size()]));
    }
    
    public static int updateGenerateKey(String sql, SQLObject... parms) {
        return DBUtil.updateGenerateKey(null, sql, parms);
    }

    public static void execute(Auditoria audit, String sql, SQLObject... parms) {
        Connection conn = Pool.getInstance().getConnection();
        try {
            CallableStatement stmt = conn.prepareCall(sql);
            boolean success = true;
            try {
                for (int i = 0; i < parms.length; i++) parms[i].set(stmt, i);
                stmt.execute();
            } catch (SQLException ex) {
                DBUtil.log(ex);
                success = false;
            } finally {
                try { stmt.close(); } catch (SQLException ex) { DBUtil.log(ex); }
            }
            if (success) {
                conn.commit();
                if (audit != null) {
                    List<String> p = new ArrayList<String>();
                    for (SQLObject o : parms) p.add(o.toString());
                    audit.registrarMudanca(sql, p.toArray(new String[p.size()]));
                }
            }
        } catch (SQLException ex) {
            DBUtil.log(ex);
        } finally {
            try { conn.close(); } catch (SQLException ex) { DBUtil.log(ex); }
        }
    }

}
