<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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


.legendaCodigo{ 
    font-family:Arial, Helvetica, sans-serif; 
    font-size:14px;  
    font-weight:bold; 
    color:black; 
    font-weight: bold;
    margin: 0px;
    font-style: italic;
}
.texto{ 
    font-family:Arial, Helvetica, sans-serif; 
    font-size:12px;  
    color:black; 
    margin: 0px;
}

.quebra { 
    page-break-after: always; 
} 
</style>



    <sql:query var="rs" dataSource="jdbc/iate">
        ${sql}
    </sql:query>
    <table width="100%" >
        <thead>
            <tr>
              <td>
                <table width="100%" >
                    <tr>
                      <th align="left" width="150px" rowspan="2" ><img src="imagens/logo-intro.png" width="70" height="55"/></th>
                      <th align="center" height="0" class="tituloRelatorio">Relatório Cadastral - Simplificado</th>
                      <th width="150px" class="dados" align="right"><fmt:formatDate value="<%= new java.util.Date() %>" pattern="HH:mm:ss"/></th>
                    </tr>
                    <tr>
                      <th align="center" class="subTituloRelatorio"></th>
                      <th class="dados" align="right"><fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy"/></th>
                    </tr>
                </table>
              </td>
            </tr>  
        </thead>    
        
    <c:forEach var="row" items="${rs.rows}">        

        <tr>
            <td>
                <table width="100%">
                    <tr>
                        <td style="width: 100px;">
                            <img src="f?tb=6&mat=${row.CD_MATRICULA}&seq=${row.ID_DEPENDENTE}&cat=${row.CD_CATEGORIA}" class="recuoPadrao MargemSuperiorPadrao" width="100" height="120">                                                    
                        </td>
                        <td valign="top">
                            <table width="100%">
                                <tr>
                                    <td>
                                        <table width="100%">
                                            <tr>
                                                <td>
                                                    <p class="legendaCodigo">Nome</p>
                                                    <p class="texto">${row.NOME_PESSOA}</p>
                                                </td>
                                                <td>
                                                    <p class="legendaCodigo">Matrícula</p>
                                                    <p class="texto"><fmt:formatNumber value="${row.CD_MATRICULA}" pattern="0000"/></p>
                                                </td>
                                                <td>
                                                    <p class="legendaCodigo">Categoria</p>
                                                    <p class="texto">${row.descr_categoria}</p>
                                                </td>
                                            </tr>
                                        </table>
                                    <td>
                                </tr>
                                <tr>
                                    <td>
                                        <table  width="100%">
                                            <tr>
                                                <td>
                                                    <p class="legendaCodigo">Tipo</p>
                                                    <p class="texto">
                                                        <c:choose>
                                                            <c:when test="${row.ID_DEPENDENTE eq 0}">
                                                                Titular
                                                            </c:when>
                                                            <c:otherwise>
                                                                Dependente
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>            
                                                </td>
                                                <td>
                                                    <p class="legendaCodigo">Endereço</p>
                                                    <p class="texto">
                                                        <c:choose>
                                                            <c:when test="${row.CD_END_CORRESPONDENCIA eq 'R'}">
                                                                ${row.ENDERECO_R}
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td colspan="3">${row.ENDERECO_C}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>            
                                                </td>
                                                <td>
                                                    <p class="legendaCodigo">Bairro</p>
                                                    <p class="texto">
                                                        <c:choose>
                                                            <c:when test="${row.CD_END_CORRESPONDENCIA eq 'R'}">
                                                                ${row.BAIRRO_R}
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td colspan="3">${row.ENDERECO_C}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>            
                                                </td>
                                            </tr>   
                                        </table>   
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table  width="100%">
                                            <tr>
                                                <td>
                                                    <p class="legendaCodigo">Cidade</p>
                                                    <p class="texto">
                                                        <c:choose>
                                                            <c:when test="${row.CD_END_CORRESPONDENCIA eq 'R'}">
                                                                ${row.BAIRRO_R}
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td colspan="3">${row.CIDADE_R}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>            
                                                </td>
                                                <td>
                                                    <p class="legendaCodigo">UF</p>
                                                    <p class="texto">
                                                        <c:choose>
                                                            <c:when test="${row.CD_END_CORRESPONDENCIA eq 'R'}">
                                                                ${row.BAIRRO_R}
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td colspan="3">${row.UF_R}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>            
                                                </td>
                                                <td>
                                                    <p class="legendaCodigo">CEP</p>
                                                    <p class="texto">
                                                        <c:choose>
                                                            <c:when test="${row.CD_END_CORRESPONDENCIA eq 'R'}">
                                                                ${row.BAIRRO_R}
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td colspan="3">${row.CEP_R}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>            
                                                </td>
                                                <td>
                                                    <p class="legendaCodigo">Telefone</p>
                                                    <p class="texto">
                                                        <c:choose>
                                                            <c:when test="${row.CD_END_CORRESPONDENCIA eq 'R'}">
                                                                ${row.BAIRRO_R}
                                                            </c:when>
                                                            <c:otherwise>
                                                                <td colspan="3">${row.TELEFONE_C}</td>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>            
                                                </td>
                                            </tr>   
                                        </table>   
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <hr>
            </td>
        </tr>

    </c:forEach>

    </table>

</body>
</html>

