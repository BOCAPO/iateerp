<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
            $("#dtReferencia").mask("99/99/9999");
            $("#carneVencInicio").mask("99/99/9999");
            $("#carneVencFim").mask("99/99/9999");            
            $("#carneGeracaoInicio").mask("99/99/9999");
            $("#carneGeracaoFim").mask("99/99/9999");            
            $("#boletoVencInicio").mask("99/99/9999");
            $("#boletoVencFim").mask("99/99/9999");            
            $("#boletoGeracaoInicio").mask("99/99/9999");
            $("#boletoGeracaoFim").mask("99/99/9999");            
            $("#movCaixaInicio").mask("99/99/9999");
            $("#movCaixaFim").mask("99/99/9999");            
            $("#credPreInicio").mask("99/99/9999");
            $("#credPreFim").mask("99/99/9999");            
            $("#debPreInicio").mask("99/99/9999");
            $("#debPreFim").mask("99/99/9999");            
            $("#prodServInicio").mask("99/99/9999");
            $("#prodServFim").mask("99/99/9999");            
            trataMostrarCarne();
            trataMostrarBoleto();
            trataMostrarMovCaixa();
            trataMostrarProdServ();
            trataMostrarCredPre();
            trataMostrarDebPre();
            
            $('#mostrarCarne').change(function() {
                trataMostrarCarne();
            })
            $('#mostrarBoleto').change(function() {
                trataMostrarBoleto();
            })
            $('#mostrarMovCaixa').change(function() {
                trataMostrarMovCaixa();
            })
            $('#mostrarProdServ').change(function() {
                trataMostrarProdServ();
            })
            $('#mostrarCredPre').change(function() {
                trataMostrarCredPre();
            })
            $('#mostrarDebPre').change(function() {
                trataMostrarDebPre();
            })

    });
    
    function trataMostrarCarne(){
        if (document.forms[0].mostrarCarne.checked){
            $("#carneVencInicio").prop("disabled", false);
            $("#carneVencFim").prop("disabled", false);
            $("#carneGeracaoInicio").prop("disabled", false);
            $("#carneGeracaoFim").prop("disabled", false);
        }else{
            $("#carneVencInicio").prop("disabled", true);
            $("#carneVencFim").prop("disabled", true);
            $("#carneGeracaoInicio").prop("disabled", true);
            $("#carneGeracaoFim").prop("disabled", true);
            $("#carneVencInicio").val('');
            $("#carneVencFim").val('');
            $("#carneGeracaoInicio").val('');
            $("#carneGeracaoFim").val('');
            
        }
    }
    function trataMostrarBoleto(){
        if (document.forms[0].mostrarBoleto.checked){
            $("#boletoVencInicio").prop("disabled", false);
            $("#boletoVencFim").prop("disabled", false);
            $("#boletoGeracaoInicio").prop("disabled", false);
            $("#boletoGeracaoFim").prop("disabled", false);
        }else{
            $("#boletoVencInicio").prop("disabled", true);
            $("#boletoVencFim").prop("disabled", true);
            $("#boletoGeracaoInicio").prop("disabled", true);
            $("#boletoGeracaoFim").prop("disabled", true);
            $("#boletoVencInicio").val('');
            $("#boletoVencFim").val('');
            $("#boletoGeracaoInicio").val('');
            $("#boletoGeracaoFim").val('');
        }
    }
    function trataMostrarMovCaixa(){
        if (document.forms[0].mostrarMovCaixa.checked){
            $("#movCaixaInicio").prop("disabled", false);
            $("#movCaixaFim").prop("disabled", false);
        }else{
            $("#movCaixaInicio").prop("disabled", true);
            $("#movCaixaFim").prop("disabled", true);
            $("#movCaixaInicio").val('');
            $("#movCaixaFim").val('');
        }
    }
    function trataMostrarProdServ(){
        if (document.forms[0].mostrarProdServ.checked){
            $("#prodServInicio").prop("disabled", false);
            $("#prodServFim").prop("disabled", false);
        }else{
            $("#prodServInicio").prop("disabled", true);
            $("#prodServFim").prop("disabled", true);
            $("#prodServInicio").val('');
            $("#prodServFim").val('');
        }
    }
    function trataMostrarCredPre(){
        if (document.forms[0].mostrarCredPre.checked){
            $("#credPreInicio").prop("disabled", false);
            $("#credPreFim").prop("disabled", false);
        }else{
            $("#credPreInicio").prop("disabled", true);
            $("#credPreFim").prop("disabled", true);
            $("#credPreInicio").val('');
            $("#credPreFim").val('');
        }
    }
    function trataMostrarDebPre(){
        if (document.forms[0].mostrarDebPre.checked){
            $("#debPreInicio").prop("disabled", false);
            $("#debPreFim").prop("disabled", false);
        }else{
            $("#debPreInicio").prop("disabled", true);
            $("#debPreFim").prop("disabled", true);
            $("#debPreInicio").val('');
            $("#debPreFim").val('');
        }
    }
    
    function validarForm(){
        if(trim(document.forms[0].dtReferencia.value) == ''){
            alert('É preciso preencher a Data de Referência');
            return;
        }
        if(!isDataValida(document.forms[0].dtReferencia.value)){
            return;
        }
        
        if(!document.forms[0].mostrarCarne.checked && 
           !document.forms[0].mostrarBoleto.checked && 
           !document.forms[0].mostrarMovCaixa.checked && 
           !document.forms[0].mostrarProdServ.checked && 
           !document.forms[0].mostrarCredPre.checked && 
           !document.forms[0].mostrarDebPre.checked){
            alert('É preciso informar pelo menos 1 filtro para pesquisa');
            return;
        }
        
        if (document.forms[0].mostrarCarne.checked){
            if($("#carneVencInicio").val() == '' &&
               $("#carneGeracaoInicio").val() == ''){
                alert('Informe pelo menos 1 período do carnê!');
                return;
            }        
            if($("#carneVencInicio").val() != ''){
                if(!isDataValida(document.forms[0].carneVencInicio.value)){
                    return;
                }
                if($("#carneVencFim").val() == ''){
                    alert('Informe a data de fim do vencimento do carnê!');
                    return;
                }else{
                    if(!isDataValida(document.forms[0].carneVencFim.value)){
                        return;
                    }
                }
            }
            if($("#carneGeracaoInicio").val() != ''){
                if(!isDataValida(document.forms[0].carneGeracaoInicio.value)){
                    return;
                }
                if($("#carneGeracaoFim").val() == ''){
                    alert('Informe a data de fim da geração do carnê!');
                    return;
                }else{
                    if(!isDataValida(document.forms[0].carneGeracaoFim.value)){
                        return;
                    }
                }
            }
        }
        if (document.forms[0].mostrarBoleto.checked){
            if($("#boletoVencInicio").val() == '' &&
               $("#boletoGeracaoInicio").val() == ''){
                alert('Informe pelo menos 1 período do Boleto!');
                return;
            }        
            if($("#boletoVencInicio").val() != ''){
                if(!isDataValida(document.forms[0].boletoVencInicio.value)){
                    return;
                }
                if($("#boletoVencFim").val() == ''){
                    alert('Informe a data de fim do vencimento do Boleto!');
                    return;
                }else{
                    if(!isDataValida(document.forms[0].boletoVencFim.value)){
                        return;
                    }
                }
            }
            if($("#boletoGeracaoInicio").val() != ''){
                if(!isDataValida(document.forms[0].boletoGeracaoInicio.value)){
                    return;
                }
                if($("#boletoGeracaoFim").val() == ''){
                    alert('Informe a data de fim da geração do Boleto!');
                    return;
                }else{
                    if(!isDataValida(document.forms[0].boletoGeracaoFim.value)){
                        return;
                    }
                }
            }
        }
        if (document.forms[0].mostrarMovCaixa.checked){
            if($("#movCaixaInicio").val() == ''){
                alert('Informe a data de início do Movimento de Caixa!');
                return;
            }        
            if(!isDataValida(document.forms[0].movCaixaInicio.value)){
                return;
            }
            if($("#movCaixaFim").val() == ''){
                alert('Informe a data de fim do Movimento de Caixa!');
                return;
            }
            if(!isDataValida(document.forms[0].movCaixaFim.value)){
                return;
            }
        }
        if (document.forms[0].mostrarProdServ.checked){
            if($("#prodServInicio").val() == ''){
                alert('Informe a data de início dos Produtos e Serviços!');
                return;
            }        
            if(!isDataValida(document.forms[0].prodServInicio.value)){
                return;
            }
            if($("#prodServFim").val() == ''){
                alert('Informe a data de fim dos Produtos e Serviços!');
                return;
            }
            if(!isDataValida(document.forms[0].prodServFim.value)){
                return;
            }
        }
        if (document.forms[0].mostrarCredPre.checked){
            if($("#credPreInicio").val() == ''){
                alert('Informe a data de início do Crédito do IateCard Pré!');
                return;
            }        
            if(!isDataValida(document.forms[0].credPreInicio.value)){
                return;
            }
            if($("#credPreFim").val() == ''){
                alert('Informe a data de fim do Crédito do IateCard Pré!');
                return;
            }
            if(!isDataValida(document.forms[0].credPreFim.value)){
                return;
            }
        }
        if (document.forms[0].mostrarDebPre.checked){
            if($("#debPreInicio").val() == ''){
                alert('Informe a data de início do Débito do IateCard Pré!');
                return;
            }        
            if(!isDataValida(document.forms[0].debPreInicio.value)){
                return;
            }
            if($("#debPreFim").val() == ''){
                alert('Informe a data de fim do Débito do IateCard Pré!');
                return;
            }
            if(!isDataValida(document.forms[0].debPreFim.value)){
                return;
            }
        }

        document.forms[0].submit();
    }
    


