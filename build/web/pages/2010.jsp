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
    <c:if test='${app == "2011"}'>
        <div id="titulo-subnav">Inclusão de Tipo de Ocorrência</div>
    </c:if>
    <c:if test='${app == "2012"}'>
        <div id="titulo-subnav">Alteração de Tipo de Ocorrência</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idTipoOcorrencia" value="${tipoocorrencia.id}"/>

        <p class="legendaCodigo">Tipo:</p>
        <select name="tipo" class="campoSemTamanho" >
            <option value="SP" <c:if test='${tipoocorrencia.tipo == "SP"}'>selected</c:if>>SUSPENSÃO</option>
            <option value="AL" <c:if test='${tipoocorrencia.tipo == "AL"}'>selected</c:if>>ALERTA</option>
        </select>          
        
        <br>
        
        <p class="legendaCodigo">Descrição:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" value="${tipoocorrencia.descricao}" />

        
        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2010';" value=" " />

    </form>
</body>
</html>
