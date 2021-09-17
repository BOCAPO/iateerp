
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

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
</script>  

<script type="text/javascript" language="JavaScript">
        function validarForm(){

            document.forms[0].submit();
        }
        
        function validarForm2(){

            window.location.href='c?app=2000&acao=showFormImprimir'+
                                 '&matricula='+document.forms[0].matricula.value+
                                 '&categoria='+document.forms[0].categoria.value+
                                 '&nome='+document.forms[0].nome.value+
                                 '&capitania='+document.forms[0].capitania.value+
                                 '&nomeBarco='+document.forms[0].nomeBarco.value+
                                 '&catNautica='+document.forms[0].catNautica.value+
                                 '&doc='+document.forms[0].doc.value+
                                 '&tipo='+document.forms[0].tipo.value;
                             
        }
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Consulta Barcos</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="2005">
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
                  <input type="text" name="nome" class="campoSemTamanho alturaPadrao " style="width:200px" value="${nome}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nº Capitania</p>
                  <input type="text" name="capitania" class="campoSemTamanho alturaPadrao" style="width:100px" value="${capitania}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nome do Barco</p>
                  <input type="text" name="nomeBarco" class="campoSemTamanho alturaPadrao" style="width:200px" value="${nomeBarco}">
              </td>
              <td>
                    <p class="legendaCodigo MargemSuperior0">Categoria Náutica:</p>
                    <div class="selectheightnovo">
                        <select name="catNautica" class="campoSemTamanho alturaPadrao" style="width:180px;" >
                            <option value="TODAS" selected>&LT;TODAS&GT;</option>
                            <c:forEach var="categoria" items="${categorias}">
                                <option value="${categoria.id}"  <c:if test='${categoria.id == catNautica}'>selected</c:if>  >${categoria.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Doc.</p>
                  <div class="selectheightnovo">
                    <select name="doc" class="campoSemTamanho alturaPadrao"  >
                        <option value="T" selected>TODAS</option>
                        <option value="S" <c:if test='${doc == "S"}'>selected</c:if>>OK</option>
                        <option value="N" <c:if test='${doc == "N"}'>selected</c:if>>Pendente</option>
                    </select>
                  </div>        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                  <div class="selectheightnovo">
                    <select name="tipo" class="campoSemTamanho alturaPadrao"  >
                        <option value="T" selected>TODOS</option>
                        <option value="N" <c:if test='${tipo == "N"}'>selected</c:if>>Barco</option>
                        <option value="S" <c:if test='${tipo == "S"}'>selected</c:if>>Box</option>
                    </select>
                  </div>        
              </td>
              <td>    
                  <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Consultar" />
              </td>
            </tr>
        </table>
    </form>
    
    <br><br><br>

    <table class="fmt" border="0" >
        <tr>
            <td>
                <div class=" campoSemTamanho alturaPadrao">
                    <a href="javascript:validarForm2()"><img src="imagens/btn-imprimir.png" width="100" height="25" /></a>
                <div>
            </td>
        <tr>
    </table>
                
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
            <tr class="odd">
                <th scope="col">Nome</th>
                <th scope="col">Matrícula</th>
                <th scope="col">Categoria</th>
                <th scope="col">Barco/Box</th>
                <th scope="col">Cat. Náutica/Local</th>
                <th scope="col">Alterar</th>
                <th scope="col">Excluir</th>
                <th scope="col">Pessoas Aut.</th>
                <th scope="col">Movim.</th>
                <th scope="col">Fotos</th>
                <th scope="col">Registro</th>
                <th scope="col">Baixa</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="barco" items="${barcos}">
                <tr>
                    <td class="column1">
                        <a href="c?app=2000&acao=detalhes&idBarco=${barco.id}">
                            ${barco.socio.nome}
                        </a>
                    </td>
                    <td class="column1" align="center">${barco.socio.matricula}</td>
                    <td class="column1" align="center">${barco.socio.categoria}</td>
                    <td class="column1">${barco.nome}</td>
                    
                    <c:if test='${barco.ehBox=="N"}'>
                        <td class="column1" align="center">${barco.categoriaNautica.descricao}</td>
                    </c:if>
                    <c:if test='${barco.ehBox=="S"}'>
                        <td class="column1" align="center">${barco.localBox.descricao}</td>
                    </c:if>

                    <td align="center">    
                        <c:if test='<%=request.isUserInRole("2002")%>'>
                            <a href="c?app=2002&acao=showForm&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </c:if>
                        </td>
                    <td align="center">
                        <c:if test='<%=request.isUserInRole("2003")%>'>
                            <a href="javascript: if(confirm('Confirma a exclusão do barco?')) window.location.href='c?app=2003&idBarco=${barco.id}&acao=excluir'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </c:if>
                        </td>
                    <td align="center">    
                        <c:if test='<%=request.isUserInRole("2002")%>'>
                            <c:if test='${barco.ehBox=="N"}'>
                                <a href="c?app=2002&acao=showListaPessoa&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-01.png"/></a>
                            </c:if>
                        </c:if>
                    </td>
                    <td align="center">    
                        <c:if test='${barco.ehBox=="N"}'>
                            <a href="c?app=2000&acao=showListaMovimento&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-16.png"/></a>
                        </c:if>
                    </td>
                    <td align="center">    
                        <c:if test='${barco.ehBox=="N"}'>
                            <a href="c?app=2000&acao=showFoto&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-07.png"/></a>
                        </c:if>
                    </td>
                    <td align="center">    
                        <a href="c?app=2000&acao=imprimirRegistro&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-13.png"/></a>
                    </td>
                    <td align="center">    
                        <a href="c?app=2000&acao=imprimirBaixa&idBarco=${barco.id}"><img src="imagens/icones/inclusao-titular-13.png"/></a>
                    </td>                
                </tr>
            </c:forEach>
        </tbody>
    </table>

        

    
    
</body>
</html>
