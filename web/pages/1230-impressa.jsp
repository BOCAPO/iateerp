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
        <title>Permissão Provisória</title>
    </head>
    <body>
    
    <!--FRENTE-->    
    <div class="bordaExterna" style="top:0px;left:0px;width:215px;height:270px;"></div>
    <img src="f?tb=2&cd=${permissao.id}" class="recuoPadrao MargemSuperiorPadrao" width="70" height="90">
    <div style="position:absolute;top:10px;left:70px;width:145px;" align="center">
        <img src="imagens/logo-intro.png" width="60" height="45"/>
    </div>    
    <div style="position:absolute;top:52px;left:70px;width:145px;" align="center">
        <span class="titulo">IATE CLUBE DE</span> 
    </div>    
    <div style="position:absolute;top:64px;left:70px;width:145px;" align="center">
        <span class="titulo">BRASÍLIA</span> 
    </div>    
    <div style="position:absolute;top:120px;left:5px;width:215px;height:20px;" align="center">
        <span class="permissao">PERMISSÃO PROVISÓRIA</span> 
    </div>    
    <div class="bordaExterna" style="top:175px;left:5px;width:205px;height:90px;"></div>
    <div style="border-bottom:1px solid black;position:absolute;top:205px;left:5px;width:205px;"/></div>        
    <div style="border-bottom:1px solid black;position:absolute;top:235px;left:5px;width:205px;"/></div>        

    <div class="tituloInstrucoes" style="position:absolute;top:178px;left:8px;height:25px;width:200px;overflow:hidden;">Nome:<br><b>${permissao.nome}</b></div>    
    <div class="tituloInstrucoes" style="position:absolute;top:208px;left:8px;">Área:<br><b>${permissao.atividade}</b></div>    
    <div class="tituloInstrucoes" style="position:absolute;top:238px;left:8px;">Período:
    <br><b>
        ${permissao.inicio} até ${permissao.fim}
    </b>
    </div>
    
    <!--VERSO-->    
    <div class="bordaExterna" style="top:0px;left:215px;width:215px;height:270px;"></div>

    <sql:query var="rs" dataSource="jdbc/iate">
        EXEC SP_ATUALIZA_NR_AUTORIZACAO ${permissao.id}
    </sql:query>
    <fmt:formatNumber pattern="000000000" var="numPermissao" value="${rs.rows[0].NR_AUTORIZACAO}"/>                

    <!--DAQUI PRA BAIXO EH TUDO SO PRA FAZER O TRATAMENTO DOS DIAS DA SEMANA E HORARIOS-->    
    <!--DAH PRA ARRUMAR EU SEI, MAS TEM QUE TER TEMPO...-->    
    <c:set var="lin" value="40"/>
    <c:set var="horarios" value=""/>
    <c:set var="jaTem" value="N"/>
    
    <c:if test="${not empty permissao.horarios[0]}">
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Segunda</b></div>
        <c:set var="horarios" value="${fn:substring(permissao.horarios[0].entrada,0,2)}:${fn:substring(permissao.horarios[0].entrada,2,4)} as ${fn:substring(permissao.horarios[0].saida,0,2)}:${fn:substring(permissao.horarios[0].saida,2,4)}"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
        <c:set var="horarios" value=""/>
        <c:set var="lin" value="${lin+10}"/>
    </c:if>
    <c:if test="${not empty permissao.horarios[1]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>-------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Terça</b></div>
        <c:set var="horarios" value="${fn:substring(permissao.horarios[1].entrada,0,2)}:${fn:substring(permissao.horarios[1].entrada,2,4)} as ${fn:substring(permissao.horarios[1].saida,0,2)}:${fn:substring(permissao.horarios[1].saida,2,4)}"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
        <c:set var="horarios" value=""/>
        <c:set var="lin" value="${lin+10}"/>
    </c:if>
    <c:if test="${not empty permissao.horarios[2]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>-------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Quarta</b></div>
        <c:set var="horarios" value="${fn:substring(permissao.horarios[2].entrada,0,2)}:${fn:substring(permissao.horarios[2].entrada,2,4)} as ${fn:substring(permissao.horarios[2].saida,0,2)}:${fn:substring(permissao.horarios[2].saida,2,4)}"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
        <c:set var="horarios" value=""/>
        <c:set var="lin" value="${lin+10}"/>
    </c:if>
    <c:if test="${not empty permissao.horarios[3]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>-------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Quinta</b></div>
        <c:set var="horarios" value="${fn:substring(permissao.horarios[3].entrada,0,2)}:${fn:substring(permissao.horarios[3].entrada,2,4)} as ${fn:substring(permissao.horarios[3].saida,0,2)}:${fn:substring(permissao.horarios[3].saida,2,4)}"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
        <c:set var="horarios" value=""/>
        <c:set var="lin" value="${lin+10}"/>
    </c:if>
    <c:if test="${not empty permissao.horarios[4]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>-------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Sexta</b></div>
        <c:set var="horarios" value="${fn:substring(permissao.horarios[4].entrada,0,2)}:${fn:substring(permissao.horarios[4].entrada,2,4)} as ${fn:substring(permissao.horarios[4].saida,0,2)}:${fn:substring(permissao.horarios[4].saida,2,4)}"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
        <c:set var="horarios" value=""/>
        <c:set var="lin" value="${lin+10}"/>
    </c:if>
    <c:if test="${not empty permissao.horarios[5]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>-------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Sábado</b></div>
        <c:set var="horarios" value="${fn:substring(permissao.horarios[5].entrada,0,2)}:${fn:substring(permissao.horarios[5].entrada,2,4)} as ${fn:substring(permissao.horarios[5].saida,0,2)}:${fn:substring(permissao.horarios[5].saida,2,4)}"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>${horarios}</b></div>    
        <c:set var="horarios" value=""/>
        <c:set var="lin" value="${lin+10}"/>
    </c:if>
    <c:if test="${not empty permissao.horarios[6]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:320px;"><b>-------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:240px;"><b>Domingo</b></div>
        <c:set var="horarios" value="${fn:substring(permissao.horarios[6].entrada,0,2)}:${fn:substring(permissao.horarios[6].entrada,2,4)} as ${fn:substring(permissao.horarios[6].saida,0,2)}:${fn:substring(permissao.horarios[6].saida,2,4)}"/>
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
    

</html>
