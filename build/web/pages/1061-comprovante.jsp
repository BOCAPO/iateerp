<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<fmt:setLocale value="pt_BR"/>
<html>
    <!-- ${pageContext.request.servletPath} -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
        <link rel="shortcut icon" href="imagens/icones/favicon.ico" type="image/x-icon"/>
	<title>Iate Clube de Brasília</title>

<script>
	factory.printing.header="";
	factory.printing.footer="";
</script>

<style type="text/css">
.tituloIate {
	font-family: Georgia, "Times New Roman", Times, serif;
	font-size: 20px;
	font-weight: bold;
	color: blue;
}

.tituloRelatorio {
	font-family: "Arial";
	font-size: 20px;
	font-weight: bold;
}
.tituloSuperintendencia{
	font-family: "Arial";
	font-size: 16px;
	font-weight: bold;
}

.tituloCampo {
        font-family: "Arial";
	font-size: 13px;
}
.tituloConfirmacao {
	font-family: "Arial";
	font-size: 18px;
	font-weight: bold;
	color: red;
}
.estiloTextArea{
	font-family: "Arial";
	font-size: 13px;
	color: #000;
	width:100%;
	height:100%;
	border:0px;
	background-color:transparent;
}

.bordaExterna{
        position:absolute; 
        border:1px solid black;
}

.campoItens{
	font-family: "Courier New";
	font-size: 14px;
	color: black;
}

