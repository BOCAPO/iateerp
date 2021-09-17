<%@include file="head.jsp"%>

<style>
    table {border:none;width:344px;margin-left:15px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>        

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $("#dataRegistro").mask("99/99/9999");
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
        
        $('#btnAtualizar').hide();
        
        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Gera DCO</div>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="1820"/>
        <input type="hidden" name="acao" value="gerar"/>   

        <p class="legendaCodigo MargemSuperior0" >Data Vencimento</p>
        <input type="text" name="dataVencimento" id="dataRegistro" class="campoSemTamanho alturaPadrao larguraData">

        <br><br>

        <input type="button" id="btnAtualizar" class="botaoatualizar"  onclick="validarForm()" value=" " />

    </form>
</body>
</html>


