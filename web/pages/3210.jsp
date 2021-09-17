<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
    <script type="text/javascript" language="JavaScript">
        
        function onlyNumber(evt) {
            if(evt.which !== 0 && evt.which !== 8 && (evt.which < 48 || evt.which > 57))
                    evt.preventDefault();
        }
        
        $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            
            $("#divImportacao").hide();
            
            $("#dtInicioVigencia").mask("99/99/9999");
            $("#dtFimVigencia").mask("99/99/9999");
            $("#dtRef").mask("99/99/9999");

            $('#icAgendaInternet').click(habilitaCampos);

            habilitaCampos();
            
            if ($('#app').val()=="3212"){
                $("#qtAluno").attr('disabled','disabled');
                $("#qtMinAtendimento").attr('disabled','disabled');
                $("#qtMinIntervalo").attr('disabled','disabled');
            }
        });     
        
        function habilitaCampos(){
            if ($('#icAgendaInternet').attr('checked')){
                $("#qtDiasAntInternet").removeAttr('disabled');
            }else{
                $("#qtDiasAntInternet").attr('disabled','disabled');
                $('#qtDiasAntInternet').val('');
            }    
        }
        
        function importaAgenda(idServico){
            $("#divImportacao").show();
        }
        
        function cancelaImportacao(){
            $("#divImportacao").hide();
        }
        
        function atualizaImportacao(){
            $("#idImportacao").val($("#idServicoImportacao").val());
            $("#acao").val('importar');
            document.forms[0].submit();
        }

        function validarForm(){
        
            if ($('#descricao').val()==""){
                alert('Informe a descrição do serviço!');
                return false;
            }
            if ($('#qtAluno').val()==""){
                alert('Informe a quantidade de alunos atendidos em cada horário!');
                return false;
            }
            if ($('#qtMinAtendimento').val()==""){
                alert('Informe a duração de cada atendimento (Min. Atend.)!');
                return false;
            }
            
            if ($('#qtLimiteSocio').val()!="" && $('#qtDiasLimiteSocio').val()==""){
                alert('Quando o campo Limite Sócio é preenchido o campo Dias Limite deve ser preenchido!');
                return false;
            }
            if ($('#qtLimiteSocio').val()=="" && $('#qtDiasLimiteSocio').val()!=""){
                alert('Quando o campo Dias Limite é preenchido o campo Limite Sócio deve ser preenchido!');
                return false;
            }
            if ($('#vagaClube').val()==""){
                alert('Informe a quantidade de vagas disponíveis no Clube!');
                return false;
            }
            if ($('#vagaInternet').val()==""){
                alert('Informe a quantidade de vagas disponíveis na internet!');
                return false;
            }

            if (parseInt($('#vagaClube').val()) > parseInt($('#qtAluno').val())){
                alert('A quantidade de vagas disponíveis para agendamento no clube não pode ser maior que o total de vagas!');
                return false;
            }
            if (parseInt($('#vagaInternet').val()) > parseInt($('#qtAluno').val())){
                alert('A quantidade de vagas disponíveis para agendamento na internet não pode ser maior que o total de vagas!');
                return false;
            }
        
            
            if (isNaN($('#vrMulta').val().replace('.', '').replace(',', '.'))){
                alert('Informe um valor válido no campo Vr. Multa Falta!');
                return false;
            }
            if (isNaN($('#vrServico').val().replace('.', '').replace(',', '.'))){
                alert('Informe um valor válido no campo Vr. Serviço!');
                return false;
            }
            
            if (parseFloat($('#vrMulta').val().replace('.', '').replace(',', '.')) > 0 && $('#cdTxMulta').val() == 0){
                alert('O campo Taxa Multa Falta é de preenchimento obrigatório quando o campo Vr. Multa está preenchido com valor maior que zero!');
                return false
            }
            if (parseFloat($('#vrServico').val().replace('.', '').replace(',', '.')) > 0 && $('#cdTxServico').val() == 0){
                alert('O campo Taxa Serviço é de preenchimento obrigatório quando o campo Vr. Serviço está preenchido com valor maior que zero!');
                return false
            }
            
            if ($('#dtInicioVigencia').val()!=""){
                if(!isDataValida(document.forms[0].dtInicioVigencia.value)){
                    $('#dtInicioVigencia').focus();
                    return false;
                }
            }
            if ($('#dtFimVigencia').val()!=""){
                if(!isDataValida(document.forms[0].dtFimVigencia.value)){
                    $('#dtFimVigencia').focus();
                    return false;
                }
            }
            
            $("#qtDiasAntInternet").removeAttr('disabled');
            document.forms[0].submit();

        }     
        function submetePesquisa(){
            $('#acao').val('showForm');
            document.forms[0].submit();
        }
            
    </script>

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <div class="divisoria"></div>
    <c:if test='${app == "3211"}'>
        <div id="titulo-subnav">Inclusão de Serviço</div>
    </c:if>
    <c:if test='${app == "3212"}'>
        <div id="titulo-subnav">Alteração de Serviço</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" id="app" value="${app}">
        <input type="hidden" name="id" id="id" value="${servico.id}">
        <input type="hidden" name="idImportacao" id="idImportacao" value="">
        <input type="hidden" name="acao" id="acao" value="gravar">
        <table class="fmt">
            <tr>
                <td >
                    <p class="legendaCodigo MargemSuperior0">Descrição:</p>
                    <input type="text" name="descricao" id="descricao" class="campoSemTamanho alturaPadrao" style="width:140px;" value="${servico.descricao}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Vagas:</p>
                    <input type="text" name="qtAluno" id="qtAluno"  class="campoSemTamanho alturaPadrao" style="width:65px;" value="${servico.qtAluno}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Vagas Clube:</p>
                    <input type="text" name="vagaClube" id="vagaClube"  class="campoSemTamanho alturaPadrao" style="width:65px;" value="${servico.vagaClube}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Limite Sócio:</p>
                    <input type="text" name="qtLimiteSocio" id="qtLimiteSocio" class="campoSemTamanho alturaPadrao" style="width:65px;" value="${servico.qtLimiteSocio}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Prazo Limite:</p>
                    <input type="text" name="qtDiasLimiteSocio" id="qtDiasLimiteSocio" class="campoSemTamanho alturaPadrao" style="width:65px;" value="${servico.qtDiasLimiteSocio}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >&nbsp;</p>
                    <div class="selectheightnovo">
                        <select name="tpPzLimite" id="tpPzCancelamento" class="campoSemTamanho alturaPadrao" style="width:70px;">
                            <option value="D" <c:if test='${servico.tpPzLimite == "D"}'>selected</c:if>>Dia(s)</option>
                            <option value="S" <c:if test='${servico.tpPzLimite == "S"}'>selected</c:if>>Semana(s)</option>
                            <option value="M" <c:if test='${servico.tpPzLimite == "M"}'>selected</c:if>>Mês(es)</option>
                            <option value="A" <c:if test='${servico.tpPzLimite == "A"}'>selected</c:if>>Ano(s)</option>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Inicio Vigên.:</p>
                    <fmt:formatDate var="dtInicioVigencia" value="${servico.dtInicioVigencia}" pattern="dd/MM/yyyy" />             
                    <input type="text" name="dtInicioVigencia" id="dtInicioVigencia" class="campoSemTamanho alturaPadrao" style="width:80px;" value="${dtInicioVigencia}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt Fim Vigên.:</p>
                    <fmt:formatDate var="dtFimVigencia" value="${servico.dtFimVigencia}" pattern="dd/MM/yyyy" />             
                    <input type="text" name="dtFimVigencia" id="dtFimVigencia" class="campoSemTamanho alturaPadrao larguraData" style="width:80px;" value="${dtFimVigencia}" >
                </td>
                
            </tr>
        </table>
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Prazo Ant. Clube:</p>
                    <input type="text" name="qtDiasAntClube" id="qtDiasAntClube" class="campoSemTamanho alturaPadrao" style="width:90px;" value="${servico.qtDiasAntClube}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >&nbsp;</p>
                    <div class="selectheightnovo">
                        <select name="tpPzAntAgendClube" id="tpPzAntAgendClube" class="campoSemTamanho alturaPadrao" style="width:60px;">
                            <option value="D" <c:if test='${servico.tpPzAntAgendClube == "D"}'>selected</c:if>>Dia(s)</option>
                            <option value="H" <c:if test='${servico.tpPzAntAgendClube == "H"}'>selected</c:if>>Hora(s)</option>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Atend. (min.):</p>
                    <input type="text" name="qtMinAtendimento" id="qtMinAtendimento" class="campoSemTamanho alturaPadrao" style="width:70px;" value="${servico.qtMinAtendimento}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Intervalo (min.):</p>
                    <input type="text" name="qtMinIntervalo" id="qtMinIntervalo" class="campoSemTamanho alturaPadrao" style="width:80px;" value="${servico.qtMinIntervalo}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Prz.Max.Clube(Dias):</p>
                    <input type="text" name="prazoMaximoClube" id="prazoMaximoClube" class="campoSemTamanho alturaPadrao" style="width:110px;" value="${servico.prazoMaximoClube}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Hr.Inic.Prz.Max:</p>
                    <input type="text" name="hhInicClube" id="hhInicClube" class="campoSemTamanho alturaPadrao" style="width:90px;" value="${servico.hhInicClube}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Prazo Cancel.:</p>
                    <input type="text" name="pzCancelamento" id="pzCancelamento" class="campoSemTamanho alturaPadrao" style="width:80px;" value="${servico.pzCancelamento}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >&nbsp;</p>
                    <div class="selectheightnovo">
                        <select name="tpPzCancelamento" id="tpPzCancelamento" class="campoSemTamanho alturaPadrao" style="width:60px;">
                            <option value="D" <c:if test='${servico.tpPzCancelamento == "D"}'>selected</c:if>>Dia(s)</option>
                            <option value="H" <c:if test='${servico.tpPzCancelamento == "H"}'>selected</c:if>>Hora(s)</option>
                        </select>
                    </div>
                </td>
                
            </tr>
        </table>
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Vr. Multa Falta:</p>
                    <input type="text" name="vrMulta" id="vrMulta" class="campoSemTamanho alturaPadrao" style="width:78px;" value="<fmt:formatNumber value="${servico.vrMulta}" pattern="#0.00"/>">
                </td>
                <td>
                    
                    <p class="legendaCodigo MargemSuperior0" >Taxa Multa Falta</p>
                    <div class="selectheightnovo">
                        <select name="cdTxMulta" id="cdTxMulta" class="campoSemTamanho alturaPadrao"  style="width:280px;">
                            <option value="0" selected>Nenhuma</option>
                            <c:forEach var="txsAdm" items="${taxasAdministrativas}">
                                <option value="${txsAdm.id}" <c:if test='${servico.cdTxMulta == txsAdm.id}'>selected</c:if>>${txsAdm.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Vr. Serviço:</p>
                    <input type="text" name="vrServico" id="vrServico" class="campoSemTamanho alturaPadrao" style="width:78px;" value="<fmt:formatNumber value="${servico.vrServico}" pattern="#0.00"/>">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Taxa Serviço</p>
                    <div class="selectheightnovo">
                        <select name="cdTxServico" id="cdTxServico" class="campoSemTamanho alturaPadrao"  style="width:280px;">
                            <option value="0" selected>Nenhuma</option>
                            <c:forEach var="txsAdm" items="${taxasAdministrativas}">
                                <option value="${txsAdm.id}" <c:if test='${servico.cdTxServico == txsAdm.id}'>selected</c:if>>${txsAdm.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
            </tr>
        </table>
        <table class="fmt">
            <tr>
                <td>
                    <input type="checkbox" class="legendaCodigo" style="margin-top: 22px;" name="icAgendaInternet" id="icAgendaInternet" value="S" <c:if test='${app == "3211"}'>checked</c:if> <c:if test='${servico.icAgendaInternet == "S"}'>checked</c:if>><spam class="legendaSemMargem larguraData">Agendamento pela Internet</spam>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Vagas Inter.:</p>
                    <input type="text" name="vagaInternet" id="vagaInternet"  class="campoSemTamanho alturaPadrao" style="width:65px;" value="${servico.vagaInternet}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Prazo Ant. Int.:</p>
                    <input type="text" name="qtDiasAntInternet" id="qtDiasAntInternet" class="campoSemTamanho alturaPadrao" style="width:80px;" value="${servico.qtDiasAntInternet}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >&nbsp;</p>
                    <div class="selectheightnovo">
                        <select name="tpPzAntAgendInternet" id="tpPzAntAgendInternet" class="campoSemTamanho alturaPadrao" style="width:60px;">
                            <option value="D" <c:if test='${servico.tpPzAntAgendInternet == "D"}'>selected</c:if>>Dia(s)</option>
                            <option value="H" <c:if test='${servico.tpPzAntAgendInternet == "H"}'>selected</c:if>>Hora(s)</option>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Prz.Max.Inter.(Dias):</p>
                    <input type="text" name="prazoMaximoInternet" id="prazoMaximoInternet" class="campoSemTamanho alturaPadrao" style="width:90px;" value="${servico.prazoMaximoInternet}" onkeypress="onlyNumber(event);">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Hr.Inic.Prz.Max:</p>
                    <input type="text" name="hhInicInternet" id="hhInicInternet" class="campoSemTamanho alturaPadrao" style="width:90px;" value="${servico.hhInicInternet}" onkeypress="onlyNumber(event);">
                </td>
            </tr>
        </table>
                
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Detalhamento do serviço:</p>
                    <textarea class="campoSemTamanho" rows="15" cols="86" name="detalhamento">${servico.detalhamento}</textarea><br>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Instruções para realização:</p>
                    <textarea class="campoSemTamanho" rows="15" cols="87" name="instrucao">${servico.instrucao}</textarea><br>
                </td>
            </tr>
        </table>
                
                
        <c:if test='${app == 3212}'>
            <div class="divisoria"></div>
            <div id="titulo-subnav">Profissionais</div>
            <div class="divisoria"></div>

            
            <table class="fmt">
                <tr>
                    <td style="padding-left: 13px; vertical-align: bottom;">
                        <a href="c?app=3215&acao=showForm&idServico=${servico.id}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>                    </td>
                    <td style="padding-left: 13px; vertical-align: bottom;">
                        <a href="javascript:importaAgenda(${servico.id});"><img src="imagens/btn-importar-agenda.png" width="100" height="25" /></a><br>
                    </td>
                    <td style="padding-left: 13px;">
                        <div class="selectheightnovo">
                            <p class="legendaCodigo MargemSuperior0" >Profissional</p>
                            <select name="funcionario" id="funcionario" class="campoSemTamanho alturaPadrao"  style="width:265px;">
                                <option value="0" selected>&lt;TODOS&gt;</option>
                                <c:forEach var="func" items="${funcionarios}">
                                    <option value="${func.id}" <c:if test='${funcSel == func.id}'>selected</c:if>>${func.descricao}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </td>
                    
                    <td>
                        <p class="legendaCodigo MargemSuperior0" >Pesquisa</p>
                        <div class="selectheightnovo2">
                          <select name="tipo" id="tipo" class="campoSemTamanho" style="width:180px">
                              <option value="AT" <c:if test='${tipo=="AT"}'>selected</c:if>>Ativos em</option>
                              <option value="TD" <c:if test='${tipo=="TD"}'>selected</c:if>>Mostrar Todos os Profissionais</option>
                              <option value="AF" <c:if test='${tipo=="AF"}'>selected</c:if>>Ativos e Futuros em</option>
                          </select>
                        </div>        
                    </td>
                    <td>
                        <p class="legendaCodigo MargemSuperior0" >Dt. Referência</p>
                        <input type="text" id="dtRef" name="dtRef" class="campoSemTamanho alturaPadrao " style="width:80px" value="${dtRef}">
                    </td>
                    <td >    
                        <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="submetePesquisa()" value="" title="Consultar" />
                    </td>
                    
                </tr>
            </table>
            
            <c:set var="dataReferencia" value="${fn:substring(dtRef,6,10)}-${fn:substring(dtRef,3,5)}-${fn:substring(dtRef,0,2)}"/>
            <input type="hidden" value="${dataReferencia}" />
            <input type="hidden" value="${funcSel}" />
            <input type="hidden" value="${tipo}" />
            

            <sql:query var="rs" dataSource="jdbc/iate">
                SELECT
                        T2.NOME_FUNCIONARIO,
                        T1.NU_SEQ_PROFISSIONAL, 
                        T1.DT_INICIO_VIGENCIA, 
                        T1.DT_FIM_VIGENCIA,
                        T1.DT_ABERTURA
                FROM 
                        TB_PROFISSIONAL_SERV_ACADEMIA T1,
                        TB_Funcionario T2
                WHERE
                        T1.CD_FUNCIONARIO = T2.CD_FUNCIONARIO AND 
                        T1.NU_SEQ_SERVICO = ${servico.id}
                        
                <c:if test='${funcSel!="0"}'>
                    AND T1.CD_FUNCIONARIO = ${funcSel}
                </c:if>                        
                <c:choose>
                    <c:when test='${tipo=="AT"}'>
                        AND (T1.DT_INICIO_VIGENCIA <= '${dataReferencia}' OR T1.DT_INICIO_VIGENCIA IS NULL)
                        AND (T1.DT_FIM_VIGENCIA >= '${dataReferencia}' OR T1.DT_FIM_VIGENCIA IS NULL)
                    </c:when>
                    <c:when test='${tipo=="AF"}'>
                        AND (T1.DT_INICIO_VIGENCIA >= ${dataReferencia} OR T1.DT_INICIO_VIGENCIA IS NULL)
                    </c:when>
                </c:choose>    
                        
                ORDER BY 3 DESC, 1 ASC
            </sql:query>    

            <table id="tabela" style="width:98%;margin-left:15px;">
                <thead>
                <tr class="odd">
                    <th scope="col" class="nome-lista">Nome</th>
                    <th scope="col" >Dt. Inicio Vigencia</th>
                    <th scope="col" >Dt. Inicio Vigencia</th>
                    <th scope="col" >Dt. Abertura</th>
                    <th scope="col" >Segunda</th>
                    <th scope="col" >Terça</th>
                    <th scope="col" >Quarta</th>
                    <th scope="col" >Quinta</th>
                    <th scope="col" >Sexta</th>
                    <th scope="col" >Sábado</th>
                    <th scope="col" >Domingo</th>
                    <th scope="col" >Alterar</th>
                    <th scope="col" >Excluir</th>
                    <th scope="col" >Duplicar</th>
                </tr>	
                </thead>
                <tbody>
                    <c:forEach var="row" items="${rs.rows}">

                        <tr height="1">
                            <td align="left">${row.NOME_FUNCIONARIO}</td>
                            <fmt:formatDate var="dataInicio" value="${row.DT_INICIO_VIGENCIA}" pattern="dd/MM/yyyy" />
                            <td align="center">${dataInicio}</td>

                            <fmt:formatDate var="dataFim" value="${row.DT_FIM_VIGENCIA}" pattern="dd/MM/yyyy" />
                            <td align="center">${dataFim}</td>
                            
                            <fmt:formatDate var="dataAbertura" value="${row.DT_ABERTURA}" pattern="dd/MM/yyyy HH:mm:ss" />
                            <td align="center">${dataAbertura}</td>

                            <sql:query var="rs2" dataSource="jdbc/iate">
                                {call SP_AGENDA_PROF_ACADEMIA (null, ${row.NU_SEQ_PROFISSIONAL}, null, null, null, null, 'N')}
                            </sql:query>                                
                            <c:forEach var="row2" items="${rs2.rows}">
                                <c:choose>
                                    <c:when test="${row2.CD_DIA eq 1}">
                                        <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                                        <c:choose>
                                            <c:when test="${empty segunda}">
                                                <c:set var="segunda" value="${tmp}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="segunda" value="${segunda}<br>${tmp}"/>
                                            </c:otherwise>
                                        </c:choose>    
                                    </c:when>
                                    <c:when test="${row2.CD_DIA eq 2}">
                                        <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                                        <c:choose>
                                            <c:when test="${empty terca}">
                                                <c:set var="terca" value="${tmp}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="terca" value="${terca}<br>${tmp}"/>
                                            </c:otherwise>
                                        </c:choose>    
                                    </c:when>
                                    <c:when test="${row2.CD_DIA eq 3}">
                                        <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                                        <c:choose>
                                            <c:when test="${empty quarta}">
                                                <c:set var="quarta" value="${tmp}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="quarta" value="${quarta}<br>${tmp}"/>
                                            </c:otherwise>
                                        </c:choose>    
                                    </c:when>
                                    <c:when test="${row2.CD_DIA eq 4}">
                                        <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                                        <c:choose>
                                            <c:when test="${empty quinta}">
                                                <c:set var="quinta" value="${tmp}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="quinta" value="${quinta}<br>${tmp}"/>
                                            </c:otherwise>
                                        </c:choose>    
                                    </c:when>
                                    <c:when test="${row2.CD_DIA eq 5}">
                                        <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                                        <c:choose>
                                            <c:when test="${empty sexta}">
                                                <c:set var="sexta" value="${tmp}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="sexta" value="${sexta}<br>${tmp}"/>
                                            </c:otherwise>
                                        </c:choose>    
                                    </c:when>
                                    <c:when test="${row2.CD_DIA eq 6}">
                                        <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                                        <c:choose>
                                            <c:when test="${empty sabado}">
                                                <c:set var="sabado" value="${tmp}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="sabado" value="${sabado}<br>${tmp}"/>
                                            </c:otherwise>
                                        </c:choose>    
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="tmp" value="${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}"/>
                                        <c:choose>
                                            <c:when test="${empty domingo}">
                                                <c:set var="domingo" value="${tmp}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="domingo" value="${domingo}<br>${tmp}"/>
                                            </c:otherwise>
                                        </c:choose>    
                                    </c:otherwise>                        
                                </c:choose>
                            </c:forEach>


                            <td align="center">${segunda}</td>
                            <td align="center">${terca}</td>
                            <td align="center">${quarta}</td>
                            <td align="center">${quinta}</td>
                            <td align="center">${sexta}</td>
                            <td align="center">${sabado}</td>
                            <td align="center">${domingo}</td>    
                            <c:remove var="segunda"/>
                            <c:remove var="terca"/>
                            <c:remove var="quarta"/>
                            <c:remove var="quinta"/>
                            <c:remove var="sexta"/>
                            <c:remove var="sabado"/>
                            <c:remove var="domingo"/>

                            <td class="column1" align="center">
                                <c:if test='<%=request.isUserInRole("3217")%>'>
                                    <a href="c?app=3217&acao=showForm&idProfissional=${row.NU_SEQ_PROFISSIONAL}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                                </c:if>
                            </td>
                            <td class="column1" align="center">
                                <c:if test='<%=request.isUserInRole("3217")%>'>
                                    <a href="c?app=3217&acao=excluir&idProfissional=${row.NU_SEQ_PROFISSIONAL}&idServico=${servico.id}"><img src="imagens/icones/inclusao-titular-05.png"/></a>
                                </c:if>
                            </td>
                            <td class="column1" align="center">
                                <c:if test='<%=request.isUserInRole("3218")%>'>
                                    <a href="c?app=3218&acao=duplicar&idProfissional=${row.NU_SEQ_PROFISSIONAL}"><img src="imagens/icones/inclusao-titular-04.png"/></a>
                                </c:if>
                            </td>
                        </tr>	
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
                
        <input type="button" id="atualizar" class="botaoatualizar" onclick="return validarForm();" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3210';" value=" " />
    </form>

        
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                    SELECAO DE SERVICO PARA IMPORTACAO DA AGENDA
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->
    <div id="divImportacao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 430px; height:400px;">
        <table style="background:#fff">
            <tr>
              <td>
                <div class="divisoria"></div>
                <div id="titulo-subnav">Importação da Agenda de Serviço</div>
                <div class="divisoria"></div>
                
                <table class="fmt" align="left" >
                    <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Serviço</p>
                            <div class="selectheightnovo">
                                <select name="idServicoImportacao" id="idServicoImportacao" class="campoSemTamanho alturaPadrao" style="width: 350px">
                                    <c:forEach var="serv" items="${servicos}">
                                        <c:if test='${serv.id != servico.id}'>
                                            <option value="${serv.id}">${serv.descricao}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </td>
                    </tr>
                </table>
                <br><br><br>
                <input style="margin-left:15px;margin-top:05px;" type="button" id="cmdCancelar" name="cmdCancelar" class="botaoatualizar" onclick="atualizaImportacao()"  />
                <input style="margin-left:15px;" type="button" id="cmdAtualizar" name="cmdAtualizar" class="botaocancelar" onclick="cancelaImportacao()" />
            </td>
          </tr>
        </table>
    </div>
    
    
</body>
</html>
