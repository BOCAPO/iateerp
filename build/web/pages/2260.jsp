<%@include file="head.jsp"%>

    <style type="text/css">
        table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
        table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
        .alerta {
                font-family: Georgia, "Times New Roman", Times, serif;
                font-size: 20px;
                font-weight: bold;
                color: #FF0000;
        }
    </style>   

    <body class="internas">

            <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Pendência de Validação Cadastral</div>
    <div class="divisoria"></div>            
            
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">

        function validarForm(){


            document.forms[0].submit();
        }

    </script>
    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
        });        
    </script>   
<form action="c" method="POST">
    <input type="hidden" name="app" value="2260">
    <input type="hidden" name="acao" value="atualizarContatos">
    <input type="hidden" name="id" value="${titular.id}">
    <input type="hidden" name="matricula" value="${titular.matricula}">
    <input type="hidden" name="idCategoria" value="${titular.idCategoria}">

    <c:if test="${titular.showWarning}">
        <div class="alerta" align="center">
            Atenção: Esta alteração está sendo feita em um cadastro que já estava pendente de validação!
        </div>
    </c:if>
    
    <table class="fmt">
        <tr>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Em@il</p>
              <input type="text" name="email" class="campoSemTamanho alturaPadrao larguraNomeProponente"    value="${titular.email}"><br>
            </td>
            <td>
              <p class="legendaCodigo MargemSuperior0" >Tel. Celular</p>
              <input type="text" name="celular" class="campoSemTamanho alturaPadrao larguraBairro"    value="${titular.celular}"><br>
            </td>
        </tr>        
    </table>
                  
    <div class="divisoria"></div>
    <div id="titulo-subnav">Endereço Residêncial </div>
    <div class="divisoria"></div>


    <table align="left" class="fmt">
        <tr>
          <td>
             <table align="left" class="fmt">
                <tr>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Endereço</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNomeProponente" name="enderecoR"   value="${titular.enderecoResidencial.endereco}">
                  </td>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Bairro</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraBairro" name="bairroR"   value="${titular.enderecoResidencial.bairro}">
                  </td>
                </tr>
             </table>
            </td>
        </tr>
        <tr>
          <td>
             <table align="left" class="fmt">                      
                <tr>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Cidade</p>
                    <input type="text" name="cidadeR" class="campoSemTamanho alturaPadrao larguraCidade"   value="${titular.enderecoResidencial.cidade}">
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
                    <input type="text" name="cepR" class="campoSemTamanho alturaPadrao larguraData"   value="${titular.enderecoResidencial.CEP}">
                  </td>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Telefone</p>
                    <input type="text" name="telefoneR" class="campoSemTamanho alturaPadrao larguraData"    value="${titular.enderecoResidencial.telefone}">
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


    <table align="left" class="fmt">
        <tr>
          <td>
             <table align="left" class="fmt">
                <tr>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Endereço</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNomeProponente" name="enderecoC"   value="${titular.enderecoComercial.endereco}">
                  </td>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Bairro</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraBairro" name="bairroC"   value="${titular.enderecoComercial.bairro}">
                  </td>
                </tr>
             </table>
            </td>
        </tr>
        <tr>
          <td>
             <table align="left" class="fmt">                      
                <tr>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Cidade</p>
                    <input type="text" name="cidadeC" class="campoSemTamanho alturaPadrao larguraCidade"   value="${titular.enderecoComercial.cidade}">
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
                    <input type="text" name="cepC" class="campoSemTamanho alturaPadrao larguraData"   value="${titular.enderecoComercial.CEP}">
                  </td>
                  <td>
                    <p class="legendaCodigo MargemSuperior0" >Telefone</p>
                    <input type="text" name="telefoneC" class="campoSemTamanho alturaPadrao larguraData"   value="${titular.enderecoComercial.telefone}">
                  </td>

                </tr>
            </table>  
          </td>
        </tr>
    </table>          

    <br><br><br><br><br><br><br>
    <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=9030&acao=consultar&matricula=${titular.matricula}&categoria=${titular.idCategoria}';" value=" " />    
    <br> <br> 

</form>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Carros </div>
    <div class="divisoria"></div>


    <div class="botaoincluirgeral">
        <a href="c?app=2260&acao=showFormCarro&matricula=${titular.matricula}&idCategoria=${titular.idCategoria}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
    </div>

    <br>                



    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Marca</th>
                <th scope="col">Modelo</th>
                <th scope="col">Cor</th>
                <th scope="col">Placa</th>
                <th scope="col">Alterar</th>
                <th scope="col">Excluir</th>
            </tr>
        </thead>
            
        <c:forEach var="carro" items="${carros}">
            <tr>
                <td class="column1">${carro.modelo.marca.descricao}</td>
                <td class="column1">${carro.modelo.descricao}</td>
                <td class="column1">${carro.cor.descricao}</td>
                <td class="column1">${carro.placa}</td>
                <td align="center">    
                <c:if test='<%=request.isUserInRole("2260")%>'>
                    <a href="c?app=2260&acao=showFormCarro&idCarro=${carro.id}&matricula=${titular.matricula}&idCategoria=${titular.idCategoria}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                </c:if>
                </td>
                <td align="center">
                <c:if test='<%=request.isUserInRole("2260")%>'>
                    <a href="javascript: if(confirm('Confirma a exclusão do carro?')) window.location.href='c?app=2260&idCarro=${carro.id}&acao=excluirCarro&matricula=${titular.matricula}&idCategoria=${titular.idCategoria}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                </c:if>
                </td>

            </tr>
        </c:forEach>
    </table>
    
</body>
</html>
