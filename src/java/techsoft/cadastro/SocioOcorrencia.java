package techsoft.cadastro;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.tabelas.TipoOcorrencia;

public class SocioOcorrencia {

    private static final Logger log = Logger.getLogger("techsoft.cadastro.SocioOcorrencia");
    
    private int id;
    private Socio socio;
    private TipoOcorrencia tipo;
    private Date dataInicio;
    private Date dataFim;
    private String descricao;

    public int getId() {
        return id;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public TipoOcorrencia getTipo() {
        return tipo;
    }

    public void setTipo(TipoOcorrencia tipo) {
        this.tipo = tipo;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public static List<SocioOcorrencia> listar(Socio socio){

        ArrayList<SocioOcorrencia> l = new ArrayList<SocioOcorrencia>();
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = cn.prepareCall("{call SP_RECUPERA_OCORRENCIA_PESSOA (?, ?, ?)}");
            cal.setInt(1, socio.getMatricula());
            cal.setInt(2, socio.getSeqDependente());
            cal.setInt(3, socio.getIdCategoria());

            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                SocioOcorrencia o = new SocioOcorrencia();
                
                o.socio = socio;
                o.id = rs.getInt("ID");
                o.tipo = TipoOcorrencia.getInstance(rs.getInt("CD_SEQ_TP_OCORRENCIA"));
                o.dataInicio = rs.getDate("DT_INICIO");
                o.dataFim = rs.getDate("DT_FIM");
                o.descricao = rs.getString("descr_ocorrencia");

                l.add(o);
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
    
    public static SocioOcorrencia getInstance(int id){
        SocioOcorrencia o = null;
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            ResultSet rs = cn.createStatement().executeQuery("SELECT * FROM TB_TIPO_OCORRENCIA_PESSOA_TB_P WHERE ID = " + id);
            while (rs.next()) {
                o = new SocioOcorrencia();
                
                o.id = rs.getInt("ID");
                o.tipo = TipoOcorrencia.getInstance(rs.getInt("CD_SEQ_TP_OCORRENCIA"));
                o.dataInicio = rs.getDate("DT_INICIO");
                o.dataFim = rs.getDate("DT_FIM");
                o.descricao = rs.getString("descr_ocorrencia");
                
                o.socio = Socio.getInstance(rs.getInt("CD_MATRICULA"), rs.getInt("SEQ_DEPENDENTE"), rs.getInt("CD_CATEGORIA"));
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

        return o;
    }
    
    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            cn.createStatement().executeUpdate("DELETE FROM TB_TIPO_OCORRENCIA_PESSOA_TB_P WHERE ID = " + id);
            cn.commit();
            cn.close();
            audit.registrarMudanca("DELETE FROM TB_TIPO_OCORRENCIA_PESSOA_TB_P WHERE ID = ", String.valueOf(id));
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
            CallableStatement cal = cn.prepareCall("{call SP_INCLUIR_OCORRENCIA_PESSOA (?, ?, ?, ?, ?, ?, ?)}");
            ParametroAuditoria parm = new ParametroAuditoria();
            cal.setInt(1, parm.getSetParametro(tipo.getId()));
            cal.setInt(2, parm.getSetParametro(socio.getMatricula()));
            cal.setInt(3, parm.getSetParametro(socio.getSeqDependente()));
            cal.setInt(4, parm.getSetParametro(socio.getIdCategoria()));
            cal.setDate(5, new java.sql.Date(parm.getSetParametro(dataInicio).getTime()));
            cal.setDate(6, new java.sql.Date(parm.getSetParametro(dataFim).getTime()));
            cal.setString(7, parm.getSetParametro(descricao));

            cal.executeUpdate();
            cn.commit();
            cn.close();

            audit.registrarMudanca("{call SP_INCLUIR_OCORRENCIA_PESSOA (?, ?, ?, ?, ?, ?, ?)}", parm.getParametroFinal());
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
