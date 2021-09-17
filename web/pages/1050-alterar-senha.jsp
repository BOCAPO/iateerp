<%@include file="head.jsp"%>

<script type="text/javascript" language="Javascript" src="js/senha.js"></script>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  


<script type="text/javascript" language="JavaScript">

    function validaForm(){
        if(validarSenhas()){
            document.forms[0].submit();
        }else{
            alert('Os campos Nova Senha e Confirmação Nova Senha não conferem!.');
        }
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Alteração de Senha</div>
    <div class="divisoria"></div>
    
    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="1056"/>
        <input type="hidden" name="idFuncionario" value="${idFuncionario}"/>
        <input type="hidden" name="acao" value="alterarSenha"/>   
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Senha:</p>
                    <input type="password" class="campoSemTamanho alturaPadrao larguraData" name="novaSenha1" maxlength="6" onkeyup="exibirIconesSenhas()"><img alt=""  name="imgSenha1" src="img/blank.gif"><br>
                <td>
                <td>
                    <p class="legendaCodigo">Confirmação:</p>
                    <input type="password" class="campoSemTamanho alturaPadrao larguraData" name="novaSenha2" maxlength="6" onkeyup="exibirIconesSenhas()"><img alt=""  name="imgSenha2" src="img/blank.gif"><br>
                <td>
            <tr>
        </table>
        <br>
        
        <input type="button" class="botaoatualizar"  onclick="validaForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1050';" value=" " />
                        
    </form>
</body>
</html>
