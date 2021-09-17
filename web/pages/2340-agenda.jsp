<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
    });        
</script>   


<body class="internas">
    
    <script type="text/javascript">
        function onlyNumber(evt) {
            if(evt.which !== 0 && evt.which !== 8 && (evt.which < 48 || evt.which > 57))
                    evt.preventDefault();
        }
    </script>

    
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">${quadra.nome} - Inclusão Agenda Semanal</div>
    <div class="divisoria"></div>

    <form name="agendaForm" action="c" method="POST">
        <input type="hidden" name="app" value="${app}" />
        <input type="hidden" name="acao" value="insertAgenda" />
        <input type="hidden" name="id" value="${id}" />

        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Dia da Semana</p>
                    
                    <div class="selectheightnovo">
                        <select name="dia" class="campoSemTamanho alturaPadrao" style="width:150px;">
                            <c:forEach var="d" items="${dias}">
                                <option value="${d.code}" <c:if test="${dia == d.code}">selected="selected"</c:if>>${d.description}</option>
                            </c:forEach>
                        </select>
                    </div>        
                </td>
                <td style="white-space: nowrap;">
                    <p class="legendaCodigo MargemSuperior0" >Horário de Início</p>
                    <div class="selectheightnovo">
                        <select name="horaInicio" class="campoSemTamanho" style="width:50px;">
                            <c:forEach var="i" begin="0" end="23">
                                <option value="${i}" <c:if test="${horaInicio == i}">selected="selected"</c:if>><fmt:formatNumber value="${i}" minIntegerDigits="2" /></option>
                            </c:forEach>
                        </select>
                        :
                        <select name="minutoInicio"  class="campoSemTamanhoSemMargem" style="width:50px;">
                            <c:forEach var="i" begin="0" end="59" step="1">
                                <option value="${i}" <c:if test="${minutoInicio == i}">selected="selected"</c:if>><fmt:formatNumber value="${i}" minIntegerDigits="2" /></option>
                            </c:forEach>
                        </select>                        
                    </div>        
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
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Situação da Quadra</p>
                    <div class="selectheightnovo">
                        <select name="tipoEvento" class="campoSemTamanho" style="width:150px;">
                            <c:forEach var="t" items="${tiposEvento}">
                                <c:set var="tipo">${t.code}</c:set>
                                <option value="${tipo}" <c:if test="${tipoEvento == tipo}">selected="selected"</c:if> title="${t.description}">${t}</option>
                            </c:forEach>
                        </select>
                    </div>        
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Minutos Liberação</p>
                    <input type="text" class="campoSemTamanho alturaPadrao" name="minutosMarcacao" value="${minutosMarcacao}" maxlength="4" onkeypress="onlyNumber(event);" />
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Observação / Motivo</p>
                    <input type="text" class="campoSemTamanho alturaPadrao larguraNomePessoa" name="observacao" value="${observacao}" maxlength="40" />
                </td>
            </tr>
        </table>
                
        
        <p style="font-weight: bold; font-size: large; color: red">&nbsp;&nbsp;&nbsp;${err}</p>
        <input type="button" class="botaoIncluir" onclick="document.agendaForm.submit()"  value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2340';" value=" " />
    </form>
        
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">${quadra.nome} - Agenda Semanal</div>
    <div class="divisoria"></div>

    <table id="tabela">
        <thead>
            <tr>
                <th>Dia da Semana</th>
                <th>Horário</th>
                <th>Situação da Quadra</th>
                <th>Liberado para Marcação antes do Término</th>
                <th>Observação / Motivo</th>
                <th>Excluir</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="evento" items="${quadra.agendaSemanal}">
                <tr>
                    <td style="white-space: nowrap;">${evento.dia.description}</td>
                    <td style="white-space: nowrap;" align="center">
                        <joda:format value="${evento.inicio}" pattern="HH':'mm" />
                        &ndash;
                        <joda:format value="${evento.fim}" pattern="HH':'mm" />
                    </td>
                    <td style="white-space: nowrap;">${evento.tipo.description}</td>
                    <td style="white-space: nowrap;" align="center"><c:if test="${not empty evento.minutosMarcacao}">${evento.minutosMarcacao} min</c:if></td>
                    <td style="white-space: nowrap;">${evento.observacao}</td>
                    <td style="white-space: nowrap;" align="center">
                        <a href="javascript: if(confirm('Confirma a exclusão do evento selecionado?')) window.location.href='c?app=2342&acao=deleteAgenda&id=${id}&idEvento=${evento.id}'">
                            <img src="imagens/icones/inclusao-titular-05.png" title="Excluir Evento da Agenda Semanal" />
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
            
</body>
</html>
