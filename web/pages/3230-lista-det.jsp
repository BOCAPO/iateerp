<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}

</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
                $("#pesqSocio").hide();
                $("#divSupervisao").hide();
                $("#divAutenticacaoFalta").hide();
                
        });     
        
        function pesquisaSocio(dtInicio, dtFim, idFuncionario, idAgenda, idExcecao) {
            $("#dtInicio").val(dtInicio);
            $("#dtFim").val(dtFim);
            $("#idFuncHidden").val(idFuncionario);
            $("#idAgenda").val(idAgenda);
            $("#idExcecao").val(idExcecao);
             
            $("#pesqSocio").show();
            var tabela = $('#tabelaSocio').find('tbody').empty();
            $("#nomeSocio").val('');
            $("#matriculaSocio").val('');
            $("#categoriaSocio").val('');
            $("#categoriaSocio").focus();
        }
        
        function cancelaPesquisa() {
            $("#pesqSocio").hide();
        }        
        
        function selecionaSocio(indice){
            $("#pesqSocio").hide();
            var nome='';
            var titulo='';
            $("#tabelaSocio tr:eq("+(parseInt(indice)+1)+")").each(function(index){
                nome = $(this).find('#tabNome').html();
                titulo = $(this).find('#campoTitulo').val();
            });


            var msgRetorno = '';
            $.ajax({url:'AgendaAcademiaAjax', async:false, dataType:'text', type:'GET',data:{
                                tipo:1,
                                titulo:titulo,
                                dtInicio:$('#dtInicio').val(),
                                idServico:$('#idServico').val(),
                                idFuncionario:$('#idFuncHidden').val()}
            }).success(function(retorno){
                msgRetorno = retorno;
            });

            $("#titulo").val(titulo);
            $("#acao").val("agendar");
            $("#app").val("3231");
            
            if (msgRetorno=='OK'){
                document.forms[0].submit();
            }else{
                if (confirm(msgRetorno + " Para continuar será necessária a autorização do supervisor. Deseja continuar?")){
                    mostraSupervisao();
                }
            }
        }
        
        function carregaSocio(){
            
            $.ajax({url:'ReservaLugarEventoAjax', type:'GET',
                                data:{
                                nome:$('#nomeSocio').val(),
                                matricula:$('#matriculaSocio').val(),
                                categoria:$('#categoriaSocio').val()
                                }
            }).success(function(retorno){
                if (retorno.erro !== undefined){
                    alert(retorno.erro);
                }else {
                    var tabela = $('#tabelaSocio').find('tbody').empty();
                    var linha="";
                    var campoHidden="";
                    var tpCadastro="";
                    var tpCategoria="";
                    $.each(retorno.jogador, function(i) {
                        if (this.dependente==0){
                            tpCadastro="TITULAR";
                        }else{
                            tpCadastro="DEPENDENTE";
                        }
                        if (this.tpCategoria=="SO"){
                            tpCategoria="SÓCIO";
                        }else{
                            tpCategoria="NÃO SÓCIO";
                        }
                    
                        campoHidden='<input type="hidden" id="campoTitulo"  value="'+this.titulo+'"/>'
                        linha='<tr><td class="column1"><a href="#" id="tabNome" class="column1" onclick="selecionaSocio('+i+')">'+this.nome+'</a></td><td class="column1" align="center">'+this.matricula+' '+campoHidden+'</td><td class="column1">'+tpCadastro+'</td><td class="column1">'+this.descricao+'</td><td class="column1">'+tpCategoria+'</td></tr>';
                        tabela.append(linha)
                    });
                }
            });
        }
        
        function mostraSupervisao() {
            $('#userSupervisao').val('');
            $('#usuario').val('');
            $('#senha').val('');
            $('#divSupervisao').show();
        }
        
        function cancelaSupervisao() {
            $('#divSupervisao').hide();
        }

        function atualizaSupervisao() {
            if ($('#usuario').val() == ''){
                alert('Informe o Usuário!');
                return;
            }
            if ($('#senha').val() == ''){
                alert('Informe a Senha!');
                return;
            }

            var ret = "";
            $.ajax({url:'UsuarioAjax', async:false, dataType:'text', type:'GET',
                                data:{
                                tipo:1,
                                usuario:$('#usuario').val(),
                                senha:$('#senha').val(),
                                aplicacao:3235
                                }
            }).success(function(retorno){
                ret = retorno;
            });

            if (ret!="OK"){
                alert(ret);
                return false;
            }        

            $('#userSupervisao').val($('#usuario').val());

            document.forms[0].submit();
        }
        

        function mostraAutentitacaoFalta() {
            $('#usuarioFalta').val('');
            $('#userFalta').val('');
            $('#senhaFalta').val('');
            $('#divAutenticacaoFalta').show();
        }
        
        function cancelaAutentitacaoFalta() {
            $('#divAutenticacaoFalta').hide();
        }

        function atualizaAutenticacaoFalta() {
            if ($('#usuarioFalta').val() == ''){
                alert('Informe o Usuário!');
                return;
            }
            if ($('#senhaFalta').val() == ''){
                alert('Informe a Senha!');
                return;
            }

            var ret = "";
            $.ajax({url:'UsuarioAjax', async:false, dataType:'text', type:'GET',
                                data:{
                                tipo:1,
                                usuario:$('#usuarioFalta').val(),
                                senha:$('#senhaFalta').val(),
                                aplicacao:3234
                                }
            }).success(function(retorno){
                ret = retorno;
            });

            if (ret!="OK"){
                alert(ret);
                return false;
            }        

            $('#userFalta').val($('#usuarioFalta').val());

            document.forms[0].submit();
        }
    
    
        function desmarcarFalta(idAgendamento){
            $("#idAgendamento").val(idAgendamento);
            $("#acao").val("desmarca");
            $("#app").val("3234");
            
            mostraAutentitacaoFalta();
        }
        
	function marcarFalta(idAgendamento){
            $("#idAgendamento").val(idAgendamento);
            $("#acao").val("marca");
            $("#app").val("3234");
            
            mostraAutentitacaoFalta();
        }
        
	function cancelarAgendamento(idAgendamento){
            var msgRetorno = '';
            $.ajax({url:'AgendaAcademiaAjax', async:false, dataType:'text', type:'GET',data:{
                                tipo:2,
                                idAgendamento:idAgendamento}
            }).success(function(retorno){
                msgRetorno = retorno;
            });
            
            $("#idAgendamento").val(idAgendamento);
            $("#acao").val("cancelar");
            $("#app").val("3233");

            if (msgRetorno=='OK'){
                if (confirm('Confirma o cancelamento do agendamento selecionado?')){
                    document.forms[0].submit();
                }
            }else{
                if (confirm(msgRetorno + " Para continuar será necessária a autorização do supervisor. Deseja continuar?")){
                    mostraSupervisao();
                }
            }
        }
        
        function imprimeRelatorio(){
            $("#acao").val("relatorio");
            
            document.forms[0].submit();
        }
        
        function submeteConsulta(){
            $("#acao").val("det");
            
            document.forms[0].submit();
        }

        
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Agenda da Academia</div>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" id="app" value="3230">
        <input type="hidden" name="acao" id="acao" value="det">
        <input type="hidden" name="data" id="data" value="${data}">
        <input type="hidden" name="dtInicio" id="dtInicio" value="">
        <input type="hidden" name="dtFim" id="dtFim" value="">
        <input type="hidden" name="idFuncHidden" id="idFuncHidden" value="">
        <input type="hidden" name="idAgenda" id="idAgenda" value="">
        <input type="hidden" name="idExcecao" id="idExcecao" value="">
        <input type="hidden" name="titulo" id="titulo" value="">
        <input type="hidden" name="userSupervisao" id="userSupervisao" value="">
        <input type="hidden" name="userFalta" id="userFalta" value="">
        <input type="hidden" name="idAgendamento" id="idAgendamento" value="">
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Data</p>
                    <input type="text" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value="${data}" disabled />
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Serviço</p>
                    <div class="selectheightnovo">
                        <select name="idServico" id="idServico" class="campoSemTamanho alturaPadrao larguraCidade" onchange="javascript: submeteConsulta();">
                            <c:forEach var="servico" items="${servicos}">
                                <option value="${servico.id}" <c:if test='${idServico == servico.id}'>selected</c:if>>${servico.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Profissional:</p>
                    <div class="selectheightnovo">
                        <select name="idFuncionario" class="campoSemTamanho alturaPadrao larguraCidade" onchange="javascript: submeteConsulta();">
                            <option value="0">TODOS</option>
                            <c:forEach var="funcionario" items="${funcionarios}">
                                <option value="${funcionario.id}" <c:if test='${idFuncionario == funcionario.id}'>selected</c:if>>${funcionario.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Turno:</p>
                    <div class="selectheightnovo">
                        <select name="turno" class="campoSemTamanho alturaPadrao " onchange="javascript: submeteConsulta();">
                            <option value="0" <c:if test='${turno == "0"}'>selected</c:if>>TODOS</option>
                            <option value="M" <c:if test='${turno == "M"}'>selected</c:if>>Manhã</option>
                            <option value="T" <c:if test='${turno == "T"}'>selected</c:if>>Tarde</option>
                            <option value="N" <c:if test='${turno == "N"}'>selected</c:if>>Noite</option>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Tipo de Vaga:</p>
                    <div class="selectheightnovo">
                        <select name="tipoVaga" class="campoSemTamanho alturaPadrao " onchange="javascript: submeteConsulta();">
                            <option value="T" <c:if test='${tipoVaga == "T"}'>selected</c:if>>TODAS</option>
                            <option value="C" <c:if test='${tipoVaga == "C"}'>selected</c:if>>Apenas Clube</option>
                            <option value="I" <c:if test='${tipoVaga == "I"}'>selected</c:if>>Apenas Internet</option>
                        </select>
                    </div>
                </td>
                <td>
                    <c:if test='<%=request.isUserInRole("3235")%>'>
                        <input type="checkbox" class="legendaCodigo MargemSuperior0" onchange="javascript: submeteConsulta();" style="margin-top:20px" <c:if test="${ignorarAbertura}">checked</c:if> name="ignorarAbertura"  id="ignorarAbertura" value="true"/><span class="legendaSemMargem larguraData">Ignorar Abertura</span>
                    </c:if>
                </td>
                
                <td>
                    <div style="margin-top:25px;margin-left:15px;">
                        <a href="javascript:imprimeRelatorio()"><img src="imagens/btn-imprimir.png" width="100" height="25" /></a>
                    </div>
                </td>
            </tr>
        </table>
        <div class="divisoria"></div>
    </form>
    
    <br>               
 
    
        
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" align="center">Horário</th>
            <th scope="col" align="center">Profissional</th>
            <th scope="col" align="center">Duração</th>
            <th scope="col" align="center">Agendamentos</th>
            <th scope="col" align="center">Agendar</th>
        </tr>	
        </thead>
        <tbody>
            
            <sql:query var="rs" dataSource="jdbc/iate">
                {call SP_AGENDA_DIA_ACADEMIA ('${dataYMD}', ${idServico}, ${idFuncionario}, '${turno}', '${tipoVaga}', 
                <c:choose>
                    <c:when test="${ignorarAbertura}">
                        'S'
                    </c:when>
                    <c:otherwise>
                        NULL
                    </c:otherwise>
                </c:choose>    
                )}
            </sql:query>     
                
            <c:forEach var="row" items="${rs.rows}">
                <tr>
                    <fmt:formatDate var="dtTemp" value="${row.DT_INICIO}" pattern="HH:mm" />         
                    <td align="center">${dtTemp}</td>
                    <td align="center">${row.NOME_FUNCIONARIO}</td>
                    <td align="center">${row.QT_MIN_ATENDIMENTO} Min.</td>
                    <td align="center">
                        <table id="tabela" style="width:98%;margin-left:15px;">
                            <thead>
                            <tr class="odd">
                                <th scope="col" align="center">Aluno</th>
                                <th scope="col" align="center">Origem</th>
                                <th scope="col" align="center">Cancelar</th>
                                <th scope="col" align="center">Marcar Falta</th>
                                <th scope="col" align="center">Desmarcar Falta</th>
                            </tr>	
                            </thead>
                            <tbody>

                                <fmt:formatDate var="dtInicioTemp" value="${row.DT_INICIO}" pattern="yyyy-MM-dd HH:mm:ss" />         
                                <fmt:formatDate var="dtFimTemp" value="${row.DT_FIM}" pattern="yyyy-MM-dd HH:mm:ss" />         
                                <sql:query var="rs2" dataSource="jdbc/iate">
                                    {call SP_AGEND_ACADEMIA_HORARIO (${row.NU_SEQ_SERVICO}, ${row.CD_FUNCIONARIO}, '${dtInicioTemp}')}
                                </sql:query>     
                                <c:forEach var="row2" items="${rs2.rows}">
                                    <tr>
                                        <td align="center">${row2.NOME_PESSOA}</td>
                                        <td align="center">${row2.ORIGEM}</td>
                                        <td align="center">
                                            <a href="#" onclick="cancelarAgendamento(${row2.NU_SEQ_AGENDAMENTO})">
                                                <c:if test='<%=request.isUserInRole("3233")%>'>
                                                    <img src="imagens/icones/inclusao-titular-05.png" />
                                                </c:if>
                                            </a>
                                        </td>
                                        <td align="center">
                                            <c:if test='${row2.IC_COMPARECIMENTO == null}'>
                                                <a href="#" onclick="marcarFalta(${row2.NU_SEQ_AGENDAMENTO})">
                                                    <c:if test='<%=request.isUserInRole("3234")%>'>
                                                        <img src="imagens/icones/inclusao-titular-16.png" />
                                                    </c:if>
                                                </a>
                                            </c:if>
                                        </td>
                                        <td align="center">
                                            <c:if test='${row2.IC_COMPARECIMENTO == "N"}'>
                                                <a href="#" onclick="desmarcarFalta(${row2.NU_SEQ_AGENDAMENTO})">
                                                    <img src="imagens/icones/inclusao-titular-16.png" />
                                                </a>
                                            </c:if>
                                        </td>
                                    </tr>	
                                </c:forEach>
                            </tbody>
                        </table>
                    </td>    
                    <td align="center">
                        <a href="javascript:pesquisaSocio('${dtInicioTemp}', '${dtFimTemp}', ${row.CD_FUNCIONARIO}, ${row.NU_SEQ_AGENDA}, ${row.NU_SEQ_EXCECAO});">
                            <c:if test='<%=request.isUserInRole("3231")%>'>
                                <img src="imagens/icones/inclusao-titular-02.png"/>
                            </c:if>
                        </a>
                        <div style="color:rgb(255,0,0);">
                            <c:set var="saldo" value="${row.QT_VAGA - row.QT_AGENDAMENTO}"/>
                            <c:if test='${saldo le 0}'>
                                LOTADO
                            </c:if>
                            <c:if test='${saldo gt 0}'>
                                (${saldo} vaga(s) restante(s))
                            </c:if>
                        <div>
                    </td>
                </tr>	
            </c:forEach>
        </tbody>
    </table>
    
    <c:set var="mes" value="${fn:substring(data, 3, 5)}" />
    <c:set var="ano" value="${fn:substring(data, 6, 10)}" />
    
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3230&mes=${mes}&ano=${ano}&idServico=${idServico}&idFuncionario=${idFuncionario}&turno=${turno}';" value=" " />
    
    
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                           TABELA DE SELECAO DE SOCIO
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->

    <div id="pesqSocio" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 700px; height:400px;">
        <table style="background:#fff">
            <tr>
              <td>
                <div class="divisoria"></div>
                <div id="titulo-subnav">Seleção de Sócio</div>
                <div class="divisoria"></div>
                
                <table class="fmt" align="left">
                    <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0">Categoria</p>
                            <input type="text" id="categoriaSocio" name="categoriaSocio" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                        </td>

                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Matricula</p>
                            <input type="text" id="matriculaSocio" name="matriculaSocio" class="campoSemTamanho alturaPadrao larguraNumero" value="">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Nome</p>
                            <input type="text" id="nomeSocio" name="nomeSocio" class="campoSemTamanho alturaPadrao" style="width:300px" value="">
                        </td>
                        <td >    
                            <input type="button" class="botaobuscainclusao" style="margin-top:20px" onclick="carregaSocio()" value="" title="Consultar" />
                        </td>
                    </tr>
                </table>
                <br><br><br>
                <table id="tabelaSocio" align="left" style="margin-left:15px;">
                    <thead>
                    <tr class="odd">
                        <th scope="col" class="nome-lista">Nome</th>
                        <th scope="col" align="left">Matric</th>
                        <th scope="col" align="left">Tp Cadastro</th>
                        <th scope="col" align="left">Categoria</th>
                        <th scope="col" align="left">Tp Categoria</th>
                    </tr>	
                    </thead>
                    <tbody>
                    </tbody>
                </table>  
                <br>
                <input style="margin-left:15px;" type="button" id="cmdcancelar" name="cmdPesquisa" class="botaocancelar" onclick="cancelaPesquisa()" />
            </td>
          </tr>
        </table>
    </div>
        
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                           AUTORIZACAO DA SUPERVISAO
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->
    <div id="divSupervisao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 430px; height:400px;">
    <table style="background:#fff">
        <tr>
          <td>
            <div class="divisoria"></div>
            <div id="titulo-subnav">Supervisão</div>
            <div class="divisoria"></div>
            <table class="fmt" align="left" align="left">
              <tr>
                <td>
                    <p class="legendaCodigo">Usuário</p>
                    <input type="text" id="usuario"  name="usuario" maxlength="50" class="campoSemTamanho alturaPadrao larguraData" value=""/>
                </td>
                <td>
                    <p class="legendaCodigo">Senha</p>
                    <input type="password" id="senha" name="senha" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value=""/>
                </td>
              </tr>
            </table>  
            <table class="fmt" align="left" align="left">
              <tr>
                <td>
                    <input style="margin-left:15px;margin-top:10px;" type="button" class="botaoatualizar" onclick="atualizaSupervisao()"  />
                </td>
                <td>
                    <input style="margin-left:15px;" type="button" class="botaocancelar" onclick="cancelaSupervisao()" />
                </td>
              </tr>
            </table>  
        </td>
      </tr>
    </table>
    </div>    
        
    <!--
        ************************************************************************
        ************************************************************************
        ************************************************************************
                        LOGIN PARA MARCAR E DESMARCAR FALTA
        ************************************************************************
        ************************************************************************
        ************************************************************************
    -->
    <div id="divAutenticacaoFalta" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 430px; height:400px;">
    <table style="background:#fff">
        <tr>
          <td>
            <div class="divisoria"></div>
            <div id="titulo-subnav">Autenticação</div>
            <div class="divisoria"></div>
            <table class="fmt" align="left" align="left">
              <tr>
                <td>
                    <p class="legendaCodigo">Usuário</p>
                    <input type="text" id="usuarioFalta"  name="usuarioFalta" maxlength="50" class="campoSemTamanho alturaPadrao larguraData" value=""/>
                </td>
                <td>
                    <p class="legendaCodigo">Senha</p>
                    <input type="password" id="senhaFalta" name="senhaFalta" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value=""/>
                </td>
              </tr>
            </table>  
            <table class="fmt" align="left" align="left">
              <tr>
                <td>
                    <input style="margin-left:15px;margin-top:10px;" type="button" class="botaoatualizar" onclick="atualizaAutenticacaoFalta()"  />
                </td>
                <td>
                    <input style="margin-left:15px;" type="button" class="botaocancelar" onclick="cancelaAutentitacaoFalta()" />
                </td>
              </tr>
            </table>  
        </td>
      </tr>
    </table>
    </div>    
        
</body>
</html>
