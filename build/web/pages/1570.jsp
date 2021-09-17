<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
</style>  
<style type="text/css">
    table.fmt2 {border:1;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt2 tr td {border:1;background:none;padding:0px;margin:0em auto;}
    
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
        $('#tabela tr:gt(0)').css('background', 'white');
        $('#tabelaSocio tr:gt(0)').css('background', 'white');

        $('#tabela tr:gt(0)').hover(function() {
                $(this).css('background','#f4f9fe');
        }, function() {
                $(this).css('background','white');
        })
        $('#tabelaSocio tr:gt(0)').hover(function() {
                $(this).css('background','#f4f9fe');
        }, function() {
                $(this).css('background','white');
        })
        
        //$("#tabelaSocio tr:eq(1)").css('display', 'none');
        
        $("#excluir").live("click", excluirLinha);
        $("#pesqSocio").hide();
        
        if($('#app').val()==1572){
            var campo=$('#lugarGato').val();
            var tipo = $("#tipo"+campo).val();
            if (tipo.substring(0,1)=="S"){
                $("#nome"+campo).prop('readonly',true);
                $("#pesquisaSocio"+campo).show();

            }else{
                $("#nome"+campo).prop('readonly', false);
                $("#pesquisaSocio"+campo).hide();
            }
            
            
        }
        
    });  

    function excluirLinha() {
        if($('#app').val()==1572){
            if (confirm('Confirma a exclusão da reserva selecionada?')){
                $('#app').val('1573');
                $('#acao').val('lugarSel');
                $('#lugarSel').val($('#lugarGato').val()+';');
                document.forms[0].submit();
            }
        }else{
            $(this).parent().parent().remove();
        }

    }
    function pesquisaSocio(campo) {
        $("#pesqSocio").show();
        var tabela = $('#tabelaSocio').find('tbody').empty();
        $("#nomeSocio").val('');
        $("#matriculaSocio").val('');
        $("#categoriaSocio").val('');
        $("#lugarSel").val(campo);
    }
    function cancelaPesquisa() {
        $("#pesqSocio").hide();
    }
    function trocaTipo(campo) {
        var tipo = $("#tipo"+campo).val();
        $("#nome"+campo).val('');
        $("#titulo"+campo).val('');
        if (tipo.substring(0,1)=="S"){
            $("#nome"+campo).prop('readonly',true);
            $("#pesquisaSocio"+campo).show();

        }else{
            $("#nome"+campo).prop('readonly', false);
            $("#pesquisaSocio"+campo).hide();
        }

    }

    function trocaDadoReserva() {
        var lugar;
        $("#tabela tr:gt(2)").each(function(index){
            lugar = $(this).find('#lugar').val();
            if (lugar!=undefined){
                if ($('#tipoDadoReserva').val()=="I"){
                    $("#nome"+lugar).show();
                    $("#valor"+lugar).show();
                    $("#tipo"+lugar).show();
                    $("#telefone"+lugar).show();
                    $("#situacao"+lugar).show();
                    $("#excluir"+lugar).show();
                    $("#pesquisaSocio"+lugar).show();
                }else{
                    $("#nome"+lugar).hide();
                    $("#valor"+lugar).hide();
                    $("#tipo"+lugar).hide();
                    $("#telefone"+lugar).hide();
                    $("#situacao"+lugar).hide();
                    $("#excluir"+lugar).hide();
                    $("#pesquisaSocio"+lugar).hide();
                }
            }
            
        });
    }
    
    function validarForm() {
        var lugar;
        var mensagem="";
        var tipoPesq="";
        var qt=0;
        
        $("#tabela tr:gt(0)").each(function(index){
            qt++;
        });        
        if (qt==0){
            alert('Nenhuma reserva para ser incluída/alterada!');
            return;
        }
        
        if ($('#tipoDadoReserva').val()=="I"){
            tipoPesq="gt(0)";
            
        }else{
            tipoPesq="eq(1)";
        }   
        
        $("#tabela tr:"+tipoPesq).each(function(index){
            lugar = $(this).find('#lugar').val();
            if (lugar!=undefined){
                if ($("#tipo"+lugar).val().substring(0,1)=="S"){
                    if ($("#titulo"+lugar).val()==""){
                        mensagem ='Informe o sócio para o lugar '+lugar.substring(0,3)+' - '+String.fromCharCode(64+parseInt(lugar.substring(3,5)));
                        return;
                    }
                }else{
                    if ($("#nome"+lugar).val()==""){
                        mensagem ='Informe o nome para o lugar '+lugar.substring(0,3)+' - '+String.fromCharCode(64+parseInt(lugar.substring(3,5)));
                        return;
                    }
                }
            }
        });
        
        if (mensagem!=""){
            alert(mensagem);
            return
        }
        
        $('#lugares').val('');
        $("#tabela tr:gt(0)").each(function(index){
            lugar = $(this).find('#lugar').val();
            if (lugar!=undefined){
                if ($('#lugares').val()==""){
                    $('#lugares').val(lugar);
                }else{
                    $('#lugares').val($('#lugares').val()+','+lugar);
                }
            }
        });

        document.forms[0].submit();
    }
    
    function selecionaSocio(indice){
        $("#pesqSocio").hide();
        $("#tabelaSocio tr:eq("+(parseInt(indice)+1)+")").each(function(index){
            $('#nome'+$('#lugarSel').val()).val($(this).find('#tabNome').html());
            $('#titulo'+$('#lugarSel').val()).val($(this).find('#campoTitulo').val());
        });
        
        $('#nome'+$('#lugarSel').val()).val(nome);
    }
    
    function carregaSocio(lugar){
        //Verificando se o socio já tem reserva para o dia
        $.ajax({url:'ReservaLugarEventoAjax', type:'GET',
                            data:{
                            nome:$('#nomeSocio').val(),
                            matricula:$('#matriculaSocio').val(),
                            categoria:$('#categoriaSocio').val()
                            }
        }).success(function(retorno){
            if (retorno.erro !== undefined){
                alert(retorno.erro);
            }else {
                var tabela = $('#tabelaSocio').find('tbody').empty();
                var linha="";
                var campoHidden="";
                $.each(retorno.jogador, function(i) {
                    campoHidden='<input type="hidden" id="campoTitulo"  value="'+this.titulo+'"/>'
                    linha='<tr><td class="column1"><a href="#" id="tabNome"  onclick="selecionaSocio('+i+')">'+this.nome+'</a></td><td id="tabCategoria">'+this.descricao+' '+campoHidden+'</td></tr>';
                    tabela.append(linha)
                });
            }
        });
        
        
    }
