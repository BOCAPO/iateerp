package techsoft.tabelas;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.acesso.LocalAcesso;
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
public class ControleCategoria{
    

    @App("1010")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", Categoria.listar());
            request.getRequestDispatcher("/pages/1010-lista.jsp").forward(request, response);

    }
    
    @App("1011")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "1011");
                request.setAttribute("catUsuarios", ComboBoxLoader.listar("TB_CATEGORIA") );
                request.getRequestDispatcher("/pages/1010.jsp").forward(request, response);            
            }else{

                int id = Integer.parseInt(request.getParameter("id"));
                String descricao = request.getParameter("descricao");
                String abreviacao = request.getParameter("abreviacao");
                String status = request.getParameter("status");
                String tipo = request.getParameter("tipo");
                String tituloTransferivel = request.getParameter("tituloTransferivel");
                String admiteDependente = request.getParameter("admiteDependente");
                int idCatUsuario=Integer.parseInt(request.getParameter("idCatUsuario"));
                float vrMaxCarne = 0;
                int qtUsuario = 0;
                int qtConvite = 0;
                int prazoRenovacaoConvite = 0;
                try{
                    vrMaxCarne=Float.parseFloat(request.getParameter("vrMaxCarne"));
                }catch(Exception e){
                    vrMaxCarne=0;
                }
                try{
                    qtUsuario=Integer.parseInt(request.getParameter("qtUsuario"));
                }catch(Exception e){
                    qtUsuario=0;
                }
                try{
                    qtConvite=Integer.parseInt(request.getParameter("qtConvite"));
                }catch(Exception e){
                    qtConvite=0;
                }
                try{
                    prazoRenovacaoConvite=Integer.parseInt(request.getParameter("prazoRenovacaoConvite"));
                }catch(Exception e){
                    prazoRenovacaoConvite=0;
                }
                
                
                Categoria d = new Categoria();
                d.setId(id);
                d.setDescricao(descricao);
                d.setAbreviacao(abreviacao);
                d.setStatus(status);
                d.setTipo(tipo);
                d.setTituloTransferivel(tituloTransferivel);
                d.setAdmiteDependente(admiteDependente);
                d.setIdCatUsuario(idCatUsuario);
                d.setVrMaxCarne(vrMaxCarne);
                d.setQtUsuario(qtUsuario);
                d.setQtConvite(qtConvite);
                d.setPrazoRenovacaoConvite(prazoRenovacaoConvite);

                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1011", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1010");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("1012")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "1012");
                
                int id = Integer.parseInt(request.getParameter("id"));
                Categoria d = Categoria.getInstance(id);
                request.setAttribute("categoria", d);
                request.setAttribute("catUsuarios", ComboBoxLoader.listar("TB_CATEGORIA") );
                request.getRequestDispatcher("/pages/1010.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                String descricao = request.getParameter("descricao");
                String abreviacao = request.getParameter("abreviacao");
                String status = request.getParameter("status");
                String tipo = request.getParameter("tipo");
                String tituloTransferivel = request.getParameter("tituloTransferivel");
                String admiteDependente = request.getParameter("admiteDependente");
                int idCatUsuario=Integer.parseInt(request.getParameter("idCatUsuario"));
                String corTitular = request.getParameter("corTitular");
                String corDependente = request.getParameter("corDependente");
                
                float vrMaxCarne = 0;
                int qtUsuario = 0;
                int qtConvite = 0;
                int prazoRenovacaoConvite = 0;
                try{
                    vrMaxCarne=Float.parseFloat(request.getParameter("vrMaxCarne"));
                }catch(Exception e){
                    vrMaxCarne=0;
                }
                try{
                    qtUsuario=Integer.parseInt(request.getParameter("qtUsuario"));
                }catch(Exception e){
                    qtUsuario=0;
                }
                try{
                    qtConvite=Integer.parseInt(request.getParameter("qtConvite"));
                }catch(Exception e){
                    qtConvite=0;
                }
                try{
                    prazoRenovacaoConvite=Integer.parseInt(request.getParameter("prazoRenovacaoConvite"));
                }catch(Exception e){
                    prazoRenovacaoConvite=0;
                }
                
                
                Categoria d = Categoria.getInstance(id);

                d.setDescricao(descricao);
                d.setAbreviacao(abreviacao);
                d.setStatus(status);
                d.setTipo(tipo);
                d.setTituloTransferivel(tituloTransferivel);
                d.setAdmiteDependente(admiteDependente);
                d.setIdCatUsuario(idCatUsuario);
                d.setVrMaxCarne(vrMaxCarne);
                d.setQtUsuario(qtUsuario);
                d.setQtConvite(qtConvite);
                d.setPrazoRenovacaoConvite(prazoRenovacaoConvite);
                d.setCorTitular(corTitular);
                d.setCorDependente(corDependente);        
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1012", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=1010");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    
    @App("1013")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        Categoria b = Categoria.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1013", request.getRemoteAddr());
        b.excluir(audit);

        response.sendRedirect("c?app=1010");
    }        
        
}
