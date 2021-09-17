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
    });    
    
    function enableDisable(tipo){

        var carteiraEnabled = !document.forms[0].imprimir[0].checked;
   
        for(var i = 0; i < document.forms[0].categorias.length; i++){
            document.forms[0].categorias[i].disabled = carteiraEnabled;
        }
        
        var conviteEnabled = !document.forms[0].imprimir[1].checked;
   
        for(var i = 0; i < document.forms[0].tiposConvite.length; i++){
            document.forms[0].tiposConvite[i].disabled = conviteEnabled;

        }

        var passaporteEnabled = !document.forms[0].imprimir[2].checked;
   
        for(var i = 0; i < document.forms[0].cursos.length; i++){
            document.forms[0].cursos[i].disabled = passaporteEnabled;

        }

            $("#carteira").hide();
            $("#passaporte").hide();
            $("#convite").hide();
            
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

        }
        
        if(document.forms[0].imprimir[1].checked){
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
            
        }
        
        if(document.forms[0].imprimir[2].checked){
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
            
        }
                
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Documentos Emitidos</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1400">
	<input type="hidden" name="acao" value="visualizar">

        
        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px;">
                        <legend >Período:</legend>        
                        &nbsp;<input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero">
                        &nbsp;&nbsp;&nbsp;&nbsp;a
                        <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                    </fieldset>
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:175px;height: 50px;">
                        <legend >Tipo</legend>        
                        <input type="radio" name="tipo" class="legendaCodigo" style="margin-top:7px" value="S" checked>Sintético
                        <input type="radio" name="tipo" class="legendaCodigo" style="margin-top:7px" value="A">Analítico
                    </fieldset>
                </td>
            </tr>
        </table>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Dados</div>
        <div class="divisoria"></div>

        <fieldset class="field-set legendaFrame recuoPadrao" style="width:655px;height: 50px;">
            <legend >Tipo:</legend>                
            &nbsp;<input type="radio" name="imprimir" onclick="enableDisable('#carteira')" class="legendaCodigo" value="carteira" checked>Carteira
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="imprimir" onclick="enableDisable('#convite')" class="legendaCodigo" value="convite">Convite
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="imprimir" onclick="enableDisable('#passaporte')" class="legendaCodigo" value="passaporte">Passaporte
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="imprimir" onclick="enableDisable('#funcionario')" class="legendaCodigo" value="crachaFuncionario">Crachá de Funcinonário
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="imprimir" onclick="enableDisable('#funcionario')" class="legendaCodigo" value="crachaConcessionario">Crachá de Concessionário
        </fieldset>

            
        <div id="carteira">
            <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value=""/></span></p>
            <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                <c:forEach var="categoria" items="${categorias}">
                    <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                </c:forEach>
            </div>
        </div>        
        
        <div id="convite">
            <p class="legendaCodigo">Tipo:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('tiposConvite')" style="margin-top: 2px;" value=""/></span></p>
            <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                <c:forEach var="tipoConvite" items="${tiposConvite}">
                    <input type="checkbox" name="tiposConvite" disabled value="${tipoConvite.cod}">${tipoConvite.descricao}<br>
                </c:forEach>
            </div>        
        </div>        


        <div id="passaporte">
            <p class="legendaCodigo">Cursos:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('cursos')" style="margin-top: 2px;" value=""/></span></p>
            <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                <c:forEach var="curso" items="${cursos}">
                    <input type="checkbox" name="cursos" disabled value="${curso.id}">${curso.descricao}<br>
                </c:forEach>
            </div>
        </div>        

        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
