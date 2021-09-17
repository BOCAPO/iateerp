package techsoft.curso;

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
public class ControleJurosCurso {
    private static final Logger log = Logger.getLogger("techsoft.curso.ControleJurosCurso");

    @App("3025")
    public static void consultar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int idCurso = Integer.parseInt(request.getParameter("idCurso"));
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
                
        request.setAttribute("curso", Curso.getInstance(idCurso));
        request.setAttribute("juros", JurosCurso.consultar(idCurso, tipo, dtRef));
        request.setAttribute("tipo", tipo);
        request.setAttribute("dtRef", dtRef);
        request.getRequestDispatcher("/pages/3025.jsp").forward(request, response);

    }
    
    @App("3026")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("gravar".equals(acao)){
            int idCurso = Integer.parseInt(request.getParameter("idCurso"));
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
            
            JurosCurso j = new JurosCurso();
            j.setCurso(idCurso);
            j.setJuros(valor);
            j.setDtInicial(dtInicio);
            j.setDtFinal(dtFim);
            
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3026", request.getRemoteAddr());
                j.inserir(audit);

                response.sendRedirect("c?app=3025&idCurso="+idCurso);
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
            
        }else{
            int idCurso = Integer.parseInt(request.getParameter("idCurso"));
            request.setAttribute("curso", Curso.getInstance(idCurso));
            request.setAttribute("app", 3026);
            request.getRequestDispatcher("/pages/3026.jsp").forward(request, response);
        }
            

    }

    @App("3027")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("gravar".equals(acao)){
            int idCurso = Integer.parseInt(request.getParameter("idCurso"));
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio"));
            Date dtFim = null;
            if (!request.getParameter("dtFim").equals("")){
                dtFim = Datas.parseDataHora(request.getParameter("dtFim"));
            }
            
            JurosCurso j = new JurosCurso();
            j.setCurso(idCurso);
            j.setDtInicial(dtInicio);
            j.setDtFinal(dtFim);
            
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3026", request.getRemoteAddr());
                j.alterar(audit);

                response.sendRedirect("c?app=3025&idCurso="+idCurso);
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
            
        }else{
            int idCurso = Integer.parseInt(request.getParameter("idCurso"));
            request.setAttribute("curso", Curso.getInstance(idCurso));
            request.setAttribute("dtInicio", request.getParameter("dtInicio"));
            request.setAttribute("dtFim", request.getParameter("dtFim"));
            request.setAttribute("valor", request.getParameter("valor"));
            request.setAttribute("app", 3027);
            request.getRequestDispatcher("/pages/3026.jsp").forward(request, response);
        }
            

    }
}
