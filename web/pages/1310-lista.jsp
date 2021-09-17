<%@include file="head.jsp"%>

<body class="internas">
            
    <%@include file="menu.jsp"%>
        
    <script type="text/javascript" language="javascript">
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
                    + '&origem=CCS'
                    );
            }
        }
        
    </script>  


    <div class="divisoria"></div>
    <div id="titulo-subnav">Consulta Cursos do Aluno</div>
    <div class="divisoria"></div>
    
   <table id="tabela"> 
    <thead>
    <tr class="odd">
        <th scope="col" class="nome-lista">Pessoa</td>
        <th scope="col">Curso</td>
        <th scope="col">Turma</td>
        <th scope="col">Status</td>
        <th scope="col">Dt. Status</td>
        <th scope="col">% Desc. Pes.</td>
        <th scope="col">% Desc. Fam.</td>
            
        <th scope="col">Professor</td>
        <th scope="col">Aula Segunda</td>
        <th scope="col">Aula Terça</td>
        <th scope="col">Aula Quarta</td>
        <th scope="col">Aula Quinta</td>
        <th scope="col">Aula Sexta</td>
        <th scope="col">Aula Sábado</td>
        <th scope="col">Aula Domingo</td>
        <th scope="col">Período</td>
        <th scope="col">Cancelar</td>
    </tr>	
    </thead>

    <c:forEach var="ccs" items="${consultaCursoSocio}">
        <tr>
            <td class="column1" >${ccs.socio.nome}</td>
            <td class="column1" >${ccs.curso}</td>
            <td class="column1" >${ccs.deTurma}</td>
            
            <c:choose>
                <c:when test="${ccs.codigoSituacao eq 'NO'}">
                    <c:set var="situacao" value="NORMAL"/>
                </c:when>
                <c:when test="${ccs.codigoSituacao eq 'CA'}">
                    <c:set var="situacao" value="CANCELADA"/>
                </c:when>
                <c:when test="${ccs.codigoSituacao eq 'EV'}">
                    <c:set var="situacao" value="EVADIDA"/>
                </c:when>
                <c:when test="${ccs.codigoSituacao eq 'TR'}">
                    <c:set var="situacao" value="TRANCADA"/>
                </c:when>
                <c:when test="${ccs.codigoSituacao eq 'CT'}">
                    <c:set var="situacao" value="CANCELADA POR TRANSFERENCIA"/>
                </c:when>
                <c:otherwise>
                    <c:set var="situacao" value="NORMAL POR TRANSFERENCIA"/>
                </c:otherwise>
            </c:choose>

            
            <td class="column1">${situacao}</td>       
            <td class="column1"><fmt:formatDate value="${ccs.dtOcorrencia}" pattern="dd/MM/yyyy"/></td>       
            <td class="column1" align="right"><fmt:formatNumber value="${ccs.descPessoal}" pattern="#0.00"/></th>
            <td class="column1" align="right"><fmt:formatNumber value="${ccs.descFamiliar}" pattern="#0.00"/></th>
            <td class="column1">${ccs.professor}</td>        
            <td class="column1">${ccs.aulaSegunda}</td>
            <td class="column1">${ccs.aulaTerca}</td>
            <td class="column1">${ccs.aulaQuarta}</td>
            <td class="column1">${ccs.aulaQuinta}</td>
            <td class="column1">${ccs.aulaSexta}</td>
            <td class="column1">${ccs.aulaSabado}</td>
            <td class="column1">${ccs.aulaDomingo}</td>
            
            <fmt:formatDate pattern="dd/MM/yyyy" var="inicio" value="${ccs.inicio}"/>
            <fmt:formatDate pattern="dd/MM/yyyy" var="fim" value="${ccs.fim}"/>
            <td class="column1">${inicio} até ${fim}</td>
            
            <fmt:formatDate var="dataMatricula" value="${ccs.dtMatricula}" pattern="dd/MM/yyyy"/>
            <td class="column1" align="center">
                <c:if test='<%=request.isUserInRole("3071")%>'>                    
                    <a href="javascript:void(0)" onclick="javascript: cancelarMatricula(${ccs.seqTurma}, '${situacao}', ${ccs.socio.matricula}, ${ccs.socio.seqDependente}, ${ccs.socio.idCategoria}, ${dataMatricula})"><img src="imagens/icones/inclusao-titular-05.png" /></a>    
                </c:if>                
            </td>
            
        </tr>        

    </c:forEach>
</table>
    
<input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${socio.matricula}&categoria=${socio.idCategoria}';" value=" " />
    
</body>
</html>
