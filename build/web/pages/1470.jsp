<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");

    });
    
    function validarForm(){
        if(trim(document.forms[0].dataInicio.value) == ''
                || trim(document.forms[0].dataFim.value) == ''){
            alert('É preciso preencher o período do relatório');
            return;
        }
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }
            
        var k = 0;
        for(var i = 0; i < document.forms[0].categorias.length; i++){
            if(document.forms[0].categorias[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma categoria.');
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Inadimplência</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1470">
	<input type="hidden" name="acao" value="visualizar">

        <fieldset class="field-set legendaFrame recuoPadrao" style="width:225px;height:50px;">
            <legend>Período:</legend>
            &nbsp;&nbsp;<input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a
            &nbsp;&nbsp;&nbsp;<input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero">        
        </fieldset>

        <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value=""/></span></p>
        <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
            <c:forEach var="categoria" items="${categorias}">
                <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
            </c:forEach>
        </div>
        <br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
