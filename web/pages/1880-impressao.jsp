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
            <td align="center" class="tituloRelatorio">Acesso aos serviços do Iate Clube de Brasília pela Internet</td>
          </tr>
        </table>
        <br>
        <table width="97%"  class="bordaSimples" align="center">
            <tr class="semBorda">
              <td align="left" class="tituloCampo">Usuário: ${usuario}</td>
              <td align="left" class="tituloCampo">Senha: ${senha}</td>
            </tr>
        </table>
        <br>
    </td>
  </tr>
</table>
</body>
</html>