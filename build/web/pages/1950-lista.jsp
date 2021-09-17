<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

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
        <div id="titulo-subnav">Feriados</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("1951")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=1951&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" align="center">Data</th>
                <th scope="col" >Excluir</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="feriado" items="${feriados}">
                <tr height="1">
                    <th class="column1" align="center">
                        <fmt:formatDate value="${feriado.data}" pattern="dd/MM/yyyy"/>
                    </th>

                    <c:if test='<%=request.isUserInRole("1952")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão do registro selecionado?')) window.location.href='c?app=1952&data=<fmt:formatDate value="${feriado.data}" pattern="dd/MM/yyyy"/>'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
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

