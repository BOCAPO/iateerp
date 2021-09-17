<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
            $("#dataInicio").mask("99/99");
            $("#dataFim").mask("99/99");
            
    });    
    
    function visualizar(){
        document.forms[0].acao.value = 'visualizar';
        validarForm();
    }
    
    function gerarArquivo(){
        document.forms[0].acao.value = 'gerarArquivo';
        validarForm();
    }
    
    function gerarEtiqueta(){
        document.forms[0].acao.value = 'gerarEtiqueta';
        validarForm();
    }    
    
    function validarForm(){
        var k = 0;
        for(var i = 0; i < document.forms[0].categorias.length; i++){
            if(document.forms[0].categorias[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Nenhuma Categoria foi selecionada.');
            return;
        }
        
        if(trim(document.forms[0].dataInicio.value) == ''){
            alert('Informe a Data de Início corretamente para o Relatório!');
            return;
        }
        if(trim(document.forms[0].dataFim.value) == ''){
            alert('Informe a Data de Fim corretamente para o Relatório!');
            return;
        }        
        if(!isDiaMesValido(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDiaMesValido(document.forms[0].dataFim.value)){
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Aniversariantes</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1250">
	<input type="hidden" name="acao">
        
        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="height:75px;width:270px;">
                                    <legend >Dia/Mês</legend>
                                    <table class="fmt">
                                        <tr>
                                            <td>
                                                <p class="legendaCodigo MargemSuperior0" >&nbsp;&nbsp;Início</p>
                                                &nbsp;&nbsp;<input type="text" name="dataInicio" id="dataInicio" class="campoSemTamanho alturaPadrao larguraData" maxlength="5">        
                                            </td>
                                            <td>
                                                <p class="legendaCodigo MargemSuperior0" >&nbsp;&nbsp;Fim</p>
                                                &nbsp;&nbsp;<input type="text" name="dataFim" id="dataFim" class="campoSemTamanho alturaPadrao larguraData" maxlength="5">        
                                            </td>
                                        </tr>
                                    </table>

                                    <br>
                                </fieldset>                                
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:270px">
                                    <legend >Sexo</legend>
                                    <input type="radio" name="sexo" class="legendaCodigo" value="T" checked>Todos
                                    <input type="radio" name="sexo" class="legendaCodigo" value="M">Masculino
                                    <input type="radio" name="sexo" class="legendaCodigo" value="F">Feminino
                                </fieldset>      
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:270px">
                                    <legend >Tipo</legend>
                                    <input type="radio" name="tipo" class="legendaCodigo" value="T" checked>Todos
                                    <input type="radio" name="tipo" class="legendaCodigo" value="I">Titulares
                                    <input type="radio" name="tipo" class="legendaCodigo" value="D">Dependentes
                                </fieldset>      
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                <fieldset class="field-set legendaFrame recuoPadrao" style="width:270px">
                                    <legend >Espólio</legend>
                                    <input type="radio" name="espolio" class="legendaCodigo" value="T" checked>Todos
                                    <input type="radio" name="espolio" class="legendaCodigo" value="S">Espólio
                                    <input type="radio" name="espolio" class="legendaCodigo" value="N">Não Espólio
                                </fieldset>      
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <div class="legendaCodigo" style="padding-top:0px;">Categoria</div>
                    <div class="recuoPadrao" style="overflow:auto;height:220px;width:240px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
        </table>
        
                    
        
        <input type="button" onclick="visualizar()" class="botaoatualizar" value="" />        
        <input type="button" onclick="gerarArquivo()" class="botaoGerarArquivo" value="" />
        <input type="button" onclick="gerarEtiqueta()" class="botaoImprimirEtiquetas"  value="" />
        
    </form>

</body>
</html>
