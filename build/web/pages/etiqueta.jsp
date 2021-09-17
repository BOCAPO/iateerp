<!DOCTYPE html>

<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>


<style type="text/css">
.texto{ 
    font-family:Courier New;
    font-size:9px;  
    color:black; 
    padding-left:10px;
    margin:0px;
}
.comImagem{
    background-image: url(img/etiqueta-fundo_92x91.jpg);
    background-repeat: no-repeat; 
    background-position: center top; 
}

.quebra { 
    page-break-after: always; 
} 
</style>



<fmt:setLocale value="pt_BR"/>
<html>
<!-- ${pageContext.request.servletPath} -->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
    <link rel="shortcut icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
    <title>Iate Clube de Brasília</title>
</head>

<body>

    <sql:query var="rs" dataSource="jdbc/iate">
        ${sql}
    </sql:query>
    
    <table><tr>
                
        <c:set var="i" value="0"/>
        <c:set var="lin" value="0"/>
        <c:forEach var="row" items="${rs.rows}">
            <!--os IFs precisam ser nessa ortem-->
            
            <c:if test="${i eq 3}">
                <c:set var="i" value="0"/> 
                <c:set var="lin" value="${lin + 1}"/> 
                </tr>
            </c:if>
            <c:if test="${lin eq 9}">
                <c:set var="lin" value="0"/>
                </table><div class="quebra"> </div>
                <table><tr>
            </c:if>
            <c:if test="${i eq 0}">
                <tr>
            </c:if>
                    
            <c:choose>
            <c:when test="${imprimirLogotipo}">
                <td  class="texto comImagem" style="width: 285px;height: 118px">
            </c:when>
            <c:otherwise>
                <td class="texto" style="width: 285px;height: 118px">
            </c:otherwise>
            </c:choose>
            
                <c:if test="${not empty row.SEXO}">
                    <c:choose>
                    <c:when test="${row.SEXO eq 'F'}">
                        ILMA. SRA.
                    </c:when>
                    <c:otherwise>
                        ILMO. SR.
                    </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${imprimirCategoriaMatricula}">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatNumber pattern="00" value="${row.CATEGORIA}"/>/<fmt:formatNumber pattern="0000" value="${row.MATRICULA}"/>
                </c:if>
                <BR>
                ${fn:substring(row.NOME, 0, 46)}<BR>
                ${fn:substring(row.ENDERECO, 0, 46)}<BR>
                ${fn:substring(row.BAIRRO, 0, 20)}&nbsp;${fn:substring(row.CIDADE, 0, 20)}&nbsp;${fn:substring(row.UF, 0, 20)}<BR>
                ${fn:substring(row.CEP, 0, 5)}-${fn:substring(row.CEP, 5, 9)}
                <c:if test="${imprimirTexto}">
                    <BR><b>${texto}</b>
                </c:if>
            </td>  
            <c:set var="i" value="${i + 1}"/>            
        </c:forEach>
    </table>
</html>

