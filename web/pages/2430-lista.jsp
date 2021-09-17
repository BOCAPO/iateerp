
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
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Boleto Avulso</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="2430">
        <input type="hidden" name="acao" value="consultar">

        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Sacado</p>
                  <input type="text" name="sacado" id="sacado" class="campoSemTamanho alturaPadrao" style="width:180px;"  value="${sacado}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Taxa</p>
                  <div class="selectheightnovo">
                      <select name="taxa" id="setor" class="campoSemTamanho alturaPadrao"  style="width: 200px">
                        <option value="0">&LT;TODAS&GT;></option>
                        <c:forEach var="taxa" items="${taxas}">
                            <option value="${taxa.id}" <c:if test='${taxa.id == taxaSel}'>selected</c:if>>${taxa.descricao}</option>
                        </c:forEach>
                      </select>
                  </div>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Situação</p>
                  <div class="selectheightnovo">
                    <select name="situacao" class="campoSemTamanho alturaPadrao" style="width:180px;" >
                      <option value="" selected>&LT;TODAS&GT;</option>
                      <option value="NP" <c:if test='${situacao == "NP"}'>selected</c:if>>Não Pago</option>
                      <option value="PG" <c:if test='${situacao == "PG"}'>selected</c:if>>Pago</option>
                      <option value="CA" <c:if test='${situacao == "CA"}'>selected</c:if>>Cancelado</option>
                    </select>
                  </div>
              </td>
              <td >    
                  <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Consultar" />
              </td>
              <td> &nbsp;&nbsp;&nbsp;
                <c:if test='<%=request.isUserInRole("2431")%>'>
                     <a href="c?app=2431&acao=showForm"><img src="imagens/btn-incluir.png" style="margin-top:25px" width="100" height="25" /></a>
                </c:if>
              </td>
            </tr>
        </table>
        <br><br>
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col" class="nome-lista">Sacado</th>
                    <th scope="col" class="nome-lista">Taxa</th>
                    <th scope="col" align="center">Vencimento</th>
                    <th scope="col" align="right">Valor</th>
                    <th scope="col" align="center">Situacao</th>
                    <th scope="col" align="center">Dt. Geração</th>
                    <th scope="col" align="center">Dt. Pagamento</th>
                    <th scope="col" align="center">Dt. Baixa</th>
                    <th scope="col" align="center">Dt. Cancelamento</th>
                    <th scope="col" align="right">Encargos</th>
                    <th scope="col" align="right">Vr. Pago</th>
                    <th scope="col" align="center">Excluir</th>
                    <th scope="col" align="center">Imprimir</th>
                </tr>
            </thead>
            <tbody>
                
                <c:forEach var="boleto" items="${lista}">
                    <tr>
                        <td class="column1">${boleto.sacado}</td>
                        <td class="column1">${boleto.taxa.descricao}</td>
                        <td class="column1" align="center"><fmt:formatDate value="${boleto.dtVencimento}" pattern="dd/MM/yyyy" /></td>
                        <th class="column1" align="right"><fmt:formatNumber value="${boleto.valor}" pattern="#,##0.00"/></th>
                        <c:choose>
                            <c:when test='${boleto.situacao == "NP"}'><td class="column1" align="center">Não Pago</td></c:when>
                            <c:when test='${boleto.situacao == "PG"}'><td class="column1" align="center">Pago</td></c:when>
                            <c:when test='${boleto.situacao == "CA"}'><td class="column1" align="center">Cancelado</td></c:when>
                        </c:choose>                        
                        <td class="column1" align="center"><fmt:formatDate value="${boleto.dtGeracao}" pattern="dd/MM/yyyy" /></td>
                        <td class="column1" align="center"><fmt:formatDate value="${boleto.dtPagamento}" pattern="dd/MM/yyyy" /></td>
                        <td class="column1" align="center"><fmt:formatDate value="${boleto.dtBaixa}" pattern="dd/MM/yyyy" /></td>
                        <td class="column1" align="center"><fmt:formatDate value="${boleto.dtCancelamento}" pattern="dd/MM/yyyy" /></td>
                        <th class="column1" align="right"><fmt:formatNumber value="${boleto.encargos}" pattern="#,##0.00"/></th>
                        <th class="column1" align="right"><fmt:formatNumber value="${boleto.vrPago}" pattern="#,##0.00"/></th>
                            
                        
                        <!-- EXCLUIR -->
                        <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("2433")%>'>
                            <c:if test='${boleto.situacao == "NP"}'>
                                <a href="javascript: if(confirm('Confirma a exclusão do objeto selecionado?')) window.location.href='c?app=2433&id=${boleto.id}'">
                                    <img src="imagens/icones/inclusao-titular-05.png"/>
                                </a>
                            </c:if>
                        </c:if>
                        </td>
                        
                        <!-- IMPRIMIR -->
                        <td class="column1" align="center">
                            <c:if test='${boleto.situacao == "NP"}'>
                                <a href="c?app=2430&acao=imprimir&id=${boleto.id}"><img src="imagens/icones/inclusao-titular-13.png"/></a>
                            </c:if>
                        </td>
                        
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
    </form>
    
    
</body>
</html>
