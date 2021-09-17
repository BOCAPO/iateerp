package techsoft.controle.tabelas;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.clube.ImpressoraCartao;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.AlterarException;
import techsoft.tabelas.Cracha;
import techsoft.tabelas.Funcionario;
import techsoft.tabelas.FuncionarioHorario;
import techsoft.tabelas.InserirException;
import techsoft.util.Datas;

@Controller
public class ControleFuncionario {

    @App("1050")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao=request.getParameter("acao");
    
        if("consultar".equals(acao)){
            request.setAttribute("setores", ComboBoxLoader.listar("TB_SETOR"));
            request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_FUNCAO"));

            String tipo = null;
            boolean funcionario = Boolean.parseBoolean(request.getParameter("funcionario"));
            boolean concessionario = Boolean.parseBoolean(request.getParameter("concessionario"));            
            
            if(funcionario ^ concessionario){
                if(funcionario){
                    tipo = "F";
                }else{
                    tipo = "C";
                }
            }else{
                tipo = "";
            }
            request.setAttribute("tipo", tipo);            
            
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));

            request.getRequestDispatcher("/pages/1050-lista.jsp").forward(request, response);
        }else if("listagem".equals(acao)){
            int idCargo = Integer.parseInt(request.getParameter("idCargo"));
            int idSetor = Integer.parseInt(request.getParameter("idSetor")); 
            boolean funcionario = Boolean.parseBoolean(request.getParameter("funcionario"));
            String Teste = request.getParameter("concessionario");
            boolean concessionario = Boolean.parseBoolean(request.getParameter("concessionario"));                        
            String nome = request.getParameter("nome");
            
            String sql = "SELECT "
                + "T2.DESCR_SETOR as Setor, "
                + "T3.DESCR_CARGO as Cargo, "
                + "T1.NOME_FUNCIONARIO as Nome_Funcionário, "
                + "Case T1.TP_FUNCIONARIO "
                + "WHEN 'F' THEN 'FUNCIONÁRIO' "
                + "Else 'CONCESSIONÁRIO' "
                + "END AS Tipo "
                + "FROM TB_FUNCIONARIO T1, TB_SETOR T2, "
                + "TB_CARGO_FUNCAO T3 "
                + "Where T1.CD_CARGO = T3.CD_CARGO "
                + "AND T1.CD_SETOR = T2.CD_SETOR ";
            if(idCargo > 0){
                sql += "AND T1.CD_CARGO = " + idCargo;
            }
            if(idSetor > 0){
                sql += "AND T1.CD_SETOR = " + idSetor;
            }
            if(nome != null && !nome.equals("")){
                sql += "AND T1.NOME_FUNCIONARIO LIKE '" + nome + "%'";
            }
            if(concessionario){
                if(!funcionario){
                    sql += "AND T1.TP_FUNCIONARIO = 'C'";
                }
            }else if(funcionario){
                sql += "AND T1.TP_FUNCIONARIO = 'F'";
            }

            request.setAttribute("titulo", "Relação de Funcionários e Concessionários");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "true");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                                    
        }else{
            request.setAttribute("setores", ComboBoxLoader.listar("TB_SETOR"));
            request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_FUNCAO"));
            request.setAttribute("funcionario", "true");
            request.setAttribute("concessionario", "true");
            request.getRequestDispatcher("/pages/1050-lista.jsp").forward(request, response);
        }
    }
    
    @App("1051")
    public static void inserir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao=request.getParameter("acao");
    
        if("gravar".equals(acao)){
            Funcionario f = new Funcionario();
            ControleFuncionario.preencherFuncionario(f, request);
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1051", request.getRemoteAddr());
                f.inserir(audit);
                response.sendRedirect("c?app=1050");
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
        }else{
            request.setAttribute("setores", ComboBoxLoader.listar("TB_SETOR"));
            request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_FUNCAO"));
            request.setAttribute("app", "1051");
            request.getRequestDispatcher("/pages/1050.jsp").forward(request, response);
        }
    }    
    
    @App("1052")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao=request.getParameter("acao");
    
        int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
        Funcionario f = Funcionario.getInstance(idFuncionario);
        if("gravar".equals(acao)){
            ControleFuncionario.preencherFuncionario(f, request);
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1052", request.getRemoteAddr());
                f.alterar(audit);
                response.sendRedirect("c?app=1050");
            }catch(AlterarException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }            
        }else{
            request.setAttribute("setores", ComboBoxLoader.listar("TB_SETOR"));
            request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_FUNCAO"));
            request.setAttribute("app", "1052");
            request.setAttribute("funcionario", f);
            request.getRequestDispatcher("/pages/1050.jsp").forward(request, response);
        }
    }
    
    @App("1053")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    
        int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
        Funcionario f = Funcionario.getInstance(idFuncionario);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1053", request.getRemoteAddr());
        f.excluir(audit);
        response.sendRedirect("c?app=1050");
    }
    
    @App("1054")
    public static void emitirCracha(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        
        String acao=request.getParameter("acao");
        
        int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
        Funcionario f = Funcionario.getInstance(idFuncionario);
        
        if("showForm".equals(acao)){
            request.setAttribute("funcionario", f);
            request.setAttribute("setores", ComboBoxLoader.listar("TB_SETOR"));
            request.setAttribute("cargos", ComboBoxLoader.listar("TB_CARGO_FUNCAO"));
            //validade padrao para concessionarios eh de 6 meses
            if(!"F".equals(f.getTipo())){
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, 6);
                request.setAttribute("dataVencimento", c.getTime());
            }

            request.getRequestDispatcher("/pages/1054.jsp").forward(request, response);                
        }else if("emitirCracha".equals(acao)){
            Date dataVencimento = Datas.parse(request.getParameter("dataVencimento"));
            String cdImpStr = request.getParameter("impressora");
            int cdImp = Integer.parseInt(cdImpStr);
            ImpressoraCartao imp = ImpressoraCartao.getInstance(cdImp);
            String iateHost = request.getLocalAddr();
            //iateHost = "localhost";
            int iatePort = request.getLocalPort();
            String iateApp = request.getContextPath();                
            Cracha c = new Cracha(f, dataVencimento, iateHost, iatePort, iateApp, imp);
            c.emitir();

            response.sendRedirect("c?app=1054&acao=showForm&idFuncionario=" + f.getId());
        }else if("excluirFoto".equals(acao)){
            f.excluirFoto();
            //reload na pagina
            response.sendRedirect("c?app=1054&acao=showForm&idFuncionario=" + f.getId());
        }


    }
    
    @App("1055")
    public static void alterarNumeroCracha(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        
        String acao=request.getParameter("acao");
        
        int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
        Funcionario f = Funcionario.getInstance(idFuncionario);
        
        if("atualizar".equals(acao)){
            int novoCracha = Integer.parseInt(request.getParameter("cracha"));
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1055", request.getRemoteAddr());
                f.alterarNumeroCracha(audit, novoCracha);
                response.sendRedirect("c?app=1050");
            }catch(AlterarException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }            
        }else{
            request.setAttribute("funcionario", f);
            request.getRequestDispatcher("/pages/1055.jsp").forward(request, response);                
        }


    }
    
    @App("1056")
    public static void alteraSenha(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        
        String acao=request.getParameter("acao");
        
        if("showFormAlterarSenha".equals(acao)){
            request.setAttribute("idFuncionario", request.getParameter("idFuncionario"));
            request.getRequestDispatcher("/pages/1050-alterar-senha.jsp").forward(request, response);
        }else if("alterarSenha".equals(acao)){
            int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
            Funcionario f = Funcionario.getInstance(idFuncionario);            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1056", request.getRemoteAddr());
            f.alterarSenha(audit, request.getParameter("novaSenha1"));
            response.sendRedirect("c?app=1050");            
        }    

    }
    
    private static void preencherFuncionario(Funcionario f, HttpServletRequest request){
        f.setPrimeiroNome(request.getParameter("primeiroNome"));
        f.setMatricula(Integer.parseInt(request.getParameter("matricula")));
        f.setLogin(request.getParameter("login"));
        f.setSangue(request.getParameter("sangue"));
        f.setNome(request.getParameter("nome"));
        f.setEndereco(request.getParameter("endereco"));
        f.setTelefone1(request.getParameter("telefone1"));
        f.setTelefone2(request.getParameter("telefone2"));
        f.setIdCargo(Integer.parseInt(request.getParameter("idCargo")));
        f.setIdSetor(Integer.parseInt(request.getParameter("idSetor")));
        f.setTipo(request.getParameter("tipo"));
        f.setEstacionamento(request.getParameter("estacionamento"));
        f.setValorMaximoConsumo(Float.parseFloat(request.getParameter("valorMaximoConsumo").replaceAll(",", ".")));
        f.setCPF(request.getParameter("cpf"));
        
        String entrada = request.getParameter("segundaEntrada");
        String saida = request.getParameter("segundaSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[0] = new FuncionarioHorario(entrada, saida);
        }

        entrada = request.getParameter("tercaEntrada");
        saida = request.getParameter("tercaSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[1] = new FuncionarioHorario(entrada, saida);
        }
        
        entrada = request.getParameter("quartaEntrada");
        saida = request.getParameter("quartaSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[2] = new FuncionarioHorario(entrada, saida);
        }

        entrada = request.getParameter("quintaEntrada");
        saida = request.getParameter("quintaSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[3] = new FuncionarioHorario(entrada, saida);
        }
        
        entrada = request.getParameter("sextaEntrada");
        saida = request.getParameter("sextaSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[4] = new FuncionarioHorario(entrada, saida);
        }
        
        entrada = request.getParameter("sabadoEntrada");
        saida = request.getParameter("sabadoSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[5] = new FuncionarioHorario(entrada, saida);
        }        
        
        entrada = request.getParameter("domingoEntrada");
        saida = request.getParameter("domingoSaida");
        if(entrada != null && !entrada.equals("") && saida != null && !saida.equals("")){
            f.getHorarios()[6] = new FuncionarioHorario(entrada, saida);
        }        
    }
}
