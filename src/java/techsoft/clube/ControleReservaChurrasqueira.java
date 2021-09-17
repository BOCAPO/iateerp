package techsoft.clube;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.operacoes.ReservaChurrasqueira;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;

@Controller
public class ControleReservaChurrasqueira{
    

   @App("1065")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("consultar".equals(acao)){     
            String dtUtilizacao = request.getParameter("dtUtilizacao");

            List<ReservaChurrasqueira> reservas = ReservaChurrasqueira.listar(dtUtilizacao);
            request.setAttribute("reservas", reservas);

            request.setAttribute("dtUtil", request.getParameter("dtUtilizacao"));
            Date dataUtil = Datas.parse(request.getParameter("dtUtilizacao"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
            Date dataAtual = null;
            try{
                dataAtual = sdf.parse(sdf.format(new Date()));
            }catch(Exception e){}

            String podeReservar = "S";
            if (dataUtil.after(dataAtual) || dataUtil.equals(dataAtual)){
                 podeReservar = "S";
            }else{
                 podeReservar = "N";
            }
            
            request.setAttribute("podeReservar", podeReservar);
            request.setAttribute("usuarioAtual", request.getUserPrincipal().getName());
            request.getRequestDispatcher("/pages/1065-lista.jsp").forward(request, response);
        }else{
            request.setAttribute("usuarioAtual", request.getUserPrincipal().getName());
            request.getRequestDispatcher("/pages/1065-lista.jsp").forward(request, response);
        }
        
        
    }
    
    @App("1066")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("consultarSocio".equals(acao)){
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

            List<Socio> socios = Socio.listarResChu(carteira, categoria, matricula, nome);
            request.setAttribute("socios", socios);
            
            request.getRequestDispatcher("/pages/1066-selSocio.jsp").forward(request, response);
            
        }else if("mostraData".equals(acao)){
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

            request.setAttribute("socio", socio);
            request.setAttribute("titulo", request.getParameter("titulo"));
            String supervisao = request.getParameter("supervisao");
            request.setAttribute("supervisao", supervisao);
            request.setAttribute("acao", acao);
            request.setAttribute("meses", ComboBoxLoader.listarSql("EXEC SP_RECUPERA_MES_RESERVA"));

            request.getRequestDispatcher("/pages/1067-incluir.jsp").forward(request, response);
            
        }else if("mostraChurrasqueiras".equals(acao)){
            
            int dia = 0;
            int mes = 0;
            int ano = 0;
            String dtReserva = "";
            DecimalFormat df2 = new DecimalFormat( "00" );
                    
            String[] mesAno = request.getParameter("mes").toString().split(";");
            dia = Integer.parseInt(request.getParameter("dia"));
            
            dtReserva = mesAno[1] + "-" + df2.format(Integer.parseInt(mesAno[0])) + "-" + df2.format(dia);
                    
            request.setAttribute("parmMes", request.getParameter("mes"));
            request.setAttribute("parmDia", request.getParameter("dia"));
            request.setAttribute("dtUtil", dtReserva);
            request.setAttribute("parmHorario", request.getParameter("horario"));

            int matricula = 0;
            int categoria = 0;
            int dependente = 0;
            categoria = Integer.parseInt(request.getParameter("categoria"));
            matricula = Integer.parseInt(request.getParameter("matricula"));
            dependente = Integer.parseInt(request.getParameter("dependente"));
            
            Socio socio = Socio.getInstance(matricula, dependente, categoria);

            request.setAttribute("socio", socio);
            request.setAttribute("acao", acao);
            
            String supervisao = request.getParameter("supervisao");
            request.setAttribute("supervisao", supervisao);
            request.setAttribute("meses", ComboBoxLoader.listarSql("EXEC SP_RECUPERA_MES_RESERVA"));
            
            request.getRequestDispatcher("/pages/1067-incluir.jsp").forward(request, response);

        }else{
           request.getRequestDispatcher("/pages/1066-selSocio.jsp").forward(request, response);
        }       
    }        
}
