<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('O campo nome é de preenchimento obrigatório!');
            return;
        }


        if(document.forms[0].placa.value == ''){
            alert('O campo placa é de preenchimento obrigatório!');
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "2211"}'>
        <div id="titulo-subnav">Inclusão de Carro</div>
    </c:if>
    <c:if test='${app == "2212"}'>
        <div id="titulo-subnav">Alteração de Carro</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idCarro" value="${carro.id}"/>

        <p class="legendaCodigo">Nome:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" value="${carro.descricao}" />
        <p class="legendaCodigo">Placa:</p>
        <input type="text" name="placa" maxlength="8" class="campoSemTamanho alturaPadrao" value="${carro.placa}" />
        
        <p class="legendaCodigo">Modelo:</p>
        <select name="idModelo" class="campoSemTamanho" >
            <c:forEach var="modelo" items="${modelos}">
                <option value="${modelo.id}" <c:if test='${carro.modelo.id == modelo.id}'>selected</c:if>>${modelo.descricao}</option>
            </c:forEach>
        </select> <br>

        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2210';" value=" " />

    </form>
</body>
</html>
