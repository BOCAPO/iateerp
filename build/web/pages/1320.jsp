<%@include file="head.jsp"%>

<style>
    table {border:none;width:400px;margin-left:15px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>        


<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].numeroArmario.value == ''){
            alert('O campo número é de preenchimento obrigatório');
            return;
        }
        
        document.forms[0].submit();
    }

</script>
<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Inclusão de Armário</div>
    <div class="divisoria"></div>
    

    <form method="POST" action="c">
    <input type="hidden" name="app" value="${app}">
    <input type="hidden" name="acao" value="gravar">
    <table align="left">
      <tr>
        <td>

            <p class="legendaCodigo tiraMargem">Tipo:</p>
            <select name="tipoArmario" class="campoSemTamanho tiraMargem alturaPadrao larguraComboCategoria">
                <c:forEach var="tipo" items="${tipos}">
                    <option value="${tipo.id}">${tipo.descricao}</option>
                </c:forEach>
            </select> <br>
        </td>
        <td >
            <p class="legendaCodigo tiraMargem">Número:</p>
            <input type="text" name="numeroArmario" maxlength="3" class="campoSemTamanho alturaPadrao larguraNumero tiraMargem"  />
        </td>
      </tr>

    </table>   
        
    <br><br><br><br>

    <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1320';" value=" " />
        
</body>
</html>