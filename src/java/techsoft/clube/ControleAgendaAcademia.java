package techsoft.clube;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.Titular;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.clube.LocalAcademia;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControleAgendaAcademia{
    
    @App("3230")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao=request.getParameter("acao");
        
        if ("det".equals(acao)){
            int idServico = 0;
            int idFuncionario = 0;
            String turno;
            String tipoVaga;
            
            List<ComboBoxLoader> servicos = ComboBoxLoader.listarSql("SELECT NU_SEQ_SERVICO, DE_SERVICO FROM TB_SERVICO_ACADEMIA WHERE (DT_FIM_VIGENCIA >= CONVERT(DATETIME, CONVERT(VARCHAR, GETDATE(), 102)) OR DT_FIM_VIGENCIA IS NULL) AND IC_SITUACAO = 'N' ORDER BY 2");
            try{
                idServico = Integer.parseInt(request.getParameter("idServico"));
            }catch(NumberFormatException e){
                for (ComboBoxLoader c : servicos){
                    idServico = c.getId();
                    break;
                }
            }
            try{
                idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
            }catch(NumberFormatException e){
                idFuncionario = 0;
            }
            turno = request.getParameter("turno");
            if (turno==null){
                turno = "0";
            }
            tipoVaga = request.getParameter("tipoVaga");
            if (tipoVaga==null){
                tipoVaga = "T";
            }
            
            SimpleDateFormat fAno = new SimpleDateFormat("yyyy-MM-dd");

            request.setAttribute("data", request.getParameter("data"));
            request.setAttribute("dataYMD", fAno.format(Datas.parse(request.getParameter("data"))));
            request.setAttribute("idServico", idServico);
            request.setAttribute("idFuncionario", idFuncionario);
            request.setAttribute("turno", turno);
            request.setAttribute("tipoVaga", tipoVaga);
            
            request.setAttribute("funcionarios", ComboBoxLoader.listarSql("{call SP_FUNCIONARIO_VIGENTE_ACADEMIA}"));
            request.setAttribute("servicos", servicos);
            String teste = request.getParameter("ignorarAbertura");
            request.setAttribute("ignorarAbertura", request.getParameter("ignorarAbertura"));
            
            request.getRequestDispatcher("/pages/3230-lista-det.jsp").forward(request, response);
        }else if ("relatorio".equals(acao)){
            
            SimpleDateFormat fAno = new SimpleDateFormat("yyyy-MM-dd");
            
            String sql = "EXEC SP_REL_AGENDA_DIA_ACADEMIA '" +
                fAno.format(Datas.parse(request.getParameter("data"))) + "', "  + 
                request.getParameter("idServico") + ", " +
                request.getParameter("idFuncionario") + ", " +
                request.getParameter("turno");

            request.setAttribute("titulo", ServicoAcademia.getInstance(Integer.parseInt(request.getParameter("idServico"))).getDescricao() );
            request.setAttribute("titulo2", request.getParameter("data"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
            
        }else{    
            Date d = new Date();
            SimpleDateFormat fAno = new SimpleDateFormat("yyyy");
            SimpleDateFormat fMes = new SimpleDateFormat("MM");
            
            int mes;
            int ano;
            int idServico = 0;
            int idFuncionario = 0;
            String turno;
            String tipoVaga;
            
            
            try{
                mes = Integer.parseInt(request.getParameter("mes"));
            }catch(NumberFormatException e){
                mes = Integer.parseInt(fMes.format(d));
            }
            try{
                ano = Integer.parseInt(request.getParameter("ano"));
            }catch(NumberFormatException e){
                ano = Integer.parseInt(fAno.format(d));
            }
            
            List<ComboBoxLoader> servicos = ComboBoxLoader.listarSql("SELECT NU_SEQ_SERVICO, DE_SERVICO FROM TB_SERVICO_ACADEMIA WHERE (DT_FIM_VIGENCIA >= CONVERT(DATETIME, CONVERT(VARCHAR, GETDATE(), 102)) OR DT_FIM_VIGENCIA IS NULL) AND IC_SITUACAO = 'N' ORDER BY 2");
            try{
                idServico = Integer.parseInt(request.getParameter("idServico"));
            }catch(NumberFormatException e){
                for (ComboBoxLoader c : servicos){
                    idServico = c.getId();
                    break;
                }
            }
            try{
                idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
            }catch(NumberFormatException e){
                idFuncionario = 0;
            }
            turno = request.getParameter("turno");
            if (turno==null){
                turno = "0";
            }
            tipoVaga = request.getParameter("tipoVaga");
            if (tipoVaga==null){
                tipoVaga = "0";
            }
            
            request.setAttribute("mes", mes);
            request.setAttribute("ano", ano);
            request.setAttribute("idServico", idServico);
            request.setAttribute("idFuncionario", idFuncionario);
            request.setAttribute("turno", turno);
            request.setAttribute("tipoVaga", tipoVaga);
            
            request.setAttribute("funcionarios", ComboBoxLoader.listarSql("{call SP_FUNCIONARIO_VIGENTE_ACADEMIA}"));
            request.setAttribute("servicos", servicos);
            String teste = request.getParameter("ignorarAbertura");
            request.setAttribute("ignorarAbertura", request.getParameter("ignorarAbertura"));

            request.getRequestDispatcher("/pages/3230-lista.jsp").forward(request, response);
        }
    }
    
    @App("3231")
    public static void agendar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int idServico = Integer.parseInt(request.getParameter("idServico"));
        int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
        String turno = request.getParameter("turno");
        String data = request.getParameter("data");
         
        String dtInicio = request.getParameter("dtInicio");
        String dtFim  = request.getParameter("dtFim");
        int idFuncAgendamento = Integer.parseInt(request.getParameter("idFuncHidden"));
        String titulo = request.getParameter("titulo");
        String userSupervisao = request.getParameter("userSupervisao");
        
        int idAgenda;
        int idExcecao;
        
        try{
            idAgenda = Integer.parseInt(request.getParameter("idAgenda"));
        }catch(Exception e){
            idAgenda= 0;
        }
        try{
            idExcecao = Integer.parseInt(request.getParameter("idExcecao"));
        }catch(Exception e){
            idExcecao= 0;
        }
        
        AgendaAcademia p = new AgendaAcademia();
        p.setCdMatricula(Integer.parseInt(titulo.substring(0, 4)));
        p.setCdCategoria(Integer.parseInt(titulo.substring(4, 6)));
        p.setSeqDependente(Integer.parseInt(titulo.substring(6)));
        p.setDtInicio(Datas.parseComFormato(dtInicio, "yyyy-MM-dd HH:mm:ss"));
        p.setDtFim(Datas.parseComFormato(dtFim, "yyyy-MM-dd HH:mm:ss"));
        p.setIdServico(idServico);
        p.setIdFuncionario(idFuncAgendamento);
        p.setIdAgenda(idAgenda);
        p.setIdExcecao(idExcecao);
        p.setUserAgendamento(request.getUserPrincipal().getName());
        p.setUserSupervisao(userSupervisao);
        
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3231", request.getRemoteAddr());
        try{
            p.inserir(audit);
            response.sendRedirect("c?app=3230&acao=det&idServico="+idServico+"&idFuncionario="+idFuncionario+"&turno="+turno+"&data="+data);
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }
    
    @App("3233")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int idAgendamento = Integer.parseInt(request.getParameter("idAgendamento"));
        String userSupervisao = request.getParameter("userSupervisao");
        int idServico = Integer.parseInt(request.getParameter("idServico"));
        int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
        String turno = request.getParameter("turno");
        String data = request.getParameter("data");

        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3233", request.getRemoteAddr());
        try{
            AgendaAcademia.excluir(idAgendamento, request.getUserPrincipal().getName(), userSupervisao, audit);
            response.sendRedirect("c?app=3230&acao=det&idServico="+idServico+"&idFuncionario="+idFuncionario+"&turno="+turno+"&data="+data);
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
        
        

   }
    
    @App("3234")
    public static void marcaDesmarcaFalta(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao=request.getParameter("acao");
        
        int idAgendamento = Integer.parseInt(request.getParameter("idAgendamento"));

        int idServico = Integer.parseInt(request.getParameter("idServico"));
        int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
        String turno = request.getParameter("turno");
        String data = request.getParameter("data");

        Auditoria audit = new Auditoria(request.getParameter("userFalta"), "3234", request.getRemoteAddr());
        try{
            if(acao.equals("marca")){
                AgendaAcademia.marcaFalta(idAgendamento, request.getParameter("userFalta"), audit);
            }else if(acao.equals("desmarca")){
                AgendaAcademia.desmarcaFalta(idAgendamento, request.getParameter("userFalta"), audit);
            }
            
            if (!request.getParameter("userFalta").toUpperCase().equals(request.getUserPrincipal().getName().toUpperCase())){
                    request.getRequestDispatcher("redirect.jsp").forward(request, response);
            }else{
                response.sendRedirect("c?app=3230&acao=det&idServico="+idServico+"&idFuncionario="+idFuncionario+"&turno="+turno+"&data="+data);   
            }

        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
        
        

   }
}
