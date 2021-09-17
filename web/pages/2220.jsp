<%@include file="head.jsp"%>
<%@include file="menu.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">    
    
    function validarForm(){
       
       if(!document.forms[0].socio.checked
               && !document.forms[0].geral.checked){
           alert('Selecione pelo menos 1 tipo.');
           return;
       }
                
       document.forms[0].submit();
    }

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Cadastro de Carros</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2220">
	<input type="hidden" name="acao" value="visualizar">
        
        <fieldset class="field-set legendaFrame recuoPadrao" style="width:160px;height:45px">
            <legend >Situação:</legend>                
            &nbsp;&nbsp;<input type="checkbox" name="socio" class="legendaCodigo" value="true" checked>Sócio
            &nbsp;&nbsp;<input type="checkbox" name="geral" class="legendaCodigo" value="true" checked>Geral
        </fieldset>
        <br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
