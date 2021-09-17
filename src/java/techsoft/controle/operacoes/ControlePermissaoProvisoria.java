package techsoft.controle.operacoes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.operacoes.PermissaoProvisoria;
import techsoft.cadastro.Socio;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.Cracha;
import techsoft.tabelas.FuncionarioHorario;
import techsoft.tabelas.InserirException;
import techsoft.util.Datas;

@Controller
public class ControlePermissaoProvisoria {

    @App("1230")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao=request.getParameter("acao");
    
        if("consultar".equals(acao)){
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));

            request.getRequestDispatcher("/pages/1230-lista.jsp").forward(request, response);
        }else{

            request.getRequestDispatcher("/pages/1230-lista.jsp").forward(request, response);
        }
    }
    
    @App("1231")
    public static void inserir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao=request.getParameter("acao");
    
        if("gravar".equals(acao)){
            PermissaoProvisoria f = new PermissaoProvisoria();
            preencherPermissao(f, request);
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1231", request.getRemoteAddr());
                f.inserir(audit);
                response.sendRedirect("c?app=1230&acao=consultar&nome="+request.getParameter("nome"));
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
        }else{
            request.setAttribute("app", "1231");
            request.getRequestDispatcher("/pages/1230.jsp").forward(request, response);
        }
    }    
    
    @App("1232")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao=request.getParameter("acao");
    
        int idPermissao = Integer.parseInt(request.getParameter("idPermissao"));
        PermissaoProvisoria f = PermissaoProvisoria.getInstance(idPermissao);
        if("gravar".equals(acao)){
            preencherPermissao(f, request);
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1232", request.getRemoteAddr());
                f.alterar(audit);
                response.sendRedirect("c?app=1230&acao=consultar&nome="+request.getParameter("nome"));
            }catch(AlterarException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }            
        }else{
            request.setAttribute("app", "1232");
            request.setAttribute("permissao", f);
            request.getRequestDispatcher("/pages/1230.jsp").forward(request, response);
        }
    }
    
    @App("1233")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    
        int idPermissao = Integer.parseInt(request.getParameter("idPermissao"));
        PermissaoProvisoria f = PermissaoProvisoria.getInstance(idPermissao);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1233", request.getRemoteAddr());
        f.excluir(audit);
        response.sendRedirect("c?app=1230");
    }
    
    @App("1234")
    public static void emitirCracha(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        
        String acao=request.getParameter("acao");
        
        int idFuncionario = Integer.parseInt(request.getParameter("idPermissao"));
        PermissaoProvisoria f = PermissaoProvisoria.getInstance(idFuncionario);
        
        if("showForm".equals(acao)){
            request.setAttribute("permissao", f);
            request.getRequestDispatcher("/pages/1230-impressao.jsp").forward(request, response);                
        }else if("emitirCracha".equals(acao)){
            request.setAttribute("permissao", f);
            request.getRequestDispatcher("/pages/1230-impressa.jsp").forward(request, response);                
        }else if("excluirFoto".equals(acao)){
            f.excluirFoto();
            //reload na pagina
            response.sendRedirect("c?app=1234&acao=showForm&idPermissao=" + f.getId());
        }


    }
    
    private static void preencherPermissao(PermissaoProvisoria f, HttpServletRequest request){
        f.setNome(request.getParameter("nome"));
        f.setAtividade(request.getParameter("atividade"));
        f.setInicio(Datas.parse(request.getParameter("inicio")));
        f.setFim(Datas.parse(request.getParameter("fim")));
        f.setEstacionamento(request.getParameter("estacionamento"));
        for(int i=0;i<7;i++){
            f.getHorarios()[i] = null;
        }
        

        String entrada = request.getParameter("segundaEntrada");
        String saida = request.getParameter("segundaSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[0] = new FuncionarioHorario(entrada, saida);
        }

        entrada = request.getParameter("tercaEntrada");
        saida = request.getParameter("tercaSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[1] = new FuncionarioHorario(entrada, saida);
        }
        
        entrada = request.getParameter("quartaEntrada");
        saida = request.getParameter("quartaSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[2] = new FuncionarioHorario(entrada, saida);
        }

        entrada = request.getParameter("quintaEntrada");
        saida = request.getParameter("quintaSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[3] = new FuncionarioHorario(entrada, saida);
        }
        
        entrada = request.getParameter("sextaEntrada");
        saida = request.getParameter("sextaSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[4] = new FuncionarioHorario(entrada, saida);
        }
        
        entrada = request.getParameter("sabadoEntrada");
        saida = request.getParameter("sabadoSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[5] = new FuncionarioHorario(entrada, saida);
        }        
        
        entrada = request.getParameter("domingoEntrada");
        saida = request.getParameter("domingoSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[6] = new FuncionarioHorario(entrada, saida);
        }        
    }
}
