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
        <div id="titulo-subnav">Tipos de Dependentes</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("1021")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=1021&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Descrição</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="tipodependente" items="${tipodependentes}">
                <tr height="1">
                    <th class="column1" align="left">${tipodependente.descricao}</th>

                    <c:if test='<%=request.isUserInRole("1022")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=1022&acao=showForm&idTipoDependente=${tipodependente.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("1023")%>'>
                        <th class="column1" align="center">
                            
                            <a href="javascript: if(confirm('Confirma a exclusão do registro selecionado?')) window.location.href='c?app=1023&idTipoDependente=${tipodependente.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
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
