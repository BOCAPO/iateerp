
<%@include file="head.jsp"%> 

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            
            //$("#dtInicio").mask("99/99/9999 99:99:99");
            $("#dtFim").mask("99/99/9999 99:99:99");
            
    });    
    
    function validarForm(){
        if (!isDataHoraValida($('#dtFim').val())) {
            alert('Data de Fim Inválida!');
            return false;
        }
        if ($('#dtFim').val()!=''){
            if (!isDataHoraValida($('#dtFim').val())) {
                alert('Data de Fim Inválida!');
                return false;
            }
        }
        
        var msg='';
        
        $.ajax({url:'TaxaNauticaAjax', async:false, dataType:'text', type:'GET',data:{
                            tipo:2,
                            dtInicio:$('#dtInicio').val(),
                            dtFim:$('#dtFim').val(),
                            idTipoVaga:$('#idTipoVaga').val()}
        }).success(function(retorno){
            if (retorno!=''){
                msg=retorno;
            } 
        });
        
        if (msg!=''){
            alert(msg);
            return false
        }
    
        $('#dtInicio').removeAttr('disabled');
        document.forms[0].submit();
    }


</script>  

<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Taxa Nautica - Alteração</div>
    <div class="divisoria"></div>
    
    <form action="c" method="POST">
        <input type="hidden" name="app" id="app" value="2482">
        <input type="hidden" name="acao" value="gravar">    
        <input type="hidden" name="idTipoVaga" id="idTipoVaga" value="${tipoVaga.id}">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Tipo de Vaga</p>
                  <input type="text" name="deTipoVaga" class="campoSemTamanho alturaPadrao " disabled style="width:250px" value="${tipoVaga.descricao}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Início</p>
                  <input type="text" id="dtInicio" name="dtInicio" class="campoSemTamanho alturaPadrao " disabled style="width:130px" value="${dtInicio}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >% por m2</p>
                  <input type="text" id="valor" name="valor" class="campoSemTamanho alturaPadrao " disabled style="width:70px" value="${valor}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Fim</p>
                  <input type="text" id="dtFim" name="dtFim" class="campoSemTamanho alturaPadrao " style="width:130px" value="${dtFim}">
              </td>
            </tr>
        </table>
    </form>
    
    <br><br><br><br>
    
    <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2480';" value=" " />
    
</body>
</html>
