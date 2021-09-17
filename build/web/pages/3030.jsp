<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <div class="divisoria"></div>
    <c:if test='${app == "3031"}'>
        <div id="titulo-subnav">Inclusão de Turmas</div>
    </c:if>
    <c:if test='${app == "3032"}'>
        <div id="titulo-subnav">Alteração de Turmas</div>
    </c:if>
    <div class="divisoria"></div>

    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            $("#excluir").live("click", removeCampo);

            $("#tabela tr:eq(1)").css('display', 'none');

            $("#entrada").mask("99:99");
            $("#saida").mask("99:99");

            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
            escondeCampos();

        });     
        
        function removeCampo() {
                $(this).parent().parent().remove();
        }

        function incluiLinhaTabela(){
            if(trim($('#entrada').val()) == ''){
                alert('Informe o horário de entrada');
                return;                
            }
            if(trim($('#saida').val()) == ''){
                alert('Informe o horário de saída');
                return;                
            }
            if (!isHoraValida($('#entrada').val())){
                $('#entrada').focus();
                return;
            }
            if (!isHoraValida($('#saida').val())){
                $('#saida').focus();
                return;
            }
            if(parseInt($('#saida').val()) <= parseInt($('#entrada').val())){
                alert('O horário de saída deve ser depois do horário de entrada');
                return;
            }            
            
            novoCampo = $("#tabela tr:last").clone();
            //retirando os ;
            novoCampo.find("#tabDia").html($('#ddSemana').val());
            novoCampo.find("#tabHorario").html($('#entrada').val() + ' às ' + $('#saida').val());
            novoCampo.find("#tabDia").html($('#ddSemana').val());
            novoCampo.css('display','');
            novoCampo.insertAfter("#tabela tr:last");
            $('#entrada').val('');
            $('#saida').val('');
            
            $('#ddSemana').focus();
        }


        function validarForm(){

            if(trim(document.forms[0].deTurma.value) == ''){
                alert('O campo Descrição é obrigatório.');
                return false;                
            }
            if(trim(document.forms[0].dataInicio.value) == ''){
                alert('O campo Data de Inicio da Turma é obrigatório.');
                return false;                
            }
            if(trim(document.forms[0].dataFim.value) == ''){
                alert('O campo Data de Fim da Turma é obrigatório.');
                return false;                
            }
            if(trim(document.forms[0].qtVagas.value) == ''){
                alert('O campo Quantidade de vagas é obrigatório.');
                return false;                 
            }
            
            if(!isDataValida(document.forms[0].dataInicio.value)){
                return false;
            }
            if(!isDataValida(document.forms[0].dataFim.value)){
                return false;
            }

            if(parseInt(document.forms[0].qtVagas.value) 
                < (${turma.qtVagas - turma.sdVagas})){
                alert('Já existem mais pessoas matrículadas do que a quantidade de vagas informada.');
                return;
            }
            
            $("#tabela tr:gt(1)").each(function(index){
                $('#horarios').val($('#horarios').val() + $(this).find("#tabDia").html() + '/' + $(this).find("#tabHorario").html().replace(' às ', '/').replace(':', '').replace(':', '') + ';')
            });
            
            document.forms[0].submit();

        }

    </script>        

    <form action="c">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="idTurma" value="${turma.id}">
        <input type="hidden" name="horarios" id="horarios" value="">
        <input type="hidden" name="acao" value="gravar">
        <table class="fmt">
            <tr>
                <td >
                    <p class="legendaCodigo">Curso:</p>
                    <select name="idCurso" class="campoSemTamanho alturaPadrao larguraComboCategoria">
                        <c:forEach var="curso" items="${cursos}">
                            <option value="${curso.id}" <c:if test="${turma.idCurso eq curso.id}">selected</c:if>>${curso.descricao}</option>
                        </c:forEach>
                    </select>
                </td>
                <td colspan="3">
                    <p class="legendaCodigo" >Descrição</p>
                    <input type="text" name="deTurma" class="campoSemTamanho alturaPadrao" style="width: 335px" value="${turma.deTurma}"><br>
                </td>
            </tr>
            <tr>
                <td >
                    <p class="legendaCodigo">Professor:</p>
                    <select name="idProfessor" class="campoSemTamanho alturaPadrao larguraComboCategoria">
                        <c:forEach var="professor" items="${professores}">
                            <option value="${professor.id}" <c:if test="${turma.idProfessor eq professor.id}">selected</c:if>>${professor.descricao}</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                    <p class="legendaCodigo" >Qt. Vagas</p>
                    <input type="text" name="qtVagas" class="campoSemTamanho alturaPadrao larguraData" onkeypress="onlyNumber(event)" value="${turma.qtVagas}"><br>
                </td>
                <td>
                    <p class="legendaCodigo" >Data Inicio</p>
                    <fmt:formatDate var="dataInicio" value="${turma.dataInicio}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dataInicio" id="dataInicio"  class="campoSemTamanho alturaPadrao larguraData" value="${dataInicio}">
                </td>
                <td>
                    <p class="legendaCodigo" >Data Fim</p>
                    <fmt:formatDate var="dataFim" value="${turma.dataFim}" pattern="dd/MM/yyyy"/>
                    <input type="text" name="dataFim" id="dataFim" class="campoSemTamanho alturaPadrao larguraData" value="${dataFim}">
                </td>
            </tr>
        </table>
                
        <div class="divisoria"></div>
        <div id="titulo-subnav">Horários</div>
        <div class="divisoria"></div>
        
        <table class="fmt">
            <tr>
                <td >
                    <p class="legendaCodigo">Dia da Semana:</p>
                    <select name="ddSemana" id="ddSemana" class="campoSemTamanho alturaPadrao">
                        <option value="Segunda-feira">Segunda-feira</option>
                        <option value="Terça-feira">Terça-feira</option>
                        <option value="Quarta-feira">Quarta-feira</option>
                        <option value="Quinta-feira">Quinta-feira</option>
                        <option value="Sexta-feira">Sexta-feira</option>
                        <option value="Sábado">Sábado</option>
                        <option value="Domingo">Domingo</option>
                    </select>
                </td>
                <td >
                    <p class="legendaCodigo" >Entrada</p>
                    <input type="text" name="entrada" id="entrada" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" onkeypress="onlyNumber(event)" value=""><br>
                </td>
                <td >
                    <p class="legendaCodigo" >Saída</p>
                    <input type="text" name="saida" id="saida" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" onkeypress="onlyNumber(event)" value=""><br>
                </td>
                <td >&nbsp;&nbsp;&nbsp;&nbsp;
                     <a href="javascript:void(0)" onclick="incluiLinhaTabela();"><img src="imagens/btn-incluir.png" style="margin-top:35px" width="100" height="25" /></a>
                </td>
            </tr>
        </table>
        
        
        <table id="tabela" style="width:332px;margin-left:15px;">
            <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista">Dia</th>
                <th scope="col" align="center">Horário</th>
                <th scope="col" align="center">Excluir</th>
            </tr>	
            </thead>
            <tbody>
                <tr>
                    <td id="tabDia" class="column1"></td>
                    <td id="tabHorario" class="column1"></td>
                    <td  id="tabExcluir" class="column1" align="center"><a id="excluir" href="javascript: void(0);'"><img src="imagens/icones/inclusao-titular-05.png" /></a></td>
                </tr>
                <c:set var="dia" value="0"/>
                <c:forEach var="horarios" items="${turma.horarios}">
                    <c:set var="dia" value="${dia+1}"/>
                    <c:set var="maxRows" value="${fn:length(horarios)}"/>
                    <c:if test="${maxRows gt 0}">
                        <c:set var="row" value="0"/>
                        <c:forEach begin="0" end="${maxRows - 1}" var="row">
                            <tr>
                                <c:choose>
                                <c:when test="${dia==1}">
                                    <td id="tabDia" class="column1">Segunda-feira</td>
                                </c:when>
                                <c:when test="${dia==2}">
                                    <td id="tabDia" class="column1">Terça-feira</td>
                                </c:when>
                                <c:when test="${dia==3}">
                                    <td id="tabDia" class="column1">Quarta-feira</td>
                                </c:when>
                                <c:when test="${dia==4}">
                                    <td id="tabDia" class="column1">Quinta-feira</td>
                                </c:when>
                                <c:when test="${dia==5}">
                                    <td id="tabDia" class="column1">Sexta-feira</td>
                                </c:when>
                                <c:when test="${dia==6}">
                                    <td id="tabDia" class="column1">Sábado</td>
                                </c:when>
                                <c:when test="${dia==7}">
                                    <td id="tabDia" class="column1">Domingo</td>
                                </c:when>
                                </c:choose>
                                
                                <td id="tabHorario" class="column1">${fn:substring(horarios[row].horaInicio,0,2)}:${fn:substring(horarios[row].horaInicio,2,4)} às ${fn:substring(horarios[row].horaFim,0,2)}:${fn:substring(horarios[row].horaFim,2,4)}</td>
                                <td  id="tabExcluir" class="column1" align="center"><a id="excluir" href="javascript: void(0);'"><img src="imagens/icones/inclusao-titular-05.png" /></a></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </c:forEach>

            </tbody>
        </table>
                
        <input type="button" id="atualizar" class="botaoatualizar" onclick="return validarForm();" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3030';" value=" " />
    </form>

        
</body>
</html>
