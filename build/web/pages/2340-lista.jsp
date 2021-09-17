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
    <div id="titulo-subnav">Cadastro de Quadras de Tenis</div>
    <div class="divisoria"></div>

    <br />

    <c:if test='<%=request.isUserInRole("2341")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=2341&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br />
        </div>
    </c:if>

    <br />                

    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista" rowspan="2">Nome da Quadra</th>
                <th scope="col" colspan="3">Duração</th>
                <c:if test='<%=request.isUserInRole("2342")%>'>
                    <th scope="col" rowspan="2">Alterar</th>
                </c:if>
                <c:if test='<%=request.isUserInRole("2343")%>'>
                <th scope="col" rowspan="2">Excluir</th>
                </c:if>
                    <th scope="col" rowspan="2">Agenda Semanal</th>
                <c:if test='<%=request.isUserInRole("2342")%>'>
                    <th scope="col" rowspan="2">Alterar <br> Agenda</th>
                </c:if>
            </tr>
            <tr>
                <th scope="col">Jogos Simples</th>
                <th scope="col">Jogos de Duplas</th>
                <th scope="col">Marcação</th>
            </tr>
        </thead>
        <tbody>

            <c:forEach var="quadra" items="${quadras}">
                <tr height="1">
                    <td class="column1" style="text-align: left;">${quadra.nome}</td>

                    <td class="column1" style="text-align: left;">${quadra.duracaoSimples} min</td>

                    <td class="column1" style="text-align: left;">${quadra.duracaoDuplas} min</td>

                    <td class="column1" style="text-align: left;">${quadra.duracaoMarcacao} seg</td>

                    <c:if test='<%=request.isUserInRole("2342")%>'>
                        <td class="column1" align="center">
                            <a href="c?app=2342&acao=showForm&id=${quadra.id}"><img src="imagens/icones/inclusao-titular-03.png" title="Alterar Quadra de Tênis" /></a>
                        </td>
                    </c:if>
                    
                    <c:if test='<%=request.isUserInRole("2343")%>'>
                        <td class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão da quadra \'${quadra.nome}\'?')) window.location.href='c?app=2343&id=${quadra.id}'">
                                <img src="imagens/icones/inclusao-titular-05.png" title="Excluir Quadra de Tênis" />
                            </a>
                        </td>
                    </c:if>
                    
                    <td>
                        <table>
                            <tbody>
                                <c:forEach var="evento" items="${quadra.agendaSemanal}">
                                    <tr>
                                        <td style="width: 20%;">${evento.dia.description}</td>
                                        <td style="width: 20%; text-align: center;">
                                            <joda:format value="${evento.inicio}" pattern="HH':'mm" />
                                            &ndash;
                                            <joda:format value="${evento.fim}" pattern="HH':'mm" />
                                        </td>
                                        <td style="width: 60%;">
                                            ${evento.tipo.description}
                                            <c:if test="${not empty evento.observacao}">
                                                (${evento.observacao})
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </td>
                    <c:if test='<%=request.isUserInRole("2342")%>'>
                        <td class="column1" align="center">
                            <a href="c?app=2342&acao=showAgenda&id=${quadra.id}"><img src="imagens/icones/inclusao-titular-07.png" title="Alterar Agenda Semanal"/></a>
                        </td>
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
