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
    <div id="titulo-subnav">Tipo de Evento de Acesso</div>
    <div class="divisoria"></div>

    <br>

    <c:if test='<%=request.isUserInRole("7021")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=7021&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>

    <br>                
    <table id="tabela">
       <thead>
        <tr class="odd">
            <th>Descrição</th>
            <th>Situação</th>
            <th>Status</th>
            <th>Dt. Início</th>
            <th>Dt. Fim</th>
            <th>Alterar</th>
            <th>Excluir</th>
        </tr>
       </thead>
        <c:forEach var="item" items="${lista}">
            <tr  height="1">
                <td class="column1" align="left">${item.descricao}</td>
                <td class="column1" align="center">${item.situacao}</td>
                <td class="column1" align="center">${item.status}</td>
                <fmt:formatDate var="dtInicio" value="${item.dtInicio}" pattern="dd/MM/yyyy" />
                <td class="column1" align="center">${dtInicio}</td>
                
                <fmt:formatDate var="dtFim" value="${item.dtFim}" pattern="dd/MM/yyyy" />
                <td class="column1" align="center">${dtFim}</td>
                <c:if test='<%=request.isUserInRole("7022")%>'>
                    <td class="column1" align="center">
                        <a href="c?app=7022&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                    </td>
                </c:if>
                
                <c:if test='<%=request.isUserInRole("7023")%>'>
                    <td class="column1" align="center">    
                        <a href="javascript: if(confirm('Confirma Exclusão de ${item.descricao} da tabela de Itens de Aluguel?')) window.location.href='c?app=7023&id=${item.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </td>
                </c:if>

            </tr>
        </c:forEach>
    </table>
</body>
</html>
