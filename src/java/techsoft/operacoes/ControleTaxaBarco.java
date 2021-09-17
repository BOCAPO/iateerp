package techsoft.operacoes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import techsoft.seguranca.Usuario;
import techsoft.tabelas.CategoriaNautica;
import techsoft.tabelas.Curso;
import techsoft.tabelas.InserirException;
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;

@Controller
public class ControleTaxaBarco{
    

    @App("1430")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        String idTipoVaga = request.getParameter("idTipoVaga");
        if (idTipoVaga==""||idTipoVaga==null){
            idTipoVaga="CO";
        }
        String tipo = "AT";
        String dtRef = "";
        
        if (request.getParameter("tipo")!=null){
            tipo = request.getParameter("tipo");
        }
        if (request.getParameter("dtRef")==null){
            Date d = new Date();
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            
            dtRef = f.format(d);
        }else{
            dtRef = request.getParameter("dtRef");
        }
                
        request.setAttribute("barcos", TaxaBarco.consultar(idTipoVaga, tipo, dtRef));
        request.setAttribute("tipo", tipo);
        request.setAttribute("dtRef", dtRef);
        request.setAttribute("tiposVagas", ComboBoxLoader.listar("TB_TIPO_VAGA_BARCO"));
        request.setAttribute("idTipoVaga", idTipoVaga);
        request.getRequestDispatcher("/pages/1430-lista.jsp").forward(request, response);
    }

    @App("1431")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("gravar".equals(acao)){
            String idTipoVaga = request.getParameter("idTipoVaga");
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio"));
            Date dtFim = null;
            if (!request.getParameter("dtFim").equals("")){
                dtFim = Datas.parseDataHora(request.getParameter("dtFim"));
            }
            float valor;
            try{
                valor = Float.parseFloat(request.getParameter("valor").replaceAll(",", "."));
            }catch(Exception e){
                valor = Float.parseFloat(request.getParameter("valor").replaceAll(",", "."));
            }
            
            TaxaBarco j = new TaxaBarco();
            j.setTipoVaga(idTipoVaga);
            j.setDtInicial(dtInicio);
            j.setDtFinal(dtFim);
            j.setValor(valor);
            
            int pesInicial=0;
            int pesFinal=0;
            try{
                for(String s : request.getParameterValues("pesInicialCobrar")){
                    Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1431", request.getRemoteAddr());
                    pesInicial = Integer.parseInt(s);
                    switch (pesInicial){
                        case 1:
                            pesFinal=15;
                            break;
                        case 16:
                            pesFinal=20;
                            break;
                        case 21:
                            pesFinal=22;
                            break;
                        case 23:
                            if (idTipoVaga.equals("CO")){
                                pesFinal=26;
                            }else{
                                pesFinal=29;
                            }
                            break;
                        case 27: //só existe para tipo de vaga coberta
                            pesFinal=1000;
                            break;
                        case 30:
                            pesFinal=34;
                            break;
                        case 35:
                            pesFinal=39;
                            break;
                        case 40:
                            pesFinal=1000;
                            break;
                    }
                    j.setPesInicial(pesInicial);
                    j.setPesFinal(pesFinal);
                    j.inserir(audit);
                }

                response.sendRedirect("c?app=1430&idTipoVaga="+idTipoVaga);
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
            
        }else{
            request.setAttribute("idTipoVaga", request.getParameter("idTipoVaga"));

            request.getRequestDispatcher("/pages/1431.jsp").forward(request, response);
        }
            

    }    
    
    @App("1432")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if ("gravar".equals(acao)){
            String idTipoVaga = request.getParameter("idTipoVaga");
            int pesInicial = Integer.parseInt(request.getParameter("pesInicial"));
            Date dtInicio = Datas.parseDataHora(request.getParameter("dtInicio"));
            Date dtFim = null;
            if (!request.getParameter("dtFim").equals("")){
                dtFim = Datas.parseDataHora(request.getParameter("dtFim"));
            }
            
            TaxaBarco j = new TaxaBarco();
            j.setTipoVaga(idTipoVaga);
            j.setPesInicial(pesInicial);
            j.setDtInicial(dtInicio);
            j.setDtFinal(dtFim);
            
            try{
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1432", request.getRemoteAddr());
                j.alterar(audit);

                response.sendRedirect("c?app=1430&idTipoVaga="+idTipoVaga);
            }catch(InserirException e){
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);                
            }
            
        }else{
            request.setAttribute("tipoVaga", TipoVagaBarco.getInstance(request.getParameter("idTipoVaga")) );
            request.setAttribute("pesInicial", request.getParameter("pesInicial"));
            request.setAttribute("pesFinal", request.getParameter("pesFinal"));

            request.setAttribute("dtInicio", request.getParameter("dtInicio"));
            request.setAttribute("dtFim", request.getParameter("dtFim"));
            request.setAttribute("valor", request.getParameter("valor"));

            request.getRequestDispatcher("/pages/1432.jsp").forward(request, response);
        }
            

    }    
}
