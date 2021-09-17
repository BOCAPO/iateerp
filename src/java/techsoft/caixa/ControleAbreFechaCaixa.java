package techsoft.caixa;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

@Controller
public class ControleAbreFechaCaixa{
    

    @App("6020")
    public static void abreFecha(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        String msg = "";
        if ("showAbrir".equals(acao) || "showFechar".equals(acao)){
            if ("showAbrir".equals(acao)){
                
                msg = AbreFechaCaixa.validaAbertura(request.getRemoteAddr(), request.getUserPrincipal().getName());
                
            }else if ("showFechar".equals(acao)){
                
                msg = AbreFechaCaixa.validaFechamento(request.getRemoteAddr(), request.getUserPrincipal().getName());
                
            }
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            request.setAttribute("data", sdf.format(cal.getTime()));
            request.setAttribute("caixa", request.getUserPrincipal().getName());
            request.setAttribute("computador", request.getRemoteAddr());
            request.setAttribute("app", "6020");
            request.setAttribute("acao", acao);
            request.setAttribute("msg", msg);

            request.getRequestDispatcher("/pages/6020.jsp").forward(request, response);
        }else if ("abrir".equals(acao)){
            AbreFechaCaixa.abreCaixa(request.getRemoteAddr(), request.getUserPrincipal().getName());
            
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            
            ArrayList<String> linhas = new ArrayList<String>();
            linhas.add("  ");
            linhas.add("*************************************");
            linhas.add("          ABERTURA DE CAIXA");
            linhas.add("  ");
            linhas.add("  ");
            linhas.add(" Caixa: " + request.getUserPrincipal().getName());
            linhas.add(" Maquina: " + request.getRemoteAddr());
            linhas.add(" Data: " + sdf.format(cal.getTime()));
            linhas.add(" Hora: " +  sdf2.format(cal.getTime()));
            int dia = cal.get(Calendar.DAY_OF_WEEK);
            if (dia == 1){
                dia=7;
            }else{
                dia=dia-1;
            }
                
            linhas.add(" " + DayOfWeek.forCode(dia).getDescription());
            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ");
            
            JobMiniImpressora job = new JobMiniImpressora();
            job.texto = linhas;
            try {        
                Socket sock = new Socket(request.getRemoteAddr(), 49001);
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                out.writeObject(job);
                out.close();
                response.sendRedirect("c?app=6000");
            }catch(Exception ex) {
                request.setAttribute("app", "6000");
                request.setAttribute("msg", ex.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
        }else if ("fechar".equals(acao)){
            AbreFechaCaixa a = new AbreFechaCaixa();
            a.setEstacao(request.getRemoteAddr());
            a.fechaCaixa(request.getRemoteAddr());
            
            float st1 = 0;
            float st2 = 0;
            
            st1 = a.getRecDinheiro() + a.getRecCheque() + a.getRecEstorno();
            st2 = a.getPagDinheiro() + a.getPagCheque() + a.getPagEstorno();
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(a.getDtMovimento());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            Calendar cal2 = Calendar.getInstance();
            
            String valTemp = "";
            DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
            ArrayList<String> linhas = new ArrayList<String>();
            linhas.add("  ");
            linhas.add("*************************************");
            linhas.add("  ");
            linhas.add("        FECHAMENTO DE CAIXA");
            linhas.add("  ");
            linhas.add("-------------------------------------");
            linhas.add("  " + a.getFuncionario());
            linhas.add("-------------------------------------");
            linhas.add("  Inicio Movimento: ");
            int dia = cal.get(Calendar.DAY_OF_WEEK);
            if (dia == 1){
                dia=7;
            }else{
                dia=dia-1;
            }
            
            linhas.add("  " + sdf.format(cal.getTime()) + " " + DayOfWeek.forCode(dia).getDescription());
            linhas.add("-------------------------------------");
            linhas.add("  ");
            linhas.add("  RECEBIMENTOS");
            valTemp = "                    " + df.format(a.getRecDinheiro());
            valTemp = valTemp.substring(valTemp.length()-20);
            linhas.add("   Dinheiro : " + valTemp);
            valTemp = "                    " + df.format(a.getRecCheque());
            valTemp = valTemp.substring(valTemp.length()-20);
            linhas.add("   Cheque   : " + valTemp);
            valTemp = "                    " + df.format(a.getRecEstorno());
            valTemp = valTemp.substring(valTemp.length()-20);
            linhas.add("   Estorno  : " + valTemp);
            linhas.add("  ");
            valTemp = "                    " + df.format(st1);
            valTemp = valTemp.substring(valTemp.length()-20);
            linhas.add("   Sub Total: " + valTemp + " =");
            linhas.add("  ");
            linhas.add("  ");
            linhas.add("  PAGAMENTOS");
            valTemp = "                    " + df.format(a.getPagDinheiro());
            valTemp = valTemp.substring(valTemp.length()-20);
            linhas.add("   Dinheiro : " + valTemp);
            valTemp = "                    " + df.format(a.getPagCheque());
            valTemp = valTemp.substring(valTemp.length()-20);
            linhas.add("   Cheque   : " + valTemp);
            valTemp = "                    " + df.format(a.getPagEstorno());
            valTemp = valTemp.substring(valTemp.length()-20);
            linhas.add("   Estorno  : " + valTemp);
            valTemp = valTemp.substring(valTemp.length()-20);
            linhas.add("  ");
            valTemp = "                    " + df.format(st2);
            valTemp = valTemp.substring(valTemp.length()-20);
            linhas.add("   Sub Total: " + valTemp + " =");
            linhas.add("             _______________________");
            linhas.add("  ");
            linhas.add("  ");
            valTemp = "                    " + df.format(a.getTotal());
            valTemp = valTemp.substring(valTemp.length()-20);
            linhas.add("  TOTAL       " + valTemp + " =");
            linhas.add("  ");
            linhas.add("-------------------------------------");
            linhas.add("  Fechamento ocorrido em ");
            linhas.add("  ");
            linhas.add("  " + sdf.format(cal2.getTime()));
            linhas.add("*************************************");
            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ");
            
            JobMiniImpressora job = new JobMiniImpressora();
            job.texto = linhas;
            try {        
                Socket sock = new Socket(request.getRemoteAddr(), 49001);
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                out.writeObject(job);
                out.close();
                response.sendRedirect("c?app=6000");
            }catch(Exception ex) {
                request.setAttribute("app", "6000");
                request.setAttribute("msg", ex.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
        }
    }
    
}
