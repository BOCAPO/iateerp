<%@include file="head.jsp"%>

<style>
    table {border:none;width:0px;margin-left:0px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>        

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $("#dtVenc").mask("99/99/9999");
        $("#dtPgto").mask("99/99/9999");
    });     
</script>    

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">

    function validarForm(){

        
        if(trim(document.forms[0].dtVenc.value) == ''){
            alert('A data de vencimento é obrigatória');
            return;
        }
        if(!isDataValida(document.forms[0].dtVenc.value)){
            return;
        }

        if(trim(document.forms[0].dtPgto.value) != ''){
            if(!isDataValida(document.forms[0].dtPgto.value)){
                return;
            }
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "2651"}'>
        <div id="titulo-subnav">Inclusão de Liberação de Boleto na Internet</div>
    </c:if>
    <c:if test='${app == "2652"}'>
        <div id="titulo-subnav">Alteração de Liberação de Boleto na Internet</div>
    </c:if>
    <div class="divisoria"></div>
    
    <form action="c" id="it-bloco01">
        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>
        <input type="hidden" name="id" value="${item.id}"/>

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Data Vencimento:</p>
                    <input type="text" name="dtVenc"  id="dtVenc" maxlength="12" class="campoSemTamanho alturaPadrao" style="width:110px;" <c:if test='${app == "2652"}'>disabled</c:if> value="<fmt:formatDate value="${item.dtVenc}" pattern="dd/MM/yyyy" />" />
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Data Pagamento:</p>
                    <input type="text" name="dtPgto"  id="dtPgto" maxlength="12" class="campoSemTamanho alturaPadrao" style="width:110px;" value="<fmt:formatDate value="${item.dtPgto}" pattern="dd/MM/yyyy" />" />
                </td>
            </tr>
        </table>
        <br>
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2650';" value=" " />

    </form>
</body>
</html>
