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
            $("#carneCancInicio").mask("99/99/9999");
            $("#carneCancFim").mask("99/99/9999");            
            $("#boletoVencInicio").mask("99/99/9999");
            $("#boletoVencFim").mask("99/99/9999");            
            $("#boletoGeracaoInicio").mask("99/99/9999");
            $("#boletoGeracaoFim").mask("99/99/9999");            
            $("#boletoCancInicio").mask("99/99/9999");
            $("#boletoCancFim").mask("99/99/9999");            
            $("#debPreInicio").mask("99/99/9999");
            $("#debPreFim").mask("99/99/9999");            
            $("#debPreCancInicio").mask("99/99/9999");
            $("#debPreCancFim").mask("99/99/9999");            
            trataMostrarCarne();
            trataMostrarBoleto();
            trataMostrarDebPre();
            
            $('#mostrarCarne').change(function() {
                trataMostrarCarne();
            })
            $('#mostrarBoleto').change(function() {
                trataMostrarBoleto();
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
            $("#carneCancInicio").prop("disabled", false);
            $("#carneCancFim").prop("disabled", false);
        }else{
            $("#carneVencInicio").prop("disabled", true);
            $("#carneVencFim").prop("disabled", true);
            $("#carneGeracaoInicio").prop("disabled", true);
            $("#carneGeracaoFim").prop("disabled", true);
            $("#carneCancInicio").prop("disabled", true);
            $("#carneCancFim").prop("disabled", true);
            $("#carneVencInicio").val('');
            $("#carneVencFim").val('');
            $("#carneGeracaoInicio").val('');
            $("#carneGeracaoFim").val('');
            $("#carneCancInicio").val('');
            $("#carneCancFim").val('');
            
        }
    }
    function trataMostrarBoleto(){
        if (document.forms[0].mostrarBoleto.checked){
            $("#boletoVencInicio").prop("disabled", false);
            $("#boletoVencFim").prop("disabled", false);
            $("#boletoGeracaoInicio").prop("disabled", false);
            $("#boletoGeracaoFim").prop("disabled", false);
            $("#boletoCancInicio").prop("disabled", false);
            $("#boletoCancFim").prop("disabled", false);
        }else{
            $("#boletoVencInicio").prop("disabled", true);
            $("#boletoVencFim").prop("disabled", true);
            $("#boletoGeracaoInicio").prop("disabled", true);
            $("#boletoGeracaoFim").prop("disabled", true);
            $("#boletoCancInicio").prop("disabled", true);
            $("#boletoCancFim").prop("disabled", true);
            $("#boletoVencInicio").val('');
            $("#boletoVencFim").val('');
            $("#boletoGeracaoInicio").val('');
            $("#boletoGeracaoFim").val('');
            $("#boletoCancInicio").val('');
            $("#boletoCancFim").val('');
        }
    }
    function trataMostrarDebPre(){
        if (document.forms[0].mostrarDebPre.checked){
            $("#debPreInicio").prop("disabled", false);
            $("#debPreFim").prop("disabled", false);
            $("#debPreCancInicio").prop("disabled", false);
            $("#debPreCancFim").prop("disabled", false);
        }else{
            $("#debPreInicio").prop("disabled", true);
            $("#debPreFim").prop("disabled", true);
            $("#debPreCancInicio").prop("disabled", true);
            $("#debPreCancFim").prop("disabled", true);
            $("#debPreInicio").val('');
            $("#debPreFim").val('');
            $("#debPreCancInicio").val('');
            $("#debPreCancFim").val('');
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
           !document.forms[0].mostrarDebPre.checked){
            alert('É preciso informar pelo menos 1 filtro para pesquisa');
            return;
        }
        
        if (document.forms[0].mostrarCarne.checked){
            if($("#carneVencInicio").val() == '' &&
               $("#carneGeracaoInicio").val() == '' && 
               $("#carneCancInicio").val() == ''){
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
            if($("#carneCancInicio").val() != ''){
                if(!isDataValida(document.forms[0].carneCancInicio.value)){
                    return;
                }
                if($("#carneCancFim").val() == ''){
                    alert('Informe a data de fim do Cancelamento do carnê!');
                    return;
                }else{
                    if(!isDataValida(document.forms[0].carneCancFim.value)){
                        return;
                    }
                }
            }
        }
        if (document.forms[0].mostrarBoleto.checked){
            if($("#boletoVencInicio").val() == '' &&
               $("#boletoGeracaoInicio").val() == '' &&
               $("#boletoCancInicio").val() == ''){
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
            if($("#boletoCancInicio").val() != ''){
                if(!isDataValida(document.forms[0].boletoCancInicio.value)){
                    return;
                }
                if($("#boletoCancFim").val() == ''){
                    alert('Informe a data de fim do cancalamento do Boleto!');
                    return;
                }else{
                    if(!isDataValida(document.forms[0].boletoCancFim.value)){
                        return;
                    }
                }
            }
        }

        if (document.forms[0].mostrarDebPre.checked){
            if($("#debPreInicio").val() == '' &&
               $("#debPreCancInicio").val() == ''){
                alert('Informe pelo menos 1 período do Débito do IateCard Pré!');
                return;
            }        
            
            if($("#debPreInicio").val() != ''){
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
            if($("#debPreCancInicio").val() != ''){
                if(!isDataValida(document.forms[0].debPreCancInicio.value)){
                    return;
                }
                if($("#debPreCancFim").val() == ''){
                    alert('Informe a data de fim do cancalamento do Débito do IateCard Pré!');
                    return;
                }
                if(!isDataValida(document.forms[0].debPreCancFim.value)){
                    return;
                }
            }        
        }

        document.forms[0].submit();
    }
    


</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Cancelamento de Receita</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2510">
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
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:665px;height:80px;margin-top: 0px">
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
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:050px;margin-top: 0px">
                                                    <legend >Cancelamento</legend>
                                                    <input type="text" id="carneCancInicio" name="carneCancInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="carneCancFim"  name="carneCancFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
                                                </fieldset>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:665px;height:80px;margin-top: 20px">
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
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:050px;margin-top: 0px">
                                                    <legend >Cancelamento</legend>
                                                    <input type="text" id="boletoCancInicio" name="boletoCancInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="boletoCancFim"  name="boletoCancFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
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
                                            <td>
                                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:200px;height:050px;margin-top: 0px">
                                                    <legend >Cancelamento</legend>
                                                    <input type="text" id="debPreCancInicio" name="debPreCancInicio" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    &nbsp;&nbsp;&nbsp;a
                                                    <input type="text" id="debPreCancFim"  name="debPreCancFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                    
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
