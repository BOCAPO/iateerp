<%@include file="head.jsp"%>

    <body class="internas">
        <style type="text/css">
            table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
            table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
        </style>  
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

    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Sócio e Embarcação</div>
    <div class="divisoria"></div>
    
    <table class="fmt">
    <tr>
        <td>
            <p class="legendaCodigo MargemSuperior0" >Sócio:</p>
            <input type="text" class="campoSemTamanho alturaPadrao larguraNomePessoa" disabled name="nome" value="${barco.socio.nome}">
        </td>    
        <td>
            <p class="legendaCodigo MargemSuperior0" >Barco:</p>
            <input type="text" name="nome" class="campoSemTamanho alturaPadrao larguraNomePessoa" disabled value="${barco.nome}">
        <td>
    </tr>
    </table>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Pessoas Autorizadas</div>
    <div class="divisoria"></div>

    <div class="botaoincluirgeral">
        <a href="c?app=2002&acao=showFormIncluirPessoa&idBarco=${barco.id}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
    </div>
    
    <br>                

    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col">Nome</th>
                <th scope="col">OBS</th>
                <th scope="col">Alterar</th>
                <th scope="col">Excluir</th>
            </tr>
        </thead>
            
        <c:forEach var="autorizado" items="${autorizados}">
            <tr>
                <td class="column1">${autorizado.nome}</td>
                <td class="column1">${autorizado.obs}</td>

                <td align="center">    
                    <a href="c?app=2002&acao=showFormAlterarPessoa&idBarco=${barco.id}&idAutorizado=${autorizado.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                </td>
                <td align="center">
                    <a href="javascript: if(confirm('Confirma a exclusão do autorizado?')) window.location.href='c?app=2002&idBarco=${barco.id}&idAutorizado=${autorizado.id}&acao=excluirPessoa'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!--<a href="c?app=2000&acao=showForm&matricula=${barco.socio.matricula}&seqDependente=${barco.socio.seqDependente}&idCategoria=${barco.socio.idCategoria}">VOLTAR PARA LISTA DE BARCOS</a> -->
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=2000&acao=showForm&matricula=${barco.socio.matricula}&idCategoria=${barco.socio.idCategoria}&seqDependente=0';" value=" " />

</body>
</html>
