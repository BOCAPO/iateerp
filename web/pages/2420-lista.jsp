
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
            
        $("#dtEntradaInicial").mask("99/99/9999");
        $("#dtEntradaFinal").mask("99/99/9999");
    });        
</script>  
<script type="text/javascript" language="JavaScript">
    function validarForm(){
        
        document.forms[0].submit();
    }
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Achados e Perdidos</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="2420">
        <input type="hidden" name="acao" value="consultar">

        <table class="fmt" align="left" >
            <tr>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Setor Encontrado</p>
                <div class="selectheightnovo">
                  <select name="setorEncontrado" id="setor" class="campoSemTamanho alturaPadrao"  style="width: 250px">
                    <option value="" selected>Todos</option>
                    <option value="0" <c:if test='${objeto.modelo == 0}'>selected</c:if>>Não informado</option>
                    <c:forEach var="setor" items="${setores}">
                        <option value="${setor.id}" <c:if test='${setorEncontrado == setor.id}'>selected</c:if>>${setor.descricao}</option>
                    </c:forEach>
                  </select>
                </div>
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Definição</p>
                <div class="selectheightnovo">
                  <select name="definicao" id="unidade" class="campoSemTamanho alturaPadrao"  style="width: 150px">
                    <option value="" selected>Todos</option>
                    <option value="0" <c:if test='${definicao == 0}'>selected</c:if>>Não informado</option>
                    <option value="1" <c:if test='${definicao == 1}'>selected</c:if>>Moda Praia</option>
                    <option value="2" <c:if test='${definicao == 2}'>selected</c:if>>Acessório Esportivo</option>
                    <option value="3" <c:if test='${definicao == 3}'>selected</c:if>>Acessório Aquático</option>
                    <option value="4" <c:if test='${definicao == 4}'>selected</c:if>>Vestiário</option>
                    <option value="5" <c:if test='${definicao == 5}'>selected</c:if>>Outros</option>
                    <option value="6" <c:if test='${definicao == 6}'>selected</c:if>>Acessórios</option>
                    <option value="7" <c:if test='${definicao == 7}'>selected</c:if>>Brinquedos</option>
                    <option value="8" <c:if test='${definicao == 8}'>selected</c:if>>Eletrônico</option>
                    <option value="9" <c:if test='${definicao == 9}'>selected</c:if>>Material escolar</option>
                    <option value="10" <c:if test='${definicao == 10}'>selected</c:if>>Moda praia</option>
                    <option value="11" <c:if test='${definicao == 11}'>selected</c:if>>Vestuário</option>
                    <option value="12" <c:if test='${definicao == 12}'>selected</c:if>>Acessórios esportivos</option>
                    <option value="13" <c:if test='${definicao == 13}'>selected</c:if>>Acessórios aquáticos</option>
                    <option value="14" <c:if test='${definicao == 14}'>selected</c:if>>Chapelaria</option>
                    <option value="15" <c:if test='${definicao == 15}'>selected</c:if>>Higiene e Limpeza</option>
                    <option value="16" <c:if test='${definicao == 16}'>selected</c:if>>Bolsas</option>
                    <option value="17" <c:if test='${definicao == 17}'>selected</c:if>>Calçados</option>
                    <option value="18" <c:if test='${definicao == 18}'>selected</c:if>>Óculos</option>
                    <option value="19" <c:if test='${definicao == 19}'>selected</c:if>>Saboneteira</option>
                    <option value="20" <c:if test='${definicao == 20}'>selected</c:if>>Acessórios diversos</option>
                    <option value="21" <c:if test='${definicao == 21}'>selected</c:if>>Utensílios de cozinha</option>
                    <option value="22" <c:if test='${definicao == 22}'>selected</c:if>>Mesa e banho</option>
                    <option value="23" <c:if test='${definicao == 23}'>selected</c:if>>Bolas em geral</option>
                    <option value="24" <c:if test='${definicao == 24}'>selected</c:if>>Garrafas</option>
                    <option value="25" <c:if test='${definicao == 25}'>selected</c:if>>Acessórios de natação</option>
                  </select>
                </div>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Situação</p>
                  <div class="selectheightnovo">
                    <select name="situacao" id="situacao" class="campoSemTamanho alturaPadrao" style="width:180px;" >
                      <option value="0" <c:if test='${situacao == 0}'>selected</c:if>>Aguardando Retirada</option>
                      <option value="1" <c:if test='${situacao == 1}'>selected</c:if>>Devolvido</option>
                      <option value="2" <c:if test='${situacao == 2}'>selected</c:if>>Extraviado</option>
                      <option value="3" <c:if test='${situacao == 3}'>selected</c:if>>Processo de Doação</option>
                    </select>
                  </div>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                  <input type="text" name="descricao" id="descricao" class="campoSemTamanho alturaPadrao" style="width:180px;"  value="${descricao}">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Dt. Entrada Inicial</p>
                <input type="text" name="dtEntradaInicial" id="dtEntradaInicial" class="campoSemTamanho alturaPadrao" style="width: 100px" value="${dtEntradaInicial}">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Dt. Entrada Final</p>
                <input type="text" name="dtEntradaFinal" id="dtEntradaFinal" class="campoSemTamanho alturaPadrao" style="width: 100px" value="${dtEntradaFinal}">
              </td>
              
              
              <td >    
                  <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Consultar" />
              </td>
              <td> &nbsp;&nbsp;&nbsp;
                <c:if test='<%=request.isUserInRole("2421")%>'>
                     <a href="c?app=2421&acao=showForm"><img src="imagens/btn-incluir.png" style="margin-top:25px" width="100" height="25" /></a>
                </c:if>
              </td>
            </tr>
        </table>
        <br><br>
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col" class="nome-lista">Nr. Ident.</th>
                    <th scope="col" class="nome-lista">Descrição</th>
                    <th scope="col" align="center">Condição do Objeto</th>
                    <th scope="col" align="center">Definição</th>
                    <th scope="col" align="center">Marca</th>
                    <th scope="col" align="center">Cor</th>
                    <th scope="col" align="center">Setor Encontrado</th>
                    <th scope="col" align="center">Dt. Catalogação</th>
                    <th scope="col" align="center">Prateleira</th>
                    <th scope="col" align="center">Situação</th>
                    <th scope="col" align="center">Alterar</th>
                    <th scope="col" align="center">Excluir</th>
                    <th scope="col" align="center">Fotos</th>
                    <th scope="col" align="center">Imp. Dev.</th>
                </tr>
            </thead>
            <tbody>
                
                <c:forEach var="objeto" items="${lista}">
                    <tr>
                        <td class="column1">${objeto.id}</td>
                        <td class="column1">${objeto.descricao}</td>
                        <td class="column1">${objeto.condicao}</td>
                        
                        <c:choose>
                            <c:when test='${objeto.definicao == 0}'><td class="column1" align="center">Não Informado</td></c:when>
                            <c:when test='${objeto.definicao == 1}'><td class="column1" align="center">Moda Praia</td></c:when>
                            <c:when test='${objeto.definicao == 2}'><td class="column1" align="center">Acessório Esportivo</td></c:when>
                            <c:when test='${objeto.definicao == 3}'><td class="column1" align="center">Acessório Aquático</td></c:when>
                            <c:when test='${objeto.definicao == 4}'><td class="column1" align="center">Vestiário</td></c:when>
                            <c:when test='${objeto.definicao == 5}'><td class="column1" align="center">Outros</td></c:when>
                            <c:when test='${objeto.definicao == 6}'><td class="column1" align="center">Acessórios</td></c:when>
                            <c:when test='${objeto.definicao == 7}'><td class="column1" align="center">Brinquedos</td></c:when>
                            <c:when test='${objeto.definicao == 8}'><td class="column1" align="center">Eletrônico</td></c:when>
                            <c:when test='${objeto.definicao == 9}'><td class="column1" align="center">Material escolar</td></c:when>
                            <c:when test='${objeto.definicao == 10}'><td class="column1" align="center">Moda praia</td></c:when>
                            <c:when test='${objeto.definicao == 11}'><td class="column1" align="center">Vestuário</td></c:when>
                            <c:when test='${objeto.definicao == 12}'><td class="column1" align="center">Acessórios esportivos</td></c:when>
                            <c:when test='${objeto.definicao == 13}'><td class="column1" align="center">Acessórios aquáticos</td></c:when>
                            <c:when test='${objeto.definicao == 14}'><td class="column1" align="center">Chapelaria</td></c:when>
                            <c:when test='${objeto.definicao == 15}'><td class="column1" align="center">Higiene e Limpeza</td></c:when>
                            <c:when test='${objeto.definicao == 16}'><td class="column1" align="center">Bolsas</td></c:when>
                            <c:when test='${objeto.definicao == 17}'><td class="column1" align="center">Calçados</td></c:when>
                            <c:when test='${objeto.definicao == 18}'><td class="column1" align="center">Óculos</td></c:when>
                            <c:when test='${objeto.definicao == 19}'><td class="column1" align="center">Saboneteira</td></c:when>
                            <c:when test='${objeto.definicao == 20}'><td class="column1" align="center">Acessórios diversos</td></c:when>
                            <c:when test='${objeto.definicao == 21}'><td class="column1" align="center">Utensílios de cozinha</td></c:when>
                            <c:when test='${objeto.definicao == 22}'><td class="column1" align="center">Mesa e banho</td></c:when>
                            <c:when test='${objeto.definicao == 23}'><td class="column1" align="center">Bolas em geral</td></c:when>
                            <c:when test='${objeto.definicao == 24}'><td class="column1" align="center">Garrafas</td></c:when>
                            <c:when test='${objeto.definicao == 25}'><td class="column1" align="center">Acessórios de natação</td></c:when>
                        </c:choose>                        
                            
                        <td class="column1">${objeto.marca}</td>
                        <td class="column1">${objeto.cor}</td>
                        <td class="column1">${objeto.setorEncontrado.descricao}</td>
                        
                        <td class="column1" align="center"><fmt:formatDate value="${objeto.dtCatalogacao}" pattern="dd/MM/yyyy" /></td>
                        <td class="column1">${objeto.nrPrateleira}</td>
                        
                        <c:choose>
                            <c:when test='${objeto.situacao == 0}'><td class="column1" align="center">Aguardando Retirada</td></c:when>
                            <c:when test='${objeto.situacao == 1}'><td class="column1" align="center">Devolvido</td></c:when>
                            <c:when test='${objeto.situacao == 2}'><td class="column1" align="center">Extraviado</td></c:when>
                            <c:when test='${objeto.situacao == 3}'><td class="column1" align="center">Processo de Doação</td></c:when>
                        </c:choose>                        
                            
                        <!-- ALTERAR -->
                        <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("2422")%>'>
                            <a href="c?app=2422&acao=showForm&id=${objeto.id}">
                                <img src="imagens/icones/inclusao-titular-03.png"/>
                            </a>
                        </c:if>
                        </td>
                        
                        <!-- EXCLUIR -->
                        <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("2423")%>'>
                            <a href="javascript: if(confirm('Confirma a exclusão do objeto selecionado?')) window.location.href='c?app=2423&id=${objeto.id}'">
                                <img src="imagens/icones/inclusao-titular-05.png"/>
                            </a>
                        </c:if>
                        </td>
                        
                        <!-- FOTOS -->
                        <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("2425")%>'>
                            <a href="c?app=2425&acao=showFoto&idObjeto=${objeto.id}"><img src="imagens/icones/inclusao-titular-07.png"/></a>
                        </c:if>

                        </td>
                        
                        <!-- IMPRIMIR TERMO DEVOLUCAO -->
                        <td class="column1" align="center">
                            <a href="c?app=2420&acao=imprimir&idObjeto=${objeto.id}"><img src="imagens/icones/inclusao-titular-13.png"/></a>
                        </td>
                        
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
    </form>
    
    
</body>
</html>
