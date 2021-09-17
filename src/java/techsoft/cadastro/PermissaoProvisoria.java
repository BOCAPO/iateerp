package techsoft.cadastro;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.cadastro.Socio;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;

public class PermissaoProvisoria implements Serializable{
    
    private String nome;
    private Date dataInicio;
    private Date dataFim;
    private String atividade;
    private int numero;
    
    private static final Logger log = Logger.getLogger("techsoft.cadastro.PermissaoProvisoria");
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Date getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }
    public Date getDataFim() {
        return dataFim;
    }
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
    public String getAtividade() {
        return atividade;
    }
    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }
    public int getNumero() {
        return numero;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }

    public static List<PermissaoProvisoria> consultar(){

        ArrayList<PermissaoProvisoria> l = new ArrayList<PermissaoProvisoria>();
        Connection cn = null;
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        Date data = new Date(); //data atual

        String sql = "SELECT " +
                            "T1.NOME_AUTORIZADO, " +
                            "T1.DT_INIC_TRAB, " +
                            "T1.DT_FIM_TRAB, " +
                            "T1.DESCR_ATIVIDADE, " +
                            "T1.NR_AUTORIZACAO " +
                      "FROM " +
                         "TB_AUTOR_ESPECIAL T1 " +
                      "WHERE " +
                            "T1.NR_AUTORIZACAO IS NOT NULL AND " +
                            "T1.DT_INIC_TRAB <= '" + sd.format(data) + "' AND " +
                            "T1.DT_FIM_TRAB >= '" + sd.format(data) + "' " + 
                      "ORDER BY " +
                            "NOME_AUTORIZADO ";

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                PermissaoProvisoria s = new PermissaoProvisoria();
                s.setNome(rs.getString("NOME_AUTORIZADO"));
                s.setAtividade(rs.getString("DESCR_ATIVIDADE"));
                s.setNumero(rs.getInt("NR_AUTORIZACAO"));
                s.setDataInicio(rs.getDate("DT_INIC_TRAB"));
                s.setDataFim(rs.getDate("DT_FIM_TRAB"));
                
                l.add(s);
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
