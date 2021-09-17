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
    <c:if test='${app == "1081"}'>
        <div id="titulo-subnav">Inclusão de Dependência</div>
    </c:if>
    <c:if test='${app == "1082"}'>
        <div id="titulo-subnav">Alteração de Dependência</div>
    </c:if>
    <div class="divisoria"></div>
    
    <form action="c" id="it-bloco01">
        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>
        <input type="hidden" name="idDependencia" value="${dependencia.id}"/>

        <p class="legendaCodigo">Descrição:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" value="${dependencia.descricao}" />


        <p class="legendaCodigo">Texto do comprovante de confirmação de reserva:</p>

        <textarea class="campoSemTamanho" rows="20" cols="150" name="textoComprovante">${dependencia.textoComprovante}</textarea><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1080';" value=" " />

    </form>
</body>
</html>
