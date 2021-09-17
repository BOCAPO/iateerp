<%@include file="head.jsp"%>

<style>
    table {border:none;width:300px;padding:0px;margin-left:5em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>        
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>

<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(!verifica_branco(document.forms[0].userCaixa.value)){
           alert("Informe o Caixa!");
           return false;
        }
        if(!verifica_branco(document.forms[0].senhaCaixa.value)){
           alert("Informe a Senha do Caixa!");
           return false;
        }
        if(!verifica_branco(document.forms[0].userSupervisor.value)){
           alert("Informe o Supervisor!");
           return false;
        }
        if(!verifica_branco(document.forms[0].senhaSupervisor.value)){
           alert("Informe a Senha do Supervisor!");
           return false;
        }
        if(document.forms[0].userCaixa.value == document.forms[0].userSupervisor.value){
           alert("O Usuário Caixa deve ser diferente do Supervisor!");
           return false;
        }
        
        $.ajax({url:'EstornoAjax', async:false, dataType:'text', type:'GET',data:{
                            tipo:3,
                            usuario:$('#userCaixa').val(),
                            senha:$('#senhaCaixa').val(),
                            aplicacao:6020} 
        }).success(function(retorno){
            ret = retorno;
            if (ret!='OK'){
                alert(ret + "(CAIXA)!");
                return false;
            }
        });
        
        if (ret=='OK'){
            $.ajax({url:'EstornoAjax', async:false, dataType:'text', type:'GET',data:{
                                tipo:3,
                                usuario:$('#userSupervisor').val(),
                                senha:$('#senhaSupervisor').val(),
                                aplicacao:6070} 
            }).success(function(retorno){
                ret = retorno;
                if (ret!='OK'){
                    alert(ret + "(SUPERVISOR)!");
                    return false;
                }
            });
            
            
            if (ret=='OK'){
                document.forms[0].submit();
            }
            
        }
    }

</script>


<body class="internas">
            
    <%@include file="menuCaixa.jsp"%>

    <div class="divisoria"></div>
        <div id="titulo-subnav">Estorno de Movimentação - Senha</div>
    <div class="divisoria"></div>
    
    
    <form action="c">
        <input type="hidden" name="app" value="6070"/>
        <input type="hidden" name="acao" value="lista"/>

        <c:if test='${msg == "OK"}'>
            <div id="oculta" style="visibility:hidden;">
                <input type="text" name="GATO" class="campoSemTamanho larguraNumero" />
                <input type="password" name="GATO2" maxlength="10" class="campoSemTamanho alturaPadrao larguraData"/>
            </div>    
            
            <table align="left">
              <tr>
                <td>
                    <p class="legendaCodigo">Caixa</p>
                    <input type="text" name="userCaixa" id="userCaixa" maxlength="12" class="campoSemTamanho alturaPadrao larguraData" readonly value="${caixa}"/>
                </td>
                <td>
                    <p class="legendaCodigo">Senha</p>
                    <input type="password" name="senhaCaixa" id="senhaCaixa" maxlength="6" class="campoSemTamanho alturaPadrao larguraData"/>
                </td>
              </tr>
              <tr>
                <td>
                    <p class="legendaCodigo">Supervisão</p>
                    <input type="text" name="userSupervisor" id="userSupervisor"  maxlength="12" class="campoSemTamanho alturaPadrao larguraData"/>
                </td>
                <td>
                    <p class="legendaCodigo">Senha</p>
                    <input type="password" name="senhaSupervisor" id="senhaSupervisor" maxlength="6" class="campoSemTamanho alturaPadrao larguraData"/>
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
        </c:if>
        
        <c:if test='${msg != "OK"}'>
            <br>
            <h1 class="msgErro">${msg} </h1>                
        </c:if>

    </form>

</body>
</html>
