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
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Alunos por Faixa Etária</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2400">
	<input type="hidden" name="acao" value="visualizar">

        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Cursos:<span><input class="botaoMarcaDesmarca" type="button"  onclick="marcaDesmarca('cursos')" style="margin-top: 2px;" value="" title="Consultar" /></span></p>
                    <div class="recuoPadrao" style="overflow:auto;height:270px;width:250px;">
                        <c:forEach var="curso" items="${cursos}">
                            <input type="checkbox" name="cursos" value="${curso.id}">${curso.descricao}<br>
                        </c:forEach>
                    </div>
                </td>
                <td style="vertical-align: top;">
                    <br>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width: 130px">
                        <legend >Tipo:</legend>
                        <br>
                        &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" value="S" checked>Sintético<br><br>
                        &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" value="A">Analítico<br><br>
                        &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" class="legendaCodigo" value="D">Detalhado<br><br>
                    </fieldset>      
                    
                </td>
            </tr>        
        </table>

        
        <br>
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
