<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:15px;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">

    function validarForm(){
        var nome = trim(document.forms[0].nome.value);

        if(nome != '' && nome.length < 3){
            alert('Nome para pesquisa deve ter no mínimo 3 letras.');
            return;
        }

        document.forms[0].submit();
    }

</script>

<script type="text/javascript" language="javascript">
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
    });        
</script>  

<body class="internas">
            
    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Cadastro de Armários</div>
    <div class="divisoria"></div>   

    <form action="c" method="POST">
        <input type="hidden" name="app" value="1323">
        <input type="hidden" name="acao" value="consultar">
        <input type="hidden" name="numeroArmario" value="${numeroArmario}">
        <input type="hidden" name="tipoArmario" value="${tipoArmario}">

        <table class="fmt" align="left">
          <tr>
            <td class="column1">
                <p class="legendaCodigo">Nome</p>
                <input type="text" name="nome" maxlength="40" class="campoSemTamanho alturaPadrao larguraNomePessoa" value="${nome}" />
            </td>
            <td class="column1">    
                <input class="botaobuscainclusao MargemSuperiorBotaoProcurar" type="button" onclick="validarForm()" value="" title="Consultar" />
            </td>
          </tr>

        </table>  


    </form>

    <br><br>
            
    <table id="tabela"">
        <thead>
        <tr class="odd">
            <th scope="col" >Nome</th>
            <th scope="col" >Categoria</th>
        </tr>	
        </thead>
        
        <c:forEach var="socio" items="${socios}">
            <c:if test="${socio.seqDependente == 0}">        
            <tr>
                <td class="column1"><a href="c?app=1323&acao=selecionar&matricula=${socio.matricula}&idCategoria=${socio.idCategoria}&numeroArmario=${numeroArmario}&tipoArmario=${tipoArmario}">${socio.nome}</a></td>
                <td class="column1">${socio.categoria}</td>
            </tr>
            </c:if>
        </c:forEach>
    </table>
    
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1320';" value=" " />
    
</body>
</html>
