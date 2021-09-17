<%@include file="head.jsp"%>

<body class="internas">

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
    
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Cadastro de Carros</div>
    <div class="divisoria"></div>

    <br>

    <c:if test='<%=request.isUserInRole("2191")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=2191&acao=showForm&matricula=${socio.matricula}&seqDependente=${socio.seqDependente}&idCategoria=${socio.idCategoria}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>
    <br>                
    
    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">DOC</th>
                <th scope="col">MARCA</th>
                <th scope="col">MODELO</th>
                <th scope="col">COR</th>
                <th scope="col">PLACA</th>
                <th scope="col">Alterar</th>
                <th scope="col">Excluir</th>
                <th scope="col">Documento</th>
            </tr>
        </thead>
            
        <c:forEach var="carro" items="${carros}">
            <tr>
                <td class="column1" align="center">
                    <c:if test='${carro.qtDocumento gt 0}'>
                        <img src="imagens/icones/livre_r.jpg" height="20px"/>
                    </c:if>
                </td>
                <td class="column1">${carro.modelo.marca.descricao}</td>
                <td class="column1">${carro.modelo.descricao}</td>
                <td class="column1">${carro.cor.descricao}</td>
                <td class="column1">${carro.placa}</td>
                <td align="center">    
                <c:if test='<%=request.isUserInRole("2192")%>'>
                    <a href="c?app=2192&acao=showForm&idCarro=${carro.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                </c:if>
                </td>
                <td align="center">
                <c:if test='<%=request.isUserInRole("2193")%>'>
                    <a href="javascript: if(confirm('Confirma a exclusão do carro?')) window.location.href='c?app=2193&idCarro=${carro.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                </c:if>
                </td>
                <td align="center">
                <c:if test='<%=request.isUserInRole("2194")%>'>
                    <a href="c?app=2194&acao=showForm&idCarro=${carro.id}"><img src="imagens/icones/inclusao-titular-07.png"/></a>
                </c:if>
                </td>

            </tr>
        </c:forEach>
    </table>
    
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${socio.matricula}&categoria=${socio.idCategoria}';" value=" " />
    
</body>
</html>
