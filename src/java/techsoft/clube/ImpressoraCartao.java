
package techsoft.clube;

import java.sql.CallableStatement;
import techsoft.tabelas.InserirException;
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
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.ExcluirException;

public class ImpressoraCartao {

    private int id;
    private String descricao;
    private String ip;
    private int porta;
            
    private static final Logger log = Logger.getLogger("techsoft.clube.ImpressoraCartao");

    public int getId(){
        return id;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getIp() {
        return ip;
    }
    public int getPorta() {
        return porta;
    }

    public static ImpressoraCartao getInstance(int id){
        ImpressoraCartao d = null;
        CallableStatement cal = null;
        Connection cn = null;
        cn = Pool.getInstance().getConnection();

        try {
            cal = cn.prepareCall("SELECT NU_SEQ_IMPRESSORA, DE_IMPRESSORA, CD_IP, CD_PORTA FROM TB_IMPRESSORA_CARTAO WHERE NU_SEQ_IMPRESSORA = ?");
            cal.setInt(1, id);
            ResultSet rs = cal.executeQuery();
            if(rs.next()){
                d = new ImpressoraCartao();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.ip = rs.getString(3);
                d.porta = rs.getInt(4);
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