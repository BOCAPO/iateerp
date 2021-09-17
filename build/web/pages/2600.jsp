<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
            $("#dtReferenciaPessoa").mask("99/99/9999");
            $("#dtReferenciaCarne").mask("99/99/9999");
            $("#dtVencimentoCarne").mask("99/99/9999");
            

    });
    
    function validarForm(){
        if(trim(document.forms[0].dtReferenciaPessoa.value) == ''){
            alert('É preciso preencher a Data de Referência do cadastro de pessoas');
            return;
        }
        if(!isDataValida(document.forms[0].dtReferenciaPessoa.value)){
            return;
        }
        if(trim(document.forms[0].dtReferenciaCarne.value) == ''){
            alert('É preciso preencher a Data de Referência dos Carnês');
            return;
        }
        if(!isDataValida(document.forms[0].dtReferenciaCarne.value)){
            return;
        }
        if(trim(document.forms[0].dtVencimentoCarne.value) == ''){
            alert('É preciso preencher a Data de Vencimento dos Carnês');
            return;
        }
        if(!isDataValida(document.forms[0].dtVencimentoCarne.value)){
            return;
        }
        
        document.forms[0].submit();
    }
    
    
</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório do Quadro Social x Financeiro</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2600">
	<input type="hidden" name="acao" value="visualizar">
                     
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Dt. Ref. Pessoa</p>
                    <input type="text" id="dtReferenciaPessoa" name="dtReferenciaPessoa" style='width:92px;' class="campoSemTamanho alturaPadrao">        
                </td>
                <td>
                    <p class="legendaCodigo">Dt. Ref. Carnê</p>
                    <input type="text" id="dtReferenciaCarne" name="dtReferenciaCarne" style='width:92px;' class="campoSemTamanho alturaPadrao">        
                </td>
                <td>
                    <p class="legendaCodigo">Dt. Venc. Carnê</p>
                    <input type="text" id="dtVencimentoCarne" name="dtVencimentoCarne" style='width:92px;' class="campoSemTamanho alturaPadrao">        
                </td>
            </tr>
        </table>
        
        <br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
