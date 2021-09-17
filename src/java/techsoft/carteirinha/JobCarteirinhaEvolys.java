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


public class JobCarteirinhaEvolys implements TechsoftPrintService, Serializable{

    private static final long serialVersionUID = 11122002L;
    private static final Logger log = Logger.getLogger("techsoft.carteirinha.JobCarteirinha");
            
    private DadosCarteirinha dados;
    String printer = null;
    Image imgFundo = null;
    Image imgFoto = null;
    Image imgLogo = null;
    
    @Override
    public void setPrinterName(String printerName) {
        this.printer = printerName;
    }
    public DadosCarteirinha getDados() {
        return dados;
    }
    public void setDados(DadosCarteirinha dados) {
        this.dados = dados;
    }
    
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
            imgFundo = ImageIO.read(new URL("http://" + getDados().getIateHost() + ":" + getDados().getIatePort() + getDados().getIateApp()+ "/img/logofundo.bmp"));
            imgLogo = ImageIO.read(new URL("http://" + getDados().getIateHost() + ":" + getDados().getIatePort() + getDados().getIateApp() + "/img/logo.jpg"));        
            imgFoto = ImageIO.read(new URL("http://" + getDados().getIateHost() + ":" + getDados().getIatePort() + getDados().getIateApp() + getDados().getUrlFotoSocio()));
        } catch (IOException ex) {
            log.severe("iateHost=" + getDados().getIateHost());
            log.severe("iatePort=" + getDados().getIatePort());
            log.severe("iateApp=" + getDados().getIateApp());
            log.severe(ex.getMessage());
        }

        String tmp;  
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
        
        //FUNDO, FOTO e LOGO
        imgFundo = imgFundo.getScaledInstance(1052, 632, Image.SCALE_AREA_AVERAGING);
        g.drawImage(imgFundo, 2502, 0,  null);
        if(imgFoto != null){
            imgFoto = imgFoto.getScaledInstance(282, 391, Image.SCALE_AREA_AVERAGING);
            g.drawImage(imgFoto, 2510, 8, null);
        }
        imgLogo = imgLogo.getScaledInstance(166, 183, Image.SCALE_AREA_AVERAGING);
        g.drawImage(imgLogo, 3071, 0, null);
        
        //TITULO
        g.setFont(Font.decode("Arial Black-PLAIN-104"));
        tmp = getDados().getAbreviacaoCategoria() + " - " + getDados().getMatricula();
        g.drawString(tmp, 2930, 324);

        g.setFont(Font.decode("Arial-PLAIN-20"));
        g.drawString("Nome:", 2527, 440);
        g.drawString("Título:", 2527, 524);
        g.drawString("Categoria:", 2693, 524);
        g.drawString("Comodoro:", 3151, 524);

        g.setFont(Font.decode("Arial-BOLD-32"));
        g.drawString(getDados().getNome(), 2534, 482);
        g.drawString(tmp, 2534, 565);

        if(getDados().isTitular()){
            g.drawString(getDados().getDescricaoCategoria(), 2710, 565);
            if(!dados.getCorTitular().equals("")){
                g.setColor(decodeColor(getDados().getCorTitular()));//TODO ler a cor do banco de dados
                g.fillRect(2510, 590, 1015, 50);
            }
        }else{
            g.drawString("DEPENDENTE", 2701, 565);
            if(!dados.getCorDependente().equals("")){
                g.setColor(decodeColor(getDados().getCorDependente()));//TODO ler a cor do banco de dados
                g.fillRect(2510, 590, 1015, 50);
            }            
        }

        if(getDados().isCargoEspecial()){
            Color fontColor = Color.WHITE;
            Color fundoColor = Color.WHITE;
            if(getDados().getCargoEspecialCorCarteira() != null){
                fundoColor = decodeColor(getDados().getCargoEspecialCorCarteira());
            }
            if(getDados().getCargoEspecialCorFonte() != null){
                fontColor = decodeColor(getDados().getCargoEspecialCorFonte());
            }

            //coloca o cargo e tarja de fundo centralizados
            byte[] b = getDados().getDescricaoCargoEspecial().getBytes();
            int width = g.getFontMetrics().bytesWidth(b, 0, b.length);                
            int start = ((1006 - width) / 2) + 2502;
            g.setColor(fundoColor);
            g.fillRect(start, 590, width, 50);
            g.setFont(Font.decode("Arial-PLAIN-32"));
            g.setColor(fontColor);

            g.drawString(getDados().getDescricaoCargoEspecial(), start, 624);
        }
        
        /*
         * Linha dummy. Não imprime nada, mas se tirar ela o último comando
         * de impressão vai para a pagina seguinte. Então o último comando é 
         * um pixel numa área fora do cartão, assim não imprime o último comando
         * na pagina errada.
         */
        g.fillRect(0, 2502, 1, 1);
        g.dispose();
        g = job.getGraphics();
            
        g.setColor(Color.red);
        if(!dados.isCargoEspecial()){
            g.setFont(Font.decode("Arial-BOLD-32"));
            g.drawString("Validade " + getDados().getDataVencimento(), 2551, 58);
        }

        g.setColor(Color.black);
        g.setFont(Font.decode("Arial-PLAIN-32"));
        g.drawString(getDados().getNumeroCarteirinha().substring(1, getDados().getNumeroCarteirinha().length()), 3259, 58);

        g.setFont(Font.decode("Times New Roman-PLAIN-36"));
        g.drawString("É dever do associado identificar-se, quando solicitado, na", 2551, 125);
        g.drawString("portaria e em qualquer dependência do IATE, mediante a", 2551, 158);
        g.drawString("apresentação da carteira social  (artigo 40, incisos VI e VII", 2551, 191);
        g.drawString("do Estatuto do Clube).", 2551, 224);
        g.drawString("A carteira social É pessoal e intransferível, sob pena de", 2551, 274);
        g.drawString("suspensão quando emprestada para possibilitar o ingresso de", 2551, 307);
        g.drawString("outrem ao Clube  (artigo 43, inciso V do Estatuto do Clube).", 2551, 341);
        g.drawString("Em caso de roubo, furto ou extravio, comunicar", 2551, 391);
        g.drawString("imediatamente à Secretaria do Clube.", 2551, 424);

        g.setFont(Font.decode("C39P36DlTt-PLAIN-120"));
        g.drawString("!" + getDados().getNumeroCarteirinha().substring(1, getDados().getNumeroCarteirinha().length()) + "!", 2651, 580);
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


}

