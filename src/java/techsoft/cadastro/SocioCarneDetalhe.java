package techsoft.cadastro;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import techsoft.db.Pool;

public class SocioCarneDetalhe {

    private static final Logger log = Logger.getLogger("techsoft.cadastro.SocioCarneDetalhe");
    
    String nomePessoa;
    String descricaoTaxa;
    float valor;
    float encargos;
    float descontos;
    float valorPago;
    String situacao;

    public static Logger getLog() {
        return log;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public String getDescricaoTaxa() {
        return descricaoTaxa;
    }

    public float getValor() {
        return valor;
    }

    public float getEncargos() {
        return encargos;
    }

    public float getDescontos() {
        return descontos;
    }

    public float getValorPago() {
        return valorPago;
    }

    public String getSituacao() {
        return situacao;
    }

    public static List<SocioCarneDetalhe> listar(int id){

        ArrayList<SocioCarneDetalhe> l = new ArrayList<SocioCarneDetalhe>();
        
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();
            //TOTAL = PARCELAS DE CURSOS + PARCELAS ADMINISTRATIVAS
            
            //PARCELAS DE CURSOS
            String sql = "select t1.val_parc_cur, t1.val_encar_parc_cur, t1.val_desconto_parc_cur, t1.val_pgto_parc_cur, t1.cd_sit_parc_cur, t2.descr_curso, t4.nome_pessoa "
                    + " FROM TB_PARCELA_CURSO T1, TB_CURSO T2, TB_TURMA T3, TB_PESSOA T4 WHERE "
                    + "T1.seq_carne = " + id + " AND T1.SEQ_TURMA = T3.SEQ_TURMA "
                    + " AND T1.CD_MATRICULA = T4.CD_MATRICULA AND T1.CD_CATEGORIA = T4.CD_CATEGORIA "
                    + " AND T1.SEQ_DEPENDENTE = T4.SEQ_DEPENDENTE AND T3.CD_CURSO = T2.CD_CURSO";

            ResultSet rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                SocioCarneDetalhe d = new SocioCarneDetalhe();
                
                d.nomePessoa = rs.getString("nome_pessoa");
                d.descricaoTaxa = rs.getString("descr_curso");
                d.valor = rs.getFloat("val_parc_cur");
                d.encargos = rs.getFloat("val_encar_parc_cur");
                d.descontos = rs.getFloat("val_desconto_parc_cur");
                d.valorPago = rs.getFloat("val_pgto_parc_cur");
                d.situacao = rs.getString("cd_sit_parc_cur");

                l.add(d);
            }
            
            //PARCELAS ADMINISTRATIVAS
            sql = "select t1.val_parc_ADM, t1.val_encarGO_ADM, t1.val_desconto_ADM, t1.val_pgto_parc_ADM, "
                    + "t1.cd_sit_parc_ADM, t2.descr_TX_ADMINISTRATIVA, t3.NOME_PESSOA "
                    + "FROM TB_PARCELA_ADMINISTRATIVA T1, TB_TAXA_ADMINISTRATIVA T2, TB_PESSOA T3 WHERE "
                    + "T1.seq_carne = " + id + " AND T1.CD_MATRICULA = T3.CD_MATRICULA "
                    + " AND T1.CD_CATEGORIA = T3.CD_CATEGORIA AND T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE"
                    + " AND T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA ";
            rs = cn.createStatement().executeQuery(sql);
            while (rs.next()) {
                SocioCarneDetalhe d = new SocioCarneDetalhe();
                
                d.nomePessoa = rs.getString("nome_pessoa");
                d.descricaoTaxa = rs.getString("DESCR_TX_ADMINISTRATIVA");
                d.valor = rs.getFloat("VAL_PARC_adm");
                d.encargos = rs.getFloat("VAL_ENCARgo_adm");
                d.descontos = rs.getFloat("VAL_DESCONTO_adm");
                d.valorPago = rs.getFloat("VAL_PGTO_PARC_ADM");
                d.situacao = rs.getString("cd_sit_parc_adm");

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

    
}
