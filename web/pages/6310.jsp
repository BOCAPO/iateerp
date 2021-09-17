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
	<input type="hidden" name="app" value="6310">
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
                                            <p class="legendaCodigo">Nome:</p>
                                            <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:280px">
                                        </td>
                                        <td>
                                            <fieldset class="field-set legendaFrame recuoPadrao" style="height:55px;width:192px;">
                                                <legend >Data:</legend>
                                                <input type="text" name="dataInicio" id="dataInicio" style="margin-top:3px;"  class="campoSemTamanho alturaPadrao larguraNumero" value="${dtAtual}">    
                                                &nbsp;&nbsp;&nbsp;&nbsp;a
                                                <input type="text" name="dataFim" id="dataFim" style="margin-top:3px;"   class="campoSemTamanho alturaPadrao larguraNumero" value="${dtAtual}">        
                                            </fieldset>                                
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
                                                <select name="idTaxa" class="campoSemTamanho alturaPadrao" style="width:280px;" >
                                                    <option value="0">TODOS</option>
                                                    <c:forEach var="taxa" items="${taxas}">
                                                        <option value="${taxa.id}">${taxa.descricao}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                        <td>
                                            <fieldset class="field-set legendaFrame recuoPadrao" style="margin-left:18px;margin-top:0px;height:55px;width:192px;">
                                                <legend >Situação:</legend>
                                                &nbsp;&nbsp;&nbsp;<input type="radio" name="situacao" style="margin-top:10px;margin-left:15px;" value="N" checked>Normal
                                                &nbsp;&nbsp;&nbsp;<input type="radio" name="situacao" style="margin-top:10px;margin-left:15px;" value="C">Cancelada
                                            </fieldset>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <BR>
                    <input type="checkbox" name="agrupado" style="margin-top:-10px;margin-left:25px;" value="true">Agrupado por mês/ano
                </td>
            </tr>
        </table>
        
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />                                    
        
    </form>

</body>
</html>
