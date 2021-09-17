
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');
            $('#tabela1 tr:gt(0)').css('background', 'white');
            $('#tabela2 tr:gt(0)').css('background', 'white');

            $('#tabela1 tr').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
            $('#tabela2 tr').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })
    });        
</script>  

<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <%@include file="menuAcesso.jsp"%>
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Mensagens da Pessoa</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="7030">
        <input type="hidden" name="acao" value="consultar">
        <table class="fmt" align="left" >
            <c:forEach var="socio" items="${socios}">
                <c:set var="doc" value="${socio.nrCarteira}" /> 
                <tr>
                    <td>
                        <p class="legendaCodigo MargemSuperior0">Tipo de Documento</p>
                        <div class="selectheightnovo">
                            <select name="tipoDoc" disabled class="campoSemTamanho alturaPadrao" style="width:180px;">
                                <option value="C" <c:if test='${tipoDoc == "C"}'>selected="selected"</c:if>>Carteirinha</option>
                                <option value="P" <c:if test='${tipoDoc == "P"}'>selected="selected"</c:if>>Passaporte</option>
                            </select>
                        </div>        
                    </td>

                    <td>
                        <p class="legendaCodigo MargemSuperior0" >Documento</p>
                        <input type="text" name="numero" disabled class="campoSemTamanho alturaPadrao larguraData" value="${socio.nrCarteira}">
                    </td>
                    <td>
                        <p class="legendaCodigo MargemSuperior0" >Nome</p>
                        <input type="text" name="nome" disabled class="campoSemTamanho alturaPadrao" style="width:300px" value="${socio.nome}">
                    </td>
                </tr>
            </c:forEach>
        </table>
        <br><br>
        
        <table id="tabela" style="width:98%;margin-left:15px;">
            <thead>
                <tr class="odd">
                    <th scope="col" align="center">Tipos de Evento de Acesso</th>
                    <th scope="col" align="center">Mensagens para a Pessoa</th>
                </tr>
            </thead>
            <tbody>
                    <tr>
                        <td class="column1" align="center">
                            <table id="tabela1" class="fmt" style="width:98%;">
                                <c:forEach var="msgNaoAssoc" items="${naoAssoc}">
                                    <tr height="35px">
                                        <td class="column1" align="left">
                                            ${msgNaoAssoc.descricao}
                                        </td>
                                        <td class="column1" align="center">
                                            <a href="c?app=7030&acao=incluir&doc=${doc}&id=${msgNaoAssoc.id}&tipoDoc=${tipoDoc}"><img src="imagens/btn-incluir.png" width="85px" height="25px" /></a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </td>
                        <td class="column1" align="center" valign="top">
                            <table id="tabela1" class="fmt" style="width:98%;">
                                <c:forEach var="msgAssoc" items="${assoc}">
                                    <tr>
                                        <td class="column1" align="left">
                                            ${msgAssoc.descricao}
                                        </td>
                                        <td class="column1" align="center" valign="center">
                                            <a href="c?app=7030&acao=excluir&doc=${doc}&id=${msgAssoc.id}&tipoDoc=${tipoDoc}"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </td>
                    </tr>
                    
            </tbody>
        </table>
        
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=7030';" value=" " />
        
    </form>
        

    
    
</body>
</html>
