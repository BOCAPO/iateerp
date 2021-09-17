<%@include file="head.jsp"%>


<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style> 

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');
            $('#mesAno').mask('99/9999');
            $('#data').mask('99/99/9999');

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

        if(document.forms[0].data.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].valor.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }

        document.forms[0].submit();
    }
    
    function validarForm2(){
        window.location.href='c?app=1300&acao=imprimir&mesAno='+$('#mesAno').val();
    }    
    function validarForm3(){
        window.location.href='c?app=1300&acao=consultar&mesAno='+$('#mesAno').val();
    }    

</script>  

<body class="internas">
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Cadastro de Comissão de Permanência</div>
    <div class="divisoria"></div>
    <br>
                
    <form action="c">
        <input type="hidden" name="app" value="1301"/>

        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Data</p>
                  <input type="text" id="data" name="data" class="campoSemTamanho alturaPadrao larguraData" maxlength="4" />
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Valor</p>
                  <input type="text" name="valor" id="valor" class="campoSemTamanho alturaPadrao larguraNumero"/>
              </td>
              <td>
                <c:if test='<%=request.isUserInRole("1301")%>'>
                    <input type="button" class="botaoIncluir"  style="margin-top:15px" onclick="validarForm()" value=" " />
                </c:if>
              </td>
              <td> 
                <input type="button" class="botaoImprimirPequeno"  style="margin-top:15px" onclick="validarForm2()" value=" " />
              </td>
              <td> 
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              </td>
              <td> 
                <fieldset class="field-set legendaFrame recuoPadrao "  style="width:170px;height:48px;margin-top: -5px;">
                    <legend >Pesquisa</legend>
                    <label class="labelDataCP" style="margin-top: 5px">&nbsp;&nbsp;&nbsp;&nbsp;Mes/Ano&nbsp;&nbsp;&nbsp;&nbsp;</label>
                    <input class="campoDataCP" type="text" id="mesAno"  name="mesAno" value="${mesAno}"/>
                    <input class="botaobuscainclusao" type="button"  onclick="validarForm3()" style="margin-top: 2px;" value="" title="Consultar" />
                </fieldset>
              </td>
            </tr>
        </table>        
    </form>

    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
            <tr>
                <th>Data</th>
                <th>Valor</th>
                <th>Excluir</th>
            </tr>	
        </thead>
        <tbody>
            <c:forEach var="cp" items="${lista}">
                <tr>
                    <fmt:formatDate var="data" value="${cp.data }" pattern="dd/MM/yyyy"/>
                    <td align="center">${data}</td>
                    <td align="right"><fmt:formatNumber value="${cp.valor}" pattern="#0.00000000"/></td>
                    <td align="center">    
                        <c:if test='<%=request.isUserInRole("1972")%>'>
                            <a href="javascript: if(confirm('Confirma Exclusão da Comissão de Permanência de ${data}?')) window.location.href='c?app=1302&data=${data}'">
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
