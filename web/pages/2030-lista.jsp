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
        <div id="titulo-subnav">Evento Náutico</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("2031")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=2031&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Descrição</th>
                <th scope="col" align="center">Período</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
                <th scope="col" >Cartão</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="evento" items="${lista}">
                <tr height="1">
                    <th class="column1" align="left">${evento.descricao}</th>
                    
                    <fmt:formatDate var="dtInicio" value="${evento.dtInicio}" pattern="dd/MM/yyyy" />
                    <fmt:formatDate var="dtFim" value="${evento.dtFim}" pattern="dd/MM/yyyy" />
                    <td class="column1" align="center">${dtInicio} a ${dtFim}</td>
                    

                    <c:if test='<%=request.isUserInRole("2032")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=2032&acao=showForm&id=${evento.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("2033")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão do registro selecionado?')) window.location.href='c?app=2033&id=${evento.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </th>
                    </c:if>
                        
                    <c:if test='<%=request.isUserInRole("2110")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=2110&acao=showForm&idEvento=${evento.id}"><img src="imagens/icones/inclusao-titular-07.png"/></a>
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
