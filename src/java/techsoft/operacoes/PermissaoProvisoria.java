package techsoft.operacoes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Logger;
import techsoft.db.DBUtil;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.db.SQLType;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.FuncionarioHorario;
import techsoft.tabelas.InserirException;

public class PermissaoProvisoria {
    
    private int id;
    private String nome;
    private String atividade;
    private Date inicio;
    private Date fim;
    private String estacionamento = "I";
    
        
    private FuncionarioHorario[] horarios = new FuncionarioHorario[7];
    
    private static final Logger log = Logger.getLogger("techsoft.controle.PermissaoProvisoria");
    
    private static final DBUtil.ObjectFactory<PermissaoProvisoria> builder = new DBUtil.ObjectFactory<PermissaoProvisoria>() {
        @Override
        public PermissaoProvisoria buildFrom(ResultSet rs) throws SQLException {
            PermissaoProvisoria f = new PermissaoProvisoria();
            f.id = rs.getInt("CD_AUTORIZACAO");
            f.nome = rs.getString("NOME_AUTORIZADO");
            f.setAtividade(rs.getString("DESCR_ATIVIDADE"));
            f.estacionamento = rs.getString("CD_ESTACIONAMENTO");
            f.inicio = rs.getDate("DT_INIC_TRAB");
            f.fim = rs.getDate("DT_FIM_TRAB");
            if(rs.getInt("TRAB_SEGUNDA") > 0){
                f.horarios[0] = new FuncionarioHorario(rs.getString("HH_ENTRA_SEGUNDA"), rs.getString("HH_SAI_SEGUNDA"));
            }
            if(rs.getInt("TRAB_TERCA") > 0){
                f.horarios[1] = new FuncionarioHorario(rs.getString("HH_ENTRA_TERCA"), rs.getString("HH_SAI_TERCA"));
            }
            if(rs.getInt("TRAB_QUARTA") > 0){
                f.horarios[2] = new FuncionarioHorario(rs.getString("HH_ENTRA_QUARTA"), rs.getString("HH_SAI_QUARTA"));
            }
            if(rs.getInt("TRAB_QUINTA") > 0){
                f.horarios[3] = new FuncionarioHorario(rs.getString("HH_ENTRA_QUINTA"), rs.getString("HH_SAI_QUINTA"));
            }
            if(rs.getInt("TRAB_SEXTA") > 0){
                f.horarios[4] = new FuncionarioHorario(rs.getString("HH_ENTRA_SEXTA"), rs.getString("HH_SAI_SEXTA"));
            }
            if(rs.getInt("TRAB_SABADO") > 0){
                f.horarios[5] = new FuncionarioHorario(rs.getString("HH_ENTRA_SABADO"), rs.getString("HH_SAI_SABADO"));
            }
            if(rs.getInt("TRAB_DOMINGO") > 0){
                f.horarios[6] = new FuncionarioHorario(rs.getString("HH_ENTRA_DOMINGO"), rs.getString("HH_SAI_DOMINGO"));
            }            
            return f;
        }
    };
    
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome.toUpperCase().trim();
    }

    public String getEstacionamento() {
        return estacionamento;
    }

    public void setEstacionamento(String estacionamento) {
        this.estacionamento = estacionamento;
    }
    public String getAtividade() {
        return atividade;
    }
    public void setAtividade(String atividade) {
        this.atividade = atividade.toUpperCase().trim();
    }
    public Date getInicio() {
        return inicio;
    }
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }
    public Date getFim() {
        return fim;
    }
    public void setFim(Date fim) {
        this.fim = fim;
    }
    public FuncionarioHorario[] getHorarios() {
        return horarios;
    }

    public static PermissaoProvisoria getInstance(int id){
        return DBUtil.queryObject(PermissaoProvisoria.builder, "SELECT * FROM TB_AUTOR_ESPECIAL WHERE CD_AUTORIZACAO = ?", SQLType.INTEGER(id));
    }
    
    
    public void inserir(Auditoria audit) throws InserirException{
        String sql = "INSERT INTO TB_AUTOR_ESPECIAL " +
                "(NOME_AUTORIZADO, DT_INIC_TRAB, DT_FIM_TRAB, " +
                "TRAB_SEGUNDA, HH_ENTRA_SEGUNDA, HH_SAI_SEGUNDA, " +
                "TRAB_TERCA, HH_ENTRA_TERCA, HH_SAI_TERCA, TRAB_QUARTA, " +
                "HH_ENTRA_QUARTA, HH_SAI_QUARTA, TRAB_QUINTA, HH_ENTRA_QUINTA, " +
                "HH_SAI_QUINTA, TRAB_SEXTA, HH_ENTRA_SEXTA, HH_SAI_SEXTA, " +
                "TRAB_SABADO, HH_ENTRA_SABADO, HH_SAI_SABADO, TRAB_DOMINGO, " +
                "HH_ENTRA_DOMINGO, HH_SAI_DOMINGO, DESCR_ATIVIDADE, CD_ESTACIONAMENTO)" +
                "VALUES (?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection cn = null;
        ParametroAuditoria par = new ParametroAuditoria();
        
        
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, par.getSetParametro(nome));
            if (inicio==null){
                p.setNull(2, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setDate(2, new java.sql.Date(par.getSetParametro(inicio.getTime())));
            }
            if (fim==null){
                p.setNull(3, java.sql.Types.DATE);
                par.getSetNull();
                
            }else{
                p.setDate(3, new java.sql.Date(par.getSetParametro(fim.getTime())));
            }
            
            int i = 4;
            for(int k = 0; k < 7; k++){
                if(horarios[k] != null){
                    p.setInt(i++, par.getSetParametro(1));
                    p.setString(i++, par.getSetParametro(horarios[k].getEntrada().replaceAll(":","")));
                    p.setString(i++, par.getSetParametro(horarios[k].getSaida().replaceAll(":","")));
                }else{
                    p.setInt(i++, par.getSetParametro(0));
                    p.setNull(i++, java.sql.Types.VARCHAR);
                    par.getSetNull();
                    p.setNull(i++, java.sql.Types.VARCHAR);
                    par.getSetNull();
                }
            }
            p.setString(25, par.getSetParametro(atividade));
            p.setString(26, par.getSetParametro(estacionamento));
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
            
            p = cn.prepareStatement("{call SP_ATUALIZA_NR_AUTORIZACAO}");
            ResultSet rs = p.executeQuery();
            cn.commit();
            
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    public void alterar(Auditoria audit)throws AlterarException{
        String sql = " UPDATE TB_AUTOR_ESPECIAL SET NOME_AUTORIZADO = ?,  "
           + "DT_INIC_TRAB = ?, DT_FIM_TRAB = ?, "
           + "TRAB_SEGUNDA = ?, HH_ENTRA_SEGUNDA = ?, HH_SAI_SEGUNDA = ?, "
           + "TRAB_TERCA = ?, HH_ENTRA_TERCA = ?, HH_SAI_TERCA = ?, "
           + "TRAB_QUARTA = ?, HH_ENTRA_QUARTA = ?, HH_SAI_QUARTA = ?, "
           + "TRAB_QUINTA = ?, HH_ENTRA_QUINTA = ?, HH_SAI_QUINTA = ?, "
           + "TRAB_SEXTA = ?, HH_ENTRA_SEXTA = ?, HH_SAI_SEXTA = ?, "
           + "TRAB_SABADO = ?, HH_ENTRA_SABADO = ?, HH_SAI_SABADO = ?, "
           + "TRAB_DOMINGO = ?, HH_ENTRA_DOMINGO = ?, HH_SAI_DOMINGO = ?, "
           + "DESCR_ATIVIDADE = ?, CD_ESTACIONAMENTO = ?  "
           + "WHERE CD_AUTORIZACAO = ?";

        Connection cn = null;
        ParametroAuditoria par = new ParametroAuditoria();
        
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, par.getSetParametro(nome));
            p.setDate(2, new java.sql.Date(par.getSetParametro(inicio.getTime())));
            p.setDate(3, new java.sql.Date(par.getSetParametro(fim.getTime())));
            
            int i = 4;
            for(int k = 0; k < 7; k++){
                if(horarios[k] != null){
                    p.setInt(i++, par.getSetParametro(1));
                    p.setString(i++, par.getSetParametro(horarios[k].getEntrada().replaceAll(":","")));
                    p.setString(i++, par.getSetParametro(horarios[k].getSaida().replaceAll(":","")));
                }else{
                    p.setInt(i++, par.getSetParametro(0));
                    p.setNull(i++, java.sql.Types.VARCHAR);
                    par.getSetNull();
                    p.setNull(i++, java.sql.Types.VARCHAR);
                    par.getSetNull();
                }
            }
            p.setString(25, par.getSetParametro(atividade));
            p.setString(26, par.getSetParametro(estacionamento));
            p.setInt(27, par.getSetParametro(id));
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    public void excluir(Auditoria audit){
        Connection cn = null;
                
        try {
            cn = Pool.getInstance().getConnection();
            String sql = "DELETE FROM TB_AUTOR_ESPECIAL WHERE CD_AUTORIZACAO  = ?";
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(id));
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
    }
    
    public void atualizarFoto(File f){

        Connection cn = null;
        BufferedInputStream bin = null;
        
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(
                "SELECT * FROM TB_FOTO_AUTORIZACAO WHERE CD_AUTORIZACAO = ?",
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            
            try{
                bin = new BufferedInputStream(new FileInputStream(f));
            }catch(FileNotFoundException e){
                log.severe(e.getMessage());
            }
                            
            if(rs.next()){
                rs.updateBinaryStream("FOTO_AUTORIZACAO", bin);
                rs.updateRow();
            }else{
                rs.moveToInsertRow();
                rs.updateInt("CD_AUTORIZACAO", id);
                rs.updateBinaryStream("FOTO_AUTORIZACAO", bin);
                rs.insertRow();
            }

            cn.commit();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
            try {
                bin.close();
            } catch (IOException e) {
                log.severe(e.getMessage());
            }
        }

    }        

    public void excluirFoto(){

        Connection cn = null;
        String sql =  "DELETE FROM TB_FOTO_AUTORIZACAO WHERE CD_AUTORIZACAO = ?";
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            p.executeUpdate();

            cn.commit();
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
