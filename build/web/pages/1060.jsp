<%@page import="techsoft.curso.TurmaHorario"%>
<%@page import="techsoft.curso.Turma"%>
<%@include file="head.jsp"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
<link rel="stylesheet" type="text/css" href="css/calendario.css"/>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>   

<body class="internas">
            
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
                $('#naoSelecionado tr:gt(0)').css('background', 'white');
                $('#selecionado tr:gt(0)').css('background', 'white');

                $('#naoSelecionado tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                $('#selecionado tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                $("#dtInicio").mask("99/99/9999");
                $("#dtFim").mask("99/99/9999");
                $("#hhInicio").mask("99:99");
                $("#hhFim").mask("99:99");
                
                $("#dtLimConf").mask("99/99/9999");
                
                $("#pesqSocio").hide();
                $("#divTipoEvento").hide();


        });         
        
        function validarForm(){
        
            if ($('#nome').val()==""){
                alert('Informe o Responsável!');
                return false;
            }

            if (!isDataHoraValida($('#dtInicio').val()+' '+$('#hhInicio').val()+':00')) {
                alert('Data de Início Inválida!');
                return false;
            }
            if (!isDataHoraValida($('#dtFim').val()+' '+$('#hhFim').val()+':00')) {
                alert('Data de Fim Inválida!');
                return false;
            }
            
            var dia = $('#dtInicio').val().substring(0, 2);
            var mes = $('#dtInicio').val().substring(3, 5);
            var ano = $('#dtInicio').val().substring(6);
            var inicioStr = mes+'/'+dia+'/'+ano+' '+$('#hhInicio').val()+':00';
            var inicio = new Date(inicioStr);
            dia = $('#dtFim').val().substring(0, 2);
            mes = $('#dtFim').val().substring(3, 5);
            ano = $('#dtFim').val().substring(6);
            var fimStr = mes+'/'+dia+'/'+ano+' '+$('#hhFim').val()+':00';
            var fim = new Date(fimStr);

            if (fim<inicio){
                alert('Data de Fim não pode ser anterior à Data de Início');
                return false;
            }

            if ($('#situacao').find(":selected").val()=='R'){
                if ($('#dtLimConf').val()==''){
                    alert('Informe a Data de Limite da Confirmação!');
                    return false;
                }
            }
            
            //alert($('#acao').val());
            
            if ($('#acao').val()=='gravarInclusao'){
                //alert('inicio ' + inicioStr);
                //alert('fim ' + fimStr);
                var ret;
                $.ajax({url:'ReservaDependenciaAjax', async:false, dataType:'text', type:'GET',data:{
                                    tipo:1,
                                    dep:$('#dep').val(),
                                    inicio:inicioStr,
                                    fim:fimStr}
                }).success(function(retorno){
                    ret = retorno;
                });
                
                if (ret!="OK"){
                   alert('Já existe outra reserva chocando com esses dias e horários.');
                   return false;
                }
                
                $.ajax({url:'ReservaDependenciaAjax', async:false, dataType:'text', type:'GET',data:{
                                    tipo:2,
                                    inicio:inicioStr}
                }).success(function(retorno){
                    ret = retorno;
                });
                
                if (ret!="OK"){
                   alert('Não é possível reservar em data passada.');
                   return false;
                }
            }
            
            var itens="";
            $('[name=idItemSel]').each(function(){
                var quant=$(this).parent().parent().find('#tabQuant').html();
                var valor=$(this).parent().parent().find('#tabValor').html();
                var item = $(this).val();
                itens = itens + item + "#" + quant + "#" + valor + ";";
                
            });  
            $('#itens').val(itens);

            document.forms[0].submit();

        }
        
        function inclui(){

            var qt=0;
            $('[name=idItem]').each(function(){
                if ($(this).attr('checked')){
                    qt=1;
                    return
                }
            });  
            if (qt==0){
                alert('Nenhuma item foi selecionado!');
                return false;
            }
            
            if ($('#quant').val()==''){
                alert('Informe a quantidade!');
                return false;
            }

            $('[name=idItem]').each(function(){
                if ($(this).attr('checked')){
                    var valor=$(this).parent().parent().find('#tabValor').html().replace('.', '').replace(',', '.');
                    var quant=$('#quant').val();
                    var total = (valor*quant).toFixed(2);
                    
                    $.format.locale({
                        number: {
                        groupingSeparator: '.',
                        decimalSeparator: ','
                        }
                    });
                    
                    var total2 = $.format.number((total*1), '#,##0.00');
                    
                    $('#selecionado').append('<tr><td align="center">'+$(this).parent().html().replace('idItem', 'idItemSel')+'</td><td id="tabDescricao" class="column1">'+$(this).parent().parent().find('#tabDescricao').html()+'</td><td id="tabQuant" class="column1" align="center">'+$('#quant').val()+'</td><td id="tabValor" class="column1" align="right">'+total2+'</td></tr>');
                    $(this).parent().parent().remove();
                }
            });  
            
            $('#naoSelecionado tr:gt(0)').css('background', 'white');
            $('#selecionado tr:gt(0)').css('background', 'white');
            
            total();

        }
        function exclui(){

            var qt=0;
            $('[name=idItemSel]').each(function(){
                if ($(this).attr('checked')){
                    qt=1;
                    return
                }
            });  
            if (qt==0){
                alert('Nenhum item foi selecionado!');
                return false;
            }

            $('[name=idItemSel]').each(function(){
                if ($(this).attr('checked')){
                    var valor=$(this).parent().parent().find('#tabValor').html().replace('.', '').replace(',', '.');
                    var quant=$(this).parent().parent().find('#tabQuant').html();
                    var vrInd = valor/quant;
                    $.format.locale({
                        number: {
                        groupingSeparator: '.',
                        decimalSeparator: ','
                        }
                    });
                    var vrInd2 = $.format.number(valor/quant, '#,##0.00');
                    $('#naoSelecionado').append('<tr><td align="center">'+$(this).parent().html().replace('idItemSel', 'idItem')+'</td><td id="tabDescricao" class="column1">'+$(this).parent().parent().find('#tabDescricao').html()+'</td><td id="tabValor" class="column1" align="right">'+vrInd2+'</td></tr>');

                    $(this).parent().parent().remove();
                }
            });  
            
            $('#naoSelecionado tr:gt(0)').css('background', 'white');
            $('#selecionado tr:gt(0)').css('background', 'white');
            
            total();
        }



        function total(){

            var total=0;
            $('[name=idItemSel]').each(function(){
                var valor=$(this).parent().parent().find('#tabValor').html().replace('.', '').replace(',', '.');
                total= (total*1) + (valor*1);
            });  
            $.format.locale({
                number: {
                groupingSeparator: '.',
                decimalSeparator: ','
                }
            });
            var total2 = $.format.number((total*1), '#,##0.00');
            //alert(total);
            $('#total').html("Valor Total: " + total2);
            
        }
        
        function pesquisaSocio() {
            $("#pesqSocio").show();
            var tabela = $('#tabelaSocio').find('tbody').empty();
            $("#nomeSocio").val('');
            $("#matriculaSocio").val('');
            $("#categoriaSocio").val('');
        }
        
        function novoTipoEvento() {
            $("#divTipoEvento").show();
        }
        
        function cancelaPesquisa() {
            $("#pesqSocio").hide();
        }        
        
        function cancelaTipoEvento() {
            $("#divTipoEvento").hide();
        }        
        
        function atualizaTipoEvento() {
            var codTipoEvento = "";
            $.ajax({url:'TipoEventoAjax', async:false, dataType:'text', type:'GET',data:{
                                tipo:1,
                                deTipoEvento:$('#deTipoEvento').val()}
            }).success(function(retorno){
                $('#tipoEvento').append('<option selected value='+retorno+'">'+$('#deTipoEvento').val()+'</option>');
                $("#divTipoEvento").hide();
            });            
        }        
        
        function selecionaSocio(indice){
            $("#pesqSocio").hide();
            $("#tabelaSocio tr:eq("+(parseInt(indice)+1)+")").each(function(index){
                $('#nome').val($(this).find('#nome2').val().substr(1, 45));
                $('#titulo').val($(this).find('#campoTitulo').val());
            });

            $('#nome'+$('#lugarSel').val()).val(nome);
        }

        function carregaSocio(lugar){
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
                    var campoHidden2="";
                    var tpCadastro="";
                    var tpCategoria="";
                    $.each(retorno.jogador, function(i) {
                        if (this.dependente==0){
                            tpCadastro="TITULAR";
                        }else{
                            tpCadastro="DEPENDENTE";
                        }
                        if (this.tpCategoria=="SO"){
                            tpCategoria="SÓCIO";
                        }else{
                            tpCategoria="NÃO SÓCIO";
                        }
                    
                        campoHidden='<input type="hidden" id="campoTitulo"  value="'+this.titulo+'"/>'
                        campoHidden2='<input type="hidden" id="nome2"  value="'+this.nome2+'"/>'
                        linha='<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaSocio('+i+')">'+this.nome+'</a> '+campoHidden2+'</td><td class="column1" align="center">'+this.matricula+' '+campoHidden+'</td><td class="column1">'+tpCadastro+'</td><td class="column1">'+this.descricao+'</td><td class="column1">'+tpCategoria+'</td></tr>';
                        tabela.append(linha)
                    });
                }
            });


        }
        
    </script>
    
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test="${acao eq 'gravarInclusao'}">
        <div id="titulo-subnav">Inclusão de Reserva de Dependência</div>
    </c:if>
    <c:if test="${acao eq 'gravarAlteracao'}">
        <div id="titulo-subnav">Alteração de Reserva de Dependência</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" id="app" value="${app}">
        <input type="hidden" name="acao" id="acao" value="${acao}">
        <input type="hidden" name="dia" value="${dia}">
        <input type="hidden" name="mes" value="${mes}">
        <input type="hidden" name="ano" value="${ano}">
        <input type="hidden" name="dep" id="dep" value="${dep}">
        <input type="hidden" name="tipoPessoa" value="${tipoPessoa}">
        <input type="hidden" name="titulo" id="titulo" value="">
        <input type="hidden" name="itens" id="itens" value="">
        <input type="hidden" name="id" id="id" value="${reserva.numero}">
        <fmt:formatDate var="dtConf" value="${reserva.dtConfirmacao}" pattern="dd/MM/yyyy HH:mm:ss" />                              
        <input type="hidden" name="dtConf" id="dtConf" value="${dtConf}">
        
        <table align="left" class="fmt">
            <tr>
              <td>
                  <table class="fmt">
                      <tr>
                          <td>
                            <p class="legendaCodigo MargemSuperior0" >Responsável:</p>
                            <input type="text" id="nome" name="nome" class="campoSemTamanho alturaPadrao" <c:if test="${tipoPessoa eq 'S'}">readonly</c:if>  <c:if test="${acao eq 'gravarAlteracao'}">disabled</c:if> style="width:302px;" value="${reserva.interessado}">
                          </td>
                          <td>    
                            <c:if test="${tipoPessoa eq 'S'}">
                                <input class="botaobuscainclusao" style="margin-top:20px" <c:if test="${acao eq 'gravarAlteracao'}">disabled</c:if> type="button" onclick="pesquisaSocio()" value="" title="Consultar" />
                            </c:if>
                            <c:if test="${tipoPessoa eq 'N'}">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                                
                          </td>
                          <td>
                            <p class="legendaCodigo MargemSuperior0" >Telefone:</p>
                            <input type="text" id="telefone" name="telefone" class="campoSemTamanho alturaPadrao" style="width:120px;" value="${reserva.telefone}">
                          </td>
                      </tr>
                  </table>
              </td>
            </tr>
            <tr>
              <td>
                  <table class="fmt">
                      <tr>
                          <td>
                            <p class="legendaCodigo MargemSuperior0" >Contato:</p>
                            <input type="text" id="contato" name="contato" class="campoSemTamanho alturaPadrao" style="width:335px;" value="${reserva.nomeContato}">
                          </td>
                          <td>
                            <p class="legendaCodigo MargemSuperior0" >Telefone:</p>
                            <input type="text" id="telContato" name="telContato" class="campoSemTamanho alturaPadrao" style="width:120px;" value="${reserva.telContato}">
                          </td>
                      </tr>
                  </table>
              </td>
            </tr>
            <tr>
              <td>
                  <table class="fmt">
                      <tr>
                          <td>
                            <p class="legendaCodigo MargemSuperior0">Situação</p>
                            <div class="selectheightnovo">
                                <select name="situacao" id="situacao" class="campoSemTamanho alturaPadrao" style="width:140px;" >
                                    <option value="R" <c:if test='${reserva.status == "AC"}'>selected</c:if>>RESERVADA</option>
                                    <option value="C" <c:if test='${reserva.status == "CF"}'>selected</c:if>>CONFIRMADA</option>
                                </select>
                            </div>        
                          </td>
                          <td>
                            <c:if test="${acao eq 'gravarInclusao'}">
                                <jsp:useBean id="now" class="java.util.Date" scope="request" />
                                <fmt:formatDate var="dtSolicitacao" value="${now}" pattern="dd/MM/yyyy" />                              
                            </c:if>
                            <c:if test="${acao eq 'gravarAlteracao'}">
                                <fmt:formatDate var="dtSolicitacao" value="${reserva.dtReserva}" pattern="dd/MM/yyyy HH:mm:ss" />                              
                            </c:if>
                            <p class="legendaCodigo MargemSuperior0" >Dt. Solicitação</p>
                            <input type="text" id="dtSolicitacao" name="dtSolicitacao" disabled class="campoSemTamanho alturaPadrao" style="width:150px;" value="${dtSolicitacao}">
                          </td>
                          <td>
                            <fmt:formatDate var="dtConfirmacao" value="${reserva.dtConfirmacao}" pattern="dd/MM/yyyy HH:mm:ss" />                              
                            <p class="legendaCodigo MargemSuperior0" >Dt. Confirmação</p>
                            <input type="text" id="dtConfirmacao" name="dtConfirmacao" disabled class="campoSemTamanho alturaPadrao" style="width:150px;" value="${dtConfirmacao}">
                          </td>
                      </tr>
                  </table>

              </td>
            </tr>
            <tr>
              <td>
                  <table class="fmt">
                      <tr>
                          <td>
                              
                            <p class="legendaCodigo MargemSuperior0" >Dt. Inicio Util.</p>
                            <c:if test="${acao eq 'gravarInclusao'}">
                                <c:set var="dtIni" value="${dtInicial}"/>
                            </c:if>
                            <c:if test="${acao eq 'gravarAlteracao'}">
                                <fmt:formatDate var="dtIni" value="${dtInicial}" pattern="dd/MM/yyyy"/>
                            </c:if>
                            <input type="text" id="dtInicio" name="dtInicio" class="campoSemTamanho alturaPadrao" <c:if test="${acao eq 'gravarAlteracao'}">disabled</c:if> style="width:110px;" value="${dtIni}">
                          </td>
                          <td>
                            <c:if test="${acao eq 'gravarInclusao'}">
                                <c:set var="dtFim" value="${dtFinal}"/>
                            </c:if>
                            <c:if test="${acao eq 'gravarAlteracao'}">
                                <fmt:formatDate var="dtFim" value="${dtFinal}" pattern="dd/MM/yyyy"/>
                            </c:if>
                            <p class="legendaCodigo MargemSuperior0" >Dt. Fim Util.</p>
                            <input type="text" id="dtFim" name="dtFim" class="campoSemTamanho alturaPadrao" <c:if test="${acao eq 'gravarAlteracao'}">disabled</c:if> style="width:110px;" value="${dtFim}">
                          </td>
                          <td>
                            <p class="legendaCodigo MargemSuperior0" >Hora Inicio Util.</p>
                            <c:if test="${acao eq 'gravarAlteracao'}">
                                <fmt:formatDate var="hhIni" value="${dtInicial}" pattern="HH:mm"/>
                            </c:if>
                            <input type="text" id="hhInicio" name="hhInicio" class="campoSemTamanho alturaPadrao" <c:if test="${acao eq 'gravarAlteracao'}">disabled</c:if> style="width:100px;" value="${hhIni}">
                          </td>
                          <td>
                            <p class="legendaCodigo MargemSuperior0" >Hora Fim Util.</p>
                            <c:if test="${acao eq 'gravarAlteracao'}">
                                <fmt:formatDate var="hhFim" value="${dtFinal}" pattern="HH:mm"/>
                            </c:if>
                            <input type="text" id="hhFim" name="hhFim" class="campoSemTamanho alturaPadrao" <c:if test="${acao eq 'gravarAlteracao'}">disabled</c:if> style="width:100px;" value="${hhFim}">
                          </td>
                      </tr>
                  </table>

              </td>
            </tr>
            <tr>
              <td>
                  <table class="fmt">
                      <tr>
                          <td>
                            <p class="legendaCodigo MargemSuperior0">Tipo de Evento:</p>
                            <div class="selectheightnovo">
                                <select name="tipoEvento" id="tipoEvento" class="campoSemTamanho alturaPadrao" style="width:250px;" >
                                    <c:forEach var="tpEvento" items="${tipoEventos}">
                                        <option value="${tpEvento.id}" <c:if test="${reserva.tipoEvento eq tpEvento.id}">selected</c:if>>${tpEvento.descricao}</option>
                                    </c:forEach>
                                </select>
                            </div>        
                          </td>
                          <td>    
                            <input class="botaoNovoItem" style="margin-top:20px" type="button" onclick="novoTipoEvento()" value="" title="Consultar" />
                          </td>
                          <td>
                            <p class="legendaCodigo MargemSuperior0" >Público Prev.</p>
                            <input type="text" id="publico" name="publico" class="campoSemTamanho alturaPadrao" style="width:75px;" value="${reserva.publico}">
                          </td>
                          <td>
                            <p class="legendaCodigo MargemSuperior0" >Dt. Lim. Conf.</p>
                            <fmt:formatDate var="dtLimConf" value="${reserva.dtLimConf}" pattern="dd/MM/yyyy" />
                            <input type="text" id="dtLimConf" name="dtLimConf" class="campoSemTamanho alturaPadrao" style="width:83px;" value="${dtLimConf}">
                          </td>
                      </tr>
                  </table>

              </td>
            </tr>
                
        </table>    
                                            
    </form>
                          
                          
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                        TABELAS DE ITENS NAO SELECIONADOS
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->
    <div style="position:absolute; margin-top:280px;overflow:auto;height:400px;width:420px;">
        <table id="naoSelecionado" style="width:100%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col">Sel</th>
                    <th scope="col">Item</th>
                    <th scope="col">Valor</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${itens}">
                    <tr>
                        <td class="column1" align="center">
                            <input type="checkbox" name="idItem" id="tabIdItem"  value="${item.id}" >
                        </td>
                        <td class="column1" id="tabDescricao" align="left">${item.descricao}</td>

                        <c:if test="${tipoPessoa eq 'S'}">
                            <fmt:formatNumber var="valor"  value="${item.valorSocio}" pattern="#,##0.00"/>
                        </c:if>
                        <c:if test="${tipoPessoa eq 'N'}">
                            <fmt:formatNumber var="valor"  value="${item.valorNaoSocio}" pattern="#,##0.00"/>
                        </c:if>

                        <td class="column1" id="tabValor"  align="right">${valor}</td>

                    </tr>
                </c:forEach>
            </tbody>
        </table>                      
    </div>
    
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                           TABELAS DE BOTOES DE PASSAGEM
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->
    <div style="position:absolute; margin-top:350px;margin-left:420px">
        <p class="legendaCodigo MargemSuperior0" >Quantidade</p>
        <input type="text" id="quant" name="quant" class="campoSemTamanho alturaPadrao" style="width:83px;" value="1">
        <br>
        <a href="#" onclick="inclui()"><img src="imagens/btn-incluir-um.png" style="margin-top:25px;margin-left:20px;" width="100" height="25" /></a>
        <br>
        <a href="#" onclick="exclui()"><img src="imagens/btn-excluir-um.png" style="margin-top:25px;margin-left:20px;" width="100" height="25" /></a>
    </div>
                          
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                           TABELAS DE ITENS SELECIONADOS
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->
    <div style="position:absolute; margin-top:280px;margin-left:540px;overflow:auto;height:400px;width:470px;">
        <table id="selecionado" style="width:90%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col">Sel</th>
                    <th scope="col">Item</th>
                    <th scope="col">Quant.</th>
                    <th scope="col">Valor</th>
                </tr>
            </thead>
            <c:if test="${acao eq 'gravarAlteracao'}">
                <sql:query var="rs" dataSource="jdbc/iate">
                    SELECT T1.DE_ITEM_ALUGUEL, T2.*
                    FROM TB_ITEM_ALUGUEL T1, TB_ITEM_ALUGUEL_DEP T2
                    WHERE T1.CD_ITEM_ALUGUEL = T2.CD_ITEM_ALUGUEL AND
                    T2.SEQ_RESERVA = ${reserva.numero}
                    ORDER BY 1
                </sql:query>    
                <c:forEach var="row" items="${rs.rows}">
                    <tr>
                        <td class="column1" align="center">
                            <input type="checkbox" name="idItemSel" id="tabIdItemSel"  value="${row.CD_ITEM_ALUGUEL}" >
                        </td>
                        <td class="column1" id="tabDescricao" align="left">${row.DE_ITEM_ALUGUEL}</td>
                        <td class="column1" id="tabQuant" align="left">${row.NU_QUANTIDADE}</td>

                        <fmt:formatNumber var="valor"  value="${row.VR_TOTAL}" pattern="#,##0.00"/>
                        <td class="column1" id="tabValor"  align="right">${valor}</td>

                    </tr>
                </c:forEach>
            </c:if>
        </table>                      
    </div>   
                          
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                           VALOR TOTAL
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->
    <div style="position:absolute; margin-top:690px">
        <h1 id="total" class="msgErro">Valor Total:</h1>
    </div>   
                          
    <div style="position:absolute; margin-top:720px">
        <table class="fmt">
            <tr>
                <td>
                  <input type="button" onclick="validarForm()" class="botaoatualizar" value="">
                </td>
                <td>
                  <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1060';" value="" />
                </td>
            </tr>
        </table>
        <br><br><br>
    </div>   

    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                           TABELA DE SELECAO DE SOCIO
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->

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
                        <th scope="col" align="left">Matric</th>
                        <th scope="col" align="left">Tp Cadastro</th>
                        <th scope="col" align="left">Categoria</th>
                        <th scope="col" align="left">Tp Categoria</th>
                    </tr>	
                    </thead>
                    <tbody>
                    </tbody>
                </table>  
                <br>
                <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisa()" />
            </td>
          </tr>
        </table>
    </div>
              
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                           INCLUSAO DE TIPO DE EVENTO
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->
    <div id="divTipoEvento" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 490px; height:400px;">
        <table style="background:#fff">
            <tr>
              <td>
                <div class="divisoria"></div>
                <div id="titulo-subnav">Novo Tipo de Evento</div>
                <div class="divisoria"></div>
                
                <table class="fmt" align="left" >
                    <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                            <input type="text" id="deTipoEvento" name="deTipoEvento" class="campoSemTamanho alturaPadrao" style="width:400px;" value="">
                        </td>
                    </tr>
                </table>
                <br><br><br>
                <input style="margin-left:15px;margin-top:05px;" type="button" id="cmdCancelar" name="cmdCancelar" class="botaoatualizar" onclick="atualizaTipoEvento()"  />
                <input style="margin-left:15px;" type="button" id="cmdAtualizar" name="cmdAtualizar" class="botaocancelar" onclick="cancelaTipoEvento()" />
            </td>
          </tr>
        </table>
    </div>
                                      
    
</body>
</html>
