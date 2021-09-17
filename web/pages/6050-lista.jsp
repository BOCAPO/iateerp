<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
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
            }, function() {
                    $(this).css('background','white');
            })
            
            $('#dataInicio').mask('99/99/9999');
            $('#dataFim').mask('99/99/9999');
            
            $('#alteracao').hide();
            
    });        
</script>  

<script type="text/javascript" language="JavaScript">
        function validarForm(){
            if(document.forms[0].dataInicio.value==""){
                alert('Informe a Data de Início!');
                return;
            }
            if(document.forms[0].dataFim.value==""){
                alert('Informe a Data de Fim!');
                return;
            }
            if(!isDataValida(document.forms[0].dataInicio.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataFim.value)){
                return;
            }        
          
            $('#acao').val('consultar');

            document.forms[0].submit();
        }
        
        function imprimir(){
            if(document.forms[0].dataInicio.value==""){
                alert('Informe a Data de Início!');
                return;
            }
            if(document.forms[0].dataFim.value==""){
                alert('Informe a Data de Fim!');
                return;
            }
            if(!isDataValida(document.forms[0].dataInicio.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataFim.value)){
                return;
            }        
          
            $('#acao').val('imprimir');
            
            document.forms[0].submit();
                             
        }
        function cancelaAlteracao(){
            $('#alteracao').hide();
        }
        
        function mostraAlteracao(id){
            $('#dtDeposito').val($('#tabDtDeposito'+id).html());
            var contr = $('#tabContrParc'+id).html();
            
            $('#contrParc').attr('checked', false);
            if (contr=='S'){
                $('#contrParc').attr('checked', true);
            }
            
            $('#idAlteracao').val(id);
            $('#alteracao').show();
        }
        
        function atualizaAlteracao(){
            if(document.forms[0].dtDeposito.value==""){
                alert('Informe a Data de Depósito!');
                return;
            }
            if(!isDataValida(document.forms[0].dtDeposito.value)){
                return;
            }
            
            $.ajax({url:'ChequeAjax', async:false, dataType:'text', type:'GET',data:{
                                    tipo:1,
                                    banco:$('#idAlteracao').val().substring(0,4),
                                    nrCheque:$('#idAlteracao').val().substring(4,14),
                                    seqCheque:$('#idAlteracao').val().substring(14,24),
                                    contrParc:$('#contrParc').attr('checked'),
                                    dtDeposito: $('#dtDeposito').val()}
            }).success(function(retorno){
               alert('Taxa alterada com sucesso!');
               document.forms[0].submit();
            });
        }
        
        
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuCaixa.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Cheques Recebidos</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="6050">
        <input type="hidden" name="acao" id="acao" value="consultar">
        <input type="hidden" name="usuario" id="usuario" value="${usuario}">
        <input type="hidden" name="idAlteracao" id="idAlteracao">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:70px">
                    <legend >Período:</legend>
                    <input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero" style="margin-top:10px;" value="${dataInicio}">        
                    &nbsp;&nbsp;&nbsp;a
                    <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero" style="margin-top:10px;" value="${dataFim}"><br>
                    
                </fieldset>
                  
              </td>
              <td>
                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height: 70px">
                    <legend >Situação:</legend>
                    <input type="radio" name="periodo" class="legendaCodigo" value="REC" checked  style="margin-top:6px;">Por Data de Recebimento<br>
                    <input type="radio" name="periodo" class="legendaCodigo" value="DEP" <c:if test='${periodo=="DEP"}'>checked</c:if>>Por Data para Depósito
                </fieldset>
                  
              </td>
              <td>
                  <input class="botaobuscainclusao"  type="button" onclick="validarForm()" style="margin-left:20px" value="" title="Consultar" />
                  
              </td>
            </tr>
            <tr>
                <td>
                    <br>
                    &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:imprimir()"><img src="imagens/btn-imprimir.png" width="100" height="25" /></a>
                </td>
                
            </tr>
        </table>
    
        <br><br><br>

        <c:if test='${acao=="consultar"}'>
            <sql:query var="rs" dataSource="jdbc/iate">
                EXEC SP_REC_CHEQUES_PERIODO '${fn:substring(dataInicio,3,5)}/${fn:substring(dataInicio,0,2)}/${fn:substring(dataInicio,6,10)} 00:00:00', '${fn:substring(dataFim,3,5)}/${fn:substring(dataFim,0,2)}/${fn:substring(dataFim,6,10)} 23:59:59', '${periodo}', 'C'
            </sql:query>  
        </c:if>            

        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <c:if test='${periodo=="REC"}'>
                        <th scope="col">Dt. Movimento</th>
                        <th scope="col">Dt. Depósito</th>
                    </c:if>
                    <c:if test='${periodo=="DEP"}'>
                        <th scope="col">Dt. Depósito</th>
                        <th scope="col">Dt. Movimento</th>
                    </c:if>
                    <th scope="col">Pagante</th>
                    <th scope="col">Banco</th>
                    <th scope="col">Nº do Cheque</th>
                    <th scope="col">Série</th>
                    <th scope="col">Con Parc/Pre</th>
                    <th scope="col">Valor</th>
                    <th scope="col">Alterar</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="taxaInd" items="${rs.rowsByIndex}">
                    <tr>
                        <fmt:formatNumber var="banco"  value="${taxaInd[3]}" pattern="0000"/>
                        <fmt:formatNumber var="cheque"  value="${taxaInd[4]}" pattern="0000000000"/>
                        <fmt:formatNumber var="seqCheque"  value="${taxaInd[8]}" pattern="0000000000"/>

                        <fmt:formatDate var="dt1" value="${taxaInd[0]}" pattern="dd/MM/yyyy"/>
                        <fmt:formatDate var="dt2" value="${taxaInd[1]}" pattern="dd/MM/yyyy"/>
                        
                        <c:if test='${periodo=="REC"}'>
                            <td class="column1" align="center">${dt1}</td>
                            <td class="column1" align="center" id="tabDtDeposito${banco}${cheque}${seqCheque}">${dt2}</td>
                        </c:if>
                        <c:if test='${periodo=="DEP"}'>
                            <td class="column1" align="center" id="tabDtDeposito${banco}${cheque}${seqCheque}">${dt1}</td>
                            <td class="column1" align="center">${dt2}</td>
                        </c:if>
                        
                        <td class="column1">${taxaInd[2]}</td>
                        <td class="column1" align="center">${taxaInd[3]}</td>
                        <td class="column1" align="center">${taxaInd[4]}</td>
                        <td class="column1" align="center">${taxaInd[5]}</td>
                        <td class="column1" align="center" id="tabContrParc${banco}${cheque}${seqCheque}">${taxaInd[6]}</td>
                        <fmt:formatNumber var="valor"  value="${taxaInd[7]}" pattern="#,##0.00"/>
                        <td class="column1" align="right">${valor}</td>
                        <td align="center">    
                            <c:if test='<%=request.isUserInRole("6051")%>'>
                                <a href="#" onclick="mostraAlteracao('${banco}${cheque}${seqCheque}')"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>


        <div id="alteracao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 420px; height:600px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Alteração de Cheques</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>
                            <td colspan="2">
                                <p class="legendaCodigo MargemSuperior0" >Dt. Depósito</p>
                                <input type="text" id="dtDeposito" name="dtDeposito" class="campoSemTamanho alturaPadrao" style="width:80px;" value="">
                                &nbsp<input type="checkbox" name="contrParc" id="contrParc" value="true" style="margin-top:-5px;margin-left:16px" <c:if test='${carnePago}'>checked</c:if>>Controlar Parcelamento/Pré-Datado
                            </td>
                        <tr>
                        </tr>
                            <td>
                                <br>
                                <input style="margin-left:15px;" type="button" id="cmdAtualizar" name="cmdAtualizar" class="botaocancelar" onclick="cancelaAlteracao()" />
                            </td>
                            <td>
                                <br>
                                <input style="margin-left:15px;margin-top:05px;" type="button" id="cmdCancelar" name="cmdCancelar" class="botaoatualizar" onclick="atualizaAlteracao()"  />
                            </td>
                        </tr>
                    </table>
                </td>
              </tr>
            </table>
        </div>

    </form>
    
</body>
</html>