</style>
</head>
<body>

    <div style="position:absolute;top:20px;left:20px;">
        <img src="imagens/logo-intro.png" width="100" height="80"/>
    </div>    
    <div class="tituloIate" style="position:absolute;top:20px;left:20px;width:700px;" align="center">
        IATE CLUBE DE BRASÍLIA
    </div>    
    <div class="tituloSuperintendencia" style="position:absolute;top:52px;left:20px;width:700px;" align="center">
        Superintendência - Fones: 3329-8708 / 3329-8709
    </div>    
    <div class="tituloRelatorio" style="position:absolute;top:75px;left:20px;width:700px;" align="center">
        Comprovante de Reserva de Dependência
    </div>    
    
    <fmt:formatDate var="dtConfirmacao" value="${reserva.dtConfirmacao}" pattern="dd/MM/yyyy" />
    <div class="tituloConfirmacao" style="position:absolute;top:105px;left:400px;">
        Dt. Confirmação: ${dtConfirmacao}
    </div>    
    <sql:query var="rs" dataSource="jdbc/iate">
        SELECT * FROM TB_DEPENDENCIA
        WHERE SEQ_DEPENDENCIA = ${reserva.dependencia}
    </sql:query>    
    <c:forEach var="row" items="${rs.rows}">
        <div class="bordaExterna" style="top:130px;left:20px;width:700px;height:850px;">    
           <div style="border-top:1px solid black;position:absolute;top:70px;left:0px;width:700px;"></div>    
           <div style="border-top:1px solid black;position:absolute;top:140px;left:0px;width:700px;"></div>    
           <div style="border-left:1px solid black;position:absolute;top:140px;left:350px;height:500px;"></div>    
           <div style="border-top:1px solid black;position:absolute;top:640px;left:0px;width:700px;"></div>    
           <div class="tituloCampo" style="position:absolute;top:15px;left:20px;"><b>Dependência: </b>${row.DESCR_DEPENDENCIA}</div>    
           <div class="tituloCampo" style="position:absolute;top:35px;left:20px;"><b>Tipo de Evento: </b>${tipoEvento.descricao}</div>    
           
            <fmt:formatDate var="dtInicio" value="${reserva.dtInicio}" pattern="dd/MM/yyyy HH:mm:ss" />
            <fmt:formatDate var="dtFim" value="${reserva.dtFim}" pattern="dd/MM/yyyy  HH:mm:ss" />
           <div class="tituloCampo" style="position:absolute;top:15px;left:370px;"><b>Período: </b>${dtInicio} a ${dtFim}</div>    
           <div class="tituloCampo" style="position:absolute;top:35px;left:370px;"><b>Publico Previsto: </b>${reserva.publico}</div>    
           <div class="tituloCampo" style="position:absolute;top:80px;left:20px;"><b>Responsável: </b>${reserva.interessado}</div>    
           <div class="tituloCampo" style="position:absolute;top:98px;left:20px;"><b>Contato: </b>${reserva.nomeContato}</div>    
           <fmt:formatDate var="dtReserva" value="${reserva.dtReserva}" pattern="dd/MM/yyyy" />
           <div class="tituloCampo" style="position:absolute;top:115px;left:20px;"><b>Dt. Solicitação: </b>${dtReserva}</div>    
           <div class="tituloCampo" style="position:absolute;top:80px;left:450px;"><b>Telefone: </b>${reserva.telefone}</div>    
           <div class="tituloCampo" style="position:absolute;top:98px;left:450px;"><b>Telefone: </b>${reserva.telContato}</div>    

           <div class="campoItens" style="position:absolute;top:150px;left:360px;"><b>Ítem</b></div>
           <div class="campoItens" style="position:absolute;top:150px;left:560px;"><b>Quant.</b></div>
           <div class="campoItens" style="position:absolute;top:150px;left:650px;"><b>Valor</b></div>
           
           <%
           int lin=170;
           %>
           <sql:query var="rs2" dataSource="jdbc/iate">
                select * from TB_ITEM_ALUGUEL_DEP t1, TB_ITEM_ALUGUEL t2   
                where t1.CD_ITEM_ALUGUEL = t2.CD_ITEM_ALUGUEL and
                SEQ_RESERVA =  ${reserva.numero}
           </sql:query>    
           <c:forEach var="row2" items="${rs2.rows}">
                <div class="campoItens" style="position:absolute;top:<%=lin%>px;left:360px;">${row2.DE_ITEM_ALUGUEL}</div>
                <div class="campoItens" style="position:absolute;top:<%=lin%>px;left:575px;width:30px;" align="right">${row2.NU_QUANTIDADE}</div>
                <fmt:formatNumber var="valor"  value="${row2.VR_TOTAL}" pattern="#,##0.00"/>
                <div class="campoItens" style="position:absolute;top:<%=lin%>px;left:605px;width:85px;" align="right">${valor}</div>
                <%lin=lin+15;%>
           </c:forEach>

                
           <sql:query var="rs3" dataSource="jdbc/iate">
                select sum(vr_total) VR_TOTAL from TB_ITEM_ALUGUEL_DEP
                where SEQ_RESERVA =  ${reserva.numero}
           </sql:query>    
           <c:forEach var="row3" items="${rs3.rows}">
                <div class="campoItens" style="position:absolute;top:<%=lin+15%>px;left:360px;"><b>TOTAL</b></div>
                <fmt:formatNumber var="valor"  value="${row3.VR_TOTAL}" pattern="#,##0.00"/>
                <div class="campoItens" style="position:absolute;top:<%=lin+15%>px;left:605px;width:85px;" align="right"><b>${valor}<b></div>
           </c:forEach>
                            
           <jsp:useBean id="now" class="java.util.Date" scope="request" />
           <fmt:formatDate var="dtAtual" value="${now}" pattern="dd/MM/yyyy" />
           <div class="tituloSuperintendencia" style="position:absolute;top:660px;left:10px;"><b>Brasília, ${dtAtual}</b></div>    

           <div class="tituloCampo" style="position:absolute;top:720px;left:80px;">
               _____________________________<br>Responsável pelo Evento<br><br><br><br>
               _____________________________<br>Tesouraria
           </div>    

           <div class="tituloCampo" style="position:absolute;top:720px;left:400px;">
               _____________________________<br>Funcionário<br><br><br><br>
               _____________________________<br>Diretor Social
           </div>    

           <div style="position:absolute; top:145px;left:3px;width:343px;height:490px;">
                <textarea class="estiloTextArea" disabled>${row.MSG_LINHA}</textarea>    
           </div>  
        </c:forEach>
    </div>
    
</body>
</html>