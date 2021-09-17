<%@page import="techsoft.util.Datas"%>
<%@include file="head.jsp"%>

<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    #autorizacao {
        left: 25%;
        top: 25%;
        position: fixed;
    }
</style>


<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>        
    
    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
                $("#atualizar").live("click", validarForm);
                $('#autorizacao').hide();
                $("#autorizar").live("click", atualizarSupervisor);
                $("#supervisao").live("click", mostraDivSupervisao);
                $("#cancelarSupervisao").live("click", escondeDivSupervisao);
                
        });
            
        function validarForm(){
            
            if ($('#churrasqueira').val() < 0){
                alert('Selecione uma churrasqueira disponIvel!');
                return;
            }
            
            var ret = '';
            $.ajax({url:'RecaptchaAjax', async:false, dataType:'text', type:'GET',data:{
                                token:grecaptcha.getResponse()
                            }
            }).success(function(retorno){
                ret = retorno;
            });
            if(ret != "OK"){
                alert("Verificação inconsistênte/Captcha inválido!");
                grecaptcha.reset();
                return false;
            }
            
            var ret = "";
            
            $.ajax({url:'ReservaChurrasqueiraAjax', async:false, dataType:'text', type:'GET',
                                data:{
                                tipo:8,
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                dependente:$('#dependente').val(),
                                dtUtil:$('#dtUtil').val(),
                                idDep:$('#churrasqueira').val(),
                                interessado:$('#nome').val(),
                                telefone:$('#telefone').val(),
                                horario:$('#horario').val(),
                                supervisao:$('#supervisao').val(),
                                motivoBloqueio:$('#motivoBloqueio').val()
                                }
            }).success(function(retorno){
                ret = retorno;
            });

            if (ret != "OK"){
                alert(ret);
                grecaptcha.reset();
                return false;
            }else{
                alert('Reserva incluída com sucesso!');
                
                var dtDMY = $('#dtUtil').val().substring(8, 10) +  "/" + $('#dtUtil').val().substring(5, 7) +  "/" + $('#dtUtil').val().substring(0, 4);
                
                window.location.href='c?app=1065&acao=consultar'+
                                     '&dtUtilizacao=' + dtDMY;
                
            }

            return;
            
            //////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////

            var ret = "";
            
            //Validacoes obrigatorias (se a churrasqueira esta liberada e se o socio nao tem outra reserva para o dia)
            $.ajax({url:'ReservaChurrasqueiraAjax', async:false, dataType:'text', type:'GET',
                                data:{
                                tipo:2,
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                data:$('#dtUtil').val(),
                                dep:$('#idDep').val(),
                                tpValidacao:0
                                }
            }).success(function(retorno){
                ret = retorno;
            });

            if (ret != "OK"){
                alert(ret);
                return false;
            }    
            
            
            //Verifica o prazo de antecedencia de 30 dias
            $.ajax({url:'ReservaChurrasqueiraAjax', async:false, dataType:'text', type:'GET',
                                data:{
                                tipo:2,
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                data:$('#dtUtil').val(),
                                dep:$('#idDep').val(),
                                tpValidacao:1
                                }
            }).success(function(retorno){
                prazo = retorno;
            });

            //Autoriza a reserva com base nas regras de tamanho e período
            var rac = "";
            $.ajax({url:'ReservaChurrasqueiraAjax', async:false, dataType:'text', type:'GET',
                                data:{
                                tipo:2,
                                matricula:$('#matricula').val(),
                                categoria:$('#categoria').val(),
                                data:$('#dtUtil').val(),
                                dep:$('#idDep').val(),
                                tpValidacao:2
                                }
            }).success(function(retorno){
                rac = retorno;
            });
            
            //alert(prazo);
            //alert(rac);
            
            var msg = "";
            if (prazo != 'OK' || rac != 'OK'){
                if (prazo != 'OK'){
                    if (rac != 'OK'){
                        //tudo com problema
                        msg = 'A reserva não atende ao artigo 7º do Regulamento do Setor de Churrasqueiras e está sendo realizada para um prazo superior a 30 dias, para continuar será necessária a autorização do superior. Deseja continuar?';
                    }else{
                        //só o prazo com problema
                        msg = 'Para reservas de churrasqueiras com prazo superior a 30 dias é necessária a autorização do superior. Deseja continuar?!';
                    }
                }else{
                    //só o rac
                    msg = 'A reserva não atende ao artigo 7º do Regulamento do Setor de Churrasqueiras, para continuar será necessária a autorização do superior. Deseja continuar?';
                }
            }
            if (msg != ""){
                if (confirm(msg)){
                    $('#autorizacao').show();
                }

                return false;
            }

            $('#acao').val('gravar');
            document.forms[0].submit();

        }
        
        function validaPesquisa(){
            $('#acao').val('mostraChurrasqueiras');
            document.forms[0].submit();
        }
        
        function mostraDivSupervisao(){
            $('#autorizacao').show();
        }
        function escondeDivSupervisao(){
            $('#autorizacao').hide();
        }
        
        function atualizarSupervisor(){
            if ($('#userSupervisor').val() == ''){
                alert('Informe o Supervisor!');
                return false;
            }
            if ($('#userSupervisor').val() == $('#usuarioAtual').val()){
                alert('O Supervisor não pode ser o mesmo usuário logado no sistema!');
                return false;
            }
            if ($('#senhaSupervisor').val() == ''){
                alert('Informe a Senha do Supervisor!');
                return false;
            }

            var ret = "";
            $.ajax({url:'ReservaChurrasqueiraAjax', async:false, dataType:'text', type:'GET',
                                data:{
                                tipo:5,
                                usuario:$('#userSupervisor').val(),
                                senha:$('#senhaSupervisor').val()
                                }
            }).success(function(retorno){
                ret = retorno;
            });
            
            if (ret!="OK"){
                alert(ret);
                return false
            }
            
            $('#supervisao').val($('#userSupervisor').val());
            alert('Supervisão validada com sucesso! A reserva será marcada como autorizada pelo supervisor.');
            escondeDivSupervisao();
            
            if ($('#churrasqueira').size() == 0){
                document.forms[0].submit();
            }

        }
            
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Inclusão de Reserva de Churrasqueira</div>
    <div class="divisoria"></div>
    
    <form method="POST">
        <input type="hidden" name="app" value="1066"/>
        <input type="hidden" id="acao" name="acao" value="${acao}"/>   
        <input type="hidden" id="matricula" name="matricula" value="${socio.matricula}"/>
        <input type="hidden" id="categoria" name="categoria" value="${socio.idCategoria}"/>
        <input type="hidden" id="dependente" name="dependente" value="${socio.seqDependente}"/>
        <input type="hidden" id="dtUtil" name="dtUtil" value="${dtUtil}"/>
        <input type="hidden" id="interessado" name="interessado" value="${socio.nome}"/>
        <input type="hidden" id="usuarioAtual" name="usuarioAtual" value="${usuarioAtual}"/>
        <input type="hidden" id="supervisao" name="supervisao" value="${supervisao}"/>
        <input type="hidden" id="supervisao" name="titulo" value="${titulo}"/>
        <input type="hidden" id="parmHorario" name="parmHorario" value="${parmHorario}"/>
    
        <table class="fmt" align="left" >
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Nome</p>
                  <input id="nome" type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:255px" disabled value="${socio.nome}">
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Telefone Contato</p>
                  <input id="telefone" type="text" name="telefone" class="campoSemTamanho alturaPadrao" style="width:165px" value="${socio.telefoneR}">
              </td>
            </tr>
        </table>
        <br><br><br><br>
        <table class="fmt" align="left" >
            
            <tr>
              <td>
                  <p class="legendaCodigo MargemSuperior0">Mês</p>
                  <div class="selectheightnovo">
                    <select name="mes" class="campoSemTamanho alturaPadrao" style="width: 120px">
                        
                        <c:forEach var="row" items="${meses}">
                            <c:set var="item" value="${row.cod};${row.descricao}"/>
                            <c:set var="mes" value="${row.cod}"/>
                            <% 
                                int j = Integer.parseInt(pageContext.getAttribute("mes").toString());
                                pageContext.setAttribute("nomeMes", Datas.nomeMes(j));
                            %>                                    
                            <option value="${item}" <c:if test="${parmMes eq item}">selected</c:if>>${nomeMes}/${row.descricao}</option>
                        </c:forEach>
                            
                    </select>
                  </div>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Período</p>
                  <div class="selectheightnovo">
                    <select name="horario" id="horario" class="campoSemTamanho alturaPadrao"  style="width: 200px">
                      <option value="D" <c:if test="${parmHorario eq 'D'}">selected</c:if>>Diurno (09:00 às 18:00) </option>
                      <option value="N" <c:if test="${parmHorario eq 'N'}">selected</c:if>>Noturno (18:00 às 22:00) </option>
                    </select>
                  </div>
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Dia</p>
                  <div class="selectheightnovo">
                    <select name="dia" class="campoSemTamanho alturaPadrao"  style="width: 60px">
                        <c:forEach var="dia" begin="1" end="31" step="1">
                            <option <c:if test="${parmDia eq dia}">selected</c:if> value="${dia}">${dia}</option>
                        </c:forEach>
                    </select>
                  </div>
              </td>
              <td >    
                  <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validaPesquisa()" value="" title="Consultar" />
              </td>
            </tr>
        </table>    
        <br/><br/><br/><br>
        
        <c:if test="${acao eq 'mostraChurrasqueiras'}">
            <sql:query var="rs" dataSource="jdbc/iate">
                EXEC SP_CONS_CHURRASQUEIRA_INTERNET '${dtUtil}', '${supervisao}'
            </sql:query>
                            
            <c:if test="${rs.rowCount gt 0}">
                <c:choose>
                    <c:when test="${rs.rows[0].MSG eq 'OK' || rs.rows[0].MSG eq 'NO'}">
                        
                        <table class="fmt" align="left" >
                            <tr>
                              <td>
                                <div class="selectheightnovo">
                                  <select id="churrasqueira" name="churrasqueira" class="campoSemTamanho alturaPadrao"  style="width: 300px">
                        
                                    <c:forEach var="row" items="${rs.rows}">
                                        <c:choose>
                                            <c:when test="${row.MSG == 'OK'}">
                                                <option style="color: green;" value="${row.SEQ_DEPENDENCIA}">${row.DESCRICAO}</option>
                                            </c:when>
                                            <c:when test="${row.MSG == 'NO'}">
                                                <option style="color: red;" value="0">${row.DESCRICAO}</option>
                                            </c:when>
                                        </c:choose>
                                        
                                    </c:forEach>
                                        
                                  </select>
                                </div>
                              </td>
                            </tr>
                        </table>
                        <br/><br/><br/>
                        
                        <c:if test="${socio.matricula == 9999}">
                            <table class="fmt" align="left" >
                                <tr>
                                  <td>
                                      <p class="legendaCodigo MargemSuperior0" >Motivo Bloqueio</p>
                                      <input id="motivoBloqueio" type="text" name="motivoBloqueio" class="campoSemTamanho alturaPadrao" maxlength="50" style="width:440px" value="">
                                  </td>
                                </tr>
                            </table>
                            <br/><br/><br/>
                        </c:if>

                        <br>
                        <div style="margin-left: 16px" class="g-recaptcha" data-sitekey="6LfMnBEUAAAAAFiLisfWUK-QgJ0eXB2zGZYoKKF5"></div>
                        
                        <br/>
                        <input type="button" id="atualizar" class="botaoatualizar" value=" " />
                        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1066&acao=consultarSocio&dtUtil=${dtUtil}&matricula=${socio.matricula}&categoria=${socio.idCategoria}&idDep=${idDep}';" value=" " />
                    </c:when>
                    <c:otherwise>
                        <script type='text/javascript'>
                            var erro = '${rs.rows[0].MSG}';
                            alert(erro);
                        </script>
                        
                        <br/>
                        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1066&acao=consultarSocio&dtUtil=${dtUtil}&matricula=${socio.matricula}&categoria=${socio.idCategoria}&idDep=${idDep}';" value=" " />
                    </c:otherwise>
                </c:choose>
            </c:if>
        </c:if>
                        
        <c:if test="${acao ne 'mostraChurrasqueiras'}">
            <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1066&acao=consultarSocio&dtUtil=${dtUtil}&matricula=${socio.matricula}&categoria=${socio.idCategoria}&idDep=${idDep}';" value=" " />
        </c:if>
        <c:if test="${supervisao eq '' || supervisao eq null}">
            <input type="button" id="supervisao" style="width:165px; height:34px; background:url('/iate/imagens/btn-supervisao.png') no-repeat; margin-left:5px; cursor:pointer; border:none;"  onclick="" value=" " />
        </c:if>
            
        <c:if test="${supervisao ne '' && supervisao ne null}">
            <div id="titulo-subnav">Autorização do(a) supervisor(a): ${supervisao}</div>
        </c:if>
        

        <div id="autorizacao" >
            <table style="background:#fff">
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Autorização</div>
                    <div class="divisoria"></div>
                    <table class="fmt" align="left" align="left">
                      <tr>
                        <td>
                            <p class="legendaCodigo">Supervisor</p>
                            <input type="text" id="userSupervisor"  name="userSupervisor" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value=""/>
                        </td>
                        <td>
                            <p class="legendaCodigo">Senha</p>
                            <input type="password" id="senhaSupervisor" name="senhaSupervisor" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value=""/>
                        </td>
                      </tr>
                      <tr>
                        <td>
                            <br>
                            <input type="button" id="autorizar" class="botaoatualizar" value=" " />
                        </td>
                        <td>
                            <br>
                            <input type="button" id="cancelarSupervisao" class="botaocancelar" value=" " style="margin-top: 0px;margin-left: 10px" />
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
