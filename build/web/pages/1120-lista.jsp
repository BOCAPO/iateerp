<%@include file="head.jsp"%>

<body class="internas">

    <%@include file="menu.jsp"%>

    <script type="text/javascript" language="javascript">
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
        });        
    </script>  


    <div class="divisoria"></div>
    <div id="titulo-subnav">Exame Médico</div>
    <div class="divisoria"></div>
    
    
    <p class="legendaCodigo MargemSuperior0" >Sócio:</p>
    <input type="text" disabled class="campoSemTamanho alturaPadrao larguraNomePessoa" value="${socio.nome}" >

    <br><br><br>

    <c:if test='<%=request.isUserInRole("1121")%>'>
        <div class="botaoincluirgeral">
            <a href="c?app=1121&acao=showForm&matricula=${socio.matricula}&categoria=${socio.idCategoria}&dependente=${socio.seqDependente}"><img src="imagens/btn-incluir.png" width="100" height="25" /></a><br>
        </div>
    </c:if>

    <br>                
    <table id="tabela">
       <thead>
        <tr class="odd">
            <th></th>
            <th>Dt. Realização</th>
            <th>Dt. Validade</th>
            <th>Situação</th>
            <th>Conclusão</th>
            <th>Alterar</th>
            <th>Excluir</th>
        </tr>
       </thead>
        <c:forEach var="item" items="${exames}">
            <tr  height="1">
                <jsp:useBean id="now" class="java.util.Date" />
                <fmt:formatDate var="agora" value="${now}" pattern="yyyyMMdd"/>
                <fmt:formatDate var="dtValidade" value="${item.dtValidade}" pattern="yyyyMMdd"/>

                <c:choose>
                <c:when test='${item.resultado==null}'>
                    <c:set var="icone" value=""/>
                </c:when>
                <c:when test='${item.resultado=="R"}'>
                    <c:set var="icone" value="no.png"/>
                </c:when>
                <c:otherwise>
                    <c:choose>
                    <c:when test='${dtValidade<agora}'>
                        <c:set var="icone" value="socio-alerta5.png"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="icone" value="ok.png"/>
                    </c:otherwise>
                    </c:choose>
                </c:otherwise>
                </c:choose>                
                <td class="column1" align="center">
                    <img src="img/${icone}" />
                </td>
                
                
                <fmt:formatDate var="dtExame" value="${item.dtExame}" pattern="dd/MM/yyyy"/>                    
                <td class="column1" align="center">${dtExame}</td>
                <fmt:formatDate var="dtValidade" value="${item.dtValidade}" pattern="dd/MM/yyyy"/>                    
                <td class="column1" align="center">${dtValidade}</td>
                <td class="column1" align="center">
                    <c:if test='${item.resultado=="A"}'>Aprovado</c:if>
                    <c:if test='${item.resultado=="R"}'>Reprovado</c:if>
                </td>
                <td class="column1" align="center">${item.conclusao}</td>

                <c:if test='<%=request.isUserInRole("1122")%>'>
                    <td class="column1" align="center">
                        <a href="c?app=1122&acao=showForm&id=${item.id}"><img src="imagens/icones/inclusao-titular-03.png"/></a>
                    </td>
                </c:if>
                
                <c:if test='<%=request.isUserInRole("1123")%>'>
                    <td class="column1" align="center">    
                        <a href="javascript: if(confirm('Confirma Exclusão do Exame Médico selecionado?')) window.location.href='c?app=1123&id=${item.id}'"><img src="imagens/icones/inclusao-titular-05.png" /></a>
                    </td>
                </c:if>

            </tr>
        </c:forEach>
    </table>

    <input type="button" class="botaoVoltar"  onclick="window.location='c?app=1120';" value=" " />
    
</body>
</html>
