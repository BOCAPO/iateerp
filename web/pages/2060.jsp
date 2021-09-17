<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  


<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
    });    
    function validarForm(){
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
        
        if(!document.forms[0].entrada.checked && !document.forms[0].saida.checked){
            alert('Selecione pelo menos 1 tipo!');
            return;
        }
        
        var k = 0;
        for(var i = 0; i < document.forms[0].locais.length; i++){
            if(document.forms[0].locais[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos um local.');
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Movimentação de Embarcações</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2060">
	<input type="hidden" name="acao" value="visualizar">
                            
        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:65px;width:270px;">
                        <legend >Período</legend>
                        <input type="text" name="dataInicio" id="dataInicio" style="margin-top:6px;" class="campoSemTamanho alturaPadrao larguraData">    
                        &nbsp;&nbsp;&nbsp;a
                        <input type="text" name="dataFim" id="dataFim" style="margin-top:6px;"  class="campoSemTamanho alturaPadrao larguraData">        
                    </fieldset>                                
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:65px;width:80px;">
                        <legend >Tipo:</legend>
                        &nbsp;&nbsp;&nbsp;<input type="checkbox" name="entrada" value="true" checked>Entrada<br>
                        &nbsp;&nbsp;&nbsp;<input type="checkbox" name="saida" value="true" checked>Saída<br>
                    </fieldset>
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:65px;width:140px;">
                        <legend >Local:</legend>
                        <table class="fmt">
                            <tr>
                                <td class="field-set legendaFrame recuoPadrao">
                                    &nbsp;&nbsp;&nbsp;<input type="checkbox" name="locais" value="C" checked>Cais<br>
                                    &nbsp;&nbsp;&nbsp;<input type="checkbox" name="locais" value="A" checked>Água<br>
                                </td>
                                <td class="field-set legendaFrame recuoPadrao">
                                    &nbsp;&nbsp;&nbsp;<input type="checkbox" name="locais" value="L" checked>Clube<br>
                                    &nbsp;&nbsp;&nbsp;<input type="checkbox" name="locais" value="R" checked>Rampa<br>            
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
        </table>

        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
