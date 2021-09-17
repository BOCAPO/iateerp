<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<fmt:setLocale value="pt_BR"/>


<style type="text/css">
.numeroConvite{
	font-family: "Times New Roman";
	font-size: 9px;
	font-weight: bold;
        font-style: italic;
}
.titulo{
	font-family: "Times New Roman";
	font-size: 11px;
	font-weight: bold;
        color:rgb(0,0,150);
}
.validade{
	font-family: "Arial";
	font-size: 11px;
	font-weight: bold;
        color:rgb(255,0,0);
}
.codBarras{
	font-family: "C39P36DlTt";
	font-size: 36px;
}
.bordaExterna{
        position:absolute; 
        border:1px solid black;
}
.tituloInstrucoes{
	font-family: "Arial";
	font-size: 10px;
}
.permissao{
	font-family: "Arial";
	font-size: 14px;
	font-weight: bold;
}

</style>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>ConviteEsportivo</title>
    </head>
    <body>

    <sql:query var="rs" dataSource="jdbc/iate">
        set language Portuguese
        SET DATEFORMAT YMD

        SELECT 
            *
        FROM 
            TB_CONVITE T1,
            TB_COMPLEMENTO_CONV_ESPORTIVO T2
        WHERE 
            T1.NR_CONVITE = T2.NR_CONVITE AND
            T1.NR_CONVITE = ${idConvite}
    </sql:query>
        
    <c:forEach var="row" items="${rs.rows}">
        <!--FRENTE-->    
        <div class="bordaExterna" style="top:0px;left:0px;width:215px;height:270px;"></div>

        <div style="position:absolute;top:10px;left:35px;width:145px;" align="center">
            <img src="imagens/logo-intro.png" width="80" height="60"/>
        </div>    
        <div style="position:absolute;top:85px;left:0px;width:215px;height:20px;" align="center">
            <span class="permissao">CONVITE ESPORTIVO</span> 
        </div>    
        <div class="bordaExterna" style="top:115px;left:5px;width:205px;height:150px;"></div>
        <div style="border-bottom:1px solid black;position:absolute;top:145px;left:5px;width:205px;"/></div>        
        <div style="border-bottom:1px solid black;position:absolute;top:175px;left:5px;width:205px;"/></div>        
        <div style="border-bottom:1px solid black;position:absolute;top:205px;left:5px;width:205px;"/></div>        
        <div style="border-bottom:1px solid black;position:absolute;top:235px;left:5px;width:205px;"/></div>        

        <div class="tituloInstrucoes" style="position:absolute;top:118px;left:8px;height:25px;width:200px;overflow:hidden;">Nome:<br><b>${row.NOME_CONVIDADO}</b></div>    
        <div class="tituloInstrucoes" style="position:absolute;top:148px;left:8px;">CPF:<br><b>${row.CPF_CONVIDADO}</b></div>    
        <div class="tituloInstrucoes" style="position:absolute;top:178px;left:8px;">Modalidade:<br><b>${row.DE_MODALIDADE}</b></div>    
        <div class="tituloInstrucoes" style="position:absolute;top:208px;left:8px;">Socio Responsável:<br><b>${row.NOME_SACADOR}</b></div>        
        <div class="tituloInstrucoes" style="position:absolute;top:238px;left:8px;">Diretor/Vice-Diretor:</div>

        <!--VERSO-->    
        <div class="bordaExterna" style="top:0px;left:215px;width:215px;height:270px;"></div>

        <fmt:formatNumber pattern="000000000" var="numPermissao" value="${row.NR_CONVITE}"/>                

        <!--DAQUI PRA BAIXO EH TUDO SO PRA FAZER O TRATAMENTO DOS DIAS DA SEMANA E HORARIOS-->    
        <!--DAH PRA ARRUMAR EU SEI, MAS TEM QUE TER TEMPO...-->    
        <c:set var="lin" value="40"/>
        <c:set var="horarios" value=""/>
        <c:set var="jaTem" value="N"/>

        <c:if test="${row.ACESSA_SEGUNDA == true}">
            <c:set var="jaTem" value="S"/>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Segunda</b></div>
            <c:set var="horarios" value="${fn:substring(row.HH_ENTRA_SEGUNDA,0,2)}:${fn:substring(row.HH_ENTRA_SEGUNDA,2,4)} as ${fn:substring(row.HH_SAI_SEGUNDA,0,2)}:${fn:substring(row.HH_SAI_SEGUNDA,2,4)}"/>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:if test="${row.ACESSA_TERCA == true}">
            <c:if test="${jaTem=='S'}">
                <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>---------------</b></div>
                <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>-------------------------</b></div>
                <c:set var="lin" value="${lin+10}"/>
            </c:if>
            <c:set var="jaTem" value="S"/>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Terça</b></div>
            <c:set var="horarios" value="${fn:substring(row.HH_ENTRA_TERCA,0,2)}:${fn:substring(row.HH_ENTRA_TERCA,2,4)} as ${fn:substring(row.HH_SAI_TERCA,0,2)}:${fn:substring(row.HH_SAI_TERCA,2,4)}"/>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:if test="${row.ACESSA_QUARTA == true}">
            <c:if test="${jaTem=='S'}">
                <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>---------------</b></div>
                <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>-------------------------</b></div>
                <c:set var="lin" value="${lin+10}"/>
            </c:if>
            <c:set var="jaTem" value="S"/>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Quarta</b></div>
            <c:set var="horarios" value="${fn:substring(row.HH_ENTRA_QUARTA,0,2)}:${fn:substring(row.HH_ENTRA_QUARTA,2,4)} as ${fn:substring(row.HH_SAI_QUARTA,0,2)}:${fn:substring(row.HH_SAI_QUARTA,2,4)}"/>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:if test="${row.ACESSA_QUINTA == true}">
            <c:if test="${jaTem=='S'}">
                <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>---------------</b></div>
                <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>-------------------------</b></div>
                <c:set var="lin" value="${lin+10}"/>
            </c:if>
            <c:set var="jaTem" value="S"/>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Quinta</b></div>
            <c:set var="horarios" value="${fn:substring(row.HH_ENTRA_QUINTA,0,2)}:${fn:substring(row.HH_ENTRA_QUINTA,2,4)} as ${fn:substring(row.HH_SAI_QUINTA,0,2)}:${fn:substring(row.HH_SAI_QUINTA,2,4)}"/>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:if test="${row.ACESSA_SEXTA == true}">
            <c:if test="${jaTem=='S'}">
                <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>---------------</b></div>
                <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>-------------------------</b></div>
                <c:set var="lin" value="${lin+10}"/>
            </c:if>
            <c:set var="jaTem" value="S"/>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Sexta</b></div>
            <c:set var="horarios" value="${fn:substring(row.HH_ENTRA_SEXTA,0,2)}:${fn:substring(row.HH_ENTRA_SEXTA,2,4)} as ${fn:substring(row.HH_SAI_SEXTA,0,2)}:${fn:substring(row.HH_SAI_SEXTA,2,4)}"/>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>


        <div style="position:absolute;top:210px;left:215px;width:215px;height:20px;" align="center">
            <span class="numeroConvite">${numPermissao}</span> 
        </div>    
        <div style="position:absolute;top:230px;left:215px;width:215px;height:50px;" align="center">
            <span class="codBarras">!${numPermissao}!</span> 
        </div> 
    </c:forEach>


</html>
