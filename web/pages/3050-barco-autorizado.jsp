<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${acao == "incluirPessoa"}'>
        <div id="titulo-subnav">Inclusão de Pessoas Autorizadas</div>
    </c:if>
    <c:if test='${acao == "alterarPessoa"}'>
        <div id="titulo-subnav">Alteração de Pessoas Autorizadas</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <script type="text/javascript" language="JavaScript">

        function validarForm(){

            if(document.forms[0].nome.value == ''){
                alert('O Nome do autorizado dever ser informado!');
                return;
            }

            document.forms[0].submit();
        }

    </script>

    <form action="c" method="POST">
        <input type="hidden" name="app" value="2002">
        <input type="hidden" name="acao" value="${acao}">
        <input type="hidden" name="idAutorizado" value="${pesAutorizada.id}">
        <input type="hidden" name="idBarco" value="${barco.id}">

        <table class="fmt">
        <tr>
            <td>
                <p class="legendaCodigo MargemSuperior0" >Sócio:</p>
                <input type="text" class="campoSemTamanho alturaPadrao larguraNomePessoa" disabled name="socio" value="${barco.socio.nome}">
            </td>    
            <td>
                <p class="legendaCodigo MargemSuperior0" >Barco:</p>
                <input type="text" name="barco" class="campoSemTamanho alturaPadrao larguraNomePessoa" disabled value="${barco.nome}">
            <td>
        </tr>
        <tr>
            <td>
                <p class="legendaCodigo MargemSuperior0" >Nome</p>
                <input type="text" class="campoSemTamanho alturaPadrao larguraNomePessoa" name="nome" value="${pesAutorizada.nome}"><br>    
            </td>    
            <td>
                <p class="legendaCodigo MargemSuperior0" >OBS</p>
                <input type="text" class="campoSemTamanho alturaPadrao larguraNomePessoa" name="obs"  value="${pesAutorizada.obs}"><br>    
            <td>
        </tr>
        </table>


                
        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2002&acao=showListaPessoa&idBarco=${barco.id}';" value=" " />
    </form>

</body>
</html>
