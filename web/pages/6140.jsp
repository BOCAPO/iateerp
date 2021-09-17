



<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
  

<body class="internas">
            
    <%@include file="menuCaixa.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "6141"}'>
        <div id="titulo-subnav">Inclusão de Produto ou Serviço</div>
    </c:if>
    <c:if test='${app == "6142"}'>
        <div id="titulo-subnav">Alteração de Produto ou Serviço</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
            habilitaDesabilitaEstoque();
        });         

        function validarForm(){

            if(document.forms[0].descricao.value == ''){
                alert('A descrição deve ser informada!');
                return;
            }
            if ($("#estoque").prop('checked')){
                if(document.forms[0].estoqueAtual.value == ''){
                    alert('Informe o estoque atual!');
                    return;
                }
                if(document.forms[0].estoqueMinimo.value == ''){
                    alert('Informe o estoque mínimo!');
                    return;
                }
            }

            document.forms[0].submit();
        }
            
        function habilitaDesabilitaEstoque(){
            if ($("#estoque").prop('checked')){
                $('#estoqueAtual').prop('disabled', false);
                $('#estoqueMinimo').prop('disabled', false);
            }else{
                $('#estoqueAtual').prop('disabled', true);
                $('#estoqueMinimo').prop('disabled', true);
                $('#estoqueAtual').val('');
                $('#estoqueMinimo').val('');
            }
            
        }
    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="id" value="${item.id}">
        <input type="hidden" name="transacao" value="${transacao}">

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                    <input type="text" name="descricao" class="campoSemTamanho alturaPadrao"  style="width:272px;" value="${item.descricao}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Vr. Padrão</p>
                    <fmt:formatNumber var="valPadrao"  value="${item.valPadrao}" pattern="#0.00"/>
                    <input type="text" name="valPadrao" class="campoSemTamanho alturaPadrao larguraNumero"  value="${valPadrao}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Tipo</p>
                    <div class="selectheightnovo">
                        <select name="tipo" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                            <option value="P" <c:if test='${item.tipo == "P"}'>selected</c:if>>Produto</option>
                            <option value="S" <c:if test='${item.tipo == "S"}'>selected</c:if>>Serviço</option>
                        </select>
                    </div>        
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Situação</p>
                    <div class="selectheightnovo">
                        <select name="ativo" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                            <option value="S" <c:if test='${item.ativo == "S"}'>selected</c:if>>Ativo</option>
                            <option value="N" <c:if test='${item.ativo == "N"}'>selected</c:if>>Inativo</option>
                        </select>
                    </div>        
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Cred. Iate</p>
                    <div class="selectheightnovo">
                        <select name="credito" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                            <option value="S" <c:if test='${item.credito == "S"}'>selected</c:if>>Sim</option>
                            <option value="N" <c:if test='${item.credito == "N"}'>selected</c:if>>Não</option>
                        </select>
                    </div>        
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:170px;height:75px;">
                        <legend >
                             <c:if test='${item.estoqueAtual == 0 || item.estoqueMinimo == 0}'>
                                 <input type="checkbox" id="estoque" name="estoque" value="true" onchange="habilitaDesabilitaEstoque()">Estoque
                             </c:if>
                             <c:if test='${item.estoqueAtual != 0 && item.estoqueMinimo != 0}'>
                                 <input type="checkbox" id="estoque" name="estoque" value="true" checked onchange="habilitaDesabilitaEstoque()">Estoque
                             </c:if>
                            
                        </legend>
                        <table class="fmt">
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Atual</p>
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >Mínimo</p>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="text" id="estoqueAtual" name="estoqueAtual" class="campoSemTamanho alturaPadrao larguraNumero" value="${item.estoqueAtual}">        
                                </td>
                                <td>
                                    <input type="text" id="estoqueMinimo"  name="estoqueMinimo" class="campoSemTamanho alturaPadrao larguraNumero" value="${item.estoqueMinimo}">        
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
        </table>
            
                
        
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=6140';" value=" " />

    </form>

</body>
</html>
