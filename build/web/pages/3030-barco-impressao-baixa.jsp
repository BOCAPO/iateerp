<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<fmt:setLocale value="pt_BR"/>

<body class="internas">
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
        .tituloRelatorio2 {
                font-family: "Times New Roman", Times, serif;
                font-size: 22px;
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
        
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <table width="100%" border="0">
        <tr>
            <td width="100px"><img src="imagens/logo-intro.png" width="70" height="55"/></td>
            <td class="tituloRelatorio"><b>Iate Clube de Brasília</b><br><b>Diretoria de Esportes Náuticos</b></td>
        </tr>
    </table>
    
    <br>
    <div align="center" class="tituloRelatorio2"><u>AUTORIZAÇÃO DE BAIXA E EXCLUSÃO DA TAXA</u></div>
    <c:if test='${barco.ehBox=="N"}'>
        <div align="center" class="tituloRelatorio2"><u>DE MANUTENÇÃO NÁUTICA</u></div>
    </c:if>
    <c:if test='${barco.ehBox=="S"}'>
        <div align="center" class="tituloRelatorio2"><u>DE BOX</u></div>
    </c:if>
    
    <br>
    
    <table width="100%">
        <tr>
            <td align="left"><b>Proprietário: </b> ${socio.nome}</td>
            <td align="left"><b>Título: </b> ${socio.matricula}</td>
        </tr>
        <tr>
            <td align="left"><b>End. Residência:</b> ${titular.enderecoResidencial.endereco} ${titular.enderecoResidencial.bairro} ${titular.enderecoResidencial.cidade} ${titular.enderecoResidencial.UF} </td>
            <td align="left"><b>Fone:</b> ${titular.enderecoResidencial.telefone}</td>
        </tr>
    </table>
    <br>  

    <c:if test='${barco.ehBox=="N"}'>
        <table>
            <tr>
                <td align="left"><b>Nome da Embarcação:</b> ${barco.nome}</td>
                <td align="left"><b>REG.CAP.Nº:</b> ${barco.numCapitania}</td>
            </tr>
            <tr>
                <td align="left"><b>Categoria:</b> ${barco.categoriaNautica.descricao} </td>
                <td align="left"><b>Classe:</b></td>
            </tr>
            <tr>
                <td align="left">
                  <b>Tipo de Vaga:</b>  
                  <c:forEach var="tipo" items="${tipos}">
                      <c:if test='${barco.tipoVaga.id == tipo.id}'>${tipo.descricao}</c:if>
                  </c:forEach>
                </td>
                <td align="left">
                    &nbsp
                </td>
            </tr>
              <tr>
                  <td><b>Possui Box?</b></td>
                  <td><b>(&nbsp;&nbsp;&nbsp;&nbsp;)Sim    (&nbsp;&nbsp;&nbsp;&nbsp;)Não</b></td>
              </tr>
              <tr>
                  <td><b>Deseja Permanecer com o Box?</b></td>
                  <td><b>(&nbsp;&nbsp;&nbsp;&nbsp;)Sim    (&nbsp;&nbsp;&nbsp;&nbsp;)Não</b></td>
              </tr>
              <tr>
                  <td><b>Possui outra embarcação em seu nome?</b></td>
                  <td><b>(&nbsp;&nbsp;&nbsp;&nbsp;)Sim    (&nbsp;&nbsp;&nbsp;&nbsp;)Não&nbsp;&nbsp;&nbsp;&nbsp;Qual?__________________</b></td>
              </tr>          
        </table>
    </c:if>
    <c:if test='${barco.ehBox=="S"}'>
        <table>
            <tr>
                <td align="left"><b>Nome da Embarcação:</b> ${barco.nome}</td>
                <td align="left">&nbsp;</td>
            </tr>
            <tr>
                <td align="left"><b>Nome do Box:</b> ${barco.nome}</td>
                <td align="left">&nbsp;</td>
            </tr>
            <tr>
                <td align="left"><b>Local:</b> ${barco.localBox.descricao}</td>
                <td align="left">&nbsp;</td>
            </tr>
            <tr>
                <td><b>Possui outro Box em seu nome?</b></td>
                <td><b>(&nbsp;&nbsp;&nbsp;&nbsp;)Sim    (&nbsp;&nbsp;&nbsp;&nbsp;)Não&nbsp;&nbsp;&nbsp;&nbsp;Qual?__________________</b></td>
            </tr>          
        </table>
    </c:if>

<p>
Obs.: Segundo art. 8º do Regulamento do Setor Náutico, a não substituição da embarcação no
periodo de 90 (noventa) dias implicará perda do direito de utilização da vaga nos pátios, galpões e ou
Box.</p>
<p>      
    
<c:if test='${barco.ehBox=="N"}'>
    O horário de entrada e saída de embarcação é de 9h às 17h, de 2ª a 6ª feira, bem como a prestação de
    serviço</p>
</c:if>


<p><b>OBS: _________________________________________________________________________</b></p>
<p><b>______________________________________________________________________________</b></p>
<p><b>______________________________________________________________________________</b></p>


  <br><br>   
  <p align="right"><b>Brasilia-DF,______de________________________de________</b></p>
  
  <br><br><br>
  <table width="100%">
      <tr>
          <td align="center"><b>___________________________________</b></td>
          <td align="center"><b>___________________________________</b></td>
      </tr>
      <tr>
          <td align="center"><b>Associado</b></td>
          <td align="center"><b>Funcionário da Secretaria Náutica</b></td>
      </tr>
  </table>

</body>
</html>
