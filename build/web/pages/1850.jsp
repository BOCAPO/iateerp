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
            alert('É preciso preencher o período do relatório');
            return;
        }
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }

        if(!document.forms[0].recebidoBanco.checked
                && !document.forms[0].recebidoCaixa.checked){
            alert('Selecione pelo menos 1 local de pagamento!');
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
            
        k = 0;
        for(var i = 0; i < document.forms[0].cursos.length; i++){
            if(document.forms[0].cursos[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos um curso.');
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Valores Recebidos de Escolinhas</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1850">
	<input type="hidden" name="acao" value="visualizar">
        <br>
        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:270px;height:70px;">
                                    <legend >Período:</legend>
                                    <input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraData">        
                                    &nbsp;&nbsp;&nbsp;a
                                    <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraData">  <br>      
                                    <input type="radio" name="periodo" class="legendaCodigo" style="margin-top:7px" value="B" checked>Baixa
                                    <input type="radio" name="periodo" class="legendaCodigo" style="margin-top:7px" value="P">Pagamento
                                    <input type="radio" name="periodo" class="legendaCodigo" style="margin-top:7px" value="V">Vencimento
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:70px;">
                                    <legend >Local de Pagamento:</legend>        
                                    &nbsp;&nbsp;<input type="checkbox" name="recebidoBanco" style="margin-top:5px" value="true" checked>Valores Recebidos no Banco<br>
                                    &nbsp;&nbsp;<input type="checkbox" name="recebidoCaixa" style="margin-top:5px" value="true">Valores Recebidos no Clube
                                </fieldset>
                            </td>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:125px;height:70px;">
                                    <legend >Banco de Pagamento:</legend>        
                                    &nbsp;&nbsp;<input type="checkbox" name="bancoBB" style="margin-top:5px" value="true" checked>Banco do Brasil<br>
                                    &nbsp;&nbsp;<input type="checkbox" name="BancoIrau" style="margin-top:5px" value="true" checked>Itaú
                                </fieldset>
                            </td>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:95px;height:70px;">
                                    <legend >Tipo:</legend>        
                                    <input type="radio" name="tipo" class="legendaCodigo" style="margin-top:5px" value="S">Sintético<br>
                                    <input type="radio" name="tipo" class="legendaCodigo" style="margin-top:5px" value="A" checked>Analítico
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value=""/></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:360px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo">Cursos:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('cursos')" style="margin-top: 2px;" value=""/></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:360px;">
                        <c:forEach var="curso" items="${cursos}">
                            <input type="checkbox" name="cursos" value="${curso.id}">${curso.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
        </table>
                    
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
