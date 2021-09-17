<%@include file="head.jsp"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>


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
        <div id="titulo-subnav">Usuários do grupo: ${grupo.descricao}</div>
        <div class="divisoria"></div>

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col" class="nome-lista">Login</th>
            </tr>	
            </thead>
            <tbody>
                
            <sql:query var="rs" dataSource="jdbc/iate">
                select 
                t2.USER_ACESSO_SISTEMA login, 
                t2.NOME_USUARIO_SISTEMA nome
                from TB_Usuario_Por_Grupo t1, tb_usuario_sistema t2
                where t1.USER_ACESSO_SISTEMA = t2.USER_ACESSO_SISTEMA 
                and t1.CD_GRUPO = ${grupo.id}
                order by 1
            </sql:query>

            <c:forEach var="row" items="${rs.rows}">
                <tr height="1">
                    <th class="column1" align="left">${row.nome}</th>
                    <th class="column1" align="left">${row.login}</th>
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
