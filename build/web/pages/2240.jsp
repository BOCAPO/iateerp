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
            
            $("#vencimentoInicio").mask("99/99/9999");
            $("#vencimentoFim").mask("99/99/9999");
            $("#pagamentoInicio").mask("99/99/9999");
            $("#pagamentoFim").mask("99/99/9999");
            
    });    
        
    function enableDisablePagamento(){
        var e = !document.forms[0].quitado.checked;
        document.forms[0].pagamentoInicio.disabled = e;
        document.forms[0].pagamentoFim.disabled = e;
    }
    
    function validarForm(){
       
        if(trim(document.forms[0].tituloInicio.value) == ''
                || trim(document.forms[0].tituloFim.value) == ''){
            alert('Infome o t�tulo inicial e final para o relat�rio');
            return;
        }
        
        if(trim(document.forms[0].vencimentoInicio.value) == ''
                || trim(document.forms[0].vencimentoFim.value) == ''){
            alert('Infome o per�odo de vencimento para o relat�rio');
            return;
        }
        
        if(!isDataValida(document.forms[0].vencimentoInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].vencimentoFim.value)){
            return;
        }

        if(document.forms[0].quitado.checked){
            if((!trim(document.forms[0].pagamentoInicio.value) == '' && trim(document.forms[0].pagamentoFim.value) == '')
                || (trim(document.forms[0].pagamentoInicio.value) == '' && !trim(document.forms[0].pagamentoFim.value) == '')){                    
                alert('Infome o per�odo inicial e final de pagamento ou deixe os dois campos em branco');
                return;
            }            
            if(!isDataValida(document.forms[0].pagamentoInicio.value)){
                return;
            }
            if(!isDataValida(document.forms[0].pagamentoFim.value)){
                return;
            }        
        }
        
        if(!document.forms[0].quitado.checked
                && !document.forms[0].pendente.checked){
            alert('Infome pelo menos uma situa��o para o relat�rio');
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

    <div class="divisoria"></div>
    <div id="titulo-subnav">Declara��o de Quita��o</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2240">
	<input type="hidden" name="acao" value="visualizar">

        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px;">
                                    <legend >T�tulo:</legend>                
                                    <input type="text" name="tituloInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="tituloFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                                </fieldset>
                            </td>
                        </tr>    
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px;">
                                    <legend >Vencimento:</legend>        
                                    &nbsp;<input type="text" id="vencimentoInicio" name="vencimentoInicio" class="campoSemTamanho alturaPadrao larguraNumero">
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" id="vencimentoFim"  name="vencimentoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>
                            </td>
                        </tr>    
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px;">
                                    <legend >Pagamento:</legend>        
                                    &nbsp;<input type="text" id="pagamentoInicio" name="pagamentoInicio" class="campoSemTamanho alturaPadrao larguraNumero">
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" id="pagamentoFim"  name="pagamentoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>
                            </td>
                        </tr>    
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 45px">
                                    <legend >Situacao:</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="quitado" value="true" onchange="enableDisablePagamento()" checked>Quitado
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="pendente" value="true" checked>Pendente<br>
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value=""/></span></p>
                                <div class="recuoPadrao" style="overflow:auto;height:240px;width:227px;">
                                    <c:forEach var="categoria" items="${categorias}">
                                        <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                                    </c:forEach>
                                </div>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
