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
        <div id="titulo-subnav">Local da Academia</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("3201")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=3201&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Descri??o</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="local" items="${lista}">
                <tr height="1">
                    <th class="column1" align="left">${local.descricao}</th>

                    <c:if test='<%=request.isUserInRole("3202")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=3202&acao=showForm&id=${local.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("3203")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclus?o do registro selecionado?')) window.location.href='c?app=3203&id=${local.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
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
