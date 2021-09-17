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
.tituloSecao {
	font-family: "Arial";
	font-size: 14px;
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
    font-family:Courier New; 
    font-size:15px;  
    color:black; 
}

.bordaExterna{
        position:relative; 
}

.quebra { 
    page-break-after: always; 
} 
textarea { 
    border: none;     
}

</style>

    <%Boolean primeiro=true;%>
    <sql:query var="rs" dataSource="jdbc/iate">
        ${sql}
    </sql:query>
    <c:forEach var="row" items="${rs.rows}">
        <c:set var="categoria" value="${fn:substring(row.Titulo, 0, 2)}" />
        <c:set var="matricula" value="${fn:substring(row.Titulo, 3, 8)}" />
        
        
        <sql:query var="rs2" dataSource="jdbc/iate">
            SELECT 
                isnull(T1.ENDERECO_R, '') as ENDERECO_R, 
                isnull(T1.BAIRRO_R, '') as BAIRRO_R, 
                isnull(T1.CIDADE_R, '') as CIDADE_R, 
                isnull(T1.UF_R, '') as UF_R, 
                isnull(T1.CEP_R, '') as CEP_R, 
                isnull(T1.ENDERECO_C, '') as ENDERECO_C, 
                isnull(T1.BAIRRO_C, '') as BAIRRO_C, 
                isnull(T1.CIDADE_C, '') as CIDADE_C, 
                isnull(T1.UF_C, '') as UF_C, 
                isnull(T1.CEP_C, '') as CEP_C, 
                isnull(T1.CD_END_CORRESPONDENCIA, '') as CD_END_CORRESPONDENCIA,
                T2.CD_SIT_PESSOA
            FROM 
                TB_COMPLEMENTO T1,
                TB_PESSOA T2
            WHERE 
                T1.CD_MATRICULA = ${matricula} AND 
                T1.CD_CATEGORIA = ${categoria} AND
                T1.CD_MATRICULA = T2.CD_MATRICULA AND
                T1.CD_CATEGORIA = T2.CD_CATEGORIA AND
                T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE
        </sql:query>
        <jsp:useBean id="now" class="java.util.Date" scope="request" />
        
        <c:forEach var="row2" items="${rs2.rows}">
            <%
            if (!primeiro){%>
                <div class="quebra"> </div>
            <%
            }else{
                primeiro = false;
            }
            %>
            
            <c:if test='${(row2.CD_SIT_PESSOA == "US" && cartaDono.texto != null) || (row2.CD_SIT_PESSOA != "US" && cartaUsuario.texto != null)}'>
                <input type="hidden" name="carro" id="carro" value="chegou2">

                <sql:query var="rs3" dataSource="jdbc/iate">
                    EXEC SP_REC_CARNE_NP ${categoria}, '<fmt:formatDate value="${now}" pattern="MM/dd/yyyy"/>', ${matricula}, '${row2.CD_SIT_PESSOA}'
                </sql:query>        

                <c:set var="carnes"></c:set>
                <c:forEach var="row3" items="${rs3.rows}">
                    <c:set var="mesNum"><fmt:formatDate value="${row3.DT_VENC_CARNE}" pattern="MM"/></c:set>
                    <c:set var="ano"><fmt:formatDate value="${row3.DT_VENC_CARNE}" pattern="yyyy"/></c:set>
                    <c:choose>
                        <c:when test="${mesNum==1}"><c:set var="mes" value="JANEIRO"/></c:when>
                        <c:when test="${mesNum==2}"><c:set var="mes" value="FEVEREIRO"/></c:when>
                        <c:when test="${mesNum==3}"><c:set var="mes" value="MARÇO"/></c:when>
                        <c:when test="${mesNum==4}"><c:set var="mes" value="ABRIL"/></c:when>
                        <c:when test="${mesNum==5}"><c:set var="mes" value="MAIO"/></c:when>
                        <c:when test="${mesNum==6}"><c:set var="mes" value="JUNHO"/></c:when>
                        <c:when test="${mesNum==7}"><c:set var="mes" value="JULHO"/></c:when>
                        <c:when test="${mesNum==8}"><c:set var="mes" value="AGOSTO"/></c:when>
                        <c:when test="${mesNum==9}"><c:set var="mes" value="SETEMBRO"/></c:when>
                        <c:when test="${mesNum==10}"><c:set var="mes" value="OUTUBRO"/></c:when>
                        <c:when test="${mesNum==11}"><c:set var="mes" value="NOVEMBRO"/></c:when>
                        <c:when test="${mesNum==12}"><c:set var="mes" value="DEZEMBRO"/></c:when>
                    </c:choose>

                    <c:set var="carnes">${carnes} ${mes}/${ano}</c:set>
                </c:forEach>



                <table><tr><td>
                    <div class="bordaExterna" style="top:0px;left:0px;width: 650px; height: 950px;">    
                        <div style="position:absolute;top:30px;left:400px;" class="texto" align="center">
                            BRASILIA,  <fmt:formatDate value="${now}" pattern="dd/MM/yyyy"/>
                        </div>    
                        <div style="position:absolute;top:60px;left:30px;" class="texto" align="left">
                            ILMO(A) SENHOR(A)<br>
                            ${row.nome}<br>
                            <c:if test='${categoria != "90"}'>
                                TITULO: ${categoria}/${matricula}
                            </c:if>
                            <c:if test='${categoria == "90"}'>
                                MATRÍCULA: ${categoria}/${matricula}
                            </c:if>
                        </div>    

                        <c:if test='${row2.CD_SIT_PESSOA == "US"}'>
                            <c:set var="textoCarta">${cartaDono.texto}</c:set>
                        </c:if>
                        <c:if test='${row2.CD_SIT_PESSOA != "US"}'>
                            <c:set var="textoCarta">${cartaUsuario.texto}</c:set>
                        </c:if>


                        <div style="position:absolute;top:140px;left:30px;" class="texto" align="left">
                            <c:set var="texto">${fn:replace(textoCarta, "<<CARNES>>", carnes)}</c:set>
                            <textarea class="campoSemTamanho texto" rows="39" cols="60" name="texto" id="texto">${fn:substring(texto, 1, 10000)}</textarea><br>
                        </div>    

                        <div style="position:absolute;top:870px;left:90px;" class="texto" align="left">
                            Destinatario<br>
                            ${row.nome}<br>
                            <c:if test='${row2.CD_END_CORRESPONDENCIA == "C"}'>
                                ${row2.ENDERECO_C}<br>
                                ${row2.BAIRRO_C}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${row2.CIDADE_C}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${row2.UF_C}<br>
                                ${row2.CEP_C}<br>
                            </c:if>
                            <c:if test='${row2.CD_END_CORRESPONDENCIA != "C"}'>
                                ${row2.ENDERECO_R}<br>
                                ${row2.BAIRRO_R}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${row2.CIDADE_R}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${row2.UF_R}<br>
                                ${row2.CEP_R}<br>
                            </c:if>

                        </div>    


                    </div>                
                </td></tr></table></div>
                
            </c:if>
        </c:forEach>
    </c:forEach>

</body>
</html>

