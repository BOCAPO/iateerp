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
        
.tituloDados {
	font-family: "Arial";
	font-size: 12px;
	color: #000;
	font-weight: bold;
}
.divisoria { border-bottom:1px #000 solid;  padding-top:1px; margin-bottom:1px; }        

</style>

<sql:query var="rs" dataSource="jdbc/iate">
    ${sql}
</sql:query>
<c:forEach var="row" items="${rs.rows}">
    <sql:query var="rsc" dataSource="jdbc/iate">
        SELECT nome_pessoa, 
        isnull(ENDERECO_R, '') as ENDERECO_R, 
        isnull(BAIRRO_R, '') as BAIRRO_R, 
        isnull(CIDADE_R, '') as CIDADE_R, 
        isnull(UF_R, '') as UF_R, 
        isnull(CEP_R, '') as CEP_R, 
        isnull(ENDERECO_C, '') as ENDERECO_C, 
        isnull(BAIRRO_C, '') as BAIRRO_C, 
        isnull(CIDADE_C, '') as CIDADE_C, 
        isnull(UF_C, '') as UF_C, 
        isnull(CEP_C, '') as CEP_C, 
        isnull(CD_END_CORRESPONDENCIA, '') as CD_END_CORRESPONDENCIA 
        FROM 
        TB_COMPLEMENTO t1, tb_pessoa t2 
        WHERE t1.cd_matricula = t2.cd_matricula and t1.cd_categoria = t2.cd_categoria and t1.seq_dependente = t2.seq_dependente and 
        t1.CD_MATRICULA = ? AND 
        t1.CD_CATEGORIA = ?
        <sql:param value="${row.CD_MATRICULA}"/>
        <sql:param value="${row.CD_CATEGORIA}"/>
    </sql:query>

<table width="100%" >
    <tr>
      <td>
        <table width="100%" >
        <tr>
          <th align="left" width="150px" rowspan="2" ><img src="imagens/logo-intro.png" width="70" height="55"/></th>
          <th align="center" height="0" class="tituloRelatorio">DECLARAÇÃO DE QUITAÇÃO</th>
          <th width="150px" class="dados" align="right"><fmt:formatDate pattern="HH:mm:ss" value="<%=new java.util.Date()%>"/></th>
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
        <sql:query var="rst" dataSource="jdbc/iate">
            SELECT texto_carta_cobranca FROM TB_carta_cobranca_nova WHERE TP_CARTA = ?

            <c:choose>
            <c:when test="${row.SIT eq 'PG'}">
                <sql:param value="Q"/>
            </c:when>
            <c:otherwise>
                <sql:param value="P"/>
            </c:otherwise>
            </c:choose>
        </sql:query>

            <fmt:formatNumber pattern="00" value="${row.CD_CATEGORIA}" var="categoria"/>
            <fmt:formatNumber pattern="0000" value="${row.CD_MATRICULA}" var="matricula"/>
            <c:set var="titulo" value="${categoria}/${matricula}"/>
            <p>${fn:replace(rst.rows[0].texto_carta_cobranca, "<<TIT>>", titulo)}</p>
            
    </td>
    </tr>                    
    <tr>
        <td align="center">
            <p>Brasília, <fmt:formatDate pattern="dd 'de' MMMMM 'de' yyyy" value="<%=new java.util.Date()%>"/></p>
        </td>
    </tr>
    <tr>
        <td align="center">
            <p>IATE CLUBE DE BRASÍLIA</p>
        </td>
    </tr>    
    <tr>
        <td align="center">
            <p>Destinatário</p>
            <p>${rsc.rows[0].NOME_PESSOA}</p>
            <c:choose>
                <c:when test="${rsc.rows[0].CD_END_CORRESPONDENCIA eq 'R'}">
                    <p>${rsc.rows[0].ENDERECO_R}</p>
                    <p>${rsc.rows[0].BAIRRO_R}   ${rsc.rows[0].CIDADE_R}   ${rsc.rows[0].UF_R}</p>
                    <p>${rsc.rows[0].CEP_R}</p>
                </c:when>
                <c:otherwise>
                    <p>${rsc.rows[0].ENDERECO_C}</p>
                    <p>${rsc.rows[0].BAIRRO_C}   ${rsc.rows[0].CIDADE_C}   ${rsc.rows[0].UF_C}</p>
                    <p>${rsc.rows[0].CEP_C}</p>                    
                </c:otherwise>
            </c:choose>
        </td>
    </tr>    
</table>
</c:forEach>

</body>
</html>