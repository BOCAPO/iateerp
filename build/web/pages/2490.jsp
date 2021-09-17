<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  


<script type="text/javascript" language="JavaScript">    
    
    function validarForm(){
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Alunos por Faixa Etária</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2490">
	<input type="hidden" name="acao" value="visualizar">

        <table class="fmt">
            <tr>
                <td style="vertical-align: top;">
                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width: 130px">
                        <legend >Tipo:</legend>
                        <br>
                        &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" value="S" checked>Sintético<br><br>
                        &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" value="A">Analítico<br><br>
                    </fieldset>      
                    
                </td>
            </tr>        
        </table>

        
        <br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
