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
    <div id="titulo-subnav">Declaracao de Quitação</div>
    <div class="divisoria"></div>

    <br>

    <br>                
    <table id="tabela">
       <thead>
        <tr class="odd">
            <th>Descrição</th>
            <th>Alterar</th>
        </tr>
       </thead>
        <c:forEach var="item" items="${lista}">
            <tr  height="1">
                <td class="column1" align="left">${item.descricao}</td>

                <td class="column1" align="center">
                    <a href="c?app=2250&acao=alterar&descricao=${item.descricao}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                </td>
                
            </tr>
        </c:forEach>
    </table>
</body>
</html>
