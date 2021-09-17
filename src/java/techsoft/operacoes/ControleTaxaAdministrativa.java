package techsoft.operacoes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import techsoft.seguranca.Usuario;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.InserirException;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleTaxaAdministrativa{
    

    @App("1220")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        int idCategoria = 0;
        try{
            idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        }catch(Exception e){
            idCategoria = 1;
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
                
        request.setAttribute("taxas", TaxaAdministrativa.consultar(idCategoria, tipo, dtRef));
        request.setAttribute("tipo", tipo);
        request.setAttribute("dtRef", dtRef);
        request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
        request.setAttribute("idCategoria", idCategoria);
        request.getRequestDispatcher("/pages/1220-lista.jsp").forward(request, response);
    }

    @App("1221")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("gravar".equals(acao)){
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio"));
            Date dtFim = null;
            if (!request.getParameter("dtFim").equals("")){
                dtFim = Datas.parseDataHora(request.getParameter("dtFim"));
            }
            
            float valor;
            try{
                valor = Float.parseFloat(request.getParameter("valor").replaceAll(",", "."));
            }catch(Exception e){
                valor = Float.parseFloat(request.getParameter("valor").replaceAll(",", "."));
            }
            
            TaxaAdministrativa j = new TaxaAdministrativa();
            j.setDtInicial(dtInicio);
            j.setDtFinal(dtFim);
            j.setCategoria(idCategoria);
            j.setValor(valor);
            
            
            try{
                for(String s : request.getParameterValues("idTaxaCobrar")){
                    Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1221", request.getRemoteAddr());
                    j.setTaxa(Integer.parseInt(s));
                    j.inserir(audit);
                }

                response.sendRedirect("c?app=1220&idCategoria="+idCategoria);
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
            
        }else{
            request.setAttribute("idCategoria", request.getParameter("idCategoria"));
            request.setAttribute("taxas", Taxa.listar());

            request.getRequestDispatcher("/pages/1221.jsp").forward(request, response);
        }
            

    }    
    
    @App("1222")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("gravar".equals(acao)){
            int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio"));
            Date dtFim = null;
            if (!request.getParameter("dtFim").equals("")){
                dtFim = Datas.parseDataHora(request.getParameter("dtFim"));
            }
            
            TaxaAdministrativa j = new TaxaAdministrativa();
            j.setTaxa(idTaxa);
            j.setDtInicial(dtInicio);
            j.setDtFinal(dtFim);
            j.setCategoria(idCategoria);
            
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1416", request.getRemoteAddr());
                j.alterar(audit);

                response.sendRedirect("c?app=1220&idCategoria="+idCategoria);
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
            
        }else{
            int idTaxa = Integer.parseInt(request.getParameter("idTaxa"));
            request.setAttribute("taxa", Taxa.getInstance(idTaxa));
            request.setAttribute("idCategoria", request.getParameter("idCategoria"));

            request.setAttribute("dtInicio", request.getParameter("dtInicio"));
            request.setAttribute("dtFim", request.getParameter("dtFim"));
            request.setAttribute("valor", request.getParameter("valor"));

            request.getRequestDispatcher("/pages/1222.jsp").forward(request, response);
        }
            

    }    
}
