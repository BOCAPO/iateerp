



<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <%@include file="menu.jsp"%>

    <script type="text/javascript" language="JavaScript">
            $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');
                
                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
                $('#divSaldoConvite').hide();

                
            });
            
            function validarForm(){

                var carteira = trim(document.forms[0].carteira.value);
                var categoria = trim(document.forms[0].categoria.value);
                var matricula = trim(document.forms[0].matricula.value);
                var nome = trim(document.forms[0].nome.value);

                if(carteira == '' && categoria == '' && matricula == '' && nome == ''){
                    alert('É preciso informar ao menos 1 parâmetro de consulta');
                    return false;
                } else if(nome != '' && nome.length < 3){
                    alert('Nome para pesquisa deve ter no mínimo 3 letras.');
                    return false;
                } else {
                    document.forms[0].submit();
                }
            }
            
            function validarSelecao(){
                var saldo=0;
                var sel = null;
                $("input:radio").each(function(){
                    if (this.checked == true) {
                        sel = this;
                        if ($('#selecao').val()=='GR'){
                            saldo = $(this).closest('tr').find('#tabSaldo').html();
                        }else if ($('#selecao').val()=='SA'){
                            saldo = $(this).closest('tr').find('#tabSaldoSauna').html();
                        }else{
                            saldo=99;
                        }
                        
                    }            
                });
                
                if (saldo == 0){
                    alert('Quantidade de Convites esgotado.');
                    return false;
                }
                var ret = "OK";
                if (sel.value.substring(4,6) != 09){
                    $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data:{
                                        tipo:6,
                                        matricula:sel.value.substring(0,4),
                                        categoria:sel.value.substring(4,6)} 
                    }).success(function(retorno){
                        ret = retorno;
                    });
                }
                if (ret=='PA'){
                    alert('Os direitos do Título estão suspensos devido aos atrasos no pagamento dos Carnes!');
                    return false;
                }
            }
            
            function mostraSaldoConvite(matricula, categoria, saldo){
                $('#matriculaSel').val(matricula);
                $('#categoriaSel').val(categoria);
                $('#saldoConvites').val(saldo);
                $('#divSaldoConvite').show();
                $('#saldoConvites').focus();
            }
            
            function cancelarSaldo(){
                $('#divSaldoConvite').hide();
            }
            
            function atualizarSaldo(){
                if ($('#saldoConvites').val() == ''){
                    alert('Informe o saldo de convites!');
                    return false
                }
                if (isNaN($('#saldoConvites').val())){
                    alert('Informe o saldo de convites corretamente!');
                    return false
                }
                
                $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data:{
                                    tipo:8,
                                    matricula:$('#matriculaSel').val(),
                                    categoria:$('#categoriaSel').val(),
                                    saldo:$('#saldoConvites').val()
                                } 
                }).success(function(retorno){
                    ret = retorno;
                    if (ret=="OK"){
                        alert('Saldo de Convites Atualizado com sucesso!');
                    }
                });
                
                validarForm();
            }
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Inclusão de Convite - Seleção de Sócio</div>
    <div class="divisoria"></div>
    
    <form action="c" method="POST" onsubmit="return validarForm()">
        <input type="hidden" name="app" value="1071">
        <input type="hidden" name="acao" value="consultarSocio">
        <input type="hidden" name="selecao" value="${selecao}">

        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Categoria</p>
                  <input type="text" name="categoria" class="campoSemTamanho alturaPadrao larguraNumero" value="${categoria}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Título</p>
                  <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNumero" value="${matricula}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nome</p>
                  <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:330px" value="${nome}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nr. Carteirinha</p>
                  <input type="text" name="carteira" class="campoSemTamanho alturaPadrao larguraData" value="${carteira}">
              </td>
              <td >    
                  <input class="botaobuscainclusao" style="margin-top:20px" type="submit" onclick="validarForm()" value="" title="Consultar" />              </td>
              
            </tr>
        </table>
    </form>
        
    <form action="c" method="POST" >
        <input type="hidden" name="app" value="1071">
        <input type="hidden" name="acao" value="convites">
        <input type="hidden" name="matriculaSel" id="matriculaSel" value="">
        <input type="hidden" name="categoriaSel" id="categoriaSel" value="">
        <input type="hidden" name="selecao" id="selecao" value="${selecao}">
        
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col" >Selecionar</th>
                    <th scope="col" class="nome-lista">Nome</th>
                    <th scope="col">Saldo Geral</th>
                    <th scope="col">Saldo Sauna</th>
                    <th scope="col">Foto</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="socio" items="${socios}">
                <tr>
                    <td id="tabSel" class="column1" align="center" style="width:30px">
                        <fmt:formatNumber pattern="0000" var="matricula" value="${socio.matricula}"/>
                        <fmt:formatNumber pattern="00" var="categoria" value="${socio.idCategoria}"/>
                        <fmt:formatNumber pattern="00" var="dependente" value="${socio.seqDependente}"/>
                        <input type="radio" name="titulo" id="selecao" checked value="${matricula}${categoria}${dependente}" />
                    </td>

                    <td id="tabNome" class="column1">${socio.nome}</td>

                    <td id="tabSaldo" class="column1" align="center">
                        ${socio.saldoConvite} &nbsp;&nbsp;&nbsp;&nbsp;
                        <c:if test='<%=request.isUserInRole("1077")%>'>
                            <a href="#void" onclick="mostraSaldoConvite(${socio.matricula}, ${socio.idCategoria}, ${socio.saldoConvite});"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </c:if>
                    </td>
                    <td id="tabSaldoSauna" class="column1" align="center">${socio.saldoConviteSauna}</td>
                    <td id="tabFoto" class="column1" align="center" >
                        <img src="f?tb=6&mat=${socio.matricula}&seq=${socio.seqDependente}&cat=${socio.idCategoria}" class="recuoPadrao MargemSuperiorPadrao" width="80" height="100">
                    </td>    
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <input type="submit" id="atualizar" class="botaoatualizar" value=" "  onclick="return validarSelecao()"/>
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1071&acao=showForm';" value=" " />

        
        <!--
            ************************************************************************
            ************************************************************************
            ************************************************************************
                          N O V O      S A L D O      C O N V I T E S
            ************************************************************************
            ************************************************************************
            ************************************************************************
        -->
        <div id="divSaldoConvite" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 400px; height:400px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Novo Saldo de Convites</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Saldo</p>
                                <input type="text" id="saldoConvites" name="saldoConvites" maxlength="1" class="campoSemTamanhoNegrito" style="width: 100px; height:30px;" value="">
                            </td>
                        </tr>
                    </table>
                    <br><br><br><br>
                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <input type="button" id="cmdAtualizarSaldo" name="cmdAtualizarSaldo"  class="botaoatualizar" onclick="atualizarSaldo()" />
                            </td>
                            <td>
                                <input style="margin-left:15px;margin-top: -5px;" type="button" id="cmdCancelarSaldo" name="cmdCancelarSaldo" class="botaocancelar" onclick="cancelarSaldo()" />
                            </td>
                        </tr>
                    </table>

                </td>
              </tr>
            </table>
        </div>                
        
        
    </form>

        
        
</body>
</html>
