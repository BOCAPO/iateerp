<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  
  

<body class="internas">
            
    <%@include file="menuCaixa.jsp"%>
    
    <div class="divisoria"></div>
    <c:if test='${acao == "showAbrir"}'>
        <div id="titulo-subnav">Abertura de Caixa</div>
    </c:if>
    <c:if test='${acao== "showFechar"}'>
        <div id="titulo-subnav">Fechamento de Caixa</div>
    </c:if>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="${app}">
        <c:if test='${acao == "showAbrir"}'>
            <input type="hidden" name="acao" value="abrir">
        </c:if>
        <c:if test='${acao== "showFechar"}'>
            <input type="hidden" name="acao" value="fechar">
        </c:if>

        
        <c:if test='${msg == "OK"}'>
            <table>
                <tr>
                    <td>
                        <p class="legendaCodigo MargemSuperior0" >Caixa</p>
                        <input type="text" name="caixa" class="campoSemTamanho alturaPadrao larguraData" disabled value="${caixa}">
                    </td>
                <tr>
                </tr>
                    <td>
                        <p class="legendaCodigo MargemSuperior0" >Computador</p>
                        <input type="text" name="computador" class="campoSemTamanho alturaPadrao larguraData" disabled value="${computador}">
                    </td>
                <tr>
                </tr>
                    <td>
                        <p class="legendaCodigo MargemSuperior0">Data</p>
                        <input type="text" name="data" class="campoSemTamanho alturaPadrao larguraData" disabled value="${data}">
                    </td>
                <tr>
                </tr>
                    <td>
                        <br>
                        <input type="submit" class="botaoatualizar"  value=" " />
                    </td>
                </tr>
            </table>
        </c:if>
        <c:if test='${msg != "OK"}'>
            <br>
            <h1 class="msgErro">${msg} </h1>                
        </c:if>
        
    </form>

</body>
</html>
