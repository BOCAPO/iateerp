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
        font-weight: bold;
}
.dado {
	font-family: "Arial";
	font-size: 14px;
}

.quebra { 
    page-break-before: always; 
} 

.bordasimples {border-collapse: collapse;}

.bordasimples tr td {border:1px solid;}

</style>

<c:set var="primeiro" value="true"/>                     
<c:set var="tamImp" value="0"/>


<sql:query var="rs" dataSource="jdbc/iate">
    set language Portuguese
    SET DATEFORMAT YMD

    SELECT 
            T2.SEQ_DEPENDENCIA,
            T1.CD_MATRICULA, 
            T1.CD_CATEGORIA,
            RIGHT('00' + CONVERT(VARCHAR, T1.CD_CATEGORIA), 2) + '/' + RIGHT('0000' + CONVERT(VARCHAR, T1.CD_MATRICULA), 4) TITULO,                  
            T1.NOME_PESSOA,
            SUBSTRING(T2.HH_INIC_UTIL, 1, 2) + ':' + SUBSTRING(T2.HH_INIC_UTIL, 3, 2) HH_INIC_UTIL,
            SUBSTRING(T2.HH_FIM_UTIL, 1, 2) + ':' + SUBSTRING(T2.HH_FIM_UTIL, 3, 2) HH_FIM_UTIL,
            T2.DT_INIC_UTILIZACAO,
            DATENAME(DW, T2.DT_INIC_UTILIZACAO) + ', ' + CONVERT(VARCHAR, DAY(T2.DT_INIC_UTILIZACAO)) + ' de ' + DATENAME(M, T2.DT_INIC_UTILIZACAO) + ' de ' + CONVERT(VARCHAR, YEAR(T2.DT_INIC_UTILIZACAO)) DATA_FORMATADA,
            T3.DE_ABREVIACAO
    FROM 
            TB_PESSOA T1,
            TB_RESERVA_DEPENDENCIA T2,
            TB_DEPENDENCIA T3
    WHERE 
            T2.SEQ_DEPENDENCIA = T3.SEQ_DEPENDENCIA AND
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
</sql:query>

