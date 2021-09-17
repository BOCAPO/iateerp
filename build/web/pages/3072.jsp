<%@page import="techsoft.curso.TurmaHorario"%>
<%@page import="techsoft.curso.Turma"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>
    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Imprimir Comprovante</div>
    <div class="divisoria"></div>

    <form action="c">
        <input type="hidden" name="app" value="3041">
        <input type="hidden" name="idTurma" value="${turma.id}">
        <input type="hidden" name="matricula" value="${socio.matricula}">
        <input type="hidden" name="idCategoria" value="${socio.idCategoria}">
        <input type="hidden" name="seqDependente" value="${socio.seqDependente}">
        <input type="hidden" name="dataMatricula" value="${dataMatricula}">
        <input type="hidden" name="acao" value="imprimir">


        <p class="legendaCodigo" >Selecione o tipo de comprovante</p>        
        <input type="radio" name="tipoComprovante" class="legendaCodigo" value="M" checked>Matrícula
        <input type="radio" name="tipoComprovante" class="legendaCodigo" value="C">Cancelamento<br>
        <br>
        <input type="submit" id="atualizar" class="botaoatualizar" value=" " />
        <input type="button" class="botaoVoltar"  onclick="window.location='c?app=3070&idTurma=${turma.id}';" value=" " />
        
    </form>

        
</body>
</html>
