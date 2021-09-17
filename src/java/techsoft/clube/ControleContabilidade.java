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
public class ControleContabilidade {

    @App("2440")
    public static void valoresAReceber(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy");
            
            boolean flagCurso = false;//escolinhas
            Date d1 = Datas.parse(request.getParameter("dtReferencia"));
            String tipoTaxa = request.getParameter("tipoTaxa");
            boolean analitico = request.getParameter("tipo").equals("A");

            boolean filtrarVencimento = Boolean.parseBoolean(request.getParameter("filtrarVencimento"));
            Date d3 = null;
            Date d4 = null;
            if (filtrarVencimento) {
                d3 = Datas.parse(request.getParameter("vencimentoInicio"));
                d4 = Datas.parse(request.getParameter("vencimentoFim"));
            }
            
            String mostraPGDiaAnt = request.getParameter("mostraPGDiaAnt");
            
            StringBuilder sql = new StringBuilder();
            StringBuilder tmp = new StringBuilder();
            for (String s : request.getParameterValues("taxas")) {
                tmp.append(s);
                tmp.append(", ");
                if (s.endsWith("8")) {
                    flagCurso = true;
                }
            }
            tmp.delete(tmp.length() - 2, tmp.length());

            
            StringBuilder tmp2 = new StringBuilder();
            for (String s : request.getParameterValues("categorias")) {
                tmp2.append(s);
                tmp2.append(", ");
            }
            tmp2.delete(tmp2.length() - 2, tmp2.length());
            
            
            sql.append("EXEC SP_REL_VAL_RECEBER_CONTABIL '");
            sql.append(fmt.format(d1));
            sql.append(" 23:59:59'");
            if (flagCurso){
                sql.append(", 'S'");
            }else{
                sql.append(", 'N'");
            }
            sql.append(", '(" + tmp + ")'");
            sql.append(", '" + tipoTaxa + "'");
            
            if (analitico){
                sql.append(", 'A', ");
            }else{
                sql.append(", 'S', ");
            }
            
            if (filtrarVencimento) {
                sql.append("'");
                sql.append(fmt.format(d3));
                sql.append(" 00:00:00', '");
                sql.append(fmt.format(d4));
                sql.append(" 23:59:59', ");
            } else {
                sql.append("'01/01/1900 00:00:00', '12/31/9999 23:59:59', ");
            }
            
            sql.append(" '(" + tmp2 + ")', ");
            
            sql.append(" '" + mostraPGDiaAnt + "'");
            
            request.setAttribute("titulo", "Relatório de Valores a Receber");
            request.setAttribute("titulo2", "Data de Referência " + fmt2.format(d1));
            
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            
            if (analitico){
                request.setAttribute("total1", "4");
            }else{
                request.setAttribute("total1", "2");
            }
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        } else {
            request.setAttribute("taxas", ComboBoxLoader.listar("TB_TAXA_ADMINISTRATIVA"));
            request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.getRequestDispatcher("/pages/2440.jsp").forward(request, response);
        }
    }

    @App("2500")
    public static void relReceitas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy");
            StringBuilder sql = new StringBuilder();
            
            Date dtRef = Datas.parse(request.getParameter("dtReferencia"));
            boolean analitico = request.getParameter("tipo").equals("A");

            boolean mostrarCarne = Boolean.parseBoolean(request.getParameter("mostrarCarne"));
            boolean mostrarBoleto = Boolean.parseBoolean(request.getParameter("mostrarBoleto"));
            boolean mostrarMovCaixa = Boolean.parseBoolean(request.getParameter("mostrarMovCaixa"));
            boolean mostrarProdServ = Boolean.parseBoolean(request.getParameter("mostrarProdServ"));
            boolean mostrarCredPre = Boolean.parseBoolean(request.getParameter("mostrarCredPre"));
            boolean mostrarDebPre = Boolean.parseBoolean(request.getParameter("mostrarDebPre"));
            
            sql.append("EXEC SP_REL_RECEITAS '");
            sql.append(fmt.format(dtRef));
            sql.append(" 23:59:59', '");
            if(analitico){
                sql.append("A', '");
            }else{
                sql.append("S', '");
            }
            
            if (mostrarCarne) {
                if (request.getParameter("carneVencInicio") != null && request.getParameter("carneVencInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("carneVencInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("carneVencFim") != null && request.getParameter("carneVencFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("carneVencFim"))));
                    sql.append(" 23:59:59', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }
                if (request.getParameter("carneGeracaoInicio") != null && request.getParameter("carneGeracaoInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("carneGeracaoInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("carneGeracaoFim") != null && request.getParameter("carneGeracaoFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("carneGeracaoFim"))));
                    sql.append(" 23:59:59', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }
            }else{
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
            }
            
            if (mostrarBoleto) {
                if (request.getParameter("boletoVencInicio") != null && request.getParameter("boletoVencInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("boletoVencInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("boletoVencFim") != null && request.getParameter("boletoVencFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("boletoVencFim"))));
                    sql.append(" 23:59:59', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }
                if (request.getParameter("boletoGeracaoInicio") != null && request.getParameter("boletoGeracaoInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("boletoGeracaoInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("boletoGeracaoFim") != null && request.getParameter("boletoGeracaoFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("boletoGeracaoFim"))));
                    sql.append(" 23:59:59', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }
            }else{
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
            }

            if (mostrarMovCaixa) {
                sql.append(fmt.format(Datas.parse(request.getParameter("movCaixaInicio"))));
                sql.append(" 00:00:00', '");
                sql.append(fmt.format(Datas.parse(request.getParameter("movCaixaFim"))));
                sql.append(" 23:59:59', '");
            }else{
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
                
            }
            
            if (mostrarProdServ) {
                sql.append(fmt.format(Datas.parse(request.getParameter("prodServInicio"))));
                sql.append(" 00:00:00', '");
                sql.append(fmt.format(Datas.parse(request.getParameter("prodServFim"))));
                sql.append(" 23:59:59', '");
            }else{
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
                
            }
            
            if (mostrarCredPre) {
                sql.append(fmt.format(Datas.parse(request.getParameter("credPreInicio"))));
                sql.append(" 00:00:00', '");
                sql.append(fmt.format(Datas.parse(request.getParameter("credPreFim"))));
                sql.append(" 23:59:59', '");
            }else{
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
                
            }
            
            if (mostrarDebPre) {
                sql.append(fmt.format(Datas.parse(request.getParameter("debPreInicio"))));
                sql.append(" 00:00:00', '");
                sql.append(fmt.format(Datas.parse(request.getParameter("debPreFim"))));
                sql.append(" 23:59:59'");
            }else{
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00'");
                
            }

            
            request.setAttribute("titulo", "Relatório de Receitas");
            request.setAttribute("titulo2", "Data de Referência " + fmt2.format(dtRef));
            
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            
            if (analitico){
                request.setAttribute("total1", "4");
            }else{
                request.setAttribute("total1", "3");
            }
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        } else {
            request.getRequestDispatcher("/pages/2500.jsp").forward(request, response);
        }
    }

    @App("2510")
    public static void relCancReceitas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("visualizar".equals(acao)) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy");
            StringBuilder sql = new StringBuilder();
            
            Date dtRef = Datas.parse(request.getParameter("dtReferencia"));
            boolean analitico = request.getParameter("tipo").equals("A");

            boolean mostrarCarne = Boolean.parseBoolean(request.getParameter("mostrarCarne"));
            boolean mostrarBoleto = Boolean.parseBoolean(request.getParameter("mostrarBoleto"));
            boolean mostrarDebPre = Boolean.parseBoolean(request.getParameter("mostrarDebPre"));
            
            sql.append("EXEC SP_REL_CANC_RECEITAS '");
            sql.append(fmt.format(dtRef));
            sql.append(" 23:59:59', '");
            if(analitico){
                sql.append("A', '");
            }else{
                sql.append("S', '");
            }
            
            if (mostrarCarne) {
                if (request.getParameter("carneVencInicio") != null && request.getParameter("carneVencInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("carneVencInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("carneVencFim") != null && request.getParameter("carneVencFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("carneVencFim"))));
                    sql.append(" 23:59:59', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }
                if (request.getParameter("carneGeracaoInicio") != null && request.getParameter("carneGeracaoInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("carneGeracaoInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("carneGeracaoFim") != null && request.getParameter("carneGeracaoFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("carneGeracaoFim"))));
                    sql.append(" 23:59:59', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }

                if (request.getParameter("carneCancInicio") != null && request.getParameter("carneCancInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("carneCancInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("carneCancFim") != null && request.getParameter("carneCancFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("carneCancFim"))));
                    sql.append(" 23:59:59', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }
            }else{
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
            }
            
            if (mostrarBoleto) {
                if (request.getParameter("boletoVencInicio") != null && request.getParameter("boletoVencInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("boletoVencInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("boletoVencFim") != null && request.getParameter("boletoVencFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("boletoVencFim"))));
                    sql.append(" 23:59:59', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }
                if (request.getParameter("boletoGeracaoInicio") != null && request.getParameter("boletoGeracaoInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("boletoGeracaoInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("boletoGeracaoFim") != null && request.getParameter("boletoGeracaoFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("boletoGeracaoFim"))));
                    sql.append(" 23:59:59', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }
                
                if (request.getParameter("boletoCancInicio") != null && request.getParameter("boletoCancInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("boletoCancInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("boletoCancFim") != null && request.getParameter("boletoCancFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("boletoCancFim"))));
                    sql.append(" 23:59:59', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }
            }else{
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
            }
            
            if (mostrarDebPre) {
                if (request.getParameter("debPreInicio") != null && request.getParameter("debPreInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("debPreInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("debPreFim") != null && request.getParameter("debPreFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("debPreFim"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("2999-12-31 00:00:00', '");
                }
                if (request.getParameter("debPreCancInicio") != null && request.getParameter("debPreCancInicio") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("debPreCancInicio"))));
                    sql.append(" 00:00:00', '");
                }else{
                    sql.append("1900-01-01 00:00:00', '");
                }
                if (request.getParameter("debPreCancFim") != null && request.getParameter("debPreCancFim") != ""){
                    sql.append(fmt.format(Datas.parse(request.getParameter("debPreCancFim"))));
                    sql.append(" 00:00:00'");
                }else{
                    sql.append("2999-12-31 00:00:00'");
                }
            }else{
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00', '");
                sql.append("1900-01-01 00:00:00', '1900-01-01 00:00:00'");
            }

            request.setAttribute("titulo", "Relatório de Receitas");
            request.setAttribute("titulo2", "Data de Referência " + fmt2.format(dtRef));
            
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            
            if (analitico){
                request.setAttribute("total1", "4");
            }else{
                request.setAttribute("total1", "2");
            }
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        } else {
            request.getRequestDispatcher("/pages/2510.jsp").forward(request, response);
        }
    }
}
