package techsoft.acesso;

import java.io.IOException;
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
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.seguranca.Auditoria;
import techsoft.acesso.Entrada;
import techsoft.cadastro.Convite;
import techsoft.cadastro.PermissaoProvisoria;
import techsoft.operacoes.AutorizacaoEmbarque;
import techsoft.util.Datas;

@Controller
public class ControleEntrada{
    

    @App("7040")
    public static void local(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("showForm".equals(acao)){
            request.setAttribute("local", LocalAcesso.getInstance(Integer.parseInt(request.getParameter("idLocal"))));
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
            
            request.getRequestDispatcher("/pages/7040.jsp").forward(request, response);
        }else if ("acesso".equals(acao)){
            int doc = 0;
            int qtd = 0;
            String docStr = "";
            List<Integer> vet = new ArrayList<Integer>();
            
            if ("S".equals(request.getParameter("origem"))) {
                //veio de uma tela de selecao
                if ("CP".equals(request.getParameter("tipoOrigem"))){
                    //quando vier de carteira e passaporte o retorno da selecao eh o titulo 
                    // por isso precisa buscar a carteira e passaporte
                    for(String s : request.getParameterValues("sel")){
                        if ("90".equals(s.substring(4,6))){
                            //buscar numero do passaporte
                            doc =  Socio.numeroPassaporte(Integer.parseInt(s.substring(0, 4)), Integer.parseInt(s.substring(6, 8)), Integer.parseInt(s.substring(4, 6)));
                        }else{
                            //buscar numero da carteira
                            Socio socio = Socio.getInstance(Integer.parseInt(s.substring(0, 4)),  Integer.parseInt(s.substring(6, 8)), Integer.parseInt(s.substring(4, 6)));
                            doc =  socio.getNrCarteira();
                        }
                        vet.add(doc);
                    }
                }else{
                    for(String s : request.getParameterValues("sel")){
                        doc = Integer.parseInt(s);
                        vet.add(doc);
                    }
                }    
            }else{
                //veio da propria tela
                try{
                    doc = Integer.parseInt(request.getParameter("doc"));
                }catch(NumberFormatException e){
                    doc = 0;
                }
                vet.add(doc);
            }
            
            DecimalFormat f = new DecimalFormat("000000000");
            docStr = String.valueOf(f.format(Integer.valueOf(vet.get(0))));
            
            try{
                qtd = Integer.parseInt(request.getParameter("qtd"));
            }catch(NumberFormatException e){
                qtd = 1;
            }
            
            Entrada e = new Entrada();
            int tamanho=0;
            ArrayList<Entrada> l = new ArrayList<Entrada>();
            String entradaSaida = request.getParameter("entradaSaida");
            String cargoEspecial = "";
            if (vet.get(0)==0){
                e.setMensagem("PROIBIR ACESSO:<br>&nbsp&nbsp Documento Inválido.");
                e.setCor("red");
                l.add(e);
            }else if (docStr.trim().length()<9){
                e.setMensagem("PROIBIR ACESSO:<br>&nbsp&nbsp Número do documento incompleto.");
                e.setCor("red");
                l.add(e);
            }else{
                tamanho = vet.size();
                for(int i = 0;i<tamanho;i++){
                    l.add(Entrada.getInstance(vet.get(i), Integer.parseInt(request.getParameter("idLocal")), request.getParameter("placa"), qtd, entradaSaida,request.getUserPrincipal().getName()));
                    if (!"".equals(l.get(i).getCargoEspecial())){
                        cargoEspecial = l.get(i).getCargoEspecial();
                    }
                }
            }
            
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
            
            request.setAttribute("entradas", l);
            request.setAttribute("tamanho", tamanho);
            request.setAttribute("cargoEspecial", cargoEspecial);
            request.setAttribute("local", LocalAcesso.getInstance(Integer.parseInt(request.getParameter("idLocal"))));
            
            if (tamanho==1){
                request.setAttribute("height", "height:270px");
                request.setAttribute("doc", vet.get(0));    
                request.setAttribute("classeFoto", "fotoUnica"); 
            }else{
                request.setAttribute("height", "");
                request.setAttribute("doc", "");    
                request.setAttribute("classeFoto", "fotoMultiplas"); 
            }
               
            request.getRequestDispatcher("/pages/7040.jsp").forward(request, response);
        }else if ("pesquisa".equals(acao)){
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
            
            if ("CP".equals(request.getParameter("tipoDoc"))){
                //PESQUISA DE CARTEIRA E PASSAPORTE
                request.setAttribute("categorias", ComboBoxLoader.listar("VW_CATEGORIA"));
                request.getRequestDispatcher("/pages/7040-pesqCP.jsp").forward(request, response);
            }else if ("CO".equals(request.getParameter("tipoDoc"))){
                //PESQUISA DE CONVITE
                request.setAttribute("tipoConvites", ComboBoxLoader.listar("VW_TIPO_CONVITE"));
                request.setAttribute("churrasqueiras", ComboBoxLoader.listar("VW_CHURRASQUEIRAS"));
                request.getRequestDispatcher("/pages/7040-pesqCO.jsp").forward(request, response);
            }else if ("AE".equals(request.getParameter("tipoDoc"))){
                //PESQUISA DE AUTORIZACAO DE EMBARQUE
                SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
                Date data = new Date(); //data atual

                List<AutorizacaoEmbarque> autorizacoes = AutorizacaoEmbarque.consultar(0, null, Datas.parse(sd.format(data)), "","");

                request.setAttribute("autorizacoes", autorizacoes);
                request.getRequestDispatcher("/pages/7040-pesqAE.jsp").forward(request, response);
            }else if ("PP".equals(request.getParameter("tipoDoc"))){
                //PESQUISA DE PERMISSAO PROVISORIA
                List<PermissaoProvisoria> permissoes = PermissaoProvisoria.consultar();

                request.setAttribute("permissoes", permissoes);
                request.getRequestDispatcher("/pages/7040-pesqPP.jsp").forward(request, response);
                
            }

        }else if ("pesqCO".equals(acao)){
            
            int matricula = 0;
            int nrConvite = 0;
            String sql = "";
            SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
            Date data = new Date();
            
            try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
            }catch(NumberFormatException e){
                matricula = 0;
            }
            try{
                nrConvite = Integer.parseInt(request.getParameter("nrConvite"));
            }catch(NumberFormatException e){
                nrConvite = 0;
            }
            sql = "SELECT * FROM TB_CONVITE WHERE CD_STATUS_CONVITE = 'NU' AND DT_MAX_UTILIZACAO >= '" + sd.format(data) + "'";
            if (matricula>0){
                sql = sql + " AND CD_MATRICULA = " + matricula;
            }
            if (nrConvite>0){
                sql = sql + " AND NR_CONVITE = " + nrConvite;
            }
            if (!"".equals(request.getParameter("nome"))){
                sql = sql + " AND NOME_SACADOR LIKE '" + request.getParameter("nome") + "%'";
            }
            if (!"".equals(request.getParameter("convidado"))){
                sql = sql + " AND NOME_CONVIDADO LIKE '" + request.getParameter("convidado") + "%'";
            }
            if (!"0".equals(request.getParameter("idChurrasqueira"))){
                sql = sql + " AND NR_CHURRASQUEIRA = " + request.getParameter("idChurrasqueira");
            }
            if (!"TO".equals(request.getParameter("tpConvite"))){
                sql = sql + " AND CD_CATEGORIA_CONVITE = '" + request.getParameter("tpConvite") + "'";
            }
            if (!"".equals(request.getParameter("cpf"))){
                sql = sql + " AND CPF_CONVIDADO = '" + request.getParameter("cpf") + "'";
            }
            if (!"".equals(request.getParameter("rg"))){
                sql = sql + " AND RG_CONVIDADO = '" + request.getParameter("rg") + "'";
            }
            if (!"".equals(request.getParameter("orgaoExp"))){
                sql = sql + " AND DE_ORGAO_EXP_CONVIDADO = '" + request.getParameter("orgaoExp") + "'";
            }
            
            
            List<Convite> convites = Convite.listar(sql);
            request.setAttribute("convites", convites);
            
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
            
            request.setAttribute("tipoConvites", ComboBoxLoader.listar("VW_TIPO_CONVITE"));
            request.setAttribute("churrasqueiras", ComboBoxLoader.listar("VW_CHURRASQUEIRAS"));
            request.getRequestDispatcher("/pages/7040-pesqCO.jsp").forward(request, response);
        }else if ("pesqCP".equals(acao)){
            
            int matricula = 0;
            
            try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
            }catch(NumberFormatException e){
                matricula = 1;
            }
            
