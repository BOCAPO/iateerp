package techsoft.db;

public enum SQLType {
        ARRAY(java.sql.Types.ARRAY),
        BIGINT(java.sql.Types.BIGINT),
        BINARY(java.sql.Types.BINARY),
        BIT(java.sql.Types.BIT),
        BLOB(java.sql.Types.BLOB),
        BOOLEAN(java.sql.Types.BOOLEAN),
        CHAR(java.sql.Types.CHAR),
        CLOB(java.sql.Types.CLOB),
        DATALINK(java.sql.Types.DATALINK),
        DATE(java.sql.Types.DATE),
        DECIMAL(java.sql.Types.DECIMAL),
        DISTINCT(java.sql.Types.DISTINCT),
        DOUBLE(java.sql.Types.DOUBLE),
        FLOAT(java.sql.Types.FLOAT),
        INTEGER(java.sql.Types.INTEGER),
        JAVA_OBJECT(java.sql.Types.JAVA_OBJECT),
        LONGNVARCHAR(java.sql.Types.LONGNVARCHAR),
        LONGVARBINARY(java.sql.Types.LONGVARBINARY),
        LONGVARCHAR(java.sql.Types.LONGVARCHAR),
        NCHAR(java.sql.Types.NCHAR),
        NCLOB(java.sql.Types.NCLOB),
        NULL(java.sql.Types.NULL),
        NUMERIC(java.sql.Types.NUMERIC),
        NVARCHAR(java.sql.Types.NVARCHAR),
        OTHER(java.sql.Types.OTHER),
        REAL(java.sql.Types.REAL),
        REF(java.sql.Types.REF),
        ROWID(java.sql.Types.ROWID),
        SMALLINT(java.sql.Types.SMALLINT),
        SQLXML(java.sql.Types.SQLXML),
        STRUCT(java.sql.Types.STRUCT),
        TIME(java.sql.Types.TIME),
        TIMESTAMP(java.sql.Types.TIMESTAMP),
        TINYINT(java.sql.Types.TINYINT),
        VARBINARY(java.sql.Types.VARBINARY),
        VARCHAR(java.sql.Types.VARCHAR);

        private final int type;
        private SQLType(int type) { this.type = type; }
        public int JDBCType() { return this.type; }
        
        public static SQLObject ARRAY(Object obj) { return new SQLObject(SQLType.ARRAY, obj); }
        public static SQLObject BIGINT(Object obj) { return new SQLObject(SQLType.BIGINT, obj); }
        public static SQLObject BINARY(Object obj) { return new SQLObject(SQLType.BINARY, obj); }
        public static SQLObject BIT(Object obj) { return new SQLObject(SQLType.BIT, obj); }
        public static SQLObject BLOB(Object obj) { return new SQLObject(SQLType.BLOB, obj); }
        public static SQLObject BOOLEAN(Object obj) { return new SQLObject(SQLType.BOOLEAN, obj); }
        public static SQLObject CHAR(Object obj) { return new SQLObject(SQLType.CHAR, obj); }
        public static SQLObject CLOB(Object obj) { return new SQLObject(SQLType.CLOB, obj); }
        public static SQLObject DATALINK(Object obj) { return new SQLObject(SQLType.DATALINK, obj); }
        public static SQLObject DATE(Object obj) { return new SQLObject(SQLType.DATE, obj); }
        public static SQLObject DECIMAL(Object obj) { return new SQLObject(SQLType.DECIMAL, obj); }
        public static SQLObject DISTINCT(Object obj) { return new SQLObject(SQLType.DISTINCT, obj); }
        public static SQLObject DOUBLE(Object obj) { return new SQLObject(SQLType.DOUBLE, obj); }
        public static SQLObject FLOAT(Object obj) { return new SQLObject(SQLType.FLOAT, obj); }
        public static SQLObject INTEGER(Object obj) { return new SQLObject(SQLType.INTEGER, obj); }
        public static SQLObject JAVA_OBJECT(Object obj) { return new SQLObject(SQLType.JAVA_OBJECT, obj); }
        public static SQLObject LONGNVARCHAR(Object obj) { return new SQLObject(SQLType.LONGNVARCHAR, obj); }
        public static SQLObject LONGVARBINARY(Object obj) { return new SQLObject(SQLType.LONGVARBINARY, obj); }
        public static SQLObject LONGVARCHAR(Object obj) { return new SQLObject(SQLType.LONGVARCHAR, obj); }
        public static SQLObject NCHAR(Object obj) { return new SQLObject(SQLType.NCHAR, obj); }
        public static SQLObject NCLOB(Object obj) { return new SQLObject(SQLType.NCLOB, obj); }
        public static SQLObject NULL(Object obj) { return new SQLObject(SQLType.NULL, obj); }
        public static SQLObject NUMERIC(Object obj) { return new SQLObject(SQLType.NUMERIC, obj); }
        public static SQLObject NVARCHAR(Object obj) { return new SQLObject(SQLType.NVARCHAR, obj); }
        public static SQLObject OTHER(Object obj) { return new SQLObject(SQLType.OTHER, obj); }
        public static SQLObject REAL(Object obj) { return new SQLObject(SQLType.REAL, obj); }
        public static SQLObject REF(Object obj) { return new SQLObject(SQLType.REF, obj); }
        public static SQLObject ROWID(Object obj) { return new SQLObject(SQLType.ROWID, obj); }
        public static SQLObject SMALLINT(Object obj) { return new SQLObject(SQLType.SMALLINT, obj); }
        public static SQLObject SQLXML(Object obj) { return new SQLObject(SQLType.SQLXML, obj); }
        public static SQLObject STRUCT(Object obj) { return new SQLObject(SQLType.STRUCT, obj); }
        public static SQLObject TIME(Object obj) { return new SQLObject(SQLType.TIME, obj); }
        public static SQLObject TIMESTAMP(Object obj) { return new SQLObject(SQLType.TIMESTAMP, obj); }
        public static SQLObject TINYINT(Object obj) { return new SQLObject(SQLType.TINYINT, obj); }
        public static SQLObject VARBINARY(Object obj) { return new SQLObject(SQLType.VARBINARY, obj); }
        public static SQLObject VARCHAR(Object obj) { return new SQLObject(SQLType.VARCHAR, obj); }
        
}
