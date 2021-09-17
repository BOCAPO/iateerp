package techsoft.clube;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.Convite;
import techsoft.cadastro.Socio;
import techsoft.cadastro.Titular;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.operacoes.ReservaChurrasqueira;
import techsoft.tabelas.Convenio;
import techsoft.util.Datas;

@Controller
public class ControleConvite{

    @App("1070")
    public static void consultar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("consultar".equals(acao)){
            int numero = 0;
            String dtEmissao = request.getParameter("dtEmissao");
            String dtValidade = request.getParameter("dtValidade");
            String responsavel = request.getParameter("responsavel");
            String convidado = request.getParameter("convidado");

            try{
                numero = Integer.parseInt(request.getParameter("numero"));
            }catch(NumberFormatException e){
                numero = 0;
            }

            List<Convite> convites = Convite.listar(numero, dtEmissao, responsavel, convidado, dtValidade);
            request.setAttribute("dtEmissao", dtEmissao);
            request.setAttribute("dtValidade", dtValidade);
            request.setAttribute("responsavel", responsavel);
            request.setAttribute("convidado", convidado);
            request.setAttribute("numero", request.getParameter("numero"));
            request.setAttribute("convites", convites);

            request.getRequestDispatcher("/pages/1070-lista.jsp").forward(request, response);
        }else if("impressao".equals(acao)){
            if (request.getParameter("categoriaConvite").equals("SP")){
                String idConvite = request.getParameter("idConvite");
                request.setAttribute("idConvite", idConvite);
                request.getRequestDispatcher("/pages/1070-impressao-conv-esp.jsp").forward(request, response);
            }else{
                request.getRequestDispatcher("/pages/1070-impressao.jsp").forward(request, response);
            }
            

        }else{
            request.getRequestDispatcher("/pages/1070-lista.jsp").forward(request, response);
        }
    }
    
    @App("1071")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "1071");
            request.setAttribute("convenios", Convenio.listar());
            request.getRequestDispatcher("/pages/1070.jsp").forward(request, response);
        }else if("socio".equals(acao)){
            int convenio = 0;

            request.setAttribute("app", "1071");
            request.setAttribute("selecao", request.getParameter("selecao"));

            try{
                convenio = Integer.parseInt(request.getParameter("convenio"));
            }catch(NumberFormatException e){
                convenio = 0;
            }

            String Tipo = request.getParameter("selecao");
            request.setAttribute("tipo", Tipo);

            if (Tipo.equals("EC")){
                String conv = request.getParameter("conv");
                int id = Integer.parseInt(conv);
                Convenio c = Convenio.getInstance(id);
                request.setAttribute("convenio", c);

                Convite d = Convite.calculaDtVenc();
                request.setAttribute("convite", d);
                request.setAttribute("usuarioAtual", request.getUserPrincipal().getName());
                request.getRequestDispatcher("/pages/1070-incluir.jsp").forward(request, response);
            }else if (Tipo.equals("CO") || Tipo.equals("VC")){
                Socio comodoro = Socio.getComodoro();
                request.setAttribute("socio", comodoro);
                Convite d = Convite.calculaDtVenc();
                request.setAttribute("convite", d);

                request.setAttribute("usuarioAtual", request.getUserPrincipal().getName());

                request.getRequestDispatcher("/pages/1070-incluir.jsp").forward(request, response);
            }else {
                request.getRequestDispatcher("/pages/1070-selSocio.jsp").forward(request, response);
            }

        }else if("consultarSocio".equals(acao)){
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

            request.setAttribute("selecao", request.getParameter("selecao"));

            List<Socio> socios = Socio.listarParaConvite(carteira, categoria, matricula, nome, request.getParameter("selecao"));
            request.setAttribute("socios", socios);

            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));

            request.getRequestDispatcher("/pages/1070-selSocio.jsp").forward(request, response);

        }else if("convites".equals(acao)){
            int matricula = 0;
            int categoria = 0;
            int dependente = 0;
            try{
                categoria = Integer.parseInt(request.getParameter("titulo").substring(4, 6));
            }catch(NumberFormatException e){
                categoria = 0;
            }
            try{
                matricula = Integer.parseInt(request.getParameter("titulo").substring(0,4));
            }catch(NumberFormatException e){
                matricula = 0;
            }
            try{
                dependente = Integer.parseInt(request.getParameter("titulo").substring(6));
            }catch(NumberFormatException e){
                dependente = 0;
            }

            Socio socio = Socio.getInstance(matricula, dependente, categoria);

            request.setAttribute("socio", socio);
            request.setAttribute("tipo", request.getParameter("selecao"));
            
            if (request.getParameter("selecao").equals("SP")){
                // TELA DIFERENCIADA PARA CONVITE ESPORTIVO
                request.getRequestDispatcher("/pages/1070-incluir-esportivo.jsp").forward(request, response);
            }else{
                // DEMAIS CONVITES
                Convite d = Convite.calculaDtVenc();
                request.setAttribute("convite", d);
                request.setAttribute("saldo", Titular.getSdconvite(matricula, categoria));
                request.setAttribute("saldoSauna", Titular.getSdconviteSauna(matricula, categoria));
                request.setAttribute("usuarioAtual", request.getUserPrincipal().getName());

                List<ReservaChurrasqueira> reservas = ReservaChurrasqueira.reservaSocioConvite(matricula, categoria);
                request.setAttribute("reservas", reservas);

                request.getRequestDispatcher("/pages/1070-incluir.jsp").forward(request, response);
            }
            
        }
    }
    
    @App("1072")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        
        if("showForm".equals(acao)){
            request.setAttribute("app", "1072");
            int idConvite = Integer.parseInt(request.getParameter("idConvite"));
            Convite d = Convite.getInstance(idConvite);
            request.setAttribute("convite", d);

            List<ReservaChurrasqueira> reservas = ReservaChurrasqueira.reservaSocioConvite(d.getMatricula(), d.getCategoriaSocio());

            request.setAttribute("reservas", reservas);
            request.getRequestDispatcher("/pages/1070-alterar.jsp").forward(request, response);
        }else{
            int idConvite = Integer.parseInt(request.getParameter("idConvite"));
            Convite d = Convite.getInstance(idConvite);
            d.setConvidado(request.getParameter("nome"));
            d.setEstacionamento(request.getParameter("estacionamento"));

            Date data = null;
            if (!d.getCategoriaConvite().equals("SP")){
                data = Datas.parse(request.getParameter("Data"));
                d.setDtLimiteUtilizacao(data);
            }else{
                data = Datas.parse(request.getParameter("iniValidade"));
                d.setDtInicValidade(data);
                data = Datas.parse(request.getParameter("fimValidade"));
                d.setDtFimValidade(data);
                d.setModalidade(request.getParameter("modalidade"));
                d.setEntraSegunda(request.getParameter("segundaEntrada"));
                d.setEntraTerca(request.getParameter("tercaEntrada"));
                d.setEntraQuarta(request.getParameter("quartaEntrada"));
                d.setEntraQuinta(request.getParameter("quintaEntrada"));
                d.setEntraSexta(request.getParameter("sextaEntrada"));
                d.setSaiSegunda(request.getParameter("segundaSaida"));
                d.setSaiTerca(request.getParameter("tercaSaida"));
                d.setSaiQuarta(request.getParameter("quartaSaida"));
                d.setSaiQuinta(request.getParameter("quintaSaida"));
                d.setSaiSexta(request.getParameter("sextaSaida"));
            }

            int reserva = 0;
            try{
                reserva = Integer.parseInt(request.getParameter("selecao"));
            }catch(Exception e){
            }
            d.setReservaChurrasqueira(reserva);

            d.setCategoriaConvite(request.getParameter("categoriaConvite"));

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1072", request.getRemoteAddr());

            d.alterar(audit);

            response.sendRedirect("c?app=1070&acao=consultar&numero=" + idConvite);
        }
    }
    
    @App("1073")
    public static void cancelar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int idConvite = Integer.parseInt(request.getParameter("idConvite"));
        Convite d = Convite.getInstance(idConvite);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1073", request.getRemoteAddr());
        d.cancela(audit);

        response.sendRedirect("c?app=1070&acao=consultar&numero="+request.getParameter("idConvite"));
    }
    
    @App("1076")
    public static void reativar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int idConvite = Integer.parseInt(request.getParameter("idConvite"));
        Convite d = Convite.getInstance(idConvite);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1076", request.getRemoteAddr());
        d.reativa(audit);

        response.sendRedirect("c?app=1070&acao=consultar&numero="+request.getParameter("idConvite"));
    }
    
    
}

