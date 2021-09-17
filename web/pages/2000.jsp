<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style> 

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $('#vencimentoInicio').mask('99/99/9999');
            $('#vencimentoFim').mask('99/99/9999');
            $('#pagamentoInicio').mask('99/99/9999');
            $('#pagamentoFim').mask('99/99/9999');
    });  
   
    function validarForm(){
        
        if(trim(document.forms[0].vencimentoInicio.value) == ''
                && trim(document.forms[0].vencimentoFim.value) == ''){
            alert('informe o período de vencimento');
            return;
        }
        if((trim(document.forms[0].pagamentoInicio.value) == ''
                && trim(document.forms[0].pagamentoFim.value) != '')
            || (trim(document.forms[0].pagamentoInicio.value) != ''
                && trim(document.forms[0].pagamentoFim.value) == '')){
            alert('informe o período de pagamento ou deixe os dois campos em branco');
            return;
        }        
        if(!isDataValida(document.forms[0].vencimentoInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].vencimentoFim.value)){
            return;
        }
        if(!isDataValida(document.forms[0].pagamentoInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].pagamentoFim.value)){
            return;
        }
        
        if(!document.forms[0].situacaoPaga.checked
                && !document.forms[0].situacaoNaoPaga.checked){
            alert('Selecione pelo menos uma situação!');
            return;
        }
        
        if(!document.forms[0].clube.checked
                && !document.forms[0].banco.checked){
            alert('Selecione pelo menos um Local de Pagamento!');
            return;
        }
        
        var k = 0;
        for(var i = 0; i < document.forms[0].categorias.length; i++){
            if(document.forms[0].categorias[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma Categoria!');
            return;
        }
        
        k = 0;
        for(var i = 0; i < document.forms[0].taxas.length; i++){
            if(document.forms[0].taxas[i].checked){
                k++;
            }
        }

        for(var i = 0; i < document.forms[0].cursos.length; i++){
            if(document.forms[0].cursos[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma taxa ou um curso!');
            return;
        }        

        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Parcelas</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2020">
	<input type="hidden" name="acao" value="visualizar">
        
        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Título:</p>
                                <input type="text" name="titulo" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 40px">
                                    <legend >Situação:</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="situacaoPaga" value="true" checked>Paga
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="situacaoNaoPaga" value="true" checked>Não Paga<br>
                                </fieldset>        
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                                    <legend >Vencimento:</legend>
                                    &nbsp;<input type="text" name="vencimentoInicio" id="vencimentoInicio" class="campoSemTamanho alturaPadrao larguraNumero">    
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="vencimentoFim" id="vencimentoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                                    <legend >Pagamento:</legend>
                                    &nbsp;<input type="text" name="pagamentoInicio" id="pagamentoInicio" class="campoSemTamanho alturaPadrao larguraNumero">    
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="pagamentoFim" id="pagamentoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 40px">
                                    <legend >Local de Pagamento:</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="clube" value="true" checked>Clube
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="banco" value="true" checked>Banco<br>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 40px">
                                    <legend >Banco de Pagamento:</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="bancoBB" value="true" checked>Banco do Brasil
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="bancoItau" value="true" checked>Itaú<br>
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:340px;width:220px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}" checked>${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo">Taxas:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('taxas')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:300px;width:220px;">
                        <c:forEach var="taxa" items="${taxas}">
                            <input type="checkbox" name="taxas" value="${taxa.id}" checked>${taxa.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo">Cursos:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('cursos')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:300px;width:220px;">
                        <c:forEach var="curso" items="${cursos}">
                            <input type="checkbox" name="cursos" value="${curso.id}" checked>${curso.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
        </table>
        
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
    </form>

</body>
</html>
