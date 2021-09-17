<%@include file="head.jsp"%>

    <body class="internas">
        <style type="text/css">
            table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
            table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
        </style>  
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
    <div id="titulo-subnav">Movimentação de Embarcação</div>
    <div class="divisoria"></div>    
    
    <c:if test='<%=request.isUserInRole("3036")%>'>
    <div class="botaoincluirgeral">
        <a href="c?app=3036&acao=showFormMovimento&idBarco=${barco.id}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
    </div>
    </c:if>
    
    <br>                

    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">Dt. Movimento</th>
                <th scope="col">Tipo</th>
                <th scope="col">Local</th>
                <th scope="col">Usuário</th>
                <th scope="col">Registro</th>
                <th scope="col">Dt. Registro</th>
                <th scope="col">Passag.</th>
                <th scope="col">Excluir</th>
            </tr>
        </thead>
            
        <c:forEach var="movimento" items="${movimentacao}">
            <tr>
                <fmt:formatDate var="dataMovimentacao" value="${movimento.dataMovimentacao}" pattern="dd/MM/yyyy HH:mm:ss"/>
                <td class="column1" align="center">${dataMovimentacao}</td>
                <td class="column1" align="center">
                    <c:choose>
                        <c:when test='${movimento.tipo == "E"}'>Entrada</c:when>
                        <c:otherwise>Saída</c:otherwise>
                    </c:choose>
                </td>
                <td class="column1" align="center">
                    <c:choose>
                        <c:when test='${movimento.local == "C"}'>Cais</c:when>
                        <c:when test='${movimento.local == "L"}'>Clube</c:when>
                        <c:when test='${movimento.local == "A"}'>Agua</c:when>
                        <c:otherwise>Rampa</c:otherwise>
                    </c:choose>
                </td>
                <td class="column1" align="center">${movimento.usuarioLote}</td>
                <td class="column1" align="center">
                    <c:choose>
                        <c:when test='${movimento.localLote == "C"}'>Cais</c:when>
                        <c:otherwise>Sistema</c:otherwise>
                    </c:choose>
                </td>
                <fmt:formatDate var="dataLote" value="${movimento.dataLote}" pattern="dd/MM/yyyy HH:mm:ss"/>
                <td class="column1" align="center">${dataLote}</td>                
                <td class="column1" align="center">${movimento.passageiros}</td>
                
                <td align="center">
                <c:if test='<%=request.isUserInRole("3037")%>'>
                    <a href="javascript: if(confirm('Confirma a exclusão da movimentação?')) window.location.href='c?app=3037&idMovimento=${movimento.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!--<a href="c?app=2000&acao=showForm&matricula=${barco.socio.matricula}&seqDependente=${barco.socio.seqDependente}&idCategoria=${barco.socio.idCategoria}">VOLTAR PARA LISTA DE BARCOS</a>-->
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2000&acao=showForm&matricula=${barco.socio.matricula}&idCategoria=${barco.socio.idCategoria}&seqDependente=0';" value=" " />

</body>
</html>
