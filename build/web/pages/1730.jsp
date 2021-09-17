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
        
        if(!document.forms[0].registro.checked && !document.forms[0].cancelamento.checked){
            alert('Selecione pelo menos 1 ação!');
            return;
        }
        if(!document.forms[0].barco.checked && !document.forms[0].box.checked){
            alert('Selecione pelo menos 1 tipo!');
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Registro e Cancelamento de Embarcações</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1730">
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
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:65px;width:115px;">
                        <legend >Ação:</legend>
                        &nbsp;&nbsp;<input type="checkbox" name="registro" value="true" checked>Registro<br>
                        &nbsp;&nbsp;<input type="checkbox" name="cancelamento" value="true" checked>Cancelamento<br>
                    </fieldset>
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:65px;width:70px;">
                        <legend >Tipo:</legend>
                        &nbsp;&nbsp;<input type="checkbox" name="barco" value="true" checked>Barco<br>
                        &nbsp;&nbsp;<input type="checkbox" name="box" value="true" checked>Box<br>
                    </fieldset>
                </td>
            <tr>
        </table>
        

        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
