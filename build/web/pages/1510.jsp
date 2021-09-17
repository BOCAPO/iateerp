<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $("#vigencia").mask("99/99/9999");
    });
    
    function validarForm(){
        if(trim(document.forms[0].vigencia.value) == ''){
            alert('Informe a data de vigência');
            return;
        }
        if(!isDataValida(document.forms[0].vigencia.value)){
            return;
        }
            
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Taxas</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1510">
	<input type="hidden" name="acao" value="visualizar">
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Taxa Vigente em:</p>
                    <input type="text" id="vigencia" name="vigencia" class="campoSemTamanho alturaPadrao larguraData">        
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:405px;height:45px;margin-top:11px">
                        <legend>Tipo:</legend>
                        <input type="radio" name="tipo" class="legendaCodigo" value="A" checked>Taxas Administrativas
                        <input type="radio" name="tipo" class="legendaCodigo" value="C">Taxas de Curso
                        <input type="radio" name="tipo" class="legendaCodigo" value="M">Taxas Náuticas
                    </fieldset>
                </td>
            </tr>
        </table>
                     
        
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
