<%@include file="head.jsp"%>
<%@include file="menu.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    .zera{ 
    font-family: "Trebuchet MS",Verdana,Arial,Helvetica,sans-serif;
    color: #678197;
    font-size: 12px;
    font-weight: normal;
    }    
</style>  


<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
            $("#emissaoInicio").mask("99/99/9999");
            $("#emissaoFim").mask("99/99/9999");
            $("#validadeInicio").mask("99/99/9999");
            $("#validadeFim").mask("99/99/9999");
            $("#utilizacaoInicio").mask("99/99/9999");
            $("#utilizacaoFim").mask("99/99/9999");            
            
    });    
    
    function validarForm(){
       
        var k = 0;
        for(var i = 0; i < document.forms[0].tiposConvite.length; i++){
            if(document.forms[0].tiposConvite[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos um tipo de convite.');
            return;
        }
        
        k = 0;
        for(var i = 0; i < document.forms[0].situacoes.length; i++){
            if(document.forms[0].situacoes[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma situacao.');
            return;
        }
        
        if((trim(document.forms[0].emissaoInicio.value) != ''
                && trim(document.forms[0].emissaoFim.value) == '')
                ||
                (trim(document.forms[0].emissaoInicio.value) == ''
                && trim(document.forms[0].emissaoFim.value) != '')){
            
            alert('Preencha a data de inicio e fim do período de emissão '
                    + 'ou deixe os dois campos em branco');
            return;
        
        }else{
            if(!isDataValida(document.forms[0].emissaoInicio.value)){
                return;
            }
            if(!isDataValida(document.forms[0].emissaoFim.value)){
                return;
            }                            

        }
        
        if((trim(document.forms[0].validadeInicio.value) != ''
                && trim(document.forms[0].validadeFim.value) == '')
                ||
                (trim(document.forms[0].validadeInicio.value) == ''
                && trim(document.forms[0].validadeFim.value) != '')){
            
            alert('Preencha a data de inicio e fim do período de validade '
                    + 'ou deixe os dois campos em branco');
            return;
        
        }else{
            if(!isDataValida(document.forms[0].validadeInicio.value)){
                return;
            }
            if(!isDataValida(document.forms[0].validadeFim.value)){
                return;
            }                            

        }
        
        if((trim(document.forms[0].utilizacaoInicio.value) != ''
                && trim(document.forms[0].utilizacaoFim.value) == '')
                ||
                (trim(document.forms[0].utilizacaoInicio.value) == ''
                && trim(document.forms[0].utilizacaoFim.value) != '')){
            
            alert('Preencha a data de inicio e fim do período de utilização '
                    + 'ou deixe os dois campos em branco');
            return;
        
        }else{
            if(!isDataValida(document.forms[0].utilizacaoInicio.value)){
                return;
            }
            if(!isDataValida(document.forms[0].utilizacaoFim.value)){
                return;
            }                            

        }        
                
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Convites</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1980">
	<input type="hidden" name="acao" value="visualizar">

        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Convites:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('tiposConvite')" style="margin-top: 2px;" value=""/></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:370px;width:280px;">
                        <c:forEach var="tipoConvite" items="${tiposConvite}">
                            <input type="checkbox" name="tiposConvite" value="${tipoConvite.cod}">${tipoConvite.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                        <legend >Período de Emissão:</legend>        
                        &nbsp;<input type="text" id="emissaoInicio" name="emissaoInicio" class="campoSemTamanho alturaPadrao larguraNumero">
                        &nbsp;&nbsp;&nbsp;&nbsp;a
                        <input type="text" id="emissaoFim"  name="emissaoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                    </fieldset>

                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                        <legend >Período de Validade:</legend>        
                        &nbsp;<input type="text" id="validadeInicio" name="validadeInicio" class="campoSemTamanho alturaPadrao larguraNumero">
                        &nbsp;&nbsp;&nbsp;&nbsp;a
                        <input type="text" id="validadeFim"  name="validadeFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                    </fieldset>

                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                        <legend >Período de Utilização:</legend>        
                        &nbsp;<input type="text" id="utilizacaoInicio" name="utilizacaoInicio" class="campoSemTamanho alturaPadrao larguraNumero">
                        &nbsp;&nbsp;&nbsp;&nbsp;a
                        <input type="text" id="utilizacaoFim"  name="utilizacaoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                    </fieldset>

                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                        <legend >Título:</legend>
                        &nbsp;<input type="text" name="tituloInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                        &nbsp;&nbsp;&nbsp;&nbsp;a
                        <input type="text" name="tituloFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                    </fieldset>

                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 90px">
                        <legend >Situação:</legend>                
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="situacoes" class="legendaCodigo" value="NU" checked>Não Utilizado
                        <br>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="situacoes" class="legendaCodigo" value="UT" checked>Utilizado
                        <br>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="situacoes" class="legendaCodigo" value="CA" checked>Cancelado
                    </fieldset>
                </td>
            </tr>
        </table>
        

        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
