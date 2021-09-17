package techsoft.clube;

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
import java.util.logging.Logger;
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
import techsoft.tabelas.Armario;
import techsoft.tabelas.CategoriaNautica;
import techsoft.util.Datas;
import techsoft.carteirinha.JobMiniImpressora;
import techsoft.db.Pool;
import techsoft.operacoes.ControleMaterial;
import techsoft.tabelas.Material;

@Controller
public class ControleEmprestimoAcademia{

    @App("2540")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            request.setAttribute("lista", ArmarioAcademia.listar());
            int idTipoArmario = 0;
            try{
                idTipoArmario = Integer.parseInt(request.getParameter("idTipoArmario"));
            }catch(Exception e){
                idTipoArmario = TipoArmarioAcademia.listar().get(0).getId();
            }
            
            request.setAttribute("idTipoArmario", idTipoArmario);
            request.setAttribute("tipoArmarios", TipoArmarioAcademia.listar());
            request.getRequestDispatcher("/pages/2540-lista.jsp").forward(request, response);
    }
    
    @App("2541")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("showForm".equals(acao)){
            
            // aqui eh usado tanto para inclusao de armario com toalhas quanto soh pra toalhas, quando eh soh pra toalhas o armario vem zerado
            // tambem eh usado para emprestimos e devolucoes, quando eh devolucao o codigo do emprestimo vem preenchido, caso contrario em branco
            int armario;
            try{
                armario = Integer.parseInt(request.getParameter("armario"));
            }catch(Exception e){
                armario = 0;
            }
            int emprestimo;
            try{
                emprestimo = Integer.parseInt(request.getParameter("emprestimo"));
            }catch(Exception e){
                emprestimo = 0;
            }
            
            request.setAttribute("armario", ArmarioAcademia.getInstance(armario));
            request.setAttribute("emprestimo", EmprestimoAcademia.getInstance(emprestimo));
            request.setAttribute("taxas", ComboBoxLoader.listarSql("SELECT * FROM tb_taxa_administrativa T1, TB_USUARIO_TAXA_INDIVIDUAL T2 WHERE T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA AND T1.ind_taxa_administrativa = 'I' AND T2.USER_ACESSO_SISTEMA = '" + request.getUserPrincipal().getName() + "' ORDER BY 2"));
            
            request.getRequestDispatcher("/pages/2540.jsp").forward(request, response);
        }else if("gravar".equals(acao)){
            int armario;
            try{
                armario = Integer.parseInt(request.getParameter("armario"));
            }catch(Exception e){
                armario = 0;
            }
            
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int categoria = Integer.parseInt(request.getParameter("categoria"));
            int dependente = Integer.parseInt(request.getParameter("dependente"));
            int qtToalha;
            try{
                qtToalha = Integer.parseInt(request.getParameter("qtToalha"));
            }catch(Exception e){
                qtToalha = 0;
            }
            
            EmprestimoAcademia e = new EmprestimoAcademia();
            e.setPessoa(Socio.getInstance(matricula, dependente, categoria));

            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2541", request.getRemoteAddr());
            try{
                e.inserir(audit);
                
                ItemEmprestimoAcademia i = new ItemEmprestimoAcademia();
                i.setEmprestimoAcademia(e);
                
                if(armario>0){
                    i.setArmario(ArmarioAcademia.getInstance(armario));
                    i.inserir(audit);
                }
                
                i.setArmario(null);
                
                for(int j = 0; j < qtToalha; j++){
                    i.setItem(1);
                    i.inserir(audit);
                }

                //carregando novamente para pegar a data do emprestimo para impressao
                e = EmprestimoAcademia.getInstance(e.getId());
                ImprimeEmprestimo(e, request, response);
                
                response.sendRedirect("c?app=2540");

            }catch(Exception x){
                request.setAttribute("err", x);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }else if("devolver".equals(acao)){
            
            // aqui eh usado tanto para devolucao de 1 item como para devolucao de todos os itens do emprestimo
            int item;
            try{    
                item = Integer.parseInt(request.getParameter("id"));
            }catch(Exception e){
                item = 0;
            }
            int emprestimo;
            try{
                emprestimo = Integer.parseInt(request.getParameter("idEmprestimo"));
            }catch(Exception e){
                emprestimo = 0;
            }
            
            boolean gerarMulta = Boolean.parseBoolean(request.getParameter("gerarMulta"));
            BigDecimal valorMulta = BigDecimal.ZERO;
            int cdTxAdm = 0;
            if(gerarMulta){
                String valorMultaTXT = request.getParameter("valorMultaParm");
                valorMulta = new BigDecimal(valorMultaTXT.replace(".", "").replace(",", "."));
                
                cdTxAdm = Integer.parseInt(request.getParameter("taxa"));
                
            }
            
            String tipoDevolucao = request.getParameter("tipoDevolucao");
            if(tipoDevolucao == null || tipoDevolucao.equals("")){
                tipoDevolucao = "T";
            }
                    
            
            ItemEmprestimoAcademia i = new ItemEmprestimoAcademia();
            i.setEmprestimoAcademia(EmprestimoAcademia.getInstance(emprestimo));
            i.setId(item);
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "2542", request.getRemoteAddr());
            try{
                i.devolver(audit, tipoDevolucao, valorMulta, cdTxAdm);
                
                if(emprestimo==0){
                    i = ItemEmprestimoAcademia.getInstance(item);
                    ImprimeDevolucao(i.getEmprestimoAcademia(), request, response);
                }else{
                    ImprimeDevolucao(EmprestimoAcademia.getInstance(emprestimo), request, response);
                }
                
                if(emprestimo>0){
                    response.sendRedirect("c?app=2540");
                }else{
                    response.sendRedirect("c?app=2541&acao=showForm");
                }
                

            }catch(Exception x){
                request.setAttribute("err", x);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        
        }
    }
    
    public static void ImprimeEmprestimo(EmprestimoAcademia emprestimo, HttpServletRequest request, HttpServletResponse response){
        ArrayList<String> linhas = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );

        linhas.add("IATE CLUBE DE BRASILIA");
        linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
        linhas.add("CGC: 00 018 978/0001-80");
        linhas.add("-------------------------------------");
        linhas.add(" ");
        linhas.add("** EMPRESTIMO DE MATERIAL ACADEMIA **");
        linhas.add(" ");

        Logger log = Logger.getLogger("techsoft.tabelas.EmprestimoAcademia");
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        CallableStatement cal = null;
        
        try {

            if (emprestimo.getPessoa().getNome().length() >23){
                linhas.add(" Socio: " + emprestimo.getPessoa().getNome().substring(0, 23));
                linhas.add("        " + emprestimo.getPessoa().getNome().substring(23));
            }else{
                linhas.add(" Socio: " + emprestimo.getPessoa().getNome());
            }
            
            String sql = "SELECT " +
                                "(SELECT DESCR_ARMARIO FROM TB_ARMARIO_ACADEMIA T0 WHERE T0.CD_ARMARIO = T1.CD_ARMARIO) DESCR_ARMARIO, " +
                                "(SELECT DESCR_TIPO_ARMARIO FROM TB_TIPO_ARMARIO_ACADEMIA TX, TB_ARMARIO_ACADEMIA TY WHERE TX.CD_TIPO_ARMARIO = TY.CD_TIPO_ARMARIO AND TY.CD_ARMARIO = T1.CD_ARMARIO) DESCR_TIPO_ARMARIO, " +
                                "CD_ITEM, " + 
                                "COUNT(*) QT_TOALHAS " + 
                         " FROM TB_ITEM_EMPRESTIMO_ACADEMIA T1 " +
                         " WHERE NU_SEQ_EMPRESTIMO = " + emprestimo.getId() + 
                         " GROUP BY CD_ARMARIO, CD_ITEM ";
            
            cal = cn.prepareCall(sql);
            ResultSet rs = cal.executeQuery();
            while (rs.next()){
                
                if(rs.getString("DESCR_ARMARIO") != null){
                    linhas.add(" No.: " + rs.getString("DESCR_TIPO_ARMARIO") + " " + rs.getString("DESCR_ARMARIO"));
                }
                
                if (rs.getString("CD_ITEM") != null){
                    int qtToalha;
                    try{
                        qtToalha = rs.getInt("QT_TOALHAS");
                    }catch(Exception e){
                        qtToalha = 0;
                    }
                    if(qtToalha > 0){
                        linhas.add(" Qt. Toalhas: " + String.valueOf(qtToalha));
                    }
                }
                
            }

            //linhas.add(" ");
            //linhas.add(" Usuario: " + rs.getString("USER_EMPRESTIMO"));
            linhas.add(" ");
            linhas.add(" Data: " + sdf.format(emprestimo.getDtEmprestimo()));
            linhas.add(" ");
            linhas.add(" ");

            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ------------------------------------");
            linhas.add("        ASSINATURA DO SOCIO");
            linhas.add(" ");
            linhas.add("                - o -     ");

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
            try{
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }catch(Exception ex2){}
        }
        
        
    }
    
    public static void ImprimeDevolucao(EmprestimoAcademia emprestimo, HttpServletRequest request, HttpServletResponse response){
        ArrayList<String> linhas = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );

        linhas.add("IATE CLUBE DE BRASILIA");
        linhas.add("SCE/NORTE, TRECHO ENSEADA-02, LOTE 02");
        linhas.add("CGC: 00 018 978/0001-80");
        linhas.add("-------------------------------------");
        linhas.add(" ");
        linhas.add("** DEVOLUCAO DE MATERIAL ACADEMIA **");

        Logger log = Logger.getLogger("techsoft.tabelas.EmprestimoAcademia");
        Connection cn = null;
        cn = Pool.getInstance().getConnection();
        CallableStatement cal = null;
        
        try {

            if (emprestimo.getPessoa().getNome().length() >23){
                linhas.add(" Socio: " + emprestimo.getPessoa().getNome().substring(0, 23));
                linhas.add("        " + emprestimo.getPessoa().getNome().substring(23));
            }else{
                linhas.add(" Socio: " + emprestimo.getPessoa().getNome());
            }
            
            linhas.add(" Data Emprestimo: " + sdf.format(emprestimo.getDtEmprestimo()));
            linhas.add(" ");
            
            String sql = "SELECT "
                            + "(SELECT DESCR_ARMARIO FROM TB_ARMARIO_ACADEMIA T0 WHERE T0.CD_ARMARIO = T1.CD_ARMARIO) DESCR_ARMARIO, " 
                            + "(SELECT DESCR_TIPO_ARMARIO FROM TB_TIPO_ARMARIO_ACADEMIA TX, TB_ARMARIO_ACADEMIA TY WHERE TX.CD_TIPO_ARMARIO = TY.CD_TIPO_ARMARIO AND TY.CD_ARMARIO = T1.CD_ARMARIO) DESCR_TIPO_ARMARIO, " 
                            + "CD_ITEM, "
                            + "DT_DEVOLUCAO, "
                            + "COUNT(*) QT_TOALHAS "
                    + " FROM TB_ITEM_EMPRESTIMO_ACADEMIA T1 "
                    + " WHERE NU_SEQ_EMPRESTIMO = " + emprestimo.getId() + " AND CD_SITUACAO = 'D' "
                    + " GROUP BY CD_ARMARIO, CD_ITEM, DT_DEVOLUCAO "
                    + " ORDER BY CD_ARMARIO DESC, DT_DEVOLUCAO DESC";
            
            cal = cn.prepareCall(sql);
            ResultSet rs = cal.executeQuery();
            while (rs.next()){
                if(rs.getString("DESCR_ARMARIO") != null){
                    linhas.add(" No.: " + rs.getString("DESCR_TIPO_ARMARIO") + " " + rs.getString("DESCR_ARMARIO"));
                }
                
                if (rs.getString("CD_ITEM") != null){
                    int qtToalha;
                    try{
                        qtToalha = rs.getInt("QT_TOALHAS");
                    }catch(Exception e){
                        qtToalha = 0;
                    }
                    if(qtToalha > 0){
                        linhas.add(" Qt. Toalhas: " + String.valueOf(qtToalha));
                    }
                }
                
                linhas.add(" Data Devolução: " + sdf.format(rs.getTimestamp("DT_DEVOLUCAO")));
                linhas.add(" ");
                
            }


            linhas.add(" ");
            linhas.add(" ");
            linhas.add(" ------------------------------------");
            linhas.add("        ASSINATURA DO SOCIO");
            linhas.add(" ");
            linhas.add("                - o -     ");

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
            try{
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }catch(Exception ex2){}
        }
        
    }
            
    
}
