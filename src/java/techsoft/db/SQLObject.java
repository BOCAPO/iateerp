package techsoft.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLObject {
    
    private final SQLType type;
    private final Object object;
    
    public SQLObject(SQLType type, Object object) {
        this.type = type;
        this.object = object;
    }
    
    public void set(PreparedStatement stmt, int i) throws SQLException {
        if (this.object == null) stmt.setNull(i + 1, this.type.JDBCType());
        else stmt.setObject(i + 1, this.object, this.type.JDBCType());
    }
    
    @Override
    public String toString() { return (this.object == null) ? "null" : this.object.toString(); }
}
