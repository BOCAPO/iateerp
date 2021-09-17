
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
            }, function() {
                    $(this).css('background','white');
            })
            
            $("#dtRef").mask("99/99/9999");
            //$("#dtFim").mask("99/99/9999");
            
    });    
    
    function validarForm(){
        document.forms[0].submit();
    }
    
    function validarForm2(){
        window.location.href='c?app=1431'+
                             '&idTipoVaga='+document.forms[0].idTipoVaga.value;
    }

</script>  

<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Taxas Náuticas</div>
    <div class="divisoria"></div>
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="1430">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0">Tipos de Vaga</p>
                  <div class="selectheightnovo2">
                    <select name="idTipoVaga" class="campoSemTamanho alturaPadrao larguraComboCategoria" onchange="javascript: document.forms[0].submit()">
                        <c:forEach var="tipoVaga" items="${tiposVagas}">
                            <option value="${tipoVaga.cod}" <c:if test="${idTipoVaga eq tipoVaga.cod}">selected</c:if>>${tipoVaga.descricao}</option>
                        </c:forEach>
                    </select>
                  </div>        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Pesquisa</p>
                  <div class="selectheightnovo2">
                    <select name="tipo" id="tipo" class="campoSemTamanho" style="width:150px">
                        <option value="AT" <c:if test='${tipo=="AT"}'>selected</c:if>>Ativas em</option>
                        <option value="TD" <c:if test='${tipo=="TD"}'>selected</c:if>>Mostrar Todas as Taxas</option>
                        <option value="AF" <c:if test='${tipo=="AF"}'>selected</c:if>>Ativas e Futuras em</option>
                    </select>
                  </div>        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dt. Referência</p>
                  <input type="text" id="dtRef" name="dtRef" class="campoSemTamanho alturaPadrao " style="width:100px" value="${dtRef}">
              </td>
              <td >    
                  <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Consultar" />
              </td>
              <td> &nbsp;&nbsp;&nbsp;
                <c:if test='<%=request.isUserInRole("1431")%>'>
                     <a href="#" onclick="validarForm2()"><img src="imagens/btn-incluir.png" style="margin-top:25px" width="100" height="25" /></a>
                </c:if>
              </td>
              
            </tr>
        </table>
    </form>
    
    <br><br><br>
                
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
            <tr class="odd">
                <th scope="col">Pés Inicial</th>
                <th scope="col">Pés Final</th>
                <th scope="col">Início</th>
                <th scope="col">Valor</th>
                <th scope="col">Fim</th>
                <th scope="col">Alterar</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="barco" items="${barcos}">
                <tr>
                    <td class="column1" align="left">${barco.pesInicial}</td>
                    <td class="column1" align="left">${barco.pesFinal}</td>

                    <fmt:formatDate var="dtInicio" value="${barco.dtInicial}" pattern="dd/MM/yyyy HH:mm:ss" />
                    <td class="column1" align="center">${dtInicio}</td>

                    <fmt:formatNumber var="valor"  value="${barco.valor}" pattern="#0.00"/>
                    <td class="column1" align="center">${valor}</td>

                    <fmt:formatDate var="dtFim" value="${barco.dtFinal}" pattern="dd/MM/yyyy HH:mm:ss" />
                    <td class="column1" align="center">${dtFim}</td>

                    <c:if test='<%=request.isUserInRole("1432")%>'>
                        <th class="column1" align="center">
                            <a href="c?app=1432&acao=showForm&idTipoVaga=${idTipoVaga}&pesInicial=${barco.pesInicial}&pesFinal=${barco.pesFinal}&dtInicio=${dtInicio}&dtFim=${dtFim}&valor=${valor}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </th>
                    </c:if>
                </tr>
            </c:forEach>
        </tbody>
    </table>

        

    
    
</body>
</html>
