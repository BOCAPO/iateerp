
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
            
            $("#dtInicio").mask("99/99/9999 99:99:99");
            $("#dtFim").mask("99/99/9999 99:99:99");
            
    });    
    
    function validarForm(){
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
        var tp = 1;
        if ($('#app').val()=='3027'){
            tp=2;
        }
        
        var msg='';
        $.ajax({url:'JurosCursoAjax', async:false, dataType:'text', type:'GET',data:{
                            tipo:tp,
                            dtInicio:$('#dtInicio').val(),
                            dtFim:$('#dtFim').val(),
                            idCurso:$('#idCurso').val()}
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
    <c:if test='${app == "3026"}'>
        <div id="titulo-subnav">Juros para Taxa de Curso - Inclusão</div>
    </c:if>
    <c:if test='${app == "3027"}'>
        <div id="titulo-subnav">Juros para Taxa de Curso - Alteração</div>
    </c:if>
    <div class="divisoria"></div>
    
    <form action="c" method="POST">
        <input type="hidden" name="app"  id="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="idCurso" id="idCurso" value="${curso.id}">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Curso</p>
                  <input type="text" name="Curso" class="campoSemTamanho alturaPadrao " disabled style="width:250px" value="${curso.descricao}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Início</p>
                  <input type="text" id="dtInicio" name="dtInicio" class="campoSemTamanho alturaPadrao " <c:if test='${app=="3027"}'>disabled</c:if> style="width:130px" value="${dtInicio}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Valor</p>
                  <input type="text" id="valor" name="valor" class="campoSemTamanho alturaPadrao " <c:if test='${app=="3027"}'>disabled</c:if> style="width:70px" value="${valor}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Fim</p>
                  <input type="text" id="dtFim" name="dtFim" class="campoSemTamanho alturaPadrao " style="width:130px" value="${dtFim}">
              </td>
            </tr>
        </table>
    </form>
    
    <br><br><br>
    
    <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3025&idCurso=${curso.id}';" value=" " />
    
</body>
</html>
