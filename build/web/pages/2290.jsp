<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");

    });
    
    function validarForm(){
        if(trim(document.forms[0].dataInicio.value) == ''
                || trim(document.forms[0].dataFim.value) == ''){
            alert('É preciso preencher o período do relatório');
            return;
        }
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Declaração de Quitação Enviado no Boleto</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2290">
	<input type="hidden" name="acao" value="visualizar">
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Matrícula</p>
                    <input class="campoSemTamanho alturaPadrao larguraNumeroPequeno" type="text" name="matricula"/>
                </td>
                <td>
                    <p class="legendaCodigo">Categoria</p>
                    <input class="campoSemTamanho alturaPadrao larguraNumeroPequeno" type="text" name="categoria" onkeypress="onlyNumber(event)"/>
                </td>
                <td>
                    <p class="legendaCodigo">Nome</p>
                    <input class="campoSemTamanho alturaPadrao" type="text" name="nome" style="width: 290px"/>
                </td>
            </tr>
        </table>
        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:50px;margin-top: 10px">
                        <legend >Emissão:</legend>
                        <input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                        &nbsp;&nbsp;&nbsp;a
                        <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                    </fieldset>
                </td>
                <td>
                    <p class="legendaCodigo">Ano Ref:</p>
                    <input type="text" name="anoRef" maxlength="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">      
                </td>
                <td>
                    <p class="legendaCodigo">Arquivo:</p>
                    <input type="text" name="arquivo" class="campoSemTamanho alturaPadrao larguraBairro">              
                </td>
            </tr>
        </table>
        


        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
