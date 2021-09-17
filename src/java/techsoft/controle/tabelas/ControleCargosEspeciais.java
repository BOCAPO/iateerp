package techsoft.controle.tabelas;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CargoEspecial;
import techsoft.tabelas.CategoriaNautica;

@Controller
public class ControleCargosEspeciais {

    @App("1190")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        request.setAttribute("cargosEspeciais", CargoEspecial.listar());
        request.getRequestDispatcher("/pages/1190-lista.jsp").forward(request, response);

    }
    
    @App("1191")
    public static void inserir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    

        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "1191");
            request.getRequestDispatcher("/pages/1190.jsp").forward(request, response);
        }else{
            CargoEspecial e = new CargoEspecial();
            ControleCargosEspeciais.preencherCargoEspecial(e, request);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1191", request.getRemoteAddr());
            try{
                e.inserir(audit);
                response.sendRedirect("c?app=1190");
            }catch(Exception ex){
                request.setAttribute("err", ex);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }

            
        }
    }
    
    @App("1192")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    

        String acao = request.getParameter("acao");
        int idCargoEspecial = Integer.parseInt(request.getParameter("idCargoEspecial"));
        CargoEspecial e = CargoEspecial.getInstance(idCargoEspecial);
        
        if("showForm".equals(acao)){    
            request.setAttribute("cargoEspecial", e);
            request.setAttribute("app", "1192");
            request.getRequestDispatcher("/pages/1190.jsp").forward(request, response);
        }else{
            ControleCargosEspeciais.preencherCargoEspecial(e, request);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1192", request.getRemoteAddr());

            e.alterar(audit);

            response.sendRedirect("c?app=1190");
        }
        
    }
    
    @App("1193")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
    
        int idCargoEspecial = Integer.parseInt(request.getParameter("idCargoEspecial"));
        CargoEspecial e = CargoEspecial.getInstance(idCargoEspecial);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1193", request.getRemoteAddr());
        e.excluir(audit);

        response.sendRedirect("c?app=1190");
    
    }

    private static void preencherCargoEspecial(CargoEspecial e, HttpServletRequest request){
        e.setDescricao(request.getParameter("descricao"));
        e.setQtMax(Integer.parseInt(request.getParameter("qtMax")));
        e.setIdTipoCargo(request.getParameter("idTipoCargo"));
        e.setCategoria(request.getParameter("categoria"));

        if(Boolean.parseBoolean(request.getParameter("imprimirDescricao"))){
            e.setCorFundo(request.getParameter("corFundo"));
            e.setGestao(request.getParameter("gestao"));        
            e.setCorFonte(request.getParameter("corFonte"));            
        }else{
            e.setCorFundo(null);
            e.setGestao(null);        
            e.setCorFonte(null);                        
        }
    }
}
