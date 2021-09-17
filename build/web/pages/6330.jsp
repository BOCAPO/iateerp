<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].valor.value == ''){
            alert('O campo valor é de preenchimento obrigatório!');
            return;
        }
        var valor = parseFloat($('#valor').val().replace('.', '').replace(',', '.'));
        if (valor==0||isNaN(valor)){
            alert('Informe corretamente o valor do combustivel!');
            mostraBotoes();
            return false;
        }
        

        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menuCaixa.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "6331"}'>
        <div id="titulo-subnav">Inclusão de Combustível</div>
    </c:if>
    <c:if test='${app == "6332"}'>
        <div id="titulo-subnav">Alteração de Combustível</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="id" value="${combustivel.id}"/>

        <p class="legendaCodigo">Descrição:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" value="${combustivel.descricao}" />
        
        <p class="legendaCodigo">Valor:</p>
        <input type="text" name="valor" id="valor" maxlength="10" class="campoSemTamanho alturaPadrao" value="<fmt:formatNumber value="${combustivel.valor}" pattern="#0.00000000"/>" />

        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=6330';" value=" " />

    </form>
</body>
</html>

