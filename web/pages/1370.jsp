<%@include file="head.jsp"%>
<%@include file="menu.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    #pesquisa {
        margin-top: -150px;
        margin-left: -300px;
        left: 50%;
        top: 50%;
        position: fixed;
    }
    
</style>  

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $("#dataFim").mask("99/99/9999");
            
            $('#pesquisa').hide();
    });    
       
    function validarForm(){
        
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

        if(trim(document.forms[0].dataRef.value) == ''){
            alert('Informe a data de referência');
            return;
        }
        
        if(!isDataValida(document.forms[0].dataRef.value)){
            return;
        }
        
        $("#cartaDono").val("0");
        $("#cartaUsuario").val("0");
        $('#pesquisa').show();
        
    }
    
    function cancelaImpressao(){
        $('#pesquisa').hide();
    }
    
    function atualizaImpressao(){
        if ($("input[name='tipoRel']:checked").val()=='R'){
            $('#acao').val('visualizar');
        }else{
            if ($("#cartaDono").val()=="0" && $("#cartaUsuario").val()=="0"){
                alert('Selecione uma carta!');
                return false
            }
            $('#acao').val('carta');
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Cobrança</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1370">
	<input type="hidden" name="acao" id="acao" value="visualizar">
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:340px;width:250px;">
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
                                <p class="legendaCodigo MargemSuperior0" >Dt. Referência:</p>
                                <fmt:formatDate pattern="dd/MM/yyyy" var="dataRef" value="<%= new java.util.Date() %>"/>
                                <input type="text" name="dataRef" id="dataRef" class="campoSemTamanho alturaPadrao larguraData" value="${dataRef}">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                                    <legend >Título:</legend>
                                    &nbsp;<input type="text" name="tituloInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="tituloFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                                    <legend >Qt. Carnê não pago:</legend>
                                    &nbsp;<input type="text" name="qtInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="qtFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
                                    <legend >Valor não pago:</legend>
                                    &nbsp;<input type="text" name="valorInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
                                    &nbsp;&nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="valorFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>        
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao"  style="width:200px;height: 70px">
                                    <legend >Tipo:</legend>                
                                    <input type="radio" name="tipo" class="legendaCodigo" value="I" checked>INPC
                                    <br>
                                    <input type="radio" name="tipo" class="legendaCodigo" value="C">Comissão de Permanência
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                &nbsp;&nbsp;&nbsp;<input type="checkbox" name="mostrarProprietario" value="true">Mostrar Prop. com Usuário no Tít.<br>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <p class="legendaCodigo">Cursos:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('cursos')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:340px;width:250px;">
                        <c:forEach var="curso" items="${cursos}">
                            <input type="checkbox" name="cursos" class="legendaCodigo" value="${curso.id}">${curso.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
        </table>
        
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />  
        
        <div id="pesquisa" >
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Tipo de Documento</div>
                    <div class="divisoria"></div>
                    <table class="fmt" align="left" align="left">
                      <tr>
                          <td colspan="2">
                            <input type="radio" id="tipoRel" name="tipoRel" class="legendaCodigo" checked value="R"/>Relatório<br>
                            <input type="radio" id="tipoRel" name="tipoRel" class="legendaCodigo" value="C"/>Carta de Cobrança<br>

                        </td>
                      </tr>
                      <tr>
                        <td colspan="2">
                            <br>
                            <p class="legendaCodigo MargemSuperior0">Para o Proprietário do Título com Usuário:</p>
                            <div class="selectheightnovo">
                             <select name="cartaDono" id="cartaDono" class="campoSemTamanho alturaPadrao" style="width:340px;">
                                 <option value="0">&lt;NENHUMA&gt;<br>
                                 <c:forEach var="carta" items="${cartas}">
                                     <option value="${carta.descricao}">${carta.descricao}</option>
                                 </c:forEach>
                             </select>
                            </div>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="2">
                            <p class="legendaCodigo MargemSuperior0">Para o Usuário e Proprietário:</p>
                            <div class="selectheightnovo">
                             <select name="cartaUsuario" id="cartaUsuario" class="campoSemTamanho alturaPadrao" style="width:340px;">
                                 <option value="0">&lt;NENHUMA&gt;<br>
                                 <c:forEach var="carta" items="${cartas}">
                                     <option value="${carta.descricao}">${carta.descricao}</option>
                                 </c:forEach>
                             </select>
                            </div>
                        </td>
                      </tr>
                      <tr>
                        <tr>
                            <td>
                                <br>
                                <input style="margin-left:15px;" type="button" id="cmdatualizar" name="cmdPesquisa" class="botaoatualizar" onclick="atualizaImpressao()" />
                            </td>
                            <td>
                                <br>
                                <input type="button" style="margin-top:-5px;" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaImpressao()" />
                            </td>
                        </tr>
                      </tr>
                    </table>  
                </td>
              </tr>
            <table>
        </div>        
        
    </form>

</body>
</html>
