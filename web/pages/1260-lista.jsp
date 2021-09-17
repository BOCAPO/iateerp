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
        <div id="titulo-subnav">Crachá de Visitante</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("1261")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=1261&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Número</th>
                <th scope="col" class="nome-lista">Setor</th>
                <th scope="col" >Excluir</th>
                <th scope="col" >Imprimir</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="crachavisitante" items="${crachavisitantes}">
                <tr height="1">
                    <th class="column1" align="left">${crachavisitante.id}</th>

                    <th class="column1" align="left">${crachavisitante.deSetor}</th>

                    <c:if test='<%=request.isUserInRole("1262")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão do registro selecionado?')) window.location.href='c?app=1262&idCrachaVisitante=${crachavisitante.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("1263")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=1263&acao=showForm&idCrachaVisitante=${crachavisitante.id}"><img src="imagens/icones/inclusao-titular-07.png"/></a>
                        </th>
                    </c:if>

                </tr>	

            </c:forEach>

            </tbody>
        </table>

        <div class="clear"></div>

        <br />
        <br />            

        <div id="rodape">
            <div id="copyright"><img src="imagens/copyright.png" /></div>
        </div>

    
</body>
</html>


