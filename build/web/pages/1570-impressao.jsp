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

<style type="text/css">
.tituloIate {
	font-family: Georgia, "Times New Roman", Times, serif;
	font-size: 20px;
	font-weight: bold;
	color: #00F;
}

.tituloComprovante{
	font-family: "Times New Roman", Times, serif;
	font-size: 16px;
	font-weight: bold;
}
.textoPreenchimentoObrigatorio{
	font-family: "Times New Roman", Times, serif;
	font-size: 20px;
	font-weight: bold;
}
.subTituloConvite{
	font-family: "Times New Roman", Times, serif;
	font-size: 10px;
	font-weight: bold;
}
.tipo{
	font-family: "Times New Roman", Times, serif;
	font-size: 16px;
	font-weight: bold;
        color:red;
}
.mensagem{
	font-family: "arial";
	font-size: 12px;
	font-weight: bold;
        color:red;
}
.quebra { 
    page-break-after: always; 
} 

.bordaExterna{
        position:relative; 
        border:1px solid black;
}

</style>
</head>
<body>

<%
String lugares = "";
for(String x:request.getParameterValues("selConfirmado")){
    lugares = lugares + x + ",";
}
lugares = lugares.substring(0, lugares.length()-1);


String deEvento="QUINTA CULTURAL - LENKA NOVOTNA E JURAJ HOLANDA";
String dtEvento="01/01/2000";
String hhEvento="21HS";
String tipo="NÃO SÓCIO";
Float valor=0f;
String lugar="001 - C";
String nome="NOME DO CARA";

String sql = "SELECT " 
               + "T2.DE_EVENTO, CONVERT(VARCHAR, T2.DT_EVENTO, 103) DT_EVENTO, T2.HH_EVENTO, T1.IC_TIPO_PESSOA, T1.VR_RESERVA, T1.NO_PESSOA, RIGHT('000'+CONVERT(VARCHAR, T1.NR_MESA), 3) NR_MESA, CHAR(64+T1.NR_CADEIRA) NR_CADEIRA  "
            + "FROM "
                + "TB_RESERVA_LUGAR T1, TB_EVENTO T2 "
            + "WHERE T1.SEQ_EVENTO = T2.SEQ_EVENTO AND T1.CD_SIT_RESERVA = 'CO' AND "
                + "T2.SEQ_EVENTO = " + request.getParameter("idEvento") + " AND "
                + "RIGHT('000'+CONVERT(VARCHAR, T1.NR_MESA), 3) + RIGHT('00'+CONVERT(VARCHAR, T1.NR_CADEIRA), 2) IN (" + lugares + ")";
//out.print(sql);
Connection cn = Pool.getInstance().getConnection();
ResultSet rs = cn.createStatement().executeQuery(sql);
int lin=117;
int col=10;
boolean quebra=false;

while(rs.next()){
deEvento=rs.getString("DE_EVENTO");
dtEvento=rs.getString("DT_EVENTO");
hhEvento=rs.getString("HH_EVENTO");
tipo=rs.getString("IC_TIPO_PESSOA");
if(tipo=="S"){
    tipo="SÓCIO";
}else if(tipo=="N"){
    tipo="NÃO SÓCIO";
}else{
    tipo="CONVÊNIO";
}
valor=rs.getFloat("VR_RESERVA");
lugar=rs.getString("NR_MESA") + " - " + rs.getString("NR_CADEIRA");
nome=rs.getString("NO_PESSOA");
//valor=NumberFormat.getCurrencyInstance().format(valor);    
request.setAttribute("valor", valor);    
    
    if (!quebra){%>
        <div class="quebra"><table><tr><td>
    <%}    
    for(int i=0;i<3;i++){
%>
        <div class="bordaExterna" style="top:<%=lin%>px;left:<%=col%>px;width:333px;height:243px;">    
           <div style="position:absolute;top:5px;left:2px;">
               <img src="imagens/logo-intro.png" width="60" height="45"/>
           </div>    
           <div style="position:absolute;top:3px;left:62px;width:270px;" align="center">
               <span class="tituloComprovante">Comprovante de Pagamento</span> 
           </div>    
           <div style="position:absolute;top:20px;left:62px;width:270px;height:15px;overflow:hidden;" align="center">
               <span class="subTituloConvite"><%=deEvento %></span> 
           </div>    
           <div style="position:absolute;top:32px;left:62px;width:270px;" align="center">
               <span class="subTituloConvite"><%=hhEvento %></span> 
           </div>    
           <div  style="position:absolute;top:80px;left:10px;width:310px;" align="center">
               <span class="tipo"><%=tipo %></span> 
           </div>    

           <div class="bordaExterna" style="position:absolute;top:120px;left:10px;width:310px;height:25px;" align="center"></div>    
           <div style="border-left:1px solid black;position:absolute;top:120px;left:170px;height:26px;"></div>    
           <fmt:formatNumber var="valor2"  value="${valor}" pattern="#0.00"/>
           <div class="subTituloConvite" style="position:absolute;top:127px;left:15px;height:25px;" align="left">Valor: R$ ${valor2}</div>   
           <div class="subTituloConvite" style="position:absolute;top:127px;left:180px;height:25px;" align="left">Lugar: <%=lugar%></div>   


           <div class="bordaExterna" style="position:absolute;top:160px;left:10px;width:310px;height:25px;" align="center"></div>    
           <div class="subTituloConvite" style="position:absolute;top:167px;left:15px;height:25px;" align="left">Nome: <%=nome%></div>   

           <div  style="position:absolute;top:195px;left:10px;width:310px;height:40px;" align="center">
               <span class="mensagem">É INDISPENSÁVEL A APRESENTAÇÃO DESTE COMPROVANTE NO DIA DO EVENTO</span> 
           </div>    
        </div>
        <br>

<%
        //lin = lin + 20;
    }
    
    if (!quebra){%>
        </td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td>
    <%}else{%>
        </td></tr></table></div>
<%        
    }
    //lin = 117;
    quebra=!quebra;
}
%>
</td></tr></table></div>

</body>
</html>
