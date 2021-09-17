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
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "2141"}'>
        <div id="titulo-subnav">Inclusão na Lista Negra</div>
    </c:if>
    <c:if test='${app == "2142"}'>
        <div id="titulo-subnav">Alteração na Lista Negra</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idListaNegra" value="${listanegra.id}"/>

        <p class="legendaCodigo">Descrição:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" value="${listanegra.descricao}" />

        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2140';" value=" " />

    </form>
</body>
</html>

