
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

        var qt=0;
        $('[name=idCurso]').each(function(){
            if ($(this).attr('checked')){
                qt=1;
                return
            }
        });  
        if (qt==0){
            alert('Nenhum curso foi selecionada!');
            return false;
        }
        
        var temErro = '';
        var temAlerta = '';
        var msg='';
        
        $('[name=idCurso]').each(function(){
            if ($(this).attr('checked')){
                $.ajax({url:'TaxaCursoAjax', async:false, dataType:'text', type:'GET',data:{
                                    tipo:1,
                                    dtInicio:$('#dtInicio').val(),
                                    dtFim:$('#dtFim').val(),
                                    idCurso:$(this).val(),
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
        
        $('[name=idCurso]').each(function(){
            if ($(this).attr('checked')){
                //alert($(this).val());s
                $('#cobrar').append('<tr><td>'+$(this).parent().html().replace('idCurso', 'idCursoCobrar')+'</td></tr>');
                $(this).parent().parent().remove();
            }
        });  
        
        $('#dtInicio').attr('disabled', 'disabled');
        $('#valor').attr('disabled', 'disabled');
        $('#dtFim').attr('disabled', 'disabled');
        
    }
    
    function exclui(){
    
        var qt=0;
        $('[name=idCursoCobrar]').each(function(){
            if ($(this).attr('checked')){
                qt=1;
                return
            }
        });  
        if (qt==0){
            alert('Nenhum curso foi selecionada!');
            return false;
        }
    
        $('[name=idCursoCobrar]').each(function(){
            if ($(this).attr('checked')){
                //alert($(this).val());
                $('#naoCobrar').append('<tr><td>'+$(this).parent().html().replace('idCursoCobrar', 'idCurso')+'</td></tr>');
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
        $('[name=idCursoCobrar]').each(function(){
            $(this).attr('checked', 'checked');
        });  
        
        $('#dtInicio').removeAttr('disabled');
        $('#valor').removeAttr('disabled');
        $('#dtFim').removeAttr('disabled');
        document.forms[0].submit();
    }


</script>  

<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Taxa de Curso - Inclusão</div>
    <div class="divisoria"></div>
    
    <form action="c" method="POST">
        <input type="hidden" name="app" id="app" value="3121">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="idCategoria" id="idCategoria" value="${idCategoria}">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Início</p>
                  <input type="text" id="dtInicio" name="dtInicio" class="campoSemTamanho alturaPadrao " style="width:130px" value="${dtInicio}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Valor</p>
                  <input type="text" id="valor" name="valor" class="campoSemTamanho alturaPadrao " style="width:70px" value="${valor}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Fim</p>
                  <input type="text" id="dtFim" name="dtFim" class="campoSemTamanho alturaPadrao " style="width:130px" value="${dtFim}">
              </td>
            </tr>
        </table>
        <br><br><br>
        <table class="fmt" align="left" >
            <tr>
              <td>
                <p class="legendaCodigo">Não cobrar</p>
                <div class="recuoPadrao" style="overflow:auto;height:200px;width:200px;">
                    <table class="fmt" id="naoCobrar">
                    <c:forEach var="curso" items="${cursos}">
                        <tr>
                            <td>
                                <input type="checkbox" name="idCurso" value="${curso.id}" >
                                ${curso.descricao}<br>
                            </td>
                        </tr>
                    </c:forEach>
                    </table>
                </div>
              </td>
              <td>
                  &nbsp;&nbsp;&nbsp;<a href="#" onclick="inclui()"><img src="imagens/btn-incluir-um.png" style="margin-top:25px" width="100" height="25" /></a>
                  <br>
                  &nbsp;&nbsp;&nbsp;<a href="#" onclick="exclui()"><img src="imagens/btn-excluir-um.png" style="margin-top:25px" width="100" height="25" /></a>
              </td>
              <td>
                <p class="legendaCodigo">Cobrar</p>
                <div class="recuoPadrao" style="overflow:auto;height:200px;width:200px;">
                    <table class="fmt" id="cobrar">
                    </table>
                </div>
              </td>
            </tr>
        </table>
        <br><br><br><br><br><br><br><br><br><br>

    </form>
    
    <br><br><br>
    
    <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3120';" value=" " />
    
</body>
</html>
