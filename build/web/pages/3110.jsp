<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
        $("#dataNascimento").mask("99/99/9999");
        $("#dataAdmissao").mask("99/99/9999");
        $("#dataEntregaBrinde").mask("99/99/9999");

        $("#cepR").mask("99.999-999");
        $("#cepC").mask("99.999-999");
        
        if ($('input[name="tipoPessoa"]:checked').val() == 'F'){
            $("#cpfCnpj").mask("999.999.999-99");
        }else{
            $("#cpfCnpj").mask("99.999.999/9999-99");
        }
        
        $("#tipoPessoaF").live("click", mascaraCPF);
        $("#tipoPessoaJ").live("click", mascaraCNPJ);

        function mascaraCPF() {
            $("#cpfCnpj").mask("999.999.999-99");
        }
        function mascaraCNPJ() {
            $("#cpfCnpj").mask("99.999.999/9999-99");
        }


    });     
</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "9031"}'>
        <div id="titulo-subnav">Inclusão de Titular - Dados Pessoais</div>
    </c:if>
    <c:if test='${app == "9032"}'>
        <div id="titulo-subnav">Alteração de Titular - Dados Pessoais</div>
    </c:if>
    <c:if test='${app == "9030"}'>
        <div id="titulo-subnav">Detalhes do Titular - Dados Pessoais</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].matricula.value == ''
                || document.forms[0].nome.value == ''
                || document.forms[0].dataNascimento.value == ''){
                alert('Para inclusão, informe a Matrícula, Nome e Data de Nascimento');
                return;
            }
            if(!isDataValida(document.forms[0].dataNascimento.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataAdmissao.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataEntregaBrinde.value)){
                return;
            }

            $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data: {tipo:2,nome:$('#nome').val()}
            }).success(function(retorno){
                var ret = 0;
                ret = retorno.substring(0,1);
                if (ret > 0){
                    if (ret == 1){
                        if (confirm('Foi encontrada uma pessoa com este mesmo nome na Lista Negra. Deseja Continuar?') == false){
                            return;
                        }
                    }else {
                        if (confirm('Foi encontrada uma pessoa iniciando com ' + retorno.substring(1) + ' na Lista Negra. Deseja Continuar?') == false){
                            if (confirm('Deseja verificar o cadastro da Lista Negra?')){
                                window.open('c?app=2140','page','toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=600,height=600'); 
                            }
                            return;
                        }
                    }
                }
            });

            document.forms[0].submit();

        }
        function carregaProponente(){
            var msgRetorno = '';
            $.ajax({url:'SocioAjax', async:false, dataType:'text', type:'GET',data:{
                                tipo:1,
                                matricula:$('#proponenteMatricula').val(),
                                categoria:$('#proponenteCategoria').val()}
            }).success(function(retorno){
                $('#proponenteNome').val(retorno);
            });

        }

    </script>

    <form  action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">

        <table align="left" >
            <tr>
                <td rowspan="4" >
                    <img src="f?tb=6&mat=${titular.socio.matricula}&seq=0&cat=${titular.socio.idCategoria}" onerror="this.src='images/tenis/avatar-default.png';" class="recuoPadrao MargemSuperiorPadrao" width="120" height="160"><BR>
                </td>    
                <td>
                    &nbsp
                </td>    
            </tr>    
            
            <tr>
                <td>
                    <table align="left">
                      <tr>
                        <td width="20%">
                            <p class="legendaCodigo MargemSuperior0">Matricula:</p>
                            <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNumero" <c:if test='${app == "9032" || app == "9042"}'>readonly</c:if> onkeypress="onlyNumber(event)" <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.socio.matricula}">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0">Categoria:</p>
                            <select name="idCategoria" class="campoSemTamanho alturaPadrao larguraComboCategoria" <c:if test='${app == "9032" || app == "9042"}'>disabled</c:if>  <c:if test='${app == "9030"}'>disabled</c:if> >
                                <c:forEach var="categoria" items="${categorias}">
                                    <option value="${categoria.id}" <c:if test='${titular.socio.idCategoria == categoria.id}'>selected</c:if>>${categoria.descricao}</option>
                                </c:forEach>
                            </select>
                        </td>
                      </tr>
                    </table>
                </td>
            </tr>
            <tr>
              <td>
                 <table align="left">
                    <tr>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Nome:</p>
                            <input type="text" id="nome" name="nome" class="campoSemTamanho alturaPadrao" style="width: 310px" maxlength="40" <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.socio.nome}">
                      </td>
                      <td>
                        <br>
                        <input type="checkbox" name="espolio"  <c:if test='${app == "9030"}'>disabled</c:if>  value="1" <c:if test='${titular.espolio}'>checked</c:if>><spam class="legendaSemMargem larguraData"> Espólio</spam>                          
                      </td>
                    </tr>
                 </table>
              </td>
            </tr>
            <tr>
              <td>
                 <table align="left">
                    <tr>
                      <td>
                          <br>
                          <fieldset class="field-set legendaFrame recuoPadrao larguraFrameTipoPessoa"  <c:if test='${app == "9030"}'>disabled</c:if> >
                              <legend >Tipo</legend>

                              <input type="radio" name="tipoPessoa" id="tipoPessoaF" class="legendaCodigo" value="F" <c:if test='${titular.pessoaFisica}'>checked</c:if>>Física
                              <input type="radio" name="tipoPessoa" id="tipoPessoaJ" class="legendaCodigo" value="J" <c:if test='${!titular.pessoaFisica}'>checked</c:if>>Jurídica

                          </fieldset>
                      </td>
                      <td>
                          <br>
                          <fieldset class="field-set legendaFrame recuoPadrao larguraFrameTipoSexo"  <c:if test='${app == "9030"}'>disabled</c:if> >
                              <legend >Sexo</legend>

                              <input type="radio" name="sexo" class="legendaCodigo" value="M" <c:if test='${titular.socio.masculino}'>checked</c:if>>M
                              <input type="radio" name="sexo" class="legendaCodigo" value="F" <c:if test='${!titular.socio.masculino}'>checked</c:if>>F


                          </fieldset>
                      </td>
                      <td>
                        <p class="legendaCodigo" >Dt. Nascimento</p>
                        <fmt:formatDate var="dataNascimento" value="${titular.socio.dataNascimento}" pattern="dd/MM/yyyy"/>
                        <input type="text" name="dataNascimento" id="dataNascimento" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "9030"}'>disabled</c:if> value="${dataNascimento}">

                      </td>
                    </tr>
                </table>  
              </td>
            </tr>
            <tr>
                <td colspan="2">
                 <table align="left">
                    <tr>
                      <td>
                        <c:if test='${titular.pessoaFisica}'>
                            <p class="legendaCodigo MargemSuperior0" >CPF</p>  
                            <fmt:formatNumber var="cpfCnpj"  value="${titular.cpfCnpj}" pattern="00000000000"/>

                        </c:if>
                        <c:if test='${!titular.pessoaFisica}'>
                            <fmt:formatNumber var="cpfCnpj"  value="${titular.cpfCnpj}" pattern="00000000000000"/>
                            <p class="legendaCodigo MargemSuperior0" >CNPJ</p>
                        </c:if>
                            
                        <input type="text" name="cpfCnpj" id="cpfCnpj" class="campoSemTamanho alturaPadrao larguraData" onkeypress="onlyNumber(event)"  <c:if test='${app == "9030"}'>disabled</c:if> value="${cpfCnpj}"><br>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >RG</p>
                        <input type="text" name="rg" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.RG}"><br>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Estado Civil</p>
                        <select name="estadoCivil" class="campoSemTamanho alturaPadrao larguraComboEstadoCivil"  <c:if test='${app == "9030"}'>disabled</c:if> >
                            <option value="US" <c:if test='${titular.estadoCivil == ""}'>selected</c:if>>NÃO INFORMADO</option>
                            <option value="CA" <c:if test='${titular.estadoCivil == "CA"}'>selected</c:if>>Casado(a)</option>
                            <option value="DI" <c:if test='${titular.estadoCivil == "DI"}'>selected</c:if>>Divorciado(a)</option>
                            <option value="DE" <c:if test='${titular.estadoCivil == "DE"}'>selected</c:if>>Desquitado(a)</option>
                            <option value="OU" <c:if test='${titular.estadoCivil == "OU"}'>selected</c:if>>Outro</option>
                            <option value="SO" <c:if test='${titular.estadoCivil == "SO"}'>selected</c:if>>Solteiro(a)</option>
                            <option value="VI" <c:if test='${titular.estadoCivil == "VI"}'>selected</c:if>>Viúvo(a)</option>
                        </select>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Situação</p>
                        <select name="situacao" class="campoSemTamanho alturaPadrao larguraComboEstadoCivil"  <c:if test='${app == "9030"}'>disabled</c:if> >
                            <option value="NO" <c:if test='${titular.socio.situacao == "NO"}'>selected</c:if>>Normal</option>
                            <option value="SA" <c:if test='${titular.socio.situacao == "SA"}'>selected</c:if>>Sem admissão</option>
                            <option value="EM" <c:if test='${titular.socio.situacao == "EM"}'>selected</c:if>>Em admissão</option>
                            <option value="NA" <c:if test='${titular.socio.situacao == "NA"}'>selected</c:if>>Não admitido</option>
                            <option value="RE" <c:if test='${titular.socio.situacao == "RE"}'>selected</c:if>>Retomado</option>
                            <option value="SU" <c:if test='${titular.socio.situacao == "SU"}'>selected</c:if>>Suspenso</option>
                            <option value="US" <c:if test='${titular.socio.situacao == "US"}'>selected</c:if>>Tit. C/ Usuário</option>
                        </select>
                      </td>
                    </tr>
                </table>  
            </td>
          </tr>
            <tr>
                <td colspan="2">
                 <table align="left">
                    <tr>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Em@il</p>
                        <input type="text" name="email" class="campoSemTamanho alturaPadrao larguraEmail"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.email}">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Naturalidade</p>
                        <input type="text" name="naturalidade" class="campoSemTamanho alturaPadrao larguraEmail"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.naturalidade}">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Nacionalidade</p>
                        <input type="text" name="nacionalidade" class="campoSemTamanho alturaPadrao larguraEmail"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.nacionalidade}"><br>
                      </td>
                    </tr>
                </table>  
            </td>
          </tr>
          <tr>
                <td colspan="2">
                 <table align="left">
                    <tr>
                      <td width="164px">
                        <p class="legendaCodigo MargemSuperior0" >Profissão</p>
                        <select name="idProfissao" class="campoSemTamanho alturaPadrao larguraComboEstadoCivil"  <c:if test='${app == "9030"}'>disabled</c:if> >
                            <option value="0" <c:if test='${titular.idProfissao == 0}'>selected</c:if>>Não informado</option>
                            <c:forEach var="profissao" items="${profissoes}">
                                <option value="${profissao.id}" <c:if test='${titular.idProfissao == profissao.id}'>selected</c:if>>${profissao.descricao}</option>
                            </c:forEach>
                        </select>
                      </td>
                      <td width="150px">
                        <p class="legendaCodigo MargemSuperior0" >Cargo Especial</p>
                        <select name="idCargoEspecial" class="campoSemTamanho alturaPadrao larguraComboEstadoCivil"  <c:if test='${app == "9030"}'>disabled</c:if> >
                            <option value="0" <c:if test='${titular.idCargoEspecial == 0}'>selected</c:if>>Nenhum</option>
                            <c:forEach var="cargo" items="${cargos}">
                                <option value="${cargo.id}" <c:if test='${titular.idCargoEspecial == cargo.id}'>selected</c:if>>${cargo.descricao}</option>
                            </c:forEach>
                        </select>
                      </td>
                      <td width="100px">
                        <br>
                        <input type="checkbox" name="cargoAtivo"  <c:if test='${app == "9030"}'>disabled</c:if>  value="1" <c:if test='${titular.cargoAtivo}'>checked</c:if>><spam class="legendaSemMargem larguraData"> Cargo Ativo</spam>
                      </td>
                      <td width="150px">
                        <p class="legendaCodigo MargemSuperior0" >Data Admissão</p>
                        <fmt:formatDate var="dataAdmissao" value="${titular.dataAdmissao}" pattern="dd/MM/yyyy"/>                        
                        <input type="text" name="dataAdmissao" id="dataAdmissao" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "9030"}'>disabled</c:if> value="${dataAdmissao}">
                      </td>
                    </tr>
                </table>  
            </td>
          </tr>
          <tr>
                <td colspan="2">
                 <table align="left">
                    <tr>
                      <td width="164px">
                        <p class="legendaCodigo MargemSuperior0" >Pai</p>
                        <input type="text" name="nomePai" class="campoSemTamanho alturaPadrao larguraPaiMae"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.nomePai}">
                      </td>
                      <td width="164px">
                        <p class="legendaCodigo MargemSuperior0" >Mãe</p>
                        <input type="text" name="nomeMae" class="campoSemTamanho alturaPadrao larguraPaiMae"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.nomeMae}">
                      </td>
                    </tr>
                </table>  
            </td>
          </tr>
          <tr>
              <td colspan="2">
                <fieldset class="field-set legendaFrame recuoPadrao larguraFrameProponente" >
                  <legend >Proponente</legend>

                  <table align="left">
                    <tr>
                      <td width="164px">
                        <p class="legendaCodigo MargemSuperior0" >Cat.</p>
                        <input type="text" name="proponenteCategoria" id="proponenteCategoria" class="campoSemTamanho alturaPadrao larguraNumeroPequeno"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.proponente.idCategoria}">
                      </td>
                      <td width="164px">
                        <p class="legendaCodigo MargemSuperior0" >Tit.</p>
                        <input type="text" name="proponenteMatricula" id="proponenteMatricula" class="campoSemTamanho alturaPadrao larguraNumeroPequeno"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.proponente.matricula}">
                      </td>
                      <td width="164px">
                        <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="carregaProponente()" value="" title="Consultar" />
                      </td>
                      <td width="164px">
                        <p class="legendaCodigo MargemSuperior0" >Nome</p>
                        <input type="text" name="proponenteNome" id="proponenteNome" class="campoSemTamanho alturaPadrao " style="width:340px; " <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.proponente.nome}">
                      </td>
                      <td width="164px">
                          &nbsp;
                      </td>

                    </tr>
                </table>  
              </fieldset>
            </td>
          </tr>
        </table>          
          
        <BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Endereços</div>
        <div class="divisoria"></div>


        <table align="left" >
            <tr>
              <td>
                 <table align="left">
                    <tr>
                      <td>
                          <fieldset class="field-set legendaFrame recuoPadrao alturaPadraoFrame" style="width: 248px;"  <c:if test='${app == "9030"}'>disabled</c:if> >
                              <legend >End. Recebimento de Correspondência</legend>
                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                              <input type="radio" name="destinoCorrespondencia" value="R" <c:if test='${titular.destinoCorrespondencia != "C"}'>checked</c:if>>Residencial
                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                              <input type="radio" name="destinoCorrespondencia" value="C" <c:if test='${titular.destinoCorrespondencia == "C"}'>checked</c:if>>Comercial
                              

                          </fieldset>
                      </td>
                      <td>
                          <fieldset class="field-set legendaFrame recuoPadrao alturaPadraoFrame" style="width: 248px;"  <c:if test='${app == "9030"}'>disabled</c:if> >
                              <legend >End. Recebimento de Carnê</legend>
                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                              <input type="radio" name="destinoCarne" value="R" <c:if test='${titular.destinoCarne != "C"}'>checked</c:if>>Residencial
                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                              <input type="radio" name="destinoCarne" value="C" <c:if test='${titular.destinoCarne == "C"}'>checked</c:if>>Comercial

                          </fieldset>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="2">
                          &nbsp;
                      </td>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table align="left" >
                            <tr>
                              <td>
                                <c:if test='${app != "9039" && app != "9049"}'>
                                    <p class="legendaCodigo MargemSuperior0" >Tel. Celular</p>
                                    <input type="text" name="telefoneCelular" class="campoSemTamanho alturaPadrao" style="width: 160px;"  <c:if test='${app == "9030"}'>disabled</c:if>  value="${titular.telefoneCelular}"><br>
                                </c:if>
                              </td>
                              <td>
                                <c:if test='${app != "9039" && app != "9049"}'>
                                    <p class="legendaCodigo MargemSuperior0" >Tel. Alternativo</p>
                                    <input type="text" name="telefoneAlternativo" class="campoSemTamanho alturaPadrao" style="width: 160px;"  <c:if test='${app == "9030"}'>disabled</c:if>  value="${titular.telefoneAlternativo}"><br>
                                </c:if>
                              </td>
                              <td>
                                <c:if test='${app != "9039" && app != "9049"}'>
                                    <p class="legendaCodigo MargemSuperior0" >Fax</p>
                                    <input type="text" name="fax" class="campoSemTamanho alturaPadrao" style="width: 160px;"  <c:if test='${app == "9030"}'>disabled</c:if>  value="${titular.fax}"><br>
                                </c:if>
                              </td>
                            </tr>
                        </table>
                      </td>
                    </tr>
                    
                </table>  
              </td>
            </tr>
        </table>          

        <BR><BR><BR><BR><BR><BR><br><br>
                              
        <div class="divisoria"></div>
        <div id="titulo-subnav">Endereço Residêncial </div>
        <div class="divisoria"></div>


        <table align="left" >
            <tr>
              <td>
                 <table align="left">
                    <tr>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Endereço</p>
                        <input type="text" class="campoSemTamanho alturaPadrao larguraNomeProponente" name="enderecoR"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.enderecoResidencial.endereco}">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Bairro</p>
                        <input type="text" class="campoSemTamanho alturaPadrao larguraBairro" name="bairroR"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.enderecoResidencial.bairro}">
                      </td>
                    </tr>
                 </table>
                </td>
            </tr>
            <tr>
              <td>
                 <table align="left">                      
                    <tr>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Cidade</p>
                        <input type="text" name="cidadeR" class="campoSemTamanho alturaPadrao larguraCidade"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.enderecoResidencial.cidade}">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >UF</p>
                        <select name="ufR" class="campoSemTamanho alturaPadrao larguraUF"  <c:if test='${app == "9030"}'>disabled</c:if> >
                            <option value="AC" <c:if test='${titular.enderecoResidencial.UF == "AC"}'>selected</c:if>>AC</option>
                            <option value="AL" <c:if test='${titular.enderecoResidencial.UF == "AL"}'>selected</c:if>>AL</option>
                            <option value="AM" <c:if test='${titular.enderecoResidencial.UF == "AM"}'>selected</c:if>>AM</option>
                            <option value="AP" <c:if test='${titular.enderecoResidencial.UF == "AP"}'>selected</c:if>>AP</option>
                            <option value="BA" <c:if test='${titular.enderecoResidencial.UF == "BA"}'>selected</c:if>>BA</option>
                            <option value="CE" <c:if test='${titular.enderecoResidencial.UF == "CE"}'>selected</c:if>>CE</option>
                            <option value="DF" <c:if test='${titular.enderecoResidencial.UF == "DF"}'>selected</c:if>>DF</option>
                            <option value="ES" <c:if test='${titular.enderecoResidencial.UF == "ES"}'>selected</c:if>>ES</option>
                            <option value="GO" <c:if test='${titular.enderecoResidencial.UF == "GO"}'>selected</c:if>>GO</option>
                            <option value="MA" <c:if test='${titular.enderecoResidencial.UF == "MA"}'>selected</c:if>>MA</option>
                            <option value="MG" <c:if test='${titular.enderecoResidencial.UF == "MG"}'>selected</c:if>>MG</option>
                            <option value="MS" <c:if test='${titular.enderecoResidencial.UF == "MS"}'>selected</c:if>>MS</option>
                            <option value="MT" <c:if test='${titular.enderecoResidencial.UF == "MT"}'>selected</c:if>>MT</option>
                            <option value="PA" <c:if test='${titular.enderecoResidencial.UF == "PA"}'>selected</c:if>>PA</option>
                            <option value="PB" <c:if test='${titular.enderecoResidencial.UF == "PB"}'>selected</c:if>>PB</option>
                            <option value="PE" <c:if test='${titular.enderecoResidencial.UF == "PE"}'>selected</c:if>>PE</option>
                            <option value="PI" <c:if test='${titular.enderecoResidencial.UF == "PI"}'>selected</c:if>>PI</option>
                            <option value="PR" <c:if test='${titular.enderecoResidencial.UF == "PR"}'>selected</c:if>>PR</option>
                            <option value="RJ" <c:if test='${titular.enderecoResidencial.UF == "RJ"}'>selected</c:if>>RJ</option>
                            <option value="RN" <c:if test='${titular.enderecoResidencial.UF == "RN"}'>selected</c:if>>RN</option>
                            <option value="RO" <c:if test='${titular.enderecoResidencial.UF == "RO"}'>selected</c:if>>RO</option>
                            <option value="RR" <c:if test='${titular.enderecoResidencial.UF == "RR"}'>selected</c:if>>RR</option>
                            <option value="RS" <c:if test='${titular.enderecoResidencial.UF == "RS"}'>selected</c:if>>RS</option>
                            <option value="SC" <c:if test='${titular.enderecoResidencial.UF == "SC"}'>selected</c:if>>SC</option>
                            <option value="SE" <c:if test='${titular.enderecoResidencial.UF == "SE"}'>selected</c:if>>SE</option>
                            <option value="SP" <c:if test='${titular.enderecoResidencial.UF == "SP"}'>selected</c:if>>SP</option>
                            <option value="TO" <c:if test='${titular.enderecoResidencial.UF == "TO"}'>selected</c:if>>TO</option>
                        </select>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >CEP</p>
                        <input type="text" name="cepR" id="cepR" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.enderecoResidencial.CEP}">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Telefone</p>
                        <input type="text" name="telefoneR" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "9030"}'>disabled</c:if>  value="${titular.enderecoResidencial.telefone}">
                      </td>
                      
                    </tr>
                </table>  
              </td>
            </tr>
        </table>          

        <BR><BR><BR><BR><BR><BR><BR>
                              
        <div class="divisoria"></div>
        <div id="titulo-subnav">Endereço Comercial </div>
        <div class="divisoria"></div>


        <table align="left" >
            <tr>
              <td>
                 <table align="left">
                    <tr>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Endereço</p>
                        <input type="text" class="campoSemTamanho alturaPadrao larguraNomeProponente" name="enderecoC"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.enderecoComercial.endereco}">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Bairro</p>
                        <input type="text" class="campoSemTamanho alturaPadrao larguraBairro" name="bairroC"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.enderecoComercial.bairro}">
                      </td>
                    </tr>
                 </table>
                </td>
            </tr>
            <tr>
              <td>
                 <table align="left">                      
                    <tr>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Cidade</p>
                        <input type="text" name="cidadeC" class="campoSemTamanho alturaPadrao larguraCidade"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.enderecoComercial.cidade}">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >UF</p>
                        <select name="ufC" class="campoSemTamanho alturaPadrao larguraUF"  <c:if test='${app == "9030"}'>disabled</c:if> >
                            <option value="AC" <c:if test='${titular.enderecoComercial.UF == "AC"}'>selected</c:if>>AC</option>
                            <option value="AL" <c:if test='${titular.enderecoComercial.UF == "AL"}'>selected</c:if>>AL</option>
                            <option value="AM" <c:if test='${titular.enderecoComercial.UF == "AM"}'>selected</c:if>>AM</option>
                            <option value="AP" <c:if test='${titular.enderecoComercial.UF == "AP"}'>selected</c:if>>AP</option>
                            <option value="BA" <c:if test='${titular.enderecoComercial.UF == "BA"}'>selected</c:if>>BA</option>
                            <option value="CE" <c:if test='${titular.enderecoComercial.UF == "CE"}'>selected</c:if>>CE</option>
                            <option value="DF" <c:if test='${titular.enderecoComercial.UF == "DF"}'>selected</c:if>>DF</option>
                            <option value="ES" <c:if test='${titular.enderecoComercial.UF == "ES"}'>selected</c:if>>ES</option>
                            <option value="GO" <c:if test='${titular.enderecoComercial.UF == "GO"}'>selected</c:if>>GO</option>
                            <option value="MA" <c:if test='${titular.enderecoComercial.UF == "MA"}'>selected</c:if>>MA</option>
                            <option value="MG" <c:if test='${titular.enderecoComercial.UF == "MG"}'>selected</c:if>>MG</option>
                            <option value="MS" <c:if test='${titular.enderecoComercial.UF == "MS"}'>selected</c:if>>MS</option>
                            <option value="MT" <c:if test='${titular.enderecoComercial.UF == "MT"}'>selected</c:if>>MT</option>
                            <option value="PA" <c:if test='${titular.enderecoComercial.UF == "PA"}'>selected</c:if>>PA</option>
                            <option value="PB" <c:if test='${titular.enderecoComercial.UF == "PB"}'>selected</c:if>>PB</option>
                            <option value="PE" <c:if test='${titular.enderecoComercial.UF == "PE"}'>selected</c:if>>PE</option>
                            <option value="PI" <c:if test='${titular.enderecoComercial.UF == "PI"}'>selected</c:if>>PI</option>
                            <option value="PR" <c:if test='${titular.enderecoComercial.UF == "PR"}'>selected</c:if>>PR</option>
                            <option value="RJ" <c:if test='${titular.enderecoComercial.UF == "RJ"}'>selected</c:if>>RJ</option>
                            <option value="RN" <c:if test='${titular.enderecoComercial.UF == "RN"}'>selected</c:if>>RN</option>
                            <option value="RO" <c:if test='${titular.enderecoComercial.UF == "RO"}'>selected</c:if>>RO</option>
                            <option value="RR" <c:if test='${titular.enderecoComercial.UF == "RR"}'>selected</c:if>>RR</option>
                            <option value="RS" <c:if test='${titular.enderecoComercial.UF == "RS"}'>selected</c:if>>RS</option>
                            <option value="SC" <c:if test='${titular.enderecoComercial.UF == "SC"}'>selected</c:if>>SC</option>
                            <option value="SE" <c:if test='${titular.enderecoComercial.UF == "SE"}'>selected</c:if>>SE</option>
                            <option value="SP" <c:if test='${titular.enderecoComercial.UF == "SP"}'>selected</c:if>>SP</option>
                            <option value="TO" <c:if test='${titular.enderecoComercial.UF == "TO"}'>selected</c:if>>TO</option>
                        </select>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >CEP</p>
                        <input type="text" name="cepC" id="cepC" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.enderecoComercial.CEP}">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Telefone</p>
                        <input type="text" name="telefoneC" class="campoSemTamanho alturaPadrao larguraData"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.enderecoComercial.telefone}">
                      </td>
                      
                    </tr>
                </table>  
              </td>
            </tr>
        </table>          
                      
        <BR><BR><BR><BR><BR><BR><BR>
        <div class="divisoria"></div>
        <div id="titulo-subnav">Débito em Conta</div>
        <div class="divisoria"></div>
        
        
        <table align="left" >
            <tr>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Agência</p>
                <input type="text" name="agencia" class="campoSemTamanho alturaPadrao larguraTelefone" onkeypress="onlyNumber(event)"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.contaCorrente.agencia}">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >DV</p>
                <input type="text" name="digitoAgencia" class="campoSemTamanho alturaPadrao larguraNumeroPequeno"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.contaCorrente.digitoAgencia}">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Conta</p>
                <input type="text" name="conta" class="campoSemTamanho alturaPadrao larguraTelefone" onkeypress="onlyNumber(event)"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.contaCorrente.conta}">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >DV</p>
                <input type="text" name="digitoConta" class="campoSemTamanho alturaPadrao larguraNumeroPequeno"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.contaCorrente.digitoConta}">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Titular</p>
                <input type="text" name="titular" class="campoSemTamanho alturaPadrao larguraTitular"  <c:if test='${app == "9030"}'>disabled</c:if> value="${titular.contaCorrente.titular}">
              </td>
            </tr>
        </table>          
        
        
        
        <BR><BR><BR><BR>
        <div class="divisoria"></div>
        <div id="titulo-subnav">Parâmetros</div>
        <div class="divisoria"></div>
        <BR>

        <input type="checkbox" class="legendaCodigo MargemSuperior0" name="ignorarPagamentos"  <c:if test='${app == "9030"}'>disabled</c:if> value="1" <c:if test='${titular.ignorarPagamentos}'>checked</c:if>><spam class="legendaSemMargem larguraData">Ignorar Pagamentos</spam>
        <br>
        <input type="checkbox" class="legendaCodigo MargemSuperior0" name="bloquearEmissaoConvites"  <c:if test='${app == "9030"}'>disabled</c:if> value="1" <c:if test='${titular.bloquearEmissaoConvites}'>checked</c:if>><spam class="legendaSemMargem larguraData">Não permitir emissão de convites</spam>
        <br>
        <input type="checkbox" class="legendaCodigo MargemSuperior0" name="bloquearReservaChurrasqueira"  <c:if test='${app == "9030"}'>disabled</c:if> value="1" <c:if test='${titular.bloquearReservaChurrasqueira}'>checked</c:if>><spam class="legendaSemMargem larguraData">Não permitir reserva de churrasqueira</spam>
        <br>
        <input type="checkbox" class="legendaCodigo MargemSuperior0" name="bloquearCadastroEmbarcacoes"  <c:if test='${app == "9030"}'>disabled</c:if> value="1" <c:if test='${titular.bloquearCadastroEmbarcacoes}'>checked</c:if>><spam class="legendaSemMargem larguraData">Não permitir cadastro de embarcações</spam>
        <br>
        <input type="checkbox" class="legendaCodigo MargemSuperior0" name="bloquearMatriculas"  <c:if test='${app == "9030"}'>disabled</c:if> value="1" <c:if test='${titular.bloquearMatriculas}'>checked</c:if>><spam class="legendaSemMargem larguraData">Não permitir matrículas em escolinhas</spam>
        <br>
        <input type="checkbox" class="legendaCodigo MargemSuperior0" name="bloquearEmprestimoMaterial"  <c:if test='${app == "9030"}'>disabled</c:if> value="1" <c:if test='${titular.bloquearEmprestimoMaterial}'>checked</c:if>><spam class="legendaSemMargem larguraData">Não permitir emprestimo de material</spam>
        <br>
        

        <br>
        <p class="legendaCodigo MargemSuperior0" >Vr Máx. Tx. Ind e Cheque</p>
        <fmt:formatNumber value="${titular.maximoTxIndCheque}" maxFractionDigits="2" minFractionDigits="2" var="maximoTxIndCheque"/>
        <input type="text" class="campoSemTamanho alturaPadrao" name="maximoTxIndCheque"  <c:if test='${app == "9030"}'>disabled</c:if> value="${maximoTxIndCheque}"><br>    

        <br>
        <p class="legendaCodigo MargemSuperior0" >Envio Boleto</p>
        
        <select name="tipoEnvioBoleto" class="campoSemTamanho alturaPadrao"  <c:if test='${app == "9030"}'>disabled</c:if> >
            <option value="zero" <c:if test='${titular.tipoEnvioBoleto == ""}'>selected</c:if>>Nao Informado</option>
            <option value="B" <c:if test='${titular.tipoEnvioBoleto == "B"}'>selected</c:if>>Somente por Correspondência</option>
            <option value="C" <c:if test='${titular.tipoEnvioBoleto == "C"}'>selected</c:if>>Somente por Email</option>
            <option value="A" <c:if test='${titular.tipoEnvioBoleto == "A"}'>selected</c:if>>Correspondência e Email</option>
        </select>
        <br>
        
        <br>
        <p class="legendaCodigo MargemSuperior0" >Data de entrega do brinde</p>
        <fmt:formatDate var="dataEntregaBrinde" value="${titular.dataEntregaBrinde}" pattern="dd/MM/yyyy"/>
        <input type="text" class="campoSemTamanho alturaPadrao" name="dataEntregaBrinde" id="dataEntregaBrinde"  name="dataEntregaBrinde" <c:if test='${app == "9030"}'>disabled</c:if> value="${dataEntregaBrinde}">
        <br>
        
        <br>
        <p class="legendaCodigo MargemSuperior0" >Data última atualização internet</p>
        <fmt:formatDate var="dataAtualizacaoInternet" value="${titular.dataAtualizacaoInternet}" pattern="dd/MM/yyyy"/>
         <div class="campoSemTamanhoSemBorda">${dataAtualizacaoInternet}</div><BR><br>
        
         <c:if test='${app != "9030"}'>
             <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
         </c:if> 
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${titular.socio.matricula}&categoria=${titular.socio.idCategoria}';" value=" " />

    </form>

</body>
</html>
