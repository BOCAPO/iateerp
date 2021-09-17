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
            <td class="tituloRelatorio"><b>Iate Clube de Brasília</b><br><b>Diretoria de Esportes Náuticos</b></td>
        </tr>
    </table>
    <br>
    <c:if test='${barco.ehBox=="N"}'>
        <div align="center"><u><b>REGISTRO DA EMBARCAÇÃO</b></u></div>
    </c:if>
    <c:if test='${barco.ehBox=="S"}'>
        <div align="center"><u><b>REGISTRO DE BOX</b></u></div>
    </c:if>
    <br>
    <table width="100%" border="0">
        <tr>
            <td align="left"><b>REGISTRADO EM NOME DE:</b> ${socio.nome}</td>
            <td align="left"><b>TITULO:</b> ${socio.matricula}</td>
        </tr>
    </table>

    <table width="100%" border="0">
        <tr>
            <td align="left">
                <b>PROPRIETARIO DA EMBARCAÇÃO:</b>
            </td>
        </tr>
    </table>
        
    <table width="100%" border="0">
        <tr>
        <td align="left">
            <b>END RESIDENCIAL:</b> ${titular.enderecoResidencial.endereco} ${titular.enderecoResidencial.bairro} ${titular.enderecoResidencial.cidade} ${titular.enderecoResidencial.UF}
        </td>
        </tr>
    </table>
            
    <table width="100%" border="0">
        <tr>
        <td align="left">
            <b>CEP:</b> ${titular.enderecoResidencial.CEP} 
        </td>
        <td align="left">
            <b>e-mail:</b> ${titular.email}
        </td>
        </tr>
    </table>
    <table width="100%" border="0">
        <tr>
        <td align="left">
            <b>Nº CELULAR :</b> ${titular.telefoneCelular}  
        </td>
        <td align="left">
            <b>TEL(Com.):</b> ${titular.enderecoComercial.telefone}  
        </td>
        <td align="left">
            <b>TEL(Res.):</b> ${titular.enderecoResidencial.telefone}
        </td>
        </tr>
    </table>
    

    <c:if test='${barco.ehBox=="N"}'>
        <p align="center"><u><b>CARACTERÍSTICAS DA EMBARCAÇÃO</b></u></p>
    
        <table width="100%" border="0">
            <tr>
            <td align="left">
                <b>NOME DA EMBARCAÇÃO:</b> ${barco.nome} &nbsp;&nbsp;&nbsp;&nbsp; <b>REG.CAP.Nº:</b> ${barco.numCapitania}
            </td>
            </tr>
        </table>
        
        <table width="100%" border="0">
            <tr>
            <td align="left">
                <b>CLASSIFICAÇÃO:</b> ${barco.classificacao} 
            </td>
            <td align="left">
                <b>MODELO:</b> ${barco.modelo}
            </td>
            </tr>
        </table>

        <table width="100%" border="0">
            <tr>
            <td align="left">
                <b>QUANT.TRIPULANTES:</b> ${barco.tripulantes} 
            </td>
            <td align="left">
                <b>COMPRIMENTO:</b> ${barco.comprimentoTotal} 
            </td>
            <td align="left">
                <b>MTS.Nº PÉS:</b> ${barco.pes} 
            </td>
            <td align="left">
                <b>Nº DO BOX:</b> ${barco.box}
            </td>
            </tr>
        </table>

        <table width="100%" border="0">
            <tr>
            <td align="left">
              <b>TIPO DE VAGA:</b> 
                <c:forEach var="tipo" items="${tipos}">
                    <c:if test='${barco.tipoVaga.id == tipo.id}'>${tipo.descricao}</c:if>
                </c:forEach>
            </td>
            </tr>
        </table>
            
        <p align="center"><strong><u>DECLARAÇÃO</u></strong></p>

        <div>Para todos os fins, declaro:  </div>
        <div>1.Estar de acordo com a cobrança de taxa referente à embarcação constante desta ficha, enquanto a mesma permanecer nas dependências do Setor Náutico, nos termos do Regulamento Interno da Diretoria de Esportes Náuticos.  </div>
        <div>2.Não retirar a embarcação do referido setor sem prévia comunicação à Secretaria Náutica.  </div>
        <div>3.Que irei informar, quando precisar de reparos, o nome do prestador de serviço, autorizando-o com antecedência, junto à Secretaria Náutica.  </div>
        <div>4.Declaro para os devidos fins, que tenho total conhecimento do regulamento do setor náutico e das normas da capitania dos portos ciente dos procedimentos que deverei seguir, assino o presente, assumindo quaisquer responsabilidade que desabonem o Iate Clube de Brasilia pela minha conduta.  </div>
    </c:if>

    <c:if test='${barco.ehBox=="S"}'>
        <p align="center"><u><b>CARACTERÍSTICAS DO BOX</b></u></p>
    
        <table width="100%" border="0">
            <tr>
            <td align="left">
                <b>NOME DO BOX:</b> ${barco.nome} &nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td align="left">
                <b>QUANT. M2.:</b> ${barco.qtM2}
            </td>
            </tr>
        </table>
        <table width="100%" border="0">
            <tr>
            <td align="left">
                <b>GELADEIRA:</b> ${barco.qtGeladeira} 
            </td>
            <td align="left">
                <b>EMPRESTIMO DE CHAVE:</b>
                <c:if test='${barco.emprestimoChave}'>
                   SIM
                </c:if>
                <c:if test='${barco.emprestimoChave==false}'>
                   NAO
                </c:if>
            </td>
            </tr>
        </table>

        <table width="100%" border="0">
            <tr>
            <td align="left">
                <b>PERÍODO DE DESCONTO:</b>
            </td>
            </tr>
        </table>
            
        <p align="center"><strong><u>DECLARAÇÃO</u></strong></p>

        <div>Para todos os fins, declaro:  </div>

        <div> 1. Estar de acordo com a cobrança de taxa referente ao box constante desta ficha, nos termos do Regulamento Interno da Diretoria de Esportes Náuticos;</div>
        <div> 2. Senão tiver interesse em manter a cessão de direito de uso do box, farei uma comunicação prévia à Secretaria Náutica;</div>
        <div> 3. Que irei informar, quando precisar de reparos, o nome do prestador de serviços, autorizando-o com antecedência, junto à Secretaria Náutica;</div>
        <div> 4. Autorizo a Secretaria Náutica a ficar com uma cópia da chave do Box e utilizá-la em casos de absoluta e comprovada emergência.</div>
        <div> 5. Estar de acordo sobre a proibição de qualquer modificação e/ou reforma no exterior do Box.</div>
        <div> 6. Estar de acordo  sobre a proibição da guarda, trânsito ou depósito de material combustível de qualquer espécie ou tipo, notadamente gasolina, álcool, querosene, no interior do box, bem como armas, explosivos e munições.</div>
        <div> 7. Declaro para os devidos fins, que tenho total conhecimento do Regulamento Interno do Setor Náutico e ciente dos procedimentos que deverei seguir, assino o presente assumindo quaisquer responsabilidades que desabonem o Iate Clube de Brasília pela minha conduta.</div>
        
    </c:if>
    
    <br>
    <p align="right"><b>Brasilia-DF,______de________________________de________</b></p>
  
  
  <br><br>
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
