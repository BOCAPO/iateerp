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
        <div id="titulo-subnav">Acessos do Usuário: ${usuario.nome}</div>
        <div class="divisoria"></div>

        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Aplicação</th>
                <th scope="col" class="nome-lista">Grupo</th>
            </tr>	
            </thead>
            <tbody>
                
            <sql:query var="rs" dataSource="jdbc/iate">
                select distinct 
                t1.cd_aplicacao,
                t1.descr_aplicacao
                from TB_CADASTRO_aplicacao t1, tb_aplicacao_por_grupo t2, TB_Usuario_Por_Grupo t3
                where t1.cd_aplicacao = t2.cd_aplicacao
                and t2.CD_GRUPO = t3.CD_GRUPO 
                and t3.USER_ACESSO_SISTEMA = '${usuario.login}'
                order by 1
            </sql:query>

            <c:forEach var="row" items="${rs.rows}">
                <tr height="1">
                    <th class="column1" align="left">${row.cd_aplicacao} - ${row.descr_aplicacao}</th>
                    <th class="column1" align="left">
                        
                        <sql:query var="rs2" dataSource="jdbc/iate">
                            select distinct 
                            T4.CD_GRUPO,
                            t4.descr_GRUPO
                            from TB_CADASTRO_aplicacao t1, tb_aplicacao_por_grupo t2, TB_Usuario_Por_Grupo t3, TB_GRUPO T4
                            where t1.cd_aplicacao = t2.cd_aplicacao
                            and t2.CD_GRUPO = t3.CD_GRUPO 
                            and t2.CD_GRUPO = t4.CD_GRUPO 
                            and t3.USER_ACESSO_SISTEMA = '${usuario.login}'
                            and t2.CD_APLICACAO = ${row.cd_aplicacao}
                            order by 2
                        </sql:query>
                            
                        <c:forEach var="row2" items="${rs2.rows}">
                            ${row2.DESCR_GRUPO} <BR>
                        </c:forEach>
                        
                    </th>
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
