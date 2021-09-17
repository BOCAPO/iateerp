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
        <div id="titulo-subnav">Arm�rio da Academia</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("2521")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=2531&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Descri��o</th>
                <th scope="col" class="nome-lista">Tipo de Arm�rio</th>
                <th scope="col" >Situa��o</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
                <th scope="col" >Bloquear/Desbloquear</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="item" items="${lista}">
                <tr height="1">
                    <th class="column1" align="left">${item.descricao}</th>
                    <th class="column1" align="left">${item.tipoArmarioAcademia.descricao}</th>
                    <c:choose>
                       <c:when test='${item.situacao == "B"}'>
                           <th class="column1" align="center">Bloqueado</th>
                       </c:when>
                       <c:otherwise>
                           <th class="column1" align="center">Normal</th>
                       </c:otherwise>
                    </c:choose>

                    <c:if test='<%=request.isUserInRole("2532")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=2532&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("2533")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclus�o do registro selecionado?')) window.location.href='c?app=2533&id=${item.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </th>
                    </c:if>
                        
                    <c:choose>
                       <c:when test='${item.situacao == "B"}'>
                            <c:if test='<%=request.isUserInRole("2534")%>'>
                                <th class="column1" align="center">
                                    <a href="javascript: if(confirm('Confirma o desbloqueio do armario selecionado?')) window.location.href='c?app=2534&acao=descbloquear&id=${item.id}'"><img src="imagens/icones/cadeado-aberto.png" /></a>
                                </th>
                            </c:if>
                       </c:when>
                       <c:otherwise>
                            <c:if test='<%=request.isUserInRole("2534")%>'>
                                <th class="column1" align="center">
                                    <a href="javascript: if(confirm('Confirma o bloqueio do armario selecionado?')) window.location.href='c?app=2534&acao=bloquear&id=${item.id}'"><img src="imagens/icones/inclusao-titular-18.png" /></a>
                                </th>
                            </c:if>
                       </c:otherwise>
                    </c:choose>

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
