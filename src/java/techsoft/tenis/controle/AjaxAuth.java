package techsoft.tenis.controle;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import techsoft.cadastro.Convite;
import techsoft.cadastro.Socio;
import techsoft.db.Pool;
import techsoft.db.PoolFoto;
import techsoft.tenis.JogadorTenis;

@WebServlet("/ajax/auth")
public class AjaxAuth extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean admin = false;
        if (!StringUtils.isEmpty(request.getParameter("admin"))) admin = true;
        
        boolean validate = true;
        if (!StringUtils.isEmpty(request.getParameter("validate"))) validate = false;
        
        String tipo = request.getParameter("tipo");
        if (StringUtils.isEmpty(tipo)) return;
        
        JSONObject json = new JSONObject();
        if (tipo.equals("socio")) {
            Socio s = null;
            String u = null;
            
            if (admin) {
                String matricula = request.getParameter("matricula");
                String categoria = request.getParameter("categoria");
                String dependente = request.getParameter("dependente");
                
                if (StringUtils.isEmpty(matricula)) json.put("erro", "Informe a matrícula do sócio");
                else if (StringUtils.isEmpty(categoria)) json.put("erro", "Informe a categoria do sócio");
                else if (StringUtils.isEmpty(dependente)) json.put("erro", "Informe o sequencial de dependente do sócio");
                else s = AjaxAuth.getSocio(matricula, categoria, dependente, json);
            } else {
                String usuario = request.getParameter("usuario");
                u = usuario;
                String senha = request.getParameter("senha");
                if (StringUtils.isEmpty(usuario)) json.put("erro", "Informe o código de usuário");
                else s = AjaxAuth.getSocio(usuario, senha, json);
            }
            
            if (s != null) {
                JogadorTenis j = new JogadorTenis(s);
                if (validate && !j.isLivre(DateTime.now()))
                    json.put("erro", "Sócio " + j.getNome() + " já está participando de outro jogo");
                else {
                    JSONObject socio = new JSONObject();
                    socio.put("nome", j.getNome());
                    socio.put("tipo", "socio");
                    socio.put("matricula", s.getMatricula());
                    socio.put("dependente", s.getSeqDependente());
                    socio.put("categoria", s.getIdCategoria());
                    String foto = fotoSocio(s);
                    if (!StringUtils.isEmpty(foto)) socio.put("foto", foto);
                    if (u != null) socio.put("usuario", u);
                    json.put("jogador", socio);
                } 
            } else {
                json.put("erro", "Erro ao autenticar o sócio (007)");
            }
        }
        
        if (tipo.equals("convidado")) {
            String  convite = request.getParameter("convite");
            if (convite == null || convite.length() == 0)
                json.put("erro", "Informe o número do convite");
            else {
                Convite c = (validate) ? AjaxAuth.getConvite(convite, json) : Convite.getInstance(Integer.parseInt(convite));
                if (c != null) {
                    JogadorTenis j = new JogadorTenis(c);
                    if (validate && !j.isLivre(new DateTime()))
                        json.put("erro", "Convidado " + j.getNome() + " já está participando de outro jogo");
                    else {
                        JSONObject convidado = new JSONObject();
                        convidado.put("nome", j.getNome());
                        convidado.put("tipo", "convidado");
                        convidado.put("convite", c.getNumero());
                        String foto = fotoConvidado(c);
                        if (!StringUtils.isEmpty(foto)) convidado.put("foto", foto);
                        json.put("jogador", convidado);
                    }
                } else {
                        json.put("erro", "Erro ao autenticar o convidado (008)");
                }
            }
        }
        
        response.setContentType("application/json");  
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    private static Socio getSocio(String user, String password, JSONObject json) {
        Socio socio = null;
        String sql = "SELECT * FROM TB_Usuario_Sistema WHERE USER_ACESSO_SISTEMA = ? AND SENHA_USUARIO_SISTEMA = ?";
        Connection conn = Pool.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);    
            try {
                stmt.setString(1, String.format("%1$-12s", user));
                stmt.setString(2, String.format("%1$-6s", password));
                ResultSet rs = stmt.executeQuery();
                try {
                    if (rs.next())
                        socio = Socio.getInstance(rs.getInt("CD_MATRICULA"), rs.getShort("SEQ_DEPENDENTE"), rs.getShort("CD_CATEGORIA"));
                    else
                        json.put("erro", "Usuário/senha inválidos");
                } catch (SQLException ex) {
                    json.put("erro", "Erro ao autenticar o sócio (001)");
                } finally {
                    try { rs.close(); } catch (SQLException ex) { json.put("erro", "Erro ao autenticar o sócio (002)"); }
                }
            } catch (SQLException ex) {
                json.put("erro", "Erro ao autenticar o sócio (003)");
            } finally {
                try { stmt.close(); } catch (SQLException ex) { json.put("erro", "Erro ao autenticar o sócio (004)"); }
            }
        } catch (SQLException ex) {
            json.put("erro", "Erro ao autenticar o sócio (005)");
        } finally {
            try { conn.close(); } catch (SQLException ex) { json.put("erro", "Erro ao autenticar o sócio (006)"); }
        }
        
        return socio;
    }
    
    private static Socio getSocio(String matricula, String categoria, String dependente, JSONObject json) {
        return Socio.getInstance(Integer.parseInt(matricula), Integer.parseInt(dependente), Integer.parseInt(categoria));
    }

    private static Convite getConvite(String numero, JSONObject json) {
        Convite convite = null;
        String sql = "{CALL SP_LOGIN_CONVITE_TENIS (?)}";
        Connection conn = Pool.getInstance().getConnection();
        try {
            CallableStatement stmt = conn.prepareCall(sql);    
            try {
                stmt.setInt(1, Integer.parseInt(numero));
                ResultSet rs = stmt.executeQuery();
                try {
                    if (rs.next()) {
                        String permite = rs.getString("PERMITE");
                        if ((permite == null) || (permite.length() == 0))
                            json.put("erro", "Erro ao autenticar o convidado (001)");
                        else if (permite.equals("N"))
                            json.put("erro", rs.getString("DE_MSG_RETORNO"));
                        else
                            convite = Convite.getInstance(Integer.parseInt(numero));
                    }
                } catch (SQLException ex) {
                    json.put("erro", "Erro ao autenticar o convidado (002)");
                } finally {
                    try { rs.close(); } catch (SQLException ex) { json.put("erro", "Erro ao autenticar o convidado (003)"); }
                }
            } catch (SQLException ex) {
                json.put("erro", "Erro ao autenticar o convidado (004)");
            } finally {
                try { stmt.close(); } catch (SQLException ex) { json.put("erro", "Erro ao autenticar o convidado (005)"); }
            }
        } catch (SQLException ex) {
            json.put("erro", "Erro ao autenticar o convidado (006)");
        } finally {
            try { conn.close(); } catch (SQLException ex) { json.put("erro", "Erro ao autenticar o convidado (007)"); }
        }

        return convite;
    }
    
    public String fotoSocio(Socio s) {
        String url = null;
        String sql = "SELECT * FROM TB_Foto_Pessoa WHERE CD_MATRICULA = ? AND SEQ_DEPENDENTE = ? AND CD_CATEGORIA = ?";
        Connection conn = PoolFoto.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);    
            try {
                stmt.setObject(1, s.getMatricula(), java.sql.Types.INTEGER);
                stmt.setObject(2, s.getSeqDependente(), java.sql.Types.SMALLINT);
                stmt.setObject(3, s.getIdCategoria(), java.sql.Types.SMALLINT);
                ResultSet rs = stmt.executeQuery();
                try {
                    if (rs.next()) url = "f?tb=6&mat=" + s.getMatricula() +"&seq=" + s.getSeqDependente() + "&cat=" + s.getIdCategoria();
                } catch (SQLException ex) {
                } finally {
                    try { rs.close(); } catch (SQLException ex) { }
                }
            } catch (SQLException ex) {
            } finally {
                try { stmt.close(); } catch (SQLException ex) { }
            }
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) { }
        }
        
        return url;
    }
 
    public String fotoConvidado(Convite c) {
        String url = null;
        String sql = "SELECT * FROM TB_FOTO_CONVITE WHERE NR_CONVITE = ?";
        Connection conn = PoolFoto.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);    
            try {
                stmt.setObject(1, c.getNumero(), java.sql.Types.NUMERIC);
                ResultSet rs = stmt.executeQuery();
                try {
                    if (rs.next()) url = "f?tb=4&nr=" + c.getNumero();
                } catch (SQLException ex) {
                } finally {
                    try { rs.close(); } catch (SQLException ex) { }
                }
            } catch (SQLException ex) {
            } finally {
                try { stmt.close(); } catch (SQLException ex) { }
            }
        } catch (SQLException ex) {
        } finally {
            try { conn.close(); } catch (SQLException ex) { }
        }
        
        return url;
    }   
}
