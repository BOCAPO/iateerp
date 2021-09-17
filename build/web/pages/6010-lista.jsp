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
    <div id="titulo-subnav">Transação</div>
    <div class="divisoria"></div>

    <br>

    <c:if test='<%=request.isUserInRole("6011")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=6011&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>

    <br>                
    <table id="tabela">
       <thead>
        <tr class="odd">
            <th>Descrição</th>
            <th>Tipo</th>
            <th>Vr. Padrão</th>
            <th>Taxa</th>
            <th>Alterar</th>
        </tr>
       </thead>
        <c:forEach var="item" items="${lista}">
            <tr  height="1">
                <td class="column1" align="left">${item.descricao}</td>
                <td class="column1" align="center">
                    <c:choose>
                       <c:when test='${item.tipo == "C"}'>
                            Crédito
                       </c:when>
                       <c:when test='${item.tipo == "R"}'>
                            Carnê
                       </c:when>
                       <c:when test='${item.tipo == "S"}'>
                            Carga pré-pago Sócio
                       </c:when>
                       <c:when test='${item.tipo == "F"}'>
                            Carga pré-pago Funcionário
                       </c:when>
                    </c:choose>                         
                </td>
                <fmt:formatNumber var="valPadrao"  value="${item.valPadrao}" pattern="#0.00"/>
                <td class="column1" align="right">${valPadrao}</td>
                <td class="column1" align="left">${item.deTaxa}</td>
                <c:if test='<%=request.isUserInRole("6012")%>'>
                    <td class="column1" align="center">
                        <a href="c?app=6012&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                    </td>
                </c:if>
                
            </tr>
        </c:forEach>
    </table>
</body>
</html>
