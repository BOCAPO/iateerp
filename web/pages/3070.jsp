<%@page import="techsoft.curso.TurmaHorario"%>
<%@page import="techsoft.curso.Turma"%>
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
    <div id="titulo-subnav">Matrícula da Turma</div>
    <div class="divisoria"></div>

    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
        });        

        function cancelarMatricula(idTurma, situacao, matricula, seqDependente, idCategoria, dataMatricula){
            if(situacao != 'NORMAL'){
                alert('Não é possível cancelar uma matrícula ' + situacao);
                return;
            }
            

            var temErro = '';
            var temAlerta = '';
            $.ajax({url:'MatriculaAjax', async:false, dataType:'text', type:'GET',data:{
                                tipo:1,
                                matricula:matricula,
                                dependente:seqDependente,
                                categoria:idCategoria,
                                turma:idTurma}
            }).success(function(retorno){
                if (retorno!='OK'){
                    if(retorno.substring(0,1)=='E' ){
                        temErro = retorno.substring(2);
                    }else{
                        temAlerta = retorno.substring(2);
                    }
                }
            });

            if (temErro!=''){
                alert(temErro);
                return;
            }else if (temAlerta!=''){
                if (!confirm(temAlerta)){
                    return;
                }
            }
            
            
            if(confirm('Confirma o cancelamento da matrícula?')){
                window.location.assign('c?app=3071&idTurma=' + idTurma 
                    + '&matricula=' + matricula + '&seqDependente=' + seqDependente
                    + '&idCategoria=' + idCategoria
                    + '&dataMatricula=' + dataMatricula
                    + '&origem=MT'
                    );
            }
        }

        function reativarMatricula(idTurma, situacao, matricula, seqDependente, idCategoria){
            if(situacao != 'CANCELADA'){
                alert('Só é possível reativar matriculas canceladas');
                return;
            }
            
            if(confirm('Confirma a reativação da matrícula?')){
                window.location.assign('c?app=3072&idTurma=' + idTurma 
                    + '&matricula=' + matricula + '&seqDependente=' + seqDependente
                    + '&idCategoria=' + idCategoria);
            }
        }
    
        function emitirPassaporte(idTurma, categoria, matricula, seqDependente, idCategoria){
            if(categoria == 'SO'){
                alert('Categoria da Pessoa não permite emissão de Passaporte!');
            }else{
                window.location.assign('c?app=3075&idTurma=' + idTurma 
                    + '&matricula=' + matricula + '&seqDependente=' + seqDependente
                    + '&idCategoria=' + idCategoria);                
            }
        }
        
        function validarForm(){
            document.forms[0].submit();
        }

    </script>        
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="3070">
        <input type="hidden" name="acao" value="consultar">
        <input type="hidden" name="idTurma" value="${turma.id}">
        <input type="hidden" name="origem" value="${origem}">
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Curso:</p>
                    <c:forEach var="curso" items="${cursos}">
                        <c:if test="${turma.idCurso eq curso.id}"><input type="text" class="campoSemTamanho alturaPadrao" size="40" readonly value="${curso.descricao}"/></c:if>
                    </c:forEach>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Professor:</p>
                    <c:forEach var="professor" items="${professores}">
                        <c:if test="${turma.idProfessor eq professor.id}"><input type="text" class="campoSemTamanho alturaPadrao" size="40" readonly value="${professor.descricao}"/></c:if>
                    </c:forEach>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Periodo:</p>
                    <fmt:formatDate var="dataInicio" value="${turma.dataInicio}" pattern="dd/MM/yyyy"/>
                    <fmt:formatDate var="dataFim" value="${turma.dataFim}" pattern="dd/MM/yyyy"/>
                    <input type="text" size="25" class="campoSemTamanho alturaPadrao" readonly value="${dataInicio} até ${dataFim}"/>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Dias de Aula:</p>
                    <%
                    String diasAula = "";
                    Turma t = (Turma)request.getAttribute("turma");
                    java.util.List<TurmaHorario>[] horarios = t.getHorarios();
                    if(horarios[0].size() > 0) diasAula += "2/";
                    if(horarios[1].size() > 0) diasAula += "3/";
                    if(horarios[2].size() > 0) diasAula += "4/";
                    if(horarios[3].size() > 0) diasAula += "5/";
                    if(horarios[4].size() > 0) diasAula += "6/";
                    if(horarios[5].size() > 0) diasAula += "S/";
                    if(horarios[6].size() > 0) diasAula += "D/";

                    if(diasAula.length() > 0){
                        diasAula = diasAula.substring(0, diasAula.length() -1);
                        pageContext.setAttribute("diasAula", String.valueOf(diasAula));
                    }
                    %>
                    <input type="text" size="15" class="campoSemTamanho alturaPadrao" readonly value="${diasAula}"/>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Situação:</p>
                    <select name="situacao" class="campoSemTamanho alturaPadrao" style="width: 90px">
                        <option value="TD" <c:if test='${situacao == "TD"}'>selected</c:if>>TODAS</option>
                        <option value="NO" <c:if test='${situacao == "NO"}'>selected</c:if>>Normal</option>
                        <option value="CA" <c:if test='${situacao == "CA"}'>selected</c:if>>Cancelada</option>
                    </select>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Aluno:</p>
                    <input type="text" class="campoSemTamanho alturaPadrao" name="nomeAluno" id="nomeAluno" size="40" value="${nomeAluno}"/>
                </td>
                <td >    
                    <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Consultar" />
                </td>
            </tr>        
        </table>
    </form>
                
    <sql:query var="rs" dataSource="jdbc/iate">
         EXEC SP_RECUPERA_MATRICULAS_TURMA_NOVA ${turma.id}, '${nomeAluno}%', '${situacao}'
    </sql:query>
        
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" class="nome-lista">Nome</th>
            <th scope="col" align="center">% Pessoal</th>
            <th scope="col" align="center">% Familiar</th>
            <th scope="col" align="center">Dt. Matrícula</th>
            <th scope="col" align="center">Situação</th>
            <th scope="col" align="center">Venc. Passaporte</th>
            <th scope="col" align="center">Cancelar</th>
            <th scope="col" align="center">Reativar</th>
            <th scope="col" align="center">Imp. Passaporte</th>
            <th scope="col" align="center">Imp. Comprovante</th>
            <th scope="col" align="center">Alt. desconto</th>
            <th scope="col" align="center">Dt. Val. Atestado</th>
        </tr>	
        </thead>
        <tbody>
        <c:forEach var="row" items="${rs.rows}">
            <c:if test="${row.NOME_PESSOA eq 'LIMITEATINGIDO'}">                    
                <tr><td colspan="11" ><font color="red">${row.MSG}</font></td></tr>
            </c:if>                
            
            <c:if test="${row.NOME_PESSOA ne 'LIMITEATINGIDO'}">                    
                <tr>
                    <td class="column1" align="left">${row.NOME_PESSOA}</td>
                    <fmt:formatNumber var="descontoPessoal" value="${row.PERC_DESCONTO_PESSOAL}" pattern="#0.00"/>
                    <td class="column1" align="right">${descontoPessoal}</td>
                    <fmt:formatNumber var="descontoFamiliar" value="${row.PERC_DESCONTO_FAMILIA}" pattern="#0.00"/>
                    <td class="column1" align="right">${descontoFamiliar}</td>
                    <fmt:formatDate var="dataMatricula" value="${row.DT_MATRICULA}" pattern="dd/MM/yyyy"/>
                    <td class="column1" align="center">${dataMatricula}</td>

                    <c:choose>
                        <c:when test="${row.CD_SIT_MATRICULA eq 'NO'}">
                            <c:set var="situacao" value="NORMAL"/>
                        </c:when>
                        <c:when test="${row.CD_SIT_MATRICULA eq 'CA'}">
                            <c:set var="situacao" value="CANCELADA"/>
                        </c:when>
                        <c:when test="${row.CD_SIT_MATRICULA eq 'EV'}">
                            <c:set var="situacao" value="EVADIDA"/>
                        </c:when>
                        <c:when test="${row.CD_SIT_MATRICULA eq 'TR'}">
                            <c:set var="situacao" value="TRANCADA"/>
                        </c:when>
                        <c:when test="${row.CD_SIT_MATRICULA eq 'CT'}">
                            <c:set var="situacao" value="CANCELADA POR TRANSFERENCIA"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="situacao" value="NORMAL POR TRANSFERENCIA"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="column1" align="left">${situacao}</td>

                    <fmt:formatDate var="validadePassaporte" value="${row.DT_VALIDADE_PASSAPORTE}" pattern="dd/MM/yyyy"/>
                    <td class="column1" align="center">${validadePassaporte}</td>                
                    <td class="column1" align="center">
                    <c:if test='<%=request.isUserInRole("3071")%>'>                    
                        <a href="javascript:void(0)" onclick="javascript: cancelarMatricula(${row.SEQ_TURMA}, '${situacao}', ${row.CD_MATRICULA}, ${row.SEQ_DEPENDENTE}, ${row.CD_CATEGORIA}, ${dataMatricula})"><img src="imagens/icones/inclusao-titular-05.png" /></a>    
                    </c:if>                
                    </td>
                    <td class="column1" align="center">
                    <c:if test='<%=request.isUserInRole("3072")%>'>                    
                        <a href="javascript:void(0)" onclick="javascript: reativarMatricula(${row.SEQ_TURMA}, '${situacao}', ${row.CD_MATRICULA}, ${row.SEQ_DEPENDENTE}, ${row.CD_CATEGORIA})"><img src="imagens/icones/inclusao-titular-06.png" /></a>    
                    </c:if>                
                    </td>
                    <td class="column1" align="center">
                    <c:if test='<%=request.isUserInRole("3075")%>'>    
                        <a href="javascript:void(0)" onclick="javascript: emitirPassaporte(${row.SEQ_TURMA}, '${row.TP_CATEGORIA}', ${row.CD_MATRICULA}, ${row.SEQ_DEPENDENTE}, ${row.CD_CATEGORIA})"><img src="imagens/icones/inclusao-titular-01.png" /></a>
                    </c:if>                
                    </td>
                    <td class="column1" align="center">
                    <c:if test='<%=request.isUserInRole("3041")%>'>                    
                        <a href="c?app=3041&idTurma=${row.SEQ_TURMA}&matricula=${row.CD_MATRICULA}&seqDependente=${row.SEQ_DEPENDENTE}&idCategoria=${row.CD_CATEGORIA}&dataMatricula=${dataMatricula}"><img src="imagens/icones/inclusao-titular-13.png" /></a>    
                    </c:if>     
                    </td>
                    <td class="column1" align="center">
                    <c:if test='<%=request.isUserInRole("3076")%>'>                    
                        <a href="c?app=3076&idTurma=${row.SEQ_TURMA}&matricula=${row.CD_MATRICULA}&seqDependente=${row.SEQ_DEPENDENTE}&idCategoria=${row.CD_CATEGORIA}&descontoPessoal=${descontoPessoal}"><img src="imagens/icones/inclusao-titular-03.png" /></a>    
                    </c:if>                                    
                    </td>                            
                    <fmt:formatDate var="validadeAtestado" value="${row.DT_VAL_ATESTADO}" pattern="dd/MM/yyyy"/>
                    <td class="column1" align="center">
                        <a href="c?app=3040&matricula=${row.CD_MATRICULA}&seqDependente=${row.SEQ_DEPENDENTE}&idCategoria=${row.CD_CATEGORIA}&idTurma=${turma.id}&acao=atestado">
                            ${validadeAtestado}
                        </a>    
                    </td>                
                </tr>
            </c:if>                
        </c:forEach>
        </tbody>
    </table>
        
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=${origem}';" value=" " />
        
</body>
</html>
