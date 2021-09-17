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
                
                $("#hhInicio").mask("99:99");
                $("#hhFim").mask("99:99");

                $("#dtInicio").mask("99/99/9999");
                $("#dtFim").mask("99/99/9999");
                
                $("#dtAbertura").mask("99/99/9999 99:99:99");
                
        });        
        
        function validarForm(){

            if(document.forms[0].dtInicio.value == ''){
                alert('O campo Dt. Início é de preenchimento obrigatório!');
                $('#dtInicio').focus();
                return false
            }
            if(document.forms[0].dtFim.value == ''){
                alert('O campo Dt. Fim é de preenchimento obrigatório!');
                $('#dtInicio').focus();
                return false
            }
            if(document.forms[0].hhInicio.value == ''){
                alert('O campo Hora Início é de preenchimento obrigatório!');
                $('#hhInicio').focus();
                return false
            }
            if(document.forms[0].hhFim.value == ''){
                alert('O campo Hora Fim é de preenchimento obrigatório!');
                $('#hhFim').focus();
                return false
            }
            if(!isDataValida(document.forms[0].dtInicio.value)){
                $('#dtInicio').focus();
                return false;
            }
            if(!isDataValida(document.forms[0].dtFim.value)){
                $('#dtFim').focus();
                return false;
            }
            if (!isHoraValida($('#hhInicio').val())){
                $('#hhInicio').focus();
                return;
            }
            if (!isHoraValida($('#hhFim').val())){
                $('#hhFim').focus();
                return;
            }
            if(parseInt($('#hhFim').val().replace(':', '')) <= parseInt($('#hhInicio').val().replace(':', ''))){
                alert('A hora de fim deve ser deve ser depois da hora de início!');
                return;
            }            
            
            if ($('#tipo').val()=='A'){
                if ($('#idServico').val()==0){
                    alert('O campo Serviço é de preenchimento obrigatório para exceções de Abertura!');
                    return false
                }
                if ($('#idFuncionario').val()==0){
                    alert('O campo Profissional é de preenchimento obrigatório para exceções de Abertura!');
                    return false
                }
                if($('#dtAbertura').val() != ''){
                    if(!isDataHoraValida($('#dtAbertura').val())){
                        $('#dtAbertura').focus();
                        return false;
                    }
                }
            }else{
                if(document.forms[0].dtAbertura.value != ''){
                    alert('Para exceções de fechamento não informe a Data de Abertura!');
                    $('#dtAbertura').focus();
                    return false
                }
            }
            
            
           if (
              !$('#icSegunda').prop('checked') &&
              !$('#icTerca').prop('checked') &&
              !$('#icQuarta').prop('checked') &&
              !$('#icQuinta').prop('checked') &&
              !$('#icSexta').prop('checked') &&
              !$('#icSabado').prop('checked') &&
              !$('#icDomingo').prop('checked')
              ){
                alert('Selecione pelo menos 1 dia da semana!');
                return false;
            }
            
            document.forms[0].submit();
        }
        
    </script>

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Inclusão de Exceção da Agenda da Academia</div>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <table class="fmt">

            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Tipo</p>
                    <div class="selectheightnovo">
                        <select name="tipo" id="tipo" class="campoSemTamanho alturaPadrao"  style="width:80px;">
                            <option value="B">Bloqueio</option>
                            <option value="A">Abertura</option>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Serviço</p>
                    <div class="selectheightnovo">
                        <select name="idServico" id="idServico" class="campoSemTamanho alturaPadrao larguraCidade">
                            <option value="0">TODOS</option>
                            <c:forEach var="servico" items="${servicos}">
                                <option value="${servico.id}" >${servico.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Profissional</p>
                    <div class="selectheightnovo">
                        <select name="idFuncionario" id="idFuncionario" class="campoSemTamanho alturaPadrao larguraCidade">
                            <option value="0">TODOS</option>
                            <c:forEach var="funcionario" items="${funcionarios}">
                                <option value="${funcionario.id}" >${funcionario.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Início</p>
                    <input type="text" name="dtInicio" id="dtInicio" class="campoSemTamanho alturaPadrao" style="width:65px;" value="">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Fim</p>
                    <input type="text" name="dtFim" id="dtFim" class="campoSemTamanho alturaPadrao" style="width:65px;" value="">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Hora Início</p>
                    <input type="text" name="hhInicio" id="hhInicio" class="campoSemTamanho alturaPadrao" style="width:65px;" value="">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Hora Fim</p>
                    <input type="text" name="hhFim" id="hhFim" class="campoSemTamanho alturaPadrao" style="width:65px;" value="">
                </td>
            </tr>
        </table>
        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao"  style="width:745px; height: 43px">
                        <legend >Dias da Semana:</legend>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="icSegunda" id="icSegunda" value="S">Segunda-Feira
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="icTerca" id="icTerca" value="S">Terça-Feira
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="icQuarta" id="icQuarta" value="S">Quarta-Feira
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="icQuinta" id="icQuinta" value="S">Quinta-Feira
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="icSexta" id="icSexta" value="S">Sexta-Feira
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="icSabado" id="icSabado" value="S">Sábado
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="icDomingo" id="icDomingo" value="S">Domingo
                    </fieldset>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Dt. Abertura</p>
                    <input type="text" name="dtAbertura" id="dtAbertura" class="campoSemTamanho alturaPadrao" style="width:110px;" value="">
                </td>
            </tr>
        </table>
                
        <input type="button" id="atualizar" class="botaoatualizar" onclick="return validarForm();" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3220';" value=" " />
    </form>

        
</body>
</html>
