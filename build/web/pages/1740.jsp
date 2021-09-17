<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Autorização de Dependentes para Utilização de Taxas</div>
    <div class="divisoria"></div>
    
    
    <form method="POST" action="c">
        <input type="hidden" name="app" value="1740">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="matricula" value="${dependente.matricula}">
        <input type="hidden" name="seqDependente" value="${dependente.seqDependente}">
        <input type="hidden" name="idCategoria" value="${dependente.idCategoria}">
        <input type="hidden" name="alerta" value="${alerta}">

        <p class="legendaCodigo">Taxas<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('idTaxaIndividual')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
        <div class="recuoPadrao" style="overflow:auto;height:350px;width:500px;">
            <c:forEach var="taxa" items="${taxas}">
                <input type="checkbox" name="idTaxaIndividual" value="${taxa.id}" <c:if test="${dependente.taxasIndividuais[taxa.id] != null}">checked</c:if>>${taxa.descricao}<br>
            </c:forEach>
        </div>
        <br>

        <input type="submit" class="botaoatualizar" value=" " />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${dependente.matricula}&categoria=${dependente.idCategoria}';" value=" " />

    </form>

</body>
</html>
