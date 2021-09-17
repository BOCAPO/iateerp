<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html>
    <style type="text/css">
        .tituloRelatorio {
                font-family: "Arial";
                font-size: 26px;
                font-weight: bold;
        }
        .tituloCampo{
                font-family: "Arial";
                font-size: 15px;
                font-weight: bold;
        }        
        .campo{
                font-family: "Arial";
                font-size: 14px;
                margin-left:15px;
        }
        .campoCIENTE{
                font-family: "Arial";
                font-size: 18px;
                margin-left:15px;
        }
    </style>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>EMPRÉSTIMO DE MATERIAL</title>
    </head>
    <body>
        <input type="hidden" name="teste" value="${seqEmprestimo}">
        <table width="100%">
          <tr>
            <td>
                <table width="100%" border="0">
                  <tr>
                    <td align="center" width="150px" rowspan="2" >
                        <img src="imagens/logo-intro.png" width="100" height="80"/>
                    </td>
                    <td valign="bottom" align="center" height="45" class="tituloRelatorio">
                        EMPRÉSTIMO DE MATERIAL
                    </td>
                    <td align="center" width="150px" rowspan="2" >
                        &nbsp;
                    </td>
                  </tr>
                </table>
            </td>
          </tr>
        </table>
        
        <br><br>
        <HR>
        <br><br>
        
        <sql:query var="rs" dataSource="jdbc/iate">
            SELECT 
                RIGHT('00' + CONVERT(VARCHAR, T3.CD_CATEGORIA), 2) + '/' + RIGHT('0000' + CONVERT(VARCHAR, T3.CD_MATRICULA), 4) + ' - ' + T3.NOME_PESSOA NOME_PESSOA, 
                T2.QT_EMPREST_PESSOA QT_EMP,
                T1.USER_EMPRESTIMO,
                T1.DT_EMPREST_PESSOA,
                T4.DESCR_MATERIAL,
                ISNULL(T1.VR_EMPRESTIMO, 0) VR_EMPRESTIMO,
                ISNULL(T4.VR_DIARIA, 0) VR_DIARIA,
                ISNULL(T4.VAL_MATERIAL, 0) VAL_MATERIAL,
                DATEADD(D, ISNULL(PZ_PADRAO_DEVOLUCAO, 0), T1.DT_EMPREST_PESSOA) DT_MAX_DEV,
                T4.QT_DIAS_INI_COB
                
            FROM 
                TB_PESSOA_TB_MATERIAL T1, 
                TB_EMPRESTIMO_MATERIAL T2, 
                TB_PESSOA T3,
                TB_MATERIAL T4
            WHERE 
                T1.CD_MATRICULA = T3.CD_MATRICULA AND 
                T1.CD_CATEGORIA = T3.CD_CATEGORIA AND 
                T1.SEQ_DEPENDENTE = T3.SEQ_DEPENDENTE AND 
                
                T1.SEQ_EMPRESTIMO = T2.SEQ_EMPRESTIMO AND 
                
                T1.CD_MATERIAL = T4.CD_MATERIAL AND
                
                T1.SEQ_EMPRESTIMO = ${seqEmprestimo}
        </sql:query>    
                    
        <table width="100%">
          <tr>
            <td style="width: 200px">
                <div class="tituloCampo">Sócio</div>
                <div class="campo">${rs.rows[0].NOME_PESSOA}</div>
            </td>
          </tr>
          <tr>
            <td align="left" width="150px">
                <div class="tituloCampo">Material:</div>
                <div class="campo">${rs.rows[0].DESCR_MATERIAL}</div>
            </td>
          </tr>
          <tr>
            <td style="width: 200px">
                <div class="tituloCampo">Quantidade:</div>
                <div class="campo">${rs.rows[0].QT_EMP}</div>
            </td>
          </tr>
          <tr>
            <td style="width: 200px">
                <div class="tituloCampo">Usuário:</div>
                <div class="campo">${rs.rows[0].USER_EMPRESTIMO}</div>
            </td>
          </tr>
          <tr>
            <td>
                <div class="tituloCampo">Data:</div>
                <div class="campo"><fmt:formatDate value="${rs.rows[0].DT_EMPREST_PESSOA}" pattern="dd/MM/yyyy HH:mm:ss"/></div>
            </td>
          <tr>
          </tr>
            <td>
                &nbsp;
            </td>
          </tr>
          <c:if test="${rs.rows[0].VR_EMPRESTIMO > 0}">
            <tr>
              <td>
                  Pelo empréstimo do material será cobrado o valor de R$ <fmt:formatNumber value="${rs.rows[0].VR_EMPRESTIMO}" pattern="#0.00"/> lançado automaticamente no carne mensal.
              </td>
            </tr>
            <tr>
              <td>
                  &nbsp;
              </td>
            </tr>
          </c:if>
          <c:if test="${rs.rows[0].VR_DIARIA > 0}">
            <tr>
              <td>
                  Após o ${rs.rows[0].QT_DIAS_INI_COB}o dia, para cada dia adicional de uso do material, será cobrado o valor de R$ <fmt:formatNumber value="${rs.rows[0].VR_DIARIA}" pattern="#0.00"/>
              </td>
            </tr>
            <tr>
              <td>
                  &nbsp;
              </td>
            </tr>
          </c:if>
          <c:if test="${rs.rows[0].VAL_MATERIAL > 0}">
            <tr>
              <td>
                  Caso o material não seja devolvido até <fmt:formatDate value="${rs.rows[0].DT_MAX_DEV}" pattern="dd/MM/yyyy"/> será lançado automaticamente no carne mensal o valor de R$ <fmt:formatNumber value="${rs.rows[0].VAL_MATERIAL}" pattern="#0.00"/>
              </td>
            </tr>
          </c:if>
        </table>
                
                
        <br><br>
        <HR>
        <br><br><br><br>

        <table width="100%">
            <tr>
                <td class="campo" align="center">___________________________________________</td>
            </tr>
            <tr >
                <td class="campo" align="center">ASSINATURA DO SÓCIO</td>
            </tr>
        </table>
    </body>
</html>
