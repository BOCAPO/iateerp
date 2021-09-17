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
.numeroConvite{
	font-family: "Times New Roman";
	font-size: 10px;
}
.codBarras{
	font-family: "C39P36DlTt";
	font-size: 36px;
}
.textoVisitante{
	font-family: "Arial";
	font-size: 30px;
        color: red;
}
.textoSetor{
	font-family: "Times New Roman";
	font-size: 14px;
        color: black;
}
</style>
</head>
<body>

<div style="position:absolute; top:20px;left:20px;">    
    <img src="imagens/logo-intro.png" width="80" height="60"/>
</div>    
<div class="textoVisitante" style="position:absolute; top:40px;left:110px;">    
    VISITANTE
</div>    

    
<table width="540px" cellspacing="0" cellpadding="0">
  <tr>
      <td class="bordaSimples" style="height:155px;">
          <table width="270px">
            <tr>
                <td class="" style="height:80px;">
                    &nbsp;
                </td>
            </tr>
            <tr>
                <td align="center" class="textoSetor" style="height:75px;">
                    <b>${crachavisitante.deSetor}</b>
                </td>
            </tr>
          </table>
      </td>
      <td class="bordaSimples" style="height:155px;">
          <table width="270px">
            <tr>
                <td class="" style="height:80px;">
                    &nbsp;
                </td>
            </tr>
            <tr>
                <td align="center" class="textoSetor" style="height:75px;">
                    <fmt:formatNumber pattern="000000000" var="nrCracha" value="${crachavisitante.id}"/>
                    <span class="numeroConvite">${nrCracha}</span><br><span class="codBarras">!${nrCracha}!</span>
                </td>
            </tr>
          </table>
      </td>
  </tr>
</table>
    

</body>
</html>

