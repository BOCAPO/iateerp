<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style> 

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#dtVenc').mask('99/99/9999');
            $('#dtPag').mask('99/99/9999');
    });    


    function validarForm(){

        if(document.forms[0].dtVenc.value == ''){
            alert('A Data de Vencimento é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].dtPag.value == ''){
            alert('A Data de Pagamento é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].valor.value == ''){
            alert('O Valor é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].percMulta.value == ''){
            alert('O Percentual de Multa é de preenchimento obrigatório!');
            return;
        }

        $.ajax({url:'EncargosAjax', async:false, dataType:'text', type:'GET',
                            data:{
                            tipo:1,
                            dtVenc:$('#dtVenc').val(),
                            dtPag:$('#dtPag').val(),
                            valor:$('#valor').val(),
                            percMulta:$('#percMulta').val()
                            }
        }).success(function(retorno){
            ret = retorno;
        });

        $('#encargo').val(ret);
        
        //alert(ret);
    }

</script>  

<body class="internas">
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Cálculo de Encargos</div>
    <div class="divisoria"></div>
    <br>
                
    <form action="c">

        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dt. Vencimento</p>
                  <input type="text" id="dtVenc" name="dtVenc" class="campoSemTamanho alturaPadrao larguraData" />
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dt. Pagamento</p>
                  <input type="text" name="dtPag" id="dtPag" class="campoSemTamanho alturaPadrao larguraData"/>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Valor</p>
                  <input type="text" name="valor" id="valor" class="campoSemTamanho alturaPadrao larguraNumero"/>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Perc. Multa</p>
                  <input type="text" name="percMulta" id="percMulta" class="campoSemTamanho alturaPadrao larguraNumero"/>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Encargos</p>
                  <input type="text" name="encargo" id="encargo" disabled class="campoSemTamanho alturaPadrao larguraNumero"/>
              </td>
              <td> 
                    <input class="botaobuscainclusao" type="button"  onclick="validarForm()" style="margin-top: 20px;" value="" title="Consultar" />
              </td>
            </tr>
        </table>        
    </form>
</body>
</html>
