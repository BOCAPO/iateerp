package techsoft.controle.tabelas;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;

@Controller
public class ManutencaoCategoriaNautica {

    @App({"1130", "1131", "1132", "1133"})
    public static void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String app = request.getParameter("app");
        String acao = request.getParameter("acao");
        if(acao == null){
            acao = "";
        }
            
        //LISTA DE CategoriaNauticas	
        if("1130".equals(app)){
            request.setAttribute("CategoriaNauticas", CategoriaNautica.listar());
            request.getRequestDispatcher("/pages/1130-lista.jsp").forward(request, response);
        }
        //INSERIR CategoriaNautica
        else if("1131".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1130.jsp").forward(request, response);
            }else{
                CategoriaNautica d = new CategoriaNautica();
            	d.setDescricao(request.getParameter("descricao"));
                d.setId(request.getParameter("id"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1130");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
        }

        //ALTERAR CategoriaNautica
        else if("1132".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                String idCategoriaNautica = request.getParameter("idCategoriaNautica");
                CategoriaNautica d = CategoriaNautica.getInstance(idCategoriaNautica);
                request.setAttribute("categorianautica", d);
                request.getRequestDispatcher("/pages/1130.jsp").forward(request, response);
            }else{
                String idCategoriaNautica = request.getParameter("idCategoriaNautica");
                CategoriaNautica d = CategoriaNautica.getInstance(idCategoriaNautica);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=1130&acao=detalhar&idCategoriaNautica=" + idCategoriaNautica);
            }
        }

        //EXCLUIR CategoriaNautica
        else if("1133".equals(app)){
                String idCategoriaNautica = request.getParameter("idCategoriaNautica");
            CategoriaNautica d = CategoriaNautica.getInstance(idCategoriaNautica);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1130");
        }
    }


}
