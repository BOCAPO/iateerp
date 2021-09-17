package techsoft.clube;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.util.Datas;

@Controller
public class ControleRelatorioAcademia {

    @App("2450")
    public static void quantitativo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy");
            
            String tipo = request.getParameter("tipo");
            Date d1 = Datas.parse(request.getParameter("dtInicio"));
            Date d2 = Datas.parse(request.getParameter("dtFim"));
            
            StringBuilder sql = new StringBuilder();
            StringBuilder tmp = new StringBuilder();
            for (String s : request.getParameterValues("servicos")) {
                tmp.append(s);
                tmp.append(", ");
            }
            tmp.delete(tmp.length() - 2, tmp.length());

            
            sql.append("EXEC SP_REL_QUANTITATIVO_ACADEMIA '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00', '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59', ");
            
            sql.append(" '(" + tmp + ")', ");
            sql.append(" '" + tipo + "'");
            
            if(tipo.equals("A")){
                request.setAttribute("titulo", "Agendamentos de Serviços da Academia");
            }else{
                request.setAttribute("titulo", "Agendamentos de Cancelamentos da Academia");
            }
            
            request.setAttribute("titulo2", "Período: " + fmt2.format(d1) + " a " + fmt2.format(d2));
            
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "true");
            
            request.setAttribute("total1", "4");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        } else {
            request.setAttribute("servicos", ComboBoxLoader.listarSql("SELECT * FROM TB_SERVICO_ACADEMIA WHERE IC_SITUACAO = 'N'"));
            request.getRequestDispatcher("/pages/2450.jsp").forward(request, response);
        }
    }


}
