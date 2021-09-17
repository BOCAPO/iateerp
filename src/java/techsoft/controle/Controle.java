
package techsoft.controle;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.*;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.operacoes.*;
import techsoft.seguranca.*;
import techsoft.tabelas.*;
import techsoft.util.Datas;
import java.util.Collections;
import techsoft.clube.ImpressoraCartao;


public class Controle extends HttpServlet {
   
    private static final long serialVersionUID = 07042014L;
    private static final Logger log = Logger.getLogger(Controle.class.getCanonicalName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet responseX
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        
        
        String app = request.getParameter("app");
        String acao = request.getParameter("acao");
        if(acao == null){
            acao = "";
        }

        //ALTERAR SENHA
        if("9020".equals(app)){
            if("showForm".equals(acao)){
                request.getRequestDispatcher("/pages/9020.jsp").forward(request, response);
            }else{
                Usuario u = Usuario.getInstance(request.getUserPrincipal().getName());
                try{
                    u.alterarSenha(request.getParameter("senhaAtual"),
                        request.getParameter("novaSenha1"));
                    response.sendRedirect("c?app=1000");
                } catch (AlterarSenhaException e) {
                    //informa que a senha nao confere
                    request.setAttribute("alterarSenhaException", e.getMessage());
                    request.getRequestDispatcher("/pages/9020.jsp").forward(request, response);
                }
            }
            return;
        }

        if(app == null || !request.isUserInRole(app)){
            request.setAttribute("msg", "Usuário não autorizado na aplicação " + app);
            
            request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            return;
        }

        //HOME
        if("1000".equals(app)){
            request.getRequestDispatcher("/pages/home.jsp").forward(request, response);
        }else if("6000".equals(app)){
            request.getRequestDispatcher("/pages/homeCaixa.jsp").forward(request, response);
        }else if("7000".equals(app)){
            request.getRequestDispatcher("/pages/homeAcesso.jsp").forward(request, response);
        }

        //LISTA DE SOCIOS E DEPENDENTES
        else if("9030".equals(app)){
            if("consultar".equals(acao)){
                int carteira = 0;
                int categoria = 0;
                int matricula = 0;
                String nome = request.getParameter("nome");

                try{
                    carteira = Integer.parseInt(request.getParameter("carteira"));
                }catch(NumberFormatException e){
                    carteira = 0;
                }
                try{
                    categoria = Integer.parseInt(request.getParameter("categoria"));
                }catch(NumberFormatException e){
                    categoria = 0;
                }
                try{
                    matricula = Integer.parseInt(request.getParameter("matricula"));
                }catch(NumberFormatException e){
                    matricula = 0;
                }
                if(nome == null) nome = "";

                
                if (carteira > 0 || matricula > 0 || categoria > 0 || nome != ""){
                    List<Socio> socios = Socio.listar(carteira, categoria, matricula, nome);
                    request.setAttribute("socios", socios);
                }
                
                for (String p : Collections.list(request.getParameterNames()))
                        request.setAttribute(p, request.getParameter(p));
                
                if (matricula == 0){
                    request.setAttribute("matricula", "");
                    request.setAttribute("categoria", "");                    
                }
                if (carteira == 0){
                    request.setAttribute("carteira", "");
                }
                
                request.getRequestDispatcher("/pages/3110-lista.jsp").forward(request, response);
            }else if("selecionar".equals(acao)){
                int matricula = 0;
                int seqDependente = 0;
                int idCategoria = 0;

//                try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
                seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
                idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
//                }catch(NumberFormatException e){
//                    log.severe("Erro ao ler parametros para app 9030 acao " + acao);
//                }
                Socio socio = Socio.getInstance(matricula, seqDependente, idCategoria);
                //TODO - Repassando alerta... deveria vir da consulta ao objeto socio
                request.setAttribute("alerta", request.getParameter("alerta"));
                request.setAttribute("socio", socio);
                request.getRequestDispatcher("/pages/3110-menu.jsp").forward(request, response);
            }else if("detalhar".equals(acao)){
                int matricula = 0;
                int seq = 0;
                int idCategoria = 0;

                matricula = Integer.parseInt(request.getParameter("matricula"));
                seq = Integer.parseInt(request.getParameter("seqDependente"));
                idCategoria = Integer.parseInt(request.getParameter("idCategoria"));

                Titular titular = Titular.getInstance(matricula, idCategoria);
                request.setAttribute("titular", titular);

                if(seq == 0){    
                    request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
                    request.setAttribute("profissoes", ComboBoxLoader.listar("TB_PROFISSAO"));
                    request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_ESPECIAL"));
                    request.setAttribute("app", app);
                    request.getRequestDispatcher("/pages/3110.jsp").forward(request, response);
                }else{
                    titular.carregarDependentes();
                    Dependente d = Dependente.getInstance(matricula, seq, idCategoria);
                    d.carregarMateriais();
                    request.getSession().setAttribute("dependente", d);
                    request.setAttribute("tipos", ComboBoxLoader.listar("TB_TIPO_DEPENDENTE"));
                    request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_ESPECIAL"));
                    request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
                    request.setAttribute("materiais", ComboBoxLoader.listar("TB_MATERIAL"));
                    request.setAttribute("app", app);
                    request.getRequestDispatcher("/pages/3115-dependente.jsp").forward(request, response);
                }
            }else{
                for (String p : Collections.list(request.getParameterNames()))
                        request.setAttribute(p, request.getParameter(p));
                
                request.getRequestDispatcher("/pages/3110-lista.jsp").forward(request, response);
            }
        }

        //INCLUIR TITULAR
        else if("9031".equals(app) || "9041".equals(app)){
            if("gravar".equals(acao)){
                Titular t = (Titular)request.getSession().getAttribute("titular");
                
                //DADOS PESSOAIS
                t.getSocio().setMatricula(Integer.parseInt(request.getParameter("matricula")));
                t.getSocio().setSeqDependente(0);
                t.getSocio().setIdCategoria(Integer.parseInt(request.getParameter("idCategoria")));
                t.getSocio().setNome(request.getParameter("nome"));
                if(request.getParameter("tipoPessoa").equals("F")){
                    t.setPessoaFisica(true);
                }else{
                    t.setPessoaFisica(false);//pessoa juridica
                }
                t.getSocio().setMasculino(request.getParameter("sexo").equalsIgnoreCase("M"));
                t.getSocio().setDataNascimento(Datas.parse(request.getParameter("dataNascimento")));
                t.setCpfCnpj(request.getParameter("cpfCnpj"));
                t.setRG(request.getParameter("rg"));
                t.setEstadoCivil(request.getParameter("estadoCivil"));
                t.getSocio().setSituacao(request.getParameter("situacao"));
                t.setEmail(request.getParameter("email"));
                t.setNaturalidade(request.getParameter("naturalidade"));
                t.setNacionalidade(request.getParameter("nacionalidade"));
                t.setIdProfissao(Integer.parseInt(request.getParameter("idProfissao")));
                t.setIdCargoEspecial(Integer.parseInt(request.getParameter("idCargoEspecial")));
                if(request.getParameter("cargoAtivo") != null && request.getParameter("cargoAtivo").equals("1")){
                    t.setCargoAtivo(true);
                }else{
                    t.setCargoAtivo(false);
                }
                t.setDataAdmissao(Datas.parse(request.getParameter("dataAdmissao")));
                t.setNomePai(request.getParameter("nomePai"));
                t.setNomeMae(request.getParameter("nomeMae"));
                t.setProponente(new Socio());
                try{
                    t.getProponente().setIdCategoria(Integer.parseInt(request.getParameter("proponenteCategoria")));
                }catch(NumberFormatException e){
                    t.getProponente().setIdCategoria(0);
                }
                try{
                    t.getProponente().setMatricula(Integer.parseInt(request.getParameter("proponenteMatricula")));
                }catch(NumberFormatException e){
                    t.getProponente().setMatricula(0);
                }
                t.getProponente().setNome(request.getParameter("proponenteNome"));
                
                if(request.getParameter("espolio") != null && request.getParameter("espolio").equals("1")){
                    t.setEspolio(true);
                }else{
                    t.setEspolio(false);
                }
                
                
                //ENDERECOS
                t.setTelefoneCelular(request.getParameter("telefoneCelular"));
                t.setTelefoneAlternativo(request.getParameter("telefoneAlternativo"));
                t.setFax(request.getParameter("fax"));
                t.getEnderecoResidencial().setEndereco(request.getParameter("enderecoR"));
                t.getEnderecoResidencial().setBairro(request.getParameter("bairroR"));
                t.getEnderecoResidencial().setCidade(request.getParameter("cidadeR"));
                t.getEnderecoResidencial().setUF(request.getParameter("ufR"));
                t.getEnderecoResidencial().setCEP(request.getParameter("cepR"));
                t.getEnderecoResidencial().setTelefone(request.getParameter("telefoneR"));
                t.getEnderecoComercial().setEndereco(request.getParameter("enderecoC"));
                t.getEnderecoComercial().setBairro(request.getParameter("bairroC"));
                t.getEnderecoComercial().setCidade(request.getParameter("cidadeC"));
                t.getEnderecoComercial().setUF(request.getParameter("ufC"));
                t.getEnderecoComercial().setCEP(request.getParameter("cepC"));
                t.getEnderecoComercial().setTelefone(request.getParameter("telefoneC"));
                t.setDestinoCorrespondencia(request.getParameter("destinoCorrespondencia"));
                t.setDestinoCarne(request.getParameter("destinoCarne"));
                
                //AUTORIZAR DEBITO EM CONTA
                try{
                    t.getContaCorrente().setAgencia(Integer.parseInt(request.getParameter("agencia")));
                }catch(NumberFormatException e){
                    t.getContaCorrente().setAgencia(null);
                }
                t.getContaCorrente().setDigitoAgencia(request.getParameter("digitoAgencia"));
                t.getContaCorrente().setConta(request.getParameter("conta"));
                t.getContaCorrente().setDigitoConta(request.getParameter("digitoConta"));
                t.getContaCorrente().setTitular(request.getParameter("titular"));
                
                //PARAMETROS
                NumberFormat fmt = NumberFormat.getCurrencyInstance();
                float f = 0;
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                t.setIgnorarPagamentos(request.getParameter("ignorarPagamentos") != null);
                t.setBloquearEmissaoConvites(request.getParameter("bloquearEmissaoConvites") != null);
                t.setBloquearReservaChurrasqueira(request.getParameter("bloquearReservaChurrasqueira") != null);
                t.setBloquearCadastroEmbarcacoes(request.getParameter("bloquearCadastroEmbarcacoes") != null);
                t.setBloquearMatriculas(request.getParameter("bloquearMatriculas") != null);
                t.setBloquearEmprestimoMaterial(request.getParameter("bloquearEmprestimoMaterial") != null);

                try {
                    f = fmt.parse(request.getParameter("consumoMaximo")).floatValue();
                } catch (Exception e) {
                    f = 0;
                }
                t.setConsumoMaximo(f);
                try {
                    f = fmt.parse(request.getParameter("maximoTxIndCheque")).floatValue();
                } catch (Exception e) {
                    f = 0;
                }
                t.setMaximoTxIndCheque(f);
                if(request.getParameter("tipoEnvioBoleto").equals("zero")){
                    t.setTipoEnvioBoleto(null);
                }else{
                    t.setTipoEnvioBoleto(request.getParameter("tipoEnvioBoleto"));
                }
                
                try {
                    t.inserir(audit);
                    request.getSession().removeAttribute("titular");
                    
                    List<Socio> socios = Socio.listar(0, 0, 0, request.getParameter("nome"));
                    request.setAttribute("socios", socios);
                    request.setAttribute("nome", request.getParameter("nome"));
                    
                    request.getRequestDispatcher("/pages/3110-lista.jsp").forward(request, response);
                    
                } catch (InserirTitularException e) {
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }else{
                //Titular precisa estar na sessao por causa da opcao de inserir e ignorar lista negra.
                request.getSession().setAttribute("titular", new Titular());
                request.setAttribute("app", app);
                request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
                request.setAttribute("profissoes", ComboBoxLoader.listar("TB_PROFISSAO"));
                request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_ESPECIAL"));
                request.getRequestDispatcher("/pages/3110.jsp").forward(request, response);
            }
        }

        //ALTERAR TITULAR
        else if("9032".equals(app) || "9042".equals(app)){
            if("gravar".equals(acao)){
                Titular t = (Titular)request.getSession().getAttribute("titular");

                //DADOS PESSOAIS
                //Matricula e categoria nao podem ser alterados
                t.getSocio().setNome(request.getParameter("nome"));
                if(request.getParameter("tipoPessoa").equals("F")){
                    t.setPessoaFisica(true);
                }else{
                    t.setPessoaFisica(false);//pessoa juridica
                }
                t.getSocio().setMasculino(request.getParameter("sexo").equalsIgnoreCase("M"));
                t.getSocio().setDataNascimento(Datas.parse(request.getParameter("dataNascimento")));
                t.setCpfCnpj(request.getParameter("cpfCnpj"));
                t.setRG(request.getParameter("rg"));
                t.setEstadoCivil(request.getParameter("estadoCivil"));
                t.getSocio().setSituacao(request.getParameter("situacao"));
                t.setEmail(request.getParameter("email"));
                t.setNaturalidade(request.getParameter("naturalidade"));
                t.setNacionalidade(request.getParameter("nacionalidade"));
                t.setIdProfissao(Integer.parseInt(request.getParameter("idProfissao")));
                t.setIdCargoEspecial(Integer.parseInt(request.getParameter("idCargoEspecial")));
                if(request.getParameter("cargoAtivo") != null && request.getParameter("cargoAtivo").equals("1")){
                    t.setCargoAtivo(true);
                }else{
                    t.setCargoAtivo(false);
                }
                t.setDataAdmissao(Datas.parse(request.getParameter("dataAdmissao")));
                t.setDataEntregaBrinde(Datas.parse(request.getParameter("dataEntregaBrinde")));
                t.setNomePai(request.getParameter("nomePai"));
                t.setNomeMae(request.getParameter("nomeMae"));
                t.setProponente(new Socio());
                try{
                    t.getProponente().setIdCategoria(Integer.parseInt(request.getParameter("proponenteCategoria")));
                }catch(NumberFormatException e){
                    t.getProponente().setIdCategoria(0);
                }
                try{
                    t.getProponente().setMatricula(Integer.parseInt(request.getParameter("proponenteMatricula")));
                }catch(NumberFormatException e){
                    t.getProponente().setMatricula(0);
                }
                t.getProponente().setNome(request.getParameter("proponenteNome"));
                
                if(request.getParameter("espolio") != null && request.getParameter("espolio").equals("1")){
                    t.setEspolio(true);
                }else{
                    t.setEspolio(false);
                }
                
                
                //ENDERECO
                t.setTelefoneCelular(request.getParameter("telefoneCelular"));
                t.setTelefoneAlternativo(request.getParameter("telefoneAlternativo"));
                t.setFax(request.getParameter("fax"));
                t.getEnderecoResidencial().setEndereco(request.getParameter("enderecoR"));
                t.getEnderecoResidencial().setBairro(request.getParameter("bairroR"));
                t.getEnderecoResidencial().setCidade(request.getParameter("cidadeR"));
                t.getEnderecoResidencial().setUF(request.getParameter("ufR"));
                t.getEnderecoResidencial().setCEP(request.getParameter("cepR"));
                t.getEnderecoResidencial().setTelefone(request.getParameter("telefoneR"));
                t.getEnderecoComercial().setEndereco(request.getParameter("enderecoC"));
                t.getEnderecoComercial().setBairro(request.getParameter("bairroC"));
                t.getEnderecoComercial().setCidade(request.getParameter("cidadeC"));
                t.getEnderecoComercial().setUF(request.getParameter("ufC"));
                t.getEnderecoComercial().setCEP(request.getParameter("cepC"));
                t.getEnderecoComercial().setTelefone(request.getParameter("telefoneC"));
                t.setDestinoCorrespondencia(request.getParameter("destinoCorrespondencia"));
                t.setDestinoCarne(request.getParameter("destinoCarne"));
                
                //AUTORIZAR DEBITO EM CONTA
                try{
                t.getContaCorrente().setAgencia(Integer.parseInt(request.getParameter("agencia")));
                }catch(NumberFormatException e){
                    t.getContaCorrente().setAgencia(null);
                }
                t.getContaCorrente().setDigitoAgencia(request.getParameter("digitoAgencia"));
                t.getContaCorrente().setConta(request.getParameter("conta"));
                t.getContaCorrente().setDigitoConta(request.getParameter("digitoConta"));
                t.getContaCorrente().setTitular(request.getParameter("titular"));
                
                //PARAMETROS
                DecimalFormat fmt = new DecimalFormat("#,##0.00");
                float f = 0;
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                t.setIgnorarPagamentos(request.getParameter("ignorarPagamentos") != null);
                t.setBloquearEmissaoConvites(request.getParameter("bloquearEmissaoConvites") != null);
                t.setBloquearReservaChurrasqueira(request.getParameter("bloquearReservaChurrasqueira") != null);
                t.setBloquearCadastroEmbarcacoes(request.getParameter("bloquearCadastroEmbarcacoes") != null);
                t.setBloquearMatriculas(request.getParameter("bloquearMatriculas") != null);
                t.setBloquearEmprestimoMaterial(request.getParameter("bloquearEmprestimoMaterial") != null);

                try {
                    f = fmt.parse(request.getParameter("consumoMaximo")).floatValue();
                } catch (Exception e) {
                    f = 0;
                }
                t.setConsumoMaximo(f);
                try {
                    f = fmt.parse(request.getParameter("maximoTxIndCheque")).floatValue();
                } catch (Exception e) {
                    f = 0;
                }                
                t.setMaximoTxIndCheque(f);
                if(request.getParameter("tipoEnvioBoleto").equals("zero")){
                    t.setTipoEnvioBoleto(null);
                }else{
                    t.setTipoEnvioBoleto(request.getParameter("tipoEnvioBoleto"));
                }
                
                t.alterar(audit);
                request.getSession().removeAttribute("titular");
                
                List<Socio> socios = Socio.listar(0, 0, 0, request.getParameter("nome"));
                request.setAttribute("socios", socios);
                request.setAttribute("nome", request.getParameter("nome"));

                request.getRequestDispatcher("/pages/3110-lista.jsp").forward(request, response);
            }else{
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));

                //Colocado na sessao para evitar fazer outra busca pelo titular na hora de gravar
                Titular titular = Titular.getInstance(matricula, idCategoria);
                request.setAttribute("app", app);
                request.getSession().setAttribute("titular", titular);
                request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
                request.setAttribute("profissoes", ComboBoxLoader.listar("TB_PROFISSAO"));
                request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_ESPECIAL"));
                request.getRequestDispatcher("/pages/3110.jsp").forward(request, response);
            }
        }

