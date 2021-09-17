package techsoft.controle.cadastro;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.BarcoIate;
import techsoft.cadastro.Titular;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleBarcoIate{
    
    @App("1670")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        //OBS: Inserir foto esta na conrole Upload.java
        if("detalhes".equals(acao)){
            int idBarco = Integer.parseInt(request.getParameter("idBarco"));
            BarcoIate b = BarcoIate.getInstance(idBarco);
            request.setAttribute("barco", b);
            request.setAttribute("app", 1670);
            request.getRequestDispatcher("/pages/1670.jsp").forward(request, response);            
        }else if("imprimir".equals(acao)){
            
            String sql = "EXEC SP_BARCO_IATE 'J'";

            request.setAttribute("titulo", "Relacao Embarcacoes do Iate");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);   
            
        }else{

            request.setAttribute("barcos", BarcoIate.listar());
            request.getRequestDispatcher("/pages/1670-lista.jsp").forward(request, response);            
        }
    }

    //INCLUSAO DE BARCOS DO IATE
    @App("1671")
    public static void inserir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        
        String acao = request.getParameter("acao");
        if("gravar".equals(acao)){
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2001", request.getRemoteAddr());
            BarcoIate b = new BarcoIate();
            preencherDadosBarco(b, request, response);

            b.inserir(audit);

            response.sendRedirect("c?app=1670&acao=showForm");                                           
        }else{
            request.setAttribute("app", "1671");
            request.setAttribute("categorias", CategoriaNautica.listar());
            request.setAttribute("tipos", TipoVagaBarco.listar());
            request.getRequestDispatcher("/pages/1670.jsp").forward(request, response);            
        }
            
    }        
    
    //ALTERAÇÃO DE BARCOS DO IATE
    @App("1672")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {            
        
        String acao = request.getParameter("acao");
        int idBarco = Integer.parseInt(request.getParameter("idBarco"));
        BarcoIate b = BarcoIate.getInstance(idBarco);
        request.setAttribute("barco", b);

        if("gravar".equals(acao)){
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2002", request.getRemoteAddr());
            preencherDadosBarco(b, request, response);

            b.alterar(audit);

            response.sendRedirect("c?app=1670&acao=showForm");                           
        }else{
            request.setAttribute("app", "1672");
            request.setAttribute("categorias", CategoriaNautica.listar());
            request.setAttribute("tipos", TipoVagaBarco.listar());
            request.getRequestDispatcher("/pages/1670.jsp").forward(request, response);                
        }
            
    }        
        
    //EXCLUSAO DE BARCOS DO IATE
    @App("1673")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int idBarco = Integer.parseInt(request.getParameter("idBarco"));
        BarcoIate b = BarcoIate.getInstance(idBarco);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1673", request.getRemoteAddr());
        b.excluir(audit);

        response.sendRedirect("c?app=1670&acao=showForm");           
    }        
    
    private static void preencherDadosBarco(BarcoIate b, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        b.setNome(request.getParameter("nome"));
        CategoriaNautica c = CategoriaNautica.getInstance(request.getParameter("idCategoriaNautica"));
        b.setCategoriaNautica(c);
        TipoVagaBarco v = TipoVagaBarco.getInstance(request.getParameter("idTipoVaga"));
        b.setTipoVaga(v);
        b.setNumCapitania(request.getParameter("numCapitania"));
        try{
            b.setBox(Integer.parseInt(request.getParameter("box")));
        }catch(NumberFormatException e){

        }
        try{
            b.setPes(Integer.parseInt(request.getParameter("pes")));
        }catch(NumberFormatException e){

        }                    
        b.setDataRegistro(Datas.parse(request.getParameter("dataRegistro")));
        try{
            b.setAnoFabricacao(Integer.parseInt(request.getParameter("anoFabricacao")));
        }catch(NumberFormatException e){

        }                    
        b.setHabilitacao(request.getParameter("habilitacao"));
        try{
            b.setTripulantes(Integer.parseInt(request.getParameter("tripulantes")));
        }catch(NumberFormatException e){

        }                    
        b.setDataCadastro(Datas.parse(request.getParameter("dataCadastro")));

        if(request.getParameter("documentacaoCompleta") != null && request.getParameter("documentacaoCompleta").equals("on")){
            b.setDocumentacaoCompleta(true);
        }else{
            b.setDocumentacaoCompleta(false);
        }
        try{
            b.setComprimentoTotal(Integer.parseInt(request.getParameter("comprimentoTotal")));
        }catch(NumberFormatException e){

        }
        try{
            b.setComprimentoBoca(Integer.parseInt(request.getParameter("comprimentoBoca")));
        }catch(NumberFormatException e){

        }
        try{
            b.setComprimentoPontal(Integer.parseInt(request.getParameter("comprimentoPontal")));
        }catch(NumberFormatException e){

        }                    
        b.setDataVencimentoSeguro(Datas.parse(request.getParameter("dataVencimentoSeguro")));
        b.setDataVencimentoRegistro(Datas.parse(request.getParameter("dataVencimentoRegistro")));
        b.setDataVencimentoHabilitacao(Datas.parse(request.getParameter("dataVencimentoHabilitacao")));
    }
    
}
