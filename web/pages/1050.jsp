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
    <c:if test='${app == "1051"}'>
        <div id="titulo-subnav">Inclusão de Funcionário</div>
    </c:if>
    <c:if test='${app == "1052"}'>
        <div id="titulo-subnav">Alteração de Funcionário</div>
    </c:if>
    <div class="divisoria"></div>

    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
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
            
            $("#cpf").mask("999.999.999-99");

        });
        
        
        function validarCPF(cpf) {  
            cpf = cpf.replace(/[^\d]+/g,'');    
            if(cpf == '') return false; 
            // Elimina CPFs invalidos conhecidos    
            if (cpf.length != 11 || 
                cpf == "00000000000" || 
                cpf == "11111111111" || 
                cpf == "22222222222" || 
                cpf == "33333333333" || 
                cpf == "44444444444" || 
                cpf == "55555555555" || 
                cpf == "66666666666" || 
                cpf == "77777777777" || 
                cpf == "88888888888" || 
                cpf == "99999999999")
                    return false;       
            // Valida 1o digito 
            add = 0;    
            for (i=0; i < 9; i ++)       
                add += parseInt(cpf.charAt(i)) * (10 - i);  
                rev = 11 - (add % 11);  
                if (rev == 10 || rev == 11)     
                    rev = 0;    
                if (rev != parseInt(cpf.charAt(9)))     
                    return false;       
            // Valida 2o digito 
            add = 0;    
            for (i = 0; i < 10; i ++)        
                add += parseInt(cpf.charAt(i)) * (11 - i);  
            rev = 11 - (add % 11);  
            if (rev == 10 || rev == 11) 
                rev = 0;    
            if (rev != parseInt(cpf.charAt(10)))
                return false;       
            return true;   
        }  
        
        function validarForm(){
            if(document.forms[0].primeiroNome.value == ''
                || document.forms[0].nome.value == ''
                || document.forms[0].matricula.value == ''
                || document.forms[0].cpf.value == ''
                ){
                alert('É obrigatório informar o primeiro nome, matricula, nome e CPF!');
                return;
            }
            
            if (!validarCPF(document.forms[0].cpf.value)){
                alert('CPF inválido! Informe o CPF corretamente!');
                return;
            }

            document.forms[0].submit();

        }

    </script>
    
    <form action="c">
        <input type="hidden" name="idFuncionario" value="${funcionario.id}">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">

        <table align="left" >
            <tr>
                <td rowspan="4" >.
                    <img src="f?tb=5&cd=${funcionario.id}" onerror="this.src='images/tenis/avatar-default.png';" class="recuoPadrao MargemSuperiorPadrao" width="120" height="160">
                </td>    
                <td>
                    &nbsp
                </td>    
            </tr>    
            
            <tr>
                <td>
                    <table align="left">
                      <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Primeiro Nome:</p>
                            <input type="text" name="primeiroNome" class="campoSemTamanho alturaPadrao" maxlength="20" style="width:85px;" value="${funcionario.primeiroNome}">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Matrícula:</p>
                            <input type="text" name="matricula"  onkeypress="onlyNumber(event)" maxlength="6" style="width:50px;" class="campoSemTamanho alturaPadrao" value="${funcionario.matricula}">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Login:</p>
                            <input type="text" name="login" class="campoSemTamanho alturaPadrao" maxlength="50"  style="width:85px;" value="${funcionario.login}">
                        </td>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >Sangue:</p>
                            <input type="text" name="sangue" class="campoSemTamanho alturaPadrao" maxlength="4"  style="width:50px;" value="${funcionario.sangue}">
                        </td>
                      </tr>    
                    </table>
                </td>    
            </tr>    
            <tr>
              <td colspan="2">
                  <p class="legendaCodigo MargemSuperior0" >Nome:</p>
                  <input type="text" name="nome" class="campoSemTamanho alturaPadrao" maxlength="45" style="width:320px;" value="${funcionario.nome}">
              </td>
            </tr>
            <tr>
              <td colspan="2">
                  <p class="legendaCodigo MargemSuperior0" >Endereço:</p>
                  <input type="text" name="endereco" class="campoSemTamanho alturaPadrao" maxlength="30"  style="width:320px;" value="${funcionario.endereco}">
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <table align="left">
                  <tr>
                    <td>
                  
                        <p class="legendaCodigo MargemSuperior0" >Setor:</p>
                        <select name="idSetor" class="campoSemTamanho alturaPadrao" style="width:225px;" >
                            <c:forEach var="setor" items="${setores}">
                                <option value="${setor.id}" <c:if test="${funcionario.idSetor eq setor.id}">selected</c:if>>${setor.descricao}</option>
                            </c:forEach>                   
                        </select>        
                      </td>
                      <td>
                        <p class="legendaCodigo MargemSuperior0" >Cargo:</p>
                        <select name="idCargo" class="campoSemTamanho alturaPadrao" style="width:225px;" >
                            <c:forEach var="cargo" items="${cargos}">
                              <option value="${cargo.id}" <c:if test="${funcionario.idCargo eq cargo.id}">selected</c:if>>${cargo.descricao}</option>
                            </c:forEach>
                        </select>        
                      </td>
                    </tr> 
                  </table>  
                </td>
            </tr>                              
            <tr>
              <td colspan="2">
                <table align="left">
                    <tr>
                        <td colspan="2">
                            <p class="legendaCodigo MargemSuperior0" >Telefones:</p>
                            <input type="text" name="telefone1" class="campoSemTamanho alturaPadrao" maxlength="25"  style="width:162px;" value="${funcionario.telefone1}">
                            <input type="text" name="telefone2" class="campoSemTamanho alturaPadrao" maxlength="25" style="width:162px;" value="${funcionario.telefone2}">
                        </td>
                        <td>
                            <fmt:formatNumber var="valorMaximoConsumo" value="${funcionario.valorMaximoConsumo}" pattern="#0.00"/>
                            <c:if test="${empty valorMaximoConsumo}"><c:set var="valorMaximoConsumo" value="0,00"/></c:if>
                            <p class="legendaCodigo MargemSuperior0" >Vr Máx. Consumo:</p>
                            <input type="text" name="valorMaximoConsumo" class="campoSemTamanho alturaPadrao larguraData" onkeypress="onlyPositiveFloat(event)" value="${valorMaximoConsumo}"><br>
                        </td>
                    </tr> 
                  </table>  
                </td>
            </tr>
            <tr>
              <td colspan="2">
                <table align="left">
                  <tr>
                    <td>
                        <br>
                        <fieldset class="field-set legendaFrame recuoPadrao" style="width: 230px">
                            <legend >Tipo</legend>
                            <input type="radio" name="tipo" class="legendaCodigo" <c:if test='${funcionario.tipo eq "F" or empty funcionario}'>checked</c:if> value="F">Funcionário
                            <input type="radio" name="tipo" class="legendaCodigo" <c:if test='${funcionario.tipo eq "C"}'>checked</c:if> value="C">Concessionário
                        </fieldset>
                    </td>
                    <td>
                        <br>
                        <fieldset class="field-set legendaFrame recuoPadrao "  style="width: 218px">
                            <legend >Estacionamento</legend>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="estacionamento" class="legendaCodigo" value="I" <c:if test='${funcionario.estacionamento eq "I" or empty funcionario}'>checked</c:if>>Interno
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="estacionamento" class="legendaCodigo" value="E" <c:if test='${funcionario.estacionamento eq "E"}'>checked</c:if>>Externo
                        </fieldset>
                    </td>
                  <tr>
                </table>    
            </tr>
            <tr>
                <td>
                    <table align="left">
                      <tr>
                        <td>
                            <p class="legendaCodigo MargemSuperior0" >CPF:</p>
                            <input type="text" name="cpf" id="cpf" class="campoSemTamanho alturaPadrao" maxlength="11" style="width:85px;" value="${funcionario.CPF}">
                        </td>
                      </tr>    
                    </table>
                </td>    
            </tr>    
        </table>    
        <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
        
        <div class="divisoria"></div>
        <div id="titulo-subnav">Horários</div>
        <div class="divisoria"></div>
                        
        <table align="left" >
            <tr>
                <td colspan="2" >
                    <p class="legendaCodigo MargemSuperior0" >Segunda</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Terça</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Quarta</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Quinta</p>
                </td>   
            </tr>
            <tr>    
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="segundaEntrada" name="segundaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${funcionario.horarios[0].entrada}">
              </td>
              <td align="left">
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;" >Saída:</p>
                  <input type="text" id="segundaSaida" name="segundaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${funcionario.horarios[0].saida}">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="tercaEntrada" name="tercaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${funcionario.horarios[1].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="tercaSaida" name="tercaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${funcionario.horarios[1].saida}">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="quartaEntrada" name="quartaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${funcionario.horarios[2].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="quartaSaida" name="quartaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${funcionario.horarios[2].saida}">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="quintaEntrada" name="quintaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${funcionario.horarios[3].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="quintaSaida" name="quintaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${funcionario.horarios[3].saida}">
              </td>
            </tr>    
            <tr>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
            </tr>    
            <tr>
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Sexta</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Sábado</p>
                </td>   
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>   
                <td colspan="2">
                    <p class="legendaCodigo MargemSuperior0" >Domingo</p>
                </td>   
            </tr>    
            <tr>    
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="sextaEntrada" name="sextaEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${funcionario.horarios[4].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="sextaSaida" name="sextaSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${funcionario.horarios[4].saida}">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="sabadoEntrada" name="sabadoEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${funcionario.horarios[5].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="sabadoSaida" name="sabadoSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${funcionario.horarios[5].saida}">
              </td>
              <td>&nbsp;&nbsp;</td>   
              <td>
                  <p class="legendaCodigo MargemSuperior0" >Entrada:</p>
                  <input type="text" id="domingoEntrada" name="domingoEntrada" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao" style="width:40px;" value="${funcionario.horarios[6].entrada}">
              </td>
              <td>
                  <p class="legendaCodigoSemMargem MargemSuperior0" style="margin-left:5px;">Saída:</p>
                  <input type="text" id="domingoSaida" name="domingoSaida" maxlength="4" size="4" onkeypress="onlyNumber(event)" class="campoSemTamanhoSemMargem alturaPadrao" style="margin-left:5px; width:40px;" value="${funcionario.horarios[6].saida}">
              </td>
            </tr>    
       </table>
       <br><br><br><br><br><br><br><br><br>
       
       <input type="button" onclick="validarForm()" class="botaoatualizar" value="">
       <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1050';" value="" />
    </form>

    
</body>
</html>
