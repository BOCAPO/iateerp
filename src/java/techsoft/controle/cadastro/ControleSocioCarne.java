package techsoft.controle.cadastro;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioCarne;
import techsoft.cadastro.SocioCarneDetalhe;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;


@Controller
public class ControleSocioCarne{

    private static final Logger log = Logger.getLogger("techsoft.controle.cadastro.ControleSocioCarne");
    
    @App("1360")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("detalhar".equals(acao)){
            Socio s = Socio.getInstance(Integer.parseInt(request.getParameter("matricula")), 0, Integer.parseInt(request.getParameter("idCategoria")));
            request.setAttribute("socio", s);
            int idCarne = Integer.parseInt(request.getParameter("idCarne"));
            request.setAttribute("detalhes", SocioCarneDetalhe.listar(idCarne));
            request.setAttribute("matricula", request.getParameter("matricula"));
            request.setAttribute("idCategoria", request.getParameter("idCategoria"));
            
            request.getRequestDispatcher("/pages/1360.jsp").forward(request, response);
            
            String tipo = request.getParameter("tipo");
            if (tipo==null || tipo.equals("")){
                tipo = "C";
            }
            request.setAttribute("tipo", tipo);
            
        }else if("trancar".equals(acao)){
            int idCarne = Integer.parseInt(request.getParameter("idCarne"));
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1362", request.getRemoteAddr());
            SocioCarne.trancar(audit, idCarne);
            response.sendRedirect("c?app=1360&matricula=" + matricula + "&idCategoria=" + idCategoria + "&seqDependente=" + seqDependente);
        }else if("destrancar".equals(acao)){
            int idCarne = Integer.parseInt(request.getParameter("idCarne"));
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1363", request.getRemoteAddr());
            SocioCarne.destrancar(audit, idCarne);
            response.sendRedirect("c?app=1360&matricula=" + matricula + "&idCategoria=" + idCategoria + "&seqDependente=" + seqDependente);
        }else if("showFormImprimirCarteira".equals(acao)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));

            if (request.getParameter("tipo").equals("C")){
                request.setAttribute("matricula", matricula);
                request.setAttribute("idCategoria", idCategoria);

                request.getRequestDispatcher("/pages/1361.jsp").forward(request, response);            
            }else if (request.getParameter("tipo").equals("B")){
                DecimalFormat f1 = new DecimalFormat("00");
                DecimalFormat f2 = new DecimalFormat("0000");
                
                String sql = "SP_BOLETO_AVULSO 'R', null, null, " + matricula + ", " + idCategoria;
                
                Socio s = Socio.getInstance(matricula, 0, idCategoria);
                
                request.setAttribute("titulo", "Relação de Boletos Avulsos do Título");
                request.setAttribute("titulo2", "Título: " + f1.format(s.getIdCategoria()) + "/" + f2.format(s.getMatricula()) + " - " + s.getNome());
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "false");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "-1");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                        
            }else {
                DecimalFormat f1 = new DecimalFormat("00");
                DecimalFormat f2 = new DecimalFormat("0000");
                
                String sql = "SP_REL_CREDITO_PESSOA " + matricula + ", " + idCategoria;
                
                Socio s = Socio.getInstance(matricula, 0, idCategoria);
                
                request.setAttribute("titulo", "Relação de Créditos do Título");
                request.setAttribute("titulo2", "Título: " + f1.format(s.getIdCategoria()) + "/" + f2.format(s.getMatricula()) + " - " + s.getNome());
                request.setAttribute("sql", sql);
                request.setAttribute("quebra1", "false");
                request.setAttribute("quebra2", "false");
                request.setAttribute("total1", "-1");
                request.setAttribute("total2", "-1");
                request.setAttribute("total3", "-1");
                request.setAttribute("total4", "-1");

                request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                        
            }
        }else if("imprimirCarteira".equals(acao)){
            DecimalFormat f1 = new DecimalFormat("00");
            DecimalFormat f2 = new DecimalFormat("0000");
            
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            Socio s = Socio.getInstance(matricula, 0, idCategoria);
            
            int mesInicio = Integer.parseInt(request.getParameter("mesInicio")); 
            int mesFim = Integer.parseInt(request.getParameter("mesFim")); 
            int anoInicio = Integer.parseInt(request.getParameter("anoInicio")); 
            int anoFim = Integer.parseInt(request.getParameter("anoFim")); 

            Calendar cal = Calendar.getInstance();           
            cal.set(Calendar.DATE, 1);
            cal.set(Calendar.MONTH, mesInicio-1);
            cal.set(Calendar.YEAR, anoInicio); 
            Date dataInicio = cal.getTime();

            cal.set(Calendar.DATE, 1);
            cal.set(Calendar.MONTH, mesFim);
            cal.set(Calendar.YEAR, anoFim); 
            Date dataFim = cal.getTime();

            
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

            String st1 = fmt.format(dataInicio);    
            String st2 = fmt.format(dataFim);
            
            String sql = "SP_REC_CARNE_TITULO "
                    + matricula + ", "
                    + idCategoria + ", '"
                    + st1 + "', '"
                    + st2 + "'";
            
            
            request.setAttribute("titulo", "Relação de Carnes do Título");
            request.setAttribute("titulo2", "Título: " + f1.format(s.getIdCategoria()) + "/" + f2.format(s.getMatricula()) + " - " + s.getNome());
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "true");
            request.setAttribute("quebra2", "false");
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");
            
            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);                        
        }else if("showFormImprimirBoleto".equals(acao)){
            
            SocioCarne s = SocioCarne.getInstance(Integer.parseInt(request.getParameter("idCarne")));
            
            Date dataPagamento = null;
            if (request.getParameter("dtPagamento") == null){
                dataPagamento = new java.sql.Date(new Date().getTime());
                if (dataPagamento.before(s.getDataVencimento())){
                    dataPagamento = s.getDataVencimento();
                }
                
            }else{
                dataPagamento = Datas.parse(request.getParameter("dtPagamento"));
            }
            String banco = request.getParameter("banco");
            if (banco == null){
                banco = "";
            }
            String semEncargos = request.getParameter("semEncargos");
            if (semEncargos == null){
                semEncargos = "";
            }
            String semDebAnt = request.getParameter("semDebAnt");
            if (semDebAnt == null){
                semDebAnt = "";
            }
            
            SocioCarne s2 = SocioCarne.dadosBoleto(Integer.parseInt(request.getParameter("idCarne")), dataPagamento, banco, semEncargos, semDebAnt);
            
            request.setAttribute("carne", s2);
            request.setAttribute("dataPagamento", dataPagamento);
            request.setAttribute("banco", request.getParameter("banco"));
            request.setAttribute("semEncargos", request.getParameter("semEncargos"));
            request.setAttribute("semDebAnt", request.getParameter("semDebAnt"));
            
            request.getRequestDispatcher("/pages/1360-Parametro-Boleto.jsp").forward(request, response);                        
        }else if("imprimeBoleto".equals(acao)){
            
            SocioCarne s = SocioCarne.dadosBoleto(Integer.parseInt(request.getParameter("idCarne")), Datas.parse(request.getParameter("dtPagamento")), request.getParameter("banco"), request.getParameter("semEncargos"), request.getParameter("semDebAnt"));
            request.setAttribute("s", s);

            request.setAttribute("parc", SocioCarne.parcelaBoleto(Integer.parseInt(s.getNossoNumero()), s.getDebAnt(), s.getEncargos(), request.getParameter("declaracaoQuitacao")));
            request.setAttribute("codBar", SocioCarne.i25Encode(s.getCodBarras()));
            
            if(request.getParameter("banco").equals("BB")){
                request.getRequestDispatcher("/pages/1360-boleto.jsp").forward(request, response);                        
            }else{
                request.getRequestDispatcher("/pages/1360-boleto-itau.jsp").forward(request, response);                        
            }
            
        }else if("showFormImprimirNadaConsta".equals(acao)){
            
            request.setAttribute("idCarne", request.getParameter("idCarne"));
            request.setAttribute("usuarioAtual", request.getUserPrincipal().getName());
            request.setAttribute("maquina", request.getRemoteAddr());
            
            request.getRequestDispatcher("/pages/1360-NadaConsta.jsp").forward(request, response);            
            
        }else{
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            String tipo = request.getParameter("tipo");
            if (tipo==null){
                tipo = "C";
            }
            
            Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);

            request.setAttribute("socio", s);
            request.getSession().setAttribute("carnes", SocioCarne.listar(s));
            request.getSession().setAttribute("matricula", request.getParameter("matricula"));
            request.getSession().setAttribute("idCategoria", request.getParameter("idCategoria"));
            request.setAttribute("tipo", tipo);
            
            request.setAttribute("taxasAdministrativas", ComboBoxLoader.listarSql("SELECT * FROM TB_TAXA_ADMINISTRATIVA WHERE IND_TAXA_ADMINISTRATIVA = 'C'"));
            request.setAttribute("usuarioAtual", request.getUserPrincipal().getName());
            
            request.getRequestDispatcher("/pages/1360.jsp").forward(request, response);            
        }
    }
        
}
