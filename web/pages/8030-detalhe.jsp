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
        <div id="titulo-subnav">Grupos do Usuários</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("8032")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=8032&acao=showFormAdicionarGrupos&login=${usuario.login}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>                
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Descrição</th>
                <th scope="col" >Excluir</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="grupo" items="${usuario.grupos}">
                <tr height="1">
                    <th class="column1" align="left">${grupo.descricao}</th>

                    <c:if test='<%=request.isUserInRole("8032")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma exclusão?')) window.location.href='c?app=8032&acao=excluirGrupo&login=${usuario.login}&idGrupo=${grupo.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </th>
                    </c:if>

                </tr>	

            </c:forEach>

            </tbody>
        </table>
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=8030';" value=" " />

        <div class="clear"></div>

        <br />
        <br />            

        <div id="rodape">
            <div id="copyright"><img src="imagens/copyright.png" /></div>
        </div>

    
</body>
</html>
