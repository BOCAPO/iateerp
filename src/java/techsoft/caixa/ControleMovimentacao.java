package techsoft.caixa;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.Titular;
import techsoft.carteirinha.JobMiniImpressora;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.Usuario;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;
import techsoft.clube.ParametroSistema;
import techsoft.operacoes.Taxa;
import techsoft.util.DayOfWeek;
import techsoft.util.Extenso;

@Controller
public class ControleMovimentacao{
    

    @App("6040")
    public static void movimentacao(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        String msg = "";
        if ("grava".equals(acao)){
            int matricula;
            try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
            }catch(Exception e){
                matricula = 0;
            }
            int categoria;
            try{
                categoria = Integer.parseInt(request.getParameter("categoria"));
            }catch(Exception e){
                categoria = 0;
            }
                    
            float vrDevido;
            try{
                vrDevido = Float.parseFloat(request.getParameter("vrDevido").replace(".", "").replace(",", "."));
            }catch(Exception e){
                vrDevido = 0;
            }
            float vrAcrescimo;
            try{
                vrAcrescimo = Float.parseFloat(request.getParameter("vrAcrescimo").replace(".", "").replace(",", "."));
            }catch(Exception e){
                vrAcrescimo = 0;
            }
            float vrDesconto;
            try{
                vrDesconto = Float.parseFloat(request.getParameter("vrDesconto").replace(".", "").replace(",", "."));
            }catch(Exception e){
                vrDesconto = 0;
            }
            float vrDinheiro;
            try{
                vrDinheiro = Float.parseFloat(request.getParameter("vrDinheiro").replace(".", "").replace(",", "."));
            }catch(Exception e){
                vrDinheiro = 0;
            }
            float vrCheque;
            try{
                vrCheque = Float.parseFloat(request.getParameter("vrCheque").replace(".", "").replace(",", "."));
            }catch(Exception e){
                vrCheque = 0;
            }
            float vrOutros;
            try{
                vrOutros = Float.parseFloat(request.getParameter("vrOutros").replace(".", "").replace(",", "."));
            }catch(Exception e){
                vrOutros = 0;
            }
            float vrTotal;
            try{
                vrTotal = Float.parseFloat(request.getParameter("vrTotal").replace(".", "").replace(",", "."));
            }catch(Exception e){
                vrTotal = 0;
            }
            
            String operador = request.getParameter("operador");

            String pagante = request.getParameter("nome");
            String cheques = request.getParameter("chq");
            String outrasFormas = request.getParameter("formas");
            Date dtVenc = Datas.parse(request.getParameter("dtVencimento"));
            String observacao = request.getParameter("observacao");
            int transacao = Integer.parseInt(request.getParameter("transacao"));
            String controleParcPre = request.getParameter("controleParcPre");
            String estacao = request.getRemoteAddr();
            
            Movimento m = new Movimento();
            
            m.setOperador(operador);
            m.setMatricula(matricula);
            m.setCategoria(categoria);
            m.setPagante(pagante);
            m.setVrDevido(vrDevido);
            m.setVrAcrescimo(vrAcrescimo);
            m.setVrDesconto(vrDesconto);
            m.setVrDinheiro(vrDinheiro);
            m.setVrCheque(vrCheque);
            m.setVrOutros(vrOutros);
            m.setValor(vrTotal);
            m.setCheques(cheques);
            m.setOutrasFormas(outrasFormas);
            m.setDtVenc(dtVenc);
            m.setObservacao(observacao);
            m.setCdTransacao(transacao);
            m.setControleParcPre(controleParcPre);
            m.setEstacao(estacao);
            
            try{
                m.setNrConvite(Integer.parseInt(request.getParameter("nrConvite")));
            }catch(Exception e){
                m.setNrConvite(0);
            }
            
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6040", request.getRemoteAddr());
     
                m.setTaxasIndividuais(request.getParameter("seqTaxaIndividual"));

                m.incluir(audit);
                m.setDetCarne(request.getParameter("detCarne"));

                imprime(m, request.getParameter("observacao"), "6040&transacao="+transacao, false, request, response);
     
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
            
        }else if ("showReimpressao".equals(acao)){
            msg = Movimento.validaMovimento(request.getRemoteAddr(), request.getUserPrincipal().getName());
            
            if (msg=="OK"){
                request.setAttribute("movimentos",  Movimento.listar(request.getRemoteAddr()));
                request.getRequestDispatcher("/pages/6040-reimpressao.jsp").forward(request, response);
            }else{
                request.setAttribute("msg", msg);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
                
        }else if ("imprime".equals(acao)){
            Movimento m = Movimento.getInstance(Integer.parseInt(request.getParameter("cdCaixa")), Integer.parseInt(request.getParameter("seqAbertura")), Integer.parseInt(request.getParameter("seqAutenticacao")), Datas.parseDataHora(request.getParameter("dtSitCaixa")));
            imprime(m, request.getParameter("obs"), "6040&acao=showReimpressao", true, request, response);

        }else if ("2via".equals(acao)){
            
            if ("imprimir".equals(request.getParameter("subacao"))){
                JobMiniImpressora job = new JobMiniImpressora();
                job.texto = new ArrayList<String>();
                for(String s : request.getParameterValues("linha")){
                    job.texto.add(s);
                }

                try {        
                    Socket sock = new Socket(request.getRemoteAddr(), 49001);
                    ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                    out.writeObject(job);
                    out.close();
                    response.sendRedirect(request.getParameter("retorno"));
                }catch(Exception ex) {
                    request.setAttribute("app", "6000");
                    request.setAttribute("msg", ex.getMessage());
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
                
            }else{
                response.sendRedirect(request.getParameter("retorno"));
            }
            
        }else {
            
            msg = Movimento.validaMovimento(request.getRemoteAddr(), request.getUserPrincipal().getName());
                
            request.setAttribute("caixa", request.getUserPrincipal().getName());
            
            if (msg=="OK"){
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                
                request.setAttribute("transacoes", Transacao.listar());
                request.setAttribute("outrasFormas", FormaPagamentoCaixa.listar());
                request.setAttribute("transacao", request.getParameter("transacao"));
                request.setAttribute("dtAtual", sdf.format(cal.getTime()));
                request.setAttribute("app6041", request.isUserInRole("6041"));
                request.setAttribute("app6187", request.isUserInRole("6187"));

                request.getRequestDispatcher("/pages/6040.jsp").forward(request, response);
            }else{
                request.setAttribute("msg", msg);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
            
        }
    }

    public static void imprime(Movimento m, String observacao, String retorno, boolean segVia, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ArrayList<String> linhas = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyMMddHHmm");
        DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
        DecimalFormat dfBanco = new DecimalFormat( "00000" );
        DecimalFormat dfCheque = new DecimalFormat( "000000" );
        String valTemp = "";

        linhas.add("  ");
        if (segVia){
            linhas.add("******* INICIO 2a. VIA *******");
            linhas.add(" ");
        }
        linhas.add("IATE CLUBE DE BRASILIA");
        linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
        linhas.add("CNPJ: 00 018 978/0001-80");
        linhas.add("-------------------------------------");
        linhas.add(" ");
        linhas.add("      *** RECIBO DE PAGAMENTO *** ");
        linhas.add(" ");

        String texto = "";
        if (m.getCdTransacao()==1){
            texto = "Recebemos de " + m.getPagante() + ", Titulo " + m.getCategoria() + "/" + m.getMatricula() + ", a importancia de R$ " ;
        }else{
            texto = "Recebemos de " + m.getPagante() + " a importancia de R$ " ;
        }

        String extenso = Extenso.valorPorExtenso(m.getValor()).toUpperCase();
        texto = texto + df.format(m.getValor()) + "(" + extenso + ") referente ao pagamento abaixo descriminado.";
        String txLn = "";
        int j = 0;
        for (int i=1;i<15;i++){
            if(texto.length()<36){
                txLn = texto;
                break;
            }

            txLn = texto.substring(0,35);

            j = txLn.length();
            while (j>0){
                if (texto.substring(j-1,j).equals(" ")){
                    linhas.add(txLn.substring(0, j));
                    texto = texto.substring(j);
                    break;
                }
                j=j-1;
            }
        }
        linhas.add(txLn);

        linhas.add(" ");                

        Transacao t = Transacao.getInstance(m.getCdTransacao());
        
        if (t.getTipo().equals("R")){
            if (m.getDetCarne()!=null && !m.getDetCarne().equals("")){
                String[] vetMov = m.getDetCarne().split(";");
                String[] vetDetMov;

                for(int i =0; i < vetMov.length ; i++){
                    vetDetMov = vetMov[i].split("#");
                    if (vetDetMov[1].trim().equals("")){
                        linhas.add(vetDetMov[0]);
                    }else{
                        vetDetMov[0] = vetDetMov[0] + "                    ";

                        valTemp = "                    " + vetDetMov[1].trim();
                        linhas.add(vetDetMov[0].substring(0, 20) + valTemp.substring(valTemp.length()-10));
                    }
                }
            }
            
        }else{
            linhas.add("Transacao: " + Transacao.getInstance(m.getCdTransacao()).getDescricao());

            valTemp = "                    " + df.format(m.getValor());
            linhas.add("Valor: R$ " + valTemp.substring(valTemp.length()-20));
        }

        if (m.getVrAcrescimo()>0){
            valTemp = "                   " + df.format(m.getVrAcrescimo());
            linhas.add("+Encargos " + valTemp.substring(valTemp.length()-20));
        }
        if (m.getVrDesconto()>0){
            valTemp = "                   " + df.format(m.getVrDesconto());
            linhas.add("+Descontos" + valTemp.substring(valTemp.length()-20));
        }

        linhas.add("                    ----------");
        valTemp = "                             " + df.format(m.getValor());
        linhas.add(valTemp.substring(valTemp.length()-30));

        linhas.add("  ");

        if (observacao.trim().length()>0){
            texto = observacao.trim();
            for (int i=1;i<15;i++){
                if(texto.length()<36){
                    txLn = texto;
                    break;
                }

                txLn = texto.substring(0,35);

                j = txLn.length();
                while (j>0){
                    if (texto.substring(j-1,j).equals(" ")){
                        linhas.add(txLn.substring(0, j));
                        texto = texto.substring(j);
                        break;
                    }
                    j=j-1;
                }
            }
            linhas.add(txLn);
            linhas.add("  ");
        }
        
        float vrFloat = 0;

        if (!m.getTaxasIndividuais().equals("")){

            linhas.add("As seguintes taxas estão ");
            linhas.add("vinculadas aos recebimento:");
            linhas.add("DATA               VALOR");
            linhas.add("----------         -----");

            String[] vetTaxas = m.getTaxasIndividuais().split(",");
            TaxaIndividual txInd = null;
            for(int i =0; i < vetTaxas.length ; i++){
                txInd = TaxaIndividual.getInstance(Integer.parseInt(vetTaxas[i]));

                valTemp = "              " + df.format(txInd.getValor());
                linhas.add(sdf.format(txInd.getDtGeracao()) +  valTemp.substring(valTemp.length()-14));
            }
            linhas.add("  ");
        }

        
        if (!m.getCheques().equals("")){

            linhas.add("A quitacao da divida esta");
            linhas.add("condicionada a compensacao");
            linhas.add("dos cheques abaixo:");
            linhas.add("BANCO   CHEQUE     VALOR");
            linhas.add("-----   ------     -----");

            String[] vetCheques = m.getCheques().split(";");
            String[] vetDet;

            for(int i =0; i < vetCheques.length ; i++){
                vetDet = vetCheques[i].split("#");

                vrFloat = Float.parseFloat(vetDet[4]);
                valTemp = "          " + df.format(vrFloat);
                linhas.add(dfBanco.format(Integer.parseInt(vetDet[0])) + "   " + dfCheque.format(Integer.parseInt(vetDet[1])) + valTemp.substring(valTemp.length()-10));
            }
            linhas.add(" ");
        }

        linhas.add("ICB" + sdf3.format(cal.getTime()) + " " + m.getCdCaixa() + " " + m.getSeqAutenticacao() + "*" + df.format(m.getValor()));
        linhas.add(" ");
        if (segVia){
            linhas.add("******* FIM 2a. VIA *******");
            linhas.add(" ");
        }

        linhas.add("               - o -");
        linhas.add("                 *");
        linhas.add("                 *");
        linhas.add("                 *");
        linhas.add("                 *");
        linhas.add("                 *");
        linhas.add("                 *");
        linhas.add("                 *");
        linhas.add("                 *");
        linhas.add("                 *");
        linhas.add("                 *");

        JobMiniImpressora job = new JobMiniImpressora();
        job.texto = linhas;
        try {        
            Socket sock = new Socket(request.getRemoteAddr(), 49001);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            out.writeObject(job);
            out.close();
            if (!retorno.equals("NAO")){
                response.sendRedirect("c?app="+retorno);
            }
            
        }catch(Exception ex) {
            request.setAttribute("app", "6000");
            request.setAttribute("msg", ex.getMessage());
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
        
    }
    
}
