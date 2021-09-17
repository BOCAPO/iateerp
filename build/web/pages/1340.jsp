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
        <div id="titulo-subnav">Armários do Sócio</div>
        <div class="divisoria"></div>

        <br>                

        <table ID="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Número</th>
                <th scope="col" class="nome-lista">Data</th>
                <th scope="col" class="nome-lista">Sócio</th>
                <th scope="col" >Desassociar</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="armario" items="${armarios}">
                <tr height="1">
                    <fmt:formatNumber pattern="000" var="numero" value="${armario.numero}"/>
                    <fmt:formatDate var="dataInclusao" value="${armario.dataInclusao}" pattern="dd/MM/yyyy"/>
                    <fmt:formatNumber pattern="00" var="idCategoria" value="${armario.socio.idCategoria}"/>
                    <fmt:formatNumber pattern="0000" var="matricula" value="${armario.socio.matricula}"/>

                    
                    <th class="column1" align="left">${numero}</th>
                    <th class="column1" align="left">${dataInclusao}</th>
                    <th class="column1" align="left">${idCategoria}/${matricula} - ${armario.socio.nome}</th>

                    <c:if test='<%=request.isUserInRole("1013")%>'>
                        <th class="column1" align="center">
                            <a href="javascript: if(confirm('Confirma a desassociação do Sócio ao Armário selecionado?')) window.location.href='c?app=1324&acao=showForm&numeroArmario=${armario.numero}&tipoArmario=${armario.tipo}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </th>
                    </c:if>

                </tr>	

            </c:forEach>

            </tbody>
        </table>

        <div class="clear"></div>

        <br />

        <div id="rodape">
            <div id="copyright"><img src="imagens/copyright.png" /></div>
        </div>

        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${socio.matricula}&categoria=${socio.idCategoria}';" value=" " />
    
</body>
</html>


