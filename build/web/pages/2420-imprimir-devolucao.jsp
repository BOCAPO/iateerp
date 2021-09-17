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
            <td class="tituloRelatorio"><b>Iate Clube de Brasília</b><br><b>Departamento de Operações e Logística</b></td>
        </tr>
    </table>
    <br><br><br>
    <div align="center"><u><b>ENTREGA DE ACHADOS E PERDIDOS</b></u></div>
    <br><br><br>
    <table width="100%" border="0">
        <tr>
            <td align="left">
                <b>MATERIAL ENTREGUE:</b> ${objeto.descricao}<br>
                <b>DEFINIÇÃO:</b> 
                <c:choose>
                    <c:when test='${objeto.definicao == 0}'>Não Informado</c:when>
                    <c:when test='${objeto.definicao == 1}'>Moda Praia</c:when>
                    <c:when test='${objeto.definicao == 2}'>Acessório Esportivo</c:when>
                    <c:when test='${objeto.definicao == 3}'>Acessório Aquático</c:when>
                    <c:when test='${objeto.definicao == 4}'>Vestiário</c:when>
                    <c:when test='${objeto.definicao == 5}'>Outros</c:when>
                    <c:when test='${objeto.definicao == 6}'>Acessórios</c:when>
                    <c:when test='${objeto.definicao == 7}'>Brinquedos</c:when>
                    <c:when test='${objeto.definicao == 8}'>Eletrônico</c:when>
                    <c:when test='${objeto.definicao == 9}'>Material escolar</c:when>
                    <c:when test='${objeto.definicao == 10}'>Moda praia</c:when>
                    <c:when test='${objeto.definicao == 11}'>Vestuário</c:when>
                    <c:when test='${objeto.definicao == 12}'>Acessórios esportivos</c:when>
                    <c:when test='${objeto.definicao == 13}'>Acessórios aquáticos</c:when>
                    <c:when test='${objeto.definicao == 14}'>Chapelaria</c:when>
                    <c:when test='${objeto.definicao == 15}'>Higiene e Limpeza</c:when>
                    <c:when test='${objeto.definicao == 16}'>Bolsas</c:when>
                    <c:when test='${objeto.definicao == 17}'>Calçados</c:when>
                    <c:when test='${objeto.definicao == 18}'>Óculos</c:when>
                    <c:when test='${objeto.definicao == 19}'>Saboneteira</c:when>
                    <c:when test='${objeto.definicao == 20}'>Acessórios diversos</c:when>
                    <c:when test='${objeto.definicao == 21}'>Utensílios de cozinha</c:when>
                    <c:when test='${objeto.definicao == 22}'>Mesa e banho</c:when>
                    <c:when test='${objeto.definicao == 23}'>Bolas em geral</c:when>
                    <c:when test='${objeto.definicao == 24}'>Garrafas</c:when>
                    <c:when test='${objeto.definicao == 25}'>Acessórios de natação</c:when>
                </c:choose>                        
                
                <br>
                <b>COR:</b> ${objeto.cor} <br>
                <b>SEXO:</b> 
                <c:choose>
                    <c:when test='${objeto.sexo == 0}'>Não informado</c:when>
                    <c:when test='${objeto.sexo == 1}'>Feminino </c:when>
                    <c:when test='${objeto.sexo == 2}'>Masculino</c:when>
                    <c:when test='${objeto.sexo == 3}'>Unisex</c:when>
                </c:choose>                        
                <br>
            </td>
        </tr>
    </table>
            
    <br><br>
    
    <table width="100%" border="0">
        <tr>
            <td align="left">
                <b>
                <c:choose>
                    <c:when test='${objeto.perfilRetirante == 0}'>SÓCIO(A): </c:when>
                    <c:when test='${objeto.perfilRetirante == 1}'>CONVIDADO(A): </c:when>
                    <c:when test='${objeto.perfilRetirante == 2}'>PRESTADOR(A): </c:when>
                    <c:when test='${objeto.perfilRetirante == 3}'>RETIRANTE: </c:when>
                </c:choose>                        
                </b>
                ${objeto.nomeRetirante}
            </td>
        </tr>
        <tr>
            <td align="left">
                <b>TELEFONE:</b> ${objeto.telRetirante}
            </td>
        </tr>
    </table>
            
    <br><br>
    
    <table width="100%" border="0">
        <tr>
        <td align="left">
            <b>DATA SAÍDA:</b> 
            <fmt:formatDate var="dataDevolucao" value="${objeto.dtDevolucao}" pattern="dd/MM/yyyy"/>
            ${dataDevolucao}
        </td>
        </tr>
    </table>
        
    <br><br>
            
    <table width="100%" border="0">
        <tr>
        <td align="left">
            <b>DATA ENTRADA:</b> 
            <fmt:formatDate var="dataEntrada" value="${objeto.dtEntrada}" pattern="dd/MM/yyyy"/>
            ${dataEntrada}
        </td>
        </tr>
    </table>
        
    <br><br><br><br>

    <table width="100%">
        <tr>
            <td align="center"><b>___________________________________</b></td>
        </tr>
        <tr>
            <td align="center"><b>ASSINATURA</b></td>
        </tr>
    </table>
    
    <br><br>
    
    <table width="100%" border="0">
        <tr>
        <td align="left">
            <b>DEVOLVIDO PELO FUNCIOÁRIO:</b> 
            ${objeto.funcDevolucao.nome} - Matricula: ${objeto.funcDevolucao.matricula}
        </td>
        </tr>
    </table>
        
</body>
</html>
