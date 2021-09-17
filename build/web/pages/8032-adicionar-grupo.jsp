<%@include file="head.jsp"%>

<body class="internas">

    <%@include file="menu.jsp"%>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="8032">
	<input type="hidden" name="acao" value="adicionarGrupos">
        <input type="hidden" name="login" value="${usuario.login}"/>

        
        <p class="legendaCodigo">Grupos</p>
        <div class="recuoPadrao" style="overflow:auto;height:350px;width:500px;">
            <c:forEach var="grupo" items="${usuario.gruposDisponiveis}">
                    <input type="checkbox" name="grupos" value="${grupo.id}">${grupo.descricao}<br>
            </c:forEach>
        </div>
        <br>

        <input type="submit" class="botaoatualizar" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=8030&acao=detalhar&login=${usuario.login}';" value=" " />
        

    </form>

</body>
</html>
