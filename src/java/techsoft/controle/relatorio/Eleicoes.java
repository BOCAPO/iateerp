package techsoft.controle.relatorio;

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
public class Eleicoes {

    @App("1900")
    public static void mapaSocios(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            int tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            int tituloFim = Integer.parseInt(request.getParameter("tituloFim"));
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date vencimento = Datas.parse(request.getParameter("vencimento"));

            sql.append("SELECT ");
            sql.append("    T1.NOME_PESSOA, ");
            sql.append("    CONVERT(CHAR(2), T1.CD_CATEGORIA) +'/' + CONVERT(CHAR(4), T1.CD_MATRICULA) AS TÍTULO, ");
            sql.append("   (SELECT COUNT(*) FROM TB_CARNE T2 WHERE T2.DT_VENC_CARNE < '"); 
            sql.append(fmt.format(vencimento));
            sql.append("' AND T2.CD_SIT_CARNE = 'NP' AND T1.CD_CATEGORIA = T2.CD_CATEGORIA AND T1.CD_MATRICULA= T2.CD_MATRICULA) AS QTD_CARNES_NP , ");
            sql.append(" (case  when cd_sit_pessoa = 'SA' then ");
            sql.append("             'Sem Admissão' ");
            sql.append("       when cd_sit_pessoa = 'EM' then ");
            sql.append("            'Em Admissão_' ");
            sql.append("       when cd_sit_pessoa = 'NA' then ");
            sql.append("            'Não Admitido' ");
            sql.append("       when cd_sit_pessoa = 'RE' then ");
            sql.append("            'Retomado____' ");
            sql.append("       when cd_sit_pessoa = 'SU' then ");
            sql.append("            'Suspenso____' ");
            sql.append("       when cd_sit_pessoa = 'US' then ");
            sql.append("            'C/Usuário___' ");
            sql.append(" Else ");
            sql.append("            '____________' ");
            sql.append(" End ) AS SITUAÇÃO, ");
            sql.append("   '_____________________________________________________' AS OBSERVAÇÕES ");
            sql.append("From ");
            sql.append("    TB_PESSOA T1 ");
            sql.append("Where ");
            sql.append("    T1.CD_CATEGORIA IN (");
            for(String s : request.getParameterValues("categorias")){
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() -2, sql.length());
            sql.append(") ");            
            sql.append(" AND ");
            sql.append("    T1.SEQ_DEPENDENTE = 0 AND ");
            sql.append("    T1.CD_MATRICULA >= ");
            sql.append(tituloInicio);
            sql.append(" AND ");
            sql.append("    T1.CD_MATRICULA <= ");
            sql.append(tituloFim);
            sql.append("Order By ");
            sql.append("    T1.NOME_PESSOA ");

            request.setAttribute("titulo", request.getParameter("titulo"));
            request.setAttribute("titulo2", request.getParameter("titulo2"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);                

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT'"));
            request.getRequestDispatcher("/pages/1900.jsp").forward(request, response);
        }
    }
    
    @App("1910")
    public static void folhaVotacao(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            int tituloInicio = Integer.parseInt(request.getParameter("tituloInicio"));
            int tituloFim = Integer.parseInt(request.getParameter("tituloFim"));
            String teste = request.getParameter("empresarial");
            boolean empresarial = Boolean.parseBoolean(request.getParameter("empresarial"));

            if(empresarial){
                sql.append("EXEC SP_FOLHA_VOTACAO ");
                sql.append(tituloInicio);
                sql.append(", ");
                sql.append(tituloFim);                
            }else{
                sql.append(" SELECT ");
                sql.append("    CONVERT(CHAR(2), CD_CATEGORIA) +'/' + CONVERT(CHAR(4), CD_MATRICULA) AS TÍTULO, ");
                sql.append("    NOME_PESSOA, ");
                sql.append("    '________________________________________' AS ASSINATURA,");
                sql.append("    '___________________________________________' AS OBSERVAÇÕES");
                sql.append(" From ");
                sql.append("    TB_PESSOA ");
                sql.append(" Where ");
                sql.append("    CD_CATEGORIA IN (");
                for(String s : request.getParameterValues("categorias")){
                    sql.append(s);
                    sql.append(", ");
                }
                sql.delete(sql.length() -2, sql.length());
                sql.append(") ");                            
                sql.append(" AND ");
                sql.append("    SEQ_DEPENDENTE = 0 AND ");
                sql.append("    CD_MATRICULA >= ");
                sql.append(tituloInicio);
                sql.append(" AND ");
                sql.append("    CD_MATRICULA <= ");
                sql.append(tituloFim);
                sql.append(" Order By ");
                sql.append("    CD_CATEGORIA, CD_MATRICULA");

            }

            request.setAttribute("titulo", request.getParameter("titulo"));
            request.setAttribute("titulo2", request.getParameter("titulo2"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", empresarial);
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", -1);
            request.setAttribute("total2", -1);
            request.setAttribute("total3", -1);
            request.setAttribute("total4", -1);                

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);
        }else{
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA WHERE STATUS_CATEGORIA = 'AT'"));
            request.getRequestDispatcher("/pages/1910.jsp").forward(request, response);
        }
    }
    
}
