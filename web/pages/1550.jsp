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
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
                $("#dataEmissao").mask("99/99/9999");
                $("#dataValidade").mask("99/99/9999");
        });        
        
        function validarForm(){
            if(document.forms[0].numAutorizacao.value == ''
                && document.forms[0].dataEmissao.value == ''
                && document.forms[0].dataValidade.value == ''
                && document.forms[0].responsavel.value == ''
                && document.forms[0].convidado.value == ''){
                alert('É preciso informar ao menos 1 parâmetro de consulta');
                return false;
            }

            if(!isDataValida(document.forms[0].dataEmissao.value)){
                return false;
            }
            if(!isDataValida(document.forms[0].dataValidade.value)){
                return false;
            }                        

            document.forms[0].submit();
        }
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Autorização de Embarque</div>
    <div class="divisoria"></div>

    <br>
    
    
    <form action="c" onsubmit="return validarForm()">
        <input type="hidden" name="app" value="1550">
        <input type="hidden" name="acao" value="consultar">

        <table class="fmt" align="left" >
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Num Autorização:</p>
                    <input type="text" name="numAutorizacao" id="numAutorizacao" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraData" value="${numAutorizacao}"/>
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Data Emissão:</p>
                    <input type="text" name="dataEmissao" id="dataEmissao" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value="${dataEmissao}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0" >Data Validade:</p>
                    <input type="text" name="dataValidade" id="dataValidade" maxlength="10" class="campoSemTamanho alturaPadrao larguraData" value="${dataValidade}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Nome Responsável:</p>
                    <input type="text" name="responsavel" id="responsavel" class="campoSemTamanho alturaPadrao" style="width:200px" value="${responsavel}">
                </td>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Nome Convidado:</p>
                    <input type="text" name="convidado" id="convidado" class="campoSemTamanho alturaPadrao" style="width:200px" value="${convidado}">
                </td>
                <td >    
                    <input type="submit" class="botaobuscainclusao" style="margin-top:20px" onclick="return validarForm()" value="" title="Consultar">
                </td>
                <td> &nbsp;&nbsp;&nbsp;
                    <c:if test='<%=request.isUserInRole("1551")%>'>
                        <a href="c?app=1551&acao=showForm"><img src="imagens/btn-incluir.png" style="margin-top:25px" width="100" height="25" /></a><br>
                    </c:if>
                </td>
                
            </tr>
        </table>
        <br><br>

    </form>

    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" class="nome-lista"></th>
            <th scope="col" class="nome-lista">Responsável</th>
            <th scope="col" class="nome-lista">Categoria</th>
            <th scope="col" class="nome-lista">Convidado</th>
            <th scope="col" class="nome-lista">Dt. Nasc.</th>
            <th scope="col" class="nome-lista">CPF</th>
            <th scope="col" class="nome-lista">Doc. Estran.</th>
            <th scope="col" align="center">Emissão</th>
            <th scope="col" align="center">Validade</th>
            <th scope="col" align="center">Nr. Autorização</th>
            <th scope="col" align="center">Dt. Utilização</th>
            <th scope="col" class="nome-lista">Embarcação</th>
            <th scope="col" class="nome-lista">Capacidade</th>
            <th scope="col" >Alterar</th>
            <th scope="col" >Excluir</th>
        </tr>	
        </thead>
        <tbody>

        <c:forEach var="a" items="${autorizacoes}">

            <tr height="1">
                <c:choose>
                    <c:when test='${a.situacao == "CA"}'>
                        <td class="column1" align="left"><img src="img/autorizacao-embarque-cancelada.png"></td>
                    </c:when>
                    <c:when test='${a.situacao == "UT"}'>
                        <td class="column1" align="left"><img src="img/autorizacao-embarque-utilizada.png"></td>
                    </c:when>                    
                    <c:otherwise>
                        <td class="column1" align="left"></td>
                    </c:otherwise>
                </c:choose>
                <td class="column1" align="left">${a.responsavel.nome}</td>
                <td class="column1" align="left">${a.responsavel.categoria}</td>
                <td class="column1" align="left">${a.convidado}</td>
                <fmt:formatDate var="dtNascimento" value="${a.dtNascimento}" pattern="dd/MM/yyyy" />                
                <td class="column1" align="center">${dtNascimento}</td>
                <td class="column1" align="left">${a.cpfConvidado}</td>
                <td class="column1" align="left">${a.docEstrangeiro}</td>
                <fmt:formatDate var="dataEmissao" value="${a.dataEmissao}" pattern="dd/MM/yyyy" />                
                <td class="column1" align="center">${dataEmissao}</td>
                <fmt:formatDate var="dataValidade" value="${a.dataValidade}" pattern="dd/MM/yyyy" />                
                <td class="column1" align="center">${dataValidade}</td>                
                <td class="column1" align="center">${a.numAutorizacao}</td>
                <fmt:formatDate var="dataUtilizacao" value="${a.dataUtilizacao}" pattern="dd/MM/yyyy" />                
                <td class="column1" align="center">${dataUtilizacao}</td>                
                <td class="column1" align="left">${a.embarcacao}</td>
                <td class="column1" align="left">${a.capacidade}</td>

                <td class="column1" align="center">
                    <c:if test='<%=request.isUserInRole("1552")%>'>
                        <c:if test='${a.situacao == "NU"}'>
                            <a href="c?app=1552&acao=showForm&idAutorizacaoEmbarque=${a.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                        </c:if>
                    </c:if>
                </td>
                <td class="column1" align="center">
                   <c:if test='<%=request.isUserInRole("1553")%>'>
                        <c:if test='${a.situacao == "NU"}'>
                            <a href="javascript: if(confirm('Confirma a exclusão da autorização selecionada?')) window.location.href='c?app=1553&idAutorizacaoEmbarque=${a.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                        </c:if>
                    </c:if>
                </td>
            </tr>	

        </c:forEach>

        </tbody>
    </table>
        
</body>
</html>
