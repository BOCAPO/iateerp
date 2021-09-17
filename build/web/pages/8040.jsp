<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
        }); 
        
        function validarForm(){
            if(document.forms[0].login.value == 'TODOS'
                && document.forms[0].permissao.value == 'TODOS'
                && document.forms[0].dataInicio.value == ''
                && document.forms[0].dataFim.value == ''){
                alert('É preciso informar ao menos 1 parâmetro de consulta');
                return;
            }


            if(!isDataValida(document.forms[0].dataInicio.value)){
                return;
            }
            if(!isDataValida(document.forms[0].dataFim.value)){
                return;
            }                        
            
            if(document.forms[0].dataInicio.value != ''){
                if(document.forms[0].dataFim.value == ''){
                    alert('Se informar Data de Início informe a Data de Fim.');
                    return;
                }
            }else{
                if(document.forms[0].dataFim.value != ''){
                    alert('Se informar Data de Fim informe a Data de Início.');
                    return;
                }                
            }
            
            document.forms[0].submit();
        }
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Consultar Log do Sistema</div>
    <div class="divisoria"></div>

    <br>

    <form action="c">
        <input type="hidden" name="app" value="8040">
        <input type="hidden" name="acao" value="consultar">

        <table class="fmt" align="left" >
            <tr>
                <td >
                    <p class="legendaCodigo MargemSuperior0" >Usuário</p>
                    <select name="login" class="campoSemTamanho alturaPadrao larguraComboTipoDependente" >
                        <option value="TODOS">TODOS</option>
                        <c:forEach var="usuario" items="${usuarios}">
                            <option value="${usuario.login}">${usuario.nome}</option>
                        </c:forEach>
                    </select>
                </td>   
                <td >
                    <p class="legendaCodigo MargemSuperior0" >Aplicação</p>
                    <select name="permissao" class="campoSemTamanho alturaPadrao larguraComboTipoDependente">
                        <option value="TODOS">TODOS</option>
                        <c:forEach var="permissao" items="${permissoes}">
                            <option value="${permissao.id}">${permissao.id} - ${permissao.descricao}</option>
                        </c:forEach>
                    </select>
                </td>   
                <td >
                    <p class="legendaCodigo MargemSuperior0" >Data Início:</p>
                    <input type="text" name="dataInicio" id="dataInicio"  maxlength="10" class="campoSemTamanho alturaPadrao larguraNumero">
                </td>   
                <td >
                    <p class="legendaCodigo MargemSuperior0" >Data Fim:</p>
                    <input type="text" name="dataFim" id="dataFim" maxlength="10" class="campoSemTamanho alturaPadrao larguraNumero">
                </td>   
                <td >
                    <p class="legendaCodigo MargemSuperior0" >Detalhamento:</p>
                    <input type="text" name="deDetalhamento" id="deDetalhamento"  class="campoSemTamanho alturaPadrao" style="width:250px;">
                </td>   
            </tr>
        </table>
        <br>
        <br><br>
        <input type="button" class="botaoatualizar" onclick="validarForm()" value=" " />
    </form>

</body>
</html>
