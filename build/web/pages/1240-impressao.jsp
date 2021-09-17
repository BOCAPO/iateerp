<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
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
.tituloSecao {
	font-family: "Arial";
	font-size: 14px;
        font-weight: bold;
}
.divisoria { border-bottom:1px #000 solid;  padding-top:1px; margin-bottom:1px; }   

.legendaCodigo{ 
    font-family:Arial, Helvetica, sans-serif; 
    font-size:14px;  
    font-weight:bold; 
    color:black; 
    font-weight: bold;
    margin: 0px;
    font-style: italic;
}
.texto{ 
    font-family:Arial, Helvetica, sans-serif; 
    font-size:12px;  
    color:black; 
    margin: 0px;
}

.quebra { 
    page-break-after: always; 
} 

</style>


    <sql:query var="rs" dataSource="jdbc/iate">
        ${sql}
    </sql:query>
    <c:forEach var="row" items="${rs.rows}">
        <div class="quebra">
        <table width="100%" >
            <thead>
                <tr>
                  <td>
                    <table width="100%" >
                        <tr>
                          <th align="left" width="150px" rowspan="2" ><img src="imagens/logo-intro.png" width="70" height="55"/></th>
                          <th align="center" height="0" class="tituloRelatorio">Relatório de Dados Cadastrais</th>
                          <th width="150px" class="dados" align="right"><fmt:formatDate value="<%= new java.util.Date() %>" pattern="HH:mm:ss"/></th>
                        </tr>
                        <tr>
                          <th align="center" class="subTituloRelatorio"></th>
                          <th class="dados" align="right"><fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy"/></th>
                        </tr>
                    </table>
                  </td>
                </tr>  
            </thead>
            <tr>
                <td align="center" class="tituloSecao" style="color: black;"><b>--------------------------------------------------------- Titular ---------------------------------------------------------</b></td>
            </tr>

            <tr>
                <td align="center" class="tituloSecao" style="color: red;"><b>----------------------------------- Dados Pessoais --------------------------------------</b></td>
            </tr>
            <tr>
                <td>
                    <table width="100%">
                        <tr>
                            <td style="width: 100px;">
                                <img src="f?tb=6&mat=${row.CD_MATRICULA}&seq=${row.SEQ_DEPENDENTE}&cat=${row.CD_CATEGORIA}" class="recuoPadrao MargemSuperiorPadrao" width="90" height="110">                                                    
                            </td>
                            <td valign="top">
                                <table width="100%">
                                    <tr>
                                        <td>
                                            <table width="100%">
                                                <tr>
                                                    <td>
                                                        <p class="legendaCodigo">Nome</p>
                                                        <p class="texto">${row.NOME_PESSOA}</p>
                                                    </td>
                                                    <td>
                                                        <p class="legendaCodigo">Matrícula</p>
                                                        <p class="texto"><fmt:formatNumber value="${row.CD_MATRICULA}" pattern="0000"/></p>
                                                    </td>
                                                    <td>
                                                        <p class="legendaCodigo">Categoria</p>
                                                        <p class="texto">${row.descr_categoria}</p>
                                                    </td>
                                                </tr>
                                            </table>
                                        <td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <table  width="100%">
                                                <tr>
                                                    <td>
                                                        <p class="legendaCodigo">Tipo</p>
                                                        <p class="texto">
                                                            <c:choose>
                                                                <c:when test="${row.CD_IND_PES_FISICA_JURIDICA eq 'F'}">
                                                                    Física
                                                                </c:when>
                                                                <c:otherwise>
                                                                    Jurídica
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </p>            
                                                    </td>
                                                    <td>
                                                        <p class="legendaCodigo">Sexo</p>
                                                        <p class="texto">
                                                            <c:choose>
                                                                <c:when test="${row.CD_SEXO eq 'M'}">
                                                                    Masculino
                                                                </c:when>
                                                                <c:otherwise>
                                                                    Feminino
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </p>            
                                                    </td>
                                                    <td>
                                                        <p class="legendaCodigo">Dt. Nascimento</p>
                                                        <p class="texto"><fmt:formatDate value="${row.dt_nascimento}" pattern="dd/MM/yyyy"/></p>            
                                                    </td>
                                                    <td>
                                                        <p class="legendaCodigo"></p>
                                                        <p class="texto"></p>            
                                                    </td>
                                                    <td>
                                                        <p class="legendaCodigo">Identidade</p>
                                                        <p class="texto">${row.NR_IDENTIDADE_PESSOA}</p>            
                                                    </td>
                                                    <td>
                                                        <p class="legendaCodigo">CPF</p>
                                                        <p class="texto">${row.CPF_CGC_PESSOA}</p>            
                                                    </td>
                                                </tr>   
                                            </table>   
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table width="100%">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Situação</p>
                                <p class="texto">
                                    <c:choose>
                                        <c:when test="${row.CD_SIT_PESSOA eq 'NO'}">
                                            Normal
                                        </c:when>
                                        <c:when test="${row.CD_SIT_PESSOA eq 'SA'}">
                                            Sem Admissão
                                        </c:when>
                                        <c:when test="${row.CD_SIT_PESSOA eq 'EM'}">
                                            Em Admissão
                                        </c:when>
                                        <c:when test="${row.CD_SIT_PESSOA eq 'NA'}">
                                            Não Admitido
                                        </c:when>
                                        <c:when test="${row.CD_SIT_PESSOA eq 'RE'}">
                                            Retomado
                                        </c:when>
                                        <c:when test="${row.CD_SIT_PESSOA eq 'SU'}">
                                            Suspenso
                                        </c:when>
                                        <c:when test="${row.CD_SIT_PESSOA eq 'US'}">
                                            Tit.C/Usuário
                                        </c:when>
                                    </c:choose>
                                </p>            
                            </td>
                            <td>
                                <p class="legendaCodigo">Receb. Corresp.</p>
                                <p class="texto">
                                    <c:choose>
                                        <c:when test="${row.CD_END_CORRESPONDENCIA eq 'R'}">
                                            End. Residencial
                                        </c:when>
                                        <c:otherwise>
                                            End. Comercial
                                        </c:otherwise>
                                    </c:choose>
                                    
                                </p>            
                            </td>
                            <td>
                                <p class="legendaCodigo">Receb. Carnê</p>
                                <p class="texto">
                                    <c:choose>
                                        <c:when test="${row.CD_END_CARNE eq 'R'}">
                                            End. Residencial
                                        </c:when>
                                        <c:otherwise>
                                            End. Comercial
                                        </c:otherwise>
                                    </c:choose>
                                </p>            
                            </td>
                            <td>
                                <p class="legendaCodigo">Profissão</p>
                                <p class="texto">
                                    <c:if test="${not empty row.CD_PROFISSAO}">
                                        <sql:query var="rsProfissao" dataSource="jdbc/iate">
                                            SELECT DESCR_PROFISSAO FROM TB_PROFISSAO 
                                            WHERE CD_PROFISSAO = ${row.CD_PROFISSAO}
                                        </sql:query>
                                        ${rsProfissao.rowsByIndex[0][0]}
                                    </c:if>
                                </p>            
                            </td>
                            <td>
                                <p class="legendaCodigo">Cargo Especial</p>
                                <p class="texto">
                                    <c:if test="${not empty row.CD_CARGO_ESPECIAL}">
                                        <sql:query var="rsCargo" dataSource="jdbc/iate">
                                            SELECT DESCR_CARGO_ESPECIAL FROM TB_CARGO_ESPECIAL
                                            WHERE CD_CARGO_ESPECIAL = ${row.CD_CARGO_ESPECIAL}
                                        </sql:query>
                                        ${rsCargo.rowsByIndex[0][0]}
                                    </c:if>
                                </p>            
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table width="100%">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Nacionalidade</p>
                                <p class="texto">${row.NACIONALIDADE}</p>            
                            </td>
                            <td>
                                <p class="legendaCodigo">Naturalidade</p>
                                <p class="texto">${row.NATURALIDADE}</p>            
                            </td>
                            <td>
                                <p class="legendaCodigo">Estado Civil</p>
                                <p class="texto">
                                    <c:choose>
                                        <c:when test="${row.ESTADO_CIVIL eq 'CA'}">
                                            Casado (a)
                                        </c:when>
                                        <c:when test="${row.ESTADO_CIVIL eq 'DI'}">
                                            Divorciado (a)
                                        </c:when>
                                        <c:when test="${row.ESTADO_CIVIL eq 'OU'}">
                                            Outro
                                        </c:when>
                                        <c:when test="${row.ESTADO_CIVIL eq 'SO'}">
                                            Solteiro (a)
                                        </c:when>
                                        <c:when test="${row.ESTADO_CIVIL eq 'VI'}">
                                            Viúvo (a)
                                        </c:when>
                                        <c:otherwise>
                                            Não Informado
                                        </c:otherwise>
                                    </c:choose>
                                </p>            
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table width="100%">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Pai</p>
                                <p class="texto">${row.NOME_PAI}</p>            
                            </td>
                            <td>
                                <p class="legendaCodigo">Mae</p>
                                <p class="texto">${row.NOME_MAE}</p>            
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <table width="100%">
                        <tr>
                            <td>
                                <p class="legendaCodigo">Dt. Cad Tit</p>
                                <p class="texto"><fmt:formatDate value="${row.dt_cadastramento_titulo}" pattern="dd/MM/yyyy"/></p>            
                            </td>
                            <td>
                                <p class="legendaCodigo">Dt. Inclusão</p>
                                <p class="texto"><fmt:formatDate value="${row.DT_INCL_PESSOA}" pattern="dd/MM/yyyy"/></p>            
                            </td>
                            <td>
                                <p class="legendaCodigo">Proponente</p>
                                <p class="texto">${row.cd_categoria_proponente}/${row.cd_matricula_proponente} - ${row.NOME_PROPONENTE}</p>            
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <c:if test="${dadosResidenciais}">
                <tr>
                    <td align="center" class="tituloSecao" style="color: red;"><b>--------------------------------- Dados Residenciais ------------------------------------</b></td>
                </tr>
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td>
                                    <p class="legendaCodigo">Endereço</p>
                                    <p class="texto">${row.ENDERECO_R}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">Bairro</p>
                                    <p class="texto">${row.CIDADE_R}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">Cidade</p>
                                    <p class="texto">${row.BAIRRO_R}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">UF</p>
                                    <p class="texto">${row.UF_R}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">CEP</p>
                                    <p class="texto">${row.CEP_R}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">Telefone</p>
                                    <p class="texto">${row.TELEFONE_R}</p>            
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </c:if>
            <c:if test="${dadosComerciais}">
                <tr>
                    <td align="center" class="tituloSecao" style="color: red;"><b>--------------------------------- Dados Comerciais ------------------------------------</b></td>
                </tr>
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td>
                                    <p class="legendaCodigo">Endereço</p>
                                    <p class="texto">${row.ENDERECO_C}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">Bairro</p>
                                    <p class="texto">${row.BAIRRO_C}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">Cidade</p>
                                    <p class="texto">${row.CIDADE_C}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">UF</p>
                                    <p class="texto">${row.UF_C}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">CEP</p>
                                    <p class="texto">${row.CEP_C}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">Telefone</p>
                                    <p class="texto">${row.TELEFONE_C}</p>            
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </c:if>
            <c:if test="${dadosBancarios}">
                <tr>
                    <td align="center" class="tituloSecao" style="color: red;"><b>--------------------------------- Dados Bancários ------------------------------------</b></td>
                </tr>
                <tr>
                    <td>
                        <table width="100%">
                            <tr>
                                <td>
                                    <p class="legendaCodigo">Agência / DV</p>
                                    <p class="texto">${row.NR_AGENCIA_CC} / ${row.DV_AGENCIA_CC}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">Conta / DV</p>
                                    <p class="texto">${row.NR_CONTA_CORRENTE} / ${row.DV_CONTA_CORRENTE}</p>            
                                </td>
                                <td>
                                    <p class="legendaCodigo">Titular da Conta</p>
                                    <p class="texto">${row.NO_TITULAR_CC}</p>            
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </c:if>
                
            <c:if test="${dependentes}">
                <tr>
                    <td align="center" class="tituloSecao" style="color: red;"><b>--------------------------------- Dependentes ------------------------------------</b></td>
                </tr>
                <sql:query var="rs2" dataSource="jdbc/iate">
                 SELECT T1.*, T2.DESCR_TP_DEPENDENTE 
                        FROM TB_PESSOA T1, TB_TIPO_DEPENDENTE T2 
                        WHERE T1.CD_TP_DEPENDENTE = T2.CD_TP_DEPENDENTE 
                        AND   T1.CD_MATRICULA = ?
                        AND   T1.CD_CATEGORIA = ?
                        AND   T1.SEQ_DEPENDENTE <> 0
                        <sql:param>${row.CD_MATRICULA}</sql:param>
                        <sql:param>${row.CD_CATEGORIA}</sql:param>
                </sql:query>
                <c:forEach var="dep" items="${rs2.rows}">
                    <tr>
                        <td>
                            <table width="100%">
                                <tr>
                                    <td style="width: 100px;">
                                        <img src="f?tb=6&mat=${row.CD_MATRICULA}&seq=${dep.SEQ_DEPENDENTE}&cat=${row.CD_CATEGORIA}" class="recuoPadrao MargemSuperiorPadrao" width="90" height="110">                                                    
                                    </td>
                                    <td valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td>
                                                    <table width="100%">
                                                        <tr>
                                                            <td>
                                                                <p class="legendaCodigo">Nome</p>
                                                                <p class="texto">${dep.NOME_PESSOA}</p>
                                                            </td>
                                                            <td>
                                                                <p class="legendaCodigo">Dt. Nascimento</p>
                                                                <p class="texto"><fmt:formatDate value="${dep.DT_NASCIMENTO}" pattern="dd/MM/yyyy"/></p>
                                                            </td>
                                                            <td>
                                                                <p class="legendaCodigo">Tipo Dependente</p>
                                                                <p class="texto">${dep.DESCR_TP_DEPENDENTE}</p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                <td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table  width="100%">
                                                        <tr>
                                                            <td>
                                                                <p class="legendaCodigo">Sexo</p>
                                                                <p class="texto">
                                                                    <c:choose>
                                                                        <c:when test="${dep.cd_sexo eq 'M'}">
                                                                            Masculino
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            Feminino
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </p>            
                                                            </td>
                                                            <td>
                                                                <p class="legendaCodigo">Caso Especial</p>
                                                                <p class="texto">
                                                                    <c:choose>
                                                                        <c:when test="${dep.CD_CASO_especial eq 'D'}">
                                                                            Deficiente
                                                                        </c:when>
                                                                        <c:when test="${dep.CD_CASO_especial eq 'U'}">
                                                                            Universitário até <fmt:formatDate value="${dep.dt_caso_especial}" pattern="dd/MM/yyyy"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            Normal
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </p>            
                                                            </td>
                                                            <td>
                                                                <p class="legendaCodigo">Ret. Convite</p>
                                                                <p class="texto">
                                                                    <c:choose>
                                                                        <c:when test="${empty dep.Dt_Valid_Ret_Convite}">
                                                                            Não Autorizado
                                                                        </c:when>
                                                                        <c:when test="${dep.Dt_Valid_Ret_Convite eq '31/12/9999'}">
                                                                            Indeterminado
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            Até <fmt:formatDate value="${dep.Dt_Valid_Ret_Convite}" pattern="dd/MM/yyyy"/>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </p>            
                                                            </td>
                                                            <td>
                                                                <p class="legendaCodigo">Res. Churrasqueira</p>
                                                                <p class="texto">${row.cd_armario}
                                                                    <c:choose>
                                                                        <c:when test="${empty dep.Dt_Valid_Reserva}">
                                                                            Não Autorizado
                                                                        </c:when>
                                                                        <c:when test="${dep.Dt_Valid_Reserva eq '31/12/9999'}">
                                                                            Indeterminado
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            Até <fmt:formatDate value="${dep.Dt_Valid_Reserva}" pattern="dd/MM/yyyy"/>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </p>            
                                                            </td>
                                                        </tr>   
                                                    </table>   
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </table>  
        </div>
    </c:forEach>

</body>
</html>

