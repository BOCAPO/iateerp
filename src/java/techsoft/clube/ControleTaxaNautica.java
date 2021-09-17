package techsoft.clube;

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
public class ControleTaxaNautica{
    

    @App("2480")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
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
                
        request.setAttribute("taxas", TaxaNautica.consultar(tipo, dtRef));
        request.setAttribute("tipo", tipo);
        request.setAttribute("dtRef", dtRef);
        request.getRequestDispatcher("/pages/2480-lista.jsp").forward(request, response);
    }

    @App("2481")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("gravar".equals(acao)){
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
            
            String idTipoVaga = request.getParameter("idTipoVaga");
            
            TaxaNautica j = new TaxaNautica();
            j.setDtInicial(dtInicio);
            j.setDtFinal(dtFim);
            j.setValor(valor);
            j.setTipoVaga(idTipoVaga);
            
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2481", request.getRemoteAddr());
                j.inserir(audit);

                response.sendRedirect("c?app=2480");
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }

        }else{
            request.setAttribute("idCategoria", request.getParameter("idCategoria"));
            request.setAttribute("tiposVagas", TipoVagaBarco.listar());

            request.getRequestDispatcher("/pages/2481.jsp").forward(request, response);
        }
    }    
    
    @App("2482")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("gravar".equals(acao)){
            String idTipoVaga = request.getParameter("idTipoVaga");
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio"));
            Date dtFim = null;
            if (!request.getParameter("dtFim").equals("")){
                dtFim = Datas.parseDataHora(request.getParameter("dtFim"));
            }
            
            TaxaNautica j = new TaxaNautica();
            j.setTipoVaga(idTipoVaga);
            j.setDtInicial(dtInicio);
            j.setDtFinal(dtFim);
            
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2482", request.getRemoteAddr());
                j.alterar(audit);

                response.sendRedirect("c?app=2480");
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
            
        }else{
            String idTipoVaga = request.getParameter("idTipoVaga");
            request.setAttribute("tipoVaga", TipoVagaBarco.getInstance(idTipoVaga));

            request.setAttribute("dtInicio", request.getParameter("dtInicio"));
            request.setAttribute("dtFim", request.getParameter("dtFim"));
            request.setAttribute("valor", request.getParameter("valor"));

            request.getRequestDispatcher("/pages/2482.jsp").forward(request, response);
        }
            

    }    
}
