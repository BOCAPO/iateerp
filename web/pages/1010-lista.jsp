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
    <div id="titulo-subnav">Categorias</div>
    <div class="divisoria"></div>

    <br>

    <c:if test='<%=request.isUserInRole("1011")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=1011&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>

    <br>                
    <table id="tabela">
       <thead>
        <tr class="odd">
            <th>Cód.</th>
            <th>Descrição</th>
            <th>Abrev.</th>
            <th>Status</th>
            <th>Tipo</th>
            <th>Tít. Transf.</th>
            <th>Admite Dep.</th>
            <th>Vr.Max Carne</th>
            <th>Usuários no Título</th>
            <th>Qt. Convites</th>
            <th>Pz Renov. Conv.</th>
            <th>Alterar</th>
            <th>Excluir</th>
        </tr>
       </thead>
        <c:forEach var="item" items="${lista}">
            <tr  height="1">
                <td class="column1" align="center">${item.id}</td>
                <td class="column1" align="left">${item.descricao}</td>
                <td class="column1" align="center">${item.abreviacao}</td>
                <td class="column1" align="center">${item.status}</td>
                <td class="column1" align="center">${item.tipo}</td>
                <td class="column1" align="center">${item.tituloTransferivel}</td>
                <td class="column1" align="center">${item.admiteDependente}</td>
                <fmt:formatNumber var="valor"  value="${item.vrMaxCarne}" pattern="#0.00"/>
                <td class="column1" align="right">${valor}</td>
                <td class="column1" align="center">
                    <c:if test='${item.deCatUsuario != null}'>
                        ${item.deCatUsuario} (Qt. = ${item.qtUsuario}) 
                    </c:if>
                </td>
                <td class="column1" align="center">${item.qtConvite}</td>
                <td class="column1" align="center">${item.prazoRenovacaoConvite}</td>
                <c:if test='<%=request.isUserInRole("1012")%>'>
                    <td class="column1" align="center">
                        <a href="c?app=1012&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                    </td>
                </c:if>
                
                <c:if test='<%=request.isUserInRole("1013")%>'>
                    <td class="column1" align="center">    
                        <a href="javascript: if(confirm('Confirma Exclusão de ${item.descricao} da tabela de Categorias?')) window.location.href='c?app=1013&id=${item.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </td>
                </c:if>

            </tr>
        </c:forEach>
    </table>
</body>
</html>
