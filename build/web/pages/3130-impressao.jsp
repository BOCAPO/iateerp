<%@page import="techsoft.tabelas.Funcionario"%>
<%@page import="techsoft.tabelas.Curso"%>
<%@page import="techsoft.curso.Turma"%>
<%@page import="java.text.*"%>
<%@page import="java.util.Date"%>
<%@page import="techsoft.db.Pool"%>
<%@page import="java.sql.*"%>
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
	font-size: 30px;
	font-weight: bold;
	color: #000;
}

.cabecalho {
	font-family: "Times New Roman";
	font-size: 12px;
	font-weight: bold;
	color: #000;
}

.dados {
	font-family: "Times New Roman";
	font-size: 12px;
	color: #000;
}
        
.tituloDados {
	font-family: "Arial";
	font-size: 12px;
	color: #000;
	font-weight: bold;
}
.divisoria { border-bottom:1px #000 solid;  padding-top:1px; margin-bottom:1px; }    

.tabela{
    border-collapse:collapse;
}

.quebra { 
    page-break-after: always; 
} 

</style>

<c:forEach var="idTurma" items="${turmas}">
    <c:if test="${idTurma gt 0}">
        <sql:query var="rs" dataSource="jdbc/iate">
            EXEC SP_RECUPERA_MATRICULAS_PAUTA ?
            <sql:param value="${idTurma}"/>
        </sql:query>
        <%
            int idTurma = Integer.parseInt(pageContext.getAttribute("idTurma").toString());
            Turma t = Turma.getInstance(idTurma);
            Curso c = Curso.getInstance(t.getIdCurso());
            Funcionario p = Funcionario.getInstance(t.getIdProfessor());
            pageContext.setAttribute("turma", t);
            pageContext.setAttribute("curso", c);
            pageContext.setAttribute("funcionario", p);

            SimpleDateFormat fmtDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat fmtHour = new SimpleDateFormat("HH:mm:ss");
            pageContext.setAttribute("dtAtual", fmtDate.format(new Date()));
            pageContext.setAttribute("hhAtual", fmtHour.format(new Date()));
            int maxReg = 15;


        %>

        <table width="100%" >
            <tr>
                <td>
                    <table width="100%" >
                        <tr>
                            <%@include file="3130-cabecalho.jsp"%>  
                        </tr>
                    </table>
                </td>
            </tr>  
            <tr>
                <td>
                    <table width="100%" class="tabela" border="1">      
                        <tr>
                            <td width="20px">&nbsp;</td>
                            <td align='center' class="dados">NOME</td>

                            <c:forEach var="col" begin="1" end="31" step="1">
                                <fmt:formatNumber var="diaFormatado" pattern="00" value="${col}"/>
                                <td align='center' class="dados">${diaFormatado}</td>
                            </c:forEach>
                        </tr>

                        <% 
                        int i = 0; 
                        int qtlin = 0;
                        %>
                        <c:forEach var="row" items="${rs.rows}">


                            <% //monta novamente o cabecalho quando passa do limite de registros
                            if (qtlin == maxReg){
                                qtlin=0;
                            %>
                                </table></td></tr></table>

                                <div class="quebra"> </div>

                                <table width="100%" >
                                    <tr>
                                        <td>
                                            <table width="100%" >
                                                <tr>
                                                    <%@include file="3130-cabecalho.jsp"%>  
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>  
                                    <tr>
                                    <td>
                                        <table width="100%" class="tabela" border="1">      
                                            <tr>
                                                <td width="20px">&nbsp;</td>
                                                <td align='center' class="dados">NOME</td>

                                                <c:forEach var="col" begin="1" end="31" step="1">
                                                    <fmt:formatNumber var="diaFormatado" pattern="00" value="${col}"/>
                                                    <td align='center' class="dados">${diaFormatado}</td>
                                                </c:forEach>
                                            </tr>
                            <% //fim do if da montagem do cabecalho
                            }
                            %>


                            <% 
                            i++; 
                            qtlin++;
                            %>

                            <tr>
                                <td width="20px" class="dados"><%=i%></td>
                                <td class="dados">
                                    <div style="width: 180px; overflow: hidden; white-space: nowrap;">
                                        ${row.NOME_REDUZIDO}
                                    </div>
                                </td>

                                <c:forEach var="col" begin="1" end="31" step="1">
                                    <td align='center' class="dados"></td>
                                </c:forEach>
                            </tr>
                        </c:forEach>

                        <c:forEach begin="0" end="4" step="1">
                            <% //monta novamente o cabecalho quando passa do limite de registros
                            if (qtlin == maxReg){
                                qtlin=0;
                            %>
                                </table></td></tr></table>

                                <div class="quebra"> </div>

                                <table width="100%" >
                                    <tr>
                                        <td>
                                            <table width="100%" >
                                                <tr>
                                                    <%@include file="3130-cabecalho.jsp"%>  
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>  
                                    <tr>
                                    <td>
                                        <table width="100%" class="tabela" border="1">      
                                            <tr>
                                                <td width="20px">&nbsp;</td>
                                                <td align='center' class="dados">NOME</td>

                                                <c:forEach var="col" begin="1" end="31" step="1">
                                                    <fmt:formatNumber var="diaFormatado" pattern="00" value="${col}"/>
                                                    <td align='center' class="dados">${diaFormatado}</td>
                                                </c:forEach>
                                            </tr>
                            <% //fim do if da montagem do cabecalho
                            }
                            i++; 
                            qtlin++;
                            %>

                            <tr>
                                <td width="20px" class="dados"><%=i%></td>
                                <td class="dados">&nbsp;</td>

                                <c:forEach var="col" begin="1" end="31" step="1">
                                    <td align='center' class="dados"></td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>    
        </table>




        <br>            
        <table width="100%" >
            <tr>
                <td align="center" class="dados">____________________________________</td>
            </tr>
            <tr>
                <td align="center" class="dados">ASSINATURA DO PROFESSOR</td>
            </tr>
            <tr>
                <td><br/></td>
            </tr>
            <tr>
                <td align="center" class="dados">____________________________________</td>
            </tr>
            <tr>
                <td align="center" class="dados">ASSINATURA DO SUPERVISOR</td>
            </tr>
        </table>
    </c:if>            
</c:forEach>

</body>
</html>