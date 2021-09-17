<%@include file="head.jsp"%>

<body class="internas">
            
    <%@include file="menu.jsp"%>
    
    <div class="divisoria"></div>
        <div id="titulo-subnav">Histórico de Utilização do Título</div>
    <div class="divisoria"></div>
    
    <script type="text/javascript" LANGUAGE="JavaScript" SRC="js/calendario.js"></script>
    <link rel="stylesheet" type="text/css" href="css/calendario.css"/>

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <table border="1">
        <tr>
            <td align="center"><b>Histórico de Utilização do Título</b></td>
            <fmt:formatDate var="hora" value="<%=new java.util.Date()%>" pattern="HH:mm"/>
            <td align="right"><b>${hora}</b></td>
        </tr>
        <tr>
            <fmt:formatNumber value="${titular.socio.idCategoria}" pattern="00" var="idCategoria"/>
            <fmt:formatNumber value="${titular.socio.matricula}" pattern="0000" var="matricula"/>
            <fmt:formatDate var="dataNascimento" value="${titular.socio.dataNascimento}" pattern="dd/MM/yyyy"/>
            <td align="center">
                <b>${idCategoria}/${matricula} - ${titular.socio.nome} - ${dataNascimento}</b>
            </td>
            <fmt:formatDate var="data" value="<%=new java.util.Date()%>" pattern="dd/MM/yyyy"/>
            <td align="right"></b>${data}</td></td>
        </tr>
    </table>

    <hr>
        
    <table>
        <c:forEach items="${historicos}" var="historico">
            <tr>
                <td>${historico.nome}</td>
                <fmt:formatDate var="dataInicio" value="${historico.dataInicio}" pattern="dd/MM/yyyy"/>
                <fmt:formatDate var="dataFim" value="${historico.dataFim}" pattern="dd/MM/yyyy"/>
                <td>${dataInicio}</td>
                <td>${dataFim}</td>
            </tr>                
        </c:forEach>
    </table>
    
    <hr>
    Impresso por TechSoft - Clube Fácil
</body>
</html>
