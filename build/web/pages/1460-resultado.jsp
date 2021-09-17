<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
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
.tituloIate{
	font-family: "Times New Roman";
	font-size: 20px;
	font-weight: bold;
	color:rgb(0,0,150);
}    
.tituloRelatorio {
	font-family: "Arial";
	font-size: 16px;
	font-weight: bold;
	color: #000;
}

.dados {
	font-family: "Arial";
	font-size: 12px;
	color: #000;
}

</style>
<%
SimpleDateFormat fmtDate = new SimpleDateFormat("dd/MM/yyyy");
SimpleDateFormat fmtHour = new SimpleDateFormat("HH:mm:ss");
%>
<table width="100%" >
    <thead>
    <tr>
      <th>
        <table width="100%" >
        <tr>
          <th align="left" width="150px" rowspan="3" ><img src="imagens/logo-intro-.png" width="70" height="55"/></th>
          <th align="center" height="0" class="tituloIate">IATE CLUBE DE BRASILIA</th>
          <th width="150px" class="dados" align="right"><%= fmtHour.format(new Date()) %></th>
        </tr>
        <tr>
          <th align="center" class="tituloRelatorio">AGENDA GERAL DE EVENTOS</th>
          <th class="dados" align="right">&nbsp;</th>
        </tr>
        <tr>
          <th align="center" class="tituloRelatorio">de ${dataInicio} até ${dataFim}</th>
          <th class="dados" align="right"><%= fmtDate.format(new Date()) %></th>
        </tr>
        </table>
      </th>
    </tr>  
    <tr><th><div class="divisoria"></div><div class="divisoria"></div></th></tr>
</body>

</table>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <sql:query var="rs" dataSource="jdbc/iate">${sql}</sql:query>
    
    <c:set var="cols" value="${rs.columnNames}"/>
    <c:forEach var="rows" items="${rs.rowsByIndex}">
        <c:forEach begin="1" end="${fn:length(cols) - 1}" var="k">
            <div class="dados"><b>${cols[k]}:&nbsp;</b>${rows[k]}<BR></div>
        </c:forEach>
            
            <c:if test="${not empty itens}">
                <sql:query var="rs2" dataSource="jdbc/iate">
                SELECT 
                     T1.DE_ITEM_ALUGUEL, 
                     T2.NU_QUANTIDADE 
                FROM 
                     TB_ITEM_ALUGUEL T1, 
                     TB_ITEM_ALUGUEL_DEP T2 
                WHERE 
                     T1.CD_ITEM_ALUGUEL = T2.CD_ITEM_ALUGUEL  AND 
                     T2.SEQ_RESERVA = ${rows[0]}
                     AND T1.CD_ITEM_ALUGUEL IN ${itens}
                </sql:query>

                <c:if test="${rs2.rowCount > 0}">
                    <table id="tabela" style="margin-left:20px;">
                        <thead>
                        <tr class="odd">
                            <th class="dados" align="left" ><b>Itens</b></th>
                            <th class="dados" ><b>Quantidade</b></th>
                        </tr>	
                        </thead>
                        <tbody>
                </c:if>
                
                <c:forEach var="linha" items="${rs2.rows}">
                    <tr height="1">
                        <td class="dados" align="left"><i>${linha.DE_ITEM_ALUGUEL}</i></td>
                        <td class="dados" align="right"><i>${linha.NU_QUANTIDADE}</i></td>
                    </tr>
                </c:forEach>
                    
                <c:if test="${rs2.rowCount > 0}">
                    </table>
                </c:if>
            </c:if>
        <HR>
    </c:forEach>

</body>
</html>
