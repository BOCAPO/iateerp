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
        <title>DEVOLUÇÃO DE MATERIAL</title>
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
                        DEVOLUÇÃO DE MATERIAL
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
        
        <c:forEach var="seq" items="${seqs}">
            <sql:query var="rs" dataSource="jdbc/iate">
                     SELECT  +
                        T1.DESCR_MATERIAL,  
                        T2.QT_EMPREST_PESSOA,  
                        T2.DT_EMPREST_PESSOA, 
                        T2.DT_REAL_DEVOLUCAO, 
                        RIGHT('00' + CONVERT(VARCHAR, T3.CD_CATEGORIA), 2) + '/' +  
                        RIGHT('0000' + CONVERT(VARCHAR, T3.CD_MATRICULA), 4) + ' - ' +  
                        T3.NOME_PESSOA NOME_PESSOA,  
                        ISNULL(T2.VR_DIARIA, 0) VR_DIARIA,  
                        ISNULL(T2.VR_RESTITUICAO, 0) VR_RESTITUICAO,  
                        ISNULL(T2.VR_EMPRESTIMO, 0) VR_EMPRESTIMO,  
                        T2.USER_DEVOLUCAO  
                     From  
                        TB_MATERIAL T1,  
                        TB_PESSOA_TB_MATERIAL T2,  
                        TB_PESSOA T3  
                     Where 
                        T1.CD_MATERIAL    =  T2.CD_MATERIAL       AND 
                        T2.CD_MATRICULA =  T3.CD_MATRICULA       AND 
                        T2.CD_CATEGORIA    =  T3.CD_CATEGORIA       AND 
                        T2.SEQ_DEPENDENTE    =  T3.SEQ_DEPENDENTE       AND
                        T2.CD_SEQ_EMPRESTIMO = ${seq}
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
                    <div class="campo">${rs.rows[0].QT_EMPREST_PESSOA}</div>
                </td>
              </tr>
              <tr>
                <td>
                    <div class="tituloCampo">Data Empréstimo:</div>
                    <div class="campo"><fmt:formatDate value="${rs.rows[0].DT_EMPREST_PESSOA}" pattern="dd/MM/yyyy HH:mm:ss"/></div>
                </td>
              <tr>
              <tr>
                <td>
                    <div class="tituloCampo">Data Devolução</div>
                    <div class="campo"><fmt:formatDate value="${rs.rows[0].DT_REAL_DEVOLUCAO}" pattern="dd/MM/yyyy HH:mm:ss"/></div>
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
                    Lançado no proximo carne o valor de R$: <fmt:formatNumber value="${rs.rows[0].VR_EMPRESTIMO}" pattern="#0.00"/> referente ao emprestimo do material.
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
                    Lançado no proximo carne o valor de R$: <fmt:formatNumber value="${rs.rows[0].VR_DIARIA}" pattern="#0.00"/> referente a utilizacao do material.
                  </td>
                </tr>
                <tr>
                  <td>
                      &nbsp;
                  </td>
                </tr>
              </c:if>
              <c:if test="${rs.rows[0].VR_RESTITUICAO > 0}">
                <tr>
                  <td>
                    Lancado no proximo carne o valor de R$: <fmt:formatNumber value="${rs.rows[0].VR_RESTITUICAO}" pattern="#0.00"/> referente a restituicao do material.
                  </td>
                </tr>
              </c:if>
            </table>
        </c:forEach>
                
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
