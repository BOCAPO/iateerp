package techsoft.controle.curso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import techsoft.cadastro.CarroSocio;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.curso.CancelarMatriculaException;
import techsoft.curso.ReativarMatriculaException;
import techsoft.curso.Turma;
import techsoft.seguranca.Auditoria;

@Controller
@MultipartConfig
public class ControleMatricula extends HttpServlet {

    @App("3040")
    public static void matricular(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");
        
        if("showFormSelecionarSocio".equals(acao)){
            request.setAttribute("idTurma", request.getParameter("idTurma"));
            request.getRequestDispatcher("/pages/3040-selecionar-socio.jsp").forward(request, response);                    
        }else if("consultarSocio".equals(acao)){
            int carteira = 0;
            int categoria = 0;
            int matricula = 0;
            String nome = request.getParameter("nome");

            try{
                carteira = Integer.parseInt(request.getParameter("carteira"));
            }catch(Exception e){
                carteira = 0;
            }
            try{
                categoria = Integer.parseInt(request.getParameter("categoria"));
            }catch(Exception e){
                categoria = 0;
            }
            try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
            }catch(Exception e){
                matricula = 0;
            }
            if(nome == null) nome = "";

            List<Socio> socios = Socio.listar(carteira, categoria, matricula, nome);
            request.setAttribute("socios", socios);
            request.setAttribute("idTurma", request.getParameter("idTurma"));
            request.getRequestDispatcher("/pages/3040-selecionar-socio.jsp").forward(request, response);            
        }else if("matricular".equals(acao)){
                int matricula = 0;
                int categoria = 0;
                int dependente = 0;
                try{
                    categoria = Integer.parseInt(request.getParameter("titulo").substring(4, 6));
                }catch(NumberFormatException e){
                    request.setAttribute("msg", "1 - Erro na inclusão da matrícula. Favor tentar novamente ou entrar em contato com o Administrador do sistema.");
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response); 
                    return;
                }
                try{
                    matricula = Integer.parseInt(request.getParameter("titulo").substring(0,4));
                }catch(NumberFormatException e){
                    request.setAttribute("msg", "2 - Erro na inclusão da matrícula. Favor tentar novamente ou entrar em contato com o Administrador do sistema.");
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                                                
                    return;
                }
                try{
                    dependente = Integer.parseInt(request.getParameter("titulo").substring(6));
                }catch(NumberFormatException e){
                    request.setAttribute("msg", "3 - Erro na inclusão da matrícula. Favor tentar novamente ou entrar em contato com o Administrador do sistema.");
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                                                
                    return;
                }

            Socio socio = Socio.getInstance(matricula, dependente, categoria);
            int idTurma = Integer.parseInt(request.getParameter("idTurma"));
            Turma t = Turma.getInstance(idTurma);

            if(t.isSocioMatriculado(socio)){
                request.setAttribute("msg", "A Pessoa já é aluna desta Turma.");
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                                                
                return;
            }
            
            if(!socio.isMatriculaAutorizada()){
                request.setAttribute("msg", "Título não autorizado para fazer matrículas em escolinhas.");
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                                
                return;
            }

            if(!socio.isPessoaOK()){
                request.setAttribute("msg", "Situação do Título não permite Matrícula!");
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                                
                return;
            }

            request.setAttribute("socio", socio);            
            request.setAttribute("turma", t);
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.setAttribute("professores", ComboBoxLoader.listar("VW_PROFESSORES"));

