<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  


<script type="text/javascript" language="JavaScript">    
    
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
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Alunos por Modalidade</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1280">
	<input type="hidden" name="acao" value="visualizar">

        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Modalidade:</p>
                    <select name="idModalidade" onchange="javascript: document.location='c?app=1280&idModalidade=' + document.forms[0].idModalidade.value;" class="campoSemTamanho alturaPadrao" style="width:200px">
                        <option value="0">TODAS</option>
                        <c:forEach var="modalidade" items="${modalidades}">
                            <option value="${modalidade.id}" <c:if test='${idModalidade == modalidade.id}'>selected</c:if>>${modalidade.descricao}</option>
                        </c:forEach>
                    </select>        
                </td>
                <td>
                    <br>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox"  class="legendaCodigo MargemSuperior0" name="somentePassaportesVigentes" value="true"><spam class="legendaSemMargem larguraData">Somente Passaportes Vigêntes<br></spam>
                </td>
            </tr>        
        </table>
                     
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Cursos:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('cursos')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:210px;">
                        <c:forEach var="curso" items="${cursos}">
                            <input type="checkbox" name="cursos" value="${curso.id}">${curso.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo">Categorias:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('categorias')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:200px;width:210px;">
                        <c:forEach var="categoria" items="${categorias}">
                            <input type="checkbox" name="categorias" value="${categoria.id}">${categoria.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>        
        </table>

        
        <fieldset class="field-set legendaFrame recuoPadrao" style="width: 437px">
            <legend >Ordenar por:</legend>
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="order" class="legendaCodigo" value="N" checked>Nome
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="order" class="legendaCodigo" value="M">Matrícula
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="order" class="legendaCodigo" value="T">Data de Matrícula
            &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="order" class="legendaCodigo" value="D">Desconto
        </fieldset>      
        <br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
