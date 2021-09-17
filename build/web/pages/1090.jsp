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

    <div id="blocopesquisa2">
        <form action="c" method="POST" onsubmit="return validarForm()">
            <input type="hidden" name="app" value="1091">
            <input type="hidden" name="acao" value="consultar">
            
            <label class="barrainclusaotext">&nbsp;&nbsp;&nbsp;Carteira</label>
            <input class="formbarrainclusao1" type="text" name="carteira" onkeypress="onlyNumber(event)" value="${carteira}" />
            
            <label class="barrainclusaotext">Categoria</label>
            <input class="formbarrainclusao2" type="text" name="categoria" onkeypress="onlyNumber(event)" value="${categoria}"/>
            
            <label class="barrainclusaotext">Título</label>
            <input class="formbarrainclusao3" type="text" name="matricula" value="${matricula}"/>
            
            <label class="barrainclusaotext">Nome</label>
            <input class="formbarrainclusao4" type="text" name="nome" value="${nome}"/>
            
            <input class="botaobuscainclusao" type="submit" value="" title="Consultar" />
        </form>
        <div class="clear"></div>
    </div>            

    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">Alerta</th>
                <th scope="col">Titulo</th>
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col">Data de Nascimento</th>
                <th scope="col">Categoria</th>
                <th scope="col">Tipo de Categoria</th>
            </tr>	
        </thead>
        <tbody>
            <c:forEach var="socio" items="${socios}">
                <c:if test="${socio.seqDependente == 0}">
                <tr>
                    <td class="column1" align="center">
                        <c:if test="${socio.alerta == 0}">
                            <img src="img/blank.gif" title="" />
                        </c:if>
                        <c:if test="${socio.alerta == 2}">
                            <img src="img/socio-alerta2.png" title="Excluído(a)" />
                        </c:if>
                        <c:if test="${socio.alerta == 5}">
                            <img src="img/socio-alerta5.png" title="Suspenso(a)" />
                        </c:if>
                    </td>
                    
                    <td class="column1">${socio.matricula}/${socio.idCategoria}</td>
                    
                    <td class="column1"><a href="c?app=1091&acao=showForm&matricula=${socio.matricula}&idCategoria=${socio.idCategoria}">${socio.nome}</a></td>
                    
                    <fmt:formatDate var="nascimento" value="${socio.dataNascimento}" pattern="dd/MM/yyyy" />
                    <td class="column1">${nascimento}</td>
                    
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
                </c:if>
            </c:forEach>
        </tbody>
    </table>



</body>
</html>
