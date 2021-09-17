
<%@include file="head.jsp"%> 

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            
            $("#dtInicio").mask("99/99/9999 99:99:99");
            $("#dtFim").mask("99/99/9999 99:99:99");
            
    });    
    
    function inclui(){
        if (!isDataHoraValida($('#dtInicio').val())) {
            alert('Data de Início Inválida!');
            return false;
        }
        if ($('#dtFim').val()!=''){
            if (!isDataHoraValida($('#dtFim').val())) {
                alert('Data de Fim Inválida!');
                return false;
            }
        }
        if ($('#valor').val()==''){
            alert('Informe o valor!');
            return false;
        }
        
        var temErro = '';
        var temAlerta = '';
        var msg='';

        $.ajax({url:'TaxaNauticaAjax', async:false, dataType:'text', type:'GET',data:{
                            tipo:1,
                            dtInicio:$('#dtInicio').val(),
                            dtFim:$('#dtFim').val(),
                            idTaxa:$(this).val(),
                            idCategoria:$('#idCategoria').val()}
        }).success(function(retorno){
            if (retorno!=''){
                if(retorno.substring(0,1)=='E' ){
                    temErro = retorno.substring(2);
                }else{
                    temAlerta = retorno.substring(2);
                }
            }
        });

        if (temErro!=''){
            alert(temErro);
            return;
        }else if (temAlerta!=''){
            if (!confirm(temAlerta)){
                return;
            }
        }
        
        $('[name=idTipoVaga]').each(function(){
            if ($(this).attr('checked')){
                //alert($(this).val());s
                $('#cobrar').append('<tr><td>'+$(this).parent().html().replace('idTipoVaga', 'idTipoVaga')+'</td></tr>');
                $(this).parent().parent().remove();
            }
        });  
        
        $('#dtInicio').attr('disabled', 'disabled');
        $('#valor').attr('disabled', 'disabled');
        $('#dtFim').attr('disabled', 'disabled');
        
    }
    
    function exclui(){
    
        var qt=0;
        $('[name=idTipoVagaCobrar]').each(function(){
            if ($(this).attr('checked')){
                qt=1;
                return
            }
        });  
        if (qt==0){
            alert('Nenhum tipo de vaga foi selecionado!');
            return false;
        }
    
        $('[name=idTipoVagaCobrar]').each(function(){
            if ($(this).attr('checked')){
                //alert($(this).val());
                $('#naoCobrar').append('<tr><td>'+$(this).parent().html().replace('idTipoVagaCobrar', 'idTipoVaga')+'</td></tr>');
                $(this).parent().parent().remove();
            }
        });  
        
        if($('#cobrar tr').length==0){
            $('#dtInicio').removeAttr('disabled');
            $('#valor').removeAttr('disabled');
            $('#dtFim').removeAttr('disabled');
        }
    }

    function validarForm(){
        
        document.forms[0].submit();
    }


</script>  

<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Taxa Náutica - Inclusão</div>
    <div class="divisoria"></div>
    
    <form action="c" method="POST">
        <input type="hidden" name="app" id="app" value="2481">
        <input type="hidden" name="acao" value="gravar">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0">Tipos de Vaga</p>
                  <div class="selectheightnovo2">
                    <select name="idTipoVaga" id="idTipoVaga" class="campoSemTamanho alturaPadrao larguraComboCategoria" >
                        <c:forEach var="tipoVaga" items="${tiposVagas}">
                            <option value="${tipoVaga.id}" <c:if test="${idTipoVaga eq tipoVaga.id}">selected</c:if>>${tipoVaga.descricao}</option>
                        </c:forEach>
                    </select>
                  </div>        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Início</p>
                  <input type="text" id="dtInicio" name="dtInicio" class="campoSemTamanho alturaPadrao " style="width:130px" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >% por m2</p>
                  <input type="text" id="valor" name="valor" class="campoSemTamanho alturaPadrao " style="width:70px" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Fim</p>
                  <input type="text" id="dtFim" name="dtFim" class="campoSemTamanho alturaPadrao " style="width:130px" value="">
              </td>
            </tr>
        </table>

    </form>
    
    <br><br><br><br>
    
    <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2480';" value=" " />
    
</body>
</html>
