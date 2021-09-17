package techsoft.controle.operacoes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.operacoes.ReservaChurrasqueira;
import techsoft.operacoes.ReservaDependencia;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.ItemAluguel;
import techsoft.tabelas.TipoEvento;
import techsoft.util.Datas;

@Controller
public class ControleReservaDependencia{
    
    @App("1060")
    public static void consultar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {     
        String acao = request.getParameter("acao");
        
        if("imprimir".equals(acao)){
            ReservaDependencia r  = ReservaDependencia.getInstance(Integer.parseInt(request.getParameter("id")));
            if (r.getTipoEvento()>0){
                TipoEvento t = TipoEvento.getInstance(r.getTipoEvento());
                request.setAttribute("tipoEvento", t);
            }
            request.setAttribute("reserva", r);
            request.getRequestDispatcher("/pages/1061-comprovante.jsp").forward(request, response);
        }else{ 
            
            int mes = 0;
            int ano = 0;
            if (request.getParameter("mesAno")==null){
                mes = Calendar.getInstance().get(Calendar.MONTH)+1;
                ano = Calendar.getInstance().get(Calendar.YEAR);
            }else{
                mes = Integer.parseInt(request.getParameter("mesAno").substring(0, 2));
                ano = Integer.parseInt(request.getParameter("mesAno").substring(3));
            }
            int qtDiasMes = ReservaDependencia.getQtDiasMes(mes, ano);

            DecimalFormat df = new DecimalFormat( "0000" );
            DecimalFormat df2 = new DecimalFormat( "00" );
            String mesAno = df2.format(mes)+df.format(ano);
            request.setAttribute("mesAno", mesAno);

            int dep = 0;
            try{
                dep = Integer.parseInt(request.getParameter("dep"));
            }catch(Exception e){
                dep = 28;
            }

            request.setAttribute("dep", dep);
            request.setAttribute("qtDiasMes", qtDiasMes);
            request.setAttribute("dependencias", ComboBoxLoader.listarSql("SELECT SEQ_DEPENDENCIA, DESCR_DEPENDENCIA FROM TB_DEPENDENCIA WHERE SEQ_DEPENDENCIA > 23 ORDER BY 2"));
            request.getRequestDispatcher("/pages/1060-lista.jsp").forward(request, response);
        }
    }    

    
    @App("1061")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        String acao = request.getParameter("acao");
        