        //ALTERAR SOMENTE ENDEREï¿½O TITULAR
        else if("9039".equals(app) || "9049".equals(app)){
            if("gravar".equals(acao)){
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                Titular t = (Titular)request.getSession().getAttribute("titular");
                t.setDestinoCorrespondencia(request.getParameter("destinoCorrespondencia"));
                t.setDestinoCarne(request.getParameter("destinoCarne"));
                t.getEnderecoResidencial().setEndereco(request.getParameter("enderecoR"));
                t.getEnderecoResidencial().setBairro(request.getParameter("bairroR"));
                t.getEnderecoResidencial().setCidade(request.getParameter("cidadeR"));
                t.getEnderecoResidencial().setUF(request.getParameter("ufR"));
                t.getEnderecoResidencial().setCEP(request.getParameter("cepR"));
                t.getEnderecoResidencial().setTelefone(request.getParameter("telefoneR"));
                t.getEnderecoComercial().setEndereco(request.getParameter("enderecoC"));
                t.getEnderecoComercial().setBairro(request.getParameter("bairroC"));
                t.getEnderecoComercial().setCidade(request.getParameter("cidadeC"));
                t.getEnderecoComercial().setUF(request.getParameter("ufC"));
                t.getEnderecoComercial().setCEP(request.getParameter("cepC"));
                t.getEnderecoComercial().setTelefone(request.getParameter("telefoneC"));
                
                t.alterarEnderecos(audit);

                List<Socio> socios = Socio.listar(0, 0, 0, t.getSocio().getNome() );
                request.setAttribute("socios", socios);
                request.setAttribute("nome", t.getSocio().getNome());

                request.getRequestDispatcher("/pages/3110-lista.jsp").forward(request, response);
            }else{
                int matricula = 0;
                int idCategoria = 0;

                matricula = Integer.parseInt(request.getParameter("matricula"));
                idCategoria = Integer.parseInt(request.getParameter("idCategoria"));

                Titular titular = Titular.getInstance(matricula, idCategoria);
                request.setAttribute("app", app);
                request.setAttribute("acao", "gravar");
                request.getSession().setAttribute("titular", titular);
                request.getRequestDispatcher("/pages/3110-enderecos.jsp").forward(request, response);
            }
        }

