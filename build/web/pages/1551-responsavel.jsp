<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    
    <script type="text/javascript" language="JavaScript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
                $('#nomeSocio').focus();
        });        
        
        function validarForm(){
            if(document.forms[0].nomeSocio.value == ''){
                alert('Informe o nome da pessoa ou parte dele para pesquisa!');
                return false;
            }

            document.forms[0].submit();
        }
    </script>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Seleção de Sócio</div>
    <div class="divisoria"></div>

    <br>
    
    <form action="c" onsubmit="return validaForm">
        <input type="hidden" name="app" value="1551">
        <input type="hidden" name="acao" value="consultarSocio">
        <table class="fmt" align="left" >
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Nome do sócio:</p>
                    <input type="text" name="nomeSocio"  id="nomeSocio" class="campoDescricao" value="${nomeSocio}"/>
                </td>
                <td >    
                    <input type="submit" class="botaobuscainclusao" style="margin-top:20px" onclick="return validarForm()" value="" title="Consultar">
                </td>
            </tr>
        </table>
        <br><br>

    </form>

    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" class="nome-lista">Nome</th>
            <th scope="col" class="nome-lista">Categoria</th>
        </tr>	
        </thead>
        <tbody>

        <c:forEach var="s" items="${socios}">

            <tr height="1">
                <td class="column1" align="left"><a href="c?app=1551&acao=selecionarResponsavel&matricula=${s.matricula}&idCategoria=${s.idCategoria}&seqDependente=${s.seqDependente}">${s.nome}</a></td>
                <td class="column1" align="left">${s.categoria}</td>
            </tr>	

        </c:forEach>

        </tbody>
    </table>
                
    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1550';" value=" " />
        
</body>
</html>
