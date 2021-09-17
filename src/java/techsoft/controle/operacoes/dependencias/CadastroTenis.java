package techsoft.controle.operacoes.dependencias;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.LocalTime;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tenis.EventoSemanal;
import techsoft.tenis.QuadraTenis;
import techsoft.util.DayOfWeek;

@Controller
public class CadastroTenis {
    private static final Logger log = Logger.getLogger(CadastroTenis.class.getCanonicalName());

    @App("2340")
    public static void listarQuadras(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String app = request.getParameter("app");
        String acao = request.getParameter("acao");

        request.setAttribute("quadras", QuadraTenis.listAll());
        request.getRequestDispatcher("/pages/2340-lista.jsp").forward(request, response);
    }

    @App("2341")
    public static void incluirQuadra(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String app = request.getParameter("app");
        String acao = request.getParameter("acao");

        if ("showForm".equals(acao)) {
            request.setAttribute("app", app);
            request.getRequestDispatcher("/pages/2340.jsp").forward(request, response);
        } else {
            QuadraTenis q = new QuadraTenis();
            
            q.setNome(request.getParameter("nome"));
            q.setMinutosSimples(Short.parseShort(request.getParameter("minutosSimples")));
            q.setMinutosDuplas(Short.parseShort(request.getParameter("minutosDuplas")));
            q.setSegundosMarcacao(Short.parseShort(request.getParameter("segundosMarcacao")));
            
            try {
                q.save(new Auditoria(request));
            } catch(Exception e) {
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }

            response.sendRedirect("c?app=2340");
        }
    }

    @App("2342")
    public static void alterarQuadra(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String app = request.getParameter("app");
        String acao = request.getParameter("acao");
        short id = Short.parseShort(request.getParameter("id"));

        if ("showForm".equals(acao)) {
            request.setAttribute("id", id);
            request.setAttribute("app", app);
            request.setAttribute("quadra", QuadraTenis.getInstance(id));
            request.getRequestDispatcher("/pages/2340.jsp").forward(request, response);
        } else if ("showAgenda".equals(acao)) {
            request.setAttribute("id", id);
            request.setAttribute("app", app);
            request.setAttribute("dias", DayOfWeek.values());
            request.setAttribute("tiposEvento", EventoSemanal.Tipo.values());
            request.setAttribute("quadra", QuadraTenis.getInstance(id));
            request.getRequestDispatcher("/pages/2340-agenda.jsp").forward(request, response);
        } else if ("insertAgenda".equals(acao)) {
            QuadraTenis quadra = QuadraTenis.getInstance(id);
            EventoSemanal evt = new EventoSemanal();

            evt.setQuadra(quadra);
            evt.setDia(DayOfWeek.forCode(Integer.parseInt(request.getParameter("dia"))));
            evt.setInicio(new LocalTime(Integer.parseInt(request.getParameter("horaInicio")), Integer.parseInt(request.getParameter("minutoInicio"))));
            evt.setFim(new LocalTime(Integer.parseInt(request.getParameter("horaFim")), Integer.parseInt(request.getParameter("minutoFim"))));
            evt.setTipo(EventoSemanal.Tipo.forCode(request.getParameter("tipoEvento")));
            String s = request.getParameter("minutosMarcacao");
            if (s != null && s.length() > 0) evt.setMinutosMarcacao(Short.valueOf(s));
            s = request.getParameter("observacao");
            if (s != null && s.length() > 0) evt.setObservacao(s);

            try {
                evt.save(new Auditoria(request));
                response.sendRedirect("c?app=2342&acao=showAgenda&id=" + id);
            } catch(Exception e) {
                request.setAttribute("err", e.getMessage());
                for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
                request.setAttribute("id", id);
                request.setAttribute("app", app);
                request.setAttribute("dias", DayOfWeek.values());
                request.setAttribute("tiposEvento", EventoSemanal.Tipo.values());
                request.setAttribute("quadra", QuadraTenis.getInstance(id));
                request.getRequestDispatcher("/pages/2340-agenda.jsp").forward(request, response);
                //request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }

        } else if ("deleteAgenda".equals(acao)) {            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), request.getParameter("app"), request.getRemoteAddr());
            try {
                EventoSemanal evt = EventoSemanal.getInstance(Integer.parseInt(request.getParameter("idEvento")));
                evt.delete(audit);
            } catch(Exception e) {
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }

            response.sendRedirect("c?app=2342&acao=showAgenda&id=" + id);
        } else {
            QuadraTenis q = QuadraTenis.getInstance(Short.parseShort(request.getParameter("id")));
            
            q.setNome(request.getParameter("nome"));
            q.setMinutosSimples(Short.parseShort(request.getParameter("minutosSimples")));
            q.setMinutosDuplas(Short.parseShort(request.getParameter("minutosDuplas")));
            q.setSegundosMarcacao(Short.parseShort(request.getParameter("segundosMarcacao")));
            
            try {
                q.save(new Auditoria(request));
            } catch(Exception e) {
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }

            response.sendRedirect("c?app=2340");
        }
    }

    @App("2343")
    public static void excluirQuadra(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String app = request.getParameter("app");
        String acao = request.getParameter("acao");
        short id = Short.parseShort(request.getParameter("id"));

        try {
            QuadraTenis q = QuadraTenis.getInstance(id);
            q.delete(new Auditoria(request));
        } catch(Exception e) {
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }

        response.sendRedirect("c?app=2340");
    }
}
