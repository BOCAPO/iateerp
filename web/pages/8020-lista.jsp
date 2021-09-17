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
        <div id="titulo-subnav">Grupos</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("8021")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=8021&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>                
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col" >Aplicações</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
                <th scope="col" >Usuário</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="grupo" items="${grupos}">

                <tr height="1">
                    <th class="column1" align="left">${grupo.descricao}</th>

                    <th class="column1" align="center">
                        <a href="c?app=8020&acao=detalhar&idGrupo=${grupo.id}"><img src="imagens/icones/inclusao-titular-10.png"/></a>
                    </th>
                    
                    <c:if test='<%=request.isUserInRole("8022")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=8022&acao=showFormRenomear&idGrupo=${grupo.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("8023")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão do grupo selecionado?')) window.location.href='c?app=8023&idGrupo=${grupo.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </th>
                    </c:if>
                        
                    <th class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("8024")%>'>
                            <a href="c?app=8024&idGrupo=${grupo.id}"><img src="imagens/icones/inclusao-titular-01.png"/></a>
                        </c:if>
                    </th>

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

