package techsoft.carteirinha;

import java.awt.Color;
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
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class JobCartaoNautica implements TechsoftPrintService, Serializable{

    private static final long serialVersionUID = 11122002L;
    private static final Logger log = Logger.getLogger("techsoft.carteirinha.JobCarteirinha");
            
    public String nome;
    public String clube;
    public String numeracao;
    public String uf;
    public String iateHost;
    public int iatePort;
    public String iateApp;
    String printer = null;
    Image imgFrente = null;
    Image imgFundo = null;
    Image imgFoto = null;
    Image imgLogo = null;
    
    @Override
    public void imprimir(){        
        JobAttributes ja = new JobAttributes();
        ja.setDialog(JobAttributes.DialogType.NONE);
        if(printer != null){
            ja.setPrinter(printer);
        }
        
        PageAttributes pa = new PageAttributes();
        pa.setColor(PageAttributes.ColorType.COLOR);
        pa.setPrintQuality(PageAttributes.PrintQualityType.HIGH);
        pa.setOrientationRequested(PageAttributes.OrientationRequestedType.LANDSCAPE);
        pa.setMedia(PageAttributes.MediaType.ISO_A4);
        pa.setPrinterResolution(300);
        
        PrintJob job = Toolkit.getDefaultToolkit().getPrintJob(new Frame(), "Carteirinha", ja, pa);

        try{
            imgFrente = ImageIO.read(new URL("http://" + iateHost + ":" + iatePort + iateApp + "/img/nautica1Frente.bmp"));
            imgFundo = ImageIO.read(new URL("http://" + iateHost + ":" + iatePort + iateApp + "/img/nautica1Fundo.bmp"));
        } catch (IOException ex) {
            log.severe("iateHost=" + iateHost);
            log.severe("iatePort=" + iatePort);
            log.severe("iateApp=" + iateApp);
            log.severe(ex.getMessage());
        }

        Graphics g = job.getGraphics();

        /*
         * TRUQUE PARA IMPRESSAO EM PAISAGEM
         * 
         * A4 = 210mm x 297mm
         * Carteirinha = 54mm x 85mm
         * 
         * Ao inves de usar a pagina em paisagem o java rotaciona a 
         * impressao em 180 graus e começa muda a posição 0,0 para ficar
         * no final (canto inferior esquerdo da pagina). Como não existe
         * uma maneira de especificar o tamanho do cartão, estou usando uma
         * folha A4 e aumentando a posição do X usando a seguinte formula.
         * O zero sera em mm no 212, ou seja, 297 - 85. Em inches isso vale
         * a 8.34. Como a resolução é de 300 dpi, em pixels isso vale a 
         * 2502, ou seja, 300 * 8.34. Enfim, o X sera aumentado em 2502 para
         * a impressao usando folha A4 coincidir com a carteirinha, uma vez que
         * não e possível usar um tamanho de papel custumizado usando a API de
         * impressão do java 1.1
         */
        
        //FUNDO - FRENTE
        //imgFrente = imgFrente.getScaledInstance(1052, 632, Image.SCALE_AREA_AVERAGING);
        g.drawImage(imgFrente, 2502, 0,  null);
        
        g.setFont(Font.decode("Times New Roman-BOLD-48"));
        g.drawString(nome, 2577, 280);
        g.setFont(Font.decode("Times New Roman-PLAIN-48"));
        g.drawString(clube, 2577, 380);
        g.setFont(Font.decode("Times New Roman-PLAIN-48"));
        g.drawString(numeracao, 2577, 475);
        g.setFont(Font.decode("Times New Roman-PLAIN-48"));
        g.drawString(uf, 2930, 475);

        /*
         * Linha dummy. Não imprime nada, mas se tirar ela o último comando
         * de impressão vai para a pagina seguinte. Então o último comando é 
         * um pixel numa área fora do cartão, assim não imprime o último comando
         * na pagina errada.
         */
        g.fillRect(0, 2502, 1, 1);
        g.dispose();
        g = job.getGraphics();
            
        //FUNDO - VERSO
        //imgFundo = imgFundo.getScaledInstance(1052, 632, Image.SCALE_AREA_AVERAGING);
        g.drawImage(imgFundo, 2502, 0,  null);

        g.fillRect(0, 2502, 1, 1);//impressão dummy denovo
        
        g.dispose();
        job.end();        
    }

    private Color decodeColor(String color){
        int i = Integer.parseInt(color);
        int red = 0x000000FF & i;
        int green = (0x000000FF & (i >>> 8));
        int blue = (0x000000FF & (i >>> 16));
        return new Color(red, green, blue);
    }

    @Override
    public void setPrinterName(String printerName) {
        this.printer = printerName;
    }

}

