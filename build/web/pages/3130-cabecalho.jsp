<%@page import="techsoft.tabelas.Funcionario"%>
<%@page import="techsoft.tabelas.Curso"%>
<%@page import="techsoft.curso.Turma"%>
<%@page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%@page import="techsoft.db.Pool"%>
<%@page import="java.sql.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



    <table width="100%" >
        <tr>
          <th align="left" width="150px"><img src="imagens/logo-intro.png" width="70" height="55"/></th>
          <th align="center" height="0" class="tituloRelatorio">Pauta</th>
          <th width="150px" class="dados" align="right">${hhAtual}<br>${dtAtual}</th>
        </tr>
    </table>
    <table width="100%" >
        <tr>
          <th align="left" class="cabecalho">
              Turma: ${curso.descricao} - ${turma.deTurma}
          </th>
          <th align="center" height="0" rowspan='3'>Mês/Ano _____/_______</th>
        </tr>
        <tr>
          <th align="left" class="cabecalho">
              Professor: ${funcionario.nome}
          </th>
        </tr>
        <tr>
          <th align="left" class="cabecalho">
              Dias:
            <c:set var="dia" value="0"/>
            <c:set var="diaAnterior" value="0"/>
            <c:forEach var="horarios" items="${turma.horarios}">
                <c:set var="dia" value="${dia+1}"/>                
                <c:set var="maxRows" value="${fn:length(horarios)}"/>
                <c:if test="${maxRows gt 0}">
                    <c:set var="lin" value="0"/>
                    <c:forEach begin="0" end="${maxRows - 1}" var="lin">
                        <c:choose>
                        <c:when test="${diaAnterior != dia}">
                            <c:set var="diaAnterior" value="${dia}"/>
                            <c:choose>
                            <c:when test="${dia==6}">S - </c:when>
                            <c:when test="${dia==7}">D - </c:when>
                            <c:otherwise>${dia+1} - </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise> - </c:otherwise>
                        </c:choose>

                        ${fn:substring(horarios[lin].horaInicio,0,2)}:${fn:substring(horarios[lin].horaInicio,2,4)} às ${fn:substring(horarios[lin].horaFim,0,2)}:${fn:substring(horarios[lin].horaFim,2,4)};

                    </c:forEach>
                </c:if>
            </c:forEach>        

            </th>
          </tr>
        </table>
        <table width="100%" >
            <tr>
                <td colspan="2">
                    <table width="100%" class="tabela" style="padding:0; margin:0;" >      
                        <tr>
                            <td>&nbsp;</td>
                            <td align="center" width="710px" class="dados">D&nbsp;&nbsp;&nbsp;&nbsp;I&nbsp;&nbsp;&nbsp;&nbsp;A&nbsp;&nbsp;&nbsp;&nbsp;S</td>
                        </tr>
                    </table>      
                </td>
            </tr>
        </table>
            