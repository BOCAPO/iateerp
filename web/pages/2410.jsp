<%@include file="head.jsp"%>

<style>
    table {border:none;width:400px;margin-left:15px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>        

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Parâmetros Financeiros</div>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="2410"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <table align="left">
          <tr valign="top">
            <td>
                <p class="legendaCodigo">Instrução 1:</p>
                <input type="text" name="deInstrucao1" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucao1}" />
                <p class="legendaCodigo">Instrução 2:</p>
                <input type="text" name="deInstrucao2" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucao2}" />
                <p class="legendaCodigo">Instrução 3:</p>
                <input type="text" name="deInstrucao3" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucao3}" />
                <p class="legendaCodigo">Instrução 4:</p>
                <input type="text" name="deInstrucao4" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucao4}" />
                <p class="legendaCodigo">Instrução 5:</p>
                <input type="text" name="deInstrucao5" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucao5}" />
                <p class="legendaCodigo">Instrução 6:</p>
                <input type="text" name="deInstrucao6" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucao6}" />
                <p class="legendaCodigo">Instrução 7:</p>
                <input type="text" name="deInstrucao7" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucao7}" />
            </td>
            <td>
                <p class="legendaCodigo">Instrução DCO 1:</p>
                <input type="text" name="deInstrucaoDCO1" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucaoDCO1}" />
                <p class="legendaCodigo">Instrução DCO 2:</p>
                <input type="text" name="deInstrucaoDCO2" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucaoDCO2}" />
                <p class="legendaCodigo">Instrução DCO 3:</p>
                <input type="text" name="deInstrucaoDCO3" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucaoDCO3}" />
                <p class="legendaCodigo">Instrução DCO 4:</p>
                <input type="text" name="deInstrucaoDCO4" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucaoDCO4}" />
                <p class="legendaCodigo">Instrução DCO 5:</p>
                <input type="text" name="deInstrucaoDCO5" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucaoDCO5}" />
                <p class="legendaCodigo">Instrução DCO 6:</p>
                <input type="text" name="deInstrucaoDCO6" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucaoDCO6}" />
                <p class="legendaCodigo">Instrução DCO 7:</p>
                <input type="text" name="deInstrucaoDCO7" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucaoDCO7}" />
                <p class="legendaCodigo">Instrução DCO 8:</p>
                <input type="text" name="deInstrucaoDCO8" maxlength="60" class="campoSemTamanho alturaPadrao" style="width: 500px;" value="${p.deInstrucaoDCO8}" />
            </td>
          </tr>
        </table>
            
        <br>
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1000';" value=" " />

    </form>
</body>
</html>

