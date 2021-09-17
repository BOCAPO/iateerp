<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Reserva de Lugares</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1580">
        <input type="hidden" name="acao" value="visualizar">

            <p class="legendaCodigo">Evento:</p>
            <div class="selectheightnovo" style="width:350px;">
                <select class="campoSemTamanho alturaPadrao"  name="idEvento">
                    <c:forEach var="evento" items="${eventos}">
                        <option value="${evento.id}">${evento.descricao}</option>
                    </c:forEach>
                </select>
            </div>
            <br>
            <fieldset class="field-set legendaFrame recuoPadrao" style="width:330px;">
                <legend >Situações:</legend>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="vagos" value="true" checked>Vagos
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="reservados" value="true" checked>Reservados
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="confirmados" value="true" checked>Confirmados
            </fieldset>
            <br>

        <input type="submit" class="botaoatualizar" value=" " />                
    </form>

</body>
</html>
