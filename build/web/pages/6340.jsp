<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }

        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menuCaixa.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "6341"}'>
        <div id="titulo-subnav">Inclusão de Formas de Pagamento no Caixa</div>
    </c:if>
    <c:if test='${app == "6342"}'>
        <div id="titulo-subnav">Alteração de Formas de Pagamento no Caixa</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="id" value="${item.id}"/>

        <p class="legendaCodigo">Descrição:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" value="${item.descricao}" />

        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=6340';" value=" " />

    </form>
</body>
</html>

