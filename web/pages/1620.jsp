<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">    
   $(document).ready(function () {
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
           
    });  
    
    function validarForm(){
        if(trim(document.forms[0].tituloInicio.value) == ''){
            alert('É preciso preencher o titulo inicial para o relatório');
            return;
        }
        if(parseInt(document.forms[0].tituloInicio.value) > parseInt(document.forms[0].tituloFim.value)){
            alert('Título inicial deve ser menor que Título Final');
            return;
        }
            
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Títulos que não Geram Taxas</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1620">
	<input type="hidden" name="acao" value="visualizar">

        <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height: 50px">
            <legend >Título:</legend>
            &nbsp;<input type="text" name="tituloInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">
            &nbsp;&nbsp;&nbsp;&nbsp;a
            <input type="text" name="tituloFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
        </fieldset>
        <br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
