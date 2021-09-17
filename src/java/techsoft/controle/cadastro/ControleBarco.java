package techsoft.controle.cadastro;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.Titular;
import techsoft.clube.LocalBoxNautica;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleBarco{
    

    //LISTA DE BARCOS DO SOCIO
    //LISTA DE MOVIMENTO DE BACO DO SOCIO
    //IMPRESSAO DO REGISTRO DO BARCO
    //IMPRESSAO DA BAIXA DO REGISTRO DO BARCO
    //LISTA DE FOTOS DO BARCO
    //EXCLUIR FOTO DO BARCO
    //CONSULTA BARCO
    @App("2000")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        //OBS: Inserir foto esta na conrole Upload.java
        if("excluirFoto".equals(acao)){
            int idBarco = Integer.parseInt(request.getParameter("idBarco"));
            int idFoto = Integer.parseInt(request.getParameter("idFoto"));
            SocioBarco b = SocioBarco.getInstance(idBarco);
            b.excluirFoto(idFoto);
            //reload na pagina
            response.sendRedirect("c?app=2000&acao=showFoto&idBarco=" + idBarco);            
        }else if("showFoto".equals(acao)){
            int idBarco = Integer.parseInt(request.getParameter("idBarco"));
            SocioBarco b = SocioBarco.getInstance(idBarco);
            request.setAttribute("barco", b);
            request.getRequestDispatcher("/pages/3030-barco-foto.jsp").forward(request, response);                    
        }else if("showFotoDetalhe".equals(acao)){
            request.setAttribute("idFoto", request.getParameter("idFoto"));
            request.setAttribute("idBarco", request.getParameter("idBarco"));
            request.getRequestDispatcher("/pages/3030-  detalhe-foto.jsp").forward(request, response);                    
        }else if("imprimirBaixa".equals(acao)){
            int idBarco = Integer.parseInt(request.getParameter("idBarco"));
            SocioBarco b = SocioBarco.getInstance(idBarco);
            request.setAttribute("barco", b);
            request.setAttribute("socio", b.getSocio());
            request.setAttribute("titular", Titular.getInstance(b.getSocio().getMatricula(), b.getSocio().getIdCategoria()));
            request.setAttribute("tipos", TipoVagaBarco.listar());
            request.getRequestDispatcher("/pages/3030-barco-impressao-baixa.jsp").forward(request, response);                    
        }else if("imprimirRegistro".equals(acao)){
            int idBarco = Integer.parseInt(request.getParameter("idBarco"));
            SocioBarco b = SocioBarco.getInstance(idBarco);
            request.setAttribute("barco", b);
            request.setAttribute("socio", b.getSocio());
            request.setAttribute("titular", Titular.getInstance(b.getSocio().getMatricula(), b.getSocio().getIdCategoria()));
            request.setAttribute("tipos", TipoVagaBarco.listar());
            request.getRequestDispatcher("/pages/3030-barco-impressao-registro.jsp").forward(request, response);            
        }else if("showListaMovimento".equals(acao)){
            int idBarco = Integer.parseInt(request.getParameter("idBarco"));
            SocioBarco b = SocioBarco.getInstance(idBarco);
            request.setAttribute("barco", b);
            request.setAttribute("movimentacao", MovimentoBarco.listar(b));
            request.getRequestDispatcher("/pages/3032-barco-movimento-lista.jsp").forward(request, response);            
        }else if("detalhes".equals(acao)){
            int idBarco = Integer.parseInt(request.getParameter("idBarco"));
            SocioBarco b = SocioBarco.getInstance(idBarco);
            request.setAttribute("barco", b);
            request.setAttribute("app", 2000);
            request.getRequestDispatcher("/pages/3030-barco.jsp").forward(request, response);            
        }else if("showFormImprimir".equals(acao)){
            int matricula = 0;
            int categoria = 0;
            
            try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
            }catch(NumberFormatException e){
                matricula = 0;
            }
            try{
                categoria = Integer.parseInt(request.getParameter("categoria"));
            }catch(NumberFormatException e){
                categoria = 0;
            }
            String nome = request.getParameter("nome");
            String capitania = request.getParameter("capitania");
            String nomeBarco = request.getParameter("nomeBarco");
            String catNautica = request.getParameter("catNautica");
            String doc = request.getParameter("doc");
            
            
            String sqlSimpl = "SP_CONS_BARCO 'R', ";
            String sqlComp = "SP_CONS_BARCO 'J', ";
            String sql = "";
            
            if (matricula==0){
                sql = sql + "null, ";
            }else{    
                sql = sql + matricula + ", ";
            }
            if (categoria==0){
                sql = sql + "null, ";
            }else{    
                sql = sql + categoria + ", ";
            }
            if ("TODAS".equals(catNautica)){
                sql = sql + "null, ";
            }else{    
                sql = sql + "'" + catNautica + "', ";
            }
            if ("".equals(nomeBarco)){
                sql = sql + "null, ";
            }else{    
                sql = sql + "'" + nomeBarco + "', ";
            }
            if ("".equals(nome)){
                sql = sql + "null, ";
            }else{    
                sql = sql + "'" + nome + "', ";
            }
            if ("T".equals(doc)){
                sql = sql + "null, ";
            }else{    
                sql = sql + "'" + doc + "', ";
            }
            
            sql = sql + "null, null, null, null, null, null, null, null,";
            
            if ("".equals(capitania)){
                sql = sql + "null, " ;
            }else{    
                sql = sql + "'" + capitania + "', ";
            }
            
            String tipo = request.getParameter("tipo");
            
            if (tipo == null || tipo.equals("T")){
                tipo = "null";
            }
            
            sql = sql + tipo;
            
            
            request.setAttribute("sqlSimplificado", sqlSimpl + sql);
            request.setAttribute("sqlCompleto", sqlComp + sql);
            
            request.getRequestDispatcher("/pages/3030-tipo-relatorio.jsp").forward(request, response);            
        }else if("imprimirRelatorio".equals(acao)){
            
            
            if (request.getParameter("tipoRel").equals("S") ){
                String sql = request.getParameter("sqlSimplificado");
                
                request.setAttribute("titulo", "Relacao Resumida de Embarcacoes");
                request.setAttribute("titulo2", "");
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "false");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "-1");
                
            }else{
                String sql = request.getParameter("sqlCompleto");
                
                request.setAttribute("titulo", "Relacao Embarcacoes");
                request.setAttribute("titulo2", "");
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "true");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "-1");
            
            }
            
            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                        
            
        }else{
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            String tipo = request.getParameter("tipo");
            if (tipo == null){
                tipo = "T";
            }
            Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);

            request.setAttribute("socio", s);
            request.setAttribute("barcos", SocioBarco.listar(s, tipo));
            request.setAttribute("tipo", tipo);
            
            request.getRequestDispatcher("/pages/3030-barco-lista.jsp").forward(request, response);            
        }
    }

    //INCLUSAO DE BARCOS DO SOCIO
    @App("2001")
    public static void inserir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        
        String acao = request.getParameter("acao");
        int matricula = Integer.parseInt(request.getParameter("matricula"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
        Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);

        if("gravar".equals(acao)){
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2001", request.getRemoteAddr());
            SocioBarco b = new SocioBarco();
            b.setSocio(s);
            preencherDadosBarco(b, request, response);

            b.inserir(audit);

            response.sendRedirect("c?app=2000&acao=showForm&matricula=" + b.getSocio().getMatricula() + "&seqDependente=" + b.getSocio().getSeqDependente() + "&idCategoria=" + b.getSocio().getIdCategoria());                                           
        }else{
            request.setAttribute("app", "2001");
            request.setAttribute("socio", s);
            request.setAttribute("categorias", CategoriaNautica.listar());
            request.setAttribute("locais", LocalBoxNautica.listar());
            request.setAttribute("tipos", TipoVagaBarco.listar());
            String tipo = request.getParameter("tipo");
            if ("BA".equals(tipo)){
                request.getRequestDispatcher("/pages/3030-barco.jsp").forward(request, response);            
            }else{
                request.getRequestDispatcher("/pages/3030-box.jsp").forward(request, response);            
            }
        }
    }        
    
    //ALTERAÇÃO DE BARCOS DO SOCIO
    @App("2002")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {            
        
        String acao = request.getParameter("acao");
        int idBarco = Integer.parseInt(request.getParameter("idBarco"));
        SocioBarco b = SocioBarco.getInstance(idBarco);
        request.setAttribute("barco", b);

        if("showListaPessoa".equals(acao)){
            request.setAttribute("autorizados", AutorizadoBarco.listar(b));
            request.getRequestDispatcher("/pages/3050-barco-autorizado-lista.jsp").forward(request, response);
        }else if("showFormIncluirPessoa".equals(acao)){                
            request.setAttribute("acao", "incluirPessoa");
            request.getRequestDispatcher("/pages/3050-barco-autorizado.jsp").forward(request, response);
        }else if("showFormAlterarPessoa".equals(acao)){
            request.setAttribute("acao", "alterarPessoa");
            int idAutorizado = Integer.parseInt(request.getParameter("idAutorizado"));
            request.setAttribute("pesAutorizada", AutorizadoBarco.getInstance(idAutorizado));
            request.getRequestDispatcher("/pages/3050-barco-autorizado.jsp").forward(request, response);                
        }else if("incluirPessoa".equals(acao)){
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2002", request.getRemoteAddr());
            AutorizadoBarco a = new AutorizadoBarco();
            a.setSocioBarco(b);
            a.setNome(request.getParameter("nome"));
            a.setObs(request.getParameter("obs"));
            a.inserir(audit);
            response.sendRedirect("c?app=2002&acao=showListaPessoa&idBarco=" + a.getSocioBarco().getId());
        }else if("alterarPessoa".equals(acao)){    
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2002", request.getRemoteAddr());
            int idAutorizado = Integer.parseInt(request.getParameter("idAutorizado"));
            AutorizadoBarco a = AutorizadoBarco.getInstance(idAutorizado);
            a.setNome(request.getParameter("nome"));
            a.setObs(request.getParameter("obs"));
            a.alterar(audit);
            response.sendRedirect("c?app=2002&acao=showListaPessoa&idBarco=" + a.getSocioBarco().getId());                
        }else if("excluirPessoa".equals(acao)){
            int idAutorizado = Integer.parseInt(request.getParameter("idAutorizado"));
            AutorizadoBarco a = AutorizadoBarco.getInstance(idAutorizado);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2002", request.getRemoteAddr());
            a.excluir(audit);

            response.sendRedirect("c?app=2002&acao=showListaPessoa&idBarco=" + a.getSocioBarco().getId());
        }else if("gravar".equals(acao)){
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2002", request.getRemoteAddr());
            preencherDadosBarco(b, request, response);

            b.alterar(audit);

            response.sendRedirect("c?app=2000&acao=showForm&matricula=" + b.getSocio().getMatricula() + "&seqDependente=" + b.getSocio().getSeqDependente() + "&idCategoria=" + b.getSocio().getIdCategoria());                           
        }else{
            request.setAttribute("app", "2002");
            request.setAttribute("socio", b.getSocio());
            request.setAttribute("categorias", CategoriaNautica.listar());
            request.setAttribute("tipos", TipoVagaBarco.listar());
            request.setAttribute("locais", LocalBoxNautica.listar());
            if (b.getEhBox().equals("S")){
                request.getRequestDispatcher("/pages/3030-box.jsp").forward(request, response);                
            }else{
                request.getRequestDispatcher("/pages/3030-barco.jsp").forward(request, response);                
            }
            
        }
            
    }        
        
    //EXCLUSAO DE BARCOS DO SOCIO
    @App("2003")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int idBarco = Integer.parseInt(request.getParameter("idBarco"));
        SocioBarco b = SocioBarco.getInstance(idBarco);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2003", request.getRemoteAddr());
        b.excluir(audit);

        response.sendRedirect("c?app=2000&acao=showForm&matricula=" + b.getSocio().getMatricula() + "&seqDependente=" + b.getSocio().getSeqDependente() + "&idCategoria=" + b.getSocio().getIdCategoria());           
    }        
    
    private static void preencherDadosBarco(SocioBarco b, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        DecimalFormat fmt = new DecimalFormat("#,##0.00");
        b.setNome(request.getParameter("nome"));
        CategoriaNautica c = CategoriaNautica.getInstance(request.getParameter("idCategoriaNautica"));
        b.setCategoriaNautica(c);
        TipoVagaBarco v = TipoVagaBarco.getInstance(request.getParameter("idTipoVaga"));
        b.setTipoVaga(v);
        try{
            b.setDesconto(fmt.parse(request.getParameter("desconto")).floatValue());
        }catch(ParseException e){
            b.setDesconto(0f);
        }
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
        b.setClassificacao(request.getParameter("classificacao"));
        b.setModelo(request.getParameter("modelo"));
        try{
            b.setPatrimonio(Integer.parseInt(request.getParameter("patrimonio")));
        }catch(NumberFormatException e){

        }     
        if(request.getParameter("documentacaoCompleta") != null && request.getParameter("documentacaoCompleta").equals("1")){
            b.setDocumentacaoCompleta(true);
        }else{
            b.setDocumentacaoCompleta(false);
        }
        try{
            b.setComprimentoTotal(Integer.parseInt(request.getParameter("comprimentoTotal")));
        }catch(NumberFormatException e){}
        try{
            b.setComprimentoBoca(Integer.parseInt(request.getParameter("comprimentoBoca")));
        }catch(NumberFormatException e){}
        try{
            b.setComprimentoPontal(Integer.parseInt(request.getParameter("comprimentoPontal")));
        }catch(NumberFormatException e){}                    
        b.setDataVencimentoSeguro(Datas.parse(request.getParameter("dataVencimentoSeguro")));
        b.setDataVencimentoRegistro(Datas.parse(request.getParameter("dataVencimentoRegistro")));
        b.setDataVencimentoHabilitacao(Datas.parse(request.getParameter("dataVencimentoHabilitacao")));
        b.setObs(request.getParameter("obs"));        
        try{
            LocalBoxNautica l = LocalBoxNautica.getInstance(Integer.parseInt(request.getParameter("idLocal")));
            b.setLocalBox(l);
        }catch(NumberFormatException e){}
        try{
            b.setQtM2(Integer.parseInt(request.getParameter("qtM2")));
        }catch(NumberFormatException e){}
        try{
            b.setQtGeladeira(Integer.parseInt(request.getParameter("qtGeladeira")));
        }catch(NumberFormatException e){}
        
        if(request.getParameter("emprestimoChave") != null && request.getParameter("emprestimoChave").equals("1")){
            b.setEmprestimoChave(true);
        }else{
            b.setEmprestimoChave(false);
        }
        if(request.getParameter("ehBox") != null && request.getParameter("ehBox").equals("S")){
            b.setEhBox("S");
        }else{
            b.setEhBox("N");
        }
        b.setDtIniDesconto(Datas.parse(request.getParameter("dtIniDesconto")));
        b.setDtFimDesconto(Datas.parse(request.getParameter("dtFimDesconto")));
        
    }
    
    @App("2005")
    public static void consultar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        int matricula = 0;
        int categoria = 0;
                
        for (String p : Collections.list(request.getParameterNames()))
                request.setAttribute(p, request.getParameter(p));
        
        if("consultar".equals(request.getParameter("acao"))){
            try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
            }catch(NumberFormatException e){
                matricula = 0;
            }
            try{
                categoria = Integer.parseInt(request.getParameter("categoria"));    
            }catch(NumberFormatException e){
                categoria = 0;
            }
            request.setAttribute("barcos", SocioBarco.listar2(categoria, matricula, request.getParameter("nome"), request.getParameter("capitania"), request.getParameter("nomeBarco"), request.getParameter("catNautica"), request.getParameter("doc"), request.getParameter("tipo")));
        }
        
        request.setAttribute("categorias", CategoriaNautica.listar());
        request.getRequestDispatcher("/pages/3030-barco-consulta.jsp").forward(request, response);  
        
        
    }        
        
}
