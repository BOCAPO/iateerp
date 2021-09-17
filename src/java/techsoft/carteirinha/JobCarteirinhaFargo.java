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
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class JobCarteirinhaFargo implements TechsoftPrintService, Serializable {

    private static final long serialVersionUID = 20180102L;
    private static final Logger log = Logger.getLogger("techsoft.carteirinha.JobCarteirinhaFargo");
            
    public DadosCarteirinha dados;
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

        log.severe("entrou");
        log.severe("http://" + dados.getIateHost() + ":" + dados.getIatePort() + dados.getIateApp()+ "/img/logofundo.bmp");
        
        try{
            imgFundo = ImageIO.read(new URL("http://" + dados.getIateHost() + ":" + dados.getIatePort() + dados.getIateApp()+ "/img/logofundo.bmp"));
            imgLogo = ImageIO.read(new URL("http://" + dados.getIateHost() + ":" + dados.getIatePort() + dados.getIateApp() + "/img/logo.jpg"));        
            imgFoto = ImageIO.read(new URL("http://" + dados.getIateHost() + ":" + dados.getIatePort() + dados.getIateApp() + dados.getUrlFotoSocio()));
        } catch (IOException ex) {
            log.severe("iateHost=" + dados.getIateHost());
            log.severe("iatePort=" + dados.getIatePort());
            log.severe("iateApp=" + dados.getIateApp());
            log.severe(ex.getMessage());
        }

        log.severe("aqui1");
        
        String tmp;  
        BufferedImage imgFront = new BufferedImage(1052, 632, BufferedImage.TYPE_INT_RGB);
        BufferedImage imgBack = new BufferedImage(1052, 632, BufferedImage.TYPE_INT_RGB);
        Graphics g = imgFront.getGraphics();        
        
        //FUNDO, FOTO e LOGO
        imgFundo = imgFundo.getScaledInstance(1052, 632, Image.SCALE_AREA_AVERAGING);
        
        g.drawImage(imgFundo, 0, 0,  null);
        if(imgFoto != null){
            imgFoto = imgFoto.getScaledInstance(282, 391, Image.SCALE_AREA_AVERAGING);
            g.drawImage(imgFoto, 8, 8, null);
        }
        imgLogo = imgLogo.getScaledInstance(166, 183, Image.SCALE_AREA_AVERAGING);
        g.drawImage(imgLogo, 569, 0, null);
        
        log.severe("aqui2");
        
        //TITULO
        g.setColor(Color.BLACK);
        g.setFont(Font.decode("Arial Black-PLAIN-104"));
        tmp = dados.getAbreviacaoCategoria() + " - " + dados.getMatricula();
        g.drawString(tmp, 428, 324);

        g.setFont(Font.decode("Arial-PLAIN-20"));
        g.drawString("Nome:", 25, 440);
        g.drawString("Título:", 25, 524);
        g.drawString("Categoria:", 191, 524);
        g.drawString("Comodoro:", 649, 524);

        g.setFont(Font.decode("Arial-BOLD-32"));
        g.drawString(dados.getNome(), 32, 482);
        g.drawString(tmp, 32, 565);
        
        log.severe("aqui3");

        if(dados.isTitular()){
            g.drawString(dados.getDescricaoCategoria(), 208, 565);
            if(!dados.getCorTitular().equals("")){
                g.setColor(decodeColor(dados.getCorTitular()));//TODO ler a cor do banco de dados
                g.fillRect(8, 590, 1015, 50);
            }
        }else{
            g.drawString("DEPENDENTE", 199, 565);
            if(!dados.getCorDependente().equals("")){
                g.setColor(decodeColor(dados.getCorDependente()));//TODO ler a cor do banco de dados
                g.fillRect(8, 590, 1015, 50);
            }            
        }

        log.severe("aqui4");
        
        if(dados.isCargoEspecial()){
            Color fontColor = Color.WHITE;
            Color fundoColor = Color.WHITE;
            if(dados.getCargoEspecialCorCarteira() != null){
                fundoColor = decodeColor(dados.getCargoEspecialCorCarteira());
            }
            if(dados.getCargoEspecialCorFonte() != null){
                fontColor = decodeColor(dados.getCargoEspecialCorFonte());
            }

            //coloca o cargo e tarja de fundo centralizados
            byte[] b = dados.getDescricaoCargoEspecial().getBytes();
            int width = g.getFontMetrics().bytesWidth(b, 0, b.length);                
            int start = ((1006 - width) / 2);
            g.setColor(fundoColor);
            g.fillRect(start, 590, width, 50);
            g.setFont(Font.decode("Arial-PLAIN-32"));
            g.setColor(fontColor);

            g.drawString(dados.getDescricaoCargoEspecial(), start, 624);
        }
        
        log.severe("aqui5");
        
        g.dispose();
        
        g = imgBack.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1052, 632);
        
        g.setColor(Color.red);
        if(!dados.isCargoEspecial()){
            g.setFont(Font.decode("Arial-BOLD-32"));
            g.drawString("Validade " + dados.getDataVencimento(), 49, 58);
        }

        g.setColor(Color.black);
        g.setFont(Font.decode("Arial-PLAIN-32"));
        g.drawString(dados.getNumeroCarteirinha().substring(1, dados.getNumeroCarteirinha().length()), 757, 58);
        
        log.severe("aqui6");

        g.setFont(Font.decode("Times New Roman-PLAIN-36"));
        g.drawString("É dever do associado identificar-se, quando solicitado, na", 49, 125);
        g.drawString("portaria e em qualquer dependência do IATE, mediante a", 49, 158);
        g.drawString("apresentação da carteira social  (artigo 40, incisos VI e VII", 49, 191);
        g.drawString("do Estatuto do Clube).", 49, 224);
        g.drawString("A carteira social É pessoal e intransferível, sob pena de", 49, 274);
        g.drawString("suspensão quando emprestada para possibilitar o ingresso de", 49, 307);
        g.drawString("outrem ao Clube  (artigo 43, inciso V do Estatuto do Clube).", 49, 341);
        g.drawString("Em caso de roubo, furto ou extravio, comunicar", 49, 391);
        g.drawString("imediatamente à Secretaria do Clube.", 49, 424);

        g.setFont(Font.decode("C39P36DlTt-PLAIN-120"));
        g.drawString("!" + dados.getNumeroCarteirinha().substring(1, dados.getNumeroCarteirinha().length()) + "!", 149, 580);
        
        log.severe("aqui7");
        
        g.dispose();
        
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
            job.doPrint("Carteirinha " + this.dados.getNome());
            job.finishDoc();
        }finally{
            if(f1 != null) f1.delete();
            if(f2 != null) f2.delete();
            
            job.close();
        }        
    }

    private Color decodeColor(String color){
        int i = Integer.parseInt(color);
        int red = 0x000000FF & i;
        int green = (0x000000FF & (i >>> 8));
        int blue = (0x000000FF & (i >>> 16));
        return new Color(red, green, blue);
    }

    
}
