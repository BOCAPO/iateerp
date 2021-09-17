package techsoft.operacoes;

import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import java.util.logging.Logger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import techsoft.util.Datas;

@WebServlet(urlPatterns = {"/EncargosAjax/*"})

public class EncargosAjax extends HttpServlet {

    private static final Logger log = Logger.getLogger("techsoft.operacoes.EncargosAjax");

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tipo = request.getParameter("tipo");

        if (tipo.equals("1")) {
            Date dtVenc = Datas.parse(request.getParameter("dtVenc"));
            Date dtPag = Datas.parse(request.getParameter("dtPag"));
            String valorStr = request.getParameter("valor");
            String multaStr = request.getParameter("percMulta");
            String valorStr2 = valorStr.replace(".", "").replace(",", ".");
            String multaStr2 = multaStr.replace(".", "").replace(",", ".");
            BigDecimal valor = new BigDecimal(valorStr2);
            BigDecimal multa = new BigDecimal(multaStr2);

            if (1 == 2) {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(dtVenc + "-" + dtPag + "-" + valor + "-" + multa);
            }

            String sql = "{call SP_CALCULA_ENCARGOS_INPC (?,?,?,null,null,?)}";
            Connection cn = null;
            try {
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                p.setDate(1, new java.sql.Date(dtVenc.getTime()));
                p.setDate(2, new java.sql.Date(dtPag.getTime()));
                p.setBigDecimal(3, valor);
                p.setBigDecimal(4, multa);

                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    DecimalFormat df = new DecimalFormat("0.##");
                    String retorno = df.format(rs.getDouble(1));

                    response.getWriter().write(retorno);
                }
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }
            }
        }
    }
}
