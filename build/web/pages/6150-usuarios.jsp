<%@include file="head.jsp"%>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Usu?rios Autorizados</div>
    <div class="divisoria"></div>
        
    <form method="POST" action="c">
    <input type="hidden" name="app" value="6150">
    <input type="hidden" name="acao" value="gravarUsuario">
    <input type="hidden" name="taxa" value="${cc.id}">

    <p class="legendaCodigo">Usu?rios</p>
    <div class="recuoPadrao" style="overflow:auto;height:350px;width:300px;">
        <c:forEach var="usuario" items="${usuarios}">
            <input type="checkbox" name="idUsuario" <c:if test="${usuario.centrosCustos[cc.id] != null}">checked</c:if> value="${usuario.login}" >
            ${usuario.login}<br>
        </c:forEach>
    </div>
    <br>
    
    <input type="submit" class="botaoatualizar" value=" " />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=6150'" value=" " />
    
</form>

</body>
</html>
