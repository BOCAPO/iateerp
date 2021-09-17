<%@include file="head.jsp"%>

<style>
    table {border:none;width:300px;padding:0px;margin-left:5em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>        
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(!verifica_branco(document.forms[0].userTesoureiro.value)){
           alert("Informe o Tesoureiro!");
           return false;
        }
        if(!verifica_branco(document.forms[0].SenhaTesoureiro.value)){
           alert("Informe a Senha do Tesoureiro!");
           return false;
        }
        if(!verifica_branco(document.forms[0].userDiretor.value)){
           alert("Informe o Diretor!");
           return false;
        }
        if(!verifica_branco(document.forms[0].SenhaDiretor.value)){
           alert("Informe a Senha do Diretor!");
           return false;
        }
        if(document.forms[0].userTesoureiro.value == document.forms[0].userDiretor.value){
           alert("O Usuário Tesoureiro deve ser diferente do Diretor!");
           return false;
        }

        if(!verifica_branco(document.forms[0].qtBaixas.value)){
           alert("Informe a Quantidade de Baixas!");
           return false;
        }
        if (isNaN(document.forms[0].qtBaixas.value)) {    
            alert("Informe uma quantidade de baixas válida!");    
            return false;    
        } 

        document.forms[0].submit();
    }

</script>


<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
        <div id="titulo-subnav">Baixa Manual de Carne - Autorização</div>
    <div class="divisoria"></div>
    
    
    <form action="c">
        <input type="hidden" name="app" value="1720"/>
        <input type="hidden" name="acao" value="valida"/>
        <input type="hidden" name="msgAutenticado" value="${bmc.msgAutenticado}"/>

        <c:if test='${bmc.msgAutenticado != "OK" && bmc.msgAutenticado != null}'>
            <script type="text/javascript" language="JavaScript">
                alert(document.forms[0].msgAutenticado.value);
            </script>
        </c:if>


        <div id="oculta" style="visibility:hidden;">
            <input type="text" name="GATO" class="campoSemTamanho larguraNumero" />
        </div>    
        <table align="left">
          <tr>
            <td>
                <p class="legendaCodigo">Tesoureiro</p>
                <input type="text" name="userTesoureiro" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value="${bmc.userTesoureiro}"/>
            </td>
            <td>
                <p class="legendaCodigo">Senha</p>
                <input type="password" name="SenhaTesoureiro" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value="${bmc.senhaTesoureiro}"/>
            </td>
          </tr>
          <tr>
            <td>
                <p class="legendaCodigo">Diretor</p>
                <input type="text" name="userDiretor" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value="${bmc.userDiretor}" />
            </td>
            <td>
                <p class="legendaCodigo">Senha</p>
                <input type="password" name="SenhaDiretor" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value="${bmc.senhaDiretor}"/>
            </td>
          </tr>
          <tr>
            <td>
                <p class="legendaCodigo">Qtd. Baixas</p>
                <input type="text" name="qtBaixas" maxlength="2" class="campoSemTamanho alturaPadrao larguraData" value="${bmc.qtBaixa}"/>
            </td>
            <td>
                
            </td>
          </tr>
        
          <tr>
              
            <td>
                <br>
                <div id="botao">
                    <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
                </div>        
            </td>
            <td>
                &nbsp;
            </td>
            
          </tr>
        </table>  

    </form>

</body>
</html>
