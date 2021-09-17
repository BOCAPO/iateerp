package techsoft.carteirinha;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.JobAttributes;
import java.awt.PageAttributes;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import techsoft.tabelas.FuncionarioHorario;


public class JobCrachaEvolys implements TechsoftPrintService, Serializable{
    private static final long serialVersionUID = 20130602;
    private static final Logger log = Logger.getLogger("techsoft.carteirinha.JobCracha");

    private DadosCracha dados;
    Image img1 = null;
    Image img2 = null;
    Image imgFoto = null;
    String printer = null;
    
    @Override
    public void setPrinterName(String printerName) {
        this.printer = printerName;
    }
    public DadosCracha getDados() {
        return dados;
    }
    public void setDados(DadosCracha dados) {
        this.dados = dados;
    }
    
    @Override
    public void imprimir(){        
        //System.err.println("CHEGOU1 ");
        JobAttributes ja = new JobAttributes();
        ja.setDialog(JobAttributes.DialogType.NONE);
        System.err.println("impressora: " + printer);
        if(printer != null){
            ja.setPrinter(printer);
        }        
        //System.err.println("CHEGOU2 ");
        PageAttributes pa = new PageAttributes();
        pa.setColor(PageAttributes.ColorType.COLOR);
        pa.setPrintQuality(PageAttributes.PrintQualityType.HIGH);
        pa.setOrientationRequested(PageAttributes.OrientationRequestedType.PORTRAIT);
        pa.setMedia(PageAttributes.MediaType.ISO_A4);
        pa.setPrinterResolution(300);
        
        PrintJob job = Toolkit.getDefaultToolkit().getPrintJob(new Frame(), "Cracha", ja, pa);
        //System.err.println("CHEGOU3 ");

        try{
            if(getDados().isFuncionario()){
                img1 = ImageIO.read(new URL("http://" + getDados().getIateHost() + ":" + getDados().getIatePort()+ getDados().getIateApp() + "/img/cr_func_1.bmp"));
                img2 = ImageIO.read(new URL("http://" + getDados().getIateHost() + ":" + getDados().getIatePort() + getDados().getIateApp() + "/img/cr_func_2.bmp"));        
            }else{
                img1 = ImageIO.read(new URL("http://" + getDados().getIateHost() + ":" + getDados().getIatePort() + getDados().getIateApp() + "/img/cr_conc_1.bmp"));
                img2 = ImageIO.read(new URL("http://" + getDados().getIateHost() + ":" + getDados().getIatePort() + getDados().getIateApp() + "/img/cr_conc_2.bmp"));        
            }
            imgFoto = ImageIO.read(new URL("http://" + getDados().getIateHost() + ":" + getDados().getIatePort() + getDados().getIateApp() + getDados().getUrlFotoFuncionario()));
        } catch (IOException ex) {
            log.severe(ex.getMessage());
        }

        //System.err.println("CHEGOU4.0 ");
        Graphics g = job.getGraphics();
        //System.err.println("CHEGOU4.1 ");
        if(getDados().isFuncionario()){
            //System.err.println("CHEGOU5 ");
            img1 = img1.getScaledInstance(637, 1015, Image.SCALE_AREA_AVERAGING);
            //System.err.println("CHEGOU5.1 ");
            g.drawImage(img1, 0, 0,  null);
            imgFoto = imgFoto.getScaledInstance(259, 354, Image.SCALE_AREA_AVERAGING);
            //System.err.println("CHEGOU5.2 ");
            g.drawImage(imgFoto, 271, 165,  null);
            //System.err.println("CHEGOU5.3 ");
            //g.setFont(Font.decode("Arial-PLAIN-28"));
            g.drawString("Nome:", 180, 605);
            g.setFont(Font.decode("Arial-BOLD-66"));
            g.drawString(getDados().getPrimeiroNome(), 200, 670);
            //System.err.println("CHEGOU5.4 ");
            g.setFont(Font.decode("Arial-PLAIN-28"));
            g.drawString("Setor:", 180, 750);            
            g.setFont(Font.decode("Arial-PLAIN-58"));
            g.drawString(getDados().getSetor(), 200, 812);
            //System.err.println("CHEGOU6 ");
            /*
             * Linha dummy. Não imprime nada, mas se tirar ela o último comando
             * de impressão vai para a pagina seguinte. Então o último comando é 
             * um pixel numa área fora do cartão, assim não imprime o último comando
             * na pagina errada.
             */
            g.fillRect(0, 2502, 1, 1);
            
            g.dispose();
            g = job.getGraphics();
            //System.err.println("CHEGOU7 ");
            
            img2 = img2.getScaledInstance(637, 1015, Image.SCALE_AREA_AVERAGING);
            g.drawImage(img2, 0, 0,  null);
            g.setFont(Font.decode("Arial-PLAIN-28"));
            g.drawString("Nome:", 150, 210);
            g.setFont(Font.decode("Arial-PLAIN-32"));
            g.drawString(getDados().getNome(), 165, 242);
            g.setFont(Font.decode("Arial-PLAIN-28"));
            g.drawString("Cargo/função:", 150, 280);            
            g.setFont(Font.decode("Arial-PLAIN-32"));
            g.drawString(getDados().getCargo(), 165, 320);
            g.setFont(Font.decode("Arial-PLAIN-28"));
            g.drawString("Matrícula:", 150, 360);
            g.setFont(Font.decode("Arial-PLAIN-32"));
            g.drawString(String.valueOf(getDados().getMatricula()), 165, 390);
            //System.err.println("CHEGOU8 ");
            if(getDados().getTipoSanguineo() != null && !dados.getTipoSanguineo().equals("")){
                g.setFont(Font.decode("Arial-PLAIN-28"));
                g.drawString("Tipo Sanguíneo:", 150, 430);
                g.setFont(Font.decode("Arial-PLAIN-32"));
                g.drawString(getDados().getTipoSanguineo(), 165, 460);
            }
            //System.err.println("CHEGOU9 ");
            g.setFont(Font.decode("C39P36DlTt-PLAIN-104"));
            g.drawString("!" + String.format("%1$09d", Integer.valueOf(getDados().getNumeroCracha())) + "!", 20, 955);
            g.setFont(Font.decode("Arial-PLAIN-24"));
            g.drawString(String.format("%1$09d", Integer.valueOf(getDados().getNumeroCracha())), 240, 997);
            /*
             * Linha dummy. Não imprime nada, mas se tirar ela o último comando
             * de impressão vai para a pagina seguinte. Então o último comando é 
             * um pixel numa área fora do cartão, assim não imprime o último comando
             * na pagina errada.
             */
            //System.err.println("CHEGOU10 ");
            g.fillRect(0, 2502, 1, 1);
            //System.err.println("CHEGOU11 ");
            //g.dispose();
            //System.err.println("CHEGOU12 ");
            job.end();
            //System.err.println("CHEGOU13 ");
        }else{
            //System.err.println("CHEGOU ERRADO ");
            img1 = img1.getScaledInstance(637, 1015, Image.SCALE_AREA_AVERAGING);
            g.drawImage(img1, 0, 0,  null);
            imgFoto = imgFoto.getScaledInstance(259, 354, Image.SCALE_AREA_AVERAGING);
            g.drawImage(imgFoto, 271, 94,  null);
            g.setFont(Font.decode("Times New Roman-PLAIN-32"));
            g.drawString("Nome:", 153, 500);
            g.setFont(Font.decode("Times New Roman-BOLD-64"));
            g.drawString(getDados().getPrimeiroNome(), 153, 570);
            g.setFont(Font.decode("Times New Roman-PLAIN-32"));
            g.drawString("Nome Completo:", 153, 622);
            g.setFont(Font.decode("Times New Roman-BOLD-32"));
            g.drawString(getDados().getNome(), 153, 657);
            g.setFont(Font.decode("Times New Roman-PLAIN-32"));
            g.drawString("Área:", 153, 728);
            g.setFont(Font.decode("Times New Roman-BOLD-32"));
            g.drawString(getDados().getSetor(), 153, 764);
            g.setFont(Font.decode("Times New Roman-PLAIN-32"));
            g.drawString("Categoria:", 153, 824);
            g.setFont(Font.decode("Times New Roman-BOLD-32"));
            g.drawString(getDados().getCargo(), 153, 860);
            g.setFont(Font.decode("Times New Roman-PLAIN-48"));
            g.drawString("Validade:", 153, 943);
            g.setFont(Font.decode("Times New Roman-BOLD-48"));
            
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            g.drawString(fmt.format(getDados().getDataVencimento()), 366, 943);
            /*
             * Linha dummy. Não imprime nada, mas se tirar ela o último comando
             * de impressão vai para a pagina seguinte. Então o último comando é 
             * um pixel numa área fora do cartão, assim não imprime o último comando
             * na pagina errada.
             */
            g.fillRect(0, 2502, 1, 1);
            
            g.dispose();
            g = job.getGraphics();
            
            img2 = img2.getScaledInstance(637, 1015, Image.SCALE_AREA_AVERAGING);
            g.drawImage(img2, 0, 0,  null);
            
            int y = 120;
            for(int i = 0; i < getDados().getHorarios().length; i++){
                if(getDados().getHorarios()[i] == null) continue;
                
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
                g.drawString(getDados().getHorarios()[i].getEntrada() + " às " + getDados().getHorarios()[i].getSaida(), 377, y);
                y += 50;
            }
            g.setFont(Font.decode("C39P36DlTt-PLAIN-104"));
            g.drawString("!" + String.format("%1$09d", Integer.valueOf(getDados().getNumeroCracha())) + "!", 20, 955);
            g.setFont(Font.decode("Arial-PLAIN-24"));
            g.drawString(String.format("%1$09d", Integer.valueOf(getDados().getNumeroCracha())), 240, 997);
            /*
             * Linha dummy. Não imprime nada, mas se tirar ela o último comando
             * de impressão vai para a pagina seguinte. Então o último comando é 
             * um pixel numa área fora do cartão, assim não imprime o último comando
             * na pagina errada.
             */
            g.fillRect(0, 2502, 1, 1);
            
            g.dispose();
            job.end();
        }

    }



}
