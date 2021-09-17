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
            if(!$("#contaPre").prop('checked') && !$("#contaPos").prop('checked')){
                alert("Informe pelo menos um tipo!");
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
            if(!isDataValida(document.forms[0].dataInicio.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataFim.value)){
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
                                    conta:tp,
                                    usuario:$('#usuario').val()}
                }).success(function(retorno){
                    alert('Taxa cancelada com sucesso!');
                });
                document.forms[0].submit();
            }
            
            
        }
        
        function cancelaAlteracao(){
            $('#alteracao').hide();
        }
        
        function mostraAlteracao(id){
            $('#mesAno').val($('#tabCobranca'+id).html());
            $('#idAlteracao').val(id);
            $('#alteracao').show();
        }
        
        function atualizaAlteracao(){
            if(!isMesAnoValido($('#mesAno').val())){
                return;
            }
            
            $.ajax({url:'TaxaIndividualAjax', async:false, dataType:'text', type:'GET',data:{
                                    tipo:2,
                                    taxa:$('#idAlteracao').val(),
                                    mes:$('#mesAno').val().substring(0,2),
                                    ano:$('#mesAno').val().substring(3)}
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
    <div id="titulo-subnav">Relatório de Taxas Individuais - Analítico</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="6190">
        <input type="hidden" name="acao" id="acao" value="consultar">
        <input type="hidden" name="usuario" id="usuario" value="${usuario}">
        <input type="hidden" name="dtAtual" id="dtAtual" value="${dtAtual}">
        <input type="hidden" name="idAlteracao" id="idAlteracao">
        
        <input type="hidden" name="lstUsuarioh" id="lstUsuarioh" value="${lstUsuario}">
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <table class="fmt">
                      <tr>
                          <td>
                            <table class="fmt">
                                <tr>
                                      <td>
                                          <p class="legendaCodigo MargemSuperior0" >Categoria</p>
                                          <input type="text" name="categoria" class="campoSemTamanho alturaPadrao larguraNumero" value="${categoria}">
                                      </td>
                                      <td>
                                          <p class="legendaCodigo MargemSuperior0" >Título</p>
                                          <input type="text" name="matricula" class="campoSemTamanho alturaPadrao larguraNumero" value="${matricula}">
                                      </td>
                                      <td>
                                          <p class="legendaCodigo MargemSuperior0" >Nome</p>
                                          <input type="text" name="nome" class="campoSemTamanho alturaPadrao " style="width:227px" value="${nome}">
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
                                            <select name="taxa" class="campoSemTamanho alturaPadrao" style="width:220px;" >
                                                <option value="0" selected>&LT;TODAS&GT;</option>
                                                <c:forEach var="tx" items="${taxas}">
                                                    <option value="${tx.id}" <c:if test='${tx.id == taxa}'>selected</c:if>>${tx.descricao}</option>
                                                </c:forEach>
                                            </select>
                                        </div>        
                                    </td>
                                    <td>
                                        <fieldset class="field-set legendaFrame recuoPadrao" style="width:145px;height: 40px">
                                            <legend >Situação:</legend>
                                            &nbsp<input type="checkbox" name="normal" id="normal" value="true" <c:if test='${normal || acao==null}'>checked</c:if>>Normal
                                            &nbsp<input type="checkbox" name="cancelada" id="cancelada" value="true" <c:if test='${cancelada  || acao==null}'>checked</c:if>>Cancelada<br>
                                        </fieldset>
                                    </td>
                                </tr>
                            </table>
                          </td>
                      </tr>
                  </table>
              </td>
              <td>
                <fieldset class="field-set legendaFrame recuoPadrao" style="width:190px;height:92px;margin-top:-20px">
                    <legend >Período:</legend>
                    <input type="text" id="dataInicio" name="dataInicio" class="campoSemTamanho alturaPadrao larguraNumero" style="margin-top:10px;" value="${dataInicio}">        
                    &nbsp;&nbsp;&nbsp;a
                    <input type="text" id="dataFim"  name="dataFim" class="campoSemTamanho alturaPadrao larguraNumero" style="margin-top:10px;" value="${dataFim}"><br>
                    &nbsp;&nbsp;<input type="radio" name="periodo" class="legendaCodigo" style="margin-top:12px" value="G" checked >Geração
                    &nbsp;&nbsp;<input type="radio" name="periodo" class="legendaCodigo" style="margin-top:12px" value="C" <c:if test='${periodo=="C"}'>checked</c:if>>Cobrança
                    
                </fieldset>
                  
              </td>
              
              <td>
                <table class="fmt">
                    <tr>
                        <td>
                            <p class="legendaCodigo" style="margin-top:-30px">Usuários</p>
                            <div class="selectheightnovo">
                                <select name="lstUsuario" class="campoSemTamanho alturaPadrao" style="width:165px;" >
                                    <option value="" selected>&LT;TODOS&GT;</option>
                                    <c:forEach var="usu" items="${usuarios}">
                                        <option value="${usu.cod}" <c:if test='${usu.cod == lstUsuario}'>selected</c:if>>${usu.descricao}</option>
                                    </c:forEach>
                                </select>
                            </div>        
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <fieldset class="field-set legendaFrame recuoPadrao" style="width:160px;height:40px;margin-top:12px">
                                <legend >Tipo:</legend>
                                &nbsp;&nbsp;<input type="checkbox" name="contaPos" id="contaPos" value="true" <c:if test='${contaPos || acao==null}'>checked</c:if>>Pós-Pago
                                &nbsp;&nbsp;<input type="checkbox" name="contaPre" id="contaPre" value="true" <c:if test='${contaPre  || acao==null}'>checked</c:if>>Pre-Pago<br>
                            </fieldset>
                        </td>
                    </tr>
                </table>
              </td>
              <td>
                  <input class="botaobuscainclusao"  type="button" onclick="validarForm()" style="margin-top:-12px;margin-left:20px" value="" title="Consultar" />
                  <br><br>
                  &nbsp<input type="checkbox" name="carnePago" value="true" style="margin-top:-5px;margin-left:16px" <c:if test='${carnePago}'>checked</c:if>>Carnes Pagos
                  
              </td>
            </tr>
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <a href="javascript:mostraImpressao()" style="margin-top:0px;margin-left:16px"><img src="imagens/btn-imprimir.png" width="100" height="25" /></a>
                            </td>
                            <td>
                                <input type="checkbox" name="agruparUsuario" value="true" style="margin-top:0px;margin-left:16px" <c:if test='${carnePago}'>checked</c:if>>Agrupar por Usuário
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    
        <c:if test='${acao=="consultar"}'>
            <sql:query var="rs" dataSource="jdbc/iate">
                <c:if test='${contaPos}'>
                    SELECT 
                        T1.CD_MATRICULA, 
                        T1.CD_CATEGORIA, 
                        T1.SEQ_DEPENDENTE, 
                        T1.NOME_PESSOA, 
                        T2.DESCR_TX_ADMINISTRATIVA, 
                        T3.VR_TAXA, 
                        T3.DT_GERACAO_TAXA, 
                        T3.MM_COBRANCA, 
                        T3.AA_COBRANCA, 
                        T3.IC_SITUACAO_TAXA, 
                        T3.USER_INCLUSAO, 
                        T3.DT_CANCELAMENTO_TAXA, 
                        T3.USER_CANCELAMENTO, 
                        T3.NU_SEQ_TAXA_INDIVIDUAL,
                        'POS' TIPO
                   FROM 
                        TB_PESSOA T1, 
                        TB_TAXA_ADMINISTRATIVA T2, 
                        TB_VAL_TAXA_INDIVIDUAL T3 
                   WHERE 
                        T1.CD_MATRICULA = T3.CD_MATRICULA AND 
                        T1.CD_CATEGORIA = T3.CD_CATEGORIA AND 
                        T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND 
                        T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA  AND 
                        T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '${usuario}')
                        <c:if test='${categoria!=""}'>
                            AND T1.CD_CATEGORIA = ${categoria}
                        </c:if>
                        <c:if test='${matricula!=""}'>
                            AND T1.CD_MATRICULA = ${matricula}
                        </c:if>
                        <c:if test='${nome!=""}'>
                            AND T1.NOME_PESSOA LIKE '${nome}%'
                        </c:if>
                        <c:if test='${taxa>0}'>
                            AND T2.CD_TX_ADMINISTRATIVA = '${taxa}'
                        </c:if>
                        <c:if test='${lstUsuario!=""}'>
                            AND T3.USER_INCLUSAO = '${lstUsuario}'
                        </c:if>
                        <c:if test='${normal}'>
                            <c:if test='${cancelada==null}'>
                                AND T3.IC_SITUACAO_TAXA = 'N'
                            </c:if>
                        </c:if>
                        <c:if test='${normal==null}'>
                            AND T3.IC_SITUACAO_TAXA = 'C'
                        </c:if>
                        <c:if test='${periodo=="G"}'>
                            AND T3.DT_GERACAO_TAXA >= '${fn:substring(dataInicio,3,5)}/${fn:substring(dataInicio,0,2)}/${fn:substring(dataInicio,6,10)} 00:00:01'
                            AND T3.DT_GERACAO_TAXA <= '${fn:substring(dataFim,3,5)}/${fn:substring(dataFim,0,2)}/${fn:substring(dataFim,6,10)} 23:59:59'
                        </c:if>
                        <c:if test='${periodo=="C"}'>
                            AND CONVERT(VARCHAR, T3.AA_COBRANCA) + RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) >= '${fn:substring(dataInicio,6,10)}${fn:substring(dataInicio,3,5)}'
                            AND CONVERT(VARCHAR, T3.AA_COBRANCA) + RIGHT('0' + CONVERT(VARCHAR, T3.MM_COBRANCA), 2) <= '${fn:substring(dataFim,6,10)}${fn:substring(dataFim,3,5)}'
                        </c:if>
                        <c:if test='${carnePago}'>
                            AND EXISTS
                                (
                                SELECT
                                      1
                                From
                                      VW_CARNE_PG T4
                                Where
                                      T1.CD_MATRICULA   = T4.CD_MATRICULA          AND
                                      T1.CD_CATEGORIA   = T4.CD_CATEGORIA          AND
                                      T3.MM_COBRANCA    = T4.MM_VENCIMENTO         AND
                                      T3.AA_COBRANCA    = T4.AA_VENCIMENTO
                                )
                        </c:if>
                </c:if>
                <c:if test='${contaPos && contaPre}'>
                    UNION ALL
                </c:if>
                <c:if test='${contaPre}'>
                    SELECT 
                        T1.CD_MATRICULA, 
                        T1.CD_CATEGORIA, 
                        T1.SEQ_DEPENDENTE, 
                        T1.NOME_PESSOA, 
                        T2.DESCR_TX_ADMINISTRATIVA, 
                        T3.VR_MOVIMENTO VR_TAXA, 
                        T3.DT_MOVIMENTO DT_GERACAO_TAXA, 
                        NULL, 
                        NULL, 
                        T3.IC_SIT_MOVIMENTO IC_SITUACAO_TAXA, 
                        T3.USER_INCLUSAO, 
                        T3.DT_CANCELAMENTO DT_CANCELAMENTO_TAXA, 
                        T3.USER_CANCELAMENTO, 
                        T3.NU_SEQ_PRE_PAGO NU_SEQ_TAXA_INDIVIDUAL,
                        'PRE' TIPO
                   FROM 
                        TB_PESSOA T1, 
                        TB_TAXA_ADMINISTRATIVA T2, 
                        TB_VAL_PRE_PAGO T3 
                   WHERE 
                        T1.CD_MATRICULA = T3.CD_MATRICULA AND 
                        T1.CD_CATEGORIA = T3.CD_CATEGORIA AND 
                        T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND 
                        T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA  AND 
                        T2.CD_TX_ADMINISTRATIVA IN (SELECT CD_TX_ADMINISTRATIVA FROM TB_USUARIO_TAXA_INDIVIDUAL WHERE USER_ACESSO_SISTEMA = '${usuario}') AND
                        T3.DT_MOVIMENTO >= '${fn:substring(dataInicio,3,5)}/${fn:substring(dataInicio,0,2)}/${fn:substring(dataInicio,6,10)} 00:00:01' AND
                        T3.DT_MOVIMENTO <= '${fn:substring(dataFim,3,5)}/${fn:substring(dataFim,0,2)}/${fn:substring(dataFim,6,10)} 23:59:59'
                        
                        <c:if test='${categoria!=""}'>
                            AND T1.CD_CATEGORIA = ${categoria}
                        </c:if>
                        <c:if test='${matricula!=""}'>
                            AND T1.CD_MATRICULA = ${matricula}
                        </c:if>
                        <c:if test='${nome!=""}'>
                            AND T1.NOME_PESSOA LIKE '${nome}%'
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
                    <th scope="col">Cobrar em</th>
                    <th scope="col">Incluído por</th>
                    <th scope="col">Dt. Canc.</th>
                    <th scope="col">Canc. Por</th>
                    <th scope="col">Alterar</th>
                    <th scope="col">Cancelar</th>
                    <th scope="col">Imprimir</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="totalFinal" value="${0}" />

                <c:forEach var="taxaInd" items="${rs.rows}">
                    <c:if test='${taxaInd.IC_SITUACAO_TAXA=="N"}'>
                        <c:set var="totalFinal" value="${totalFinal + taxaInd.VR_TAXA}" />
                    </c:if>
                    
                    <tr>
                        <td class="column1" align="center">
                            <c:if test='${taxaInd.IC_SITUACAO_TAXA=="C"}'>
                                <img src="img/no.png"/>
                            </c:if>
                        </td>

                        <fmt:formatNumber var="titulo"  value="${taxaInd.CD_MATRICULA}" pattern="0000"/>
                        <fmt:formatNumber var="categoria"  value="${taxaInd.CD_CATEGORIA}" pattern="00"/>

                        <td class="column1">${categoria}/${titulo} - ${taxaInd.NOME_PESSOA}</td>
                        <td class="column1">${taxaInd.DESCR_TX_ADMINISTRATIVA}</td>
                        <fmt:formatNumber var="valor"  value="${taxaInd.VR_TAXA}" pattern="#0.00"/>
                        <td class="column1" align="right">${valor}</td>
                        <fmt:formatDate var="dtGeracao" value="${taxaInd.DT_GERACAO_TAXA}" pattern="dd/MM/yyyy"/>
                        <td class="column1" align="center">${dtGeracao}</td>
                        <fmt:formatNumber var="mes"  value="${taxaInd.MM_COBRANCA}" pattern="00"/>
                        <td class="column1" align="center" id="tabCobranca${taxaInd.NU_SEQ_TAXA_INDIVIDUAL}">${mes}/${taxaInd.AA_COBRANCA}</td>
                        <td class="column1" align="center">${taxaInd.USER_INCLUSAO}</td>
                        <fmt:formatDate var="dtCanc" value="${taxaInd.DT_CANCELAMENTO_TAXA}" pattern="dd/MM/yyyy"/>
                        <td class="column1" align="center">${dtCanc}</td>
                        <td class="column1" align="center">${taxaInd.USER_CANCELAMENTO}</td>
                        <td align="center">    
                            <c:if test='<%=request.isUserInRole("6193")%>'>
                                <c:if test='${taxaInd.IC_SITUACAO_TAXA=="N" && taxaInd.TIPO=="POS"}'>
                                    <a href="#" onclick="mostraAlteracao(${taxaInd.NU_SEQ_TAXA_INDIVIDUAL})"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                                </c:if>
                            </c:if>
                        </td>
                        <td align="center">
                            <c:choose>
                                <c:when test='${fn:toUpperCase(usuario)==fn:toUpperCase(taxaInd.USER_INCLUSAO) && dtGeracao == dtAtual}'>
                                    <c:if test='<%=request.isUserInRole("6191") || request.isUserInRole("6192")%>'>
                                        <c:if test='${taxaInd.IC_SITUACAO_TAXA=="N"}'>
                                            <a href="#" onclick="cancelaTaxa(${taxaInd.NU_SEQ_TAXA_INDIVIDUAL}, '${taxaInd.TIPO}')"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                                        </c:if>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    
                                    <c:if test='<%=request.isUserInRole("6192")%>'>
                                        <c:if test='${taxaInd.IC_SITUACAO_TAXA=="N"}'>
                                            <a href="#" onclick="cancelaTaxa(${taxaInd.NU_SEQ_TAXA_INDIVIDUAL}, '${taxaInd.TIPO}')"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                                        </c:if>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td align="center">    
                            <c:if test='${taxaInd.IC_SITUACAO_TAXA=="C"}'>
                                <a href="c?app=6190&acao=recibo${taxaInd.TIPO}&id=${taxaInd.NU_SEQ_TAXA_INDIVIDUAL}"><img src="imagens/icones/inclusao-titular-13.png"/></a>
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
                        <td></td>
                        <td></td>
                    </tr>
                        
            </tbody>
        </table>


        <div id="alteracao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 500px; height:600px;">
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Alteração de Taxa Individual</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>
                            <td colspan="2">
                                <p class="legendaCodigo MargemSuperior0" >Mes/Ano</p>
                                <input type="text" id="mesAno" name="mesAno" class="campoSemTamanho alturaPadrao" style="width:60px;" value="">
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
