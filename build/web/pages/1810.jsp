<%@include file="head.jsp"%>

<style>
    table {border:none;width:0px;margin-left:0px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>        

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $("#dataRegistro").mask("99/99/9999");
        
        marcaDesmarca('categorias')
    });     
</script>    

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if($("#banco").val() != 'B'){
            if($("#debitoConta").is(':checked')){
                alert('Só é possível enviar débito em conta para Banco do Brasil!');
                return;
            }
        
            if($("#idDeclaracao").val() != 0){
                alert('Só é possível enviar Declaração de Quitação para Banco do Brasil!');
                return;
            }
        }

        if(!isDataValida(document.forms[0].dataVencimento.value)){
            return;
        }
        
        if(trim(document.forms[0].dataVencimento.value) == ''){
            alert('A data de vencimento é obrigatória');
            return;
        }
        
        var k = 0;
        for(var i = 0; i < document.forms[0].categorias.length; i++){
            if(document.forms[0].categorias[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos uma categoria.');
            return;
        }
        
        $('#btnAtualizar').hide();
        
        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Gera Arquivo</div>
    <div class="divisoria"></div>


    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="1810"/>
        <input type="hidden" name="acao" value="gerar"/>   

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button" onclick="marcaDesmarca('categorias')" style="margin-top: -20px;" value=""/></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:300px;width:227px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <table>
                        <tr>
                            <td colspan="3">
                                <p class="legendaCodigo MargemSuperior0">Banco</p>
                                <div class="selectheightnovo">
                                    <select name="banco" id="banco"  style="width:240px;" class="campoSemTamanho alturaPadrao">
                                        <option value="B" selected>Banco do Brasil</option>
                                        <option value="I">Itaú</option>
                                    </select>
                                </div>
                                <br>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table>
                                    <tr>
                                        <td>
                                            <p class="legendaCodigo MargemSuperior0" >Título</p>
                                            <input type="text" name="titulo" class="campoSemTamanho alturaPadrao" style="width:40px;"/>
                                        </td>
                                        <td>
                                            <p class="legendaCodigo MargemSuperior0">Dt. Venc.</p>
                                            <input type="text" name="dataVencimento" id="dataRegistro" class="campoSemTamanho alturaPadrao" style="width:70px;">
                                        </td>
                                        <td style="width:220px;">
                                            <br>
                                            <input type="checkbox" class="legendaCodigo MargemSuperior0" name="debitoConta" id="debitoConta"><spam class="legendaSemMargem larguraData"> Débito Conta</spam>
                                        </td>
                                    </tr>
                                </table>
                                <br>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <p class="legendaCodigo MargemSuperior0">Declaração de Quitação</p>

                                <div class="selectheightnovo">
                                    <select name="idDeclaracao" id="idDeclaracao" class="campoSemTamanho alturaPadrao" style="width:240px;">
                                        <option value="0">Não Enviar Declaração</option>
                                        <option value="1">Enviar apenas para quem não recebeu no ano</option>
                                        <option value="2">Enviar para todos</option>
                                    </select>        
                                </div>
                                <br>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <br>
                                <p class="legendaCodigo MargemSuperior0">Opção de Envio para o sócio</p>
                                <input type="checkbox" class="legendaCodigo MargemSuperior0" name="naoInformado"><spam class="legendaSemMargem larguraData">Não Informado</spam>
                                <br>
                                <input type="checkbox" class="legendaCodigo MargemSuperior0" name="somenteBoleto"><spam class="legendaSemMargem larguraData">Somente Boleto</spam>
                                <br>
                                <input type="checkbox" class="legendaCodigo MargemSuperior0" name="correspondenciaBoleto"><spam class="legendaSemMargem larguraData">Correspondência e Boleto</spam>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        

        <input type="button" id="btnAtualizar" class="botaoatualizar"  onclick="validarForm()" value=" " />

    </form>
</body>
</html>


