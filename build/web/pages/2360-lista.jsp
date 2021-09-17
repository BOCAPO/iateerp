
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');
            
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
            
            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
    });        
</script>  

<script type="text/javascript" language="JavaScript">
        function validarForm(){
            if($('#dataInicio').val() == ''){
               alert("Informe a data de início!");
               return false;
            }
            if(!isDataValida($('#dataInicio').val())){
                return false; 
            }
            if($('#dataFim').val() == ''){
               alert("Informe a data de fim!");
               return false;
            }
            if(!isDataValida($('#dataFim').val())){
                return false; 
            }

            document.forms[0].submit();
        }
        
        function validarForm2(){

            var quadras = " ";
            for(i=0; i < document.forms[0].length; i++){
                if (document.forms[0].elements[i].type == "checkbox" &&  document.forms[0].elements[i].name == "idQuadra" ) {
                    if(document.forms[0].elements[i].checked) {
                        quadras = quadras + document.forms[0].elements[i].value + ",";
                    }                    
                }    
            } 
            var categorias = " ";
            for(i=0; i < document.forms[0].length; i++){
                if (document.forms[0].elements[i].type == "checkbox" &&  document.forms[0].elements[i].name == "idCategoria" ) {
                    if(document.forms[0].elements[i].checked) {
                        categorias = categorias + document.forms[0].elements[i].value + ",";
                    }                    
                }    
            } 


            window.location.href='c?app=2360&acao=imprimirRelatorio'+
                                 '&idCategoria='+categorias+
                                 '&idQuadra='+quadras+
                                 '&matricula='+document.forms[0].matricula.value+
                                 '&nomeSocio='+document.forms[0].nomeSocio.value+
                                 '&nomeConvidado='+document.forms[0].nomeConvidado.value+
                                 '&dataInicio='+document.forms[0].dataInicio.value+
                                 '&horaInicio='+document.forms[0].horaInicio.value+
                                 '&minutoInicio='+document.forms[0].minutoInicio.value+
                                 '&dataFim='+document.forms[0].dataFim.value+
                                 '&horaFim='+document.forms[0].horaFim.value+
                                 '&minutoFim='+document.forms[0].minutoFim.value+
                                 '&tipo='+document.forms[0].tipo.value+
                                 '&origem='+document.forms[0].origem.value+
                                 '&resultado='+document.forms[0].resultado.value;
                             
        }
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Relação de Marcação de Tenis</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="2360">
        <input type="hidden" name="acao" value="consultar">
        
        <table class="fmt" align="left" >
            <tr>
                <td rowspan="3">

                    <p class="legendaCodigo MargemSuperior0">Quadras</p>
                    
                    <div class="recuoPadrao" style="overflow:auto;height:130px;width:150px;">
                        <c:forEach var="quadra" items="${quadras}">
                            <c:set var="selected" value=""/> 
                            <c:forEach var="quadra2" items="${idQuadra}">
                                <c:if test="${quadra2 == quadra.id}">
                                    <c:set var="selected" value="checked"/> 
                                </c:if>
                            </c:forEach>
                            <input type="checkbox" name="idQuadra"  value="${quadra.id}" ${selected}/>
                            ${quadra.descricao}
                            <br />
                        </c:forEach>
                    </div>
                </td>
                <td rowspan="3">
                    <p class="legendaCodigo MargemSuperior0">Categorias</p>
                    <div class="recuoPadrao" style="overflow:auto;height:130px;width:200px;">
                        <c:forEach var="categoria" items="${categorias}">
                            
                            <c:set var="selected" value=""/> 
                            <c:forEach var="categoria2" items="${idCategoria}">
                                <c:if test="${categoria2 == categoria.id}">
                                    <c:set var="selected" value="checked"/> 
                                </c:if>
                            </c:forEach>
                            
                            <input type="checkbox" name="idCategoria"  value="${categoria.id}" ${selected}/>
                            ${categoria.descricao}
                            <br />
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <table class="fmt" align="left" >
                        <tr>    
                          <td>
                              <p class="legendaCodigo MargemSuperior0" >Título</p>
                              <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNumero" value="${matricula}">
                          </td>
                          <td>
                              <p class="legendaCodigo MargemSuperior0" >Nome do Sócio</p>
                              <input type="text" name="nomeSocio" class="campoSemTamanho alturaPadrao " style="width:188px" value="${nomeSocio}">
                          </td>
                          <td>
                              <p class="legendaCodigo MargemSuperior0" >Nome do Convidado</p>
                              <input type="text" name="nomeConvidado" class="campoSemTamanho alturaPadrao" style="width:188px" value="${nomeConvidado}">
                          </td>
                          <td>
                              <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Consultar" />
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
                            <p class="legendaCodigo MargemSuperior0">Data de Início</p>
                            <input name="dataInicio" id="dataInicio" type="text" class="campoSemTamanho alturaPadrao larguraData" value="${dataInicio}">
                        </td>

                        <td style="white-space: nowrap;">
                            <p class="legendaCodigo MargemSuperior0" >Horário de Início</p>
                            <div class="selectheightnovo">
                                <select name="horaInicio" class="campoSemTamanho" style="width: 50px;">
                                    <c:forEach var="i" begin="0" end="23">
                                        <option value="${i}" <c:if test="${horaInicio == i}">selected="selected"</c:if>><fmt:formatNumber value="${i}" minIntegerDigits="2" /></option>
                                    </c:forEach>
                                </select>
                                :
                                <select name="minutoInicio"  class="campoSemTamanhoSemMargem" style="width: 50px;">
                                    <c:forEach var="i" begin="0" end="59" step="1">
                                        <option value="${i}" <c:if test="${minutoInicio == i}">selected="selected"</c:if>><fmt:formatNumber value="${i}" minIntegerDigits="2" /></option>
                                    </c:forEach>
                                </select>                        
                            </div>        
                        </td>

                        <td>
                            <p class="legendaCodigo MargemSuperior0">Data de Término</p>
                            <input name="dataFim" id="dataFim" type="text" class="campoSemTamanho alturaPadrao larguraData" value="${dataFim}">
                        </td>

                        <td style="white-space: nowrap;">
                            <p class="legendaCodigo MargemSuperior0" >Horário de Término</p>
                            <div class="selectheightnovo">
                                <select name="horaFim" class="campoSemTamanho" style="width:50px;">
                                    <c:forEach var="i" begin="0" end="23">
                                        <option value="${i}" <c:if test="${horaFim == i}">selected="selected"</c:if>><fmt:formatNumber value="${i}" minIntegerDigits="2" /></option>
                                    </c:forEach>
                                </select>
                                :
                                <select name="minutoFim"  class="campoSemTamanhoSemMargem" style="width:50px;">
                                    <c:forEach var="i" begin="0" end="59" step="1">
                                        <option value="${i}" <c:if test="${minutoFim == i}">selected="selected"</c:if>><fmt:formatNumber value="${i}" minIntegerDigits="2" /></option>
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
                                <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                                <div class="selectheightnovo">
                                  <select name="tipo" class="campoSemTamanho alturaPadrao"  >
                                      <option value="T" selected>TODAS</option>
                                      <option value="S" <c:if test="${tipo == 'S'}">selected="selected"</c:if>>Simples</option>
                                      <option value="D" <c:if test="${tipo == 'D'}">selected="selected"</c:if>>Duplas</option>
                                  </select>
                                </div>        
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Origem</p>
                                <div class="selectheightnovo">
                                  <select name="origem" class="campoSemTamanho alturaPadrao"  >
                                      <option value="T" selected>TODAS</option>
                                      <option value="C" <c:if test="${origem == 'C'}">selected="selected"</c:if>>Clube</option>
                                      <option value="S" <c:if test="${origem == 'S'}">selected="selected"</c:if>>Sócio</option>
                                  </select>
                                </div>        
                            </td>
                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Resultado</p>
                                <div class="selectheightnovo">
                                  <select name="resultado" class="campoSemTamanho alturaPadrao"  >
                                      <option value="A" <c:if test="${resultado == 'A'}">selected="selected"</c:if>>Analítico</option>
                                      <option value="S" <c:if test="${resultado == 'S'}">selected="selected"</c:if>>Sintético</option>
                                  </select>
                                </div>        
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>
    
    <br><br><br><br><br><br><br><br><br><br>

    <table class="fmt" border="0" >
        <tr>
            <td>
                <div class=" campoSemTamanho alturaPadrao">
                    <a href="javascript:validarForm2()"><img src="imagens/btn-imprimir.png" width="100" height="25" /></a>
                <div>
            </td>
        <tr>
    </table>

    <!--
    ********************************
    ANALÍTICO
    ********************************
    -->
    <c:if test="${resultado == 'A'}">

        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col">Quadra</th>
                    <th scope="col">Jogador</th>
                    <th scope="col">Utilização</th>
                    <th scope="col">Tipo</th>
                    <th scope="col">Origem</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="marcacao" items="${marcacoes}">
                    <tr>
                        <td class="column1" align="center">${marcacao.quadra}</td>
                        <td class="column1" >${marcacao.jogador}</td>
                        <td class="column1" align="center"><joda:format value="${marcacao.dtInicio}" pattern="dd/MM/yyyy HH':'mm" /> a <joda:format value="${marcacao.dtFim}" pattern="dd/MM/yyyy HH':'mm" /></td>
                        <td class="column1" align="center">${marcacao.tipo}</td>
                        <td class="column1" align="center">${marcacao.origem}</td>

                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>    
        

    <!--
    ********************************
    SINTÉTICO
    ********************************
    -->
    <c:if test="${resultado == 'S'}">
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col">Quadra</th>
                    <th scope="col">Qt. Jogos Simples</th>
                    <th scope="col">Qt. Jogos Dupla</th>
                    <th scope="col">Qt. Jogos Total</th>
                    <th scope="col">Min. Util. Simples</th>
                    <th scope="col">Min. Util. Duplas</th>
                    <th scope="col">Min. Util. Total</th>
                    <th scope="col">Min. Bloqueio</th>
                </tr>
            </thead>
            <tbody>
                
                <c:forEach var="marcacao" items="${marcacoes}">
                    <tr>
                        <td class="column1" align="center">${marcacao.quadra}</td>
                        <td class="column1" >${marcacao.qtJogoSimples}</td>
                        <td class="column1" >${marcacao.qtJogoDuplas}</td>
                        <td class="column1" >${marcacao.qtJogoTotal}</td>
                        <td class="column1" >${marcacao.minSimples}</td>
                        <td class="column1" >${marcacao.minDuplas}</td>
                        <td class="column1" >${marcacao.minTotal}</td>
                        <td class="column1" >${marcacao.minBloqueio}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>    
    
    
</body>
</html>
