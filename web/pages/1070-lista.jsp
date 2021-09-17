
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
                    //$(this).find('img').width(120);
                    //$(this).find('img').height(160);
            }, function() {
                    $(this).css('background','white');
                    //$(this).find('img').width(0);
                    //$(this).find('img').height(0);
            })
    });        
</script>  
<script type="text/javascript" language="javascript">
    $(document).ready(function () {
        $("#dtEmissao").mask("99/99/9999");
        $("#dtValidade").mask("99/99/9999");
    });     
</script>
<script type="text/javascript" language="JavaScript">
        function validarForm(){

            if(
                trim(document.forms[0].numero.value) == '' && 
                trim(document.forms[0].dtEmissao.value) == '' && 
                trim(document.forms[0].dtValidade.value) == '' && 
                trim(document.forms[0].responsavel.value) == '' && 
                trim(document.forms[0].convidado.value) == ''
               )     
                {
                alert('É preciso informar ao menos 1 parâmetro de consulta');
                return false;
            } else {
                document.forms[0].submit();
            }
        }
        function imprimeTermica(convite){
            $.ajax({url:'ConviteAjax', async:false, dataType:'text', type:'GET',data:{tipo:7,nrConvite:convite}});
        }
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Convite</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="1070">
        <input type="hidden" name="acao" value="consultar">

        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Convite</p>
                  <input type="text" name="numero" class="campoSemTamanho alturaPadrao larguraData" value="${numero}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dt. Emissão</p>
                  <input type="text" name="dtEmissao" id="dtEmissao" class="campoSemTamanho alturaPadrao larguraData" value="${dtEmissao}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dt. Validade</p>
                  <input type="text" name="dtValidade" id="dtValidade" class="campoSemTamanho alturaPadrao larguraData" value="${dtValidade}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Responsável</p>
                  <input type="text" name="responsavel" class="campoSemTamanho alturaPadrao" style="width:200px" value="${responsavel}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Convidado</p>
                  <input type="text" name="convidado" class="campoSemTamanho alturaPadrao" style="width:200px" value="${convidado}">
              </td>
              <td >    
                  <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Consultar" />
              </td>
              <td> &nbsp;&nbsp;&nbsp;
                <c:if test='<%=request.isUserInRole("1071")%>'>
                     <a href="c?app=1071&acao=showForm"><img src="imagens/btn-incluir.png" style="margin-top:25px" width="100" height="25" /></a>
                </c:if>
              </td>
            </tr>
        </table>
        <br><br>
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col" class="nome-lista">Convidado</th>
                    <th scope="col" class="nome-lista">Responsável</th>
                    <th scope="col" align="center">Categoria</th>
                    <th scope="col" align="center">Emissão</th>
                    <th scope="col" align="center">Validade</th>
                    <th scope="col" align="center">Situação</th>
                    <th scope="col" align="center">Número</th>
                    <th scope="col" align="center">Estacionamento</th>
                    <th scope="col" align="center">Tipo</th>
                    <th scope="col" align="center">Dt. Utilização</th>
                    <th scope="col" align="center">Alterar</th>
                    <th scope="col" align="center">Cancelar</th>
                    <th scope="col" align="center">Reativar</th>
                    <th scope="col" align="center">Imprimir</th>
                    <th scope="col" align="center">Imp. Térmica</th>
                    <th scope="col" align="center">Foto</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="convite" items="${convites}">
                    <tr>
                        <td class="column1">${convite.convidado}</td>
                        <td class="column1">${convite.sacador}</td>
                        <c:choose>
                            <c:when test='${convite.categoriaConvite == "GR"}'><td class="column1" align="center">GERAL</td></c:when>
                            <c:when test='${convite.categoriaConvite == "CH"}'><td class="column1" align="center">CHURRASQUEIRA</td></c:when>
                            <c:when test='${convite.categoriaConvite == "SA"}'><td class="column1" align="center">SAUNA</td></c:when>
                            <c:when test='${convite.categoriaConvite == "SI"}'><td class="column1" align="center">SINUCA</td></c:when>
                            <c:when test='${convite.categoriaConvite == "ES"}'><td class="column1" align="center">ESPECIAL SÓCIO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "EC"}'><td class="column1" align="center">ESPECIAL CONVENIO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "ED"}'><td class="column1" align="center">ESPECIAL DIRETOR</td></c:when>
                            <c:when test='${convite.categoriaConvite == "EV"}'><td class="column1" align="center">ESPECIAL VICE-DIRETOR</td></c:when>
                            <c:when test='${convite.categoriaConvite == "EO"}'><td class="column1" align="center">ESPECIAL VICE-COMODORO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "EA"}'><td class="column1" align="center">ESPECIAL ASSESSOR-COMODORO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "EN"}'><td class="column1" align="center">ESPECIAL CONSELHEIRO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "EP"}'><td class="column1" align="center">ESPECIAL PRESIDENTE DO CONSELHO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "CO"}'><td class="column1" align="center">INSTITUCIONAL</td></c:when>
                            <c:when test='${convite.categoriaConvite == "VD"}'><td class="column1" align="center">EVENTOS DIRETOR</td></c:when>
                            <c:when test='${convite.categoriaConvite == "VV"}'><td class="column1" align="center">EVENTOS VICE-DIRETOR</td></c:when>
                            <c:when test='${convite.categoriaConvite == "VC"}'><td class="column1" align="center">EVENTOS COMODORO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "VO"}'><td class="column1" align="center">EVENTOS VICE-COMODORO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "VA"}'><td class="column1" align="center">EVENTOS ASSESSOR-COMODORO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "VN"}'><td class="column1" align="center">EVENTOS CONSELHEIRO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "VP"}'><td class="column1" align="center">EVENTOS PRESIDENTE DO CONSELHO</td></c:when>
                            <c:when test='${convite.categoriaConvite == "SP"}'><td class="column1" align="center">ESPORTIVO</td></c:when>
                            <c:otherwise><td class="column1" align="center">FAMÍLIA</td></c:otherwise>
                        </c:choose>                        
                        <fmt:formatDate var="retirada" value="${convite.dtRetirada}" pattern="dd/MM/yyyy" />
                        <td class="column1" align="center">${retirada}</td>
                        <fmt:formatDate var="limiteUtilizacao" value="${convite.dtLimiteUtilizacao}" pattern="dd/MM/yyyy" />
                        <td class="column1" align="center">${limiteUtilizacao}</td>
                        <c:choose>
                            <c:when test='${convite.status == "NU"}'>
                                <td class="column1" align="center">NORMAL</td>
                            </c:when>
                            <c:when test='${convite.status == "CA"}'>
                                <td class="column1" align="center">CANCELADO</td>
                            </c:when>
                            <c:otherwise>
                                <td class="column1" align="center">UTILIZADO</td>
                            </c:otherwise>
                        </c:choose>                        

                        <td class="column1" align="center">${convite.numero}</td>
                        <c:choose>
                            <c:when test='${convite.estacionamento == 0}'>
                                <td class="column1" align="center">PÚBLICO</td>
                            </c:when>
                            <c:otherwise>
                                <td class="column1" align="center">INTERNO</td>
                            </c:otherwise>
                        </c:choose>                        
                        <c:choose>
                            <c:when test='${convite.tipo == "N"}'>
                                <td class="column1" align="center">NORMAL</td>
                            </c:when>
                            <c:otherwise>
                                <td class="column1" align="center">VENDIDO</td>
                            </c:otherwise>
                        </c:choose>                        
                        <fmt:formatDate var="utilizacao" value="${convite.dtUtilizacao}" pattern="dd/MM/yyyy" />
                        <td class="column1" align="center">${utilizacao}</td>
                        
                        <!-- ALTERAR -->
                        <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("1072")%>'>
                            <c:choose>
                                <c:when test='${convite.status == "NU"}'>
                                    <a href="c?app=1072&acao=showForm&idConvite=${convite.numero}">
                                        <img src="imagens/icones/inclusao-titular-03.png"/>
                                    </a>
                                </c:when>
                            </c:choose>
                        </c:if>
                        </td>
                        
                        <!-- EXCLUIR -->
                        <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("1073")%>'>
                            <c:choose>
                                <c:when test='${convite.status == "NU"}'>
                                    <a href="javascript: if(confirm('Confirma o CANCELAMENTO do convite selecionado?')) window.location.href='c?app=1073&idConvite=${convite.numero}'">
                                        <img src="imagens/icones/inclusao-titular-05.png"/>
                                    </a>
                                </c:when>
                            </c:choose>
                        </c:if>
                        </td>
                        
                        <!-- REATIVAR -->
                        <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("1076")%>'>
                            <c:choose>
                                <c:when test='${convite.status == "CA"}'>
                                    <a href="javascript: if(confirm('Confirma o REATIVAÇÃO do convite selecionado?')) window.location.href='c?app=1076&idConvite=${convite.numero}'">
                                        <img src="imagens/icones/inclusao-titular-06.png"/>
                                    </a>
                                </c:when>
                            </c:choose>
                        </c:if>
                        </td>
                        
                        <!-- IMPRIMIR -->
                        <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("1074")%>'>
                            <c:choose>
                                <c:when test='${convite.status == "NU"}'>
                                    <a href="c?app=1070&acao=impressao&idConvite=${convite.numero}&categoriaConvite=${convite.categoriaConvite}">
                                        <img src="imagens/icones/inclusao-titular-13.png"/>
                                    </a>
                                </c:when>
                            </c:choose>
                        </c:if>
                        </td>

                        <!-- IMPRESSAO TERMICA -->
                        <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("1074")%>'>
                            <c:choose>
                                <c:when test='${convite.status == "NU" && convite.categoriaConvite != "SP"}'>
                                    <a href="javascript:imprimeTermica('${convite.numero}');">
                                        <img src="imagens/icones/inclusao-titular-13.png"/>
                                    </a>
                                </c:when>
                            </c:choose>
                        </c:if>
                        </td>
                        
                        <!-- FOTO -->
                        <td class="column1" align="center">
                        <c:if test='<%=request.isUserInRole("1075")%>'>
                            <c:choose>
                                <c:when test='${convite.categoriaConvite == "ES" || convite.categoriaConvite == "EC" || convite.categoriaConvite == "ED" || convite.categoriaConvite == "EV" || convite.categoriaConvite == "EO" || convite.categoriaConvite == "EA" || convite.categoriaConvite == "EN" || convite.categoriaConvite == "EP"}'>
                                    <a href="c?app=1075&acao=showForm&idConvite=${convite.numero}">
                                        <img src="imagens/icones/inclusao-titular-07.png"/>
                                    </a>
                                </c:when>
                            </c:choose>
                        </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
    </form>
        

    
    
</body>
</html>
