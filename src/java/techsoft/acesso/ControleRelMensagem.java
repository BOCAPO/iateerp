package techsoft.acesso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.acesso.Entrada;
import techsoft.cadastro.Convite;
import techsoft.cadastro.PermissaoProvisoria;
import techsoft.operacoes.AutorizacaoEmbarque;
import techsoft.util.Datas;

@Controller
public class ControleRelMensagem{
    

    @App("7080")
    public static void local(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");
        

        if ("imprime".equals(acao)){
            String categorias = "";
            for(String s : request.getParameterValues("idMensagem")){
                categorias = categorias + s + ",";
            }
            categorias = categorias.substring(0, categorias.length()-1);
            
            SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
            Date data = new Date();
            
            String sql = "SELECT " +
                            "T1.DESCR_TP_EVENTO_ACESSO AS 'Mensagem', " +
                            "T3.DESCR_CATEGORIA AS 'Categoria', " +
                            "T2.CD_MATRICULA AS 'Titulo', " +
                            "T2.NOME_PESSOA AS 'Nome' " +
                       "FROM " +
                            "TB_TIPO_EVENTO_ACESSO T1, " +
                            "TB_PESSOA T2, " +
                            "TB_CATEGORIA T3, " +
                            "TB_CONTROLE_ACESSO T4 " +
                       "WHERE " +
                            "T1.CD_TP_EVENTO_ACESSO = T4.CD_TP_EVENTO_ACESSO AND " +
                            "T2.NR_CARTEIRA_PESSOA = T4.NR_DOCUMENTO_ACESSO AND " +
                            "T2.CD_CATEGORIA = T3.CD_CATEGORIA AND " +
                            "T1.CD_TP_EVENTO_ACESSO IN (" + categorias + ")";

            request.setAttribute("titulo", "Relatório de Mensagens");
            String cat = request.getParameter("soAtivas");
            if("SA".equals(request.getParameter("soAtivas"))){
                request.setAttribute("titulo2", "Mensagens Ativas");    
                
                sql = sql + " AND T1.CD_STATUS_EVENTO_ACESSO = 'AT' ";
                sql = sql + " AND T1.DT_INICIO_VIGENCIA <= '" + sd.format(data) + "' AND T1.DT_FIM_VIGENCIA >= '" + sd.format(data) + "'";
            }else{
                request.setAttribute("titulo2", "Todas as Mensagens");
            }
            sql = sql + " ORDER BY 1, 2";
            
            
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "true");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");
                
            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                
        }else{
            request.setAttribute("mensagens", ComboBoxLoader.listar("TB_TIPO_EVENTO_ACESSO"));
            request.getRequestDispatcher("/pages/7080.jsp").forward(request, response);
        }
    }
}
