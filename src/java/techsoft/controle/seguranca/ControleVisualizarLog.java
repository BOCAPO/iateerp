
package techsoft.controle.seguranca;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.seguranca.Permissao;
import techsoft.seguranca.RegistroAuditoria;
import techsoft.seguranca.Usuario;
import techsoft.util.Datas;

@Controller
public class ControleVisualizarLog {

    @App("8040")
    public static void visualizarLogSistema(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");        
        
        if("showFormConsultarLogs".equals(acao)){
            request.setAttribute("usuarios", Usuario.listar());
            request.setAttribute("permissoes", Permissao.listar());

            request.getRequestDispatcher("/pages/8040.jsp").forward(request, response);
        }else{
            String login = request.getParameter("login");
            int perm = 0;

            if(login.equals("TODOS")) login = null;
            try{
                perm = Integer.parseInt(request.getParameter("permissao"));
            }catch(NumberFormatException e){
                perm = 0;
            }
            Date d1 = Datas.parse(request.getParameter("dataInicio"), "00:00:00");
            Date d2 = Datas.parse(request.getParameter("dataFim"), "23:59:59");
            
            String det = request.getParameter("deDetalhamento");

            List<RegistroAuditoria> registros = Auditoria.consultarRegistros(login, perm, d1, d2, det);
            request.setAttribute("registros", registros);

            request.getRequestDispatcher("/pages/8040-resultado.jsp").forward(request, response);
        }
    }    

    @App("1960")
    public static void visualizarLogInternet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");        
        
        if("showFormConsultarLogs".equals(acao)){
            request.setAttribute("usuarios", Usuario.listarInternet());
            request.setAttribute("permissoes", Permissao.listarInternet());

            request.getRequestDispatcher("/pages/1960.jsp").forward(request, response);
        }else{
            String login = request.getParameter("login");
            int perm = 0;

            if(login.equals("TODOS")) login = null;
            try{
                perm = Integer.parseInt(request.getParameter("permissao"));
            }catch(NumberFormatException e){
                perm = 0;
            }
            Date d1 = Datas.parse(request.getParameter("dataInicio"), "00:00:00");
            Date d2 = Datas.parse(request.getParameter("dataFim"), "23:59:59");
            
            String det = request.getParameter("deDetalhamento");

            List<RegistroAuditoria> registros = Auditoria.consultarRegistrosInternet(login, perm, d1, d2, det);
            request.setAttribute("registros", registros);

            request.getRequestDispatcher("/pages/1960-resultado.jsp").forward(request, response);
        }
    }    
    
}
