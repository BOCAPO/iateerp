package techsoft.controle.cadastro;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.SocioBarco;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;

@Controller
public class ControleMovimentoBarco{
    
    
    /*
     * A LISTAGEM DO MOVIMENTO NA APLICAÇÃO 2000 ControleBarco.java
     */
    
    //INCLUSAO DE MOVIMENTO DO BARCO DO SOCIO
    @App("3036")
    public static void inserir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        
        String acao = request.getParameter("acao");

        if("gravar".equals(acao)){
            int idBarco = Integer.parseInt(request.getParameter("idBarco"));
            SocioBarco b = SocioBarco.getInstance(idBarco);            
            MovimentoBarco m = new MovimentoBarco();
            m.setBarco(b);
            m.setDataMovimentacao(Datas.parse(request.getParameter("dataMovimento"), request.getParameter("horaMovimento") + ":00"));
            m.setTipo(request.getParameter("tipo"));
            m.setLocal(request.getParameter("local"));
            try{
                int p = Integer.parseInt(request.getParameter("passageiros"));
                m.setPassageiros(p);
            }catch(NumberFormatException e){
            }
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3036", request.getRemoteAddr());
            m.inserir(audit);

            response.sendRedirect("c?app=2000&acao=showListaMovimento&idBarco=" + m.getBarco().getId());
        }else{
            int idBarco = Integer.parseInt(request.getParameter("idBarco"));
            SocioBarco b = SocioBarco.getInstance(idBarco);
            request.setAttribute("barco", b);
            request.setAttribute("movimentacao", MovimentoBarco.listar(b));
            request.getRequestDispatcher("/pages/3032-barco-movimento.jsp").forward(request, response);            
        }
            
    }        
        
    //EXCLUSAO DE MOVIMENTO DO BARCO DO SOCIO
    @App("3037")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int idMovimento = Integer.parseInt(request.getParameter("idMovimento"));
        MovimentoBarco m = MovimentoBarco.getInstance(idMovimento);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3037", request.getRemoteAddr());
        m.excluir(audit);

        response.sendRedirect("c?app=2000&acao=showListaMovimento&idBarco=" + m.getBarco().getId());
    }        
    
        
}
