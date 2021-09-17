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
    <div id="titulo-subnav">Carta de Cobrança</div>
    <div class="divisoria"></div>

    <br>

    <c:if test='<%=request.isUserInRole("1201")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=1201&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>

    <br>                
    <table id="tabela">
       <thead>
        <tr class="odd">
            <th>Descrição</th>
            <th>Alterar</th>
            <th>Excluir</th>
        </tr>
       </thead>
        <c:forEach var="item" items="${lista}">
            <tr  height="1">
                <td class="column1" align="left">${item.descricao}</td>

                <c:if test='<%=request.isUserInRole("1202")%>'>
                    <td class="column1" align="center">
                        <a href="c?app=1202&acao=showForm&descricao=${item.descricao}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                    </td>
                </c:if>
                
                <c:if test='<%=request.isUserInRole("1203")%>'>
                    <td class="column1" align="center">    
                        <a href="javascript: if(confirm('Confirma Exclusão de ${item.descricao} da tabela de Carta da Cobrança?')) window.location.href='c?app=1203&descricao=${item.descricao}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </td>
                </c:if>

            </tr>
        </c:forEach>
    </table>
</body>
</html>
