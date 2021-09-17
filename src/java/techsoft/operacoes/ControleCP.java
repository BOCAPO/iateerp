package techsoft.operacoes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.INPC;
import techsoft.util.Datas;

@Controller
public class ControleCP {
    
    
    @App("1300")
    public static void listarINPC(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if ("imprimir".equals(acao)){
            String mesAno = request.getParameter("mesAno");
            String sql = " SELECT CONVERT(VARCHAR, DT_VALIDADE_CMP, 103) 'Dt. Validade' , VAL_IND_CMP as 'Índice' FROM TB_CMP "+
                         " WHERE MONTH(DT_VALIDADE_CMP) = " + mesAno.substring(0,2) + " AND YEAR(DT_VALIDADE_CMP) = "+mesAno.substring(3)+
                         " ORDER BY DT_VALIDADE_CMP ";

            request.setAttribute("titulo", "Comissão de Permanência");
            request.setAttribute("titulo2", "Referência: "+mesAno);
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");
            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                        
            
        }else if ("consultar".equals(acao)){
            request.setAttribute("lista", CP.listar(request.getParameter("mesAno")));
            request.setAttribute("mesAno", request.getParameter("mesAno"));
            request.getRequestDispatcher("/pages/1300.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("/pages/1300.jsp").forward(request, response);
            
        }
    
    }
    
    
    @App("1301")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        CP i = new CP();
        i.setData(Datas.parse(request.getParameter("data")));
        i.setValor(new BigDecimal(request.getParameter("valor")));

        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), request.getParameter("app"), request.getRemoteAddr());
        try {
            i.inserir(audit);
            response.sendRedirect("c?app=1300");
        } catch(Exception e) {
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }
    
    
    @App("1302")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        Date data = Datas.parse(request.getParameter("data"));
        CP i = CP.getInstance(data);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), request.getParameter("app"), request.getRemoteAddr());
        i.excluir(audit);

        response.sendRedirect("c?app=1300");
    }
    
}
