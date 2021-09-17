package techsoft.operacoes;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.InserirException;

public class ReservaDependencia { 
    private int numero;
    private int categoriaSocio;
    private int matricula;
    private int dependente;
    private int dependencia;
    private Date dtInicio;
    private Date dtFim;
    private Date dtLimConf;
    private String interessado;
    private String deDependencia;
    private String status;
    private String telefone;
    private Date dtConfirmacao;
    private Date dtReserva;
    private String usuarioCanc;
    private String descricaoCanc;
    private String nomeContato;
    private String telContato;
    private int publico;
    private int tipoEvento;
    
    public int getPublico() {
        return publico;
    }
    public void setPublico(int publico) {
        this.publico = publico;
    }
    public int getTipoEvento() {
        return tipoEvento;
    }
    public void setTipoEvento(int tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
    public int getNumero() {
        return numero;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }
    public int getCategoriaSocio() {
        return categoriaSocio;
    }
    public void setCategoriaSocio(int categoriaSocio) {
        this.categoriaSocio = categoriaSocio;
    }
    public int getMatricula() {
        return matricula;
    }
    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
    public int getDependente() {
        return dependente;
    }
    public void setDependente(int dependente) {
        this.dependente = dependente;
    }
    public int getDependencia() {
        return dependencia;
    }
    public void setDependencia(int dependencia) {
        this.dependencia = dependencia;
    }
    public Date getDtInicio() {
        return dtInicio;
    }
    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }
    public Date getDtFim() {
        return dtFim;
    }
    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }
    public Date getDtLimConf() {
        return dtLimConf;
    }
    public void setDtLimConf(Date dtLimConf) {
        this.dtLimConf = dtLimConf;
    }
    public String getInteressado() {
        return interessado;
    }
    public void setInteressado(String interessado) {
        this.interessado = interessado;
    }
    public String getDeDependencia() {
        return deDependencia;
    }
    public void setDeDependencia(String deDependencia) {
        this.deDependencia = deDependencia;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public Date getDtConfirmacao() {
        return dtConfirmacao;
    }
    public void setDtConfirmacao(Date dtConfirmacao) {
        this.dtConfirmacao = dtConfirmacao;
    }
    public Date getDtReserva() {
        return dtReserva;
    }
    public void setDtReserva(Date dtReserva) {
        this.dtReserva = dtReserva;
    }
    public String getUsuarioCanc() {
        return usuarioCanc;
    }
    public void setUsuarioCanc(String usuarioCanc) {
        this.usuarioCanc = usuarioCanc;
    }
    public String getNomeContato() {
        return nomeContato;
    }
    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }
    public String getTelContato() {
        return telContato;
    }
    public void setTelContato(String telContato) {
        this.telContato = telContato;
    }
    public String getDescricaoCanc() {
        return descricaoCanc;
    }
    public void setDescricaoCanc(String descricaoCanc) {
        this.descricaoCanc = descricaoCanc;
    }
    public static int getQtDiasMes(int mes, int ano){
        Calendar cal = new GregorianCalendar(ano, mes-1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH); 
    }
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.ReservaDependencia");

    public static ReservaDependencia getInstance(int id){
        ReservaDependencia d = null;
        String sql = "SELECT * FROM TB_RESERVA_DEPENDENCIA WHERE SEQ_RESERVA = ? ";

        Connection cn = null;
        
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new ReservaDependencia();
                
                
                d.setNumero(rs.getInt("SEQ_RESERVA"));
                d.setCategoriaSocio(rs.getInt("CD_CATEGORIA"));
                d.setMatricula(rs.getInt("CD_MATRICULA"));
                d.setDependente(rs.getInt("SEQ_DEPENDENTE"));
                d.setDependencia(rs.getInt("SEQ_DEPENDENCIA"));
                d.setDtInicio(rs.getTimestamp("DT_INIC_UTILIZACAO"));
                d.setDtFim(rs.getTimestamp("DT_FIM_UTILIZACAO"));
                d.setDtLimConf(rs.getDate("DT_LIMITE_CONF"));
                d.setInteressado(rs.getString("NOME_INTERESSADO"));
                d.setStatus(rs.getString("CD_STATUS_RESERVA"));
                d.setTelefone(rs.getString("TEL_CONTATO"));
                d.setDtReserva(rs.getTimestamp("DT_RESERVA"));
                d.setDtConfirmacao(rs.getTimestamp("DT_CONFIRMACAO"));
                d.setNomeContato(rs.getString("NO_CONTATO"));
                d.setTelContato(rs.getString("TEL_CONTATO_RES"));
                d.setPublico(rs.getInt("QT_PUBLICO_PREVISTO"));
                d.setTipoEvento(rs.getInt("CD_TIPO_EVENTO"));
                
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
    
    public void inserir(Auditoria audit, String itens)throws InserirException{


        Connection cn = null;

        try {

            ParametroAuditoria par = new ParametroAuditoria();
            
            String sql = "INSERT INTO TB_RESERVA_DEPENDENCIA (SEQ_DEPENDENCIA, CD_CATEGORIA, CD_MATRICULA, SEQ_DEPENDENTE, DT_INIC_UTILIZACAO, DT_FIM_UTILIZACAO, " +
                         "NOME_INTERESSADO, DT_RESERVA, DT_CONFIRMACAO, TEL_CONTATO, CD_STATUS_RESERVA, DT_LIMITE_CONF, " +
                         "NO_CONTATO, TEL_CONTATO_RES, QT_PUBLICO_PREVISTO, CD_TIPO_EVENTO)" +
                         "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            p.setInt(1, par.getSetParametro(dependencia));
            if (matricula==0){
                p.setNull(2, java.sql.Types.INTEGER);
                par.getSetNull();
                p.setNull(3, java.sql.Types.INTEGER);
                par.getSetNull();
                p.setNull(4, java.sql.Types.INTEGER);
                par.getSetNull();
            }else{
                p.setInt(2, par.getSetParametro(categoriaSocio));
                p.setInt(3, par.getSetParametro(matricula));
                p.setInt(4, par.getSetParametro(dependente));
            }
            p.setTimestamp(5, new java.sql.Timestamp(par.getSetParametro(dtInicio).getTime()));
            p.setTimestamp(6, new java.sql.Timestamp(par.getSetParametro(dtFim).getTime()));
            p.setString(7, par.getSetParametro(interessado));
            p.setTimestamp(8, new java.sql.Timestamp(par.getSetParametro(new Date()).getTime()));
            if ("C".equals(status)){
                p.setTimestamp(9, new java.sql.Timestamp(par.getSetParametro(new Date()).getTime()));
            }else{
                p.setNull(9, java.sql.Types.DATE);
                par.getSetNull();
            }
            p.setString(10, par.getSetParametro(telefone));
            if ("C".equals(status)){
                p.setString(11, par.getSetParametro("CF"));
            }else{
                p.setString(11, par.getSetParametro("AC"));
            }    
            if (dtLimConf==null){
                p.setNull(12, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setDate(12, new java.sql.Date(par.getSetParametro(dtLimConf).getTime()));
            }
            p.setString(13, par.getSetParametro(nomeContato));
            p.setString(14, par.getSetParametro(telContato));
            p.setInt(15, par.getSetParametro(publico));
            p.setInt(16, par.getSetParametro(tipoEvento));
            
            p.executeUpdate();
            ResultSet rs = p.getGeneratedKeys();
            audit.registrarMudanca(sql, par.getParametroFinal());
            if(rs.next()){
                numero = rs.getInt(1);
                cn.commit();
                
                if (!"".equals(itens)){
                    String[] vetItens = itens.split(";");
                    String[] vetDet;
                    for(int i =0; i < vetItens.length ; i++){
                        par.limpaParametro();
                        vetDet = vetItens[i].split("#");
                        sql = "INSERT INTO TB_ITEM_ALUGUEL_DEP (SEQ_RESERVA, CD_ITEM_ALUGUEL, NU_QUANTIDADE, VR_TOTAL) VALUES(?, ?, ?, ?)";

                        p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        p.setInt(1, par.getSetParametro(numero));
                        p.setInt(2, par.getSetParametro(Integer.parseInt(vetDet[0])));
                        p.setInt(3, par.getSetParametro(Integer.parseInt(vetDet[1])));
                        p.setFloat(4, par.getSetParametro(Float.parseFloat(vetDet[2].replace(".", "").replace(",", "."))));
                        
                        p.executeUpdate();
                        cn.commit();
                        audit.registrarMudanca(sql, par.getParametroFinal());
                    }
                }
                
                
            }
            
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

    public void alterar(Auditoria audit, String itens)throws InserirException{


        Connection cn = null;
        ParametroAuditoria par = new ParametroAuditoria();

        try {

            String sql = "UPDATE TB_RESERVA_DEPENDENCIA " +
                         "SET DT_CONFIRMACAO = ?, TEL_CONTATO = ?, CD_STATUS_RESERVA = ?, DT_LIMITE_CONF = ?, " +
                         "NO_CONTATO = ?, TEL_CONTATO_RES = ?, QT_PUBLICO_PREVISTO = ?, CD_TIPO_EVENTO = ? WHERE SEQ_RESERVA = ? ";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            if ("C".equals(status)){
                if(dtConfirmacao==null){
                    p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(new Date()).getTime()));
                }else{
                    p.setTimestamp(1, new java.sql.Timestamp(par.getSetParametro(dtConfirmacao).getTime()));
                }
            }else{
                p.setNull(1, java.sql.Types.DATE);
                par.getSetNull();
            }
            p.setString(2, par.getSetParametro(telefone));
            if ("C".equals(status)){
                p.setString(3, par.getSetParametro("CF"));
            }else{
                p.setString(3, par.getSetParametro("AC"));
            }    
            if (dtLimConf==null){
                p.setNull(4, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setDate(4, new java.sql.Date(par.getSetParametro(dtLimConf).getTime()));
            }
            p.setString(5, par.getSetParametro(nomeContato));
            p.setString(6, par.getSetParametro(telContato));
            p.setInt(7, par.getSetParametro(publico));
            p.setInt(8, par.getSetParametro(tipoEvento));
            p.setInt(9, par.getSetParametro(numero));
            
            p.executeUpdate();
            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
            
            sql = "DELETE FROM TB_ITEM_ALUGUEL_DEP WHERE SEQ_RESERVA = ?";

            p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            p.setInt(1, numero);
            p.executeUpdate();
            cn.commit();
            
            if (!"".equals(itens)){
                
                String[] vetItens = itens.split(";");
                String[] vetDet;
                for(int i =0; i < vetItens.length ; i++){
                    par.limpaParametro();
                    vetDet = vetItens[i].split("#");
                    sql = "INSERT INTO TB_ITEM_ALUGUEL_DEP (SEQ_RESERVA, CD_ITEM_ALUGUEL, NU_QUANTIDADE, VR_TOTAL) VALUES(?, ?, ?, ?)";

                    p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    p.setInt(1, par.getSetParametro(numero));
                    p.setInt(2, par.getSetParametro(Integer.parseInt(vetDet[0])));
                    p.setInt(3, par.getSetParametro(Integer.parseInt(vetDet[1])));
                    p.setFloat(4, par.getSetParametro(Float.parseFloat(vetDet[2].replace(".", "").replace(",", "."))));

                    p.executeUpdate();
                    cn.commit();
                    audit.registrarMudanca(sql, par.getParametroFinal());
                }
            }
            
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
    
    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "UPDATE TB_RESERVA_DEPENDENCIA SET CD_STATUS_RESERVA = 'CA', DT_CONFIRMACAO = GETDATE(), DE_CANC_RESERVA = ?, DE_USER_CANC = ? WHERE SEQ_RESERVA = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setString(1, descricaoCanc);
            p.setString(2, usuarioCanc);
            p.setInt(3, numero);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, descricaoCanc, usuarioCanc, String.valueOf(numero));
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
