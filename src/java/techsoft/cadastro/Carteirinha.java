package techsoft.cadastro;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import techsoft.carteirinha.DadosCarteirinha;
import techsoft.carteirinha.JobCarteirinhaEvolys;
import techsoft.carteirinha.JobCarteirinhaFargo;
import techsoft.clube.ImpressoraCartao;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.ParametroAuditoria;


public class Carteirinha{

    private static final Logger log = Logger.getLogger(Carteirinha.class.getCanonicalName());
    private Socio socio;
    private String login;//Login do usuario que esta emitindo a carteirinha
    private String numeroCarteirinha;
    private DadosCarteirinha dados = new DadosCarteirinha();
    private ImpressoraCartao imp;
    //JobCarteirinhaFargo jobFargo;
    //JobCarteirinhaEvolys jobEvolys;
    
    public Carteirinha(Socio s, String login, String dataVencimento, String iateHost, int iatePort, String iateApp, ImpressoraCartao imp){
        this.socio = s;
        this.login = login;
        dados.setDataVencimento(dataVencimento);
        dados.setIateHost(iateHost);
        dados.setIatePort(iatePort);
        dados.setIateApp(iateApp);
        this.imp = imp;
    }

    /*
     * Busca as informacoes que serao impressos na carteirinha e preenche
     * o object JobCarteirinha com essas informacoes. Por fim, chama o 
     * servidor de impressao da carteirinha passando o objeto JobCarteirinha
     * preenchido
     */
    public void emitir(String estacao)throws AtualizarCarteirinhaException{
        String gestao = null;
        
        Connection cn = Pool.getInstance().getConnection();
        try {
            CallableStatement c = cn.prepareCall("{call SP_ATUALIZA_NR_CART_IMPRESSA(?, ?, ?, ?, ?)}");
            ParametroAuditoria par = new ParametroAuditoria();
            c.setInt(1, par.getSetParametro(socio.getMatricula()));
            c.setInt(2, par.getSetParametro(socio.getSeqDependente()));
            c.setInt(3, par.getSetParametro(socio.getIdCategoria()));
            c.setString(4, par.getSetParametro(login));
            try {
                c.setDate(5, new java.sql.Date(par.getSetParametro(socio.calcularDataVencimentoCarteirinha()).getTime()));
            } catch (VencimentoCarteirinhaException ex) {
                Logger.getLogger(Carteirinha.class.getName()).log(Level.SEVERE, null, ex);
            }

            ResultSet rs = c.executeQuery();
            cn.commit();
            Auditoria audit = new Auditoria(login, "9080", estacao);
            audit.registrarMudanca("{call SP_ATUALIZA_NR_CART_IMPRESSA(?, ?, ?, ?, ?)}", par.getParametroFinal());

            if (rs.next()) {
                numeroCarteirinha = rs.getString("nr_Carteira");
                if(numeroCarteirinha.length() == 7){
                    atualizarNumeroCarteirinha();
                }
                dados.setAbreviacaoCategoria(rs.getString("ABREV_CATEGORIA"));
                dados.setCorTitular(rs.getString("cor_titular"));
                dados.setCorDependente(rs.getString("cor_dependente"));
            }else{
                log.severe("Não foi possível atualizar o Nr. Carteira.");
            }

            /*
             * Busca informacoes se a carteirinha eh de uma pessoa com cargo especial ou de gestao
             */
            String sql = "SELECT T3.DE_TIPO_CARGO, T1.DE_CATEGORIA, T1.COR_CARTEIRA, T1.COR_FONTE, T1.DE_GESTAO "
                    + "FROM TB_CARGO_ESPECIAL T1, TB_PESSOA T2, TB_TIPO_CARGO T3 "
                    + "WHERE T1.CD_CARGO_ESPECIAL = T2.CD_CARGO_ESPECIAL AND "
                    + "T1.COR_CARTEIRA IS NOT NULL AND T2.CD_CARGO_ATIVO = 'S' AND "
                    + "T2.CD_MATRICULA = " + socio.getMatricula() + " AND "
                    + "T2.CD_CATEGORIA = " + socio.getIdCategoria() + " AND "
                    + "T2.SEQ_DEPENDENTE = " + socio.getSeqDependente() + " AND "
                    + "T1.IC_TIPO_CARGO *= T3.CO_TIPO_CARGO";
            rs = cn.createStatement().executeQuery(sql);
            if(rs.next()){
                dados.setCargoEspecialCorCarteira(rs.getString("COR_CARTEIRA"));
                dados.setCargoEspecialCorFonte(rs.getString("COR_FONTE"));
                gestao = rs.getString("DE_GESTAO");
                dados.setCargoEspecial(true);
            }else{
                dados.setCargoEspecial(false);
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
        
        dados.setNome(socio.getNome());
        dados.setMatricula(socio.getMatricula());
        dados.setDescricaoCategoria(socio.descricaoCategoria());
        dados.setDescricaoCargoEspecial(socio.descricaoCargoEspecial().trim());
        if(gestao != null){
            dados.setDescricaoCargoEspecial(dados.getDescricaoCargoEspecial() + " - GESTÃO " + gestao);
        }
        dados.setTitular((socio.getSeqDependente() == 0));
        dados.setNumeroCarteirinha(numeroCarteirinha);
        dados.setUrlFotoSocio("/f?tb=6&mat=" + socio.getMatricula() + "&seq=" + socio.getSeqDependente() + "&cat=" + socio.getIdCategoria());
        

        try {        
            Socket sock = new Socket(imp.getIp(), imp.getPorta());
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            if(imp.getId() == 1){
                JobCarteirinhaEvolys job = new JobCarteirinhaEvolys();
                job.setDados(dados);
                out.writeObject(job);    
            }else{
                if(imp.getId() == 2){
                    JobCarteirinhaFargo job = new JobCarteirinhaFargo();
                    job.setDados(dados);
                    out.writeObject(job);    
                }
            }
            
            
            out.close();
        }catch(Exception ex) {
            log.severe(ex.getMessage());
        }
    }
    
    private void atualizarNumeroCarteirinha(){
        DecimalFormat f = new DecimalFormat("0000000000");
        String digitoCarteirinha = String.valueOf(Titular.digitoCarteirinha(f.format(Integer.valueOf(numeroCarteirinha))));
        numeroCarteirinha = f.format(Integer.valueOf(numeroCarteirinha + digitoCarteirinha));
        
        //Connection cn2 = Pool.getInstance().getConnection();
        Connection cn = Pool.getInstance().getConnection();
        try {
            String sql = "UPDATE TB_PESSOA SET NR_CARTEIRA_PESSOA = " + numeroCarteirinha
               + " WHERE CD_MATRICULA   = " + Integer.toString(socio.getMatricula())
               + " AND   CD_CATEGORIA   = " + Integer.toString(socio.getIdCategoria())
               + " AND   SEQ_DEPENDENTE = " + Integer.toString(socio.getSeqDependente());
            //CallableStatement c = cn.prepareCall(sql);
/*
            p.setInt(1, Integer.valueOf(numeroCarteirinha));
            p.setInt(2, socio.getMatricula());
            p.setInt(3, socio.getIdCategoria());
            p.setInt(4, socio.getSeqDependente());
*/
            cn.createStatement().executeUpdate(sql);

            PreparedStatement p = cn.prepareStatement("UPDATE TB_HIST_EMISSAO_CARTEIRA SET NR_CARTEIRA = ? WHERE NR_CARTEIRA = ?");
            p.setInt(1, Integer.valueOf(numeroCarteirinha));
            //guarda o numero da carteirinha sem o digito formatado em 10 casa decimais
            p.setString(2, f.format(Integer.valueOf(numeroCarteirinha.substring(0, numeroCarteirinha.length() - 1))));
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
