<%@include file="head.jsp"%>
<%@include file="menu.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style> 

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
            $("#validade").mask("99/99/9999");
            
    });    
    
    function validarForm(){
       
       if(trim(document.forms[0].validade.value) != ''){
            if(!isDataValida(document.forms[0].validade.value)){
                return;
            }           
       }
                
       document.forms[0].submit();
    }

</script>

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Permissão Provisória</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2230">
	<input type="hidden" name="acao" value="visualizar">
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0"  >Nome</p>
                    <input type="text" name="nome" class="campoSemTamanho alturaPadrao larguraNumero" style="width:260px;">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Atividade</p>
                    <input type="text" name="atividade" class="campoSemTamanho alturaPadrao larguraNumero" style="width:260px;">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0 larguraData" >Validade</p>
                    <input type="text" id="validade" name="validade" class="campoSemTamanho alturaPadrao larguraData">
                </td>
            </tr>
        </table>
        
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
