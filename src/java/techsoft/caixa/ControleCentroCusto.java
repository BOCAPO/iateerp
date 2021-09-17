package techsoft.caixa;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import techsoft.seguranca.Usuario;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleCentroCusto{
    

    @App("6150")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("usuario".equals(acao)){
            int id = Integer.parseInt(request.getParameter("id"));
            CentroCusto d = CentroCusto.getInstance(id);
            request.setAttribute("cc", d);
            request.setAttribute("usuarios", Usuario.listar());
            request.getRequestDispatcher("/pages/6150-usuarios.jsp").forward(request, response);
        }else if("gravarUsuario".equals(acao)){
                String usuarios="";
                if(request.getParameterValues("idUsuario") != null){
                    for(String s : request.getParameterValues("idUsuario")){
                        usuarios = usuarios + s + ";";
                    }
                }
                CentroCusto.atualizarUsuarios(Integer.parseInt(request.getParameter("taxa")), usuarios);
                response.sendRedirect("c?app=6150");
        }else{
            request.setAttribute("lista", CentroCusto.listar());
            request.getRequestDispatcher("/pages/6150-lista.jsp").forward(request, response);
        }
    }
    
    @App("6151")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "6151");
                
                String sql = " SELECT * FROM tb_taxa_administrativa T1, TB_USUARIO_TAXA_INDIVIDUAL T2 "+
                             " WHERE T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA AND T1.ind_taxa_administrativa = 'I' AND T2.USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "'";
                request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));
                
                request.getRequestDispatcher("/pages/6150.jsp").forward(request, response);            
            }else{
                CentroCusto d = new CentroCusto();
            	d.setDescricao(request.getParameter("descricao"));
                int taxa;
                try{
                    taxa = Integer.parseInt(request.getParameter("taxa"));
                }catch(Exception e){
                    taxa = 0;
                }
                d.setTaxa(taxa);
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6151", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=6150");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("6152")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "6152");
                
                int id = Integer.parseInt(request.getParameter("id"));
                CentroCusto d = CentroCusto.getInstance(id);
                request.setAttribute("item", d);
                
                String sql = " SELECT * FROM tb_taxa_administrativa T1, TB_USUARIO_TAXA_INDIVIDUAL T2 "+
                             " WHERE T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA AND T1.ind_taxa_administrativa = 'I' AND T2.USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "'";
                request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));
                
                request.getRequestDispatcher("/pages/6150.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                CentroCusto d = CentroCusto.getInstance(id);
                d.setId(id); 
            	d.setDescricao(request.getParameter("descricao"));
                int taxa;
                try{
                    taxa = Integer.parseInt(request.getParameter("taxa"));
                }catch(Exception e){
                    taxa = 0;
                }
                d.setTaxa(taxa);
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6152", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=6150");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }

}
