<%@page import="com.sun.imageio.plugins.common.BogusColorSpace"%>
<%@page import="java.math.BigDecimal"%>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%@page import="techsoft.db.Pool"%>
<%@page import="java.sql.*"%>

<fmt:setLocale value="pt_BR"/>
<html>
    <!-- ${pageContext.request.servletPath} -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
        <link rel="shortcut icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
        <title>Iate Clube de Brasília</title>
    </head>

    <body class="internas">

        <style type="text/css">
            .tituloRelatorio {
                font-family: "Arial";
                font-size: 16px;
                font-style: italic;
                font-weight: bold;
                color: #000;
            }

            .subTituloRelatorio {
                font-family: "Arial";
                font-size: 12px;
                font-style: italic;
                color: #000;
            }

            .dados {
                font-family: "Arial";
                font-size: 10px;
                color: #000;
            }

            .tituloDados {
                font-family: "Arial";
                font-size: 12px;
                color: #000;
                font-weight: bold;
            }
            .divisoria { border-bottom:1px #000 solid;  padding-top:1px; margin-bottom:1px; }        

        </style>

        <%

        //String titulo = request.getAttribute("titulo").toString();
        //String titulo2 = request.getAttribute("titulo2").toString();
            String sql = request.getAttribute("sql").toString();
            boolean quebra1 = Boolean.parseBoolean(request.getAttribute("quebra1").toString());
            boolean quebra2 = Boolean.parseBoolean(request.getAttribute("quebra2").toString());
        //quebra1=false;
        //quebra2=false;
            int total1 = Integer.parseInt(request.getAttribute("total1").toString());
            int total2 = Integer.parseInt(request.getAttribute("total2").toString());
            int total3 = Integer.parseInt(request.getAttribute("total3").toString());
            int total4 = Integer.parseInt(request.getAttribute("total4").toString());
            
            String extratoSocio = "";
            extratoSocio = (String)request.getAttribute("extratoSocio");
            
            if (extratoSocio==null){
                extratoSocio = "";
            }
            
            int total5 = 0;
            try {
                total5 = Integer.parseInt(request.getAttribute("total5").toString());
            } catch (Exception e) {
            }

            int total6 = 0;
            try {
                total6 = Integer.parseInt(request.getAttribute("total6").toString());
            } catch (Exception e) {
            }

            SimpleDateFormat fmtDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat fmtHour = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat fmtTimestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            DecimalFormat fmtMoney = new DecimalFormat("#,##0.00");
            DecimalFormat fmtInt = new DecimalFormat("#,###");

            Connection cn = Pool.getInstance().getConnection();
        //out.print(sql);
        //ResultSet rs = cn.createStatement().executeQuery("select 1");
            ResultSet rs = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(sql);
            ResultSetMetaData m = rs.getMetaData();
            int colCount = m.getColumnCount();
            int qtLin = 0;
            try {
                rs.last();
                qtLin = rs.getRow();
                rs.beforeFirst();
            } catch (Exception e) {
            }

        %>

        <form method="POST">
            <input type="hidden" name="qtLin" value="<%=qtLin%>"/>
        </form>

        <script>
            if (!confirm("Foram encontrados " + document.forms[0].qtLin.value + " registros.\nDeseja continuar com a emissão?")) {
                history.back();
            }
        </script>

        <table width="100%" >
            <thead> 
                <tr>
                    <th colspan="<%=m.getColumnCount()%>">
            <table width="100%" >
                <tr>
                    <th align="left" width="150px" rowspan="2" ><img src="imagens/logo-intro.png" width="70" height="55"/></th>
                    <th align="center" height="0" class="tituloRelatorio">${titulo}</th>
                    <th width="150px" class="dados" align="right"><%= fmtHour.format(new Date())%></th>
                </tr>
                <tr>
                    <th align="center" class="subTituloRelatorio">${titulo2}</th>
                    <th class="dados" align="right"><%= fmtDate.format(new Date())%></th>
                </tr>
            </table>
        </th>
    </tr>  
    <tr><th colspan="<%=m.getColumnCount()%>"><div class="divisoria"></div><div class="divisoria"></div></th></tr>
        <%

            if (quebra1) {
                out.print("<tr>\n");
                out.print("<th class=\"tituloDados\" align=\"left\" colspan=\"" + m.getColumnCount() + "\">");
                out.print(m.getColumnName(1));
                out.print("</th>\n");
                out.print("</tr>\n");
            }

            if (quebra2) {
                out.print("<tr>\n");
                out.print("<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>\n");
                out.print("<th class=\"tituloDados\" align=\"left\" colspan=\"" + (m.getColumnCount() - 1) + "\">");
                out.print(m.getColumnName(2));
                out.print("</th>\n");
                out.print("</tr>\n");
            }

            int inicioContador = 1;
            out.print("<tr>\n");
            if (quebra1) {
                out.print("<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>\n");
                inicioContador = 2;
            }
            if (quebra2) {
                out.print("<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>\n");
                inicioContador = 3;
            }
            for (int i = inicioContador; i <= colCount; i++) {
                if (m.getColumnType(i) == Types.REAL || m.getColumnType(i) == Types.DOUBLE || m.getColumnType(i) == Types.FLOAT || m.getColumnType(i) == Types.DECIMAL) {
                    out.print("<td class=\"tituloDados\" align=\"right\" >");
                } else if (m.getColumnType(i) == Types.DATE || m.getColumnType(i) == Types.TIME || m.getColumnType(i) == Types.TIMESTAMP) {
                    out.print("<td class=\"tituloDados\" align=\"center\">");
                } else {
                    out.print("<td class=\"tituloDados\" align=\"left\">");
                }
                out.print(m.getColumnName(i));
                out.print("</th>\n");
            }
            out.print("</tr>\n");

        %>
<tr><th colspan="<%=m.getColumnCount()%>"><div class="divisoria"></div><div class="divisoria"></div></th></tr>
</thead>

<%
    //imprime os dados do relatorio
    BigDecimal acumTotal1 = BigDecimal.ZERO;
    BigDecimal acumTotal2 = BigDecimal.ZERO;
    BigDecimal acumTotal3 = BigDecimal.ZERO;
    BigDecimal acumTotal4 = BigDecimal.ZERO;
    BigDecimal acumTotal5 = BigDecimal.ZERO;
    BigDecimal acumTotal6 = BigDecimal.ZERO;
    BigDecimal quebra1Total1 = BigDecimal.ZERO;
    BigDecimal quebra1Total2 = BigDecimal.ZERO;
    BigDecimal quebra1Total3 = BigDecimal.ZERO;
    BigDecimal quebra1Total4 = BigDecimal.ZERO;
    BigDecimal quebra1Total5 = BigDecimal.ZERO;
    BigDecimal quebra1Total6 = BigDecimal.ZERO;
    BigDecimal quebra2Total1 = BigDecimal.ZERO;
    BigDecimal quebra2Total2 = BigDecimal.ZERO;
    BigDecimal quebra2Total3 = BigDecimal.ZERO;
    BigDecimal quebra2Total4 = BigDecimal.ZERO;
    BigDecimal quebra2Total5 = BigDecimal.ZERO;
    BigDecimal quebra2Total6 = BigDecimal.ZERO;
    String textoQuebra1 = "";
    String textoQuebra2 = "";
    String lastQuebra1 = "";
    String lastQuebra2 = "";
    while (rs.next()) {

        if (quebra1) {
            textoQuebra1 = (rs.getString(1) == null) ? "" : rs.getString(1);
            if (!lastQuebra1.equals(textoQuebra1)) {
                lastQuebra1 = textoQuebra1;
                lastQuebra2 = "";//forca reimprimir cabecalho da quebra2
                out.print("<tr>\n");
                out.print("<td class=\"tituloDados\" align=\"left\" colspan=\"" + m.getColumnCount() + "\">");
                out.print("<b>" + lastQuebra1 + "</b>");
                out.print("</td>\n");
                out.print("</tr>\n");
            }
        }

        if (quebra2) {
            textoQuebra2 = (rs.getString(2) == null) ? "" : rs.getString(2);
            if (!lastQuebra2.equals(textoQuebra2)) {
                lastQuebra2 = textoQuebra2;
                        
                out.print("<tr>\n");
                out.print("<td></td>\n");
                out.print("<td class=\"tituloDados\" align=\"left\" colspan=\"" + (m.getColumnCount() - 1) + "\">");
                out.print("<b>" + lastQuebra2 + "</b>");
                out.print("</td>\n");
                out.print("</tr>\n");
            }
        }

        out.print("<tr>\n");

        //identacao
        if (quebra1) {
            out.print("<td></td>\n");
        }
        if (quebra2) {
            out.print("<td></td>\n");
        }

        for (int i = inicioContador; i <= colCount; i++) {

            if (m.getColumnType(i) == Types.REAL || m.getColumnType(i) == Types.DOUBLE || m.getColumnType(i) == Types.FLOAT || m.getColumnType(i) == Types.DECIMAL) {
                out.print("<td class=\"dados\" align=\"right\" >");
            } else if (m.getColumnType(i) == Types.DATE || m.getColumnType(i) == Types.TIME || m.getColumnType(i) == Types.TIMESTAMP) {
                out.print("<td class=\"dados\" align=\"center\">");
            } else {
                out.print("<td class=\"dados\" align=\"left\">");
            }

            switch (m.getColumnType(i)) {
                case Types.TIME:
                    if (rs.getDate(i) != null) {
                        out.print(fmtHour.format(rs.getDate(i)));
                    }
                    break;
                case Types.DATE:
                case Types.TIMESTAMP:
                    if (rs.getDate(i) != null) {
                        if (m.getColumnName(i).length() >= 9) {
                            if (m.getColumnName(i).substring(0, 9).toUpperCase().equals("DATA/HORA")) {
                                out.print(fmtTimestamp.format(rs.getTimestamp(i)));
                            } else {
                                out.print(fmtDate.format(rs.getDate(i)));
                            }
                        } else {
                            out.print(fmtDate.format(rs.getDate(i)));
                        }
                    }
                    break;
                case Types.INTEGER:
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.DECIMAL:
                case Types.REAL:
                    //Eh assim mesmo, pra testar se eh nulo tem que ser como String, depois o tratamento é feito como Double.
                    if (rs.getString(i) != null) {
                        if (total1 == i) {
                            acumTotal1 = acumTotal1.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra1Total1 = quebra1Total1.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra2Total1 = quebra2Total1.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        }
                        if (total2 == i) {
                            acumTotal2 = acumTotal2.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra1Total2 = quebra1Total2.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra2Total2 = quebra2Total2.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        }
                        if (total3 == i) {
                            acumTotal3 = acumTotal3.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra1Total3 = quebra1Total3.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra2Total3 = quebra2Total3.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        }
                        if (total4 == i) {
                            acumTotal4 = acumTotal4.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra1Total4 = quebra1Total4.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra2Total4 = quebra2Total4.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        }
                        if (total5 == i) {
                            acumTotal5 = acumTotal5.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra1Total5 = quebra1Total5.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra2Total5 = quebra2Total5.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        }
                        if (total6 == i) {
                            acumTotal6 = acumTotal6.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra1Total6 = quebra1Total6.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                            quebra2Total6 = quebra2Total6.add(rs.getBigDecimal(i).setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        }
                        if (m.getColumnType(i) == Types.INTEGER) {
                            out.print(fmtInt.format(rs.getBigDecimal(i)));
                        } else {
                            out.print(fmtMoney.format(rs.getBigDecimal(i)));
                        }
                    }
                    break;
                default:
                    if (rs.getString(i) != null) {
                        out.print(rs.getString(i));
                    }
            }
            out.print("</td>\n");
        }
        out.print("</tr>\n");
    }

    //tratamento especifico para o Extrato de Sócio
    if (extratoSocio.equals("SIM") && acumTotal1.compareTo(BigDecimal.ZERO) == 0){
        out.print("<tr>\n<td class=\"tituloDados\" colspan=4 align=center><br>************************************************* NÃO CONSTAM DÉBITOS *************************************************</td></tr>");
    }
    
    //imprime os totais do relatorio
    if (total1 > 0 || total2 > 0 || total3 > 0 || total4 > 0 || total5 > 0 || total6 > 0) {
        out.print("<tr></tr>\n");
        out.print("<tr>\n");
        if (quebra1 || quebra2) {
            out.print("<td colspan=\"" + (inicioContador - 1) + "\">\n");
        }
        out.print("</td >\n");
        for (int i = inicioContador; i <= colCount; i++) {
            if (m.getColumnType(i) == Types.INTEGER) {
                out.print("<td class=\"tituloDados\">\n");
            } else {
                out.print("<td class=\"tituloDados\" align=\"right\">\n");
            }
            if (total1 == i) {
                if (m.getColumnType(i) == Types.INTEGER) {
                    out.print(fmtInt.format(acumTotal1));
                } else {
                    out.print(fmtMoney.format(acumTotal1));
                }
            } else if (total2 == i) {
                if (m.getColumnType(i) == Types.INTEGER) {
                    out.print(fmtInt.format(acumTotal2));
                } else {
                    out.print(fmtMoney.format(acumTotal2));
                }
            } else if (total3 == i) {
                if (m.getColumnType(i) == Types.INTEGER) {
                    out.print(fmtInt.format(acumTotal3));
                } else {
                    out.print(fmtMoney.format(acumTotal3));
                }
            } else if (total4 == i) {
                if (m.getColumnType(i) == Types.INTEGER) {
                    out.print(fmtInt.format(acumTotal4));
                } else {
                    out.print(fmtMoney.format(acumTotal4));
                }
            } else if (total5 == i) {
                if (m.getColumnType(i) == Types.INTEGER) {
                    out.print(fmtInt.format(acumTotal5));
                } else {
                    out.print(fmtMoney.format(acumTotal5));
                }
            } else if (total6 == i) {
                if (m.getColumnType(i) == Types.INTEGER) {
                    out.print(fmtInt.format(acumTotal6));
                } else {
                    out.print(fmtMoney.format(acumTotal6));
                }
            }
            out.print("</td>\n");
        }
        out.print("</tr>\n");
    }
%>

</table>


<%
    cn.close();
    
%>

</body>
</html>

