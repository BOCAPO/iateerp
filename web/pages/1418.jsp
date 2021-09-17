<%@include file="head.jsp"%>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Usuários Autorizados</div>
    <div class="divisoria"></div>
        
    <form method="POST" action="c">
    <input type="hidden" name="app" value="1410">
    <input type="hidden" name="acao" value="gravarUsuario">
    <input type="hidden" name="idTaxa" value="${taxa.id}">

    <p class="legendaCodigo">Usuários</p>
    <div class="recuoPadrao" style="overflow:auto;height:350px;width:300px;">
        <c:forEach var="usuario" items="${usuarios}">
            <input type="checkbox" name="idUsuario" <c:if test="${usuario.taxasIndividuais[taxa.id] != null}">checked</c:if> value="${usuario.login}" >
            ${usuario.login}<br>
        </c:forEach>
    </div>
    <br>
    
    <input type="submit" class="botaoatualizar" value=" " />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1410'" value=" " />
    
</form>

</body>
</html>