</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Receitas</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2500">
	<input type="hidden" name="acao" value="visualizar">
                     
        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <table class="fmt">
                                    <tr>
                                        <td>
                                            <p class="legendaCodigo">Dt. Referência</p>
                                            <input type="text" id="dtReferencia" name="dtReferencia" style='width:92px;' value="" class="campoSemTamanho alturaPadrao">        
                                        </td>
                                        <td>
                                            <fieldset class="field-set legendaFrame recuoPadrao" style="width:170px;height:40px;margin-top: 20px">
                                                <legend >Ordenação:</legend>                
                                                <input type="radio" name="tipo" class="legendaCodigo" value="A" checked>Analítico
                                                <input type="radio" name="tipo" class="legendaCodigo" value="S">Sintético
                                            </fieldset>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:450px;height:80px;margin-top: 0px">
                                    <legend ><input type="checkbox" name="mostrarCarne" id="mostrarCarne" value="true">Mostrar Carnês</legend>
                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:50px;margin-top: 0px">
                                                    <legend >Vencimento</legend>
                                                    <input type="text" id="carneVencInicio" name="carneVencInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="carneVencFim"  name="carneVencFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
                                                </fieldset>
                                            </td>
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:050px;margin-top: 0px">
                                                    <legend >Geração</legend>
                                                    <input type="text" id="carneGeracaoInicio" name="carneGeracaoInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="carneGeracaoFim"  name="carneGeracaoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
                                                </fieldset>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:450px;height:80px;margin-top: 20px">
                                    <legend ><input type="checkbox" name="mostrarBoleto" id="mostrarBoleto" value="true">Mostrar Boleto Avulso</legend>
                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:50px;margin-top: 0px">
                                                    <legend >Vencimento</legend>
                                                    <input type="text" id="boletoVencInicio" name="boletoVencInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="boletoVencFim"  name="boletoVencFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
                                                </fieldset>
                                            </td>
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:050px;margin-top: 0px">
                                                    <legend >Geração</legend>
                                                    <input type="text" id="boletoGeracaoInicio" name="boletoGeracaoInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="boletoGeracaoFim"  name="boletoGeracaoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
                                                </fieldset>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:235px;height:80px;margin-top: 20px">
                                    <legend ><input type="checkbox" name="mostrarMovCaixa" id="mostrarMovCaixa" value="true">Mostrar Movimentação de Caixa</legend>
                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:50px;margin-top: 0px">
                                                    <legend >Período</legend>
                                                    <input type="text" id="movCaixaInicio" name="movCaixaInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="movCaixaFim"  name="movCaixaFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
                                                </fieldset>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:235px;height:80px;margin-top: 20px">
                                    <legend ><input type="checkbox" name="mostrarProdServ" id="mostrarProdServ" value="true">Mostrar Produtos e Serviçoes</legend>
                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:50px;margin-top: 0px">
                                                    <legend >Período</legend>
                                                    <input type="text" id="prodServInicio" name="prodServInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="prodServFim"  name="prodServFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
                                                </fieldset>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:235px;height:80px;margin-top: 20px">
                                    <legend ><input type="checkbox" name="mostrarCredPre" id="mostrarCredPre" value="true">Mostrar Créditos IateCard Pré</legend>
                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:50px;margin-top: 0px">
                                                    <legend >Período</legend>
                                                    <input type="text" id="credPreInicio" name="credPreInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="credPreFim"  name="credPreFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
                                                </fieldset>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:235px;height:80px;margin-top: 20px">
                                    <legend ><input type="checkbox" name="mostrarDebPre" id="mostrarDebPre" value="true">Mostrar Débitos IateCard Pré</legend>
                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:50px;margin-top: 0px">
                                                    <legend >Período</legend>
                                                    <input type="text" id="debPreInicio" name="debPreInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="debPreFim"  name="debPreFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
                                                </fieldset>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>
                        
                    </table>
                </td>
            </tr>
        </table>
                    
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
