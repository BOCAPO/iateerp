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
        if(!document.forms[0].sucesso.checked
                && !document.forms[0].erro.checked){
            alert('Selecione pelo menos um tipo de resultado');
            return;
        }                
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Boletos Enviados por Email</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2320">
	<input type="hidden" name="acao" value="visualizar">

        <label class="barrainclusaotext">Matrícula</label>
        <input class="formbarrainclusao3" type="text" name="matricula"/>
        
        <label class="barrainclusaotext">Categoria</label>
        <input class="formbarrainclusao2" type="text" name="categoria" onkeypress="onlyNumber(event)"/>

        <label class="barrainclusaotext">Nome</label>
        <input class="formbarrainclusao4" type="text" name="nome"/>

        <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:50px;">
            <legend >Emissão:</legend>
            <input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
            &nbsp;&nbsp;&nbsp;a
            <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero">        
        </fieldset>

        <fieldset class="field-set legendaFrame recuoPadrao">
            <legend >Resultado:</legend>
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="sucesso" value="true" checked>Sucesso
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="erro" value="true" checked>Erro<br>
        </fieldset>
                    
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
