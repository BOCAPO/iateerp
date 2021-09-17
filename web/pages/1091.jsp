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
    <div id="titulo-subnav">Transferência de Títulos</div>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].matricula.value == ''
                || document.forms[0].nome.value == ''
                || document.forms[0].dataNascimento.value == ''){
                alert('Para transferência, informe a Matrícula, Nome e Data de Nascimento');
                return;
            }
            if(!isDataValida(document.forms[0].dataNascimento.value)){
                return;
            }

            document.forms[0].submit();

        }

    </script>

    <form  action="c" method="POST">
        <input type="hidden" name="app" value="1091">
        <input type="hidden" name="acao" value="gravar">

        <table align="left" >           
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Matricula:</p>
                    <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNumero" readonly value="${matricula}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Categoria:</p>
                    <select name="idCategoria" class="campoSemTamanho alturaPadrao" disabled style="width: 213px">
                        <c:forEach var="categoria" items="${categorias}">
                            <option value="${categoria.id}" <c:if test='${idCategoria == categoria.id}'>selected</c:if>>${categoria.descricao}</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Dt. Nascimento</p>
                  <input type="text" name="dataNascimento" id="dataNascimento" class="campoSemTamanho alturaPadrao larguraData">

                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width: 90px">
                        <legend>Sexo</legend>

                        <input type="radio" name="sexo" class="legendaCodigo" checked value="M">M
                        <input type="radio" name="sexo" class="legendaCodigo" value="F">F


                    </fieldset>
                </td>
            </tr>
        </table>  
        <br><br><br>
        <table align="left" >           
            <tr>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Nome:</p>
                <input type="text" id="nome" name="nome" class="campoSemTamanho alturaPadrao" style="width: 345px">
              </td>
              <td>
                <fieldset class="field-set legendaFrame recuoPadrao larguraFrameTipoPessoa">
                    <legend >Tipo</legend>

                    <input type="radio" name="tipoPessoa" id="tipoPessoaF" checked class="legendaCodigo" value="F">Física
                    <input type="radio" name="tipoPessoa" id="tipoPessoaJ" class="legendaCodigo" value="J">Jurídica

                </fieldset>
              </td>
            </tr>
        </table>        
        <br><br><br>        
        <table align="left" >           
            <tr>
                <td colspan="4">
                 <table align="left">
                    <tr>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >CPF</p>
                        <input type="text" name="cpfCnpj" id="cpfCnpj" class="campoSemTamanho alturaPadrao larguraData" onkeypress="onlyNumber(event)"><br>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >RG</p>
                        <input type="text" name="rg" class="campoSemTamanho alturaPadrao larguraData"><br>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Estado Civil</p>
                        <select name="estadoCivil" class="campoSemTamanho alturaPadrao larguraComboEstadoCivil">
                            <option value="US">NÃO INFORMADO</option>
                            <option value="CA">Casado(a)</option>
                            <option value="DI">Divorciado(a)</option>
                            <option value="DE">Desquitado(a)</option>
                            <option value="OU">Outro</option>
                            <option value="SO">Solteiro(a)</option>
                            <option value="VI">Viúvo(a)</option>
                        </select>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Situação</p>
                        <select name="situacao" class="campoSemTamanho alturaPadrao larguraComboEstadoCivil">
                            <option value="NO">Normal</option>
                            <option value="SA">Sem admissão</option>
                            <option value="EM">Em admissão</option>
                            <option value="NA">Não admitido</option>
                            <option value="RE">Retomado</option>
                            <option value="SU">Suspenso</option>
                            <option value="US">Tit. C/ Usuário</option>
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
                        <input type="text" name="email" class="campoSemTamanho alturaPadrao larguraEmail">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Naturalidade</p>
                        <input type="text" name="naturalidade" class="campoSemTamanho alturaPadrao larguraEmail">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Nacionalidade</p>
                        <input type="text" name="nacionalidade" class="campoSemTamanho alturaPadrao larguraEmail"><br>
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
                        <select name="idProfissao" class="campoSemTamanho alturaPadrao larguraComboEstadoCivil">
                            <option value="0">Não informado</option>
                            <c:forEach var="profissao" items="${profissoes}">
                                <option value="${profissao.id}">${profissao.descricao}</option>
                            </c:forEach>
                        </select>
                      </td>
                      <td width="150px">
                        <p class="legendaCodigo MargemSuperior0" >Cargo Especial</p>
                        <select name="idCargoEspecial" class="campoSemTamanho alturaPadrao larguraComboEstadoCivil">
                            <option value="0">Nenhum</option>
                            <c:forEach var="cargo" items="${cargos}">
                                <option value="${cargo.id}">${cargo.descricao}</option>
                            </c:forEach>
                        </select>
                      </td>
                      <td width="100px">
                        <br>
                        <input type="checkbox" width="100px" name="cargoAtivo" value="1"><spam class="legendaSemMargem larguraData"> Cargo Ativo</spam>
                      </td>
                      <td width="150px">

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
                        <input type="text" name="nomePai" class="campoSemTamanho alturaPadrao larguraPaiMae">
                      </td>
                      <td width="164px">
                        <p class="legendaCodigo MargemSuperior0" >Mãe</p>
                        <input type="text" name="nomeMae" class="campoSemTamanho alturaPadrao larguraPaiMae">
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
                        <input type="text" name="proponenteCategoria" class="campoSemTamanho alturaPadrao larguraNumeroPequeno">
                      </td>
                      <td width="164px">
                        <p class="legendaCodigo MargemSuperior0" >Tit.</p>
                        <input type="text" name="proponenteMatricula" class="campoSemTamanho alturaPadrao larguraNumeroPequeno">
                      </td>
                      <td width="164px">
                        <p class="legendaCodigo MargemSuperior0" >Nome</p>
                        <input type="text" name="proponenteNome" class="campoSemTamanho alturaPadrao larguraNomeProponente">
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
          
        <BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Endereços</div>
        <div class="divisoria"></div>


        <table align="left" >
            <tr>
              <td>
                 <table align="left">
                    <tr>
                      <td>
                          <fieldset class="field-set legendaFrame recuoPadrao larguraFrameCorrespondencia alturaPadraoFrame">
                              <legend >End. Recebimento de Correspondência</legend>
                              &nbsp;&nbsp;&nbsp;&nbsp;
                              <input type="radio" name="destinoCorrespondencia" checked value="R">Residencial
                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
                              <input type="radio" name="destinoCorrespondencia" value="C">Comercial
                              

                          </fieldset>
                      </td>
                      <td>
                          <fieldset class="field-set legendaFrame recuoPadrao larguraFrameCarne alturaPadraoFrame">
                              <legend >End. Recebimento de Carnê</legend>

                              <input type="radio" name="destinoCarne" checked value="R">Residencial
                              <input type="radio" name="destinoCarne" value="C">Comercial

                          </fieldset>
                      </td>
                      <td>

                      </td>
                    </tr>
                </table>  
              </td>
            </tr>
        </table>          

        <BR><BR><BR><BR>
                              
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
                        <input type="text" class="campoSemTamanho alturaPadrao larguraNomeProponente" name="enderecoR">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Bairro</p>
                        <input type="text" class="campoSemTamanho alturaPadrao larguraBairro" name="bairroR">
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
                        <input type="text" name="cidadeR" class="campoSemTamanho alturaPadrao larguraCidade">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >UF</p>
                        <select name="ufR" class="campoSemTamanho alturaPadrao larguraUF">
                            <option value="AC">AC</option>
                            <option value="AL">AL</option>
                            <option value="AM">AM</option>
                            <option value="AP">AP</option>
                            <option value="BA">BA</option>
                            <option value="CE">CE</option>
                            <option value="DF">DF</option>
                            <option value="ES">ES</option>
                            <option value="GO">GO</option>
                            <option value="MA">MA</option>
                            <option value="MG">MG</option>
                            <option value="MS">MS</option>
                            <option value="MT">MT</option>
                            <option value="PA">PA</option>
                            <option value="PB">PB</option>
                            <option value="PE">PE</option>
                            <option value="PI">PI</option>
                            <option value="PR">PR</option>
                            <option value="RJ">RJ</option>
                            <option value="RN">RN</option>
                            <option value="RO">RO</option>
                            <option value="RR">RR</option>
                            <option value="RS">RS</option>
                            <option value="SC">SC</option>
                            <option value="SE">SE</option>
                            <option value="SP">SP</option>
                            <option value="TO">TO</option>
                        </select>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >CEP</p>
                        <input type="text" name="cepR" id="cepR" class="campoSemTamanho alturaPadrao larguraData">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Telefone</p>
                        <input type="text" name="telefoneR" class="campoSemTamanho alturaPadrao larguraData">
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
                        <input type="text" class="campoSemTamanho alturaPadrao larguraNomeProponente" name="enderecoC">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Bairro</p>
                        <input type="text" class="campoSemTamanho alturaPadrao larguraBairro" name="bairroC">
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
                        <input type="text" name="cidadeC" class="campoSemTamanho alturaPadrao larguraCidade">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >UF</p>
                        <select name="ufC" class="campoSemTamanho alturaPadrao larguraUF">
                            <option value="AC">AC</option>
                            <option value="AL">AL</option>
                            <option value="AM">AM</option>
                            <option value="AP">AP</option>
                            <option value="BA">BA</option>
                            <option value="CE">CE</option>
                            <option value="DF">DF</option>
                            <option value="ES">ES</option>
                            <option value="GO">GO</option>
                            <option value="MA">MA</option>
                            <option value="MG">MG</option>
                            <option value="MS">MS</option>
                            <option value="MT">MT</option>
                            <option value="PA">PA</option>
                            <option value="PB">PB</option>
                            <option value="PE">PE</option>
                            <option value="PI">PI</option>
                            <option value="PR">PR</option>
                            <option value="RJ">RJ</option>
                            <option value="RN">RN</option>
                            <option value="RO">RO</option>
                            <option value="RR">RR</option>
                            <option value="RS">RS</option>
                            <option value="SC">SC</option>
                            <option value="SE">SE</option>
                            <option value="SP">SP</option>
                            <option value="TO">TO</option>
                        </select>
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >CEP</p>
                        <input type="text" name="cepC" id="cepC" class="campoSemTamanho alturaPadrao larguraData">
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Telefone</p>
                        <input type="text" name="telefoneC" class="campoSemTamanho alturaPadrao larguraData">
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
                <input type="text" name="agencia" class="campoSemTamanho alturaPadrao larguraTelefone" onkeypress="onlyNumber(event)">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >DV</p>
                <input type="text" name="digitoAgencia" class="campoSemTamanho alturaPadrao larguraNumeroPequeno">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Conta</p>
                <input type="text" name="conta" class="campoSemTamanho alturaPadrao larguraTelefone" onkeypress="onlyNumber(event)">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >DV</p>
                <input type="text" name="digitoConta" class="campoSemTamanho alturaPadrao larguraNumeroPequeno">
              </td>
              <td>
                <p class="legendaCodigo MargemSuperior0" >Titular</p>
                <input type="text" name="titular" class="campoSemTamanho alturaPadrao larguraTitular">
              </td>
            </tr>
        </table>          
        <BR><BR><BR>
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1091&acao=consultar&matricula=${matricula}&categoria=${idCategoria}';" value=" " />

    </form>

</body>
</html>
