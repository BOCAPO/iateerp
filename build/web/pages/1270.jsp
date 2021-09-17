<%@include file="head.jsp"%>
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
            
            $("#nascimentoInicio").mask("99/99/9999");
            $("#nascimentoFim").mask("99/99/9999");
            $("#dataUniversitario").mask("99/99/9999");
    });    

    function visualizar(){
        document.forms[0].acao.value = 'visualizar';
        validarForm();
    }
        
    function gerarEtiqueta(){
        document.forms[0].acao.value = 'gerarEtiqueta';
        validarForm();
    }    
    
    function enableDisableTodos(){
        var enabled = document.forms[0].todos.checked;
   
        document.forms[0].titular.disabled = enabled;
        for(var i = 0; i < document.forms[0].dependentes.length; i++){
            document.forms[0].dependentes[i].disabled = enabled;

        }
    }
    
    function enableDisableUniversitario(){
        var enabled = !document.forms[0].universitario.checked;
   
        document.forms[0].dataUniversitario.disabled = enabled;
    }    
    
    function validarForm(){
        if(!isDataValida(document.forms[0].nascimentoInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].nascimentoFim.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataUniversitario.value)){
            return;
        }        
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório Genérico de Sócio</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1270">
	<input type="hidden" name="acao">
                     
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Categorias:</p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo">Profissoes:</p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                        <c:forEach var="profissao" items="${profissoes}">
                            <input type="checkbox" name="profissoes" value="${profissao.id}">${profissao.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo">Cargos Especiais:</p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:227px;">
                        <c:forEach var="cargoEspecial" items="${cargosEspeciais}">
                            <input type="checkbox" name="cargosEspeciais" value="${cargoEspecial.id}">${cargoEspecial.descricao}<br>
                        </c:forEach>
                    </div>
                </td>                
            </tr>        
        </table>
        
        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:240px;height:245px;">
                        <legend >Tipo de dependentes:</legend>
                        <input type="checkbox" name="todos"  onchange="enableDisableTodos()" value="true"><span class="zera">Todos</span><BR>
                        &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="titular" value="true"><span class="zera">Titular</span>

                        <div class="legendaCodigo" style="padding-top:0px;">Dependentes:</div>
                        <div class="recuoPadrao" style="overflow:auto;height:150px;width:210px;">
                            
                            <c:forEach var="dependente" items="${dependentes}">
                                <input type="checkbox" name="dependentes" value="${dependente.id}"><span class="zera">${dependente.descricao}</span><br>
                            </c:forEach>
                        </div>
                    </fieldset>
                </td>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:50px;">
                                    <legend >CEP:</legend>
                                    <input type="text" name="cepInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">      
                                    &nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="cepFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:50px;">
                                    <legend >Nascimento:</legend>
                                    <input type="text" id="nascimentoInicio" name="nascimentoInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                    &nbsp;&nbsp;&nbsp;a
                                    <input type="text" id="nascimentoFim"  name="nascimentoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:50px;">
                                    <legend >Título:</legend>
                                    <input type="text" name="tituloInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                                    &nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="tituloFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </fieldset>                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao">
                                    <legend >Sexo:</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="masculino" value="true" checked>Masculino
                                    &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="feminino" value="true" checked>Feminino<br>
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao"style="width:250px;height:45px;">
                                    <legend >Dados:</legend>
                                    &nbsp;&nbsp;&nbsp;<input type="checkbox" name="imprimirTelefone" value="true">Imprimir Telefone
                                    &nbsp;&nbsp;&nbsp;<input type="checkbox" name="imprimirEmail" value="true">Imprimir Email      
                                </fieldset>      
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:250px;height:95px;">
                                    <legend >Situação:</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="normal" value="true" checked>Normal &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="usuarioTitulo" value="true" >Usuário no Título<br>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="deficiente" value="true">Deficiente<br>        
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="universitario" onchange="enableDisableUniversitario()" value="true">Universitário
                                    <input type="text" name="dataUniversitario" id="dataUniversitario" disabled class="campoSemTamanho alturaPadrao larguraData">        
                                </fieldset>      
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width: 250px;">
                                    <legend >Ordenação:</legend>        

                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <div class="legendaCodigo" style="padding-top:0px;">1º:</div>
                                            </td>
                                            <td>
                                                <div class="legendaCodigo" style="padding-top:0px;">2º:</div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <select name="ordenacao1" class="campoSemTamanho alturaPadrao" style="width:100px">
                                                    <option value="Nome">Nome</option>
                                                    <option value="Titulo">Titulo</option>
                                                </select>                
                                            </td>
                                            <td>
                                                <select name="ordenacao2" class="campoSemTamanho alturaPadrao" style="width:100px">
                                                    <option value="Nome">Nome</option>
                                                    <option value="Titulo">Titulo</option>
                                                </select>                        
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

        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width: 240px;height: 50px">
                        <legend >Espólio:</legend>        

                        <select name="espolio" class="campoSemTamanho alturaPadrao" style="width:210px">
                            <option value="T">Todos</option>
                            <option value="S">Espólio</option>
                            <option value="N">Não Espólio</option>
                        </select>                
                    </fieldset>      
                </td>
            </tr>
        </table>
        
        <input type="button" onclick="visualizar()" class="botaoatualizar" value=" " />        
        <input type="button" onclick="gerarEtiqueta()" class="botaoImprimirEtiquetas"  value=" " />
        
    </form>

</body>
</html>
