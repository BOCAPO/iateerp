<%@page import="techsoft.util.Datas"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.io.*"%>

<%@include file="head.jsp"%>

<body class="internas">
    
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
    <div id="titulo-subnav">Visualização do Arquivo de Retorno</div>
    <div class="divisoria"></div>
    
    <table id="tabela">
        <thead>
        
        <tr class="odd">
            <th>Seq. Carne</th>
            <th>Dt. Pagamento</th>
            <th>Tot. Devido</th>
            <th>Tot. Pago</th>
            <th>Tot. Encargos</th>
        </tr>        

        <%
            File f = new File(request.getAttribute("arquivobb").toString());
            BufferedReader buf = new BufferedReader(new FileReader(f));
            String s = buf.readLine();
            if(!s.startsWith("0")){
                out.print("<br><h1 class=\"msgErro\">Arquivo sem header, contate o Banco</h1><br>");
            }else{

                BigDecimal totDevido = BigDecimal.ZERO;
                BigDecimal totPago = BigDecimal.ZERO;
                BigDecimal totEncTotal = BigDecimal.ZERO;
                while((s = buf.readLine()) != null){
                    if(s.startsWith("1")){                
                        String tmp1 = s.substring(45, 62).trim();//Int
                        if(tmp1.equals("")){
                            tmp1 = s.substring(69, 74);
                        }                    
                        String tmp2 = s.substring(110, 112) + "/" + s.substring(112, 114) + "/20" + s.substring(114, 116);//Date
                        String tmp3 = s.substring(152, 163) + "." + s.substring(163, 165);//R$

                        totDevido = totDevido.add(new BigDecimal(tmp3));

                        String tmp4 = s.substring(253, 264) + "." + s.substring(264, 266);//R$
                        totPago = totPago.add(new BigDecimal(tmp4));

                        BigDecimal totEnc = new BigDecimal(s.substring(266, 277) + "." + s.substring(277, 279));
                        BigDecimal totEncTemp = new BigDecimal(s.substring(279, 290) + "." + s.substring(290, 292));                
                        totEnc = totEnc.add(totEncTemp);//R$

                        totEncTotal = totEncTotal.add(totEnc);
                        %>
                        <tr>
                            <td class="column1" align="center"><%=String.valueOf(Long.parseLong(tmp1))%></td>
                            <td class="column1" align="center">
                                <fmt:formatDate var="dataPagamento" value="<%=Datas.parse(tmp2)%>" pattern="dd/MM/yyyy"/>
                                ${dataPagamento}
                            </td>
                            <td class="column1" align="right"><fmt:formatNumber value="<%=new BigDecimal(tmp3).toPlainString()%>" pattern="#,##0.00"/></td>
                            <td class="column1" align="right"><fmt:formatNumber value="<%=new BigDecimal(tmp4).toPlainString()%>" pattern="#,##0.00"/></td>
                            <td class="column1" align="right"><fmt:formatNumber value="<%=totEnc.toPlainString()%>" pattern="#,##0.00"/></td>
                        </tr>                        
                        <%
                    }
                }//while
                %>
                <tr>
                    <td class="column1" align="center" colspan="2"><b>TOTAL</b></td>
                    <td class="column1" align="right"><b><fmt:formatNumber value="<%=totDevido.toPlainString()%>" pattern="#,##0.00"/></b></td>
                    <td class="column1" align="right"><b><fmt:formatNumber value="<%=totPago.toPlainString()%>" pattern="#,##0.00"/></b></td>
                    <td class="column1" align="right"><b><fmt:formatNumber value="<%=totEncTotal.toPlainString()%>" pattern="#,##0.00"/></b></td>
                </tr>            
                <%
            }//else
            buf.close();
            f.delete();
        %>



    </table>
        
    <input type="button" class="botaoVoltar"  onclick="window.location='upload?app=1940';" value=" " />


</body>
</html>
