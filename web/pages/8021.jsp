<%@include file="head.jsp"%>


<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Grupo</div>
    <div class="divisoria"></div>

    <script type="text/javascript" language="Javascript" src="js/senha.js"></script>

    <form action="c" id="it-bloco01">
        <input type="hidden" name="app" value="8021"/>
        <input type="hidden" name="acao" value="inserir"/>

        <p class="legendaCodigo">Descrição</p>
        <input type="text" name="descricao" class="campoSemTamanho alturaPadrao larguraNomeDependente" ><br>

        <br><br>

        <input type="submit" class="botaoatualizar"   value=" ">
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=8020';" value=" " />
        
    </form>

</body>
</html>