<c:forEach var="row" items="${rs.rows}">
    <div class="quebra"> 
        <table width="100%" >
            <tr>
                <td>
                    <table width="100%" >
                        <tr>
                            <td>
                                <img src="imagens/logo-intro.png" width="50" height="43"/>
                            </td>
                            <td class="tituloRelatorio" align="center">
                                IATE CLUBE DE BRASILIA<br>
                                TERMO DE VISTORIA / ENTREGA DE CHURRASQUEIRA
                             </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                            </td>
                        </tr>
                    </table>
                    
                    <table class="bordasimples" width="100%" >  
                        <tr>
                            <td class="tituloSecao" bgcolor=silver>
                                Data:
                            </td>    
                            <td class="dado">
                                ${row.DATA_FORMATADA}
                            </td>    
                            <td class="tituloSecao" bgcolor=silver>
                                Churrasqueira:
                            </td>    
                            <td class="dado">
                                ${row.DE_ABREVIACAO}
                            </td>    
                        </tr>
                        <tr>
                            <td class="tituloSecao" bgcolor=silver>
                                Horário:
                            </td>    
                            <td class="dado">
                                ${row.HH_INIC_UTIL} às ${row.HH_FIM_UTIL}hs
                            </td>    
                            <td>
                                &nbsp;
                            </td>    
                            <td>
                                &nbsp;
                            </td>    
                        </tr>
                        <tr>
                            <td class="tituloSecao" bgcolor=silver>
                                Nome Sócio:
                            </td>    
                            <td class="dado">
                                ${row.NOME_PESSOA}
                            </td>    
                            <td class="tituloSecao" bgcolor=silver>
                                Título:
                            </td>    
                            <td class="dado">
                                ${row.TITULO}
                            </td>    
                        </tr>
                        <tr>
                            <td class="dado" colspan="4">
                                <b>
                                    IMPORTANTE!!!
                                    <br>
                                    O período de trabalho dos serventes e seguranças contratados é de 6 horas, a partir do horário de início da reserva da churrasqueira.
                                </b>
                            </td>    
                        </tr>
                    </table>
                    <br>
                    <table class="bordasimples" width="100%" >  
                        <tr>
                            <td class="dado" colspan="4" align="center">
                                <b>
                                    Serviço de Servente &nbsp; (&nbsp;&nbsp;&nbsp;) SIM&nbsp;&nbsp;&nbsp;(&nbsp;&nbsp;&nbsp;)&nbsp;NÃO 
                                </b>
                            </td>    
                        </tr>
                        
                        <tr>
                            <td class="tituloSecao" bgcolor=silver style="width: 200px">
                                Nome / Mat. do Servente:
                            </td>   
                            <td>
                                &nbsp;
                            </td>   
                        </tr>
                        <tr>
                            <td class="tituloSecao" bgcolor=silver>
                                Data/Horário de chegada:
                            </td>    
                            <td>
                                &nbsp;
                            </td>   
                        </tr>
                        <tr>
                            <td class="tituloSecao" bgcolor=silver>
                                Data/Horário de Saída:
                            </td>    
                            <td>
                                &nbsp;
                            </td>   
                        </tr>
                        <tr>
                            <td class="tituloSecao" bgcolor=silver>
                                Pesquisa de Satisfação:<br>
                                (preenchido pelo sócio)
                            </td>    
                            <td>
                                (&nbsp;&nbsp;&nbsp;) Ruim &nbsp;&nbsp;&nbsp;(&nbsp;&nbsp;&nbsp;) Bom &nbsp;&nbsp;&nbsp;(&nbsp;&nbsp;&nbsp;) Ótimo 
                            </td>   
                        </tr>
                        <tr>
                            <td class="dado" colspan="2">
                                Comentários:
                                <br><br><br><br>
                            </td>    
                        </tr>
                        <tr>
                            <td class="dado" colspan="4">
                                <b>
                                    Regulamento das churrasqueiras:
                                    <br>
                                    Art. 11. Durante a utilização da churrasqueira ficam proibidas:
                                    <br>
                                    I. ocupação ou uso da estrutura, área e equipamentos com objetivo diverso de sua finalidade;
                                    <br>
                                    II. modificação da arquitetura ou retirada dos equipamentos;
                                    <br>
                                    III. uso de karaokê e a utilização de aparelhagem de som ou qualquer equipamento para música ao vivo acima do limite de 65 decibéis no período diurno e 55 decibéis no período noturno; e
                                    <br>
                                    IV. instalação de rede para descanso, equipamentos de diversão, tais como pula-pula, cama-elástica, tobogã, balanço, palco ou outros assemelhados.
                                </b>
                            </td>    
                        </tr>
                        <tr>
                            <td class="dado" colspan="4">
                                <b>
                                    Estatuto do Clube:
                                    <br>
                                    Seção II - Art. 39, incisso VII - identificar-se em qualquer dependência do CLUBE, quando solicitado por integrante dos Conselhos Deliberativo e Diretor, ou empregado devidamente credenciado.
                                </b>
                            </td>    
                        </tr>
                    </table>
                    
                    <table class="bordasimples" width="100%" >  
                        <tr>
                            <td class="tituloSecao" colspan="8" align="center" >
                                <b>
                                    VISTORIAS
                                </b>
                            </td>    
                        </tr>    
                        <tr>
                            <td class="tituloSecao" rowspan="2" bgcolor=silver align="center">
                                <b>
                                    Hora
                                </b>
                            </td>    
                            <td class="tituloSecao" rowspan="2"  bgcolor=silver align="center">
                                <b>
                                    Apoio Responsável
                                    <br>
                                    Nome
                                </b>
                            </td>    
                            <td class="tituloSecao" colspan="2" bgcolor=silver align="center">
                                <b>
                                    Compareceu?
                                </b>
                            </td>    
                            
                            <td class="tituloSecao" rowspan="2" bgcolor=silver align="center">
                                <b>
                                    Hora
                                </b>
                            </td>    
                            <td class="tituloSecao" rowspan="2"  bgcolor=silver align="center">
                                <b>
                                    Apoio Responsável
                                    <br>
                                    Nome
                                </b>
                            </td>    
                            <td class="tituloSecao" colspan="2" bgcolor=silver align="center">
                                <b>
                                    Compareceu?
                                </b>
                            </td>    
                            
                        </tr>
                        <tr>
                            <td class="tituloSecao" bgcolor=silver align="center">
                                <b>
                                    Sim
                                </b>
                            </td>    
                            <td class="tituloSecao" bgcolor=silver align="center">
                                <b>
                                    Não
                                </b>
                            </td>    
                            <td class="tituloSecao" bgcolor=silver align="center">
                                <b>
                                    Sim
                                </b>
                            </td>    
                            <td class="tituloSecao" bgcolor=silver align="center">
                                <b>
                                    Não
                                </b>
                            </td>    
                        </tr>
                        
                        <tr>
                            <td class="dado" align="center">
                                09h às 10h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td class="dado" align="center">
                                17h às 18h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                        </tr>
                        <tr>
                            <td class="dado" align="center">
                                10h às 11h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td class="dado" align="center">
                                18h às 19h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                        </tr>
                        <tr>
                            <td class="dado" align="center">
                                11h às 12h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td class="dado" align="center">
                                19h às 20h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                        </tr>
                        <tr>
                            <td class="dado" align="center">
                                12h às 13h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td class="dado" align="center">
                                20h às 21h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                        </tr>
                        <tr>
                            <td class="dado" align="center">
                                13h às 14h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td class="dado" align="center">
                                21h às 22h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                        </tr>
                        <tr>
                            <td class="dado" align="center">
                                14h às 15h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td class="dado" align="center">
                                22h às 23h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                        </tr>
                        <tr>
                            <td class="dado" align="center">
                                15h às 16h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td class="dado" align="center">
                                23h LIMITE
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                        </tr>
                        <tr>
                            <td class="dado" align="center">
                                16h às 17h
                            </td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td>&nbsp;</td>    
                            <td class="dado" colspan="4" align="center">
                                
                            </td>    
                        </tr>
                    </table>
                    <br>  
                    
                    <table width="100%" align="center">
                        <tr>
                            <td>
                                <br><br><br>
                            </td>
                        </tr>    
                        <tr>
                            <td class="dado">
                                <b>
                                    ASSINATURA DO(A) SÓCIO(A) 
                                </b>
                            </td>
                            <td class="dado" align="center">
                                <b>
                                    ASSINATURA DO APOIO VOLANTE
                                    <br>
                                    08h às 20h
                                </b>
                            </td>
                            <td class="dado" align="center">
                                <b>
                                    ASSINATURA DO(A) APOIO
                                    <br>
                                    08h às 20h
                                </b>
                            </td>
                        </tr>    
                        
                        <tr>
                            <td>
                                <br><br><br>
                            </td>
                        </tr>    
                        <tr>
                            <td class="dado">
                                &nbsp;
                            </td>
                            <td class="dado" align="center">
                                <b>
                                    ASSINATURA DO APOIO VOLANTE
                                    <br>
                                    20h às 08h
                                </b>
                            </td>
                            <td class="dado" align="center">
                                <b>
                                    ASSINATURA DO(A) APOIO
                                    <br>
                                    20h às 08h
                                </b>
                            </td>
                        </tr>    
                    </table>
                    
                    
                    
                </td>
            </tr>
        </table>
    </div>

    <div class="quebra"> 
        <table width="100%" >
            <tr>
                <td>
                    <table class="bordasimples" width="100%" >  
                        <tr>
                            <td class="tituloSecao" colspan="4" align="center" bgcolor=silver>
                                <b>
                                    CHURRASQUEIRA ${row.SEQ_DEPENDENCIA}
                                </b>
                            </td>    
                        </tr>    
                        <tr>
                            <td class="tituloSecao" align="center" bgcolor=silver>
                                <b>
                                    Item
                                </b>
                            </td>    
                            <td class="tituloSecao" align="center" bgcolor=silver>
                                <b>
                                    Descrição
                                </b>
                            </td>    
                            <td class="tituloSecao" align="center" bgcolor=silver>
                                <b>
                                    Quant.
                                </b>
                            </td>    
                            <td class="tituloSecao" align="center" bgcolor=silver width="20%">
                                <b>
                                    Observação
                                </b>
                            </td>    
                        
                        <sql:query var="rs2" dataSource="jdbc/iate">
                            SET DATEFORMAT YMD

                            SELECT 
                                CD_ITEM, 
                                DE_ITEM,
                                QT_ITEM,
                                DE_OBSERVACAO
                            FROM 
                                    TB_ITEM_CHURRASQUEIRA
                            WHERE 
                                    SEQ_DEPENDENCIA = ${row.SEQ_DEPENDENCIA}
                            ORDER BY 
                                    CD_ITEM 
                        </sql:query>
                        <c:forEach var="row2" items="${rs2.rows}">
                            <tr>
                                <td class="dado" align="center" width="10%">
                                        ${row2.CD_ITEM}
                                </td>    
                                <td class="dado"  align="left" width="40%">
                                        ${row2.DE_ITEM}
                                </td>    
                                <td class="dado" align="center" width="10%">
                                        ${row2.QT_ITEM}
                                </td>    
                                <td class="dado" align="left" width="40%">
                                        ${row2.DE_OBSERVACAO}
                                </td>    
                            </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <br>
                </td>
            </tr>
            <tr>
                <td>
                    <table class="bordasimples" width="100%" >  
                        <tr>
                            <td class="tituloSecao" colspan="4" align="center" bgcolor=silver>
                                <b>
                                    Pessoas autorizadas
                                </b>
                            </td>    
                        </tr>    
                        
                        <sql:query var="rs3" dataSource="jdbc/iate">
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
                                    
                        <c:forEach var="row3" items="${rs3.rows}">
                            <tr>
                                <td class="tituloSecao" align="left">
                                    <b>
                                        ${row3.NOME_PESSOA}
                                    </b>
                                </td>    
                            </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <br>
                </td>
            </tr>
            
            <tr>
                <td>
                    <table class="" width="100%" >  
                        <tr>
                            <td class="dado" align="left">
                                <b>
                                    TÁBUA ENTREGUE: (&nbsp;&nbsp;&nbsp;&nbsp;) SIM
                                </b>
                            </td>    
                            <td class="dado" align="center">
                                <b>
                                    _________________________<BR>
                                    RECEBIDO
                                    
                                </b>
                            </td>    
                            <td class="dado" align="center">
                                <b>
                                    _________________________<BR>
                                    DEVOLVIDO
                                    
                                </b>
                            </td>    
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
</c:forEach>

</body>
</html>

