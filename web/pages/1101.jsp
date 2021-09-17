<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">
    function selecionar(p){
        for(i = 0; i < document.forms[0].idModalidadeEsportiva.length; i++){
            document.forms[0].idModalidadeEsportiva[i].checked = p;
        }            
    }    

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <form method="GET" action="c">
    <input type="hidden" name="app" value="1105">
    <input type="hidden" name="acao" value="gravar">
    <input type="hidden" name="matricula" value="${socio.matricula}">
    <input type="hidden" name="seqDependente" value="${socio.seqDependente}">
    <input type="hidden" name="idCategoria" value="${socio.idCategoria}">
    <input type="hidden" name="alerta" value="${alerta}">

    <p class="legendaCodigo">Modalidades Esportivas</p>
    <div class="recuoPadrao" style="overflow:auto;height:200px;width:500px;">
        <c:forEach var="modalidade" items="${modalidades}">
            <input type="checkbox" name="idModalidadeEsportiva" value="${modalidade.id}" <c:if test="${socio.modalidadesEsportivas[modalidade.id] != null}">checked</c:if>>${modalidade.descricao}<br>
        </c:forEach>
    </div>
    <br>
    
    <input type="submit" class="botaoatualizar" value=" " />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${socio.matricula}&categoria=${socio.idCategoria}';" value=" " />    
    
</form>

</body>
</html>
