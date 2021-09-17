<%@page import="techsoft.curso.TurmaHorario"%>
<%@page import="techsoft.curso.Turma"%>
<%@include file="head.jsp"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
<link rel="stylesheet" type="text/css" href="css/calendario.css"/>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
    
input[type="file"]
{
}    
    
</style>  

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "1231"}'>
        <div id="titulo-subnav">Inclusão de Permissão Provisória</div>
    </c:if>
    <c:if test='${app == "1232"}'>
        <div id="titulo-subnav">Alteração de Permissão Provisória</div>
    </c:if>
    <div class="divisoria"></div>

    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
            $("#inicio").mask("99/99/9999");
            $("#fim").mask("99/99/9999");
            
            $("#segundaEntrada").mask("99:99");
            $("#tercaEntrada").mask("99:99");
            $("#quartaEntrada").mask("99:99");
            $("#quintaEntrada").mask("99:99");
            $("#sextaEntrada").mask("99:99");
            $("#sabadoEntrada").mask("99:99");
            $("#domingoEntrada").mask("99:99");
            $("#segundaSaida").mask("99:99");
            $("#tercaSaida").mask("99:99");
            $("#quartaSaida").mask("99:99");
            $("#quintaSaida").mask("99:99");
            $("#sextaSaida").mask("99:99");
            $("#sabadoSaida").mask("99:99");
            $("#domingoSaida").mask("99:99");
        });
        
        function validarForm(){

            if(document.forms[0].atividade.value == ''
                || document.forms[0].nome.value == ''
                || document.forms[0].inicio.value == ''
                || document.forms[0].fim.value == ''){
                alert('É obrigatório informar o Nome, Atividade, Início e Fim');
                return;
            }

            document.forms[0].submit();

        }

    </script>
    
    <form action="c">
        <input type="hidden" name="idPermissao" value="${permissao.id}">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <table align="left" >
            <tr>
                <td rowspan="4" >.
                    <img src="f?tb=2&cd=${permissao.id}" onerror="this.src='images/tenis/avatar-default.png';" class="recuoPadrao MargemSuperiorPadrao" width="120" height="160">
                </td>    
                <td>
                    &nbsp
                </td>    
            </tr>    
            
            <tr>
              <td>
                  <table class="fmt">
                      <tr>
                          <td>
                            <p class="legendaCodigo MargemSuperior0" >Nome:</p>
                            <input type="text" id="nome" name="nome" class="campoSemTamanho alturaPadrao" style="width:335px;" maxlength="40" value="${permissao.nome}">
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
                                <p class="legendaCodigo MargemSuperior0" >Atividade:</p>
                                <input type="text" id="atividade" name="atividade" class="campoSemTamanho alturaPadrao" style="width:335px;" maxlength="50" value="${permissao.atividade}">
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
                            <fmt:formatDate var="inicio" value="${permissao.inicio}" pattern="dd/MM/yyyy"/>
                            <p class="legendaCodigo MargemSuperior0" >Início:</p>
                            <input type="text" id="inicio" name="inicio" class="campoSemTamanho alturaPadrao" style="width:70px;" value="${inicio}">
                          </td>
                          <td>
                            <fmt:formatDate var="fim" value="${permissao.fim}" pattern="dd/MM/yyyy"/>
                            <p class="legendaCodigo MargemSuperior0" >Fim:</p>
                            <input type="text" id="fim" name="fim" class="campoSemTamanho alturaPadrao" style="width:70px;" value="${fim}">
                          </td>
                          <td>
                            <fieldset class="field-set legendaFrame recuoPadrao "  style="width: 160px;margin-top: 3px;">
                                <legend >Estacionamento</legend>
                                <input type="radio" name="estacionamento" class="legendaCodigo" value="I" <c:if test='${permissao.estacionamento eq "I" or empty permissao}'>checked</c:if>>Interno
                                <input type="radio" name="estacionamento" class="legendaCodigo" value="E" <c:if test='${permissao.estacionamento eq "E"}'>checked</c:if>>Externo
                            </fieldset>
                           </td>
                          
                      </tr>
                  </table>
                      
              </td>
            </tr>
        </table>    
                            
        <br><br><br><br><br><br><br><br><br><br><br>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Horários</div>
        <div class="divisoria"></div>
                        
        <table align="left" >
            <tr>
                <td colspan="2" >
                    <p class="legendaCodigo MargemSuperior0" >Segunda</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Terça</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Quarta</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Quinta</p>
                </td>   
            </tr>
            <tr>    
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="segundaEntrada" name="segundaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${permissao.horarios[0].entrada}">
              </td>
              <td align="left">
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;" >Saída:</p>
                  <input type="text" id="segundaSaida" name="segundaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${permissao.horarios[0].saida}">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="tercaEntrada" name="tercaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${permissao.horarios[1].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="tercaSaida" name="tercaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${permissao.horarios[1].saida}">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="quartaEntrada" name="quartaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${permissao.horarios[2].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="quartaSaida" name="quartaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${permissao.horarios[2].saida}">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="quintaEntrada" name="quintaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${permissao.horarios[3].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="quintaSaida" name="quintaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${permissao.horarios[3].saida}">
              </td>
            </tr>    
            <tr>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
            </tr>    
            <tr>
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Sexta</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Sábado</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Domingo</p>
                </td>   
            </tr>    
            <tr>    
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="sextaEntrada" name="sextaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${permissao.horarios[4].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="sextaSaida" name="sextaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${permissao.horarios[4].saida}">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="sabadoEntrada" name="sabadoEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${permissao.horarios[5].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="sabadoSaida" name="sabadoSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${permissao.horarios[5].saida}">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="domingoEntrada" name="domingoEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${permissao.horarios[6].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="domingoSaida" name="domingoSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${permissao.horarios[6].saida}">
              </td>
            </tr>    
       </table>
       <br><br><br><br><br><br><br><br><br>
       
       <input type="button" onclick="validarForm()" class="botaoatualizar" value="">
       <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1230';" value="" />
    </form>

    
</body>
</html>
