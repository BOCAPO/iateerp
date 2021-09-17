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
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.operacoes.TaxaAdministrativa;

@Controller
public class ControleServicoAcademia{
    

    @App("3210")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.setAttribute("lista", ServicoAcademia.listar());

        request.getRequestDispatcher("/pages/3210-lista.jsp").forward(request, response);
    }
    
    @App("3211")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "3211");
            request.setAttribute("taxasAdministrativas", ComboBoxLoader.listar("VW_TAXA_ADMINISTRATIVA"));
            request.getRequestDispatcher("/pages/3210.jsp").forward(request, response);            
        }else if("gravar".equals(acao)){
            ServicoAcademia s = new ServicoAcademia();
            carregaVariaveis(s, request);
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3211", request.getRemoteAddr());
            try{
                s.inserir(audit);
                response.sendRedirect("c?app=3210");
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }

    }
    
    @App("3212")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        String acao = request.getParameter("acao");
        
        if("showForm".equals(acao)){
            request.setAttribute("app", "3212");
            request.setAttribute("servico", ServicoAcademia.getInstance(Integer.parseInt(request.getParameter("id"))));
            request.setAttribute("servicos", ServicoAcademia.listar());
            request.setAttribute("taxasAdministrativas", ComboBoxLoader.listar("VW_TAXA_ADMINISTRATIVA"));
            String sql = "";
            sql = "{call SP_FUNCIONARIO_SERVICO_ACADEMIA ("+request.getParameter("id")+")}";
            request.setAttribute("funcionarios", ComboBoxLoader.listarSql(sql));
            
            if (request.getParameter("funcionario")!=null){
                request.setAttribute("funcSel", request.getParameter("funcionario"));
            }else{
                request.setAttribute("funcSel", "0");
            }
            String tipo = "AT";
            String dtRef = "";

            if (request.getParameter("tipo")!=null){
                tipo = request.getParameter("tipo");
            }
            if (request.getParameter("dtRef")==null){
                Date d = new Date();
                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

                dtRef = f.format(d);
            }else{
                dtRef = request.getParameter("dtRef");
            }

            request.setAttribute("tipo", tipo);
            request.setAttribute("dtRef", dtRef);
            

            request.getRequestDispatcher("/pages/3210.jsp").forward(request, response);            
        }else if("gravar".equals(acao)){
            ServicoAcademia s = ServicoAcademia.getInstance(Integer.parseInt(request.getParameter("id")));
            
            carregaVariaveis(s, request);
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3212", request.getRemoteAddr());
            try{
                s.alterar(audit);
                response.sendRedirect("c?app=3210");
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
        }else if("importar".equals(acao)){
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3214", request.getRemoteAddr());
            try{
                ServicoAcademia.importar(Integer.parseInt(request.getParameter("idImportacao")), Integer.parseInt(request.getParameter("id")), audit);
                response.sendRedirect("c?app=3212&acao=showForm&id="+request.getParameter("id"));
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }
    }        
    
    
    @App("3213")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        ServicoAcademia b = ServicoAcademia.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3213", request.getRemoteAddr());
            try{
                b.excluir(audit);
                response.sendRedirect("c?app=3210");
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }

    }        
    
    @App("3215")
    public static void incluirProfessor(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "3215");
            request.setAttribute("funcionarios", ComboBoxLoader.listarSql("{call SP_FUNCIONARIO_ACADEMIA }"));
            request.setAttribute("idServico", request.getParameter("idServico"));
            request.setAttribute("locais", ComboBoxLoader.listar("TB_LOCAL_ACADEMIA"));

            request.getRequestDispatcher("/pages/3210-professor.jsp").forward(request, response);            
        }else if("gravar".equals(acao)){
            ProfissionalAcademia p = new ProfissionalAcademia();
            p.setIdFuncionario(Integer.parseInt(request.getParameter("idFuncionario")));
            p.setIdServico(Integer.parseInt(request.getParameter("idServico")));
            if (!request.getParameter("dtInicioVigencia").equals("")){
                p.setDtInicioVigencia(Datas.parse(request.getParameter("dtInicioVigencia")));
            }
            if (!request.getParameter("dtFimVigencia").equals("")){
                p.setDtFimVigencia(Datas.parse(request.getParameter("dtFimVigencia")));
            }
            if (!request.getParameter("dtAbertura").equals("")){
                p.setDtAbertura(Datas.parseDataHora(request.getParameter("dtAbertura")));
            }else{
                p.setDtAbertura(null);
            }
            p.setHorarios(request.getParameter("horarios"));
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3216", request.getRemoteAddr());
            try{
                p.inserir(audit);
                response.sendRedirect("c?app=3212&acao=showForm&id="+p.getIdServico());
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }

        }
    }
    
    @App("3217")
    public static void alterarProfessor(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "3217");
            request.setAttribute("funcionarios", ComboBoxLoader.listarSql("{call SP_FUNCIONARIO_ACADEMIA }"));
            request.setAttribute("locais", ComboBoxLoader.listar("TB_LOCAL_ACADEMIA"));
            ProfissionalAcademia p = ProfissionalAcademia.getInstance(Integer.parseInt(request.getParameter("idProfissional")));
            request.setAttribute("idServico", p.getIdServico());
            request.setAttribute("profissional", p);
            request.getRequestDispatcher("/pages/3210-professor.jsp").forward(request, response);            
        }else if("gravar".equals(acao)){
            ProfissionalAcademia p = ProfissionalAcademia.getInstance(Integer.parseInt(request.getParameter("idProfissional")));
            if (!request.getParameter("dtInicioVigencia").equals("")){
                p.setDtInicioVigencia(Datas.parse(request.getParameter("dtInicioVigencia")));
            }else{
                p.setDtInicioVigencia(null);
            }
            if (!request.getParameter("dtFimVigencia").equals("")){
                p.setDtFimVigencia(Datas.parse(request.getParameter("dtFimVigencia")));
            }else{
                p.setDtFimVigencia(null);
            }
            if (!request.getParameter("dtAbertura").equals("")){
                p.setDtAbertura(Datas.parseDataHora(request.getParameter("dtAbertura")));
            }else{
                p.setDtAbertura(null);
            }
            p.setHorarios(request.getParameter("horarios"));
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3217", request.getRemoteAddr());
            try{
                p.alterar(audit);
                response.sendRedirect("c?app=3212&acao=showForm&id="+p.getIdServico());
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }else if("excluir".equals(acao)){
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3217", request.getRemoteAddr());
            try{
                ProfissionalAcademia.excluir(audit, Integer.parseInt(request.getParameter("idProfissional")));
                response.sendRedirect("c?app=3212&acao=showForm&id="+request.getParameter("idServico"));
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }
    }   
    @App("3218") 
    public static void duplicarAgenda(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("duplicar".equals(acao)){
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3218", request.getRemoteAddr());
            ProfissionalAcademia p = ProfissionalAcademia.getInstance(Integer.parseInt(request.getParameter("idProfissional")));
            try{
                ProfissionalAcademia.duplicar(Integer.parseInt(request.getParameter("idProfissional")), audit);
                response.sendRedirect("c?app=3212&acao=showForm&id="+p.getIdServico());
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }
    }
    
    
    private static void carregaVariaveis(ServicoAcademia s, HttpServletRequest request){

        s.setDescricao(request.getParameter("descricao"));
        try{
            s.setQtAluno(Integer.parseInt(request.getParameter("qtAluno")));
        }catch(Exception e){}
        if("".equals(request.getParameter("qtLimiteSocio"))){
            s.setQtLimiteSocio(null);
        }else{
            s.setQtLimiteSocio(Integer.parseInt(request.getParameter("qtLimiteSocio")));
        }
        if("".equals(request.getParameter("qtDiasLimiteSocio"))){
            s.setQtDiasLimiteSocio(null);
        }else{
            s.setQtDiasLimiteSocio(Integer.parseInt(request.getParameter("qtDiasLimiteSocio")));
        }
        if("".equals(request.getParameter("qtDiasAntClube"))){
            s.setQtDiasAntClube(null);
        }else{
            s.setQtDiasAntClube(Integer.parseInt(request.getParameter("qtDiasAntClube")));
        }
        try{
            s.setQtMinAtendimento(Integer.parseInt(request.getParameter("qtMinAtendimento")));
        }catch(Exception e){}
        try{
            s.setQtMinIntervalo(Integer.parseInt(request.getParameter("qtMinIntervalo")));
        }catch(Exception e){
            s.setQtMinIntervalo(null);
        }
        if("".equals(request.getParameter("vrMulta"))){
            s.setVrMulta(null);
        }else{
            s.setVrMulta(new BigDecimal(request.getParameter("vrMulta").replaceAll(",", ".") ));
        }
        if("0".equals(request.getParameter("cdTxMulta"))){
            s.setCdTxMulta(null);
        }else{
            s.setCdTxMulta(Integer.parseInt(request.getParameter("cdTxMulta")));
        }
        if("".equals(request.getParameter("vrServico"))){
            s.setVrServico(null);
        }else{
            s.setVrServico(new BigDecimal(request.getParameter("vrServico").replaceAll(",", ".") ));
        }
        if("0".equals(request.getParameter("cdTxServico"))){
            s.setCdTxServico(null);
        }else{
            s.setCdTxServico(Integer.parseInt(request.getParameter("cdTxServico")));
        }
        if("".equals(request.getParameter("pzCancelamento"))){
            s.setPzCancelamento(null);
        }else{
            s.setPzCancelamento(Integer.parseInt(request.getParameter("pzCancelamento")));
        }
        s.setTpPzCancelamento(request.getParameter("tpPzCancelamento"));
        if("S".equals(request.getParameter("icAgendaInternet"))){
            s.setIcAgendaInternet("S");
        }else{
            s.setIcAgendaInternet("N");
        }
        if("".equals(request.getParameter("qtDiasAntInternet"))){
            s.setQtDiasAntInternet(null);
        }else{
            s.setQtDiasAntInternet(Integer.parseInt(request.getParameter("qtDiasAntInternet")));
        }
        if("".equals(request.getParameter("dtInicioVigencia"))){
            s.setDtInicioVigencia(null);
        }else{
            s.setDtInicioVigencia(Datas.parse(request.getParameter("dtInicioVigencia")));
        }
        if("".equals(request.getParameter("dtFimVigencia"))){
            s.setDtFimVigencia(null);
        }else{
            s.setDtFimVigencia(Datas.parse(request.getParameter("dtFimVigencia")));
        }
        s.setTpPzLimite(request.getParameter("tpPzLimite"));
        
        s.setDetalhamento(request.getParameter("detalhamento"));
        s.setInstrucao(request.getParameter("instrucao"));
        
        s.setTpPzAntAgendClube(request.getParameter("tpPzAntAgendClube"));
        s.setTpPzAntAgendInternet(request.getParameter("tpPzAntAgendInternet"));
        
        try{
            s.setVagaClube(Integer.parseInt(request.getParameter("vagaClube")));
        }catch(Exception e){}
        try{
            s.setVagaInternet(Integer.parseInt(request.getParameter("vagaInternet")));
        }catch(Exception e){}
        
        if("".equals(request.getParameter("prazoMaximoClube"))){
            s.setPrazoMaximoClube(null);
        }else{
            s.setPrazoMaximoClube(Integer.parseInt(request.getParameter("prazoMaximoClube")));
        }
        if("".equals(request.getParameter("prazoMaximoInternet"))){
            s.setPrazoMaximoInternet(null);
        }else{
            s.setPrazoMaximoInternet(Integer.parseInt(request.getParameter("prazoMaximoInternet")));
        }
        
        if("".equals(request.getParameter("hhInicClube"))){
            s.setHhInicClube(null);
        }else{
            s.setHhInicClube(Integer.parseInt(request.getParameter("hhInicClube")));
        }
        if("".equals(request.getParameter("hhInicInternet"))){
            s.setHhInicInternet(null);
        }else{
            s.setHhInicInternet(Integer.parseInt(request.getParameter("hhInicInternet")));
        }

    }
}
