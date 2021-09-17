<%@include file="head.jsp"%>

<style>
    table {border:none;width:700px;margin-left:15px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>        
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
            $("#dataRef").mask("99/99/9999");
    });
</script>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(!verifica_branco(document.forms[0].dataRef.value)){
           alert("É Obrigatório informar a Data!");
           return false;
        }

        if(!isDataValida(document.forms[0].dataRef.value)){
            return false; 
        }

        if(document.forms[0].MatIni.value != '' ){
            if (isNaN(document.forms[0].MatIni.value)) {    
                   alert("Matrícula Inicial inválida!");    
                   return false;    
            } 
        }
        if(document.forms[0].MatFim.value != '' ){
            if (isNaN(document.forms[0].MatFim.value)) {    
                   alert("Matrícula Final inválida!");    
                   return false;    
            } 
        }

        var selecionado = false;
        for(i=0; i < document.forms[0].length; i++){
            if (document.forms[0].elements[i].type == "checkbox" &&  document.forms[0].elements[i].name == "pCategoria" ) {
                if(document.forms[0].elements[i].checked) {
                    selecionado = true;
                }                    
            }    
        } 
        
        if (!selecionado){
            alert("Selecione pelo menos uma categoria!");    
            return false;    
        }
        
        document.getElementById("botao").style.display = "none";
        
        document.forms[0].submit();
    }

</script>


<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
        <div id="titulo-subnav">Geração de Carne Mensal</div>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gera"/>
        <input type="hidden" name="tipo" value="M"/>

        <table align="left">
          <tr>
            <td colspan="2">
                <p class="legendaCodigo">Categoria<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('pCategoria')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                <div style="overflow:auto;height:200px;">
                    <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="pCategoria" class="campoSemTamanho" value="${categoria.id}">${categoria.descricao}<br>
                    </c:forEach>    
                </div>
            </td>
            <td width="350px" colspan="2">
                <p class="legendaCodigo">Data</p>
                <input type="text" name="dataRef" id="dataRef" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" />
                <p class="legendaCodigo">Matrícula Inicial: </p>
                <input type="text" name="MatIni" maxlength="4" class="campoSemTamanho alturaPadrao larguraData" />
                <p class="legendaCodigo">Matrícula Final: </p>
                <input type="text" name="MatFim" maxlength="4" class="campoSemTamanho alturaPadrao larguraData" />
            </td>
          </tr>
          <tr>
            <td>
                <br>
                <div id="botao">
                    <input type="button" class="botaoGerar"  onclick="validarForm()" value=" " />
                </div>        
            </td>
          </tr>
        </table>  

    </form>

</body>
</html>
