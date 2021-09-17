<%@include file="head.jsp"%>

<script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">
    
    $(document).ready(function () {
        $("#dataVencimento").mask("99/99/9999");
    });        
    
    
    function validarForm(){
        var numCracha = document.forms[0].cracha.value;
        
        if(numCracha.charAt(1) != '7'){
            alert('Nr. Informado incorretamente.');
            return;
        }

        document.forms[0].submit();
    }

</script>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
    
input[type="file"]
{
}    
    
</style>  

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Alteração do Número da Carteira</div>
    <div class="divisoria"></div>


    <form action="c">
        <input type="hidden" name="idFuncionario" value="${funcionario.id}">
        <input type="hidden" name="app" value="1055">
        <input type="hidden" name="acao" value="atualizar">

        <table align="left" >
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Primeiro Nome:</p>
                    <input type="text" name="primeiroNome" class="campoSemTamanho alturaPadrao larguraNomePessoa" readonly value="${funcionario.primeiroNome}">
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Nome:</p>
                    <input type="text" name="nome" class="campoSemTamanho alturaPadrao larguraNomePessoa" readonly value="${funcionario.nome}">
                </td>
            </tr>            
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Matrícula:</p>
                    <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNomePessoa" readonly value="${funcionario.matricula}">
                </td>
            </tr>            
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Crachá</p>
                    <input type="text" name="cracha" maxlength="9"  onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNomePessoa">
                </td>
            </tr>                        
            <tr><td><input type="button" onclick="validarForm()" class="botaoatualizar" value=""></td></tr>
       </table>
    
    </form>
    <br><br><br><br><br><br>
</body>
</html>
