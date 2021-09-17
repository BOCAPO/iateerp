
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}

    #altera {
        margin-top: -250px;
        margin-left: -400px;
        left: 50%;
        top: 50%;
        position: fixed;
    }

</style>

<script type="text/javascript" language="JavaScript">
    var teste;
    
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');
            $("#tpConvite").live("change", habilitaChurras);
            habilitaChurras();
            $('#altera').hide();
            $("#imgAltera").live("click", habilitaAlteracao);   
            
            $("#nascAlt").mask("99/99/9999");

    });        
    
    function habilitaChurras(){
        if ($("#tpConvite").val() == "CH"){
            $('#idChurrasqueira').removeAttr('disabled');
        }else{
            $("#idChurrasqueira").val('0');
            $('#idChurrasqueira').attr('disabled', 'disabled');
        }
    }
    
    function validarForm(){
        var matricula = trim(document.forms[0].matricula.value);
        var tpConvite = trim(document.forms[0].tpConvite.value);
        var nome = trim(document.forms[0].nome.value);
        var nrConvite = trim(document.forms[0].nrConvite.value);
        var convidado = trim(document.forms[0].convidado.value);
        var idChurrasqueira = trim(document.forms[0].idChurrasqueira.value);
        var cpf = trim(document.forms[0].cpf.value);
        var rg = trim(document.forms[0].rg.value);
        var orgaoExp = trim(document.forms[0].orgaoExp.value);

        if(nome == '' && matricula == '' && idChurrasqueira == '0' && tpConvite == 'TO' && convidado == '' && nrConvite == '' && cpf == '' && rg == '' && orgaoExp == ''){
            alert('Informe pelo menos 1 filtro para pesquisa!');
            return false;
        } else {
            $('#idChurrasqueira').removeAttr('disabled');
            $('#acao').val('pesqCO');
        }
    }
    
    function validaSelecao(){
        selecao = new Array();
        
        $("input:radio").each(function(){
            if (this.checked == true) {
		selecao.push($(this).val());
            }            
            
        });
        var tamanho = selecao.length;
        
        if (tamanho==0){
            alert('Nenhuma Convidado foi selecionado.');
            return;
        }
        
        document.forms[0].submit();
    }
    
    
    function habilitaAlteracao(nrConv){
        var Numero = $(this).closest("tr").find("td:eq(12)").html();
        var Nome = $(this).closest("tr").find("td:eq(1)").html();
        var cpf = $(this).closest("tr").find("td:eq(3)").html();
        var rg = $(this).closest("tr").find("td:eq(5)").html();
        var orgaoExp = $(this).closest("tr").find("td:eq(6)").html();
        var tipoConv = $(this).closest("tr").find("td:eq(8)").html();
        var dtNasc = $(this).closest("tr").find("td:eq(2)").html();
        var docEst = $(this).closest("tr").find("td:eq(4)").html();
        teste = $(this);
        $('#nrConv').val('');
        $('#nrConv').val(Numero);
        $('#tipoConv').val(tipoConv);
        $('#nomeAlt').val(Nome.trim());
        $('#cpfAlt').val(cpf.trim());
        $('#rgAlt').val(rg.trim());
        $('#orgaoExpAlt').val(orgaoExp.trim());
        $('#nascAlt').val(dtNasc.trim());
        $('#docEstAlt').val(docEst.trim());
        $('#altera').show();
    }
    
    function fechaAlteracao(){
        $('#altera').hide();
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
    
    function atualizaAlteracao(){
        
        var temDoc = "N";
        if($('#cpfAlt').val() == '' && $('#docEstAlt').val() == ''){
            if($('#nascAlt').val() == ''){
               alert("Informe a Data de Nascimento do convidado!");
               return false;
            }

            if(!isDataValida($('#nascAlt').val())){
                return false; 
            }

            var hoje = new Date();
            if ((hoje.getFullYear() - $('#nascAlt').val().substring(6)) > 18){
                alert('Para maiores de 18 anos o convite deve ser emitido com CPF ou Documento Estrangeiro!');
                return false;
            }
            if ((hoje.getFullYear() - $('#nascAlt').val().substring(6)) == 18){
                if ((hoje.getMonth() + 1) > $('#nascAlt').val().substring(3,5)){
                    alert('Para maiores de 18 anos o convite deve ser emitido com CPF ou Documento Estrangeiro!');
                    return false
                }
                if ((hoje.getMonth() + 1) == $('#nascAlt').val().substring(3,5)){
                    if (hoje.getDate() >= $('#nascAlt').val().substring(0,2)){
                        alert('Para maiores de 18 anos o convite deve ser emitido com CPF ou Documento Estrangeiro!');
                        return false
                    }
                }
            }

        }else{
            temDoc = "S";
            var docProblema = 0;
            if($('#cpfAlt').val() == ''){
                //valida documento estrangeiro
                //nao tem validacao

            }else{
                //valida cpf
                if($('#cpfAlt').val().length != 11){
                   alert("Número de CPF incompleto!");
                   return false;
                }
                if(!CPFValido($('#cpfAlt').val())){
                   alert("Número do CPF inválido!");
                   return false;
                }
            }

            if (docProblema == 1){
                return false;
            }
        }
        
        if (($('#tipoConv').val() == "GERAL" || $('#tipoConv').val() == "CHURRASQUEIRA" || $('#tipoConv').val().substring(0,7) == "EVENTOS") && temDoc == 'S'){
            var qtd = 0;
            $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data:{tipo:1,nrConv:$('#nrConv').val(),cpf:$('#cpfAlt').val(),docEst:$('#docEstAlt').val()}}).success(function(retorno){
                qtd = retorno;
            });
            if (qtd >= 12){
                alert('Já foi extrapolado o limite de convites para o CPF/Documento Estrangeiro informado.');
                return false;
            }
        }    
        
        $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data:{
                            tipo:5,
                            convidado:$('#nomeAlt').val(),
                            cpf:$('#cpfAlt').val(),
                            docEst:$('#docEstAlt').val(),
                            dtNasc:$('#nascAlt').val(),
                            rg:$('#rgAlt').val(),
                            orgaoExp:$('#orgaoExpAlt').val(),
                            nrConvite:$('#nrConv').val()}
        }).success(function(retorno){
           teste.closest("tr").find("td:eq(1)").html($('#nomeAlt').val())
           teste.closest("tr").find("td:eq(3)").html($('#cpfAlt').val())
           teste.closest("tr").find("td:eq(5)").html($('#rgAlt').val())
           teste.closest("tr").find("td:eq(6)").html($('#orgaoExpAlt').val())
           teste.closest("tr").find("td:eq(2)").html($('#nascAlt').val())
           teste.closest("tr").find("td:eq(4)").html($('#docEstAlt').val())
           alert('Convidado alterado com sucesso!');
           $('#altera').hide();
        });
    }
    
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuAcesso.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Seleção de Convite</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST" onsubmit="return validarForm()">
        <input type="hidden" name="app" value="7040">
        <input type="hidden" name="acao" id="acao" value="acesso">
        <input type="hidden" name="origem" value="S">
        <input type="hidden" name="nrConv" id="nrConv" value="">
        <input type="hidden" name="tipoConv" id="tipoConv" value="">
        <input type="hidden" name="idLocal" value="${idLocal}">
        <input type="hidden" name="placa" value="${placa}">
        <input type="hidden" name="qtd" value="${qtd}">
        <input type="hidden" name="entradaSaida" value="${entradaSaida}">

        <table class="fmt" align="left" >
            <tr>
                <td>
                    <table class="fmt" align="left" >
                    <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Titulo</p>
                            <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNumero" value="${matricula}">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Nome</p>
                            <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:200px" value="${nome}">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0">Tipo de Convite</p>
                            <div class="selectheightnovo">
                                <select name="tpConvite" id="tpConvite" class="campoSemTamanho">
                                    <c:forEach var="tipoConvite" items="${tipoConvites}">
                                        <option value="${tipoConvite.cod}" <c:if test='${tpConvite == tipoConvite.cod}'>selected</c:if>>${tipoConvite.descricao}</option>
                                    </c:forEach>
                                </select>
                            </div>        
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0">Churrasqueira</p>
                            <div class="selectheightnovo">
                                <select name="idChurrasqueira" id="idChurrasqueira" class="campoSemTamanho">
                                    <c:forEach var="churrasqueira" items="${churrasqueiras}">
                                        <option value="${churrasqueira.id}" <c:if test='${idChurrasqueira == churrasqueira.id}'>selected</c:if>>${churrasqueira.descricao}</option>
                                    </c:forEach>
                                </select>
                            </div>        
                        </td>
                    </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Nr. Convite</p>
                                <input type="text" name="nrConvite" class="campoSemTamanho alturaPadrao larguraData" value="${nrConvite}">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Convidado</p>
                                <input type="text" name="convidado" class="campoSemTamanho alturaPadrao" style="width:200px" value="${convidado}">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >CPF</p>
                                <input type="text" name="cpf" class="campoSemTamanho alturaPadrao" style="width:100px" value="${cpf}">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >RG</p>
                                <input type="text" name="rg" class="campoSemTamanho alturaPadrao" style="width:100px" value="${rg}">
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Orgão Exp.</p>
                                <input type="text" name="orgaoExp" class="campoSemTamanho alturaPadrao" style="width:110px" value="${orgaoExp}">
                            </td>
                            <td >    
                                <input type="submit" class="botaobuscainclusao" style="margin-top:20px" onclick="return validarForm()" value="" title="Consultar" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2"> 
                    <input type="button" id="selecionar" name="selecionar" accessKey="s" style="margin-top:15px" class="botaoSelecionar" onclick="validaSelecao()"  value=" " />
                    <input type="button" id="voltar" name="voltar" style="margin-top:15px" class="botaoVoltar" onclick="window.location='c?app=7040&acao=showForm&idLocal=${idLocal}&placa=${placa}&qtd=${qtd}';" value=" " />
                </td>
            </tr>
        </table>
                
                    
        <table id="tabela">
            <thead>
            <tr class="odd">
                <th scope="col" align="center">Sel</th>
                <th scope="col" class="nome-lista">Convidado</th>
                <th scope="col" class="nome-lista">Dt. Nascimento</th>
                <th scope="col" class="nome-lista">CPF</th>
                <th scope="col" class="nome-lista">Doc. Estrangeiro</th>
                <th scope="col" class="nome-lista">RG</th>
                <th scope="col" class="nome-lista">Orgão Exp.</th>
                <th scope="col" class="nome-lista">Responsável</th>
                <th scope="col" align="center">Tipo Convite</th>
                <th scope="col" align="center">Emissão</th>
                <th scope="col" align="center">Validade</th>
                <th scope="col" class="nome-lista">Churrasqueira</th>
                <th scope="col" class="nome-lista">Nr. Convite</th>
                <th scope="col" align="center">Alterar</th>
            </tr>	
            </thead>
            <tbody>

            <c:forEach var="a" items="${convites}">

                <tr height="1">
                    <td class="column1" align="center">
                            <input type="radio" name="sel" value="${a.numero}" />
                    </td>
                    <td class="column1" id="noConvidado" align="left">${a.convidado}</td>
                    
                    <fmt:formatDate var="dtNascConvidado" value="${a.dtNascConvidado}" pattern="dd/MM/yyyy" />                
                    <td class="column1" id="noConvidado" align="left">${dtNascConvidado}</td>
                    <td class="column1" id="noConvidado" align="left">${a.cpfConvidado}</td>
                    <td class="column1" id="noConvidado" align="left">${a.docEstrangeiro}</td>
                    <td class="column1" id="noConvidado" align="left">${a.rgConvidado}</td>
                    <td class="column1" id="noConvidado" align="left">${a.orgaoExpConvidado}</td>
                    <td class="column1" align="left">${a.categoriaSocio}/${a.matricula} - ${a.sacador}</td>
                    <c:choose>
                        <c:when test='${a.categoriaConvite == "GR"}'><td class="column1" align="center">GERAL</td></c:when>
                        <c:when test='${a.categoriaConvite == "CH"}'><td class="column1" align="center">CHURRASQUEIRA</td></c:when>
                        <c:when test='${a.categoriaConvite == "SA"}'><td class="column1" align="center">SAUNA</td></c:when>
                        <c:when test='${a.categoriaConvite == "SI"}'><td class="column1" align="center">SINUCA</td></c:when>
                        <c:when test='${a.categoriaConvite == "ES"}'><td class="column1" align="center">ESPECIAL SÓCIO</td></c:when>
                        <c:when test='${a.categoriaConvite == "EC"}'><td class="column1" align="center">ESPECIAL CONVENIO</td></c:when>
                        <c:when test='${a.categoriaConvite == "ED"}'><td class="column1" align="center">ESPECIAL DIRETOR</td></c:when>
                        <c:when test='${a.categoriaConvite == "EV"}'><td class="column1" align="center">ESPECIAL VICE-DIRETOR</td></c:when>
                        <c:when test='${a.categoriaConvite == "EO"}'><td class="column1" align="center">ESPECIAL VICE-COMODORO</td></c:when>
                        <c:when test='${a.categoriaConvite == "EA"}'><td class="column1" align="center">ESPECIAL ASSESSOR-COMODORO</td></c:when>
                        <c:when test='${a.categoriaConvite == "EN"}'><td class="column1" align="center">ESPECIAL CONSELHEIRO</td></c:when>
                        <c:when test='${a.categoriaConvite == "EP"}'><td class="column1" align="center">ESPECIAL PRESIDENTE DO CONSELHO</td></c:when>
                        <c:when test='${a.categoriaConvite == "CO"}'><td class="column1" align="center">INSTITUCIONAL</td></c:when>
                        <c:when test='${a.categoriaConvite == "VD"}'><td class="column1" align="center">EVENTOS DIRETOR</td></c:when>
                        <c:when test='${a.categoriaConvite == "VV"}'><td class="column1" align="center">EVENTOS VICE-DIRETOR</td></c:when>
                        <c:when test='${a.categoriaConvite == "VC"}'><td class="column1" align="center">EVENTOS COMODORO</td></c:when>
                        <c:when test='${a.categoriaConvite == "VO"}'><td class="column1" align="center">EVENTOS VICE-COMODORO</td></c:when>
                        <c:when test='${a.categoriaConvite == "VA"}'><td class="column1" align="center">EVENTOS ASSESSOR-COMODORO</td></c:when>
                        <c:when test='${a.categoriaConvite == "VN"}'><td class="column1" align="center">EVENTOS CONSELHEIRO</td></c:when>
                        <c:when test='${a.categoriaConvite == "VP"}'><td class="column1" align="center">EVENTOS PRESIDENTE DO CONSELHO</td></c:when>
                        <c:when test='${a.categoriaConvite == "SP"}'><td class="column1" align="center">ESPORTIVO</td></c:when>
                        <c:otherwise><td class="column1" align="center">FAMÍLIA</td></c:otherwise>
                    </c:choose>                        
                    <fmt:formatDate var="dtRetirada" value="${a.dtRetirada}" pattern="dd/MM/yyyy" />                
                    <td class="column1" align="center">${dtRetirada}</td>
                    <fmt:formatDate var="dtLimiteUtilizacao" value="${a.dtLimiteUtilizacao}" pattern="dd/MM/yyyy" />                
                    <td class="column1" align="center">${dtLimiteUtilizacao}</td>
                    <td class="column1" align="center">
                        <c:if test='${a.nrChurrasqueira != null}'>
                            CHURRASQUEIRA - ${a.nrChurrasqueira}
                        </c:if>
                    </td>
                    <td class="column1" align="left">${a.numero}</td>
                    <td class="column1" align="center" ><img src="imagens/icones/inclusao-titular-03.png" id="imgAltera" /></td>

                </tr>	

            </c:forEach>

            </tbody>
        </table>

        <div id="altera" >
            <table style="background:#fff">
                <tr>
                  <td >
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Alteração de Convite</div>
                    <div class="divisoria"></div>
                    <table class="fmt" align="left" align="left">
                    <tr>
                        <td colspan="2">
                            <p class="legendaCodigo MargemSuperior0" >Nome</p>
                            <input type="text" name="nomeAlt" maxlength="40" id="nomeAlt" class="campoSemTamanho alturaPadrao" style="width:400px" value="">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Dt. Nascimento</p>
                            <input id="nascAlt" type="text" name="nascAlt" class="campoSemTamanho alturaPadrao larguraData" maxlength="10" value="">
                        </td>
                        <td colspan="2">
                            <p class="legendaCodigo MargemSuperior0" >CPF</p>
                            <input type="text" name="cpfAlt" maxlength="11" id="cpfAlt" class="campoSemTamanho alturaPadrao" style="width:100px" value="">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Doc. Estrangeiro</p>
                            <input id="docEstAlt" type="text" name="docEstAlt" class="campoSemTamanho alturaPadrao larguraData" maxlength="30" value="">
                        </td>
                        <td colspan="2">
                            <p class="legendaCodigo MargemSuperior0" >RG</p>
                            <input type="text" name="rgAlt" maxlength="10" id="rgAlt" class="campoSemTamanho alturaPadrao" style="width:100px" value="">
                        </td>
                        <td colspan="2">
                            <p class="legendaCodigo MargemSuperior0" >Orgão Exp.</p>
                            <input type="text" name="orgaoExpAlt" maxlength="15" id="orgaoExpAlt" class="campoSemTamanho alturaPadrao" style="width:100px" value="">
                        </td>
                    </tr>
                    <tr>
                      <td align="left">
                          <br>
                          <input type="button" id="cmdPesquisa" name="cmdPesquisa" class="botaoatualizar" style="margin-top:5px;" value=" " onclick="atualizaAlteracao()" value="" />
                      </td>
                      <td align="left">
                          <br>
                          <input type="button" id="cmdPesquisa" name="cmdPesquisa" class="botaocancelar" value=" " onclick="fechaAlteracao()" value="" />
                      </td>
                    </tr>
                    </table>  
                </td>
              </tr>
            <table>
        </div>        
                
                
    </form>
        
</body>
</html>
