package techsoft.clube;

import java.io.IOException;
import java.math.BigDecimal;
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
import techsoft.tabelas.Funcionario;
import techsoft.tabelas.Material;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleExcecaoAgendaAcademia{
    

    @App("3220")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String dtRef = "";
        int idServico = 0;
        int idFuncionario = 0;
        String idTipoPesquisa = "";
        String tipo = "";
        if (request.getParameter("dtRef")==null){
            Date d = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            
            dtRef = f.format(d);
        }else{
            dtRef = request.getParameter("dtRef");
        }
        try{
            idServico = Integer.parseInt(request.getParameter("idServico"));
        }catch(Exception e){}
        try{
            idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
        }catch(Exception e){}
        if (request.getParameter("idTipoPesquisa")==null){
            idTipoPesquisa = "A";
        }else{
            idTipoPesquisa = request.getParameter("idTipoPesquisa");
        }
        if (request.getParameter("tipo")==null){
            tipo = "T";
        }else{
            tipo = request.getParameter("tipo");
        }
        
        
        request.setAttribute("excecoes", ExcecaoAgendaAcademia.listar(tipo, idServico, idFuncionario, idTipoPesquisa, Datas.parse(dtRef)));
        
        request.setAttribute("dtRef", dtRef);
        request.setAttribute("tipo", tipo);
        request.setAttribute("idServico", idServico);
        request.setAttribute("idFuncionario", idFuncionario);
        request.setAttribute("idTipoPesquisa", idTipoPesquisa);
        request.setAttribute("servicos", ServicoAcademia.listar());
        request.setAttribute("funcionarios", ComboBoxLoader.listarSql("{call SP_FUNCIONARIO_ACADEMIA }"));
        request.getRequestDispatcher("/pages/3220-lista.jsp").forward(request, response);
    }
    
    @App("3221")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "3221");
            request.setAttribute("servicos", ServicoAcademia.listar());
            request.setAttribute("funcionarios", ComboBoxLoader.listarSql("{call SP_FUNCIONARIO_ACADEMIA }"));
            request.getRequestDispatcher("/pages/3220.jsp").forward(request, response);            
        }else if("gravar".equals(acao)){
            ExcecaoAgendaAcademia s = new ExcecaoAgendaAcademia();
            carregaVariaveis(s, request);
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3221", request.getRemoteAddr());
            try{
                s.inserir(audit);
                response.sendRedirect("c?app=3220");
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }
    }
    
    @App("3223")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        int id = Integer.parseInt(request.getParameter("id"));
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3223", request.getRemoteAddr());
            try{
                ExcecaoAgendaAcademia.excluir(audit, id);
                response.sendRedirect("c?app=3220");
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
    }        
    
    private static void carregaVariaveis(ExcecaoAgendaAcademia s, HttpServletRequest request){
        
        s.setTipo(request.getParameter("tipo"));
        s.setDtInicio(Datas.parse(request.getParameter("dtInicio")));
        s.setDtFim(Datas.parse(request.getParameter("dtFim")));
        s.setHhInicio(request.getParameter("hhInicio"));
        s.setHhFim(request.getParameter("hhFim"));
        try{
            s.setDtAbertura(Datas.parseDataHora(request.getParameter("dtAbertura")));
        }catch(Exception e){}
        try{
            s.setIdServico(Integer.parseInt(request.getParameter("idServico")));
        }catch(Exception e){}
        try{
            s.setIdFuncionario(Integer.parseInt(request.getParameter("idFuncionario")));
        }catch(Exception e){}
        if("S".equals(request.getParameter("icSegunda"))){
            s.setIcSegunda("S");
        }else{
            s.setIcSegunda("N");
        }
        if("S".equals(request.getParameter("icTerca"))){
            s.setIcTerca("S");
        }else{
            s.setIcTerca("N");
        }
        if("S".equals(request.getParameter("icQuarta"))){
            s.setIcQuarta("S");
        }else{
            s.setIcQuarta("N");
        }
        if("S".equals(request.getParameter("icQuinta"))){
            s.setIcQuinta("S");
        }else{
            s.setIcQuinta("N");
        }
        if("S".equals(request.getParameter("icSexta"))){
            s.setIcSexta("S");
        }else{
            s.setIcSexta("N");
        }
        if("S".equals(request.getParameter("icSabado"))){
            s.setIcSabado("S");
        }else{
            s.setIcSabado("N");
        }
        if("S".equals(request.getParameter("icDomingo"))){
            s.setIcDomingo("S");
        }else{
            s.setIcDomingo("N");
        }
    }
    
}
