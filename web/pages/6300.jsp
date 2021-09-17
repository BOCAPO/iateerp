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
            $('#mesAno').mask('99/9999');
            
            $('#alteracao').hide();
            $('#impressao').hide();
            
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
          
            if(!$("#normal").prop('checked') && !$("#cancelada").prop('checked')){
                alert("Informe pelo menos uma das situações!");
                return;
            }
            $('#acao').val('consultar');

            document.forms[0].submit();
        }
        
        function cancelaImpressao(){
            $('#impressao').hide();
        }
        
        function mostraImpressao(id){
            $('#impressao').show();
        }
        
        function atualizaImpressao(){
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
          
            if(!$("#normal").prop('checked') && !$("#cancelada").prop('checked')){
                alert("Informe pelo menos uma das situações!");
                return;
            }
            
            $('#acao').val('imprimir');
            
            document.forms[0].submit();
                             
        }
        function cancelaTaxa(id, tp){
            if (confirm('Deseja realmente cancelar a Taxa selecionada?')){
                $.ajax({url:'TaxaIndividualAjax', async:false, dataType:'text', type:'GET',data:{
                                    tipo:1,
                                    taxa:id,
                                    conta:'PRE',
                                    usuario:$('#usuario').val()}
                }).success(function(retorno){
                    alert('Taxa cancelada com sucesso!');
                });
                document.forms[0].submit();
            }
            
            
        }
        
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuCaixa.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Taxas Individuais - Analítico</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="6300">
        <input type="hidden" name="acao" id="acao" value="consultar">
        <input type="hidden" name="usuario" id="usuario" value="${usuario}">
        <input type="hidden" name="dtAtual" id="dtAtual" value="${dtAtual}">
        <input type="hidden" name="idAlteracao" id="idAlteracao">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <table class="fmt">
                      <tr>
                          <td>
                            <table class="fmt">
                                <tr>
                                    <td>
                                        <p class="legendaCodigo MargemSuperior0" >Nome</p>
                                        <input type="text" name="nome" class="campoSemTamanho alturaPadrao " style="width:280px" value="${nome}">
                                    </td>
                                    <td>
                                        <fieldset class="field-set legendaFrame recuoPadrao" style="width:185px;height:50px;margin-top:0px">
                                            <legend >Período:</legend>
                                            <input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero" style="margin-top:0px;" value="${dataInicio}">        
                                            &nbsp;&nbsp;&nbsp;a
                                            <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero" style="margin-top:0px;" value="${dataFim}"><br>

                                        </fieldset>
                                    </td>
                                    <td>
                                        <p class="legendaCodigo" style="margin-top:-20px">Usuários</p>
                                        <div class="selectheightnovo">
                                            <select name="lstUsuario" class="campoSemTamanho alturaPadrao" style="width:165px;" >
                                                <option value="" selected>&LT;TODOS&GT;</option>
                                                <c:forEach var="usu" items="${usuarios}">
                                                    <option value="${usu.cod}" <c:if test='${usu.cod == lstUsuario}'>selected</c:if>>${usu.descricao}</option>
                                                </c:forEach>
                                            </select>
                                        </div>        
                                        
                                    </td>
                                    <td>
                                        <input class="botaobuscainclusao"  type="button" onclick="validarForm()" style="margin-top:13px;margin-left:40px" value="" title="Consultar" />
                                        
                                    </td>
                                </tr>
                            </table>
                          </td>
                      </tr>
                      <tr>
                          <td>
                            <table class="fmt">
                                <tr>
                                    <td>
                                        <p class="legendaCodigo MargemSuperior0">Taxas:</p>
                                        <div class="selectheightnovo">
                                            <select name="taxa" class="campoSemTamanho alturaPadrao" style="width:280px;" >
                                                <option value="0" selected>&LT;TODAS&GT;</option>
                                                <c:forEach var="tx" items="${taxas}">
                                                    <option value="${tx.id}" <c:if test='${tx.id == taxa}'>selected</c:if>>${tx.descricao}</option>
                                                </c:forEach>
                                            </select>
                                        </div>        
                                    </td>
                                    <td>
                                        <fieldset class="field-set legendaFrame recuoPadrao" style="width:185px;height: 50px;margin-left:18px"">
                                            <legend >Situação:</legend>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="normal" id="normal" value="true"  style="margin-top:6px;" <c:if test='${normal || acao==null}'>checked</c:if>>Normal
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="cancelada" id="cancelada" value="true" style="margin-top:6px;" <c:if test='${cancelada  || acao==null}'>checked</c:if>>Cancelada<br>
                                        </fieldset>
                                    </td>
                                    
                                </tr>
                            </table>
                          </td>
                      </tr>
                  </table>
              </td>
              <td>
              </td>
            </tr>
            <tr>
                
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:mostraImpressao()"><img src="imagens/btn-imprimir.png" width="100" height="25" /></a>
                            </td>
                            <td>
                                <input type="checkbox" name="agruparUsuario" value="true" style="margin-top:0px;margin-left:16px" <c:if test='${carnePago}'>checked</c:if>>Agrupar por Usuário
                            </td>
                        </tr>
                    </table>
                </td>
                
                
            </tr>
        </table>
    
        <br><br><br>

        <c:if test='${acao=="consultar"}'>
            <sql:query var="rs" dataSource="jdbc/iate">
                SELECT 
                    T1.CD_FUNCIONARIO, 
                    T1.NOME_FUNCIONARIO, 
                    T2.DESCR_TX_ADMINISTRATIVA, 
                    T3.VR_MOVIMENTO, 
                    T3.DT_MOVIMENTO, 
                    T3.IC_SIT_MOVIMENTO, 
                    T3.USER_INCLUSAO, 
                    T3.DT_CANCELAMENTO, 
                    T3.USER_CANCELAMENTO, 
                    T3.NU_SEQ_PRE_PAGO
               FROM 
                    TB_FUNCIONARIO T1, 
                    TB_TAXA_ADMINISTRATIVA T2, 
                    TB_VAL_PRE_PAGO T3 
               WHERE 
                    T1.CD_FUNCIONARIO = T3.CD_FUNCIONARIO AND 
                    T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA  AND 
                    T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '${usuario}') AND
                    T3.DT_MOVIMENTO >= '${fn:substring(dataInicio,3,5)}/${fn:substring(dataInicio,0,2)}/${fn:substring(dataInicio,6,10)} 00:00:01' AND
                    T3.DT_MOVIMENTO <= '${fn:substring(dataFim,3,5)}/${fn:substring(dataFim,0,2)}/${fn:substring(dataFim,6,10)} 23:59:59'

                    <c:if test='${nome!=""}'>
                        AND T1.NOME_FUNCIONARIO LIKE '${nome}%'
                    </c:if>
                    <c:if test='${taxa>0}'>
                        AND T2.CD_TX_ADMINISTRATIVA = '${taxa}'
                    </c:if>
                    <c:if test='${lstUsuario!=""}'>
                        AND T3.USER_INCLUSAO = '${lstUsuario}'
                    </c:if>
                    <c:if test='${normal}'>
                        <c:if test='${cancelada==null}'>
                            AND T3.IC_SIT_MOVIMENTO = 'N'
                        </c:if>
                    </c:if>
                    <c:if test='${normal==null}'>
                        AND T3.IC_SIT_MOVIMENTO = 'C'
                    </c:if>
                        
                ORDER BY 7
            </sql:query>  
        </c:if>            

        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col">Sit.</th>
                    <th scope="col">Pessoa</th>
                    <th scope="col">Taxa</th>
                    <th scope="col">Valor</th>
                    <th scope="col">Dt. Geração</th>
                    <th scope="col">Incluído por</th>
                    <th scope="col">Dt. Canc.</th>
                    <th scope="col">Canc. Por</th>
                    <th scope="col">Cancelar</th>
                    <th scope="col">Imprimir</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="totalFinal" value="${0}" />

                <c:forEach var="taxaInd" items="${rs.rows}">
                    <c:if test='${taxaInd.IC_SIT_MOVIMENTO=="N"}'>
                        <c:set var="totalFinal" value="${totalFinal + taxaInd.VR_MOVIMENTO}" />
                    </c:if>
                    
                    <tr>
                        <td class="column1" align="center">
                            <c:if test='${taxaInd.IC_SIT_MOVIMENTO=="C"}'>
                                <img src="img/no.png"/>
                            </c:if>
                        </td>
                        <td class="column1">${taxaInd.NOME_FUNCIONARIO}</td>
                        <td class="column1">${taxaInd.DESCR_TX_ADMINISTRATIVA}</td>
                        <fmt:formatNumber var="valor"  value="${taxaInd.VR_MOVIMENTO}" pattern="#0.00"/>
                        <td class="column1" align="right">${valor}</td>
                        <fmt:formatDate var="dtGeracao" value="${taxaInd.DT_MOVIMENTO}" pattern="dd/MM/yyyy"/>
                        <td class="column1" align="center">${dtGeracao}</td>
                        <td class="column1" align="center">${taxaInd.USER_INCLUSAO}</td>
                        <fmt:formatDate var="dtCanc" value="${taxaInd.DT_CANCELAMENTO}" pattern="dd/MM/yyyy"/>
                        <td class="column1" align="center">${dtCanc}</td>
                        <td class="column1" align="center">${taxaInd.USER_CANCELAMENTO}</td>
                        <td align="center">
                            <c:choose>
                                <c:when test='${fn:toUpperCase(usuario)==fn:toUpperCase(taxaInd.USER_INCLUSAO) && dtGeracao == dtAtual}'>
                                    <c:if test='<%=request.isUserInRole("6191") || request.isUserInRole("6192")%>'>
                                        <c:if test='${taxaInd.IC_SIT_MOVIMENTO=="N"}'>
                                            <a href="#" onclick="cancelaTaxa(${taxaInd.NU_SEQ_PRE_PAGO})"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                                        </c:if>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    
                                    <c:if test='<%=request.isUserInRole("6192")%>'>
                                        <c:if test='${taxaInd.IC_SIT_MOVIMENTO=="N"}'>
                                            <a href="#" onclick="cancelaTaxa(${taxaInd.NU_SEQ_PRE_PAGO})"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                                        </c:if>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td align="center">    
                            <c:if test='${taxaInd.IC_SIT_MOVIMENTO=="C"}'>
                                <a href="c?app=6190&acao=reciboPRE&id=${taxaInd.NU_SEQ_PRE_PAGO}"><img src="imagens/icones/inclusao-titular-13.png"/></a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                    <fmt:formatNumber var="totalFinal2"  value="${totalFinal}" pattern="#,##0.00"/>
                    <tr>
                        <td></td>
                        <td></td>
                        <td class="column1"><b>TOTAL</b></td>
                        <td class="column1" align="right"><b>${totalFinal2}</b></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                        
            </tbody>
        </table>


        <div id="impressao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 400px; height:600px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Tipo de Impressao</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>
                            <td colspan="2">
                                <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                                &nbsp;&nbsp;<input type="radio" name="tipoRel" class="legendaCodigo" style="margin-top:12px" value="A" checked >Analítico
                                &nbsp;&nbsp;<input type="radio" name="tipoRel" class="legendaCodigo" style="margin-top:12px" value="S" <c:if test='${periodo=="C"}'>checked</c:if>>Sintético
                            </td>
                        <tr>
                        </tr>
                            <td>
                                <br>
                                <input style="margin-left:15px;" type="button" id="cmdAtualizar" name="cmdAtualizar" class="botaocancelar" onclick="cancelaImpressao()" />
                            </td>
                            <td>
                                <br>
                                <input style="margin-left:15px;margin-top:05px;" type="button" id="cmdCancelar" name="cmdCancelar" class="botaoatualizar" onclick="atualizaImpressao()"  />
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
