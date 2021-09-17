<%@include file="head.jsp"%>

<style>
    table {border:none;width:344px;margin-left:15px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>        



<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].qtEstoque.value != '' ){
            if (isNaN(document.forms[0].qtEstoque.value)) {    
                   alert("Estoque inválido!");    
                   return false;    
            } 
        }
        if(document.forms[0].valSocio.value != ''){
            if (isNaN(document.forms[0].valSocio.value.replace(",","."))) {    
                   alert("Valor Sócio inválida!");    
                   return false;    
            } 
        }        
        if(document.forms[0].valNaoSocio.value != ''){
            if (isNaN(document.forms[0].valNaoSocio.value.replace(",","."))) {    
                   alert("Valor Não Sócio inválido!");    
                   return false;    
            } 
        }        

        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "1391"}'>
        <div id="titulo-subnav">Inclusão de Item de Aluguel</div>
    </c:if>
    <c:if test='${app == "1392"}'>
        <div id="titulo-subnav">Alteração de Item de Aluguel</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="id" value="${itemAluguel.id}"/>

        <table align="left">
          <tr>
            <td colspan="3">
                <p class="legendaCodigo tiraMargem">Descrição:</p>
                <input type="text" name="descricao" maxlength="40" class="campoDescricao tiraMargem" value="${itemAluguel.descricao}" />
            </td>
          </tr>
          <tr>
            <td colspan="3">
                 &nbsp   
            </td>
          </tr>
          <tr>
            <td >
                <p class="legendaCodigo tiraMargem">Vr. Sócio:</p>
                <input type="text" name="valSocio" maxlength="8" class="campoSemTamanho alturaPadrao larguraData tiraMargem" value="<fmt:formatNumber value="${itemAluguel.valorSocio}" pattern="#0.00"/>" />
            </td>
            <td >
                <p class="legendaCodigo tiraMargem">Vr. Não Sócio:</p>
                <input type="text" name="valNaoSocio" maxlength="8" class="campoSemTamanho alturaPadrao larguraData tiraMargem" value="<fmt:formatNumber value="${itemAluguel.valorNaoSocio}" pattern="#0.00"/>" />
            </td>
            <td >
                <p class="legendaCodigo tiraMargem" >Estoque:</p>
                <input type="text" name="qtEstoque" maxlength="3" class="campoSemTamanho alturaPadrao larguraData tiraMargem" value="${itemAluguel.quantidadeEstoque}" />
            </td>
          </tr>
          
        </table>        
        
        <br><br><br><br><br><br><br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1390';" value=" " />


    </form>
</body>
</html>


