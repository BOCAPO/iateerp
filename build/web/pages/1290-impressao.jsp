<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%@page import="techsoft.db.Pool"%>
<%@page import="java.sql.*"%>

<fmt:setLocale value="pt_BR"/>
<html>
<!-- ${pageContext.request.servletPath} -->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
    <title>Iate Clube de Brasília</title>
</head>

<body class="internas">

<style type="text/css">
.tituloRelatorio {
	font-family: "Arial";
	font-size: 16px;
	font-style: italic;
	font-weight: bold;
	color: #000;
}

.subTituloRelatorio {
	font-family: "Arial";
	font-size: 12px;
	font-style: italic;
	color: #000;
}

.dados {
	font-family: "Arial";
	font-size: 12px;
	color: #000;
}
        
.tituloDados {
	font-family: "Arial";
	font-size: 12px;
	color: #000;
	font-weight: bold;
}
.divisoria { border-bottom:1px #000 solid;  padding-top:1px; margin-bottom:1px; }        
.divisoria2 { border-bottom:1px #000 dashed;  padding-top:1px; margin-bottom:1px; }        
</style>

<% 

String sql = request.getAttribute("sql").toString();
String tipo = request.getAttribute("tipo").toString();

SimpleDateFormat fmtDate = new SimpleDateFormat("dd/MM/yyyy");
SimpleDateFormat fmtHour = new SimpleDateFormat("HH:mm:ss");
Connection cn = Pool.getInstance().getConnection();
//out.print(sql);

ResultSet rs = cn.createStatement().executeQuery(sql);
%>

<table width="100%" >
    <tr>
        <td>
            <table width="100%" >
                <tr>
                  <th align="left" width="150px" rowspan="2" ><img src="imagens/logo-intro.png" width="70" height="55"/></th>
                  <th align="center" height="0" class="tituloRelatorio">Relatório de Acesso ao Clube - Quantidade</th>
                  <th width="150px" class="dados" align="right"><%= fmtHour.format(new Date()) %></th>
                </tr>
                <tr>
                  <th align="center" class="subTituloRelatorio"></th>
                  <th class="dados" align="right"><%= fmtDate.format(new Date()) %></th>
                </tr>
            </table>
        </td>
    </tr>  
    <tr>
        <td>
            <div class="divisoria"></div>
            <div class="divisoria"></div>
        </td>
    </tr>
    <tr>
        <td class="tituloRelatorio">
            Documento: <%= tipo %>
        </td>
    </tr>
    <tr>
        <td class="tituloRelatorio">
            <%
            if (rs.next()){
                out.print("Acessos: " + rs.getString(1));
            }
            %>
                
        </td>
    </tr>
    <tr>
        <td class="tituloRelatorio" align="center">
            <div class="divisoria2"></div>
            Filtro
            <div class="divisoria2"></div>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" >
                <tr valign="top">
                    <td>
                        <table width="100%" >
                            <tr>
                                <td class="dados">
                                    <b>Período</b>
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    DE ${dataIni} ATÉ ${dataFim}
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    &nbsp;
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    <b>Locais</b>
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    ${local}
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    &nbsp;
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    <b>Nome</b>
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    ${nome}
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    &nbsp;
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    <b>Tipo de Acesso</b>
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    ${tipoAcesso}
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    &nbsp;
                                </td>
                            </tr>
                            
                            <tr>
                                <td class="dados">
                                    <b>${titParm1}</b>
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    ${parm1}
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    &nbsp;
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    <b>${titParm2}</b>
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    ${parm2}
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    &nbsp;
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    <b>${titParm3}</b>
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    ${parm3}
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    &nbsp;
                                </td>
                            </tr>
                            
                        </table>
                    </td>
                    
                    <td>
                        <table width="100%" >
                            <tr>
                                <td class="dados">
                                    <b>${tituloLista1}</b>
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    ${lista1}
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td>
                        <table width="100%" >
                            <tr>
                                <td class="dados">
                                    <b>${tituloLista2}</b>
                                </td>
                            </tr>
                            <tr>
                                <td class="dados">
                                    ${lista2}
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    
</table>

                                
<%
cn.close();
%>

</body>
</html>

