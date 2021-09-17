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
        
        k = 0;
        for(var i = 0; i < document.forms[0].locaisAcesso.length; i++){
            if(document.forms[0].locaisAcesso[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos um local de acesso.');
            return;
        }

        k = 0;
        for(var i = 0; i < document.forms[0].documentos.length; i++){
            if(document.forms[0].documentos[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos um tipo de documento.');
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Estatísticas de Acesso ao Clube</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1480">
	<input type="hidden" name="acao" value="visualizar">

        <table class="fmt">
            <tr>
                <td>
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
                                <p class="legendaCodigo">Local de Acesso:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('locaisAcesso')" style="margin-top: 2px;" value=""/></span></p>
                                <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                                    <c:forEach var="l" items="${locaisAcesso}">
                                        <input type="checkbox" name="locaisAcesso" value="${l.id}">${l.descricao}<br>
                                    </c:forEach>
                                </div>
                            </td>
                            <td>
                                <p class="legendaCodigo">Documentos:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('documentos')" style="margin-top: 2px;" value=""/></span></p>
                                <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                                    <input type="checkbox" name="documentos" value="1">CARTEIRA><br>
                                    <input type="checkbox" name="documentos" value="3">CONVITE><br>
                                    <input type="checkbox" name="documentos" value="5">CRACHÁ DE VISITANTE><br>
                                    <input type="checkbox" name="documentos" value="7">FUNCIONÁRIO><br>
                                    <input type="checkbox" name="documentos" value="2">PASSAPORTE><br>
                                    <input type="checkbox" name="documentos" value="4">PERMISSÃO PROVISÓRIA><br>
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
