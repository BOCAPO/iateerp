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
        <div id="titulo-subnav">Materiais</div>
        <div class="divisoria"></div>

        <br>

        <c:if test='<%=request.isUserInRole("1151")%>'>
            <div class="botaoincluirgeral">
                <a href="c?app=1151&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
            </div>
        </c:if>

        <br>                

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Descrição</th>
                <th scope="col" align="right">Estoque</th>
                <th scope="col" align="right">Qt. Total</th>
                <th scope="col" align="right">Vr. Empréstimo</th>
                <th scope="col" align="right">Dias</th>
                <th scope="col" align="right">Vr. Diária</th>
                <th scope="col" align="right">Vr. Bem</th>
                <th scope="col" align="right">Pz. Max.</th>
                <th scope="col" class="nome-lista">Taxa Individual</th>
                <th scope="col" >Alterar</th>
                <th scope="col" >Excluir</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="material" items="${materiais}">
                <tr height="1">
                    <th class="column1" align="left">${material.descricao}</th>
                    <th class="column1" align="right">${material.qtEstoque}</th>
                    <th class="column1" align="right">${material.qtTotal}</th>
                    <th class="column1" align="right"><fmt:formatNumber value="${material.vrEmprestimo}" pattern="#0.00"/></th>
                    <th class="column1" align="right">${material.qtDiasIniCob}</th>
                    <th class="column1" align="right"><fmt:formatNumber value="${material.vrDiaria}" pattern="#0.00"/></th>
                    <th class="column1" align="right"><fmt:formatNumber value="${material.valMaterial}" pattern="#0.00"/></th>
                    <th class="column1" align="right">${material.pzPadraoDevolucao}</th>
                    <th class="column1" align="left">${material.deTxAdministrativa}</th>
                    

                    <c:if test='<%=request.isUserInRole("1152")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=1152&acao=showForm&idMaterial=${material.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                    <c:if test='<%=request.isUserInRole("1153")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a exclusão do registro selecionado?')) window.location.href='c?app=1153&idMaterial=${material.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
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

