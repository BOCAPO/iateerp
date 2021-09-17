package techsoft.clube;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.ComboBoxLoader;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.SocioCarne;
import techsoft.cadastro.Titular;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.operacoes.ConvenioBB;
import techsoft.operacoes.Taxa;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;


@Controller
public class ControleArquivoDiaItau{
    
    @App("2640")
    public static void gerarMovItau109(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("gerar".equals(acao)) {
            
            Date dataVencimento = Datas.parse(request.getParameter("dataVencimento"));
            Date dataPagamento = Datas.parse(request.getParameter("dataPagamento"));
            
            String debitoConta = request.getParameter("debitoConta");
            
            StringBuilder categorias = new StringBuilder();
            categorias.append("(");    
            for(String s : request.getParameterValues("categorias")){
                categorias.append(s);
                categorias.append(", ");
            }
            categorias.delete(categorias.length() -2, categorias.length());            
            categorias.append(")");    
            
            Auditoria audit = new Auditoria(acao, "2640", request.getRemoteAddr());
            ConvenioBB.gerarMovItau109(audit, debitoConta, dataVencimento, dataPagamento, categorias.toString());

            response.sendRedirect("c?app=2640");

        } else {
            request.getSession().setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.getRequestDispatcher("/pages/2640.jsp").forward(request, response);
        }
    }    
    


    @App("2560")
    public static void gerarArquivoDiaItau(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("gerar".equals(acao)) {
            
            Date dtReferencia = Datas.parse(request.getParameter("dataReferencia"));
            
            String arquivo = request.getServletContext().getRealPath("/");
            arquivo += "mov-dia-itau.txt";

            Auditoria audit = new Auditoria(acao, "2560", request.getRemoteAddr());
            ConvenioBB.gerarMovDiaItau(audit, dtReferencia, arquivo);

            download(arquivo, request, response);
        } else {
            request.getSession().setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.getRequestDispatcher("/pages/2560.jsp").forward(request, response);
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


