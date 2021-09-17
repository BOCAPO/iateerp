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
        <div id="titulo-subnav">Item da Churrasqueira</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("2611")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=2611&acao=showForm&idDependencia=${idDependencia}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Código</th>
                <th scope="col" class="nome-lista">Descrição</th>
                <th scope="col" class="nome-lista">Quant.</th>
                <th scope="col" class="nome-lista">Observação</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="item" items="${lista}">
                <tr height="1"> 
                    <th class="column1" align="left">${item.codigo}</th>
                    <th class="column1" align="left">${item.descricao}</th>
                    <th class="column1" align="left">${item.quantidade}</th>
                    <th class="column1" align="left">${item.observacao}</th>

                    <c:if test='<%=request.isUserInRole("2612")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=2612&acao=showForm&id=${item.id}&idDependencia=${idDependencia}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("2613")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão do registro selecionado?')) window.location.href='c?app=2613&id=${item.id}&idDependencia=${idDependencia}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </th>
                    </c:if>

                </tr>	

            </c:forEach>

            </tbody>
        </table>
        
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1080';" value=" " />
            
        <div class="clear"></div>

        <br />
        <br />            

        <div id="rodape">
            <div id="copyright"><img src="imagens/copyright.png" /></div>
        </div>

    
</body>
</html>
