<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
            $("#dtReferencia").mask("99/99/9999");
            $("#vencimentoInicio").mask("99/99/9999");
            $("#vencimentoFim").mask("99/99/9999");            
            

    });
    
    function validarForm(){
        if(trim(document.forms[0].dtReferencia.value) == ''){
            alert('É preciso preencher a Data de Referência');
            return;
        }
        if(!isDataValida(document.forms[0].dtReferencia.value)){
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
        
        if ($("#tipoTaxa" ).val() == "T"){
            var k = 0;
            for(var i = 0; i < document.forms[0].taxas.length; i++){
                if(document.forms[0].taxas[i].checked){
                    k++;
                }
            }
            if(k == 0){
                alert('Selecione pelo menos uma Taxa.');
                return;
            }
        }
        
        var j = 0;
        for(var i = 0; i < document.forms[0].categorias.length; i++){
            if(document.forms[0].categorias[i].checked){
                j++;
            }
        }
        if(j == 0){
            alert('Selecione pelo menos uma Categoria.');
            return;
        }
        
        document.forms[0].submit();
    }
    
    function trocaTipoTaxa(){
        $("input[name='taxas']").prop("checked", false);

        if ($("#tipoTaxa" ).val() == "T"){
            $("input[name='taxas']").prop("disabled", false);
            $("#marcaTaxas").prop("disabled", false);
        }else{
            $("input[name='taxas']").prop("disabled", true);
            $("#marcaTaxas").prop("disabled", true);
        }
    }
    

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Valores a Receber</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2440">
	<input type="hidden" name="acao" value="visualizar">
                     
        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <br>
                                <p class="legendaCodigo">Dt. Referência</p>
                                <input type="text" id="dtReferencia" name="dtReferencia" style='width:92px;' class="campoSemTamanho alturaPadrao">        
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:45px;margin-top: 30px">
                                    <legend >Tipo:</legend>        
                                    &nbsp;&nbsp;&nbsp<input type="radio" name="tipo" class="legendaCodigo" style="margin-top:3px" value="A" checked>Analítico
                                    &nbsp;&nbsp;&nbsp<input type="radio" name="tipo" class="legendaCodigo" style="margin-top:3px" value="S">Sintético
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <p class="legendaCodigo">Tipo de Taxa</p>
                                <div class="selectheightnovo">
                                    <select name="tipoTaxa" id="tipoTaxa"  class="campoSemTamanho" style="width:190px; " onchange="trocaTipoTaxa()"  >
                                        <option value="T" >TODAS</option>
                                        <option value="I" >Crédito para o Iate</option>
                                        <option value="C" >Repasse para Concessionário</option>
                                    </select>
                                </div>        
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:50px;margin-top: 30px">
                                    <legend ><input type="checkbox" name="filtrarVencimento" value="true">Data de Vencimento</legend>
                                    <input type="text" id="vencimentoInicio" name="vencimentoInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                    &nbsp;&nbsp;&nbsp;a
                                    <input type="text" id="vencimentoFim"  name="vencimentoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
                <td colspan="4">
                    <table class="fmt">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Taxas:<span><input class="botaoMarcaDesmarca" type="button" id="marcaTaxas"  onclick="marcaDesmarca('taxas')" style="margin-top: 0px;" value="" title="Consultar" /></span></p>
                                <div id="divtaxas" class="recuoPadrao" style="overflow:auto;height:270px;width:280px;">
                                    <c:forEach var="taxa" items="${taxas}">
                                        <input type="checkbox" name="taxas" value="${taxa.id}">${taxa.descricao}<br>
                                    </c:forEach>
                                </div>
                            </td>
                            <td>
                                <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button" id="marcaTaxas"  onclick="marcaDesmarca('categorias')" style="margin-top: 0px;" value="" title="Consultar" /></span></p>
                                <div id="divtaxas" class="recuoPadrao" style="overflow:auto;height:270px;width:280px;">
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
        
        <span class="legendaCodigo"><input type="checkbox" name="mostraPGDiaAnt" value="S">Não incluir registros que tenham sido pagos no Itaú e a Data de Pagamento seja a mesma informada na Data de referência.</span> 
        <br><br>
                    
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
