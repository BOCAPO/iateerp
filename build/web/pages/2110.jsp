<%@include file="head.jsp"%>

<style>
    table {border:none;width:344px;margin-left:0px;padding:0px;}
    tr {border:none;background:none;padding:0px;}
    td {border:none;background:none;padding:0px;}
</style>        



<script type="text/javascript" language="JavaScript">

    function validarForm(){

        if(document.forms[0].nome.value == ''){
            alert('O nome é de preenchimento obrigatório!');
            return false;
        }
        if(document.forms[0].clube.value == ''){
            alert('O clube é de preenchimento obrigatório!');
            return false;
        }
        if(document.forms[0].uf.value == ''){
            alert('A UF é de preenchimento obrigatório!');
            return false;
        }else{
            var ufs = ['AC','AL','AM','AP','BA','CE','DF','ES','GO',
            'MA','MG','MS','MT','PA','PB','PE','PI',
            'PR','RJ','RN','RO','RR','RS','SC','SE',
            'SP','TO']
            if (ufs.indexOf(document.forms[0].uf.value.toUpperCase())<0){
                alert('UF inválida!');
                return false;
            }            
        }
        if(document.forms[0].numeracao.value == ''){
            alert('A numeração é de preenchimento obrigatório!');
            return false;
        }
        if(document.forms[0].categoria.value == ''){
            alert('A categoria é de preenchimento obrigatório!');
            return false;
        }

        document.forms[0].submit();
    }

</script>

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <c:if test='${app == "2111"}'>
        <div id="titulo-subnav">Inclusão de Cartão Náutico</div>
    </c:if>
    <c:if test='${app == "2112"}'>
        <div id="titulo-subnav">Alteração de Cartão Náutico</div>
    </c:if>
    <div class="divisoria"></div>

    <form action="c" id="it-bloco01">

        <input type="hidden" name="app" value="${app}"/>
        <input type="hidden" name="acao" value="gravar"/>   
        <input type="hidden" name="nrCartao" value="${cartao.nrCartao}"/>
        <input type="hidden" name="idEvento" value="${idEvento}"/>

        <table align="left">
          <tr>
            <td colspan="2">
                <p class="legendaCodigo ">Nome</p>
                <input type="text" name="nome" maxlength="40" class="campoDescricao " style="width:250px;" value="${cartao.nome}" />
            </td>
            <td >
                <p class="legendaCodigo ">Clube</p>
                <input type="text" name="clube" maxlength="30" class="campoDescricao " style="width:150px;" value="${cartao.clube}" />
            </td>
          </tr>
          <tr>
            <td >
                <p class="legendaCodigo ">UF:</p>
                <input type="text" name="uf" maxlength="2" class="campoSemTamanho alturaPadrao larguraData " style="width:50px;" value="${cartao.uf}" />
            </td>
            <td >
                <p class="legendaCodigo ">Numeração</p>
                <input type="text" name="numeracao" maxlength="30" class="campoSemTamanho alturaPadrao larguraData " style="width:184px;" value="${cartao.numeracao}" />
            </td>
            <td >
                <p class="legendaCodigo " >Categoria:</p>
                <input type="text" name="categoria" maxlength="30" class="campoSemTamanho alturaPadrao larguraData " style="width:150px;" value="${cartao.categoria}" />
            </td>
          </tr>
          
        </table>        
        
        <br><br><br><br><br><br><br><br><br>

        <input type="button" class="botaoatualizar"   onclick="validarForm()" value=" " />
        <input type="button" class="botaoVoltar "  onclick="window.location='c?app=2110&idEvento=${idEvento}';" value=" " />


    </form>
</body>
</html>


