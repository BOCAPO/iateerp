<%@include file="head.jsp"%>

<body class="internas">

    <%@include file="menuAcesso.jsp"%>

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
    <div id="titulo-subnav">Locais de Acesso</div>
    <div class="divisoria"></div>

    <br>

    <c:if test='<%=request.isUserInRole("7011")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=7011&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>

    <br>                
    <table id="tabela">
       <thead>
        <tr class="odd">
            <th>Descrição</th>
            <th>Estação</th>
            <th>Req. Ex. Méd.</th>
            <th>Acesso Conv. Util.</th>
            <th>Só Carro</th>
            <th>Mostra Placa</th>
            <th>Mostra Quant.</th>
            <th>Alterar</th>
            <th>Excluir</th>
        </tr>
       </thead>
        <c:forEach var="item" items="${lista}">
            <tr  height="1">
                <td class="column1" align="left">${item.descricao}</td>
                <td class="column1" align="center">${item.estacao}</td>
                <td class="column1" align="center">${item.requerExame}</td>
                <td class="column1" align="center">${item.convUtil}</td>
                <td class="column1" align="center">${item.soCarro}</td>
                <td class="column1" align="center">${item.motraPlaca}</td>
                <td class="column1" align="center">${item.mostraQuantidade}</td>
                <c:if test='<%=request.isUserInRole("7012")%>'>
                    <td class="column1" align="center">
                        <a href="c?app=7012&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                    </td>
                </c:if>
                
                <c:if test='<%=request.isUserInRole("7013")%>'>
                    <td class="column1" align="center">    
                        <a href="javascript: if(confirm('Confirma Exclusão de ${item.descricao} da tabela de Local de Acesso?')) window.location.href='c?app=7013&id=${item.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </td>
                </c:if>

            </tr>
        </c:forEach>
    </table>
</body>
</html>
