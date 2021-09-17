package techsoft.operacoes;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.cadastro.Socio;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class AutorizacaoEmbarque implements Serializable{

    private int id;
    private long numAutorizacao;
    private Socio responsavel;
    private Date dataEmissao;
    private Date dataValidade;
    private Date dataUtilizacao;
    private String embarcacao;
    private int capacidade;
    private String convidado;
    private String situacao;
    private Date dtNascimento;
    private String cpfConvidado;
    private String docEstrangeiro;
    
    private static final Logger log = Logger.getLogger("techsoft.operacoes.AutorizacaoEmbarque");
    
    public int getId() {
        return id;
    }

    public long getNumAutorizacao() {
        return numAutorizacao;
    }

    public Socio getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Socio responsavel) {
        this.responsavel = responsavel;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Date getDataUtilizacao() {
        return dataUtilizacao;
    }

    public String getEmbarcacao() {
        return embarcacao;
    }

    public void setEmbarcacao(String embarcacao) {
        this.embarcacao = embarcacao;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getConvidado() {
        return convidado;
    }

    public void setConvidado(String convidado) {
        this.convidado = convidado;
    }

    public String getSituacao() {
        return situacao;
    }
    public Date getDtNascimento() {
        return dtNascimento;
    }
    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }
    public String getCpfConvidado() {
        return cpfConvidado;
    }
    public void setCpfConvidado(String cpfConvidado) {
        this.cpfConvidado = cpfConvidado;
    }
    public String getDocEstrangeiro() {
        return docEstrangeiro;
    }
    public void setDocEstrangeiro(String docEstrangeiro) {
        this.docEstrangeiro = docEstrangeiro;
    }

    public static List<AutorizacaoEmbarque> consultar(
        long numAutorizacao, 
        Date dataEmissao, 
        Date dataValidade, 
        String responsavel, 
        String convidado){

        ArrayList<AutorizacaoEmbarque> l = new ArrayList<AutorizacaoEmbarque>();
        Connection cn = null;
        int i = 0;//contador de parametros
        String sql = "SELECT "
            + "T1.*, "
            + "T2.DESCR_CATEGORIA "
         + "FROM "
            + "TB_AUTORIZACAO_EMBARQUE T1, "
            + "TB_CATEGORIA T2 "
         + "WHERE "
            + "T1.CD_CATEGORIA = T2.CD_CATEGORIA ";

        if(numAutorizacao > 0){
            sql += " AND T1.NR_AUT_EMBARQUE = ? ";
            i++;
        }
        if(dataValidade != null){
            sql += " AND T1.DT_VALIDADE = ?";
            i++;
        }
        if(dataEmissao != null){
            sql += " AND T1.DT_EMISSAO >= ? AND T1.DT_EMISSAO <= ? ";//entre 00:00:00 e 23:59:59
            i++;
        }        
        if(convidado != null && !convidado.trim().equals("")){
            sql += " AND T1.NOME_CONVIDADO LIKE ?";//toUpperCase() post match
            i++;
        }
        if(responsavel != null && !responsavel.trim().equals("")){
            sql += " AND T1.NOME_SOCIO LIKE ?";//toUpperCase() post match
            i++;
        }


        //a validacao no cliente pode ser violada, eh necessario validar aqui tambem
        if(i == 0){
            String err = "Informe algum filtro para pesquisa das Autorizações de Embarque";
            log.severe(err);
            throw new RuntimeException(err);
        }

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);

            i = 1;
            if(numAutorizacao > 0){
                p.setLong(i++, numAutorizacao);
            }
            if(dataValidade != null){
                p.setDate(i++, new java.sql.Date(dataValidade.getTime()));
            }
            if(dataEmissao != null){               
                p.setTimestamp(i++, new java.sql.Timestamp(dataEmissao.getTime()));
                Calendar c = Calendar.getInstance();
                c.setTime(dataEmissao);
                c.add(Calendar.HOUR_OF_DAY, 23);
                c.add(Calendar.MINUTE, 59);
                c.add(Calendar.SECOND, 59);
                p.setTimestamp(i++, new java.sql.Timestamp(c.getTime().getTime()));
            }        
            if(convidado != null && !convidado.trim().equals("")){
                p.setString(i++, convidado.toUpperCase() + "%");
            }
            if(responsavel != null && !responsavel.trim().equals("")){
                p.setString(i++, responsavel.toUpperCase() + "%");
            }
        
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                AutorizacaoEmbarque a = new AutorizacaoEmbarque();
                Socio s = new Socio();
                s.setNome(rs.getString("NOME_SOCIO"));
                s.setCategoria(rs.getString("descr_categoria"));
                a.id = rs.getInt("SEQ_AUTORIZACAO");
                a.responsavel = s;
                a.capacidade = rs.getInt("QT_CAPACIDADE");
                a.convidado = rs.getString("NOME_CONVIDADO");
                a.dataValidade = rs.getDate("DT_VALIDADE");
                a.dataEmissao = rs.getDate("DT_EMISSAO");
                a.situacao = rs.getString("CD_SIT_AUT_EMBARQUE");
                a.numAutorizacao = rs.getLong("NR_AUT_EMBARQUE");
                a.dataUtilizacao = rs.getDate("DT_UTILIZACAO");
                a.embarcacao = rs.getString("DE_EMBARCACAO");
                a.dtNascimento = rs.getDate("DT_NASCIMENTO");
                a.cpfConvidado = rs.getString("CPF_CONVIDADO");
                a.docEstrangeiro = rs.getString("NR_DOC_ESTRANGEIRO");
                
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
    
    public static AutorizacaoEmbarque getInstance(int id){
        AutorizacaoEmbarque a = null;
        String sql = "SELECT * FROM TB_AUTORIZACAO_EMBARQUE WHERE SEQ_AUTORIZACAO = ?";
        
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                a = new AutorizacaoEmbarque();
                Socio s = Socio.getInstance(rs.getInt("CD_MATRICULA"), rs.getInt("SEQ_DEPENDENTE"), rs.getInt("CD_CATEGORIA"));
                a.id = rs.getInt("SEQ_AUTORIZACAO");
                a.responsavel = s;
                a.capacidade = rs.getInt("QT_CAPACIDADE");
                a.convidado = rs.getString("NOME_CONVIDADO");
                a.dataValidade = rs.getDate("DT_VALIDADE");
                a.dataEmissao = rs.getDate("DT_EMISSAO");
                a.situacao = rs.getString("CD_SIT_AUT_EMBARQUE");
                a.numAutorizacao = rs.getLong("NR_AUT_EMBARQUE");
                a.dataUtilizacao = rs.getDate("DT_UTILIZACAO");
                a.embarcacao = rs.getString("DE_EMBARCACAO");
                a.dtNascimento = rs.getDate("DT_NASCIMENTO");
                a.cpfConvidado = rs.getString("CPF_CONVIDADO");
                a.docEstrangeiro = rs.getString("NR_DOC_ESTRANGEIRO");
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

        return a;
    }

    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "UPDATE TB_AUTORIZACAO_EMBARQUE"
                    + " SET CD_SIT_AUT_EMBARQUE = 'CA' WHERE SEQ_AUTORIZACAO = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, String.valueOf(id));
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
    
    public static void inserir(List<AutorizacaoEmbarque> autorizacoes, Auditoria audit){

        Connection cn = null;
        StringBuilder sb = new StringBuilder();
        String sql = "";
        ParametroAuditoria par = new ParametroAuditoria();

        try {
            cn = Pool.getInstance().getConnection();
            for(AutorizacaoEmbarque a : autorizacoes){
                CallableStatement c = cn.prepareCall("{call SP_GERA_NR_AUTORIZACAO_EMBARQUE()}");
                ResultSet rs = c.executeQuery();
                rs.next();
                int numAutorizacao = Integer.parseInt(rs.getString(1));
                
                sql = "INSERT INTO TB_AUTORIZACAO_EMBARQUE ("
                    + "CD_MATRICULA, "
                    + "CD_CATEGORIA, "
                    + "SEQ_DEPENDENTE, "
                    + "NOME_SOCIO, "
                    + "NOME_CONVIDADO, "
                    + "DE_EMBARCACAO, "
                    + "QT_CAPACIDADE, "
                    + "DT_EMISSAO, "
                    + "DT_VALIDADE, "
                    + "DT_UTILIZACAO, "
                    + "NR_AUT_EMBARQUE, "
                    + "CD_SIT_AUT_EMBARQUE, "
                    + "DT_NASCIMENTO, "
                    + "CPF_CONVIDADO, "
                    + "NR_DOC_ESTRANGEIRO) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE(), ?, NULL, ?, 'NU', ?, ?, ?)";
                PreparedStatement p = cn.prepareStatement(sql);
                
                p.setInt(1, par.getSetParametro(a.responsavel.getMatricula()));
                p.setInt(2, par.getSetParametro(a.responsavel.getIdCategoria()));
                p.setInt(3, par.getSetParametro(a.responsavel.getSeqDependente()));
                p.setString(4, par.getSetParametro(a.responsavel.getNome()));
                
                if(a.convidado == null || a.convidado.equals("")){
                    p.setNull(5, java.sql.Types.VARCHAR);
                    par.getSetNull();
                }else{
                    p.setString(5, par.getSetParametro(a.convidado));
                }
                
                p.setString(6, par.getSetParametro(a.embarcacao));
                p.setInt(7, par.getSetParametro(a.capacidade));
                p.setDate(8, new java.sql.Date(par.getSetParametro(a.dataValidade.getTime())));
                p.setInt(9, par.getSetParametro(numAutorizacao));
                if(a.dtNascimento == null || a.dtNascimento.equals("")){
                    p.setNull(10, java.sql.Types.DATE);
                    par.getSetNull();
                }else{
                    p.setDate(10, new java.sql.Date(par.getSetParametro(a.dtNascimento.getTime())));
                }

                if(a.cpfConvidado == null || a.cpfConvidado.equals("")){
                    p.setNull(11, java.sql.Types.VARCHAR);
                    par.getSetNull();
                }else{
                    p.setString(11, par.getSetParametro(a.cpfConvidado));
                }
                
                if(a.docEstrangeiro == null || a.docEstrangeiro.equals("")){
                    p.setNull(12, java.sql.Types.VARCHAR);
                    par.getSetNull();
                }else{
                    p.setString(12, par.getSetParametro(a.docEstrangeiro));
                }

                p.executeUpdate();
                
            }

            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
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

        Connection cn = null;
        ParametroAuditoria par = new ParametroAuditoria();

        try {
            String sql = "UPDATE TB_AUTORIZACAO_EMBARQUE SET "
               + "NOME_CONVIDADO = ?, DE_EMBARCACAO = ?, QT_CAPACIDADE = ?, "
               + "DT_VALIDADE = ?, DT_NASCIMENTO = ?, CPF_CONVIDADO = ?, NR_DOC_ESTRANGEIRO = ? " 
               + "WHERE SEQ_AUTORIZACAO = ?";
            
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            
            if(convidado == null || convidado.equals("")){
                p.setNull(1, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(1, par.getSetParametro(convidado));
            }
            p.setString(2, par.getSetParametro(embarcacao));
            p.setInt(3, par.getSetParametro(capacidade));
            p.setDate(4, new java.sql.Date(par.getSetParametro(dataValidade).getTime()));            
            
            if(dtNascimento == null || dtNascimento.equals("")){
                p.setNull(5, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                p.setDate(5, new java.sql.Date(par.getSetParametro(dtNascimento.getTime())));
            }

            if(cpfConvidado == null || cpfConvidado.equals("")){
                p.setNull(6, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(6, par.getSetParametro(cpfConvidado));
            }
            if(docEstrangeiro == null || docEstrangeiro.equals("")){
                p.setNull(7, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(7, par.getSetParametro(docEstrangeiro));
            }
            
            p.setInt(8, par.getSetParametro(id));

            
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, par.getParametroFinal());
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
