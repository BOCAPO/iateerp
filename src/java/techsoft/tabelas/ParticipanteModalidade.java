
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

public class ParticipanteModalidade {
    
    private static final Logger log = Logger.getLogger("techsoft.tabelas.ParticipanteModalidade");
    private String nome;
    private String titulo;
    
    public String getNome() {
        return nome;
    }

    public String getTitulo(){
        return titulo;
    }

    public static List<ParticipanteModalidade> listar(int cdModalidade){
        Connection cn = null;       
        
        ArrayList<ParticipanteModalidade> l = new ArrayList<ParticipanteModalidade>();
        
        String sql = "SELECT T1.NOME_PESSOA, CONVERT(VARCHAR, T1.CD_MATRICULA) + '/' +  CONVERT(VARCHAR, T1.CD_CATEGORIA) TITULO "
                   + "FROM TB_PESSOA T1, TB_MODALIDADE_TB_PESSOA T2 "
                   + "WHERE T1.CD_MATRICULA = T2.CD_MATRICULA " 
                   + "AND   T1.CD_CATEGORIA = T2.CD_CATEGORIA " 
                   + "AND   T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE " 
                   + "AND   T2.CD_MODALIDADE = ?";

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, cdModalidade);
            ResultSet rs = p.executeQuery();


            while (rs.next()) {


                ParticipanteModalidade d = new ParticipanteModalidade();
                d.nome = rs.getString(1);
                d.titulo = rs.getString(2);
                
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
    

}