            request.getRequestDispatcher("/pages/3061.jsp").forward(request, response);                            
        }else if("mostraFoto".equals(acao)){
            request.setAttribute("matricula", request.getParameter("matricula"));
            request.setAttribute("categoria", request.getParameter("categoria"));
            request.setAttribute("seqDependente", request.getParameter("seqDependente"));
            request.getRequestDispatcher("/pages/3061-atestado.jsp").forward(request, response);                
        }else if("atestado".equals(acao)){
            int matricula = 0;
            int categoria = 0;
            int dependente = 0;
            try{
                categoria = Integer.parseInt(request.getParameter("idCategoria"));
            }catch(NumberFormatException e){}
            try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
            }catch(NumberFormatException e){}
            try{
                dependente = Integer.parseInt(request.getParameter("seqDependente"));
            }catch(NumberFormatException e){}

            Socio socio = Socio.getInstance(matricula, dependente, categoria);
            request.setAttribute("socio", socio);            
            request.setAttribute("idTurma", request.getParameter("idTurma"));

            request.getRequestDispatcher("/pages/3061-cad-atestado.jsp").forward(request, response);                
        }else{        
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.setAttribute("professores", ComboBoxLoader.listar("VW_PROFESSORES"));
            request.setAttribute("idCurso", request.getParameter("idCurso"));
            request.setAttribute("idProfessor", request.getParameter("idProfessor"));
            request.setAttribute("mostrarSomenteTurmasAtivas", request.getParameter("mostrarSomenteTurmasAtivas"));
            request.getRequestDispatcher("/pages/3040-lista.jsp").forward(request, response);
        }

    }    

    @App("3070")
    public static void visualizar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
               
        request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
        request.setAttribute("professores", ComboBoxLoader.listar("VW_PROFESSORES"));
        int idTurma = Integer.parseInt(request.getParameter("idTurma"));
        Turma t = Turma.getInstance(idTurma);
        request.setAttribute("turma", t);
        request.setAttribute("origem", request.getParameter("origem"));
        request.setAttribute("nomeAluno", request.getParameter("nomeAluno"));
        request.setAttribute("situacao", request.getParameter("situacao"));
        request.getRequestDispatcher("/pages/3070.jsp").forward(request, response);
        
    }    
    
    @App("3071")
    public static void cancelar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        int matricula = Integer.parseInt(request.getParameter("matricula"));
        int idTurma = Integer.parseInt(request.getParameter("idTurma"));        
        String dataMatricula = request.getParameter("dataMatricula");            
        
        Turma t = Turma.getInstance(idTurma);
        Socio socio = Socio.getInstance(matricula, seqDependente, idCategoria);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3071", request.getRemoteAddr());
        try{
            t.cancelarMatricula(socio, audit);
        }catch(CancelarMatriculaException e){
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("msg", "Deseja imprimir o comprovante de cancelamento?");
        request.setAttribute("sim", "c?app=3041&acao=imprimir"
                + "&idTurma=" + idTurma
                + "&matricula=" + matricula
                + "&seqDependente=" + seqDependente
                + "&idCategoria=" + idCategoria            
                + "&dataMatricula=" + dataMatricula
                + "&tipoComprovante=C"
                );
        
        if (request.getParameter("origem").equals("CCS")){
            //consulta cursos do aluno (tela de pessoas)
            request.setAttribute("nao", "c?app=3080&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
        }else{
            request.setAttribute("nao", "c?app=3070&idTurma=" + idTurma);
        }
        request.getRequestDispatcher("/pages/confirmacao.jsp").forward(request, response);        
    }        

    @App("3072")
    public static void reativar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        int matricula = Integer.parseInt(request.getParameter("matricula"));
        int idTurma = Integer.parseInt(request.getParameter("idTurma"));        
        Turma t = Turma.getInstance(idTurma);
        Socio socio = Socio.getInstance(matricula, seqDependente, idCategoria);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3071", request.getRemoteAddr());
        if (!socio.getSituacao().equals("NO")) {
            request.setAttribute("msg", "A situação da Pessoa não permite reativação!");
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            return;                
        }else{
            try{
                t.reativarMatricula(socio, audit);
            }catch(ReativarMatriculaException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                return;                
            }
        }

        response.sendRedirect("c?app=3070&idTurma=" + idTurma);

    }        
    
    @App("3076")
    public static void alterarDesconto(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao=request.getParameter("acao");
        
        int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        int matricula = Integer.parseInt(request.getParameter("matricula"));
        int idTurma = Integer.parseInt(request.getParameter("idTurma"));        
        Turma t = Turma.getInstance(idTurma);
        Socio socio = Socio.getInstance(matricula, seqDependente, idCategoria);
            
        if("gravar".equals(acao)){
            float descontoPessoal = 0f;            
            try{
                descontoPessoal = Float.parseFloat(request.getParameter("descontoPessoal").toString().replaceAll(",", "."));
            }catch(NumberFormatException e){
                request.setAttribute("msg", "O valor do desconto pessoal tem mais de uma virgula");
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                return;
            }
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3071", request.getRemoteAddr());
            t.alterarDesconto(socio, descontoPessoal, audit);
            
            response.sendRedirect("c?app=3070&idTurma=" + idTurma);            
        }else{
            request.setAttribute("descontoPessoal", request.getParameter("descontoPessoal"));
            request.setAttribute("turma", t);
            request.setAttribute("socio", socio);
            request.getRequestDispatcher("/pages/3071.jsp").forward(request, response);        
        }
    }        

    @App("3041")
    public static void imprimirComprovante(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao=request.getParameter("acao");
        
        int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        int matricula = Integer.parseInt(request.getParameter("matricula"));
        int idTurma = Integer.parseInt(request.getParameter("idTurma"));        
        Turma t = Turma.getInstance(idTurma);
        Socio socio = Socio.getInstance(matricula, seqDependente, idCategoria);
        request.setAttribute("dataMatricula", request.getParameter("dataMatricula"));            
        
        if("imprimir".equals(acao)){
            request.setAttribute("tipoComprovante", request.getParameter("tipoComprovante"));
            request.setAttribute("turma", t);
            request.setAttribute("socio", socio);
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.setAttribute("professores", ComboBoxLoader.listar("VW_PROFESSORES"));            
            request.getRequestDispatcher("/pages/3041-impressao-comprovante.jsp").forward(request, response);        
        }else{
            request.setAttribute("turma", t);
            request.setAttribute("socio", socio);
            request.getRequestDispatcher("/pages/3072.jsp").forward(request, response);        
        }
    }            
    
    @App("3075")
    public static void imprimirPassaporte(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao=request.getParameter("acao");
        
        int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        int matricula = Integer.parseInt(request.getParameter("matricula"));
        int idTurma = Integer.parseInt(request.getParameter("idTurma"));        
        Turma t = Turma.getInstance(idTurma);
        Socio socio = Socio.getInstance(matricula, seqDependente, idCategoria);
        
        if("imprimir".equals(acao)){
            request.setAttribute("turma", t);
            request.setAttribute("socio", socio);
            request.setAttribute("modalidade", request.getParameter("modalidade"));
            request.setAttribute("diasAula", request.getParameter("diasAula"));
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.getRequestDispatcher("/pages/3062-impressao-passaporte.jsp").forward(request, response);        
        }else{
            request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
            request.setAttribute("turma", t);
            request.setAttribute("socio", socio);
            request.getRequestDispatcher("/pages/3062.jsp").forward(request, response);        
        }
    }                
}
