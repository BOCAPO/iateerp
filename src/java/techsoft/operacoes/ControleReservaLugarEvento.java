package techsoft.operacoes;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import techsoft.operacoes.Evento;
import techsoft.tabelas.Material;

@Controller
public class ControleReservaLugarEvento{
    
    
    
    @App("1570")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao=request.getParameter("acao");
        
        if ("imprimir".equals(acao)){
            request.getRequestDispatcher("/pages/1570-impressao.jsp").forward(request, response);
        }else{
            List<Evento> e = new ArrayList<Evento>();
            e = Evento.listarAtivosFuturos();
            int eventoSel = 0;
            try{
                eventoSel = Integer.parseInt(request.getParameter("idEvento"));
            }catch(Exception ex){
                for(Evento x : e){
                    eventoSel = x.getId();
                    break;
                }
            }

            if (eventoSel>0){
                request.setAttribute("eventoSel", Evento.getInstance(eventoSel));
                request.setAttribute("lugares", ReservaLugarEvento.lugaresReservados(eventoSel));
            }
            request.setAttribute("idEvento", eventoSel);
            request.setAttribute("eventos", Evento.listarAtivosFuturos());
            request.getRequestDispatcher("/pages/1570-lista.jsp").forward(request, response);
        }


    }
    
    @App("1571")
    public static void incluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao=request.getParameter("acao");
        
        if ("selTabela".equals(acao) || "selMapa".equals(acao)){
            request.setAttribute("idEvento", request.getParameter("idEvento"));    
            request.setAttribute("lugares", request.getParameterValues(acao));    
            request.setAttribute("app", 1571);
            request.getRequestDispatcher("/pages/1570.jsp").forward(request, response);
        }else if ("gravar".equals(acao)){

            ArrayList<ReservaLugarEvento> l = new ArrayList<ReservaLugarEvento>();
            
            int idEvento = Integer.parseInt(request.getParameter("idEvento"));
            String tipoDadoReserva = request.getParameter("tipoDadoReserva");
            String lugar = "";
            String tipo = "";
            Float valor = 0f;
            String nome = "";
            String titulo = "";
            String telefone = "";
            String situacao = "";
            
            String[] vetLugares = request.getParameter("lugares").split(",");
                
            for(int i =0; i < vetLugares.length ; i++){
                if ("U".equals(tipoDadoReserva)){
                    //só foi informado dados da primeira reserva que serão replicados para as demais
                    lugar=vetLugares[0];
                }else{
                    //cada reserva tem o seu dado
                    lugar=vetLugares[i];
                }

                tipo=request.getParameter("tipo"+lugar);
                nome=request.getParameter("nome"+lugar);
                titulo=request.getParameter("titulo"+lugar);
                telefone=request.getParameter("telefone"+lugar);
                situacao=request.getParameter("situacao"+lugar);
                try{
                    valor = Float.parseFloat(request.getParameter("valor"+lugar).toString().replaceAll(",", "."));
                }catch(Exception e){
                    valor = 0f;
                }

                ReservaLugarEvento r = new ReservaLugarEvento();
                r.setMesa(Integer.parseInt(vetLugares[i].substring(0,3)));
                r.setCadeira(Integer.parseInt(vetLugares[i].substring(3,5)));
                r.setEvento(idEvento);
                r.setPessoa(nome);
                r.setSituacao(situacao);
                r.setTipoPessoa(tipo.substring(0,1));
                r.setTelefone(telefone);
                r.setValor(valor);
                if ("S".equals(tipo.substring(0,1))){
                    r.setMatricula(Integer.parseInt(titulo.substring(0,4)));
                    r.setCategoria(Integer.parseInt(titulo.substring(4,6)));
                    r.setDependente(Integer.parseInt(titulo.substring(6)));
                }
                r.setMeia(tipo.substring(1));
                r.setUsuario(request.getUserPrincipal().getName());

                l.add(r);

            }
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1571", request.getRemoteAddr());

            try{
                ReservaLugarEvento.inserir(l, audit);

                response.sendRedirect("c?app=1570&idEvento="+idEvento);
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
            
        }else{
            
        }

    }
    
    @App("1572")
    public static void alterar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao=request.getParameter("acao");
        int idEvento = Integer.parseInt(request.getParameter("idEvento"));
        
        if ("confirmar".equals(acao)){
            for(String s : request.getParameterValues("selReservado")){
                ReservaLugarEvento d = ReservaLugarEvento.getInstance(idEvento, Integer.parseInt(s.substring(0,3)), Integer.parseInt(s.substring(3,5)));
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1572", request.getRemoteAddr());
                try{
                    d.confirmar(audit);
                }catch(Exception e){
                    request.setAttribute("err", e);
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                }
            }
            response.sendRedirect("c?app=1570&idEvento="+idEvento);
        }else if ("gravar".equals(acao)){
            
            String lugar = request.getParameter("lugarGato");
            String tipo =request.getParameter("tipo"+lugar);
            Float valor = 0f;
            try{
                valor = Float.parseFloat(request.getParameter("valor"+lugar).toString().replaceAll(",", "."));
            }catch(Exception e){
                valor = 0f;
            }
            String nome = request.getParameter("nome"+lugar);
            String titulo = request.getParameter("titulo"+lugar);
            String telefone = request.getParameter("telefone"+lugar);
            String situacao = request.getParameter("situacao"+lugar);

            ReservaLugarEvento r = new ReservaLugarEvento();
            r.setMesa(Integer.parseInt(lugar.substring(0,3)));
            r.setCadeira(Integer.parseInt(lugar.substring(3,5)));
            r.setEvento(idEvento);
            r.setPessoa(nome);
            r.setSituacao(situacao);
            r.setTipoPessoa(tipo.substring(0,1));
            r.setTelefone(telefone);
            r.setValor(valor);
            
            if ("S".equals(tipo.substring(0,1))){
                r.setMatricula(Integer.parseInt(titulo.substring(0,4)));
                r.setCategoria(Integer.parseInt(titulo.substring(4,6)));
                r.setDependente(Integer.parseInt(titulo.substring(6)));
            }
            r.setMeia(tipo.substring(1));
            r.setUsuario(request.getUserPrincipal().getName());
            
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1571", request.getRemoteAddr());
            try{
                r.alterar(audit);

                response.sendRedirect("c?app=1570&idEvento="+idEvento);
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
                        
            
        }else{
            request.setAttribute("idEvento", request.getParameter("idEvento"));    
            String lugar = request.getParameter("lugar");    
            ArrayList<ReservaLugarEvento> l = new ArrayList<ReservaLugarEvento>();
            ReservaLugarEvento d = ReservaLugarEvento.getInstance(idEvento, Integer.parseInt(lugar.substring(0,3)), Integer.parseInt(lugar.substring(3,5)));
            l.add(d);
            
            request.setAttribute("lugares", l);
            request.setAttribute("app", 1572);
            request.getRequestDispatcher("/pages/1570.jsp").forward(request, response);
        }
    }

    @App("1573")
    public static void excluir(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String acao=request.getParameter("acao");
        int idEvento = Integer.parseInt(request.getParameter("idEvento"));

        for(String s : request.getParameterValues(acao)){
            ReservaLugarEvento d = ReservaLugarEvento.getInstance(idEvento, Integer.parseInt(s.substring(0,3)), Integer.parseInt(s.substring(3,5)));
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1573", request.getRemoteAddr());
            try{
                d.excluir(audit);
            }catch(Exception e){
                request.setAttribute("err", e);
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }
        }
        response.sendRedirect("c?app=1570&idEvento="+idEvento);

    }
}
