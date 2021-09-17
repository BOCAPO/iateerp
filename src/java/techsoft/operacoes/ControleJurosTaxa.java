package techsoft.operacoes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioCarne;
import techsoft.cadastro.SocioCarneDetalhe;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.controle.operacoes.dependencias.ExcecaoTenis;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.Curso;
import techsoft.tabelas.InserirException;
import techsoft.tenis.EventoSemanal;
import techsoft.tenis.RelatorioTenis;
import techsoft.util.Datas;


@Controller
public class ControleJurosTaxa {
    private static final Logger log = Logger.getLogger("techsoft.operacoes.ControleJurosTaxa");

    @App("1415")
    public static void consultar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
        String tipo = "AT";
        String dtRef = "";
        
        if (request.getParameter("tipo")!=null){
            tipo = request.getParameter("tipo");
        }
        if (request.getParameter("dtRef")==null){
            Date d = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            
            dtRef = f.format(d);
        }else{
            dtRef = request.getParameter("dtRef");
        }
                
        request.setAttribute("taxa", Taxa.getInstance(idTaxa));
        request.setAttribute("juros", JurosTaxa.consultar(idTaxa, tipo, dtRef));
        request.setAttribute("tipo", tipo);
        request.setAttribute("dtRef", dtRef);
        request.getRequestDispatcher("/pages/1415.jsp").forward(request, response);

    }
    
    @App("1416")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("gravar".equals(acao)){
            int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio"));
            Date dtFim = null;
            if (!request.getParameter("dtFim").equals("")){
                dtFim = Datas.parseDataHora(request.getParameter("dtFim"));
            }
            float valor;
            
            try{
                valor = Float.parseFloat(request.getParameter("valor"));
            }catch(Exception e){
                valor = Float.parseFloat(request.getParameter("valor"));
            }
            
            JurosTaxa j = new JurosTaxa();
            j.setTaxa(idTaxa);
            j.setJuros(valor);
            j.setDtInicial(dtInicio);
            j.setDtFinal(dtFim);
            
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1416", request.getRemoteAddr());
                j.inserir(audit);

                response.sendRedirect("c?app=1415&idTaxa="+idTaxa);
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
            
        }else{
            int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
            request.setAttribute("taxa", Taxa.getInstance(idTaxa));
            request.setAttribute("app", 1416);
            request.getRequestDispatcher("/pages/1416.jsp").forward(request, response);
        }
            

    }

    @App("1417")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("gravar".equals(acao)){
            int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio"));
            Date dtFim = null;
            if (!request.getParameter("dtFim").equals("")){
                dtFim = Datas.parseDataHora(request.getParameter("dtFim"));
            }
            
            JurosTaxa j = new JurosTaxa();
            j.setTaxa(idTaxa);
            j.setDtInicial(dtInicio);
            j.setDtFinal(dtFim);
            
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1416", request.getRemoteAddr());
                j.alterar(audit);

                response.sendRedirect("c?app=1415&idTaxa="+idTaxa);
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
            
        }else{
            int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
            request.setAttribute("taxa", Taxa.getInstance(idTaxa));
            request.setAttribute("dtInicio", request.getParameter("dtInicio"));
            request.setAttribute("dtFim", request.getParameter("dtFim"));
            request.setAttribute("valor", request.getParameter("valor"));
            request.setAttribute("app", 1417);
            request.getRequestDispatcher("/pages/1416.jsp").forward(request, response);
        }
            

    }
}
