package techsoft.clube;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.SocioCarne;
import techsoft.cadastro.Titular;
import techsoft.clube.LocalAcademia;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.operacoes.Taxa;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.Funcionario;
import techsoft.tabelas.Setor;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;


@Controller
public class ControleBoletoAvulso{

    @App("2430")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("imprimir".equals(acao)){
            BoletoAvulso b = BoletoAvulso.getInstance(Integer.parseInt(request.getParameter("id")));
            BoletoImpresso s = BoletoAvulso.dadosBoleto(Integer.parseInt(request.getParameter("id")), b.getDtVencimento());
            
            request.setAttribute("s", s);
            request.setAttribute("codBar", SocioCarne.i25Encode(s.getCodBarras()));
            
            request.getRequestDispatcher("/pages/2430-boleto.jsp").forward(request, response);                        
        }else{
            int taxa = 0;
            try{
                taxa = Integer.parseInt(request.getParameter("taxa"));
            }catch(Exception e){}   
                

            String sacado = request.getParameter("sacado");
            if (sacado == null){
                sacado = "";
            }else{
                sacado = sacado.trim();
            }   
            
            String situacao = request.getParameter("situacao");
            if (situacao == null){
                situacao = "";
            }else{
                situacao = situacao.trim();
            }   

            request.setAttribute("lista", BoletoAvulso.listar(taxa, sacado, situacao, 0, 0, 0));
            request.setAttribute("taxaSel", taxa);
            request.setAttribute("sacado", sacado);
            request.setAttribute("situacao", situacao);
            request.setAttribute("taxas", Taxa.listar());

            request.getRequestDispatcher("/pages/2430-lista.jsp").forward(request, response);
            
        }
    }
    
    @App("2431")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "2431");
            request.setAttribute("taxas", Taxa.listar());

            request.getRequestDispatcher("/pages/2430.jsp").forward(request, response);            
        }else{
            BoletoAvulso o = new BoletoAvulso();
            
            o.setTaxa(Taxa.getInstance(Integer.parseInt(request.getParameter("taxa"))));
            o.setSacado(request.getParameter("sacado"));
            o.setValor(new BigDecimal(request.getParameter("valor").replace(".", "").replace(",", ".")));
            o.setDtVencimento(Datas.parse(request.getParameter("dtVencimento")));
            
            try{
                o.setMatricula(Integer.parseInt(request.getParameter("matricula")));    
                o.setCategoria(Integer.parseInt(request.getParameter("categoria")));    
                o.setDependente(Integer.parseInt(request.getParameter("dependente")));    
            }catch(Exception e){
                o.setMatricula(0);    
                o.setCategoria(0);    
                o.setDependente(0);    
            }

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2431", request.getRemoteAddr());
            try{
                o.inserir(audit);
                response.sendRedirect("c?app=2430");

            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }
    }
    @App("2433")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        BoletoAvulso b = BoletoAvulso.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2433", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2430");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        
    
}
