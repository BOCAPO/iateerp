package techsoft.operacoes;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
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
import techsoft.carteirinha.JobMiniImpressora;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;
import techsoft.operacoes.Evento;
import techsoft.tabelas.Material;
@Controller
public class ControleMaterial{
    
    
    
    @App("1150")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("materiais", Material.listar());
            request.getRequestDispatcher("/pages/1150-lista.jsp").forward(request, response);
    }
    
    @App("1151")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "1151");
            request.setAttribute("taxasAdministrativas", ComboBoxLoader.listar("VW_TAXA_ADMINISTRATIVA"));

            request.getRequestDispatcher("/pages/1150.jsp").forward(request, response);
        }else{
            Material d = new Material();
            d.setDescricao(request.getParameter("descricao"));
            if("".equals(request.getParameter("qtEstoque"))){
                d.setQtEstoque(0);
            }else{
                d.setQtEstoque(Integer.parseInt(request.getParameter("qtEstoque")));
            }
            if("".equals(request.getParameter("pzPadraoDevolucao"))){
                d.setPzPadraoDevolucao(0);
            }else{
                d.setPzPadraoDevolucao(Integer.parseInt(request.getParameter("pzPadraoDevolucao")));
            }
            if("".equals(request.getParameter("qtTotal"))){
                d.setQtTotal(0);
            }else{
                d.setQtTotal(Integer.parseInt(request.getParameter("qtTotal")));
            }
            if("".equals(request.getParameter("vrEmprestimo"))){
                d.setVrEmprestimo(new BigDecimal(0));
            }else{
                d.setVrEmprestimo(new BigDecimal(request.getParameter("vrEmprestimo").replaceAll(",", ".") ));
            }
            if("".equals(request.getParameter("qtDiasIniCob"))){
                d.setQtDiasIniCob(0);
            }else{
                d.setQtDiasIniCob(Integer.parseInt(request.getParameter("qtDiasIniCob")));
            }
            if("".equals(request.getParameter("valMaterial"))){
                d.setValMaterial(new BigDecimal(0));
            }else{
                d.setValMaterial(new BigDecimal(request.getParameter("valMaterial").replaceAll(",", ".") ));
            }
            if("".equals(request.getParameter("vrDiaria"))){
                d.setVrDiaria(new BigDecimal(0));
            }else{
                d.setVrDiaria(new BigDecimal(request.getParameter("vrDiaria").replaceAll(",", ".") ));
            }
            d.setCdTxAdministrativa(Integer.parseInt(request.getParameter("cdTxAdministrativa")));

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1151", request.getRemoteAddr());
            try{
                d.inserir(audit);
                response.sendRedirect("c?app=1150");
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }


    }
    @App("1152")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("showForm".equals(acao)){
            request.setAttribute("app", "1152");
            request.setAttribute("taxasAdministrativas", ComboBoxLoader.listar("VW_TAXA_ADMINISTRATIVA"));

            int idMaterial = Integer.parseInt(request.getParameter("idMaterial"));
            Material d = Material.getInstance(idMaterial);
            request.setAttribute("material", d);
            request.getRequestDispatcher("/pages/1150.jsp").forward(request, response);
        }else{
            int idMaterial = Integer.parseInt(request.getParameter("idMaterial"));
            Material d = Material.getInstance(idMaterial);
            d.setDescricao(request.getParameter("descricao"));
            if ("".equals(request.getParameter("qtEstoque"))){
                d.setQtEstoque(0);
            }else{
                d.setQtEstoque(Integer.parseInt(request.getParameter("qtEstoque")));
            }
            if("".equals(request.getParameter("pzPadraoDevolucao"))){
                d.setPzPadraoDevolucao(0);
            }else{
                d.setPzPadraoDevolucao(Integer.parseInt(request.getParameter("pzPadraoDevolucao")));
            }
            if("".equals(request.getParameter("qtTotal"))){
                d.setQtTotal(0);
            }else{
                d.setQtTotal(Integer.parseInt(request.getParameter("qtTotal")));
            }
            if("".equals(request.getParameter("vrEmprestimo"))){
                d.setVrEmprestimo(new BigDecimal(0));
            }else{
                d.setVrEmprestimo(new BigDecimal(request.getParameter("vrEmprestimo").replaceAll(",", ".") ));
            }
            if("".equals(request.getParameter("qtDiasIniCob"))){
                d.setQtDiasIniCob(0);
            }else{
                d.setQtDiasIniCob(Integer.parseInt(request.getParameter("qtDiasIniCob")));
            }
            if("".equals(request.getParameter("valMaterial"))){
                d.setValMaterial(new BigDecimal(0));
            }else{
                d.setValMaterial(new BigDecimal(request.getParameter("valMaterial").replaceAll(",", ".")));
            }
            if("".equals(request.getParameter("vrDiaria"))){
                d.setVrDiaria(new BigDecimal(0));
            }else{
                d.setVrDiaria(new BigDecimal(request.getParameter("vrDiaria").replaceAll(",", ".") ));
            }

            d.setCdTxAdministrativa(Integer.parseInt(request.getParameter("cdTxAdministrativa")));

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1152", request.getRemoteAddr());

            d.alterar(audit);

            response.sendRedirect("c?app=1150&acao=detalhar&idMaterial=" + idMaterial);
        }
    }
    
    
    @App("1153")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        int idMaterial = Integer.parseInt(request.getParameter("idMaterial"));
        Material d = Material.getInstance(idMaterial);
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1153", request.getRemoteAddr());
        d.excluir(audit);

        response.sendRedirect("c?app=1150");
    }        
    
    @App("1140")
    public static void EmprestimoDevolucao(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        String acao = request.getParameter("acao");
        
        if("listaMateriais".equals(acao)){
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
            
            request.setAttribute("materiais", ComboBoxLoader.listar("TB_MATERIAL"));
            request.setAttribute("socio", socio);
            request.setAttribute("titulo", request.getParameter("titulo"));
            request.setAttribute("emprestimos", Material.listarEmprestimos(matricula, categoria, dependente, request.getParameter("todosEmp")));
            request.setAttribute("materiais", ComboBoxLoader.listar("TB_MATERIAL"));
            request.setAttribute("todosEmp", request.getParameter("todosEmp"));
            request.setAttribute("app1145", request.isUserInRole("1145")); 
            request.getRequestDispatcher("/pages/1140.jsp").forward(request, response);            
        }else if ("EMP".equals(acao)){
            request.setAttribute("lista", Material.listarHistEmprestimos(
                                    Integer.parseInt(request.getParameter("matricula")), 
                                    Integer.parseInt(request.getParameter("idCategoria")), 
                                    Integer.parseInt(request.getParameter("seqDependente"))));
            request.setAttribute("tituloPag", "Histórico de Empréstimo de Material");
            request.setAttribute("acao", "IMPEMP");
            request.setAttribute("titulo", request.getParameter("titulo"));
            request.getRequestDispatcher("/pages/1140-historico.jsp").forward(request, response);            
        }else if ("IMPEMP".equals(acao)){
            ArrayList<String> linhas = new ArrayList<String>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
            DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
            
            linhas.add("IATE CLUBE DE BRASILIA");
            linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
            linhas.add("CGC: 00 018 978/0001-80");
            linhas.add("-------------------------------------");
            linhas.add(" ");
            linhas.add("    ** EMPRESTIMO DE MATERIAL **  ");

            Logger log = Logger.getLogger("techsoft.tabelas.Material");
            Connection cn = null;
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            
            String sql = "SELECT RIGHT('00' + CONVERT(VARCHAR, T3.CD_CATEGORIA), 2) + '/' + RIGHT('0000' + CONVERT(VARCHAR, T3.CD_MATRICULA), 4) + ' - ' + T3.NOME_PESSOA NOME_PESSOA, "
                            + " T1.*, T2.QT_EMPREST_PESSOA QT_EMP "
                      + " FROM TB_PESSOA_TB_MATERIAL T1, TB_EMPRESTIMO_MATERIAL T2, TB_PESSOA T3 "
                      + " WHERE T1.CD_MATRICULA = T3.CD_MATRICULA AND T1.CD_CATEGORIA = T3.CD_CATEGORIA AND T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND "
                      + " T1.SEQ_EMPRESTIMO = T2.SEQ_EMPRESTIMO AND T2.SEQ_EMPRESTIMO = " + request.getParameter("seqEmprestimo");
            
            try {
                cal = cn.prepareCall(sql);
                ResultSet rs = cal.executeQuery();
                if (rs.next()){
                    Material m = Material.getInstance(rs.getInt("CD_MATERIAL"));
                    
                    if (rs.getString("NOME_PESSOA").length()>23){
                        linhas.add(" Socio: " + rs.getString("NOME_PESSOA").substring(0, 23));
                        linhas.add("        " + rs.getString("NOME_PESSOA").substring(23));
                    }else{
                        linhas.add(" Socio: " + rs.getString("NOME_PESSOA"));
                    }
                    if (m.getDescricao().length()>23){
                        linhas.add(" Material: " + m.getDescricao().substring(0, 23));
                        linhas.add("           " + m.getDescricao().substring(23));
                    }else{
                        linhas.add(" Material: " + m.getDescricao());
                    }
                    
                    linhas.add(" Quantidade: " + rs.getString("QT_EMP"));
                    linhas.add(" ");
                    linhas.add(" Usuario: " + rs.getString("USER_EMPRESTIMO"));
                    linhas.add(" ");
                    linhas.add(" Data: " + sdf.format(rs.getTimestamp("DT_EMPREST_PESSOA")));
                    linhas.add(" ");
                    linhas.add(" ");
                    
                    if (m.getVrEmprestimo().compareTo(BigDecimal.ZERO)>0){
                        linhas.add("Pelo emprestimo do material sera ");    
                        linhas.add("cobrado o valor de R$ " + df.format(m.getVrEmprestimo()));
                        linhas.add("lancado automaticamente no ");
                        linhas.add("carne mensal. ");
                        
                        linhas.add(" ");
                    }
                    
                    if (m.getVrDiaria().compareTo(BigDecimal.ZERO)>0){
                        linhas.add("Apos o " + m.getQtDiasIniCob() + "o dia, para cada dia ");
                        linhas.add("adicional de uso do material, ");
                        linhas.add("sera cobrado o valor de R$ " + df.format(m.getVrDiaria()));
                        
                        linhas.add(" ");
                    }
                    if (m.getValMaterial().compareTo(BigDecimal.ZERO)>0){
                       Calendar c = Calendar.getInstance();  
                       c.setTime(rs.getTimestamp("DT_EMPREST_PESSOA"));
                       c.add(Calendar.DATE, m.getPzPadraoDevolucao());
                       
                       linhas.add(" Caso o material nao seja");
                       linhas.add(" devolvido ate " + sdf2.format(c.getTime()));
                       linhas.add(" sera lançado automaticamente");
                       linhas.add(" no carne mensal o valor de");
                       linhas.add(" R$ " + df.format(m.getValMaterial()));
                       linhas.add(" ");
                    }
                    
                    linhas.add(" ");
                    linhas.add(" ");
                    linhas.add(" ------------------------------------");
                    linhas.add("        ASSINATURA DO SOCIO");
                    linhas.add(" ");
                    linhas.add("                - o -     ");
                    
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }finally{
                try {
                    cn.close();
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }
            }
            
            JobMiniImpressora job = new JobMiniImpressora();
            job.texto = linhas;
            String deErro = "";
            try {              
                deErro= request.getRemoteAddr() + "lugar 1";
                Socket sock = new Socket(request.getRemoteAddr(), 49001);
                deErro= deErro + "lugar 2";
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                deErro= deErro + "lugar 3";
                out.writeObject(job);
                deErro= deErro + "lugar 4";
                out.close();
                deErro= deErro + "lugar 5";
                request.getRequestDispatcher("c?app=1140&acao=listaMateriais").forward(request, response);            
            }catch(Exception ex) {
                request.setAttribute("app", "1000");
                request.setAttribute("msg", deErro + ex.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
        }else if ("IMPEMPTELA".equals(acao)){
            request.setAttribute("seqEmprestimo", request.getParameter("seqEmprestimo"));
            request.getRequestDispatcher("/pages/1140-comprovante-emp.jsp").forward(request, response);    
        }else if ("IMPDEV".equals(acao)){
            Material d = null;  
            boolean jaImpSocio = false;
            ArrayList<String> linhas = new ArrayList<String>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
            
            linhas.add("IATE CLUBE DE BRASILIA");
            linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
            linhas.add("CGC: 00 018 978/0001-80");
            linhas.add("-------------------------------------");
            linhas.add(" ");
            linhas.add("     ** DEVOLUCAO DE MATERIAL **  ");
            linhas.add(" ");
            
            
            for(String s : request.getParameterValues("sel")){
                int id = Integer.parseInt(s);
                d = Material.getInstanciaDevolucao(id);
                if (!jaImpSocio){
                    if (d.getNomeSocio().length()>23){
                        linhas.add(" Socio: " + d.getNomeSocio().substring(0, 23));
                        linhas.add("        " + d.getNomeSocio().substring(23));
                    }else{
                        linhas.add(" Socio: " + d.getNomeSocio());
                    }
                    linhas.add(" ");
                }
                jaImpSocio = true;
                
                if (d.getDescricao().length()>23){
                    linhas.add(" Material: " + d.getDescricao().substring(0, 23));
                    linhas.add("           " + d.getDescricao().substring(23));
                }else{
                    linhas.add(" Material: " + d.getDescricao());
                }
                linhas.add(" Quantidade: " + d.getQtTotal());
                linhas.add(" Dt Emp:: " + sdf.format(d.getDtEmprestimo()));
                linhas.add(" Dt Devol: " + sdf.format(d.getDtDevolucao()));
                linhas.add(" ");
                
                if (d.getVrEmprestimo().compareTo(BigDecimal.ZERO)>0){
                    linhas.add(" Lançado no proximo carne o");
                    linhas.add(" valor de R$: " + df.format(d.getVrEmprestimo()));
                    linhas.add(" referente ao emprestimo do material.");
                    linhas.add(" ");
                }
                if (d.getVrDiaria().compareTo(BigDecimal.ZERO)>0){
                    linhas.add(" Lançado no proximo carne o");
                    linhas.add(" valor de R$: " + df.format(d.getVrDiaria()));
                    linhas.add(" referente a utilizacao do material.");
                    linhas.add(" ");
                }
                if (d.getValMaterial().compareTo(BigDecimal.ZERO)>0){
                    linhas.add(" Lancado no proximo carne o");
                    linhas.add(" valor de R$: " + df.format(d.getValMaterial()));
                    linhas.add(" referente a restituicao do material.");
                    linhas.add(" ");
                }
                
            }
            
            linhas.add(" Usuario: " + d.getUsuarioDevol());
            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ------------------------------------");
            linhas.add("        ASSINATURA DO SOCIO");
            linhas.add(" ");
            linhas.add("                - o -     ");
            
            JobMiniImpressora job = new JobMiniImpressora();
            job.texto = linhas;
            try {        
                Socket sock = new Socket(request.getRemoteAddr(), 49001);
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                out.writeObject(job);
                out.close();
                request.getRequestDispatcher("c?app=1140&acao=listaMateriais").forward(request, response);            
            }catch(Exception ex) {
                request.setAttribute("app", "1000");
                request.setAttribute("msg", ex.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }else if ("IMPDEVTELA".equals(acao)){
            request.setAttribute("seqs", request.getParameterValues("sel"));
            request.getRequestDispatcher("/pages/1140-comprovante-dev.jsp").forward(request, response);    
            
        }else if ("DEV".equals(acao)){

            request.setAttribute("lista", Material.listarHistDevolucoes(
                                    Integer.parseInt(request.getParameter("matricula")), 
                                    Integer.parseInt(request.getParameter("idCategoria")), 
                                    Integer.parseInt(request.getParameter("seqDependente"))));
            request.setAttribute("titulo", "Histórico de Devolução de Material");
            request.setAttribute("acao", "IMPDEV");
            request.setAttribute("titulo", request.getParameter("titulo"));
            request.getRequestDispatcher("/pages/1140-historico.jsp").forward(request, response);            
            
        }else if("consultar".equals(acao)){
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

            List<Socio> socios = Socio.listarEmpMat(carteira, categoria, matricula, nome);
            request.setAttribute("socios", socios);

            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));

            request.getRequestDispatcher("/pages/1140-lista.jsp").forward(request, response);            
        }else{
            request.getRequestDispatcher("/pages/1140-lista.jsp").forward(request, response);
        }
    }        

    @App("1141")
    public static void emprestimo(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1141", request.getRemoteAddr());    
        
        Material m = Material.getInstance(Integer.parseInt(request.getParameter("idMaterial")));
        
        int idEmprestimo = Material.emprestimo(
                Integer.parseInt(request.getParameter("matricula")), 
                Integer.parseInt(request.getParameter("idCategoria")), 
                Integer.parseInt(request.getParameter("seqDependente")), 
                Integer.parseInt(request.getParameter("quantidade")), 
                request.getUserPrincipal().getName(), 
                Integer.parseInt(request.getParameter("idMaterial")), 
                request.getParameter("senha"), 
                m.getVrEmprestimo(),
                audit);
        
        request.getRequestDispatcher("c?app=1140&seqEmprestimo="+idEmprestimo+"&acao=IMPEMP").forward(request, response);            
    }

    
    @App("1161")
    public static void devolucao(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {                
        Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1161", request.getRemoteAddr());        
        
        for(String s : request.getParameterValues("sel")){
            Material m = new Material();
            m.setId(Integer.parseInt(s));
            m.setUsuario(request.getUserPrincipal().getName());

            m.devolucao(audit);
        }
        request.getRequestDispatcher("c?app=1140&seqEmprestimo="+request.getParameterValues("sel")+"&acao=IMPDEV").forward(request, response);            
    }
    
    @App("1890")
    public static void relatorioEmprestimo(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("visualizar".equals(acao)){
            StringBuilder sql = new StringBuilder();
            StringBuilder sqlWhere = new StringBuilder();

            sqlWhere.append("From ");
            sqlWhere.append("TB_MATERIAL    T1, ");
            sqlWhere.append("TB_PESSOA      T2, ");
            sqlWhere.append("TB_PESSOA_TB_MATERIAL T3 ");
            sqlWhere.append("Where ");
            sqlWhere.append("T1.CD_MATERIAL    = T3.CD_MATERIAL     AND ");
            sqlWhere.append("T2.CD_MATRICULA      = T3.CD_MATRICULA    AND ");
            sqlWhere.append("T2.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE     AND ");
            sqlWhere.append("T2.CD_CATEGORIA = T3.CD_CATEGORIA ");
            
            int categoria = 0;
            try{
                categoria = Integer.parseInt(request.getParameter("categoria"));
            }catch(NumberFormatException e){
                categoria = 0;
            }
            if(categoria > 0){
                sqlWhere.append(" AND T2.CD_CATEGORIA = ");
                sqlWhere.append(categoria);
            }
                
            int matricula = 0;
            try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
            }catch(NumberFormatException e){
                matricula = 0;
            }
            if(matricula > 0){
                sqlWhere.append(" AND T2.CD_MATRICULA = ");
                sqlWhere.append(matricula);
            }
            
            String nome = request.getParameter("nome");
            if(nome != null && !nome.trim().equals("")){
                sqlWhere.append(" AND T2.NOME_PESSOA LIKE '");
                sqlWhere.append(nome);
                sqlWhere.append("%'");
            }
            
            int idMaterial = Integer.parseInt(request.getParameter("idMaterial"));
            if(idMaterial > 0){
                sqlWhere.append(" AND T1.CD_MATERIAL = ");
                sqlWhere.append(idMaterial);
            }            
            
            Boolean emprestado = Boolean.parseBoolean(request.getParameter("emprestado"));
            Boolean devolvido = Boolean.parseBoolean(request.getParameter("emprestado"));
            
            if(emprestado){
                if(!devolvido){
                    sqlWhere.append(" AND T3.DT_REAL_DEVOLUCAO IS NULL ");
                }
            }else{
                sqlWhere.append(" AND T3.DT_REAL_DEVOLUCAO IS NOT NULL ");
            }

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

            Date d1 = Datas.parse(request.getParameter("dataInicio"));
            Date d2 = Datas.parse(request.getParameter("dataFim"));
            
            if(request.getParameter("tipoPeriodo").equals("E")){
                if(d1 != null){
                    sqlWhere.append(" AND T3.DT_EMPREST_PESSOA >= '");
                    sqlWhere.append(fmt.format(d1));
                    sqlWhere.append(" 00:00:00'");
                }
                if(d2 != null){
                    sqlWhere.append(" AND T3.DT_EMPREST_PESSOA <= '");
                    sqlWhere.append(fmt.format(d2));
                    sqlWhere.append(" 23:59:59'");
                }            
            }else{
                if(d1 != null){
                    sqlWhere.append(" AND T3.DT_REAL_DEVOLUCAO >= '");
                    sqlWhere.append(fmt.format(d1));
                    sqlWhere.append(" 00:00:00'");
                }
                if(d2 != null){
                    sqlWhere.append(" AND T3.DT_REAL_DEVOLUCAO <= '");
                    sqlWhere.append(fmt.format(d2));
                    sqlWhere.append(" 23:59:59'");
                }                            
            }          
            
            sql.append("SELECT ");
            sql.append("T1.DESCR_MATERIAL   as 'Material' , ");
            sql.append("T2.NOME_PESSOA      as 'Pessoa', ");
            sql.append("T3.DT_EMPREST_PESSOA AS 'Dt. Emprestimo', ");
            sql.append("T3.DT_REAL_DEVOLUCAO AS 'Dt. Devolucao', ");
            sql.append("NULL AS 'Dt. Max Devolucao', ");
            sql.append("T3.VR_DIARIA         AS 'Vr. Empr.', ");
            sql.append("T3.VR_RESTITUICAO    AS 'Vr. Restituicao', ");
            sql.append("T3.USER_EMPRESTIMO   AS 'User Emp.', ");
            sql.append("T3.USER_DEVOLUCAO    AS 'User Dev.' ");
            sql.append(sqlWhere);
            sql.append(" AND T3.DT_REAL_DEVOLUCAO IS NOT NULL ");           

            sql.append(" UNION ALL ");

            sql.append("SELECT ");
            sql.append("T1.DESCR_MATERIAL   as 'Material' , ");
            sql.append("T2.NOME_PESSOA      as 'Pessoa', ");
            sql.append("T3.DT_EMPREST_PESSOA AS 'Dt. Emprestimo', ");
            sql.append("T3.DT_REAL_DEVOLUCAO AS 'Dt. Devolucao', ");
            sql.append("DATEADD(d, PZ_PADRAO_DEVOLUCAO, T3.DT_EMPREST_PESSOA) AS 'Dt. Max Devolucao', ");
            sql.append("CASE ");
            sql.append("WHEN T1.VR_DIARIA IS NULL THEN ");
            sql.append("   NULL ");
            sql.append("Else ");
            sql.append("   (DATEDIFF(d, T3.DT_EMPREST_PESSOA, getdate()) + 1) * T1.VR_DIARIA ");
            sql.append("END AS 'Vr. Empr.', ");
            sql.append("CASE ");
            sql.append("WHEN DATEADD(d, PZ_PADRAO_DEVOLUCAO, T3.DT_EMPREST_PESSOA) <= CONVERT(DATETIME, CONVERT(VARCHAR, MONTH(GETDATE())) + '/' + CONVERT(VARCHAR, DAY(GETDATE())) + '/' + CONVERT(VARCHAR, YEAR(GETDATE())) + ' 23:59:59') THEN ");
            sql.append("   T1.VAL_MATERIAL ");
            sql.append("Else ");
            sql.append("   NULL ");
            sql.append("END AS 'Vr. Restituicao', ");
            sql.append("T3.USER_EMPRESTIMO   AS 'User Emp.', ");
            sql.append("T3.USER_DEVOLUCAO    AS 'User Dev.' ");
            sql.append(sqlWhere);
            sql.append(" AND T3.DT_REAL_DEVOLUCAO IS NULL ");           
            sql.append("ORDER By 1, 2, 3");
            
            request.setAttribute("titulo", "Relatório de Empréstimo de Material");
            request.setAttribute("titulo2", "");
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "true");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                        
        }else{
            request.setAttribute("materiais", ComboBoxLoader.listar("TB_MATERIAL"));
            request.getRequestDispatcher("/pages/1890.jsp").forward(request, response);            
        }
    }

}
