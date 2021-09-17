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
        <div id="titulo-subnav">Cartão Náutico</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("2111")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=2111&acao=showForm&idEvento=${idEvento}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col" align="left">Categoria</th>
                <th scope="col" align="left">Clube</th>
                <th scope="col" align="center">UF</th>
                <th scope="col" align="left">Numeracao</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
                <th scope="col" >Imprimir</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="cartao" items="${lista}">
                <tr height="1">
                    <th class="column1" align="left">${cartao.nome}</th>
                    <th class="column1" align="left">${cartao.categoria}</th>
                    <th class="column1" align="left">${cartao.clube}</th>
                    <th class="column1" align="center">${cartao.uf}</th>
                    <th class="column1" align="left">${cartao.numeracao}</th>

                    <c:if test='<%=request.isUserInRole("2112")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=2112&idEvento=${idEvento}&acao=showForm&nrCartao=${cartao.nrCartao}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("2113")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão do registro selecionado?')) window.location.href='c?app=2113&nrCartao=${cartao.nrCartao}&idEvento=${cartao.idEvento}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </th>
                    </c:if>
                        
                    <c:if test='<%=request.isUserInRole("2114")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=2114&nrCartao=${cartao.nrCartao}"><img src="imagens/icones/inclusao-titular-07.png"/></a>
                        </th>
                    </c:if>

                </tr>	

            </c:forEach>

            </tbody>
        </table>

        <div class="clear"></div>

        <br />
        <br />            

        <div id="rodape">
            <div id="copyright"><img src="imagens/copyright.png" /></div>
        </div>
    
</body>
</html>
