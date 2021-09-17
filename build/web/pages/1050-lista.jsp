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

        function validarForm(){
            if(!document.forms[0].funcionario.checked 
                && !document.forms[0].concessionario.checked){
                alert('Selecione pelo menos 1 tipo de Funcionário!');
                return false;
            }else{
                return true;
            }
        }

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
            var idCargo = document.forms[0].idCargo.value;
            var idSetor = document.forms[0].idSetor.value;
            var nome = document.forms[0].nome.value;
            var funcionario = document.forms[0].funcionario.value;
            var concessionario = document.forms[0].concessionario.value;
            ativarBotao(15, "c?app=1050&acao=listagem&idCargo=" + idCargo + "&idSetor=" + idSetor + "&nome=" + nome + "&funcionario=" + funcionario + "&concessionario=" + concessionario);
                        
            desativarBotao(3);
            desativarBotao(5);
            desativarBotao(7);
            desativarBotao(18);

            <c:if test='<%=request.isUserInRole("1051")%>'>
                ativarBotao(2, "c?app=1051&acao=showForm");
            </c:if>
        }
        
        function atualizarBotoes(nome, idFuncionario) {
            //inicializarBotoes();
            for (var b = 3; b <= 21; b++) {
                switch (b) {                    
                    case  3: // Alterar
                        <c:if test='<%=request.isUserInRole("1052")%>'>
                            ativarBotao(b, "c?app=1052&acao=showForm&idFuncionario=" + idFuncionario);
                        </c:if>
                        break;
                    
                    case  5: // Excluir 
                        <c:if test='<%=request.isUserInRole("1053")%>'>
                            ativarBotao(b, "javascript: if(confirm('confirma exclusao de " + nome + "?')) window.location.href='c?app=1053&idFuncionario=" + idFuncionario + "'");
                        </c:if>
                        break;
                                        
                    case 7:  // Emitir cracha
                        <c:if test='<%=request.isUserInRole("1054")%>'>
                            ativarBotao(b, "c?app=1054&acao=showForm&idFuncionario=" + idFuncionario);
                        </c:if>
                        break;
                    
                    case 18: // alterar senha
                        <c:if test='<%=request.isUserInRole("1056")%>'>
                            ativarBotao(b, "c?app=1056&acao=showFormAlterarSenha&idFuncionario=" + idFuncionario);
                            break;
                        </c:if>
                            
                    case 20: // Carro
                        <c:if test='<%=request.isUserInRole("3240")%>'>
                            ativarBotao(b, "c?app=3240&acao=showForm&id=" + idFuncionario);
                            break;
                        </c:if>

                }
            }
        }
        
    </script>
    
    <div id="blocoicones">
        <ul>
            <li id="icone02"><a id="b02" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-02.png" title="Incluir funcionário" /></a></li>
            <li id="icone03"><a id="b03" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-03.png" title="Alterar dados do funcionário selecionado" /></a></li>
            <li id="icone05"><a id="b05" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-05.png" title="Excluir funcionário selecionado" /></a></li>
            <li id="icone07"><a id="b07" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-07.png" title="Emitir crachá do funcionário selecionado" /></a></li>
            <li id="icone15"><a id="b15" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-15.png" title="Imprimir relação" /></a></li>
            <li id="icone18"><a id="b18" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-18.png" title="Alterar senha" /></a></li>
            <li id="icone20"><a id="b20" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-20.png" title="Cadastro de Carro" /></a></li>
            
            
        </ul>
    </div>

    <div id="blocopesquisa">
        <form action="c" method="POST" onsubmit="return validarForm()">
            <input type="hidden" name="app" value="1050">
            <input type="hidden" name="acao" value="consultar">
            
            <label class="barrainclusaotext">&nbsp;&nbsp;&nbsp;Setor</label>
            <div class="selectheightnovo">
                <select name="idSetor" class="formbarrainclusaoSEMTAMANHO">
                    <option value="0">TODOS</option>
                    <c:forEach var="setor" items="${setores}">
                        <option value="${setor.id}" <c:if test="${idSetor eq setor.id}">selected</c:if>>${setor.descricao}</option>
                    </c:forEach>
                </select>            
            </div>
            
            <label class="barrainclusaotext">Cargo</label>
            <div class="selectheightnovo">
                <select name="idCargo" class="formbarrainclusaoSEMTAMANHO">
                    <option value="0">TODOS</option>
                    <c:forEach var="cargo" items="${cargos}">
                        <option value="${cargo.id}" <c:if test="${idCargo eq cargo.id}">selected</c:if>>${cargo.descricao}</option>
                    </c:forEach>
                </select>            
            </div>

            <label class="barrainclusaotext">Nome</label>
            <input class="formbarrainclusao4" type="text" name="nome" value="${nome}"/>
            
            <input type="checkbox" width="100px" class="formbarrainclusaoSEMTAMANHO" style="margin-left: 10px" name="funcionario" value="true" <c:if test='${funcionario}'>checked</c:if>><label class="barrainclusaocheck">Funcionário</label>
            <input type="checkbox" width="100px" class="formbarrainclusaoSEMTAMANHO" name="concessionario" value="true" <c:if test='${concessionario}'>checked</c:if>><label class="barrainclusaocheck">Concessionário</label>
            
            <input class="botaobuscainclusao" type="submit" value="" title="Consultar" />
        </form>
        <div class="clear"></div>
    </div>            

    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">Selecionar</th>
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col">Setor</th>
                <th scope="col">Cargo</th>
                <th scope="col">Endereço</th>
                <th scope="col">Telefones</th>
                <th scope="col">Matricula</th>
                <th scope="col">Nome Abrev</th>
                <th scope="col">Sangue</th>
                <th scope="col">Tipo</th>
                <th scope="col">Segunda</th>
                <th scope="col">Terça</th>
                <th scope="col">Quarta</th>
                <th scope="col">Quinta</th>
                <th scope="col">Sexta</th>
                <th scope="col">Sábado</th>
                <th scope="col">Domingo</th>
                <th scope="col">User</th>
                <th scope="col">Estacionamento</th>                
            </tr>	
        </thead>
        <tbody>
            <sql:query var="rs" dataSource="jdbc/iate">
                <c:choose>
                <c:when test="${acao eq 'consultar'}">
                    EXEC SP_RECUPERA_FUNCIONARIOS ${idCargo}, ${idSetor}, '${nome}%', '${tipo}'
                </c:when>
                <c:otherwise>
                    EXEC SP_RECUPERA_FUNCIONARIOS 0, 0, '', ''
                </c:otherwise>
                </c:choose>
            </sql:query>
                    
            <c:forEach var="row" items="${rs.rows}">
                <tr onclick="this.getElementsByTagName('input')[0].click();">
                    <td class="column1" align="center">
                        <input type="radio" name="selecao" value="" onclick="atualizarBotoes('${row.E_Nome_Funcionario_DE}', ${row.I_Cod_Funcionario_NU})" />
                    </td>
                    <td class="column1">${row.E_Nome_Funcionario_DE}</td>
                    <td class="column1">${row.E_Setor_DE}</td>
                    <td class="column1">${row.E_Cargo_DE}</td>
                    <td class="column1">${row.E_Endereco_DE}</td>
                    <td class="column1">${row.E_Telefones_DE}</td>
                    <td class="column1">${row.EE_Matricula_NU}</td>
                    <td class="column1">${row.E_Nome_Abrev_NU}</td>
                    <td class="column1">${row.E_Sangue_NU}</td>
                    <td class="column1">${row.E_Tipo_DE}</td>
                    <td class="column1">${row.C_Trab_Segunda_DE}</td>
                    <td class="column1">${row.C_Trab_Terca_DE}</td>
                    <td class="column1">${row.C_Trab_Quarta_DE}</td>
                    <td class="column1">${row.C_Trab_Quinta_DE}</td>
                    <td class="column1">${row.C_Trab_Sexta_DE}</td>
                    <td class="column1">${row.C_Trab_Sabado_DE}</td>
                    <td class="column1">${row.C_Trab_Domingo_DE}</td>
                    <td class="column1">${row.E_User_DE}</td>
                    <td class="column1">${row.E_Estacionamento_DE}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
