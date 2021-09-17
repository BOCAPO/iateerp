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
    <div id="titulo-subnav">Itens de Aluguel</div>
    <div class="divisoria"></div>

    <br>

    <c:if test='<%=request.isUserInRole("1391")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=1391&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>

    <br>                
    <table id="tabela">
       <thead>
        <tr class="odd">
            <th>Descrição</th>
            <th>Valor Sócio</th>
            <th>Valor Não Sócio</th>
            <th>Qt. Estoque</th>
            <th>Alterar</th>
            <th>Excluir</th>
        </tr>
       </thead>
        <c:forEach var="item" items="${lista}">
            <tr  height="1">
                <td class="column1" align="left">${item.descricao}</td>
                <td class="column1" align="right"><fmt:formatNumber value="${item.valorSocio}" pattern="#0.00"/></td>
                <td class="column1" align="right"><fmt:formatNumber value="${item.valorNaoSocio}" pattern="#0.00"/></td>
                <td class="column1" align="center">${item.quantidadeEstoque}</td>
                <c:if test='<%=request.isUserInRole("1392")%>'>
                    <td class="column1" align="center">
                        <a href="c?app=1392&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                    </td>
                </c:if>
                
                <c:if test='<%=request.isUserInRole("1393")%>'>
                    <td class="column1" align="center">    
                        <a href="javascript: if(confirm('Confirma Exclusão de ${item.descricao} da tabela de Itens de Aluguel?')) window.location.href='c?app=1393&id=${item.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </td>
                </c:if>

            </tr>
        </c:forEach>
    </table>
</body>
</html>
