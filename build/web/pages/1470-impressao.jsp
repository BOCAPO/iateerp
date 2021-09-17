<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
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
	font-size: 12px;
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
String sql = request.getAttribute("sql").toString();
int total1 = 2;
int total2 = 3;
int total3 = 4;
int total4 = 5;
int total5 = 6;
int total6 = 7;
int total7 = 8;
int total8 = 9;
int total9 = 10;

SimpleDateFormat fmtDate = new SimpleDateFormat("dd/MM/yyyy");
SimpleDateFormat fmtHour = new SimpleDateFormat("HH:mm:ss");
DecimalFormat fmtMoney = new DecimalFormat("#,##0.00");

Connection cn = Pool.getInstance().getConnection();
CallableStatement c = cn.prepareCall("{call SP_REL_INADIMPLENCIA(?, ?)}");
Date d1 = (Date)request.getAttribute("dataInicio");
Date d2 = (Date)request.getAttribute("dataFim");
c.setDate(1, new java.sql.Date(d1.getTime()));
c.setDate(2, new java.sql.Date(d2.getTime()));
c.executeUpdate();

//out.print(sql);
ResultSet rs = cn.createStatement().executeQuery(sql);
ResultSetMetaData m = rs.getMetaData();
int colCount = m.getColumnCount();
%>

<table width="100%" >
    <thead>
    <tr>
      <th colspan="<%=m.getColumnCount()%>">
        <table width="100%" >
        <tr>
          <th align="left" width="150px" rowspan="2" ><img src="imagens/logo-intro.png" width="70" height="55"/></th>
          <th align="center" height="0" class="tituloRelatorio">${titulo}</th>
          <th width="150px" class="dados" align="right"><%= fmtHour.format(new Date()) %></th>
        </tr>
        <tr>
          <th align="center" class="subTituloRelatorio">${titulo2}</th>
          <th class="dados" align="right"><%= fmtDate.format(new Date()) %></th>
        </tr>
        </table>
      </th>
    </tr>  
    <tr><th colspan="<%=m.getColumnCount()%>"><div class="divisoria"></div><div class="divisoria"></div></th></tr>

    <tr>
        <td rowspan="2" class="tituloDados" align="left">Categoria</td>
        <td colspan="2" class="tituloDados" align="center">-------- Carnes Gerados --------</td>
        <td colspan="6" class="tituloDados" align="center">--------------------------------------------- Não Pago ---------------------------------------------</td>
        <td class="tituloDados" align="center">Taxa de Inad.</td>
    </tr>
    <tr>
        <td class="tituloDados" align="right">Valor</td>
        <td class="tituloDados" align="right">%</td>
        <td class="tituloDados" align="right">Vr. Principal</td>
        <td class="tituloDados" align="right">%</td>
        <td class="tituloDados" align="right">Encargos</td>
        <td class="tituloDados" align="right">%</td>
        <td class="tituloDados" align="right">Total</td>
        <td class="tituloDados" align="right">%</td>
        <td class="tituloDados" align="right">%</td>
    </tr>

    <tr><th colspan="<%=m.getColumnCount()%>"><div class="divisoria"></div><div class="divisoria"></div></th></tr>
    </thead>
    
    
    
    <%

    //imprime os dados do relatorio
    double acumTotal1 = 0;
    double acumTotal2 = 0;
    double acumTotal3 = 0;
    double acumTotal4 = 0;
    double acumTotal5 = 0;
    double acumTotal6 = 0;
    double acumTotal7 = 0;
    double acumTotal8 = 0;
    double acumTotal9 = 0;
    int inicioContador = 1;
    while(rs.next()){
        
        out.print("<tr>\n");
        
        for(int i = inicioContador; i <= colCount; i++){
            
            if (m.getColumnType(i) == Types.REAL || m.getColumnType(i) == Types.DOUBLE || m.getColumnType(i) == Types.FLOAT ||m.getColumnType(i) == Types.DECIMAL){
                out.print("<td class=\"dados\" align=\"right\" >");
            }else{
                out.print("<td class=\"dados\" align=\"left\">");
            }
            
            switch(m.getColumnType(i)){
                case Types.DOUBLE:
                case Types.FLOAT:
                case Types.DECIMAL:
                case Types.REAL:
                    //Eh assim mesmo, pra testar se eh nulo tem que ser como String, depois o tratamento é feito como Double.
                    if (rs.getString(i) != null){
                        if(total1 == i) acumTotal1 += rs.getDouble(i);
                        if(total2 == i) acumTotal2 += rs.getDouble(i);
                        if(total3 == i) acumTotal3 += rs.getDouble(i);
                        if(total4 == i) acumTotal4 += rs.getDouble(i);
                        if(total5 == i) acumTotal5 += rs.getDouble(i);
                        if(total6 == i) acumTotal6 += rs.getDouble(i);
                        if(total7 == i) acumTotal7 += rs.getDouble(i);
                        if(total8 == i) acumTotal8 += rs.getDouble(i);
                        if(total9 == i) acumTotal9 += rs.getDouble(i);
                        out.print(fmtMoney.format(rs.getDouble(i)));
                    }    
                    break;
                default:
                    if (rs.getString(i) != null){
                        out.print(rs.getString(i));                    
                    }    
            }
            out.print("</td>\n");
        }
        out.print("</tr>\n");
    }
    

    out.print("<tr></tr>\n");
    out.print("<tr>\n");
    out.print("</td >\n");
    for(int i = inicioContador; i <= colCount; i++){
        out.print("<td class=\"tituloDados\" align=\"right\">\n");

        if(total1 == i){
            out.print(fmtMoney.format(acumTotal1));
        }else if(total2 == i){
            out.print(fmtMoney.format(acumTotal2));
        }else if(total3 == i){
            out.print(fmtMoney.format(acumTotal3));
        }else if(total4 == i){
            out.print(fmtMoney.format(acumTotal4));
        }else if(total5 == i){
            out.print(fmtMoney.format(acumTotal5));
        }else if(total6 == i){
            out.print(fmtMoney.format(acumTotal6));
        }else if(total7 == i){
            out.print(fmtMoney.format(acumTotal7));
        }else if(total8 == i){
            out.print(fmtMoney.format(acumTotal8));
        }else if(total9 == i){
            out.print(fmtMoney.format(acumTotal9));
        }
        out.print("</td>\n");
    }
    out.print("</tr>\n");
    
    %>

</table>

                                
<%
cn.close();
%>

</body>
</html>

