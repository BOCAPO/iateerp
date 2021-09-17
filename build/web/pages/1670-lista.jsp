<%@include file="head.jsp"%>

    <body class="internas">
        
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
                
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Embarcações do Iate</div>
    <div class="divisoria"></div>
    <br>

    <c:if test='<%=request.isUserInRole("1671")%>'>
    <div class="botaoincluirgeral">
        <a href="c?app=1671&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a>
    </div>
    </c:if>
    <a href="c?app=1670&acao=imprimir" style="padding-left:140px "><img src="imagens/btn-imprimir.png" width="100" height="25" /></a>

    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">Barco</th>
                <th scope="col">Cat. Náutica</th>
                <th scope="col">Vaga</th>
                <th scope="col">Nº Pés</th>
                <th scope="col">Alterar</th>
                <th scope="col">Excluir</th>
            </tr>
        </thead>
            
        <c:forEach var="barco" items="${barcos}">
            <tr>
                <td class="column1">
                    <a href="c?app=1670&acao=detalhes&idBarco=${barco.id}">
                        ${barco.nome}
                    </a>
                </td>    
                <td class="column1" align="center">${barco.categoriaNautica.descricao}</td>
                <td class="column1" align="center">${barco.tipoVaga.descricao}</td>
                <td class="column1" align="center">${barco.pes}</td>

                <td align="center">    
                <c:if test='<%=request.isUserInRole("1672")%>'>
                    <a href="c?app=1672&acao=showForm&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                </c:if>
                </td>
                <td align="center">
                <c:if test='<%=request.isUserInRole("1673")%>'>
                    <a href="javascript: if(confirm('Confirma a exclusão do barco?')) window.location.href='c?app=1673&idBarco=${barco.id}&acao=excluir'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
    
</body>
</html>
