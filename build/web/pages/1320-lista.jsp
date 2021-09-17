
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
    </script>  


    <div class="divisoria"></div>
    <div id="titulo-subnav">Cadastro de Armários</div>
    <div class="divisoria"></div>

    <br>
    <form method="POST" action="c">
    <input type="hidden" name="app" value="${app}">
    <input type="hidden" name="acao" value="gravar">
        <div class="botaoincluirgeral">
            <p class="legendaSemMargem">Tipo</p>
            <select name="tipoArmario" class="campoSemTamanho alturaPadrao larguraComboCategoria tiraMargem" onchange="document.forms[0].submit()">
                <c:forEach var="tipo" items="${tipos}">
                    <option value="${tipo.id}" <c:if test='${tipoArmario == tipo.id}'>selected</c:if>>${tipo.descricao}</option>
                </c:forEach>
            </select>
        </div>
        <br><br><br>

        <c:if test='<%=request.isUserInRole("1321")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=1321&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>


        <br>


    </form>

    <table id="tabela">
        <thead>
        <tr class="odd">
            <th scope="col" >Número</th>
            <th scope="col" >Data</th>
            <th scope="col" >Sócio</th>
            <th scope="col" >Excluir</th>
            <th scope="col" >Vincular</th>
            <th scope="col" >Desvincular</th>
        </tr>	
        </thead>

        <c:forEach var="armario" items="${armarios}">
            <tr>

                <fmt:formatNumber pattern="000" var="numero" value="${armario.numero}"/>
                <td class="column1" align="center" >${numero}</td>

                <fmt:formatDate var="dataInclusao" value="${armario.dataInclusao}" pattern="dd/MM/yyyy"/>
                <td class="column1" align="center">${dataInclusao}</td>
                <td class="column1">
                    <c:choose>
                        <c:when test="${armario.socio != null}">
                            <fmt:formatNumber pattern="00" var="idCategoria" value="${armario.socio.idCategoria}"/>
                            <fmt:formatNumber pattern="0000" var="matricula" value="${armario.socio.matricula}"/>
                            ${idCategoria}/${matricula} - ${armario.socio.nome}
                        </c:when>
                    </c:choose>
                </td>

                <td class="column1" align="center">
                    <c:if test='<%=request.isUserInRole("1322")%>'>
                        <a href="javascript: if(confirm('Confirma Exclusão do Armário')) window.location.href='c?app=1322&numeroArmario=${armario.numero}&tipoArmario=${armario.tipo}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </c:if>        
                </td>

                <td align="center">
                    <c:choose>
                        <c:when test="${armario.socio == null}">
                            <c:if test='<%=request.isUserInRole("1323")%>'>
                                <a href="c?app=1323&acao=showForm&numeroArmario=${armario.numero}&tipoArmario=${armario.tipo}">Vincular</a>
                            </c:if>
                        </c:when>
                    </c:choose>
                </td>
                <td align="center">
                    <c:choose>
                        <c:when test="${armario.socio != null}">
                            <c:if test='<%=request.isUserInRole("1324")%>'>
                                <a href="javascript: if(confirm('Confirma a desassociação do Sócio ao Armário selecionado?')) window.location.href='c?app=1324&numeroArmario=${armario.numero}&tipoArmario=${armario.tipo}'">Desvincular</a>
                            </c:if>
                        </c:when>
                    </c:choose>
                </td>
            </tr>        
        </c:forEach>
    </table>
</body>
</html>
