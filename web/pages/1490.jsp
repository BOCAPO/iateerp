<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
    });    
    
    function selectUnselect(componet){
        var c = document.getElementsByName(componet);
        for(var i = 0; i < c.length; i++){
            c[i].checked = !c[i].checked;
        }
    }
    
    function validarForm(){
        var k = 0;
        for(var i = 0; i < document.forms[0].locais.length; i++){
            if(document.forms[0].locais[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos um local de acesso.');
            return;
        }
        
        if(trim(document.forms[0].dataInicio.value) == ''
            || trim(document.forms[0].dataFim.value) == ''){
            alert('É preciso informar as datas iniciais e finais para o relatório');
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
    <div id="titulo-subnav">Relatório de Autorização de Entrada</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1490">
        <input type="hidden" name="acao" value="visualizar">

        <fieldset class="field-set legendaFrame recuoPadrao" style="height:65px;width:270px;">
            <legend >Período</legend>
            <input type="text" name="dataInicio" id="dataInicio" style="margin-top:6px;" class="campoSemTamanho alturaPadrao larguraData">    
            &nbsp;&nbsp;&nbsp;a
            <input type="text" name="dataFim" id="dataFim" style="margin-top:6px;"  class="campoSemTamanho alturaPadrao larguraData">        
        </fieldset>                                

        <p class="legendaCodigo">Locais de Acesso:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('locais')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
        <div class="recuoPadrao" style="overflow:auto;height:200px;width:270px;">
            <c:forEach var="local" items="${locais}">
                <input type="checkbox" name="locais" value="${local.id}">${local.descricao}<br>
            </c:forEach>
        </div>
        <br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
