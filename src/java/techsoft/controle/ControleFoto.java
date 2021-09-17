package techsoft.controle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import techsoft.db.PoolFoto;

@WebServlet("/f")
public class ControleFoto extends HttpServlet {

    private static final Logger log = Logger.getLogger(ControleFoto.class.getCanonicalName());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tb = request.getParameter("tb");
        if (tb == null) tb = "";
        
        if        (tb.equals("1")) {
        
        } else if (tb.equals("2")) {
            /* /f?tb=2&cd=xxxxx
             * TB_FOTO_AUTORIZACAO
             *   - CD_AUTORIZACAO    SMALLINT
             *   - FOTO_AUTORIZACAO  IMAGE
             */ 
            String sql = "SELECT FOTO_AUTORIZACAO FROM TB_FOTO_AUTORIZACAO WHERE CD_AUTORIZACAO = ?";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("cd")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
            
        } else if (tb.equals("21")) {
            String sql = "SELECT FOTO_AUTORIZACAO FROM TB_FOTO_AUTORIZACAO WHERE CD_AUTORIZACAO = (SELECT CD_AUTORIZACAO FROM IATE..TB_AUTOR_ESPECIAL WHERE NR_AUTORIZACAO = ?)";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("nr")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }

                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }

        } else if (tb.equals("3")) {
            /* /f?tb=3&cd=xxxxxxxxxx&brc=xxxxx
             * TB_FOTO_BARCO
             *   - CD_FOTO_BARCO     INT
             *   - CD_BARCO          SMALLINT
             *   - FOTO_BARCO        IMAGE
             */ 
            String sql = "SELECT FOTO_BARCO FROM TB_FOTO_BARCO WHERE CD_FOTO_BARCO = ?";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("cd")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
            
            
        } else if (tb.equals("4")) {
            /* /f?tb=4&nr=xxxxxxxxxx
             * TB_FOTO_CONVITE
             *   - NR_CONVITE        NUMERIC(10,0)
             *   - FOTO_CONVIDADO    IMAGE
             */ 
            String sql = "SELECT FOTO_CONVIDADO FROM TB_FOTO_CONVITE WHERE NR_CONVITE = ?";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setBigDecimal(1, new BigDecimal(request.getParameter("cd")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
            
            
        } else if (tb.equals("5")) {
            /* /f?tb=5&cd=xxxxx
             * TB_FOTO_FUNCIONARIO
             *   - CD_FUNCIONARIO    SMALLINT
             *   - FOTO_FUNCIONARIO  IMAGE
             */
            String sql = "SELECT FOTO_FUNCIONARIO FROM TB_FOTO_FUNCIONARIO WHERE CD_FUNCIONARIO = ?";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("cd")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
            
        } else if (tb.equals("51")) {
            String sql = "SELECT FOTO_FUNCIONARIO FROM TB_FOTO_FUNCIONARIO WHERE CD_FUNCIONARIO = (SELECT CD_FUNCIONARIO FROM IATE..TB_FUNCIONARIO WHERE NR_CRACHA = ?)";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("nr")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }

        } else if (tb.equals("6")) {
            /* /f?tb=6&mat=xxxxxxxxxx&seq=xxxxx&cat=xxxxx
             * TB_FOTO_PESSOA
             *   - CD_MATRICULA      INT
             *   - SEQ_DEPENDENTE    SMALLINT
             *   - CD_CATEGORIA      SMALLINT
             *   - FOTO_PESSOA       IMAGE
             */
            String sql = "SELECT FOTO_PESSOA FROM TB_FOTO_PESSOA WHERE CD_MATRICULA = ? AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";
                    
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("mat")));                
                    p.setShort(2, Short.parseShort(request.getParameter("seq")));
                    p.setShort(3, Short.parseShort(request.getParameter("cat")));
                }catch(Exception e){
                    p.setInt(1, 0);
                    p.setInt(2, 0);
                    p.setInt(3, 0);
                }
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
        } else if (tb.equals("61")) {
            String sql = "SELECT FOTO_PESSOA FROM TB_FOTO_PESSOA T1, IATE..TB_PESSOA T2 " +
                            "WHERE T1.CD_MATRICULA   = T2.CD_MATRICULA " +
                            " AND  T1.CD_CATEGORIA   = T2.CD_CATEGORIA " +
                            " AND  T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE " +
                            " AND  T2.NR_CARTEIRA_PESSOA = ? "; 
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("cart")));                
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
            
        } else if (tb.equals("62")) {
            String sql = "SELECT TOP 1 FOTO_PESSOA FROM TB_FOTO_PESSOA T1, IATE..TB_MATRICULA_CURSO T2 " +
                            "WHERE T1.CD_MATRICULA   = T2.CD_MATRICULA " +
                            " AND  T1.CD_CATEGORIA   = T2.CD_CATEGORIA " +
                            " AND  T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE " +
                            " AND  T2.NR_PASSAPORTE = ?";
                     
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("pass")));                
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
        } else if (tb.equals("7")) {
            /* /f?tb=7&cd=xxxxx
             * TB_FOTO_VISITANTE
             *   - CD_VISITANTE      SMALLINT
             *   - FOTO_VISITANTE    IMAGE
             */
            String sql = "SELECT FOTO_VISITANTE FROM TB_FOTO_VISITANTE WHERE CD_VISITANTE = ?";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("cd")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
            
        } else if (tb.equals("71")) {
            String sql = "SELECT FOTO_VISITANTE FROM TB_FOTO_VISITANTE WHERE CD_VISITANTE = (SELECT CD_VISITANTE FROM IATE..TB_REGISTRO_VISITANTE WHERE DT_ENTRADA = (SELECT MAX(DT_ENTRADA) FROM IATE..TB_REGISTRO_VISITANTE WHERE NR_CRACHA = ?))";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("nr")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
        } else if (tb.equals("8")) {
            /* /f?tb=7&cd=xxxxx
             * TB_FOTO_VISITANTE
             *   - CD_VISITANTE      SMALLINT
             *   - FOTO_VISITANTE    IMAGE
             */
            String sql = "SELECT FOTO_CARRO FROM TB_FOTO_CARRO_SOCIO WHERE CD_CARRO = ?";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("cd")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
            
        } else if (tb.equals("9")) {
            /* /f?tb=8&cd=xxxxx
             * TB_FOTO_CARRO_FUNCIONARIO
             */
            String sql = "SELECT FOTO_CARRO FROM TB_FOTO_CARRO_FUNCIONARIO WHERE CD_CARRO = ?";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("cd")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
        } else if (tb.equals("10")) {
            /* /f?tb=6&mat=xxxxxxxxxx&seq=xxxxx&cat=xxxxx
             * TB_FOTO_PESSOA
             *   - CD_MATRICULA      INT
             *   - SEQ_DEPENDENTE    SMALLINT
             *   - CD_CATEGORIA      SMALLINT
             *   - FOTO_PESSOA       IMAGE
             */
            String sql = "SELECT FOTO_ATESTADO FROM TB_ATESTADO_MEDICO_PESSOA WHERE CD_MATRICULA = ? AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";
                    
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("mat")));                
                    p.setShort(2, Short.parseShort(request.getParameter("seq")));
                    p.setShort(3, Short.parseShort(request.getParameter("cat")));
                }catch(Exception e){
                    p.setInt(1, 0);
                    p.setInt(2, 0);
                    p.setInt(3, 0);
                }
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
            }
        } else if (tb.equals("20")) {
            /* /f?tb=3&cd=xxxxxxxxxx&brc=xxxxx
             * TB_FOTO_BARCO
             *   - CD_FOTO_BARCO     INT
             *   - CD_BARCO          SMALLINT
             *   - FOTO_BARCO        IMAGE
             */ 
            String sql = "SELECT FOTO_OBJETO FROM TB_FOTO_ACHADOS_PERDIDOS WHERE CD_FOTO_OBJETO = ?";
            Connection cn = null;
            OutputStream out = null;
            try {
                cn = PoolFoto.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                try{
                    p.setInt(1, Integer.parseInt(request.getParameter("cd")));
                }catch(Exception e){
                    p.setInt(1, 0);
                }
                ResultSet rs = p.executeQuery();
                if (rs.next()) {
                    InputStream in = rs.getBinaryStream(1);
                    response.setContentType("image/jpeg");
                    out = response.getOutputStream();
                    IOUtils.copy(in, out);
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            } finally {
                IOUtils.closeQuietly(out);
                try {cn.close();} catch (SQLException e) {log.severe(e.getMessage());}
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
