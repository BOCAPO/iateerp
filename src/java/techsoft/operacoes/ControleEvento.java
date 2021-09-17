package techsoft.operacoes;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;

@Controller
public class ControleEvento{
    

    @App("1560")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", Evento.listar());
            request.getRequestDispatcher("/pages/1560-lista.jsp").forward(request, response);

    }
    
    @App("1561")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "1561");
                request.getRequestDispatcher("/pages/1560.jsp").forward(request, response);            
            }else{
                
                Evento d = new Evento();
                d.setDescricao(request.getParameter("descricao"));
                d.setAnimacao(request.getParameter("animacao"));
                d.setLocal(request.getParameter("local"));
                d.setData(Datas.parse(request.getParameter("data")));
                d.setHora(request.getParameter("hora"));
                d.setQtMesas(Integer.parseInt(request.getParameter("qtMesas")));
                d.setQtCadeiras(Integer.parseInt(request.getParameter("qtCadeiras")));
                d.setQtIngressos(Integer.parseInt(request.getParameter("qtIngressos")));

                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1561", request.getRemoteAddr());
                try{
                    d.inserir(audit);
                    response.sendRedirect("c?app=1560");
                    
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }

    }
    
    @App("1562")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            String acao = request.getParameter("acao");

            if("showForm".equals(acao)){
                request.setAttribute("app", "1562");
                
                int id = Integer.parseInt(request.getParameter("id"));
                Evento d = Evento.getInstance(id);
                request.setAttribute("evento", d);
                request.getRequestDispatcher("/pages/1560.jsp").forward(request, response);
            }else{
                Evento d = Evento.getInstance(Integer.parseInt(request.getParameter("id")));
                d.setDescricao(request.getParameter("descricao"));
                d.setAnimacao(request.getParameter("animacao"));
                d.setLocal(request.getParameter("local"));
                d.setData(Datas.parse(request.getParameter("data")));
                d.setHora(request.getParameter("hora"));
                d.setQtMesas(Integer.parseInt(request.getParameter("qtMesas")));
                d.setQtCadeiras(Integer.parseInt(request.getParameter("qtCadeiras")));
                d.setQtIngressos(Integer.parseInt(request.getParameter("qtIngressos")));

                
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1562", request.getRemoteAddr());
                try{
                    d.alterar(audit);
                    response.sendRedirect("c?app=1560");
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            
        }
    
    
    @App("1563")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        
        int id = Integer.parseInt(request.getParameter("id"));
        Evento b = Evento.getInstance(id);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1563", request.getRemoteAddr());
        b.excluir(audit);

        response.sendRedirect("c?app=1560");
    }        
        
    @App("1590")
    public static void consultaReservaLugar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("consultar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            
            sql.append("SELECT ");
            sql.append("T1.DE_EVENTO, ");
            sql.append("T2.NR_MESA, ");
            sql.append("CHAR(64 + T2.NR_CADEIRA) AS LETRA_CADEIRA, ");
            sql.append("T2.NO_PESSOA, ");
            sql.append("T2.IC_TIPO_PESSOA, ");
            sql.append("T2.IC_MEIA, ");
            sql.append("T2.CD_SIT_RESERVA, ");
            sql.append("T2.VR_RESERVA ");
            sql.append("FROM ");
            sql.append("TB_EVENTO T1, ");
            sql.append("TB_RESERVA_LUGAR T2 ");
            sql.append("WHERE ");
            sql.append("T1.SEQ_EVENTO = T2.SEQ_EVENTO ");

            int idEvento = Integer.parseInt(request.getParameter("idEvento"));
            if(idEvento > 0){
                sql.append(" AND T1.SEQ_EVENTO = ");
                sql.append(idEvento);
            }
            
            String nome = request.getParameter("nome");
            if(nome != null && !nome.trim().equals("")){
                sql.append(" AND T2.NO_PESSOA LIKE '");
                sql.append(nome.toUpperCase());
                sql.append("%'");
            }
            
            try{
                int mesa = Integer.parseInt(request.getParameter("mesa"));
                sql.append(" AND T2.NR_MESA = ");
                sql.append(mesa);
            }catch(NumberFormatException e){
                
            }            

            sql.append("ORDER BY 1, 2, 3");
            
            request.setAttribute("sql", sql.toString());
            request.setAttribute("eventos", ComboBoxLoader.listar("TB_EVENTO"));
            request.setAttribute("idEvento", request.getParameter("idEvento"));
            request.getRequestDispatcher("/pages/1590.jsp").forward(request, response);
        }else{
            request.setAttribute("eventos", ComboBoxLoader.listar("TB_EVENTO"));
            request.getRequestDispatcher("/pages/1590.jsp").forward(request, response);
        }
    }
    
    @App("1580")
    public static void relatorioReservaLugar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();            
            int idEvento = Integer.parseInt(request.getParameter("idEvento"));
            Evento e = Evento.getInstance(idEvento);
            Boolean vagos = Boolean.parseBoolean(request.getParameter("vagos"));
            Boolean reservados = Boolean.parseBoolean(request.getParameter("reservados"));
            Boolean confirmados = Boolean.parseBoolean(request.getParameter("confirmados"));
            
            sql.append("EXEC SP_REL_RESERVA_LUGARES ");
            sql.append(idEvento);
            sql.append(", ");
            if(vagos){
                sql.append("'VA'");
            }else{
                sql.append("'XX'");
            }
            sql.append(", ");
            if(reservados){
                sql.append("'RE'");
            }else{
                sql.append("'XX'");
            }
            sql.append(", ");
            if(confirmados){
                sql.append("'CO'");
            }else{
                sql.append("'XX'");
            }            

            request.setAttribute("titulo", e.getDescricao());
            request.setAttribute("titulo2", "Animação: " + e.getAnimacao());
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);            
        }else{
            request.setAttribute("eventos", ComboBoxLoader.listar("TB_EVENTO"));
            request.getRequestDispatcher("/pages/1580.jsp").forward(request, response);
        }
    }    
}
