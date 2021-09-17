<%@include file="head.jsp"%>


<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Alteração de Senha</div>
    <div class="divisoria"></div>

    <script type="text/javascript" language="Javascript" src="js/senha.js"></script>

    <form action="c" id="it-bloco01">
        <input type="hidden" name="app" value="8034">
        <input type="hidden" name="login" value="${usuario.login}">
        <input type="hidden" name="acao" value="definirSenha">

        <p class="legendaCodigo">Senha</p>
        <input type="password" name="novaSenha1" maxlength="6" class="campoSemTamanho alturaPadrao" onkeyup="exibirIconesSenhas()"><img alt=""  name="imgSenha1" src="img/blank.gif"><br>
        <p class="legendaCodigo">Confirmação</p>
        <input type="password" name="novaSenha2" maxlength="6" class="campoSemTamanho alturaPadrao" onkeyup="exibirIconesSenhas()"><img alt=""  name="imgSenha2" src="img/blank.gif"><br>


        <br><br>

        <input type="button" class="botaoatualizar"   value=" " onclick="alterarSenha()">
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=8030';" value=" " />
        
    </form>

</body>
</html>
