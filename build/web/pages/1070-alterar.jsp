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
                $("#validade").mask("99/99/9999");


                $("#tabela tr:gt(0)").hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
                $("#iniValidade").mask("99/99/9999");
                $("#fimValidade").mask("99/99/9999");

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
                

            });
    </script>

    <script type="text/javascript" language="JavaScript">

        function validarForm(){
            
            if(document.forms[0].nome.value == ''){
                alert('O Nome do Convidado é de preenchimento obrigatório!');
                return;
            }

            if ($('#categoriaConvite').val() != 'SP'){
                if(document.forms[0].validade.value == ''){
                    alert('A Validade é de preenchimento obrigatório!');
                    return;
                }
            }else{
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
            }

            document.forms[0].submit();
        }

    </script>
    <div class="divisoria"></div>
    <div id="titulo-subnav">Inclusão de Convite - Responsável</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
        <input type="hidden" name="app" value="1072"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="idConvite" value="${convite.numero}"/>
        <input type="hidden" name="categoriaConvite" id="categoriaConvite" value="${convite.categoriaConvite}"/>
    
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nome</p>
                  <input id="responsavel" type="text" name="socio" class="campoSemTamanho alturaPadrao" style="width:300px" disabled value="${convite.sacador}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                  
                  <c:choose>
                    <c:when test='${convite.categoriaConvite == "GR"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="GERAL"></c:when>
                    <c:when test='${convite.categoriaConvite == "CH"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="CHURRASQUEIRA"></c:when>
                    <c:when test='${convite.categoriaConvite == "SA"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="SAUNA"></c:when>
                    <c:when test='${convite.categoriaConvite == "SI"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="SINUCA"></c:when>
                    <c:when test='${convite.categoriaConvite == "ES"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL SÓCIO"></c:when>
                    <c:when test='${convite.categoriaConvite == "EC"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL CONVENIO"></c:when>
                    <c:when test='${convite.categoriaConvite == "ED"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL DIRETOR"></c:when>
                    <c:when test='${convite.categoriaConvite == "EV"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL VICE-DIRETOR"></c:when>
                    <c:when test='${convite.categoriaConvite == "EO"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL VICE-COMODORO"></c:when>
                    <c:when test='${convite.categoriaConvite == "EA"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL ASSESSOR-COMODORO"></c:when>
                    <c:when test='${convite.categoriaConvite == "EN"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL CONSELHEIRO"></c:when>
                    <c:when test='${convite.categoriaConvite == "EP"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPECIAL PRESIDENTE DO CONSELHO"></c:when>
                    <c:when test='${convite.categoriaConvite == "CO"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="INSTITUCIONAL"></c:when>
                    <c:when test='${convite.categoriaConvite == "VD"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS DIRETOR"></c:when>
                    <c:when test='${convite.categoriaConvite == "VV"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS VICE-DIRETOR"></c:when>
                    <c:when test='${convite.categoriaConvite == "VC"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS COMODORO"></c:when>
                    <c:when test='${convite.categoriaConvite == "VO"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS VICE-COMODORO"></c:when>
                    <c:when test='${convite.categoriaConvite == "VA"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS ASSESSOR-COMODORO"></c:when>
                    <c:when test='${convite.categoriaConvite == "VN"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS CONSELHEIRO"></c:when>
                    <c:when test='${convite.categoriaConvite == "VP"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="EVENTOS PRESIDENTE DO CONSELHO"></c:when>
                    <c:when test='${convite.categoriaConvite == "SP"}'><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="ESPORTIVO"></c:when>
                    <c:otherwise><input id="tipo" type="text" name="tipo" class="campoSemTamanho alturaPadrao" style="width:217px" disabled value="FAMILIA"></c:otherwise>
                  </c:choose>                        
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
                  <input id="convidado" type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:300px" value="${convite.convidado}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0">Estacionamento</p>
                  <select id="est" name="estacionamento" class="campoSemTamanho alturaPadrao larguraData" >
                      <option value="1" <c:if test='${convite.estacionamento==1}'>selected</c:if>>Interno</option>
                      <option value="0" <c:if test='${convite.estacionamento==0}'>selected</c:if>>Público</option>
                  </select>
              </td>
              <c:if test='${convite.categoriaConvite!="SP"}'>
                <td>
                    <fmt:formatDate var="validade" value="${convite.dtLimiteUtilizacao}" pattern="dd/MM/yyyy"/>
                    <p class="legendaCodigo MargemSuperior0" >Validade</p>
                    <input id="validade" type="text" name="Data" class="campoSemTamanho alturaPadrao larguraData" <c:if test='${convite.categoriaConvite=="CH"}'>disabled</c:if> value="${validade}">
                </td>
              </c:if>            

            </tr>
        </table>    
                  
        <c:if test='${convite.categoriaConvite=="SP"}'>
            <br><br><br><br>
            <div class="divisoria"></div>
            <div id="titulo-subnav">Dados do Convite</div>
            <div class="divisoria"></div>

            <table class="fmt" align="left" >
                <tr>
                  <td>
                      <p class="legendaCodigo MargemSuperior0" >Inicio Validade</p>
                      <fmt:formatDate var="validade" value="${convite.dtInicValidade}" pattern="dd/MM/yyyy"/>
                      <input id="iniValidade" type="text" name="iniValidade" class="campoSemTamanho alturaPadrao" style="width:80px" value="${validade}">
                  </td>
                  <td>
                      <p class="legendaCodigo MargemSuperior0" >Fim Validade</p>
                      <fmt:formatDate var="validade" value="${convite.dtFimValidade}" pattern="dd/MM/yyyy"/>
                      <input id="fimValidade" type="text" name="fimValidade" class="campoSemTamanho alturaPadrao" style="width:80px" value="${validade}">
                  </td>
                  <td>
                      <p class="legendaCodigo MargemSuperior0" >Modalidade</p>
                      <input id="modalidade" type="text" name="modalidade" class="campoSemTamanho alturaPadrao" style="width:202px" value="${convite.modalidade}">
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
                      <input type="text" id="segundaEntrada" name="segundaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${convite.entraSegunda}">
                  </td>
                  <td align="left">
                      <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;" >Saída:</p>
                      <input type="text" id="segundaSaida" name="segundaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${convite.saiSegunda}">
                  </td>
                  <td>&nbsp;&nbsp;</td>   
                  <td>
                      <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                      <input type="text" id="tercaEntrada" name="tercaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${convite.entraTerca}">
                  </td>
                  <td>
                      <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                      <input type="text" id="tercaSaida" name="tercaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${convite.saiTerca}">
                  </td>
                  <td>&nbsp;&nbsp;</td>   
                  <td>
                      <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                      <input type="text" id="quartaEntrada" name="quartaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${convite.entraQuarta}">
                  </td>
                  <td>
                      <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                      <input type="text" id="quartaSaida" name="quartaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${convite.saiQuarta}">
                  </td>
                  <td>&nbsp;&nbsp;</td>   
                  <td>
                      <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                      <input type="text" id="quintaEntrada" name="quintaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${convite.entraQuinta}">
                  </td>
                  <td>
                      <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                      <input type="text" id="quintaSaida" name="quintaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${convite.saiQuinta}">
                  </td>
                  <td>&nbsp;&nbsp;</td>   
                  <td>
                      <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                      <input type="text" id="sextaEntrada" name="sextaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${convite.entraSexta}">
                  </td>
                  <td>
                      <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                      <input type="text" id="sextaSaida" name="sextaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${convite.saiSexta}">
                  </td>
                </tr>    
           </table>
           <br>
        </c:if>            
        
        <c:if test='${convite.categoriaConvite=="CH"}'>
            <table id="tabela">
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
                    <c:if test='${reserva.saldoConvite > 0 || reserva.numero == convite.reservaChurrasqueira}'>
                        <tr height="1">
                            <th class="column1" align="center">
                                <input type="radio" name="selecao" <c:if test='${reserva.numero == convite.reservaChurrasqueira}'>checked</c:if> value="${reserva.numero}"  />
                            </th>
                            <th class="column1" align="left">${reserva.deChurrasqueira}</th>

                            <fmt:formatDate var="inicio" value="${reserva.dtInicio}" pattern="dd/MM/yyyy HH:mm:ss" />
                            <th class="column1" align="center">${inicio}</th>

                            <fmt:formatDate var="fim" value="${reserva.dtFim}" pattern="dd/MM/yyyy HH:mm:ss" />
                            <th class="column1" align="center">${fim}</th>

                            <th class="column1" align="left">${reserva.interessado}</th>
                            <th class="column1" align="center">${reserva.saldoConvite}</th>
                        </tr>	
                    </c:if>            
                </c:forEach>
                </tbody>
            </table>     
        </c:if>            
        <c:if test='${convite.categoriaConvite!="CH"}'>
            <br><br><br>
        </c:if>          
            <br>
        <input type="button" id="atualizar" class="botaoatualizar" onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1070&acao=consultar&numero=${convite.numero}';" value=" " />
        
    </form>
        

</body>
</html>
