package techsoft.caixa;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
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
public class ControleProdutoServico{
    

    @App("6140")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            int transacao = 0;
            String sql = " SELECT t1.CD_TRANSACAO, t1.DESCR_TRANSACAO " +
                         " FROM TB_TRANSACAO T1, TB_USUARIO_CENTRO_CUSTO T2 " +
                         " WHERE T1.CD_TRANSACAO = T2.CD_TRANSACAO AND T1.CD_DEBITO_CREDITO = 'P' AND T2.USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "'";
            List<ComboBoxLoader> transacoes = ComboBoxLoader.listarSql(sql);
            
            if (request.getParameter("transacao")==null){
                for (ComboBoxLoader x:transacoes){
                    transacao = x.getId();
                    break;
                }
            }else{
                transacao = Integer.parseInt(request.getParameter("transacao"));    
            }
            
            request.setAttribute("transacoes", transacoes);
            request.setAttribute("transacao", transacao);
            request.setAttribute("lista", ProdutoServico.listar(transacao));
            request.getRequestDispatcher("/pages/6140-lista.jsp").forward(request, response);

    }
    
    @App("6141")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "6141");
                request.setAttribute("transacao", request.getParameter("transacao"));
                request.getRequestDispatcher("/pages/6140.jsp").forward(request, response);            
            }else{
                ProdutoServico d = new ProdutoServico();
            	d.setDescricao(request.getParameter("descricao"));
                d.setTipo(request.getParameter("tipo"));
                d.setCredito(request.getParameter("credito"));
                d.setAtivo(request.getParameter("ativo"));
                float valPadrao;
                try{
                    valPadrao = Float.parseFloat(request.getParameter("valPadrao").replaceAll(",", "."));
                }catch(Exception e){
                    valPadrao = 0;
                }
                d.setValPadrao(valPadrao);
                
                int estoqueAtual=0;
                int estoqueMinimo=0;
                try{
                    estoqueAtual = Integer.parseInt(request.getParameter("estoqueAtual"));
                    estoqueMinimo = Integer.parseInt(request.getParameter("estoqueMinimo"));
                }catch(Exception e){
                    estoqueAtual=0;
                    estoqueMinimo=0;
                }
                d.setEstoqueAtual(estoqueAtual);
                d.setEstoqueMinimo(estoqueMinimo);
                d.setTransacao(Integer.parseInt(request.getParameter("transacao")));
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6141", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=6140");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    @App("6142")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "6142");
                
                int id = Integer.parseInt(request.getParameter("id"));
                ProdutoServico d = ProdutoServico.getInstance(id);
                request.setAttribute("item", d);
                request.getRequestDispatcher("/pages/6140.jsp").forward(request, response);
            }else{
                int id = Integer.parseInt(request.getParameter("id"));
                ProdutoServico d = ProdutoServico.getInstance(id);
            	d.setDescricao(request.getParameter("descricao"));
                d.setTipo(request.getParameter("tipo"));
                d.setCredito(request.getParameter("credito"));
                d.setAtivo(request.getParameter("ativo"));
                float valPadrao;
                try{
                    valPadrao = Float.parseFloat(request.getParameter("valPadrao").replaceAll(",", "."));
                }catch(Exception e){
                    valPadrao = 0;
                }
                d.setValPadrao(valPadrao);
                
                int estoqueAtual=0;
                int estoqueMinimo=0;
                try{
                    estoqueAtual = Integer.parseInt(request.getParameter("estoqueAtual"));
                    estoqueMinimo = Integer.parseInt(request.getParameter("estoqueMinimo"));
                }catch(Exception e){
                    estoqueAtual=0;
                    estoqueMinimo=0;
                }
                d.setEstoqueAtual(estoqueAtual);
                d.setEstoqueMinimo(estoqueMinimo);
                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "6142", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=6140");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    
}
