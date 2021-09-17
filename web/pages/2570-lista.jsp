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
            
            $('#mesAno').mask('99/9999');
            
    });        
</script>  

<script type="text/javascript" language="JavaScript">
    
    function excluiRepasse(id){
        if (confirm('Deseja realmente excluir o repasse do Concessionario?')){
            $.ajax({url:'RepasseConcessionarioAjax', async:false, dataType:'text', type:'POST',data:{
                                tipo:2,
                                id:id,
                                usuario:$('#usuario').val()}
            }).success(function(retorno){
                alert('Repasse excluído com sucesso!');
            });
            
            document.forms[0].submit();
            
        }
    }

    function validaInclusao(){
        if($('#mesAno').val()==''){
            alert('Informe o mês e ano de referência');
            return false;
        }
        
        if(!isMesAnoValido($('#mesAno').val())){
            return;
        }
        
        $('#app').val('2571');
        
        document.forms[0].submit();
        
    }    

    function validarForm(){
        
        if($('#mesAno').val()==''){
            alert('Informe o mês e ano de referência');
            return false;
        }
        
        if(!isMesAnoValido($('#mesAno').val())){
            return;
        }
        
        document.forms[0].submit();
    }    

</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Taxas Individuais - Analítico</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" id="app"  value="2570">
        <input type="hidden" name="acao" id="acao" value="consultar">
        <input type="hidden" name="usuario" id="usuario" value="${usuario}">
        <input type="hidden" name="mes" id="mes" value="${mes}">
        <input type="hidden" name="ano" id="ano" value="${ano}">
        <input type="hidden" name="csSel" id="csSel" value="${csSel}">
        
        <sql:query var="rsConcessionarios" dataSource="jdbc/iate">
            SELECT
                RIGHT('00' + CONVERT(VARCHAR, CD_CATEGORIA), 2) +
                RIGHT('0000' + CONVERT(VARCHAR, CD_MATRICULA), 4) +
                RIGHT('00' + CONVERT(VARCHAR, SEQ_DEPENDENTE), 2) ID,
                NOME_PESSOA
            FROM
                TB_PESSOA
            WHERE
                CD_CATEGORIA = 96
            ORDER BY
                NOME_PESSOA
        </sql:query>  
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                    <p class="legendaCodigo MargemSuperior0">Concessionarios:</p>
                    <div class="selectheightnovo">
                        <select name="concessionario" class="campoSemTamanho alturaPadrao" style="width:220px;" >
                            <c:forEach var="cs" items="${rsConcessionarios.rows}">
                                <option value="${cs.ID}" <c:if test='${cs.ID == csSel}'>selected</c:if>>${cs.NOME_PESSOA}</option>
                            </c:forEach>
                        </select>
                    </div>        
              </td>
              <td>
                    <p class="legendaCodigo MargemSuperior0" >Mes/Ano</p>
                    <input type="text" id="mesAno" name="mesAno" class="campoSemTamanho alturaPadrao" style="width:60px;" value="${mesAno}">
              </td>
              <td>
                  <input class="botaobuscainclusao"  type="button" onclick="validarForm()" style="margin-top: 20px; margin-left:20px" value="" title="Consultar" />
                  
              </td>
              <td>
                  <a href="javascript:validaInclusao()" ><img src="imagens/btn-incluir.png" width="100" height="25" style="margin-top:20px;margin-left:20px"/></a>
              </td>
            </tr>
        </table>
    
        <c:if test='${acao=="consultar"}'>
            <sql:query var="rs" dataSource="jdbc/iate">
                SELECT 
                    ISNULL(T1.NOME_PESSOA, 'EXCLUIDO') NOME_PESSOA, 
                    T2.DESCR_TX_ADMINISTRATIVA, 
                    T3.VR_TAXA, 
                    T3.DT_GERACAO_TAXA, 
                    T3.MM_COBRANCA, 
                    T3.AA_COBRANCA, 
                    T3.IC_SITUACAO_TAXA, 
                    T4.NU_SEQ_REPASSE_CONCESSIONARIO
               FROM 
                    TB_PESSOA T1, 
                    TB_TAXA_ADMINISTRATIVA T2, 
                    TB_VAL_TAXA_INDIVIDUAL T3,
                    TB_REPASSE_CONCESSIONARIO T4
               WHERE 
                    T1.CD_MATRICULA =* T3.CD_MATRICULA AND 
                    T1.CD_CATEGORIA =* T3.CD_CATEGORIA AND 
                    T1.SEQ_DEPENDENTE =* T3.SEQ_DEPENDENTE AND 
                    T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA  AND 

                    T4.NU_SEQ_TAXA_INDIVIDUAL = T3.NU_SEQ_TAXA_INDIVIDUAL AND

                    T4.CD_CATEGORIA = ${categoriaCS} AND 
                    T4.CD_MATRICULA = ${matriculaCS} AND
                    T4.SEQ_DEPENDENTE = ${dependenteCS} AND

                    T4.MM_REFERENCIA = ${mes} AND
                    T4.AA_REFERENCIA =  ${ano} 
                        
                UNION ALL
                
                SELECT 
                    ISNULL(T1.NOME_PESSOA, 'EXCLUIDO') NOME_PESSOA, 
                    T2.DESCR_TX_ADMINISTRATIVA, 
                    T3.VR_MOVIMENTO VR_TAXA, 
                    T3.DT_MOVIMENTO DT_GERACAO_TAXA, 
                    NULL, 
                    NULL, 
                    T3.IC_SIT_MOVIMENTO IC_SITUACAO_TAXA, 
                    T4.NU_SEQ_REPASSE_CONCESSIONARIO
                FROM 
                    TB_PESSOA T1, 
                    TB_TAXA_ADMINISTRATIVA T2, 
                    TB_VAL_PRE_PAGO T3 ,
                    TB_REPASSE_CONCESSIONARIO T4
               WHERE 
                    T1.CD_MATRICULA =* T3.CD_MATRICULA AND 
                    T1.CD_CATEGORIA =* T3.CD_CATEGORIA AND 
                    T1.SEQ_DEPENDENTE =* T3.SEQ_DEPENDENTE AND 
                    T2.CD_TX_ADMINISTRATIVA = T3.CD_TX_ADMINISTRATIVA  AND 

                    T4.NU_SEQ_PRE_PAGO = T3.NU_SEQ_PRE_PAGO AND

                    T4.CD_CATEGORIA = ${categoriaCS} AND 
                    T4.CD_MATRICULA = ${matriculaCS} AND
                    T4.SEQ_DEPENDENTE = ${dependenteCS} AND

                    T4.MM_REFERENCIA = ${mes} AND
                    T4.AA_REFERENCIA =  ${ano} 
                        
                ORDER BY 4, 1
                
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
                    <th scope="col">Retirar do Repasse</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="totalFinal" value="${0}" />

                <c:forEach var="taxaInd" items="${rs.rows}">
                    <c:set var="totalFinal" value="${totalFinal + taxaInd.VR_TAXA}" />
                    
                    <tr>
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
                        <fmt:formatNumber var="mesReg" value="${taxaInd.MM_COBRANCA}" pattern="00"/>
                        <td class="column1" align="center" >${mesReg}/${taxaInd.AA_COBRANCA}</td>
                        <td align="center">
                            <a href="#" onclick="excluiRepasse(${taxaInd.NU_SEQ_REPASSE_CONCESSIONARIO})"><img src="imagens/icones/inclusao-titular-05.png" /></a>
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
                    </tr>
                        
            </tbody>
        </table>

    </form>
    
</body>
</html>
