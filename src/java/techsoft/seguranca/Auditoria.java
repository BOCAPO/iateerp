
package techsoft.seguranca;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import techsoft.db.Pool;

/**
 * Classes de negocio que efetuam modificacoes no banco de dados
 * devem usar um objeto <code>Auditoria</code> para registrar as
 * modificacoes.
 */
public class Auditoria {
    private String login;
    private String app;
    private String ip;
    private static final Logger log = Logger.getLogger(Auditoria.class.getName());
    
    public Auditoria(HttpServletRequest request) {
        this.login = StringUtils.trimToEmpty(request.getUserPrincipal().getName());
        this.app = StringUtils.trimToEmpty(request.getParameter("app"));
        this.ip = StringUtils.trimToEmpty(request.getRemoteAddr());
    }

    public Auditoria(String login, String app, String estacao) {
        this.login = login;
        this.app = app;
        this.ip = estacao;
    }

    public String getLogin(){
        return login;
    }
    
    public void registrarMudanca(String sql, String... params){
        Connection cn = Pool.getInstance().getConnection();

        try {
            StringBuilder sb = new StringBuilder();
            CallableStatement cal = cn.prepareCall("{call SP_GRAVA_LOG (?, ?, ?, ?)}");
            cal.setString(1, login);
            cal.setInt(2, Integer.parseInt(app));

            sb.append(" [");
            for(String param : params){
                sb.append(param);
                sb.append(", ");
            }
            if(params.length > 0) sb.delete(sb.length() - 2, sb.length());
            sb.append("]");
            cal.setString(3, sql + sb.toString());
            cal.setString(4, (this.ip == null) ? "" : this.ip);

            cal.executeUpdate();
            cn.commit();
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

    public static List<RegistroAuditoria> consultarRegistros
            (String login, int permissao, Date dataInicio, Date dataFim, String det){

        ArrayList<RegistroAuditoria> l = new ArrayList<RegistroAuditoria>();
        Connection cn = null;
        int i = 0;
        String sql = "SELECT t1.dt_HORA_TRANSACAO, t2.CD_APLICACAO, "
                + "t2.descr_aplicacao, t3.NOME_USUARIO_SISTEMA, t1.de_atualizacao, t1.ed_Estacao "
                + "FROM TB_log_sistema t1, TB_CADASTRO_aplicacao t2, "
                + "TB_usuario_SISTEMA t3 "
                + "WHERE t1.CD_APLICACAO = t2.CD_APLICACAO "
                + "AND t1.USER_ACESSO_SISTEMA = t3.USER_ACESSO_SISTEMA ";

        if(login != null){
            sql += "and t3.USER_ACESSO_SISTEMA = ? ";
            i += 1;
        }

        if(permissao > 0){
           sql += "and t2.CD_APLICACAO = ? ";
           i += 2;
        }

        if(dataInicio != null){
           sql += "and t1.DT_HORA_TRANSACAO >= ? ";
           sql += "and t1.DT_HORA_TRANSACAO <= ? ";
           i += 4;
        }
        
        if(det != null){
            if(!det.equals("")){
                sql += "and t1.DE_ATUALIZACAO LIKE '"+det+"%' ";
            }
        }

        //a validacao no cliente pode ser violada, eh necessario validar aqui tambem
        if(i == 0){
            String err = "É preciso passar um parâmetro de filtro "
                    + "para consultar os registros de auditoria.";
            log.severe(err);
            throw new RuntimeException(err);
        }
        if(i >= 4 && (dataInicio == null || dataFim == null)){
            String err = "Deve ser informadas as datas de inicio "
                    + "e fim para consultar os registros de auditoria por perÃ­odo.";
            log.severe(err);
            throw new RuntimeException(err);
        }

        sql += "ORDER BY 1, 2, 4";

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);

            /*
             * OBS: Usei outra forma mais simples de processar os 
             * parametros em techsoft.operacoes -> AutorizacaoEmbarque.java
             */
            switch(i){
                case 1://soh login
                    p.setString(1, login);
                    break;
                case 2://soh app
                    p.setInt(1, permissao);
                    break;
                case 3://login + app
                    p.setString(1, login);
                    p.setInt(2, permissao);
                    break;
                case 4://soh periodo
                    p.setTimestamp(1, new Timestamp(dataInicio.getTime()));
                    p.setTimestamp(2, new Timestamp(dataFim.getTime()));
                    break;
                case 5://login + periodo
                    p.setString(1, login);
                    p.setTimestamp(2, new Timestamp(dataInicio.getTime()));
                    p.setTimestamp(3, new Timestamp(dataFim.getTime()));
                    break;
                case 6://app + periodo
                    p.setInt(1, permissao);
                    p.setTimestamp(2, new Timestamp(dataInicio.getTime()));
                    p.setTimestamp(3, new Timestamp(dataFim.getTime()));
                    break;
                case 7://tudo
                    p.setString(1, login);
                    p.setInt(2, permissao);
                    p.setTimestamp(3, new Timestamp(dataInicio.getTime()));
                    p.setTimestamp(4, new Timestamp(dataFim.getTime()));
            }

            ResultSet rs = p.executeQuery();
            while(rs.next()){
                l.add(new RegistroAuditoria(rs.getTimestamp(1),
                        new Permissao(rs.getInt(2), rs.getString(3)),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)));
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
    
    public static List<RegistroAuditoria> consultarRegistrosInternet
            (String login, int permissao, Date dataInicio, Date dataFim, String det){

        ArrayList<RegistroAuditoria> l = new ArrayList<RegistroAuditoria>();
        Connection cn = null;
        int i = 0;

        String sql = "select t1.dt_HORA_TRANSACAO, t2.CD_APLICACAO, t2.descr_aplicacao, "
            + "t3.NOME_USUARIO_SISTEMA, T1.DE_ATUALIZACAO, T1.ed_estacao from TB_log_sistema t1, TB_APLICACAO_INTERNET t2, TB_usuario_SISTEMA t3 "
            + "where t1.CD_APLICACAO = t2.CD_APLICACAO "
            + "and   t1.USER_ACESSO_SISTEMA = t3.USER_ACESSO_SISTEMA "
            + "and   t3.CD_MATRICULA IS NOT NULL ";
        
        
        if(login != null){
            sql += "and t3.USER_ACESSO_SISTEMA = ? ";
            i += 1;
        }

        if(permissao > 0){
           sql += "and t2.CD_APLICACAO = ? ";
           i += 2;
        }

        if(dataInicio != null){
           sql += "and t1.DT_HORA_TRANSACAO >= ? ";
           sql += "and t1.DT_HORA_TRANSACAO <= ? ";
           i += 4;
        }

        //a validacao no cliente pode ser violada, eh necessario validar aqui tambem
        if(i == 0){
            String err = "É preciso passar um parâmetro de filtro "
                    + "para consultar os registros de auditoria.";
            log.severe(err);
            throw new RuntimeException(err);
        }
        if(i >= 4 && (dataInicio == null || dataFim == null)){
            String err = "Deve ser informadas as datas de inicio "
                    + "e fim para consultar os registros de auditoria por perÃ­odo.";
            log.severe(err);
            throw new RuntimeException(err);
        }
        
        if(det != null){
            if(!det.equals("")){
                sql += "and t1.DE_ATUALIZACAO LIKE '"+det+"%' ";
            }
        }
        

        sql += "ORDER BY 1, 2, 4";

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);

            /*
             * OBS: Usei outra forma mais simples de processar os 
             * parametros em techsoft.operacoes -> AutorizacaoEmbarque.java
             */            
            switch(i){
                case 1://soh login
                    p.setString(1, login);
                    break;
                case 2://soh app
                    p.setInt(1, permissao);
                    break;
                case 3://login + app
                    p.setString(1, login);
                    p.setInt(2, permissao);
                    break;
                case 4://soh periodo
                    p.setTimestamp(1, new Timestamp(dataInicio.getTime()));
                    p.setTimestamp(2, new Timestamp(dataFim.getTime()));
                    break;
                case 5://login + periodo
                    p.setString(1, login);
                    p.setTimestamp(2, new Timestamp(dataInicio.getTime()));
                    p.setTimestamp(3, new Timestamp(dataFim.getTime()));
                    break;
                case 6://app + periodo
                    p.setInt(1, permissao);
                    p.setTimestamp(2, new Timestamp(dataInicio.getTime()));
                    p.setTimestamp(3, new Timestamp(dataFim.getTime()));
                    break;
                case 7://tudo
                    p.setString(1, login);
                    p.setInt(2, permissao);
                    p.setTimestamp(3, new Timestamp(dataInicio.getTime()));
                    p.setTimestamp(4, new Timestamp(dataFim.getTime()));
            }

            ResultSet rs = p.executeQuery();
            while(rs.next()){
                l.add(new RegistroAuditoria(rs.getTimestamp(1),
                        new Permissao(rs.getInt(2), rs.getString(3)),
                        rs.getString(4), rs.getString(5), rs.getString(6)));
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
