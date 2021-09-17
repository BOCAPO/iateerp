



<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
  

<body class="internas">
            
    <%@include file="menuAcesso.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${app == "1411"}'>
        <div id="titulo-subnav">Inclusão de Taxas</div>
    </c:if>
    <c:if test='${app == "1412"}'>
        <div id="titulo-subnav">Alteração de Taxas</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].descricao.value == ''){
                alert('A descrição deve ser informada!');
                return;
            }
            document.forms[0].submit();
        }

    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <input type="hidden" name="acao" value="gravar">
        <input type="hidden" name="id" value="${taxa.id}">

        <table>
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Descrição</p>
                    <input type="text" name="descricao" class="campoSemTamanho alturaPadrao"  style="width:272px;" value="${taxa.descricao}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Tipo</p>
                    <div class="selectheightnovo">
                        <select name="tipo" class="campoSemTamanho alturaPadrao" style="width:100px;" >
                            <option value="G" <c:if test='${taxa.tipo == "G"}'>selected</c:if>>Geral</option>
                            <option value="E" <c:if test='${taxa.tipo == "E"}'>selected</c:if>>Específica</option>
                            <option value="I" <c:if test='${taxa.tipo == "I"}'>selected</c:if>>Individual</option>
                            <option value="C" <c:if test='${taxa.tipo == "C"}'>selected</c:if>>Crédito</option>
                        </select>
                    </div>        
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Receita</p>
                    <div class="selectheightnovo">
                        <select name="receita" class="campoSemTamanho alturaPadrao" style="width:200px;" >
                            <option value="I" <c:if test='${taxa.receita == "I"}'>selected</c:if>>Crédito para o Iate</option>
                            <option value="C" <c:if test='${taxa.receita == "C"}'>selected</c:if>>Repasse para Concessionário</option>
                        </select>
                    </div>        
                </td>
            </tr>
        </table>
            
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1410';" value=" " />

    </form>

</body>
</html>
