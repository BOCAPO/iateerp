package techsoft.carteirinha;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JobImpressoraTermica implements Serializable, TechsoftPrintService{
    private static final long serialVersionUID = 15062014;
    private static final Logger log = Logger.getLogger("techsoft.carteirinha.JobImpressoraTermica");
    private String portaImpressora;
    
    public String descricaoTitularResponsavel;
    public String descricaoTituloValidade;
    public String numeroConvite;
    public String categoriaConvite;
    public String nomeConvidado;
    public String cpfConvidado;
    public String nascimentoConvidado;
    public String nomeSacador;
    public String dataMaximaUtilizacao;
    public String dataHoje;
    public boolean estacionamentoInterno;
    public String nomeComodoro;
    public String numeroChurrasqueira;
    public String horarioEntradaChurrasqueira;
    public String horarioSaidaChurrasqueira;
    public String iateHost;
    public int iatePort;
    public String iateApp;
    
   
    
    @Override
    public void imprimir() {
        synchronized(log){
            //formata o nome do comodoro centralizado entre 30 espacos

            System.err.println("Iniciando Impressao - JobImpressoraTermica");
            
            if(nomeComodoro != null){
                nomeComodoro = nomeComodoro.trim();
                if(nomeComodoro.length() > 30){
                    nomeComodoro = nomeComodoro.substring(0, 30);
                }
                int x = nomeComodoro.length() / 2;
                String espaco = String.format("%1$-" + x + "s", " ");
                nomeComodoro = espaco + nomeComodoro + espaco;
            }
            
            System.err.println("ANTES");
            int retorno0 = MP2032.INSTANCE.ConfiguraModeloImpressora(8);
            System.err.println("DEPOIS");
            System.err.println("ConfiguraModeloImpressora - Retorno: "+retorno0);
            int retorno = MP2032.INSTANCE.IniciaPorta(portaImpressora);
            System.err.println("IniciaPorta - Retorno: "+retorno);
            MP2032.INSTANCE.AjustaLarguraPapel(76);

            String cabec = "";
            if (categoriaConvite.equals("FA")){
                cabec = "GR";
            }else{
                cabec = categoriaConvite;
            }

            System.err.println("Buscando cabecalho");
            try {
                saveImage("http://" + iateHost + ":" + iatePort + iateApp + "/img/ConviteTermica/cabec_"+cabec+".bmp", "cabec.bmp");
            } catch (IOException ex) {
                Logger.getLogger(JobImpressoraTermica.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Cabecalho Salva");
            MP2032.INSTANCE.ImprimeBmpEspecial("cabec.bmp", 100, 100, 0);
            System.err.println("Cabecalho impresso");

            MP2032.INSTANCE.FormataTX("________________________________________________\r\n", 3, 0, 0, 0, 0);
            MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);
            MP2032.INSTANCE.FormataTX(" É indispensável a apresentação\r\n", 1, 0, 0, 1, 1);
            MP2032.INSTANCE.FormataTX("   da carteira de identidade\r\n", 1, 0, 0, 1, 1);
            MP2032.INSTANCE.FormataTX("________________________________________________\r\n", 3, 0, 0, 0, 0);

            MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);

            if(categoriaConvite.equalsIgnoreCase("CO")){
                MP2032.INSTANCE.FormataTX("           O comodoro do ICB convida: \r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX(nomeConvidado.trim() + "\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("------------------------------------------------\r\n", 3, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("     para frequentar o clube até " + dataMaximaUtilizacao + "\r\n", 2, 0, 0, 0, 0);

                MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);

                MP2032.INSTANCE.FormataTX("------------------\r\n", 2, 0, 0, 0, 1);
                MP2032.INSTANCE.FormataTX("| Estacionamento:|\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("| " + (estacionamentoInterno ? "INTERNO" : "PÚBLICO") + "        |\r\n", 2, 0, 0, 0, 1);
                MP2032.INSTANCE.FormataTX("|----------------|\r\n", 2, 0, 0, 0, 1);
                MP2032.INSTANCE.FormataTX("| Emissão:       |" + " _____________________________\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("| " + dataHoje + "     |           Comodoro\r\n", 2, 0, 0, 0, 1);
                MP2032.INSTANCE.FormataTX("------------------ " + nomeComodoro + "\r\n", 2, 0, 0, 0, 1);

                MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);

                MP2032.INSTANCE.FormataTX("                           " + numeroConvite + " \r\n", 1, 0, 0, 0, 0);

                MP2032.INSTANCE.ConfiguraCodigoBarras(100, 1, 1, 0, 40);
                MP2032.INSTANCE.ImprimeCodigoBarrasCODE39(numeroConvite);
                MP2032.INSTANCE.AjustaLarguraPapel(76);
            }else{

                MP2032.INSTANCE.FormataTX("Convidado: " + nomeConvidado.trim() + "\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("------------------------------------------------\r\n", 3, 0, 0, 0, 0);

    /*                         
                IMPRIMIR Lin + 31, Col + 3, "MS Sans Serif", 8, False, False, "CPF: " & Mid(!CPF_CONVIDADO, 1, 3) & "." & Mid(!CPF_CONVIDADO, 4, 3) & "." & Mid(!CPF_CONVIDADO, 7, 3) & "-" & Mid(!CPF_CONVIDADO, 10)
                IMPRIMIR Lin + 31, Col + 40, "MS Sans Serif", 8, False, False, "Dt. Nascimento: " & Format(!DT_NASC_CONVIDADO, "DD/MM/YYYY")
    */
                MP2032.INSTANCE.FormataTX("CPF: " + cpfConvidado + "   | Dt. Nascim. : " + nascimentoConvidado + "\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("------------------------------------------------\r\n", 3, 0, 0, 0, 0);

                MP2032.INSTANCE.FormataTX("Emissão: " + dataHoje + "   | " + descricaoTituloValidade + dataMaximaUtilizacao + "\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("------------------------------------------------\r\n", 3, 0, 0, 0, 0);

                MP2032.INSTANCE.FormataTX("ESTACIONAMENTO: " + (estacionamentoInterno ? "INTERNO" : "PÚBLICO") + "\r\n", 2, 0, 0, 0, 1);
                MP2032.INSTANCE.FormataTX("------------------------------------------------\r\n", 3, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX(descricaoTitularResponsavel + ":\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX(nomeSacador + "\r\n", 2, 0, 0, 0, 1);
                MP2032.INSTANCE.FormataTX("------------------------------------------------\r\n", 3, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("Instruções:\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("- Favor apresentar identidade na portaria\r\n", 1, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("- Favor manter consigo este convite enquanto permanecer no clube\r\n", 1, 0, 0, 0, 0);

                if(categoriaConvite.equals("GR")  || categoriaConvite.startsWith("V") || categoriaConvite.startsWith("E")){
                    if(categoriaConvite.equals("GR")  || categoriaConvite.startsWith("V")){
                       MP2032.INSTANCE.FormataTX("- Válido para uma única entrada\r\n", 1, 0, 0, 0, 0);
                       MP2032.INSTANCE.FormataTX("- Utilizar até a data limite acima\r\n", 1, 0, 0, 0, 0);
                    }

                    MP2032.INSTANCE.FormataTX("- Para o uso das piscinas - Pagar o exame médico na tesouraria.\r\n", 1, 0, 0, 0, 0);
                }else if(categoriaConvite.equals("SA")){
                        MP2032.INSTANCE.FormataTX("- Válido para uma única entrada.\r\n", 1, 0, 0, 0, 0);
                        MP2032.INSTANCE.FormataTX("- Somente para uso após às 16:00.\r\n", 1, 0, 0, 0, 0);
                }else if(categoriaConvite.equals("SI")){
                        MP2032.INSTANCE.FormataTX("- Restrito a área da Sinuca.\r\n", 1, 0, 0, 0, 0);
                        MP2032.INSTANCE.FormataTX("- Válido para uma única entrada.\r\n", 1, 0, 0, 0, 0);
                        MP2032.INSTANCE.FormataTX("- Somente para uso de Terça à Sexta a partir da 17:00 Hs.\r\n", 1, 0, 0, 0, 0);
                }else{
                    if(categoriaConvite.equals("CH")){
                        MP2032.INSTANCE.FormataTX("- Restrito a área da Churrasqueira.\r\n", 1, 0, 0, 0, 0);

                        MP2032.INSTANCE.FormataTX("------------------------------------------------\r\n", 3, 0, 0, 0, 0);
                        MP2032.INSTANCE.FormataTX("Churrasqueira Nr.: " + numeroChurrasqueira + "  | Horário: " + horarioEntradaChurrasqueira + " as " + horarioSaidaChurrasqueira + "\r\n", 3, 0, 0, 0, 0);
                    }
                }

                MP2032.INSTANCE.FormataTX("------------------------------------------------\r\n", 3, 0, 0, 0, 0);

                if(categoriaConvite.equals("GR")  || categoriaConvite.startsWith("V") || categoriaConvite.startsWith("E")){
                    if(categoriaConvite.equals("EC") || categoriaConvite.equals("ED") || categoriaConvite.equals("EV") || categoriaConvite.equals("EO") || categoriaConvite.equals("EA") || categoriaConvite.equals("EN") || categoriaConvite.equals("EP")){
                      MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                      MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                      MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                      MP2032.INSTANCE.FormataTX("      ____________________________________\r\n", 2, 0, 0, 0, 0);
                      MP2032.INSTANCE.FormataTX(nomeComodoro + "\r\n", 2, 0, 0, 0, 0);
                      MP2032.INSTANCE.FormataTX("                    Comodoro\r\n", 2, 0, 0, 0, 0);

                    }
                }

                //MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);

                //MP2032.INSTANCE.FormataTX("                           " + numeroConvite + " \r\n", 1, 0, 0, 0, 0);

                MP2032.INSTANCE.ConfiguraCodigoBarras(100, 1, 1, 0,  40);
                MP2032.INSTANCE.ImprimeCodigoBarrasCODE39(numeroConvite);
                MP2032.INSTANCE.AjustaLarguraPapel(76);

                if(!categoriaConvite.equals("GR") && !categoriaConvite.startsWith("V") && !categoriaConvite.startsWith("E")){
                    MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);
                    MP2032.INSTANCE.FormataTX("------------------------------------------------\r\n", 3, 0, 0, 0, 0);

                    MP2032.INSTANCE.FormataTX("Convidado: \r\n", 3, 0, 0, 0, 0);
                    MP2032.INSTANCE.FormataTX("________________________________________________\r\n", 3, 0, 0, 0, 0);

                    MP2032.INSTANCE.ConfiguraCodigoBarras(100, 1, 1, 0, 40);
                    MP2032.INSTANCE.ImprimeCodigoBarrasCODE39(numeroConvite);
                    MP2032.INSTANCE.AjustaLarguraPapel(76);

                }
            }

            if(categoriaConvite.equals("GR") || categoriaConvite.startsWith("V")){
                MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);

                MP2032.INSTANCE.FormataTX("   Preenchimento Obrigatório\r\n", 1, 0, 0, 1, 1);
                //MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);

                MP2032.INSTANCE.FormataTX("Eu,____________________________________________\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("declaro  não  ter  acessado  o  Iate  Clube  de \r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("Brasília por mais de 12 vezes no corrente ano.\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                //MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                //MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);

                MP2032.INSTANCE.FormataTX("Brasília, _____ de _________________ de _______\r\n", 2, 0, 0, 0, 0);
                //MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);

                MP2032.INSTANCE.FormataTX("         ______________________________\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("             Assinatura do Convidado\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                //MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);

                MP2032.INSTANCE.FormataTX("   RAC - art 12: O acesso de um mesmo convidado ao clube será\r\n", 1, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("   limitado a 12 vezes ao ano, sendo o controle exercido pela \r\n", 1, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("   Secretaria.\r\n", 1, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);

                MP2032.INSTANCE.FormataTX("         ______________________________\r\n", 2, 0, 0, 0, 0);
                MP2032.INSTANCE.FormataTX("              Assinatura do Sócio\r\n", 2, 0, 0, 0, 0);
                //MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);
                //MP2032.INSTANCE.FormataTX(" \r\n", 2, 0, 0, 1, 1);

                //MP2032.INSTANCE.FormataTX("                           " + numeroConvite + " \r\n", 1, 0, 0, 0, 0);

                MP2032.INSTANCE.ConfiguraCodigoBarras(100, 1, 1, 0, 40);
                MP2032.INSTANCE.ImprimeCodigoBarrasCODE39(numeroConvite);
            }

            MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);
            MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);
            MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);
            MP2032.INSTANCE.FormataTX(" \r\n", 3, 0, 0, 0, 0);

            log.fine("AcionaGuilhotina(0)");
            MP2032.INSTANCE.AcionaGuilhotina(0);
            log.fine("FechaPorta()");
            /*
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(JobImpressoraTermica.class.getName()).log(Level.SEVERE, null, ex);
            }
             */
            MP2032.INSTANCE.FechaPorta();


        }
    }
    
    @Override
    public void setPrinterName(String printerName) {
        portaImpressora = printerName;
    }
    
    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
            }

            is.close();
            os.close();
    }
    
    
}
