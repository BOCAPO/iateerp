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
        <div id="titulo-subnav">Liberação Boleto Internet</div>
        <div class="divisoria"></div>
        <br>
                
            <c:if test='<%=request.isUserInRole("2651")%>'>
                <div class="botaoincluirgeral">
                    <a href="c?app=2651&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
                </div>
            </c:if>
        
            <br>                

            <table id="tabela">
                <thead>
                <tr class="odd">
                    <th scope="col" >Data Vencimento</th>
                    <th scope="col" >Data Pagamento</th>
                    <th scope="col" >Alterar</th>
                    <th scope="col" >Excluir</th>
                </tr>	
                </thead>
                <tbody>
                    
                <c:forEach var="item" items="${lista}">
                    <tr height="1">
                        <th class="column1" align="center"><fmt:formatDate value="${item.dtVenc}" pattern="dd/MM/yyyy" /></th>
                        <th class="column1" align="center"><fmt:formatDate value="${item.dtPgto}" pattern="dd/MM/yyyy" /></th>

                        <c:if test='<%=request.isUserInRole("2652")%>'>
                            <th align="center">
                                <a href="c?app=2652&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                            </th>
                        </c:if>
                        <c:if test='<%=request.isUserInRole("2653")%>'>
                            <th align="center">
                                <a href="javascript: if(confirm('Confirma a exclusão do registro selecionado?')) window.location.href='c?app=2653&id=${item.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
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
        
        </div>
    
</body>
</html>
