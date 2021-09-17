package techsoft.clube;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;
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
import techsoft.db.Pool;
import techsoft.operacoes.ConvenioBB;
import techsoft.operacoes.Taxa;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;


@Controller
public class ControleArquivoSocio{
    

    public static final Logger log = Logger.getLogger("techsoft.operacoes.ConvenioBB");

    @App("2700")
    public static void gerarArquivoSocio(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("gerar".equals(acao)) {
            StringBuilder categorias = new StringBuilder();
            String sql = ""; 
            
            for(String s : request.getParameterValues("categorias")){
                categorias.append(s);
                categorias.append(", ");
            }
            categorias.delete(categorias.length() -2, categorias.length());            
            
             sql = "EXEC SP_GERA_CSV_SOCIO '" + categorias + "'";
            
            String arquivo = request.getServletContext().getRealPath("/");
            arquivo += "end-socios.csv";

            Auditoria audit = new Auditoria(acao, "2560", request.getRemoteAddr());
            gerarArquivoCSV(audit, sql, arquivo);

            download(arquivo, request, response);
        } else {
            request.getSession().setAttribute("categorias", ComboBoxLoader.listar("TB_CATEGORIA"));
            request.getRequestDispatcher("/pages/2700.jsp").forward(request, response);
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
    
    public static void gerarArquivoCSV(
            Auditoria audit,
            String sql,
            String arquivo){
        
        Connection cn = null;

        try {
            cn = Pool.getInstance().getConnection();

            CallableStatement cal = cn.prepareCall(sql);            
            cal.execute();
            
            audit.registrarMudanca(sql);            
            
            ResultSet rs = cal.getResultSet();
            FileWriter w = new FileWriter(arquivo);
            while(rs.next()){
                w.write(rs.getString("DE_LINHA"));
                w.write("\r\n");
            }
            w.close();

        }catch (SQLException e) {
            log.severe(e.getMessage());
        }catch (IOException e) {
            log.severe(e.getMessage());            
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }
    }       
    
    
}


