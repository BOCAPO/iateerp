package techsoft.clube;

import java.io.IOException;
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
import techsoft.cadastro.Titular;
import techsoft.clube.LocalAcademia;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.Funcionario;
import techsoft.tabelas.Setor;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleAchadosPerdidos{

    @App("2420")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("imprimir".equals(acao)){
            int id = Integer.parseInt(request.getParameter("idObjeto"));
            AchadosPerdidos d = AchadosPerdidos.getInstance(id);
            request.setAttribute("objeto", d);
            
            request.getRequestDispatcher("/pages/2420-imprimir-devolucao.jsp").forward(request, response);
        }else{
            Integer setorEncontrado = null;
            if (request.getParameter("setorEncontrado") != null && !request.getParameter("setorEncontrado").equals("")){
                setorEncontrado = Integer.parseInt(request.getParameter("setorEncontrado"));
            }
            
            Integer definicao = null;
            if (request.getParameter("definicao") != null && !request.getParameter("definicao").equals("")){
                definicao = Integer.parseInt(request.getParameter("definicao"));
            }

            String sit = request.getParameter("situacao");
            int situacao = 0;
            if (sit != null)
            {
                situacao = Integer.parseInt(sit);
            }

            String descricao = request.getParameter("descricao");
            if (descricao == null){
                descricao = "";
            }else{
                descricao = descricao.trim();
            }   

            request.setAttribute("lista", AchadosPerdidos.listar(setorEncontrado, definicao, situacao, descricao, Datas.parse(request.getParameter("dtEntradaInicial")), Datas.parse(request.getParameter("dtEntradaFinal"))));
            request.setAttribute("setorEncontrado", setorEncontrado);
            request.setAttribute("definicao", definicao);
            request.setAttribute("situacao", situacao);
            request.setAttribute("descricao", descricao);
            request.setAttribute("dtEntradaInicial", request.getParameter("dtEntradaInicial"));
            request.setAttribute("dtEntradaFinal", request.getParameter("dtEntradaFinal"));
            request.setAttribute("setores", ComboBoxLoader.listarSql("select * from tb_setor where descr_setor not like '%DESCONHECIDO%' ORDER BY DESCR_SETOR" ));

            request.getRequestDispatcher("/pages/2420-lista.jsp").forward(request, response);
            
        }
        

    }
    
    @App("2421")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "2421");
            request.setAttribute("setores", ComboBoxLoader.listarSql("select * from tb_setor where descr_setor not like '%DESCONHECIDO%' ORDER BY DESCR_SETOR" ));
            request.setAttribute("funcionarios", ComboBoxLoader.listar("VW_PROFESSORES"));
            request.getRequestDispatcher("/pages/2420.jsp").forward(request, response);            
        }else{
            AchadosPerdidos o = new AchadosPerdidos();
            ControleAchadosPerdidos.preencherObjeto(o, request);

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2421", request.getRemoteAddr());
            try{
                o.inserir(audit);
                response.sendRedirect("c?app=2420");

            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }
    }
    @App("2422")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "2422");
            int id = Integer.parseInt(request.getParameter("id"));
            AchadosPerdidos d = AchadosPerdidos.getInstance(id);
            request.setAttribute("objeto", d);
            request.setAttribute("setores", ComboBoxLoader.listarSql("select * from tb_setor where descr_setor not like '%DESCONHECIDO%' ORDER BY DESCR_SETOR" ));
            request.setAttribute("funcionarios", ComboBoxLoader.listar("VW_PROFESSORES"));
            request.getRequestDispatcher("/pages/2420.jsp").forward(request, response);
        }else{
            String imprimeTermo = request.getParameter("imprimeTermo");
            int id = Integer.parseInt(request.getParameter("id"));
            AchadosPerdidos o = AchadosPerdidos.getInstance(id);
            ControleAchadosPerdidos.preencherObjeto(o, request);

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2422", request.getRemoteAddr());
            try{
                o.alterar(audit);
                if(imprimeTermo.equals("S")){
                    response.sendRedirect("c?app=2420&acao=imprimir&idObjeto="+id);
                }else{
                    response.sendRedirect("c?app=2420");
                }
                
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }

    }
    
    @App("2423")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        AchadosPerdidos b = AchadosPerdidos.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2423", request.getRemoteAddr());
        try{
            b.excluir(audit);
            response.sendRedirect("c?app=2420");
        }catch(Exception e){
            request.setAttribute("err", e);
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
        }
    }        

    @App("2425")
    public static void fotoObjeto(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        String acao = request.getParameter("acao");
        //OBS: Inserir foto esta na conrole Upload.java
        
        if("showFoto".equals(acao)){
            int idObjeto = Integer.parseInt(request.getParameter("idObjeto"));
            AchadosPerdidos b = AchadosPerdidos.getInstance(idObjeto);
            request.setAttribute("objeto", b);
            request.getRequestDispatcher("/pages/2420-foto.jsp").forward(request, response);                    
        }else if("excluirFoto".equals(acao)){
            int idObjeto = Integer.parseInt(request.getParameter("idObjeto"));
            int idFoto = Integer.parseInt(request.getParameter("idFoto"));
            AchadosPerdidos b = AchadosPerdidos.getInstance(idObjeto);
            b.excluirFoto(idFoto);
            //reload na pagina
            response.sendRedirect("c?app=2425&acao=showFoto&idObjeto=" + idObjeto);            
        }else if("showFotoDetalhe".equals(acao)){
            request.setAttribute("idFoto", request.getParameter("idFoto"));
            request.setAttribute("idObjeto", request.getParameter("idObjeto"));
            request.getRequestDispatcher("/pages/2420-foto-detalhe.jsp").forward(request, response);                    
        }
    }        

    
    private static void preencherObjeto(AchadosPerdidos o, HttpServletRequest request){
        
        int setorOrigem = 0;
        int setorEncontrado = 0;
        int funcCatalogador = 0;
        int funcDevolucao = 0;
        
        o.setDescricao(request.getParameter("descricao"));
        
        setorOrigem = Integer.parseInt(request.getParameter("setorOrigem"));   
        if (setorOrigem==0){
            o.setSetorOrigem(null);
        }else{
            o.setSetorOrigem(Setor.getInstance(setorOrigem));            
        }
        
        try{
            o.setQtObjeto(Integer.parseInt(request.getParameter("qtObjeto")));
        }catch(Exception e){
            o.setQtObjeto(null);
        }
        
        try{
            o.setUnidade(Integer.parseInt(request.getParameter("unidade")));
        }catch(Exception e){
            o.setUnidade(null);
        }
        
        o.setCondicao(request.getParameter("condicao"));
        
        try{
            o.setDefinicao(Integer.parseInt(request.getParameter("definicao")));
        }catch(Exception e){
            o.setDefinicao(null);
        }
        
        setorEncontrado = Integer.parseInt(request.getParameter("setorEncontrado"));   
        if (setorEncontrado==0){
            o.setSetorEncontrado(null);
        }else{
            o.setSetorEncontrado(Setor.getInstance(setorEncontrado));            
        }
        
        String modelo = request.getParameter("modelo");
        o.setModelo(Integer.parseInt(request.getParameter("modelo")));
        o.setSexo(Integer.parseInt(request.getParameter("sexo")));
        
        o.setMarca(request.getParameter("marca"));
        o.setCor(request.getParameter("cor"));
        try{
            o.setTamanho(Integer.parseInt(request.getParameter("tamanho")));
        }catch(Exception e){
            o.setTamanho(null);
        }
        o.setDtEntrada(Datas.parse(request.getParameter("dtEntrada")));
        o.setDtCatalogacao(Datas.parse(request.getParameter("dtCatalogacao")));
        
        funcCatalogador = Integer.parseInt(request.getParameter("funcCatalogador"));   
        if (funcCatalogador==0){
            o.setFuncCatalogador(null);
        }else{
            o.setFuncCatalogador(Funcionario.getInstance(funcCatalogador));
        }
        
        o.setNomeEntrega(request.getParameter("nomeEntrega"));
        
        try{
            o.setNrPrateleira(Integer.parseInt(request.getParameter("nrPrateleira")));
        }catch(Exception e){
            o.setNrPrateleira(null);
        }
        
        o.setPerfilRetirante(Integer.parseInt(request.getParameter("perfilRetirante")));
        
        o.setNomeRetirante(request.getParameter("nomeRetirante"));
        o.setDocRetirante(request.getParameter("docRetirante"));
        o.setTelRetirante(request.getParameter("telRetirante"));
        
        funcDevolucao = Integer.parseInt(request.getParameter("funcDevolucao"));   
        if (funcDevolucao==0){
            o.setFuncDevolucao(null);
        }else{
            o.setFuncDevolucao(Funcionario.getInstance(funcDevolucao));
        }
        
        o.setDtDevolucao(Datas.parse(request.getParameter("dtDevolucao")));
        o.setSituacao(Integer.parseInt(request.getParameter("situacao")));  
        
    }
    
}
