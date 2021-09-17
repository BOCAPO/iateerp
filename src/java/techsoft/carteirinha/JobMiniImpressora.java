package techsoft.carteirinha;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

public class JobMiniImpressora implements TechsoftPrintService, Serializable{
    private static final long serialVersionUID = 24082013;        
    public ArrayList<String> texto;   
    public String textoString = "";
    public String tipoImpressora = "";
            
    @Override
    public void imprimir(){        
        PrintService impressora = null;
        DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;  
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(df, null);  
        for (PrintService p : ps) {  
            System.err.println("Impressora encontrada: " + p.getName());  
            if(p.getName()!=null && p.getName().contains("Generic")){  
                System.err.println("Impressora Selecionada: " + p.getName());  
                impressora = p;  
            }     
        }
        
        DocPrintJob dpj = impressora.createPrintJob();  
        InputStream stream = null;
        
        if (tipoImpressora.equals("BEMATECH")){
            //Bematech
            for(int i=0;i<texto.size();i++){
                textoString = textoString + texto.get(i) + "\r\n";
                System.err.println(texto.get(i));  
            }
            textoString = textoString + "\r\n\r\n";
            stream = new ByteArrayInputStream(textoString.getBytes());  
        }else{
            //Epson
            stream = new ByteArrayInputStream(texto.toString().getBytes());  
        }
        
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;  
        Doc doc = new SimpleDoc(stream, flavor, null);  
            
        try {
            dpj.print(doc, null);  
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    @Override
    public void setPrinterName(String printerName) {
        /*
         * deve ser usada a impressora padrao do usuario. Esse job 
         * nao pode rodar com conta de systema, senão a impressao não 
         * sai, é preciso usar uma conta de usuario para rodar esse serviço.
         * o parametro abaixo serve para saber qual o tipo de mini impressora
         * dependendo do modelo é preciso dar alguns tratamentos ao texto
         */
        
        tipoImpressora = printerName;
    }

}
