package techsoft.tabelas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.cadastro.Socio;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;

public class Armario {
    private int numero;
    private int tipo;
    private Socio socio;
    private Date dataInclusao;
    private static final Logger log = Logger.getLogger("techsoft.tabelas.Armario");

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }
    
    public static List<Armario> listarVinculados(Socio s){
        ArrayList<Armario> l = new ArrayList<Armario>();
        
        String sql = "SELECT T1.NU_ARMARIO, "
              + "T1.DT_INCLUSAO, "
              + "T2.CD_MATRICULA, "
              + "T2.CD_CATEGORIA, "
              + "T2.NOME_PESSOA, "
              + "T1.NU_TIPO_ARMARIO "
       + "FROM TB_ARMARIO T1, "
            + "TB_PESSOA T2 "
       + "WHERE T1.CD_MATRICULA = T2.CD_MATRICULA "
       + " AND  T1.CD_CATEGORIA = T2.CD_CATEGORIA "
       + " AND  T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE "
       + " AND  T1.CD_MATRICULA = ?"
       + " AND  T1.CD_CATEGORIA = ?"
       + " AND  T1.SEQ_DEPENDENTE = ?";

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, s.getMatricula());
            p.setInt(2, s.getIdCategoria());
            p.setInt(3, s.getSeqDependente());
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Armario a = new Armario();
                a.numero = rs.getInt(1);
                a.dataInclusao = rs.getDate(2);
                a.tipo = rs.getInt(6);
                a.socio = s;

                l.add(a);
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
        
    public static List<Armario> listarTodos(int tipoArmario){
        ArrayList<Armario> l = new ArrayList<Armario>();
        
        if(tipoArmario < 1){
            tipoArmario = 1;
        }
        
        String sql = "SELECT T1.NU_ARMARIO, "
              + "T1.DT_INCLUSAO, "
              + "T2.CD_MATRICULA, "
              + "T2.CD_CATEGORIA, "
              + "T3.DESCR_CATEGORIA, "
              + "T2.NOME_PESSOA "
       + "FROM TB_ARMARIO T1, "
            + "TB_PESSOA T2, "
            + "TB_CATEGORIA T3 "
       + "WHERE T1.CD_MATRICULA *= T2.CD_MATRICULA "
       + " AND  T1.CD_CATEGORIA *= T2.CD_CATEGORIA "
       + " AND  T1.SEQ_DEPENDENTE *= T2.SEQ_DEPENDENTE "
       + " AND  T1.CD_CATEGORIA *= T3.CD_CATEGORIA "                
       + " AND  T1.NU_TIPO_ARMARIO = "
       + String.valueOf(tipoArmario);

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Armario a = new Armario();
                Socio s = null;
                a.numero = rs.getInt(1);
                a.dataInclusao = rs.getDate(2);
                rs.getInt(3);
                if(!rs.wasNull()){
                    s = new Socio();
                    s.setMatricula(rs.getInt(3));
                    s.setIdCategoria(rs.getInt(4));
                    s.setCategoria(rs.getString(5));
                    s.setNome(rs.getString(6));
                }
                a.socio = s;
                a.tipo = tipoArmario;

                l.add(a);
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
    
    public static Armario getInstance(int numero, int tipo){
        Armario a = null;
        String sql = "SELECT T1.NU_ARMARIO, "
              + "T1.DT_INCLUSAO, "
              + "T2.CD_MATRICULA, "
              + "T2.CD_CATEGORIA, "
              + "T2.NOME_PESSOA "
       + "FROM TB_ARMARIO T1, "
            + "TB_PESSOA T2 "
       + "WHERE T1.CD_MATRICULA *= T2.CD_MATRICULA "
       + " AND  T1.CD_CATEGORIA *= T2.CD_CATEGORIA "
       + " AND  T1.SEQ_DEPENDENTE *= T2.SEQ_DEPENDENTE "
       + " AND  T1.NU_ARMARIO = ? AND T1.NU_TIPO_ARMARIO = ?";

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, numero);
            p.setInt(2, tipo);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                a = new Armario();
                Socio s = null;
                a.numero = rs.getInt(1);
                a.dataInclusao = rs.getDate(2);
                rs.getInt(3);
                if(!rs.wasNull()){
                    s = new Socio();
                    s.setMatricula(rs.getInt(3));
                    s.setIdCategoria(rs.getInt(4));
                    s.setNome(rs.getString(5));
                }
                a.socio = s;
                a.tipo = tipo;
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

        return a;
    }

    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_ARMARIO WHERE NU_ARMARIO = ? AND NU_TIPO_ARMARIO = ?";
           
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, numero);
            p.setInt(2, tipo);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(numero), String.valueOf(tipo));
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }    

    public void inserir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "INSERT INTO TB_ARMARIO VALUES (? , ? , NULL, NULL, NULL, CURRENT_TIMESTAMP)";
           
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, numero);
            p.setInt(2, tipo);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(numero), String.valueOf(tipo));
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }    
        
    public void desvincularSocioAtual(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "UPDATE TB_ARMARIO "
            + "SET CD_MATRICULA = NULL "
            + ",   CD_CATEGORIA = NULL "
            + ", SEQ_DEPENDENTE = NULL "
            + "WHERE NU_ARMARIO = ? AND NU_TIPO_ARMARIO = ?";

           
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, numero);
            p.setInt(2, tipo);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(numero), String.valueOf(tipo));
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }    
    
    public void vincular(Socio s, Auditoria audit){
        Connection cn = null;

        try {
            String sql = "UPDATE TB_ARMARIO SET CD_MATRICULA = ?, CD_CATEGORIA = ?, SEQ_DEPENDENTE = 0"
                    + "WHERE NU_ARMARIO = ? AND NU_TIPO_ARMARIO = ?";
           
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, s.getMatricula());
            p.setInt(2, s.getIdCategoria());            
            p.setInt(3, numero);
            p.setInt(4, tipo);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(s.getMatricula()), String.valueOf(s.getIdCategoria()), String.valueOf(numero), String.valueOf(tipo));
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }

            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }
    
}
