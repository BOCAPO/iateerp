package techsoft.clube;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import techsoft.tabelas.Cor;
import techsoft.tabelas.Funcionario;
import techsoft.tabelas.ModeloCarro;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleCarroFuncionario{
    

    @App("3240")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
            int id = Integer.parseInt(request.getParameter("id"));
            Funcionario f = Funcionario.getInstance(id);
            request.setAttribute("funcionario", f);
            request.setAttribute("carros", CarroFuncionario.listar(f));

            request.getRequestDispatcher("/pages/3240-lista.jsp").forward(request, response);
    }
    
    @App("3241")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                int id = Integer.parseInt(request.getParameter("id"));
                Funcionario f = Funcionario.getInstance(id);
                CarroFuncionario c = new CarroFuncionario();
                c.setFuncionario(f);
                request.setAttribute("carro", c);
                request.setAttribute("app", 3241); 
                request.setAttribute("modelos", ComboBoxLoader.listar("VW_MODELO_MARCA"));
                request.setAttribute("cores", ComboBoxLoader.listar("TB_COR"));
                request.getRequestDispatcher("/pages/3240.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                Funcionario f = Funcionario.getInstance(id);
                CarroFuncionario c = new CarroFuncionario();
                Cor cor = Cor.getInstance(Integer.parseInt(request.getParameter("idCor")));
            	c.setCor(cor);
                c.setPlaca(request.getParameter("placa"));
                ModeloCarro m = ModeloCarro.getInstance(Integer.parseInt(request.getParameter("idModelo")));
                c.setModelo(m);
                Funcionario s = Funcionario.getInstance(id);
                c.setFuncionario(s);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3241", request.getRemoteAddr());
                try{
                    c.inserir(audit);
                    response.sendRedirect("c?app=3240&id=" + id);
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("3242")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            int idCarro = Integer.parseInt(request.getParameter("idCarro"));
            CarroFuncionario c = CarroFuncionario.getInstance(idCarro);
            
            if("showForm".equals(acao)){
                request.setAttribute("app", 3242);
                request.setAttribute("modelos", ComboBoxLoader.listar("VW_MODELO_MARCA"));
                request.setAttribute("cores", ComboBoxLoader.listar("TB_COR"));
                
                request.setAttribute("carro", c);
                request.getRequestDispatcher("/pages/3240.jsp").forward(request, response);
            }else{
                Cor cor = Cor.getInstance(Integer.parseInt(request.getParameter("idCor")));
            	c.setCor(cor);
                c.setPlaca(request.getParameter("placa"));
                ModeloCarro m = ModeloCarro.getInstance(Integer.parseInt(request.getParameter("idModelo")));                
                c.setModelo(m);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3242", request.getRemoteAddr());

                c.alterar(audit);

            	response.sendRedirect("c?app=3240&acao=detalhar&idCarro=" + idCarro + "&id=" + c.getFuncionario().getId());
            }
            
        }
    
    @App("3243")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int idCarro = Integer.parseInt(request.getParameter("idCarro"));
        CarroFuncionario c = CarroFuncionario.getInstance(idCarro);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3243", request.getRemoteAddr());
        c.excluir(audit);

        response.sendRedirect("c?app=3240&id=" + c.getFuncionario().getId());
        
    }        
    
    @App("3244")
    public static void Documento(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        String acao = request.getParameter("acao");

        int idCarro = Integer.parseInt(request.getParameter("idCarro"));
        CarroFuncionario c = CarroFuncionario.getInstance(idCarro);

        if("showForm".equals(acao)){
            request.setAttribute("carro", c);

            request.getRequestDispatcher("/pages/3244.jsp").forward(request, response);                
        }else if("excluirFoto".equals(acao)){

            c.excluirFoto();

            //reload na pagina
            request.setAttribute("carro", c);
            response.sendRedirect("c?app=3244&acao=showForm&idCarro=" + c.getId());
        }else if("mostraFoto".equals(acao)){

            request.setAttribute("carro", c);
            request.getRequestDispatcher("/pages/3244-foto.jsp").forward(request, response);                
        }
        
    }        
}
