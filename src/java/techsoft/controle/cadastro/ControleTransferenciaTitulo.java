package techsoft.controle.cadastro;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.cadastro.Titular;
import techsoft.cadastro.TransferenciaTitulo;
import techsoft.cadastro.TransferenciaTituloException;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;

@Controller
public class ControleTransferenciaTitulo {

    @App("1091")
    public static void consultar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");            
        
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

            request.getRequestDispatcher("/pages/1090.jsp").forward(request, response);
        }else if("showForm".equals(acao)){
            
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            boolean ignorarTaxaIndividual = Boolean.parseBoolean(request.getParameter("ignorarTaxaIndividual"));
            
            Titular titular = Titular.getInstance(matricula, idCategoria);
            try{
                TransferenciaTitulo t = TransferenciaTitulo.validar(titular, ignorarTaxaIndividual);
                request.getSession().setAttribute("transferencia", t);
                request.setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
                request.setAttribute("profissoes", ComboBoxLoader.listar("TB_PROFISSAO"));
                request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_ESPECIAL"));
                request.setAttribute("matricula", request.getParameter("matricula"));
                request.setAttribute("idCategoria", request.getParameter("idCategoria"));
                request.getRequestDispatcher("/pages/1091.jsp").forward(request, response);                            
            }catch(TransferenciaTituloException e){
                request.setAttribute("err", e);
                
                if(e.isExisteTaxaIndividual()){
                    request.setAttribute("msg", e.getMessage());
                    request.setAttribute("sim", "c?app=1091&acao=showForm"
                            + "&matricula=" + matricula
                            + "&idCategoria=" + idCategoria
                            + "&ignorarTaxaIndividual=true");
                    request.setAttribute("nao", "javascript: history.go(-1)");
                    request.getRequestDispatcher("/pages/confirmacao.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }                 
            }
        }else if("gravar".equals(acao)){
                //novo titular para quem vai ser transferido o titulo
                Titular t = new Titular();

                //DADOS PESSOAIS
                //Matricula e categoria se mantem com a transferencia
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
                
                //ENDERECO
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
                    t.getContaCorrente().setAgencia(0);
                }
                t.getContaCorrente().setDigitoAgencia(request.getParameter("digitoAgencia"));
                t.getContaCorrente().setConta(request.getParameter("conta"));
                t.getContaCorrente().setDigitoConta(request.getParameter("digitoConta"));
                t.getContaCorrente().setTitular(request.getParameter("titular"));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1091", request.getRemoteAddr());
                TransferenciaTitulo transferencia = 
                        (TransferenciaTitulo)request.getSession().getAttribute("transferencia");
                transferencia.transferir(t, audit);
                request.getSession().removeAttribute("transferencia");
                response.sendRedirect("c?app=1091&acao=consultar&nome="+request.getParameter("nome"));
        }else{
            request.getSession().removeAttribute("transferencia");
            request.getRequestDispatcher("/pages/1090.jsp").forward(request, response);
        }
    }
}
