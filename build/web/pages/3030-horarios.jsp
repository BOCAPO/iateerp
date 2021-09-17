<%@page import="javax.xml.ws.handler.MessageContext.Scope"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Alterar Turmas</div>
    <div class="divisoria"></div>

    <br>
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(trim(document.forms[0].horaInicio.value) == ''){
                alert('Informe o horário de entrada');
                return;                
            }
            if(trim(document.forms[0].horaFim.value) == ''){
                alert('Informe o horário de saída');
                return;                
            }

            if(parseInt(document.forms[0].horaInicio.value) > 2400){
                alert('o horário de entrada deve estar entre 0000 e 2400');
                return;
            }
            if(parseInt(document.forms[0].horaFim.value) > 2400){
                alert('o horário de saída deve estar entre 0000 e 2400');
                return;
            }
            if(parseInt(document.forms[0].horaFim.value) <= parseInt(document.forms[0].horaInicio.value)){
                alert('o horário de saída deve ser depois do horário de entrada');
                return;
            }            
            
            document.forms[0].submit();

        }

    </script>        

    <form action="c">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="adicionarHorario">

        <table>
        <tr>
        <td>
        <p class="legendaCodigo MargemSuperior0">Curso:</p>
        <c:forEach var="curso" items="${cursos}">
            <c:if test="${turma.idCurso eq curso.id}"><input type="text" size="50" readonly value="${curso.descricao}"/></c:if>
        </c:forEach>
        </td>
        <td>
        <p class="legendaCodigo MargemSuperior0">Professor:</p>
        <c:forEach var="professor" items="${professores}">
            <c:if test="${turma.idProfessor eq professor.id}"><input type="text" size="50" readonly value="${professor.descricao}"/></c:if>
        </c:forEach>
        </td>
        </tr>
        <tr>
        <td>
        <input type="radio" name="dia" class="legendaCodigo" value="0" checked>Segunda-Feira<BR>
        <input type="radio" name="dia" class="legendaCodigo" value="1">Terça-Feira<BR>
        <input type="radio" name="dia" class="legendaCodigo" value="2">Quarta-Feira<BR>
        <input type="radio" name="dia" class="legendaCodigo" value="3">Quinta-Feira<BR>
        <input type="radio" name="dia" class="legendaCodigo" value="4">Sexta-Feira<BR>
        <input type="radio" name="dia" class="legendaCodigo" value="5">Sábado<BR>
        <input type="radio" name="dia" class="legendaCodigo" value="6">Domingo<BR>
        </td>
        <td>        
        <p class="legendaCodigo MargemSuperior0" >Entrada</p>
        <input type="text" maxlength="4" name="horaInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraData"><br>

        <p class="legendaCodigo MargemSuperior0" >Saída</p>
        <input type="text" maxlength="4" name="horaFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraData"><br>
        <BR>
        <input type="button" onclick="validarForm()" value="Adicionar Horário" />
        </td>
        </tr>
        </table>
        <table>
            <tr>
                <td>Segunda-Feira</td>
                <td>Terça-Feira</td>
                <td>Quarta-Feira</td>
                <td>Quinta-Feira</td>
                <td>Sexta-Feira</td>
                <td>Sábado</td>
                <td>Domingo</td>
            </tr>
            
            <c:set var="maxRows" value="1"/>
            <c:forEach var="horarios" items="${turma.horarios}">
                <c:if test="${fn:length(horarios) gt maxRows}"><c:set var="maxRows" value="${fn:length(horarios)}"/></c:if>
            </c:forEach>

            <c:forEach begin="0" end="${maxRows -1}" var="row">
                <tr>
                <% int col = 0; %>
                <c:forEach var="horarios" items="${turma.horarios}">
                    <c:choose>
                    <c:when test="${not empty horarios[row]}">
                    <td>${horarios[row].horaInicio} às ${horarios[row].horaFim}<a href="c?app=3032&acao=removerHorario&dia=<%=col++%>&horaInicio=${horarios[row].horaInicio}&horaFim=${horarios[row].horaFim}"><img src="img/no.png" title="remover"></a></td>
                    </c:when>
                    <c:otherwise>
                        <td></td>
                    </c:otherwise>
                    </c:choose>
                </c:forEach>
                </tr>
            </c:forEach>

        </table>
        <c:choose>
            <c:when test="${app == '3031'}">
                <a href='c?app=3031&acao=voltarTelaCadastro'>voltar</a>
            </c:when>
            <c:otherwise>
                <a href='c?app=3032&acao=showForm&idTurma=${turma.id}'>voltar</a>
            </c:otherwise>
        </c:choose>        
        <a href='c?app=${app}&acao=gravar'>gravar</a>
    </form>

        
</body>
</html>
