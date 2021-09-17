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
        
    function validarForm(){
        var k = 0;
        for(var i = 0; i < document.forms[1].turmas.length; i++){
            if(document.forms[1].turmas[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma turma para impressao.');
            return;
        }       
        document.forms[1].submit();
    }
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Impressão de Pauta</div>
    <div class="divisoria"></div>

    
    <form action="c">
        <input type="hidden" name="app" value="3130">
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
            </tr>
        </table>
    </form>
        
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
                TB_TURMA.DT_FIM_TURMA    , 
                TB_TURMA.DE_TURMA    
        FROM TB_TURMA,
             TB_FUNCIONARIO,
             TB_CURSO,
             TB_MODALIDADE_CURSO
        WHERE TB_TURMA.CD_FUNCIONARIO = TB_FUNCIONARIO.CD_FUNCIONARIO
        AND   TB_CURSO.CD_MODALIDADE = TB_MODALIDADE_CURSO.CD_MODALIDADE
        AND   TB_TURMA.CD_CURSO = TB_CURSO.CD_CURSO
        <c:if test='${idCurso gt 0}'>AND TB_CURSO.CD_CURSO = ${idCurso}</c:if> 
        <c:if test='${idProfessor gt 0}'>AND TB_FUNCIONARIO.CD_FUNCIONARIO = ${idProfessor}</c:if> 
    ORDER BY 7
    </sql:query>    

    <form action="c">
    <input type="hidden" name="app" value="3130">
    <input type="hidden" name="acao" value="visualizar">    
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" class="nome-lista">Imprimir</th>
            <th scope="col" class="nome-lista">Curso</th>
            <th scope="col" class="nome-lista">Turma</th>
            <th scope="col" class="nome-lista">Professor</th>
            <th scope="col" class="nome-lista">Sd. Vagas</th>
            <th scope="col" class="nome-lista">Qt. Vagas</th>
            <th scope="col" class="nome-lista">Aula Segunda</th>
            <th scope="col" class="nome-lista">Aula Terça</th>
            <th scope="col" class="nome-lista">Aula Quarta</th>
            <th scope="col" class="nome-lista">Aula Quinta</th>
            <th scope="col" class="nome-lista">Aula Sexta</th>
            <th scope="col" class="nome-lista">Aula Sábado</th>
            <th scope="col" class="nome-lista">Aula Domingo</th>
            <th scope="col" class="nome-lista">Período</th>
            <th scope="col" class="nome-lista">Modalidade</th>
        </tr>	
        </thead>
        <tbody>

        <c:forEach var="row" items="${rs.rows}">

            <tr height="1">
                <td><input type="checkbox" class="legendaCodigo MargemSuperior0" style="margin-top:20px" name="turmas" value="${row.SEQ_TURMA}"/></td>
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
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
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
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
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
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
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
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
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
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
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
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
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
                            <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
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
                <td class="column1" align="left">${dataInicio} até ${dataFim}</td>
                <td class="column1" align="left">${row.DESCR_MODALIDADE}</td>
            </tr>	

        </c:forEach>

        </tbody>
    </table>
    
    <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />
    
    <!-- gato para funcionar quando só tem uma turma, pois quando só tem uma turma o javascript nao funciona para varrer o vetor.-->
    <input type="hidden" name="turmas" value="0"/>
    </form>
        
</body>
</html>
