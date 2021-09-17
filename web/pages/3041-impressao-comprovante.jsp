<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html>
    <style type="text/css">
        .tituloRelatorio {
                font-family: "Arial";
                font-size: 26px;
                font-weight: bold;
        }
        .tituloCampo{
                font-family: "Arial";
                font-size: 15px;
                font-weight: bold;
        }        
        .campo{
                font-family: "Arial";
                font-size: 14px;
                margin-left:15px;
        }
        .campoCIENTE{
                font-family: "Arial";
                font-size: 18px;
                margin-left:15px;
        }
    </style>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <c:if test="${tipoComprovante eq 'M'}"><title>FICHA DE INSCRIÇÃO</title></c:if>
        <c:if test="${tipoComprovante eq 'C'}"><title>PEDIDO DE CANCELAMENTO</title></c:if>        
    </head>
    <body>
        
        <c:set var="diasHorarios" value=""/>
        <c:if test="${not empty turma.horarios[0]}">
            <c:set var="diasHorarios" value="${diasHorarios}Segunda:<br>"/>
            <c:forEach var="horario" items="${turma.horarios[0]}">
                <c:set var="diasHorarios" value="${diasHorarios}&nbsp;&nbsp;&nbsp;${horario.horaInicio} as ${horario.horaFim}<br>"/>
            </c:forEach>
        </c:if>
        <c:if test="${not empty turma.horarios[1]}">
            <c:set var="diasHorarios" value="${diasHorarios}Terça:<br>"/>
            <c:forEach var="horario" items="${turma.horarios[1]}">
                <c:set var="diasHorarios" value="${diasHorarios}&nbsp;&nbsp;&nbsp;${horario.horaInicio} as ${horario.horaFim}<br>"/>
            </c:forEach>
        </c:if>
        <c:if test="${not empty turma.horarios[2]}">
            <c:set var="diasHorarios" value="${diasHorarios}Quarta:<br>"/>
            <c:forEach var="horario" items="${turma.horarios[2]}">
                <c:set var="diasHorarios" value="${diasHorarios}&nbsp;&nbsp;&nbsp;${horario.horaInicio} as ${horario.horaFim}<br>"/>
            </c:forEach>
        </c:if>
        <c:if test="${not empty turma.horarios[3]}">
            <c:set var="diasHorarios" value="${diasHorarios}Quinta:<br>"/>
            <c:forEach var="horario" items="${turma.horarios[3]}">
                <c:set var="diasHorarios" value="${diasHorarios}&nbsp;&nbsp;&nbsp;${horario.horaInicio} as ${horario.horaFim}<br>"/>
            </c:forEach>
        </c:if>
        <c:if test="${not empty turma.horarios[4]}">
            <c:set var="diasHorarios" value="${diasHorarios}Sexta:<br>"/>
            <c:forEach var="horario" items="${turma.horarios[4]}">
                <c:set var="diasHorarios" value="${diasHorarios}&nbsp;&nbsp;&nbsp;${horario.horaInicio} as ${horario.horaFim}<br>"/>
            </c:forEach>
        </c:if>
        <c:if test="${not empty turma.horarios[5]}">
            <c:set var="diasHorarios" value="${diasHorarios}Sábado:<br>"/>
            <c:forEach var="horario" items="${turma.horarios[5]}">
                <c:set var="diasHorarios" value="${diasHorarios}&nbsp;&nbsp;&nbsp;${horario.horaInicio} as ${horario.horaFim}<br>"/>
            </c:forEach>
        </c:if>
        <c:if test="${not empty turma.horarios[6]}">
            <c:set var="diasHorarios" value="${diasHorarios}Domingo:<br>"/>
            <c:forEach var="horario" items="${turma.horarios[6]}">
                <c:set var="diasHorarios" value="${diasHorarios}&nbsp;&nbsp;&nbsp;${horario.horaInicio} as ${horario.horaFim}<br>"/>
            </c:forEach>
        </c:if>
        
        <table width="100%">
          <tr>
            <td>
                <table width="100%" border="0">
                  <tr>
                    <td align="center" width="150px" rowspan="2" >
                        <img src="imagens/logo-intro.png" width="100" height="80"/>
                    </td>
                    <td valign="bottom" align="center" height="45" class="tituloRelatorio">
                        <c:if test="${tipoComprovante eq 'M'}">FICHA DE INSCRIÇÃO</c:if>
                        <c:if test="${tipoComprovante eq 'C'}">PEDIDO DE CANCELAMENTO</c:if>
                    </td>
                    <td align="center" width="150px" rowspan="2" >
                        <img src="f?tb=6&mat=${socio.matricula}&seq=${socio.seqDependente}&cat=${socio.idCategoria}" width="80" height="100"/>
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
        </table>
                    
                    
        <table width="100%">
          <tr>
            <td style="width: 200px">
                <div class="tituloCampo">Nome</div>
                <div class="campo">${socio.nome}</div>
            </td>
            <td align="left" width="150px">
                <div class="tituloCampo">Nº do Título:</div>
                <fmt:formatNumber pattern="0000" var="matricula" value="${socio.matricula}"/>
                <fmt:formatNumber pattern="00" var="categoria" value="${socio.idCategoria}"/>
                <div class="campo">${matricula}/${categoria}</div>
            </td>
          </tr>
          <tr>
            <td style="width: 200px">
                <div class="tituloCampo">Instrutor</div>
                <c:forEach var="professor" items="${professores}">
                    <c:if test="${turma.idProfessor eq professor.id}"><div class="campo">${professor.descricao}</div></c:if>
                </c:forEach>
            </td>
            <td align="left" width="150px" rowspan="7" valign="top">
                <div class="tituloCampo">Dias e Horários:</div>
                <fmt:formatNumber pattern="0000" var="matricula" value="${socio.matricula}"/>
                <fmt:formatNumber pattern="00" var="categoria" value="${socio.idCategoria}"/>
                <div class="campo">${diasHorarios}</div>
            </td>
          </tr>
          <tr>
            <td>
                <div class="tituloCampo">Data:</div>
                <div class="campo">${dataMatricula}</div>
            </td>
          <tr>
          </tr>
            <td>
                <div class="tituloCampo">Modalidade:</div>
                <c:forEach var="curso" items="${cursos}">
                    <c:if test="${turma.idCurso eq curso.id}"><div class="campo">${curso.descricao} - ${turma.deTurma}</div></c:if>
                </c:forEach>                
            </td>
          </tr>
          </tr>
            <td>
                <div class="tituloCampo">Dt. Nascimento:</div>
                <fmt:formatDate var="dataNascimento" value="${socio.dataNascimento}" pattern="dd/MM/yyyy"/>
                <div class="campo">${dataNascimento}</div>
            </td>
          </tr>
          </tr>
            <td>
                <div class="tituloCampo">Fone:</div>
                <div class="campo">${socio.telefoneR} / ${socio.telefoneC}</div>
            </td>
          </tr>
        </table>
                
                
    <HR>
    
    <table>
        <tr>
            <td class="campo">
                <b>Atenção:</b> A Solicitação de cancelamento deverá ser feita pessoalmente na recepção das Secretarias das modalidades, até o dia 5(cinco) do mês em curso.<br>
            </td>
        </tr>
        <tr>
            <td class="campo">
                <b>No período do dia 6 ao dia 11, será cobrada taxa proporcional.</b>
            </td>
        </tr>
        <tr>
            <td class="campo">
                <p><b>Obs.: Cancelamentos feitos a partir do dia 12 serão cobrados integralmente.</b></p>
            </td>
        </tr>
        <tr>
            <td class="campoCIENTE">
                <p><b>CIENTE:</b></p>
            </td>
        </tr>
        <tr>
            <td class="campo">
                <fmt:formatDate var="hoje" value="<%=new java.util.Date()%>" pattern="EEEE, dd 'de' MMMM 'de' yyyy"/>
                <p>Brasília, ${hoje}</p>    
            </td>
        </tr>
    </table>
    

    <br><br><br>        
    
    <table width="100%">
        <tr>
            <td class="campo" align="center">___________________________________________</td>
            <td class="campo" align="center">___________________________________________</td>
        </tr>
        <tr >
            <td class="campo" align="center">Responsável</td>
            <td class="campo" align="center">Sec. de Esportes</td>
        </tr>
    </table>
    </body>
</html>
