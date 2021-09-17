package techsoft.acesso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.ComboBoxLoader;
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
public class ControlePresencaClube{
    

    @App("7060")
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
            
            socios = Socio.listar(doc, 0, 0, nome);
            request.setAttribute("socios", socios);

            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));

            request.getRequestDispatcher("/pages/7060-lista.jsp").forward(request, response);
        }else if ("detalhe".equals(acao)){
            int doc = Integer.parseInt(request.getParameter("doc"));
            int local = 0;
            try{
                local = Integer.parseInt(request.getParameter("idLocal"));
            }catch(NumberFormatException e){
                local = 0;
            }
            
            Date dtInicio = null;
            Date dtFim = null;
            SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
            
            if (request.getParameter("dtInicial") == null){
                Date dt = new Date();
                dtInicio = Datas.parse(sd.format(dt));
                dtFim = Datas.parse(sd.format(dt));
            }else{
                dtInicio = Datas.parse(request.getParameter("dtInicial"));
                dtFim = Datas.parse(request.getParameter("dtFinal"));
            }
            request.setAttribute("lista", PresencaClube.listar(doc, local, dtInicio, dtFim));
            request.setAttribute("locais", ComboBoxLoader.listar("VW_LOCAL_ACESSO"));
            request.setAttribute("dtInicial", sd.format(dtInicio));
            request.setAttribute("dtFinal", sd.format(dtFim));
            request.setAttribute("doc", doc);
            request.setAttribute("local", local);
            request.setAttribute("nome", Socio.getInstance(doc).getNome());
            request.getRequestDispatcher("/pages/7060.jsp").forward(request, response);
        }else{
            request.setAttribute("lista", LocalAcesso.listar());
            request.getRequestDispatcher("/pages/7060-lista.jsp").forward(request, response);
        }
    }
    

}
