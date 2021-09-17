<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].id.value == ''){
            alert('O campo c�digo � de preenchimento obrigat�rio!');
            return;
        }

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
    <c:if test='${app == "1761"}'>
        <div id="titulo-subnav">Inclus�o de Categoria N�utica</div>
    </c:if>
    <c:if test='${app == "1762"}'>
        <div id="titulo-subnav">Altera��o de Categoria N�utica</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idTipoVagaBarco" value="${tipovagabarco.id}"/>

        <c:if test='${app == "1761"}'>
            <p class="legendaCodigo">C�digo:</p>
            <input type="text" name="id" maxlength="2" class="campoCodigo" value="${tipovagabarco.id}" />
        </c:if>
        <c:if test='${app == "1762"}'>
            <p class="legendaCodigo">C�digo:</p>
            <input type="text" name="id" disabled="true" class="campoCodigo" value="${tipovagabarco.id}" />
        </c:if>

        <p class="legendaCodigo">Descri��o:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" value="${tipovagabarco.descricao}" />

        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1760';" value=" " />


    </form>
</body>
</html>
