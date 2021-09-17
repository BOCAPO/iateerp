<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            $("#dataInicio").mask("99/99/9999");
            $("#dataFim").mask("99/99/9999");
            
            $('#tabela tr:gt(0)').css('background', 'white');

            $('#tabela tr:gt(0)').hover(function() {
                    $(this).css('background','#f4f9fe');
            }, function() {
                    $(this).css('background','white');
            })            
    });        
    function validarForm(){
        
        if(trim(document.forms[0].dataInicio.value) == ''
            || trim(document.forms[0].dataFim.value) == ''){
            alert('É preciso informar as datas iniciais e finais para a consulta');
            return;
        }
        if(!isDataValida(document.forms[0].dataInicio.value)){
            return;
        }
        if(!isDataValida(document.forms[0].dataFim.value)){
            return;
        }
        
        document.forms[0].submit();
    }

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Consulta Cancelamento de Reserva</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="1063">
        <input type="hidden" name="acao" value="consultar">
        
        <table class="fmt" align="left" >
            <tr>
              <td>
                <fieldset class="field-set legendaFrame recuoPadrao" style="height:65px;width:270px;">
                    <legend >Período do Cancelamento:</legend>
                    <input type="text" name="dataInicio" id="dataInicio" style="margin-top:6px;" class="campoSemTamanho alturaPadrao larguraData">    
                    &nbsp;&nbsp;&nbsp;a
                    <input type="text" name="dataFim" id="dataFim" style="margin-top:6px;"  class="campoSemTamanho alturaPadrao larguraData">        
                </fieldset>                                
              </td>
              <td >    
                  <input class="botaobuscainclusao" style="margin-top:15px;margin-left:30px;" type="button" onclick="validarForm()" value="" title="Consultar" />
              </td>
            </tr>
        </table>

    </form>

    
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
            <tr class="odd">
                <th scope="col" >Responsável</th>
                <th scope="col" >Local</th>
                <th scope="col" >Dt. Início</th>
                <th scope="col" >Dt. Fim</th>
                <th scope="col" >Dt. Lim. confirmação</th>
                <th scope="col" >Motivo</th>
                <th scope="col" >Funcionário</th>
                <th scope="col" >Data</th>
            </tr>	
        </thead>
        <tbody>

            <c:if test="${not empty dataInicio and not empty dataFim}">
                <sql:query var="rs" dataSource="jdbc/iate">
                SELECT
                    T1.NOME_INTERESSADO,
                    T2.DESCR_DEPENDENCIA,
                    T1.DT_INIC_UTILIZACAO,
                    T1.DT_FIM_UTILIZACAO,
                    T1.DE_CANC_RESERVA,
                    T1.DE_USER_CANC,
                    T1.DT_CONFIRMACAO,
                    T1.DT_LIMITE_CONF
               FROM
                    TB_RESERVA_DEPENDENCIA T1,
                    TB_DEPENDENCIA T2
               WHERE
                    T1.CD_STATUS_RESERVA = 'CA' AND
                    T1.SEQ_DEPENDENCIA > 23 AND
                    T1.SEQ_DEPENDENCIA = T2.SEQ_DEPENDENCIA AND
                    T1.DT_CONFIRMACAO >= ? AND
                    T1.DT_CONFIRMACAO <= ?
                    <sql:dateParam type="date" value="${dataInicio}"/>
                    <sql:dateParam type="date" value="${dataFim}"/>
                </sql:query>


                    <c:forEach var="row" items="${rs.rows}">
                        <tr height="1">
                            <td class="column1" align="left">${row.NOME_INTERESSADO}</td>
                            <td class="column1" align="left">${row.DESCR_DEPENDENCIA}</td>
                            <fmt:formatDate var="d1" pattern="dd/MM/yyyy HH:mm:ss" value="${row.DT_INIC_UTILIZACAO}"/>
                            <td class="column1" align="center">${d1}</td>
                            <fmt:formatDate var="d1" pattern="dd/MM/yyyy HH:mm:ss" value="${row.DT_FIM_UTILIZACAO}"/>
                            <td class="column1" align="center">${d2}</td>
                            <fmt:formatDate var="d1" pattern="dd/MM/yyyy HH:mm:ss" value="${row.DT_LIMITE_CONF}"/>
                            <td class="column1" align="center">${d3}</td>                    
                            <td class="column1" align="left">${row.DE_CANC_RESERVA}</td>
                            <td class="column1" align="center">${row.DE_USER_CANC}</td>
                            <fmt:formatDate var="d1" pattern="dd/MM/yyyy HH:mm:ss" value="${row.DT_CONFIRMACAO}"/>
                            <td class="column1" align="center">${d4}</td>
                        </tr>
                    </c:forEach>
            </c:if>
                
        </tbody>
    </table>

</body>
</html>
