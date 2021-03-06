<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
        });        
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Cadastro de Turmas</div>
    <div class="divisoria"></div>

    
    <form action="c">
        <input type="hidden" name="app" value="3030">
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Curso:</p>
                    <select name="idCurso" class="campoSemTamanho alturaPadrao larguraComboCategoria" onchange="javascript: document.forms[0].submit()">
                        <option value="0">TODOS</option>
                        <c:forEach var="curso" items="${cursos}">
                            <option value="${curso.id}" <c:if test="${idCurso eq curso.id}">selected</c:if>>${curso.descricao}</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Professor:</p>
                    <select name="idProfessor" class="campoSemTamanho alturaPadrao larguraComboCategoria" onchange="javascript: document.forms[0].submit()">
                        <option value="0">TODOS</option>
                        <c:forEach var="professor" items="${professores}">
                            <option value="${professor.id}" <c:if test="${idProfessor eq professor.id}">selected</c:if>>${professor.descricao}</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <input type="checkbox" class="legendaCodigo MargemSuperior0" style="margin-top:20px" <c:if test="${mostrarSomenteTurmasAtivas}">checked</c:if> onchange="javascript: document.forms[0].submit()" name="mostrarSomenteTurmasAtivas"  id="mostrarSomenteTurmasAtivas" value="true"/><span class="legendaSemMargem larguraData">mostrar somente turmas ativas</span>
                </td>
            </tr>
        </table>
    </form>
        
    <c:if test='<%=request.isUserInRole("3031")%>'>
        <div class="botaoincluirmargem0" style="margin-left:15px;">
            <a href="c?app=3031&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>                
    </c:if>
    <br>               

    <sql:query var="rs" dataSource="jdbc/iate">
        SELECT TB_TURMA.SEQ_TURMA             ,
                TB_FUNCIONARIO.CD_FUNCIONARIO        ,
                TB_FUNCIONARIO.NOME_FUNCIONARIO,
                TB_MODALIDADE_CURSO.CD_MODALIDADE         ,
                TB_MODALIDADE_CURSO.DESCR_MODALIDADE , 
                TB_CURSO.CD_CURSO              ,
                TB_CURSO.DESCR_CURSO          ,
                TB_TURMA.QT_VAGAS_TURMA        ,
                TB_TURMA.SD_VAGAS_TURMA   ,
                TB_TURMA.DT_INIC_TURMA,
                TB_TURMA.DT_FIM_TURMA,
                TB_TURMA.DE_TURMA
        FROM TB_TURMA,
             TB_FUNCIONARIO,
             TB_CURSO,
             TB_MODALIDADE_CURSO
        WHERE TB_TURMA.CD_FUNCIONARIO = TB_FUNCIONARIO.CD_FUNCIONARIO
        AND   TB_CURSO.CD_MODALIDADE = TB_MODALIDADE_CURSO.CD_MODALIDADE
        AND   TB_TURMA.CD_CURSO = TB_CURSO.CD_CURSO
        <c:if test='${mostrarSomenteTurmasAtivas}'>AND TB_TURMA.DT_FIM_TURMA >= GETDATE()</c:if>
        <c:if test='${idCurso gt 0}'>AND TB_CURSO.CD_CURSO = ${idCurso}</c:if> 
        <c:if test='${idProfessor gt 0}'>AND TB_FUNCIONARIO.CD_FUNCIONARIO = ${idProfessor}</c:if> 
    ORDER BY 7
    </sql:query>    
        
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" class="nome-lista">Curso</th>
            <th scope="col" class="nome-lista">Turma</th>
            <th scope="col" class="nome-lista">Professor</th>
            <th scope="col" class="nome-lista">Sd. Vagas</th>
            <th scope="col" class="nome-lista">Qt. Vagas</th>
            <th scope="col" class="nome-lista">Aula Segunda</th>
            <th scope="col" class="nome-lista">Aula Ter?a</th>
            <th scope="col" class="nome-lista">Aula Quarta</th>
            <th scope="col" class="nome-lista">Aula Quinta</th>
            <th scope="col" class="nome-lista">Aula Sexta</th>
            <th scope="col" class="nome-lista">Aula S?bado</th>
            <th scope="col" class="nome-lista">Aula Domingo</th>
            <th scope="col" class="nome-lista">Per?odo</th>
            <th scope="col" class="nome-lista">Modalidade</th>
            <th scope="col" >Alterar</th>
            <th scope="col" >Excluir</th>
            <th scope="col" >Matr?culas</th>
        </tr>	
        </thead>
        <tbody>

        <c:forEach var="row" items="${rs.rows}">

            <tr height="1">
                <td class="column1" align="left">${row.DESCR_CURSO}</td>
                <td class="column1" align="left">${row.DE_TURMA}</td>
                <td class="column1" align="left">${row.NOME_FUNCIONARIO}</td>
                <td class="column1" align="left">${row.SD_VAGAS_TURMA}</td>
                <td class="column1" align="left">${row.QT_VAGAS_TURMA}</td>
                <sql:query var="rs2" dataSource="jdbc/iate">
                    SELECT CD_DIA, HH_INICIO, HH_FIM FROM TB_HORARIO_TURMA 
                    WHERE SEQ_TURMA = ${row.SEQ_TURMA} ORDER BY 1
                    
                </sql:query>                                
                <c:forEach var="row2" items="${rs2.rows}">
                    <c:choose>
                        <c:when test="${row2.CD_DIA eq 1}">
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} ?s ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                            <c:choose>
                                <c:when test="${empty segunda}">
                                    <c:set var="segunda" value="${tmp}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="segunda" value="${segunda} - ${tmp}"/>
                                </c:otherwise>
                            </c:choose>    
                        </c:when>
                        <c:when test="${row2.CD_DIA eq 2}">
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} ?s ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                            <c:choose>
                                <c:when test="${empty terca}">
                                    <c:set var="terca" value="${tmp}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="terca" value="${terca} - ${tmp}"/>
                                </c:otherwise>
                            </c:choose>    
                        </c:when>
                        <c:when test="${row2.CD_DIA eq 3}">
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} ?s ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                            <c:choose>
                                <c:when test="${empty quarta}">
                                    <c:set var="quarta" value="${tmp}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="quarta" value="${quarta} - ${tmp}"/>
                                </c:otherwise>
                            </c:choose>    
                        </c:when>
                        <c:when test="${row2.CD_DIA eq 4}">
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} ?s ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                            <c:choose>
                                <c:when test="${empty quinta}">
                                    <c:set var="quinta" value="${tmp}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="quinta" value="${quinta} - ${tmp}"/>
                                </c:otherwise>
                            </c:choose>    
                        </c:when>
                        <c:when test="${row2.CD_DIA eq 5}">
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} ?s ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                            <c:choose>
                                <c:when test="${empty sexta}">
                                    <c:set var="sexta" value="${tmp}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="sexta" value="${sexta} - ${tmp}"/>
                                </c:otherwise>
                            </c:choose>    
                        </c:when>
                        <c:when test="${row2.CD_DIA eq 6}">
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} ?s ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                            <c:choose>
                                <c:when test="${empty sabado}">
                                    <c:set var="sabado" value="${tmp}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="sabado" value="${sabado} - ${tmp}"/>
                                </c:otherwise>
                            </c:choose>    
                        </c:when>
                        <c:otherwise>
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} ?s ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                            <c:choose>
                                <c:when test="${empty domingo}">
                                    <c:set var="domingo" value="${tmp}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="domingo" value="${domingo} - ${tmp}"/>
                                </c:otherwise>
                            </c:choose>    
                        </c:otherwise>                        
                    </c:choose>
                        

                </c:forEach>
                <td>${segunda}</td>
                <td>${terca}</td>
                <td>${quarta}</td>
                <td>${quinta}</td>
                <td>${sexta}</td>
                <td>${sabado}</td>
                <td>${domingo}</td>    
                <c:remove var="segunda"/>
                <c:remove var="terca"/>
                <c:remove var="quarta"/>
                <c:remove var="quinta"/>
                <c:remove var="sexta"/>
                <c:remove var="sabado"/>
                <c:remove var="domingo"/>
                <fmt:formatDate var="dataInicio" value="${row.DT_INIC_TURMA}" pattern="dd/MM/yyyy" />
                <fmt:formatDate var="dataFim" value="${row.DT_FIM_TURMA}" pattern="dd/MM/yyyy" />
                <td class="column1" align="left">${dataInicio} at? ${dataFim}</td>
                <td class="column1" align="left">${row.DESCR_MODALIDADE}</td>

                <c:if test='<%=request.isUserInRole("3032")%>'>
                    <td class="column1" align="center">
                        <a href="c?app=3032&acao=showForm&idTurma=${row.SEQ_TURMA}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                    </td>
                </c:if>
                <c:if test='<%=request.isUserInRole("3033")%>'>
                    <td class="column1" align="center">
                        <a href="javascript: if(confirm('Confirma a exclus?o da turma selecionada?')) window.location.href='c?app=3033&idTurma=${row.SEQ_TURMA}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </td>
                </c:if>
                <c:if test='<%=request.isUserInRole("3070")%>'>
                    <td class="column1" align="center">
                            <a href="c?app=3070&idTurma=${row.SEQ_TURMA}&origem=3030"><img src="imagens/icones/inclusao-titular-04.png" /></a>

                    </td>
                </c:if>
            </tr>	

        </c:forEach>

        </tbody>
    </table>
        
</body>
</html>
