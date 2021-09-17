
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:0;width:0;padding:0px;margin-left:0em;margin-top:0em;padding-left:100px;}
    table.fmt tr td {border:0;background:none;padding:0px;margin:0em;}
        
    .campoNome{ 
        font-family:Arial, Helvetica, sans-serif; 
        font-size:24px;  
        border:1px solid #cccde3; 
        margin-left:15px;
        width:443px;
        height:40px
    }    
    
    .campoCargoEspecial{ 
        font-family:Times New Roman; 
        font-size:22px;  
        color: black;
        font-weight: bold;
        margin-left:15px;
    }    

    .campoMensagem{ 
        font-family:MS Sans Serif; 
        font-size:14px;  
        font-weight: bold;
        width:460px;
    }    
    
    .fotoMultiplas{ 
        margin-left:30px;
        margin-top:10px;
        width:180px;
        height:250px;    
    }    
    .fotoUnica{ 
        margin-left:30px;
        margin-top:10px;
        width:380px;
        height:480px;    
    }    
    #pesquisa {
        margin-top: -50px;
        margin-left: -50px;
        left: 50%;
        top: 50%;
        position: fixed;
    }

</style>

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#pesquisa').hide();
            
            $("#entraSai").live("click", trocaES);
            
            if ($('#entradaSaida').val() == 'S'){
                $('#entradaSaida').val('S');
                $('#textoEntraSai').text('Controle Saída');
            }else{
                $('#entradaSaida').val('E');
                $('#textoEntraSai').text('Controle Entrada');
            }
            if ($('#acessaVeiculo').val() == 'S' && $('#mostraPlaca').val() == 'S'){
                $('#lblPlaca').show();
                $('#placa').show();
            }else{
                $('#lblPlaca').hide();
                $('#placa').hide();
            }
            if ($('#mostraQuantidade').val() == 'S'){
                $('#lblQdt').show();
                $('#qtd').show();
            }else{
                $('#lblQdt').hide();
                $('#qtd').hide();
            }
            
            if ($('#nome').val() != "<MULTIPLOS>" && $('#nome').val() != ""){
                setTimeout(limpa, 5000);
            }
    });            
    
    
    function trocaES(){
        if ($('#entradaSaida').val() == 'E'){
            $('#entradaSaida').val('S');
            $('#textoEntraSai').text('Controle Saída');
        }else{
            $('#entradaSaida').val('E');
            $('#textoEntraSai').text('Controle Entrada');
        }
    }

    function habilitaPesquisa(){
        $('#pesquisa').show();
    }

    
    function limpa(){

        window.location.href='c?app=7040&acao=showForm'+
                             '&idLocal='+document.forms[0].idLocal.value

    }
    
    function submetePesquisa(){

        //alert(tipoDoc.filter(':checked').val());

        window.location.href='c?app=7040&acao=pesquisa'+
                             '&idLocal='+document.forms[0].idLocal.value+
                             '&placa='+document.forms[0].placa.value+
                             '&entradaSaida='+document.forms[0].entradaSaida.value+
                             '&qtd='+document.forms[0].qtd.value+
                             '&tipoDoc='+$("input[name='tipoDoc']:checked").val();

    }
    

