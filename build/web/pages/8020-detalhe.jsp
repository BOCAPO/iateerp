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

        <c:if test='<%=request.isUserInRole("8022")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=8022&acao=showFormAdicionarPermissoes&idGrupo=${grupo.id}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
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

            <c:forEach var="permissao" items="${grupo.permissoes}">
                <tr height="1">
                    <th class="column1" align="left">${permissao.id} - ${permissao.descricao}</th>

                    <c:if test='<%=request.isUserInRole("8022")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma exclusão?')) window.location.href='c?app=8022&acao=excluirPermissao&idPermissao=${permissao.id}&idGrupo=${grupo.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </th>
                    </c:if>

                </tr>	

            </c:forEach>

            </tbody>
        </table>
        
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=8020';" value=" " />

        <div class="clear"></div>

        <br />
        <br />            

        <div id="rodape">
            <div id="copyright"><img src="imagens/copyright.png" /></div>
        </div>

    
</body>
</html>
