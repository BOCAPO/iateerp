<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%> 

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    #pesquisa {
        margin-top: -50px;
        margin-left: -50px;
        left: 50%;
        top: 50%;
        position: fixed;
    }
    
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            
            $("#divCancela").hide();
            $("#pesquisa").hide();
            $('#divSupervisao').hide();
            $("#mesAno").mask("99/9999");

            
    });    

    function atualizaCancelamento() {
        if(trim($('#motivo').val()) == '' ){
            alert('Informe o motivo do cancelamento!');
            return false;
        } else {
            $("#divCancela").hide();
            $('#divSupervisao').show();
        }
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
                            aplicacao:1068
                            }
        }).success(function(retorno){
            ret = retorno;
        });
        
        if (ret!="OK"){
            alert(ret);
            return false
        }        
        
        $("#app").val('1062');
        $("#motivoParm").val($('#motivo').val());
        document.forms[0].submit();
    }
    
    function cancelaEvento(id) {
        $("#id").val(id);
        $("#divCancela").show();
    }
    function cancelaCancelamento() {
        $("#divCancela").hide();
    }
    
    function validarForm(){
        if(trim(document.forms[0].mesAno.value) == '' ){
            alert('Informe o Mês/Ano para consulta!');
            return false;
        } else {
            document.forms[0].submit();
        }
    }
    
    function mostraDiv(dia, mes, ano){
        $('#dia').val(dia);
        $('#mes').val(mes);
        $('#ano').val(ano);
        $("#pesquisa").show();
    }
    
    function submetePesquisa(){

        window.location.href='c?app=1061'+
                             '&dia='+document.forms[0].dia.value+
                             '&mes='+document.forms[0].mes.value+
                             '&ano='+document.forms[0].ano.value+
                             '&dep='+document.forms[0].dep.value+
                             '&acao=incluir'+
                             '&tipoPessoa='+$("input[name='tipoPessoa']:checked").val();

    }
    
    
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Consulta Reserva de Dependências</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" id="app" value="1060">
        <input type="hidden" name="acao" value="">
        <input type="hidden" name="dia" id="dia" value="">
        <input type="hidden" name="mes" id="mes" value="">
        <input type="hidden" name="ano" id="ano" value="">
        <input type="hidden" name="id" id="id" value="">
        <input type="hidden" name="motivoParm" id="motivoParm" value="">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                <p class="legendaCodigo MargemSuperior0">Dependencia:</p>
                <div class="selectheightnovo">
                    <select name="dep" class="campoSemTamanho alturaPadrao" style="width:180px;" onchange="validarForm()">
                        <c:forEach var="dependencia" items="${dependencias}">
                            <option value="${dependencia.id}" <c:if test="${dep eq dependencia.id}">selected</c:if>>${dependencia.descricao}</option>
                        </c:forEach>
                    </select>
                </div>        
              </td>
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Mês/Ano</p>
                  <input type="text" name="mesAno" id="mesAno" class="campoSemTamanho alturaPadrao" style="width:60px;" value="${mesAno}">
              </td>
              <td>    
                  <input class="botaobuscainclusao" style="margin-top:20px" type="button" onclick="validarForm()" value="" title="Consultar" />
              </td>
            </tr>
        </table>
    </form>
    
    <br><br><br>

                
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
            <tr class="odd">
                <th scope="col">Dia</th>
                <th scope="col">Eventos Agendados</th>
                <th scope="col">Incluir</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach  var="dia" begin="0" end="${qtDiasMes}">  
                <tr>
                    <td align="center" class="legendaCodigo" style="background:#f4f9fe; padding-top:0px;">${dia+1}</td>
                    <td class="column1">
                        <c:set var="mes" value="${fn:substring(mesAno, 0, 2)}" />
                        <c:set var="ano" value="${fn:substring(mesAno, 2, 6)}" />
                        <sql:query var="rs" dataSource="jdbc/iate">
                            SELECT * 
                            FROM TB_RESERVA_DEPENDENCIA 
                            WHERE SEQ_DEPENDENCIA = ${dep} AND
                            DAY(DT_INIC_UTILIZACAO) = ${dia}+1 AND
                            MONTH(DT_INIC_UTILIZACAO) = ${mes} AND
                            YEAR(DT_INIC_UTILIZACAO) = ${ano} AND
                            CD_STATUS_RESERVA <> 'CA'
                            ORDER BY DT_INIC_UTILIZACAO
                        </sql:query>    
                            
                        <c:if test='${rs.rowCount>0}'>
                            <table id="tabela1">
                                <thead>
                                    <tr class="odd">
                                        <th scope="col">Período</th>
                                        <th scope="col">Responsável</th>
                                        <th scope="col">Situação</th>
                                        <th scope="col">Alterar</th>
                                        <th scope="col">Excluir</th>
                                        <th scope="col">Imprimir</th>
                                    </tr>
                                </thead>
                                <c:forEach var="row" items="${rs.rows}">
                                    <tr>
                                        <fmt:formatDate var="inicio" value="${row.DT_INIC_UTILIZACAO}" pattern="dd/MM/yyyy HH:mm"/>
                                        <fmt:formatDate var="fim" value="${row.DT_FIM_UTILIZACAO}" pattern="dd/MM/yyyy HH:mm"/>

                                        <td class="column1" align="center">${inicio} a ${fim}</td>
                                        <td class="column1">${row.NOME_INTERESSADO}</td>
                                        <c:choose>
                                            <c:when test='${row.CD_STATUS_RESERVA == "AC"}'>
                                                <td align="center" style="background-color: green;">
                                            </c:when>
                                            <c:when test='${row.CD_STATUS_RESERVA == "CF"}'>
                                                <td align="center" style="background-color: red;">
                                            </c:when>
                                        </c:choose>
                                                
                                        <td class="column1" align="center">
                                            <a href="c?app=1061&acao=alterar&id=${row.SEQ_RESERVA}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                                        </td>
                                        <c:if test='<%=request.isUserInRole("1062")%>'>
                                            <td class="column1" align="center">    
                                                <a href="#" onclick="cancelaEvento(${row.SEQ_RESERVA})"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                                            </td>
                                        </c:if>
                                        <td class="column1" align="center">
                                            <a href="c?app=1060&acao=imprimir&id=${row.SEQ_RESERVA}"><img src="imagens/icones/inclusao-titular-13.png"/></a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:if>
                    </td>
                    <td class="column1" align="center">
                        <a href="#" onclick="mostraDiv(${dia+1},${mes},${ano})"><img src="imagens/icones/inclusao-titular-01.png"/></a>
                    </td>
                </tr>
            </c:forEach>              
            

        </tbody>
    </table>

        
 

    <div id="divCancela" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 490px; height:400px;">
        <table style="background:#fff">
            <tr>
              <td>
                <div class="divisoria"></div>
                <div id="titulo-subnav">Cancelamento de Evento</div>
                <div class="divisoria"></div>
                
                <table class="fmt" align="left" >
                    <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Motivo</p>
                            <input type="text" id="motivo" name="motivo" class="campoSemTamanho alturaPadrao" style="width:400px;" value="">
                        </td>
                    </tr>
                </table>
                <br><br><br>
                <input style="margin-left:15px;margin-top:05px;" type="button" class="botaoatualizar" onclick="atualizaCancelamento()"  />
                <input style="margin-left:15px;" type="button" class="botaocancelar" onclick="cancelaCancelamento()" />
            </td>
          </tr>
        </table>
    </div>   
    
    <div id="pesquisa" >
        <table style="background:#fff">
            <tr>
              <td>
                <div class="divisoria"></div>
                <div id="titulo-subnav">Tipo de Pessoa</div>
                <div class="divisoria"></div>
                <table class="fmt" align="left" align="left">
                  <tr>
                    <td>
                        <input type="radio" id="tipoPessoa" name="tipoPessoa" checked value="S"/>Sócio<br>
                        <input type="radio" id="tipoPessoa" name="tipoPessoa" value="N"/>Não Sócio<br>
                    </td>
                  </tr>
                  <tr>
                    <td>
                        <br>
                        <input type="button" id="cmdPesquisa" name="cmdPesquisa" class="botaoatualizar" value=" " onclick="submetePesquisa()" value="" />
                    </td>
                  </tr>
                </table>  
            </td>
          </tr>
        </table>
    </div>        

    <div id="divSupervisao" style="overflow:auto;left: 25%;top: 20%;position: fixed;width: 430px; height:400px;">
    <table style="background:#fff">
        <tr>
          <td>
            <div class="divisoria"></div>
            <div id="titulo-subnav">Cancelamento</div>
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
    <table>
</div>        



</body>
</html>
