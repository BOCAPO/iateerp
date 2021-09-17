<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">
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
    <div id="titulo-subnav">Cadastro de Serviço da Academia</div>
    <div class="divisoria"></div>

    
    <c:if test='<%=request.isUserInRole("3211")%>'>
        <div class="botaoincluirmargem0" style="margin-left:15px;">
            <a href="c?app=3211&acao=showForm"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>                
    </c:if>
    <br>               
 
        
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" class="nome-lista">Descrição</th>
            <th scope="col" >Alterar</th>
            <th scope="col" >Excluir</th>
        </tr>	
        </thead>
        <tbody>
            <c:forEach var="servico" items="${lista}">
                <tr height="1">
                    <td class="column1" align="left">${servico.descricao}</td>

                    <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("3212")%>'>
                            <a href="c?app=3212&acao=showForm&id=${servico.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </c:if>
                    </td>
                    <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("3213")%>'>
                            <a href="javascript: if(confirm('Confirma a exclusão do serviço selecionado?')) window.location.href='c?app=3213&id=${servico.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </c:if>
                    </td>
                </tr>	
            </c:forEach>

        </tbody>
    </table>
        
</body>
</html>
