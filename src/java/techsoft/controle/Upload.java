package techsoft.controle;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import techsoft.cadastro.CarroSocio;
import techsoft.operacoes.PermissaoProvisoria;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.clube.AchadosPerdidos;
import techsoft.clube.CarroFuncionario;
import techsoft.curso.Turma;
import techsoft.operacoes.ConvenioBB;
import techsoft.operacoes.ConvenioBBException;
import techsoft.operacoes.ResultadoBaixaArquivoConvenioBB;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.Funcionario;
import techsoft.util.Datas;

@MultipartConfig
public class Upload extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String app = request.getParameter("app");
        String acao = request.getParameter("acao");
        
        if("3075".equals(app)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            int idTurma = Integer.parseInt(request.getParameter("idTurma"));        
            File f = File.createTempFile(app, ".tmp");
            Part p = request.getPart("arquivo");
            p.write(f.getCanonicalPath()); 
            Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);            
            s.atualizarFoto(f);
            f.delete();
            
            //reload na pagina
            response.sendRedirect("c?app=3075&matricula=" + s.getMatricula() + "&seqDependente=" + s.getSeqDependente() + "&idCategoria=" + s.getIdCategoria() + "&idTurma=" + idTurma);
        }else if("1054".equals(app) && "atualizarFoto".equals(acao)){
            int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
            Funcionario funcionario = Funcionario.getInstance(idFuncionario);
            File f = File.createTempFile(app, ".tmp");
            Part p = request.getPart("arquivo");
            p.write(f.getCanonicalPath()); 
            funcionario.atualizarFoto(f);
            f.delete();
            
            //reload na pagina
            response.sendRedirect("c?app=1054&acao=showForm&idFuncionario=" + funcionario.getId());            
        }else if("2194".equals(app) && "atualizarFoto".equals(acao)){
            int idCarro = Integer.parseInt(request.getParameter("idCarro"));
            CarroSocio carro = CarroSocio.getInstance(idCarro);
            File f = File.createTempFile(app, ".tmp");
            Part p = request.getPart("arquivo");
            p.write(f.getCanonicalPath()); 
            carro.atualizarFoto(f);
            f.delete();
            
            //reload na pagina
            response.sendRedirect("c?app=2194&acao=showForm&idCarro=" + carro.getId());            
        }else if("3040".equals(app) && "gravar".equals(acao)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            Date dtValidadeAtestado = Datas.parse(request.getParameter("dtValidadeAtestado"));
            Socio socio = Socio.getInstance(matricula, seqDependente, idCategoria);
            socio.setDtValidadeAtestado(dtValidadeAtestado);
            int idTurma = Integer.parseInt(request.getParameter("idTurma"));

            String fazMatricula = request.getParameter("fazMatricula");
            if (fazMatricula.equals("SIM")){
                float descontoFamiliar = Float.parseFloat(request.getParameter("descontoFamiliar").toString().replaceAll(",", "."));
                float descontoPessoal = 0f;            
                try{
                    descontoPessoal = Float.parseFloat(request.getParameter("descontoPessoal").toString().replaceAll(",", "."));
                }catch(NumberFormatException e){
                    request.setAttribute("msg", "O valor do desconto pessoal tem mais de uma virgula");
                    request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                    return;
                }

                Turma t = Turma.getInstance(idTurma);
                Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "3040", request.getRemoteAddr());
                t.matricular(socio, descontoFamiliar, descontoPessoal, audit);
            }
            
            //atualizando atestado
            Part p = request.getPart("arquivo");
            File f = File.createTempFile("3040", ".tmp");
            p.write(f.getCanonicalPath()); 
            socio.atualizarAtestado(f);
            f.delete();
            
            if (fazMatricula.equals("SIM")){
                response.sendRedirect("c?app=3040&mostrarSomenteTurmasAtivas=true");
            }else{
                response.sendRedirect("c?app=3040&acao=atestado&idTurma="+idTurma+"&matricula="+matricula+"&seqDependente="+seqDependente+"&idCategoria="+idCategoria);
            }
                
            
        }else if("3244".equals(app) && "atualizarFoto".equals(acao)){
            int idCarro = Integer.parseInt(request.getParameter("idCarro"));
            CarroFuncionario carro = CarroFuncionario.getInstance(idCarro);
            File f = File.createTempFile(app, ".tmp");
            Part p = request.getPart("arquivo");
            p.write(f.getCanonicalPath()); 
            carro.atualizarFoto(f);
            f.delete();
            
            //reload na pagina
            response.sendRedirect("c?app=3244&acao=showForm&idCarro=" + carro.getId());            
        }else if("1234".equals(app) && "atualizarFoto".equals(acao)){
            int idPermissao = Integer.parseInt(request.getParameter("idPermissao"));
            PermissaoProvisoria permissao = PermissaoProvisoria.getInstance(idPermissao);
            File f = File.createTempFile(app, ".tmp");
            Part p = request.getPart("arquivo");
            p.write(f.getCanonicalPath()); 
            permissao.atualizarFoto(f);
            f.delete();
            
            //reload na pagina
            response.sendRedirect("c?app=1234&acao=showForm&idPermissao=" + idPermissao);            
        }else if("9080".equals(app) && "atualizarFoto".equals(acao)){
            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int seqDependente = Integer.parseInt(request.getParameter("seqDependente"));
            int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
            File f = File.createTempFile(app, ".tmp");
            Part p = request.getPart("arquivo");
            p.write(f.getCanonicalPath()); 
            Socio s = Socio.getInstance(matricula, seqDependente, idCategoria);            
            s.atualizarFoto(f);
            f.delete();
            
            //reload na pagina
            response.sendRedirect("c?app=9080&matricula=" + s.getMatricula() + "&seqDependente=" + s.getSeqDependente() + "&idCategoria=" + s.getIdCategoria() + "&acao=showForm");            
        }else if("1265".equals(app) && "capturarFoto".equals(acao)){
            //int idCracha = Integer.parseInt(request.getParameter("idCracha"));
            File path = new File(request.getServletContext().getRealPath("/img/"));
            File f = File.createTempFile(app, ".tmp", path);
            Part p = request.getPart("arquivo");
            
            if(p.getSize() > 0){
                p.write(f.getCanonicalPath()); 
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");                            
                response.getWriter().write(f.getName());
            }else{
                f.delete();
            }
            
            //reload na pagina
            //response.sendRedirect("c?app=1265&idCracha=" + idCracha + "&urlFotoVisitante=img/" + f.getName() + "&acao=showForm");
        }else if("2000".equals(app)){
            int idBarco = Integer.parseInt(request.getParameter("idBarco"));
            File f = File.createTempFile(app, ".tmp");
            Part p = request.getPart("arquivo");
            p.write(f.getCanonicalPath()); 
            SocioBarco b = SocioBarco.getInstance(idBarco);            
            b.atualizarFoto(f);
            f.delete();
            
            //reload na pagina
            response.sendRedirect("c?app=2000&acao=showFoto&idBarco=" + idBarco);   
            
        }else if("2425".equals(app)){
            int idObjeto = Integer.parseInt(request.getParameter("idObjeto"));
            File f = File.createTempFile(app, ".tmp");
            Part p = request.getPart("arquivo");
            p.write(f.getCanonicalPath()); 
            AchadosPerdidos b = AchadosPerdidos.getInstance(idObjeto);            
            b.atualizarFoto(f);
            f.delete();
            
            //reload na pagina
            response.sendRedirect("c?app=2425&acao=showFoto&idObjeto=" + idObjeto);            
            
        /*
         * Baixa Arquivo BB
         */
        }else if("1700".equals(app)){
            
            boolean force = Boolean.parseBoolean(request.getParameter("force"));
            int dias = 0;
            String arquivobb = null;
            String banco = null;
            
            if("baixar".equals(acao)){
                //ja baixou o arquivo, recuperar da sessao
                if(force){
                    dias = Integer.parseInt(request.getSession().getAttribute("dias").toString());
                    arquivobb = request.getSession().getAttribute("arquivobb").toString();
                    banco = request.getSession().getAttribute("banco").toString();
                }else{
                    dias = Integer.parseInt(request.getParameter("dias"));
                    banco = request.getParameter("banco");
                    
                    String path = request.getServletContext().getRealPath("/");
                    
                    Part p = request.getPart("arquivo");
                    String arquivo = null;

                    for (String cd : p.getHeader("content-disposition").split(";")) {
                        if (cd.trim().startsWith("filename")) {
                            arquivo = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                        }
                    }
                    arquivobb = path + arquivo;

                    File f = new File(arquivobb);            

                    p.write(f.getCanonicalPath()); 

                    request.getSession().setAttribute("dias", String.valueOf(dias));
                    request.getSession().setAttribute("arquivobb", arquivobb);
                    request.getSession().setAttribute("banco", banco);
                }            

                try{
                    Auditoria audit = new Auditoria(acao, app, request.getRemoteAddr());
                    ResultadoBaixaArquivoConvenioBB result = ConvenioBB.baixarArquivo(audit, dias, arquivobb, force, banco);
                    request.setAttribute("result", result);
                    request.getRequestDispatcher("/pages/1700-resultado.jsp").forward(request, response);
                }catch(ConvenioBBException e){
                    if(e.isSolicitarConfirmacao()){
                        request.setAttribute("msg", e.getMessage());
                        request.setAttribute("sim", "upload?app=1700&force=true&acao=baixar");
                        request.setAttribute("nao", "upload?app=1700");
                        request.getRequestDispatcher("/pages/confirmacao.jsp").forward(request, response);
                    }else{
                        request.setAttribute("msg", e.getMessage());                    
                        request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                    }
                }

                //f.delete();  *** VAI SER DELETADO DENTRO DA ROTINA DE BAIXA ***
            }else{
                request.getRequestDispatcher("/pages/1700.jsp").forward(request, response);                
            }
            
        /*
         * Baixa DCO
         */
        }else if("1710".equals(app)){
            
            boolean force = Boolean.parseBoolean(request.getParameter("force"));
            Date dataVencimento = null;
            String arquivobb = null;
            
            if("baixar".equals(acao)){
                //ja baixou o arquivo, recuperar da sessao
                if(force){
                    dataVencimento = (Date)request.getSession().getAttribute("dataVencimento");
                    arquivobb = request.getSession().getAttribute("arquivobb").toString();
                }else{
                    dataVencimento = Datas.parse(request.getParameter("dataVencimento"));
                    String path = request.getServletContext().getRealPath("/");
                    Part p = request.getPart("arquivo");
                    String arquivo = null;

                    for (String cd : p.getHeader("content-disposition").split(";")) {
                        if (cd.trim().startsWith("filename")) {
                            arquivo = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                        }
                    }
                    arquivobb = path + arquivo;

                    File f = new File(arquivobb);            

                    p.write(f.getCanonicalPath()); 

                    request.getSession().setAttribute("dataVencimento", dataVencimento);
                    request.getSession().setAttribute("arquivobb", arquivobb);
                }            

                try{
                    Auditoria audit = new Auditoria(acao, app, request.getRemoteAddr());
                    ResultadoBaixaArquivoConvenioBB result = ConvenioBB.baixarDCO(audit, dataVencimento, arquivobb, force);
                    request.setAttribute("result", result);
                    request.getRequestDispatcher("/pages/1710-resultado.jsp").forward(request, response);
                }catch(ConvenioBBException e){
                    if(e.isSolicitarConfirmacao()){
                        request.setAttribute("msg", e.getMessage());
                        request.setAttribute("sim", "upload?app=1710&force=true&acao=baixar");
                        request.setAttribute("nao", "upload?app=1710");
                        request.getRequestDispatcher("/pages/confirmacao.jsp").forward(request, response);
                    }else{
                        request.setAttribute("msg", e.getMessage());                    
                        request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
                    }
                }

                //f.delete();  *** VAI SER DELETADO DENTRO DA ROTINA DE BAIXA ***
            }else{
                request.getRequestDispatcher("/pages/1710.jsp").forward(request, response);                
            }
        
        /*
         * Visualizar arquivo de retorno do banco
         */
        }else if("1940".equals(app)){
            if("visualizar".equals(acao)){
                String path = request.getServletContext().getRealPath("/");
                Part p = request.getPart("arquivo");

                File f = File.createTempFile("visualizar-retorno", "bb", new File(path));

                p.write(f.getCanonicalPath()); 
                request.setAttribute("arquivobb", f.getCanonicalPath());
                request.getRequestDispatcher("/pages/1940-resultado.jsp").forward(request, response);
                //f.delete();  *** VAI SER DELETADO NA jsp DE VISUALIZAÇÃO ***
            }else{
                request.getRequestDispatcher("/pages/1940.jsp").forward(request, response);
            }
        }        
        
    }

    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
