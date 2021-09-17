package techsoft.clube;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;
import techsoft.carteirinha.JobCartaoNautica;
import techsoft.db.Pool;


public class CredencialNautica{
    private static final Logger log = Logger.getLogger("techsoft.clube.CredencialNautica");
    private CartaoNautica cartao;
    private JobCartaoNautica job;
    
    public CredencialNautica(CartaoNautica c, String iateHost, int iatePort, String iateApp){
        this.cartao = c;
        job = new JobCartaoNautica();
        job.iateHost = iateHost;
        job.iatePort = iatePort;
        job.iateApp = iateApp;
    }

    /*
     * Busca as informacoes que serao impressos na carteirinha e preenche
     * o object JobCarteirinha com essas informacoes. Por fim, chama o 
     * servidor de impressao da carteirinha passando o objeto JobCarteirinha
     * preenchido
     */
    public void emitir(){
        String carteirinhaHost = null;
        int carteirinhaPort = 49001;
        
        Connection cn = Pool.getInstance().getConnection();
        try {
            ResultSet rs = cn.createStatement().executeQuery("SELECT * FROM TB_Parametro_Sistema");
            if(rs.next()){
                carteirinhaHost = rs.getString("CARTEIRINHA_HOST");
                carteirinhaPort = rs.getInt("CARTEIRINHA_PORT");
            }else{
                log.severe("Não foi possível ler os dados CARTEIRINHA_HOST e CARTEIRINHA_PORT de TB_Parametro_Sistema");
                return;
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

        job.nome = cartao.getNome();
        job.clube = cartao.getClube();
        job.uf = cartao.getUf();
        job.numeracao = cartao.getNumeracao();
        
        try {        
            Socket sock = new Socket(carteirinhaHost, carteirinhaPort);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            out.writeObject(job);
            out.close();
        }catch(Exception ex) {
            log.severe(ex.getMessage());
        }

        
    }

}
