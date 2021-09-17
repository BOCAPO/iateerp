<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style> 

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $('#dtReferencia').mask('99/99/9999');
    });  
   
    
    function validarForm(){        
        if ($('#dtReferencia').val()==''){
            alert('Informe a Data de Referencia!');
            return;
        }
        
        if(!isDataValida(document.forms[0].dtReferencia.value)){
            return;
        }

        if (!$('#titular').attr('checked') && !$('#dependente').attr('checked')){
            alert('Selecione pelo menos um tipo (Titular ou Dependente)!');
            return;
        }
        
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
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Geral de Sócio</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2630">
        <input type="hidden" name="acao" value="visualizar">
        <table class="fmt">
            <tr style="vertical-align: top">
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:130px;height: 50px">
                                    <legend >Dt. Referência:</legend>
                                    &nbsp;<input type="text" name="dtReferencia" id="dtReferencia" class="campoSemTamanhoSemMargem alturaPadrao larguraData" style="margin-left: 10px">    
                                </fieldset>                                
                            </td>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:165px;height: 50px">
                                    <legend >Tipo:</legend>
                                    &nbsp;&nbsp;&nbsp;<input type="checkbox" name="titular" id="titular" value="true" checked style="margin-top: 8px">Titular
                                    &nbsp;&nbsp;&nbsp;<input type="checkbox" name="dependente" id="dependente" value="true" checked>Dependente<br>
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr style="vertical-align: top">
            </tr>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:350px;width:315px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
        </table>
        <br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
    </form>

</body>
</html>
