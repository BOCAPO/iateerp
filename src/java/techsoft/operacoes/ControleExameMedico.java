package techsoft.operacoes;

import java.io.IOException;
import java.math.BigDecimal;
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
import techsoft.acesso.LocalAcesso;
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
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;
import techsoft.operacoes.Evento;
import techsoft.tabelas.Material;

@Controller
public class ControleExameMedico{
    
    @App("1120")
    public static void consultar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        String acao = request.getParameter("acao");
        
        if("consultar".equals(acao)){
            int carteira = 0;
            int categoria = 0;
            int matricula = 0;
            String nome = request.getParameter("nome");

            try{
                carteira = Integer.parseInt(request.getParameter("carteira"));
            }catch(NumberFormatException e){
                carteira = 0;
            }
            try{
                categoria = Integer.parseInt(request.getParameter("categoria"));
            }catch(NumberFormatException e){
                categoria = 0;
            }
            try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
            }catch(NumberFormatException e){
                matricula = 0;
            }
            if(nome == null) nome = "";

            request.setAttribute("selecao", request.getParameter("selecao"));

            List<Socio> socios = Socio.listarExameMedico(carteira, categoria, matricula, nome);
            request.setAttribute("socios", socios);

            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));

            request.getRequestDispatcher("/pages/1120-selSocio.jsp").forward(request, response);            
        }else if("listaExame".equals(acao)){
            int categoria = 0;
            int matricula = 0;
            int dependente = 0;

            matricula = Integer.parseInt(request.getParameter("titulo").substring(0,4));
            categoria = Integer.parseInt(request.getParameter("titulo").substring(4,6));
            dependente = Integer.parseInt(request.getParameter("titulo").substring(6));
            
            
            request.setAttribute("socio", Socio.getInstance(matricula, dependente, categoria));
            request.setAttribute("exames", ExameMedico.listar(matricula, categoria, dependente));
            request.getRequestDispatcher("/pages/1120-lista.jsp").forward(request, response);            
        }else{
            request.getRequestDispatcher("/pages/1120-selSocio.jsp").forward(request, response);
        }
    }    
    
    @App("1121")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "1121");
                
                for (String p : Collections.list(request.getParameterNames()))
                        request.setAttribute(p, request.getParameter(p));
                
                request.getRequestDispatcher("/pages/1120.jsp").forward(request, response);            
            }else{
                ExameMedico d = new ExameMedico();
                d.setMatricula(Integer.parseInt(request.getParameter("matricula")));
                d.setCategoria(Integer.parseInt(request.getParameter("categoria")));
                d.setDependente(Integer.parseInt(request.getParameter("dependente")));
            	d.setConclusao(request.getParameter("conclusao"));
                d.setDescricao(request.getParameter("descricao"));
            	d.setDtValidade(Datas.parse(request.getParameter("validade")));
            	d.setResultado(request.getParameter("resultado"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1121", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1120&acao=listaExame&titulo="+request.getParameter("titulo"));
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    
    @App("1122")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "1122");
                
                request.setAttribute("exame", ExameMedico.getInstance(Integer.parseInt(request.getParameter("id"))));
                request.getRequestDispatcher("/pages/1120.jsp").forward(request, response);            
            }else{
                ExameMedico d = ExameMedico.getInstance(Integer.parseInt(request.getParameter("id")));
            	d.setConclusao(request.getParameter("conclusao"));
                d.setDescricao(request.getParameter("descricao"));
            	d.setDtValidade(Datas.parse(request.getParameter("validade")));
            	d.setResultado(request.getParameter("resultado"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1122", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=1120&acao=listaExame&titulo="+request.getParameter("titulo"));
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    
    @App("1123")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        ExameMedico b = ExameMedico.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1123", request.getRemoteAddr());
        b.excluir(audit);

        response.sendRedirect("c?app=1120");
    }        
    
    
}
