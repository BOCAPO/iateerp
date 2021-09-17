
package techsoft.tabelas;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;

public class Material {

    private int id;
    private String descricao;
    private BigDecimal valMaterial;
    private int pzPadraoDevolucao;
    private int qtEstoque;
    private int qtTotal;
    private BigDecimal vrDiaria;
    private int cdTxAdministrativa;
    private String deTxAdministrativa;
    private int qtDiasIniCob;
    private Date dtEmprestimo;
    private Date dtDevolucao;
    private String usuario;
    private String usuarioDevol;
    private String nomeSocio;
    private BigDecimal vrEmprestimo;

    private static final Logger log = Logger.getLogger("techsoft.tabelas.Material");

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao.trim();
    }
    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public BigDecimal getValMaterial(){
        return valMaterial;
    }
    public void setValMaterial(BigDecimal valMaterial) {
        this.valMaterial = valMaterial;
    }
    public int getPzPadraoDevolucao(){
        return pzPadraoDevolucao;
    }
    public void setPzPadraoDevolucao(int pzPadraoDevolucao) {
        this.pzPadraoDevolucao = pzPadraoDevolucao;
    }
    public int getQtEstoque(){
        return qtEstoque;
    }
    public void setQtEstoque(int qtEstoque) {
        this.qtEstoque = qtEstoque;
    }
    public int getQtTotal(){
        return qtTotal;
    }
    public void setQtTotal(int qtTotal) {
        this.qtTotal = qtTotal;
    }
    public BigDecimal getVrDiaria(){
        return vrDiaria;
    }
    public void setVrDiaria(BigDecimal vrDiaria) {
        this.vrDiaria = vrDiaria;
    }    
    public int getCdTxAdministrativa(){
        return cdTxAdministrativa;
    }
    public void setCdTxAdministrativa(int cdTxAdministrativa) {
        this.cdTxAdministrativa = cdTxAdministrativa;
    }    
    public String getDeTxAdministrativa() {
        return deTxAdministrativa;
    }
    public void setDeTxAdministrativa(String deTxAdministrativa) {
        this.deTxAdministrativa = deTxAdministrativa.trim();
    }
    
    public int getQtDiasIniCob(){
        return qtDiasIniCob;
    }    
    public void setQtDiasIniCob(int qtDiasIniCob) {
        this.qtDiasIniCob = qtDiasIniCob;
    }     
    
    public Date getDtEmprestimo() {
        return dtEmprestimo;
    }
    public void setDtEmprestimo(Date dtEmprestimo) {
        this.dtEmprestimo = dtEmprestimo;
    }
    public Date getDtDevolucao() {
        return dtDevolucao;
    }
    public void setDtDevolucao(Date dtDevolucao) {
        this.dtDevolucao = dtDevolucao;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getUsuarioDevol () {
        return usuarioDevol;
    }
    public void setUsuarioDevol(String usuarioDevol) {
        this.usuarioDevol = usuarioDevol;
    }
    public String getNomeSocio() {
        return nomeSocio;
    }
    public void setNomeSocio(String nomeSocio) {
        this.nomeSocio = nomeSocio.trim();
    }
    public BigDecimal getVrEmprestimo() {
        return vrEmprestimo;
    }
    public void setVrEmprestimo(BigDecimal vrEmprestimo) {
        this.vrEmprestimo = vrEmprestimo;
    }

    public static List<Material> listar(){
        ArrayList<Material> l = new ArrayList<Material>();
        String sql = "SELECT T1.CD_MATERIAL, T1.DESCR_MATERIAL, T1.VAL_MATERIAL, T1.PZ_PADRAO_DEVOLUCAO, T1.QT_ESTOQUE, T1.QT_TOTAL, T1.VR_DIARIA, T1.CD_TX_ADMINISTRATIVA, T1.QT_DIAS_INI_COB, T2.DESCR_TX_ADMINISTRATIVA, ISNULL(T1.VR_EMPRESTIMO, 0) VR_EMPRESTIMO FROM TB_MATERIAL T1, TB_TAXA_ADMINISTRATIVA T2 WHERE T1.CD_TX_ADMINISTRATIVA *= T2.CD_TX_ADMINISTRATIVA ORDER BY 2";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Material d = new Material();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.valMaterial = rs.getBigDecimal(3);
                d.pzPadraoDevolucao = rs.getInt(4);
                d.qtEstoque = rs.getInt(5);
                d.qtTotal = rs.getInt(6);
                d.vrDiaria = rs.getBigDecimal(7);
                d.cdTxAdministrativa = rs.getInt(8);
                d.qtDiasIniCob = rs.getInt(9);
                d.deTxAdministrativa = rs.getString(10);
                d.vrEmprestimo = rs.getBigDecimal("VR_EMPRESTIMO");
                
                l.add(d);
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

    public static Material getInstance(int id){
        Material d = null;
        String sql = "SELECT T1.CD_MATERIAL, T1.DESCR_MATERIAL, T1.VAL_MATERIAL, T1.PZ_PADRAO_DEVOLUCAO, T1.QT_ESTOQUE, T1.QT_TOTAL, T1.VR_DIARIA, T1.CD_TX_ADMINISTRATIVA, T1.QT_DIAS_INI_COB, T2.DESCR_TX_ADMINISTRATIVA, ISNULL(T1.VR_EMPRESTIMO, 0) VR_EMPRESTIMO FROM TB_MATERIAL T1, TB_TAXA_ADMINISTRATIVA T2 WHERE T1.CD_TX_ADMINISTRATIVA *= T2.CD_TX_ADMINISTRATIVA AND T1.CD_MATERIAL = ?";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                d = new Material();
                d.id = rs.getInt(1);
                d.descricao = rs.getString(2);
                d.valMaterial = rs.getBigDecimal(3);
                d.pzPadraoDevolucao = rs.getInt(4);
                d.qtEstoque = rs.getInt(5);
                d.qtTotal = rs.getInt(6);
                d.vrDiaria = rs.getBigDecimal(7);
                d.cdTxAdministrativa = rs.getInt(8);
                d.qtDiasIniCob = rs.getInt(9);
                d.deTxAdministrativa = rs.getString(10);
                d.vrEmprestimo = rs.getBigDecimal("VR_EMPRESTIMO");
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

    public void excluir(Auditoria audit){
        Connection cn = null;

        try {
            String sql = "DELETE FROM TB_MATERIAL WHERE CD_MATERIAL = ?";
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

    public void inserir(Auditoria audit)throws InserirException{
 
        ResultSet rs = null;
        Connection cn = null;
        
        if(id != 0 || descricao == null) return;
        
        try {
            cn = Pool.getInstance().getConnection();
            ParametroAuditoria par = new ParametroAuditoria();
            String sql = "INSERT INTO TB_MATERIAL (DESCR_MATERIAL, VAL_MATERIAL, TP_MATERIAL, PZ_PADRAO_DEVOLUCAO, QT_ESTOQUE, QT_TOTAL, VR_DIARIA, CD_TX_ADMINISTRATIVA, QT_DIAS_INI_COB, VR_EMPRESTIMO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement p = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, par.getSetParametro(descricao));
            p.setBigDecimal(2, par.getSetParametro(valMaterial));
            p.setString(3, par.getSetParametro("BD"));
            p.setInt(4, par.getSetParametro(pzPadraoDevolucao));
            p.setInt(5, par.getSetParametro(qtEstoque));
            p.setInt(6, par.getSetParametro(qtTotal));
            p.setBigDecimal(7, par.getSetParametro(vrDiaria));
            p.setInt(8, par.getSetParametro(cdTxAdministrativa));
            p.setInt(9, par.getSetParametro(qtDiasIniCob));
            p.setBigDecimal(10, par.getSetParametro(vrEmprestimo));
            
            p.executeUpdate();
            rs = p.getGeneratedKeys();

            if(rs.next()){
                id = rs.getInt(1);
                cn.commit();
                audit.registrarMudanca(sql, descricao);
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

    public void alterar(Auditoria audit){

        if(id == 0 || descricao == null) return;

        Connection cn = null;

        try {
            String sql = "UPDATE TB_MATERIAL SET DESCR_MATERIAL = ?, VAL_MATERIAL = ?, PZ_PADRAO_DEVOLUCAO = ?, QT_ESTOQUE = ?, QT_TOTAL = ?, VR_DIARIA = ?, CD_TX_ADMINISTRATIVA = ?, QT_DIAS_INI_COB  = ?, VR_EMPRESTIMO = ? WHERE CD_MATERIAL = ?";
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            p.setString(1, par.getSetParametro(descricao));
            p.setBigDecimal(2, par.getSetParametro(valMaterial));
            p.setInt(3, par.getSetParametro(pzPadraoDevolucao));
            p.setInt(4, par.getSetParametro(qtEstoque));
            p.setInt(5, par.getSetParametro(qtTotal));
            p.setBigDecimal(6, par.getSetParametro(vrDiaria));
            p.setInt(7, par.getSetParametro(cdTxAdministrativa));
            p.setInt(8, par.getSetParametro(qtDiasIniCob));
            p.setBigDecimal(9, par.getSetParametro(vrEmprestimo));
            
            p.setInt(10, par.getSetParametro(id));
            p.executeUpdate();

            cn.commit();
            audit.registrarMudanca(sql, descricao, String.valueOf(id));
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

    public static List<Material> listarEmprestimos(int matricula, int categoria, int seqDependente, String todos) {
        ArrayList<Material> l = new ArrayList<Material>();

        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        CallableStatement cal = null;
        
        try {
            cal = cn.prepareCall("{call SP_RECUPERA_MATERIAL_PESSOA (?, ?, ?)}");
            cal.setInt(1, matricula);
            cal.setInt(2, seqDependente);
            cal.setInt(3, categoria);
            
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                if ("S".equals(todos) || rs.getDate("DT_REAL_DEVOLUCAO") == null){
                    Material d = new Material();
                    d.id = rs.getInt("CD_SEQ_EMPRESTIMO");
                    d.descricao = rs.getString("DESCR_MATERIAL");
                    d.dtEmprestimo = rs.getDate("DT_EMPREST_PESSOA");
                    d.dtDevolucao = rs.getDate("DT_REAL_DEVOLUCAO");
                    d.usuario = rs.getString("USER_EMPRESTIMO");

                    l.add(d);
                }
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

    public static List<Material> listarHistEmprestimos(int matricula, int categoria, int seqDependente) {
        ArrayList<Material> l = new ArrayList<Material>();

        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        CallableStatement cal = null;
        String sql = "SELECT " +
                    "T1.DESCR_MATERIAL, " +
                    "T2.SEQ_EMPRESTIMO, " +
                    "T2.QT_EMPREST_PESSOA AS QT_EMPREST_PESSOA, " +
                    "(SELECT TOP 1 DT_EMPREST_PESSOA FROM TB_PESSOA_TB_MATERIAL T3 WHERE T2.SEQ_EMPRESTIMO = T3.SEQ_EMPRESTIMO) AS DT_EMPREST, " +
                    "(SELECT TOP 1 USER_EMPRESTIMO FROM TB_PESSOA_TB_MATERIAL T4 WHERE T2.SEQ_EMPRESTIMO = T4.SEQ_EMPRESTIMO) AS USER_EMPRESTIMO  " +
                 "From " +
                    "TB_MATERIAL T1,  " +
                    "TB_EMPRESTIMO_MATERIAL T2  " +
                 "Where  " +
                    "T1.CD_MATERIAL = T2.CD_MATERIAL AND " +
                    "T2.CD_MATRICULA   =  ? AND " +
                    "T2.SEQ_DEPENDENTE    =  ? AND " +
                    "T2.CD_CATEGORIA   =  ? " +
                 " ORDER BY DT_EMPREST DESC ";
        
        try {
            cal = cn.prepareCall(sql);
            cal.setInt(1, matricula);
            cal.setInt(2, seqDependente);
            cal.setInt(3, categoria);
            
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Material d = new Material();
                d.id = rs.getInt("SEQ_EMPRESTIMO");
                d.descricao = rs.getString("DESCR_MATERIAL");
                d.setQtTotal(rs.getInt("QT_EMPREST_PESSOA"));
                d.dtEmprestimo = rs.getDate("DT_EMPREST");
                d.usuario = rs.getString("USER_EMPRESTIMO");

                l.add(d);
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

    public static List<Material> listarHistDevolucoes(int matricula, int categoria, int seqDependente) {
        ArrayList<Material> l = new ArrayList<Material>();

        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        CallableStatement cal = null;
        String sql = "SELECT " +
                    "T1.DESCR_MATERIAL, " +
                    "QT_EMPREST_PESSOA AS QT_EMPREST_PESSOA, " +
                    "T2.DT_EMPREST_PESSOA, " +
                    "T2.USER_EMPRESTIMO, " +
                    "T2.DT_REAL_DEVOLUCAO," +
                    "T2.USER_DEVOLUCAO, " +
                    "T2.CD_SEQ_EMPRESTIMO " +
                 "From " +
                    "TB_MATERIAL T1, " +
                    "TB_PESSOA_TB_MATERIAL T2 " +
                 "Where " +
                    "T1.CD_MATERIAL    =  T2.CD_MATERIAL       AND " +
                    "T2.DT_REAL_DEVOLUCAO IS NOT NULL AND " +
                    "T2.CD_MATRICULA   =  ? AND " +
                    "T2.SEQ_DEPENDENTE    =  ? AND " +
                    "T2.CD_CATEGORIA   =  ? " +
                 " ORDER BY DT_EMPREST_PESSOA DESC ";
        
        try {
            cal = cn.prepareCall(sql);
            cal.setInt(1, matricula);
            cal.setInt(2, seqDependente);
            cal.setInt(3, categoria);
            
            ResultSet rs = cal.executeQuery();
            while (rs.next()) {
                Material d = new Material();
                d.id = rs.getInt("CD_SEQ_EMPRESTIMO");
                d.descricao = rs.getString("DESCR_MATERIAL");
                d.setQtTotal(rs.getInt("QT_EMPREST_PESSOA"));
                d.dtEmprestimo = rs.getDate("DT_EMPREST_PESSOA");
                d.usuario = rs.getString("USER_EMPRESTIMO");
                d.dtDevolucao = rs.getDate("DT_REAL_DEVOLUCAO");
                d.usuarioDevol = rs.getString("USER_DEVOLUCAO");

                l.add(d);
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

    public static Material getInstanciaDevolucao(int id) {
        ArrayList<Material> l = new ArrayList<Material>();

        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        CallableStatement cal = null;
        Material d = new Material();

        String sql = "SELECT " +
                    "T1.DESCR_MATERIAL, " +
                    "T2.QT_EMPREST_PESSOA, " +
                    "T2.DT_EMPREST_PESSOA, " +
                    "T2.DT_REAL_DEVOLUCAO," +
                    "RIGHT('00' + CONVERT(VARCHAR, T3.CD_CATEGORIA), 2) + '/' + " + 
                    "RIGHT('0000' + CONVERT(VARCHAR, T3.CD_MATRICULA), 4) + ' - ' + " + 
                    "T3.NOME_PESSOA NOME_PESSOA, " +
                    "ISNULL(T2.VR_DIARIA, 0) VR_DIARIA, " +
                    "ISNULL(T2.VR_RESTITUICAO, 0) VR_RESTITUICAO, " +
                    "ISNULL(T2.VR_EMPRESTIMO, 0) VR_EMPRESTIMO, " +
                    "T2.USER_DEVOLUCAO " +
                 "From " +
                    "TB_MATERIAL T1, " +
                    "TB_PESSOA_TB_MATERIAL T2, " +
                    "TB_PESSOA T3 " +
                 "Where " +
                    "T1.CD_MATERIAL    =  T2.CD_MATERIAL       AND " +
                    "T2.CD_MATRICULA =  T3.CD_MATRICULA       AND " +
                    "T2.CD_CATEGORIA    =  T3.CD_CATEGORIA       AND " +
                    "T2.SEQ_DEPENDENTE    =  T3.SEQ_DEPENDENTE       AND " +
                    "T2.CD_SEQ_EMPRESTIMO = ? ";
        
        try {
            cal = cn.prepareCall(sql);
            cal.setInt(1, id);
            
            ResultSet rs = cal.executeQuery();
            if (rs.next()) {
                d.descricao = rs.getString("DESCR_MATERIAL");
                d.setQtTotal(rs.getInt("QT_EMPREST_PESSOA"));
                d.dtEmprestimo = rs.getTimestamp("DT_EMPREST_PESSOA");
                d.dtDevolucao = rs.getTimestamp("DT_REAL_DEVOLUCAO");
                d.vrDiaria =  rs.getBigDecimal("VR_DIARIA");
                d.vrEmprestimo =  rs.getBigDecimal("VR_EMPRESTIMO");
                d.valMaterial =  rs.getBigDecimal("VR_RESTITUICAO");
                d.nomeSocio = rs.getString("NOME_PESSOA");
                d.usuarioDevol = rs.getString("USER_DEVOLUCAO");
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

        return d;
    }
    
    
    public void devolucao(Auditoria audit){

        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        CallableStatement cal = null;

        try {
            
            cal = cn.prepareCall("{call SP_DEVOLVE_MATERIAL (?, ?)}");
            cal.setInt(1, id);
            cal.setString(2, usuario);
            cal.executeUpdate();

            cn.commit();
            audit.registrarMudanca("{call SP_DEVOLVE_MATERIAL (?, ?)}", String.valueOf(id), usuario);
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
    
    public static int emprestimo(int matricula, int categoria, int seqDependente, int qtEmprestimo, String usuario, int idMaterial, String senha, BigDecimal vrEmprestimo, Auditoria audit){

        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        CallableStatement cal = null;
        ResultSet rs = null;
        int idEmprestimo=0;
        ParametroAuditoria par = new ParametroAuditoria();
        try {
            
            cal = cn.prepareCall("{call SP_INCLUI_EMPRESTIMO_MATERIAL (?, ?, ?, ?, ?, ?)}");
            
            cal.setInt(1, par.getSetParametro(idMaterial));
            cal.setInt(2, par.getSetParametro(matricula));
            cal.setInt(3, par.getSetParametro(seqDependente));
            cal.setInt(4, par.getSetParametro(categoria));
            cal.setInt(5, par.getSetParametro(qtEmprestimo));
            cal.setString(6, par.getSetParametro(senha));
            cal.execute();
            rs = cal.getResultSet();
            if (rs.next()){
                idEmprestimo = rs.getInt("SEQ_EMPRESTIMO");
                audit.registrarMudanca("{call SP_INCLUI_EMPRESTIMO_MATERIAL (?, ?, ?, ?, ?, ?)}", par.getParametroFinal());
            }
            
            for(int i=0;i<qtEmprestimo;i++){
                par.limpaParametro();
                cal = cn.prepareCall("{call SP_INCLUIR_MOV_MATERIAL (?, ?, ?, ?, 1, ?, ?, ?)}");
                cal.setInt(1, par.getSetParametro(idMaterial));
                cal.setInt(2, par.getSetParametro(matricula));
                cal.setInt(3, par.getSetParametro(seqDependente));
                cal.setInt(4, par.getSetParametro(categoria));
                cal.setString(5, par.getSetParametro(usuario));
                cal.setInt(6, par.getSetParametro(idEmprestimo));
                cal.setBigDecimal(7, par.getSetParametro(vrEmprestimo));
                cal.executeUpdate();
                
                cal = cn.prepareCall("{call SP_DIMINUI_QT_MAT_ESTOQUE (?)}");
                cal.setInt(1, idMaterial);
                cal.executeUpdate();
                audit.registrarMudanca("{call SP_INCLUIR_MOV_MATERIAL (?, ?, ?, ?, 1, ?, ?, ?)}", par.getParametroFinal());
                
            }
            
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
        
        return idEmprestimo;
    }


}
