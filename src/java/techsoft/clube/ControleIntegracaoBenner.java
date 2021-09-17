package techsoft.clube;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import techsoft.cadastro.AutorizadoBarco;
import techsoft.cadastro.MovimentoBarco;
import techsoft.cadastro.Socio;
import techsoft.cadastro.SocioBarco;
import techsoft.cadastro.Titular;
import techsoft.controle.annotation.App;
import techsoft.controle.annotation.Controller;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.tabelas.CategoriaNautica;
import techsoft.util.Datas;

@Controller
public class ControleIntegracaoBenner{
    

    @App("2550")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");
        
        if("geraArquivo".equals(acao)){
            String arquivo = request.getParameter("arquivo").replace("/", "\\");
            
            Date dtGeracao = Datas.parseComFormato(request.getParameter("dtGeracao"), "dd/MM/yyyy HH:mm:ss.SSS");
            
            String arqDest = request.getServletContext().getRealPath("/");
            arqDest += "Integ-Benner.txt";
            
            gerarArquivo(dtGeracao, arquivo, arqDest);

            download(arqDest, request, response);
            
        }else{
            request.getRequestDispatcher("/pages/2550-lista.jsp").forward(request, response);
        }

    }
    
    public static void gerarArquivo(Date dtGeracao, String arqIntegracao, String arqDest){
            Connection cn = null;

        Logger log = Logger.getLogger("techsoft.clube.ItemEmprestimoAcademia");
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            
            cn = Pool.getInstance().getConnection();
            CallableStatement cal = null;
            
            String sql = "SELECT DE_LINHA FROM TB_INTEGRACAO_BENNER_DET_ARQUIVOS WHERE DT_GERACAO = '" + sdf.format(dtGeracao) + "' AND DE_ARQUIVO = '" + arqIntegracao + "'";
            
            cal = cn.prepareCall(sql);
            
            ResultSet rs = cal.executeQuery();
            
            FileWriter w = new FileWriter(arqDest);
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
