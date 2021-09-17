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
            $("#vencimentoInicio").mask("99/99/9999");
            $("#vencimentoFim").mask("99/99/9999");            

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
        
        if(document.forms[0].filtrarVencimento.checked){            
            if(trim(document.forms[0].vencimentoInicio.value) == ''
                    || trim(document.forms[0].vencimentoFim.value) == ''){
                alert('É preciso preencher o período do vencimento');
                return;
            }            
            if(!isDataValida(document.forms[0].vencimentoInicio.value)){
                return;
            }
            if(!isDataValida(document.forms[0].vencimentoFim.value)){
                return;
            }            
        }

        if(!document.forms[0].recebidoBanco.checked
                && !document.forms[0].recebidoCaixa.checked){
            alert('Selecione pelo menos 1 local de pagamento!');
            return;
        }
        
        if(document.forms[0].recebidoBanco.checked){
            if(!document.forms[0].bancoBB.checked
                    && !document.forms[0].bancoItau.checked){
                alert('Selecione pelo menos 1 banco!');
                return;
            }
        }else{
            if(document.forms[0].bancoBB.checked
                   || document.forms[0].bancoItau.checked){
                alert('Foi selecionado apenas o movimento recebido no caixa. Não selecione banco!');
                return;
            }
        }

        var k = 0;
        for(var i = 0; i < document.forms[0].taxas.length; i++){
            if(document.forms[0].taxas[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma taxa.');
            return;
        }
            
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Valores Recebidos</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1800">
	<input type="hidden" name="acao" value="visualizar">
                     
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Taxas:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('taxas')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:370px;width:310px;">
                        <input type="checkbox" name="taxas" value="0">BOLETO AVULSO<br>
                        <c:forEach var="taxa" items="${taxas}">
                            <input type="checkbox" name="taxas" value="${taxa.id}">${taxa.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:70px;">
                                    <legend >Período:</legend>
                                    <input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                    &nbsp;&nbsp;&nbsp;a
                                    <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero"><br>        
                                    &nbsp;&nbsp;<input type="radio" name="periodo" class="legendaCodigo" style="margin-top:7px" value="B" checked>Baixa
                                    &nbsp;&nbsp;<input type="radio" name="periodo" class="legendaCodigo" style="margin-top:7px" value="P">Pagamento
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:50px;">
                                    <legend ><input type="checkbox" name="filtrarVencimento" value="true">Data de Vencimento</legend>
                                    <input type="text" id="vencimentoInicio" name="vencimentoInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                    &nbsp;&nbsp;&nbsp;a
                                    <input type="text" id="vencimentoFim"  name="vencimentoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:70px;">
                                    <legend >Local de Pagamento:</legend>        
                                    &nbsp;<input type="checkbox" name="recebidoBanco" value="true" style="margin-top:5px"checked>Valores Recebidos no Banco<br>
                                    &nbsp;<input type="checkbox" name="recebidoCaixa" value="true" style="margin-top:5px">Valores Recebidos no Clube
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:50px;">
                                    <legend >Banco de Pagamento:</legend>       
                                    &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="bancoBB" value="true" style="margin-top:5px" checked>Banco do Brasil
                                    &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="bancoItau" value="true" style="margin-top:5px" checked>Itaú
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:70px;">
                                    <legend >Tipo:</legend>        
                                    &nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" style="margin-top:5px" value="S">Sintético
                                    &nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" style="margin-top:5px" value="A" checked>Analítico
                                    <br>
                                    &nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" style="margin-top:5px" value="D">Detalhado
                                </fieldset>
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
