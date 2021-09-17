<%@include file="head.jsp"%>

<style>
    table {border:none;width:650px;padding:0px;margin-left:1em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>        
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
            $("#dtPagto").mask("99/99/9999");
            
            $.format.locale({
                number: {
                groupingSeparator: '.',
                decimalSeparator: ','
                }
            });
    });


    function validarForm(){

    if (document.forms[0].anulaCarne.checked == false){
        if (!verifica_branco(document.forms[0].dtPagto.value)){
            alert("Informe a Data de Pagamento!");
            return false;
        }
        if(!isDataValida(document.forms[0].dtPagto.value)){
            return false; 
        }
        if (!verifica_branco(document.forms[0].vrPago.value)){
            alert("Informe o Valor Pago!");
            return false;
        }
        if (isNaN(document.forms[0].vrPago.value.replace(",","."))) {    
            alert("Valor Pago inválido!");    
            return false;    
        } 

        var vrDevido = parseFloat(document.forms[0].ValorDevidoHidden.value.replace(",",""));
        var vrPago = parseFloat(document.forms[0].vrPago.value.replace(".","").replace(",","."));
        if (!verifica_branco(document.forms[0].encargos.value)){
            var Soma=(parseFloat(vrDevido));
        }else{
            var vrEncargos = parseFloat(document.forms[0].encargos.value.replace(".","").replace(",","."));
            var Soma=(parseFloat(vrDevido)+ parseFloat(vrEncargos));
        }
        
        if ($.format.number(vrPago, '#,##0.00') != $.format.number(Soma, '#,##0.00')) {
            alert("A soma do Valor Devido + Encargos não bate com Valor Pago!");   
            return false;
        }
        
    }
    
    if (document.forms[0].QtBaixasHidden.value==0) {    
        alert("Não há saldo para baixa!");    
        return false;    
    } 
    
    document.forms[0].submit();

    }

</script>


<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
        <div id="titulo-subnav">Baixa Manual de Carne - Atualização</div>
    <div class="divisoria"></div>
    
    
    <form action="c">
        <input type="hidden" name="app" value="1720"/>
        <input type="hidden" name="acao" value="atualizabaixacarne"/>
        <input type="hidden" name="ValorDevidoHidden" value="${bmc.valor}"/>
        <input type="hidden" name="QtBaixasHidden" value="${bmc.qtBaixa}"/>
        
        <table align="left">
          <tr>
            <td>
                <p class="legendaCodigo">Seq. Carne:</p>
                <input type="text" name="seqCarne" maxlength="8" class="campoSemTamanho alturaPadrao larguraData" disabled value="${bmc.seqCarne}"/>
            </td>
            <td>
                <p class="legendaCodigo">Categoria:</p>
                <input type="text" name="categoria" maxlength="2" class="campoSemTamanho alturaPadrao larguraData" disabled value="${bmc.categoria}"/>
            </td>
            <td>
                <p class="legendaCodigo">Matrícula:</p>
                <input type="text" name="matricula" maxlength="4" class="campoSemTamanho alturaPadrao larguraData" disabled value="${bmc.matricula}"/>
            </td>
            <td>
                <p class="legendaCodigo">Dt. Vencimento</p>
                <input type="text" name="dtVenc" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="<fmt:formatDate value="${bmc.dataVenc}" pattern="dd/MM/yyyy"/>"/>
            </td>
            <td>
                <p class="legendaCodigo">Valor</p>
                <input type="text" name="vrDevido" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="<fmt:formatNumber value="${bmc.valor}" pattern="#0.00"/>"/>
            </td>
            <td>
                <p class="legendaCodigo">Qt. Baixa</p>
                <input type="text" name="qtBaixas" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="${bmc.qtBaixa}"/>
            </td>
          </tr>
          <tr>
            <td>
                <br><br>
                <input type="checkbox" class="legendaCodigo" name="anulaCarne"> <spam class="legendaSemMargem"> Anula Carne </spam>
            </td>
            <td>
                <p class="legendaCodigo">Dt. Pagamento:</p>
                <input type="text" name="dtPagto" id="dtPagto" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" />
            </td>
            <td>
                <p class="legendaCodigo">Encargos:</p>
                <input type="text" name="encargos" maxlength="8" class="campoSemTamanho alturaPadrao larguraData" />
            </td>
            <td>
                <p class="legendaCodigo">Valor Pago:</p>
                <input type="text" name="vrPago" maxlength="8" class="campoSemTamanho alturaPadrao larguraData" />
            </td>
            <td>
                <p class="legendaCodigo ">Banco</p>
                <div class="selectheightnovo">
                    <select name="banco" id="banco"  class="campoSemTamanho alturaPadrao larguraData">
                        <option value="BB" selected>Banco do Brasil</option>
                        <option value="ITAU">Itaú</option>
                    </select>
                </div>
            </td>
          </tr>
        </table>  

        <br><br><br><br><br><br><br><br>
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1720&acao=selecionacarne';" value=" " />
            
            
    </form>

</body>
</html>
