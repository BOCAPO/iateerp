<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
.bordaSimples	{
	border-collapse: collapse;
	border-style: solid;
	border-bottom-width: 1px;
	border-top-width: 1px;
	border-right-width: 1px;
	border-left-width: 1px;
	border-color: #000;
	}
.bordaGrossa	{
	border-collapse: collapse;
	border-style: solid;
	border-bottom-width: 2px;
	border-top-width: 2px;
	border-right-width: 2px;
	border-left-width: 2px;
	border-color: #000;
	}
.semBorda	{
	border-collapse: collapse;
	border-style: none;
	border-bottom-width: 0px;
	border-top-width: 0px;
	border-right-width: 0px;
	border-left-width: 0px;
	}
.sohBordaSuperior	{
	border-collapse: collapse;
	border-bottom-style: none;
	border-top-style: solid;
	border-bottom-width: 0;
	border-top-width: 1px;
	border-right-width: 0;
	border-left-width: 0;
	border-color: #000;
	}
	
.tituloIate {
	font-family: Georgia, "Times New Roman", Times, serif;
	font-size: 20px;
	font-weight: bold;
	color: #00F;
}

.tituloRelatorio {
	font-family: "Times New Roman", Times, serif;
	font-size: 20px;
	font-style: italic;
	font-weight: bold;
}

.tituloCampo {
	font-weight: bold;
}

.estiloTextArea{
	font-family: "Times New Roman", Times, serif;
	font-size: 14px;
	color: #000;
	width:100%;
	height:400px;
	border:0px;
	background-color:transparent;
}
</style>
</head>
<body>

<table width="100%" class="bordaGrossa">
  <tr>
    <td>
		<table width="100%" border="0">
		  <tr>
		    <td align="center" width="150px" rowspan="2" ><img src="imagens/logo-intro.png" width="100" height="80"/></td>
		    <td align="center" height="45" class="tituloIate">IATE CLUBE DE BRASÍLIA</td>
		  </tr>
		  <tr>
		    <td align="center" class="tituloRelatorio">Comprovante de Reserva de Dependência</td>
		  </tr>
		</table>
		<br><br>
		<table width="100%"  class="bordaSimples">
			  <tr class="semBorda">
			    <td align="left" class="tituloCampo">Dependência</td>
			    <td align="left" class="tituloCampo">Data de Utilização</td>
			    <td align="left" class="tituloCampo">Previsão de Horário</td>
			  </tr>
			  <tr class="semBorda">
			    <td align="left">${reserva.deChurrasqueira}</td>
                            <fmt:formatDate var="utilizacao" value="${reserva.dtInicio}" pattern="dd MMM yyyy"/>
			    <td align="left">${utilizacao}</td>
                            <c:set var="horaInic" value="${fn:substring(reserva.hhInicio, 0, 2)}"/>
                            <c:set var="minutoInic" value="${fn:substring(reserva.hhInicio, 2, 4)}"/>
                            <c:set var="horaFim" value="${fn:substring(reserva.hhFim, 0, 2)}"/>
                            <c:set var="minutoFim" value="${fn:substring(reserva.hhFim, 2, 4)}"/>
			    <td align="left">${horaInic}:${minutoInic}hs a ${horaFim}:${minutoFim}hs</td>
			  </tr>
			  <tr class=sohBordaSuperior>
			    <td align="left" class="tituloCampo">Sócio</td>
			    <td align="left" class="tituloCampo">Telefone p/ Contato</td>
			    <td align="left" class="tituloCampo">Data de Confirmação</td>
			  </tr>
			  <tr  class="semBorda">
                            <fmt:formatNumber var="mat"  value="${reserva.matricula}" pattern="0000"/>
                            <fmt:formatNumber var="cat"  value="${reserva.categoriaSocio}" pattern="00"/>
			    <td align="left">${cat}/${mat} - ${reserva.interessado}</td>
			    <td align="left">${reserva.telefone}</td>
                            <fmt:formatDate var="confirmacao" value="${reserva.dtConfirmacao}" pattern="dd MMM yyyy HH:mm:ss"/>
			    <td align="left">${confirmacao}</td>
			  </tr>
			  <tr class=sohBordaSuperior>
			    <td align="left" class="tituloCampo">Email</td>
			    <td align="left" class="tituloCampo">Fax</td>
			    <td align="left" class="tituloCampo"></td>
			  </tr>
			  <tr  class="semBorda">
			    <td align="left">${reserva.email}&nbsp;</td>
			    <td align="left">${reserva.fax}&nbsp;</td>
			    <td align="left"></td>
			  </tr>
		</table>
    </td>
  </tr>
  <tr>
    <td>
		<textarea class="estiloTextArea" disabled>

${reserva.txComprovante}
                </textarea>    
    </td>
  </tr>
</table>

</body>
</html>