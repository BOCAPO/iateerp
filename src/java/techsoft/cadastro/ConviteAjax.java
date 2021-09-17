package techsoft.cadastro; 

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import techsoft.db.Pool;
import techsoft.seguranca.Auditoria;
import java.util.logging.Logger;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import techsoft.operacoes.ConviteTermica;
import techsoft.seguranca.ParametroAuditoria;
import techsoft.util.Datas;

@WebServlet(urlPatterns={"/ConviteAjax/*"}) 
public class ConviteAjax extends HttpServlet {

    private static final Logger log = Logger.getLogger("techsoft.cadastro.ConviteAjax");    

    private String matricula;
    private String categoria;
    private String responsavel;
    private String convidado;
    private String usuario;
    private String estacionamento;
    private String cpf;
    private String docEst;
    private String rg;
    private String orgaoExp;
    private String dtNasc;
    private String nrConvite = "";
    private String categoriaConvite;
    private String convenio;
    private String reserva;
    private String validade;
    private Connection cn = null;

    @Override
    public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String tipo = request.getParameter("tipo");

        if (tipo.equals("1")){
            cpf = request.getParameter("cpf");
            docEst = request.getParameter("docEst");
            
            int nrConvite = 0;
            try{
                nrConvite = Integer.parseInt(request.getParameter("nrConv"));
            } catch (Exception e) {}

            
            String sql = "SELECT COUNT(*) QT " +
                         "FROM TB_CONVITE " + 
                         "WHERE YEAR(DT_MAX_UTILIZACAO) = YEAR(GETDATE()) " + 
                         "AND CD_CATEGORIA_CONVITE IN ('GR', 'VA', 'VC', 'VD', 'VN', 'VO', 'VP', 'VV', 'CH') " +
                         "AND CD_STATUS_CONVITE <> 'CA' ";
            if (cpf.equals("")){
                sql = sql + "AND NR_DOC_ESTRANGEIRO = '" + docEst + "'";
            }else{
                sql = sql + "AND CPF_CONVIDADO = '" + cpf + "'";
            }
            
            if(nrConvite>0){
                sql = sql + "AND NR_CONVITE <> " + nrConvite;
            }

            Connection cn = null;

            try {
                cn = Pool.getInstance().getConnection();
                ResultSet rs = cn.createStatement().executeQuery(sql);
                if (rs.next()) {
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(rs.getString(1));
                }
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }finally{
                try {
                    cn.close();
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }
            }
        }else if (tipo.equals("2")){
            String nome = URLDecoder.decode(request.getParameter("nome"), "UTF-8");

            String sql = "{call SP_VERIFICA_LISTA_NEGRA (?)}";
            Connection cn = null;

            try {
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                p.setString(1, nome);
                ResultSet rs = p.executeQuery();
                if(rs.next()){
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(rs.getString(1)+rs.getString(2));
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }finally{
                try {
                    cn.close();
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }
            }
        }else if (tipo.equals("3")){
            String usuario = request.getParameter("usuario");
            String senha = request.getParameter("senha");

            String sql = "{call SP_VERIFICA_SUPERVISOR (?, ?, ?)}";
            Connection cn = null;

            try {
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                p.setString(1, usuario);
                p.setString(2, senha);
                p.setInt(3, 1077);

                ResultSet rs = p.executeQuery();
                if(rs.next()){
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(rs.getString(1));
                }
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }finally{
                try {
                    cn.close();
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }
            }

        }else if (tipo.equals("4")){

            recebeParametrosConvite(request);
            
            incluiConvite(request.getRemoteAddr());
            
            try {
                cn.commit();
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
            
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(nrConvite);
            

        }else if (tipo.equals("5")){

            String convidado = request.getParameter("convidado");
            String cpf = request.getParameter("cpf");
            String rg = request.getParameter("rg");
            String orgaoExp = request.getParameter("orgaoExp");
            String dtNasc = request.getParameter("dtNasc");
            String docEst = request.getParameter("docEst");

            int nrConvite = Integer.parseInt(request.getParameter("nrConvite"));

           if(1==2){
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(convidado + " - " + nrConvite);
                return;
            }

            Connection cn = null;   
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "7042", request.getRemoteAddr());


            String sql = "UPDATE TB_CONVITE SET NOME_CONVIDADO = ?, CPF_CONVIDADO = ?, RG_CONVIDADO = ?, DE_ORGAO_EXP_CONVIDADO = ?, DT_NASC_CONVIDADO = ?, NR_DOC_ESTRANGEIRO = ? WHERE NR_CONVITE = ?";

            try {
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);

                p.setString(1, convidado);
                p.setString(2, cpf);
                p.setString(3, rg);
                p.setString(4, orgaoExp);
                
                if (dtNasc == ""){
                    p.setNull(5, java.sql.Types.DATE);
                }else{
                    Date dataNasc = Datas.parse(dtNasc);
                    p.setDate(5, new java.sql.Date(dataNasc.getTime()));
                }
                
                p.setString(6, docEst);
                p.setInt(7, nrConvite);

                p.executeUpdate();
                cn.commit();

                audit.registrarMudanca(sql, convidado, cpf, rg, orgaoExp, String.valueOf(nrConvite));

                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(nrConvite);

            } catch (SQLException e) {
                log.severe(e.getMessage());
            }finally{
                try {
                    cn.close();
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }
            }

        }else if (tipo.equals("6")){
            String retorno = "OK";

           if(1==2){
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(request.getParameter("matricula") + " - " + request.getParameter("categoria"));
                return;
            }


            int matricula = Integer.parseInt(request.getParameter("matricula"));
            int categoria = Integer.parseInt(request.getParameter("categoria"));

            String sql = "{call SP_RECUPERA_CARNE_PENDENTE (?, ?)}";
            Connection cn = null;

            try {
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                p.setInt(1, matricula);
                p.setInt(2, categoria);

                ResultSet rs = p.executeQuery();
                if(rs.next()){
                    if (rs.getString("CD_ACESSO_PERMITIDO").equals("PA")){
                        retorno = "PA";
                    }
                }
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(retorno);
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }finally{
                try {
                    cn.close();
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }
            }
        }else if (tipo.equals("7")){

            int nrConvite = Integer.parseInt(request.getParameter("nrConvite"));
            String addr = request.getRemoteAddr();
            //String iateHost = request.getLocalAddr();
            //iateHost = "localhost";
            String iateHost = getIpAddress();
            int iatePort = request.getLocalPort();
            String iateApp = request.getContextPath();                

            ConviteTermica convite = new ConviteTermica(nrConvite, iateHost, iatePort, iateApp);
            try{
                convite.emitir(addr);
            }catch(Exception ex) {
                request.setAttribute("app", "1070");
                request.setAttribute("msg", ex.getMessage());
                request.getRequestDispatcher("/pages/erro.jsp").forward(request, response);
            }       
            
        }else if (tipo.equals("8")){

            String matricula = request.getParameter("matricula");
            String categoria = request.getParameter("categoria");
            String saldo = request.getParameter("saldo");

            Connection cn = null;   
            Auditoria audit = new Auditoria(request.getUserPrincipal().getName(), "1077", request.getRemoteAddr());


            String sql = "UPDATE TB_COMPLEMENTO SET SD_CONVITE = ? WHERE CD_MATRICULA = ? AND CD_CATEGORIA = ?";

            try {
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);

                p.setInt(1, Integer.parseInt(saldo));
                p.setInt(2, Integer.parseInt(matricula));
                p.setInt(3, Integer.parseInt(categoria));
                
                p.executeUpdate();
                cn.commit();

                audit.registrarMudanca(sql, saldo, matricula, categoria);

                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("OK");

            } catch (SQLException e) {
                log.severe(e.getMessage());
            }finally{
                try {
                    cn.close();
                } catch (SQLException e) {
                    log.severe(e.getMessage());
                }
            }            
        }else if (tipo.equals("9")){

            recebeParametrosConvite(request);
            
            incluiConvite(request.getRemoteAddr());
            
            String sql = " INSERT TB_COMPLEMENTO_CONV_ESPORTIVO  (" +
                         " NR_CONVITE,	DT_INIC_VALIDADE, DT_FIM_VALIDADE, DE_MODALIDADE, CD_TIPO_CONVITE, " +
                         " ACESSA_SEGUNDA, HH_ENTRA_SEGUNDA, HH_SAI_SEGUNDA, " +
                         " ACESSA_TERCA, HH_ENTRA_TERCA, HH_SAI_TERCA, ACESSA_QUARTA, " +
                         " HH_ENTRA_QUARTA, HH_SAI_QUARTA, ACESSA_QUINTA, HH_ENTRA_QUINTA, " +
                         " HH_SAI_QUINTA, ACESSA_SEXTA, HH_ENTRA_SEXTA, HH_SAI_SEXTA) " +
                         " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            ParametroAuditoria par = new ParametroAuditoria();

            try {

                request.setCharacterEncoding("UTF-8");
                String inicioValidade = request.getParameter("inicioValidade");
                String fimValidade = request.getParameter("fimValidade");
                String modalidade = request.getParameter("modalidade");
                String tipoConvite = request.getParameter("tipoConvite");
                String hhEntraSegunda = request.getParameter("hhEntraSegunda").replaceAll(":","");
                String hhSaiSegunda = request.getParameter("hhSaiSegunda").replaceAll(":","");
                String hhEntraTerca = request.getParameter("hhEntraTerca").replaceAll(":","");
                String hhSaiTerca = request.getParameter("hhSaiTerca").replaceAll(":","");
                String hhEntraQuarta = request.getParameter("hhEntraQuarta").replaceAll(":","");
                String hhSaiQuarta = request.getParameter("hhSaiQuarta").replaceAll(":","");
                String hhEntraQuinta = request.getParameter("hhEntraQuinta").replaceAll(":","");
                String hhSaiQuinta = request.getParameter("hhSaiQuinta").replaceAll(":","");
                String hhEntraSexta = request.getParameter("hhEntraSexta").replaceAll(":","");
                String hhSaiSexta = request.getParameter("hhSaiSexta").replaceAll(":","");


                Date data = null;
                cn = Pool.getInstance().getConnection();
                PreparedStatement p = cn.prepareStatement(sql);
                p.setInt(1, par.getSetParametro(Integer.parseInt(nrConvite)));
                data = Datas.parse(par.getSetParametro(inicioValidade));
                p.setDate(2, new java.sql.Date(par.getSetParametro(data.getTime())));
                data = Datas.parse(par.getSetParametro(fimValidade));
                p.setDate(3, new java.sql.Date(par.getSetParametro(data.getTime())));
                p.setString(4, par.getSetParametro(modalidade));
                p.setString(5, par.getSetParametro(tipoConvite));

                if ((!hhEntraSegunda.equals("")) || (!hhSaiSegunda.equals(""))){
                    p.setInt(6, par.getSetParametro(1));
                    p.setString(7, par.getSetParametro(hhEntraSegunda));
                    p.setString(8, par.getSetParametro(hhSaiSegunda));
                }else{
                    p.setInt(6, par.getSetParametro(0));
                    p.setNull(7, java.sql.Types.VARCHAR);
                    p.setNull(8, java.sql.Types.VARCHAR);
                }
                if ((!hhEntraTerca.equals("")) || (!hhSaiTerca.equals(""))){
                    p.setInt(9, par.getSetParametro(1));
                    p.setString(10, par.getSetParametro(hhEntraTerca));
                    p.setString(11, par.getSetParametro(hhSaiTerca));
                }else{
                    p.setInt(9, par.getSetParametro(0));
                    p.setNull(10, java.sql.Types.VARCHAR);
                    p.setNull(11, java.sql.Types.VARCHAR);
                }
                if ((!hhEntraQuarta.equals("")) || (!hhSaiQuarta.equals(""))){
                    p.setInt(12, par.getSetParametro(1));
                    p.setString(13, par.getSetParametro(hhEntraQuarta));
                    p.setString(14, par.getSetParametro(hhSaiQuarta));
                }else{
                    p.setInt(12, par.getSetParametro(0));
                    p.setNull(13, java.sql.Types.VARCHAR);
                    p.setNull(14, java.sql.Types.VARCHAR);
                }
                if ((!hhEntraQuinta.equals("")) || (!hhSaiQuinta.equals(""))){
                    p.setInt(15, par.getSetParametro(1));
                    p.setString(16, par.getSetParametro(hhEntraQuinta));
                    p.setString(17, par.getSetParametro(hhSaiQuinta));
                }else{
                    p.setInt(15, par.getSetParametro(0));
                    p.setNull(16, java.sql.Types.VARCHAR);
                    p.setNull(17, java.sql.Types.VARCHAR);
                }
                if ((!hhEntraSexta.equals("")) || (!hhSaiSexta.equals(""))){
                    p.setInt(18, par.getSetParametro(1));
                    p.setString(19, par.getSetParametro(hhEntraSexta));
                    p.setString(20, par.getSetParametro(hhSaiSexta));
                }else{
                    p.setInt(18, par.getSetParametro(0));
                    p.setNull(19, java.sql.Types.VARCHAR);
                    p.setNull(20, java.sql.Types.VARCHAR);
                }

                p.executeUpdate();
                cn.commit();
            
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(nrConvite);
            
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }finally{
                try {
                    cn.close();
                } catch (SQLException ex) {
                    log.severe(ex.getMessage());
                }
            }
        }
    }
    
    public static String getIpAddress() { 
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        String ipAddress=inetAddress.getHostAddress().toString();
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            log.severe("Socket exception in GetIP Address of Utilities");
        }
        return null; 
    }    
    
    public void recebeParametrosConvite(HttpServletRequest request) throws IOException, ServletException {
        
        request.setCharacterEncoding("UTF-8");
        matricula = request.getParameter("matricula");
        categoria = request.getParameter("categoria");
        responsavel = URLDecoder.decode(request.getParameter("responsavel"), "UTF-8");
        convidado = URLDecoder.decode(request.getParameter("convidado"), "UTF-8");
        categoriaConvite = request.getParameter("categoriaConvite");
        usuario = request.getParameter("usuario");
        estacionamento = request.getParameter("estacionamento");
        convenio = request.getParameter("convenio");
        reserva = request.getParameter("reserva");
        cpf = request.getParameter("cpf");
        docEst = request.getParameter("docEst");
        rg = request.getParameter("rg");
        orgaoExp = request.getParameter("orgaoExp");
        dtNasc = request.getParameter("dtNasc");
        validade = request.getParameter("validade");

    }
    
    public String incluiConvite(String enderecoCliente){
        
        String sql = "{call SP_GERA_NR_CONVITE ()}";
        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                 nrConvite = rs.getString(1);
            }
            cn.commit();
        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException e) {
                log.severe(e.getMessage());
            }
        }

        sql = "{call SP_INCLUIR_CONVITE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
