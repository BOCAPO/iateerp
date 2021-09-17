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
                
                $("#dtRef").mask("99/99/9999");
                
        });     
        
    function validarForm(){
        document.forms[0].submit();
    }
        
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Cadastro de Exceções da Agenda da Academia</div>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" value="3220">
        <input type="hidden" name="app" value="consultar">
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Tipo:</p>
                    <div class="selectheightnovo">
                        <select name="tipo" class="campoSemTamanho alturaPadrao">
                            <option value="T" <c:if test='${tipo == "T"}'>selected</c:if>>TODOS</option>
                            <option value="B" <c:if test='${tipo == "B"}'>selected</c:if>>Bloqueio</option>
                            <option value="A" <c:if test='${tipo == "A"}'>selected</c:if>>Abertura</option>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Serviço:</p>
                    <div class="selectheightnovo">
                        <select name="idServico" class="campoSemTamanho alturaPadrao larguraCidade" >
                            <option value="0" <c:if test='${idServico == "0"}'>selected</c:if>>TODOS</option>
                            <c:forEach var="servico" items="${servicos}">
                                <option value="${servico.id}" <c:if test='${idServico == servico.id}'>selected</c:if>>${servico.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Profissional:</p>
                    <div class="selectheightnovo">
                        <select name="idFuncionario" class="campoSemTamanho alturaPadrao larguraCidade" >
                            <option value="0" <c:if test='${idFuncionario == "0"}'>selected</c:if>>TODOS</option>
                            <c:forEach var="funcionario" items="${funcionarios}">
                                <option value="${funcionario.id}" <c:if test='${idFuncionario == funcionario.id}'>selected</c:if>>${funcionario.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Pesquisa:</p>
                    <div class="selectheightnovo">
                        <select name="idTipoPesquisa" class="campoSemTamanho alturaPadrao larguraCidade" >
                            <option value="A" <c:if test='${idTipoPesquisa == "A"}'>selected</c:if>>Ativas</option>
                            <option value="F" <c:if test='${idTipoPesquisa == "F"}'>selected</c:if>>Ativas e Futuras</option>
                            <option value="T" <c:if test='${idTipoPesquisa == "T"}'>selected</c:if>>Todas</option>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Referência</p>
                    <input type="text" id="dtRef" name="dtRef" class="campoSemTamanho alturaPadrao " style="width:100px" value="${dtRef}">
                </td>
                <td >    
                    <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Consultar" />
                </td>
            </tr>
        </table>
    </form>
    
    <c:if test='<%=request.isUserInRole("3221")%>'>
        <div class="botaoincluirmargem0" style="margin-left:15px;">
            <a href="c?app=3221&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>                
    </c:if>
    <br>               
 
        
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" class="nome-lista">Tipo</th>
            <th scope="col" class="nome-lista">Serviço</th>
            <th scope="col" class="nome-lista">Profissional</th>
            <th scope="col">Dia(s)</th>
            <th scope="col">Período</th>
            <th scope="col">Horário</th>
            <th scope="col">Dt. Abertura</th>
            <th scope="col" >Excluir</th>
        </tr>	
        </thead>
        <tbody>
            <c:forEach var="excecao" items="${excecoes}">
                <tr height="1">
                    <c:choose>
                        <c:when test="${excecao.tipo == 'B'}">
                            <td class="column1" align="left">Bloqueio</td>
                        </c:when>
                        <c:otherwise>
                            <td class="column1" align="left">Abertura</td>
                        </c:otherwise>
                    </c:choose>    
                    
                    <c:choose>
                        <c:when test="${excecao.descricaoServico == null}">
                            <td class="column1" align="left">TODOS</td>
                        </c:when>
                        <c:otherwise>
                            <td class="column1" align="left">${excecao.descricaoServico}</td>
                        </c:otherwise>
                    </c:choose>    
                    <c:choose>
                        <c:when test="${excecao.nomeFuncionario == null}">
                            <td class="column1" align="left">TODOS</td>
                        </c:when>
                        <c:otherwise>
                            <td class="column1" align="left">${excecao.nomeFuncionario}</td>
                        </c:otherwise>
                    </c:choose>    
                    <c:set var="dias" value=""/>
                    <c:if test='${excecao.icSegunda == "S"}'>
                        <c:choose>
                            <c:when test="${empty dias}">
                                <c:set var="dias" value="Segunda-feira"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="dias" value="${dias}<br>Segunda-feira"/>
                            </c:otherwise>
                        </c:choose>    
                    </c:if>                    
                    <c:if test='${excecao.icTerca == "S"}'>
                        <c:choose>
                            <c:when test="${empty dias}">
                                <c:set var="dias" value="Terça-feira"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="dias" value="${dias}<br>Terça-feira"/>
                            </c:otherwise>
                        </c:choose>    
                    </c:if>                    
                    <c:if test='${excecao.icQuarta== "S"}'>
                        <c:choose>
                            <c:when test="${empty dias}">
                                <c:set var="dias" value="Quarta-feira"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="dias" value="${dias}<br>Quarta-feira"/>
                            </c:otherwise>
                        </c:choose>    
                    </c:if>                    
                    <c:if test='${excecao.icQuinta== "S"}'>
                        <c:choose>
                            <c:when test="${empty dias}">
                                <c:set var="dias" value="Quinta-feira"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="dias" value="${dias}<br>Quinta-feira"/>
                            </c:otherwise>
                        </c:choose>    
                    </c:if>                    
                    <c:if test='${excecao.icSexta== "S"}'>
                        <c:choose>
                            <c:when test="${empty dias}">
                                <c:set var="dias" value="Sexta-feira"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="dias" value="${dias}<br>Sexta-feira"/>
                            </c:otherwise>
                        </c:choose>    
                    </c:if>                    
                    <c:if test='${excecao.icSabado== "S"}'>
                        <c:choose>
                            <c:when test="${empty dias}">
                                <c:set var="dias" value="Sabado"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="dias" value="${dias}<br>Sabado"/>
                            </c:otherwise>
                        </c:choose>    
                    </c:if>                    
                    <c:if test='${excecao.icDomingo== "S"}'>
                        <c:choose>
                            <c:when test="${empty dias}">
                                <c:set var="dias" value="Domnigo"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="dias" value="${dias}<br>Domingo"/>
                            </c:otherwise>
                        </c:choose>    
                    </c:if>                    
                                                
                    
                    <td class="column1" align="center">${dias}</td>
                    <fmt:formatDate var="dtInicio" value="${excecao.dtInicio}" pattern="dd/MM/yyyy" />
                    <fmt:formatDate var="dtFim" value="${excecao.dtFim}" pattern="dd/MM/yyyy" />
                    <td class="column1" align="center">${dtInicio} a ${dtFim}</td>
                    <td class="column1" align="center">${fn:substring(excecao.hhInicio,0,2)}:${fn:substring(excecao.hhInicio,2,4)} às ${fn:substring(excecao.hhFim,0,2)}:${fn:substring(excecao.hhFim,2,4)}</td>
                    
                    <fmt:formatDate var="dtAbertura" value="${excecao.dtAbertura}" pattern="dd/MM/yyyy HH:mm:ss" />
                    <td class="column1" align="center">${dtAbertura}</td>

                    <c:if test='<%=request.isUserInRole("3223")%>'>
                        <td class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão do bloqueio selecionado?')) window.location.href='c?app=3223&id=${excecao.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </td>
                    </c:if>
                </tr>	
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