            List<Socio> socios = Socio.listar(0, Integer.parseInt(request.getParameter("categoria")), matricula, request.getParameter("nome"));
            request.setAttribute("socios", socios);
            
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
            
            request.setAttribute("categorias", ComboBoxLoader.listar("VW_CATEGORIA"));
            request.getRequestDispatcher("/pages/7040-pesqCP.jsp").forward(request, response);

        }else if ("atlzCP".equals(acao)){
            
            int matricula = 0;
            
            try{
                matricula = Integer.parseInt(request.getParameter("matricula"));
            }catch(NumberFormatException e){
                matricula = 1;
            }
            
            List<Socio> socios = Socio.listar(0, Integer.parseInt(request.getParameter("categoria")), matricula, request.getParameter("nome"));
            request.setAttribute("socios", socios);
            
            for (String p : Collections.list(request.getParameterNames()))
                    request.setAttribute(p, request.getParameter(p));
            
            request.setAttribute("categorias", ComboBoxLoader.listar("VW_CATEGORIA"));
            request.getRequestDispatcher("/pages/7040-pesqCP.jsp").forward(request, response);

        }else {
            String sql = "SELECT CD_LOCAL_ACESSO, DESCR_LOCAL_ACESSO FROM TB_LOCAL_ACESSO WHERE ED_ESTACAO IS NULL OR ED_ESTACAO = '" + request.getRemoteAddr() + "'";
            request.setAttribute("locais", ComboBoxLoader.listarSql(sql));
            
            request.getRequestDispatcher("/pages/7040-local.jsp").forward(request, response);
        }

    }
    

}
