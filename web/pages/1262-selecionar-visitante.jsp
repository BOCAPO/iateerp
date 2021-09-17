<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>
    
<body class="internas">
    
    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript">
           $(document).ready(function () {
                   $('#tabela tr:gt(0)').css('background', 'white');

                   $('#tabela tr:gt(0)').hover(function() {
                           $(this).css('background','#f4f9fe');
                   }, function() {
                           $(this).css('background','white');
                   })
           });
           
           function validaForm(){
                if ($('#nome').val()=='' && $('#documento').val()==''){
                    alert('Informe pelo menos 1 filtro para pesquisa!');
                    return;
                }
                
                
                document.forms[0].submit();    
           }
    </script>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Entrega de Crachá - Seleção de Visitante</div>
    <div class="divisoria"></div>
    
    <form action="c" method="POST" onsubmit="return validarForm()">
        <input type="hidden" name="app" value="1265">
        <input type="hidden" name="acao" value="consultarVisitante">
        <input type="hidden" name="idCracha" value="${idCracha}">

        <table class="fmt">
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nome</p>
                  <input type="text" name="nome" id="nome" class="campoSemTamanho alturaPadrao" style="width:300px;"
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Documento</p>
                  <input type="text" name="documento" id="documento" class="campoSemTamanho alturaPadrao larguraData">
              </td>
              <td>    
                 <input type="button" class="botaobuscainclusao" style="margin-top:20px" onclick="validaForm();" value="" title="Consultar" />                                    
              </td>
              
            </tr>
        </table>
    </form>
        
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Nome</th>
                <th scope="col">Documento</th>
                <th scope="col">Placa</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="visitante" items="${visitantes}">
            <tr>
                <td class="column1"><a href="c?app=1265&acao=selecionarVisitante&idCracha=${idCracha}&idVisitante=${visitante.id}">${visitante.nome}</a></td>
                <td class="column1">${visitante.documento}</td>
                <td class="column1">${visitante.placa}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</body>
</html>
