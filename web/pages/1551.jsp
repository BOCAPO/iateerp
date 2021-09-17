<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">
        
        $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            $("#excluir").live("click", removeCampo);

            $("#tabela tr:eq(1)").css('display', 'none');

            $("#nasc").mask("99/99/9999");
            $("#dataValidade").mask("99/99/9999");

        });     
        
        function validaDadosAutorizacao(){
            if(trim(document.forms[0].dataValidade.value) == ''){
                alert('Informe a data de validade corretamente');
                return false;
            }
            
            if(!isDataValida(document.forms[0].dataValidade.value)){
                return false;
            }                        

            if(trim(document.forms[0].embarcacao.value) == ''){
                alert('Informe a embarcação da autorização');
                return false;
            }

            if(trim(document.forms[0].capacidade.value) == ''){
                alert('Informe a capacidade da autorização');
                return false;
            }
            
            return true;
        }
    
        function validarForm(){
            
            validaDadosAutorizacao();
            
            var podeIncluir = "SIM";
            if($('#app').val()==1551){
                if ($('#tabela tr').length <= 2){
                    alert('Não há autorização a ser emitida!');
                    return false;
                }
                $("#tabela tr:gt(1)").each(function(index){
                    $('#variosConvidados').val($('#variosConvidados').val() + "*" + $(this).find("#tabNome").html() + '#')
                    $('#variosConvidados').val($('#variosConvidados').val() + "*" + $(this).find("#tabDtNasc").html() + '#')
                    $('#variosConvidados').val($('#variosConvidados').val() + "*" + $(this).find("#tabCPF").html() + '#')
                    $('#variosConvidados').val($('#variosConvidados').val() + "*" + $(this).find("#tabDocEst").html() + '#;')
                });
                
            }else{
                if (!validaDocumentos()){
                    return false;
/*                    
                }else{
                    if($('#cpf').val() != '' || $('#docEst').val() != ''){
                        
                        var anoValidacao = $('#dataValidade').val().substring(6);
                        $.ajax({url:'AutorizacaoEmbarqueAjax', async:false, dataType:'text', type:'GET',
                                data:{
                                        tipo:1,
                                        cpf:$('#cpf').val(),
                                        docEst:$('#docEst').val(),
                                        seqAutorizacao:$('#idAutorizacaoEmbarque').val(),
                                        anoValidacao:anoValidacao
                                     }}).success(function(retorno){
                            var qtd = 0;
                            qtd = retorno;
                            if (qtd >= 12){
                                podeIncluir = "NAO";
                                alert('CPF/Documento Estrangeiro com limite extrapolado!');
                                return false;
                            }
                        });
                    }   
*/        
                }
            }
            
            $('#dataValidade').attr('disabled', false);
            $('#embarcacao').attr('disabled', false);
            $('#capacidade').attr('disabled', false);
            if (podeIncluir=="SIM"){
                document.forms[0].submit();
            }
            
        }
        
        function validaDocumentos(){
            
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
                var docProblema = 0;
                if($('#cpf').val() == ''){
                    //valida documento estrangeiro
                    $("#tabela tr:gt(1)").each(function(index){
                        //alert($(this).find("#tabDocEst").html());
                        //alert($('#docEst').val());
                        if ($(this).find("#tabDocEst").html() == $('#docEst').val()){
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
            
            return true;
            
        }
        
        function adicionarConvidado(){
            
            if (!validaDadosAutorizacao()){
                return false;
            }
            
            if(trim(document.forms[0].convidado.value) == ''){
                alert('Informe o nome do convidado');
                return;
            }
                
            if (!validaDocumentos()){
                return false;
            }

            var incluiLinha = 1;           
/*            
            if($('#cpf').val() != '' || $('#docEst').val() != ''){
                var anoValidacao = $('#dataValidade').val().substring(6);
                $.ajax({url:'AutorizacaoEmbarqueAjax', async:false, dataType:'text', type:'GET',
                        data:{
                           tipo:1,
                           cpf:$('#cpf').val(),
                           docEst:$('#docEst').val(),
                           anoValidacao:anoValidacao
                        }}).success(function(retorno){
                    var qtd = 0;
                    qtd = retorno;
                    if (qtd >= 12){
                        incluiLinha = 0;
                        alert('CPF/Documento Estrangeiro com limite extrapolado!');
                        return false;
                    }
                });
            }   
*/
            $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data: {tipo:2,nome:$('#convidado').val()}
            }).success(function(retorno){
                var ret = 0;
                ret = retorno.substring(0,1);
                if (ret > 0){
                    if (ret == 1){
                        if (confirm('Foi encontrada uma pessoa com este mesmo nome na Lista Negra. Deseja Continuar?') == false){
                            incluiLinha = 0; 
                            return;
                        }
                    }else {
                        if (confirm('Foi encontrada uma pessoa iniciando com ' + retorno.substring(1) + ' na Lista Negra. Deseja Continuar?') == false){
                            if (confirm('Deseja verificar o cadastro da Lista Negra?')){
                                window.open('c?app=2140','page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600'); 
                            }
                            incluiLinha = 0; 
                            return;
                        }
                    }
                }
                
                if (incluiLinha == 1){
                    incluiLinhaTabela();   
                    habilitaDadosAutorizacao();
                }
            
                //document.forms[0].acao.value = 'adicionarConvidado';
                //document.forms[0].submit();                                                    
                
            });
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
        
        
        function removeCampo() {
            $(this).parent().parent().remove();
            habilitaDadosAutorizacao();
        }

        function habilitaDadosAutorizacao(){
            if ($('#tabela tr').length > 2){
                $('#dataValidade').attr('disabled', true);
                $('#embarcacao').attr('disabled', true);
                $('#capacidade').attr('disabled', true);
            }else{
                $('#dataValidade').attr('disabled', false);
                $('#embarcacao').attr('disabled', false);
                $('#capacidade').attr('disabled', false);
            }
            
        }

        function incluiLinhaTabela(){
            novoCampo = $("#tabela tr:last").clone();
            novoCampo.find("#tabDtNasc").html($('#nasc').val());
            novoCampo.find("#tabNome").html($('#convidado').val().toUpperCase() );
            novoCampo.find("#tabCPF").html($('#cpf').val());
            novoCampo.find("#tabDocEst").html($('#docEst').val());
            
            //retirando os ;
            //novoCampo.find("#tabNome").html($('#convidado').val().split(';').join(''));
            novoCampo.css('display','');
            novoCampo.insertAfter("#tabela tr:last");
            $('#convidado').val('');
            $('#nasc').val('');
            $('#cpf').val('');
            $('#docEst').val('');
            
        }

    </script>  

    <div class="divisoria"></div>
    <c:if test='${app == "1551"}'><div id="titulo-subnav">Inclusão de Autorização de Embarque - Responsável</div></c:if>
    <c:if test='${app == "1552"}'><div id="titulo-subnav">Alteração Autorização de Embarque - Responsável</div></c:if>
    <div class="divisoria"></div>

    <br>
    
    <form action="c">
        <input type="hidden" name="app" id="app" value="${app}">
        <input type="hidden" name="matricula" value="${responsavel.matricula}">
        <input type="hidden" name="idCategoria" value="${responsavel.idCategoria}">
        <input type="hidden" name="seqDependente" value="${responsavel.seqDependente}">
        <input type="hidden" name="idAutorizacaoEmbarque" id="idAutorizacaoEmbarque" value="${autorizacao.id}">
        <input type="hidden" name="variosConvidados"  id="variosConvidados" value="">
        <input type="hidden" name="acao" value="gravar">
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nome</p>
                  <input type="text" readonly="true" class="campoSemTamanho alturaPadrao" style="width:525px" value="${responsavel.nome}"/>
              </td>
            </tr>
        </table>    
        <br><br><br><br>

        <div class="divisoria"></div>
        <div id="titulo-subnav">Dados da Autorização</div>
        <div class="divisoria"></div>

        <table class="fmt" align="left" >
            <tr>
              <td>
                    <p class="legendaCodigo MargemSuperior0" >Validade</p>
                    <fmt:formatDate var="dataValidade" value="${autorizacao.dataValidade}" pattern="dd/MM/yyyy" />
                    <input type="text" name="dataValidade" id="dataValidade" maxlength="10" class="campoSemTamanho alturaPadrao larguraNumero" value="${dataValidade}">
              </td>
              <td>
                    <p class="legendaCodigo MargemSuperior0">Embarcação:</p>
                    <input type="text" name="embarcacao" id="embarcacao" class="campoSemTamanho alturaPadrao" style="width:150px" value="${autorizacao.embarcacao}"/>
              </td>
              <td>
                    <p class="legendaCodigo MargemSuperior0">Capacidade:</p>
                    <input type="text" name="capacidade" id="capacidade" maxlength="3" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero" value="${autorizacao.capacidade}"/>
              </td>
            </tr>
        </table>    
              
        <br><br><br><br>
        
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Dados do(s) Convidado(s)</div>
        <div class="divisoria"></div>

        <table class="fmt" align="left" >
            <tr>
              <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" id="lblConvidado">Nome</p>
                    <input type="text" name="convidado" id="convidado" class="campoSemTamanho alturaPadrao" style="width:200px" <c:if test='${app == "1552"}'>value="${autorizacao.convidado}"</c:if>/>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dt. Nascimento</p>
                  <c:if test='${app == "1552"}'>
                      <fmt:formatDate var="dtNascimento" value="${autorizacao.dtNascimento}" pattern="dd/MM/yyyy" />
                  </c:if>                  
                  <input id="nasc" type="text" name="nasc" class="campoSemTamanho alturaPadrao larguraData" maxlength="10" <c:if test='${app == "1552"}'>value="${dtNascimento}"</c:if>>
              </td>
              
              <td>
                  <p class="legendaCodigo MargemSuperior0" >CPF</p>
                  <input id="cpf" type="text" name="cpf" class="campoSemTamanho alturaPadrao larguraData" maxlength="11" <c:if test='${app == "1552"}'>value="${autorizacao.cpfConvidado}"</c:if>>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Doc. Estrangeiro</p>
                  <input id="docEst" type="text" name="docEst" class="campoSemTamanho alturaPadrao larguraData" maxlength="30" <c:if test='${app == "1552"}'>value="${autorizacao.docEstrangeiro}"</c:if>>
              </td>
            </tr>
        </table>    
              
        <br><br><br><br>
        
        
        <c:if test='${app != "1552"}'>
            <input type="button" id="btnAdicionarConvidado" onclick="adicionarConvidado()" class="botaoIncluir" value=" " />
            
            <table id="tabela" style="width:98%;margin-left:15px;">
                <thead>
                <tr class="odd">
                    <th scope="col" class="nome-lista">Convidados</th>
                    <th scope="col" class="nome-lista">Dt. Nascimento</th>
                    <th scope="col" class="nome-lista">CPF</th>
                    <th scope="col" class="nome-lista">Doc. Estrang.</th>
                    <th scope="col" align="center">Excluir</th>
                </tr>	
                </thead>
                <tbody>
                    <tr>
                        <td id="tabNome" class="column1"></td>
                        <td id="tabDtNasc" class="column1"></td>
                        <td id="tabCPF" class="column1"></td>
                        <td id="tabDocEst" class="column1"></td>
                        <td id="tabExcluir" class="column1" align="center"><a id="excluir" href="javascript: void(0);'"><img src="imagens/icones/inclusao-titular-05.png" /></a></td>
                    </tr>

                </tbody>
            </table>
        </c:if>

        <br>
        <input type="button" class="botaoatualizar" onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1551&acao=&nomeSocio=${responsavel.nome}';" value=" " />
        
    </form>

        
</body>
</html>
