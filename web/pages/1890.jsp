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
        
        if(!document.forms[0].emprestado.checked
            && !document.forms[0].devolvido.checked){
            alert('Informe pelo menos uma das situações!');
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
    <div id="titulo-subnav">Relatório de Empréstimo de Material</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1890">
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
                                            <input type="text" name="nome" class="campoSemTamanho alturaPadrao larguraNomePessoa">
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
                                            <p class="legendaCodigo">Material:</p>
                                            <div class="selectheightnovo">
                                                <select name="idMaterial" class="campoSemTamanho alturaPadrao" style="width:250px;" >
                                                    <option value="0">TODOS</option>
                                                    <c:forEach var="material" items="${materiais}">
                                                        <option value="${material.id}">${material.descricao}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </td>
                                        <td>
                                            <fieldset class="field-set legendaFrame recuoPadrao" style="margin-top:15px;height:45px;width:230px;">
                                                <legend >Situações:</legend>
                                                <input type="checkbox" name="emprestado" style="margin-top:5px;margin-left:25px;" value="true" checked>Emprestado
                                                <input type="checkbox" name="devolvido" style="margin-top:5px;margin-left:25px;" value="true" checked>Devolvido
                                            </fieldset>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:110px;width:270px;">
                        <legend >Data:</legend>
                        <table class="fmt">
                            <tr>
                                <td align="center">
                                    <input type="radio" name="tipoPeriodo" class="legendaCodigo" checked style="margin-top:15px;" value="E" checked>Empréstimo
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
                                    <input type="radio" name="tipoPeriodo" class="legendaCodigo" style="margin-top:15px;" value="D">Devolução
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <br>
                                    <input type="text" name="dataInicio" id="dataInicio" class="campoSemTamanho alturaPadrao larguraData">    
                                    &nbsp;&nbsp;&nbsp;a
                                    <input type="text" name="dataFim" id="dataFim" class="campoSemTamanho alturaPadrao larguraData">        
                                </td>
                            </tr>
                        </table>
                    </fieldset>                                
                    
                </td>
                <td>

                </td>
            </tr>
        </table>
        
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />                                    
        
    </form>

</body>
</html>
