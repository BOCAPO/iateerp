package techsoft.acesso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.Titular;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleMensagens{
    

    @App("7030")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");
        String tipoDoc = request.getParameter("tipoDoc");

        if ("consultar".equals(acao)){
            int doc = 0;

            String nome = request.getParameter("nome");

            try{
                doc = Integer.parseInt(request.getParameter("doc"));
            }catch(NumberFormatException e){
                doc = 0;
            }
            if(nome == null) nome = "";

            List<Socio> socios;
            
            if ("C".equals(tipoDoc)){
                socios = Socio.listar(doc, 0, 0, nome);
            }else{
                socios = Socio.listarPassaporte(doc, nome);
            }
            request.setAttribute("socios", socios);

            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));

            request.getRequestDispatcher("/pages/7030.jsp").forward(request, response);
        }else if ("detalhe".equals(acao)){
            int doc = Integer.parseInt(request.getParameter("doc"));
            List<Socio> socios;
            
            if ("C".equals(tipoDoc)){
                socios = Socio.listar(doc, 0, 0, "");
            }else{
                socios = Socio.listarPassaporte(doc, "");
            }
            
            List<Mensagem> assoc = Mensagem.listar(doc, "A");
            List<Mensagem> naoAssoc = Mensagem.listar(doc, "N");
            
            request.setAttribute("socios", socios);
            request.setAttribute("assoc", assoc);
            request.setAttribute("naoAssoc", naoAssoc);
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));

            request.getRequestDispatcher("/pages/7030-mensagens.jsp").forward(request, response);
            
        }else if ("incluir".equals(acao)){
            Mensagem d = new Mensagem();
            d.setId(Integer.parseInt(request.getParameter("id")));
            d.setDoc(Integer.parseInt(request.getParameter("doc")));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "7031", request.getRemoteAddr());
            d.incluir(audit);
            
            response.sendRedirect("c?app=7030&acao=detalhe&doc="+request.getParameter("doc")+"&tipoDoc="+tipoDoc);
            
        }else if ("excluir".equals(acao)){
            Mensagem d = new Mensagem();
            d.setId(Integer.parseInt(request.getParameter("id")));
            d.setDoc(Integer.parseInt(request.getParameter("doc")));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "7032", request.getRemoteAddr());
            d.excluir(audit);

            response.sendRedirect("c?app=7030&acao=detalhe&doc="+request.getParameter("doc")+"&tipoDoc="+tipoDoc);

        }else{
            request.setAttribute("lista", LocalAcesso.listar());
            request.getRequestDispatcher("/pages/7030.jsp").forward(request, response);
        }
    }
    

}
