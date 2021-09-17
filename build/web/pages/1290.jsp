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
            
            $("#carteira").show();
            $("#passaporte").hide();
            $("#convite").hide();
            $("#funcionario").hide();
            $("#visitante").hide();
            
    });    
    
    function enableDisable(tipo){

        var carteiraEnabled = !document.forms[0].imprimir[0].checked;
   
        for(var i = 0; i < document.forms[0].categorias.length; i++){
            document.forms[0].categorias[i].disabled = carteiraEnabled;
        }
        
        document.forms[0].tituloCarteira.disabled = carteiraEnabled;
        
        document.forms[0].titular.disabled = carteiraEnabled;
        document.forms[0].dependente.disabled = carteiraEnabled;
        
        document.forms[0].masculinoCarteira.disabled = carteiraEnabled;
        document.forms[0].femininoCarteira.disabled = carteiraEnabled;

        var passaporteEnabled = !document.forms[0].imprimir[1].checked;
   
        for(var i = 0; i < document.forms[0].cursos.length; i++){
            document.forms[0].cursos[i].disabled = passaporteEnabled;

        }
        
        document.forms[0].tituloPassaporte.disabled = passaporteEnabled;
        document.forms[0].masculinoPassaporte.disabled = passaporteEnabled;
        document.forms[0].femininoPassaporte.disabled = passaporteEnabled;

        var conviteEnabled = !document.forms[0].imprimir[2].checked;
   
        for(var i = 0; i < document.forms[0].tiposConvite.length; i++){
            document.forms[0].tiposConvite[i].disabled = conviteEnabled;

        }
        
        document.forms[0].convitePagoVendido.disabled = conviteEnabled;
        document.forms[0].convitePagoNormal.disabled = conviteEnabled;

        var funcionarioEnabled = !document.forms[0].imprimir[3].checked;
   
        for(var i = 0; i < document.forms[0].setoresFuncionario.length; i++){
            document.forms[0].setoresFuncionario[i].disabled = funcionarioEnabled;

        }
        
        for(var i = 0; i < document.forms[0].cargos.length; i++){
            document.forms[0].cargos[i].disabled = funcionarioEnabled;

        }
        
        document.forms[0].funcionario.disabled = funcionarioEnabled;
        document.forms[0].concessionario.disabled = funcionarioEnabled;

        var visitanteEnabled = !document.forms[0].imprimir[4].checked;
   
        for(var i = 0; i < document.forms[0].setoresVisitante.length; i++){
            document.forms[0].setoresVisitante[i].disabled = visitanteEnabled;

        }
        
        $("#carteira").hide();
        $("#passaporte").hide();
        $("#convite").hide();
        $("#funcionario").hide();
        $("#visitante").hide();
        
        $(tipo).show();
    }
    
    function validarForm(){
       
        if(trim(document.forms[0].dataInicio.value) == ''
                || trim(document.forms[0].dataFim.value) == ''){
            alert('Infome o período para o relatório');
            return;
        }
        
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }
        
        var k = 0;
        for(var i = 0; i < document.forms[0].tiposAcesso.length; i++){
            if(document.forms[0].tiposAcesso[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos um tipo de acesso.');
            return;
        }

        if(document.forms[0].imprimir[0].checked){
            k = 0;
            for(var i = 0; i < document.forms[0].categorias.length; i++){
                if(document.forms[0].categorias[i].checked){
                    k++;
                }
            }
            if(k == 0){
                alert('Selecione pelo menos uma categoria para CARTEIRA.');
                return;
            }
            
            if(!document.forms[0].masculinoCarteira.checked
                    && !document.forms[0].femininoCarteira.checked){
                alert('Selecione pelo menos um sexo para CARTEIRA.');
                return;
            }
            
            if(!document.forms[0].titular.checked
                    && !document.forms[0].dependente.checked){
                alert('Selecione pelo menos um tipo para CARTEIRA.');
                return;
            }            
        }
        
        if(document.forms[0].imprimir[1].checked){
            k = 0;
            for(var i = 0; i < document.forms[0].cursos.length; i++){
                if(document.forms[0].cursos[i].checked){
                    k++;
                }
            }
            if(k == 0){
                alert('Selecione pelo menos um curso para PASSAPORTE.');
                return;
            }
            
            if(!document.forms[0].masculinoPassaporte.checked
                    && !document.forms[0].femininoPassaporte.checked){
                alert('Selecione pelo menos um sexo para PASSAPORTE.');
                return;
            }
        }
        
        if(document.forms[0].imprimir[2].checked){
            k = 0;
            for(var i = 0; i < document.forms[0].tiposConvite.length; i++){
                if(document.forms[0].tiposConvite[i].checked){
                    k++;
                }
            }
            if(k == 0){
                alert('Selecione pelo menos um tipo de CONVITE.');
                return;
            }
            
            if(!document.forms[0].convitePagoVendido.checked
                    && !document.forms[0].convitePagoNormal.checked){
                alert('informe se o convite é pago ou não.');
                return;
            }
        }
        
        if(document.forms[0].imprimir[3].checked){
            k = 0;
            for(var i = 0; i < document.forms[0].setoresFuncionario.length; i++){
                if(document.forms[0].setoresFuncionario[i].checked){
                    k++;
                }
            }
            if(k == 0){
                alert('Selecione pelo menos um setor para FUNCIONÁRIO.');
                return;
            }
            
            k = 0;
            for(var i = 0; i < document.forms[0].cargos.length; i++){
                if(document.forms[0].cargos[i].checked){
                    k++;
                }
            }
            if(k == 0){
                alert('Selecione pelo menos um cargo para FUNCIONÁRIO.');
                return;
            }
            
            if(!document.forms[0].funcionario.checked
                    && !document.forms[0].concessionario.checked){
                alert('informe um tipo de funcionário.');
                return;
            }
        }
        
        if(document.forms[0].imprimir[4].checked){
            k = 0;
            for(var i = 0; i < document.forms[0].setoresVisitante.length; i++){
                if(document.forms[0].setoresVisitante[i].checked){
                    k++;
                }
            }
            if(k == 0){
                alert('Selecione pelo menos um setor para VISITANTE.');
                return;
            }
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Acesso ao Clube</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1290">
	<input type="hidden" name="acao" value="visualizar">

        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:310px;height: 50px;">
                                    <legend >Tipo:</legend>                
                                    <input type="radio" name="tipoFiltro" class="legendaCodigo" style="margin-top:7px" value="N" checked>Nome
                                    <input type="radio" name="tipoFiltro" class="legendaCodigo" style="margin-top:7px" value="A">Quant. Acesso
                                    <input type="radio" name="tipoFiltro" class="legendaCodigo" style="margin-top:7px" value="P">Quant. Pessoas
                                </fieldset>
                            </td>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px;">
                                    <legend >Período:</legend>        
                                    &nbsp;<input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero">
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>
                            </td>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:320px;height: 50px;">
                                    <legend >Tipo de Acesso:</legend>        
                                    <input type="checkbox" name="tiposAcesso" class="legendaCodigo" style="margin-top:7px" value="PE" checked>Acesso Acatado
                                    <input type="checkbox" name="tiposAcesso" class="legendaCodigo" style="margin-top:7px" value="PA" checked>Proibir Acesso
                                    <input type="checkbox" name="tiposAcesso" class="legendaCodigo" style="margin-top:7px" value="AL" checked>Alerta
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:140px;height: 50px;">
                                    <legend >Ordenação:</legend>                
                                    <input type="radio" name="ordenacao" style="margin-top:7px" class="legendaCodigo" value="N" checked>Nome
                                    <input type="radio" name="ordenacao" style="margin-top:7px" class="legendaCodigo" value="D">Data
                                </fieldset>
                            </td>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:240px;height: 50px;">
                                    <legend >Local de Acesso:</legend>                
                                    <select name="idLocalAcesso" class="campoSemTamanho alturaPadrao" style="margin-top:3px;width:210px">
                                        <option value="0">TODOS</option>
                                        <c:forEach var="l" items="${locaisAcesso}">
                                            <option value="${l.id}">${l.descricao}</option>
                                        </c:forEach>
                                    </select>
                                </fieldset>
                            </td>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:282px;height: 50px;">
                                    <legend >Nome:</legend>                
                                    <input class="campoSemTamanho alturaPadrao larguraNumero" style="width:252px" type="text" name="nome"/><BR>
                                </fieldset>
                            </td>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:150px;height: 50px;">
                                    <legend >Sentido:</legend>                
                                    <input type="radio" name="sentido" class="legendaCodigo" style="margin-top:7px" value="E" checked>Entrada
                                    <input type="radio" name="sentido" class="legendaCodigo" style="margin-top:7px" value="S">Saída
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        

<div class="divisoria"></div>
<div id="titulo-subnav">Dados</div>
<div class="divisoria"></div>
        
<fieldset class="field-set legendaFrame recuoPadrao" style="width:862px;height: 50px;">
    <legend >Tipo:</legend>                
    &nbsp;<input type="radio" name="imprimir" onclick="enableDisable('#carteira')" class="legendaCodigo" value="carteira" checked>Carteira
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="imprimir" onclick="enableDisable('#passaporte')" class="legendaCodigo" value="passaporte">Passaporte
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="imprimir" onclick="enableDisable('#convite')" class="legendaCodigo" value="convite">Convite
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="imprimir" onclick="enableDisable('#funcionario')" class="legendaCodigo" value="funcionario">Funcinonário
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="imprimir" onclick="enableDisable('#visitante')" class="legendaCodigo" value="visitante">Visitante
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="imprimir" onclick="enableDisable('')" class="legendaCodigo" value="permissaoProvisoria">Permissão Provisória
    &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="imprimir" onclick="enableDisable('')" class="legendaCodigo" value="autorizacaoEmbarque">Autorização de Embarque
</fieldset>


<div name="carteira" id="carteira">
    <table class="fmt">
        <tr>
            <td>
                <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value=""/></span></p>
                <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                    <c:forEach var="categoria" items="${categorias}">
                        <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                    </c:forEach>
                </div>
            </td>
            <td>
                <table class="fmt">
                    <tr>
                        <td>
                            <br><br>
                            <fieldset class="field-set legendaFrame recuoPadrao" style="width:100px;height: 50px">
                                <legend >Título:</legend>
                                &nbsp;<input type="text" name="tituloCarteira" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                            </fieldset>                                
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <br><br>
                            <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 45px">
                                <legend >Tipo:</legend>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="titular" value="true" checked>Titular
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="dependente" value="true" checked>Dependente<br>
                            </fieldset>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <br><br>
                            <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 45px">
                                <legend >Sexo:</legend>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="masculinoCarteira" value="true" checked>Masculino
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="femininoCarteira" value="true" checked>Feminino<br>
                            </fieldset>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>

<div name="passaporte" id="passaporte">
    <table class="fmt">
        <tr>
            <td>
                <p class="legendaCodigo">Cursos:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('cursos')" style="margin-top: 2px;" value=""/></span></p>
                <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                    <c:forEach var="curso" items="${cursos}">
                        <input type="checkbox" name="cursos" disabled value="${curso.id}">${curso.descricao}<br>
                    </c:forEach>
                </div>
            </td>
            <td>
                <table class="fmt">
                    <tr>
                        <td>
                            <br><br>
                            <fieldset class="field-set legendaFrame recuoPadrao" style="width:100px;height: 50px">
                                <legend >Título:</legend>
                                &nbsp;<input type="text" name="tituloPassaporte" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                            </fieldset>                                
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <br><br>
                            <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 45px">
                                <legend >Sexo:</legend>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="masculinoPassaporte" value="true" checked>Masculino
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="femininoPassaporte" value="true" checked>Feminino<br>
                            </fieldset>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
        
<div name="convite" id="convite">
    <table class="fmt">
        <tr valign="top">
            <td>
                <p class="legendaCodigo">Tipo:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('tiposConvite')" style="margin-top: 2px;" value=""/></span></p>
                <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                    <c:forEach var="tipoConvite" items="${tiposConvite}">
                        <input type="checkbox" name="tiposConvite" disabled value="${tipoConvite.cod}">${tipoConvite.descricao}<br>
                    </c:forEach>
                </div>
            </td>
            <td>
                <table class="fmt">
                    <tr>
                        <td>
                            <br><br>
                            <fieldset class="field-set legendaFrame recuoPadrao" style="width:125px;height: 50px">
                                <legend >CPF:</legend>
                                &nbsp;<input type="text" name="cpfConvidado" maxlength="11" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:90px">
                            </fieldset>                                
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <br>
                            <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 45px">
                                <legend >Sexo:</legend>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="convitePagoVendido" value="true" checked>Vendido
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="convitePagoNormal" value="true" checked>Normal<br>
                            </fieldset>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>


<div name="funcionario" id="funcionario">
    <table class="fmt">
        <tr valign="top">
            <td>
                <p class="legendaCodigo">Setor:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('setoresFuncionario')" style="margin-top: 2px;" value=""/></span></p>
                <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                    <c:forEach var="setor" items="${setores}">
                        <input type="checkbox" name="setoresFuncionario" disabled value="${setor.id}">${setor.descricao}<br>
                    </c:forEach>
                </div>
            </td>
            <td>
                <p class="legendaCodigo">Cargo:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('cargos')" style="margin-top: 2px;" value=""/></span></p>
                <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                    <c:forEach var="cargo" items="${cargos}">
                        <input type="checkbox" name="cargos" disabled value="${cargo.id}">${cargo.descricao}<br>
                    </c:forEach>
                </div>
            </td>
            <td>
                <br>
                <fieldset class="field-set legendaFrame recuoPadrao" style="width:240px;height: 45px">
                    <legend >Pago:</legend>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="funcionario" value="true" disabled checked>Funcionário
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="concessionario" value="true" disabled checked>Concecionário<br>
                </fieldset>
            </td>
        </tr>
    </table>
</div>

        
<div name="visitante" id="visitante">
    <p class="legendaCodigo">Setor:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('setoresVisitante')" style="margin-top: 2px;" value=""/></span></p>
    <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
        <c:forEach var="setor" items="${setores}">
            <input type="checkbox" name="setoresVisitante" disabled value="${setor.id}">${setor.descricao}<br>
        </c:forEach>
    </div>
</div>

        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
