<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>
<script type="text/javascript" language="JavaScript" src="js/format.js"></script>
<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">    
    $(document).ready(function () {
            
            $("#repasseInicio").mask("99/9999");
            $("#repasseFim").mask("99/9999");            
            
    });
    
    function validarForm(){
        if(trim(document.forms[0].repasseInicio.value) == ''
                || trim(document.forms[0].repasseFim.value) == ''){
            alert('É preciso preencher o período do repasse');
            return;
        }            
        if(!isMesAnoValido($('#repasseInicio').val())){
            return;
        }
        if(!isMesAnoValido($('#repasseFim').val())){
            return;
        }
        
        var k = 0;
        for(var i = 0; i < document.forms[0].concessionarios.length; i++){
            if(document.forms[0].concessionarios[i].checked){
                k++;
            }
        }
        if(k == 0){
            alert('Selecione pelo menos um Concessionário.');
            return;
        }
        
        //alert('Vai submit');
        document.forms[0].submit();
    }
    
    

</script>

<body class="internas">

    <%@include file="menu.jsp"%>

    <div class="divisoria"></div>
    <div id="titulo-subnav">Repasse aos Concessionários</div>
    <div class="divisoria"></div>
    
    <form method="POST" action="c">
	<input type="hidden" name="app" value="2580">
	<input type="hidden" name="acao" value="visualizar">
        
        <sql:query var="rsConcessionarios" dataSource="jdbc/iate">
            SELECT
                RIGHT('00' + CONVERT(VARCHAR, CD_CATEGORIA), 2) +
                RIGHT('0000' + CONVERT(VARCHAR, CD_MATRICULA), 4) +
                RIGHT('00' + CONVERT(VARCHAR, SEQ_DEPENDENTE), 2) ID,
                NOME_PESSOA
            FROM
                TB_PESSOA
            WHERE
                CD_CATEGORIA = 96
            ORDER BY
                NOME_PESSOA
        </sql:query>  
                     
        <table class="fmt">
            <tr>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:170px;height:50px;margin-top: 5px">
                        <legend >Tipo:</legend>        
                        <input type="radio" name="tipo" class="legendaCodigo" style="margin-top:6px" value="A" checked>Analítico
                        <input type="radio" name="tipo" class="legendaCodigo" style="margin-top:6px" value="S">Sintético
                    </fieldset>
                </td>
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:160px;height:50px;margin-top: 5px">
                        <legend >Período do Repasse</legend>
                        <input type="text" id="repasseInicio" name="repasseInicio" class="campoSemTamanho alturaPadrao" style="width: 45px;" >        
                        &nbsp;&nbsp;&nbsp;a
                        <input type="text" id="repasseFim"  name="repasseFim" class="campoSemTamanho alturaPadrao" style="width: 45px;" >        
                    </fieldset>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <p class="legendaCodigo">Concessionarios:<span><input class="botaoMarcaDesmarca" type="button" id="marcaTaxas"  onclick="marcaDesmarca('concessionarios')" style="margin-top: 0px;" value="" title="Consultar" /></span></p>
                    <div id="divtaxas" class="recuoPadrao" style="overflow:auto;height:300px;width:350px;">
                        <c:forEach var="cs" items="${rsConcessionarios.rows}">
                            <input type="checkbox" name="concessionarios" value="${cs.id}">${cs.nome_pessoa}<br>
                        </c:forEach>
                    </div>
                </td>
            </tr>
        </table>
        
        <br>
                    
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />        
        
    </form>

</body>
</html>
