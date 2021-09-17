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
    <c:if test='${app == "1041"}'>
        <div id="titulo-subnav">Inclus�o de Cargo</div>
    </c:if>
    <c:if test='${app == "1042"}'>
        <div id="titulo-subnav">Altera��o de Cargo</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idCargo" value="${cargo.id}"/>

        <p class="legendaCodigo">Descri��o:</p>
        <input type="text" name="descricao" maxlength="25" class="campoDescricao" value="${cargo.descricao}" />

        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1040';" value=" " />
                        
    </form>
</body>
</html>
