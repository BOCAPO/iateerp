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

.tituloSecao {
	font-family: "Arial";
	font-size: 14px;
}

.divisoria { border-bottom:1px #000 solid;  padding-top:1px; margin-bottom:1px; }        

.quebra { 
    page-break-before: always; 
} 

</style>

<sql:query var="rs" dataSource="jdbc/iate">
    <c:if test='${dataReferencia != ""}'>
        SET DATEFORMAT YMD
        
        SELECT 
                T2.SEQ_DEPENDENCIA,
                T1.CD_MATRICULA, 
                T1.CD_CATEGORIA,
                RIGHT('00' + CONVERT(VARCHAR, T1.CD_CATEGORIA), 2) + '/' + RIGHT('0000' + CONVERT(VARCHAR, T1.CD_MATRICULA), 4) TITULO,                  
                T1.NOME_PESSOA,
                (
                    SELECT 
                        COUNT(*) 
                    FROM 
                        TB_PESSOA T0
                    WHERE
                            T0.CD_MATRICULA = T1.CD_MATRICULA AND
                            T0.CD_CATEGORIA = T1.CD_CATEGORIA AND
                            (DT_VALID_RESERVA > GETDATE() OR 
                            SEQ_DEPENDENTE = 0)
                ) QT_PESSOAS
        FROM 
                TB_PESSOA T1,
                TB_RESERVA_DEPENDENCIA T2
        WHERE 
                T1.CD_MATRICULA = T2.CD_MATRICULA AND 
                T1.CD_CATEGORIA = T2.CD_CATEGORIA AND
                T1.SEQ_DEPENDENTE = T2.SEQ_DEPENDENTE AND
                CONVERT(DATE, T2.DT_INIC_UTILIZACAO) = '${fn:substring(dataReferencia, 6, 10)}-${fn:substring(dataReferencia, 3, 5)}-${fn:substring(dataReferencia, 0, 2)}' AND
                T2.CD_STATUS_RESERVA = 'CF'
                <c:if test='${titulo != ""}'>   
                    AND T1.CD_MATRICULA = ${titulo}
                </c:if>                
                <c:if test='${categoria != ""}'>   
                    AND T1.CD_CATEGORIA = ${categoria}
                </c:if>                
        ORDER BY 
                1
             
    </c:if>
    <c:if test='${dataReferencia == ""}'>
        SET DATEFORMAT YMD
        
        SELECT 
                T1.CD_MATRICULA, 
                T1.CD_CATEGORIA,
                RIGHT('00' + CONVERT(VARCHAR, T1.CD_CATEGORIA), 2) + '/' + RIGHT('0000' + CONVERT(VARCHAR, T1.CD_MATRICULA), 4) TITULO,                  
                COUNT(*) QT_PESSOAS
        FROM 
                TB_PESSOA T1
        WHERE 
                CD_MATRICULA = ${titulo} AND
                CD_CATEGORIA = ${categoria} AND
                SEQ_DEPENDENTE = 0
        GROUP BY 
                T1.CD_MATRICULA, 
                T1.CD_CATEGORIA,
                RIGHT('00' + CONVERT(VARCHAR, T1.CD_CATEGORIA), 2) + '/' + RIGHT('0000' + CONVERT(VARCHAR, T1.CD_MATRICULA), 4)                  
    </c:if>
</sql:query>

<c:set var="primeiro" value="true"/>                     
<c:set var="tamImp" value="0"/>

<c:forEach var="row" items="${rs.rows}">
        <c:set var="tamImp" value="${tamImp + 4 + row.QT_PESSOAS}"/> 
        <c:if test='${tamImp gt 40}'>
                        </td>
                    </tr>
                </table>
            </div>
            
            <c:set var="primeiro" value="true"/>                     
            <c:set var="tamImp" value="${4 + row.QT_PESSOAS}"/> 
        </c:if>
        
        <c:if test='${primeiro}'>
            <div class="quebra"> 
                <table>
                    <tr>
                        <td>
                            
            <c:set var="primeiro" value="false"/>
        </c:if>
                    <table>
                        <tr>
                            <td>
                                <img src="imagens/logo-intro.png" width="40" height="35"/>
                            </td>
                            <td class="tituloRelatorio">
                                AUTORIZADOS - ASSINATURA DO TERMO DE CHURRASQUEIRAS 
                             </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="tituloSecao">
                                <c:if test='${dataReferencia != ""}'>
                                    <b>CHURRASQUEIRA No.: </b>${row.SEQ_DEPENDENCIA} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:if>
                                
                                <b>TITULO: </b>${row.TITULO}
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="tituloSecao">
                                <b>NOMES:</b>
                            </td>
                        </tr>
                        <sql:query var="rs2" dataSource="jdbc/iate">
                            SELECT 
                                    T1.NOME_PESSOA
                            FROM 
                                    TB_PESSOA T1
                            WHERE 
                                    (DT_VALID_RESERVA > GETDATE() OR 
                                    SEQ_DEPENDENTE = 0) AND
                                    CD_MATRICULA = ${row.CD_MATRICULA} AND
                                    CD_CATEGORIA = ${row.CD_CATEGORIA}
                            ORDER BY 
                                    1
                        </sql:query>
                        <c:forEach var="row2" items="${rs2.rows}">
                            
                            <tr>
                                <td colspan="2" class="tituloSecao" style="padding-left: 20px;">
                                    ${row2.NOME_PESSOA}
                                </td>
                            </tr>
                        </c:forEach>
                            
                        <tr>
                            <td colspan="2">
                                <hr>
                            </td>
                        </tr>

                    </table>

</c:forEach>

                </td>
            </tr>
        </table>
    </div>


</body>
</html>

