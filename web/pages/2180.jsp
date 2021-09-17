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
    <c:if test='${app == "2181"}'>
        <div id="titulo-subnav">Inclusão de Modelo de Carro</div>
    </c:if>
    <c:if test='${app == "2182"}'>
        <div id="titulo-subnav">Alteração de Modelo de Carro</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idModeloCarro" value="${modeloCarro.id}"/>

        <p class="legendaCodigo">Marca</p>
        <select name="idMarca" class="campoSemTamanho">
            <c:forEach var="marca" items="${marcas}">
                <option value="${marca.id}" <c:if test='${modeloCarro.marca.id == marca.id}'>selected</c:if>>${marca.descricao}</option>
            </c:forEach>
        </select> <br>
        
        
        <p class="legendaCodigo">Descrição:</p>
        <input type="text" name="descricao" maxlength="40" class="campoDescricao" value="${modeloCarro.descricao}" />

        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2180';" value=" " />

    </form>
</body>
</html>
