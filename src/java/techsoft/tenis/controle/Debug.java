package techsoft.tenis.controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import techsoft.tenis.AgendaTenis;
import techsoft.tenis.EventoQuadraTenis;
import techsoft.tenis.EventoSemanal;
import techsoft.tenis.ExcecaoAgendaSemanal;
import techsoft.tenis.QuadraTenis;

@WebServlet("/debug")
public class Debug extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        String s = "";
        
//        Interval i = new Interval(new DateTime(2013, 2, 21, 12, 20), new DateTime(2013, 2, 21, 12, 20));
//        s = s + "Intervalo: " + i.getStart().toString("EEE dd/MM/yyyy HH'h'mm") + " - " + i.getEnd().toString("EEE dd/MM/yyyy HH'h'mm") + "\n";
//        
//        for (QuadraTenis q : QuadraTenis.listAll()) {
//            AgendaTenis a = new AgendaTenis(i);
//            s = s + "\n" + q + '\n';
//
//            s = s + "AgendaTenis Semanal:\n";
//            for (EventoSemanal e : q.getAgendaSemanal()) s = s + "  " + e + '\n';
//
//            s = s + "AgendaTenis Semanal no Período:\n";
//            List<Evento> l = new ArrayList<Evento>();
//            for (EventoSemanal e : q.getAgendaSemanal()) l.addAll(Arrays.asList(e.toEvento(i)));
//            Collections.sort(l);
//            a.addAll(l);
//            for (EventoQuadraTenis e : l) s = s + "  " + e + '\n';
//
//            s = s + "AgendaTenis de Exceções:\n";
//            for (ExcecaoAgendaSemanal e : q.getAgendaExcecoes()) s = s + "  " + e + '\n';
//
//            s = s + "AgendaTenis de Exceções no Período:\n";
//            l = new ArrayList<Evento>();
//            for (ExcecaoAgendaSemanal e : ExcecaoAgendaSemanal.listFor(q, i)) l.addAll(Arrays.asList(e.toEvento(i)));
//            Collections.sort(l);
//            a.addAll(l);
//            for (EventoQuadraTenis e : l) s = s + "  " + e + '\n';
//
//            s = s + "AgendaTenis no Período:\n";
//            for (EventoQuadraTenis e : q.getAgenda(i)) s = s + "  " + e + '\n';
//        }

        QuadraTenis a = new QuadraTenis();
        a.setNome("Teste insert");
        a.setMinutosSimples(1);
        a.setMinutosDuplas(2);
        a.setSegundosMarcacao(3);
        s = s + a + "\n";
        a.save(null);
        s = s + a + "\n";
        
        for (QuadraTenis q : QuadraTenis.listAll())
            s = s + q + "\n";
        
        response.setContentType("text/html");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<!DOCTYPE html>");
        response.getWriter().write("<html>");
        response.getWriter().write("<head>");
        response.getWriter().write("<title>Debug - Iate Clube de Brasília</title>");
        response.getWriter().write("</head>");
        response.getWriter().write("<body>");
        response.getWriter().write("<pre>");
        response.getWriter().write(s);
        response.getWriter().write("</pre>");
        response.getWriter().write("</body>");
        response.getWriter().write("</html>");
    }
}
