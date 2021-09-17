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
            $("#excluir").live("click", removeCampo);

            $("#tabela tr:eq(1)").css('display', 'none');

            $("#entrada").mask("99:99");
            $("#saida").mask("99:99");

            $("#dtInicioVigencia").mask("99/99/9999");
            $("#dtFimVigencia").mask("99/99/9999");
            $("#dtAbertura").mask("99/99/9999 99:99:99");
            escondeCampos();

        });     
        
        function removeCampo() {
                var item = $(this);
                var idAgenda = item.parent().find("#idAgenda").val();
                if (idAgenda != '0'){
                    if (confirm('Confirma a exclusão do horário?')){
                        $.ajax({url:'ProfissionalAcademiaAjax', async:false, dataType:'text', type:'GET',data:{
                                            tipo:2,
                                            idAgenda:idAgenda
                                        }
                        }).success(function(retorno){
                            if (retorno!='OK'){
                                alert(retorno)
                            }else{
                                item.parent().parent().remove();
                                alert('Exclusão efetuada com sucesso!');
                            }
                        });
                    }
                }else{
                    $(this).parent().parent().remove();
                }
                
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
            if(parseInt($('#saida').val().replace(':', '')) <= parseInt($('#entrada').val().replace(':', ''))){
                alert('O horário de saída deve ser depois do horário de entrada');
                return;
            }            
            
            //verficando se o horário não choca com outro já cadastrado
            var entradaLista = 0;
            var saidaLista = 0;
            var diaLista = '';
            var entrada = 0;
            var saida = 0;
            var dia = '';
            entrada = parseInt($('#entrada').val().replace(':', ''));
            saida = parseInt($('#saida').val().replace(':', ''));
            dia = $('#ddSemana').val();
            
            var problema = '';
            $("#tabela tr:gt(1)").each(function(index){
                entradaLista = parseInt($(this).find("#tabHorario").html().replace(' às ', '').replace(':', '').replace(':', '').substring(0, 4));
                saidaLista = parseInt($(this).find("#tabHorario").html().replace(' às ', '').replace(':', '').replace(':', '').substring(4, 8));
                diaLista = $(this).find("#tabDia").html();
                
                if (((entradaLista < entrada && saidaLista > entrada) || (entradaLista < saida && saidaLista > saida) || (entrada <= entradaLista && saida >= saidaLista) ) && dia == diaLista) {
                    problema = 'PROBLEMA';
                    return false;
                    
                }
            });
            
            if (problema == 'PROBLEMA'){
                alert('O horário choca com outro já cadastrado!');
                return false;
            }
            
            
            novoCampo = $("#tabela tr:last").clone();
            //retirando os ;
            novoCampo.find("#tabDia").html($('#ddSemana').val());
            novoCampo.find("#tabHorario").html($('#entrada').val() + ' às ' + $('#saida').val());
            novoCampo.find("#tabDia").html($('#ddSemana').val());
            novoCampo.find("#tabLocal").html($('#local option:selected').text() + "<input type='hidden' name='idLocal' id='idLocal' value='"+$('#local').val()+"'>");
            novoCampo.find("#tabExcluir").html("<input type='hidden' name='idAgenda' id='idAgenda' value='0'><a id='excluir' href='javascript: void(0);'><img src='imagens/icones/inclusao-titular-05.png' /></a>");
            novoCampo.css('display','');
            novoCampo.insertAfter("#tabela tr:last");
            $('#entrada').val('');
            $('#saida').val('');
            
            $('#ddSemana').focus();
        }

        function validarForm(){

            if($('#dtInicioVigencia').val() != ''){
                if(!isDataValida($('#dtInicioVigencia').val())){
                    $('#dtInicioVigencia').focus();
                    return false;
                }
            }
            if($('#dtFimVigencia').val() == ''){
                if(!isDataValida($('#dtFimVigencia').val())){
                    $('#dtFimVigencia').focus();
                    return false;
                }
            }
            if($('#dtAbertura').val() != ''){
                if(!isDataHoraValida($('#dtAbertura').val())){
                    $('#dtAbertura').focus();
                    return false;
                }
            }
            
            var temErro = '';

            $('#horarios').val('');
            $("#tabela tr:gt(1)").each(function(index){
                $('#horarios').val($('#horarios').val() + $(this).find("#tabDia").html() + '/');
                $('#horarios').val($('#horarios').val() + $(this).find("#tabHorario").html().replace(' às ', '/').replace(':', '').replace(':', '') + '/');
                $('#horarios').val($('#horarios').val() + $(this).find("#idLocal").val() + '/');
                $('#horarios').val($('#horarios').val() + $(this).find("#idAgenda").val() + ';');
                
                $.ajax({url:'ProfissionalAcademiaAjax', async:false, dataType:'text', type:'GET',data:{
                                    tipo:1,
                                    dtInicio:$('#dtInicioVigencia').val(),
                                    dtFim:$('#dtFimVigencia').val(),
                                    horario:$(this).find("#tabHorario").html().replace(' às ', '/').replace(':', '').replace(':', '') + '/',
                                    dia:$(this).find("#tabDia").html(),
                                    idProfissional:$('#idProfissional').val(),
                                    idFuncionario:$('#idFuncionario').val()
                                }
                }).success(function(retorno){
                    if (retorno!=''){
                        temErro = retorno;
                    }
                });

                if (temErro!='OK'){
                    return false;
                }
            });
            
            if (temErro!='OK'){
                alert(temErro);
                return false;
            }
            document.forms[0].submit();

        }


    </script>               

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Inclusão de Profissional</div>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="idServico" value="${idServico}">
        <input type="hidden" name="idProfissional" id="idProfissional" value="${profissional.id}">
        <input type="hidden" name="horarios" id="horarios" value="">
        <input type="hidden" name="acao" value="gravar">
        <table class="fmt">
            <tr>
                <td >
                    <p class="legendaCodigo MargemSuperior0">Profissional:</p>
                    <div class="selectheightnovo">
                        <select name="idFuncionario" id="idFuncionario" class="campoSemTamanho alturaPadrao" <c:if test='${app == "3217"}'>disabled</c:if> style="width: 330px">
                            <c:forEach var="funcionario" items="${funcionarios}">
                                <option value="${funcionario.id}" <c:if test='${funcionario.id == profissional.idFuncionario}'>selected</c:if>>${funcionario.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Inicio Vigência:</p>
                    <fmt:formatDate var="dataInicio" value="${profissional.dtInicioVigencia}" pattern="dd/MM/yyyy" />
                    <input type="text" name="dtInicioVigencia" id="dtInicioVigencia" class="campoSemTamanho alturaPadrao larguraData" value="${dataInicio}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt Fim Vigência:</p>
                    <fmt:formatDate var="dataFim" value="${profissional.dtFimVigencia}" pattern="dd/MM/yyyy" />
                    <input type="text" name="dtFimVigencia" id="dtFimVigencia" class="campoSemTamanho alturaPadrao larguraData" value="${dataFim}" >
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt Abertura:</p>
                    <fmt:formatDate var="dataAbertura" value="${profissional.dtAbertura}" pattern="dd/MM/yyyy HH:mm:ss" />
                    <input type="text" name="dtAbertura" id="dtAbertura" class="campoSemTamanho alturaPadrao" style="width: 110px" value="${dataAbertura}" >
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
                    <div class="selectheightnovo">
                        <select name="ddSemana" id="ddSemana" class="campoSemTamanho alturaPadrao">
                            <option value="Segunda-feira">Segunda-feira</option>
                            <option value="Terça-feira">Terça-feira</option>
                            <option value="Quarta-feira">Quarta-feira</option>
                            <option value="Quinta-feira">Quinta-feira</option>
                            <option value="Sexta-feira">Sexta-feira</option>
                            <option value="Sábado">Sábado</option>
                            <option value="Domingo">Domingo</option>
                        </select>
                    </div>
                </td>
                <td >
                    <p class="legendaCodigo" >Início</p>
                    <input type="text" name="entrada" id="entrada" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" onkeypress="onlyNumber(event)" value=""><br>
                </td>
                <td >
                    <p class="legendaCodigo" >Término</p>
                    <input type="text" name="saida" id="saida" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" onkeypress="onlyNumber(event)" value=""><br>
                </td>
                <td >
                    <p class="legendaCodigo">Local:</p>
                    <div class="selectheightnovo">
                        <select name="local" id="local" class="campoSemTamanho alturaPadrao" style="width: 150px">
                            <c:forEach var="local" items="${locais}">
                                <option value="${local.id}">${local.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td >&nbsp;&nbsp;&nbsp;&nbsp;
                     <a href="javascript:void(0)" onclick="incluiLinhaTabela();"><img src="imagens/btn-incluir.png" style="margin-top:35px" width="100" height="25" /></a>
                </td>
            </tr>
        </table>
        
        
        <table id="tabela" style="width:500px;margin-left:15px;">
            <thead>
            <tr class="odd">
                <th scope="col" align="center">Dia</th>
                <th scope="col" align="center">Horário</th>
                <th scope="col" align="center">Local</th>
                <th scope="col" align="center">Excluir</th>
            </tr>	
            </thead>
            <tbody>
                <tr>
                    <td id="tabDia" class="column1"></td>
                    <td id="tabHorario" class="column1"></td>
                    <td id="tabLocal" class="column1"></td>
                    <td  id="tabExcluir" class="column1" align="center"><input type="hidden" name="idAgenda" id="idAgenda" value="0"><a id="excluir" href="javascript: void(0);"><img src="imagens/icones/inclusao-titular-05.png" /></a></td>
                </tr>
                
                <sql:query var="rs2" dataSource="jdbc/iate">
                    {call SP_HORARIO_PROF_SERV_ACADEMIA (
                    <c:if test='${app == "3215"}'> 0</c:if>
                    <c:if test='${app == "3217"}'> ${profissional.id}</c:if>
                    )}
                </sql:query>                                
                <c:forEach var="row2" items="${rs2.rows}">
                    <tr>
                        <c:choose>
                        <c:when test="${row2.CD_DIA==1}">
                            <td id="tabDia" align="center">Segunda-feira</td>
                        </c:when>
                        <c:when test="${row2.CD_DIA==2}">
                            <td id="tabDia" align="center">Terça-feira</td>
                        </c:when>
                        <c:when test="${row2.CD_DIA==3}">
                            <td id="tabDia" align="center">Quarta-feira</td>
                        </c:when>
                        <c:when test="${row2.CD_DIA==4}">
                            <td id="tabDia" align="center">Quinta-feira</td>
                        </c:when>
                        <c:when test="${row2.CD_DIA==5}">
                            <td id="tabDia" align="center">Sexta-feira</td>
                        </c:when>
                        <c:when test="${row2.CD_DIA==6}">
                            <td id="tabDia" align="center">Sábado</td>
                        </c:when>
                        <c:when test="${row2.CD_DIA==7}">
                            <td id="tabDia" align="center">Domingo</td>
                        </c:when>
                        </c:choose>

                        <td id="tabHorario" align="center">${fn:substring(row2.HH_INICIO,0,2)}:${fn:substring(row2.HH_INICIO,2,4)} às ${fn:substring(row2.HH_FIM,0,2)}:${fn:substring(row2.HH_FIM,2,4)}</td>
                        
                        <td id="tabLocal" align="center"><input type="hidden" name="idLocal" id="idLocal" value="${row2.CD_LOCAL}"> ${row2.DESCR_LOCAL}</td>
                        <td id="tabExcluir" align="center"><input type="hidden" name="idAgenda" id="idAgenda" value="${row2.NU_SEQ_AGENDA}"><a id='excluir' href='javascript: void(0);'><img src='imagens/icones/inclusao-titular-05.png' /></a></td>
                    </tr>
                </c:forEach>
                
            </tbody>
        </table>
                
        <input type="button" id="atualizar" class="botaoatualizar" onclick="return validarForm();" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3212&acao=showForm&id=${idServico}';" value=" " />
    </form>

        
</body>
</html>
