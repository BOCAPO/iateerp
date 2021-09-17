<%@include file="head.jsp"%>

<style>
    table {border:none;width:800px;padding:0px;margin-left:1em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>        
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

    if (!verifica_branco(document.forms[0].motivo.value)){
        alert("Informe o Motivo!");
        return false;
    }
        
    if (document.forms[0].QtEstornosHidden.value==0) {    
        alert("Não há saldo para estorno!");    
        return false;    
    } 
    
    document.forms[0].submit();

    }

</script>


<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
        <div id="titulo-subnav">Estorno Manual de Carne - Atualização</div>
    <div class="divisoria"></div>
    
    
    <form action="c">
        <input type="hidden" name="app" value="1750"/>
        <input type="hidden" name="acao" value="atualizaestornocarne"/>
        <input type="hidden" name="ValorDevidoHidden" value="${emc.valor}"/>
        <input type="hidden" name="QtEstornosHidden" value="${emc.qtEstorno}"/>
        
        <table align="left">
          <tr>
            <td>
                <p class="legendaCodigo">Seq. Carne:</p>
                <input type="text" name="seqCarne" maxlength="8" class="campoSemTamanho alturaPadrao larguraData" disabled value="${emc.seqCarne}"/>
            </td>
            <td>
                <p class="legendaCodigo">Categoria:</p>
                <input type="text" name="categoria" maxlength="2" class="campoSemTamanho alturaPadrao larguraData" disabled value="${emc.categoria}"/>
            </td>
            <td>
                <p class="legendaCodigo">Matrícula:</p>
                <input type="text" name="matricula" maxlength="4" class="campoSemTamanho alturaPadrao larguraData" disabled value="${emc.matricula}"/>
            </td>
            <td>
                <p class="legendaCodigo">Dt. Vencimento</p>
                <input type="text" name="dtVenc" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="<fmt:formatDate value="${emc.dataVenc}" pattern="dd/MM/yyyy"/>"/>
            </td>
            <td colspan="3">
                <p class="legendaCodigo">Nome</p>
                <input type="text" name="nome" maxlength="10" class="campoDescricao" disabled value="${emc.pessoa}"/>
            </td>
          </tr>
          <tr>
            <td>
                <p class="legendaCodigo">Vr. Devido</p>
                <input type="text" name="vrDevido" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="<fmt:formatNumber value="${emc.valor}" pattern="#0.00"/>"/>
            </td>
            <td>
                <p class="legendaCodigo">Vr. Desconto</p>
                <input type="text" name="vrDesconto" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="<fmt:formatNumber value="${emc.desconto}" pattern="#0.00"/>"/>
            </td>
            <td>
                <p class="legendaCodigo">Vr. Encargos</p>
                <input type="text" name="vrEncargo" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="<fmt:formatNumber value="${emc.encargos}" pattern="#0.00"/>"/>
            </td>
            <td>
                <p class="legendaCodigo">Dt. Pagamento</p>
                <input type="text" name="dtPagto" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="<fmt:formatDate value="${emc.dataPagto}" pattern="dd/MM/yyyy"/>"/>
            </td>
            <td>
                <p class="legendaCodigo">Dt. Baixa</p>
                <input type="text" name="dtBaixa" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="<fmt:formatDate value="${emc.dataBaixa}" pattern="dd/MM/yyyy"/>"/>
            </td>
            <td>
                <p class="legendaCodigo">Local</p>
                <input type="text" name="local" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="${emc.local}"/>
            </td>
            <td>
                <p class="legendaCodigo">Qt. Estornos</p>
                <input type="text" name="qtEstornos" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" disabled value="${emc.qtEstorno}"/>
            </td>
          </tr>
          
          <tr>
            <td colspan="5">
                <p class="legendaCodigo">Motivo:</p>
                <input type="text" name="motivo" maxlength="40" class="campoDescricao" />
            </td>
          </tr>
        </table>  

            
        <br><br><br><br><br><br><br><br><br><br><br><br>    
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1750&acao=selecionacarne';" value=" " />

    </form>

</body>
</html>
