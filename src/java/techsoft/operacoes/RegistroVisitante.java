package techsoft.operacoes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;
import techsoft.tabelas.CrachaVisitante;
import techsoft.tabelas.Setor;

public class RegistroVisitante {
    private CrachaVisitante cracha;
    private Date entrada;
    private Date saida;
    private String usuarioEntrega;
    private String usuarioDevolucao;
    private Visitante visitante;
    private Setor setorVisitado;

    private static final Logger log = Logger.getLogger("techsoft.operacoes.RegistroVisitante");
    
    public CrachaVisitante getCracha() {
        return cracha;
    }

    public void setCracha(CrachaVisitante cracha) {
        this.cracha = cracha;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getSaida() {
        return saida;
    }

    public void setSaida(Date saida) {
        this.saida = saida;
    }

    public String getUsuarioEntrega() {
        return usuarioEntrega;
    }

    public void setUsuarioEntrega(String usuarioEntrega) {
        this.usuarioEntrega = usuarioEntrega;
    }

    public String getUsuarioDevolucao() {
        return usuarioDevolucao;
    }

    public void setUsuarioDevolucao(String usuarioDevolucao) {
        this.usuarioDevolucao = usuarioDevolucao;
    }

    public Visitante getVisitante() {
        return visitante;
    }

    public void setVisitante(Visitante visitante) {
        this.visitante = visitante;
    }

    public Setor getSetorVisitado() {
        return setorVisitado;
    }

    public void setSetorVisitado(Setor setorVisitado) {
        this.setorVisitado = setorVisitado;
    }
    
    public static List<RegistroVisitante> listar(CrachaVisitante cracha){
        ArrayList<RegistroVisitante> l = new ArrayList<RegistroVisitante>();
        String sql = "SELECT * FROM TB_VISITANTE T1, TB_REGISTRO_VISITANTE T2 "
                + "WHERE T1.CD_VISITANTE = T2.CD_VISITANTE AND NR_CRACHA = ?" 
                + " ORDER BY DT_ENTRADA ASC";
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            p.setInt(1, cracha.getId());
            
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                RegistroVisitante r = new RegistroVisitante();
                
                r.cracha = cracha;
                r.entrada = rs.getTimestamp("DT_ENTRADA");
                r.saida = rs.getTimestamp("DT_SAIDA");
                r.usuarioEntrega = rs.getString("USER_FUNC_ENTREGA");
                r.usuarioDevolucao = rs.getString("USER_FUNC_DEVOLUCAO");
                r.setorVisitado = Setor.getInstance(rs.getInt("CD_SETOR"));
                
                r.visitante = new Visitante();
                r.visitante.setId(rs.getInt("CD_VISITANTE"));
                r.visitante.setNome(rs.getString("NO_VISITANTE"));
                r.visitante.setDocumento(rs.getString("NR_DOCUMENTO"));
                r.visitante.setPlaca(rs.getString("DE_PLACA"));
                
                l.add(r);
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
