package techsoft.carteirinha;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    
    //http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.txt
    private static int port = 49001;
    private static String printer = null;
    private static String tipoMiniImp = null;
    private Socket client = null;
            
    public static void main(String[] args) throws Exception{
        if(args.length > 0){
            Server.port = Integer.parseInt(args[0]);
            try{
                Server.printer = args[1];
            }catch(Exception e){
                Server.printer = null;
            }
            try{
                Server.tipoMiniImp = args[1];
            }catch(Exception e){
                Server.tipoMiniImp = null;
            }
        }
        
        ServerSocket cn = new ServerSocket(port);   
        while(true){
            System.err.println("aguardando conexao na porta " + Server.port);
            Server s = new Server();
            s.client = cn.accept();
            System.err.println("conexao recebida");
            new Thread(s).start();
        }
        
    }

    @Override
    public void run(){
        try{
            System.err.println("run...");
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            TechsoftPrintService job = (TechsoftPrintService)in.readObject();
            System.err.println("job carregado");
            job.setPrinterName(Server.printer);
            System.err.println("IMPRESSORA: " + Server.printer);
            job.imprimir();
            client.close();
            System.err.println("conexao finalizada");
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    
    
}

