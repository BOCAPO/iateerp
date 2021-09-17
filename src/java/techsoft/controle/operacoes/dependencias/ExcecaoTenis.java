package techsoft.controle.operacoes.dependencias;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tenis.EventoSemanal;
import techsoft.tenis.ExcecaoAgendaSemanal;
import techsoft.tenis.QuadraTenis;

@Controller
public class ExcecaoTenis {
    private static final Logger log = Logger.getLogger(ExcecaoTenis.class.getCanonicalName());

    @App("2350")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    
        String id = request.getParameter("id");

        if (id == null) {
            request.setAttribute("quadras", QuadraTenis.listAll());
            request.getRequestDispatcher("/pages/2350-lista.jsp").forward(request, response);
        } else {
            QuadraTenis q = QuadraTenis.getInstance(Integer.parseInt(id));
            List<ExcecaoAgendaSemanal> l = Arrays.asList(q.getAgendaExcecoes());
            Collections.reverse(l);
            
            request.setAttribute("id", id);
            request.setAttribute("quadra", q);
            request.setAttribute("eventos", l);
            request.setAttribute("tiposEvento", EventoSemanal.Tipo.values());
            request.getRequestDispatcher("/pages/2350.jsp").forward(request, response);
        }
    }
    
    @App("2351")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String app = request.getParameter("app");
        short id = Short.parseShort(request.getParameter("id"));
        
        QuadraTenis q = QuadraTenis.getInstance(id);
        ExcecaoAgendaSemanal e = new ExcecaoAgendaSemanal();
        
        LocalDate dataInicio = LocalDate.parse(request.getParameter("dataInicio"), DateTimeFormat.forPattern("dd/MM/yyyy"));
        int horaInicio = Integer.parseInt(request.getParameter("horaInicio"));
        int minutoInicio = Integer.parseInt(request.getParameter("minutoInicio"));

        LocalDate dataFim = LocalDate.parse(request.getParameter("dataFim"), DateTimeFormat.forPattern("dd/MM/yyyy"));
        int horaFim = Integer.parseInt(request.getParameter("horaFim"));
        int minutoFim = Integer.parseInt(request.getParameter("minutoFim"));
        
        e.setQuadra(q);
        e.setTipo(EventoSemanal.Tipo.forCode(request.getParameter("tipoEvento")));
        e.setInicio(dataInicio.toDateTime(new LocalTime(horaInicio, minutoInicio)));
        e.setFim(dataFim.toDateTime(new LocalTime(horaFim, minutoFim)));
        String s = request.getParameter("minutosMarcacao");
        if (s != null && s.length() > 0) e.setMinutosMarcacao(Short.valueOf(s));
        s = request.getParameter("observacao");
        if (s != null && s.length() > 0) e.setObservacao(s);

        try {
            e.save(new Auditoria(request));
            response.sendRedirect("c?app=2350&id=" + id);
        } catch(Exception ex) {
            request.setAttribute("err", ex.getMessage());
            for (String p : Collections.list(request.getParameterNames()))
                request.setAttribute(p, request.getParameter(p));
            request.setAttribute("id", id);
            request.setAttribute("quadra", q);
            request.setAttribute("eventos", Arrays.asList(q.getAgendaExcecoes()));
            request.setAttribute("tiposEvento", EventoSemanal.Tipo.values());
            request.getRequestDispatcher("/pages/2350.jsp").forward(request, response);
            //request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }        
    }
    
    @App("2353")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String app = request.getParameter("app");
        String id = request.getParameter("id");
        String idEvento = request.getParameter("idEvento");

        try {
            ExcecaoAgendaSemanal.getInstance(Integer.parseInt(idEvento)).delete(new Auditoria(request));
            response.sendRedirect("c?app=2350&id=" + id);
        } catch(Exception ex) {
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }        

        
    }
}
