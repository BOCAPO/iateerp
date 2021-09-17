<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(!verifica_branco(document.forms[0].data.value)){
           alert("É Obrigatório informar a Data!");
           return false;
        }

        if(!isDataValida(document.forms[0].data.value)){
            return false; 
        }

        document.forms[0].submit();
    }

</script>

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $("#data").mask("99/99/9999");
    });     
</script>   
<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
        <div id="titulo-subnav">Inclusão de Feriado</div>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   

        <p class="legendaCodigo">Data:</p>
        <input type="text" name="data" id="data" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value="${feriado.data}" />

        <br><br>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1950';" value=" " />

    </form>
</body>
</html>

