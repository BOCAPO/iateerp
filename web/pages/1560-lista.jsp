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
    <div id="titulo-subnav">Eventos</div>
    <div class="divisoria"></div>

    <br>

    <c:if test='<%=request.isUserInRole("1561")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=1561&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>

    <br>                
    <table id="tabela">
       <thead>
        <tr class="odd">
            <th>Descri��o</th>
            <th>Anima��o</th>
            <th>Local</th>
            <th>Data/Hora</th>
            <th>Qt. Mesas</th>
            <th>Qt. Cadeiras</th>
            <th>Qt. Ingressos</th>
            <th>Alterar</th>
            <th>Excluir</th>
        </tr>
       </thead>
        <c:forEach var="item" items="${lista}">
            <tr  height="1">
                <td class="column1" align="left">${item.descricao}</td>
                <td class="column1" align="left">${item.animacao}</td>
                <td class="column1" align="left">${item.local}</td>

                <fmt:formatDate var="data" value="${item.data}" pattern="dd/MM/yyyy" />
                <td class="column1" align="center">${data} - ${item.hora}</td>
                <td class="column1" align="center">${item.qtMesas}</td>
                <td class="column1" align="center">${item.qtCadeiras}</td>
                <td class="column1" align="center">${item.qtIngressos}</td>

                <c:if test='<%=request.isUserInRole("1562")%>'>
                    <td class="column1" align="center">
                        <a href="c?app=1562&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                    </td>
                </c:if>
                
                <c:if test='<%=request.isUserInRole("1563")%>'>
                    <td class="column1" align="center">    
                        <a href="javascript: if(confirm('Confirma Exclus�o de ${item.descricao} da tabela de Eventos?')) window.location.href='c?app=1563&id=${item.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </td>
                </c:if>

            </tr>
        </c:forEach>
    </table>
</body>
</html>
