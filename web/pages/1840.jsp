<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $("#iniVenc").mask("99/99/9999");
            $("#fimVenc").mask("99/99/9999");
    });
    
    function validarForm(){
        if(trim(document.forms[0].iniVenc.value) == ''){
            alert('Informe a data de início do vencimento');
            return;
        }
        if(!isDataValida(document.forms[0].iniVenc.value)){
            return;
        }
        if(trim(document.forms[0].fimVenc.value) == ''){
            alert('Informe a data de fim do vencimento');
            return;
        }
        if(!isDataValida(document.forms[0].fimVenc.value)){
            return;
        }
            
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Demonstrativo de Arrecadação</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1840">
	<input type="hidden" name="acao" value="visualizar">
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo" style="margin-top:-15px">Dt. Ini. Venc.</p>
                    <input type="text" id="iniVenc" name="iniVenc" class="campoSemTamanho alturaPadrao larguraData">        
                </td>
                <td>
                    <p class="legendaCodigo" style="margin-top:-15px">Dt. Fim Venc.</p>
                    <input type="text" id="fimVenc" name="fimVenc" class="campoSemTamanho alturaPadrao larguraData">        
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Ordenação:</p>
                    <select name="ordenacao" class="campoSemTamanho alturaPadrao" style="width:100px">
                        <option value="C">Categoria</option>
                        <option value="T">Taxa</option>
                    </select>        
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:180px;height:45px;">
                        <legend >Tipo:</legend>        
                        <input type="radio" name="tipo" class="legendaCodigo" value="S">Sintético
                        <input type="radio" name="tipo" class="legendaCodigo" value="A" checked>Analítico
                    </fieldset>
                </td>
                <td>
                    <input type="checkbox"  class="field-set legendaFrame recuoPadrao legendaCodigo"  style="margin-top:15px" name="incluirCursos" value="true">Incluir Cursos
                </td>
                <td>
                    <input type="checkbox"  class="field-set legendaFrame recuoPadrao legendaCodigo"  style="margin-top:15px" name="incluirExcluidos" value="true">Incluir Excluídos
                </td>
            </tr>
        </table>
                    
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
