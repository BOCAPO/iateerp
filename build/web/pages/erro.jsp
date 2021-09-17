<%@include file="head.jsp"%>

<style>
    table {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    tr {border:none;background:none;padding:0px;margin:0em auto;}
    td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <c:set var="modulo" value="${fn:substring(param.app, 0, 1)}" />
    
    <c:if test='${modulo=="7"}'>
        <%@include file="menuAcesso.jsp"%>
    </c:if>
    <c:if test='${modulo=="6"}'>
        <%@include file="menuCaixa.jsp"%>
    </c:if>
    <c:if test='${modulo!="6" && modulo!="7"}'>
        <%@include file="menu.jsp"%>
    </c:if>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Erro</div>
    <div class="divisoria"></div>
    <br>
    
    <c:if test='${empty msg}'>
        <h1 class="msgErro">${err.getMessage()} </h1>
    </c:if>
    <c:if test='${not empty msg}'>
        <h1 class="msgErro">${msg}</h1>
    </c:if>

    <br>
    
    <input type="button" onclick="javascript: history.go(-1)" class="botaoVoltar" value=" " />

</body>
</html>

