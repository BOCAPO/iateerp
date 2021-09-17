<%@include file="head.jsp"%>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
        <div id="titulo-subnav">Resultado da Geração de Carne</div>
    <div class="divisoria"></div>
    
    <form action="c">
        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="showForm"/>

        <p class="legendaCodigo">
        ${geraCarne.resultadoGeracao}<br>
        </p>

    </form>
        
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1500';" value=" " />

</body>
</html>