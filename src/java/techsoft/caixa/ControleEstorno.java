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
public class ControleEstorno{
    

    @App("6070")
    public static void estorno(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
            String acao = request.getParameter("acao");
        String msg = "";
        if ("lista".equals(acao)){
            request.setAttribute("movimentos",  Movimento.listar(request.getRemoteAddr()));
            request.getRequestDispatcher("/pages/6070-lista.jsp").forward(request, response);
        }else if ("grava".equals(acao)){
            Movimento m = new Movimento();
            m.setCdCaixa(Integer.parseInt(request.getParameter("cdCaixa")));
            m.setSeqAbertura(Integer.parseInt(request.getParameter("seqAbertura")));
            m.setSeqAutenticacao(Integer.parseInt(request.getParameter("seqAutenticacao")));
            m.setDtSitCaixa(Datas.parseDataHora(request.getParameter("dtSitCaixa")));
            m.setPagante(request.getParameter("pagante"));
            m.setDeTransacao(request.getParameter("deTransacao"));
            m.setValor(Float.parseFloat(request.getParameter("valor").toString().replace(".", "").replace(",", ".")));
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6070", request.getRemoteAddr());
            try {
                m.estornar(audit);
            } catch (Exception ex) {
                request.setAttribute("err", ex);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
            
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
            DecimalFormat df = new DecimalFormat("#,###,###,##0.00" );
            DecimalFormat df2 = new DecimalFormat("000");

            ArrayList<String> linhas = new ArrayList<String>();
            linhas.add("  ");
            linhas.add("  ");
            linhas.add("*****       E S T O R N O       *****");
            linhas.add("  ");
            linhas.add("  Pagante: " + m.getPagante());
            linhas.add("  Autenticacao: " + request.getParameter("seqAutenticacao"));
            linhas.add("  Transação: " + m.getDeTransacao());
            linhas.add("  Valor: "  + df.format(m.getValor()));
            linhas.add("  ");
            linhas.add(" ICB: " + sdf.format(cal.getTime()) + " " + m.getCdCaixa() + "*" + m.getSeqAutenticacao() + "*" + df.format(m.getValor()));

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
            
        }else{

            msg = Movimento.validaMovimento(request.getRemoteAddr(), request.getUserPrincipal().getName());
                
            request.setAttribute("caixa", request.getUserPrincipal().getName());
            request.setAttribute("msg", msg);

            request.getRequestDispatcher("/pages/6070.jsp").forward(request, response);
            
        }
    }
    
}
