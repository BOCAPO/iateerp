package techsoft.controle.operacoes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.operacoes.RegistroVisitante;
import techsoft.operacoes.Visitante;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CrachaVisitante;
import techsoft.tabelas.InserirException;
import techsoft.tabelas.Setor;

@WebServlet(urlPatterns={"/AjaxRegistroVisitante/*"})
@Controller
public class AjaxRegistroVisitante extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request,HttpServletResponse response) 
            throws IOException, ServletException {
        
        int cracha = Integer.parseInt(request.getParameter("idCracha"));

        CrachaVisitante c = CrachaVisitante.getInstance(cracha);
        if (c == null) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Crachá inexistente");
        }
    
    }
    
    @App("1265")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("visualizarRelatorio".equals(acao)){
            StringBuilder sql = new StringBuilder();
            
            sql.append("SELECT ");
            sql.append("DT_ENTRADA as 'Data/Hora Entrada', ");
            sql.append("DT_SAIDA as 'Data/Hora Saída', ");
            sql.append("substring(NO_VISITANTE, 1, 35) as 'Visitante', ");
            sql.append("NR_DOCUMENTO as 'Documento', ");
            sql.append("USER_FUNC_ENTREGA as 'Entrega', ");
            sql.append("USER_FUNC_DEVOLUCAO as 'Devolucao' ");
            sql.append("From ");
            sql.append("TB_VISITANTE T1, ");
            sql.append("TB_REGISTRO_VISITANTE T2 ");
            sql.append("where ");
            sql.append("T1.CD_VISITANTE = T2.CD_VISITANTE AND ");
            sql.append("NR_CRACHA = ");
            sql.append(request.getParameter("cracha"));
            sql.append(" Order By ");
            sql.append("DT_ENTRADA Asc ");

            request.setAttribute("titulo", "Relatório de Visitantes");
            request.setAttribute("titulo2", request.getParameter("cracha"));
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else if("showFormSelecionarVisitante".equals(acao)){
            request.setAttribute("idCracha", request.getParameter("idCracha"));
            request.getRequestDispatcher("/pages/1262-selecionar-visitante.jsp").forward(request, response);
        }else if("consultarVisitante".equals(acao)){
            List<Visitante> visitantes = Visitante.consultar(request.getParameter("nome"), request.getParameter("documento"));
            request.setAttribute("idCracha", request.getParameter("idCracha"));
            request.setAttribute("visitantes", visitantes);
            
            request.getRequestDispatcher("/pages/1262-selecionar-visitante.jsp").forward(request, response);
        }else if("selecionarVisitante".equals(acao)){            
            int idCracha = Integer.parseInt(request.getParameter("idCracha"));
            int idVisitante = Integer.parseInt(request.getParameter("idVisitante"));
            CrachaVisitante cracha = CrachaVisitante.getInstance(idCracha);
            List<RegistroVisitante> registros = RegistroVisitante.listar(cracha);
            request.setAttribute("cracha", cracha);
            request.setAttribute("registros", registros);
            request.setAttribute("setores", Setor.listar());
            request.setAttribute("visitante", Visitante.getInstance(idVisitante));

            request.getRequestDispatcher("/pages/1262.jsp").forward(request, response);            
        }else if("listar".equals(acao)){
            int idCracha = Integer.parseInt(request.getParameter("idCracha"));
            CrachaVisitante cracha = CrachaVisitante.getInstance(idCracha);
            List<RegistroVisitante> registros = RegistroVisitante.listar(cracha);
            request.setAttribute("cracha", cracha);
            request.setAttribute("registros", registros);
            request.setAttribute("setores", Setor.listar());
            if(cracha.emprestado()){
                request.setAttribute("setorVisitado", registros.get(registros.size() - 1).getSetorVisitado());
                request.setAttribute("visitante", registros.get(registros.size() - 1).getVisitante());
            }
            request.getRequestDispatcher("/pages/1262.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("/pages/1261.jsp").forward(request, response);
        }
    }
    
    @App("1266")
    public static void entregarDevolverCracha(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("entregar".equals(acao)){
            int idCracha = Integer.parseInt(request.getParameter("idCracha"));
            CrachaVisitante cracha = CrachaVisitante.getInstance(idCracha);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1266", request.getRemoteAddr());
            Visitante visitante = null;
            Setor setor = null;            
            String arquivoFotoVisitante = request.getParameter("arquivoFotoVisitante");
            
            try{
                int idVisitante = Integer.parseInt(request.getParameter("idVisitante"));
                visitante = Visitante.getInstance(idVisitante);
            }catch(NumberFormatException e){
                visitante = new Visitante();
            }
            
            visitante.setNome(request.getParameter("nome"));
            visitante.setDocumento(request.getParameter("documento"));
            visitante.setPlaca(request.getParameter("placa"));
                
            try{
                int idSetor = Integer.parseInt(request.getParameter("idSetor"));
                setor = Setor.getInstance(idSetor);
            }catch(NumberFormatException e){
            }

            try{
                Visitante ret = cracha.registrarEntreaga(audit, visitante, setor);
                if(arquivoFotoVisitante != null && !arquivoFotoVisitante.trim().equals("")){
                    String path = request.getServletContext().getRealPath("/img/");
                    ret.atualizarFoto(new File(path + File.separator + arquivoFotoVisitante));
                }                
                response.sendRedirect("c?app=1265");
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }

        }else{
            int idCracha = Integer.parseInt(request.getParameter("idCracha"));
            CrachaVisitante cracha = CrachaVisitante.getInstance(idCracha);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1266", request.getRemoteAddr());

            if(cracha.emprestado()){
                cracha.registrarDevolucao(audit);
                response.sendRedirect("c?app=1265");
            }else{
                request.setAttribute("msg", "Esse crachá não foi emprestado portanto não pode ser devolvido");
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }

        }
    }    
    
}
