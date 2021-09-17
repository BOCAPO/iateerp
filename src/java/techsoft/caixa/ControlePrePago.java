package techsoft.caixa;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import techsoft.tabelas.TipoVagaBarco;
import techsoft.util.Datas;
import techsoft.clube.ParametroSistema;
import techsoft.tabelas.Funcionario;

@Controller
public class ControlePrePago{
    

    @App("6290")
    public static void listar(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if("consultar".equals(acao)){
            request.setAttribute("acao", acao); 
            if (request.getParameter("idFuncionario").equals("")){
                request.setAttribute("tipoPessoa", "S");
                Socio s = Socio.getInstance(Integer.parseInt(request.getParameter("matricula")), 0, Integer.parseInt(request.getParameter("idCategoria")));
                
                request.setAttribute("matricula", request.getParameter("matricula"));
                request.setAttribute("categoria", request.getParameter("idCategoria"));
                if (s!=null){
                    request.setAttribute("nome", s.getNome());
                }else{
                    request.setAttribute("nome", "");
                }
                
                
            }else{
                request.setAttribute("tipoPessoa", "F");
                Funcionario f = Funcionario.getInstance(Integer.parseInt(request.getParameter("idFuncionario")));
                
                request.setAttribute("nome", f.getNome());
                request.setAttribute("idFuncionario", f.getId());

            }
            
            request.setAttribute("dtInicio", request.getParameter("dtInicio"));
            request.setAttribute("dtFim", request.getParameter("dtFim"));
            
            request.getRequestDispatcher("/pages/6290.jsp").forward(request, response);
        }else if("imprime".equals(acao)){
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy");
            
            Date d1 = Datas.parse(request.getParameter("dtInicio"));
            Date d2 = Datas.parse(request.getParameter("dtFim"));
            
            StringBuilder sql = new StringBuilder();
            Socio s = null;
            Funcionario f = null;
            
            sql.append("EXEC SP_EXTRATO_PRE_PAGO ");
            
            if (request.getParameter("idFuncionario").equals("")){
                s = Socio.getInstance(Integer.parseInt(request.getParameter("matricula")), 0, Integer.parseInt(request.getParameter("idCategoria")));
                
                sql.append(request.getParameter("matricula") + ", ");
                sql.append(request.getParameter("idCategoria") + ", ");
                sql.append("NULL, '");
            }else{
                f = Funcionario.getInstance(Integer.parseInt(request.getParameter("idFuncionario")));
                
                sql.append("NULL, NULL, " + request.getParameter("idFuncionario") + ", '");
            }
               
            sql.append(fmt.format(d1));
            sql.append(" 00:00:00', '");
            sql.append(fmt.format(d2));
            sql.append(" 23:59:59'");
            
            request.setAttribute("titulo", "Extrato Iate Card Pré");
            
            String titulo2 = "";
            if (request.getParameter("idFuncionario").equals("")){
                titulo2 = request.getParameter("matricula") + "/" + request.getParameter("idCategoria") + " - ";
                if (s!=null){
                    titulo2 = titulo2 + s.getNome();
                }else{
                    titulo2 = titulo2 + "EXCLUIDO";
                }
                
            }else{
                titulo2 = f.getNome();
            }
            request.setAttribute("titulo2", titulo2);
            
            
            request.setAttribute("sql", sql);
            request.setAttribute("quebra1", "false");
            request.setAttribute("quebra2", "false");
            
            request.setAttribute("total1", "-1");
            request.setAttribute("total2", "-1");
            request.setAttribute("total3", "-1");
            request.setAttribute("total4", "-1");

            request.getRequestDispatcher("/pages/listagem.jsp").forward(request, response);              
        }else{
            request.getRequestDispatcher("/pages/6290.jsp").forward(request, response);
        }
    }
    
}
