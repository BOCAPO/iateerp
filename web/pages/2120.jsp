<%@include file="head.jsp"%>

<body class="internas">

    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Bloqueio de Taxa</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
        <input type="hidden" name="app" value="2120">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="matricula" value="${titular.socio.matricula}">
        <input type="hidden" name="idCategoria" value="${titular.socio.idCategoria}">
        <input type="hidden" name="alerta" value="${alerta}">

        <p class="legendaCodigo">Taxas</p>
        <div class="recuoPadrao" style="overflow:auto;height:350px;width:500px;">
            <c:forEach var="taxa" items="${taxas}">
                <input type="checkbox" name="idTaxaIndividual" value="${taxa.id}" <c:if test="${titular.taxasIndividuais[taxa.id] != null}">checked</c:if>>${taxa.descricao}<br>
            </c:forEach>
        </div>
        <br>

        <input type="submit" class="botaoatualizar" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${titular.socio.matricula}&categoria=${titular.socio.idCategoria}';" value=" " />
    
    </form>

</body>
</html>
