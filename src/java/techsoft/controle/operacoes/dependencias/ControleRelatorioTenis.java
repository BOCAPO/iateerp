package techsoft.controle.operacoes.dependencias;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.tenis.EventoSemanal;
import techsoft.tenis.RelatorioTenis;

@Controller
public class ControleRelatorioTenis {
    private static final Logger log = Logger.getLogger(ExcecaoTenis.class.getCanonicalName());

    @App("2360")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    
        String acao = request.getParameter("acao");
        if("consultar".equals(acao)){
            for (String p : Collections.list(request.getParameterNames()))
                request.setAttribute(p, request.getParameter(p));
            
            request.setAttribute("idCategoria", request.getParameterValues("idCategoria"));
            request.setAttribute("idQuadra", request.getParameterValues("idQuadra"));
            
            String quadras = " ";
            if (request.getParameter("idQuadra") != null){
                for(String s : request.getParameterValues("idQuadra")){
                    quadras = quadras + s + ",";
                }
            }    

            String categorias = " ";
            if (request.getParameter("idCategoria") != null){
                for(String s : request.getParameterValues("idCategoria")){
                    categorias = categorias + s + ",";
                }
            }
            
            String sDataInicio = "01/01/1900";
            String sDataFim = "01/01/2999";
            if (!request.getParameter("dataInicio").equals("")) {
                sDataInicio = request.getParameter("dataInicio");
            }
            if (!request.getParameter("dataFim").equals("")) {
                sDataFim = request.getParameter("dataFim");
            }    
            
            LocalDate dataInicio = LocalDate.parse(sDataInicio, DateTimeFormat.forPattern("dd/MM/yyyy"));
            int horaInicio = Integer.parseInt(request.getParameter("horaInicio"));
            int minutoInicio = Integer.parseInt(request.getParameter("minutoInicio"));
            
            LocalDate dataFim = LocalDate.parse(sDataFim, DateTimeFormat.forPattern("dd/MM/yyyy"));
            int horaFim = Integer.parseInt(request.getParameter("horaFim"));
            int minutoFim = Integer.parseInt(request.getParameter("minutoFim"));
            
            //Se o SQL de dentro do método consultar for alterado também tem que alterar logo aqui embaixo na impressao
            request.setAttribute("marcacoes", RelatorioTenis.consultar(quadras.substring(0, quadras.length()-1), categorias.substring(0, categorias.length()-1), request.getParameter("matricula"), request.getParameter("nomeSocio"), request.getParameter("nomeConvidado"), dataInicio.toDateTime(new LocalTime(horaInicio, minutoInicio)), dataFim.toDateTime(new LocalTime(horaFim, minutoFim)), request.getParameter("tipo"), request.getParameter("origem"), request.getParameter("resultado")));
            
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.setAttribute("quadras", ComboBoxLoader.listar("TB_QUADRA_TENIS"));
            request.getRequestDispatcher("/pages/2360-lista.jsp").forward(request, response);
            
        }else if("imprimirRelatorio".equals(acao)){

            
            String quadras = request.getParameter("idQuadra");
            String categorias = request.getParameter("idCategoria");
            
            String sDataInicio = "01/01/1900";
            String sDataFim = "01/01/2999";
            if (!request.getParameter("dataInicio").equals("")) {
                sDataInicio = request.getParameter("dataInicio");
            }
            if (!request.getParameter("dataFim").equals("")) {
                sDataFim = request.getParameter("dataFim");
            }    
            
            LocalDate dataInicio = LocalDate.parse(sDataInicio, DateTimeFormat.forPattern("dd/MM/yyyy"));
            int horaInicio = Integer.parseInt(request.getParameter("horaInicio"));
            int minutoInicio = Integer.parseInt(request.getParameter("minutoInicio"));
            
            LocalDate dataFim = LocalDate.parse(sDataFim, DateTimeFormat.forPattern("dd/MM/yyyy"));
            int horaFim = Integer.parseInt(request.getParameter("horaFim"));
            int minutoFim = Integer.parseInt(request.getParameter("minutoFim"));
            
            String resultado = request.getParameter("resultado");
                    
            //Se esse SQL for alterado também tem que alterar dentro da classe RelatorioTenis também
            String sql = "";
            
            if (resultado.equals("S")){
                sql = "SELECT DISTINCT  " +
                            "T2.NM_QUADRA, " +
                            "T2.CD_QUADRA, " +
                            "T1.TS_INICIO, " +
                            "T1.TS_FIM, " +
                            "CASE  " +
                            "WHEN (SELECT COUNT(*) FROM TB_JOGADOR_TENIS T0 WHERE T0.CD_JOGO = T1.CD_JOGO) > 2 THEN " +
                                    "'Duplas' " +
                            "ELSE " +
                                    "'Simples' " +
                            "END TIPO ";
                        
            }else{

                sql = "SELECT " +
                            "T2.NM_QUADRA Quadra, " +
                            "CASE  " +
                                    "WHEN T3.NR_CONVITE IS NULL THEN " +
                                            "(SELECT RIGHT('00' + CONVERT(VARCHAR, CD_CATEGORIA), 2) + '/' + RIGHT('0000' + CONVERT(VARCHAR, CD_MATRICULA), 4) + ' - ' + NOME_PESSOA FROM TB_PESSOA T0 WHERE T0.CD_MATRICULA = T3.CD_MATRICULA AND T0.CD_CATEGORIA = T3.CD_CATEGORIA AND T0.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE) " +
                                    "ELSE " +
                                            "(SELECT NOME_CONVIDADO FROM TB_CONVITE T0 WHERE T0.NR_CONVITE = T3.NR_CONVITE) " +
                            "END Jogador, " +
                            "CONVERT(VARCHAR, T1.TS_INICIO, 103) + ' ' + CONVERT(VARCHAR, T1.TS_INICIO, 108) + ' a ' + " + 
                            "CONVERT(VARCHAR, T1.TS_FIM, 103) + ' ' + CONVERT(VARCHAR, T1.TS_FIM, 108) Utilizacao, " +
                            "CASE  " +
                                    "WHEN (SELECT COUNT(*) FROM TB_JOGADOR_TENIS T0 WHERE T0.CD_JOGO = T1.CD_JOGO) > 2 THEN " +
                                            "'Duplas' " +
                                    "ELSE " +
                                            "'Simples' " +
                                    "END Tipo, " +
                            "'CLUBE' Origem ";
            }
            
            
            sql = sql + "FROM  " +
                        "TB_JOGO_TENIS T1, " +
                        "TB_QUADRA_TENIS T2, " +
                        "TB_JOGADOR_TENIS T3 " +
                  "WHERE " +
                        "T1.CD_QUADRA = T2.CD_QUADRA AND " +
                        "T1.CD_JOGO = T3.CD_JOGO ";

            if (!"".equals(quadras.substring(0, quadras.length()-1))){
                sql = sql + " AND T2.CD_QUADRA IN (" + quadras.substring(0, quadras.length()-1) + ")";
            }
            if (!"".equals(categorias.substring(0, categorias.length()-1))){
                sql = sql + " AND T3.CD_CATEGORIA IN (" + categorias.substring(0, categorias.length()-1) + ")";
            }
            if (!"".equals(request.getParameter("matricula"))){
                sql = sql + " AND T3.CD_MATRICULA = " + request.getParameter("matricula");
            }
            if (!"".equals(request.getParameter("nomeSocio"))){
                sql = sql + " AND EXISTS (SELECT 1 FROM TB_PESSOA T0 WHERE T3.CD_MATRICULA = T0.CD_MATRICULA AND T3.CD_CATEGORIA = T0.CD_CATEGORIA AND T3.SEQ_DEPENDENTE = T0.SEQ_DEPENDENTE AND T0.NOME_PESSOA LIKE '" + request.getParameter("nomeSocio") + "%')";
            }
            if (!"".equals(request.getParameter("nomeConvidado"))){
                sql = sql + " AND EXISTS (SELECT 1 FROM TB_CONVITE T0 WHERE T3.NR_CONVITE = T0.NR_CONVITE AND T0.NOME_CONVIDADO LIKE '" + request.getParameter("nomeConvidado") + "%')";
            }
            if (!"T".equals(request.getParameter("tipo"))){
                if ("D".equals(request.getParameter("tipo"))){
                    sql = sql + " AND (SELECT COUNT(*) FROM TB_JOGADOR_TENIS T0 WHERE T0.CD_JOGO = T1.CD_JOGO) > 2";
                }else{
                    sql = sql + " AND (SELECT COUNT(*) FROM TB_JOGADOR_TENIS T0 WHERE T0.CD_JOGO = T1.CD_JOGO) < 3";
                }
            }
            sql = sql + " AND T1.TS_INICIO > '" + dataInicio.toDateTime(new LocalTime(horaInicio, minutoInicio)).toString("yyyy-MM-dd HH:mm:ss") + "'";
            sql = sql + " AND T1.TS_INICIO < '" + dataFim.toDateTime(new LocalTime(horaFim, minutoFim)).toString("yyyy-MM-dd HH:mm:ss") + "'";

            if (resultado.equals("S")){
                sql = "SELECT " +
                            "NM_QUADRA Quadra,  " +
                            "SUM(ISNULL(QT_JOGO_SIMPLES, 0)) 'Qt. Jogo Simples',  " +
                            "SUM(ISNULL(QT_JOGO_DUPLA, 0)) 'Qt. Jogo Duplas',  " +
                            "SUM(ISNULL(QT_JOGO_SIMPLES, 0))	+ SUM(ISNULL(QT_JOGO_DUPLA, 0)) 'Qt. Jogo Total',  " +
                            "SUM(ISNULL(MIN_SIMPLES, 0)) 'Min. Simples',  " +
                            "SUM(ISNULL(MIN_DUPLA, 0)) 'Min. Duplas', " +
                            "SUM(ISNULL(MIN_SIMPLES, 0)) + SUM(ISNULL(MIN_DUPLA, 0)) 'Min. Total', " +
                            "DBO.FC_CALCULA_BLOQUEIO_QUADRA_TENIS('" + dataInicio.toDateTime(new LocalTime(horaInicio, minutoInicio)).toString("yyyy-MM-dd HH:mm:ss") + "', '" + dataFim.toDateTime(new LocalTime(horaFim, minutoFim)).toString("yyyy-MM-dd HH:mm:ss") + "', CD_QUADRA) 'Min. Bloqueio' " +
                        "FROM  " +
                        "( " +
                        "SELECT DISTINCT " +
                                "NM_QUADRA, " +
                                "CD_QUADRA, " +
                                "SUM(CASE WHEN TIPO = 'Simples' THEN 1 END) QT_JOGO_SIMPLES, " +
                                "SUM(CASE WHEN TIPO = 'Duplas' THEN 1 END) QT_JOGO_DUPLA, " +
                                "SUM(CASE WHEN TIPO = 'Simples' THEN DATEDIFF(mi, TS_INICIO, TS_FIM) END) MIN_SIMPLES, " +
                                "SUM(CASE WHEN TIPO = 'Duplas' THEN DATEDIFF(mi, TS_INICIO, TS_FIM) END) MIN_DUPLA " +
                        "FROM " +
                       "(" + sql +
                       ") T1 " +
                       "GROUP BY " +
                            "NM_QUADRA, " +
                            "TIPO, " +
                            "CD_QUADRA " +
                       ") T2 " +
                       "GROUP BY NM_QUADRA, CD_QUADRA";

                sql = sql + " ORDER BY 1 ";
                
            }else{
                sql = sql + " ORDER BY 1, 2";
            }
            
            request.setAttribute("titulo", "Relação de Jogos de Tenis");
            if (resultado.equals("S")){
                request.setAttribute("titulo2", "Sintético");
                request.setAttribute("quebra1", "false");
            }else{
                request.setAttribute("titulo2", "Analítico");
                request.setAttribute("quebra1", "true");
            }

            
            request.setAttribute("sql", sql);
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                        
            
        } else {
            
            
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.setAttribute("quadras", ComboBoxLoader.listar("TB_QUADRA_TENIS"));
            request.getRequestDispatcher("/pages/2360-lista.jsp").forward(request, response);
        }
    }
    
}
