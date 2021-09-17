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
            $("#atualizar").live("click", atualizar);

            $("#iniValidade").mask("99/99/9999");
            $("#fimValidade").mask("99/99/9999");
            $("#nasc").mask("99/99/9999");

            $("#segundaEntrada").mask("99:99");
            $("#tercaEntrada").mask("99:99");
            $("#quartaEntrada").mask("99:99");
            $("#quintaEntrada").mask("99:99");
            $("#sextaEntrada").mask("99:99");
            $("#segundaSaida").mask("99:99");
            $("#tercaSaida").mask("99:99");
            $("#quartaSaida").mask("99:99");
            $("#quintaSaida").mask("99:99");
            $("#sextaSaida").mask("99:99");

            $('#supervisao').hide();
        });

        function atualizar () {
            if ($('#convidado').val() == ''){
                alert('Informe o nome do convidado!');
                return false;
            }

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
                if($('#cpf').val() != ''){
                    //valida cpf
                    if($('#cpf').val().length != 11){
                       alert("Número de CPF incompleto!");
                       return false;
                    }
                    if(!CPFValido($('#cpf').val())){
                       alert("Número do CPF inválido!");
                       return false;
                    }
                }
            }

            var cancelou = "N";

            $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data:{tipo:2,nome:encodeURIComponent($('#convidado').val().toUpperCase())}}).success(function(retorno){
                var ret = 0;
                ret = retorno.substring(0,1);
                if (ret > 0){
                    var mostraLista = 0;
                    if (ret == 1){
                        if (confirm('Foi encontrada uma pessoa com este mesmo nome na Lista Negra. Deseja Continuar?') == false){

                            cancelou == "S";
                        }
                    }else {
                        if (confirm('Foi encontrada uma pessoa iniciando com ' + retorno.substring(1) + ' na Lista Negra. Deseja Continuar?') == false){

                            cancelou == "S";

                            if (confirm('Deseja verificar o cadastro da Lista Negra?')){
                                mostraLista = 1;
                            }
                        }
                    }
                    if (mostraLista == 1){
                        window.open('c?app=2140','page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600'); 
                    }
                }
            });

            if (cancelou == "S"){
                return false;
            }
            
            if($('#iniValidade').val() == ''){
               alert("Informe a data de início de validade!");
               return false;
            }
            if(!isDataValida($('#iniValidade').val())){
                return false; 
            }
            if($('#fimValidade').val() == ''){
               alert("Informe a data de fim de validade!");
               return false;
            }
            if(!isDataValida($('#fimValidade').val())){
                return false; 
            }
            if($('#modalidade').val() == ''){
               alert("Informe descrição da modalidade!");
               return false;
            }
            
            gravaConvites();
            
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

        function gravaConvites(){
            var convite = '';
            $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',
                    data:{
                        tipo:9,
                        matricula:$('#matricula').val(),
                        categoria:$('#categoria').val(),
                        responsavel:encodeURIComponent($('#responsavel').val()),
                        convidado:encodeURIComponent($('#convidado').val()),
                        categoriaConvite:"SP",
                        usuario:$('#usuarioAtual').val(),
                        estacionamento:$('#est').val(),
                        cpf:$('#cpf').val(),
                        docEst:$('#docEst').val(),
                        rg:$('#rg').val(),
                        orgaoExp:$('#orgaoExp').val(),
                        validade:$('#fimValidade').val(),
                        dtNasc:$('#nasc').val(),
                        
                        inicioValidade:$('#iniValidade').val(),
                        fimValidade:$('#fimValidade').val(),
                        modalidade:$('#modalidade').val(),
                        tipoConvite:$('#tipoEsportivo').val(),
                        
                        hhEntraSegunda:$('#segundaEntrada').val(),
                        hhSaiSegunda:$('#segundaSaida').val(),
                        hhEntraTerca:$('#tercaEntrada').val(),
                        hhSaiTerca:$('#tercaSaida').val(),
                        hhEntraQuarta:$('#quartaEntrada').val(),
                        hhSaiQuarta:$('#quartaSaida').val(),
                        hhEntraQuinta:$('#quintaEntrada').val(),
                        hhSaiQuinta:$('#quintaSaida').val(),
                        hhEntraSexta:$('#sextaEntrada').val(),
                        hhSaiSexta:$('#sextaSaida').val()
                        
                    }
                }).success(function(retorno){
                    convite = retorno;
                    alert('Convite emitido com sucesso!');
                });

            if (confirm('Deseja Imprimir o Convite Agora?')){
                window.location.href='c?app=1070&acao=impressao&idConvite='+convite+'&categoriaConvite=SP';
            }else{
                document.forms[0].submit();
            }

        }
                
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
                  <input id="responsavel" type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:250px" disabled value="${socio.nome}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                  <input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:200px" disabled value="ESPORTIVO">
              </td>
            </tr>
        </table>    

        <br><br><br><br>

        <div class="divisoria"></div>
        <div id="titulo-subnav">Dados do Convidado</div>
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
                  <input id="orgaoExp" type="text" name="orgaoExp" class="campoSemTamanho alturaPadrao larguraData" maxlength="15" value="">
              </td>
            </tr>
        </table>    
        
        <br><br><br><br>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Dados do Convite</div>
        <div class="divisoria"></div>
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Inicio Validade</p>
                  <input id="iniValidade" type="text" name="iniValidade" class="campoSemTamanho alturaPadrao" style="width:80px" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Fim Validade</p>
                  <input id="fimValidade" type="text" name="fimValidade" class="campoSemTamanho alturaPadrao" style="width:80px" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Modalidade</p>
                  <input id="modalidade" type="text" name="modalidade" class="campoSemTamanho alturaPadrao" style="width:202px" value="">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0">Tipo</p>
                  <select id="tipoEsportivo" name="tipoEsportivo" class="campoSemTamanho alturaPadrao larguraData" >
                      <option value="4C">4 Convites</option>
                      <option value="8C">8 Convites</option>
                      <option value="12">Até 12 anos</option>
                  </select>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0">Estacionamento</p>
                  <select id="est" name="idCargoEspecial" class="campoSemTamanho alturaPadrao larguraData" >
                      <option value="Interno" >Interno</option>
                      <option value="Externo" selected>Público</option>
                  </select>
              </td>
              
            </tr>
        </table>    
        
        <br><br><br><br>

        <div class="divisoria"></div>
        <div id="titulo-subnav">Horários</div>
        <div class="divisoria"></div>
                        
        <table class="fmt" align="left" >
            <tr>
                <td colspan="2" >
                    <p class="legendaCodigo MargemSuperior0" >Segunda</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Terça</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Quarta</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Quinta</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Sexta</p>
                </td>   
            </tr>
            <tr>    
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="segundaEntrada" name="segundaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="">
              </td>
              <td align="left">
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;" >Saída:</p>
                  <input type="text" id="segundaSaida" name="segundaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="tercaEntrada" name="tercaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="tercaSaida" name="tercaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="quartaEntrada" name="quartaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="quartaSaida" name="quartaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="quintaEntrada" name="quintaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="quintaSaida" name="quintaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="sextaEntrada" name="sextaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="sextaSaida" name="sextaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="">
              </td>
            </tr>    
       </table>

        <br><br><br><br><br>
        
        <input type="button" id="atualizar" class="botaoatualizar" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1071&acao=consultarSocio&matricula=${socio.matricula}&categoria=${socio.idCategoria}&nome=&selecao=${tipo}';" value=" " />
        
    </form>

</body>
</html>
