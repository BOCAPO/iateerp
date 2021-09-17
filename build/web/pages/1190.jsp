<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style> 
<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].qtMax.value == ''){
            alert('O campo quantidade máxima de vagas é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].categoria.value == ''){
            alert('O campo categoria é de preenchimento obrigatório!');
            return;
        }        
        document.forms[0].submit();
    }

</script>
<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $('#imprimirDescricao').click(escondeCampos);
        
        escondeCampos();
        
        function escondeCampos(){
            if ($('#imprimirDescricao').attr('checked') == 'checked'){
                $('#corFundo').show();
                $('#lblCorFundo').show();
                $('#corFonte').show();
                $('#lblCorFonte').show();
                $('#gestao').show();
                $('#lblGestao').show();
            }else{
                $('#corFundo').hide();
                $('#lblCorFundo').hide();
                $('#lblCorFundo').hide();
                $('#corFonte').hide();
                $('#lblCorFonte').hide();
                $('#gestao').hide();
                $('#lblGestao').hide();
            }    
        }
        
        
    });     
</script>  

<!--
   jPicker eh mais bonito mas usa jQuery
   http://www.digitalmagicpro.com/jPicker/

   http://blog.jquery.com/2012/08/24/jquery-color-2-1-0/
-->
<script type="text/javascript" src="js/jscolor/jscolor.js"></script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "1191"}'>
        <div id="titulo-subnav">Inclusão de Cargo Especial</div>
    </c:if>
    <c:if test='${app == "1192"}'>
        <div id="titulo-subnav">Alteração de Cargo Especial</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idCargoEspecial" value="${cargoEspecial.id}"/>

        
        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Descrição:</p>
                    <input type="text" name="descricao" class="campoSemTamanho alturaPadrao larguraCidade" value="${cargoEspecial.descricao}" />
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Qt. Max:</p>
                    <input type="text" name="qtMax" maxlength="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" value="${cargoEspecial.qtMax}" />
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Tipo Cargo:</p>
                    <div class="selectheightnovo">
                        <select name="idTipoCargo" class="campoSemTamanho alturaPadrao" style="width:180px;">
                            <option value="NE" selected="selected">Nenhum</option>
                            <option value="AC" <c:if test='${cargoEspecial.idTipoCargo == "AC"}'>selected="selected"</c:if>>ASSESSOR COMODORIA</option>
                            <option value="AC" <c:if test='${cargoEspecial.idTipoCargo == "AC"}'>selected="selected"</c:if>>ASSESSOR COMODORIA</option>
                            <option value="CD" <c:if test='${cargoEspecial.idTipoCargo == "CD"}'>selected="selected"</c:if>>CONSELHO DELIBERATIVO</option>
                            <option value="CO" <c:if test='${cargoEspecial.idTipoCargo == "CO"}'>selected="selected"</c:if>>COMODORO</option>
                            <option value="DI" <c:if test='${cargoEspecial.idTipoCargo == "DI"}'>selected="selected"</c:if>>DIRETOR</option>
                            <option value="EC" <c:if test='${cargoEspecial.idTipoCargo == "EC"}'>selected="selected"</c:if>>EX-COMODORO</option>
                            <option value="PC" <c:if test='${cargoEspecial.idTipoCargo == "PC"}'>selected="selected"</c:if>>PRES. CONS. DELIBERATIVO</option>
                            <option value="VC" <c:if test='${cargoEspecial.idTipoCargo == "VC"}'>selected="selected"</c:if>>VICE-COMODORO</option>
                            <option value="VD" <c:if test='${cargoEspecial.idTipoCargo == "VD"}'>selected="selected"</c:if>>VICE-DIRETOR</option>
                        </select>
                    </div>        
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Categoria:</p>
                    <input type="text" name="categoria" class="campoSemTamanho alturaPadrao larguraCidade" maxlength="30" class="campoDescricao" value="${cargoEspecial.categoria}" />
                </td>
            </tr>
        </table>

        <table>
            <tr>
                <td>
                    <input type="checkbox" class="legendaCodigo MargemSuperior0" name="imprimirDescricao"  id="imprimirDescricao" value="true" <c:if test='${cargoEspecial.corFundo != null || cargoEspecial.corFonte != null}'>checked="checked"</c:if> /><span>Imprimir Descrição</span>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" id="lblCorFundo">Cor de fundo:</p>
                    <input class="campoSemTamanho alturaPadrao larguraNumero color" size="10" value="${cargoEspecial.corFundo}" name="corFundo" id="corFundo" onload="document.getElementById('corFundo').color.fromString('${cargoEspecial.corFundo}')">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" id="lblCorFonte">Cor da fonte:</p>
                    <input class="campoSemTamanho alturaPadrao larguraNumero color" size="1" value="${cargoEspecial.corFonte}" name="corFonte" id="corFonte" onload="document.getElementById('corFonte').color.fromString('${cargoEspecial.corFonte}')">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" id="lblGestao">Gestão:</p>
                    <input type="text" name="gestao" id="gestao"  maxlength="30" class="campoSemTamanho alturaPadrao" style="width:160px;" value="${cargoEspecial.gestao}" />
                </td>
            </tr>
        </table>
        <br>
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1190';" value=" " />

    </form>
</body>
</html>
