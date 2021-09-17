package techsoft.controle.operacoes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.Socio;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.operacoes.AutorizacaoEmbarque;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;

@Controller
public class ControleAutorizacaoEmbarque {
    
    @App("1550")
    public static void consultar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");        
        
        if("consultar".equals(acao)){
            int numAutorizacao = 0;

            try{
                numAutorizacao = Integer.parseInt(request.getParameter("numAutorizacao"));
            }catch(NumberFormatException e){
                numAutorizacao = 0;
            }
            Date d1 = Datas.parse(request.getParameter("dataEmissao"));
            Date d2 = Datas.parse(request.getParameter("dataValidade"));

            List<AutorizacaoEmbarque> autorizacoes = 
                    AutorizacaoEmbarque.consultar(
                    numAutorizacao, 
                    d1, 
                    d2, 
                    request.getParameter("responsavel"),
                    request.getParameter("convidado"));
            
            for (String p : Collections.list(request.getParameterNames()))
                request.setAttribute(p, request.getParameter(p));

            request.setAttribute("autorizacoes", autorizacoes);
            request.getRequestDispatcher("/pages/1550.jsp").forward(request, response);            
        }else{            
            request.getSession().removeAttribute("responsavel");
            request.getSession().removeAttribute("convidados");            
            request.getRequestDispatcher("/pages/1550.jsp").forward(request, response);
        }
    }        

    @App("1551")
    public static void inserir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");        
  
        if("gravar".equals(acao)){

            ArrayList<AutorizacaoEmbarque> autorizacoes = new ArrayList<AutorizacaoEmbarque>();
            String[] vetConv =  request.getParameter("variosConvidados").split(";");
            for(int i =0; i < vetConv.length ; i++){
                AutorizacaoEmbarque a = new AutorizacaoEmbarque();
                
                a.setDataValidade(Datas.parse(request.getParameter("dataValidade")));
                a.setEmbarcacao(request.getParameter("embarcacao"));
                a.setCapacidade(Integer.parseInt(request.getParameter("capacidade")));
                a.setResponsavel((Socio)request.getSession().getAttribute("responsavel"));
                
                String[] vetDet =  vetConv[i].split("#");
                //um gato pra tratar o split, pois quando chega soh ### ele nao considera como casa do vetor, assimm tem sempre um caractere no inicio
                a.setConvidado(vetDet[0].substring(1));
                a.setDtNascimento(Datas.parse(vetDet[1].substring(1)));
                a.setCpfConvidado(vetDet[2].substring(1));
                a.setDocEstrangeiro(vetDet[3].substring(1));
                autorizacoes.add(a);
            }

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1551", request.getRemoteAddr());
            AutorizacaoEmbarque.inserir(autorizacoes, audit);
            
            request.getSession().removeAttribute("responsavel");
            request.getSession().removeAttribute("convidados");
            response.sendRedirect("c?app=1550");
            
        }else if("selecionarResponsavel".equals(acao)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            
            Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);
            request.getSession().setAttribute("responsavel", s);
            request.getSession().setAttribute("convidados", new ArrayList<AutorizacaoEmbarque>());
            request.setAttribute("app", "1551");
            for (String p : Collections.list(request.getParameterNames()))
                request.setAttribute(p, request.getParameter(p));
            
            request.getRequestDispatcher("/pages/1551.jsp").forward(request, response);            
        }else{//consultarSocio
            String n = request.getParameter("nomeSocio");
            if(n != null && !n.trim().equals("")){
                List<Socio> socios = Socio.listar(0, 0, 0, n);
                request.setAttribute("socios", socios);
            }
            for (String p : Collections.list(request.getParameterNames()))
                request.setAttribute(p, request.getParameter(p));
            
            request.getRequestDispatcher("/pages/1551-responsavel.jsp").forward(request, response);
        }

    }        
    
    @App("1552")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");
        int idAutorizacaoEmbarque = Integer.parseInt(request.getParameter("idAutorizacaoEmbarque"));
        AutorizacaoEmbarque a = AutorizacaoEmbarque.getInstance(idAutorizacaoEmbarque);
            
        if("gravar".equals(acao)){
            a.setDataValidade(Datas.parse(request.getParameter("dataValidade")));
            a.setEmbarcacao(request.getParameter("embarcacao"));
            a.setCapacidade(Integer.parseInt(request.getParameter("capacidade")));
            a.setResponsavel((Socio)request.getSession().getAttribute("responsavel"));

            a.setConvidado(request.getParameter("convidado"));
            a.setDtNascimento(Datas.parse(request.getParameter("nasc")));
            a.setCpfConvidado(request.getParameter("cpf"));
            a.setDocEstrangeiro(request.getParameter("docEst"));

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1552", request.getRemoteAddr());
            a.alterar(audit);
            response.sendRedirect("c?app=1550");
        }else{
            request.setAttribute("app", "1552");
            request.setAttribute("responsavel", a.getResponsavel());
            request.setAttribute("autorizacao", a);
            request.getRequestDispatcher("/pages/1551.jsp").forward(request, response);
        }

        
    }        
    
    @App("1553")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        int idAutorizacaoEmbarque = Integer.parseInt(request.getParameter("idAutorizacaoEmbarque"));
        AutorizacaoEmbarque a = AutorizacaoEmbarque.getInstance(idAutorizacaoEmbarque);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1553", request.getRemoteAddr());
        a.excluir(audit);

        response.sendRedirect("c?app=1550");
    }        
    
    @App("1554")
    public static void imprimir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        int idAutorizacaoEmbarque = Integer.parseInt(request.getParameter("idAutorizacaoEmbarque"));
        AutorizacaoEmbarque a = AutorizacaoEmbarque.getInstance(idAutorizacaoEmbarque);
        request.setAttribute("autorizacao", a);
        request.getRequestDispatcher("/pages/1554.jsp").forward(request, response);
    }
    
}
