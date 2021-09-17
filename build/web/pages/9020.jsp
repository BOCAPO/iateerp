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

<script type="text/javascript" language="Javascript" src="js/senha.js"></script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Alteração de Senha</div>
    <div class="divisoria"></div>

    
    <p style="font-weight: bold; font-size: large; color: red">&nbsp;&nbsp;&nbsp;${alterarSenhaException}</p>    
    
    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="9020"/>
        <input type="hidden" name="acao" value="alterarSenha"/>   

        
        <p class="legendaCodigo">Senha Anterior:</p>
        <input type="password" class="campoSemTamanho alturaPadrao larguraData" name="senhaAtual" maxlength="6" ><br>
        <p class="legendaCodigo">Nova Senha:</p>
        <input type="password" class="campoSemTamanho alturaPadrao larguraData" name="novaSenha1" maxlength="6" onkeyup="exibirIconesSenhas()"><img alt=""  name="imgSenha1" src="img/blank.gif"><br>
        <p class="legendaCodigo">Confirmação Nova Senha:</p>
        <input type="password" class="campoSemTamanho alturaPadrao larguraData" name="novaSenha2" maxlength="6" onkeyup="exibirIconesSenhas()"><img alt=""  name="imgSenha2" src="img/blank.gif"><br>
        <br>
        <input type="button" class="botaoatualizar"  onclick="alterarSenha()" value=" " />
                        
    </form>
</body>
</html>
