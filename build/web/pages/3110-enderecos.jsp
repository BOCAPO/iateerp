<%@include file="head.jsp"%>

    <style>
        table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
        tr {border:none;background:none;padding:0px;margin:0em auto;}
        td {border:none;background:none;padding:0px;margin:0em auto;}
    </style>  

    <body class="internas">

            <%@include file="menu.jsp"%>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
            $("#cepR").mask("99.999-999");
            $("#cepC").mask("99.999-999");
        });     
    </script>

    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){
            document.forms[0].submit();
        }

    </script>

<form action="c" method="POST">
    <input type="hidden" name="app" value="${app}">
    <input type="hidden" name="acao" value="${acao}">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Endereços</div>
    <div class="divisoria"></div>


    <table align="left" >
        <tr>
          <td>
             <table align="left">
                <tr>
                  <td>
                      <fieldset class="field-set legendaFrame recuoPadrao larguraFrameCorrespondencia alturaPadraoFrame"   >
                          <legend >End. Recebimento de Correspondência</legend>
                          &nbsp;&nbsp;&nbsp;&nbsp;
                          <input type="radio" name="destinoCorrespondencia" value="R" <c:if test='${titular.destinoCorrespondencia == "R" || titular.destinoCorrespondencia == ""}'>checked</c:if>>Residencial
                          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
                          <input type="radio" name="destinoCorrespondencia" value="C" <c:if test='${titular.destinoCorrespondencia == "C"}'>checked</c:if>>Comercial
                      </fieldset>

                  </td>
                  <td>
                      <fieldset class="field-set legendaFrame recuoPadrao larguraFrameCarne alturaPadraoFrame"   >
                          <legend >End. Recebimento de Carnê</legend>

                          <input type="radio" name="destinoCarne" value="R" <c:if test='${titular.destinoCarne == "R" || titular.destinoCarne == ""}'>checked</c:if>>Residencial
                          <input type="radio" name="destinoCarne" value="C" <c:if test='${titular.destinoCarne == "C"}'>checked</c:if>>Comercial

                      </fieldset>
                  </td>
                  <td>
                      &nbsp;
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
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNomeProponente" maxlength="60" name="enderecoR"   value="${titular.enderecoResidencial.endereco}">
                  </td>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Bairro</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraBairro" maxlength="20" name="bairroR"   value="${titular.enderecoResidencial.bairro}">
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
                    <input type="text" name="cidadeR" class="campoSemTamanho alturaPadrao larguraCidade"   maxlength="20" value="${titular.enderecoResidencial.cidade}">
                  </td>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >UF</p>
                    <select name="ufR" class="campoSemTamanho alturaPadrao larguraUF"   >
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
                    <input type="text" name="cepR" id="cepR" class="campoSemTamanho alturaPadrao larguraData"   value="${titular.enderecoResidencial.CEP}">
                  </td>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Telefone</p>
                    <input type="text" name="telefoneR" id="telefoneR" class="campoSemTamanho alturaPadrao larguraData"  maxlength="25"   value="${titular.enderecoResidencial.telefone}">
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
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNomeProponente" name="enderecoC"  maxlength="60"  value="${titular.enderecoComercial.endereco}">
                  </td>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Bairro</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraBairro" name="bairroC"  maxlength="20"  value="${titular.enderecoComercial.bairro}">
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
                    <input type="text" name="cidadeC" class="campoSemTamanho alturaPadrao larguraCidade"  maxlength="20"  value="${titular.enderecoComercial.cidade}">
                  </td>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >UF</p>
                    <select name="ufC" class="campoSemTamanho alturaPadrao larguraUF"   >
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
                    <input type="text" name="cepC" id="cepC"  class="campoSemTamanho alturaPadrao larguraData"   value="${titular.enderecoComercial.CEP}">
                  </td>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Telefone</p>
                    <input type="text" name="telefoneC" id="telefoneC" class="campoSemTamanho alturaPadrao larguraData"   maxlength="60" value="${titular.enderecoComercial.telefone}">
                  </td>

                </tr>
            </table>  
          </td>
        </tr>
    </table>          

    <br><br><br><br><br><br><br> 
    <input type="button" class="botaoatualizar"  onclick="validarForm()" value="" />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${titular.socio.matricula}&categoria=${titular.socio.idCategoria}';" value="" />


</form>

</body>
</html>
