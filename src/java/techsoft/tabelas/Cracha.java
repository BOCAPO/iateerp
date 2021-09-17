package techsoft.tabelas;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;
import techsoft.carteirinha.DadosCracha;
import techsoft.carteirinha.JobCarteirinhaEvolys;
import techsoft.carteirinha.JobCarteirinhaFargo;
import techsoft.carteirinha.JobCrachaEvolys;
import techsoft.carteirinha.JobCrachaFargo;
import techsoft.clube.ImpressoraCartao;
import techsoft.db.Pool;


public class Cracha{
    private static final Logger log = Logger.getLogger("techsoft.tabelas.Cracha");
    private Funcionario funcionario;
    private DadosCracha dados = new DadosCracha();
    private ImpressoraCartao imp;
    
    public Cracha(Funcionario f, Date dataVencimento, String iateHost, int iatePort, String iateApp, ImpressoraCartao imp){
        this.funcionario = f;
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
    public void emitir(){
        
        Connection cn = Pool.getInstance().getConnection();
        try {
            CallableStatement c = cn.prepareCall("{call SP_ATUALIZA_NR_CRACHA_IMPRESSO(?, ?)}");
            c.setInt(1, funcionario.getId());
            if(dados.getDataVencimento() == null){
                c.setNull(2, java.sql.Types.DATE);
            }else{
                c.setDate(2, new java.sql.Date(dados.getDataVencimento().getTime()));
            }
            
            ResultSet rs = c.executeQuery();
            rs.next();
            dados.setNumeroCracha(rs.getString(1));
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

        dados.setPrimeiroNome(funcionario.getPrimeiroNome());
        dados.setNome(funcionario.getNome());
        dados.setMatricula(funcionario.getMatricula());
        dados.setSetor(Setor.getInstance(funcionario.getIdSetor()).getDescricao());
        dados.setCargo(Cargo.getInstance(funcionario.getIdCargo()).getDescricao());
        dados.setTipoSanguineo(funcionario.getSangue());
        dados.setFuncionario(funcionario.getTipo().equalsIgnoreCase("F"));
        if(!dados.isFuncionario()){
            dados.setHorarios(funcionario.getHorarios());
        }
        dados.setUrlFotoFuncionario("/f?tb=5&cd=" + String.valueOf(funcionario.getId()));
        
        try {        
            Socket sock = new Socket(imp.getIp(), imp.getPorta());
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            if(imp.getId() == 1){
                JobCrachaEvolys job = new JobCrachaEvolys();
                job.setDados(dados);
                out.writeObject(job);    
            }else{
                if(imp.getId() == 2){
                    JobCrachaFargo job = new JobCrachaFargo();
                    job.setDados(dados);
                    out.writeObject(job);    
                }
            }

            out.close();
        }catch(Exception ex) {
            log.severe(ex.getMessage());
        }

        
    }

}