        //EXCLUIR TITULAR
        else if("9033".equals(app) || "9043".equals(app)){
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            int matricula = 0;
            int idCategoria = 0;
            matricula = Integer.parseInt(request.getParameter("matricula"));
            idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            boolean excluirTaxaIndividual = Boolean.parseBoolean(request.getParameter("excluirTaxaIndividual"));
            Titular titular = Titular.getInstance(matricula, idCategoria);

            try {
                titular.excluir(audit, excluirTaxaIndividual);
                
                List<Socio> socios = Socio.listar(0, 0, 0, titular.getSocio().getNome() );
                request.setAttribute("socios", socios);
                request.setAttribute("nome", titular.getSocio().getNome());

                request.getRequestDispatcher("/pages/3110-lista.jsp").forward(request, response);
            }catch(ExcluirTitularException e){
                request.setAttribute("err", e);
                
                if(e.isExisteTaxaIndividual()){
                    request.setAttribute("msg", e.getMessage());
                    request.setAttribute("sim", "c?app=" + app
                            + "&matricula=" + matricula
                            + "&idCategoria=" + idCategoria
                            + "&excluirTaxaIndividual=true");
                    request.setAttribute("nao", "javascript: history.go(-1)");
                    request.getRequestDispatcher("/pages/confirmacao.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                } 
            }
        }

        //INCLUIR DEPENDENTE
        else if("9035".equals(app) || "9045".equals(app)){
            if("gravar".equals(acao)){
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                Dependente d = new Dependente();
                d.setMatricula(Integer.parseInt(request.getParameter("matricula")));
                d.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria")));
                d.setNome(request.getParameter("nome"));
                d.setIdTipoDependente(Integer.parseInt(request.getParameter("idTipoDependente")));
                d.setCasoEspecial(request.getParameter("casoEspecial"));
                d.setDataCasoEspecial(Datas.parse(request.getParameter("dataCasoEspecial")));
                if(request.getParameter("sexo").equalsIgnoreCase("M")){
                    d.setMasculino(true);
                }else{
                    d.setMasculino(false);
                }
                d.setDataNascimento(Datas.parse(request.getParameter("dataNascimento")));
                d.setDataValidadeRetiradaMaterial(Datas.parse(request.getParameter("dataValidadeRetiradaMaterial")));
                d.setDataValidadeRetiradaConvite(Datas.parse(request.getParameter("dataValidadeRetiradaConvite")));
                d.setDataValidadeReservaChurrasqueira(Datas.parse(request.getParameter("dataValidadeReservaChurrasqueira")));
                d.setTelefoneCelular(request.getParameter("telefoneCelular"));
                d.setTelefoneComercial(request.getParameter("telefoneComercial"));
                d.setTelefoneResidencial(request.getParameter("telefoneResidencial"));
                d.setFax(request.getParameter("fax"));
                d.setEmail(request.getParameter("email"));
                d.setIdCargoEspecial(Integer.parseInt(request.getParameter("idCargoEspecial")));
                if(request.getParameter("cargoAtivo") != null && request.getParameter("cargoAtivo").equals("1")){
                    d.setCargoAtivo(true);
                }else{
                    d.setCargoAtivo(false);
                }
                if(request.getParameter("empTodosMat") != null && request.getParameter("empTodosMat").equals("1")){
                    d.setEmpTodosMat(true);
                }else{
                    d.setEmpTodosMat(false);
                }
                if(request.getParameter("consumoTodasTx") != null && request.getParameter("consumoTodasTx").equals("1")){
                    d.setConsumoTodasTx(true);
                }else{
                    d.setConsumoTodasTx(false);
                }

                if(request.getParameterValues("idMaterial") != null){
                    for(String s : request.getParameterValues("idMaterial")){
                        d.getMateriais().put(Integer.parseInt(s), s);
                    }
                }

                try {
                    d.inserir(audit);
                    List<Socio> socios = Socio.listar(0 ,d.getIdCategoria(), d.getMatricula(), "");
                    request.setAttribute("socios", socios);
                    request.setAttribute("categoria", d.getIdCategoria());
                    request.setAttribute("matricula", d.getMatricula());

                    request.getRequestDispatcher("/pages/3110-lista.jsp").forward(request, response);
                } catch (InserirDependenteException e) {
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }else{
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));

                Titular titular = Titular.getInstance(matricula, idCategoria);
                titular.carregarDependentes();
                Dependente d = new Dependente();
                request.setAttribute("dependente", d);
                request.setAttribute("titular", titular);
                request.setAttribute("app", app);
                request.setAttribute("tipos", ComboBoxLoader.listar("TB_TIPO_DEPENDENTE"));
                request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_ESPECIAL"));
                request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
                request.setAttribute("materiais", ComboBoxLoader.listar("TB_MATERIAL"));
                request.getRequestDispatcher("/pages/3115-dependente.jsp").forward(request, response);
            }
        }

        //ALTERAR DEPENDENTE
        else if("9036".equals(app) || "9046".equals(app)){
            if("gravar".equals(acao)){
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                Dependente d = new Dependente();
                d.setMatricula(Integer.parseInt(request.getParameter("matricula")));
                d.setSeqDependente(Integer.parseInt(request.getParameter("seqDependente")));
                d.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria")));
                d.setNome(request.getParameter("nome"));
                d.setIdTipoDependente(Integer.parseInt(request.getParameter("idTipoDependente")));
                d.setCasoEspecial(request.getParameter("casoEspecial"));
                d.setDataCasoEspecial(Datas.parse(request.getParameter("dataCasoEspecial")));
                if(request.getParameter("sexo").equalsIgnoreCase("M")){
                    d.setMasculino(true);
                }else{
                    d.setMasculino(false);
                }
                d.setDataNascimento(Datas.parse(request.getParameter("dataNascimento")));
                d.setDataValidadeRetiradaMaterial(Datas.parse(request.getParameter("dataValidadeRetiradaMaterial")));
                d.setDataValidadeRetiradaConvite(Datas.parse(request.getParameter("dataValidadeRetiradaConvite")));
                d.setDataValidadeReservaChurrasqueira(Datas.parse(request.getParameter("dataValidadeReservaChurrasqueira")));
                d.setTelefoneCelular(request.getParameter("telefoneCelular"));
                d.setTelefoneComercial(request.getParameter("telefoneComercial"));
                d.setTelefoneResidencial(request.getParameter("telefoneResidencial"));
                d.setFax(request.getParameter("fax"));
                d.setEmail(request.getParameter("email"));
                d.setIdCargoEspecial(Integer.parseInt(request.getParameter("idCargoEspecial")));
                if(request.getParameter("cargoAtivo") != null && request.getParameter("cargoAtivo").equals("1")){
                    d.setCargoAtivo(true);
                }else{
                    d.setCargoAtivo(false);
                }
                if(request.getParameter("empTodosMat") != null && request.getParameter("empTodosMat").equals("1")){
                    d.setEmpTodosMat(true);
                }else{
                    d.setEmpTodosMat(false);
                }
                if(request.getParameter("consumoTodasTx") != null && request.getParameter("consumoTodasTx").equals("1")){
                    d.setConsumoTodasTx(true);
                }else{
                    d.setConsumoTodasTx(false);
                }

                if(request.getParameterValues("idMaterial") != null){
                    for(String s : request.getParameterValues("idMaterial")){
                        d.getMateriais().put(Integer.parseInt(s), s);
                    }
                }
                
                d.alterar(audit);
                //response.sendRedirect("c?app=9030");
                //volta para a selecao do dependente
                
                response.sendRedirect("c?app=9030&acao=consultar&matricula="
                        + d.getMatricula()
                        + "&categoria="
                        + d.getIdCategoria());
            }else{
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));

                Titular titular = Titular.getInstance(matricula, idCategoria);
                titular.carregarDependentes();
                Dependente d = Dependente.getInstance(matricula, seqDependente, idCategoria);
                d.carregarMateriais();
                request.getSession().setAttribute("dependente", d);
                request.setAttribute("titular", titular);
                request.setAttribute("app", app);
                request.setAttribute("tipos", ComboBoxLoader.listar("TB_TIPO_DEPENDENTE"));
                request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_ESPECIAL"));
                request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
                request.setAttribute("materiais", ComboBoxLoader.listar("TB_MATERIAL"));
                request.getRequestDispatcher("/pages/3115-dependente.jsp").forward(request, response);
            }
        }

        //EXCLUIR DEPENDENTE
        else if("9037".equals(app) || "9047".equals(app)){
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));

            boolean excluirTaxaIndividual = Boolean.parseBoolean(request.getParameter("excluirTaxaIndividual"));
            Dependente dependente = Dependente.getInstance(matricula, seqDependente, idCategoria);

            try {
                dependente.excluir(audit, excluirTaxaIndividual);
                response.sendRedirect("c?app=9030&acao=consultar&matricula="
                        + matricula
                        + "&categoria="
                        + idCategoria);
            }catch(ExcluirDependenteException e){
                request.setAttribute("err", e);
                if(e.isExisteTaxaIndividual()){
                    request.setAttribute("msg", e.getMessage());
                    request.setAttribute("sim", "c?app=" + app
                            + "&matricula=" + matricula
                            + "&idCategoria=" + idCategoria
                            + "&seqDependente=" + seqDependente
                            + "&excluirTaxaIndividual=true");
                    request.setAttribute("nao", "javascript: history.go(-1)");
                    request.getRequestDispatcher("/pages/confirmacao.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
        }

        //LISTA DE CURSOS DO SOCIO
        else if("3080".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);
            
            request.setAttribute("consultaCursoSocio", ConsultaCursoSocio.listar(s));
            request.setAttribute("socio", s);
            request.getRequestDispatcher("/pages/1310-lista.jsp").forward(request, response);
        }
        
        //RETIRAR TAXA DA GERACAO DE CARNE
        else if("1610".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int alerta = Integer.parseInt(request.getParameter("alerta"));
            Titular t = Titular.getInstance(matricula, idCategoria);
            
            if("showForm".equals(acao)){
                t.carregarTaxasAdministrativas();
                request.setAttribute("titular", t);
                request.setAttribute("alerta", alerta);
                request.setAttribute("taxas", ComboBoxLoader.listar("TB_TAXA_ADMINISTRATIVA"));
                request.getRequestDispatcher("/pages/1610.jsp").forward(request, response);
            }else{
                if(request.getParameterValues("idTaxaAdministrativa") != null){
                    for(String s : request.getParameterValues("idTaxaAdministrativa")){
                        t.getTaxasAdministrativas().put(Integer.parseInt(s), s);
                    }
                }
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                t.atualizarTaxasAdministrativas(audit);
                response.sendRedirect("c?app=1610&acao=showForm&matricula="
                        + matricula
                        + "&idCategoria="
                        + idCategoria
                        + "&alerta="
                        + alerta);
            }
        }
        
        //BLOQUEIO DE TAXA
        else if("2120".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int alerta = Integer.parseInt(request.getParameter("alerta"));
            Titular t = Titular.getInstance(matricula, idCategoria);
            
            if("showForm".equals(acao)){
                t.carregarTaxasIndividuais();
                request.setAttribute("titular", t);
                request.setAttribute("alerta", alerta);
                request.setAttribute("taxas", ComboBoxLoader.listar("VW_TAXAS_INDIVIDUAIS"));
                request.getRequestDispatcher("/pages/2120.jsp").forward(request, response);
            }else{
                if(request.getParameterValues("idTaxaIndividual") != null){
                    for(String s : request.getParameterValues("idTaxaIndividual")){
                        t.getTaxasIndividuais().put(Integer.parseInt(s), s);
                    }
                }
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                t.atualizarTaxasIndividuais(audit);
                response.sendRedirect("c?app=2120&acao=showForm&matricula="
                        + matricula
                        + "&idCategoria="
                        + idCategoria
                        + "&alerta="
                        + alerta);
            }
        }
        
        //AUTORIZACAO PARA DEPENDENTE UTILIZAR TAXA
        else if("1740".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            int alerta = Integer.parseInt(request.getParameter("alerta"));
            Dependente d = Dependente.getInstance(matricula, seqDependente, idCategoria);
            
            if("showForm".equals(acao)){
                d.carregarTaxasIndividuais();
                request.setAttribute("dependente", d);
                request.setAttribute("alerta", alerta);
                request.setAttribute("taxas", ComboBoxLoader.listar("VW_TAXAS_INDIVIDUAIS"));
                request.getRequestDispatcher("/pages/1740.jsp").forward(request, response);
            }else{
                if(request.getParameterValues("idTaxaIndividual") != null){
                    for(String s : request.getParameterValues("idTaxaIndividual")){
                        d.getTaxasIndividuais().put(Integer.parseInt(s), s);
                    }
                }
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                d.atualizarTaxasIndividuais(audit);
                response.sendRedirect("c?app=1740&acao=showForm&matricula="
                        + matricula
                        + "&idCategoria="
                        + idCategoria
                        + "&seqDependente="
                        + seqDependente
                        + "&alerta="
                        + alerta);
            }
        }
        
        //LISTA DE ARMARIOS
        else if("1320".equals(app)){
            int tipoArmario = 0;
            try{
                tipoArmario = Integer.parseInt(request.getParameter("tipoArmario"));
            }catch(NumberFormatException e){
                tipoArmario = 1;
            }
            request.setAttribute("app", app);
            request.setAttribute("tipoArmario", tipoArmario);
            request.setAttribute("tipos", ComboBoxLoader.listar("TB_TIPO_ARMARIO"));
            request.setAttribute("armarios", Armario.listarTodos(tipoArmario));
            request.getRequestDispatcher("/pages/1320-lista.jsp").forward(request, response);
        }

        //INSERIR ARMARIO
        else if("1321".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.setAttribute("tipos", ComboBoxLoader.listar("TB_TIPO_ARMARIO"));
                request.getRequestDispatcher("/pages/1320.jsp").forward(request, response);
            }else{
                Armario a = new Armario();
            	a.setNumero(Integer.parseInt(request.getParameter("numeroArmario")));
                a.setTipo(Integer.parseInt(request.getParameter("tipoArmario")));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    a.inserir(audit);
                    response.sendRedirect("c?app=1320");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
        }
        
        //EXCLUIR ARMARIO
        else if("1322".equals(app)){
            int numeroArmario = Integer.parseInt(request.getParameter("numeroArmario"));
            int tipoArmario = Integer.parseInt(request.getParameter("tipoArmario"));
            Armario a = Armario.getInstance(numeroArmario, tipoArmario);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            a.excluir(audit);
            	
            response.sendRedirect("c?app=1320");
        }
        
        //VINCULAR ARMARIO
        else if("1323".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("numeroArmario", request.getParameter("numeroArmario"));
                request.setAttribute("tipoArmario", request.getParameter("tipoArmario"));
                
                request.getRequestDispatcher("/pages/1330.jsp").forward(request, response);
            }else if("consultar".equals(acao)){
                request.setAttribute("numeroArmario", request.getParameter("numeroArmario"));
                request.setAttribute("tipoArmario", request.getParameter("tipoArmario"));
                
                /*
                 * A logica eh mostrar apenas os socios onde seqDependente = 0
                 * mas como a classe socio nao tem essa logica no metodo listar
                 * a filtragem esta sendo na JSP
                 * Hugo
                 */
                List<Socio> socios = Socio.listar(0, 0, 0, request.getParameter("nome"));
                
                for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
                
                request.setAttribute("socios", socios);
                request.getRequestDispatcher("/pages/1330.jsp").forward(request, response);
            }else{
                //vincular
                int numeroArmario = Integer.parseInt(request.getParameter("numeroArmario"));
                int tipoArmario = Integer.parseInt(request.getParameter("tipoArmario"));
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));                
                Armario a = Armario.getInstance(numeroArmario, tipoArmario);
                Socio s = Socio.getInstance(matricula, 0, idCategoria);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                a.vincular(s, audit);
                response.sendRedirect("c?app=1320");
            }
            	
            
        }
        
        //DESVINCULAR ARMARIO
        else if("1324".equals(app)){
            int numeroArmario = Integer.parseInt(request.getParameter("numeroArmario"));
            int tipoArmario = Integer.parseInt(request.getParameter("tipoArmario"));
            Armario a = Armario.getInstance(numeroArmario, tipoArmario);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            a.desvincularSocioAtual(audit);

            if("showForm".equals(acao)){
                request.setAttribute("armarios", Armario.listarVinculados(a.getSocio()));
                request.setAttribute("socio", a.getSocio());
                request.getRequestDispatcher("/pages/1340.jsp").forward(request, response);
            }else{
                response.sendRedirect("c?app=1320");
            }
        }
        
        //DESVINCULAR ARMARIO PELO MENU DO CADASTRO DE SOCIOS
        else if("1340".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);
            request.setAttribute("app", app);
            request.setAttribute("armarios", Armario.listarVinculados(s));
            request.setAttribute("socio", s);
            request.getRequestDispatcher("/pages/1340.jsp").forward(request, response);
        }
        
        //EMISSAO DE CARTEIRINHA
        else if("9080".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);
            if("showForm".equals(acao)){
                Date vencimento = null;
                
                if(!s.getSituacao().equals("NO")){
                    request.setAttribute("err", new RuntimeException("Situacao da pessoa nao permite emissÃ£o da carteirinha"));
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                    return;
                }
                
                request.setAttribute("socio", s);
                request.setAttribute("cargoEspecial", s.descricaoCargoEspecial());
                try{
                    vencimento = s.calcularDataVencimentoCarteirinha();
                    request.setAttribute("dataVencimento", vencimento);
                    request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
                    request.getRequestDispatcher("/pages/9080.jsp").forward(request, response);                
                }catch(VencimentoCarteirinhaException e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }else if("emitirCarteirinha".equals(acao)){
                String vencimento = request.getParameter("dataVencimento");
                String cdImpStr = request.getParameter("impressora");
                int cdImp = Integer.parseInt(cdImpStr);
                ImpressoraCartao imp = ImpressoraCartao.getInstance(cdImp);
                String iateHost = request.getLocalAddr();
                //iateHost = "localhost";
                int iatePort = request.getLocalPort();  
                String iateApp = request.getContextPath();                
                Carteirinha c = new Carteirinha(s, request.getUserPrincipal().getName(), vencimento, iateHost, iatePort, iateApp, imp);
                try{
                    c.emitir(request.getRemoteAddr());
                    response.sendRedirect("c?app=9080&matricula=" + s.getMatricula() + "&seqDependente=" + s.getSeqDependente() + "&idCategoria=" + s.getIdCategoria() + "&acao=showForm");
                }catch(AtualizarCarteirinhaException e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }else if("excluirFoto".equals(acao)){
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "9082", request.getRemoteAddr());
                s.excluirFoto(audit);
                //reload na pagina
                response.sendRedirect("c?app=9080&matricula=" + s.getMatricula() + "&seqDependente=" + s.getSeqDependente() + "&idCategoria=" + s.getIdCategoria() + "&acao=showForm");
            }

        }
        
        //LISTA DE SETORES  
        else if("1030".equals(app)){
            request.setAttribute("setores", Setor.listar());
            request.getRequestDispatcher("/pages/1030-lista.jsp").forward(request, response);
        }
        //INSERIR SETOR
        else if("1031".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1030.jsp").forward(request, response);
            }else{
                Setor d = new Setor();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1030");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR SETOR
        else if("1032".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idSetor = Integer.parseInt(request.getParameter("idSetor"));
                Setor d = Setor.getInstance(idSetor);
                request.setAttribute("setor", d);
                request.getRequestDispatcher("/pages/1030.jsp").forward(request, response);
            }else{
                int idSetor = Integer.parseInt(request.getParameter("idSetor"));
                Setor d = Setor.getInstance(idSetor);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=1030&acao=detalhar&idSetor=" + idSetor);
            }
        }

        //EXCLUIR SETOR
        else if("1033".equals(app)){
            int idSetor = Integer.parseInt(request.getParameter("idSetor"));
            Setor d = Setor.getInstance(idSetor);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1030");
        }

        //LISTA DE modalidades	
        else if("3010".equals(app)){
            request.setAttribute("modalidades", Modalidade.listar());
            request.getRequestDispatcher("/pages/3010-lista.jsp").forward(request, response);
        }
        //INSERIR Modalidade
        else if("3011".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/3010.jsp").forward(request, response);
            }else{
                Modalidade d = new Modalidade();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                	response.sendRedirect("c?app=3010");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR Modalidade
        else if("3012".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idModalidade = Integer.parseInt(request.getParameter("idModalidade"));
                Modalidade d = Modalidade.getInstance(idModalidade);
                request.setAttribute("modalidade", d);
                request.getRequestDispatcher("/pages/3010.jsp").forward(request, response);
            }else{
                int idModalidade = Integer.parseInt(request.getParameter("idModalidade"));
                Modalidade d = Modalidade.getInstance(idModalidade);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=3010&acao=detalhar&idModalidade=" + idModalidade);
            }
        }

        //EXCLUIR Modalidade
        else if("3013".equals(app)){
            int idModalidade = Integer.parseInt(request.getParameter("idModalidade"));
            Modalidade d = Modalidade.getInstance(idModalidade);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=3010");
        }

        //LISTA DE profissoes	
        else if("1110".equals(app)){
            request.setAttribute("profissoes", Profissao.listar());
            request.getRequestDispatcher("/pages/1110-lista.jsp").forward(request, response);
        }
        //INSERIR Profissao
        else if("1111".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1110.jsp").forward(request, response);
            }else{
                Profissao d = new Profissao();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1110");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR Profissao
        else if("1112".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idProfissao = Integer.parseInt(request.getParameter("idProfissao"));
                Profissao d = Profissao.getInstance(idProfissao);
                request.setAttribute("profissao", d);
                request.getRequestDispatcher("/pages/1110.jsp").forward(request, response);
            }else{
                int idProfissao = Integer.parseInt(request.getParameter("idProfissao"));
                Profissao d = Profissao.getInstance(idProfissao);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=1110&acao=detalhar&idProfissao=" + idProfissao);
            }
        }

        //EXCLUIR Profissao
        else if("1113".equals(app)){
            int idProfissao = Integer.parseInt(request.getParameter("idProfissao"));
            Profissao d = Profissao.getInstance(idProfissao);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1110");
        }

        //LISTA DE tipodependentes	
        else if("1020".equals(app)){
            request.setAttribute("tipodependentes", TipoDependente.listar());
            request.getRequestDispatcher("/pages/1020-lista.jsp").forward(request, response);
        }
        //INSERIR TipoDependente
        else if("1021".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1020.jsp").forward(request, response);
            }else{
                TipoDependente d = new TipoDependente();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1020");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR TipoDependente
        else if("1022".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idTipoDependente = Integer.parseInt(request.getParameter("idTipoDependente"));
                TipoDependente d = TipoDependente.getInstance(idTipoDependente);
                request.setAttribute("tipodependente", d);
                request.getRequestDispatcher("/pages/1020.jsp").forward(request, response);
            }else{
                int idTipoDependente = Integer.parseInt(request.getParameter("idTipoDependente"));
                TipoDependente d = TipoDependente.getInstance(idTipoDependente);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=1020&acao=detalhar&idTipoDependente=" + idTipoDependente);
            }
        }

        //EXCLUIR TipoDependente
        else if("1023".equals(app)){
            int idTipoDependente = Integer.parseInt(request.getParameter("idTipoDependente"));
            TipoDependente d = TipoDependente.getInstance(idTipoDependente);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1020");
        }


        //LISTA DE convenios	
        else if("1210".equals(app)){
            request.setAttribute("convenios", Convenio.listar());
            request.getRequestDispatcher("/pages/1210-lista.jsp").forward(request, response);
        }
        //INSERIR Convenio
        else if("1211".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1210.jsp").forward(request, response);
            }else{
                Convenio d = new Convenio();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1210");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR Convenio
        else if("1212".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idConvenio = Integer.parseInt(request.getParameter("idConvenio"));
                Convenio d = Convenio.getInstance(idConvenio);
                request.setAttribute("convenio", d);
                request.getRequestDispatcher("/pages/1210.jsp").forward(request, response);
            }else{
                int idConvenio = Integer.parseInt(request.getParameter("idConvenio"));
                Convenio d = Convenio.getInstance(idConvenio);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=1210&acao=detalhar&idConvenio=" + idConvenio);
            }
        }

        //EXCLUIR Convenio
        else if("1213".equals(app)){
            int idConvenio = Integer.parseInt(request.getParameter("idConvenio"));
            Convenio d = Convenio.getInstance(idConvenio);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1210");
        }


        //LISTA DE TipoEventos	
        else if("1450".equals(app)){
            request.setAttribute("TipoEventos", TipoEvento.listar());
            request.getRequestDispatcher("/pages/1450-lista.jsp").forward(request, response);
        }
        //INSERIR TipoEvento
        else if("1451".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1450.jsp").forward(request, response);
            }else{
                TipoEvento d = new TipoEvento();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1450");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
        }

        //ALTERAR TipoEvento
        else if("1452".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idTipoEvento = Integer.parseInt(request.getParameter("idTipoEvento"));
                TipoEvento d = TipoEvento.getInstance(idTipoEvento);
                request.setAttribute("tipoevento", d);
                request.getRequestDispatcher("/pages/1450.jsp").forward(request, response);
            }else{
                int idTipoEvento = Integer.parseInt(request.getParameter("idTipoEvento"));
                TipoEvento d = TipoEvento.getInstance(idTipoEvento);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=1450&acao=detalhar&idTipoEvento=" + idTipoEvento);
            }
        }

        //EXCLUIR TipoEvento
        else if("1453".equals(app)){
            int idTipoEvento = Integer.parseInt(request.getParameter("idTipoEvento"));
            TipoEvento d = TipoEvento.getInstance(idTipoEvento);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1450");
        }

        //LISTA DE Cargos	
        else if("1040".equals(app)){
            request.setAttribute("Cargos", Cargo.listar());
            request.getRequestDispatcher("/pages/1040-lista.jsp").forward(request, response);
        }
        //INSERIR Cargo
        else if("1041".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1040.jsp").forward(request, response);
            }else{
                Cargo d = new Cargo();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1040");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR Cargo
        else if("1042".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idCargo = Integer.parseInt(request.getParameter("idCargo"));
                Cargo d = Cargo.getInstance(idCargo);
                request.setAttribute("cargo", d);
                request.getRequestDispatcher("/pages/1040.jsp").forward(request, response);
            }else{
                int idCargo = Integer.parseInt(request.getParameter("idCargo"));
                Cargo d = Cargo.getInstance(idCargo);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=1040&acao=detalhar&idCargo=" + idCargo);
            }
        }

        //EXCLUIR Cargo
        else if("1043".equals(app)){
            int idCargo = Integer.parseInt(request.getParameter("idCargo"));
            Cargo d = Cargo.getInstance(idCargo);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1040");
        }
        //LISTA DE ListaNegras	
        else if("2140".equals(app)){
            request.setAttribute("ListaNegras", ListaNegra.listar());
            request.getRequestDispatcher("/pages/2140-lista.jsp").forward(request, response);
        }
        //INSERIR ListaNegra
        else if("2141".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/2140.jsp").forward(request, response);
            }else{
                ListaNegra d = new ListaNegra();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2140");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR ListaNegra
        else if("2142".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idListaNegra = Integer.parseInt(request.getParameter("idListaNegra"));
                ListaNegra d = ListaNegra.getInstance(idListaNegra);
                request.setAttribute("listanegra", d);
                request.getRequestDispatcher("/pages/2140.jsp").forward(request, response);
            }else{
                int idListaNegra = Integer.parseInt(request.getParameter("idListaNegra"));
                ListaNegra d = ListaNegra.getInstance(idListaNegra);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=2140&acao=detalhar&idListaNegra=" + idListaNegra);
            }
        }

        //EXCLUIR ListaNegra
        else if("2143".equals(app)){
            int idListaNegra = Integer.parseInt(request.getParameter("idListaNegra"));
            ListaNegra d = ListaNegra.getInstance(idListaNegra);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=2140");
        }


        //LISTA DE marcaCarros	
        else if("2160".equals(app)){
            request.setAttribute("marcaCarros", MarcaCarro.listar());
            request.getRequestDispatcher("/pages/2160-lista.jsp").forward(request, response);
        }
        //INSERIR MarcaCarro
        else if("2161".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/2160.jsp").forward(request, response);
            }else{
                MarcaCarro d = new MarcaCarro();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2160");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR MarcaCarro
        else if("2162".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idMarcaCarro = Integer.parseInt(request.getParameter("idMarcaCarro"));
                MarcaCarro d = MarcaCarro.getInstance(idMarcaCarro);
                request.setAttribute("marcacarro", d);
                request.getRequestDispatcher("/pages/2160.jsp").forward(request, response);
            }else{
                int idMarcaCarro = Integer.parseInt(request.getParameter("idMarcaCarro"));
                MarcaCarro d = MarcaCarro.getInstance(idMarcaCarro);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=2160&acao=detalhar&idMarcaCarro=" + idMarcaCarro);
            }
        }

        //EXCLUIR MarcaCarro
        else if("2163".equals(app)){
            int idMarcaCarro = Integer.parseInt(request.getParameter("idMarcaCarro"));
            MarcaCarro d = MarcaCarro.getInstance(idMarcaCarro);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=2160");
        }

        //LISTA DE cores	
        else if("2170".equals(app)){
            request.setAttribute("cores", Cor.listar());
            request.getRequestDispatcher("/pages/2170-lista.jsp").forward(request, response);
        }
        //INSERIR Cor
        else if("2171".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/2170.jsp").forward(request, response);
            }else{
                Cor d = new Cor();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                	response.sendRedirect("c?app=2170");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
        }

        //ALTERAR Cor
        else if("2172".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idCor = Integer.parseInt(request.getParameter("idCor"));
                Cor d = Cor.getInstance(idCor);
                request.setAttribute("cor", d);
                request.getRequestDispatcher("/pages/2170.jsp").forward(request, response);
            }else{
                int idCor = Integer.parseInt(request.getParameter("idCor"));
                Cor d = Cor.getInstance(idCor);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=2170&acao=detalhar&idCor=" + idCor);
            }
        }

        //EXCLUIR Cor
        else if("2173".equals(app)){
            int idCor = Integer.parseInt(request.getParameter("idCor"));
            Cor d = Cor.getInstance(idCor);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=2170");
        }

        //LISTA DE modalidadeesportivas	
        else if("1100".equals(app)){
            if("Participantes".equals(acao)){
                request.setAttribute("app", app);
                int idModalidadeEsportiva = Integer.parseInt(request.getParameter("idModalidadeEsportiva"));
                request.setAttribute("participantemodalidades", ParticipanteModalidade.listar(idModalidadeEsportiva));
                request.getRequestDispatcher("/pages/1100-participantes.jsp").forward(request, response);
            }else{
                request.setAttribute("modalidadeesportivas", ModalidadeEsportiva.listar());
                request.getRequestDispatcher("/pages/1100-lista.jsp").forward(request, response);
            }
        }
        
        //INSERIR ModalidadeEsportiva
        else if("1101".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1100.jsp").forward(request, response);
            }else{
                ModalidadeEsportiva d = new ModalidadeEsportiva();
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1100");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR ModalidadeEsportiva
        else if("1102".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idModalidadeEsportiva = Integer.parseInt(request.getParameter("idModalidadeEsportiva"));
                ModalidadeEsportiva d = ModalidadeEsportiva.getInstance(idModalidadeEsportiva);
                request.setAttribute("modalidadeesportiva", d);
                request.getRequestDispatcher("/pages/1100.jsp").forward(request, response);
            }else{
                int idModalidadeEsportiva = Integer.parseInt(request.getParameter("idModalidadeEsportiva"));
                ModalidadeEsportiva d = ModalidadeEsportiva.getInstance(idModalidadeEsportiva);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=1100&acao=detalhar&idModalidadeEsportiva=" + idModalidadeEsportiva);
            }
        }

        //EXCLUIR ModalidadeEsportiva
        else if("1103".equals(app)){
            int idModalidadeEsportiva = Integer.parseInt(request.getParameter("idModalidadeEsportiva"));
            ModalidadeEsportiva d = ModalidadeEsportiva.getInstance(idModalidadeEsportiva);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1100");
        }


        //LISTA DE tipovagabarcos	
        else if("1760".equals(app)){
            request.setAttribute("tipovagabarcos", TipoVagaBarco.listar());
            request.getRequestDispatcher("/pages/1760-lista.jsp").forward(request, response);
        }
        //INSERIR TipoVagaBarco
        else if("1761".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1760.jsp").forward(request, response);
            }else{
                TipoVagaBarco d = new TipoVagaBarco();
            	d.setDescricao(request.getParameter("descricao"));
                d.setId(request.getParameter("id"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1760");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR TipoVagaBarco
        else if("1762".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                String idTipoVagaBarco = request.getParameter("idTipoVagaBarco");
                TipoVagaBarco d = TipoVagaBarco.getInstance(idTipoVagaBarco);
                request.setAttribute("tipovagabarco", d);
                request.getRequestDispatcher("/pages/1760.jsp").forward(request, response);
            }else{
                String idTipoVagaBarco = request.getParameter("idTipoVagaBarco");
                TipoVagaBarco d = TipoVagaBarco.getInstance(idTipoVagaBarco);
            	d.setDescricao(request.getParameter("descricao"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=1760&acao=detalhar&idTipoVagaBarco=" + idTipoVagaBarco);
            }
        }

        //EXCLUIR TipoVagaBarco
        else if("1763".equals(app)){
            String idTipoVagaBarco = request.getParameter("idTipoVagaBarco");
            TipoVagaBarco d = TipoVagaBarco.getInstance(idTipoVagaBarco);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1760");
        }

        //LISTA DE CategoriaNauticas	
        else if("1130".equals(app)){
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
        

        //LISTA DE TipoOcorrencias	
        else if("2010".equals(app)){
            request.setAttribute("TipoOcorrencias", TipoOcorrencia.listar());
            request.getRequestDispatcher("/pages/2010-lista.jsp").forward(request, response);
        }
        //INSERIR TipoOcorrencia
        else if("2011".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/2010.jsp").forward(request, response);
            }else{
                TipoOcorrencia d = new TipoOcorrencia();
            	d.setDescricao(request.getParameter("descricao"));
                d.setTipo(request.getParameter("tipo"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2010");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR TipoOcorrencia
        else if("2012".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idTipoOcorrencia = Integer.parseInt(request.getParameter("idTipoOcorrencia"));
                TipoOcorrencia d = TipoOcorrencia.getInstance(idTipoOcorrencia);
                request.setAttribute("tipoocorrencia", d);
                request.getRequestDispatcher("/pages/2010.jsp").forward(request, response);
            }else{
                int idTipoOcorrencia = Integer.parseInt(request.getParameter("idTipoOcorrencia"));
                TipoOcorrencia d = TipoOcorrencia.getInstance(idTipoOcorrencia);
            	d.setDescricao(request.getParameter("descricao"));
                d.setTipo(request.getParameter("tipo"));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=2010&acao=detalhar&idTipoOcorrencia=" + idTipoOcorrencia);
            }
        }

        //EXCLUIR TipoOcorrencia
        else if("2013".equals(app)){
            int idTipoOcorrencia = Integer.parseInt(request.getParameter("idTipoOcorrencia"));
            TipoOcorrencia d = TipoOcorrencia.getInstance(idTipoOcorrencia);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=2010");
        }

        //LISTA DE modelocarros	
        else if("2180".equals(app)){
            request.setAttribute("modeloCarros", ModeloCarro.listar());
            request.getRequestDispatcher("/pages/2180-lista.jsp").forward(request, response);
        }
        //INSERIR ModeloCarro
        else if("2181".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.setAttribute("marcas", ComboBoxLoader.listar("TB_MARCA_CARRO"));
                request.getRequestDispatcher("/pages/2180.jsp").forward(request, response);
            }else{
                ModeloCarro d = new ModeloCarro();
            	d.setDescricao(request.getParameter("descricao"));
                MarcaCarro m = MarcaCarro.getInstance(Integer.parseInt(request.getParameter("idMarca")));
                d.setMarca(m);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2180");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR ModeloCarro
        else if("2182".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                int idModeloCarro = Integer.parseInt(request.getParameter("idModeloCarro"));
                ModeloCarro d = ModeloCarro.getInstance(idModeloCarro);
                request.setAttribute("modeloCarro", d);
                request.setAttribute("marcas", ComboBoxLoader.listar("TB_MARCA_CARRO"));
                request.getRequestDispatcher("/pages/2180.jsp").forward(request, response);
            }else{
                int idModeloCarro = Integer.parseInt(request.getParameter("idModeloCarro"));
                ModeloCarro d = ModeloCarro.getInstance(idModeloCarro);
            	d.setDescricao(request.getParameter("descricao"));
                MarcaCarro m = MarcaCarro.getInstance(Integer.parseInt(request.getParameter("idMarca")));
                d.setMarca(m);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=2180&acao=detalhar&idModeloCarro=" + idModeloCarro);
            }
        }

        //EXCLUIR ModeloCarro
        else if("2183".equals(app)){
            int idModeloCarro = Integer.parseInt(request.getParameter("idModeloCarro"));
            ModeloCarro d = ModeloCarro.getInstance(idModeloCarro);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=2180");
        }


        //LISTA DE crachavisitantes	
        else if("1260".equals(app)){
            request.setAttribute("crachavisitantes", CrachaVisitante.listar());
            request.getRequestDispatcher("/pages/1260-lista.jsp").forward(request, response);
        }
        //INSERIR CrachaVisitante
        else if("1261".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.setAttribute("setores", ComboBoxLoader.listar("TB_SETOR"));
                request.getRequestDispatcher("/pages/1260.jsp").forward(request, response);
            }else{
                CrachaVisitante d = new CrachaVisitante();
            	d.setSetor(Integer.parseInt(request.getParameter("idSetor")));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1260");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //EXCLUIR CrachaVisitante
        else if("1262".equals(app)){
            int idCrachaVisitante = Integer.parseInt(request.getParameter("idCrachaVisitante"));
            CrachaVisitante d = CrachaVisitante.getInstance(idCrachaVisitante);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1260");
        }
        
        //IMPRIMIR CrachaVisitante
        else if("1263".equals(app)){
            request.setAttribute("app", app);
            int idCrachaVisitante = Integer.parseInt(request.getParameter("idCrachaVisitante"));
            CrachaVisitante d = CrachaVisitante.getInstance(idCrachaVisitante);
            request.setAttribute("crachavisitante", d);
            request.getRequestDispatcher("/pages/1260-imprimir.jsp").forward(request, response);
        }        

       //GERACAO DE CARNE
        else if("1500".equals(app)){
            if("gera".equals(acao)){
                GeraCarne d = new GeraCarne();
                
                d.setDataReferencia(Datas.parse(request.getParameter("dataRef")));
                d.setTipo(request.getParameter("tipo"));
                
                if ("M".equals(request.getParameter("tipo"))){
                    //parametros do carne MENSAL
                    try{
                        d.setMatriculaInicial(Integer.parseInt(request.getParameter("MatIni")));
                    }catch(NumberFormatException e){
                        d.setMatriculaInicial(0);
                    }                
                    try{
                        d.setMatriculaFinal(Integer.parseInt(request.getParameter("MatFim")));
                    }catch(NumberFormatException e){
                        d.setMatriculaFinal(0);
                    }                


                    String[] categorias = request.getParameterValues("pCategoria");
                    for(String s : categorias){
                            int i = Integer.parseInt(s);
                            d.setCategorias(d.getCategorias() + Integer.toString(i) + ",");
                    }                
                    
                }else{
                    //parametros do carne AVULSO
                    try{
                        d.setMatriculaInicial(Integer.parseInt(request.getParameter("matricula")));
                        d.setMatriculaFinal(Integer.parseInt(request.getParameter("matricula")));
                    }catch(NumberFormatException e){
                        d.setMatriculaInicial(0);
                        d.setMatriculaFinal(0);
                    }
                    try{
                        d.setCategorias(Integer.parseInt(request.getParameter("categoria")) + ",");
                    }catch(NumberFormatException e){
                        d.setCategorias(",");
                    }
                }
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.gerar(request.getRemoteAddr(), audit);
                    request.setAttribute("geraCarne", d);
                    request.setAttribute("app", app);

                    request.getRequestDispatcher("/pages/1500-resultado.jsp").forward(request, response);
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
 
            }else if("avulso".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1500-avulso.jsp").forward(request, response);

            }else{
                request.setAttribute("app", app);
                request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
                request.getRequestDispatcher("/pages/1500.jsp").forward(request, response);
                }
        }       
        
        //LISTA DE CARROS DO SOCIO
        else if("2190".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));                            
            Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);
            request.setAttribute("socio", s);
            request.setAttribute("carros", CarroSocio.listar(s));
            request.getRequestDispatcher("/pages/2190-lista.jsp").forward(request, response);
        }
        
        //INSERIR CARRO SOCIO
        else if("2191".equals(app)){          
            if("showForm".equals(acao)){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));                            
                Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);
                CarroSocio c = new CarroSocio();
                c.setSocio(s);
                request.setAttribute("carro", c);
                request.setAttribute("app", app);
                request.setAttribute("modelos", ComboBoxLoader.listar("VW_MODELO_MARCA"));
                request.setAttribute("cores", ComboBoxLoader.listar("TB_COR"));
                request.getRequestDispatcher("/pages/2190.jsp").forward(request, response);
            }else{
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));                                                        
                CarroSocio c = new CarroSocio();
                Cor cor = Cor.getInstance(Integer.parseInt(request.getParameter("idCor")));
            	c.setCor(cor);
                c.setPlaca(request.getParameter("placa"));
                ModeloCarro m = ModeloCarro.getInstance(Integer.parseInt(request.getParameter("idModelo")));
                c.setModelo(m);
                Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);
                c.setSocio(s);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    c.inserir(audit);
                    response.sendRedirect("c?app=2190&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR CARRO SOCIO
        else if("2192".equals(app)){
            int idCarro = Integer.parseInt(request.getParameter("idCarro"));
            CarroSocio c = CarroSocio.getInstance(idCarro);
            
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.setAttribute("modelos", ComboBoxLoader.listar("VW_MODELO_MARCA"));
                request.setAttribute("cores", ComboBoxLoader.listar("TB_COR"));
                
                request.setAttribute("carro", c);
                request.getRequestDispatcher("/pages/2190.jsp").forward(request, response);
            }else{
                Cor cor = Cor.getInstance(Integer.parseInt(request.getParameter("idCor")));
            	c.setCor(cor);
                c.setPlaca(request.getParameter("placa"));
                ModeloCarro m = ModeloCarro.getInstance(Integer.parseInt(request.getParameter("idModelo")));                
                c.setModelo(m);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                c.alterar(audit);

            	response.sendRedirect("c?app=2190&acao=detalhar&idCarro=" + idCarro + "&matricula=" + c.getSocio().getMatricula() + "&seqDependente=" + c.getSocio().getSeqDependente() + "&idCategoria=" + c.getSocio().getIdCategoria());
            }
        }

        //DOCUMENTO CARRO SOCIO
        else if("2194".equals(app)){
            int idCarro = Integer.parseInt(request.getParameter("idCarro"));
            CarroSocio c = CarroSocio.getInstance(idCarro);
            
            if("showForm".equals(acao)){
                request.setAttribute("carro", c);
                
                request.getRequestDispatcher("/pages/2194.jsp").forward(request, response);                
            }else if("excluirFoto".equals(acao)){
                
                c.excluirFoto();
                
                //reload na pagina
                request.setAttribute("carro", c);
                response.sendRedirect("c?app=2194&acao=showForm&idCarro=" + c.getId());
            }else if("mostraFoto".equals(acao)){
                
                request.setAttribute("carro", c);
                request.getRequestDispatcher("/pages/2194-foto.jsp").forward(request, response);                
            }
        }
        
        // EXCLUIR CARRO SOCIO
        else if("2193".equals(app)){
            int idCarro = Integer.parseInt(request.getParameter("idCarro"));
            CarroSocio c = CarroSocio.getInstance(idCarro);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            c.excluir(audit);

            response.sendRedirect("c?app=2190&matricula=" + c.getSocio().getMatricula() + "&seqDependente=" + c.getSocio().getSeqDependente() + "&idCategoria=" + c.getSocio().getIdCategoria());
        }
        
        //LISTA DE CARRO GERAL
        else if("2210".equals(app)){
            request.setAttribute("carros", Carro.listar());
            request.getRequestDispatcher("/pages/2210-lista.jsp").forward(request, response);
        }
        
        //INSERIR CARRO GERAL
        else if("2211".equals(app)){           
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.setAttribute("modelos", ComboBoxLoader.listar("VW_MODELO_MARCA"));
                request.setAttribute("cores", ComboBoxLoader.listar("TB_COR"));
                request.getRequestDispatcher("/pages/2210.jsp").forward(request, response);
            }else{
                Carro d = new Carro();
            	d.setDescricao(request.getParameter("descricao"));
                d.setPlaca(request.getParameter("placa"));
                ModeloCarro m = ModeloCarro.getInstance(Integer.parseInt(request.getParameter("idModelo")));
                d.setModelo(m);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=2210");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR CARRO GERAL
        else if("2212".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.setAttribute("modelos", ComboBoxLoader.listar("VW_MODELO_MARCA"));
                request.setAttribute("cores", ComboBoxLoader.listar("TB_COR"));
                int idCarro = Integer.parseInt(request.getParameter("idCarro"));
                Carro d = Carro.getInstance(idCarro);
                request.setAttribute("carro", d);
                request.getRequestDispatcher("/pages/2210.jsp").forward(request, response);
            }else{
                int idCarro = Integer.parseInt(request.getParameter("idCarro"));
                Carro d = Carro.getInstance(idCarro);
            	d.setDescricao(request.getParameter("descricao"));
                d.setPlaca(request.getParameter("placa"));
                ModeloCarro m = ModeloCarro.getInstance(Integer.parseInt(request.getParameter("idModelo")));                
                d.setModelo(m);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=2210&acao=detalhar&idCarro=" + idCarro);
            }
        }

        //EXCLUIR CARRO GERAL
        else if("2213".equals(app)){
            int idCarro = Integer.parseInt(request.getParameter("idCarro"));
            Carro d = Carro.getInstance(idCarro);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=2210");
        }

        //CADASTRO DE TITULAR PENDENTE DE VALIDACAO
        else if("2260".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria")); 
            TitularPendencia tp = TitularPendencia.getInstance(matricula, idCategoria);
            
            if("showForm".equals(acao)){
                request.setAttribute("titular", tp);
                request.setAttribute("carros", TitularPendenciaCarro.listar(tp));
                request.getRequestDispatcher("/pages/2260.jsp").forward(request, response);
            }else if("atualizarContatos".equals(acao)){
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                
                tp.setEmail(request.getParameter("email"));
                tp.setCelular(request.getParameter("celular"));
                tp.getEnderecoResidencial().setTelefone(request.getParameter("telefoneR"));
                tp.getEnderecoResidencial().setEndereco(request.getParameter("enderecoR"));
                tp.getEnderecoResidencial().setBairro(request.getParameter("bairroR"));
                tp.getEnderecoResidencial().setCidade(request.getParameter("cidadeR"));
                tp.getEnderecoResidencial().setUF(request.getParameter("ufR"));
                tp.getEnderecoResidencial().setCEP(request.getParameter("cepR"));
                tp.getEnderecoComercial().setTelefone(request.getParameter("telefoneC"));
                tp.getEnderecoComercial().setEndereco(request.getParameter("enderecoC"));
                tp.getEnderecoComercial().setBairro(request.getParameter("bairroC"));
                tp.getEnderecoComercial().setCidade(request.getParameter("cidadeC"));
                tp.getEnderecoComercial().setUF(request.getParameter("ufC"));
                tp.getEnderecoComercial().setCEP(request.getParameter("cepC"));
                tp.alterarEnderecos(audit);
                response.sendRedirect("c?app=2260&acao=showForm&matricula="
                        + matricula
                        + "&idCategoria="
                        + idCategoria);                
            }else if("showFormCarro".equals(acao)){
                int idCarro;//se nao tem o idCarro entao eh insercao senao eh atualizacao
                try{
                    idCarro = Integer.parseInt(request.getParameter("idCarro"));
                    request.setAttribute("carro", TitularPendenciaCarro.getInstance(idCarro));
                    request.setAttribute("acao", "atualizarCarro");
                }catch(NumberFormatException e){
                    idCarro = 0;
                    request.setAttribute("acao", "inserirCarro");
                }
                request.setAttribute("titular", tp);
                request.setAttribute("modelos", ComboBoxLoader.listar("VW_MODELO_MARCA"));
                request.setAttribute("cores", ComboBoxLoader.listar("TB_COR"));                

                request.getRequestDispatcher("/pages/2260-carro.jsp").forward(request, response);
            }else if("inserirCarro".equals(acao)){
                TitularPendenciaCarro c = new TitularPendenciaCarro();
                Cor cor = Cor.getInstance(Integer.parseInt(request.getParameter("idCor")));
            	c.setCor(cor);
                c.setPlaca(request.getParameter("placa"));
                ModeloCarro m = ModeloCarro.getInstance(Integer.parseInt(request.getParameter("idModelo")));
                c.setModelo(m);
                c.setTitular(tp);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                c.inserir(audit);
                
                response.sendRedirect("c?app=2260&acao=showForm&matricula="
                        + matricula
                        + "&idCategoria="
                        + idCategoria);                
            }else if("atualizarCarro".equals(acao)){
                int idCarro = Integer.parseInt(request.getParameter("idCarro"));
                TitularPendenciaCarro c = TitularPendenciaCarro.getInstance(idCarro);
                Cor cor = Cor.getInstance(Integer.parseInt(request.getParameter("idCor")));
            	c.setCor(cor);
                c.setPlaca(request.getParameter("placa"));
                ModeloCarro m = ModeloCarro.getInstance(Integer.parseInt(request.getParameter("idModelo")));
                c.setModelo(m);
                c.setTitular(tp);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                c.alterar(audit);
                
                response.sendRedirect("c?app=2260&acao=showForm&matricula="
                        + matricula
                        + "&idCategoria="
                        + idCategoria);                
            }else if("excluirCarro".equals(acao)){
                int idCarro = Integer.parseInt(request.getParameter("idCarro"));
                TitularPendenciaCarro c = TitularPendenciaCarro.getInstance(idCarro);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                c.excluir(audit);
                
                response.sendRedirect("c?app=2260&acao=showForm&matricula="
                        + matricula
                        + "&idCategoria="
                        + idCategoria);
            }
        }
        
        //CADASTRO DE DEPENDENTE PENDENTE DE VALIDACAO
        else if("2261".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria")); 
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente")); 
            
            DependentePendencia dp = DependentePendencia.getInstance(matricula, idCategoria, seqDependente);
            
            if("showForm".equals(acao)){
                request.setAttribute("dependente", dp);
                request.getRequestDispatcher("/pages/2261.jsp").forward(request, response);
            }else if("atualizar".equals(acao)){
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                
                dp.setEmail(request.getParameter("email"));
                dp.setCelular(request.getParameter("celular"));
                dp.setTelefoneResidencial(request.getParameter("telefoneR"));
                dp.setTelefoneComercial(request.getParameter("telefoneC"));
                dp.alterar(audit);
                response.sendRedirect("c?app=9030");                
            }   
        }
        
        //HISTORICO DE UTILIZACAO DO TITULO
        else if("1540".equals(app)){

            if("atualizarDataAdmissao".equals(acao)){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria")); 
                Titular t = Titular.getInstance(matricula, idCategoria);
                
                t.setDataAdmissao(Datas.parse(request.getParameter("dataAdmissao")));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                t.atualizarDataAdmissao(audit);

                response.sendRedirect("c?app=1540&matricula=" + matricula + "&idCategoria=" + idCategoria + "&showMessage=true");
            }else if("imprimir".equals(acao)){
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria")); 
                DecimalFormat f1 = new DecimalFormat("00");
                DecimalFormat f2 = new DecimalFormat("0000");
                Socio s = Socio.getInstance(matricula, 0, idCategoria);

                
                String sql = "SELECT " +
                        "NO_PESSOA as 'Nome', " +
                        "DT_INIC_UTILIZACAO as 'Dt. Início', " +
                        "DT_FIM_UTILIZACAO as 'Dt. Fim' " +
                   "FROM " +
                        "TB_HIST_UTIL_TITULO " +
                   "WHERE " +
                        "CD_MATRICULA = " + matricula + " AND " +
                        "CD_CATEGORIA = " + idCategoria + " AND " +
                        "SEQ_DEPENDENTE = 0";
                
                request.setAttribute("titulo", "Histórico de Utilização do Título");
                request.setAttribute("titulo2", f1.format(idCategoria) + "/" + f2.format(matricula) + " - " + s.getNome());
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "false");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "-1");
                
                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                        
                
            }else{
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria")); 
                Titular t = Titular.getInstance(matricula, idCategoria);

                if(request.getParameter("showMessage") != null){
                    request.setAttribute("showMessage", "true");
                }
                
                request.setAttribute("titular", t);
                request.setAttribute("historicos", TitularHistorico.listar(t));
                request.getRequestDispatcher("/pages/1540-lista.jsp").forward(request, response);
            }   
        }        

        //INSERIR HISTORICO DE UTILIZACAO DO TITULO
        else if("1541".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria")); 
            Titular t = Titular.getInstance(matricula, idCategoria);
                
            if("showForm".equals(acao)){
                request.setAttribute("titular", t);
                request.setAttribute("app", "1541");
                
                request.getRequestDispatcher("/pages/1540.jsp").forward(request, response);
            }else{
                TitularHistorico h = new TitularHistorico();
                h.setTitular(t);
                h.setNome(request.getParameter("nome"));
                h.setDataInicio(Datas.parse(request.getParameter("dataInicio")));
                h.setDataFim(Datas.parse(request.getParameter("dataFim")));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                h.inserir(audit);

            	response.sendRedirect("c?app=1540&matricula=" + matricula + "&idCategoria=" + idCategoria);
            }
        }

        //ALTERAR HISTORICO DE UTILIZACAO DO TITULO
        else if("1542".equals(app)){
            int seqUtilizacao = Integer.parseInt(request.getParameter("seqUtilizacao")); 
            TitularHistorico h = TitularHistorico.getInstance(seqUtilizacao);
            
            if("showForm".equals(acao)){
                request.setAttribute("titular", h.getTitular());
                request.setAttribute("historico", h);
                request.setAttribute("app", "1542");
                
                request.getRequestDispatcher("/pages/1540.jsp").forward(request, response);                
            }else{
                h.setNome(request.getParameter("nome"));
                h.setDataInicio(Datas.parse(request.getParameter("dataInicio")));
                h.setDataFim(Datas.parse(request.getParameter("dataFim")));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                h.alterar(audit);

            	response.sendRedirect("c?app=1540&matricula=" + h.getTitular().getSocio().getMatricula()
                        + "&idCategoria=" + h.getTitular().getSocio().getIdCategoria());
            }
        }

        //EXCLUIR HISTORICO DE UTILIZACAO DO TITULO
        else if("1543".equals(app)){
            int seqUtilizacao = Integer.parseInt(request.getParameter("seqUtilizacao")); 
            TitularHistorico h = TitularHistorico.getInstance(seqUtilizacao);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            h.excluir(audit);

            response.sendRedirect("c?app=1540&matricula=" + h.getTitular().getSocio().getMatricula()
                    + "&idCategoria=" + h.getTitular().getSocio().getIdCategoria());
        }
        
        //AUTORIZACAO PARA ACESSO PELA INTERNET
        else if("1880".equals(app)){
            if("imprimir".equals(acao)){
                request.setAttribute("usuario", request.getParameter("usuario"));
                request.setAttribute("senha", request.getParameter("senha"));
                request.getRequestDispatcher("/pages/1880-impressao.jsp").forward(request, response);
            }else{
                int matricula = Integer.parseInt(request.getParameter("matricula"));
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria")); 
                int seqDependente = Integer.parseInt(request.getParameter("seqDependente")); 

                Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);

                if("showForm".equals(acao)){
                    AutorizacaoInternet a = AutorizacaoInternet.getInstance(s);
                    request.setAttribute("autorizacao", a);
                    request.setAttribute("aplicacoes", ComboBoxLoader.listar("TB_APLICACAO_INTERNET"));
                    request.getRequestDispatcher("/pages/1880.jsp").forward(request, response);
                }else if("atualizar".equals(acao)){
                    AutorizacaoInternet a = new AutorizacaoInternet();
                    Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                    a.setSocio(s);
                    if(request.getParameterValues("idAplicacao") != null){
                        for(String x : request.getParameterValues("idAplicacao")){
                            a.getAplicacoesInternet().put(Integer.parseInt(x), x);
                        }
                    }    
                    a.setSenha(request.getParameter("senha"));
                    if(request.getParameter("autorizar") != null){
                        a.setAutorizado(true);
                    }else{
                        a.setAutorizado(false);
                    }
                    a.atualizar(audit);

                    response.sendRedirect("c?app=1880&acao=showForm&matricula="
                            + matricula
                            + "&idCategoria="
                            + idCategoria
                            + "&seqDependente="
                            + seqDependente);
                }   
            }   
        }

        //LISTA DE cursos	
        else if("3020".equals(app)){
            request.setAttribute("cursos", Curso.listar());
            request.getRequestDispatcher("/pages/3020-lista.jsp").forward(request, response);
        }
        //INSERIR Curso
        else if("3021".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.setAttribute("taxasAdministrativas", ComboBoxLoader.listar("VW_TAXA_ADMINISTRATIVA"));
                request.setAttribute("modalidades", ComboBoxLoader.listar("TB_MODALIDADE_CURSO"));
                request.getRequestDispatcher("/pages/3020.jsp").forward(request, response);
            }else{
                Curso d = new Curso();
            	d.setDescricao(request.getParameter("descricao"));
                d.setCdTxAdministrativa(Integer.parseInt(request.getParameter("cdTxAdministrativa")));
                d.setCdModalidade(Integer.parseInt(request.getParameter("cdModalidade")));
                if(request.getParameter("situacao").equals("N")){
                    d.setSituacao("N");
                }else{
                    d.setSituacao("C");
                }
                if(request.getParameter("tpDesconto").equals("S")){
                    d.setTpDesconto("S");
                }else{
                    d.setTpDesconto("");
                }
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=3020");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR Curso
        else if("3022".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.setAttribute("taxasAdministrativas", ComboBoxLoader.listar("VW_TAXA_ADMINISTRATIVA"));
                request.setAttribute("modalidades", ComboBoxLoader.listar("TB_MODALIDADE_CURSO"));

                int idCurso = Integer.parseInt(request.getParameter("idCurso"));
                Curso d = Curso.getInstance(idCurso);
                request.setAttribute("curso", d);
                request.getRequestDispatcher("/pages/3020.jsp").forward(request, response);
            }else{
                int idCurso = Integer.parseInt(request.getParameter("idCurso"));
                Curso d = Curso.getInstance(idCurso);
            	d.setDescricao(request.getParameter("descricao"));
                d.setCdTxAdministrativa(Integer.parseInt(request.getParameter("cdTxAdministrativa")));
                d.setCdModalidade(Integer.parseInt(request.getParameter("cdModalidade")));
                if(request.getParameter("situacao").equals("N")){
                    d.setSituacao("N");
                }else{
                    d.setSituacao("C");
                }
                if(request.getParameter("tpDesconto").equals("S")){
                    d.setTpDesconto("S");
                }else{
                    d.setTpDesconto("");
                }
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=3020&acao=detalhar&idCurso=" + idCurso);
            }
        }

        //EXCLUIR Curso
        else if("3023".equals(app)){
            int idCurso = Integer.parseInt(request.getParameter("idCurso"));
            Curso d = Curso.getInstance(idCurso);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=3020");
        }

        
        //LISTA Item de Aluguel
        else if("1390".equals(app)) {
            request.setAttribute("lista", ItemAluguel.listar());
            request.getRequestDispatcher("/pages/1390-lista.jsp").forward(request, response);
        }
        
        //INSERIR Item de Aluguel
        else if("1391".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);

                request.getRequestDispatcher("/pages/1390.jsp").forward(request, response);
            }else{
                ItemAluguel d = new ItemAluguel();
            	d.setDescricao(request.getParameter("descricao"));
                if(request.getParameter("qtEstoque") == "" ){
                    d.setQuantidadeEstoque(0);
                }else{
                    d.setQuantidadeEstoque(Integer.parseInt(request.getParameter("qtEstoque")));
                }
                if(request.getParameter("valNaoSocio") == "" ){
                    d.setValorNaoSocio(new BigDecimal(0));
                }else{
                    d.setValorNaoSocio(new BigDecimal(request.getParameter("valNaoSocio").replaceAll(",", ".") ));
                }
                if(request.getParameter("valSocio") == "" ){
                    d.setValorSocio(new BigDecimal(0));
                }else{
                    d.setValorSocio(new BigDecimal(request.getParameter("valSocio").replaceAll(",", ".") ));
                }
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1390");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //ALTERAR Item de Aluguel
        else if("1392".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                
                int id = Integer.parseInt(request.getParameter("id"));
                ItemAluguel d = ItemAluguel.getInstance(id);
                request.setAttribute("itemAluguel", d);
                request.getRequestDispatcher("/pages/1390.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                ItemAluguel d = ItemAluguel.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                if(request.getParameter("qtEstoque") == "" ){
                    d.setQuantidadeEstoque(0);
                }else{
                    d.setQuantidadeEstoque(Integer.parseInt(request.getParameter("qtEstoque")));
                }
                if(request.getParameter("valNaoSocio") == "" ){
                    d.setValorNaoSocio(new BigDecimal(0));
                }else{
                    d.setValorNaoSocio(new BigDecimal(request.getParameter("valNaoSocio").replaceAll(",", ".") ));
                }
                if(request.getParameter("valSocio") == "" ){
                    d.setValorSocio(new BigDecimal(0));
                }else{
                    d.setValorSocio(new BigDecimal(request.getParameter("valSocio").replaceAll(",", ".") ));
                }
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());

                d.alterar(audit);

            	response.sendRedirect("c?app=1390");
            }
            
        }

        
        //EXCLUIR Item de Aluguel
        else if("1393".equals(app)) {
            int id = Integer.parseInt(request.getParameter("id"));
            ItemAluguel i = ItemAluguel.getInstance(id);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            i.excluir(audit);

            response.sendRedirect("c?app=1390");
        }

        //LISTA DE feriados	
        else if("1950".equals(app)){
            request.setAttribute("feriados", Feriado.listar());
            request.getRequestDispatcher("/pages/1950-lista.jsp").forward(request, response);
        }
        //INSERIR Feriado
        else if("1951".equals(app)){
            if("showForm".equals(acao)){
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1950.jsp").forward(request, response);
            }else{
                Feriado d = new Feriado();
            	d.setData(Datas.parse(request.getParameter("data")));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1950");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }

            }
        }

        //EXCLUIR Feriado
        else if("1952".equals(app)){

            Date data = Datas.parse(request.getParameter("data"));
            Feriado d = Feriado.getInstance(data);
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=1950");
        }        

       //BAIXA MANUAL DE CARNE
        else if("1720".equals(app)){
            if("valida".equals(acao)){
                request.setAttribute("app", app);
                
                BaixaManualCarne d = new BaixaManualCarne();
                d.setUserTesoureiro(request.getParameter("userTesoureiro"));
                d.setSenhaTesoureiro(request.getParameter("SenhaTesoureiro"));
                d.setUserDiretor(request.getParameter("userDiretor"));
                d.setSenhaDiretor(request.getParameter("SenhaDiretor"));
                if (request.getParameter("qtBaixas") != ""){
                    d.setQtBaixa(Integer.parseInt(request.getParameter("qtBaixas")));
                }
                
                
                try{
                    d.Autenticar();
                    
                    request.getSession().setAttribute("bmc", d);
                    request.setAttribute("bmc", d);
                    String retorno = d.getMsgAutenticado();

                    if (retorno.equals("OK")) {
                        response.sendRedirect("c?app=1720&acao=selecionacarne");
                    }else{
                        request.setAttribute("msg", retorno);
                        request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                    }
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
 
                
            }else if("selecionacarne".equals(acao)){
                BaixaManualCarne t = (BaixaManualCarne)request.getSession().getAttribute("bmc");
                if (t == null){
                    request.getRequestDispatcher("/pages/1720.jsp").forward(request, response);
                }else if (t.getMsgAutenticado().equals("OK")){
                    request.getRequestDispatcher("/pages/1720-seleciona-carne.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher("/pages/1720.jsp").forward(request, response);
                }
            }else if("validacarne".equals(acao)){
                BaixaManualCarne t = (BaixaManualCarne)request.getSession().getAttribute("bmc");
                BaixaManualCarne d = new BaixaManualCarne();
                if (request.getParameter("seqCarne") == ""){
                    d.setSeqCarne(0);
                }else{
                    d.setSeqCarne(Integer.parseInt(request.getParameter("seqCarne")));
                }
                if (request.getParameter("matricula") == ""){
                    d.setMatricula(0);
                }else{
                    d.setMatricula(Integer.parseInt(request.getParameter("matricula")));
                }
                if (request.getParameter("categoria") == ""){
                    d.setCategoria(0);
                }else{
                    d.setCategoria(Integer.parseInt(request.getParameter("categoria")));
                }
                if (request.getParameter("dtVenc") == ""){
                    d.setDataVenc(Datas.parse("01/01/1900"));
                }else{
                    d.setDataVenc(Datas.parse(request.getParameter("dtVenc")));
                }
                d.validaCarne();
                d.setMsgAutenticado("OK");
                d.setQtBaixa(t.getQtBaixa());
                request.getSession().setAttribute("bmc", d);
                BigDecimal carne = d.getValor();
                if (carne == BigDecimal.valueOf(0)) {
                    response.sendRedirect("c?app=1720&acao=selecionacarne");
                }else{
                    response.sendRedirect("c?app=1720&acao=baixaCarne");
                }
            }else if("baixaCarne".equals(acao)){
                BaixaManualCarne t = (BaixaManualCarne)request.getSession().getAttribute("bmc");
                if (t == null){
                    request.getRequestDispatcher("/pages/1720.jsp").forward(request, response);
                }else if (t.getValor() == BigDecimal.valueOf(0)){
                    request.getRequestDispatcher("/pages/1720.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher("/pages/1720-baixa-carne.jsp").forward(request, response);
                }
            }else if("atualizabaixacarne".equals(acao)){
                BaixaManualCarne t = (BaixaManualCarne)request.getSession().getAttribute("bmc");
                BaixaManualCarne d = new BaixaManualCarne();
                
                d.setSeqCarne(t.getSeqCarne());
                d.setMatricula(t.getMatricula());
                d.setCategoria(t.getCategoria());
                d.setDataVenc(t.getDataVenc());
                d.setValor(t.getValor());
                d.setQtBaixa(t.getQtBaixa());

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                
                if (request.getParameter("anulaCarne") != null){
                    d.anulaCarne(audit);
                }else{
                    d.setVrPago(new BigDecimal(request.getParameter("vrPago").replaceAll(",", ".") ));
                    
                    if (request.getParameter("encargos") == ""){
                        d.setEncargos(new BigDecimal(0));
                    }else{
                        d.setEncargos(new BigDecimal(request.getParameter("encargos").replaceAll(",", ".") ));
                    }
                    
                    d.setDataPagto(Datas.parse(request.getParameter("dtPagto")));
                    
                    String banco = request.getParameter("banco");
                    d.setBanco(banco);
                    
                    d.baixaCarne(audit);
                }
                
                d.setMsgAutenticado("OK");
                request.getSession().setAttribute("bmc", d);
                
                if (d.getQtBaixa() > 0){
                    response.sendRedirect("c?app=1720&acao=selecionacarne");
                }else{
                    response.sendRedirect("c?app=1720");
                }
            }else{
                request.getSession().removeAttribute("bmc");
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1720.jsp").forward(request, response);
            }
        }
        
       //ESTORNO MANUAL DE CARNE
        else if("1750".equals(app)){
            if("valida".equals(acao)){
                request.setAttribute("app", app);
                
                EstornoManualCarne d = new EstornoManualCarne();
                d.setUserTesoureiro(request.getParameter("userTesoureiro"));
                d.setSenhaTesoureiro(request.getParameter("SenhaTesoureiro"));
                d.setUserDiretor(request.getParameter("userDiretor"));
                d.setSenhaDiretor(request.getParameter("SenhaDiretor"));
                if (request.getParameter("qtEstornos") != ""){
                    d.setQtEstorno(Integer.parseInt(request.getParameter("qtEstornos")));
                }
                
                
                try{
                    d.Autenticar();
                    
                    request.getSession().setAttribute("emc", d);
                    String retorno = d.getMsgAutenticado();
                    if (retorno.equals("OK")) {
                        response.sendRedirect("c?app=1750&acao=selecionacarne");
                    }else{
                        request.setAttribute("msg", retorno);
                        request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                    }
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
 
            }else if("selecionacarne".equals(acao)){
                EstornoManualCarne t = (EstornoManualCarne)request.getSession().getAttribute("emc");
                if (t == null){
                    request.getRequestDispatcher("/pages/1750.jsp").forward(request, response);
                }else if (t.getMsgAutenticado().equals("OK")){
                    request.getRequestDispatcher("/pages/1750-seleciona-carne.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher("/pages/1750.jsp").forward(request, response);
                }
            }else if("validacarne".equals(acao)){
                EstornoManualCarne t = (EstornoManualCarne)request.getSession().getAttribute("emc");
                EstornoManualCarne d = new EstornoManualCarne();
                if (request.getParameter("seqCarne") == ""){
                    d.setSeqCarne(0);
                }else{
                    d.setSeqCarne(Integer.parseInt(request.getParameter("seqCarne")));
                }
                if (request.getParameter("matricula") == ""){
                    d.setMatricula(0);
                }else{
                    d.setMatricula(Integer.parseInt(request.getParameter("matricula")));
                }
                if (request.getParameter("categoria") == ""){
                    d.setCategoria(0);
                }else{
                    d.setCategoria(Integer.parseInt(request.getParameter("categoria")));
                }
                if (request.getParameter("dtVenc") == ""){
                    d.setDataVenc(Datas.parse("01/01/1900"));
                }else{
                    d.setDataVenc(Datas.parse(request.getParameter("dtVenc")));
                }
                d.validaCarne();
                d.setMsgAutenticado("OK");
                d.setQtEstorno(t.getQtEstorno());
                request.getSession().setAttribute("emc", d);
                BigDecimal carne = d.getValor();
                if (carne == BigDecimal.valueOf(0)) {
                    response.sendRedirect("c?app=1750&acao=selecionacarne");
                }else{
                    response.sendRedirect("c?app=1750&acao=estornaCarne");
                }
            }else if("estornaCarne".equals(acao)){
                EstornoManualCarne t = (EstornoManualCarne)request.getSession().getAttribute("emc");
                if (t == null){
                    request.getRequestDispatcher("/pages/1750.jsp").forward(request, response);
                }else if (t.getValor() == BigDecimal.valueOf(0)){
                    request.getRequestDispatcher("/pages/1750.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher("/pages/1750-estorna-carne.jsp").forward(request, response);
                }
            }else if("atualizaestornocarne".equals(acao)){
                EstornoManualCarne t = (EstornoManualCarne)request.getSession().getAttribute("emc");
                EstornoManualCarne d = new EstornoManualCarne();
                
                d.setSeqCarne(t.getSeqCarne());
                d.setMatricula(t.getMatricula());
                d.setCategoria(t.getCategoria());
                d.setDataVenc(t.getDataVenc());
                d.setValor(t.getValor());
                d.setQtEstorno(t.getQtEstorno());

                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), app, request.getRemoteAddr());
                
                d.setMotivo(request.getParameter("motivo"));

                String usuario = request.getUserPrincipal().getName();
                
                d.estornaCarne(audit, usuario);
                
                d.setMsgAutenticado("OK");
                request.getSession().setAttribute("emc", d);
                
                if (d.getQtEstorno() > 0){
                    response.sendRedirect("c?app=1750&acao=selecionacarne");
                }else{
                    response.sendRedirect("c?app=1750");
                }
            }else{
                request.getSession().removeAttribute("emc");
                request.setAttribute("app", app);
                request.getRequestDispatcher("/pages/1750.jsp").forward(request, response);
            }
        }        
        else if("1068".equals(app)){
            int idReserva = Integer.parseInt(request.getParameter("idSeqCancelamento"));
            ReservaChurrasqueira d = ReservaChurrasqueira.getInstance(idReserva);
            request.setAttribute("reserva", d);

            request.getRequestDispatcher("/pages/1069-compCancelamento.jsp").forward(request, response);    
        }
        //Comprovante Reserva Churrasqueira
        else if("1069".equals(app)){
            int idReserva = Integer.parseInt(request.getParameter("idReserva"));
            ReservaChurrasqueira d = ReservaChurrasqueira.getInstance(idReserva);
            request.setAttribute("reserva", d);

            request.getRequestDispatcher("/pages/1068-compConfirmacao.jsp").forward(request, response);    
        }
        else if("1920".equals(app)){
            request.getRequestDispatcher("/pages/1920.jsp").forward(request, response);    
        }
        else if("8050".equals(app)){
            request.getRequestDispatcher("/pages/8050.jsp").forward(request, response);    
        }
        else if("1418".equals(app)){
            request.getRequestDispatcher("/pages/1418.jsp").forward(request, response);    
        }
            
        // Chama o mï¿½todo a partir da tabela de mï¿½todos
        else {
            Method m = this.apps.get(app);
            if (m != null) {    
                try {
                    m.invoke(null, request, response);
                } catch (Exception e) {
                    String msg = "Excecao ao invocar " + m.getDeclaringClass().getName() + "." + m.getName() + "() (app=" + app + ")." + "\n";
                    StringWriter errors = new StringWriter();
                    e.getCause().printStackTrace(new PrintWriter(errors));
                    msg = msg + errors;
                    log.severe(msg);
                }
            } else {
                log.severe("Nao foi encontrado nenhum metodo para tratar app=" + app + ".\n");
            }
        }
        
    } 

    
    private List<Class> controllers;
    private Map<String, Method> apps;
    
    @Override
    public void init() throws ServletException {
        // Carrega as classes de Controller
        this.controllers = new ArrayList<Class>();
        log.info("Carregando as classes de controle\n");
        try {

            for (Class c : this.getClasses("techsoft")) {
                if (c.isAnnotationPresent(Controller.class)) {
                    this.controllers.add(c);
                }
            }
        } catch(Exception e) {
            log.severe(e.getMessage());
        }
        
        // Monta a tabela de cï¿½digo da aplicaï¿½ï¿½o X mï¿½todo implementador
        this.apps = new HashMap<String, Method>();
        for (Class c : this.controllers) {
            for (Method m : c.getMethods()) {
                if (m.isAnnotationPresent(App.class)) {
                    if (Controle.signatureVerified(m))
                      for (String s : m.getAnnotation(App.class).value()) apps.put(s, m);
                        //TODO: cï¿½digo para detectar apps repetidas
                    else log.severe("Mï¿½todo marcado como @App com assinatura invï¿½lida:\n" + c + "\n" + m + "\n");
                    }
                }
            }
    }

    
    private static boolean signatureVerified(Method m) {
        int mod = m.getModifiers();
        if (!Modifier.isStatic(mod)) return false;
        if (Modifier.isAbstract(mod) || Modifier.isInterface(mod)) return false;

        if (m.getReturnType() != Void.TYPE) return false;
        
        Class[] parameter = m.getParameterTypes();
        if (parameter.length == 2) {
            if (parameter[0] != HttpServletRequest.class) return false;
            if (parameter[1] != HttpServletResponse.class) return false;
        } else return false;
        
        Class[] exceptions = m.getExceptionTypes();
        if (exceptions.length == 2) {
            if ((exceptions[0] != ServletException.class) && (exceptions[1] != ServletException.class)) return false;
            if ((exceptions[0] != IOException.class) && (exceptions[1] != IOException.class)) return false;
        } else return false;
        
        return true;
    }

    
    
    private Iterable<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            try {
                dirs.add(new File(resource.toURI()));
            } catch (URISyntaxException ex) {
                Logger.getLogger(Controle.class.getName()).severe(ex.getMessage());
            }
        }
        List<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }

        return classes;
    }

    
    private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
    
        List<Class> classes = new ArrayList<Class>();
        
        if (!directory.exists()) {
            return classes;
        }
        
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            }
            else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
    
    
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    
    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

}
