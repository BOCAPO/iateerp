<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })            
    });       
    function validarForm(){
        
        if(trim(document.forms[0].nome.value) == ''
            && trim(document.forms[0].mesa.value) == ''
            && document.forms[0].idEvento.value == 0){
            alert('Informe algum dado para a pesquisa')
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Consulta Reserva de Lugar</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1590">
        <input type="hidden" name="acao" value="consultar">

        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo">Evento:</p>
                    <div class="selectheightnovo" style="width:380px;">
                        <select class="campoSemTamanho alturaPadrao" style="width:360px;"  name="idEvento">
                            <option value="0">TODOS</option>
                            <c:forEach var="evento" items="${eventos}">
                                <option value="${evento.id}" <c:if test="${evento.id eq idEvento}">selected</c:if>>${evento.descricao}</option>
                            </c:forEach>
                        </select>
                    </div>
                </td>
                <td>
                    <p class="legendaCodigo">Nome:</p>
                    <input type="text" name="nome" class="campoSemTamanho alturaPadrao" style="width:250px;">
                </td>
                <td>
                    <p class="legendaCodigo">Mesa:</p>
                    <input type="text" name="mesa" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumeroPequeno">
                </td>
                <td>
                    <input class="botaobuscainclusao" style="margin-top:35px;margin-left:20px;" type="button" onclick="validarForm()" value="" title="Consultar" />
                </td>
            </tr>
        </table>
    </form>
    
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" >Evento</th>
            <th scope="col" >Lugar</th>
            <th scope="col" >Pessoa</th>
            <th scope="col" >Tipo</th>
            <th scope="col" >Situação</th>
            <th scope="col" >Valor</th>
        </tr>	
        </thead>
        <tbody>
            <c:if test="${not empty sql}">
                <sql:query var="rs" dataSource="jdbc/iate">
                    ${sql}
                </sql:query>


                    <c:forEach var="row" items="${rs.rows}">
                        <tr height="1">
                            <td class="column1" align="left">${row.DE_EVENTO}</td>
                            <fmt:formatNumber var="mesa" pattern="000" value="${row.NR_MESA}"/>
                            <td class="column1" align="center">${mesa} - ${row.LETRA_CADEIRA}</td>
                            <td class="column1" align="left">${row.NO_PESSOA}</td>
                            <c:choose>
                                <c:when test="${row.IC_TIPO_PESSOA eq 'S'}">
                                    <c:set var="tipoPessoa" value="Sócio"/>
                                </c:when>
                                <c:when test="${row.IC_TIPO_PESSOA eq 'N'}">
                                    <c:set var="tipoPessoa" value="Não Sócio"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="tipoPessoa" value="Convênio"/>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${row.IC_MEIA eq 'S'}">
                                <c:set var="tipoPessoa" value="${tipoPessoa} - meia"/>
                            </c:if>
                            <td class="column1" align="center">${tipoPessoa}</td>                    

                            <c:choose>
                                <c:when test="${row.CD_SIT_RESERVA eq 'CO'}">
                                    <c:set var="situacao" value="Confirmada"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="situacao" value="Reservada"/>
                                </c:otherwise>
                            </c:choose>                    
                            <td class="column1" align="center">${situacao}</td>

                            <fmt:formatNumber var="valor" pattern="0.00" value="${row.vr_reserva}"/>
                            <td class="column1" align="right">${valor}</td>
                        </tr>
                    </c:forEach>
            </c:if>

        </tbody>
    </table>
</body>
</html>