/*                
        @CONVITE 		NUMERIC (10)	,
        @MATRICULA 		SMALLINT	,
        @SEQ_DEPENDENTE 	SMALLINT	,
        @CATEGORIA 		SMALLINT	,
        @RETIRADA 		DATETIME	,
        @UTILIZACAO 		DATETIME	,
        @SACADOR 		CHAR (40)	,
        @CONVIDADO 		CHAR (40)	,
        @CATEGORIA_CONVITE 	CHAR (2)	,
        @STATUS 		CHAR (2)	,
        @USER_ACESSO_SISTEMA 	CHAR (12)	,
        @ESTACIONAMENTO_INTERNO	BIT		,
        @CD_CONVENIO 		SMALLINT	,
        @CD_TIPO_CONVITE 	CHAR(1)		,
        @SEQ_RESERVA	INT,
        @CD_LOTE_FAMILIA 	SMALLINT	,
        @CPF_CONVIDADO	VARCHAR(11),
        @DT_NASC_CONVIDADO	DATETIME,
        @RG_CONVIDADO	VARCHAR(10),
        @DE_ORGAO_EXP_CONVIDADO	VARCHAR(15),
    ?   @NR_DOC_ESTRANGEIRO VARCHAR(30)


*/

        try {
            cn = Pool.getInstance().getConnection();
            PreparedStatement p = cn.prepareStatement(sql);
            ParametroAuditoria par = new ParametroAuditoria();
            Auditoria audit = new Auditoria(usuario, "1071", enderecoCliente);

            p.setInt(1, par.getSetParametro(Integer.parseInt(nrConvite)));

            if(categoriaConvite.equals("EC")){
                p.setNull(2, java.sql.Types.NUMERIC);
                par.getSetNull();
                p.setNull(3, java.sql.Types.NUMERIC);
                par.getSetNull();
                p.setNull(4, java.sql.Types.NUMERIC);
                par.getSetNull();
            }else{
                p.setInt(2, par.getSetParametro(Integer.parseInt(matricula)));
                p.setInt(3, par.getSetParametro(0));
                p.setInt(4, par.getSetParametro(Integer.parseInt(categoria)));
            }
            p.setDate(5, new java.sql.Date(par.getSetParametro(new Date()).getTime()));
            Date data = Datas.parse(par.getSetParametro(validade));
            p.setDate(6, new java.sql.Date(par.getSetParametro(data.getTime())));
            p.setString(7, par.getSetParametro(responsavel));
            p.setString(8, par.getSetParametro(convidado));
            p.setString(9, par.getSetParametro(categoriaConvite));
            p.setString(10, par.getSetParametro("NU"));
            p.setString(11, par.getSetParametro(usuario));
            if(estacionamento.equals("Externo")){
                p.setBoolean(12, par.getSetParametro(false));
            }else{
                p.setBoolean(12, par.getSetParametro(true));
            }
            if(categoriaConvite.equals("EC")){
                p.setInt(13, par.getSetParametro(Integer.parseInt(convenio)));
            }else{
                p.setNull(13, java.sql.Types.NUMERIC);
                par.getSetNull();
            }
            p.setString(14, par.getSetParametro("N"));

            if(categoriaConvite.equals("CH")){
                p.setInt(15, par.getSetParametro(Integer.parseInt(reserva)));
            }else{
                p.setNull(15, java.sql.Types.NUMERIC);
                par.getSetNull();
            }
            p.setNull(16, java.sql.Types.NUMERIC);
            par.getSetNull();

            if (cpf == ""){
                p.setNull(17, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(17, par.getSetParametro(cpf));
            }
            if (dtNasc == ""){
                p.setNull(18, java.sql.Types.DATE);
                par.getSetNull();
            }else{
                Date dataNasc = Datas.parse(dtNasc);
                p.setDate(18, new java.sql.Date(par.getSetParametro(dataNasc).getTime()));
            }
            if (rg == ""){
                p.setNull(19, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(19, par.getSetParametro(rg));
            }
            if (orgaoExp == ""){
                p.setNull(20, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(20, par.getSetParametro(orgaoExp));
            }
            if (docEst == ""){
                p.setNull(21, java.sql.Types.VARCHAR);
                par.getSetNull();
            }else{
                p.setString(21, par.getSetParametro(docEst));
            }

            p.executeUpdate();
            cn.commit();
            

        } catch (SQLException e) {
            log.severe(e.getMessage());
        }finally{
            try {
                cn.close();
            } catch (SQLException ex) {
                log.severe(ex.getMessage());
            }
        }
        
        return nrConvite;
    }
}

