package techsoft.controle.curso;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.curso.Turma;
import techsoft.curso.TurmaHorario;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;

@Controller
public class ControleTurma {

    @App("3030")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        request.setAttribute("cursos", ComboBoxLoader.listar("VW_CURSOS"));
        request.setAttribute("professores", ComboBoxLoader.listar("VW_PROFESSORES"));
        request.setAttribute("idCurso", request.getParameter("idCurso"));
        request.setAttribute("idProfessor", request.getParameter("idProfessor"));
        request.setAttribute("mostrarSomenteTurmasAtivas", request.getParameter("mostrarSomenteTurmasAtivas"));
        request.getRequestDispatcher("/pages/3030-lista.jsp").forward(request, response);
    }    

    @App("3032")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");
        
        if("gravar".equals(acao)){
            int idTurma = Integer.parseInt(request.getParameter("idTurma"));
            Turma t = Turma.getInstance(idTurma);

            ControleTurma.preencherTurma(t, request);
            int teste = t.getId();
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3032", request.getRemoteAddr());
            t.alterar(audit);            

            response.sendRedirect("c?app=3030&mostrarSomenteTurmasAtivas=true");
        }else{
            int idTurma = Integer.parseInt(request.getParameter("idTurma"));
            request.setAttribute("turma", Turma.getInstance(idTurma));
            request.setAttribute("cursos", ComboBoxLoader.listarSql("SELECT CD_CURSO, DESCR_CURSO FROM TB_Curso WHERE IC_SITUACAO = 'N' ORDER BY 2"));
            request.setAttribute("professores", ComboBoxLoader.listar("VW_PROFESSORES"));

            request.setAttribute("app", "3032");
            request.getRequestDispatcher("/pages/3030.jsp").forward(request, response);            
        }
    }    
    
    @App("3031")
    public static void inserir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");
        
        if("gravar".equals(acao)){
            Turma t = new Turma();
            ControleTurma.preencherTurma(t, request);
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3031", request.getRemoteAddr());
            t.inserir(audit);

            response.sendRedirect("c?app=3030&mostrarSomenteTurmasAtivas=true");
        }else{
            //request.getSession().setAttribute("turma", new Turma());
            request.setAttribute("cursos", ComboBoxLoader.listarSql("SELECT CD_CURSO, DESCR_CURSO FROM TB_Curso WHERE IC_SITUACAO = 'N' ORDER BY 2"));
            request.setAttribute("professores", ComboBoxLoader.listar("VW_PROFESSORES"));

            request.setAttribute("app", "3031");
            request.getRequestDispatcher("/pages/3030.jsp").forward(request, response);            
        }
    }    
    
    @App("3033")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        int idTurma = Integer.parseInt(request.getParameter("idTurma"));
        Turma t = Turma.getInstance(idTurma);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3033", request.getRemoteAddr());
        t.excluir(audit);

        response.sendRedirect("c?app=3030");
    }        
    
    private static void preencherTurma(Turma t, HttpServletRequest request){
            int idCurso = Integer.parseInt(request.getParameter("idCurso"));
            int idProfessor = Integer.parseInt(request.getParameter("idProfessor"));
            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            int qtVagas = Integer.parseInt(request.getParameter("qtVagas"));
            t.setIdCurso(idCurso);
            t.setIdProfessor(idProfessor);
            t.setDataInicio(d1);
            t.setDataFim(d2);
            t.setQtVagas(qtVagas);
            t.setDeTurma(request.getParameter("deTurma"));
            
            for(int i =0; i < 7 ; i++){
                t.getHorarios()[i].clear();
            }    
            String[] vetDias = request.getParameter("horarios").split(";");
            String[] vetDet;
            int diaInt=0;
            for(int i =0; i < vetDias.length ; i++){
                vetDet = vetDias[i].split("/");
                diaInt=ControleTurma.diaSemana(vetDet[0]);
                TurmaHorario h = new TurmaHorario(vetDet[1], vetDet[2]);
                t.getHorarios()[diaInt].add(h);
            }
            
    }
    private static int diaSemana(String dia){
        int diaInt=0;
        if (dia.equals("Segunda-feira")){
            diaInt=0;
        }else if (dia.equals("Terça-feira")){
            diaInt=1;
        }else if (dia.equals("Quarta-feira")){
            diaInt=2;
        }else if (dia.equals("Quinta-feira")){
            diaInt=3;
        }else if (dia.equals("Sexta-feira")){
            diaInt=4;
        }else if (dia.equals("Sábado")){
            diaInt=5;
        }else if (dia.equals("Domingo")){
            diaInt=6;
        }
        return diaInt;
        
    }
            
}
