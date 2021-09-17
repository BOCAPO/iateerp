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
            
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Ocorrências - Convênio bancos</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1830">
	<input type="hidden" name="acao" value="visualizar">

        <fieldset class="field-set legendaFrame recuoPadrao" style="width:485px;height:50px;">
            <legend>Período:</legend>
            <input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
            &nbsp;&nbsp;&nbsp;a
            <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero">        
            <input type="radio" name="periodo" class="legendaCodigo" value="V" checked>Vencimento
            <input type="radio" name="periodo" class="legendaCodigo" value="M">Movimento
            <input type="radio" name="periodo" class="legendaCodigo" value="P">Pagamento
        </fieldset>
        <br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
