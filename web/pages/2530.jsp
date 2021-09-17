<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('O campo descri��o � de preenchimento obrigat�rio!');
            return;
        }

        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "2531"}'>
        <div id="titulo-subnav">Inclus�o Arm�rio da Academia</div>
    </c:if>
    <c:if test='${app == "2532"}'>
        <div id="titulo-subnav">Altera��o Arm�rio da Academia</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="id" value="${item.id}"/>

        <p class="legendaCodigo">Tipo de Arm�rio</p>
        <select name="idTipoArmario" class="campoSemTamanho" <c:if test='${app == 2532}'>disabled</c:if>>
            <c:forEach var="tipoArmario" items="${tipoArmarios}">
                <option value="${tipoArmario.id}" <c:if test='${item.tipoArmarioAcademia.id == tipoArmario.id}'>selected</c:if>>${tipoArmario.descricao}</option>
            </c:forEach>
        </select> <br>
        
        <p class="legendaCodigo">Descri��o:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" value="${item.descricao}" />

        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2530';" value=" " />

    </form>
</body>
</html>

