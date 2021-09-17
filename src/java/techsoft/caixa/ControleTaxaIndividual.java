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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.CarroSocio;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.Titular;
import techsoft.carteirinha.JobMiniImpressora;
import techsoft.clube.CarroFuncionario;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.Usuario;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;
import techsoft.clube.ParametroSistema;
import techsoft.operacoes.Taxa;
import techsoft.operacoes.TaxaAdministrativa;
import techsoft.tabelas.Carro;
import techsoft.tabelas.MarcaCarro;
import techsoft.tabelas.ModeloCarro;
import techsoft.util.Extenso;

@Controller
public class ControleTaxaIndividual{
    
    @App("6180")
    public static void cadastro(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        
        if ("grava".equals(acao)){
            
            TaxaIndividual t = new TaxaIndividual();
            
            String nome = request.getParameter("nome");
            t.setTaxa(Integer.parseInt(request.getParameter("taxa")));
            Taxa tx = Taxa.getInstance(t.getTaxa());
            
            if (request.getParameter("tipoPessoa").equals("S")){
                String categoria = nome.substring(1, 3);
                String matricula = nome.substring(4, 8);
                String dependente = nome.substring(9, 11);

                t.setMatricula(Integer.parseInt(matricula));
                t.setCategoria(Integer.parseInt(categoria));
                t.setDependente(Integer.parseInt(dependente));
            }else{
                String idFuncionario = nome.substring(1, nome.indexOf("-")-1 );

                t.setIdFuncionario(Integer.parseInt(idFuncionario));
            }
            t.setMes(Integer.parseInt(request.getParameter("mes")));
            t.setAno(Integer.parseInt(request.getParameter("ano")));
            t.setValor(Float.parseFloat(request.getParameter("valor").replace(".", "").replace(",", ".")));
            t.setUserInclusao(request.getUserPrincipal().getName());
            t.setObservacao(request.getParameter("observacao"));
            
            try{
                t.setCarro(Integer.parseInt(request.getParameter("carro")));
            }catch(Exception e){
                t.setCarro(0);
            }
            
            if (tx.getSelecionaCarro() != null && tx.getSelecionaCarro().equals("S")){
                t.setIdCombustivel(Integer.parseInt(request.getParameter("combustivel")));
                t.setVrCombustivel(Float.parseFloat(request.getParameter("precoComb").replace(".", "").replace(",", ".")));
                t.setQtLitros(Float.parseFloat(request.getParameter("litrosComb").replace(".", "").replace(",", ".")));
            }
            
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6180", request.getRemoteAddr());
                if (request.getParameter("contaLanc").equals("PRE")){
                    t.incluirPrePago(audit);
                }else{
                    t.incluir(Integer.parseInt(request.getParameter("qtVezes")), audit);
                }

                imprime(t, Integer.parseInt(request.getParameter("qtVezes")), nome, request.getParameter("senha"), request.getParameter("contaLanc"), request.getParameter("tipoPessoa"), "c?app=6180", request, response);
                
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
            
        }else{
            Calendar cal = Calendar.getInstance();
            int dia = cal.get(Calendar.DAY_OF_MONTH);
            if (dia>10){
                cal.add(Calendar.MONTH, 1);
            }
            request.setAttribute("mes", cal.get(Calendar.MONTH)+1);                
            request.setAttribute("ano", cal.get(Calendar.YEAR));                
            request.setAttribute("taxas", ComboBoxLoader.listarSql("SELECT * FROM tb_taxa_administrativa T1, TB_USUARIO_TAXA_INDIVIDUAL T2 WHERE T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA AND T1.ind_taxa_administrativa = 'I' AND T2.USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "' ORDER BY 2"));
            request.setAttribute("combustiveis", Combustivel.listar());
            request.setAttribute("app6181", request.isUserInRole("6181"));            
            request.setAttribute("app6182", request.isUserInRole("6182")); 
            request.setAttribute("app6183", request.isUserInRole("6183"));    
            request.setAttribute("app6185", request.isUserInRole("6185"));
            request.setAttribute("app6186", request.isUserInRole("6186"));     
            request.setAttribute("app6187", request.isUserInRole("6187"));
            
            request.getRequestDispatcher("/pages/6180.jsp").forward(request, response);
        }
        
    }
    
    public static void imprime(TaxaIndividual t, int qtVezes, String nome, String senha, String contaLanc, String tipoPessoa, String retorno, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ArrayList<String> linhas = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
        DecimalFormat fmtMes = new DecimalFormat( "00" );
        String valTemp = "";

        linhas.add("  ");
        linhas.add("IATE CLUBE DE BRASILIA");
        linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
        linhas.add("CNPJ: 00 018 978/0001-80");
        linhas.add("-------------------------------------");
        linhas.add(" ");
        linhas.add("      *** AUTORIZACAO DE DEBITO *** ");
        if (contaLanc.equals("PRE")){
            linhas.add("      ***        PRE-PAGO       *** ");
        }
        linhas.add(" ");
        linhas.add("      Autorizo a cobrança do valor ");
        linhas.add("          abaixo discriminado");
        linhas.add(" ");
        linhas.add(" ");
        if (nome.length()>23){
            linhas.add(" Cobrar de: " + nome.substring(0, 23));
            linhas.add("            " + nome.substring(23));
        }else{
            linhas.add(" Cobrar de: " + nome);
        }
        
        Taxa tx = Taxa.getInstance(t.getTaxa());
        if (tx.getDescricao().length()>21){
            linhas.add(" Referente a: " + tx.getDescricao().substring(0, 21));
            linhas.add("              " + tx.getDescricao().substring(21));
        }else{
            linhas.add(" Referente a: " + tx.getDescricao());
        }

        if (contaLanc.equals("POS")){
            linhas.add(" Valor: " + qtVezes + "x " + df.format(t.getValor()));
            linhas.add(" Sera cobrado a partir de: " + fmtMes.format(t.getMes()) + "/" + t.getAno());
        }else{
            linhas.add(" Valor: " + df.format(t.getValor()));
            linhas.add(" Saldo apos o débito: " + df.format(t.getSaldoPrePago()));
        }
                
        if (t.getCarro()>0){
            String textoCarro = "";
            if (tipoPessoa.equals("S")){
                CarroSocio c = CarroSocio.getInstance(t.getCarro());
                textoCarro = c.getModelo().getMarca().getDescricao() + " - " + c.getModelo().getDescricao() + "(" + c.getPlaca() + ")";
            }else{
                CarroFuncionario c = CarroFuncionario.getInstance(t.getCarro());
                textoCarro = c.getModelo().getMarca().getDescricao() + " - " + c.getModelo().getDescricao() + "(" + c.getPlaca() + ")";
            }
            
            if (textoCarro.length()>21){
                linhas.add(" Carro: " + textoCarro.substring(0, 21));
                linhas.add("        " + textoCarro.substring(21));
            }else{
                linhas.add(" Carro: " + textoCarro);
            }
        }
        
        if (tx.getSelecionaCarro() != null && tx.getSelecionaCarro().equals("S")){
            Combustivel comb = Combustivel.getInstance(t.getIdCombustivel());
            linhas.add(" Combustivel: " + comb.getDescricao());
            linhas.add(" Preço: " + df.format(t.getVrCombustivel()));
            linhas.add(" Litros: " + df.format(t.getQtLitros()));
            
        }
        
        
        String txLn = "";
        String texto = "";
        if (t.getObservacao() != null){
            texto = t.getObservacao().trim();
            for (int i=1;i<15;i++){
                if(texto.length()<36){
                    txLn = texto;
                    break;
                }

                txLn = texto.substring(0,35);

                int j = txLn.length();
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
        linhas.add("  ");
        linhas.add(" Data: " + sdf.format(cal.getTime()));
        linhas.add("  ");
        linhas.add("  ");
        linhas.add("  ");
        linhas.add("  ");
        
        if("sim".equals(senha)){
            linhas.add(" ***** AUTORIZADO POR SENHA ***** ");
        }else{
            linhas.add(" -------------------------------- ");
            if (tipoPessoa.equals("S")){
                linhas.add("       ASSINATURA DO SOCIO ");
            }else{
                linhas.add("    ASSINATURA DO FUNCIONARIO ");
            }
        }
        linhas.add("               - o -");
        linhas.add("                 *");
        linhas.add("      (CUPOM SEM VALOR FISCAL)");
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
            
            request.setAttribute("retorno", retorno);                
            request.setAttribute("linhas", linhas);                
            request.getRequestDispatcher("/pages/2Via.jsp").forward(request, response);
            
        }catch(Exception ex) {
            request.setAttribute("app", "6000");
            request.setAttribute("msg", ex.getMessage());
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
        
        

    }
    
    
}
