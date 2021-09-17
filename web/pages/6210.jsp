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
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menuCaixa.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Taxas Individuais - Sintético</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="6210">
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
                                            <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:295px">
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
                                            <p class="legendaCodigo MargemSuperior0">Taxa:</p>
                                            <div class="selectheightnovo">
                                                <select name="idTaxa" class="campoSemTamanho alturaPadrao" style="width:220px;" >
                                                    <option value="0">TODOS</option>
                                                    <c:forEach var="taxa" items="${taxas}">
                                                        <option value="${taxa.id}">${taxa.descricao}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                        <td>
                                            <fieldset class="field-set legendaFrame recuoPadrao" style="margin-top:0px;height:45px;width:175px;">
                                                <legend >Situação:</legend>
                                                <input type="radio" name="situacao" style="margin-top:5px;margin-left:15px;" value="N" checked>Normal
                                                <input type="radio" name="situacao" style="margin-top:5px;margin-left:15px;" value="C">Cancelada
                                            </fieldset>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:95px;width:200px;">
                        <legend >Data:</legend>
                        <input type="radio" name="tipoPeriodo" class="legendaCodigo" checked style="margin-top:5px;" value="G" checked>Geração
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" name="tipoPeriodo" class="legendaCodigo" style="margin-top:5px;" value="C">Cobrança
                        <br>
                        <input type="text" name="dataInicio" id="dataInicio" style="margin-top:10px;"  class="campoSemTamanho alturaPadrao larguraNumero" value="${dtAtual}">    
                        &nbsp;&nbsp;&nbsp;&nbsp;a
                        <input type="text" name="dataFim" id="dataFim" style="margin-top:10px;"   class="campoSemTamanho alturaPadrao larguraNumero" value="${dtAtual}">        
                    </fieldset>                                
                </td>
                <td>
                  <fieldset class="field-set legendaFrame recuoPadrao" style="width:90px;height:95px;">
                      <legend >Tipo:</legend>
                      &nbsp;&nbsp;<input type="checkbox" name="contaPos" id="contaPos" value="true" style="margin-top:12px;" checked>Pós-Pago
                      <br>
                      &nbsp;&nbsp;<input type="checkbox" name="contaPre" id="contaPre" value="true" style="margin-top:12px;" checked>Pre-Pago
                      <br>
                  </fieldset>
                </td>
                
                <td>
                    <BR>
                    <input type="checkbox" name="agrupado" style="margin-top:5px;margin-left:25px;" value="true">Agrupado por mês/ano
                    <BR>
                    <input type="checkbox" name="pago" style="margin-top:5px;margin-left:25px;" value="true">Carnês Pagos
                </td>
            </tr>
        </table>
        
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />                                    
        
    </form>

</body>
</html>
