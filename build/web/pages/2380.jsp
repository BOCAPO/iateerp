<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }

        document.forms[0].submit();
    }

</script>
<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "2381"}'>
        <div id="titulo-subnav">Inclusão de Tipo de Armário</div>
    </c:if>
    <c:if test='${app == "2382"}'>
        <div id="titulo-subnav">Alteração de Tipo de Armário</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="id" value="${local.id}"/>

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Descrição:</p>
                    <input type="text" name="descricao" style="width:272px;"  class="campoSemTamanho alturaPadrao" value="${local.descricao}" />
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Taxa</p>
                    <div class="selectheightnovo">
                        <select name="taxa" class="campoSemTamanho alturaPadrao" style="width:300px;" >
                            <c:forEach var="tx" items="${taxas}">
                                <option value="${tx.id}" <c:if test="${local.taxa.id eq tx.id}">selected</c:if>>${tx.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>        
                    
                </td>
            </tr>
        </table>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2380';" value=" " />

    </form>
</body>
</html>

