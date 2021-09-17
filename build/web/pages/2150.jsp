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
        var k = 0;
        for(var i = 0; i < document.forms[0].cursos.length; i++){
            if(document.forms[0].cursos[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Nenhum Curso foi selecionado.');
            return;
        }
        
        k = 0;
        for(var i = 0; i < document.forms[0].categorias.length; i++){
            if(document.forms[0].categorias[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Nenhuma Categoria foi selecionada.');
            return;
        }
        
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }        
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Desconto dos Alunos</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2150">
	<input type="hidden" name="acao" value="visualizar">
        
        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:75px;width:295px;">
                        <legend >Dt. Aniversário:</legend>
                        <table class="fmt">
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dt. Início</p>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="dataInicio" id="dataInicio" class="campoSemTamanho alturaPadrao larguraData">        
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dt. Fim</p>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="dataFim" id="dataFim" class="campoSemTamanho alturaPadrao larguraData">        
                                </td>
                            </tr>
                        </table>

                        <br>
                    </fieldset>                                
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="height:75px;width:295px;"> 
                        <legend >% de Desconto:</legend>
                        <table class="fmt">
                            <tr>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Inícial</p>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="descontoInicio" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </td>
                                <td>
                                    <p class="legendaCodigo MargemSuperior0" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Final</p>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="descontoFim" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero">        
                                </td>
                            </tr>
                        </table>
                    </fieldset>                                
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo">Cursos:</p>
                    <div class="recuoPadrao" style="overflow:auto;height:300px;width:300px;">
                        <c:forEach var="curso" items="${cursos}">
                            <input type="checkbox" name="cursos" value="${curso.id}">${curso.descricao}<br>
                        </c:forEach>
                    </div>
                <td>
                    <p class="legendaCodigo">Categorias:</p>
                    <div class="recuoPadrao" style="overflow:auto;height:300px;width:300px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
        </table>

        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        

    </form>

</body>
</html>
