package techsoft.controle.cadastro;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioOcorrencia;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.TipoOcorrencia;
import techsoft.util.Datas;

@Controller
public class ControleOcorrencia{

    //LISTA DE OCORRENCIAS DO SOCIO
    @App("1170")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int matricula = Integer.parseInt(request.getParameter("matricula"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
        Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);

        request.setAttribute("socio", s);
        request.setAttribute("ocorrencias", SocioOcorrencia.listar(s));
        request.getRequestDispatcher("/pages/1170-lista.jsp").forward(request, response);            
    }

    //INCLUSAO DE OCORRENCIA PARA O SOCIO
    @App("1171")
    public static void inserir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {    
        
        String acao = request.getParameter("acao");
        int matricula = Integer.parseInt(request.getParameter("matricula"));
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
        Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);

        if("gravar".equals(acao)){
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1171", request.getRemoteAddr());
            SocioOcorrencia o = new SocioOcorrencia();
            o.setSocio(s);
            preencherDadosOcorrencia(o, request, response);

            o.inserir(audit);

            response.sendRedirect("c?app=1170&matricula=" + o.getSocio().getMatricula() + "&seqDependente=" + o.getSocio().getSeqDependente() + "&idCategoria=" + o.getSocio().getIdCategoria());           
        }else{
            request.setAttribute("app", "1171");
            request.setAttribute("socio", s);
            request.setAttribute("tipos", TipoOcorrencia.listar());
            request.getRequestDispatcher("/pages/1170.jsp").forward(request, response);            
        }
            
    }        
            
    //EXCLUSAO DE BARCOS DO SOCIO
    @App("1173")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int idOcorrencia = Integer.parseInt(request.getParameter("idOcorrencia"));
        SocioOcorrencia o = SocioOcorrencia.getInstance(idOcorrencia);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1173", request.getRemoteAddr());
        o.excluir(audit);

        response.sendRedirect("c?app=1170&matricula=" + o.getSocio().getMatricula() + "&seqDependente=" + o.getSocio().getSeqDependente() + "&idCategoria=" + o.getSocio().getIdCategoria());           
    }        
    
    private static void preencherDadosOcorrencia(SocioOcorrencia o, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{

        int i = Integer.parseInt(request.getParameter("idTipoOcorrencia"));
        o.setTipo(TipoOcorrencia.getInstance(i));
        o.setDataInicio(Datas.parse(request.getParameter("dataInicio")));
        o.setDataFim(Datas.parse(request.getParameter("dataFim")));
        o.setDescricao(request.getParameter("descricao"));

    }
        
}
