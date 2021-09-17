<%@include file="head.jsp"%>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
        });        
    </script>  


    <div class="divisoria"></div>
    <div id="titulo-subnav">Usu�rios</div>
    <div class="divisoria"></div>

    <br>

    <table id="tabela">
        <thead>
        <tr class="odd">
            <th scope="col" class="nome-lista">Data</th>
            <th scope="col" >Aplica��o</th>
            <th scope="col" >Usu�rio</th>
            <th scope="col" >Detalhamento</th>
            <th scope="col" >Estacao</th>
        </tr>	
        </thead>
        <tbody>

        <c:forEach var="registro" items="${registros}">
            
            <tr height="1">
                <fmt:formatDate var="dataRegistro" value="${registro.dataRegistro}" pattern="dd/MM/yyyy HH:mm:ss"/>
                <th class="column1" align="left">${dataRegistro}</th>
                <th class="column1" align="left">${registro.permissao.id} - ${registro.permissao.descricao}</th>
                <th class="column1" align="left">${registro.login}</th>
                <th class="column1" align="left">${registro.descricao}</th>
                <th class="column1" align="left">${registro.estacao}</th>

            </tr>	

        </c:forEach>

        </tbody>
    </table>
    
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=8040&acao=showFormConsultarLogs';" value=" " />
    
</body>
</html>
