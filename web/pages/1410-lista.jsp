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
    <div id="titulo-subnav">Cadastro de Taxas</div>
    <div class="divisoria"></div>

    <br>

    <c:if test='<%=request.isUserInRole("1411")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=1411&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>

    <br>                
    <table id="tabela">
       <thead>
        <tr class="odd">
            <th>Descrição</th>
            <th>Tipo</th>
            <th>Receita</th>
            <th>Alterar</th>
            <th>Excluir</th>
            <th>Usuários Autorizados</th>
            <th>Juros</th>
        </tr>
       </thead>
        <c:forEach var="item" items="${lista}">
            <tr  height="1">
                <td class="column1" align="left">${item.descricao}</td>
                <td class="column1" align="center">
                    <c:if test='${item.tipo=="E"}'>
                        Específica
                    </c:if>
                    <c:if test='${item.tipo=="I"}'>
                        Individual
                    </c:if>
                    <c:if test='${item.tipo=="G"}'>
                        Geral
                    </c:if>
                    <c:if test='${item.tipo=="C"}'>
                        Crédito
                    </c:if>
                </td>
                <td class="column1" align="center">
                    <c:if test='${item.receita=="I"}'>
                        Crédito para o Iate
                    </c:if>
                    <c:if test='${item.receita=="C"}'>
                        Repasse para Concessionário
                    </c:if>
                </td>
                <c:if test='<%=request.isUserInRole("1412")%>'>
                    <td class="column1" align="center">
                        <a href="c?app=1412&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                    </td>
                </c:if>
                
                <c:if test='<%=request.isUserInRole("1413")%>'>
                    <td class="column1" align="center">    
                        <a href="javascript: if(confirm('Confirma Exclusão de ${item.descricao}?')) window.location.href='c?app=1413&id=${item.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </td>
                </c:if>

                <td class="column1" align="center">    
                    <c:if test='${item.tipo=="I"}'>
                        <a href="c?app=1410&acao=usuario&idTaxa=${item.id}"><img src="imagens/icones/inclusao-titular-01.png"/></a>
                    </c:if>
                </td>
                <td class="column1" align="center">    
                    <a href="c?app=1415&acao=showForm&idTaxa=${item.id}"><img src="imagens/icones/inclusao-titular-15.png"/></a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
