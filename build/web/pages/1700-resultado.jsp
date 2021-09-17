<%@page import="java.math.BigDecimal"%>
<%@page import="techsoft.operacoes.ResultadoBaixaArquivoConvenioBB"%>
<%@include file="head.jsp"%>

<body class="internas">
    <style>
        .legendaArquivo{ 
            font-family:Arial, Helvetica, sans-serif; 
            font-size:14px;  
            font-weight:bold; 
            color:#009; 
            margin-left:15px;
            padding-top: 15px;
        }        
    </style>    
    
    
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
    <div id="titulo-subnav">Baixa Arquivo</div>
    <div class="divisoria"></div>

    <br>

    <b><span class="legendaArquivo MargemSuperior0" >Qt. carnês arquivo: ${result.totalRegistros}</span></b>
    
    <table id="tabela" >
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
            <td align="right"><fmt:formatNumber value="${result.valorAcatado.toPlainString()}" pattern="#,##0.00"/></td>
        </tr>
        <tr>
            <td>Rejeitados</td>
            <td align="right">${result.totalRejeitado}</td>
            <td align="right"><fmt:formatNumber value="${result.valorRejeitado.toPlainString()}" pattern="#,##0.00"/></td>
        </tr>
        <tr>
            <td>Já pagos anteriormente</td>
            <td align="right">${result.totalPgAnt}</td>
            <td align="right"><fmt:formatNumber value="${result.valorPgAnt.toPlainString()}" pattern="#,##0.00"/></td>
        </tr>        
        <tr>
            <td>Pagos a mais</td>
            <td align="right">${result.totalAMais}</td>
            <td align="right"><fmt:formatNumber value="${result.valorAMais.toPlainString()}" pattern="#,##0.00"/></td>
        </tr>
        <tr>
            <td>Boletos Avulso</td>
            <td align="right">${result.totalAvulso}</td>
            <td align="right" align="right"><fmt:formatNumber value="${result.valorAvulso.toPlainString()}" pattern="#,##0.00"/></td>
        </tr>        
        <tr>
            <td><b>TOTAL</b></td>
            <td align="right"><b>${result.totalAcatado + result.totalRejeitado + result.totalPgAnt + result.totalAvulso}</b></td>
            <td align="right">
                <%
                    ResultadoBaixaArquivoConvenioBB result = (ResultadoBaixaArquivoConvenioBB)request.getAttribute("result");
                    BigDecimal total = result.getValorAcatado();
                    total = total.add(result.getValorRejeitado());
                    total = total.add(result.getValorAMais());
                    total = total.add(result.getValorAvulso());
                %>                
               <b> <fmt:formatNumber value="<%=total.toPlainString()%>" pattern="#,##0.00"/> </b>
            </td>
        </tr>                
    </table>

    <input type="button" class="botaoVoltar"  onclick="window.location='/iate/upload?app=1700';" value=" " />

    
</body>
</html>