        if("gravarInclusao".equals(acao)){
            int dep = Integer.parseInt(request.getParameter("dep"));
            String itens = request.getParameter("itens");
            String titulo = request.getParameter("titulo");
            String nome = request.getParameter("nome");
            String telefone = request.getParameter("telefone");
            String contato = request.getParameter("contato");
            String telContato = request.getParameter("telContato");
            String situacao = request.getParameter("situacao");
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio") + " " + request.getParameter("hhInicio") + ":00");
            Date dtFim = Datas.parseDataHora(request.getParameter("dtFim") + " " + request.getParameter("hhFim") + ":00");
            int tipoEvento = Integer.parseInt(request.getParameter("tipoEvento"));
            int publicoPrev = 0;
            try{
                publicoPrev = Integer.parseInt(request.getParameter("publico"));
            }catch(Exception e){
                publicoPrev = 0;  
            }
            Date dtLimConf = Datas.parse(request.getParameter("dtLimConf"));
            
            
            
            ReservaDependencia r = new ReservaDependencia();
            r.setDependencia(dep);
            if ("".equals(titulo)){
                r.setMatricula(0);
                r.setCategoriaSocio(0);
                r.setDependente(0);
            }else{
                r.setMatricula(Integer.parseInt(titulo.substring(0,4)));
                r.setCategoriaSocio(Integer.parseInt(titulo.substring(4,6)));
                r.setDependente(Integer.parseInt(titulo.substring(6)));
            }
            r.setDtInicio(dtInicio);
            r.setDtFim(dtFim);
            r.setInteressado(nome);
            r.setTelefone(telefone);
            r.setNomeContato(contato);
            r.setTelContato(telContato);
            r.setStatus(situacao);
            r.setDtLimConf(dtLimConf);
            r.setPublico(publicoPrev);
            r.setTipoEvento(tipoEvento);
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1061", request.getRemoteAddr());
            try{
                r.inserir(audit, itens);
                
                Calendar cal = Calendar.getInstance();
                cal.setTime(dtInicio);
                int mes = cal.get(Calendar.MONTH);                
                int ano = cal.get(Calendar.YEAR);                
                DecimalFormat df = new DecimalFormat( "0000" );
                DecimalFormat df2 = new DecimalFormat( "00" );
                String mesAno = df2.format(mes+1) +'/'+df.format(ano);
                
                response.sendRedirect("c?app=1060&mesAno="+mesAno+"&dep="+dep);
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }

                
        }else if("gravarAlteracao".equals(acao)){
            String itens = request.getParameter("itens");
            String telefone = request.getParameter("telefone");
            String contato = request.getParameter("contato");
            String telContato = request.getParameter("telContato");
            String situacao = request.getParameter("situacao");
            int tipoEvento = Integer.parseInt(request.getParameter("tipoEvento"));
            int publicoPrev = 0;
            try{
                publicoPrev = Integer.parseInt(request.getParameter("publico"));
            }catch(Exception e){
                publicoPrev = 0;  
            }
            Date dtLimConf = Datas.parse(request.getParameter("dtLimConf"));
            
            
            
            ReservaDependencia r = new ReservaDependencia();
            r.setNumero(Integer.parseInt(request.getParameter("id")));
            r.setTelefone(telefone);
            r.setNomeContato(contato);
            r.setTelContato(telContato);
            r.setStatus(situacao);
            r.setDtLimConf(dtLimConf);
            r.setPublico(publicoPrev);
            r.setTipoEvento(tipoEvento);
            if (request.getParameter("dtConf")!=""){
                r.setDtConfirmacao(Datas.parseDataHora(request.getParameter("dtConf")));
            }
                
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1061", request.getRemoteAddr());
            try{
                r.alterar(audit, itens);
                response.sendRedirect("c?app=1060");
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }else if("incluir".equals(acao)){
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(request.getParameter("ano")), Integer.parseInt(request.getParameter("mes"))-1, Integer.parseInt(request.getParameter("dia")));            
            Date dt = cal.getTime();
            
            request.setAttribute("app", "1061");
            request.setAttribute("acao", "gravarInclusao");
            request.setAttribute("dia", request.getParameter("dia"));
            request.setAttribute("mes", request.getParameter("mes"));
            request.setAttribute("ano", request.getParameter("ano"));
            request.setAttribute("dep", request.getParameter("dep"));
            String data = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
            request.setAttribute("dtInicial", data);
            request.setAttribute("dtFinal", data);
            request.setAttribute("tipoPessoa", request.getParameter("tipoPessoa"));
            request.setAttribute("tipoEventos", ComboBoxLoader.listar("TB_TIPO_EVENTO"));
            request.setAttribute("itens", ItemAluguel.listar());
            request.getRequestDispatcher("/pages/1060.jsp").forward(request, response);
                
        }else{//alterar
            ReservaDependencia r  = ReservaDependencia.getInstance(Integer.parseInt(request.getParameter("id")));
            
            request.setAttribute("app", "1061");
            request.setAttribute("acao", "gravarAlteracao");
            request.setAttribute("dtInicial", r.getDtInicio());
            request.setAttribute("dtFinal", r.getDtFim());
            request.setAttribute("reserva", r);
            if (r.getMatricula()>0){
                request.setAttribute("tipoPessoa", "S");
            }else{
                request.setAttribute("tipoPessoa", "N");
            }
            
            request.setAttribute("tipoEventos", ComboBoxLoader.listar("TB_TIPO_EVENTO"));
            request.setAttribute("itens", ItemAluguel.listar(r.getNumero()));
            request.getRequestDispatcher("/pages/1060.jsp").forward(request, response);
        }        
    }    

    @App("1062")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        ReservaDependencia r  = ReservaDependencia.getInstance(Integer.parseInt(request.getParameter("id")));
        r.setUsuarioCanc(request.getUserPrincipal().getName());
        r.setDescricaoCanc(request.getParameter("motivoParm"));

        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1061", request.getRemoteAddr());
        try{
            r.excluir(audit);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(r.getDtInicio());
            int mes = cal.get(Calendar.MONTH);                
            int ano = cal.get(Calendar.YEAR);                
            DecimalFormat df = new DecimalFormat( "0000" );
            DecimalFormat df2 = new DecimalFormat( "00" );
            String mesAno = df2.format(mes+1) +'/'+df.format(ano);

            response.sendRedirect("c?app=1060&mesAno="+mesAno+"&dep="+r.getDependencia());
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }

    }      
    
    @App("1530")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if("consultar".equals(acao)){
            int mostraCanceladas = 0;
            
            try{
                mostraCanceladas=Integer.parseInt(request.getParameter("trazCanceladas"));
            }catch(Exception e){
                mostraCanceladas=0;
            }
            
            List<ReservaChurrasqueira> reservas = ReservaChurrasqueira.Consultar(
                                                                Datas.parse(request.getParameter("dtInicio")), 
                                                                Datas.parse(request.getParameter("dtFim")), 
                                                                request.getParameter("nome"), 
                                                                Integer.parseInt(request.getParameter("dep")), 
                                                                mostraCanceladas,
                                                                request.getParameter("origem"));
            request.setAttribute("reservas", reservas);
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
            
            if ("CH".equals(request.getParameter("origem"))){
                request.setAttribute("dependencias", ComboBoxLoader.listar("VW_CHURRASQUEIRAS"));
            }else{
                request.setAttribute("dependencias", ComboBoxLoader.listarSql("SELECT 0, '&lt; TODAS &gt;' UNION ALL SELECT SEQ_DEPENDENCIA, DESCR_DEPENDENCIA FROM TB_DEPENDENCIA WHERE SEQ_DEPENDENCIA > 23 ORDER BY 2"));
            }
            
            request.getRequestDispatcher("/pages/1530.jsp").forward(request, response);            
        }else if("imprimir".equals(acao)){
            
            String sql = "SELECT " +
                            "T2.DESCR_DEPENDENCIA AS 'Dependência', " +
                            "T1.NOME_INTERESSADO AS 'Nome', " +
                            "CONVERT(VARCHAR, T1.DT_INIC_UTILIZACAO, 103) + ' ' + CONVERT(VARCHAR, T1.DT_INIC_UTILIZACAO, 108) AS 'Início', " +
                            "CONVERT(VARCHAR, T1.DT_FIM_UTILIZACAO, 103) + ' ' + CONVERT(VARCHAR, T1.DT_FIM_UTILIZACAO, 108) AS 'Fim' ";
            
            if(request.getParameter("origem").equals("CH")){
                   sql = sql + ", T1.USER_CONFIRMACAO AS 'Usuário', " +
                               "(SELECT COUNT(*) FROM TB_CONVITE T3 WHERE T1.SEQ_RESERVA = T3.SEQ_RESERVA AND CD_STATUS_CONVITE <> 'CA') AS 'Qt. Convite' ";
                
            }
            
            sql = sql + "FROM " +
                            "TB_RESERVA_DEPENDENCIA T1, " +
                            "TB_DEPENDENCIA T2 " +
                        "WHERE " +
                            "T1.SEQ_DEPENDENCIA = T2.SEQ_DEPENDENCIA AND ";
            
            if (request.getParameter("origem").equals("CH")){
                sql = sql +"T1.SEQ_DEPENDENCIA < 24 ";
            }else{
                sql = sql +"T1.SEQ_DEPENDENCIA > 23 ";
            }

            if (!"".equals(request.getParameter("nome"))){
                sql = sql + " AND T1.NOME_INTERESSADO LIKE '%" + request.getParameter("nome") + "%'";
            }
            if (Integer.parseInt(request.getParameter("dep"))>0){
                sql = sql + " AND T1.SEQ_DEPENDENCIA = " + request.getParameter("dep");
            }
            if (Integer.parseInt(request.getParameter("trazCanceladas"))!=1){
                sql = sql + " AND T1.CD_STATUS_RESERVA <> 'CA'";
            }
            if (request.getParameter("dtInicio")!=""){
                LocalDate dataInicio = LocalDate.parse(request.getParameter("dtInicio"), DateTimeFormat.forPattern("dd/MM/yyyy"));
            
                sql = sql + " AND T1.DT_INIC_UTILIZACAO >= '" + dataInicio.toString("MM/dd/yyyy") + "'";
            }
            if (request.getParameter("dtFim")!=""){
                String sdtfim = request.getParameter("dtFim");
                LocalDate dataFim = LocalDate.parse(request.getParameter("dtFim"), DateTimeFormat.forPattern("dd/MM/yyyy"));
                sql = sql + " AND T1.DT_INIC_UTILIZACAO < DATEADD(D, 1, '" + dataFim.toString("MM/dd/yyyy") + "')";
            }

            sql = sql + " ORDER BY DT_INIC_UTILIZACAO ";
            
            request.setAttribute("titulo", "Relatório de Reservas de Churrasqueiras");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                        
            
        }else{
            if ("CH".equals(request.getParameter("origem"))){
                request.setAttribute("dependencias", ComboBoxLoader.listar("VW_CHURRASQUEIRAS"));
            }else{
                request.setAttribute("dependencias", ComboBoxLoader.listarSql("SELECT 0, '&lt; TODAS &gt;' UNION ALL SELECT SEQ_DEPENDENCIA, DESCR_DEPENDENCIA FROM TB_DEPENDENCIA WHERE SEQ_DEPENDENCIA > 23 ORDER BY 2"));
            }
            request.setAttribute("origem", request.getParameter("origem"));
            request.getRequestDispatcher("/pages/1530.jsp").forward(request, response);
        }
    }
        
    @App("1063")
    public static void consultarCancelamentos(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if("consultar".equals(acao)){
            request.setAttribute("dataInicio", Datas.parse(request.getParameter("dataInicio")));
            request.setAttribute("dataFim", Datas.parse(request.getParameter("dataFim")));
            request.getRequestDispatcher("/pages/1064.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("/pages/1064.jsp").forward(request, response);
        }
    }
    
    @App("1460")
    public static void relatorioGeralEventos(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT T1.SEQ_RESERVA, ");

            for(String s : request.getParameterValues("colunas")){
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() -2, sql.length());

            sql.append("FROM ");
            sql.append("TB_RESERVA_DEPENDENCIA T1, ");
            sql.append("TB_DEPENDENCIA T2, ");
            sql.append("TB_TIPO_EVENTO T3 ");
            sql.append("WHERE ");
            sql.append("T1.SEQ_DEPENDENCIA = T2.SEQ_DEPENDENCIA AND ");
            sql.append("T1.CD_TIPO_EVENTO *= T3.CD_TIPO_EVENTO AND ");
            sql.append("T2.SEQ_DEPENDENCIA > 23 AND ");

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            
            sql.append("T1.DT_INIC_UTILIZACAO >= '");
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00' AND ");
            sql.append("T1.DT_INIC_UTILIZACAO <= '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59' ");
            
            //SITUACAO DA RESERVA
            Boolean reservada = Boolean.parseBoolean(request.getParameter("reservada"));
            Boolean confirmada = Boolean.parseBoolean(request.getParameter("confirmada"));
            
            if(confirmada && reservada){
                sql.append(" AND CD_STATUS_RESERVA IN ('CF', 'AC') ");
            }else{
                if(confirmada){
                    sql.append(" AND CD_STATUS_RESERVA = 'CF' ");
                }else{
                    sql.append(" AND CD_STATUS_RESERVA = 'AC' ");
                }
            }

            //DEPENDENCIAS
            sql.append(" AND T2.SEQ_DEPENDENCIA IN (");
            for(String s : request.getParameterValues("dependencias")){
                sql.append(s);
                sql.append(", ");
            }
            sql.delete(sql.length() -2, sql.length());
            sql.append(") ");            

            //TIPOS DE EVENTOS
            sql.append(" AND (");
            StringBuilder tmp = new StringBuilder();
            tmp.append("(");
            for(String s : request.getParameterValues("tiposEvento")){
                if(s.equals("0")){
                    sql.append(" T1.CD_TIPO_EVENTO IS NULL OR ");
                }else{
                    tmp.append(s);
                    tmp.append(", ");
                }
            }
            tmp.delete(tmp.length() -2, tmp.length());
            tmp.append(")");                        
            
            sql.append(" T1.CD_TIPO_EVENTO IN ");
            sql.append(tmp.toString());
            sql.append(")");
            
            //ITENS DE ALUGUEL
            String nenhumItemAluguelSelecionado = "";
            tmp.delete(0, tmp.length());
            tmp.append("(");
            for(String s : request.getParameterValues("itensAluguel")){
                if(s.equals("0")){
                    nenhumItemAluguelSelecionado = " (NOT EXISTS (SELECT 1 FROM TB_ITEM_ALUGUEL_DEP T5 WHERE T1.SEQ_RESERVA = T5.SEQ_RESERVA))";
                }else{
                    tmp.append(s);
                    tmp.append(", ");
                }
            }
            if(tmp.length() > 1){
                tmp.delete(tmp.length() -2, tmp.length());
                tmp.append(")");
            }else{
                tmp.delete(0, tmp.length());
            }
            
            if(tmp.length() > 0){
                sql.append(" AND (EXISTS (SELECT 1 FROM TB_ITEM_ALUGUEL_DEP T4 WHERE T1.SEQ_RESERVA = T4.SEQ_RESERVA AND T4.CD_ITEM_ALUGUEL IN ");
                sql.append(tmp);
                sql.append(")");
                if(nenhumItemAluguelSelecionado.length() > 0){
                    sql.append(" OR ");
                    sql.append(nenhumItemAluguelSelecionado);
                }
                sql.append(")");
            }else{
                if(nenhumItemAluguelSelecionado.length() > 0){
                    sql.append(" AND ");
                    sql.append(nenhumItemAluguelSelecionado);
                }
            }
            
            sql.append(" ORDER BY 2 ");
            
            tmp.delete(0, tmp.length());
            if(request.getParameterValues("itens") != null){
                tmp.append("(");
                for(String s : request.getParameterValues("itens")){
                    tmp.append(s);
                    tmp.append(", ");
                }                
                tmp.delete(tmp.length() -2, tmp.length());
                tmp.append(")");
            }

            request.setAttribute("itens", tmp.toString());
            request.setAttribute("sql", sql.toString());
            request.setAttribute("dataInicio", request.getParameter("dataInicio"));
            request.setAttribute("dataFim", request.getParameter("dataFim"));
            
            request.getRequestDispatcher("/pages/1460-resultado.jsp").forward(request, response);
        }else{
            request.setAttribute("dependencias", ComboBoxLoader.listar("TB_DEPENDENCIA"));
            request.setAttribute("tiposEvento", ComboBoxLoader.listar("TB_TIPO_EVENTO"));
            request.setAttribute("itensAluguel", ComboBoxLoader.listar("TB_ITEM_ALUGUEL"));
            request.getRequestDispatcher("/pages/1460.jsp").forward(request, response);
        }
    }
    
}
