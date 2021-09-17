package techsoft.clube; 

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import techsoft.util.Datas;
import techsoft.clube.VerificaRecaptcha;

@WebServlet(urlPatterns={"/RecaptchaAjax/*"})
public class RecaptchaAjax extends HttpServlet {
 
    private static final Logger log = Logger.getLogger("internet.RecaptchaAjax");    

    @Override
    public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {

    String gRecaptchaResponse = request.getParameter("token");
    
    boolean captchaOk = VerificaRecaptcha.verifica(gRecaptchaResponse);
    
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    if (captchaOk){
        response.getWriter().write("OK");
    }else{
        response.getWriter().write("PROBLEMA");
    }
        

    }
}

