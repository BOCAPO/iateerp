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
    <div id="titulo-subnav">Ocorrência</div>
    <div class="divisoria"></div>

    <br>

    <c:if test='<%=request.isUserInRole("1171")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=1171&acao=showForm&matricula=${socio.matricula}&seqDependente=${socio.seqDependente}&idCategoria=${socio.idCategoria}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>
    
    <br>                

    <table ID="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">Tipo</th>
                <th scope="col">Dt. Início</th>
                <th scope="col">Dt. Fim</th>
                <th scope="col">Descrição</th>
                <th scope="col">Excluir</th>
            </tr>
        </thead>
            
        <c:forEach var="ocorrencia" items="${ocorrencias}">
            <tr>
                <td class="column1">${ocorrencia.tipo.descricao}</td>
                <fmt:formatDate var="dataInicio" value="${ocorrencia.dataInicio}" pattern="dd/MM/yyyy"/>
                <td class="column1" align="center">${dataInicio}</td>
                <fmt:formatDate var="dataFim" value="${ocorrencia.dataFim}" pattern="dd/MM/yyyy"/>
                <td class="column1" align="center" >${dataFim}</td>                
                <td class="column1">${ocorrencia.descricao}</td> 
                <td align="center">
                <c:if test='<%=request.isUserInRole("1173")%>'>
                    <a href="javascript: if(confirm('Confirma a exclusão da ocorrência?')) window.location.href='c?app=1173&idOcorrencia=${ocorrencia.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${socio.matricula}&categoria=${socio.idCategoria}';" value=" " />
    
</body>
</html>
