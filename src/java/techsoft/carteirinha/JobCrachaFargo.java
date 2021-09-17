package techsoft.carteirinha;

import impl.Fargo.CurrentActivity;
import impl.Fargo.PrintJob;
import impl.Fargo.PrinterInfo;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import techsoft.tabelas.FuncionarioHorario;


public class JobCrachaFargo implements TechsoftPrintService, Serializable{
    private static final long serialVersionUID = 20180101;
    private static final Logger log = Logger.getLogger("techsoft.carteirinha.JobCrachaFargo");
            
    private DadosCracha dados;
    Image img1 = null;
    Image img2 = null;
    Image imgFoto = null;
    String printer = null;
    
    @Override
    public void setPrinterName(String printerName) {
        this.printer = printerName;
    }
    public DadosCracha GetDados() {
        return dados;
    }
    public void setDados(DadosCracha dados) {
        this.dados = dados;
    }
    
    @Override
    public void imprimir(){        
        
        try{
            if(dados.isFuncionario()){
                img1 = ImageIO.read(new URL("http://" + dados.getIateHost() + ":" + dados.getIatePort() + dados.getIateApp() + "/img/cr_func_1.bmp"));
                img2 = ImageIO.read(new URL("http://" + dados.getIateHost() + ":" + dados.getIatePort() + dados.getIateApp() + "/img/cr_func_2.bmp"));        
            }else{
                img1 = ImageIO.read(new URL("http://" + dados.getIateHost() + ":" + dados.getIatePort() + dados.getIateApp() + "/img/cr_conc_1.bmp"));
                img2 = ImageIO.read(new URL("http://" + dados.getIateHost() + ":" + dados.getIatePort() + dados.getIateApp() + "/img/cr_conc_2.bmp"));        
            }
            imgFoto = ImageIO.read(new URL("http://" + dados.getIateHost() + ":" + dados.getIatePort() + dados.getIateApp() + dados.getUrlFotoFuncionario()));
            
        } catch (IOException ex) {
            log.severe(ex.getMessage());
        }

        BufferedImage imgFront = new BufferedImage(640, 1020, BufferedImage.TYPE_INT_RGB);
        BufferedImage imgBack = new BufferedImage(640, 1020, BufferedImage.TYPE_INT_RGB);
        Graphics g = imgFront.getGraphics();
        
        if(dados.isFuncionario()){
            img1 = img1.getScaledInstance(637, 1015, Image.SCALE_AREA_AVERAGING);
            g.drawImage(img1, 0, 0,  null);
            if (imgFoto != null){
                imgFoto = imgFoto.getScaledInstance(259, 354, Image.SCALE_AREA_AVERAGING);
                g.drawImage(imgFoto, 271, 165,  null);
            }
            
            g.setColor(Color.BLACK);
            g.setFont(Font.decode("Arial-PLAIN-28"));
            g.drawString("Nome:", 180, 605);
            g.setFont(Font.decode("Arial-BOLD-66"));
            g.drawString(dados.getPrimeiroNome(), 200, 670);

            g.setFont(Font.decode("Arial-PLAIN-28"));
            g.drawString("Setor:", 180, 750);            
            g.setFont(Font.decode("Arial-PLAIN-58"));
            g.drawString(dados.getSetor(), 200, 812);
            /*
             * Linha dummy. Não imprime nada, mas se tirar ela o último comando
             * de impressão vai para a pagina seguinte. Então o último comando é 
             * um pixel numa área fora do cartão, assim não imprime o último comando
             * na pagina errada.
             */
            //g.fillRect(0, 2502, 1, 1);
            g.dispose();
            
            g = imgBack.getGraphics();
            g.setColor(Color.BLACK);
            img2 = img2.getScaledInstance(637, 1015, Image.SCALE_AREA_AVERAGING);
            g.drawImage(img2, 0, 0,  null);
            g.setFont(Font.decode("Arial-PLAIN-28"));
            g.drawString("Nome:", 150, 210);
            g.setFont(Font.decode("Arial-PLAIN-32"));
            g.drawString(dados.getNome(), 165, 242);
            g.setFont(Font.decode("Arial-PLAIN-28"));
            g.drawString("Cargo/função:", 150, 280);            
            g.setFont(Font.decode("Arial-PLAIN-32"));
            g.drawString(dados.getCargo(), 165, 320);
            g.setFont(Font.decode("Arial-PLAIN-28"));
            g.drawString("Matrícula:", 150, 360);
            g.setFont(Font.decode("Arial-PLAIN-32"));
            g.drawString(String.valueOf(dados.getMatricula()), 165, 390);
            
            if(dados.getTipoSanguineo() != null && !dados.getTipoSanguineo().equals("")){
                g.setFont(Font.decode("Arial-PLAIN-28"));
                g.drawString("Tipo Sanguíneo:", 150, 430);
                g.setFont(Font.decode("Arial-PLAIN-32"));
                g.drawString(dados.getTipoSanguineo(), 165, 460);
            }
            
            g.setFont(Font.decode("C39P36DlTt-PLAIN-104"));
            g.drawString("!" + String.format("%1$09d", Integer.valueOf(dados.getNumeroCracha())) + "!", 20, 955);
            g.setFont(Font.decode("Arial-PLAIN-24"));
            g.drawString(String.format("%1$09d", Integer.valueOf(dados.getNumeroCracha())), 240, 997);
            /*
             * Linha dummy. Não imprime nada, mas se tirar ela o último comando
             * de impressão vai para a pagina seguinte. Então o último comando é 
             * um pixel numa área fora do cartão, assim não imprime o último comando
             * na pagina errada.
             */
            //g.fillRect(0, 2502, 1, 1);
            g.dispose();
        }else{
            g.setColor(Color.BLACK);
            img1 = img1.getScaledInstance(637, 1015, Image.SCALE_AREA_AVERAGING);
            g.drawImage(img1, 0, 0,  null);
            imgFoto = imgFoto.getScaledInstance(259, 354, Image.SCALE_AREA_AVERAGING);
            g.drawImage(imgFoto, 271, 94,  null);
            g.setFont(Font.decode("Times New Roman-PLAIN-32"));
            g.drawString("Nome:", 153, 500);
            g.setFont(Font.decode("Times New Roman-BOLD-64"));
            g.drawString(dados.getPrimeiroNome(), 153, 570);
            g.setFont(Font.decode("Times New Roman-PLAIN-32"));
            g.drawString("Nome Completo:", 153, 622);
            g.setFont(Font.decode("Times New Roman-BOLD-32"));
            g.drawString(dados.getNome(), 153, 657);
            g.setFont(Font.decode("Times New Roman-PLAIN-32"));
            g.drawString("Área:", 153, 728);
            g.setFont(Font.decode("Times New Roman-BOLD-32"));
            g.drawString(dados.getSetor(), 153, 764);
            g.setFont(Font.decode("Times New Roman-PLAIN-32"));
            g.drawString("Categoria:", 153, 824);
            g.setFont(Font.decode("Times New Roman-BOLD-32"));
            g.drawString(dados.getCargo(), 153, 860);
            g.setFont(Font.decode("Times New Roman-PLAIN-48"));
            g.drawString("Validade:", 153, 943);
            g.setFont(Font.decode("Times New Roman-BOLD-48"));
            
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            g.drawString(fmt.format(dados.getDataVencimento()), 366, 943);
            /*
             * Linha dummy. Não imprime nada, mas se tirar ela o último comando
             * de impressão vai para a pagina seguinte. Então o último comando é 
             * um pixel numa área fora do cartão, assim não imprime o último comando
             * na pagina errada.
             */
            //g.fillRect(0, 2502, 1, 1);
            g.dispose();
            
            g = imgBack.getGraphics();
            g.setColor(Color.BLACK);
            img2 = img2.getScaledInstance(637, 1015, Image.SCALE_AREA_AVERAGING);
            g.drawImage(img2, 0, 0,  null);
            
            //g.dispose();
            
            int y = 120;
            for(int i = 0; i < dados.getHorarios().length; i++){
                if(dados.getHorarios()[i] == null) continue;
                
                String dia = null;
                switch(i){
                    case 0: dia = "Segunda"; break;
                    case 1: dia = "Terça"; break;
                    case 2: dia = "Quarta"; break;
                    case 3: dia = "Quinta"; break;
                    case 4: dia = "Sexta"; break;
                    case 5: dia = "Sábado"; break;
                    case 6: dia = "Domingo"; break;
                }
                g.setFont(Font.decode("MS Sans Serif-BOLD-32"));
                g.drawString(dia, 141, y);
                g.setFont(Font.decode("MS Sans Serif-PLAIN-32"));
                g.drawString(dados.getHorarios()[i].getEntrada() + " às " + dados.getHorarios()[i].getSaida(), 377, y);
                y += 50;
            }
            
            g.setFont(Font.decode("C39P36DlTt-PLAIN-104"));
            g.drawString("!" + String.format("%1$09d", Integer.valueOf(dados.getNumeroCracha())) + "!", 20, 955);
            g.setFont(Font.decode("Arial-PLAIN-24"));
            g.drawString(String.format("%1$09d", Integer.valueOf(dados.getNumeroCracha())), 240, 997);
            /*
             * Linha dummy. Não imprime nada, mas se tirar ela o último comando
             * de impressão vai para a pagina seguinte. Então o último comando é 
             * um pixel numa área fora do cartão, assim não imprime o último comando
             * na pagina errada.
             */
            //g.fillRect(0, 2502, 1, 1);
            g.dispose();
        }
        
        
        File f1 = null;
        File f2 = null;
        try { 
            f1 = File.createTempFile("img", ".jpg");
            f2 = File.createTempFile("img", ".jpg");            
            ImageIO.write(imgFront, "jpg", f1);
            ImageIO.write(imgBack, "jpg", f2);
        }catch(IOException e){ 
            log.severe(e.getMessage());
        }
        
        PrintJob job = new PrintJob(this.printer);
        PrinterInfo printerInfo = new PrinterInfo(this.printer);        
        try{
            if (!job.open(this.printer)) {
                log.severe("Falha ao chamar PrintJob.open()");
            }

            CurrentActivity currentActivity = printerInfo.currentActivity();
            if(currentActivity == CurrentActivity.CurrentActivityUnknown){
                log.severe("Falha na comunicacao com a impressora");
            }        

            job.addPrintImageElement(f1.getAbsolutePath(), 1, 0, 0, 0);
            job.addPrintImageElement(f2.getAbsolutePath(), 1, 0, 0, 1);
            job.doPrint("Cracha " + dados.getNome());
            job.finishDoc();
        }finally{
            if(f1 != null) f1.delete();
            if(f2 != null) f2.delete();
            
            job.close();
        }
    }



}
