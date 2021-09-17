package techsoft.controle.relatorio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import techsoft.db.Pool;

public class Emiate {
    private static final Logger log = Logger.getLogger("techsoft.controle.relatorio.Emiate");
    
    /**
     * Gera uma cópia preenchida do arquivo MDB de modelo do EMIATE
     * @param sql Consulta para selecionar os registros que serão preenchidos.
     * @param modelo File apontando para o arquivo de modelo do EMIATE.
     * @return File apontando para a copia do MDB preenchido.
     */
    public static File gerar(String sql, File modelo) throws SQLException, IOException{
        File copia = File.createTempFile("emiate", "tmp");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(modelo));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(copia));
        byte[] buf = new byte[4096];
        
        int i = 0;
        while((i = in.read(buf)) > 0){
            out.write(buf, 0, i);
        }
        out.close();
        in.close();

        Connection cnMdb = null;
        Connection cn = null;
        
        try{
            cnMdb = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + copia.getCanonicalPath(), "", "");
            cn = Pool.getInstance().getConnection();
            ResultSet rsMdb = cnMdb.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT * FROM TB_GERAL");
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while(rs.next()){
                rsMdb.moveToInsertRow();
                rsMdb.updateString(1, rs.getString(1));
                rsMdb.updateInt(2, rs.getInt(2));
                rsMdb.updateString(3, rs.getString(3));
                rsMdb.updateDate(4, rs.getDate(4));
                rsMdb.updateString(5, rs.getString(5));
                rsMdb.updateString(6, rs.getString(6));
                rsMdb.updateString(7, rs.getString(7));
                rsMdb.updateString(8, rs.getString(8));
                rsMdb.updateString(9, rs.getString(9));
                rsMdb.updateString(10, rs.getString(10));
                rsMdb.updateString(11, rs.getString(11));
                rsMdb.insertRow();
            }
        }finally{
            cn.close();
            cnMdb.close();
        }
        
        return copia;
    }
}
