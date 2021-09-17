<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
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
        
</style>

<sql:query var="rs" dataSource="jdbc/iate">
    EXEC SP_DECLARACAO_NADA_CONSTA ?, ?, ?
    <sql:param value="${idCarne}"/>
    <sql:param value="${usuarioAtual}"/>
    <sql:param value="${maquina}"/>
</sql:query>
    
<table width="100%" >
    <tr>
        <td width="10%"></td>
        <td width="80%">
            <table width="100%" >
                <tr>
                    <td>
                      <table width="100%" >
                      <tr>
                        <th align="left" width="100px" rowspan="2" ><img src="imagens/logo-intro.png" width="70" height="55"/></th>
                        <th align="center" height="0" class="tituloRelatorio">
                            <br>
                            DECLARAÇÃO DE NADA CONSTA
                        </th>
                        <th width="100px" class="dados" align="right"><fmt:formatDate pattern="HH:mm:ss" value="<%=new java.util.Date()%>"/></th>
                      </tr>
                      <tr>
                        <th align="center" class="subTituloRelatorio"></th>
                        <th class="dados" align="right"><fmt:formatDate pattern="dd/MM/yyyy" value="<%=new java.util.Date()%>"/></th>
                      </tr>
                      </table>
                    </td>
                </tr>  
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td width="35%"></td>
                                <td width="30%">
                                    <br><br><br><br><br><br>
                                    <div style="width: 500px; ">  
                                        ${rs.rows[0].MSG}
                                        
                                    </div> 
                                    <br><br><br>
                                </td>
                                <td width="35%"></td>
                            </tr>
                        </table>
                    </td>
                </tr>    

                <tr>
                    <td align="right">
                        <p>Brasília, <fmt:formatDate pattern="dd 'de' MMMMM 'de' yyyy" value="<%=new java.util.Date()%>"/></p>
                        <br><br>
                    </td>
                </tr>
                
                <tr>
                  <td height="100" align="center">
                      <p>&nbsp;Autenticação:<br />
                         <font color="#006689"><strong>&nbsp;${rs.rows[0].CD_AUTENTICACAO} - ${rs.rows[0].HASH_AUTENTICACAO}</strong></font>
                         
                      </p>
                  </td>
                </tr>
                
                <tr>
                    <td align="center">
                        <p>
                            <b>Diretoria Financeira</b>
                            <br><br>
                            *Observação: Esta declaração não quita débitos de consumos e/ou parcelamentos vincendos.                
                        </p>
                    </td>
                </tr>    
            </table>
        </td>                        
        <td width="10%"></td>
    </tr>    
</table>
                        

</body>
</html>