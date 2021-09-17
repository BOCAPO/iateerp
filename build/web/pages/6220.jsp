<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">    
   $(document).ready(function () {
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
            $("#vencimentoInicio").mask("99/99/9999");
            $("#vencimentoFim").mask("99/99/9999");
    });       
    function validarForm(){
        
        if(trim(document.forms[0].dataInicio.value) == ''
            || trim(document.forms[0].dataFim.value) == ''){
            alert('Informe o período para o relatório');
            return;
        }
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }
        if(!isDataValida(document.forms[0].vencimentoInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].vencimentoFim.value)){
            return;
        }        
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menuCaixa.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Taxas Individuais por Situação do Carnê</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="6220">
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
                                            <p class="legendaCodigo">Cat.:</p>
                                            <input type="text" name="categoria" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumeroPequeno">
                                        </td>
                                        <td>
                                            <p class="legendaCodigo">Matr.:</p>
                                            <input type="text" name="matricula" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumeroPequeno">
                                        </td>
                                        <td>
                                            <p class="legendaCodigo">Nome:</p>
                                            <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:245px;">
                                        </td>
                                        <td>
                                            <p class="legendaCodigo">Taxa:</p>
                                            <div class="selectheightnovo">
                                                <select name="idTaxa" class="campoSemTamanho alturaPadrao" style="width:250px;" >
                                                    <option value="0">TODOS</option>
                                                    <c:forEach var="taxa" items="${taxas}">
                                                        <option value="${taxa.id}">${taxa.descricao}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>                                        
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table class="fmt">
                                    <tr>
                                        <td>
                                            <fieldset class="field-set legendaFrame recuoPadrao" style="margin-top:0px;height:100px;width:210px;">
                                                <legend >Carnê:</legend>
                                                <input type="radio" name="carne" style="margin-top:5px;margin-left:15px;" value="PG">Com Carnê Gerado - Pago<BR>
                                                <input type="radio" name="carne" style="margin-top:5px;margin-left:15px;" value="NP" checked>Com Carnê Gerado - Não Pago<BR>
                                                <input type="radio" name="carne" style="margin-top:5px;margin-left:15px;" value="S">Sem Carnê Gerado
                                            </fieldset>
                                        </td>
                                        <td>
                                            <fieldset class="field-set legendaFrame recuoPadrao" style="height:100px;width:190px;">
                                                <legend >Período:</legend>
                                                <table class="fmt">
                                                    <tr>
                                                        <td>
                                                            <input type="text" name="dataInicio" style="margin-top:10px;" id="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero" value="${dtAtual}">    
                                                            &nbsp;&nbsp;&nbsp;a
                                                            <input type="text" name="dataFim" id="dataFim" style="margin-top:10px;"  class="campoSemTamanho alturaPadrao larguraNumero" value="${dtAtual}">        
                                                        </td>
                                                    </tr>                                                    
                                                    <tr>
                                                        <td align="center">
                                                            <input type="radio" name="tipoPeriodo" class="legendaCodigo" checked style="margin-top:15px;color:#009;" value="G" checked>Geração
                                                            &nbsp;&nbsp;
                                                            <input type="radio" name="tipoPeriodo" class="legendaCodigo" style="margin-top:15px;" value="C">Cobrança
                                                        </td>
                                                    </tr>
                                                </table>
                                            </fieldset>
                                        </td>
                                        <td>
                                            <fieldset class="field-set legendaFrame recuoPadrao" style="height:100px;width:190px;">
                                                <legend >Vencimento do Carne:</legend>
                                                <table class="fmt">
                                                    <tr>
                                                        <td>
                                                            <input type="text" name="vencimentoInicio" style="margin-top:10px;" id="vencimentoInicio" class="campoSemTamanho alturaPadrao larguraNumero">    
                                                            &nbsp;&nbsp;&nbsp;a
                                                            <input type="text" name="vencimentoFim" style="margin-top:10px;" id="vencimentoFim" class="campoSemTamanho alturaPadrao larguraNumero">        
                                                        </td>
                                                    </tr>                                                    
                                                    <tr>
                                                        <td align="center">
                                                            <input type="radio" name="tipo" class="legendaCodigo" checked style="margin-top:15px;" value="A" checked>Analítico
                                                            &nbsp;&nbsp;
                                                            <input type="radio" name="tipo" class="legendaCodigo" style="margin-top:15px;" value="S">Sintético
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
                </td>
            </tr>
        </table>
        
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />                                    
        
    </form>

</body>
</html>
