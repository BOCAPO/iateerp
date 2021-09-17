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
        <div id="titulo-subnav">Usuários</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("8031")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=8031&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col" class="nome-lista">Login</th>
                <th scope="col" class="nome-lista">Observação</th>
                <th scope="col" >Grupos</th>
                <th scope="col" >Senha</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
                <th scope="col" >Ativar</th>
                <th scope="col" >Desativar</th>
                <th scope="col" >Acessos</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="usuario" items="${usuarios}">

                <tr height="1">
                    <th class="column1" align="left">${usuario.nome}</th>
                    <th class="column1" align="left">${usuario.login}</th>
                    <th class="column1" align="left">${usuario.observacao}</th>

                    <th class="column1" align="center">
                        <a href="c?app=8030&acao=detalhar&login=${usuario.login}"><img src="imagens/icones/inclusao-titular-10.png"/></a>
                    </th>
                    <th class="column1" align="center">
                        <a href="c?app=8034&acao=showFormDefinirSenha&login=${usuario.login}"><img src="imagens/icones/inclusao-titular-16.png"/></a>
                    </th>
                    
                    <th class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("8032")%>'>
                            <a href="c?app=8032&acao=showFormRenomear&login=${usuario.login}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </c:if>
                    </th>
                    <th class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("8033")%>'>
                                <a href="javascript: if(confirm('Confirma a exclusão do usuário selecionado?')) window.location.href='c?app=8033&login=${usuario.login}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </c:if>
                    </th>
                    <th class="column1" align="center">
                        <c:if test='${usuario.ativo eq "N"}'>
                            <c:if test='<%=request.isUserInRole("8034")%>'>
                                    <a href="javascript: if(confirm('Confirma a ativação do usuário selecionado?')) window.location.href='c?app=8035&login=${usuario.login}'"><img src="imagens/icones/inclusao-titular-17.png" /></a>
                            </c:if>
                        </c:if>
                    </th>
                    <th class="column1" align="center">
                        <c:if test='${usuario.ativo ne "N"}'>
                            <c:if test='<%=request.isUserInRole("8035")%>'>
                                    <a href="javascript: if(confirm('Confirma a desativação do usuário selecionado?')) window.location.href='c?app=8035&login=${usuario.login}'"><img src="imagens/icones/inclusao-titular-21.png" /></a>
                            </c:if>
                        </c:if>
                    </th>
                    <th class="column1" align="center">
                        <a href="c?app=8036&login=${usuario.login}"><img src="imagens/icones/inclusao-titular-19.png"/></a>
                    </th>

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
