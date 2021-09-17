



<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');
                
                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                        //$(this).find('img').width(120);
                        //$(this).find('img').height(160);
                }, function() {
                        $(this).css('background','white');
                        //$(this).find('img').width(0);
                        //$(this).find('img').height(0);
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
                document.forms[0].submit();
            }
        }
        

    </script>  

    
    <%@include file="menu.jsp"%>
        
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Empréstimo de Material - Seleção de Sócio</div>
    <div class="divisoria"></div>
    
    <form action="c" method="POST" onsubmit="return validarForm()">
        <input type="hidden" name="app" value="1140">
        <input type="hidden" name="acao" value="consultar">

        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Categoria</p>
                  <input type="text" name="categoria" class="campoSemTamanho alturaPadrao larguraNumero" value="${categoria}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Título</p>
                  <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNumero" value="${matricula}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nome</p>
                  <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:330px" value="${nome}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nr. Carteirinha</p>
                  <input type="text" name="carteira" class="campoSemTamanho alturaPadrao larguraData" value="${carteira}">
              </td>
              <td >    
                  <input class="botaobuscainclusao" style="margin-top:20px" type="submit" onclick="validarForm()" value="" title="Consultar" />              </td>
              
            </tr>
        </table>
    </form>
        
    <form action="c" method="POST">
        <input type="hidden" name="app" value="1140">
        <input type="hidden" name="acao" value="listaMateriais">
        
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col" >Selecionar</th>
                    <th scope="col" class="nome-lista">Nome</th>
                    <th scope="col">Matrícula</th>
                    <th scope="col">Tp Cadastro</th>
                    <th scope="col">Categoria</th>
                    <th scope="col">Tp Categoria</th>
                    <th scope="col">Foto</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="socio" items="${socios}">
                <tr>
                    <td id="tabSel" class="column1" align="center" style="width:30px">
                        <fmt:formatNumber pattern="0000" var="matricula" value="${socio.matricula}"/>
                        <fmt:formatNumber pattern="00" var="categoria" value="${socio.idCategoria}"/>
                        <fmt:formatNumber pattern="00" var="dependente" value="${socio.seqDependente}"/>
                        <input type="radio" name="titulo" checked value="${matricula}${categoria}${dependente}" />
                    </td>

                    <td id="tabNome" class="column1">${socio.nome}</td>

                    <th scope="col">${socio.matricula}</th>
                    <th scope="col">
                        <c:if test='${socio.seqDependente == 0}'>TITULAR</c:if>
                        <c:if test='${socio.seqDependente > 0}'>DEPENDENTE</c:if>
                    </th>
                    <th scope="col">${socio.categoria}</th>
                    <th scope="col">
                        <c:if test='${socio.tipoCategoria == "SO"}'>SÓCIO</c:if>
                        <c:if test='${socio.tipoCategoria == "NS"}'>NÃO SÓCIO</c:if>
                    </th>

                    <td id="tabFoto" class="column1" align="center" >
                        <img src="f?tb=6&mat=${socio.matricula}&seq=${socio.seqDependente}&cat=${socio.idCategoria}" class="recuoPadrao MargemSuperiorPadrao" width="80" height="100" onerror="this.src='images/tenis/avatar-default.png';">
                    </td>    
                </tr>
            </c:forEach>
            </tbody>
        </table>
        
        <input type="submit" id="atualizar" class="botaoatualizar" value=" " />
        
    </form>
        
</body>
</html>
