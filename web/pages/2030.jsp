<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<style>
    table {border:none;width:344px;margin-left:15px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>        

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
        $("#dtInicio").mask("99/99/9999");
        $("#dtFim").mask("99/99/9999");
    });     

    function validarForm(){

        if(document.forms[0].descricao.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }
        
        if(document.forms[0].dtInicio.value == ''){
            alert('Informe a Data de Início');
            return false;
        }
        if(document.forms[0].dtFim.value == ''){
            alert('Informe a Data de Fim');
            return false;
        }
        if(!isDataValida(document.forms[0].dtInicio.value)){
            $('#dtInicio').focus();
            return false;
        }
        if(!isDataValida(document.forms[0].dtFim.value)){
            $('#dtFim').focus();
            return false;
        }

        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "2031"}'>
        <div id="titulo-subnav">Inclusão de Evento Náutico</div>
    </c:if>
    <c:if test='${app == "2032"}'>
        <div id="titulo-subnav">Alteração de Evento Náutico</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="id" value="${evento.id}"/>

        <table align="left">
          <tr>
            <td colspan="2">
                <p class="legendaCodigo tiraMargem">Descrição:</p>
                <input type="text" name="descricao" maxlength="40" class="campoDescricao tiraMargem" value="${evento.descricao}" />
            </td>

            <td >
                <p class="legendaCodigo tiraMargem">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dt. Início</p>
                <fmt:formatDate var="dtInicio" value="${evento.dtInicio}" pattern="dd/MM/yyyy" />             
                <input type="text" name="dtInicio" id="dtInicio" maxlength="8" class="campoSemTamanho alturaPadrao larguraData " value="${dtInicio}" />
            </td>
            <td >
                <p class="legendaCodigo tiraMargem">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dt. Fim</p>
                <fmt:formatDate var="dtFim" value="${evento.dtFim}" pattern="dd/MM/yyyy" />             
                <input type="text" name="dtFim" id="dtFim" maxlength="8" class="campoSemTamanho alturaPadrao larguraData " value="${dtFim}" />
            </td>
          </tr>
          
        </table>        



        <br><br><br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2030';" value=" " />

    </form>
</body>
</html>

