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
        <div id="titulo-subnav">Praticante de Modalidade Esportiva</div>
        <div class="divisoria"></div>

        <table id="tabela"">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Título</th>
                <th scope="col" class="nome-lista">Nome</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="participantemodalidade" items="${participantemodalidades}">
                <tr height="1">
                    <th class="column1" align="left">${participantemodalidade.titulo}</th>
                    <th class="column1" align="left">${participantemodalidade.nome}</th>


            </c:forEach>

            </tbody>
        </table>

        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1100';" value=" " />

        <div class="clear"></div>

        <br />
        <br />            

        <div id="rodape">
            <div id="copyright"><img src="imagens/copyright.png" /></div>
        </div>

    
</body>
</html>
