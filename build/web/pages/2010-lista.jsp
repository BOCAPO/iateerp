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
        <div id="titulo-subnav">Tipos de Ocorr�ncia</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("2011")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=2011&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Descri��o</th>
                <th scope="col" >Tipo de Ocorr�ncia</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="tipoocorrencia" items="${TipoOcorrencias}">
                <tr height="1">
                    <th class="column1" align="left">${tipoocorrencia.descricao}</th>
                    <th class="column1" align="center">   
                        <c:choose>
                            <c:when test='${tipoocorrencia.tipo == "SP"}'>
                                SUSPENS�O
                            </c:when>
                            <c:when test='${tipoocorrencia.tipo == "AL"}'>
                                ALERTA
                            </c:when>
                        </c:choose>                        
                    </th>
                    <c:if test='<%=request.isUserInRole("2012")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=2012&acao=showForm&idTipoOcorrencia=${tipoocorrencia.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("2013")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclus�o do registro selecionado?')) window.location.href='c?app=2013&idTipoOcorrencia=${tipoocorrencia.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
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
