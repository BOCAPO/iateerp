package techsoft.cadastro;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class AutorizadoBarco {
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.AutorizadoBarco");

    private int id;
    private String nome;
    private String obs;
    private SocioBarco barco;

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public SocioBarco getSocioBarco() {
        return barco;
    }

    public void setSocioBarco(SocioBarco barco) {
        this.barco = barco;
    }
        
    public static List<AutorizadoBarco> listar(SocioBarco barco){

        ArrayList<AutorizadoBarco> l = new ArrayList<AutorizadoBarco>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_PESSOA_AUTOR_BARCO ('C', NULL, ?)}");
            cal.setInt(1, barco.getId());

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                AutorizadoBarco b = new AutorizadoBarco();
                
                b.id = rs.getInt("CD_AUTORIZACAO");
                b.nome = rs.getString("NO_PESSOA");
                b.obs = rs.getString("DE_OBSERVACAO");
                l.add(b);
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
    
    public static AutorizadoBarco getInstance(int id){
        AutorizadoBarco b = null;
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            ResultSet rs = cn.createStatement().executeQuery("SELECT * FROM TB_PESSOA_AUTOR_BARCO WHERE CD_AUTORIZACAO = " + id);
            while (rs.next()) {
                b = new AutorizadoBarco();
                
                b.id = rs.getInt("CD_AUTORIZACAO");
                b.nome = rs.getString("NO_PESSOA");
                b.obs = rs.getString("DE_OBSERVACAO");
                b.barco = SocioBarco.getInstance(rs.getInt("CD_BARCO"));
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

        return b;
    }
    
    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            CallableStatement cal = cn.prepareCall("{call SP_PESSOA_AUTOR_BARCO ('E', ?)}");
            cal.setInt(1, id);
            cal.executeUpdate();
            cn.commit();
            cn.close();
            audit.registrarMudanca("{call SP_PESSOA_AUTOR_BARCO ('E', ?)}", String.valueOf(id));
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }    
    
    public void alterar(Auditoria audit){

        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_PESSOA_AUTOR_BARCO ('A', ?, ?, ?, ?)}");
            ParametroAuditoria par = new ParametroAuditoria();
            cal.setInt(1, par.getSetParametro(id));
            cal.setInt(2, par.getSetParametro(barco.getId()));
            cal.setString(3, par.getSetParametro(nome));
            cal.setString(4, par.getSetParametro(obs));

            cal.executeUpdate();
            cn.commit();
            cn.close();

            audit.registrarMudanca("{call SP_PESSOA_AUTOR_BARCO ('A', ?, ?, ?, ?)}", par.getParametroFinal());
        } catch (SQLException e) {
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
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_PESSOA_AUTOR_BARCO ('I', NULL, ?, ?, ?)}");
            ParametroAuditoria par = new ParametroAuditoria();
            cal.setInt(1, par.getSetParametro(barco.getId()));
            cal.setString(2, par.getSetParametro(nome));
            cal.setString(3, par.getSetParametro(obs));

            cal.executeUpdate();
            cn.commit();
            cn.close();

            audit.registrarMudanca("call SP_PESSOA_AUTOR_BARCO 'I' ", par.getParametroFinal());
        } catch (SQLException e) {
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
