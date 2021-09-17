package techsoft.caixa;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
public class ControleTransacao{
    

    @App("6010")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", Transacao.listar());
            request.getRequestDispatcher("/pages/6010-lista.jsp").forward(request, response);

    }
    
    @App("6011")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "6011");
                
                String sql = " SELECT * FROM tb_taxa_administrativa T1, TB_USUARIO_TAXA_INDIVIDUAL T2 "+
                             " WHERE T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA AND T1.ind_taxa_administrativa = 'I' AND T2.USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "' ORDER BY 2";
                request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));
                
                request.getRequestDispatcher("/pages/6010.jsp").forward(request, response);            
            }else{
                Transacao d = new Transacao();
            	d.setDescricao(request.getParameter("descricao"));
                d.setTipo(request.getParameter("tipo"));
                float valPadrao;
                try{
                    valPadrao = Float.parseFloat(request.getParameter("valPadrao").replaceAll(",", "."));
                }catch(Exception e){
                    valPadrao = 0;
                }
                d.setValPadrao(valPadrao);
                
                int taxa;
                try{
                    taxa = Integer.parseInt(request.getParameter("taxa"));
                }catch(Exception e){
                    taxa = 0;
                }
                d.setCdTaxa(taxa);
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6011", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=6010");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("6012")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "6012");
                
                int id = Integer.parseInt(request.getParameter("id"));
                Transacao d = Transacao.getInstance(id);
                request.setAttribute("item", d);
                
                String sql = " SELECT * FROM tb_taxa_administrativa T1, TB_USUARIO_TAXA_INDIVIDUAL T2 "+
                             " WHERE T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA AND T1.ind_taxa_administrativa = 'I' AND T2.USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "' ORDER BY 2";
                request.setAttribute("taxas", ComboBoxLoader.listarSql(sql));
                
                request.getRequestDispatcher("/pages/6010.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                Transacao d = Transacao.getInstance(id);
                d.setId(id); 
            	d.setDescricao(request.getParameter("descricao"));
                d.setTipo(request.getParameter("tipo"));
                float valPadrao;
                try{
                    valPadrao = Float.parseFloat(request.getParameter("valPadrao").replaceAll(",", "."));
                }catch(Exception e){
                    valPadrao = 0;
                }
                d.setValPadrao(valPadrao);
                
                int taxa;
                try{
                    taxa = Integer.parseInt(request.getParameter("taxa"));
                }catch(Exception e){
                    taxa = 0;
                }
                d.setCdTaxa(taxa);
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6012", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=6010");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    
}
