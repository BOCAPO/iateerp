<%@page import="java.math.BigDecimal"%>
<%@page import="techsoft.operacoes.ResultadoBaixaArquivoConvenioBB"%>
<%@include file="head.jsp"%>

<body class="internas">
            
    <%@include file="menu.jsp"%>

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

    <div class="divisoria"></div>
    <div id="titulo-subnav">Baixa DCO</div>
    <div class="divisoria"></div>

    <br>
    
    <table id="tabela">
        <thead>
            <tr class="odd">
                <th scope="col" class="nome-lista" >Descrição</th>
                <th scope="col">Quantidade</th>
                <th scope="col">Valor (R$)</th>
            </tr>
        </thead>
        
        <tr>
            <td>Acatados</td>
            <td align="right">${result.totalAcatado}</td>
            <td align="right"><fmt:formatNumber value="${result.valorAcatado}" pattern="#,##0.00"/></td>
        </tr>
        <tr>
            <td>Rejeitados</td>
            <td align="right">${result.totalRejeitado}</td>
            <td align="right"><fmt:formatNumber value="${result.valorRejeitado}" pattern="#,##0.00"/></td>
        </tr>
        <tr>
            <td><b>Total</b></td>
            <td align="right"><b>${result.totalAcatado + result.totalRejeitado}</b></td>
            <td align="right">
                <%
                    ResultadoBaixaArquivoConvenioBB result = (ResultadoBaixaArquivoConvenioBB)request.getAttribute("result");
                    BigDecimal total = result.getValorAcatado().setScale(2);
                    total = total.add(result.getValorRejeitado().setScale(2));
                %>
                <b><fmt:formatNumber value="<%=total.toPlainString()%>" pattern="#,##0.00"/></b>
            </td>
        </tr>                
    </table>

    <input type="button" class="botaoVoltar"  onclick="window.location='/iate/upload?app=1710';" value=" " />
    
</body>
</html>
