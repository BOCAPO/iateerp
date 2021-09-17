<%@include file="head.jsp"%>


<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>        
    
    <script type="text/javascript" language="javascript">
        
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');
                $('#tabela2 tr:gt(0)').css('background', 'white');

                $("#excluir").live("click", removeCampo);
                $("#incluir").live("click", adicionaCampo);
                $("#atualizar").live("click", atualizar);
                $("#atualizarSupervisor").live("click", atualizarSupervisor);
                
                $("#validade").mask("99/99/9999");
                $("#nasc").mask("99/99/9999");

                
                $("#tabela tr:gt(0)").hover(function() {
                        $(this).css('background','f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                $("#tabela2 tr:gt(0)").hover(function() {
                        $(this).css('background','f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })

                $("#tabela tr:eq(1)").css('display', 'none');

                $('#supervisao').hide();

                function removeCampo() {
                        $(this).parent().parent().remove();
                }

                function adicionaCampo () {
                        if ($('#convidado').val() == ''){
                            alert('Informe o nome do convidado!');
                            return false;
                        }
                        if($('#validade').val() == ''){
                           alert("Informe a data de validade!");
                           return false;
                        }
                        
                        if(!isDataValida($('#validade').val())){
                            return false; 
                        }
                        
                        var temDoc = "N";
                        if($('#cpf').val() == '' && $('#docEst').val() == ''){
                            if($('#nasc').val() == ''){
                               alert("Informe a Data de Nascimento do convidado!");
                               return false;
                            }

                            if(!isDataValida($('#nasc').val())){
                                return false; 
                            }

                            var hoje = new Date();
                            if ((hoje.getFullYear() - $('#nasc').val().substring(6)) > 18){
                                alert('Para maiores de 18 anos o convite deve ser emitido com CPF ou Documento Estrangeiro!');
                                return false;
                            }
                            if ((hoje.getFullYear() - $('#nasc').val().substring(6)) == 18){
                                if ((hoje.getMonth() + 1) > $('#nasc').val().substring(3,5)){
                                    alert('Para maiores de 18 anos o convite deve ser emitido com CPF ou Documento Estrangeiro!');
                                    return false
                                }
                                if ((hoje.getMonth() + 1) == $('#nasc').val().substring(3,5)){
                                    if (hoje.getDate() >= $('#nasc').val().substring(0,2)){
                                        alert('Para maiores de 18 anos o convite deve ser emitido com CPF ou Documento Estrangeiro!');
                                        return false
                                    }
                                }
                            }

                        }else{
                            temDoc = "S";
                            var docProblema = 0;
                            if($('#cpf').val() == ''){
                                //valida documento estrangeiro
                                $("#tabela tr:gt(1)").each(function(index){
                                    if ($(this).find("#tabdocEst").html() == $('#docEst').val()){
                                        alert('O Documento Estrangeiro informado já consta na lista de convites!');
                                        docProblema = 1;
                                        return false;
                                    }
                                });
                                
                            }else{
                                //valida cpf
                                if($('#cpf').val().length != 11){
                                   alert("Número de CPF incompleto!");
                                   return false;
                                }
                                if(!CPFValido($('#cpf').val())){
                                   alert("Número do CPF inválido!");
                                   return false;
                                }

                                $("#tabela tr:gt(1)").each(function(index){
                                    if ($(this).find("#tabCPF").html() == $('#cpf').val()){
                                        alert('O CPF informado já consta na lista de convites!');
                                        docProblema = 1;
                                        return false;
                                    }
                                });
                            }
                            
                            if (docProblema == 1){
                                return false;
                            }
                        }
                            
                        var incluiLinha = 1;                
                        
                        if (($('#tipo').val() == "GERAL" || $('#tipo').val() == "CHURRASQUEIRA" || $('#tipo').val().substring(0,7) == "EVENTOS") && temDoc == 'S'){
                            $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data:{tipo:1,cpf:$('#cpf').val(),docEst:$('#docEst').val()}}).success(function(retorno){
                                $('#obs').val('');
                                var qtd = 0;
                                qtd = retorno;
                                if (qtd >= 12){
                                    if ($('#supervisor').val() == "S"){
                                        if (confirm('Já foi extrapolado o limite de convites para o CPF/Documento Estrangeiro informado. Deseja continuar com a emissão?') == false){
                                            incluiLinha = 0;
                                        }else{
                                            $('#obs').val('CPF/Documento Estrangeiro com limite extrapolado<br>');
                                        }
                                    }else{
                                        if (confirm('Já foi extrapolado o limite de convites para o CPF/Documento Estrangeiro informado. Posteriormente será necessária a autorização do supervisor! Deseja continuar com a emissão?') == false){
                                            incluiLinha = 0;
                                        }else{
                                            $('#obs').val('CPF/Documento Estrangeiro com limite extrapolado<br>');
                                        }
                                    }
                                    
                                }
                            });
                        }    
                        
                        $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data:{tipo:2,nome:encodeURIComponent($('#convidado').val().toUpperCase())}}).success(function(retorno){
                            var ret = 0;
                            ret = retorno.substring(0,1);
                            if (ret > 0){
                                var mostraLista = 0;
                                if (ret == 1){
                                    if (confirm('Foi encontrada uma pessoa com este mesmo nome na Lista Negra. Deseja Continuar?') == false){
                                        incluiLinha = 0;
                                    }else{
                                        $('#obs').val($('#obs').val() + 'Mesmo nome na Lista Negra');
                                    }
                                }else {
                                    if (confirm('Foi encontrada uma pessoa iniciando com ' + retorno.substring(1) + ' na Lista Negra. Deseja Continuar?') == false){
                                        incluiLinha = 0;

                                        if (confirm('Deseja verificar o cadastro da Lista Negra?')){
                                            mostraLista = 1;
                                        }
                                    }else{
                                        $('#obs').val($('#obs').val() + 'Nome com início igual na Lista Negra');
                                    }
                                }
                                if (mostraLista == 1){
                                    window.open('c?app=2140','page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600'); 
                                }
                            }
                            
                            if (incluiLinha == 1){
                                incluiLinhaTabela();   
                            }
                            
                        });
                        
                        
                        
                        
                }

                function incluiLinhaTabela(){
                    novoCampo = $("#tabela tr:last").clone();
                    novoCampo.find("#tabDtNasc").html($('#nasc').val());
                    novoCampo.find("#tabNome").html($('#convidado').val().toUpperCase() );
                    novoCampo.find("#tabCPF").html($('#cpf').val());
                    novoCampo.find("#tabDocEst").html($('#docEst').val());
                    novoCampo.find("#tabRG").html($('#rg').val());
                    novoCampo.find("#tabOrgaoExp").html($('#orgaoexp').val());
                    novoCampo.find("#tabDtVal").html($('#validade').val());
                    novoCampo.find("#tabEst").html($('#est').val());
                    novoCampo.find("#tabObs").html($('#obs').val());
                    novoCampo.css('display','');
                    novoCampo.insertAfter("#tabela tr:last");
                    $('#convidado').val('');
                    $('#cpf').val('');
                    $('#docEst').val('');
                    $('#rg').val('');
                    $('#orgaoexp').val('');
                    $('#nasc').val('');
                    $('#obs').val('');
                }
                
                function CPFValido(strCPF) {
                    var Soma;
                    var Resto;
                    Soma = 0;
                        if (strCPF == "00000000000") return false;

                        for (i=1; i<=9; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (11 - i);
                        Resto = (Soma * 10) % 11;

                    if ((Resto == 10) || (Resto == 11))  Resto = 0;
                    if (Resto != parseInt(strCPF.substring(9, 10)) ) return false;

                        Soma = 0;
                    for (i = 1; i <= 10; i++) Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (12 - i);
                    Resto = (Soma * 10) % 11;

                    if ((Resto == 10) || (Resto == 11))  Resto = 0;
                    if (Resto != parseInt(strCPF.substring(10, 11) ) ) return false;
                    return true;
                }                
                
                function atualizar(){
                    
                    if (validaSaldos()){
                        var cpfProblema = 0;
                        if ($('#supervisor').val() == "N"){
                            $("#tabela tr:gt(1)").each(function(index){
                                if ($(this).find("#tabObs").html().substring(0,3) == "CPF"){
                                    cpfProblema = 1;
                                    return;
                                }
                            });
                        }
                        if (cpfProblema == 1){
                            $('#atualizar').hide();
                            $('#supervisao').show();
                            return;
                        }else{
                            gravaConvites();
                        }
                    }
                    
                }
                
                function atualizarSupervisor(){
                
                    if (validaSaldos()){
                        var cpfProblema = 0;
                        $("#tabela tr:gt(1)").each(function(index){
                            if ($(this).find("#tabObs").html().substring(0,3) == "CPF"){
                                cpfProblema = 1;
                                return;
                            }
                        });
                        
                        if (cpfProblema == 1){
                            if ($('#userSupervisor').val() == ''){
                                alert('Informe o Supervisor!');
                                return;
                            }
                            if ($('#userSupervisor').val() == $('#usuarioAtual').val()){
                                alert('O Supervisor não pode ser o mesmo usuário logado no sistema!');
                                return;
                            }
                            if ($('#senhaSupervisor').val() == ''){
                                alert('Informe a Senha do Supervisor!');
                                return;
                            }

                            $.get('ConviteAjax', {tipo:3,usuario:$('#userSupervisor').val(),senha:$('#senhaSupervisor').val()}, function(responseText) {
                                var retorno = '';
                                retorno = responseText;
                                if(retorno == "OK"){
                                    gravaConvites();
                                }else{
                                    alert(retorno);
                                }
                            });    
                        }else{
                            gravaConvites();
                        }
                    }
                }
                
                convites = new Array();
                function gravaConvites(){
                    var qtReg = 0;
                    $("#tabela tr:gt(1)").each(function(index){
                        
                        $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data:{
                                            tipo:4,
                                            matricula:$('#matricula').val(),
                                            categoria:$('#categoria').val(),
                                            responsavel:encodeURIComponent($('#responsavel').val()),
                                            convidado:encodeURIComponent($(this).find('#tabNome').html()),
                                            categoriaConvite:$('#categoriaConvite').val(),
                                            usuario:$('#usuarioAtual').val(),
                                            estacionamento:$(this).find('#tabEst').html(),
                                            convenio:$('#convenio').val(),
                                            reserva:$('#tabResId:checked').val(),
                                            cpf:$(this).find('#tabCPF').html(),
                                            docEst:$(this).find('#tabDocEst').html(),
                                            rg:$(this).find('#tabRG').html(),
                                            orgaoExp:$(this).find('#tabOrgaoExp').html(),
                                            validade:$(this).find('#tabDtVal').html(),
                                            dtNasc:$(this).find('#tabDtNasc').html()}
                        }).success(function(retorno){
                            convites[qtReg] = retorno;
                            qtReg = qtReg + 1;
                        });
                    });
                    if (confirm('Deseja Imprimir os Convites Agora?')){
                        window.location.href='c?app=1070&acao=impressao&idConvite='+convites+'&categoriaConvite='+$('#categoriaConvite').val();
                    }else{
                        document.forms[0].submit();
                    }
                }
                
                function verificaFim(qtAtual){
                    var qtLinha = 0;
                    
                    $("#tabela tr:gt(1)").each(function(index){
                        qtLinha++;
                    });
                    
                    if (qtAtual>=qtLinha){
                        return true;
                    }else{
                        return false;
                    }
                }
                
                function validaSaldos(){
                    var qtLinha = 0;
                    
                    $("#tabela tr:gt(1)").each(function(index){
                        qtLinha++;
                    });
                    
                    if (qtLinha == 0){
                        alert('Não há convite a ser emitido!');
                        return false;
                    }
                        
                    if ($('#saldo').val() < qtLinha && ($('#tipo').val() == "GERAL" || $('#tipo').val() == "SAUNA")){
                        alert('Não é possível incluir ' + qtLinha + ' Convites pois o saldo da Pessoa é de apenas ' + $('#saldo').val());
                        return false;
                    }
                    
                    
                    if ($('#tipo').val() == "CHURRASQUEIRA") {
                        //validar se está funcionando
                        var saldo = 0;
                        saldo = $('#tabResId:checked').parent().parent().find('#tabResSaldo').html();
                        
                        if (saldo < qtLinha){
                            alert('Não há saldo disponível de convites para esta reserva!');
                            return false;
                        }
                    }
                    
                    return true;
                }

            });
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Inclusão de Convite - Responsável</div>
    <div class="divisoria"></div>
    
    <form method="POST">
        <input type="hidden" name="app" value="1070"/>
        <input type="hidden" id="matricula" name="matricula" value="${socio.matricula}"/>
        <input type="hidden" id="categoria" name="categoria" value="${socio.idCategoria}"/>
        <input type="hidden" id="dependente" name="dependente" value="${socio.seqDependente}"/>
        <input type="hidden" id="convenio" name="convenio" value="${convenio.id}"/>
        
        <c:choose>
          <c:when test='${tipo=="GR"}'>
              <input type="hidden" id="saldo" name="saldo" value="${saldo}"/>
          </c:when>
          <c:otherwise>
              <input type="hidden" id="saldo" name="saldo" value="${saldoSauna}"/>
          </c:otherwise>
        </c:choose>                    
              
              
        
        <input type="hidden" id="usuarioAtual" name="usuarioAtual" value="${usuarioAtual}"/>
        <c:choose>
          <c:when test='<%=request.isUserInRole("1077")%>'>
              <input type="hidden" id="supervisor" name="supervisor" value="S"/>
          </c:when>
          <c:otherwise>
              <input type="hidden" id="supervisor" name="supervisor" value="N"/>
          </c:otherwise>
        </c:choose>                        
        <input type="hidden" id="obs" name="obs" value=""/>
        
        <input type="hidden" name="tipo" id="categoriaConvite" value="${tipo}"/>
    
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nome</p>
                  <c:choose>
                    <c:when test='${tipo == "EC"}'>
                        <input id="responsavel" type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:300px" disabled value="${convenio.descricao}">
                    </c:when>
                    <c:otherwise>
                        <input id="responsavel" type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:300px" disabled value="${socio.nome}">
                    </c:otherwise>
                  </c:choose>                        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                  <c:choose>
                    <c:when test='${tipo == "GR"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="GERAL"></c:when>
                    <c:when test='${tipo == "CH"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="CHURRASQUEIRA"></c:when>
                    <c:when test='${tipo == "SA"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="SAUNA"></c:when>
                    <c:when test='${tipo == "SI"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="SINUCA"></c:when>
                    <c:when test='${tipo == "ES"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL SÓCIO"></c:when>
                    <c:when test='${tipo == "EC"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL CONVENIO"></c:when>
                    <c:when test='${tipo == "ED"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL DIRETOR"></c:when>
                    <c:when test='${tipo == "EV"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL VICE-DIRETOR"></c:when>
                    <c:when test='${tipo == "EO"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL VICE-COMODORO"></c:when>
                    <c:when test='${tipo == "EA"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL ASSESSOR-COMODORO"></c:when>
                    <c:when test='${tipo == "EN"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL CONSELHEIRO"></c:when>
                    <c:when test='${tipo == "EP"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL PRESIDENTE DO CONSELHO"></c:when>
                    <c:when test='${tipo == "CO"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="INSTITUCIONAL"></c:when>
                    <c:when test='${tipo == "VD"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS DIRETOR"></c:when>
                    <c:when test='${tipo == "VV"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS VICE-DIRETOR"></c:when>
                    <c:when test='${tipo == "VC"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS COMODORO"></c:when>
                    <c:when test='${tipo == "VO"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS VICE-COMODORO"></c:when>
                    <c:when test='${tipo == "VA"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS ASSESSOR-COMODORO"></c:when>
                    <c:when test='${tipo == "VN"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS CONSELHEIRO"></c:when>
                    <c:when test='${tipo == "VP"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS PRESIDENTE DO CONSELHO"></c:when>
                    <c:otherwise><td class="column1" align="center">FAMÍLIA</td></c:otherwise>
                  </c:choose>                        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Validade</p>
                  <fmt:formatDate var="validade" value="${convite.dtLimiteUtilizacao}" pattern="dd/MM/yyyy"/>
                  <input id="validade" type="text" name="validade" class="campoSemTamanho alturaPadrao larguraData" value="${validade}">
              </td>
            </tr>
        </table>    

        <br><br><br><br>

        <div class="divisoria"></div>
        <div id="titulo-subnav">Convidado</div>
        <div class="divisoria"></div>

        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nome</p>
                  <input id="convidado" type="text" name="convidado" class="campoSemTamanho alturaPadrao" style="width:300px" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dt. Nascimento</p>
                  <input id="nasc" type="text" name="nasc" class="campoSemTamanho alturaPadrao larguraData" maxlength="10" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >CPF</p>
                  <input id="cpf" type="text" name="cpf" class="campoSemTamanho alturaPadrao larguraData" maxlength="11" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Doc. Estrangeiro</p>
                  <input id="docEst" type="text" name="docEst" class="campoSemTamanho alturaPadrao larguraData" maxlength="30" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >RG</p>
                  <input id="rg" type="text" name="rg" class="campoSemTamanho alturaPadrao larguraData" maxlength="10" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Orgão Exp.</p>
                  <input id="orgaoexp" type="text" name="orgaoexp" class="campoSemTamanho alturaPadrao larguraData" maxlength="15" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0">Estacionamento</p>
                  <c:choose>
                    <c:when test='${tipo == "CO" || tipo == "VC" || tipo == "VP"}'>
                        <select id="est" name="idCargoEspecial" disabled class="campoSemTamanho alturaPadrao larguraData" >
                            <option value="Interno" selected>Interno</option>
                            <option value="Externo">Externo</option>
                        </select>
                    </c:when>
                    <c:otherwise>
                        <select id="est" name="idCargoEspecial" class="campoSemTamanho alturaPadrao larguraData" >
                            <option value="Interno" >Interno</option>
                            <option value="Externo" selected>Público</option>
                        </select>
                        
                    </c:otherwise>
                  </c:choose>                        
              </td>
            </tr>
        </table>    
        <br/><br/><br/><br>
        <input type="button" id="incluir" class="botaoIncluir" value=" " />

        <br/>
        <div class="divisoria"></div>
        <div id="titulo-subnav">Convite</div>
        <div class="divisoria"></div>
        <table id="tabela">
            <thead>
                <tr class="odd">
                    <th scope="col" class="nome-lista">Nome</th>
                    <th scope="col">Dt. Validade</th>
                    <th scope="col">CPF</th>
                    <th scope="col">Doc. Estrangeiro</th>
                    <th scope="col">RG</th>
                    <th scope="col">Órgão Exp.</th>
                    <th scope="col">Dt. Nascimento</th>
                    <th scope="col">Estacionamento</th>
                    <th scope="col">Observação</th>
                    <th scope="col">Excluir</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td id="tabNome" class="column1"></td>
                    <td id="tabDtNasc" class="column1" align="center"></td>
                    <td id="tabCPF" class="column1" align="center"></td>
                    <td id="tabDocEst" class="column1" align="center"></td>
                    <td id="tabRG" class="column1" align="center"></td>
                    <td id="tabOrgaoExp" class="column1" align="center"></td>
                    <td id="tabDtVal" class="column1" align="center"></td>
                    <td id="tabEst" class="column1" align="center"></td>
                    <td id="tabObs" class="column1" align="center"></td>
                    <td class="column1" align="center"><a id="excluir" href="javascript: void(0);'"><img src="imagens/icones/inclusao-titular-05.png" /></a></td>
                </tr>
            </tbody>
        </table>
        

        <c:if test='${tipo=="CH"}'>
            <div class="divisoria"></div>
            <div id="titulo-subnav">Reserva</div>
            <div class="divisoria"></div>
            
            <table id="tabela2">
                <thead>
                <tr class="odd">
                    <th scope="col">Selecionar</th>
                    <th scope="col" class="nome-lista">Churrasqueira</th>
                    <th scope="col" >Início</th>
                    <th scope="col" >Fim</th>
                    <th scope="col" class="nome-lista">Nome</th>
                    <th scope="col" >Saldo</th>
                </tr>	
                </thead>
                <tbody>

                <c:forEach var="reserva" items="${reservas}">
                            </th>
                    <c:if test='${reserva.saldoConvite > 0}'>
                        <tr height="1">
                            <th class="column1" align="center">
                                <input type="radio" id="tabResId" name="tabResId" checked value="${reserva.numero}"  />
                            </th>
                            <th class="column1" align="left">${reserva.deChurrasqueira}</th>

                            <fmt:formatDate var="inicio" value="${reserva.dtInicio}" pattern="dd/MM/yyyy HH:mm:ss" />
                            <th class="column1" align="center">${inicio}</th>

                            <fmt:formatDate var="fim" value="${reserva.dtFim}" pattern="dd/MM/yyyy HH:mm:ss" />
                            <th class="column1" align="center">${fim}</th>

                            <th class="column1" align="left">${reserva.interessado}</th>
                            <th class="column1" id="tabResSaldo" name="tabResSaldo" align="center">${reserva.saldoConvite}</th>
                        </tr>	
                    </c:if>            
                </c:forEach>
                </tbody>
            </table>     
        </c:if>      
            
        <br/>
        <input type="button" id="atualizar" class="botaoatualizar" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1071&acao=consultarSocio&matricula=${socio.matricula}&categoria=${socio.idCategoria}&nome=&selecao=${tipo}';" value=" " />
        
        
        <div id="supervisao" >

            <div class="divisoria"></div>
            <div id="titulo-subnav">Supervisão</div>
            <div class="divisoria"></div>
            <table class="fmt" align="left" align="left">
              <tr>
                <td>
                    <p class="legendaCodigo">Supervisor</p>
                    <input type="text" id="userSupervisor"  name="userSupervisor" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value=""/>
                </td>
                <td>
                    <p class="legendaCodigo">Senha</p>
                    <input type="password" id="senhaSupervisor" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value=""/>
                </td>
              </tr>
              <tr>
                <td>
                    <br>
                    <input type="button" id="atualizarSupervisor" class="botaoatualizar" value=" " />
                </td>
              </tr>
            </table>  
        </div>        
    </form>
        

</body>
</html>
