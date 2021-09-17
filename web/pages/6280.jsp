<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>
<%@include file="menuCaixa.jsp"%>

<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
    
    .zera{ 
    font-family: "Trebuchet MS",Verdana,Arial,Helvetica,sans-serif;
    color: #678197;
    font-size: 12px;
    font-weight: normal;
    }    
</style>  

<body class="internas">

    <div class="divisoria"></div>
    <div id="titulo-subnav">Saldo Comprometido</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="6280">
        <br>
        <table class="fmt">
            <tr>
                <td>
                    <table class="fmt">
                        <tr>
                            <td>
                            <fieldset class="field-set legendaFrame recuoPadrao" style="width:150px;height: 50px">
                                <legend >Categoria/Título:</legend>
                                <input type="text" name="idCategoria" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" maxlength="2" value="${idCategoria}">
                                &nbsp;&nbsp;&nbsp;&nbsp;/
                                <input type="text" name="matricula" onkeypress="onlyNumber(event)" class="campoSemTamanho alturaPadrao larguraNumeroPequeno" maxlength="4" value="${matricula}">
                            </fieldset>
                            </td>
                            <td>
                                <input class="botaobuscainclusao"  type="submit" style="margin-top:10px;margin-left:20px" value="" title="Consultar" />
                            </td>

                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        <table class="fmt">
            <tr>
                <td><p class="legendaCodigo MargemSuperior0">Cheques Pré-Datados:</p></td>
                <td align="right"><p class="legendaCodigo MargemSuperior0"><fmt:formatNumber pattern="#,##0.00" value="${saldoComprometido.chequePre}"/></p></td>
            </tr>
            <tr>
                <td><p class="legendaCodigo MargemSuperior0">Taxa Individual Futura:</p></td>
                <td align="right"><p class="legendaCodigo MargemSuperior0"><fmt:formatNumber pattern="#,##0.00" value="${saldoComprometido.taxaIndFutura}"/></p></td>
            </tr>
            <tr>
                <td><p class="legendaCodigo MargemSuperior0">Total:</p></td>
                <td align="right"><p class="legendaCodigo MargemSuperior0"><fmt:formatNumber pattern="#,##0.00" value="${saldoComprometido.chequePre + saldoComprometido.taxaIndFutura}"/></p></td>
            </tr>
            <tr>                
                <td><p class="legendaCodigo MargemSuperior0">Limite:</p></td>
                <td align="right"><p class="legendaCodigo MargemSuperior0"><fmt:formatNumber pattern="#,##0.00" value="${saldoComprometido.limiteCategoria}"/></p></td>
            </tr>
            <tr>                
                <td><p class="legendaCodigo MargemSuperior0">Saldo:</p></td>
                <td align="right"><p class="legendaCodigo MargemSuperior0"><fmt:formatNumber pattern="#,##0.00" value="${saldoComprometido.limiteCategoria - (saldoComprometido.chequePre + saldoComprometido.taxaIndFutura)}"/></p></td>
            </tr>
        </table>

    </form>

</body>
</html>
