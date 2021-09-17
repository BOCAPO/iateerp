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
            
            $("#botaoatualizar").click(function(){
                
                $("#botaoatualizar").hide();
                
                setTimeout(function() {
                    atualiza();
                }, 1);
                
//                
            });      
    });     
    
</script>  

<script type="text/javascript" language="JavaScript">
        function atualiza(){
            var taxas = '';
            
            $('[name=selTaxa]').each(function(){
                if ($(this).attr('checked')){
                    taxas = taxas + $(this).attr('id') + ';';
                }
            });            
            
            if (taxas == ''){
                alert('Nenhuma taxa selecionada para vinculação!');
                $("#botaoatualizar").show();
                return false;
            }

            $.ajax({url:'RepasseConcessionarioAjax', type:'POST', async:false,
                                data:{
                                tipo:1,
                                usuario:$('#usuario').val(),
                                matricula:$('#matriculaCS').val(),
                                categoria:$('#categoriaCS').val(),
                                dependente:$('#dependenteCS').val(),
                                mes:$('#mes').val(),
                                ano:$('#ano').val(),
                                taxas:taxas
                                }
            }).success(function(){});

            alert('Vinculação realizada com sucesso!');
        
            $('#acao2').val('consultar');

            document.forms[0].submit();
        
        
        }
        
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
            
            $('#acao2').val('consultar');

            document.forms[0].submit();
        }
        
    function selecionaTudo(nome){        
        $('[name='+nome+']').each(function(){
            if ($('#marcaDesmarcaGeral').attr('checked')){
                $(this).attr('checked', true);
            }else{
                $(this).attr('checked', false);
            }
            
        });            
    }
        
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">
        Concessionário: ${s.nome}
        <br>
        Referência: ${mes} / ${ano}
    </div>
    <div class="divisoria"></div>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Seleção de Taxas para vinculção ao Repasse do Concessionário</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="2571">
        <input type="hidden" name="acao2" id="acao2" value="consultar">
        <input type="hidden" name="matriculaCS" id="matriculaCS" value="${s.matricula}">
        <input type="hidden" name="categoriaCS" id="categoriaCS" value="${s.idCategoria}">
        <input type="hidden" name="dependenteCS" id="dependenteCS" value="${s.seqDependente}">
        <input type="hidden" name="mes" id="mes" value="${mes}">
        <input type="hidden" name="ano" id="ano" value="${ano}">
        <input type="hidden" name="mesAno" id="mesAno" value="${mesAno}">
        <input type="hidden" name="concessionario" id="concessionario" value="${concessionario}">
        <input type="hidden" name="usuario" id="usuario" value="${usuario}">

        
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
                                            &nbsp<input type="checkbox" name="normal" id="normal" value="true" <c:if test='${normal || acao2==null}'>checked</c:if>>Normal
                                            &nbsp<input type="checkbox" name="cancelada" id="cancelada" value="true" <c:if test='${cancelada  || acao2==null}'>checked</c:if>>Cancelada<br>
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
                                &nbsp;&nbsp;<input type="checkbox" name="contaPos" id="contaPos" value="true" <c:if test='${contaPos || acao2==null}'>checked</c:if>>Pós-Pago
                                &nbsp;&nbsp;<input type="checkbox" name="contaPre" id="contaPre" value="true" <c:if test='${contaPre  || acao2==null}'>checked</c:if>>Pre-Pago<br>
                            </fieldset>
                        </td>
                    </tr>
                </table>
              </td>
              <td>
                  <input class="botaobuscainclusao"  type="button" onclick="validarForm()" style="margin-top:-12px;margin-left:20px" value="" title="Consultar" />
                  
              </td>
            </tr>
        </table>
    
        <c:if test='${acao2=="consultar"}'>
            <sql:query var="rs" dataSource="jdbc/iate">
                <c:if test='${contaPos}'>
                    SELECT 
                        T3.CD_MATRICULA, 
                        T3.CD_CATEGORIA, 
                        T3.SEQ_DEPENDENTE, 
                        ISNULL(T1.NOME_PESSOA, 'EXCLUIDO') NOME_PESSOA, 
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
                        T1.CD_MATRICULA =* T3.CD_MATRICULA AND 
                        T1.CD_CATEGORIA =* T3.CD_CATEGORIA AND 
                        T1.SEQ_DEPENDENTE =* T3.SEQ_DEPENDENTE AND 
                        T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA  AND 
                        T3.NU_SEQ_TAXA_INDIVIDUAL NOT IN (SELECT NU_SEQ_TAXA_INDIVIDUAL FROM TB_REPASSE_CONCESSIONARIO WHERE NU_SEQ_TAXA_INDIVIDUAL IS NOT NULL) 
                        <c:if test='${categoria!=""}'>
                            AND T3.CD_CATEGORIA = ${categoria}
                        </c:if>
                        <c:if test='${matricula!=""}'>
                            AND T3.CD_MATRICULA = ${matricula}
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
                </c:if>
                <c:if test='${contaPos && contaPre}'>
                    UNION ALL
                </c:if>
                <c:if test='${contaPre}'>
                    SELECT 
                        T3.CD_MATRICULA, 
                        T3.CD_CATEGORIA, 
                        T3.SEQ_DEPENDENTE, 
                        ISNULL(T1.NOME_PESSOA, 'EXCLUIDO') NOME_PESSOA, 
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
                        T1.CD_MATRICULA =* T3.CD_MATRICULA AND 
                        T1.CD_CATEGORIA =* T3.CD_CATEGORIA AND 
                        T1.SEQ_DEPENDENTE =* T3.SEQ_DEPENDENTE AND 
                        T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA  AND 
                        T3.NU_SEQ_PRE_PAGO NOT IN (SELECT NU_SEQ_PRE_PAGO FROM TB_REPASSE_CONCESSIONARIO WHERE NU_SEQ_PRE_PAGO IS NOT NULL) AND

                        T3.DT_MOVIMENTO >= '${fn:substring(dataInicio,3,5)}/${fn:substring(dataInicio,0,2)}/${fn:substring(dataInicio,6,10)} 00:00:01' AND
                        T3.DT_MOVIMENTO <= '${fn:substring(dataFim,3,5)}/${fn:substring(dataFim,0,2)}/${fn:substring(dataFim,6,10)} 23:59:59'
                        
                        <c:if test='${categoria!=""}'>
                            AND T3.CD_CATEGORIA = ${categoria}
                        </c:if>
                        <c:if test='${matricula!=""}'>
                            AND T3.CD_MATRICULA = ${matricula}
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
                    <th scope="col"><input type="checkbox" onclick="selecionaTudo('selTaxa')" name="marcaDesmarcaGeral" id="marcaDesmarcaGeral" value="true"></th>
                    <th scope="col">Sit.</th>
                    <th scope="col">Pessoa</th>
                    <th scope="col">Taxa</th>
                    <th scope="col">Valor</th>
                    <th scope="col">Dt. Geração</th>
                    <th scope="col">Cobrar em</th>
                    <th scope="col">Incluído por</th>
                    <th scope="col">Dt. Canc.</th>
                    <th scope="col">Canc. Por</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="totalFinal" value="${0}" />

                <c:forEach var="taxaInd" items="${rs.rows}">
                    <c:if test='${taxaInd.IC_SITUACAO_TAXA=="N"}'>
                        <c:set var="totalFinal" value="${totalFinal + taxaInd.VR_TAXA}" />
                    </c:if>
                    
                    <tr>
                        <th scope="col"><input type="checkbox" name="selTaxa" id="${taxaInd.TIPO}${taxaInd.NU_SEQ_TAXA_INDIVIDUAL}" value="true"></th>                        
                        
                        <td class="column1" align="center">
                            <c:if test='${taxaInd.IC_SITUACAO_TAXA=="C"}'>
                                <img src="img/no.png"/>
                            </c:if>
                        </td>
                        <td class="column1">${taxaInd.NOME_PESSOA}</td>
                        <td class="column1">${taxaInd.DESCR_TX_ADMINISTRATIVA}</td>
                        <fmt:formatNumber var="valor"  value="${taxaInd.VR_TAXA}" pattern="#0.00"/>
                        <td class="column1" align="right">${valor}</td>
                        <fmt:formatDate var="dtGeracao" value="${taxaInd.DT_GERACAO_TAXA}" pattern="dd/MM/yyyy"/>
                        <td class="column1" align="center">${dtGeracao}</td>
                        <fmt:formatNumber var="mesReg"  value="${taxaInd.MM_COBRANCA}" pattern="00"/>
                        <td class="column1" align="center" >${mesReg}/${taxaInd.AA_COBRANCA}</td>
                        <td class="column1" align="center">${taxaInd.USER_INCLUSAO}</td>
                        <fmt:formatDate var="dtCanc" value="${taxaInd.DT_CANCELAMENTO_TAXA}" pattern="dd/MM/yyyy"/>
                        <td class="column1" align="center">${dtCanc}</td>
                        <td class="column1" align="center">${taxaInd.USER_CANCELAMENTO}</td>
                    </tr>
                </c:forEach>
                    <fmt:formatNumber var="totalFinal2"  value="${totalFinal}" pattern="#,##0.00"/>
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td class="column1"><b>TOTAL</b></td>
                        <td class="column1" align="right"><b>${totalFinal2}</b></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                        
            </tbody>
        </table>
                        
        <input type="button" id="botaoatualizar" class="botaoatualizar"  value=" " />
        
        <input type="button" id="botaoVoltar" class="botaoVoltar"  onclick="window.location='c?app=2570&mesAno=${mesAno}&concessionario=${concessionario}';" value=" " />
                        
                        
    </form>
    
</body>
</html>
