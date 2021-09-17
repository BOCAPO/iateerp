<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
  

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "1121"}'>
        <div id="titulo-subnav">Inclusão de Exame Médico</div>
    </c:if>
    <c:if test='${app == "1122"}'>
        <div id="titulo-subnav">Alteração de Exame Médico</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>
    <script type="text/javascript" src="js/jscolor/jscolor.js"></script>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
                
                $("#validade").mask("99/99/9999");

            });

        function validarForm(){

            if(document.forms[0].validade.value == ''){
                alert('Informe corretamente a validade do Exame!');
                return;
            }
            document.forms[0].submit();
        }
    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="matricula" value="${matricula}">
        <input type="hidden" name="categoria" value="${categoria}">
        <input type="hidden" name="dependente" value="${dependente}">
        <input type="hidden" name="id" value="${exame.id}">
        
        <c:if test='${app == "1121"}'>
            <fmt:formatNumber pattern="0000" var="mat" value="${matricula}"/>
            <fmt:formatNumber pattern="00" var="cat" value="${categoria}"/>
            <fmt:formatNumber pattern="00" var="dep" value="${dependente}"/>
        </c:if>
        <c:if test='${app == "1122"}'>
            <fmt:formatNumber pattern="0000" var="mat" value="${exame.matricula}"/>
            <fmt:formatNumber pattern="00" var="cat" value="${exame.categoria}"/>
            <fmt:formatNumber pattern="00" var="dep" value="${exame.dependente}"/>
        </c:if>
        <input type="hidden" name="titulo" value="${mat}${cat}${dep}">

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                    <input type="text" name="descricao" id="descricao" class="campoSemTamanho alturaPadrao"  style="width:350px;" value="${exame.descricao}">
                </td>
            </tr>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Conclusão</p>
                    <input type="text" name="conclusao" id="conclusao" class="campoSemTamanho alturaPadrao"  style="width:350px;" value="${exame.conclusao}">
                </td>
            </tr>
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:350px;height:75px">
                        <legend >Resultado Final:</legend>
                        <table class="fmt">
                            <tr>
                                <td>
                                    
                                    <p class="legendaCodigo MargemSuperior0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Situação</p>
                                    <div class="selectheightnovo">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <select name="resultado" id="admiteDependente" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                                            <option value="A" <c:if test='${exame.resultado == "A"}'>selected</c:if>>Aprovado</option>
                                            <option value="R" <c:if test='${exame.resultado == "R"}'>selected</c:if>>Reprovado</option>
                                        </select>
                                    </div>        
                                </td>
                                <td>
                                    <fmt:formatDate var="validade" value="${exame.dtValidade}" pattern="dd/MM/yyyy"/>
                                    <p class="legendaCodigo MargemSuperior0" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Validade</p>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="validade" id="validade" class="campoSemTamanho alturaPadrao"  style="width:110px;" value="${validade}">
                                </td>
                            </tr>
                        </table>
                    </fieldset>                                
                </td>
            </tr>
        </table>
                                
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1120&titulo=${mat}${cat}${dep}&acao=listaExame';" value=" " />

    </form>

</body>
</html>
