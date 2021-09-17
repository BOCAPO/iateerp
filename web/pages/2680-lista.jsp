<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}

</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    <script type="text/javascript" language="JavaScript">
        function validarForm(){
            document.forms[0].submit();
        }
        
        function agendaDia(armario, emprestimo){
            $('#armario').val(armario);
            $('#emprestimo').val(emprestimo);
            $('#app').val(2681);
            document.forms[0].submit();
        }
        
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Emprestimos da Sauna</div>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" id="app" value="2680">
        <input type="hidden" name="acao" id="acao" value="showForm">
        <input type="hidden" name="armario" id="armario" value="">
        <input type="hidden" name="emprestimo" id="emprestimo" value="">
        
        
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Tipo de Armário</p>
                    <div class="selectheightnovo">
                        <select name="idTipoArmario" class="campoSemTamanho alturaPadrao larguraCidade" onchange="validarForm()">
                            <c:forEach var="tipoArmario" items="${tipoArmarios}">
                                <option value="${tipoArmario.id}" <c:if test='${idTipoArmario == tipoArmario.id}'>selected</c:if>>${tipoArmario.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <div style="margin-top: 20px; margin-left: 20px">
                        <a href="c?app=2681&acao=showForm"><img src="imagens/btn-kitSauna.png" width="100" height="25" /></a><br>
                    </div>
                </td>
            </tr>
        </table>
        
        <div class="divisoria"></div>
    </form>
        
    <table id="tabela" style="width:98%;margin-left:15px;">
        <tbody>
            
            
            <sql:query var="rs" dataSource="jdbc/iate">
                {call SP_EMPRESTIMO_SAUNA ('L', ${idTipoArmario}, NULL, NULL, NULL, NULL)}
            </sql:query>                    
            <c:forEach var="row" items="${rs.rows}">
                <c:if test='${qtLinha == 8}'>
                    <c:set var="qtLinha" value="0"/>
                    </tr><tr>
                </c:if>                
                    
                <c:choose>
                   <c:when test = "${row.CD_MATRICULA == null && row.IC_SITUACAO == null}">
                      <td class="column1" align="center" style="font-size: 18px !important; vertical-align: top !important; height:93px !important; width: 100px !important; background-color: lightgreen">
                        <a onclick="agendaDia(${row.CD_ARMARIO}, 0)" href="#">
                            <b>${row.DESCR_ARMARIO}</b>
                            <br>
                        </a>
                   </c:when>
                   <c:when test = "${row.CD_MATRICULA == null && row.IC_SITUACAO != null}">
                      <td class="column1" align="center" style="font-size: 18px !important; color: white; vertical-align: top !important; height:93px !important; width: 100px !important; background-color: black">
                            <b>${row.DESCR_ARMARIO}</b>
                            <br><br>
                            BLOQUEADO
                   </c:when>
                   <c:otherwise>
                      <td class="column1" align="center" style="font-size: 18px !important; vertical-align: top !important; height:93px !important; width: 100px !important; background-color: tomato">
                        <a onclick="agendaDia(${row.CD_ARMARIO}, ${row.NU_SEQ_EMPRESTIMO})" href="#">
                            <b>${row.DESCR_ARMARIO}</b>
                            <br>
                        </a>
                            
                        <fmt:formatNumber var="titulo" value='${row.CD_MATRICULA}' pattern='0000'/>
                        <fmt:formatNumber var="categoria" value='${row.CD_CATEGORIA}' pattern='00'/>
                        <fmt:formatDate var="dtEmp" value='${row.DT_EMPRESTIMO}' pattern='dd/MM/yyyy HH:mm:ss'/>
                            
                        <div style="font-size:10px !important; color: black; padding-top: 10px" align="left">  
                          <b>Sócio:</b> ${categoria}/${titulo} - ${row.NOME_PESSOA}
                          <br>
                          <br>Dt. Emprestimo:</b> ${dtEmp}
                          
                            <c:if test='${row.QT_TOALHA gt 0}'>
                            <br>
                            <b>Kit Pendente(s):</b> ${row.QT_TOALHA}
                          </c:if>
                          
                        </div>    
                   </c:otherwise>
                </c:choose>
                      
                </td>
                
                <c:set var="qtLinha" value="${qtLinha+1}"/>
            </c:forEach>
            <c:forEach var="i" begin="${qtLinha}" end="7">
                <td class="column1" align="center" style="font-size: 18px !important; vertical-align: top !important; background-color: rgb(180,180,180) !important"></td>
            </c:forEach>
                
        </tbody>
    </table>
        
</body>
</html>
