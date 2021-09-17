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
    <c:if test='${app == "3021"}'>
        <div id="titulo-subnav">Inclusão de Curso</div>
    </c:if>
    <c:if test='${app == "3022"}'>
        <div id="titulo-subnav">Alteração de Curso</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idCurso" value="${curso.id}"/>

        <p class="legendaCodigo">Descrição:</p>
        <input type="text" name="descricao" maxlength="25" class="campoDescricao" value="${curso.descricao}" />

        <p class="legendaCodigo">Modalidade:</p>
        <select name="cdModalidade" class="campoSemTamanho campoDescricao">
            <c:forEach var="modalidade" items="${modalidades}">
                <option value="${modalidade.id}" <c:if test='${curso.cdModalidade == modalidade.id}'>selected</c:if>>${modalidade.descricao}</option>
            </c:forEach>
        </select> <br>    
        
        <p class="legendaCodigo">Taxa para Cobrança</p>
        <select name="cdTxAdministrativa" class="campoSemTamanho campoDescricao">
            <c:forEach var="txsAdm" items="${taxasAdministrativas}">
                <option value="${txsAdm.id}" <c:if test='${curso.cdTxAdministrativa == txsAdm.id}'>selected</c:if>>${txsAdm.descricao}</option>
            </c:forEach>
        </select> <br>

        <p class="legendaCodigo">Desconto Familiar</p>
        <select name="tpDesconto" class="campoSemTamanho campoDescricao">
            <option value="" selected>NENHUM</option>
            <option value="S" <c:if test='${curso.tpDesconto == "S"}'>selected</c:if>>4 a 14 anos - 2 primeiras matrículas grátis</option>
        </select> <br><br>     
        
        <input type="radio" class="legendaCodigo" name="situacao" value="N" checked> <spam class="legendaSemMargem"> Normal </spam>
        <input type="radio" name="situacao" value="C" <c:if test='${curso.situacao == "C"}'>checked</c:if>> <spam class="legendaSemMargem"> Inativo </spam>
        
        
        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3020';" value=" " />

    </form>
</body>
</html>

