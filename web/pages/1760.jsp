<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].id.value == ''){
            alert('O campo código é de preenchimento obrigatório!');
            return;
        }

        if(document.forms[0].descricao.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }

        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "1761"}'>
        <div id="titulo-subnav">Inclusão de Categoria Náutica</div>
    </c:if>
    <c:if test='${app == "1762"}'>
        <div id="titulo-subnav">Alteração de Categoria Náutica</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idTipoVagaBarco" value="${tipovagabarco.id}"/>

        <c:if test='${app == "1761"}'>
            <p class="legendaCodigo">Código:</p>
            <input type="text" name="id" maxlength="2" class="campoCodigo" value="${tipovagabarco.id}" />
        </c:if>
        <c:if test='${app == "1762"}'>
            <p class="legendaCodigo">Código:</p>
            <input type="text" name="id" disabled="true" class="campoCodigo" value="${tipovagabarco.id}" />
        </c:if>

        <p class="legendaCodigo">Descrição:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" value="${tipovagabarco.descricao}" />

        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1760';" value=" " />


    </form>
</body>
</html>
