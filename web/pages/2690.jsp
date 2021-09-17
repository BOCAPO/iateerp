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

        document.forms[0].submit();
    }

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Endereços Repetidos</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2690">
	<input type="hidden" name="acao" value="visualizar">
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('categorias')" style="margin-top: -20px;" value=""/></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:420px;width:227px;">
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
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:150px;">
                                    <legend >Tipo de Endereço</legend>                
                                    <br>
                                    <input type="radio" name="tipoEndereco" class="legendaCodigo" value="R" checked>Residencial
                                    <br><br>
                                    <input type="radio" name="tipoEndereco" class="legendaCodigo" value="C">Comercial
                                    <br><br>
                                    <input type="radio" name="tipoEndereco" class="legendaCodigo" value="R">Carne
                                    <br><br>
                                    <input type="radio" name="tipoEndereco" class="legendaCodigo" value="P">Correspondencia
                                    <br>
                                    &nbsp;
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:150px;">
                                    <legend >Opções:</legend>
                                    <br>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="endereco" value="true" checked>Endereço
                                    <br><br>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="bairro" value="true" checked>Bairro
                                    <br><br>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="cidade" value="true" checked>Cidade
                                    <br><br>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="uf" value="true" checked>UF
                                    <br><br>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="cep" value="true" checked>CEP
                                    <br>
                                    &nbsp;
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
