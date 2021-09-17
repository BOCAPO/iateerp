/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package techsoft.cadastro;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.util.Datas;

public class Convite {


    private int numero;
    private int categoriaSocio;
    private int matricula;
    private int dependente;
    private Date dtRetirada;
    private Date dtLimiteUtilizacao;
    private String sacador;
    private String convidado;
    private Date dtNascConvidado;
    private Date dtValidExMedico;
    private String categoriaConvite;
    private String status;
    private String usuario;
    private String estacionamento;
    private int convenio;
    private String tipo;
    private String nrChurrasqueira;
    private String hhEntrada;
    private String hhSaida;
    private Date dtUtilizacao;
    private int reservaChurrasqueira;
    private String cpfConvidado;
    private String rgConvidado;
    private String orgaoExpConvidado;
    private String docEstrangeiro;
    
    private Date dtInicValidade;
    private Date dtFimValidade;
    private String modalidade;
    private String tipoConviteEsportivo;
    private String entraSegunda;
    private String entraTerca;
    private String entraQuarta;
    private String entraQuinta;
    private String entraSexta;
    private String saiSegunda;
    private String saiTerca;
    private String saiQuarta;
    private String saiQuinta;
    private String saiSexta;

    private static final Logger log = Logger.getLogger("techsoft.cadastro.Convite");

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Convite)) return false;
        return (this.numero == ((Convite)o).numero);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.numero;
        return hash;
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
    public int getConvenio() {
        return convenio;
    }
    public void setConvenio(int convenio) {
        this.convenio = convenio;
    }    
    public int getReservaChurrasqueira() {
        return reservaChurrasqueira;
    }
    public void setReservaChurrasqueira(int reservaChurrasqueira) {
        this.reservaChurrasqueira = reservaChurrasqueira;
    }    
    public Date getDtRetirada() {
        return dtRetirada;
    }
    public void setDtRetirada(Date dtRetirada) {
        this.dtRetirada = dtRetirada;
    }    
    public Date getDtLimiteUtilizacao() {
        return dtLimiteUtilizacao;
    }
    public void setDtLimiteUtilizacao(Date dtLimiteUtilizacao) {
        this.dtLimiteUtilizacao = dtLimiteUtilizacao;
    }    
    public Date getDtNascConvidado() {
        return dtNascConvidado;
    }
    public void setDtNascConvidado(Date dtNascConvidado) {
        this.dtNascConvidado = dtNascConvidado;
    }    
    public Date getDtUtilizacao() {
        return dtUtilizacao;
    }
    public void setDtUtilizacao(Date dtUtilizacao) {
        this.dtUtilizacao = dtUtilizacao;
    }    
    public Date getDtValidExMedico() {
        return dtValidExMedico;
    }
    public void setDtValidExMedico(Date dtValidExMedico) {
        this.dtValidExMedico = dtValidExMedico;
    }    
    public String getSacador() {
        return sacador;
    }
    public void setSacador(String sacador) {
        this.sacador = sacador;
    }    
    public String getConvidado() {
        return convidado;
    }
    public void setConvidado(String convidado) {
        this.convidado = convidado;
    }    
    public String getCategoriaConvite() {
        return categoriaConvite;
    }
    public void setCategoriaConvite(String categoriaConvite) {
        this.categoriaConvite = categoriaConvite;
    }    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }    
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }    
    public String getEstacionamento() {
        return estacionamento;
    }
    public void setEstacionamento(String estacionamento) {
        this.estacionamento = estacionamento;
    }    
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }    
    public String getNrChurrasqueira() {
        return nrChurrasqueira;
    }
    public void setNrChurrasqueira(String nrChurrasqueira) {
        this.nrChurrasqueira = nrChurrasqueira;
    }    
    public String getHhEntrada() {
        return hhEntrada;
    }
    public void setHhEntrada(String hhEntrada) {
        this.hhEntrada = hhEntrada;
    }    
    public String getHhSaida() {
        return hhSaida;
    }
    public void setHhSaida(String hhSaida) {
        this.hhSaida = hhSaida;
    }    
    public String getCpfConvidado() {
        return cpfConvidado;
    }
    public void setCpfConvidado(String cpfConvidado) {
        this.cpfConvidado = cpfConvidado;
    }    
    public String getRgConvidado() {
        return rgConvidado;
    }
    public void setRgConvidado(String rgConvidado) {
        this.rgConvidado = rgConvidado;
    }
    public String getOrgaoExpConvidado() {
        return orgaoExpConvidado;
    }
    public void setOrgaoExpConvidado(String orgaoExpConvidado) {
        this.orgaoExpConvidado = orgaoExpConvidado;
    }
    public String getDocEstrangeiro() {
        return docEstrangeiro;
    }
    public void setDocEstrangeiro(String docEstrangeiro) {
        this.docEstrangeiro = docEstrangeiro;
    }
    public Date getDtInicValidade() {
        return dtInicValidade;
    }
    public void setDtInicValidade(Date dtInicValidade) {
        this.dtInicValidade = dtInicValidade;
    }
    public Date getDtFimValidade() {
        return dtFimValidade;
    }
    public void setDtFimValidade(Date dtFimValidade) {
        this.dtFimValidade = dtFimValidade;
    }
    public String getModalidade() {
        return modalidade;
    }
    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }
    public String getTipoConviteEsportivo() {
        return tipoConviteEsportivo;
    }
    public void setTipoConviteEsportivo(String tipoConviteEsportivo) {
        this.tipoConviteEsportivo = tipoConviteEsportivo;
    }
    public String getEntraSegunda() {
        return entraSegunda;
    }
    public void setEntraSegunda(String entraSegunda) {
        this.entraSegunda = entraSegunda;
    }
    public String getEntraTerca() {
        return entraTerca;
    }
    public void setEntraTerca(String entraTerca) {
        this.entraTerca = entraTerca;
    }
    public String getEntraQuarta() {
        return entraQuarta;
    }
    public void setEntraQuarta(String entraQuarta) {
        this.entraQuarta = entraQuarta;
    }
    public String getEntraQuinta() {
        return entraQuinta;
    }
    public void setEntraQuinta(String entraQuinta) {
        this.entraQuinta = entraQuinta;
    }
    public String getEntraSexta() {
        return entraSexta;
    }
    public void setEntraSexta(String entraSexta) {
        this.entraSexta = entraSexta;
    }
    public String getSaiSegunda() {
        return saiSegunda;
    }
    public void setSaiSegunda(String saiSegunda) {
        this.saiSegunda = saiSegunda;
    }
    public String getSaiTerca() {
        return saiTerca;
    }
    public void setSaiTerca(String saiTerca) {
        this.saiTerca = saiTerca;
    }
    public String getSaiQuarta() {
        return saiQuarta;
    }
    public void setSaiQuarta(String saiQuarta) {
        this.saiQuarta = saiQuarta;
    }
    public String getSaiQuinta() {
        return saiQuinta;
    }
    public void setSaiQuinta(String saiQuinta) {
        this.saiQuinta = saiQuinta;
    }
    public String getSaiSexta() {
        return saiSexta;
    }
    public void setSaiSexta(String saiSexta) {
        this.saiSexta = saiSexta;
    }        

    public static List<Convite> listar(int numero, String dtEmissao, String responsavel, String convidado, String dtValidade){

        ArrayList<Convite> l = new ArrayList<Convite>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall("{call sp_recupera_convite_java (?, ?, ?, ?, ?)}");
            cal.setInt(1, numero);
            Date d1 = Datas.parse(dtEmissao);
            if (d1==null){
                cal.setNull(2, java.sql.Types.DATE);
            }else{
                cal.setDate(2, new java.sql.Date(d1.getTime()));
            }
            cal.setString(3, responsavel);
            cal.setString(4, convidado);
            Date d2 = Datas.parse(dtValidade);
            if (d2==null){
                cal.setNull(5, java.sql.Types.DATE);
            }else{
                cal.setDate(5, new java.sql.Date(d2.getTime()));
            }
            
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Convite s = new Convite();

                s.setNumero(rs.getInt("NR_CONVITE"));
                s.setCategoriaSocio(rs.getInt("CD_CATEGORIA"));
                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setConvenio(rs.getInt("CD_CONVENIO"));
                s.setDtRetirada(rs.getDate("DT_RETIRADA"));
                s.setDtLimiteUtilizacao(rs.getDate("DT_MAX_UTILIZACAO"));
                s.setDtNascConvidado(rs.getDate("DT_NASC_CONVIDADO"));
                s.setDtUtilizacao(rs.getDate("DT_UTILIZACAO"));
                s.setDtValidExMedico(rs.getDate("DT_VALID_EX_MEDICO_CONVITE"));
                s.setSacador(rs.getString("NOME_SACADOR"));
                s.setConvidado(rs.getString("NOME_CONVIDADO"));
                s.setCategoriaConvite(rs.getString("CD_CATEGORIA_CONVITE"));
                s.setStatus(rs.getString("CD_STATUS_CONVITE"));
                s.setUsuario(rs.getString("USER_ACESSO_SISTEMA"));
                s.setEstacionamento(rs.getString("ESTACIONAMENTO_INTERNO"));
                s.setTipo(rs.getString("CD_TIPO_CONVITE"));
                s.setNrChurrasqueira(rs.getString("NR_CHURRASQUEIRA"));
                s.setHhEntrada(rs.getString("HH_ENTRADA"));
                s.setHhSaida(rs.getString("HH_SAIDA"));
                s.setCpfConvidado(rs.getString("CPF_CONVIDADO"));
                s.setRgConvidado(rs.getString("RG_CONVIDADO"));
                s.setOrgaoExpConvidado(rs.getString("DE_ORGAO_EXP_CONVIDADO"));
                s.setDocEstrangeiro(rs.getString("NR_DOC_ESTRANGEIRO"));
                
                    
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
    
    public static List<Convite> listar(String sql){

        ArrayList<Convite> l = new ArrayList<Convite>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;

            cal = cn.prepareCall(sql);
            
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Convite s = new Convite();

                s.setNumero(rs.getInt("NR_CONVITE"));
                s.setCategoriaSocio(rs.getInt("CD_CATEGORIA"));
                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setConvenio(rs.getInt("CD_CONVENIO"));
                s.setDtRetirada(rs.getDate("DT_RETIRADA"));
                s.setDtLimiteUtilizacao(rs.getDate("DT_MAX_UTILIZACAO"));
                s.setDtNascConvidado(rs.getDate("DT_NASC_CONVIDADO"));
                s.setDtUtilizacao(rs.getDate("DT_UTILIZACAO"));
                s.setDtValidExMedico(rs.getDate("DT_VALID_EX_MEDICO_CONVITE"));
                s.setSacador(rs.getString("NOME_SACADOR"));
                s.setConvidado(rs.getString("NOME_CONVIDADO"));
                s.setCategoriaConvite(rs.getString("CD_CATEGORIA_CONVITE"));
                s.setStatus(rs.getString("CD_STATUS_CONVITE"));
                s.setUsuario(rs.getString("USER_ACESSO_SISTEMA"));
                s.setEstacionamento(rs.getString("ESTACIONAMENTO_INTERNO"));
                s.setTipo(rs.getString("CD_TIPO_CONVITE"));
                s.setNrChurrasqueira(rs.getString("NR_CHURRASQUEIRA"));
                s.setHhEntrada(rs.getString("HH_ENTRADA"));
                s.setHhSaida(rs.getString("HH_SAIDA"));
                s.setCpfConvidado(rs.getString("CPF_CONVIDADO"));
                s.setRgConvidado(rs.getString("RG_CONVIDADO"));
                s.setOrgaoExpConvidado(rs.getString("DE_ORGAO_EXP_CONVIDADO"));
                s.setDocEstrangeiro(rs.getString("NR_DOC_ESTRANGEIRO"));
                
                    
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
    
    public static Convite getInstance(int id){
        Convite d = null;
        String sql = "SELECT * FROM TB_CONVITE WHERE NR_CONVITE = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new Convite();
                
                d.numero = rs.getInt("NR_CONVITE");
                d.categoriaSocio = rs.getInt("CD_CATEGORIA");
                d.matricula = rs.getInt("CD_MATRICULA");
                d.dependente = rs.getInt("SEQ_DEPENDENTE");
                d.convenio = rs.getInt("CD_CONVENIO");
                d.reservaChurrasqueira = rs.getInt("SEQ_RESERVA");
                d.dtRetirada = rs.getDate("DT_RETIRADA");
                d.dtLimiteUtilizacao = rs.getDate("DT_MAX_UTILIZACAO");
                d.dtNascConvidado = rs.getDate("DT_NASC_CONVIDADO");
                d.dtUtilizacao = rs.getDate("DT_UTILIZACAO");
                d.dtValidExMedico = rs.getDate("DT_VALID_EX_MEDICO_CONVITE");
                d.sacador = rs.getString("NOME_SACADOR");
                d.convidado = rs.getString("NOME_CONVIDADO");
                d.categoriaConvite = rs.getString("CD_CATEGORIA_CONVITE");
                d.status = rs.getString("CD_STATUS_CONVITE");
                d.usuario = rs.getString("USER_ACESSO_SISTEMA");
                d.estacionamento = rs.getString("ESTACIONAMENTO_INTERNO");
                d.tipo = rs.getString("CD_TIPO_CONVITE");
                d.nrChurrasqueira = rs.getString("NR_CHURRASQUEIRA");
                d.hhEntrada = rs.getString("HH_ENTRADA");
                d.hhSaida = rs.getString("HH_SAIDA");
                if (d.categoriaConvite.equals("SP")){
                    sql = "SELECT * FROM TB_COMPLEMENTO_CONV_ESPORTIVO   WHERE NR_CONVITE = ?";
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p2 = cn.prepareStatement(sql);
                    p2.setInt(1, id);
                    ResultSet rs2 = p2.executeQuery();
                    if(rs2.next()){
                        d.dtInicValidade = rs2.getDate("DT_INIC_VALIDADE");
                        d.dtFimValidade = rs2.getDate("DT_FIM_VALIDADE");
                        d.modalidade = rs2.getString("DE_MODALIDADE");
                        d.tipoConviteEsportivo = rs2.getString("CD_TIPO_CONVITE");
                        
                        d.entraSegunda = rs2.getString("HH_ENTRA_SEGUNDA");
                        d.entraTerca = rs2.getString("HH_ENTRA_TERCA");
                        d.entraQuarta = rs2.getString("HH_ENTRA_QUARTA");
                        d.entraQuinta = rs2.getString("HH_ENTRA_QUINTA");
                        d.entraSexta = rs2.getString("HH_ENTRA_SEXTA");
                        
                        d.saiSegunda = rs2.getString("HH_SAI_SEGUNDA");
                        d.saiTerca = rs2.getString("HH_SAI_TERCA");
                        d.saiQuarta = rs2.getString("HH_SAI_QUARTA");
                        d.saiQuinta = rs2.getString("HH_SAI_QUINTA");
                        d.saiSexta = rs2.getString("HH_SAI_SEXTA");
                        
                    }
                    
                }
                
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
    
    public static Convite calculaDtVenc(){
        Convite d = null;
        String sql = "{call SP_CALCULA_DT_VENC_CONVITE ()}";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new Convite();
                
                d.dtLimiteUtilizacao = rs.getDate("VENCIMENTO");
                
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

    public void reativa(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "UPDATE TB_CONVITE SET CD_STATUS_CONVITE = 'NU' WHERE NR_CONVITE = ?";
                
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall(sql);
            cal.setInt(1, numero);
            cal.executeUpdate();
            cn.commit();
            
            audit.registrarMudanca(sql, String.valueOf(numero));
            
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
    
    public void cancela(Auditoria audit){
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall("{call SP_EXCLUIR_CONVITE (?)}");
            cal.setInt(1, numero);
            cal.executeUpdate();
            cn.commit();
            
            audit.registrarMudanca("{call SP_EXCLUIR_CONVITE (?)}", String.valueOf(numero));
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
    public void alterar(Auditoria audit){

        if(numero == 0) return;

        Connection cn = null;
        String sql = null;
        ParametroAuditoria par = new ParametroAuditoria();
        try {
            if (categoriaConvite.equals("CH")) {
                cn = Pool.getInstance().getConnection();
                CallableStatement cal = null;
                sql = "{call SP_ALTERA_CONV_CHU (?, ?, ?, ?)}";
                cal = cn.prepareCall(sql);
                cal.setInt(1, par.getSetParametro(numero));
                cal.setInt(2, par.getSetParametro(reservaChurrasqueira));
                cal.setString(3, par.getSetParametro(estacionamento));
                cal.setString(4, par.getSetParametro(convidado));

                cal.execute();
                //rs2 = p2.getResultSet();
                cn.commit();
                
            }else{
                sql = "UPDATE TB_CONVITE SET NOME_CONVIDADO = ?, DT_MAX_UTILIZACAO = ?, ESTACIONAMENTO_INTERNO = ? where nr_convite = ?";
                        
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                p.setString(1, par.getSetParametro(convidado));
                p.setDate(2, new java.sql.Date(par.getSetParametro(dtLimiteUtilizacao).getTime()));
                p.setString(3, par.getSetParametro(estacionamento));
                p.setInt(4, par.getSetParametro(numero));
                p.executeUpdate();
                cn.commit();
                
                audit.registrarMudanca(sql, par.getParametroFinal());
                
                if (categoriaConvite.equals("SP")){
                    sql = "UPDATE TB_COMPLEMENTO_CONV_ESPORTIVO "+ 
                          "SET DT_INIC_VALIDADE = ?, DT_FIM_VALIDADE = ?, DE_MODALIDADE= ?, "+
                          " ACESSA_SEGUNDA = ?, HH_ENTRA_SEGUNDA = ?, HH_SAI_SEGUNDA = ?, " +
                          " ACESSA_TERCA = ?, HH_ENTRA_TERCA = ?, HH_SAI_TERCA = ?,  " +
                          " ACESSA_QUARTA = ?, HH_ENTRA_QUARTA = ?, HH_SAI_QUARTA = ?,  " +
                          " ACESSA_QUINTA = ?, HH_ENTRA_QUINTA = ?, HH_SAI_QUINTA = ?, "+ 
                          " ACESSA_SEXTA = ?, HH_ENTRA_SEXTA = ?, HH_SAI_SEXTA = ? " +
                          " WHERE NR_CONVITE = ? ";
                    
                    cn = Pool.getInstance().getConnection();
                    PreparedStatement p2 = cn.prepareStatement(sql);
                    par.limpaParametro();

                    p2.setDate(1, new java.sql.Date(dtInicValidade.getTime()));
                    p2.setDate(2, new java.sql.Date(dtFimValidade.getTime()));
                    p2.setString(3, par.getSetParametro(modalidade));

                    if ((!entraSegunda.equals("")) || (!saiSegunda.equals(""))){
                        p2.setInt(4, par.getSetParametro(1));
                        p2.setString(5, par.getSetParametro(entraSegunda.replaceAll(":","")));
                        p2.setString(6, par.getSetParametro(saiSegunda.replaceAll(":","")));
                    }else{
                        p2.setInt(4, par.getSetParametro(0));
                        p2.setNull(5, java.sql.Types.VARCHAR);
                        p2.setNull(6, java.sql.Types.VARCHAR);
                    }
                    if ((!entraTerca.equals("")) || (!saiTerca.equals(""))){
                        p2.setInt(7, par.getSetParametro(1));
                        p2.setString(8, par.getSetParametro(entraTerca.replaceAll(":","")));
                        p2.setString(9, par.getSetParametro(saiTerca.replaceAll(":","")));
                    }else{
                        p2.setInt(7, par.getSetParametro(0));
                        p2.setNull(8, java.sql.Types.VARCHAR);
                        p2.setNull(9, java.sql.Types.VARCHAR);
                    }
                    if ((!entraQuarta.equals("")) || (!saiQuarta.equals(""))){
                        p2.setInt(10, par.getSetParametro(1));
                        p2.setString(11, par.getSetParametro(entraQuarta.replaceAll(":","")));
                        p2.setString(12, par.getSetParametro(saiQuarta.replaceAll(":","")));
                    }else{
                        p2.setInt(10, par.getSetParametro(0));
                        p2.setNull(11, java.sql.Types.VARCHAR);
                        p2.setNull(12, java.sql.Types.VARCHAR);
                    }
                    if ((!entraQuinta.equals("")) || (!saiQuinta.equals(""))){
                        p2.setInt(13, par.getSetParametro(1));
                        p2.setString(14, par.getSetParametro(entraQuinta.replaceAll(":","")));
                        p2.setString(15, par.getSetParametro(saiQuinta.replaceAll(":","")));
                    }else{
                        p2.setInt(13, par.getSetParametro(0));
                        p2.setNull(14, java.sql.Types.VARCHAR);
                        p2.setNull(15, java.sql.Types.VARCHAR);
                    }
                    if ((!entraSexta.equals("")) || (!saiSexta.equals(""))){
                        p2.setInt(16, par.getSetParametro(1));
                        p2.setString(17, par.getSetParametro(entraSexta.replaceAll(":","")));
                        p2.setString(18, par.getSetParametro(saiSexta.replaceAll(":","")));
                    }else{
                        p2.setInt(16, par.getSetParametro(0));
                        p2.setNull(17, java.sql.Types.VARCHAR);
                        p2.setNull(18, java.sql.Types.VARCHAR);
                    }

                    p2.setInt(19, par.getSetParametro(numero));
                    
                    p2.executeUpdate();
                    cn.commit();
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

    
    
}

