<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@include file="head.jsp"%>

<body class="internas" style="margin-top: 190px;">
            
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
                
                inicializarBotoes();
        });        

    </script>

    <script type="text/javascript" language="JavaScript">
        
        function zeroPad(num, leading) {
            var zero = leading - num.toString().length + 1;
            return Array(+(zero > 0 && zero)).join("0") + num;
        }
        
        function ativarBotao(b, href) {
            if (b == 8) return;
            var bb = zeroPad(b, 2);
            document.getElementById('icone' + bb).className = 'icone' + bb + '-ativo';
            document.getElementById('b' + bb).style.cursor = 'pointer';
            document.getElementById('b' + bb).setAttribute('href', href);            
        }
        
        function desativarBotao(b) {
            if (b == 8) return;
            var bb = zeroPad(b, 2);
            document.getElementById('icone' + bb).className = null;
            document.getElementById('b' + bb).style.cursor = 'default';
            document.getElementById('b' + bb).setAttribute('href', 'javascript:void(0);');
        }
        
        function inicializarBotoes() {
            desativarBotao(3);
            desativarBotao(5);
            desativarBotao(7);

            <c:if test='<%=request.isUserInRole("1231")%>'>
                ativarBotao(2, "c?app=1231&acao=showForm");
            </c:if>
        }
        
        function atualizarBotoes(nome, idPermissao) {
            //inicializarBotoes();
            for (var b = 3; b <= 21; b++) {
                switch (b) {                    
                    case  3: // Alterar
                        <c:if test='<%=request.isUserInRole("1232")%>'>
                            ativarBotao(b, "c?app=1232&acao=showForm&idPermissao=" + idPermissao);
                        </c:if>
                        break;
                    
                    case  5: // Excluir 
                        <c:if test='<%=request.isUserInRole("1233")%>'>
                            ativarBotao(b, "javascript: if(confirm('confirma exclusao de " + nome + "?')) window.location.href='c?app=1233&idPermissao=" + idPermissao + "'");
                        </c:if>
                        break;
                                        
                    case 7:  // Emitir cracha
                        <c:if test='<%=request.isUserInRole("1234")%>'>
                            ativarBotao(b, "c?app=1234&acao=showForm&idPermissao=" + idPermissao);
                        </c:if>
                        break;
                }
            }
        }
        
    </script>
    
    <div id="blocoicones">
        <ul>
            <li id="icone02"><a id="b02" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-02.png" title="Incluir Permissão Provisória "/></a></li>
            <li id="icone03"><a id="b03" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-03.png" title="Alterar dados da Permissão Provisória selecionada" /></a></li>
            <li id="icone05"><a id="b05" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-05.png" title="Excluir Permissão Provisória selecionado" /></a></li>
            <li id="icone07"><a id="b07" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-07.png" title="Emitir a Permissão Provisória selecionada" /></a></li>
        </ul>
    </div>

    <div id="blocopesquisa">
        <form action="c" method="POST">
            <input type="hidden" name="app" value="1230">
            <input type="hidden" name="acao" value="consultar">

            <label class="barrainclusaotext">Nome</label>
            <input class="formbarrainclusao4" type="text" name="nome" value="${nome}"/>
            
            <input class="botaobuscainclusao" type="submit" value="" title="Consultar" />
        </form>
        <div class="clear"></div>
    </div>            

    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">Selecionar</th>
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col">Atividade</th>
                <th scope="col">Início</th>
                <th scope="col">Fim</th>
                <th scope="col">Segunda</th>
                <th scope="col">Terça</th>
                <th scope="col">Quarta</th>
                <th scope="col">Quinta</th>
                <th scope="col">Sexta</th>
                <th scope="col">Sábado</th>
                <th scope="col">Domingo</th>
                <th scope="col">Estacionamento</th>                
            </tr>	
        </thead>
        <tbody>
            <sql:query var="rs" dataSource="jdbc/iate">
                <c:choose>
                <c:when test="${acao eq 'consultar'}">
                    EXEC SP_RECUPERA_AUTOR_ESPECIAL '${nome}'
                </c:when>
                <c:otherwise>
                    EXEC SP_RECUPERA_AUTOR_ESPECIAL '##$$%%'
                </c:otherwise>
                </c:choose>
            </sql:query>
                    
            <c:forEach var="row" items="${rs.rows}">
                <tr onclick="this.getElementsByTagName('input')[0].click();">
                    <td class="column1" align="center">
                        <input type="radio" name="selecao" value="" onclick="atualizarBotoes('${row.E_Nome_Funcionario_DE}', ${row.I_Cod_Autorizacao_NU})" />
                    </td>
                    <td class="column1">${row.E_Nome_Funcionario_DE}</td>
                    <td class="column1">${row.E_Atividade_DE}</td>
                    <fmt:formatDate var="inicio" value="${row.E_Inicio_DT}" pattern="dd/MM/yyyy"/>
                    <td class="column1" align="center">${inicio}</td>
                    <fmt:formatDate var="fim" value="${row.E_Fim_DT}" pattern="dd/MM/yyyy"/>
                    <td class="column1" align="center">${fim}</td>
                    <td class="column1" align="center">${row.C_Trab_Segunda_DE}</td>
                    <td class="column1" align="center">${row.C_Trab_Terca_DE}</td>
                    <td class="column1" align="center">${row.C_Trab_Quarta_DE}</td>
                    <td class="column1" align="center">${row.C_Trab_Quinta_DE}</td>
                    <td class="column1" align="center">${row.C_Trab_Sexta_DE}</td>
                    <td class="column1" align="center">${row.C_Trab_Sabado_DE}</td>
                    <td class="column1" align="center">${row.C_Trab_Domingo_DE}</td>
                    <td class="column1">${row.E_Estacionamento_DE}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
