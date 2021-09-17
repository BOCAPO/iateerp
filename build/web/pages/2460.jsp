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
            $("#iniBaixa").mask("99/99/9999");
            $("#fimBaixa").mask("99/99/9999");
            $("#iniPgto").mask("99/99/9999");
            $("#fimPgto").mask("99/99/9999");
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
        
        if(trim(document.forms[0].iniBaixa.value) != '' || trim(document.forms[0].fimBaixa.value) != ''){
            if(trim(document.forms[0].iniBaixa.value) == ''){
                alert('Informe a data de início de baixa');
                $("#iniBaixa").focus();
                return;
            }
            if(!isDataValida(document.forms[0].iniBaixa.value)){
                $("#iniBaixa").focus();
                return;
            }
            if(trim(document.forms[0].fimBaixa.value) == ''){
                alert('Informe a data de fim de baixa');
                $("#fimBaixa").focus();
                return;
            }
            if(!isDataValida(document.forms[0].fimBaixa.value)){
                $("#fimBaixa").focus();
                return;
            }
        }
            
        if(trim(document.forms[0].iniPgto.value) != '' || trim(document.forms[0].fimPgto.value) != ''){
            if(trim(document.forms[0].iniPgto.value) == ''){
                alert('Informe a data de início de Pagamento');
                $("#iniPgto").focus();
                return;
            }
            if(!isDataValida(document.forms[0].iniPgto.value)){
                $("#iniPgto").focus();
                return;
            }
            if(trim(document.forms[0].fimPgto.value) == ''){
                alert('Informe a data de fim de pagamento');
                $("#fimPgto").focus();
                return;
            }
            if(!isDataValida(document.forms[0].fimPgto.value)){
                $("#fimPgto").focus();
                return;
            }
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Saldo de Crédito</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2460">
	<input type="hidden" name="acao" value="visualizar">
    
        <br>
            
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo" style="margin-top:-15px">Dt. Ini. Cobrança</p>
                    <input type="text" id="iniVenc" name="iniVenc" class="campoSemTamanho alturaPadrao larguraData">        
                </td>
                <td>
                    <p class="legendaCodigo" style="margin-top:-15px">Dt. Fim Cobrança</p>
                    <input type="text" id="fimVenc" name="fimVenc" class="campoSemTamanho alturaPadrao larguraData">        
                </td>
            </tr>
            <tr>
                <td>
                    &nbsp;
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo" style="margin-top:-15px">Dt. Ini. Baixa</p>
                    <input type="text" id="iniBaixa" name="iniBaixa" class="campoSemTamanho alturaPadrao larguraData">        
                </td>
                <td>
                    <p class="legendaCodigo" style="margin-top:-15px">Dt. Fim Baixa</p>
                    <input type="text" id="fimBaixa" name="fimBaixa" class="campoSemTamanho alturaPadrao larguraData">        
                </td>
            </tr>
            <tr>
                <td>
                    &nbsp;
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo" style="margin-top:-15px">Dt. Ini. Pagamento</p>
                    <input type="text" id="iniPgto" name="iniPgto" class="campoSemTamanho alturaPadrao larguraData">        
                </td>
                <td>
                    <p class="legendaCodigo" style="margin-top:-15px">Dt. Fim Pagamento</p>
                    <input type="text" id="fimPgto" name="fimPgto" class="campoSemTamanho alturaPadrao larguraData">        
                </td>
            </tr>
            <tr>
                <td>
                    &nbsp;
                </td>
            </tr>
        </table>
                    
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
