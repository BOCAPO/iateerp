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
            
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
            
    });    
       
    function validarForm(){
        
        if(trim(document.forms[0].tituloInicio.value) == ''
                && trim(document.forms[0].tituloFim.value) == ''){
            alert('Informe o título inicial e final.');
            return;
        }
        
        if(parseInt(document.forms[0].tituloInicio.value) >
                parseInt(document.forms[0].tituloFim.value)){
            alert('O título final deve ser maior que o título inicial.');
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

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Dados Cadastrais do Sócio</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1240">
	<input type="hidden" name="acao" value="visualizar">
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('categorias')" style="margin-top: -20px;" value=""/></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:370px;width:227px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="height:75px;width:170px;">
                                    <legend >Título:</legend>  
                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <p class="legendaCodigoSemPadding">Inicial</p>
                                                <input type="text" name="tituloInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                                            </td>
                                            <td>
                                                <p class="legendaCodigoSemPadding">Final</p>
                                                <input type="text" name="tituloFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:170px;">
                                    <legend >Ordenação:</legend>                
                                    <input type="radio" name="ordenacao" class="legendaCodigo" value="N" checked>Nome
                                    <input type="radio" name="ordenacao" class="legendaCodigo" value="M">Matrícula
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:170px;">
                                    <legend >Opções:</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="dadosPessoais" value="true" disabled checked>Dados Pessoais<BR>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="dadosComerciais" value="true" checked>Dados Comerciais<br>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="dependentes" value="true" checked>Dependentes<br>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="dadosResidenciais" value="true" checked>Dados Residenciais<br>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="dadosBancarios" value="true" checked>Dados Bancários<br>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="height:75px;width:170px;">
                                    <legend >Data de Adminissão:</legend>        
                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <p class="legendaCodigoSemPadding">Inicial</p>
                                                <input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero">
                                            </td>
                                            <td>
                                                <p class="legendaCodigoSemPadding">Inicial</p>
                                                <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                            </td>
                                        </tr>
                                    </table>
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
