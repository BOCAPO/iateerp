<%@include file="head.jsp"%>


<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Alteração de Senha</div>
    <div class="divisoria"></div>

    <script type="text/javascript" language="Javascript" src="js/senha.js"></script>

    <form action="c" id="it-bloco01">
        <input type="hidden" name="app" value="8031"/>
        <input type="hidden" name="acao" value="inserir"/>

        <p class="legendaCodigo">Nome Reduzido</p>
        <input type="text" name="login" maxlength="50" class="campoSemTamanho alturaPadrao larguraNomeDependente" ><br>
        <p class="legendaCodigo">Nome Usuário</p>
        <input type="text" name="nome" maxlength="40" class="campoSemTamanho alturaPadrao larguraNomeDependente" ><br>
        <p class="legendaCodigo">Observação</p>
        <input type="text" name="observacao" class="campoSemTamanho alturaPadrao " style="width: 500px" ><br>

        <br><br>

        <input type="submit" class="botaoatualizar"   value=" ">
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=8030';" value=" " />
        
    </form>

</body>
</html>

