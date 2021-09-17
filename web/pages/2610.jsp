<%@include file="head.jsp"%>

<script type="text/javascript" language="JavaScript">

    function validarForm(){
        
        if(document.forms[0].codigo.value == ''){
            alert('O campo código é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].descricao.value == ''){
            alert('O campo descrição é de preenchimento obrigatório!');
            return;
        }
        if(document.forms[0].quantidade.value == ''){
            alert('O campo quantidade é de preenchimento obrigatório!');
            return;
        }

        document.forms[0].submit();
    }

</script>
<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "2611"}'>
        <div id="titulo-subnav">Inclusão de Item da Churrasqueira</div>
    </c:if>
    <c:if test='${app == "2612"}'>
        <div id="titulo-subnav">Alteração de Item da Churrasqueira</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="idDependencia" value="${idDependencia}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="id" value="${item.id}"/>

        <table> 
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Código :</p>
                    <input type="text" name="codigo" style="width:50px;"  class="campoSemTamanho alturaPadrao" value="${item.codigo}" />
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Descrição:</p>
                    <input type="text" name="descricao" style="width:272px;"  class="campoSemTamanho alturaPadrao" value="${item.descricao}" />
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Quant.:</p>
                    <input type="text" name="quantidade" style="width:50px;"  class="campoSemTamanho alturaPadrao" value="${item.quantidade}" />
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Observação:</p>
                    <input type="text" name="observacao" style="width:272px;"  class="campoSemTamanho alturaPadrao" value="${item.observacao}" />
                </td>
            </tr>
        </table>

        <input type="button" class="botaoatualizar"  onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2610&idDependencia=${idDependencia}';" value=" " />

    </form>
</body>
</html>

