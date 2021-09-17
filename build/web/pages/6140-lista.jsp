<%@include file="head.jsp"%>

<body class="internas">

    <%@include file="menuCaixa.jsp"%>

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
    <div id="titulo-subnav">Produtos e Serviços</div>
    <div class="divisoria"></div>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="6140">
        
        
        <p class="legendaCodigo MargemSuperior0">Centro de Custos:</p>
        <div class="selectheightnovo">
            <select name="transacao" class="campoSemTamanho alturaPadrao" style="width:180px;" onchange="submit()">
                <c:forEach var="trans" items="${transacoes}">
                    <option value="${trans.id}" <c:if test="${transacao eq trans.id}">selected</c:if>>${trans.descricao}</option>
                </c:forEach>
            </select>
        </div>        
        <br>
        <c:if test='<%=request.isUserInRole("6141")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=6141&acao=showForm&transacao=${transacao}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>
        <br>
        <table id="tabela">
           <thead>
            <tr class="odd">
                <th>Descrição</th>
                <th>Vr. Padrão</th>
                <th>Tipo</th>
                <th>Situação</th>
                <th>Crédito Iate</th>
                <th>Estoque Atual</th>
                <th>Estoque Mínimo</th>
                <th>Alterar</th>
            </tr>
           </thead>
            <c:forEach var="item" items="${lista}">
                <tr  height="1">
                    <td class="column1" align="left">${item.descricao}</td>
                    <fmt:formatNumber var="valPadrao"  value="${item.valPadrao}" pattern="#0.00"/>
                    <td class="column1" align="right">${valPadrao}</td>
                    <td class="column1" align="center">${item.tipo}</td>
                    <td class="column1" align="center">${item.ativo}</td>
                    <td class="column1" align="center">${item.credito}</td>
                    <td class="column1" align="center">${item.estoqueAtual}</td>
                    <td class="column1" align="center">${item.estoqueMinimo}</td>
                    <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("6142")%>'>
                                <a href="c?app=6142&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </form>
</body>
</html>
