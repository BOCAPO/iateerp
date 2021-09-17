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
        $(document).ready(function () {
                $('#tabela tr:gt(0)').css('background', 'white');
                $('#tabela2 tr:gt(0)').css('background', 'white');

                $('#tabela tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                $('#tabela2 tr:gt(0)').hover(function() {
                        $(this).css('background','#f4f9fe');
                }, function() {
                        $(this).css('background','white');
                })
                
        });        
    </script>
    
    <div class="divisoria"></div>
    <div id="titulo-subnav">Consulta Carnê p/ Sequencial</div>
    <div class="divisoria"></div>

    <sql:query var="rs" dataSource="jdbc/iate">
        <c:choose>
            <c:when test="${seqCarne lt 100000}">
                select * FROM TB_CARNE WHERE seq_carne_ATU = ${seqCarne}
            </c:when>
            <c:otherwise>
                select * FROM TB_CARNE WHERE seq_carne = ${seqCarne}
            </c:otherwise>
        </c:choose>
    </sql:query>    
    
    <form action="c">
        <input type="hidden" name="app" value="1365">
        <table class="fmt">
            <tr>
                <td>
                    <p class="legendaCodigo MargemSuperior0">Seq. Carnê:</p>
                    <input type="text" name="seqCarne" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumero" value="${seqCarne}">
                </td>
                <td><input class="botaobuscainclusao" style="margin-top: 20px" type="submit" value="" title="Consultar"></td>
                <c:forEach var="titulo" items="${rs.rows}">
                    <td><p class="legendaCodigo MargemSuperior0" style="margin-top: 20px">Título: ${titulo.CD_CATEGORIA}/${titulo.CD_MATRICULA}</p></td>
                </c:forEach>
                
            </tr>
        </table>
    </form>
        
    <br>               

        
    <table id="tabela" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" class="nome-lista">Vencimento</th>
            <th scope="col" class="nome-lista">Valor Devido</th>
            <th scope="col" class="nome-lista">(+) Encargos</th>
            <th scope="col" class="nome-lista">(-) Descontos</th>
            <th scope="col" class="nome-lista">(=) Valor Pago</th>
            <th scope="col" class="nome-lista">Data Pagamento</th>
            <th scope="col" class="nome-lista">Local Pg.</th>
            <th scope="col" class="nome-lista">Dt. Baixa</th>
        </tr>	
        </thead>
        <tbody>
        <c:forEach var="row" items="${rs.rows}">
            <tr height="1">
                <td class="column1" align="center"><a href="c?app=1365&seqCarne=${seqCarne}&seqCarneDetalhe=${row.SEQ_CARNE}"><fmt:formatDate value="${row.DT_VENC_CARNE}" pattern="dd/MM/yyyy" /></a></td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_CARNE}" pattern="#,##0.00"/></td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_ENCAR_CARNE}" pattern="#,##0.00"/></td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_DESC_CARNE}" pattern="#,##0.00"/></td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_PGTO_CARNE}" pattern="#,##0.00"/></td>
                <td class="column1" align="center"><fmt:formatDate value="${row.DT_PGTO_CARNE}" pattern="dd/MM/yyyy" /></td>
                <td class="column1" align="left">${row.local_pgto_carne}</td>
                <td class="column1" align="center"><fmt:formatDate value="${row.dt_baixa_carne}" pattern="dd/MM/yyyy" /></td>
            </tr>	
        </c:forEach>
        </tbody>
    </table>
                
    <br><br>
    
    <table id="tabela2" style="width:98%;margin-left:15px;">
        <thead>
        <tr class="odd">
            <th scope="col" class="nome-lista">Taxa</th>
            <th scope="col" class="nome-lista">Valor Devido</th>
            <th scope="col" class="nome-lista">(+) Encargos</th>
            <th scope="col" class="nome-lista">(-) Descontos</th>
            <th scope="col" class="nome-lista">(=) Valor Pago</th>
            <th scope="col" class="nome-lista">Situação</th>
        </tr>	
        </thead>
        
        <c:if test="${seqCarneDetalhe gt 0}">
        <tbody>
        <sql:query var="rs" dataSource="jdbc/iate">
            select t1.val_parc_cur, 
                    t1.val_encar_parc_cur, 
                    t1.val_desconto_parc_cur, 
                    t1.val_pgto_parc_cur, 
                    t1.cd_sit_parc_cur, 
                    t2.descr_curso 
             FROM TB_PARCELA_CURSO T1, TB_CURSO T2, TB_TURMA T3 WHERE 
                    T1.seq_carne = ${seqCarneDetalhe}
                     AND T1.SEQ_TURMA = T3.SEQ_TURMA 
                     AND T3.CD_CURSO = T2.CD_CURSO
        </sql:query>    

        <c:forEach var="row" items="${rs.rows}">
            <tr height="1">
                <td class="column1" align="center">${row.descr_curso}</td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_PARC_CUR}" pattern="#,##0.00"/></td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_ENCAR_PARC_CUR}" pattern="#,##0.00"/></td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_DESCONTO_PARC_CUR}" pattern="#,##0.00"/></td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_PGTO_PARC_CUR}" pattern="#,##0.00"/></td>
                <td class="column1" align="center">${row.cd_sit_parc_cur}</td>
            </tr>	
        </c:forEach>
            
        <sql:query var="rs" dataSource="jdbc/iate">
            select t1.val_parc_ADM, 
                   t1.val_encarGO_ADM, 
                   t1.val_desconto_ADM, 
                   t1.val_pgto_parc_ADM, 
                   t1.cd_sit_parc_ADM, 
                   t2.descr_TX_ADMINISTRATIVA 
            FROM TB_PARCELA_ADMINISTRATIVA T1, TB_TAXA_ADMINISTRATIVA T2 WHERE 
                   T1.seq_carne = ${seqCarneDetalhe}
                    AND T1.CD_TX_ADMINISTRATIVA = T2.CD_TX_ADMINISTRATIVA 
        </sql:query>    

        <c:forEach var="row" items="${rs.rows}">
            <tr height="1">
                <td class="column1" align="center">${row.DESCR_TX_ADMINISTRATIVA}</td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_PARC_adm}" pattern="#,##0.00"/></td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_ENCARgo_adm}" pattern="#,##0.00"/></td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_DESCONTO_adm}" pattern="#,##0.00"/></td>
                <td class="column1" align="right"><fmt:formatNumber value="${row.VAL_PGTO_PARC_ADM}" pattern="#,##0.00"/></td>
                <td class="column1" align="center">${row.cd_sit_parc_adm}</td>
            </tr>	
        </c:forEach>            
        </tbody>
        </c:if>        
        
    </table>    
    
</body>
</html>
