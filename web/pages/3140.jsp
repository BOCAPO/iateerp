<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
        $("#dataInicio").mask("99/99/9999");
        $("#dataFim").mask("99/99/9999");
    }); 
    
    function validarForm(){
        
        
        if(document.forms[0].dataInicio.value == ''
            || document.forms[0].dataFim.value == ''){
            alert('Os campos data de in�cio e data de fim s�o obrigat�rios');
            return;
        }
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }        
            
        var k = 0;
        for(var i = 0; i < document.forms[0].cursos.length; i++){
            if(document.forms[0].cursos[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos 1 Curso para emiss�o do Relat�rio');
            return;
        }

        if(!document.forms[0].matricula.checked
            && !document.forms[0].cancelamento.checked){
            alert('Seleciona pelo menos uma informa��o (matr�culas e/ou cancelamentos)');
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Relat�rio de Matr�culas e Cancelamentos</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="3140">
	<input type="hidden" name="acao" value="visualizar">
        <table class="fmt">
            <tr >
                <td rowspan="5">
                    <p class="legendaCodigo">Cursos:</p>
                    <div class="recuoPadrao" style="overflow:auto;height:300px;width:300px;">
                        <c:forEach var="curso" items="${cursos}">
                            <input type="checkbox" name="cursos" value="${curso.id}">${curso.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="height:75px;width:330px;">
                                    <legend >Per�odo:</legend>
                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                
                                            </td>
                                            <td>
                                                <p class="legendaCodigo MargemSuperior0" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dt. In�cio</p>
                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="dataInicio" id="dataInicio" class="campoSemTamanho alturaPadrao larguraData">        
                                            </td>
                                            <td>
                                                <p class="legendaCodigo MargemSuperior0" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dt. Fim</p>
                                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="dataFim" id="dataFim" class="campoSemTamanho alturaPadrao larguraData">        
                                            </td>
                                        </tr>
                                    </table>
                                        
                                    <br>
                                </fieldset>                                
                            </td>
                        <tr>
                        </tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:330px;">
                                    <legend >Tipo:</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" value="A" checked>Anal�tico
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" value="S">Sint�tico
                                </fieldset>        
                            </td>
                        <tr>
                        </tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:330px;">
                                    <legend >Informa��es:</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="matricula" value="true" checked>Matr�culas
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="cancelamento" value="true" checked>Cancelamentos<br>
                                </fieldset>
                            </td>
                        <tr>
                        </tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:330px;">
                                    <legend >Origem</legend>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="clube" value="true" checked>Clube
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="internet" value="true" checked>Internet<br>
                                </fieldset>
                            </td>
                        <tr>
                        </tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:330px;">
                                    <legend >Ordenar por:</legend>
                                    <input type="radio" name="order" class="legendaCodigo" value="N" checked>Nome
                                    <input type="radio" name="order" class="legendaCodigo" value="T">Data de Matr�cula
                                    <input type="radio" name="order" class="legendaCodigo" value="C">Categoria/T�tulo
                                </fieldset>        
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <br>
        

        
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        

    </form>

</body>
</html>
