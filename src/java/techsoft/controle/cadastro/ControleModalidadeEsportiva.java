package techsoft.controle.cadastro;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;

@Controller
public class ControleModalidadeEsportiva{
    
    //ASSOCIA UMA PESSOA A UMA MODALIDADE ESPORTIVA
    @App("1105")
    public static void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");
        
        int matricula = Integer.parseInt(request.getParameter("matricula"));
        int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        int alerta = Integer.parseInt(request.getParameter("alerta"));
        Socio socio = Socio.getInstance(matricula, seqDependente, idCategoria);

        if("showForm".equals(acao)){
            socio.carregarModalidadesEsportivas();
            request.setAttribute("socio", socio);
            request.setAttribute("alerta", alerta);
            request.setAttribute("modalidades", ComboBoxLoader.listar("TB_MODALIDADE"));
            request.getRequestDispatcher("/pages/1101.jsp").forward(request, response);
        }else{
            if(request.getParameterValues("idModalidadeEsportiva") != null){
                for(String s : request.getParameterValues("idModalidadeEsportiva")){
                    socio.getModalidadesEsportivas().put(Integer.parseInt(s), s);
                }
            }

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1105", request.getRemoteAddr());
            socio.atualizarTaxasAdministrativas(audit);
            response.sendRedirect("c?app=1105&acao=showForm&matricula="
                    + matricula
                    + "&idCategoria="
                    + idCategoria
                    + "&seqDependente="
                    + seqDependente
                    + "&alerta="
                    + alerta);
        }
                

    }        
}
