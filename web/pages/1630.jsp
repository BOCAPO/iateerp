<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">    
    
    function validarForm(){
        var k = 0;
        for(var i = 0; i < document.forms[0].cargos.length; i++){
            if(document.forms[0].cargos[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Nenhum cargo foi selecionado.');
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Cargos Especiais</div>
    <div class="divisoria"></div>

    <form method="POST" action="c">
	<input type="hidden" name="app" value="1630">
	<input type="hidden" name="acao" value="visualizar">
        
        <p class="legendaCodigo">Cargos Especiais:</p>
        <div class="recuoPadrao" style="overflow:auto;height:350px;width:300px;">
            <c:forEach var="cargo" items="${cargos}">
                <input type="checkbox" name="cargos" value="${cargo.id}">${cargo.descricao}<br>
            </c:forEach>
        </div>
        <br>

        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        

    </form>

</body>
</html>
