<%@include file="head.jsp"%>

<body class="internas">

    <%@include file="menuCaixa.jsp"%>

    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
        });        
        function Estorna(dtSitCaixa, cdCaixa, seqAutenticacao, seqAbertura, pagante, deTransacao, valor){
            
            var msg = 'Confirma o Estorno do Movimento: \n\n Pagante: ' + pagante + '\n Autenticação: ' + seqAutenticacao + '\n Transação: ' + deTransacao + '\n Valor: ' + valor;
            
            if (confirm(msg)){
                $('#dtSitCaixa').val(dtSitCaixa);
                $('#cdCaixa').val(cdCaixa);
                $('#seqAutenticacao').val(seqAutenticacao);
                $('#seqAbertura').val(seqAbertura);
                $('#pagante').val(pagante);
                $('#deTransacao').val(deTransacao);
                $('#valor').val(valor);

                document.forms[0].submit();
            }
            
        }
    </script>  


    <div class="divisoria"></div>
    <div id="titulo-subnav">Estorno</div>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" value="6070"/>
        <input type="hidden" name="acao" value="grava"/>
        <input type="hidden" name="pagante" id="pagante" value=""/>
        <input type="hidden" name="deTransacao" id="deTransacao" value=""/>
        <input type="hidden" name="seqAutenticacao" id="seqAutenticacao" value=""/>
        <input type="hidden" name="cdCaixa" id="cdCaixa" value=""/>
        <input type="hidden" name="seqAbertura" id="seqAbertura" value=""/>
        <input type="hidden" name="dtSitCaixa" id="dtSitCaixa" value=""/>
        <input type="hidden" name="valor" id="valor" value=""/>
        
        <table id="tabela">
           <thead>
            <tr class="odd">
                <th>Nome Pagante</th>
                <th>Sq. Autenticação</th>
                <th>Valor</th>
                <th>Estornar</th>
            </tr>
           </thead>
            <c:forEach var="item" items="${movimentos}">
                <tr  height="1">
                    <td class="column1" align="left">${item.pagante}</td>
                    <fmt:formatNumber var="seqAutenticacao"  value="${item.seqAutenticacao}" pattern="000"/>
                    <td class="column1" align="center">${seqAutenticacao}</td>
                    <fmt:formatNumber var="valor"  value="${item.valor}" pattern="#,##0.00"/>
                    <td class="column1" align="right">${valor}</td>
                    <td class="column1" align="center">
                        <fmt:formatDate var="dtSitCaixa" value="${item.dtSitCaixa}" pattern="dd/MM/yyyy HH:mm:ss"/>
                        <a  href="javascript:Estorna('${dtSitCaixa}', ${item.cdCaixa}, '${seqAutenticacao}', ${item.seqAbertura}, '${item.pagante}', '${item.deTransacao}', '${valor}')"><img src="imagens/icones/inclusao-titular-05.png"/></a>
                    </td>

                </tr>
            </c:forEach>
        </table>
    </form>            
</body>
</html>
