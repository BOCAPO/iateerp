<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $('#dataAtualizacaoInicio').mask('99/99/9999');
            $('#dataAtualizacaoFim').mask('99/99/9999');
            $('#dataEntregaInicio').mask('99/99/9999');
            $('#dataEntregaFim').mask('99/99/9999');
    });  
    
    function enableDisable(){
        var enabled = !document.forms[0].entregue.checked;
        
        document.forms[0].dataEntregaInicio.disabled = enabled;
        document.forms[0].dataEntregaFim.disabled = enabled;
    }
    
    function validarForm(){
        
        if(!isDataValida(document.forms[0].dataAtualizacaoInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataAtualizacaoFim.value)){
            return;
        }        
        if(!isDataValida(document.forms[0].dataEntregaInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataEntregaFim.value)){
            return;
        }        
        
        if(!document.forms[0].naoEntregue.checked
            && !document.forms[0].entregue.checked){
            alert('Selecione pelo menos uma situação.');
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Brinde Atualização Internet</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2280">
	<input type="hidden" name="acao" value="visualizar">       
        
        <br>
        <fieldset class="field-set legendaFrame recuoPadrao" style="width:270px;height: 50px">
            <legend >Período de atualização na internet:</legend>
            <input type="text" name="dataAtualizacaoInicio" id="dataAtualizacaoInicio" class="campoSemTamanho alturaPadrao larguraData">        
            &nbsp;&nbsp;&nbsp; a
            <input type="text" name="dataAtualizacaoFim" id="dataAtualizacaoFim" class="campoSemTamanho alturaPadrao larguraData">        
        </fieldset>                                
        <br>
        <fieldset class="field-set legendaFrame recuoPadrao" style="width:270px;height: 50px">
            <legend >Período de entrega do brinde:</legend>
            <input type="text" name="dataEntregaInicio" id="dataEntregaInicio" class="campoSemTamanho alturaPadrao larguraData">   
            &nbsp;&nbsp;&nbsp; a
            <input type="text" name="dataEntregaFim" id="dataEntregaFim" class="campoSemTamanho alturaPadrao larguraData">        
        </fieldset>                                
        <br>
        <fieldset class="field-set legendaFrame recuoPadrao" style="width:270px;height: 40px">
            <legend >Informações:</legend>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="naoEntregue" value="true" checked>Não Entregue
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="entregue" onchange="enableDisable()" value="true" checked>Entregue<br>
        </fieldset>
        <br>

        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        

    </form>

</body>
</html>
