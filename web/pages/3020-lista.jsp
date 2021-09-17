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
        <div id="titulo-subnav">Cursos</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("3021")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=3021&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Descrição</th>
                <th scope="col" class="nome-lista">Modalidade</th>
                <th scope="col" class="nome-lista">Taxa</th>
                <th scope="col" >Situação</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
                <th scope="col" >Juros</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="curso" items="${cursos}">
                <tr height="1">
                    <th class="column1" align="left">${curso.descricao}</th>
                    <th class="column1" align="left">${curso.deModalidade}</th>
                    <th class="column1" align="left">${curso.deTxAdministrativa}</th>
                    <th class="column1" align="center">   
                        <c:choose>
                            <c:when test='${curso.situacao == "N"}'>
                                Normal
                            </c:when>
                            <c:when test='${curso.situacao == "C"}'>
                                Inativo
                            </c:when>
                        </c:choose>                        
                    </th>

                    <c:if test='<%=request.isUserInRole("3022")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=3022&acao=showForm&idCurso=${curso.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("3023")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão do registro selecionado?')) window.location.href='c?app=3023&idCurso=${curso.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("3025")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=3025&acao=showForm&idCurso=${curso.id}"><img src="imagens/icones/inclusao-titular-15.png"/></a>
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
