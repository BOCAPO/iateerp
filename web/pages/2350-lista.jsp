<%@include file="head.jsp"%>

<script type="text/javascript" language="javascript">
    $(document).ready(function() {
        $('#tabela tr:gt(0)')
            .css('background', 'white')
            .hover(
                function() { $(this).css('background','#f4f9fe'); },
                function() { $(this).css('background','white');   }
            );
    });
</script>  


<body class="internas">
            
    <%@include file="menu.jsp"%>
        
        

    <div class="divisoria"></div>
    <div id="titulo-subnav">Exceções da Agenda Semanal</div>
    <div class="divisoria"></div>

    <br />

    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Quadra</th>
                <th scope="col">Agenda Semanal</th>
                <th scope="col">Exceções</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="quadra" items="${quadras}">
                <tr>
                    <td class="column1" align="left">${quadra.nome}</td>
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
                    <td style="text-align: center;">
                        <a href="c?app=2350&id=${quadra.id}"><img src="imagens/icones/inclusao-titular-07.png" title="Visualizar Exceções da Agenda Semanal" /></a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>