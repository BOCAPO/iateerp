<%@include file="head.jsp"%>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div id="titulo-subnav">Inclusão de Crachá de Visitante</div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   

        <p class="legendaCodigo">Setor:</p>
        <select name="idSetor" class="campoSemTamanho">
            <c:forEach var="setor" items="${setores}">
                <option value="${setor.id}">${setor.descricao}</option>
            </c:forEach>
        </select> <br>        
        
        <br><br>

        <input type="submit" class="botaoatualizar" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1260';" value=" " />


    </form>
</body>
</html>