</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuAcesso.jsp"%>
    <div id="entraSai">
        <div class="divisoria" ></div>
        <div class="titulo-subnav" id="textoEntraSai">Controle de Entrada</div>
        <div class="divisoria"></div>
    </div>        
    
    <form action="c" method="POST" onsubmit="return true" id="myForm">
        <input type="hidden" name="app" value="7040">
        <input type="hidden" name="acao" value="acesso">
        <input type="hidden" name="cliente" value="${cliente}">
        <input type="hidden" name="cliente2" value="${cliente2}">
        
        <c:choose>
              <c:when test='${entradaSaida == "S"}'>
                  <c:set var="ES" value="S" scope="page" />
              </c:when>
              <c:otherwise>
                  <c:set var="ES" value="E" scope="page" />
              </c:otherwise>
        </c:choose>                        

        <input type="hidden" name="entradaSaida" id="entradaSaida" value="${ES}">
        <input type="hidden" name="idLocal" value="${local.id}">
        <input type="hidden" name="acessaVeiculo" id="acessaVeiculo" value="${local.soCarro}">
        <input type="hidden" name="mostraPlaca" id="mostraPlaca"  value="${local.motraPlaca}">
        <input type="hidden" name="mostraQuantidade" id="mostraQuantidade" value="${local.mostraQuantidade}">
        <!--diferenciando quando sao multiplos documentos -->
        <c:choose>
              <c:when test='${tamanho == 1}'>
                  <c:forEach var="entrada" items="${entradas}">
                        <c:set var="documento" value="${entrada.documento}" scope="page" />
                        <c:set var="dtNascimento" value="${entrada.dtNasc}" scope="page" />
                        <c:set var="nomeVar" value="${entrada.nome}" scope="page" />
                  </c:forEach>
              </c:when>
              <c:otherwise>
                  <c:set var="documento" value="" scope="page" />
                  <c:set var="dtNascimento" value="" scope="page" />
                  <c:set var="nomeVar" value="" scope="page" />
                  <c:if test='${tamanho > 1}'>
                      <c:set var="nomeVar" value="<MULTIPLOS>" scope="page" />
                  </c:if>
                  
              </c:otherwise>
        </c:choose>                        
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                <table class="fmt" align="left" >
                    <tr>
                      <td>
                          <p class="legendaCodigo MargemSuperior0" id="lblPlaca">Placa</p>
                          <input id="placa" type="text" name="placa" id="placa" class="campoSemTamanho alturaPadrao" maxlength="8" style="width:70px" value="${placa}">
                      </td>
                      <td>
                          <p class="legendaCodigo MargemSuperior0" id="lblQdt" >Qtd.</p>
                          <input id="qtd" type="text" name="qtd" id="qtd" class="campoSemTamanho alturaPadrao" maxlength="1" style="width:30px" value="${qtd}">
                      </td>
                      <td>
                          <p class="legendaCodigo MargemSuperior0" >Documento</p>
                          <input id="doc" type="text" name="doc" id="doc" class="campoSemTamanho alturaPadrao" maxlength="9" style="width:70px" value="${documento}">
                      </td>
                      <td>
                          <p class="legendaCodigo MargemSuperior0" >Nascimento</p>
                          <fmt:formatDate var="nasc" value="${dtNascimento}" pattern="dd/MM/yyyy" />
                          <input id="responsavel" type="text" name="dtNasc" id="dtNasc" class="campoSemTamanho alturaPadrao" style="width:70px" disabled value="${nasc}">
                          
                      </td>
                      <td>
                          <p class="legendaCodigo MargemSuperior0" >Local de Acesso</p>
                          <input id="responsavel" type="text" name="local" id="local" class="campoSemTamanho alturaPadrao" style="width:160px" disabled value="${local.descricao}">
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
                          <p class="legendaCodigo MargemSuperior0" >Nome Pessoa</p>
                          <input type="text" name="nome" id="nome" class="campoNome" readonly value="${nomeVar}">
                      </td>
                      <td >    
                          <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="habilitaPesquisa()" value="" title="Consultar" />
                      </td>
                    </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td align="center" class="campoCargoEspecial">
                  ${cargoEspecial}
              </td>
            </tr>
            <tr>
              <td align="center" class="campoCargoEspecial">
                <br>
                <input type="button" id="limpar" name="limpar" class="botaolimpar" onclick="limpa()" value=" " />
                <input type="submit" id="atualizar" name="atualizar" class="botaoatualizar"  value=" " />
              </td>
            </tr>
            <tr>
              <td align="left" class="campoCargoEspecial">
                  <br>
                  <p class="legendaCodigo MargemSuperior0" >Mensagem</p>
              </td>
            </tr>
              <c:forEach var="entrada" items="${entradas}">
                <tr>
                    <td>
                      <div style="border:1px solid black;margin-left:15px;background-color:${entrada.cor};${height}">  
                            <c:choose>
                                  <c:when test='${tamanho == 1}'>
                                        <p class="campoMensagem" style="color: ${entrada.corFonte}">${entrada.mensagem}<br>&nbsp</p>
                                  </c:when>
                                  <c:otherwise>
                                        <p class="campoMensagem" style="color: ${entrada.corFonte}">${entrada.nome}<br>${entrada.mensagem}<br>&nbsp</p>
                                  </c:otherwise>
                            </c:choose>                        
                          
                      </div>    
                    </td>
                <tr>
              </c:forEach>
          </tr>
        </table>
              
        <c:set var="i" value="0" scope="page" />
        <table class="fmt" align="left" >
            <c:forEach var="entrada" items="${entradas}">
                <c:if test='${i == 0}'>
                    <tr>
                </c:if>
                <td>
                  <img class="${classeFoto}" src="${entrada.foto}" onerror="this.src='images/tenis/avatar-default.png';">
                </td>
                <c:choose>
                    <c:when test='${i == 0}'>
                        <c:set var="i" value="1" scope="page" />
                    </c:when>
                    <c:otherwise>
                        </tr>
                        <c:set var="i" value="0" scope="page" />
                    </c:otherwise>
                </c:choose>                        
            </c:forEach>
        </table>
    </form>
    

    <div id="pesquisa" >
        <table style="background:#fff">
            <tr>
              <td>
                <div class="divisoria"></div>
                <div id="titulo-subnav">Tipo de Documento</div>
                <div class="divisoria"></div>
                <table class="fmt" align="left" align="left">
                  <tr>
                    <td>
                        <input type="radio" id="tipoDoc" name="tipoDoc" checked value="CP"/>Carteira/Passaporte<br>
                        <input type="radio" id="tipoDoc" name="tipoDoc" value="CO"/>Convite<br>
                        <input type="radio" id="tipoDoc" name="tipoDoc" value="AE"/>Autorização de Embarque<br>
                        <input type="radio" id="tipoDoc" name="tipoDoc" value="PP"/>Permissão Provisória<br>
                        
                    </td>
                  </tr>
                  <tr>
                    <td>
                        <br>
                        <input type="button" id="cmdPesquisa" name="cmdPesquisa" class="botaoatualizar" value=" " onclick="submetePesquisa()" value="" />
                    </td>
                  </tr>
                </table>  
            </td>
          </tr>
        </table>
    </div>        
                
                
</body>
</html>
