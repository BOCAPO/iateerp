<%@include file="head.jsp"%>

<style>
    table {border:none;width:0px;margin-left:0px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>        

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $("#dataVencimento").mask("99/99/9999");
        $("#dataPagamento").mask("99/99/9999");
        
        marcaDesmarca('categorias')
    });     
</script>    

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">

    function validarForm(){


        if(!isDataValida(document.forms[0].dataVencimento.value)){
            return;
        }
        
        if(trim(document.forms[0].dataVencimento.value) == ''){
            alert('A data de vencimento é obrigatória');
            return;
        }

        if(!isDataValida(document.forms[0].dataPagamento.value)){
            return;
        }
        
        if(trim(document.forms[0].dataPagamento.value) == ''){
            alert('A data de pagamento é obrigatória');
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
    <div id="titulo-subnav">Gera Movimento Carteira 109 Itaú</div>
    <div class="divisoria"></div>


    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="2640"/>
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
                            <td>
                                <table>
                                    <tr>
                                        <td>
                                            <p class="legendaCodigo MargemSuperior0">Data de Vencimento</p>
                                            <input type="text" name="dataVencimento" id="dataVencimento" class="campoSemTamanho alturaPadrao" style="width:110px;">
                                        </td>
                                        <td>
                                            <p class="legendaCodigo MargemSuperior0">Data Pagamento</p>
                                            <input type="text" name="dataPagamento" id="dataPagamento" class="campoSemTamanho alturaPadrao" style="width:110px;">
                                        </td>
                                    </tr>
                                </table>
                                <br>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <p class="legendaCodigo MargemSuperior0">Débito em Conta</p>
                                <div class="selectheightnovo">
                                    <select name="debitoConta" id="debitoConta"  style="width:240px;" class="campoSemTamanho alturaPadrao">
                                        <option value="T" selected>TODOS</option>
                                        <option value="N">Enviar apenas SEM débito em conta</option>
                                        <option value="S">Enviar apenas COM débito em conta</option>
                                    </select>
                                </div>
                                <br>
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


