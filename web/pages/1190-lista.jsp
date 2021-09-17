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
        <div id="titulo-subnav">Cargos Especiais</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("1191")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=1191&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>                
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Descrição</th>
                <th scope="col" class="nome-lista">Qt. Max.</th>
                <th scope="col" class="nome-lista">Gestão</th>
                <th scope="col" class="nome-lista">Tipo</th>
                <th scope="col" class="nome-lista">Categoria</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="ce" items="${cargosEspeciais}">

                <tr height="1">
                    <td class="column1" align="left">${ce.descricao}</td>
                    <td class="column1" align="left">${ce.qtMax}</td>
                    <td class="column1" align="left">${ce.gestao}</td>
                    <td class="column1" align="left">${ce.descricaoTipoCargo}</td>
                    <td class="column1" align="left">${ce.categoria}</td>
                    
                    <c:if test='<%=request.isUserInRole("1192")%>'>
                        <td class="column1" align="center">
                            <a href="c?app=1192&acao=showForm&idCargoEspecial=${ce.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </td>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("1193")%>'>
                        <td class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão do cargo especial selecionado?')) window.location.href='c?app=1193&idCargoEspecial=${ce.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </td>
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

