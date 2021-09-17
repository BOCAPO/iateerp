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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.acesso.Entrada;
import techsoft.cadastro.Convite;
import techsoft.cadastro.PermissaoProvisoria;
import techsoft.carteirinha.JobMiniImpressora;
import techsoft.operacoes.AutorizacaoEmbarque;
import techsoft.util.Datas;
import techsoft.util.Extenso;

@Controller
public class ControleMovProdServ{
    

    @App("6160")
    public static void movProdServ(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String msg = "";
        if("grava".equals(acao)){
            int matricula = 0;
            int categoria = 0;
            int dependente = 0;
            
            if (request.getParameter("nome").substring(0,1).equals("#")){
                categoria = Integer.parseInt(request.getParameter("nome").substring(1, 3));
                matricula = Integer.parseInt(request.getParameter("nome").substring(4, 8));
                dependente = Integer.parseInt(request.getParameter("nome").substring(9, 11));
            }
        
            float vrDevido;
            try{
                vrDevido = Float.parseFloat(request.getParameter("vrDevido").replace(".", "").replace(",", "."));
            }catch(Exception e){
                vrDevido = 0;
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
            float vrDebito;
            try{
                vrDebito = Float.parseFloat(request.getParameter("vrDebito").replace(".", "").replace(",", "."));
            }catch(Exception e){
                vrDebito = 0;
            }
            float vrTotal;
            try{
                vrTotal = Float.parseFloat(request.getParameter("vrTotal").replace(".", "").replace(",", "."));
            }catch(Exception e){
                vrTotal = 0;
            }
            
            String pagante = request.getParameter("nome");
            String cheques = request.getParameter("chq");
            
            String observacao = request.getParameter("observacao");
            int transacao = Integer.parseInt(request.getParameter("idCentroCusto"));
            String controleParcPre = request.getParameter("controleParcPre");
            String estacao = request.getRemoteAddr();
            String detMov = request.getParameter("detMov");
        
            Movimento m = new Movimento();
            
            m.setMatricula(matricula);
            m.setCategoria(categoria);
            m.setPagante(pagante);
            m.setVrDevido(vrDevido);
            m.setVrDesconto(vrDesconto);
            m.setVrDinheiro(vrDinheiro);
            m.setVrCheque(vrCheque);
            m.setValor(vrTotal);
            m.setCheques(cheques);
            m.setOutrasFormas("");
            m.setObservacao(observacao);
            m.setCdTransacao(transacao);
            m.setControleParcPre(controleParcPre);
            m.setEstacao(estacao);
            m.setDetMov(detMov);
            
            
            TaxaIndividual t = new TaxaIndividual();
            if (vrDebito>0){

                t.setTaxa(Integer.parseInt(request.getParameter("cdTxIndividual")));
                t.setMatricula(matricula);
                t.setCategoria(categoria);
                t.setDependente(dependente);
                
                Calendar cal = Calendar.getInstance();
                int dia = cal.get(Calendar.DAY_OF_MONTH);
                if (dia>10){
                    cal.add(Calendar.MONTH, 1);
                }
                t.setMes(cal.get(Calendar.MONTH)+1);                
                t.setAno(cal.get(Calendar.YEAR));                
                
                t.setValor(vrDebito);
                t.setUserInclusao(request.getUserPrincipal().getName());
                
            }
                

            try{
                if (vrDebito>0){
                    Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6180", request.getRemoteAddr());
                    t.incluir(1, audit);
                    m.setIdTxIndividual(t.getId());
                }    
                    
                Auditoria audit2 = new Auditoria(request.getUserPrincipal().getName(), "6160", request.getRemoteAddr());
                m.incluir(audit2);
                MovProdServ.exclui(Integer.parseInt(request.getParameter("pendencia")));
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                String retorno = "c?app=6160&acao=imprime";
                retorno = retorno + "&cdCaixa=" + m.getCdCaixa();
                retorno = retorno + "&seqAbertura=" + m.getSeqAbertura();
                retorno = retorno + "&dtMovimento=" + sdf.format(m.getDtSitCaixa());
                retorno = retorno + "&seqAutenticacao=" + m.getSeqAutenticacao();
                retorno = retorno + "&observacao=" + observacao;
                
                if (vrDebito>0){
                    ControleTaxaIndividual.imprime(t, 1, pagante, "nao", "POS", "S", retorno, request, response);
                }else{
                    response.sendRedirect(retorno);
                }
                
            }catch(Exception e){
                 request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
            
        }else if ("imprime".equals(acao)){
            
            String dtCaixa = request.getParameter("dtMovimento");
            Movimento m = Movimento.getInstance(Integer.parseInt(request.getParameter("cdCaixa")), Integer.parseInt(request.getParameter("seqAbertura")), Integer.parseInt(request.getParameter("seqAutenticacao")), Datas.parseDataHora(dtCaixa));

            ArrayList<String> linhas = new ArrayList<String>();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyMMddHHmm");
            DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
            DecimalFormat df2 = new DecimalFormat( "000" );
            DecimalFormat dfBanco = new DecimalFormat( "00000" );
            DecimalFormat dfCheque = new DecimalFormat( "000000" );
            String valTemp = "";

            linhas.add("  ");
            linhas.add("IATE CLUBE DE BRASILIA");
            linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
            linhas.add("CNPJ: 00 018 978/0001-80");
            linhas.add("-------------------------------------");
            linhas.add(" ");
            linhas.add("      *** RECIBO DE PAGAMENTO *** ");
            linhas.add(" ");

            String texto = "";
            texto = "Recebemos de " + m.getPagante() + " a importancia de R$ " ;

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

            linhas.add(" ");
            linhas.add("PROD/SERV          QTD    VALOR");
            linhas.add("---------          ---    -----");

            String[] vetMovimento = m.getDetMov().split(";");
            String[] vetDet;
            float vrFloat=0;
            String descricao = "";
            String dtAtual = "";
            for(int i =0; i < vetMovimento.length ; i++){
                vetDet = vetMovimento[i].split("#");

                if (dtAtual!=vetDet[0]){
                    dtAtual = vetDet[0];
                    linhas.add(dtAtual);
                }
                vrFloat = Float.parseFloat(vetDet[3]);
                valTemp = "         " + df.format(vrFloat);
                descricao = "   " + ProdutoServico.getInstance(Integer.parseInt(vetDet[1])).getDescricao() + "                   ";
                        
                linhas.add(descricao.substring(0,19) + df2.format(Integer.parseInt(vetDet[2])) + valTemp.substring(valTemp.length()-9));
            }
            
            
            linhas.add(" ");
            

            texto = "Centro de Custos: " + CentroCusto.getInstance(m.getCdTransacao()).getDescricao();
            if (texto.length()>35){
                linhas.add(texto.substring(0, 35));
                linhas.add("                  " + texto.substring(35));
            }else{
                linhas.add(texto);
            }

            valTemp = "                    " + df.format(m.getValor());
            linhas.add("Valor: R$ " + valTemp.substring(valTemp.length()-20));

            if (m.getVrDesconto()>0){
                valTemp = "                   " + df.format(m.getVrDesconto());
                linhas.add("+Descontos" + valTemp.substring(valTemp.length()-20));
            }

            linhas.add("                    ----------");
            valTemp = "                             " + df.format(m.getValor());
            linhas.add(valTemp.substring(valTemp.length()-30));

            linhas.add("  ");
            
            String observacao=request.getParameter("observacao");
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
            }
            linhas.add("  ");

            vrFloat = 0;
            if (!m.getCheques().equals("")){

                linhas.add("A quitacao da divida esta");
                linhas.add("condicionada a compensacao");
                linhas.add("dos cheques abaixo:");
                linhas.add("BANCO   CHEQUE     VALOR");
                linhas.add("-----   ------     -----");

                String[] vetCheques = m.getCheques().split(";");

                for(int i =0; i < vetCheques.length ; i++){
                    vetDet = vetCheques[i].split("#");

                    vrFloat = Float.parseFloat(vetDet[4]);
                    valTemp = "          " + df.format(vrFloat);
                    linhas.add(dfBanco.format(Integer.parseInt(vetDet[0])) + "   " + dfCheque.format(Integer.parseInt(vetDet[1])) + valTemp.substring(valTemp.length()-10));
                }
            }

            linhas.add(" ");
            linhas.add("ICB" + sdf3.format(cal.getTime()) + " " + m.getCdCaixa() + " " + m.getSeqAutenticacao() + "*" + df.format(m.getValor()));
            linhas.add(" ");

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
                response.sendRedirect("c?app=6160&acao=showForm&idCentroCusto="+m.getCdTransacao());
            }catch(Exception ex) {
                request.setAttribute("app", "6000");
                request.setAttribute("msg", ex.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }else if ("showForm".equals(acao)){
            request.setAttribute("centroCusto", CentroCusto.getInstance(Integer.parseInt(request.getParameter("idCentroCusto"))));
            
            
            request.setAttribute("produtos", ProdutoServico.listar(Integer.parseInt(request.getParameter("idCentroCusto"))));
            
            String sql = "SELECT CD_RECEBIMENTO, NO_REFERENCIA FROM TB_RECEBIMENTO_PENDENTE " + 
                         "WHERE CD_TRANSACAO = " + request.getParameter("idCentroCusto") + " ORDER BY 2";
            
            request.setAttribute("pendencias", ComboBoxLoader.listarSql(sql));
            
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            request.setAttribute("dtAtual", sdf.format(cal.getTime()));
            request.setAttribute("app6041", request.isUserInRole("6041"));
            request.setAttribute("app6187", request.isUserInRole("6187"));
            request.setAttribute("app6182", request.isUserInRole("6182")); 
            
            request.getRequestDispatcher("/pages/6160.jsp").forward(request, response);
            
        }else if ("atualiza".equals(acao)){
            
            int cdPendencia = Integer.parseInt(request.getParameter("pendencia"));
            String detMovimento = request.getParameter("detMov");
            int idCentroCusto = Integer.parseInt(request.getParameter("idCentroCusto"));
            String nome = request.getParameter("nome");
            
            
            try{
                MovProdServ.atualiza(idCentroCusto, detMovimento, cdPendencia, nome);
                
                response.sendRedirect("c?app=6160&acao=showForm&idCentroCusto="+idCentroCusto);
                
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
        }else if ("exclui".equals(acao)){
            
            int cdPendencia = Integer.parseInt(request.getParameter("pendencia"));
            int idCentroCusto = Integer.parseInt(request.getParameter("idCentroCusto"));
            
            try{
                MovProdServ.exclui(cdPendencia);
                
                response.sendRedirect("c?app=6160&acao=showForm&idCentroCusto="+idCentroCusto);
                
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
        }else if ("imprimeParcial".equals(acao)){

            ArrayList<String> linhas = new ArrayList<String>();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
            DecimalFormat df2 = new DecimalFormat( "000" );
            String valTemp = "";

            linhas.add("  ");
            linhas.add("IATE CLUBE DE BRASILIA");
            linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
            linhas.add("CNPJ: 00 018 978/0001-80");
            linhas.add("-------------------------------------");
            linhas.add(" ");
            linhas.add("      *** FECHAMENTO PARCIAL *** ");
            linhas.add(" ");

            String texto = "Centro de Custos: " + CentroCusto.getInstance(Integer.parseInt(request.getParameter("idCentroCusto"))).getDescricao();
            if (texto.length()>35){
                linhas.add(texto.substring(0, 35));
                linhas.add("                  " + texto.substring(35));
            }else{
                linhas.add(texto);
            }
            
            texto = "Referencia: " + request.getParameter("nome");
            if (texto.length()>35){
                linhas.add(texto.substring(0, 35));
                 linhas.add("            " + texto.substring(35));
            }else{
                linhas.add(texto);
            }
            
            linhas.add(" ");
            linhas.add("PROD/SERV          QTD    VALOR");
            linhas.add("---------          ---    -----");

            String[] vetMovimento = request.getParameter("detMov").split(";");
            String[] vetDet;
            float vrFloat=0;
            float vrTotal=0;
            String descricao = "";
            for(int i =0; i < vetMovimento.length ; i++){
                vetDet = vetMovimento[i].split("#");

                vrFloat = Float.parseFloat(vetDet[3]);
                vrTotal = vrTotal + vrFloat;
                valTemp = "         " + df.format(vrFloat);
                descricao = ProdutoServico.getInstance(Integer.parseInt(vetDet[1])).getDescricao() + "                   ";
                        
                linhas.add(descricao.substring(0,19) + df2.format(Integer.parseInt(vetDet[2])) + valTemp.substring(valTemp.length()-9));
            }
            
            
            valTemp = "                         " + df.format(vrTotal);
            linhas.add("                        -------");
            linhas.add("TOTAL " + valTemp.substring(valTemp.length()-25));
            linhas.add(" ");
            linhas.add(" ");

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
                response.sendRedirect("c?app=6160&acao=showForm&idCentroCusto="+Integer.parseInt(request.getParameter("idCentroCusto")));
            }catch(Exception ex) {
                request.setAttribute("app", "6000");
                request.setAttribute("msg", ex.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
            
        }else {
            
            msg = Movimento.validaMovimento(request.getRemoteAddr(), request.getUserPrincipal().getName());
                
            request.setAttribute("caixa", request.getUserPrincipal().getName());
            
            if (msg=="OK"){
                int cc = MovProdServ.validaMovimento(request.getRemoteAddr());

                if (cc==0){
                    
                    String sql = "SELECT T1.CD_TRANSACAO, T1.DESCR_TRANSACAO FROM TB_TRANSACAO T1, TB_USUARIO_CENTRO_CUSTO T2 " + 
                                 "WHERE T1.CD_TRANSACAO = T2.CD_TRANSACAO AND T1.CD_DEBITO_CREDITO = 'P' AND T2.USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "' ORDER BY 2";

                    request.setAttribute("centros", ComboBoxLoader.listarSql(sql));
                    request.getRequestDispatcher("/pages/6160-centroCusto.jsp").forward(request, response);    
                }else{
                    response.sendRedirect("c?app=6160&acao=showForm&idCentroCusto="+cc);
                }
            }else{
                request.setAttribute("msg", msg);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
        }
    }
    
    
}