</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app==1571}'>
        <div id="titulo-subnav">Inclusão de Reserva de Lugar</div>
    </c:if>
    <c:if test='${app==1572}'>
        <div id="titulo-subnav">Alteração de Reserva de Lugar</div>
    </c:if>
    <div class="divisoria"></div>
    
    <form action="c" id="it-bloco01">
        <input type="hidden" name="app" id="app"  value="${app}"/>
        <input type="hidden" name="acao" id="acao" value="gravar"/>
        <input type="hidden" name="idEvento" id="idEvento" value="${idEvento}"/>
        <input type="hidden" name="lugares" id="lugares" value=""/>
        <input type="hidden" name="lugarSel" id="lugarSel" value=""/>
        
        <c:if test='${app==1571}'>
            <p class="legendaCodigo MargemSuperior0">Dados da Reserva:</p>
            <div class="selectheightnovo2">
                <select name="tipoDadoReserva" id="tipoDadoReserva" onchange="trocaDadoReserva()" class="campoSemTamanho alturaPadrao" style="width:245px" >
                    <option value="I">Cada reserva terá seus dados</option>
                    <option value="U">Todas as reservas terão os mesmos dados</option>
                </select>
            </div>
        </c:if>

        <table id="tabela" style="width:1000px;margin-left:15px;">
            <thead>
             <tr class="odd">
                <th align="center">Tipo</th>
                <th align="center">Valor</th>
                <th align="center">Nome</th>
                <th align="center">Telefone</th>
                <th align="center">Situacão</th>
                <th align="center">Lugar</th>
                <th align="center">Excluir</th>
             </tr>
            </thead>
            <c:forEach var="item" items="${lugares}">
                <c:if test='${app==1571}'>
                    <c:set var="lugar" value="${item}"/>
                </c:if>
                <c:if test='${app==1572}'>
                    <fmt:formatNumber var="mesa"  value="${item.mesa}" pattern="000"/>
                    <fmt:formatNumber var="cadeira"  value="${item.cadeira}" pattern="00"/>
                    <c:set var="lugar" value="${mesa}${cadeira}"/>
                    
                    <c:set var="tipoPessoa" value="${item.tipoPessoa}"/>
                    <c:set var="meia" value="${item.meia}"/>
                    <c:set var="valor" value="${item.valor}"/>
                    <c:set var="pessoa" value="${item.pessoa}"/>
                    <c:set var="telefone" value="${item.telefone}"/>
                    <c:set var="situacao" value="${item.situacao}"/>
                    <fmt:formatNumber var="matricula"  value="${item.matricula}" pattern="0000"/>
                    <fmt:formatNumber var="categoria"  value="${item.categoria}" pattern="00"/>
                    <fmt:formatNumber var="dependente"  value="${item.dependente}" pattern="00"/>
                    <c:set var="titulo" value="${matricula}${categoria}${dependente}"/>
                    
                    <input type="hidden" name="mesa" id="mesa" value="${mesa}"/>
                    <input type="hidden" name="cadeira" id="cadeira" value="${cadeira}"/>
                    <input type="hidden" name="lugarGato" id="lugarGato" value="${mesa}${cadeira}"/>
                </c:if>
                
                <tr>
                    <td align="center">
                        <div class="selectheightnovo2">
                            <select name="tipo${lugar}" id="tipo${lugar}" onchange="trocaTipo('${lugar}')" class="campoSemTamanho alturaPadrao" style="width:105px" ">
                            <option value="SN" <c:if test='${tipoPessoa=="S" && item.meia=="N"}'>selected</c:if>>Sócio</option>
                            <option value="NN" <c:if test='${tipoPessoa=="N" && item.meia=="N"}'>selected</c:if>>Não Sócio</option>
                            <option value="CN" <c:if test='${tipoPessoa=="C" && item.meia=="N"}'>selected</c:if>>Convênio</option>
                            <option value="SS" <c:if test='${tipoPessoa=="S" && item.meia=="S"}'>selected</c:if>>Sócio - Meia</option>
                            <option value="NS" <c:if test='${tipoPessoa=="N" && item.meia=="S"}'>selected</c:if>>Não Sócio - Meia</option>
                            <option value="CS" <c:if test='${tipoPessoa=="C" && item.meia=="S"}'>selected</c:if>>Convênio - Meia</option>
                            </select>
                        </div>    
                    </td>
                    <td align="center">
                        <fmt:formatNumber var="valor"  value="${valor}" pattern="#0.00"/>
                        <input type="text" name="valor${lugar}" id="valor${lugar}" maxlength="10" class="campoSemTamanhoSemMargem alturaPadrao" style="width:70px" value="${valor}" />
                    </td align="center">
                    <td align="center">
                        <table class="fmt">
                            <tr>
                                <td>
                                    <input type="text" name="nome${lugar}" id="nome${lugar}" maxlength="40" readonly class="campoSemTamanhoSemMargem alturaPadrao" style="width:300px;margin-top:7px;" value="${pessoa}" />
                                    <input type="hidden" name="titulo${lugar}" id="titulo${lugar}" value="${titulo}"/>
                                </td>
                                <td>
                                    <input name="pesquisaSocio${lugar}" id="pesquisaSocio${lugar}" class="botaobuscainclusao" style="margin-top:7px" type="button" onclick="pesquisaSocio('${lugar}')" value="" title="Consultar" />
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td align="center">
                        <input type="text" name="telefone${lugar}" id="telefone${lugar}" maxlength="40" class="campoSemTamanhoSemMargem alturaPadrao" style="width:120px" value="${telefone}" />
                    </td>
                    <td align="center">
                        <div class="selectheightnovo2">
                            <select name="situacao${lugar}" id="situacao${lugar}" class="campoSemTamanhoSemMargem alturaPadraoo" style="width:105px" >
                            <option value="RE" <c:if test='${situacao=="RE"}'>selected</c:if>>Reservada</option>
                            <option value="CO" <c:if test='${situacao=="CO"}'>selected</c:if>>Confirmada</option>
                            </select>
                        </div>    
                    </td>
                    
                    <td align="center" id="tabLugar">
                        <c:set var="mesa" value="${fn:substring(lugar, 0, 3)}" />
                        <c:set var="cadeira" value="${fn:substring(lugar, 3, 5)}" />
                        ${mesa} - &#${64+cadeira}
                        <input type="hidden" name="lugar" id="lugar" value="${lugar}"/>
                    </td>
                    
                    <td class="column1" align="center" >    
                        <a id="excluir" href="javascript: void(0);'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </td>
                    
                </tr>
            </c:forEach>
        </table>
        
        
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1570&idEvento=${idEvento}';" value=" " />

    </form>
                
        
        


    <div id="pesqSocio" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
        <table style="background:#fff">
            <tr>
              <td>
                <div class="divisoria"></div>
                <div id="titulo-subnav">Seleção de Sócio</div>
                <div class="divisoria"></div>
                
                <table class="fmt" align="left" >
                    <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Categoria</p>
                            <input type="text" id="categoriaSocio" name="categoriaSocio" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                        </td>

                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Matricula</p>
                            <input type="text" id="matriculaSocio" name="matriculaSocio" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Nome</p>
                            <input type="text" id="nomeSocio" name="nomeSocio" class="campoSemTamanho alturaPadrao" style="width:300px" value="">
                        </td>
                        <td >    
                            <input type="button" class="botaobuscainclusao" style="margin-top:20px" onclick="carregaSocio()" value="" title="Consultar" />
                        </td>
                    </tr>
                </table>
                <br><br><br>
                <table id="tabelaSocio" align="left" style="margin-left:15px;">
                    <thead>
                    <tr class="odd">
                        <th scope="col" class="nome-lista">Nome</th>
                        <th scope="col" align="left">Categoria</th>
                    </tr>	
                    </thead>
                    <tbody>
                    </tbody>
                </table>  
                <br>
                <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisa()" />
            </td>
          </tr>
        <table>
            
    </div>    
        
        
</body>
</html>
