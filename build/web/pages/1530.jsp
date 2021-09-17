
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
            
            $("#dtInicio").mask("99/99/9999");
            $("#dtFim").mask("99/99/9999");
            
            if($('#origem').val()!='CH'){
                $('#trazCanceladas').attr('checked', true);
                $('#trazCanceladas').hide();
                $('#lblTrazCanceladas').hide();
            } 
            
    });    

    function validarForm2(){
        var trazCanceladas=0;
        if(document.forms[0].trazCanceladas.checked){
            trazCanceladas=1;
        }

        window.location.href='c?app=1530&acao=imprimir'+
                             '&nome='+document.forms[0].nome.value+
                             '&dep='+document.forms[0].dep.value+
                             '&dtInicio='+document.forms[0].dtInicio.value+
                             '&dtFim='+document.forms[0].dtFim.value+
                             '&origem='+document.forms[0].origem.value+
                             '&trazCanceladas='+trazCanceladas;

    }

</script>  

<script type="text/javascript" language="JavaScript">
    function validarForm(){
        if(
            trim(document.forms[0].nome.value) == '' && 
            trim(document.forms[0].dtInicio.value) == '' && 
            trim(document.forms[0].dtFim.value) == '' && 
            trim(document.forms[0].dep.value) == 0
           )     
            {
            alert('É preciso informar ao menos 1 parâmetro de consulta');
            return false;
        } else {
            document.forms[0].submit();
        }
    }
    

    
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Consulta Reserva de Dependências</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="1530">
        <input type="hidden" name="acao" value="consultar">
        <input type="hidden" name="origem" id="origem" value="${origem}">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nome</p>
                  <input type="text" name="nome" class="campoSemTamanho alturaPadrao " style="width:200px" value="${nome}">
              </td>
              <td>
                    <p class="legendaCodigo MargemSuperior0">Dependencia:</p>
                    <div class="selectheightnovo">
                        <select name="dep" class="campoSemTamanho alturaPadrao" style="width:180px;" >
                            <c:forEach var="dependencia" items="${dependencias}">
                                <option value="${dependencia.id}" <c:if test="${dep eq dependencia.id}">selected</c:if>>${dependencia.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dt. Início</p>
                  <input type="text" name="dtInicio" id="dtInicio" class="campoSemTamanho alturaPadrao" size="10" value="${dtInicio}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dt. Fim</p>
                  <input type="text" name="dtFim" id="dtFim" class="campoSemTamanho alturaPadrao" size="10" value="${dtFim}">
              </td>
              <td>
                  <input type="checkbox" class="legendaCodigo MargemSuperior0" style="margin-top:20px" name="trazCanceladas" id="trazCanceladas" value="1" <c:if test='${trazCanceladas==1}'>checked</c:if>><spam id="lblTrazCanceladas" class="legendaSemMargem larguraData">Mostra Reservas Canceladas</spam>                  
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
                <th scope="col">Período</th>
                <th scope="col">Dependência</th>
                <c:if test="${origem eq 'CH'}">
                  <th scope="col">Qt. Convites</th>
                  <th scope="col">Usuário Res.</th>
                  <th scope="col">Usuário Canc.</th>
                </c:if>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="reserva" items="${reservas}">
                <tr>
                    <td class="column1">${reserva.interessado}</td>

                    <fmt:formatDate var="dtInicio" value="${reserva.dtInicio}" pattern="dd/MM/yyyy HH:mm:ss" />
                    <fmt:formatDate var="dtFim" value="${reserva.dtFim}" pattern="dd/MM/yyyy HH:mm:ss" />
                    <td class="column1" align="center">${dtInicio} a ${dtFim}</td>
                    <td class="column1" align="center">${reserva.deChurrasqueira}</td>
                    <c:if test="${origem eq 'CH'}">
                        <td class="column1" align="center">${reserva.qtConvites}</td>
                        <td class="column1" align="center">${reserva.usuario}</td>
                        <td class="column1" align="center">${reserva.usuarioCanc}</td>
                    </c:if>

                </tr>
            </c:forEach>
        </tbody>
    </table>

        

    
    
</body>
</html>
