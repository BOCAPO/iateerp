<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@include file="head.jsp"%>

<style type="text/css">
    .tipoBoleto {position:absolute; left:2.5%;}
    .imprimir {position:absolute; top:162px; left:300px;}
    .incluir {position:absolute; top:162px; left:420px;}

    .fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    .fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">
    <script type="text/javascript" language="javascript">
        $(document).ready(function() {
            $('#tabela tr:gt(0)').css('background', 'white');
            $('#tabela2 tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                $(this).css('background', '#f4f9fe');
            }, function() {
                $(this).css('background', 'white');
            })
            $('#tabela2 tr:gt(0)').hover(function() {
                $(this).css('background', '#f4f9fe');
            }, function() {
                $(this).css('background', 'white');
            })

            $("#incluiCredito").hide();
        });
        function gravaCredito() {
            var mes = parseInt($('#mes').val());
            if (mes == 0 || isNaN(mes) || mes > 12) {
                alert('Informe o mês de cobrança corretamente!');
                return false;
            }

            var ano = parseInt($('#ano').val());
            if (ano == 0 || isNaN(ano) || ano < 2016) {
                alert('Informe o ano de cobrança corretamente!');
                return false;
            }

            var valor = parseFloat($('#valorCredito').val().replace('.', '').replace(',', '.'));
            if (valor == 0 || isNaN(valor)) {
                alert('Informe o valor do crédito!');
                return false;
            }

            var ret = '';
            var valor = parseFloat($('#valorCredito').val().replace('.', '').replace(',', '.'));
            $.ajax({url: 'CreditoPessoaAjax', type: 'GET', async: false,
                data: {
                    tipo: 1,
                    taxa: $('#cdTxAdministrativa').val(),
                    matricula: $('#matricula').val(),
                    categoria: $('#idCategoria').val(),
                    dependente: 0,
                    mes: $('#mes').val(),
                    ano: $('#ano').val(),
                    valor: valor,
                    usuario: $('#usuarioAtual').val(),
                    observacao: encodeURIComponent($('#observacao').val())
                }
            }).success(function(retorno) {
                ret = retorno;
            });

            if (ret == 'OK') {
                alert('Crédito incluído com sucesso!');
            } else {
                alert('Erro na inclusão do crédito!');
            }

            document.forms[0].submit();
        }
        function cancelaInclusao() {
            $("#incluiCredito").hide();
        }

        function cancelaCredito(id) {
            var ret = '';
            $.ajax({url: 'CreditoPessoaAjax', type: 'GET', async: false,
                data: {
                    tipo: 2,
                    id: id,
                    usuario: $('#usuarioAtual').val()
                }
            }).success(function(retorno) {
                ret = retorno;
            });

            if (ret == 'OK') {
                alert('Crédito cancelado com sucesso!');
            } else {
                alert('Erro no cancelamento do crédito!');
            }

            document.forms[0].submit();
        }
    </script>   

    <%@include file="menu.jsp"%>

    <form action="c">
        <input type="hidden" name="app" id="app" value="1360">
        <input type="hidden" name="matricula" id="matricula" value="${matricula}">
        <input type="hidden" name="idCategoria" id="idCategoria" value="${idCategoria}">
        <input type="hidden" name="seqDependente" id="seqDependente" value="0">
        <input type="hidden" id="usuarioAtual" name="usuarioAtual" value="${usuarioAtual}">


        <div class="divisoria"></div>
        <div id="titulo-subnav">Carnês - ${idCategoria} / ${matricula} - ${socio.nome}</div>
        <div class="divisoria"></div>

        <div class="imprimir">
            <a href="c?app=1360&acao=showFormImprimirCarteira&matricula=${matricula}&idCategoria=${idCategoria}&tipo=${tipo}"><img src="imagens/btn-imprimir.png" width="100" height="25" /></a>
        </div>
        <c:if test ='${tipo eq "R"}'>
            <c:if test ='<%=request.isUserInRole("1367")%>'>
                <div class="incluir">
                    <a href='javascript: $("#incluiCredito").show();'><img src="imagens/btn-incluir.png" width="100" height="25" /></a>
                </div>
            </c:if>
        </c:if>


        <div class="tipoBoleto">
            <fieldset class="field-set legendaFrame" style="width:240px;height:40px;">
                <legend >Tipo:</legend>        
                &nbsp;&nbsp;<input type="radio" name="tipo" id="tipo" class="legendaCodigo" style="margin-top:0px" onChange='this.form.submit();' value="C" checked>Carnê
                &nbsp;&nbsp;<input type="radio" name="tipo" id="tipo" class="legendaCodigo" style="margin-top:0px" onChange='this.form.submit();' value="R" <c:if test ='${tipo eq "R"}'>checked</c:if>>Crédito
                &nbsp;&nbsp;<input type="radio" name="tipo" id="tipo" class="legendaCodigo" style="margin-top:0px" onChange='this.form.submit();' value="B" <c:if test ='${tipo eq "B"}'>checked</c:if>>Boleto
                </fieldset>
            </div>
            <br><br>

        <c:if test ='${tipo eq "C" || tipo == null}'>
            <div style="overflow:auto;height:300px;">
                <table id="tabela">
                    <thead>
                        <tr class="odd">
                            <th scope="col">Vencimento</th>
                            <th scope="col">Valor Devido</th>
                            <th scope="col">(+) Encargos</th>
                            <th scope="col">(-) Descontos</th>
                            <th scope="col">(=) Valor Pago</th>
                            <th scope="col">Valor Original</th>
                            <th scope="col">Valor Crédito</th>
                            <th scope="col">Data Pagamento</th>
                            <th scope="col">Local Pg.</th>
                            <th scope="col">Banco Pg.</th>
                            <th scope="col">Dt. Baixa</th>
                            <th scope="col">Imprimir Boleto</th>
                            <th scope="col">Bloquear</th>
                            <th scope="col">Desbloquear</th>
                            <th scope="col">Nada Consta</th>
                        </tr>
                    </thead>

                    <c:forEach var="carne" items="${carnes}">
                        <tr>
                            <fmt:formatDate var="dataVencimento" value="${carne.dataVencimento}" pattern="dd/MM/yyyy"/>
                            <td class="column1" align="center"><a href='c?app=1360&acao=detalhar&idCarne=${carne.id}&matricula=${carne.socio.matricula}&idCategoria=${carne.socio.idCategoria}'>${dataVencimento}</a></td>

                            <fmt:formatNumber var="valor" value="${carne.valor}" pattern="#,##0.00"/>
                            <td class="column1" align="right">${valor}</td>

                            <fmt:formatNumber var="encargos" value="${carne.encargos}" pattern="#,##0.00"/>
                            <td class="column1" align="right">${encargos}</td>

                            <fmt:formatNumber var="descontos" value="${carne.descontos}" pattern="#,##0.00"/>
                            <td class="column1" align="right">${descontos}</td>

                            <fmt:formatNumber var="valorPago" value="${carne.valorPago}" pattern="#,##0.00"/>
                            <td class="column1" align="right">${valorPago}</td>

                            <fmt:formatNumber var="valorOriginal" value="${carne.valorOriginal}" pattern="#,##0.00"/>
                            <td class="column1" align="right">${valorOriginal}</td>

                            <fmt:formatNumber var="valorCredito" value="${carne.valorCredito}" pattern="#,##0.00"/>
                            <td class="column1" align="right">${valorCredito}</td>

                            <fmt:formatDate var="dataPagamento" value="${carne.dataPagamento}" pattern="dd/MM/yyyy"/>
                            <td class="column1" align="center">${dataPagamento}</td>

                            <td class="column1" align="center">${carne.localPagamento}</td>                
                            <td class="column1" align="center">${carne.bancoPagamento}</td>                
                            

                            <fmt:formatDate var="dataBaixa" value="${carne.dataBaixa}" pattern="dd/MM/yyyy"/>
                            <td class="column1" align="center">${dataBaixa}</td>



                            <c:choose>
                                <c:when test='${carne.localPagamento == null || carnelocalPagemento == ""}'>
                                    <c:choose>
                                        <c:when test="${carne.permitePagamento}">
                                            <td></td>
                                            <td></td>
                                            <td align="center">    
                                                <a href="javascript: if(confirm('Deseja realmente permitir que o carne selecionado seja pago?')) window.location.href='c?app=1360&acao=destrancar&idCarne=${carne.id}&matricula=${carne.socio.matricula}&idCategoria=${carne.socio.idCategoria}&seqDependente=${carne.socio.seqDependente}'"><img src="imagens/icones/cadeado-aberto.png" /></a>
                                            </td>

                                        </c:when>
                                        <c:otherwise>
                                            <td align="center">    
                                                <c:if test='<%=request.isUserInRole("1361")%>'>
                                                    <a href="c?app=1360&acao=showFormImprimirBoleto&&idCarne=${carne.id}"><img src="imagens/icones/inclusao-titular-15.png" /></a>
                                                </c:if>
                                            </td>

                                            <td align="center">    
                                                <a href="javascript: if(confirm('Deseja realmente impedir que o carne selecionado seja pago?\n(Neste caso será enviada mensagem no boleto para o associado)')) window.location.href='c?app=1360&acao=trancar&idCarne=${carne.id}&matricula=${carne.socio.matricula}&idCategoria=${carne.socio.idCategoria}&seqDependente=${carne.socio.seqDependente}'"><img src="imagens/icones/inclusao-titular-18.png"/></a>
                                            </td>
                                            
                                            <td></td>

                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </c:otherwise>
                            </c:choose>
                            <sql:query var="pode" dataSource="jdbc/iate">
                                SELECT
                                    DBO.FC_PODE_EMITIR_NADA_CONSTA_CARNE(${carne.id}) PODE_EMITIR
                            </sql:query>     
                            <td align="center">        
                                <c:if test="${pode.rows[0].PODE_EMITIR=='S'}">
                                    <a href="c?app=1360&acao=showFormImprimirNadaConsta&idCarne=${carne.id}"><img src="imagens/icones/inclusao-titular-13.png" /></a>
                                </c:if>
                                
                            </td>        
                        </tr>
                    </c:forEach>
                </table>
            </div>

            <div class="divisoria"></div>
            <div id="titulo-subnav">Parcela do Carnê</div>
            <div class="divisoria"></div>

            <table id="tabela2">
                <thead>
                    <tr class="odd">
                        <th scope="col">Pessoa</th>
                        <th scope="col">Taxa</th>
                        <th scope="col">Valor Devido</th>
                        <th scope="col">(+) Encargos</th>
                        <th scope="col">(-) Descontos</th>
                        <th scope="col">(=) Valor Pago</th>
                        <th scope="col">Sit.</th>
                    </tr>
                </thead>

                <c:forEach var="detalhe" items="${detalhes}">
                    <tr>

                        <td class="column1">${detalhe.nomePessoa}</td>
                        <td class="column1">${detalhe.descricaoTaxa}</td>

                        <fmt:formatNumber var="valor" value="${detalhe.valor}" pattern="#,##0.00"/>
                        <td class="column1" align="right">${valor}</td>

                        <fmt:formatNumber var="encargos" value="${detalhe.encargos}" pattern="#,##0.00"/>
                        <td class="column1" align="right">${encargos}</td>

                        <fmt:formatNumber var="descontos" value="${detalhe.descontos}" pattern="#,##0.00"/>
                        <td class="column1" align="right">${descontos}</td>

                        <fmt:formatNumber var="valorPago" value="${detalhe.valorPago}" pattern="#,##0.00"/>
                        <td class="column1" align="right">${valorPago}</td>

                        <td class="column1" align="center">${detalhe.situacao}</td>
                    </tr>
                </c:forEach>
            </table>    
        </c:if>


        <c:if test ='${tipo eq "B"}'>
            <table id="tabela">
                <thead>
                    <tr class="odd">
                        <th scope="col" class="nome-lista">Sacado</th>
                        <th scope="col" class="nome-lista">Taxa</th>
                        <th scope="col" align="center">Vencimento</th>
                        <th scope="col" align="right">Valor</th>
                        <th scope="col" align="center">Situacao</th>
                        <th scope="col" align="center">Dt. Geração</th>
                        <th scope="col" align="center">Dt. Pagamento</th>
                        <th scope="col" align="center">Dt. Baixa</th>
                        <th scope="col" align="center">Dt. Cancelamento</th>
                        <th scope="col" align="right">Encargos</th>
                        <th scope="col" align="right">Vr. Pago</th>
                        <th scope="col" align="center">Imprimir</th>
                    </tr>
                </thead>
                <tbody>
                    <sql:query var="rs" dataSource="jdbc/iate">
                        SELECT
                        T1.*,
                        T2.DESCR_TX_ADMINISTRATIVA
                        FROM
                        TB_BOLETO_AVULSO T1, 
                        TB_TAXA_ADMINISTRATIVA T2
                        WHERE
                        T1.CD_MATRICULA = ${matricula} AND
                        T1.CD_CATEGORIA = ${idCategoria} AND
                        T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA
                        ORDER BY
                        T1.DT_GERACAO
                    </sql:query>     
                    <c:forEach var="boleto" items="${rs.rows}">
                        <tr>
                            <td class="column1">${boleto.NO_SACADO}</td>
                            <td class="column1">${boleto.DESCR_TX_ADMINISTRATIVA}</td>
                            <td class="column1" align="center"><fmt:formatDate value="${boleto.DT_VENCIMENTO}" pattern="dd/MM/yyyy" /></td>
                            <th class="column1" align="right"><fmt:formatNumber value="${boleto.VR_BOLETO}" pattern="#,##0.00"/></th>
                                <c:choose>
                                    <c:when test='${boleto.CD_SITUACAO == "NP"}'><td class="column1" align="center">Não Pago</td></c:when>
                                <c:when test='${boleto.CD_SITUACAO == "PG"}'><td class="column1" align="center">Pago</td></c:when>
                                <c:when test='${boleto.CD_SITUACAO == "CA"}'><td class="column1" align="center">Cancelado</td></c:when>
                            </c:choose>                        
                            <td class="column1" align="center"><fmt:formatDate value="${boleto.DT_GERACAO}" pattern="dd/MM/yyyy" /></td>
                            <td class="column1" align="center"><fmt:formatDate value="${boleto.DT_PAGAMENTO}" pattern="dd/MM/yyyy" /></td>
                            <td class="column1" align="center"><fmt:formatDate value="${boleto.DT_BAIXA}" pattern="dd/MM/yyyy" /></td>
                            <td class="column1" align="center"><fmt:formatDate value="${boleto.DT_CANCELAMENTO}" pattern="dd/MM/yyyy" /></td>
                            <th class="column1" align="right"><fmt:formatNumber value="${boleto.VR_ENCARGOS}" pattern="#,##0.00"/></th>
                            <th class="column1" align="right"><fmt:formatNumber value="${boleto.VR_PAGO}" pattern="#,##0.00"/></th>


                            <!-- IMPRIMIR -->
                            <td class="column1" align="center">
                                <c:if test='${boleto.CD_SITUACAO == "NP"}'>
                                    <a href="c?app=2430&acao=imprimir&id=${boleto.SEQ_BOLETO}"><img src="imagens/icones/inclusao-titular-13.png"/></a>
                                    </c:if>
                            </td>

                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>



        <c:if test ='${tipo eq "R"}'>
            <table id="tabela">
                <thead>
                    <tr class="odd">
                        <th scope="col" class="center">Referência</th>
                        <th scope="col" align="left">Taxa</th>
                        <th scope="col" align="right">Valor</th>
                        <th scope="col" align="center">Situação</th>
                        <th scope="col" align="center">Dt. Geração</th>
                        <th scope="col" align="center">Incluído por</th>
                        <th scope="col" align="center">Dt. Cancelamento</th>
                        <th scope="col" align="center">Cancelado por</th>
                        <th scope="col" align="left">Observação</th>
                        <th scope="col" align="center">Cancelar</th>
                    </tr>
                </thead>
                <tbody>
                    <sql:query var="rs" dataSource="jdbc/iate">
                        SELECT
                        T1.*,
                        T2.DESCR_TX_ADMINISTRATIVA
                        FROM
                        TB_VAL_CREDITO_PESSOA T1, 
                        TB_TAXA_ADMINISTRATIVA T2
                        WHERE
                        T1.CD_MATRICULA = ${matricula} AND
                        T1.CD_CATEGORIA = ${idCategoria} AND
                        T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA
                        ORDER BY
                        T1.AA_COBRANCA DESC, T1.MM_COBRANCA DESC
                    </sql:query>     
                    <c:forEach var="credito" items="${rs.rows}">
                        <tr>
                            <th class="column1" align="center"><fmt:formatNumber value="${credito.MM_COBRANCA}" pattern="00"/> / ${credito.AA_COBRANCA}</th>
                            <td class="column1" align="left">${credito.DESCR_TX_ADMINISTRATIVA}</td>
                            <th class="column1" align="right"><fmt:formatNumber value="${credito.VR_CREDITO}" pattern="#,##0.00"/></th>
                                <c:choose>
                                    <c:when test='${credito.IC_SITUACAO_CREDITO == "N"}'><td class="column1" align="center">Normal</td></c:when>
                                <c:when test='${credito.IC_SITUACAO_CREDITO == "C"}'><td class="column1" align="center">Cancelado</td></c:when>
                            </c:choose>                        
                            <td class="column1" align="center"><fmt:formatDate value="${credito.DT_GERACAO_CREDITO}" pattern="dd/MM/yyyy" /></td>
                            <td class="column1" align="center">${credito.USER_INCLUSAO}</td>
                            <td class="column1" align="center"><fmt:formatDate value="${credito.DT_CANCELAMENTO_CREDITO}" pattern="dd/MM/yyyy" /></td>
                            <td class="column1" align="center">${credito.USER_CANCELAMENTO}</td>
                            <td class="column1" align="center">${credito.DE_OBSERVACAO}</td>

                            <td align="center">
                                <c:if test='<%=request.isUserInRole("1368")%>'>
                                    <c:if test='${credito.IC_SITUACAO_CREDITO == "N"}'>
                                        <a href="javascript: if(confirm('Confirma o cancelamento do crédito selecionado?')) cancelaCredito(${credito.NU_SEQ_CREDITO})"><img src="imagens/icones/inclusao-titular-05.png"/></a>
                                        </c:if>
                                    </c:if>
                            </td>


                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>

    </form>
    <input type="button" class="botaoVoltar"  onclick="window.location = 'c?app=9030&acao=consultar&matricula=${matricula}&categoria=${idCategoria}';" value=" " />


    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                                 CADASTRO DE CREDITO
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->
    <div id="incluiCredito" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 850px; height:200px;background-color: white">
        <table style="fmt">
            <tr>
                <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Inclusão de Crédito</div>
                    <div class="divisoria"></div>

                    <table class="fmt" align="left" >
                        <tr>

                            <td>
                                <p class="legendaCodigo MargemSuperior0">Taxa:</p>
                                <select name="cdTxAdministrativa" ID="cdTxAdministrativa" class="campoSemTamanho alturaPadrao" style="width: 250px">
                                    <c:forEach var="txAdm" items="${taxasAdministrativas}">
                                        <option value="${txAdm.id}">${txAdm.descricao}</option>
                                    </c:forEach>
                                </select>
                            </td>

                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Cobrar em (Mês/Ano)</p>
                                <input type="text" name="mes" id="mes" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" value="${mes}">
                                <b> &nbsp; / &nbsp; </b>
                                <input type="text" name="ano" id="ano" class="campoSemTamanhoSemMargem alturaPadrao larguraNumero" value="${ano}">
                            </td>

                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Valor</p>
                                <input type="text" id="valorCredito" name="valorCredito" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                            </td>

                            <td>
                                <p class="legendaCodigo MargemSuperior0" >Observação</p>
                                <input type="text" id="observacao" name="observacao" class="campoSemTamanho alturaPadrao" maxlength="250" style="width: 250px;" value="">
                            </td>
                        </tr>
                    </table>
                    <table class="fmt" align="left" >
                        <tr>
                            <td>
                                <input style="margin-left:15px;" type="button" id="cmdIncluir" name="cmdIncluir" class="botaoatualizar" onclick="gravaCredito()" />
                            </td>
                            <td>
                                <input style="margin-left:15px;margin-top: 0px;" type="button" id="cmdCancelar" name="cmdCancelar" class="botaocancelar" onclick="cancelaInclusao()" />
                            </td>

                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>

</body>
</html>
