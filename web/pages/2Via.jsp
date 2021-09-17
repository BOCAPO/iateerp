
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em;}
</style>  
<style type="text/css">
    #local {
        margin-top: -110px;
        margin-left: -250px;
        left: 50%;
        top: 50%;
        position: fixed;
    }
</style>


<script type="text/javascript" language="JavaScript">
    function submete(acao){
        $('#subacao').val(acao);
        document.forms[0].submit();
    }
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuCaixa.jsp"%>
        
    
    <form action="c" method="POST" onsubmit="return validarForm()">
        <input type="hidden" name="app" id="app" value="6040">
        <input type="hidden" name="acao" id="acao" value="2via">
        <input type="hidden" name="subacao" id="subacao" value="">
        <input type="hidden" name="retorno" id="retorno" value="${retorno}">
        <c:forEach var="s" items="${linhas}">
            <input type="hidden" name="linha" value="${s}">
        </c:forEach>
        
        <div id="local" >
            <table>
                <tr>
                  <td>
                    <div class="divisoria"></div>
                    <div id="titulo-subnav">Impressão da 2ª Via</div>
                    <div class="divisoria"></div>
                    <table class="fmt" align="left">
                      <tr>
                        <td align="center">
                            <br>
                            <input type="button" id="atualizar" name="atualizar" onclick="submete('imprimir')"  class="botaoatualizar" value=" " />
                        </td>
                        <td align="center">
                            <br>
                            <input type="button" id="cancelar" style="margin-top:0px;margin-left: 20px;" name="cancelar" onclick="submete('cancelar')"  class="botaocancelar" value=" " />
                        </td>
                      </tr>
                      
                    </table>  
                </td>
              </tr>
            <table>
        </div>        
        
    </form>
    
</body>
</html>
