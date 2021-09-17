package techsoft.clube;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.clube.ParametroSistema;

@Controller
public class ControleParametroSistema{
    

    @App("2410")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("gravar".equals(acao)){
            try{
                ParametroSistema p = new ParametroSistema();
                p.setDeInstrucao1(request.getParameter("deInstrucao1"));
                p.setDeInstrucao2(request.getParameter("deInstrucao2"));
                p.setDeInstrucao3(request.getParameter("deInstrucao3"));
                p.setDeInstrucao4(request.getParameter("deInstrucao4"));
                p.setDeInstrucao5(request.getParameter("deInstrucao5"));
                p.setDeInstrucao6(request.getParameter("deInstrucao6"));
                p.setDeInstrucao7(request.getParameter("deInstrucao7"));

                p.setDeInstrucaoDCO1(request.getParameter("deInstrucaoDCO1"));
                p.setDeInstrucaoDCO2(request.getParameter("deInstrucaoDCO2"));
                p.setDeInstrucaoDCO3(request.getParameter("deInstrucaoDCO3"));
                p.setDeInstrucaoDCO4(request.getParameter("deInstrucaoDCO4"));
                p.setDeInstrucaoDCO5(request.getParameter("deInstrucaoDCO5"));
                p.setDeInstrucaoDCO6(request.getParameter("deInstrucaoDCO6"));
                p.setDeInstrucaoDCO7(request.getParameter("deInstrucaoDCO7"));
                p.setDeInstrucaoDCO8(request.getParameter("deInstrucaoDCO8"));
                
                p.atualizaParametrosFinanceiros();
                
                request.setAttribute("p", ParametroSistema.getInstance());
                request.getRequestDispatcher("/pages/2410.jsp").forward(request, response);
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }else{
            request.setAttribute("p", ParametroSistema.getInstance());
            request.getRequestDispatcher("/pages/2410.jsp").forward(request, response);
        }
    }
    
}
