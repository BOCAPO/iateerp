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

                
                //$('#nome').focus();
        });        
        
        function validarForm(){
            var carteira = trim(document.forms[0].carteira.value);
            var categoria = trim(document.forms[0].categoria.value);
            var matricula = trim(document.forms[0].matricula.value);
            var nome = trim(document.forms[0].nome.value);

            if(carteira == '' && categoria == '' && matricula == '' && nome == ''){
                alert('É preciso informar ao menos 1 parâmetro de consulta');
                return false;
            } else if(nome != '' && nome.length < 3){
                alert('Nome para pesquisa deve ter no mínimo 3 letras.');
                return false;
            } else {
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
            for (var b = 1; b <= 21; b++) {
                desativarBotao(b);
            }

            <c:if test='<%=request.isUserInRole("9031") || request.isUserInRole("9041")%>'>
                ativarBotao(1, "c?app=9031&acao=showForm");
            </c:if>
                
        }
        
        function atualizarBotoes(nome, matricula, seqDependente, idCategoria, situacao, alerta) {
            inicializarBotoes();
            for (var b = 2; b <= 21; b++) {
                switch (b) {
                    case  2: // Incluir Dependente Titular selecionado
                        if (situacao != "CA") {
                            if (idCategoria == 91) {
                                <c:if test='<%=request.isUserInRole("9045")%>'>
                                    ativarBotao(b, "c?app=9045&acao=showForm&matricula=" + matricula + "&seqDependente=0&idCategoria=" + idCategoria);
                                </c:if>
                            } else {
                                <c:if test='<%=request.isUserInRole("9035")%>'>
                                    ativarBotao(b, "c?app=9035&acao=showForm&matricula=" + matricula + "&seqDependente=0&idCategoria=" + idCategoria);
                                </c:if>
                            }
                        }
                        break;
                    
                    case  3: // Alterar dados Titular/Dependente selecionado
                        if (situacao != "CA") {
                            if (seqDependente == 0 && idCategoria == 91) {
                                <c:if test='<%=request.isUserInRole("9042")%>'>
                                    ativarBotao(b, "c?app=9042&acao=showForm&matricula=" + matricula + "&seqDependente=0&idCategoria=" + idCategoria);
                                </c:if>
                            } else if (seqDependente == 0 && idCategoria != 91) {
                                <c:if test='<%=request.isUserInRole("9032")%>'>
                                    ativarBotao(b, "c?app=9032&acao=showForm&matricula=" + matricula + "&seqDependente=0&idCategoria=" + idCategoria);
                                </c:if>
                            } else if (seqDependente > 0 && idCategoria == 91) {
                                <c:if test='<%=request.isUserInRole("9046")%>'>
                                    ativarBotao(b, "c?app=9046&acao=showForm&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
                                </c:if>
                            } else if (seqDependente > 0 && idCategoria != 91) {
                                <c:if test='<%=request.isUserInRole("9036")%>'>
                                    ativarBotao(b, "c?app=9036&acao=showForm&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
                                </c:if>
                            }
                        }
                        break;
                    
                    case  4: // Alterar Endereço do Titular Selecionado
                        if (seqDependente == 0 && situacao != "CA") {
                            if (idCategoria == 91) {
                                <c:if test='<%=request.isUserInRole("9049")%>'>
                                    ativarBotao(b, "c?app=9049&acao=showForm&matricula=" + matricula + "&seqDependente=0&idCategoria=" + idCategoria);
                                </c:if>                                
                            } else {
                                <c:if test='<%=request.isUserInRole("9039")%>'>
                                    ativarBotao(b, "c?app=9039&acao=showForm&matricula=" + matricula + "&seqDependente=0&idCategoria=" + idCategoria);
                                </c:if>
                            }
                        }
                        break;
                    
                    case  5: // Excluir Titular/Dependente selecionado
                        if (situacao != "CA") {
                            if (seqDependente == 0 && idCategoria == 91) {
                                <c:if test='<%=request.isUserInRole("9043")%>'>
                                    ativarBotao(b, "javascript: if(confirm('confirma exclusao do titular e TODOS os seus dependentes?')) window.location.href='c?app=9043&matricula=" + matricula + "&seqDependente=0&idCategoria=" + idCategoria + "'");
                                </c:if>
                            } else if (seqDependente == 0 && idCategoria != 91) {
                                <c:if test='<%=request.isUserInRole("9033")%>'>
                                    ativarBotao(b, "javascript: if(confirm('confirma exclusao do titular e TODOS os seus dependentes?')) window.location.href='c?app=9033&matricula=" + matricula + "&seqDependente=0&idCategoria=" + idCategoria + "'");
                                </c:if>
                            } else if (seqDependente > 0 && idCategoria == 91) {
                                <c:if test='<%=request.isUserInRole("9047")%>'>
                                    ativarBotao(b, "javascript: if(confirm('confirma exclusao de " + nome + "?')) window.location.href='c?app=9047&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria + "'");
                                </c:if>
                            } else if (seqDependente > 0 && idCategoria != 91) {
                                <c:if test='<%=request.isUserInRole("9037")%>'>
                                    ativarBotao(b, "javascript: if(confirm('confirma exclusao de " + nome + "?')) window.location.href='c?app=9037&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria + "'");
                                </c:if>
                            }
                        }
                        break;
                    
                    case  6: // Detalhes pessoa selecionada
                        ativarBotao(b, "c?app=9030&acao=detalhar&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
                        break;
                    
                    case 7:  // Emitir Carteirinha p/ Pessoa selecionada
                        if (situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("9080")%>'>
                                ativarBotao(b, "c?app=9080&matricula=" + matricula  + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria + "&acao=showForm");
                            </c:if>
                        }
                        break;
                    
                    case  9: // Associa a pessoa a uma Modalidade esportiva
                        if (situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("1105")%>'>
                                ativarBotao(b, "c?app=1105&acao=showForm&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria + "&alerta=" + alerta);
                            </c:if>                        
                        }                                                
                        break;
                    
                    case 10: // Inclui uma Ocorrencia para a pessoa
                        if (situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("1170")%>'>
                                ativarBotao(b, "c?app=1170&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
                            </c:if>                        
                        }                        
                        break;
                    
                    case 11: // Barcos
                        if (situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("2000")%>'>
                                ativarBotao(b, "c?app=2000&acao=showForm&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
                            </c:if>                        
                        }
                        break;
                    
                    case 12: // Histórico do Título
                        if (seqDependente == 0 && situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("1540")%>'>
                                ativarBotao(b, "c?app=1540&matricula=" + matricula + "&idCategoria=" + idCategoria);
                            </c:if>
                        }                        
                        break;
                    
                    case 13: // Cursos de um aluno
                        if (situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("3080")%>'>
                                ativarBotao(b, "c?app=3080&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
                            </c:if>
                        }
                        break;
                    
                    case 14: // Armário do Sócio
                        if (situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("1340")%>'>
                                ativarBotao(b, "c?app=1340&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
                            </c:if>
                        }
                        break;
                    
                    case 15: // Carnes da pessoa selecionada
                        <c:if test='<%=request.isUserInRole("1360")%>'>
                            ativarBotao(b, "c?app=1360&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
                        </c:if>
                        break;
                    
                    case 16: // Seleção de Taxas para Não Geração no Carne
                        if (seqDependente == 0 && situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("1610")%>'>
                                ativarBotao(b, "c?app=1610&acao=showForm&matricula=" + matricula + "&idCategoria=" + idCategoria + "&alerta=" + alerta);
                            </c:if>
                        }
                        break;
                    
                    case 17: // Autorização de Dependentes em Taxas
                        if (seqDependente > 0 && situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("1740")%>'>
                                ativarBotao(b, "c?app=1740&acao=showForm&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria + "&alerta=" + alerta);
                            </c:if>
                        }
                        break;
                    
                    case 18: // Bloqueio de Taxa
                        if (seqDependente == 0 && situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("2120")%>'>
                                ativarBotao(b, "c?app=2120&acao=showForm&matricula=" + matricula + "&idCategoria=" + idCategoria + "&alerta=" + alerta);
                            </c:if>
                        }
                        break;
                    
                    case 19: // Acesso pela Internet
                        if (situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("1880")%>'>
                                ativarBotao(b, "c?app=1880&acao=showForm&matricula=" + matricula + "&idCategoria=" + idCategoria + "&seqDependente=" + seqDependente);
                            </c:if>
                        }                        
                        break;
                    
                    case 20: // Cadastro de Carro
                        if (seqDependente == 0 && situacao != "CA") {
                            <c:if test='<%=request.isUserInRole("2190")%>'>
                                ativarBotao(b, "c?app=2190&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria);
                            </c:if>
                        }
                        break;
                    
                    case 21: // Pendencia de atualização cadastral
                        if (situacao != "CA") {
                            if (seqDependente == 0){
                                <c:if test='<%=request.isUserInRole("2260")%>'>
                                    ativarBotao(b, "c?app=2260&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria + "&acao=showForm");
                                </c:if>
                            }else{
                                <c:if test='<%=request.isUserInRole("2261")%>'>
                                    ativarBotao(b, "c?app=2261&matricula=" + matricula + "&seqDependente=" + seqDependente + "&idCategoria=" + idCategoria + "&acao=showForm");
                                </c:if>
                            }
                        }                        
                        break;
                }
            }
        }
        
    </script>

    <div id="blocoicones">
        <ul>
            <li id="icone01"><a id="b01" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-01.png" title="Incluir Titular" /></a></li>
            <li id="icone02"><a id="b02" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-02.png" title="Incluir Dependente Titular selecionado" /></a></li>
            <li id="icone03"><a id="b03" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-03.png" title="Alterar dados Titular/Dependente selecionado" /></a></li>
            <li id="icone04"><a id="b04" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-04.png" title="Alterar Endereço do Titular Selecionado" /></a></li>
            <li id="icone05"><a id="b05" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-05.png" title="Excluir Titular/Dependente selecionado" /></a></li>
            <li id="icone06"><a id="b06" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-06.png" title="Detalhes pessoa selecionada" /></a></li>
            <li id="icone07"><a id="b07" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-07.png" title="Emitir Carteirinha p/ Pessoa selecionada" /></a></li>
            <li id="icone09"><a id="b09" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-09.png" title="Associa a pessoa a uma Modalidade esportiva" /></a></li>
            <li id="icone10"><a id="b10" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-10.png" title="Inclui uma Ocorrencia para a pessoa" /></a></li>
            <li id="icone11"><a id="b11" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-11.png" title="Barcos" /></a></li>    
            <li id="icone12"><a id="b12" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-12.png" title="Histórico do Título" /></a></li>    
            <li id="icone13"><a id="b13" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-13.png" title="Cursos de um aluno" /></a></li>    
            <li id="icone14"><a id="b14" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-14.png" title="Armário do Sócio" /></a></li>
            <li id="icone15"><a id="b15" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-15.png" title="Carnes da pessoa selecionada" /></a></li>
            <li id="icone16"><a id="b16" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-16.png" title="Seleção de Taxas para Não Geração no Carne" /></a></li>    
            <li id="icone17"><a id="b17" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-17.png" title="Autorização de Dependentes em Taxas" /></a></li>    
            <li id="icone18"><a id="b18" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-18.png" title="Bloqueio de Taxa" /></a></li>
            <li id="icone19"><a id="b19" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-19.png" title="Acesso pela Internet" /></a></li>
            <li id="icone20"><a id="b20" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-20.png" title="Cadastro de Carro" /></a></li>
            <li id="icone21"><a id="b21" href="javascript:void(0);"><img src="imagens/icones/inclusao-titular-21.png" title="Pendencia de atualização cadastral" /></a></li>
        </ul>
    </div>

    <div id="blocopesquisa">
        <form action="c" method="POST" onsubmit="return validarForm()" >
            <input type="hidden" name="app" value="9030">
            <input type="hidden" name="acao" value="consultar">
            
            <label class="barrainclusaotext">&nbsp;&nbsp;&nbsp;Carteira</label>
            <input class="formbarrainclusao1" type="text" name="carteira" onkeypress="onlyNumber(event)" value="${carteira}" />
            
            <label class="barrainclusaotext">Categoria</label>
            <input class="formbarrainclusao2" type="text" name="categoria" onkeypress="onlyNumber(event)" value="${categoria}"/>
            
            <label class="barrainclusaotext">Título</label>
            <input class="formbarrainclusao3" type="text" name="matricula" value="${matricula}"/>
            
            <label class="barrainclusaotext">Nome</label>
            <input class="formbarrainclusao4" type="text" name="nome" id="nome" value="${nome}"/>
            
            <input class="botaobuscainclusao" type="submit" value="" title="Consultar" />
        </form>
        <div class="clear"></div>
    </div>            

    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">Alerta</th>
                <th scope="col">Selecionar</th>
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col">Matricula</th>
                <th scope="col">Dep</th>
                <th scope="col">Data de Nascimento</th>
                <th scope="col">Tipo de Cadastro</th>
                <th scope="col">Categoria</th>
                <th scope="col">Tipo de Categoria</th>
            </tr>	
        </thead>
        <tbody>
            <c:forEach var="socio" items="${socios}">
                <tr onclick="this.getElementsByTagName('input')[0].click();">
                    <td class="column1" align="center">
                        <c:if test="${socio.alerta == 0}">
                            <img src="img/blank.gif" title="" />
                        </c:if>
                        <c:if test="${socio.alerta == 1}">
                            <img src="img/socio-alerta1.png" title="Situação Especial" />
                        </c:if>
                        <c:if test="${socio.alerta == 2}">
                            <img src="img/socio-alerta2.png" title="Excluído(a)" />
                        </c:if>
                        <c:if test="${socio.alerta == 4}">
                            <img src="img/socio-alerta4.png" title="Embarcação" />
                        </c:if>
                    </td>
                    
                    <td class="column1" align="center">
                        <input type="radio" name="selecao" id="selecao" value="" onclick="atualizarBotoes('${socio.nome}', ${socio.matricula}, ${socio.seqDependente}, ${socio.idCategoria}, '${socio.situacao}', ${socio.alerta})" />
                    </td>
                    
                    <td class="column1" id="tabNome">${socio.nome}</td>
                    
                    <td class="column1">${socio.matricula}</td>
                    <td class="column1">${socio.seqDependente}</td>
                    
                    <fmt:formatDate var="nascimento" value="${socio.dataNascimento}" pattern="dd/MM/yyyy" />
                    <td class="column1">${nascimento}</td>
                    
                    <c:choose>
                        <c:when test='${socio.seqDependente == 0}'>
                            <td class="column1" id="tabTipo">TITULAR</td>
                        </c:when>
                        <c:otherwise>
                            <td class="column1" id="tabTipo">DEPENDENTE</td>
                        </c:otherwise>
                    </c:choose>
                    
                    <td class="column1">${socio.categoria}</td>
                    
                    <c:choose>
                        <c:when test='${socio.tipoCategoria == "SO"}'>
                            <td class="column1">SÓCIO</td>
                        </c:when>
                        <c:otherwise>
                            <td class="column1">NÃO SÓCIO</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <script type="text/javascript" language="JavaScript">
        window.onload = window.onpageshow = function() {
            $("#tabela tr:eq(1)").each(function(index){
                if ($(this).find("#tabTipo").html() == "TITULAR"){
                    this.getElementsByTagName('input')[0].click();
                }
            });
            
        };
    </script>

</body>
</html>
