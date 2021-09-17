package techsoft.controle.tabelas.taxas;

import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.INPC;

@Controller
public class ManutencaoINPC {
    
    
    @App("1970")
    public static void listarINPC(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if ("imprimir".equals(acao)){

            String sql = "SELECT RIGHT('00' + CONVERT(VARCHAR, MM_REFERENCIA), 2) + '/' + CONVERT(VARCHAR, AA_REFERENCIA) AS 'Referência', VAL_IND_INPC as 'Índice' FROM TB_INPC ORDER BY AA_REFERENCIA DESC, MM_REFERENCIA DESC     ";

            request.setAttribute("titulo", "Índices de Preço ao Consumidor");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");
            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                        
            
        }else{
            request.setAttribute("lista", INPC.listar());
            request.getRequestDispatcher("/pages/1970.jsp").forward(request, response);
            
        }
    
    }
    
    
    @App("1971")
    public static void incluirINPC(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        INPC i = new INPC();
        i.setAno(Integer.parseInt(request.getParameter("ano")));
        i.setMes(Integer.parseInt(request.getParameter("mes")));
        i.setValor(new BigDecimal(request.getParameter("valor").replace(",", ".")));

        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), request.getParameter("app"), request.getRemoteAddr());
        try {
            i.inserir(audit);
            response.sendRedirect("c?app=1970");
        } catch(Exception e) {
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }
    
    
    @App("1972")
    public static void excluirINPC(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        int ano = Integer.parseInt(request.getParameter("ano"));
        int mes = Integer.parseInt(request.getParameter("mes"));
        INPC i = INPC.getInstance(ano, mes);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), request.getParameter("app"), request.getRemoteAddr());
        i.excluir(audit);

        response.sendRedirect("c?app=1970");
    }
    
}
