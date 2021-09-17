<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
    });

    function validarForm(){
        var k = 0;
        for(var i = 0; i < document.forms[0].situacoes.length; i++){
            if(document.forms[0].situacoes[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma situação.');
            return;
        }
        
        if(trim(document.forms[0].dataInicio.value) == ''
            || trim(document.forms[0].dataFim.value) == ''){
            alert('É preciso preencher as datas de início e fim')
            return;            
        }
    
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }
        
        if(document.forms[0].ordenacao1.value == document.forms[0].ordenacao2.value){
            alert('Ordenação não pode ser a mesma para o primeiro e segundo critério!')
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Autorização de Embarque</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1660">
        <input type="hidden" name="acao" value="visualizar">
        
        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:110px;width:270px;">
                        <legend >Data:</legend>
                        <table class="fmt">
                            <tr>
                                <td align="center">
                                    <input type="radio" name="tipoData" class="legendaCodigo" checked style="margin-top:15px;" value="E" checked>Emissão
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="radio" name="tipoData" class="legendaCodigo" style="margin-top:15px;" value="V">Validade
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <br>
                                    <input type="text" name="dataInicio" id="dataInicio" class="campoSemTamanho alturaPadrao larguraData">    
                                    &nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="dataFim" id="dataFim" class="campoSemTamanho alturaPadrao larguraData">        
                                </td>
                            </tr>
                        </table>
                    </fieldset>                                
                    
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:110px;width:150px;">
                        <legend >Situação:</legend>
                        <div class="recuoPadrao" >
                            <input type="checkbox" name="situacoes" checked style="margin-top:7px;" value="NU">Não Utilizada<br>
                            <input type="checkbox" name="situacoes" checked style="margin-top:7px;" value="UT">Utilizada<br>
                            <input type="checkbox" name="situacoes" checked style="margin-top:7px;" value="CA">Cancelada<br>
                        </div>        
                    </fieldset>                                
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:110px;width:230px;">
                        <legend >Ordenação:</legend>        
                    <p class="legendaCodigo MargemSuperior0">1º:</p>
                    <select id="ordenacao1" name="ordenacao1" class="campoSemTamanho alturaPadrao" style="width:200px">
                        <option value="1">Nome do Sócio</option>
                        <option value="2">Nome do Convidado</option>
                        <option value="3">Nome da Embarcação</option>
                        <option value="4">Dt. Emissão</option>
                        <option value="5">Dt. Validade</option>
                        <option value="6">Dt. Utilização</option>
                    </select>                
                    <p class="legendaCodigo MargemSuperior0">2º:</p>
                    <select id="ordenacao2" name="ordenacao2" class="campoSemTamanho alturaPadrao" style="width:200px">
                        <option value="1">Nome do Sócio</option>
                        <option value="2">Nome do Convidado</option>
                        <option value="3">Nome da Embarcação</option>
                        <option value="4">Dt. Emissão</option>
                        <option value="5">Dt. Validade</option>
                        <option value="6">Dt. Utilização</option>            
                    </select>
                    </fieldset>      
                    
                </td>
            </tr>
        </table>
            
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
