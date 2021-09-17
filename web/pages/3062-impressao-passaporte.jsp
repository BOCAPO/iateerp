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

</style>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>teste</title>
    </head>
    <body>
    
    <!--FRENTE-->    
    <div class="bordaExterna" style="top:0px;left:0px;width:215px;height:270px;"></div>
    <img src="f?tb=6&mat=${socio.matricula}&seq=${socio.seqDependente}&cat=${socio.idCategoria}" class="recuoPadrao MargemSuperiorPadrao" width="70" height="90">
    <div style="position:absolute;top:10px;left:70px;width:145px;" align="center">
        <img src="imagens/logo-intro.png" width="60" height="45"/>
    </div>    
    <div style="position:absolute;top:52px;left:70px;width:145px;" align="center">
        <span class="titulo">IATE CLUBE DE</span> 
    </div>    
    <div style="position:absolute;top:64px;left:70px;width:145px;" align="center">
        <span class="titulo">BRASÍLIA</span> 
    </div>    
    <div class="bordaExterna" style="top:120px;left:5px;width:205px;height:145px;"></div>
    <div style="border-bottom:1px solid black;position:absolute;top:147px;left:5px;width:205px;"/></div>        
    <div style="border-bottom:1px solid black;position:absolute;top:174px;left:5px;width:205px;"/></div>        
    <div style="border-bottom:1px solid black;position:absolute;top:201px;left:5px;width:205px;"/></div>        
    <div style="border-bottom:1px solid black;position:absolute;top:228px;left:5px;width:205px;"/></div>     

    <div class="tituloInstrucoes" style="position:absolute;top:122px;left:8px;height:25px;width:200px;overflow:hidden;">Nome:<br><b>${socio.nome}</b></div>    
    <div class="tituloInstrucoes" style="position:absolute;top:149px;left:8px;">Área:<br><b>${modalidade}</b></div>    
    <div class="tituloInstrucoes" style="position:absolute;top:176px;left:8px;">Categoria:
    <br><b>
        <c:forEach var="curso" items="${cursos}">
            <c:if test="${turma.idCurso eq curso.id}">${curso.descricao}</c:if>
        </c:forEach>                
    </b>
    </div>
    
    <sql:query var="rs" dataSource="jdbc/iate">
        SELECT NOME_DIRETOR_SECRETARIO FROM TB_PARAMETRO_SISTEMA
    </sql:query>
    
    <div class="tituloInstrucoes" style="position:absolute;top:203px;left:8px;">Dias<br><b>${diasAula}</b></div>    
    <div class="tituloInstrucoes" style="position:absolute;top:253px;left:8px;"><b>Dir. Secretário: ${rs.rows[0].NOME_DIRETOR_SECRETARIO}</b></div>    
    

    <sql:query var="rs" dataSource="jdbc/iate">
        EXEC SP_ATUALIZA_NR_PASSAPORTE ${socio.matricula}, ${socio.seqDependente}, ${socio.idCategoria}, ${turma.id}
    </sql:query>
    <fmt:formatNumber pattern="000000000" var="numPassaporte" value="${rs.rows[0].NR_PASSAPORTE}"/>                
    <fmt:formatDate var="ano" value="${rs.rows[0].DT_VALIDADE}" pattern="yyyy"/>

    <!--VERSO-->    
    <div class="bordaExterna" style="top:0px;left:215px;width:215px;height:270px;"></div>
    <div style="position:absolute;top:10px;left:215px;width:215px;" align="center">
        <span class="validade">${ano}</span> 
    </div>    
    
    <!--DAQUI PRA BAIXO EH TUDO SO PRA FAZER O TRATAMENTO DOS DIAS DA SEMANA E HORARIOS-->    
    <!--DAH PRA ARRUMAR EU SEI, MAS TEM QUE TER TEMPO...-->    
    <c:set var="lin" value="40"/>
    <c:set var="horarios" value=""/>
    <c:set var="jaTem" value="N"/>
    
    <c:if test="${not empty turma.horarios[0]}">
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:220px;"><b>Segunda</b></div>
        <c:forEach var="horario" items="${turma.horarios[0]}">
            <c:choose>
                <c:when test="${horarios==''}">
                    <c:set var="horarios" value="${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                </c:when>    
                <c:otherwise>
                    <c:set var="horarios" value="${horarios} - ${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                    <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
                    <c:set var="horarios" value=""/>
                    <c:set var="lin" value="${lin+10}"/>
                </c:otherwise>
            </c:choose> 
        </c:forEach>
        <c:if test="${horarios!=''}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
    </c:if>
    <c:if test="${not empty turma.horarios[1]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:222px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:272px;"><b>---------------------------------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:220px;"><b>Terça</b></div>
        <c:forEach var="horario" items="${turma.horarios[1]}">
            <c:choose>
                <c:when test="${horarios==''}">
                    <c:set var="horarios" value="${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                </c:when>    
                <c:otherwise>
                    <c:set var="horarios" value="${horarios} - ${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                    <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
                    <c:set var="horarios" value=""/>
                    <c:set var="lin" value="${lin+10}"/>
                </c:otherwise>
            </c:choose> 
        </c:forEach>
        <c:if test="${horarios!=''}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
    </c:if>
    <c:if test="${not empty turma.horarios[2]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:222px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:272px;"><b>---------------------------------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:220px;"><b>Quarta</b></div>
        <c:forEach var="horario" items="${turma.horarios[2]}">
            <c:choose>
                <c:when test="${horarios==''}">
                    <c:set var="horarios" value="${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                </c:when>    
                <c:otherwise>
                    <c:set var="horarios" value="${horarios} - ${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                    <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
                    <c:set var="horarios" value=""/>
                    <c:set var="lin" value="${lin+10}"/>
                </c:otherwise>
            </c:choose> 
        </c:forEach>
        <c:if test="${horarios!=''}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
    </c:if>
    <c:if test="${not empty turma.horarios[3]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:222px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:272px;"><b>---------------------------------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:220px;"><b>Quinta</b></div>
        <c:forEach var="horario" items="${turma.horarios[3]}">
            <c:choose>
                <c:when test="${horarios==''}">
                    <c:set var="horarios" value="${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                </c:when>    
                <c:otherwise>
                    <c:set var="horarios" value="${horarios} - ${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                    <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
                    <c:set var="horarios" value=""/>
                    <c:set var="lin" value="${lin+10}"/>
                </c:otherwise>
            </c:choose> 
        </c:forEach>
        <c:if test="${horarios!=''}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
    </c:if>
    <c:if test="${not empty turma.horarios[4]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:222px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:272px;"><b>---------------------------------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:220px;"><b>Sexta</b></div>
        <c:forEach var="horario" items="${turma.horarios[4]}">
            <c:choose>
                <c:when test="${horarios==''}">
                    <c:set var="horarios" value="${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                </c:when>    
                <c:otherwise>
                    <c:set var="horarios" value="${horarios} - ${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                    <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
                    <c:set var="horarios" value=""/>
                    <c:set var="lin" value="${lin+10}"/>
                </c:otherwise>
            </c:choose> 
        </c:forEach>
        <c:if test="${horarios!=''}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
    </c:if>
    <c:if test="${not empty turma.horarios[5]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:222px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:272px;"><b>---------------------------------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:220px;"><b>Sábado</b></div>
        <c:forEach var="horario" items="${turma.horarios[5]}">
            <c:choose>
                <c:when test="${horarios==''}">
                    <c:set var="horarios" value="${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                </c:when>    
                <c:otherwise>
                    <c:set var="horarios" value="${horarios} - ${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                    <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
                    <c:set var="horarios" value=""/>
                    <c:set var="lin" value="${lin+10}"/>
                </c:otherwise>
            </c:choose> 
        </c:forEach>
        <c:if test="${horarios!=''}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
    </c:if>
    <c:if test="${not empty turma.horarios[6]}">
        <c:if test="${jaTem=='S'}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:222px;"><b>---------------</b></div>
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:272px;"><b>---------------------------------------------------</b></div>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
        <c:set var="jaTem" value="S"/>
        <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:220px;"><b>Domingo</b></div>
        <c:forEach var="horario" items="${turma.horarios[6]}">
            <c:choose>
                <c:when test="${horarios==''}">
                    <c:set var="horarios" value="${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                </c:when>    
                <c:otherwise>
                    <c:set var="horarios" value="${horarios} - ${fn:substring(horario.horaInicio,0,2)}:${fn:substring(horario.horaInicio,2,4)} as ${fn:substring(horario.horaFim,0,2)}:${fn:substring(horario.horaFim,2,4)}"/>
                    <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
                    <c:set var="horarios" value=""/>
                    <c:set var="lin" value="${lin+10}"/>
                </c:otherwise>
            </c:choose> 
        </c:forEach>
        <c:if test="${horarios!=''}">
            <div class="tituloInstrucoes" style="position:absolute;top:${lin}px;left:270px;"><b>${horarios}</b></div>    
            <c:set var="horarios" value=""/>
            <c:set var="lin" value="${lin+10}"/>
        </c:if>
    </c:if>
            

    <div style="position:absolute;top:210px;left:215px;width:215px;height:20px;" align="center">
        <span class="numeroConvite">${numPassaporte}</span> 
    </div>    
    <div style="position:absolute;top:230px;left:215px;width:215px;height:50px;" align="center">
        <span class="codBarras">!${numPassaporte}!</span> 
    </div> 
    

</html>
