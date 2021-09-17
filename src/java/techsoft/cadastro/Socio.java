/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package techsoft.cadastro;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.seguranca.Auditoria;

    public class Socio {
    private int matricula;
    private int seqDependente;
    private int idCategoria;
    private int saldoConvite;
    private int saldoConviteSauna;
    private String nome;
    private Date dataNascimento;
    private String situacao;
    private String categoria;
    private String tipoCategoria;
    private int nrCarteira;
    private int idTipoDependente;
    private char casoEspecial;
    private boolean masculino = true;
    private Date dataCasoEspecial;
    private String telefoneR;
    private String telefoneC;
    private Date dtExame;
    private String resultadoExame;
    private String conclusaoExame;
    private Date dtValidadeExame;
    private Date dtValidadeAtestado;
    
    //Usada para associar pessoa a uma modalidade esportiva
    private Map<Integer, String> modalidadesEsportivas = new HashMap<Integer, String>();
    
    /*
     * icone de alerta para situacao especial, excluido, 
     * dependente com idade extrapolada, possui embarcacao
     * ou esta suspenso
     */
    private int alerta;

    private static final Logger log = Logger.getLogger("techsoft.cadastro.Socio");
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Socio)) return false;
        Socio s = (Socio) o;
        return ((this.matricula == s.matricula) && (this.seqDependente == s.seqDependente) && (this.idCategoria == s.idCategoria));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + this.matricula;
        hash = 61 * hash + this.seqDependente;
        hash = 61 * hash + this.idCategoria;
        return hash;
    }
    
    
    public Date getDataCasoEspecial() {
        return dataCasoEspecial;
    }

    public void setDataCasoEspecial(Date dataCasoEspecial) {
        this.dataCasoEspecial = dataCasoEspecial;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public char getCasoEspecial() {
        return casoEspecial;
    }

    public void setCasoEspecial(char casoEspecial) {
        this.casoEspecial = casoEspecial;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(String tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }

    public int getSeqDependente() {
        return seqDependente;
    }

    public void setSeqDependente(int seqDependente) {
        this.seqDependente = seqDependente;
    }

    public int getIdTipoDependente() {
        return idTipoDependente;
    }

    public void setIdTipoDependente(int idTipoDependente) {
        this.idTipoDependente = idTipoDependente;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }
    public int getSaldoConvite() {
        return saldoConvite;
    }

    public void setSaldoConvite(int saldoConvite) {
        this.saldoConvite = saldoConvite;
    }
    public int getSaldoConviteSauna() {
        return saldoConviteSauna;
    }

    public void setSaldoConviteSauna(int saldoConviteSauna) {
        this.saldoConviteSauna = saldoConviteSauna;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNrCarteira() {
        return nrCarteira;
    }

    public void setNrCarteira(int nrCarteira) {
        this.nrCarteira = nrCarteira;
    }

    public boolean isMasculino() {
        return masculino;
    }

    public void setMasculino(boolean masculino) {
        this.masculino = masculino;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public int getAlerta() {
        return alerta;
    }
    public String getTelefoneR() {
        return telefoneR;
    }
    public void setTelefoneR(String telefoneR) {
        this.telefoneR = telefoneR;
    }
    public String getTelefoneC() {
        return telefoneC;
    }
    public void setTelefoneC(String telefoneC) {
        this.telefoneC = telefoneC;
    }

    public Map<Integer, String> getModalidadesEsportivas() {
        return modalidadesEsportivas;
    }

    public void setModalidadesEsportivas(Map<Integer, String> modalidadesEsportivas) {
        this.modalidadesEsportivas = modalidadesEsportivas;
    }
    public Date getDtExame() {
        return dtExame;
    }
    public void setDtExame(Date dtExame) {
        this.dtExame = dtExame;
    }
    public String getResultadoExame() {
        return resultadoExame;
    }
    public void setResultadoExame(String resultadoExame) {
        this.resultadoExame = resultadoExame;
    }
    public String getConclusaoExame() {
        return conclusaoExame;
    }
    public void setConclusaoExame(String conclusaoExame) {
        this.conclusaoExame = conclusaoExame;
    }
    public Date getDtValidadeExame() {
        return dtValidadeExame;
    }
    public void setDtValidadeExame(Date dtValidadeExame) {
        this.dtValidadeExame = dtValidadeExame;
    }
    public Date getDtValidadeAtestado() {
        return dtValidadeAtestado;
    }
    public void setDtValidadeAtestado(Date dtValidadeAtestado) {
        this.dtValidadeAtestado = dtValidadeAtestado;
    }

    
    public static Socio getInstance(int matricula, int seqDependente, int idCategoria){
        Connection cn = null;
        Socio s = null;
        String sql = "SELECT T1.*, T2.*, T3.*, (SELECT DT_VAL_ATESTADO FROM IATE_FOTO..TB_ATESTADO_MEDICO_PESSOA T0 WHERE T0.CD_MATRICULA = T1.CD_MATRICULA AND T0.CD_CATEGORIA = T1.CD_CATEGORIA AND T0.SEQ_DEPENDENTE = T1.SEQ_DEPENDENTE) DT_VAL_ATESTADO  FROM TB_PESSOA T1, VW_COMPLEMENTO T2, TB_CATEGORIA T3 WHERE T1.CD_CATEGORIA = T3.CD_CATEGORIA AND T1.CD_MATRICULA = T2.CD_MATRICULA AND T1.CD_CATEGORIA = T2.CD_CATEGORIA AND T1.CD_MATRICULA = ? AND T1.SEQ_DEPENDENTE = ? AND T1.CD_CATEGORIA = ?";
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, seqDependente);
            p.setInt(3, idCategoria);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                s = new Socio();

                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                s.setNome(rs.getString("NOME_PESSOA"));
                s.setSituacao(rs.getString("CD_SIT_PESSOA"));
                s.setTelefoneR(rs.getString("TELEFONE_R"));
                s.setTelefoneC(rs.getString("TELEFONE_C"));
                s.setCategoria(rs.getString("DESCR_CATEGORIA"));
                s.setDataNascimento(rs.getDate("DT_NASCIMENTO"));
                s.setNrCarteira(rs.getInt("NR_CARTEIRA_PESSOA"));
                s.setDtValidadeAtestado(rs.getDate("DT_VAL_ATESTADO"));
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
        return s;
    }
    public static Socio getInstance(int nrCarteira){
        Connection cn = null;
        Socio s = null;
        String sql = "SELECT * FROM TB_PESSOA T1, VW_COMPLEMENTO T2, TB_CATEGORIA T3 WHERE T1.CD_CATEGORIA = T3.CD_CATEGORIA AND T1.CD_MATRICULA = T2.CD_MATRICULA AND T1.CD_CATEGORIA = T2.CD_CATEGORIA AND T1.NR_CARTEIRA_PESSOA = ? ";
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, nrCarteira);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                s = new Socio();

                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                s.setNome(rs.getString("NOME_PESSOA"));
                s.setSituacao(rs.getString("CD_SIT_PESSOA"));
                s.setTelefoneR(rs.getString("TELEFONE_R"));
                s.setTelefoneC(rs.getString("TELEFONE_C"));
                s.setCategoria(rs.getString("DESCR_CATEGORIA"));
                s.setDataNascimento(rs.getDate("DT_NASCIMENTO"));
                s.setNrCarteira(rs.getInt("NR_CARTEIRA_PESSOA"));
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
        return s;
    }
    public static Socio getComodoro(){
        Connection cn = null;
        Socio s = null;
        String sql = "select cd_matricula, cd_categoria, seq_dependente, nome_pessoa from tb_pessoa where cd_cargo_especial IN (SELECT CD_CARGO_ESPECIAL FROM TB_CARGO_ESPECIAL WHERE IC_TIPO_CARGO = 'CO') and cd_cargo_ativo = 'S'";
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                s = new Socio();

                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                s.setNome(rs.getString("NOME_PESSOA"));
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
        return s;
    }

    public static List<Socio> listar(int carteira, int categoria, int matricula, String nome){


        ArrayList<Socio> l = new ArrayList<Socio>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            if(carteira > 0){
                cal = cn.prepareCall("{call SP_RECUPERA_PESSOA_CARTEIRA (?)}");
                cal.setInt(1, carteira);
            }else{
                if(categoria > 0 && matricula > 0){
                    cal = cn.prepareCall("{call SP_RECUPERA_PESSOA_MATR (?, ?)}");
                    cal.setInt(1, matricula);
                    cal.setInt(2, categoria);
                }else{
                    cal = cn.prepareCall("{call SP_RECUPERA_PESSOA_NOME (?)}");
                    cal.setString(1, nome + "%");
                }
            }

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Socio s = new Socio();
                
                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                s.setNome(rs.getString("NOME_PESSOA"));
                s.setDataNascimento(rs.getDate("DT_NASCIMENTO"));
                s.setSituacao(rs.getString("CD_SIT_PESSOA"));
                s.setCategoria(rs.getString("DESCR_CATEGORIA"));
                s.setTipoCategoria(rs.getString("TP_CATEGORIA"));
                s.setNrCarteira(rs.getInt("NR_CARTEIRA_PESSOA"));
                s.setIdTipoDependente(rs.getInt("CD_TP_DEPENDENTE"));
                if(rs.getString("CD_CASO_ESPECIAL") == null){
                    s.setCasoEspecial((char)0);
                }else{
                    s.setCasoEspecial(rs.getString("CD_CASO_ESPECIAL").charAt(0));
                }
                
                if(rs.getString("CD_SEXO").toLowerCase().charAt(0) == 'm'){
                    s.setMasculino(true);
                }else{
                    s.setMasculino(false);
                }
                s.setDataCasoEspecial(rs.getDate("dt_caso_especial"));

                l.add(s);
            }


            processarAlertas(l);
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
    
    public static List<Socio> listarExameMedico(int carteira, int categoria, int matricula, String nome){


        ArrayList<Socio> l = new ArrayList<Socio>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            if(carteira > 0){
                cal = cn.prepareCall("{call SP_RECUPERA_PESSOA_CARTEIRA (?)}");
                cal.setInt(1, carteira);
            }else{
                if(categoria > 0 && matricula > 0){
                    cal = cn.prepareCall("{call SP_RECUPERA_PESSOA_MATR (?, ?)}");
                    cal.setInt(1, matricula);
                    cal.setInt(2, categoria);
                }else{
                    cal = cn.prepareCall("{call SP_RECUPERA_PESSOA_NOME (?)}");
                    cal.setString(1, nome + "%");
                }
            }

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Socio s = new Socio();
                
                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                s.setNome(rs.getString("NOME_PESSOA"));
                s.setDataNascimento(rs.getDate("DT_NASCIMENTO"));
                s.setSituacao(rs.getString("CD_SIT_PESSOA"));
                s.setCategoria(rs.getString("DESCR_CATEGORIA"));
                s.setTipoCategoria(rs.getString("TP_CATEGORIA"));
                s.setNrCarteira(rs.getInt("NR_CARTEIRA_PESSOA"));
                s.setIdTipoDependente(rs.getInt("CD_TP_DEPENDENTE"));
                if(rs.getString("CD_CASO_ESPECIAL") == null){
                    s.setCasoEspecial((char)0);
                }else{
                    s.setCasoEspecial(rs.getString("CD_CASO_ESPECIAL").charAt(0));
                }
                
                if(rs.getString("CD_SEXO").toLowerCase().charAt(0) == 'm'){
                    s.setMasculino(true);
                }else{
                    s.setMasculino(false);
                }
                s.setDataCasoEspecial(rs.getDate("dt_caso_especial"));

                processaExameMedico(s);
                
                l.add(s);
            }


            processarAlertas(l);
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
    
    public static void processaExameMedico(Socio s){
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            String sql = "SELECT " +
                        "DT_EXAME       , " +
                        "DE_CONCLUSAO         , " +
                        "IC_RESULTADO         , " +
                        "DT_VALIDADE_EXAME " +
                   "From " +
                        "TB_EXAME_MEDICO " +
                     "Where " +
                        "CD_EXAME_MEDICO      = " +
                        "( " +
                           "SELECT " +
                              "Max (CD_EXAME_MEDICO) " +
                           "From " +
                              "TB_EXAME_MEDICO " +
                           "Where " +
                              "CD_MATRICULA      = ? AND " +
                              "CD_CATEGORIA      = ? AND " +
                              "SEQ_DEPENDENTE    = ? " +
                        ") ";
                              
            cal = cn.prepareCall(sql);
            cal.setInt(1, s.matricula);
            cal.setInt(2, s.idCategoria);
            cal.setInt(3, s.seqDependente);

            ResultSet rs = cal.executeQuery();
            if (rs.next()) {
                s.setDtExame(rs.getDate("DT_EXAME"));
                s.setDtValidadeExame(rs.getDate("DT_VALIDADE_EXAME"));
                s.setConclusaoExame(rs.getString("DE_CONCLUSAO"));
                s.setResultadoExame(rs.getString("IC_RESULTADO"));
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

    }
    
    public static List<Socio> listarPassaporte(int passaporte, String nome){


        ArrayList<Socio> l = new ArrayList<Socio>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            if(passaporte > 0){
                cal = cn.prepareCall("{call SP_RECUPERA_PESSOA_PASSAPORTE (?)}");
                cal.setInt(1, passaporte);
            }else{
                cal = cn.prepareCall("{call SP_RECUPERA_PASSAPORTE_NOME (?)}");
                cal.setString(1, nome + "%");
            }

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Socio s = new Socio();
                
                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                s.setNome(rs.getString("NOME_PESSOA"));
                s.setDataNascimento(rs.getDate("DT_NASCIMENTO"));
                s.setSituacao(rs.getString("CD_SIT_PESSOA"));
                s.setCategoria(rs.getString("DESCR_CATEGORIA"));
                s.setTipoCategoria(rs.getString("TP_CATEGORIA"));
                s.setNrCarteira(rs.getInt("NR_PASSAPORTE"));
                s.setIdTipoDependente(rs.getInt("CD_TP_DEPENDENTE"));
                if(rs.getString("CD_CASO_ESPECIAL") == null){
                    s.setCasoEspecial((char)0);
                }else{
                    s.setCasoEspecial(rs.getString("CD_CASO_ESPECIAL").charAt(0));
                }
                
                if(rs.getString("CD_SEXO").toLowerCase().charAt(0) == 'm'){
                    s.setMasculino(true);
                }else{
                    s.setMasculino(false);
                }

                l.add(s);
            }


            processarAlertas(l);
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

    public static List<Socio> listarParaConvite(int carteira, int categoria, int matricula, String nome, String tipoConvite){


        ArrayList<Socio> l = new ArrayList<Socio>();
        Connection cn = null;
        String sql = "";
        
        sql = "select " +
                    "t1.cd_categoria, t1.cd_matricula, t1.seq_dependente, " +
                    "t1.nome_pessoa,  t2.sd_convite, t2.sd_convite_churrasqueira,  " +
                    "t2.sd_convite_sauna, t2.sd_convite_sinuca, t1.cd_tp_dependente,  " +
                    "t1.cd_caso_especial, t1.dt_nascimento   " +
              "from  " +
                    "tb_pessoa t1,  " +
                    "tb_complemento t2,  " +
                    "tb_categoria t3  " +
              "where  " +
                    "t1.cd_categoria = t3.cd_categoria and  " +
                    "t3.tp_categoria = 'SO' and  " +
                    "(t1.seq_dependente = 0 OR (T1.DT_VALID_RET_CONVITE  >= getdate() and T1.DT_VALID_RET_CONVITE IS NOT NULL)) AND  " +
                    "t1.cd_matricula = t2.cd_matricula and  " +
                    "t2.seq_dependente = 0 and " +
                    "t1.cd_categoria = t2.cd_categoria AND " +
                    "T1.CD_SIT_PESSOA = 'NO' AND  " +
                    "t2.CD_VRF_CONVITE is null ";
        
            if(carteira >= 1 ){
                sql = sql + " AND T1.NR_CARTEIRA_PESSOA = " + carteira; 
            }                
            if(categoria >= 1 ){
                sql = sql + " AND T1.CD_CATEGORIA = " + categoria; 
            }              
            if(matricula >= 1 ){
                sql = sql + " AND T1.CD_MATRICULA = " + matricula; 
            }              
            if(nome != "" ){
                sql = sql + " AND T1.NOME_PESSOA LIKE  '" + nome + "%'"; 
            }     
            
            if (tipoConvite.equals("EA") || tipoConvite.equals("VA")) {
               sql = sql + "AND CD_CARGO_ESPECIAL IN (SELECT CD_CARGO_ESPECIAL FROM TB_CARGO_ESPECIAL WHERE IC_TIPO_CARGO = 'AC')";
            }   
            if (tipoConvite.equals("ED") || tipoConvite.equals("VD")) {
               sql = sql + "AND CD_CARGO_ESPECIAL IN (SELECT CD_CARGO_ESPECIAL FROM TB_CARGO_ESPECIAL WHERE IC_TIPO_CARGO = 'DI')";
            }   
            if (tipoConvite.equals("EV") || tipoConvite.equals("VV")) {
               sql = sql + "AND CD_CARGO_ESPECIAL IN (SELECT CD_CARGO_ESPECIAL FROM TB_CARGO_ESPECIAL WHERE IC_TIPO_CARGO = 'VD')";
            }   
            if (tipoConvite.equals("EO") || tipoConvite.equals("VO")) {
               sql = sql + "AND CD_CARGO_ESPECIAL IN (SELECT CD_CARGO_ESPECIAL FROM TB_CARGO_ESPECIAL WHERE IC_TIPO_CARGO = 'VC')";
            }   
            if (tipoConvite.equals("EP") || tipoConvite.equals("VP")) {
               sql = sql + "AND CD_CARGO_ESPECIAL IN (SELECT CD_CARGO_ESPECIAL FROM TB_CARGO_ESPECIAL WHERE IC_TIPO_CARGO = 'PC')";
            }   
            if (tipoConvite.equals("EN") || tipoConvite.equals("VN")) {
               sql = sql + "AND CD_CARGO_ESPECIAL IN (SELECT CD_CARGO_ESPECIAL FROM TB_CARGO_ESPECIAL WHERE IC_TIPO_CARGO = 'CD')";
            }   
            
        
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall(sql);

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Socio s = new Socio();
                
                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                s.setNome(rs.getString("NOME_PESSOA"));
                s.setDataNascimento(rs.getDate("DT_NASCIMENTO"));
                s.setSaldoConvite(rs.getInt("sd_convite"));
                s.setSaldoConviteSauna(rs.getInt("sd_convite_sauna"));

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
    
    public static List<Socio> listarResChu(int carteira, int categoria, int matricula, String nome){


        ArrayList<Socio> l = new ArrayList<Socio>();
        Connection cn = null;
        String sql = "";

        sql = "select " +
                   "t1.cd_categoria, t1.cd_matricula, t1.seq_dependente, " +
                   "t1.nome_pessoa, t3.telefone_r, t3.telefone_c   " +
               "from " +
                    "tb_pessoa t1, tb_categoria t2, VW_complemento t3 " +
               "where " +
                    "t1.cd_categoria     = t2.cd_categoria        and " +
                    "t2.tp_categoria     = 'SO'                   and " +
                    "(t1.seq_dependente   = 0                     OR  " +
                    "(T1.DT_VALID_RESERVA >= getdate() and T1.DT_VALID_RESERVA IS NOT NULL)) AND " +
                    "t1.cd_matricula     = t3.cd_matricula        and " +
                    "t3.seq_dependente   = 0      and " +
                    "t1.cd_categoria     = t3.cd_categoria        AND " +
                    "T1.CD_SIT_PESSOA    = 'NO' AND " +
                    "T3.CD_VRF_CHURRASQUEIRA is null ";        
            if(carteira >= 1 ){
                sql = sql + " AND T1.NR_CARTEIRA_PESSOA = " + carteira; 
            }                
            if(categoria >= 1 ){
                sql = sql + " AND T1.CD_CATEGORIA = " + categoria; 
            }              
            if(matricula >= 1 ){
                sql = sql + " AND T1.CD_MATRICULA = " + matricula; 
            }              
            if(nome != "" ){
                sql = sql + " AND T1.NOME_PESSOA LIKE  '" + nome + "%'"; 
            }     
            
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall(sql);

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Socio s = new Socio();
                
                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                s.setNome(rs.getString("NOME_PESSOA"));
                s.setTelefoneR(rs.getString("TELEFONE_R"));
                s.setTelefoneC(rs.getString("TELEFONE_C"));

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
    
    public static List<Socio> listarEmpMat(int carteira, int categoria, int matricula, String nome){


        ArrayList<Socio> l = new ArrayList<Socio>();
        Connection cn = null;
        String sql = "";

        sql = "select    " +
               "T1.CD_MATRICULA         ," +
               "T1.SEQ_DEPENDENTE       ," +
               "T1.CD_CATEGORIA         ," +
               "T1.NOME_PESSOA          ," +
               "T1.DT_NASCIMENTO        ," +
               "T1.CD_SIT_PESSOA        ," +
               "T2.DESCR_CATEGORIA   ," +
               "T2.TP_CATEGORIA      ," +
               "T1.NR_CARTEIRA_PESSOA," +
               "T1.CD_TP_DEPENDENTE  ," +
               "T1.CD_CASO_ESPECIAL   ," +
               "T1.CD_SEXO     ," +
               "T1.dt_caso_especial " +
               "from " +
               "tb_pessoa t1, tb_categoria t2, tb_complemento t3 where " +
               "t1.cd_categoria     = t2.cd_categoria        and " +
               "t2.tp_categoria     = 'SO'                   and " +
               "(t1.seq_dependente   = 0                     OR  " +
               "(T1.DT_VALID_MATERIAL >= CONVERT(VARCHAR, getdate(), 101) and T1.DT_VALID_MATERIAL IS NOT NULL)) AND " +
               "t1.cd_matricula     = t3.cd_matricula        and " +
               "t3.seq_dependente   = 0      and " +
               "t1.cd_categoria     = t3.cd_categoria        AND " +
               "T1.CD_SIT_PESSOA    = 'NO' AND " +
               "T3.CD_VRF_EMPRESTIMO is null ";
        
        
            if(carteira >= 1 ){
                sql = sql + " AND T1.NR_CARTEIRA_PESSOA = " + carteira; 
            }                
            if(categoria >= 1 ){
                sql = sql + " AND T1.CD_CATEGORIA = " + categoria; 
            }              
            if(matricula >= 1 ){
                sql = sql + " AND T1.CD_MATRICULA = " + matricula; 
            }              
            if(!"".equals(nome)){
                sql = sql + " AND T1.NOME_PESSOA LIKE  '" + nome + "%'"; 
            }     
            
        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            cal = cn.prepareCall(sql);

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Socio s = new Socio();
                
                s.setMatricula(rs.getInt("CD_MATRICULA"));
                s.setSeqDependente(rs.getInt("SEQ_DEPENDENTE"));
                s.setIdCategoria(rs.getInt("CD_CATEGORIA"));
                s.setNome(rs.getString("NOME_PESSOA"));
                s.setCategoria(rs.getString("DESCR_CATEGORIA"));
                s.setTipoCategoria(rs.getString("TP_CATEGORIA"));
                
                

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
    /*
     * Preenche a flag alerta com o codigo de alerta correspondente 
     * a situacao especial, excluido, dependente com idade extrapolada,
     * possui embarcacao ou esta suspenso.
     */
    private static void processarAlertas(List<Socio> socios){

       for(Socio s : socios){
           
           /*
           
           a pedido da alice em 09/12/2016 não é para mostrar dependente com idade extrapolada
           
           if(s.getIdTipoDependente() == 3 || s.getIdTipoDependente() == 4){
               if(s.isMasculino()){
                   if(s.getDataNascimento() != null){
                       if(s.getCasoEspecial() == 0 || s.getCasoEspecial() != 'U'){
                           Calendar nowMenos21 = Calendar.getInstance();
                           nowMenos21.add(Calendar.YEAR, -21);
                           Calendar nascimento = Calendar.getInstance();
                           nascimento.setTime(s.getDataNascimento());
                           if(nowMenos21.compareTo(nascimento) >= 0){
                               s.alerta = 3;
                           }
                       }else{
                           if(s.getDataCasoEspecial() != null){
                               Calendar now = Calendar.getInstance();
                               Calendar especial = Calendar.getInstance();
                               especial.setTime(s.getDataCasoEspecial());
                               if(especial.after(now)){
                                   Calendar nowMenos25 = Calendar.getInstance();
                                   nowMenos25.add(Calendar.YEAR, -25);
                                   Calendar nascimento = Calendar.getInstance();
                                   nascimento.setTime(s.getDataNascimento());
                                   if(nowMenos25.compareTo(nascimento) >= 0){
                                       s.alerta = 3;
                                   }
                               }else{
                                   s.alerta = 3;
                               }
                           }else{
                               //dt caso especial == null
                               s.alerta = 3;
                           }
                       }
                   }else{
                       //dt nascimento == null
                       s.alerta = 3;
                   }
               }
           }
           */

           if(s.getSituacao().equals("CA")){
               s.alerta = 2;
           }else if(!s.getSituacao().equals("NO")){
               s.alerta = 1;
           }else if(s.getSituacao().equals("SU")){
               s.alerta = 5;
           }

           if(s.getSeqDependente() == 0 && s.alerta == 0){
               if(Socio.temBarco(s.getMatricula(), s.getIdCategoria())){
                   s.alerta = 4;
               }
           }
       }

    }

    private static boolean temBarco(int matricula, int idCategoria){
        String sql = "SELECT * FROM TB_BARCO WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ?";
        Connection cn = null;
        boolean temBarco = false;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);

            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                temBarco = true;
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

        return temBarco;
    }

    //usado no caso de uso 9080 para emissao de carteirinha
    public String descricaoCargoEspecial(){
        String sql = "SELECT T1.DESCR_CARGO_ESPECIAL FROM TB_CARGO_ESPECIAL T1 "
           + "WHERE T1.CD_CARGO_ESPECIAL = (SELECT T2.CD_CARGO_ESPECIAL "
           + "FROM TB_PESSOA T2 WHERE T2.CD_MATRICULA = ? "
           + " AND T2.CD_CATEGORIA = ? "
           + " AND T2.SEQ_DEPENDENTE = ?) ";
           
        String cargoEspecial = "Nenhum";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            p.setInt(3, seqDependente);

            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                cargoEspecial = rs.getString("DESCR_CARGO_ESPECIAL");
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

        return cargoEspecial;
    }
    
    //usado no caso de uso 9080 para emissao de carteirinha
    public String descricaoCategoria(){
        String sql = "SELECT * FROM TB_CATEGORIA WHERE CD_CATEGORIA = ?";
           
        String categoria = "";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, idCategoria);

            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                categoria = rs.getString(2);
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

        return categoria;
    }
    
     //usado no caso de uso 9080 para emissao de carteirinha
    public Date calcularDataVencimentoCarteirinha()throws VencimentoCarteirinhaException{

        Connection cn = null;
        Date vencimento = null;
        cn = Pool.getInstance().getConnection();
        try {
            
            CallableStatement c = cn.prepareCall("{call SP_CALCULA_DT_VENC_CARTEIRA(?, ?, ?)}");
            c.setInt(1, matricula);
            c.setInt(2, idCategoria);
            c.setInt(3, seqDependente);

            ResultSet rs = c.executeQuery();
            cn.commit();
            if (rs.next()) {
                String OK =  rs.getString("MSG");
                if("OK".equals(OK)){
                    vencimento = rs.getDate("DT_VENCIMENTO");
                }else{
                    throw new VencimentoCarteirinhaException(OK);
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

        return vencimento;
    }
    
    public void excluirFoto(Auditoria audit){

        Connection cn = null;
        String sql =  "DELETE FROM TB_foto_PESSOA "
            + " WHERE CD_MATRICULA   = ? "
            + " AND   CD_CATEGORIA   = ? "
            + " AND   SEQ_DEPENDENTE = ?";
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            p.setInt(3, seqDependente);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(matricula), String.valueOf(idCategoria), String.valueOf(seqDependente));
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
    
    public static int numeroPassaporte(int matricula, int dependente, int categoria){

        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        int nrPass = 0;
        try {
            
            String sql = "SELECT NR_PASSAPORTE FROM TB_MATRICULA_CURSO " +
                   " WHERE CD_MATRICULA = ? " + 
                   " AND  CD_CATEGORIA = ? " + 
                   " AND  SEQ_DEPENDENTE = ? " + 
                   " AND NR_PASSAPORTE IS NOT NULL";
            
            CallableStatement c = cn.prepareCall(sql);
            c.setInt(1, matricula);
            c.setInt(2, categoria);
            c.setInt(3, dependente);

            ResultSet rs = c.executeQuery();
            cn.commit();
            if (rs.next()) {
                nrPass = rs.getInt("NR_PASSAPORTE");
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
        
        return nrPass;
    }    
    
    public void atualizarFoto(File f){

        Connection cn = null;
        String sql =  "SELECT * FROM TB_foto_PESSOA "
            + " WHERE CD_MATRICULA   = ? "
            + " AND   CD_CATEGORIA   = ? "
            + " AND   SEQ_DEPENDENTE = ?";
        BufferedInputStream bin = null;
        
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            p.setInt(3, seqDependente);
            ResultSet rs = p.executeQuery();
            
            try{
                bin = new BufferedInputStream(new FileInputStream(f));
            }catch(FileNotFoundException e){
                log.severe(e.getMessage());
            }
                            
            if(rs.next()){
                
                rs.updateBinaryStream("FOTO_PESSOA", bin);
                rs.updateRow();
            }else{
                rs.moveToInsertRow();
                rs.updateInt("CD_MATRICULA", matricula);
                rs.updateInt("CD_CATEGORIA", idCategoria);
                rs.updateInt("SEQ_DEPENDENTE", seqDependente);
                rs.updateBinaryStream("FOTO_PESSOA", bin);
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
    
    public void atualizarAtestado(File f){

        Connection cn = null;
        String sql =  "SELECT * FROM TB_ATESTADO_MEDICO_PESSOA "
            + " WHERE CD_MATRICULA   = ? "
            + " AND   CD_CATEGORIA   = ? "
            + " AND   SEQ_DEPENDENTE = ?";
        BufferedInputStream bin = null;
        
        try {
            Long t = f.length();
            
            
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            p.setInt(3, seqDependente);
            ResultSet rs = p.executeQuery();
            
            try{    
                bin = new BufferedInputStream(new FileInputStream(f));
            }catch(FileNotFoundException e){
                log.severe(e.getMessage());
            }
                            
            if(rs.next()){
                if(f.length()>0){
                    rs.updateBinaryStream("FOTO_ATESTADO", bin);
                }
                
                if((dtValidadeAtestado == null)){
                    rs.updateDate("DT_VAL_ATESTADO", null);
                }else{
                    rs.updateDate("DT_VAL_ATESTADO", new java.sql.Date(dtValidadeAtestado.getTime()));
                }
                rs.updateRow();
            }else{
                rs.moveToInsertRow();
                rs.updateInt("CD_MATRICULA", matricula);
                rs.updateInt("CD_CATEGORIA", idCategoria);
                rs.updateInt("SEQ_DEPENDENTE", seqDependente);
                if((dtValidadeAtestado == null)){
                    rs.updateDate("DT_VAL_ATESTADO", null);
                }else{
                    rs.updateDate("DT_VAL_ATESTADO", new java.sql.Date(dtValidadeAtestado.getTime()));
                }
                if(f.length()>0){
                    rs.updateBinaryStream("FOTO_ATESTADO", bin);
                }
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
    
    public Image carregarFoto(){

        Connection cn = null;
        Image img = null;
        
        String sql =  "SELECT * FROM TB_foto_PESSOA "
            + " WHERE CD_MATRICULA   = ? "
            + " AND   CD_CATEGORIA   = ? "
            + " AND   SEQ_DEPENDENTE = ?";
        
        try {
            cn = PoolFoto.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            p.setInt(3, seqDependente);
            ResultSet rs = p.executeQuery();
                            
            if(rs.next()){
                try {
                    img = ImageIO.read(rs.getBinaryStream("FOTO_PESSOA"));
                } catch (IOException ex) {
                    log.severe(ex.getMessage());
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
        
        return img;

    }        
      
    public void carregarModalidadesEsportivas(){
        String sql = "SELECT T1.CD_MODALIDADE FROM TB_MODALIDADE T1, TB_MODALIDADE_TB_PESSOA T2 "
                + "WHERE T1.CD_MODALIDADE = T2.CD_MODALIDADE "
                + "AND T2.CD_MATRICULA = ? AND T2.SEQ_DEPENDENTE = ? AND T2.CD_CATEGORIA = ?";


        modalidadesEsportivas.clear();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, seqDependente);
            p.setInt(3, idCategoria);
            
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                modalidadesEsportivas.put(rs.getInt("CD_MODALIDADE"), rs.getString("CD_MODALIDADE"));
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }

    public void atualizarTaxasAdministrativas(Auditoria audit){
        String sql = "delete FROM TB_MODALIDADE_tb_pessoa where CD_MATRICULA = ? "
                + "AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";       
       
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, seqDependente);
            p.setInt(3, idCategoria);
            p.executeUpdate();

            sql = "insert into TB_MODALIDADE_tb_pessoa values(?, ?, ?, ?)";

            p = cn.prepareStatement(sql);
            for(int i : modalidadesEsportivas.keySet()){
                p.setInt(1, idCategoria);
                p.setInt(2, matricula);
                p.setInt(3, seqDependente);
                p.setInt(4, i);
                p.executeUpdate();
                audit.registrarMudanca(sql, String.valueOf(idCategoria), String.valueOf(matricula), String.valueOf(seqDependente), String.valueOf(i));
            }

            cn.commit();
        } catch (Exception e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

    }
    
    public boolean isMatriculaAutorizada(){
        boolean ret = false;
        String sql = "Select CD_VRF_ESCOLINHA from tb_complemento "
            + " where cd_matricula = ? and cd_categoria = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                ret = true;
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }

        return ret;
    }
    
    public boolean isPessoaOK(){
        boolean ret = false;
        String sql = "Select CD_SIT_PESSOA from tb_PESSOA "
            + " where cd_matricula = ? and cd_categoria = ? AND SEQ_DEPENDENTE = 0";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, matricula);
            p.setInt(2, idCategoria);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                if (rs.getString(1).equals("NO")){
                    ret = true;
                }
            }
        } catch (SQLException ex) {
            log.severe(ex.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }

        return ret;
    }

}
