<%@include file="head.jsp"%>

<body class="internas">

    <%@include file="menu.jsp"%>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="8022">
	<input type="hidden" name="acao" value="adicionarPermissoes">
	<input type="hidden" name="idGrupo" value="${grupo.id}">

        
        <p class="legendaCodigo">Aplicações</p>
        <div class="recuoPadrao" style="overflow:auto;height:350px;width:500px;">
            <c:forEach var="permissao" items="${grupo.permissoesDisponiveis}">
                    <input type="checkbox" name="permissoes" value="${permissao.id}">${permissao.id} - ${permissao.descricao}<br>
            </c:forEach>
        </div>
        <br>

        <input type="submit" class="botaoatualizar" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=8020&acao=detalhar&idGrupo=${grupo.id}';" value=" " />
        

    </form>

</body>
</html>
