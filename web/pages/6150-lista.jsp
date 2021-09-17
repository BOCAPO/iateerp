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
    <div id="titulo-subnav">Centros de Custo</div>
    <div class="divisoria"></div>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="6150">
        
        
        <c:if test='<%=request.isUserInRole("6151")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=6151&acao=showForm&transacao=${transacao}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>
        <br>
        <table id="tabela">
           <thead>
            <tr class="odd">
                <th>Descrição</th>
                <th>Taxa</th>
                <th>Alterar</th>
                <th>Usuários</th>
            </tr>
           </thead>
            <c:forEach var="item" items="${lista}">
                <tr  height="1">
                    <td class="column1" align="left">${item.descricao}</td>
                    <td class="column1" align="left">${item.deTaxa}</td>
                    <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("6152")%>'>
                                <a href="c?app=6152&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </c:if>
                    </td>
                    <td class="column1" align="center">
                        <a href="c?app=6150&acao=usuario&id=${item.id}"><img src="imagens/icones/inclusao-titular-01.png"/></a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </form>
</body>
</html>
