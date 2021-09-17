package techsoft.controle.operacoes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.operacoes.ConvenioBB;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;

@Controller
public class ControleConvenioBB {

    private static final Logger log = Logger.getLogger("techsoft.controle.operacoes.ControleConvenioBB");

    //App 1700, 1710 e 1940 estao na servlet Upload uma vez que o arquivo do banco eh enviado para essa servlet
    @App("1810")
    public static void gerarArquivoBB(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("gerar".equals(acao)) {
            StringBuilder categorias = new StringBuilder();
            
            categorias.append("(");    
            for(String s : request.getParameterValues("categorias")){
                categorias.append(s);
                categorias.append(", ");
            }
            categorias.delete(categorias.length() -2, categorias.length());            
            categorias.append(")");    

            String banco = request.getParameter("banco");
            int titulo = 0;
            try {
                titulo = Integer.parseInt(request.getParameter("titulo"));
            } catch (NumberFormatException e) {
                titulo = 0;
            }
            Date vencimento = Datas.parse(request.getParameter("dataVencimento"));
            int idDeclaracao = Integer.parseInt(request.getParameter("idDeclaracao"));

            boolean debitoConta = false;
            if (request.getParameter("debitoConta") != null && request.getParameter("debitoConta").equals("on")) {
                debitoConta = true;
            }
            boolean naoInformado = false;
            if (request.getParameter("naoInformado") != null && request.getParameter("naoInformado").equals("on")) {
                naoInformado = true;
            }
            boolean somenteBoleto = false;
            if (request.getParameter("somenteBoleto") != null && request.getParameter("somenteBoleto").equals("on")) {
                somenteBoleto = true;
            }
            boolean correspondenciaBoleto = false;
            if (request.getParameter("correspondenciaBoleto") != null && request.getParameter("correspondenciaBoleto").equals("on")) {
                correspondenciaBoleto = true;
            }

            String arquivo = request.getServletContext().getRealPath("/");
            if (banco.equals("B")) {
                arquivo += "convenio-bb.txt";
            } else {
                arquivo += "convenio-itau.txt";
            }

            Auditoria audit = new Auditoria(acao, "1810", request.getRemoteAddr());
            ConvenioBB.gerarArquivo(audit, debitoConta, vencimento, categorias.toString() , titulo, idDeclaracao, somenteBoleto, correspondenciaBoleto, naoInformado, arquivo, banco);

            download(arquivo, request, response);
        } else {
            request.getSession().setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.getRequestDispatcher("/pages/1810.jsp").forward(request, response);
        }
    }

    @App("1820")
    public static void gerarDCO(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("gerar".equals(acao)) {
            Date vencimento = Datas.parse(request.getParameter("dataVencimento"));

            String arquivo = request.getServletContext().getRealPath("/");
            arquivo += "bb-dco.txt";

            Auditoria audit = new Auditoria(acao, "1820", request.getRemoteAddr());
            ConvenioBB.gerarDCO(audit, vencimento, arquivo);

            download(arquivo, request, response);
        } else {
            request.getRequestDispatcher("/pages/1820.jsp").forward(request, response);
        }
    }

    /*
     * Sugere ao navegador que seja feito download do arquivo ao inves de mostrar seu conteudo
     */
    private static void download(String arquivo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File f = new File(arquivo);
        response.setContentLength((int) f.length());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");

        byte[] buf = new byte[4096];
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int i = 0;
        while ((i = in.read(buf)) > 0) {
            out.write(buf, 0, i);
        }
        out.close();
        in.close();
    }

}
