<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@include file="head.jsp"%>

<style type="text/css">
    table.fmt {border:none;width:0;padding:0px;margin-left:0em;margin-top:0em;}
    table.fmt tr td {border:none;background:none;padding:0px;margin:0em auto;}
</style>  

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
            
            $("#dtReferencia").mask("99/99/9999");

    });
    
    function validarForm(){
        if(trim(document.forms[0].dtReferencia.value) == ''){
            alert('É preciso preencher a Data de Referência');
            return;
        }
        if(!isDataValida(document.forms[0].dtReferencia.value)){
            return;
        }

        if(!$("#socio").prop('checked') && !$("#funcionario").prop('checked')){
            alert("Informe pelo menos um tipo!");
            return;
        }

        document.forms[0].submit();
    }
</script>  


<body class="internas">

    <script type="text/javascript" language="JavaScript" src="js/format.js"></script>
    
    <c:if test='${origem=="L"}'>
        <%@include file="menu.jsp"%>
    </c:if>
    <c:if test='${origem!="L"}'>
        <%@include file="menuCaixa.jsp"%>
    </c:if>
    
        
    <div class="divisoria"></div>
    <div id="titulo-subnav">Relatório de Saldo Geral Pré-Pago</div>
    <div class="divisoria"></div>
    
    
    <form action="c" method="POST">
        <input type="hidden" name="app" value="6320">
        <input type="hidden" name="acao" id="acao" value="visualizar">
        <table class="fmt" align="left" >
            <tr>
                <td>
                    <p class="legendaCodigo">Dt. Referência</p>
                    <input type="text" id="dtReferencia" name="dtReferencia" style='width:92px' class="campoSemTamanho alturaPadrao">        
                </td>
                
                <td>
                    <fieldset class="field-set legendaFrame recuoPadrao" style="width:150px;height:42px;margin-left:18px;margin-top: 15px">
                        <legend >Situação:</legend>
                        &nbsp;&nbsp;&nbsp;<input type="checkbox" name="socio" id="socio" value="true"  style="margin-top:5px;" <c:if test='${normal || acao==null}'>checked</c:if>>Sócio
                        <input type="checkbox" name="funcionario" id="funcionario" value="true" style="margin-top:5px;" <c:if test='${cancelada  || acao==null}'>checked</c:if>>Funcionário<br>
                    </fieldset>
                </td>

            </tr>
        </table>
        <br><br><br><br>
                    
        <input type="button" onclick="validarForm()" class="botaoatualizar" value=" " />                                    
    </form>
    
</body>
</html>
