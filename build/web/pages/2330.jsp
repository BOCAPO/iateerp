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


    function validarForm(){
    
        if(document.forms[0].ano.value == ''){
            alert('O campo ano é de preenchimento obrigatório!');
            return;
        }
        
        if(document.forms[0].quant.value == ''){
            alert('O campo quantidade é de preenchimento obrigatório!');
            return;
        }

        document.forms[0].submit();
    }
    
</script>  

<body class="internas">
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Dias Sem Multa Internet</div>
    <div class="divisoria"></div>
    <br>
                
<c:if test='<%=request.isUserInRole("2331")%>'>
    <form action="c">
        <input type="hidden" name="app" value="2331"/>

        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Mês</p>
                  <div class="selectheightnovo">
                    <select name="mes" class="campoSemTamanho alturaPadrao" style="width:180px;">
                      <option value="1">Janeiro</option>
                      <option value="2">Fevereiro</option>
                      <option value="3">Março</option>
                      <option value="4">Abril</option>
                      <option value="5">Maio</option>
                      <option value="6">Junho</option>
                      <option value="7">Julho</option>
                      <option value="8">Agosto</option>
                      <option value="9">Setembro</option>
                      <option value="10">Outubro</option>
                      <option value="11">Novembro</option>
                      <option value="12">Dezembro</option>
                    </select>
                  </div>        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Ano</p>
                  <input type="text" name="ano" class="campoSemTamanho alturaPadrao larguraNumero" maxlength="4" />
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Quant.</p>
                  <input type="text" name="quant" class="campoSemTamanho alturaPadrao larguraNumero"/>
              </td>
              <td> 
                <input type="button" class="botaoIncluir"  style="margin-top:15px" onclick="validarForm()" value=" " />
              </td>
            </tr>
        </table>        
    </form>
</c:if>

    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
            <tr>
                <th>Ano</th>
                <th>Mês</th>
                <th>Quant.</th>
                <th>Excluir</th>
            </tr>	
        </thead>
        <tbody>
            <c:forEach var="dias" items="${lista}">
                <tr>
                    <td align="center">${dias.ano}</td>
                    <td align="center">${dias.mesTexto}</td>
                    <td align="center"><fmt:formatNumber value="${dias.qtDias}" /></td>
                    <td align="center">    
                        <c:if test='<%=request.isUserInRole("2333")%>'>
                            <a href="javascript: if(confirm('Confirma Exclusão do registro selecionado?')) window.location.href='c?app=2333&id=${dias.id}'">
                                <img src="imagens/icones/inclusao-titular-05.png" />
                            </a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
