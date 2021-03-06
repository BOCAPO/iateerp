<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style> 

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $('#vencimento').mask('99/99/9999');

    });  
   
    function validarForm(){        
        var k = 0;
        
        if (!document.forms[0].empresarial.checked){
            for(var i = 0; i < document.forms[0].categorias.length; i++){
                if(document.forms[0].categorias[i].checked){
                    k++;
                }
            }
            if(k == 0){
                alert('Selecione pelo menos uma 15Categoria!');
                return;
            }
        }
        
        if(trim(document.forms[0].tituloInicio.value) == ''){
            alert('? preciso informar a matricula inicial');
            return;
        }        
        if(trim(document.forms[0].tituloFim.value) == ''){
            alert('? preciso informar a matricula final');
            return;
        }        
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Mapa de S?cios com Direito a Voto</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1910">
	<input type="hidden" name="acao" value="visualizar">
        <table class="fmt">
            <tr valign="top">
                <td>
                    <p class="legendaCodigo">Categorias:</p>
                    <div class="recuoPadrao" style="overflow:auto;height:220px;width:200px;">
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
                                &nbsp;&nbsp;&nbsp;<input type="checkbox" name="empresarial" value="true">Empresarial e Usu?rios
                            </td>
                        </tr>                        
                        <tr>
                            <td>
                                <br><br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:140px;height:155px">
                                    <legend >Matr?cula:</legend>
                                    <p class="legendaCodigo">Inicial:</p>
                                    <input type="text" name="tituloInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraData">
                                    <p class="legendaCodigo">Final:</p>
                                    <input type="text" name="tituloFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraData">        
                                </fieldset>                                
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0">T?tulo1:</p>
                    <input type="text" name="titulo" class="campoSemTamanho alturaPadrao larguraNumero" style="width:360px;">
                    <br>
                    <p class="legendaCodigo MargemSuperior0">T?tulo2:</p>                                    
                    <input type="text" name="titulo2" class="campoSemTamanho alturaPadrao larguraNumero" style="width:360px;">        
                </td>
            </tr>                        
        </table>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
    </form>

</body>
</html>
